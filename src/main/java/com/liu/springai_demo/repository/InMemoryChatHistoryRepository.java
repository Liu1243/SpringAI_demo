package com.liu.springai_demo.repository;

import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author 1
 * @Description 将chatId 保存在内存中
 */
@Repository
public class InMemoryChatHistoryRepository implements ChatHistoryRepository{

    private final Map<String, List<String>> chatHistory = new HashMap<>();

    /**
     * 保存
     * @param type
     * @param chatId
     */
    @Override
    public void save(String type, String chatId) {
        List<String> chatIds = chatHistory.computeIfAbsent(type, k -> new ArrayList<>());

        if (chatIds.contains(chatId)) {
            return;
        }
        chatIds.add(chatId);
    }

    /**
     * 删除
     * @param type
     * @param chatId
     */
    @Override
    public void delete(String type, String chatId) {

    }

    /**
     * 根据 type查询 chatIds
     * @param type
     * @return
     */
    @Override
    public List<String> getChatIds(String type) {
        return chatHistory.getOrDefault(type, Collections.emptyList());
    }
}
