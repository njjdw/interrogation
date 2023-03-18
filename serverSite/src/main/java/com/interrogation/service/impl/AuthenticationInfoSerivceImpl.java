package com.interrogation.service.impl;

import com.interrogation.exception.InterrogationException;
import com.interrogation.mapper.AuthenticationInfoMapper;
import com.interrogation.pojo.dto.DoctorInfoDTO;
import com.interrogation.pojo.entity.AuthenticationInfo;
import com.interrogation.service.AuthenticationInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AuthenticationInfoSerivceImpl implements AuthenticationInfoService {

    @Resource
    private AuthenticationInfoMapper authenticationInfoMapper;
    @Override
    public List<DoctorInfoDTO> getAllDoctors() {
        List<DoctorInfoDTO> doctorInfoDTOS = authenticationInfoMapper.selectDoctors();
        return doctorInfoDTOS;
    }

    @Override
    public DoctorInfoDTO getAuthenticateInfo(Integer userId) {
        DoctorInfoDTO info = authenticationInfoMapper.getAuthenticateInfoByUserId(userId);
        return info;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitAuthenticationInfo(AuthenticationInfo authenticationInfo) {
        try {
            authenticationInfoMapper.insertAuthenticationInfo(authenticationInfo);
        } catch (Exception e) {
            throw new InterrogationException("认证提交失败",-1);
        }
    }

    @Override
    public String getDoctorName(Integer doctorId) {
        String name = authenticationInfoMapper.selectDoctorNameById(doctorId);
        return name;
    }

    @Override
    public List<AuthenticationInfo> getAllInfoByPage() {
        List<AuthenticationInfo> authenticationInfos = authenticationInfoMapper.selectInfoByPage();
        return authenticationInfos;
    }

    @Override
    public void changeStatus(Integer id, Integer status) {

    }
}
