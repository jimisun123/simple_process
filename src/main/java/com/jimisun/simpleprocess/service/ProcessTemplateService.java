package com.jimisun.simpleprocess.service;

import com.jimisun.simpleprocess.entity.ProcessTemplate;

/**
 * 流程模板服务层
 */
public interface ProcessTemplateService {

    public ProcessTemplate getProcessTemplateByProcessTemplateKey(String processTemplateKey);

}
