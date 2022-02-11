package com.jimisun.simpleprocess.core;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jimisun.simpleprocess.common.ProcessStatusEnum;
import com.jimisun.simpleprocess.common.ProcessTaskStatusEnum;
import com.jimisun.simpleprocess.dao.ProcessMapper;
import com.jimisun.simpleprocess.dao.ProcessNodeMapper;
import com.jimisun.simpleprocess.dao.ProcessTaskMapper;
import com.jimisun.simpleprocess.dao.ProcessTemplateMapper;
import com.jimisun.simpleprocess.entity.Process;
import com.jimisun.simpleprocess.entity.ProcessNode;
import com.jimisun.simpleprocess.entity.ProcessTask;
import com.jimisun.simpleprocess.entity.ProcessTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认流程引擎实现
 */
@Service
@Transactional
public class DefaultProcessEngine implements ProcessEngine {
    @Resource
    private ProcessTemplateMapper processTemplateMapper;

    @Resource
    private ProcessNodeMapper processNodeMapper;

    @Resource
    private ProcessMapper processMapper;

    @Resource
    private ProcessTaskMapper processTaskMapper;

    @Override
    public Boolean saveOrUpdateProcessTemplate(ProcessTemplate processTemplate) {
        if (StrUtil.isNotEmpty(processTemplate.getProcessTemplateKey())) {
            processTemplateMapper.updateById(processTemplate);
        } else {
            processTemplateMapper.insert(processTemplate);
        }
        return true;
    }

    @Override
    public Boolean saveOrUpdateProcess(Process process) {
        if (StrUtil.isNotEmpty(process.getProcessId())) {
            processMapper.updateById(process);
        } else {
            processMapper.insert(process);
        }
        return true;
    }

    @Override
    public Boolean saveOrUpdateProcessTask(ProcessTask processTask) {
        if (StrUtil.isNotEmpty(processTask.getProcessTaskId())) {
            processTaskMapper.updateById(processTask);
        } else {
            processTaskMapper.insert(processTask);
        }
        return true;
    }

    @Override
    public Boolean saveOrUpdateProcessNode(ProcessNode processNode) {
        if (StrUtil.isNotEmpty(processNode.getProcessNodeKey())) {
            processNodeMapper.updateById(processNode);
        } else {
            processNodeMapper.insert(processNode);
        }
        return true;
    }

    @Override
    public Boolean createProcess(String processTemplateKey, String busKey) {
        //收集信息
        LambdaQueryWrapper<ProcessNode> processNodeLambdaQueryWrapper = Wrappers.lambdaQuery();
        processNodeLambdaQueryWrapper.eq(ProcessNode::getProcessTemplateKey, processTemplateKey).eq(ProcessNode::getProcessNodeKey, "start");
        ProcessNode startProcess = processNodeMapper.selectOne(processNodeLambdaQueryWrapper);

        //处理业务
        Process process = new Process();
        process.setProcessId(IdUtil.simpleUUID());
        process.setProcessTemplateKey(processTemplateKey);
        process.setBusKey(busKey);
        process.setProcessStatus(ProcessStatusEnum.RUN);
        process.setCurrentNodeKey(startProcess.getProcessNodeKey());
        process.setCurrentNodeName(startProcess.getProcessNodeName());
        process.setCurrentNodeDistributeType(startProcess.getNodeDistributeType());
        process.setCurrentNodeExecuteType(startProcess.getNodeExecuteType());
        processMapper.insert(process);
        //跳过开始节点，执行下个任务
        implementProcess(process.getProcessId());
        return true;
    }


    @Override
    public Boolean changeProcessTemplateStatus(String processTemplateKey, Integer status) {
        ProcessTemplate processTemplate = processTemplateMapper.selectById(processTemplateKey);
        processTemplate.setProcessStatus(status);
        processTemplateMapper.updateById(processTemplate);
        return true;
    }


    @Override
    public Boolean changeProcessStatus(String processId, Integer status) {
        Process process = processMapper.selectById(processId);
        process.setProcessStatus(status);
        processMapper.updateById(process);
        return true;
    }

    @Override
    public Boolean changeProcessTaskStatus(String processTaskId, Integer status) {
        ProcessTask processTask = processTaskMapper.selectById(processTaskId);
        processTask.setProcessTaskStatus(status);
        processTaskMapper.updateById(processTask);
        return true;
    }

    @Override
    public Boolean changeProcessData(String processId, String data) {
        Process process = processMapper.selectById(processId);
        process.setProcessData(data);
        processMapper.updateById(process);
        return true;
    }

