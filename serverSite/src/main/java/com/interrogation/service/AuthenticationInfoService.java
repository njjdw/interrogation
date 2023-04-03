package com.interrogation.service;

import com.interrogation.pojo.dto.DoctorInfoDTO;
import com.interrogation.pojo.entity.AuthenticationInfo;

import java.util.List;

public interface AuthenticationInfoService {

    List<DoctorInfoDTO> getAllDoctors();

    DoctorInfoDTO getAuthenticateInfo(Integer userId);

    void submitAuthenticationInfo(AuthenticationInfo authenticationInfo);

    String getDoctorName(Integer doctorId);

    List<AuthenticationInfo> getAllInfoByPage();

    void changeStatus(Integer id, Integer status);
}
