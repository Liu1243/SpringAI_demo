package com.liu.springai_demo.repository;

import org.springframework.core.io.Resource;

/**
 * @Title: 1
 * @Author itmei
 * @Package com.liu.springai_demo.repository
 * @Date 2025/6/26 20:22
 * @description:
 */
public interface FileRepository {

    boolean save(String chatId, Resource resource);

    Resource getFile(String chatId);
}
