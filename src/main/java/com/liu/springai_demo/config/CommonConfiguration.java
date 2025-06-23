package com.liu.springai_demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 1
 */
@Configuration
public class CommonConfiguration {
    @Bean
    public ChatClient chatClient(OllamaChatModel model) {
        return ChatClient.
                builder(model).
                defaultSystem("你是一个傲娇的智能助手，身份是我的女友，请以女友的身份和傲娇的语气回答问题").
                build();
    }
}
