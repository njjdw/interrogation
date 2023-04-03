package com.interrogation.mapper;

import com.interrogation.pojo.entity.UserLoginRecords;
import java.util.List;

public interface UserLoginRecordsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLoginRecords record);

    UserLoginRecords selectByPrimaryKey(Long id);

    List<UserLoginRecords> selectAll();

    int updateByPrimaryKey(UserLoginRecords record);
}