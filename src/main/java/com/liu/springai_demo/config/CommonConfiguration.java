package com.liu.springai_demo.config;

import com.liu.springai_demo.constant.SystemConstants;
import com.liu.springai_demo.memory.InSqlChatMemory;
import com.liu.springai_demo.tools.ElectiveCourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author 1
 */
@Configuration
public class CommonConfiguration {
    /**
     * AI Chat
     * @param model
     * @param chatMemory
     * @return
     */
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
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.
                builder(model).
                defaultSystem(SystemConstants.GAME_SYSTEM_PROMPT).
                defaultAdvisors(new SimpleLoggerAdvisor()).
                defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).
                build();
    }

    /**
     * 客服用ChatClient对象，用于模拟选修课程推荐客服
     * @param model 使用OpenAI的模型
     * @param chatMemory 通过内存进行会话历史存储
     * @return
     */
    @Bean
    public ChatClient serviceChatClient(OpenAiChatModel model, ChatMemory chatMemory, ElectiveCourseTools electiveCourseTools) {
        return ChatClient
                .builder(model)// 选择模型
                .defaultSystem(SystemConstants.SERVICE_SYSTEM_PROMPT)// 系统设置
                .defaultAdvisors(new SimpleLoggerAdvisor())// 添加日志记录
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())// 添加会话记忆功能
                .defaultTools(electiveCourseTools)// 添加工具
                .build();
    }

    @Bean
    @Primary
    public ChatMemory chatMemory() {
        return new InSqlChatMemory(); // 使用项目中存在的 ChatMemory 实现
    }
}
