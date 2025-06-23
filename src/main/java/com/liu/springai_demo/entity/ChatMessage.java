package com.liu.springai_demo.entity;

import lombok.Data;

/**
 * @author 1
 */
@Data
public class ChatMessage {
    private Long id;
    private String conversationId;
    private String role;
    private String content;
}
