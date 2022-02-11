package com.jimisun.simpleprocess.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jimisun.simpleprocess.entity.Process;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessMapper extends BaseMapper<Process> {
}
