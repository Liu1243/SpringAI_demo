package com.liu.springai_demo.controller;

import com.liu.springai_demo.constant.ChatType;
import com.liu.springai_demo.entity.vo.MessageVO;
import com.liu.springai_demo.memory.InSqlChatMemory;
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
import java.util.Arrays;
import java.util.List;

/**
 * @author 1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    private final ChatMemory chatMemory;// 使用内存存储会话内容

    private final InSqlChatMemory inSqlChatMemorychatMemory;// 使用数据库存储会话内容

    @Autowired
    @Qualifier( "inMemoryChatHistoryRepository") // 使用内存存储会话
    private ChatHistoryRepository inMemoryChatHistoryRepository;

    @Autowired
    @Qualifier ("inDatabaseChatHistoryRepository") // 使用数据库存储会话id
    private ChatHistoryRepository inSqlChatHistoryRepository;

    @GetMapping("/{type}")
    public List<String> getChatids(@PathVariable("type") String type) {
        // 如果是聊天类型或者客服类型，则从数据库中获取会话id列表
        if (isDatabaseType(type))
        {
            return inSqlChatHistoryRepository.getChatIds(type);
        }
        // 如果是其他类型，则从内存中获取会话id列表
        else {
            return inMemoryChatHistoryRepository.getChatIds(type);
        }
    }

    @RequestMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(@PathVariable("type") String type, @PathVariable("chatId") String chatId) {
        List<Message> messages = chatMemory.get(chatId);

        // 如果是聊天类型或者客服类型，则从数据库中获取会话历史记录
        if (isDatabaseType(type))
        {
            messages = inSqlChatMemorychatMemory.get(chatId);
            if (messages == null)
            {
                return List.of();
            }
            return messages.stream().map(MessageVO::new).toList();
        }
        // 如果是其他类型，则从内存中获取会话历史记录
        else {
            messages = chatMemory.get(chatId);

        }
        if (messages == null)
        {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }

    /**
     * 判断是否是需要使用数据库的类型
     */
    private boolean isDatabaseType(String type) {
        // return ChatType.CHAT.getValue().equalsIgnoreCase(type);
        // 可以扩展更多类型，例如：
        return Arrays.asList(ChatType.CHAT.getValue(), ChatType.SERVICE.getValue()).contains(type.toLowerCase());
    }


}
