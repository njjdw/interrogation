package com.interrogation.service;

import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.pojo.entity.Dict;

import java.util.List;

public interface DictService {

    List<DictDTO> getDict(Integer parentId);
}
