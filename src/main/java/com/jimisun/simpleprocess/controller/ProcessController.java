package com.jimisun.simpleprocess.controller;

import com.jimisun.simpleprocess.common.ProcessStatusEnum;
import com.jimisun.simpleprocess.core.ProcessEngine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Resource
    private ProcessEngine processEngine;

    @RequestMapping("/createProcess")
    public Object createProcess(String templateKey, String busKey) {
        return processEngine.createProcess(templateKey, busKey,null,null);
    }

    @RequestMapping("/pauseProcess")
    public Object pauseProcess(String processId, String operator, String opinion) {
        return processEngine.changeProcessStatus(processId, operator, opinion, ProcessStatusEnum.PAUSE);
    }

    @RequestMapping("/closeProcess")
    public Object closeProcess(String processId, String operator, String opinion) {
        return processEngine.changeProcessStatus(processId, operator, opinion,ProcessStatusEnum.CLOSE);
    }

    @RequestMapping("/executeTask")
    public Object executeProcessTask(String processTaskId, String operator, String opinion) {
        return processEngine.executeProcessTask(processTaskId, operator, opinion);
    }

    @RequestMapping("/rejectTask")
    public Object rejectTask(String processTaskId, String operator, String opinion) {
        return processEngine.rejectTask(processTaskId, operator, opinion);
    }




}
