package com.interrogation.mapper;

import com.interrogation.pojo.entity.ChatRecords;
import java.util.List;

public interface ChatRecordsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChatRecords record);

    ChatRecords selectByPrimaryKey(Integer id);

    List<ChatRecords> selectAll();

    int updateByPrimaryKey(ChatRecords record);
}