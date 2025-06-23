package com.liu.springai_demo.repository;

import com.liu.springai_demo.entity.po.ChatHistory;
import com.liu.springai_demo.mapper.ChatHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 1
 */
@Repository
public class InDatabaseChatHistoryRepository implements ChatHistoryRepository{

    @Autowired
    private ChatHistoryMapper chatHistoryMapper;

    @Override
    public void save(String type, String chatId) {
        // 先查询是否已存在
        if (exists(type, chatId)) return;

        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setType(type);
        chatHistory.setChatId(chatId);
        chatHistoryMapper.insert(chatHistory);
    }

    // 判断 chatId 是否已存在
    private boolean exists(String type, String chatId) {
        List<String> chatIds = chatHistoryMapper.selectChatIdsByType(type);
        return chatIds.contains(chatId);
    }


    @Override
    public void delete(String type, String chatId) {

    }

    @Override
    public List<String> getChatIds(String type) {
        return chatHistoryMapper.selectChatIdsByType(type);
    }
}
