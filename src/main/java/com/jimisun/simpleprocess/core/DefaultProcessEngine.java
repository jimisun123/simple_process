package com.jimisun.simpleprocess.core;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jimisun.simpleprocess.common.NodeExecuteTypeEnum;
import com.jimisun.simpleprocess.common.NodeTypeEnum;
import com.jimisun.simpleprocess.common.ProcessStatusEnum;
import com.jimisun.simpleprocess.common.ProcessTaskStatusEnum;
import com.jimisun.simpleprocess.dao.ProcessMapper;
import com.jimisun.simpleprocess.dao.ProcessTaskMapper;
import com.jimisun.simpleprocess.entity.Process;
import com.jimisun.simpleprocess.entity.ProcessNodeDefine;
import com.jimisun.simpleprocess.entity.ProcessTask;
import com.jimisun.simpleprocess.entity.ProcessTemplate;
import com.jimisun.simpleprocess.service.ProcessTemplateService;
import com.jimisun.simpleprocess.utils.GroovyUtil;
import com.jimisun.simpleprocess.utils.ProcessTemplateNodeDefineUtil;
import com.jimisun.simpleprocess.utils.StandardUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 默认流程引擎实现
 */
@Component
@Transactional
public class DefaultProcessEngine implements ProcessEngine {

    @Resource
    private ProcessTemplateService processTemplateService;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Resource
    private ProcessEngineExt processEngineExt;

    /**
     * 创建流程实例
     *
     * @param processTemplateKey
     * @param processBusKey
     * @param processBusData
     * @param processAttributeData
     * @return
     */
    @Override
    public Boolean createProcess(String processTemplateKey, String processBusKey, Map processBusData, Map processAttributeData) {
        // step1 创建流程实例
        ProcessTemplate processTemplateByProcessTemplateKey = processTemplateService.getProcessTemplateByProcessTemplateKey(processTemplateKey);
        if (ObjectUtil.isNotEmpty(processTemplateByProcessTemplateKey)) {
            Process process = new Process();
            process.setProcessId(StandardUtil.getStandardId());
            process.setProcessTemplateKey(processTemplateKey);
            process.setProcessBusKey(processBusKey);
            if (ObjectUtil.isNotEmpty(processBusData)) {
                process.setProcessBusData(JSONUtil.toJsonStr(processBusData));
            }
            if (ObjectUtil.isNotEmpty(processAttributeData)) {
                process.setProcessAttributeData(JSONUtil.toJsonStr(processAttributeData));
            }
            process.setProcessStatus(ProcessStatusEnum.RUN);
            process.setProcessOpenTime(StandardUtil.getStandarStrTime());
            processMapper.insert(process);
            //step2 自动执行start节点
            jumpProcessNode(process.getProcessId(), "start");
        } else {
            throw new RuntimeException("找不到流程模板，可能processTemplateKey错误");
        }
        return true;
    }


