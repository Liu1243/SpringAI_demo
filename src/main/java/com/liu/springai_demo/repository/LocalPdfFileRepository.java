package com.liu.springai_demo.repository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

/**
 * @Title: 1
 * @Author itmei
 * @Package com.liu.springai_demo.repository
 * @Date 2025/6/26 20:24
 * @description:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalPdfFileRepository implements FileRepository{

    private final VectorStore vevectorStore;

    private final Properties chatFiles = new Properties();

    @Override
    public boolean save(String chatId, Resource resource) {
        String filename = resource.getFilename();
        File target = new File(Objects.requireNonNull(filename));
        if (!target.exists()) {
            try {
                Files.copy(resource.getInputStream(), target.toPath());
            } catch (IOException e) {
                log.error("保存文件失败", e);
                return false;
            }
        }

        chatFiles.put(chatId, filename);

        return true;
    }

    @Override
    public Resource getFile(String chatId) {
        return new FileSystemResource(chatFiles.getProperty(chatId));
    }

    @PostConstruct
    private void init() {
        FileSystemResource pdfResource = new FileSystemResource("chat-pdf.properties");
        if (pdfResource.exists()) {
            try {
                chatFiles.load(pdfResource.getInputStream());
            } catch (IOException e) {
                log.error("加载文件失败", e);
            }
        }
    }

    @PreDestroy
    private void persistent() {
        FileSystemResource pdfResource = new FileSystemResource("chat-pdf.properties");
        try {
            chatFiles.store(pdfResource.getOutputStream(), LocalDateTime.now().toString());
        } catch (IOException e) {
            log.error("保存文件失败", e);
        }
    }
}
