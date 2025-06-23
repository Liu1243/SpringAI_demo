package com.liu.springai_demo.repository;

import java.util.List;

/**
 * @author 1
 */
public interface ChatHistoryRepository {

    void save(String type, String chatId);

    void delete(String type, String chatId);

    List<String> getChatIds(String type);
}
