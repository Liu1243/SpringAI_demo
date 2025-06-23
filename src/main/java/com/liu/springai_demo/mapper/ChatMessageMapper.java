package com.liu.springai_demo.mapper;

import com.liu.springai_demo.entity.po.ChatMessage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatMessageMapper {

    @Insert("INSERT INTO chat_message (conversation_id, role, content) VALUES (#{conversationId}, #{role}, #{content})")
    void save(ChatMessage message);

    @Select("SELECT * FROM chat_message WHERE conversation_id = #{conversationId} ORDER BY id ASC")
    List<ChatMessage> findByConversationId(String conversationId);

    @Delete("DELETE FROM chat_message WHERE conversation_id = #{conversationId}")
    void deleteByConversationId(String conversationId);

}
