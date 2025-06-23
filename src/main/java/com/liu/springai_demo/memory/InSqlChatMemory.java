package com.liu.springai_demo.memory;

import com.liu.springai_demo.entity.ChatMessage;
import com.liu.springai_demo.mapper.ChatMessageMapper;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author 1
 */
@Component
public class InSqlChatMemory implements ChatMemory {

    @Autowired
    private ChatMessageMapper chatMessageMapper;

    @Override
    public void add(String conversationId, List<Message> messages) {
        Assert.hasText(conversationId, "conversationId must not be null");
        Assert.notNull(messages, "messages must not be null");
        Assert.noNullElements(messages, "messages must not contain null elements");

        for (Message message : messages) {
            String role = "";
            switch (message.getMessageType()) {
                case USER:
                    role = "user";
                    break;
                case ASSISTANT:
                    role = "assistant";
                    break;
                case SYSTEM:
                    role = "system";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid message type: " + message.getMessageType());
            }

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setConversationId(conversationId);
            chatMessage.setRole(role);
            chatMessage.setContent(message.getText());

            // 插入到数据库
            chatMessageMapper.save(chatMessage);
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be empty");

        List<ChatMessage> chatMessages = chatMessageMapper.findByConversationId(conversationId);

        return chatMessages.stream().map(chatMessage -> {
            Message message;
            switch (chatMessage.getRole()) {
                case "user":
                    message = new UserMessage(chatMessage.getContent());
                    break;
                case "assistant":
                    message = new AssistantMessage(chatMessage.getContent());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role: " + chatMessage.getRole());
            }
            return message;
        }).toList();
    }

    @Override
    public void clear(String conversationId) {
        chatMessageMapper.deleteByConversationId(conversationId);
    }
}
