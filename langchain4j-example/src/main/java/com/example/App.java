package com.example;

import java.util.Arrays;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;

public class App {

    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");

        OpenAiChatModel llm = OpenAiChatModel.builder().apiKey(apiKey)
                .modelName(OpenAiChatModelName.GPT_4_O_MINI).build();
        String response = llm.chat("こんにちは！");
        System.out.println(response);

        OpenAiEmbeddingModel embedding = OpenAiEmbeddingModel.builder().apiKey(apiKey)
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_3_SMALL).dimensions(64).build();
        float[] vector = embedding.embed("こんにちは！").content().vector();
        System.out.println(Arrays.toString(vector));
    }
}