    /**
     * 完成流程任务
     *
     * @param processTaskId
     * @param operator
     * @param opinion
     * @return
     */
    @Override
    public Boolean executeProcessTask(String processTaskId, String operator, String opinion) {
        //step1 收集资料
        ProcessTask processTask = processTaskMapper.selectById(processTaskId);
        Process process = processMapper.selectById(processTask.getProcessId());
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateByProcessTemplateKey(process.getProcessTemplateKey());
        ProcessNodeDefine nodeDefine = ProcessTemplateNodeDefineUtil.getNodeDefine(processTemplate, processTask.getProcessNodeKey());
        ProcessNodeDefine nextNodeDefine = ProcessTemplateNodeDefineUtil.getNextNodeDefine(processTemplate, processTask.getProcessNodeKey());

        //step2 将当前流程任务标记为已完成
        processTask.setProcessTaskOperation(operator);
        processTask.setProcessTaskOpinion(opinion);
        processTask.setProcessTaskStatus(ProcessTaskStatusEnum.TREATED);
        processTask.setProcessTaskCloseTime(StandardUtil.getStandarStrTime());
        processTaskMapper.updateById(processTask);


        //step3 判断当前任务所属节点的执行类型
        if (nodeDefine.getNodeTaskExecuteType().equals(NodeExecuteTypeEnum.ALL)) {
            //查询流程任务是否还有未处理的任务 如果当前节点的任务均已执行完毕 则执行下一个任务
            LambdaQueryWrapper<ProcessTask> processTaskLambdaQueryWrapper = Wrappers.lambdaQuery();
            processTaskLambdaQueryWrapper.eq(ProcessTask::getProcessId, process.getProcessId())
                    .eq(ProcessTask::getProcessNodeKey, nodeDefine.getNodeKey())
                    .eq(ProcessTask::getProcessTaskStatus, ProcessTaskStatusEnum.UNTREATED);
            List<ProcessTask> processTasks = processTaskMapper.selectList(processTaskLambdaQueryWrapper);
            if (ObjectUtil.isEmpty(processTasks)) {
                //判断下个节点类型是否是关闭网关
                if (nextNodeDefine.getNodeType().equals(NodeTypeEnum.CLOSE_GATEWAY)) {
                    //如果是关闭网关类型 要查询该流程实例下的其他节点是否包含未处理的流程任务
                    if (ObjectUtil.isEmpty(queryUntreatedForNotEqualsProcessNodeKey(processTask))) {
                        jumpProcessNode(processTask.getProcessId(), nodeDefine.getNextNodeKey());
                    }
                } else {
                    //说明该分支节点后面还要分支任务要执行
                    jumpProcessNode(processTask.getProcessId(), nodeDefine.getNextNodeKey());
                }
            }
        } else if (nodeDefine.getNodeTaskExecuteType().equals(NodeExecuteTypeEnum.ANY)) {
            //取消该任务节点的所有代办任务
            changeAllUntreatedProcessTask(process, nodeDefine, ProcessTaskStatusEnum.CANCEL);
            //判断该流程任务是否是分支流程
            if (nodeDefine.getNodeType().equals(NodeTypeEnum.BRANCH_TASK)) {
                //判断下个节点类型是否是关闭网关
                if (nextNodeDefine.getNodeType().equals(NodeTypeEnum.CLOSE_GATEWAY)) {
                    //如果是关闭网关类型 要查询该流程实例下的其他节点是否包含未处理的流程任务
                    if (ObjectUtil.isEmpty(queryUntreatedForNotEqualsProcessNodeKey(processTask))) {
                        jumpProcessNode(processTask.getProcessId(), nodeDefine.getNextNodeKey());
                    }
                } else {
                    //说明该分支节点后面还要分支任务要执行
                    jumpProcessNode(processTask.getProcessId(), nodeDefine.getNextNodeKey());
                }
            } else {
                jumpProcessNode(process.getProcessId(), nodeDefine.getNextNodeKey());
            }
        }
        return true;
    }


