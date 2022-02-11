package com.jimisun.simpleprocess.core;

import com.jimisun.simpleprocess.entity.Process;
import com.jimisun.simpleprocess.entity.ProcessNode;
import com.jimisun.simpleprocess.entity.ProcessTask;
import com.jimisun.simpleprocess.entity.ProcessTemplate;

/**
 * 流程引擎接口
 */
public interface ProcessEngine {

    public Boolean saveOrUpdateProcessTemplate(ProcessTemplate processTemplate);

    public Boolean saveOrUpdateProcess(Process process);

    public Boolean saveOrUpdateProcessTask(ProcessTask processTask);

    public Boolean saveOrUpdateProcessNode(ProcessNode processNode);

    public Boolean createProcess(String processTemplatekey, String buskey);

    public Boolean changeProcessTemplateStatus(String processTemplateKey, Integer status);

    public Boolean changeProcessStatus(String processId, Integer status);

    public Boolean changeProcessTaskStatus(String processTaskId, Integer status);

    public Boolean changeProcessData(String processId, String data);

    public Boolean implementProcess(String processId);

    public Boolean rejectProcess(String processId, String operator);

    public Boolean executeProcessTask(String processTaskId, String operator, String opinion);

    public Boolean rejectTask(String processTaskId, String operator, String opinion);


}
