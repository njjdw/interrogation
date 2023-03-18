package com.interrogation.mapper;

import com.interrogation.pojo.dto.UserAccountDTO;
import com.interrogation.pojo.dto.UserInfoDTO;
import com.interrogation.pojo.entity.UserInfo;
import java.util.List;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer id);

    void insertUser(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    List<UserInfo> selectAll();

    int updateByPrimaryKey(UserInfo record);

    int countNumber(String number);
    UserInfo selectUserByUserPhone(String userPhone);
}