    @Override
    public Boolean implementProcess(String processId) {
        //收集数据
        Process process = processMapper.selectById(processId);
        LambdaQueryWrapper<ProcessNode> processNodeLambdaQueryWrapper = Wrappers.lambdaQuery();
        processNodeLambdaQueryWrapper.eq(ProcessNode::getProcessTemplateKey, process.getProcessTemplateKey());
        List<ProcessNode> processNodes = processNodeMapper.selectList(processNodeLambdaQueryWrapper);
        Map<String, ProcessNode> processNodeMap = processNodes.stream().collect(Collectors.toMap(ProcessNode::getProcessNodeKey, a -> a, (k1, k2) -> k1));
        //获取下个流程节点
        ProcessNode currentNode = processNodeMap.get(process.getCurrentNodeKey());
        ProcessNode nextProcessNode = processNodeMap.get(currentNode.getNextProcessNodeKey());
        //更改流程实例状态
        process.setCurrentNodeKey(nextProcessNode.getProcessNodeKey());
        process.setCurrentNodeName(nextProcessNode.getProcessNodeName());
        process.setCurrentNodeExecuteType(nextProcessNode.getNodeExecuteType());
        process.setCurrentNodeDistributeType(nextProcessNode.getNodeDistributeType());
        if (nextProcessNode.getProcessNodeKey().equals("end")) {
            //结束流程
            process.setProcessStatus(ProcessStatusEnum.COMPLETED);
        } else {
            //发布任务
            releaseTask(processId, nextProcessNode.getActors(), nextProcessNode.getProcessNodeId(), nextProcessNode.getProcessNodeKey(), nextProcessNode.getProcessNodeName(), nextProcessNode.getFormUrl(),
                    "此处是执行任务的url地址", "此处是驳回任务的url地址");
        }
        processMapper.updateById(process);
        return true;
    }

    private void releaseTask(String processId, String nextActors, String processNodeId, String nextNodeKey, String nextNodeName, String formUrl, String agreeAddress, String regectAddress) {
        List<String> actors = Arrays.asList(nextActors.split(","));
        for (String actor : actors) {
            ProcessTask processTask = new ProcessTask();
            processTask.setProcessId(processId);
            processTask.setProcessTaskId(IdUtil.simpleUUID());
            processTask.setProcessNodeId(processNodeId);
            processTask.setProcessNodeKey(nextNodeKey);
            processTask.setProcessNodeName(nextNodeName);
            processTask.setProcessTaskStatus(ProcessTaskStatusEnum.CREATE);
            processTask.setAscription(actor);
            processTask.setFormUrl(formUrl);
            processTask.setTaskAgreeAddress(agreeAddress);
            processTask.setTaskRejectAddress(regectAddress);
            processTaskMapper.insert(processTask);
        }
    }

    @Override
    public Boolean rejectProcess(String processId, String operator) {
        //收集信息
        Process process = processMapper.selectById(processId);
        LambdaQueryWrapper<ProcessTask> processTaskLambdaQueryWrapper = Wrappers.lambdaQuery();
        processTaskLambdaQueryWrapper.eq(ProcessTask::getProcessId, process.getProcessId()).eq(ProcessTask::getProcessNodeKey, process.getCurrentNodeKey()).eq(ProcessTask::getProcessTaskStatus, ProcessTaskStatusEnum.CREATE);
        List<ProcessTask> processTasks = processTaskMapper.selectList(processTaskLambdaQueryWrapper);
        LambdaQueryWrapper<ProcessNode> processNodeLambdaQueryWrapper = Wrappers.lambdaQuery();
        processNodeLambdaQueryWrapper.eq(ProcessNode::getProcessTemplateKey, process.getProcessTemplateKey()).eq(ProcessNode::getNextProcessNodeKey, process.getCurrentNodeKey());
        ProcessNode preNode = processNodeMapper.selectOne(processNodeLambdaQueryWrapper);
        //更改当前节点下所有未处理等任务为驳回状态
        for (ProcessTask processTask : processTasks) {
            processTask.setProcessTaskStatus(ProcessTaskStatusEnum.REJECT);
            processTask.setOperator(operator);
            processTask.setOperatorTime(new Date().toString());
            processTaskMapper.updateById(processTask);
        }
        //修改当前流程实例的当前节点信息为上个节点
        process.setCurrentNodeKey(preNode.getProcessNodeKey());
        process.setCurrentNodeName(preNode.getProcessNodeName());
        process.setCurrentNodeDistributeType(preNode.getNodeDistributeType());
        process.setCurrentNodeExecuteType(preNode.getNodeExecuteType());
        process.setProcessStatus(ProcessStatusEnum.RUN);
        //如果上个节点不是开始节点则创建该节点等流程任务
        if (!preNode.getProcessNodeKey().equals("start")) {
            releaseTask(processId, preNode.getActors(), preNode.getProcessNodeId(), preNode.getProcessNodeKey(), preNode.getProcessNodeName(), preNode.getFormUrl(), "此处是执行任务的url地址", "此处是驳回任务的url地址");
        }
        processMapper.updateById(process);
        return true;
    }

