package com.interrogation.service.impl;

import com.interrogation.mapper.DictMapper;
import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.pojo.entity.Dict;
import com.interrogation.service.DictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {
    @Resource
    private DictMapper dictMapper;
    @Override
    public List<DictDTO> getDict(Integer parentId) {
        List<DictDTO> dicts = dictMapper.selectDictByParentId(parentId);
        return dicts;
    }
}
