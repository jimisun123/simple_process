package com.jimisun.simpleprocess.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "process_task")
public class ProcessTask {

    /**
     * 流程任务id
     */
    @TableId(type = IdType.INPUT)
    private String processTaskId;


    /**
     * 流程Id
     */
    private String processId;
    /**
     * 流程节点ID
     */
    private String processNodeId;

    /**
     * 任务节点key
     */
    private String processNodeKey;

    /**
     * 任务节点名称
     */
    private String processNodeName;

    /**
     * 任务归属
     */
    private String ascription;

    /**
     * 任务实际操作人
     */
    private String operator;
    /**
     * 任务实际操作时间
     */
    private String operatorTime;

    /**
     * 任务节点状态 1 创建  2 完成  3 取消  4驳回
     */
    private int processTaskStatus;

    /**
     * 意见
     */
    private String opinion;

    /**
     * 任务数据
     */
    private String taskData;

    /**
     * 任务表单地址
     */
    private String formUrl;

    /**
     * 执行任务地址
     */
    private String taskAgreeAddress;

    /**
     * 驳回任务地址
     */
    private String taskRejectAddress;

    /**
     * 任务创建时间
     */
    private String taskCreateTime;
}
