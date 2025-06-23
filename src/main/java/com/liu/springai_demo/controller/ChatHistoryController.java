package com.liu.springai_demo.controller;

import com.liu.springai_demo.entity.vo.MessageVO;
import com.liu.springai_demo.repository.ChatHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    @Autowired
    @Qualifier("inDatabaseChatHistoryRepository")
    private ChatHistoryRepository chatHistoryRepository;

    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getChatids(@PathVariable("type") String type) {
        return chatHistoryRepository.getChatIds(type);
    }

    @RequestMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);

        if (messages == null) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }

}