    /**
     * 驳回任务
     *
     * @param processTaskId
     * @param operator
     * @param opinion
     * @return
     */
    @Override
    public Boolean rejectTask(String processTaskId, String operator, String opinion) {
        //step1 收集资料
        ProcessTask processTask = processTaskMapper.selectById(processTaskId);
        Process process = processMapper.selectById(processTask.getProcessId());
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateByProcessTemplateKey(process.getProcessTemplateKey());
        ProcessNodeDefine nodeDefine = ProcessTemplateNodeDefineUtil.getNodeDefine(processTemplate, processTask.getProcessNodeKey());
        ProcessNodeDefine preNodeDefine = ProcessTemplateNodeDefineUtil.getPreNodeDefine(processTemplate, nodeDefine.getNodeKey());

        //step2 将当前节点的流程任务标记为取消
        if (nodeDefine.getNodeType().equals(NodeTypeEnum.TASK)) {
            //判断是否可以驳回
            ProcessNodeDefine startNode = ProcessTemplateNodeDefineUtil.getNodeDefine(processTemplate, "start");
            if (startNode.getNextNodeKey().equals(nodeDefine.getNodeKey())) {
                throw new RuntimeException("已经是开始节点无法再被驳回！");
            }
            //将此流程任务标记未驳回
            processTask.setProcessTaskOperation(operator);
            processTask.setProcessTaskOpinion(opinion);
            processTask.setProcessTaskStatus(ProcessTaskStatusEnum.REGECTED);
            processTask.setProcessTaskCloseTime(StandardUtil.getStandarStrTime());
            processTaskMapper.updateById(processTask);
            //取消当前节点的所有未处理的任务
            changeAllUntreatedProcessTask(process, nodeDefine, ProcessTaskStatusEnum.CANCEL);
        } else if (nodeDefine.getNodeType().equals(NodeTypeEnum.BRANCH_TASK)) {
            //判断是否可以驳回
            if (ObjectUtil.isEmpty(preNodeDefine) ||
                    ObjectUtil.isEmpty(preNodeDefine.getNodeType()) ||
                    preNodeDefine.getNodeType().equals(NodeTypeEnum.OPEN_GATEWAY)) {
                throw new RuntimeException("上个节点无法处理驳回请求！");
            }
            //如果可以驳回  则将此流程任务标记为驳回  此节点的其他流程任务标记为取消
            processTask.setProcessTaskOperation(operator);
            processTask.setProcessTaskOpinion(opinion);
            processTask.setProcessTaskStatus(ProcessTaskStatusEnum.REGECTED);
            processTask.setProcessTaskCloseTime(StandardUtil.getStandarStrTime());
            processTaskMapper.updateById(processTask);
            changeAllUntreatedProcessTask(process, nodeDefine, ProcessTaskStatusEnum.CANCEL);
        }

        processEngineExt.ProcessTaskRejectCompleteExt(processTaskId); //向外发布事件

        //step3 重新发布任务
        jumpProcessNode(process.getProcessId(), preNodeDefine.getNodeKey());
        return true;
    }


