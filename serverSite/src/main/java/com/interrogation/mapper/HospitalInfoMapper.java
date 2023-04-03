package com.interrogation.mapper;

import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.pojo.entity.HospitalInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HospitalInfoMapper {
    String selectHospitalNameById(@Param("hospitalInd") Integer hospitalId);
    List<DictDTO> selectAllHospitalName();
}