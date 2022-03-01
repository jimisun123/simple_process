package com.jimisun.simpleprocess.service;

import com.jimisun.simpleprocess.dao.ProcessTemplateMapper;
import com.jimisun.simpleprocess.entity.ProcessTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ProcessTemplateServiceImpl implements ProcessTemplateService{

    @Resource
    private ProcessTemplateMapper processTemplateMapper;

    @Override
    public ProcessTemplate getProcessTemplateByProcessTemplateKey(String processTemplateKey) {
        return processTemplateMapper.selectById(processTemplateKey);
    }
}
