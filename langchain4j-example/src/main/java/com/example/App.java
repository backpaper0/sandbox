package com.example;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.function.Function;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.QueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class App {

    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");

        // テキスト生成
        OpenAiChatModel chatModel = OpenAiChatModel.builder().apiKey(apiKey)
                .modelName(OpenAiChatModelName.GPT_4_O_MINI).build();
        String chatQuery = "こんにちは！";
        String chatAnswer = chatModel.chat(chatQuery);
        System.out.printf("チャット:%n  Human: %s%n     AI: %s%n%n", chatQuery, chatAnswer);

        // 埋め込み・ベクトル検索
        InMemoryEmbeddingStore<TextSegment> vectorStore = new InMemoryEmbeddingStore<>();

        OpenAiEmbeddingModel embeddings = OpenAiEmbeddingModel.builder().apiKey(apiKey)
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL).dimensions(256).build();

                Function<String, Embedding> addEmbedding = query -> {
            TextSegment textSegment = TextSegment.from(query);
            Embedding embedding = embeddings.embed(textSegment).content();
            vectorStore.add(embedding, textSegment);
            return embedding;
        };

        Embedding embedding1 = addEmbedding.apply("うらがみの好きなゲームは『エンダーマグノリア』");
        System.out.println(Arrays.toString(embedding1.vector()));

        addEmbedding.apply("今日の天気はめちゃ晴れで良き");
        addEmbedding.apply("うらがみの好きなアニメは『リコリス・リコイル』");
        addEmbedding.apply("うらがみの好きなお菓子はチョコレート");

        String vectorSearchQuery1 = "好きなゲームは？";
        EmbeddingSearchRequest searchRequest1 = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddings.embed(vectorSearchQuery1).content()).maxResults(1).build();
        EmbeddingSearchResult<TextSegment> searchResult1 = vectorStore.search(searchRequest1);
        searchResult1.matches().forEach(
                a -> System.out.printf("ベクトル検索1:%n  Q: %s%n  A: %s%n%n", vectorSearchQuery1, a.embedded().text()));

        String vectorSearchQuery2 = "どんな天気？";
        EmbeddingSearchRequest searchRequest2 = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddings.embed(vectorSearchQuery2).content()).maxResults(1).build();
        EmbeddingSearchResult<TextSegment> searchResult2 = vectorStore.search(searchRequest2);
        searchResult2.matches().forEach(
                a -> System.out.printf("ベクトル検索2:%n  Q: %s%n  A: %s%n%n", vectorSearchQuery2, a.embedded().text()));

        // RAG
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddings)
                .embeddingStore(vectorStore)
                .maxResults(3)
                .build();
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                // .queryTransformer(null)
                // .queryRouter(null)
                // .contentAggregator(null)
                // .contentInjector(null)
                // .executor(null)
                .contentRetriever(contentRetriever)
                .build();
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .retrievalAugmentor(retrievalAugmentor)
                .build();
        String ragQuery = "うらがみさんの好きなものを教えて。";
        String ragAnswer = assistant.chat(ragQuery);
        System.out.printf("RAG:%n  Q: %s%n  A: %s%n%n", ragQuery, ragAnswer);
    }

    interface Assistant {

        String chat(String userMessage);
    }
}
