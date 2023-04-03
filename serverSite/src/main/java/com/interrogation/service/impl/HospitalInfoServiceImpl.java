package com.interrogation.service.impl;

import com.interrogation.mapper.HospitalInfoMapper;
import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.service.HospitalInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HospitalInfoServiceImpl implements HospitalInfoService {

    @Resource
    private HospitalInfoMapper hospitalInfoMapper;
    @Override
    public List<DictDTO> getAllHospitalNames() {
        List<DictDTO> hospitalNames = hospitalInfoMapper.selectAllHospitalName();
        return hospitalNames;
    }
}
