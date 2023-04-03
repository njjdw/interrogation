package com.interrogation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.interrogation.mapper.InterviewRecordsMapper;
import com.interrogation.pojo.entity.InterviewRecords;
import com.interrogation.service.InterviewRecordsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class InterviewRecordsServiceImpl implements InterviewRecordsService {
    @Resource
    private InterviewRecordsMapper interviewRecordsMapper;

    /**
     *
     * @param jsonObject{"patientName":string,
     *                  "patientSex":string,
     *                  "doctorId":int,
     *                  "patientAge":int,
     *                  "description":string,
     *                  "queryUser":int}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertRecord(JSONObject jsonObject) {
        InterviewRecords records = new InterviewRecords();
        BeanUtils.copyProperties(jsonObject,records);
        records.setUserId(jsonObject.getInteger("queryUser"));
        System.out.println(records);
        return interviewRecordsMapper.insertInterviewRecords(records);
    }

}
