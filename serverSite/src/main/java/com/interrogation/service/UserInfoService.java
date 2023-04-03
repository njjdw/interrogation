package com.interrogation.service;

import com.interrogation.pojo.dto.UserInfoDTO;

public interface UserInfoService {

    public boolean isExistNumber(String number);
    public void registUser(String userPhone,String password);
    public UserInfoDTO loginUser(String userPhone, String password);
}