    /**
     * 将流程实例跳转到执行节点
     * 1. 判断要跳转节点的类型 根据类型选择后续动作
     *
     * @param processId   流程实例ID
     * @param jumpNodeKey 要跳转到到流程节点
     * @return
     */
    private void jumpProcessNode(String processId, String jumpNodeKey) {
        //step1 收集数据
        Process process = processMapper.selectById(processId);
        ProcessTemplate processTemplate = processTemplateService.getProcessTemplateByProcessTemplateKey(process.getProcessTemplateKey());
        ProcessNodeDefine nodeDefine = ProcessTemplateNodeDefineUtil.getNodeDefine(processTemplate, jumpNodeKey);

        if (ObjectUtil.isEmpty(nodeDefine)) {
            throw new RuntimeException("节点定义不存在:" + jumpNodeKey);
        }

        if (ObjectUtil.notEqual(nodeDefine.getNodeKey(), "start")) { //向外发布事件
            ProcessNodeDefine preNodeDefine = ProcessTemplateNodeDefineUtil.getPreNodeDefine(processTemplate, jumpNodeKey);
            if (ObjectUtil.isNotEmpty(preNodeDefine) && StrUtil.isNotEmpty(preNodeDefine.getNodeKey())){
                processEngineExt.ProcessNodeCompleteExt(processId, preNodeDefine.getNodeKey());
            }
        }


        if (ObjectUtil.isEmpty(nodeDefine)) {
            throw new RuntimeException("节点定义不存在:" + jumpNodeKey);
        }

        //step2 重新发布jumpNodeKey流程节点的任务
        if (nodeDefine.getNodeType().equals(NodeTypeEnum.START)) {
            //如果要跳转的流程节点是START类型和CLOSE_GATEWAY类型的节点 需要跳过该节点继续向后面节点执行
            ProcessTask processTask = new ProcessTask(StandardUtil.getStandardId(), processTemplate.getProcessTemplateKey(),
                    processTemplate.getProcessTemplateName(),nodeDefine.getNodeKey(), nodeDefine.getNodeName(),
                    process.getProcessId(),  "超级管理员", "超级管理员", "", process.getProcessBusData(), "",
                    nodeDefine.getNodeType(), ProcessTaskStatusEnum.TREATED, StandardUtil.getStandarStrTime(), StandardUtil.getStandarStrTime(), "流程引擎跳过开始节点");
            processTaskMapper.insert(processTask);
            jumpProcessNode(process.getProcessId(), nodeDefine.getNextNodeKey());
        } else if (nodeDefine.getNodeType().equals(NodeTypeEnum.END)) {
            //如果要跳转的流程节点是END类型的节点 自动插入一条流程任务标记为完成 更改流程实例的状态
            ProcessTask processTask = new ProcessTask(StandardUtil.getStandardId(),
                    processTemplate.getProcessTemplateKey(),
                    processTemplate.getProcessTemplateName(),nodeDefine.getNodeKey(), nodeDefine.getNodeName(),
                    process.getProcessId(), "超级管理员", "超级管理员", "", process.getProcessBusData(), "",
                    nodeDefine.getNodeType(), ProcessTaskStatusEnum.TREATED, StandardUtil.getStandarStrTime(), StandardUtil.getStandarStrTime(), "流程引擎自动执行结束节点");
            processTaskMapper.insert(processTask);
            process.setProcessStatus(ProcessStatusEnum.END);
            process.setProcessCloseTime(StandardUtil.getStandarStrTime());
            processMapper.updateById(process);
        } else if (nodeDefine.getNodeType().equals(NodeTypeEnum.TASK)
                || nodeDefine.getNodeType().equals(NodeTypeEnum.BRANCH_TASK)) {
            //根据流程定义 发布流程任务
            releaseTask(processTemplate, process, nodeDefine);
        } else if (nodeDefine.getNodeType().equals(NodeTypeEnum.OPEN_GATEWAY)) {
            //根据开始网关发布流程任务
            String openGatewayScript = nodeDefine.getOpenGatewayScript();
            Map attributeDateMap = JSONUtil.toBean(process.getProcessAttributeData(), Map.class);
            String[] nodeArray = (String[]) GroovyUtil.eval(openGatewayScript, attributeDateMap);
            for (String nodeKey : nodeArray) {
                jumpProcessNode(process.getProcessId(), nodeKey);
            }
        } else if (nodeDefine.getNodeType().equals(NodeTypeEnum.CLOSE_GATEWAY)) {
            //如果要跳转的流程节点是CLOSE_GATEWAY类型的节点 需要跳过该节点继续向后面节点执行
            ProcessTask processTask = new ProcessTask(StandardUtil.getStandardId(), processTemplate.getProcessTemplateKey(),processTemplate.getProcessTemplateName(),nodeDefine.getNodeKey(), nodeDefine.getNodeName(),
                    process.getProcessId(),  "超级管理员", "超级管理员", "",
                    process.getProcessBusData(), "", nodeDefine.getNodeType(), ProcessTaskStatusEnum.TREATED, StandardUtil.getStandarStrTime(),
                    StandardUtil.getStandarStrTime(), "流程引擎跳过结束网关");
            processTaskMapper.insert(processTask);
            jumpProcessNode(process.getProcessId(), nodeDefine.getNextNodeKey());
        }
    }


    /**
     * 发布流程任务
     *
     * @param processTemplate
     * @param process
     * @param nodeDefine
     */
    private void releaseTask(ProcessTemplate processTemplate, Process process, ProcessNodeDefine nodeDefine) {
        String nodeActors = nodeDefine.getNodeActors();
        Map attributeDateMap = JSONUtil.toBean(process.getProcessAttributeData(), Map.class);
        String[] nodeArray = (String[]) GroovyUtil.eval(nodeActors, attributeDateMap);
        for (String actor : nodeArray) {
            ProcessTask processTask = new ProcessTask();
            processTask.setProcessTaskId(StandardUtil.getStandardId());
            //封装流程定义信息
            processTask.setProcessTemplateKey(processTemplate.getProcessTemplateKey());
            processTask.setProcessTemplateName(processTemplate.getProcessTemplateName());
            //封装流程实例信息
            processTask.setProcessId(process.getProcessId());
            //封装流程节点信息
            processTask.setProcessNodeKey(nodeDefine.getNodeKey());
            processTask.setProcessNodeName(nodeDefine.getNodeName());
            //封装流程任务信息
            processTask.setProcessTaskActor(actor);
            processTask.setProcessTaskFormUrl(nodeDefine.getNodeFormUrl());
            processTask.setProcessBusDataSnapshot(process.getProcessBusData());
            processTask.setProcessTaskNodeType(nodeDefine.getNodeType());
            processTask.setProcessTaskStatus(ProcessTaskStatusEnum.UNTREATED);
            processTask.setProcessTaskCreateTime(StandardUtil.getStandarStrTime());
            processTaskMapper.insert(processTask);
        }
    }