    @Override
    public Boolean executeProcessTask(String processTaskId, String operator, String opinion) {
        //收集信息
        ProcessTask processTask = processTaskMapper.selectById(processTaskId);
        Process process = processMapper.selectById(processTask.getProcessId());
        ProcessNode processNode = processNodeMapper.selectById(processTask.getProcessNodeId());
        //将当前任务标记为已完成
        processTask.setProcessTaskStatus(ProcessTaskStatusEnum.OK);
        processTask.setOpinion(opinion);
        processTask.setOperator(operator);//设置实际操作人
        processTask.setOperatorTime(new Date().toString());
        processTaskMapper.updateById(processTask);
        //操作当前节点的其他任务
        if (processNode.getNodeExecuteType().equals("ANY")) {
            //将当前任务标记为已完成，该节点的其他任务标记为取消
            ProcessTask processTaskForUpdateValue = new ProcessTask();
            processTaskForUpdateValue.setProcessTaskStatus(ProcessTaskStatusEnum.CANCEL);
            processTaskForUpdateValue.setOperatorTime(new Date().toString());
            LambdaUpdateWrapper<ProcessTask> processTaskLambdaUpdateWrapper = Wrappers.lambdaUpdate();
            processTaskLambdaUpdateWrapper.eq(ProcessTask::getProcessId, process.getProcessId())
                    .eq(ProcessTask::getProcessNodeKey, process.getCurrentNodeKey())
                    .ne(ProcessTask::getProcessTaskId, processTask.getProcessTaskId());
            processTaskMapper.update(processTaskForUpdateValue, processTaskLambdaUpdateWrapper);
            implementProcess(process.getProcessId());
            return true;
        } else {
            //将当前任务标记为已完成，查询是否还有未完成的任务，全部完成后执行下一个任务
            LambdaQueryWrapper<ProcessTask> taskLambdaQueryWrapper = Wrappers.lambdaQuery();
            taskLambdaQueryWrapper.eq(ProcessTask::getProcessId, process.getProcessId())
                    .eq(ProcessTask::getProcessNodeKey, processTask.getProcessNodeKey())
                    .ne(ProcessTask::getProcessTaskId, processTask.getProcessTaskId());
            List<ProcessTask> processTasks = processTaskMapper.selectList(taskLambdaQueryWrapper);
            if (processTasks.size() == 0 || processTasks == null) {
                implementProcess(process.getProcessId());
            }
        }
        return true;
    }

    @Override
    public Boolean rejectTask(String processTaskId, String operator, String opinion) {
        //当前版本等撤回逻辑为，当前节点等任何一个任务发生撤回，则该节点撤回
        ProcessTask processTask = processTaskMapper.selectById(processTaskId);
        processTask.setProcessTaskStatus(ProcessTaskStatusEnum.REJECT);
        processTask.setOpinion(opinion);
        processTask.setOperator(operator);
        processTask.setOperatorTime(new Date().toString());
        processTaskMapper.updateById(processTask);
        //将当前节点未处理等任务标记为取消
        LambdaQueryWrapper<ProcessTask> processTaskLambdaQueryWrapper = Wrappers.lambdaQuery();
        processTaskLambdaQueryWrapper.eq(ProcessTask::getProcessId, processTask.getProcessId())
                .eq(ProcessTask::getProcessNodeKey, processTask.getProcessNodeKey())
                .eq(ProcessTask::getProcessTaskStatus, ProcessTaskStatusEnum.CREATE);
        List<ProcessTask> processTasks = processTaskMapper.selectList(processTaskLambdaQueryWrapper);
        for (ProcessTask e : processTasks) {
            e.setProcessTaskStatus(ProcessTaskStatusEnum.CANCEL);
            e.setOperatorTime(new Date().toString());
            processTaskMapper.updateById(e);
        }
        //修改当前流程实例的当前节点信息为上个节点
        Process process = processMapper.selectById(processTask.getProcessId());
        LambdaQueryWrapper<ProcessNode> processNodeLambdaQueryWrapper = Wrappers.lambdaQuery();
        processNodeLambdaQueryWrapper.eq(ProcessNode::getProcessTemplateKey, process.getProcessTemplateKey()).eq(ProcessNode::getNextProcessNodeKey, process.getCurrentNodeKey());
        ProcessNode preNode = processNodeMapper.selectOne(processNodeLambdaQueryWrapper);
        process.setCurrentNodeKey(preNode.getProcessNodeKey());
        process.setCurrentNodeName(preNode.getProcessNodeName());
        process.setCurrentNodeDistributeType(preNode.getNodeDistributeType());
        process.setCurrentNodeExecuteType(preNode.getNodeExecuteType());
        process.setProcessStatus(ProcessStatusEnum.RUN);
        //如果上个节点不是开始节点则创建该节点等流程任务
        if (!preNode.getProcessNodeKey().equals("start")) {
            releaseTask(process.getProcessId(), preNode.getActors(), preNode.getProcessNodeId(), preNode.getProcessNodeKey(), preNode.getProcessNodeName(), preNode.getFormUrl(), "此处是执行任务的url地址", "此处是驳回任务的url地址");
        }
        processMapper.updateById(process);
        return true;
    }
}
