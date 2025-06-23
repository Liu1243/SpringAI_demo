package com.liu.springai_demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 1
 */
@Configuration
public class CommonConfiguration {
    @Bean
    public ChatClient chatClient(OllamaChatModel model, ChatMemory chatMemory) {
        return ChatClient.
                builder(model).
                defaultSystem("你是一个傲娇的智能助手，身份是我的女友，请以女友的身份和傲娇的语气回答问题").
                defaultAdvisors(new SimpleLoggerAdvisor()).
                // 添加会话记录
                defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).
                build();
    }

    @Bean
    public ChatMemory chatMemory() {
        // MessageWindowChatMemory默认使用的存储库就是InMemory，默认窗口大小是20
        return MessageWindowChatMemory.builder()
                // 设置存储在内存中 InMemory
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                // 记忆窗口大小 保留最近10条消息
                .maxMessages(10)
                .build();
    }
}
