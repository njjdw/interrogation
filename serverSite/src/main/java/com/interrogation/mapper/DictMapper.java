package com.interrogation.mapper;

import com.interrogation.pojo.dto.DictDTO;
import com.interrogation.pojo.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictMapper {

    String selectDict(Integer level);

    List<DictDTO> selectDictByParentId(Integer parentId);
}