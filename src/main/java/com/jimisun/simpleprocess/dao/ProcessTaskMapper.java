package com.jimisun.simpleprocess.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jimisun.simpleprocess.entity.ProcessTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessTaskMapper extends BaseMapper<ProcessTask> {
}