    /**
     * 改变所有未处理任务的状态
     *
     * @param process           所属流程
     * @param processNodeDefine 所属流程节点 可传递空 如果为空则会改变该流程下的所有未处理的任务
     * @param processTaskStatus 状态
     */
    private void changeAllUntreatedProcessTask(Process process, ProcessNodeDefine processNodeDefine, String processTaskStatus) {
        List<ProcessTask> processTasks;
        if (ObjectUtil.isNotEmpty(processNodeDefine)) {
            processTasks = queryUntreatedForProcessAndNode(process, processNodeDefine);
        } else {
            processTasks = queryUntreatedForProcess(process);
        }
        for (ProcessTask processTask : processTasks) {
            processTask.setProcessTaskOperation("超级管理员");
            processTask.setProcessBusDataSnapshot(process.getProcessBusData());
            processTask.setProcessTaskStatus(processTaskStatus);
            processTaskMapper.updateById(processTask);
        }
    }


    /**
     * 查询当前流程实例下不等于该流程节点的未处理任务
     *
     * @param processTask
     * @return
     */
    private List<ProcessTask> queryUntreatedForNotEqualsProcessNodeKey(ProcessTask processTask) {
        LambdaQueryWrapper<ProcessTask> processTaskLambdaQueryWrapper = Wrappers.lambdaQuery();
        processTaskLambdaQueryWrapper.eq(ProcessTask::getProcessId, processTask.getProcessId())
                .ne(ProcessTask::getProcessNodeKey, processTask.getProcessNodeKey())
                .eq(ProcessTask::getProcessTaskStatus, ProcessTaskStatusEnum.UNTREATED);
        return processTaskMapper.selectList(processTaskLambdaQueryWrapper);

    }


    /**
     * 查询当前流程实例下是否还存在未处理的任务
     *
     * @param process
     * @return
     */
    private List<ProcessTask> queryUntreatedForProcess(Process process) {
        LambdaQueryWrapper<ProcessTask> otherProcessTaskLambdaQueryWrapper = Wrappers.lambdaQuery();
        otherProcessTaskLambdaQueryWrapper.eq(ProcessTask::getProcessId, process.getProcessId())
                .eq(ProcessTask::getProcessTaskStatus, ProcessTaskStatusEnum.UNTREATED);
        List<ProcessTask> processTasks = processTaskMapper.selectList(otherProcessTaskLambdaQueryWrapper);
        return processTasks;
    }


    /**
     * 查询当前流程实例的流程节点下是否还存在未处理的任务
     *
     * @param process
     * @return
     */
    private List<ProcessTask> queryUntreatedForProcessAndNode(Process process, ProcessNodeDefine processNodeDefine) {
        LambdaQueryWrapper<ProcessTask> otherProcessTaskLambdaQueryWrapper = Wrappers.lambdaQuery();
        otherProcessTaskLambdaQueryWrapper.eq(ProcessTask::getProcessId, process.getProcessId())
                .eq(ProcessTask::getProcessNodeKey, processNodeDefine.getNodeKey())
                .eq(ProcessTask::getProcessTaskStatus, ProcessTaskStatusEnum.UNTREATED);
        List<ProcessTask> processTasks = processTaskMapper.selectList(otherProcessTaskLambdaQueryWrapper);
        return processTasks;
    }


}
