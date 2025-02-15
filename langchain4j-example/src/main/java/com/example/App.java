package com.example;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;

public class App {

    public static void main(String[] args) {
        OpenAiChatModel llm = OpenAiChatModel.builder().apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(OpenAiChatModelName.GPT_4_O_MINI).build();
        String response = llm.chat("こんにちは！");
        System.out.println(response);
    }
}
