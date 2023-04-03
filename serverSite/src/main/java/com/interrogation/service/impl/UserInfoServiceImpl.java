package com.interrogation.service.impl;

import com.interrogation.exception.Assert;
import com.interrogation.mapper.UserInfoMapper;
import com.interrogation.pojo.dto.DoctorInfoDTO;
import com.interrogation.pojo.dto.UserInfoDTO;
import com.interrogation.pojo.entity.UserInfo;
import com.interrogation.result.ResponseEnum;
import com.interrogation.service.UserInfoService;
import com.interrogation.utils.JwtUtils;
import com.interrogation.utils.MD5;
import com.interrogation.utils.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     *
     * 1、生成盐值
     * 2、密码加密
     * 3、写入数据
     *
     * @param userPhone 电话号
     * @param password  密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registUser(String userPhone, String password) {

        String salt = RandomUtils.getDigitBits(5);

        String encryptPassword = encrypt(salt,password);

        UserInfo userInfo = new UserInfo();

        userInfo.setUserPhone(userPhone);
        userInfo.setPassword(encryptPassword);
        userInfo.setSalt(salt);
        userInfoMapper.insertUser(userInfo);
    }

    @Override
    public UserInfoDTO loginUser(String userPhone, String passowrd) {
        UserInfo userInfo = userInfoMapper.selectUserByUserPhone(userPhone);
        System.out.println(userInfo);
        Assert.notNullOrEmpty(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);
        String encryptPassword = encrypt(userInfo.getSalt(), passowrd);
        Assert.isTrue(encryptPassword.equals(userInfo.getPassword()),ResponseEnum.LOGIN_PASSWORD_ERROR);

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        /*生成token*/
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getUserPhone());
        userInfoDTO.setToken(token);
        userInfoDTO.setId(userInfo.getId());
        userInfoDTO.setNickName(userInfo.getNickName());
        userInfoDTO.setHeadImg(userInfo.getHeadImg());
        userInfoDTO.setUserPhone(userInfo.getUserPhone());
        userInfoDTO.setRole(userInfo.getRole());
        return userInfoDTO;
    }

    /**
     * MD5迭代加密
     * @param salt
     * @param password
     * @return
     */
    private String encrypt(String salt,String password){
        for (int i = 0; i < 3; i++) {//三次迭代加密
            password = MD5.encrypt(salt+password+salt);
        }
        return password;
    }
    @Override
    public boolean isExistNumber(String number) {
        return userInfoMapper.countNumber(number) > 0;
    }

}
