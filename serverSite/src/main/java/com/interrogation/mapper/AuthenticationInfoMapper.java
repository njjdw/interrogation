package com.interrogation.mapper;

import com.interrogation.pojo.dto.DoctorInfoDTO;
import com.interrogation.pojo.entity.AuthenticationInfo;
import java.util.List;

public interface AuthenticationInfoMapper {

    List<DoctorInfoDTO> selectDoctors();

    DoctorInfoDTO getAuthenticateInfoByUserId(Integer userId);

    void insertAuthenticationInfo(AuthenticationInfo authenticationInfo);

    String selectDoctorNameById(Integer doctorId);

    List<AuthenticationInfo> selectInfoByPage();
}