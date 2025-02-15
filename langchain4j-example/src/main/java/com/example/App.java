package com.example;

import java.util.Arrays;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class App {

    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");

        OpenAiChatModel llm = OpenAiChatModel.builder().apiKey(apiKey)
                .modelName(OpenAiChatModelName.GPT_4_O_MINI).build();
        String response = llm.chat("こんにちは！");
        System.out.println(response);

        InMemoryEmbeddingStore<TextSegment> vectorStore = new InMemoryEmbeddingStore<>();

        OpenAiEmbeddingModel embeddings = OpenAiEmbeddingModel.builder().apiKey(apiKey)
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL).dimensions(256).build();
        TextSegment textSegment1 = TextSegment.from("うらがみの好きなゲームは『エンダーマグノリア』");
        Embedding embedding1 = embeddings.embed(textSegment1).content();
        vectorStore.add(embedding1, textSegment1);
        System.out.println(Arrays.toString(embedding1.vector()));

        TextSegment textSegment2 = TextSegment.from("今日の天気はめちゃ晴れで良き");
        Embedding embedding2 = embeddings.embed(textSegment2).content();
        vectorStore.add(embedding2, textSegment2);

        EmbeddingSearchRequest searchRequest1 = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddings.embed("好きなゲームは？").content()).maxResults(1).build();
        EmbeddingSearchResult<TextSegment> searchResult1 = vectorStore.search(searchRequest1);
        searchResult1.matches().forEach(a -> System.out.println(a.embedded().text()));

        EmbeddingSearchRequest searchRequest2 = EmbeddingSearchRequest.builder()
                .queryEmbedding(embeddings.embed("どんな天気？").content()).maxResults(1).build();
        EmbeddingSearchResult<TextSegment> searchResult2 = vectorStore.search(searchRequest2);
        searchResult2.matches().forEach(a -> System.out.println(a.embedded().text()));
    }
}
