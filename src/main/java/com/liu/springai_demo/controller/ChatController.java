package com.liu.springai_demo.controller;

import com.liu.springai_demo.constant.ChatType;
import com.liu.springai_demo.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author 1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    @Autowired
    @Qualifier("inDatabaseChatHistoryRepository")
    private ChatHistoryRepository chatHistoryRepository;

    @RequestMapping(value = "/chat_sync", produces = "text/html;charset=utf-8")
    public String chat(String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    @RequestMapping(value = "/chat", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(String prompt, String chatId) {
        // 保存会话id
        chatHistoryRepository.save(ChatType.CHAT.getValue(), chatId);

        return chatClient.prompt()
                .user(prompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
