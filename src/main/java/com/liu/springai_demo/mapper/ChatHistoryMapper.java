package com.liu.springai_demo.mapper;

import com.liu.springai_demo.entity.ChatHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author 1
 */
@Mapper
public interface ChatHistoryMapper {

    /**
     * 插入一条聊天记录
     * @param chatHistory
     */
    @Insert("INSERT INTO chat_history (type, chat_id) VALUES (#{type}, #{chatId})")
    void insert(ChatHistory chatHistory);

    /**
     * 删除一条聊天记录
     * @param type
     * @param chatId
     */
    @Delete("DELETE FROM chat_history WHERE type = #{type} AND chat_id = #{chatId}")
    void delete(@Param("type") String type, @Param("chatId") String chatId);

    /**
     * 根据type获取聊天记录的chatIds
     * @param type
     * @return
     */
    @Select("SELECT chat_id FROM chat_history WHERE type = #{type}")
    List<String> selectChatIdsByType(String type);


}
