package com.interrogation.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.interrogation.mapper.ChatRecordsMapper;
import com.interrogation.pojo.entity.ChatRecords;
import com.interrogation.service.ChatingRecordsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ChatingRecordsServiceImpl implements ChatingRecordsService {

    @Resource
    private ChatRecordsMapper chatingRecordsMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void inputChatRecords(JSONObject chatRecords) {
        ChatRecords records = new ChatRecords();
        BeanUtils.copyProperties(chatRecords,records);
        chatingRecordsMapper.insert(records);
    }
}
