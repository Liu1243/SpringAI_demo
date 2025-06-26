package com.liu.springai_demo.config;

import com.liu.springai_demo.constant.SystemConstants;
import com.liu.springai_demo.memory.InSqlChatMemory;
import com.liu.springai_demo.tools.ElectiveCourseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.JedisPooled;

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

    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled("localhost", 63791);
    }

    /**
     * 项目中存在 ollama 以及 OpenAi 两种方式，这里 VectorStore 自动装配会报错 找不到 EmbeddingModel 所以这里手动指定 EmbeddingModel
     * @param jedisPooled
     * @param embeddingModel
     * @return
     */
    @Bean
    public VectorStore vectorStore(JedisPooled jedisPooled, @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return RedisVectorStore.builder(jedisPooled, embeddingModel)
                .indexName("spring_ai_index")                // Optional: defaults to "spring-ai-index"
                .prefix("doc:")                  // Optional: defaults to "embedding:"
                .initializeSchema(true)                   // Optional: defaults to false
                .build();
    }

    @Bean
    public ChatClient pdfChatClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        return ChatClient
                .builder(model)
                .defaultSystem("请根据上下文回答问题，遇到上下文没有的问题，不要随意编造。")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder().similarityThreshold(0.6d).topK(2).build())
                                .build()
                )
                .build();
    }


}
