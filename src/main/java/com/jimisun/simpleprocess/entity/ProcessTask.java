package com.jimisun.simpleprocess.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程任务
 * 由task||branch_task节点创建的流程任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "process_task")
public class ProcessTask {

    /**
     * 流程任务ID
     */
    @TableId(type = IdType.INPUT)
    private String processTaskId;

    /**
     * 所属流程节点key
     */
    private String processNodeKey;


    /**
     * 所属节点名称
     */
    private String processNodeName;

    /**
     * 所属流程ID
     */
    private String processId;

    /**
     * 所属流程模板Key
     */
    private String processTemplateKey;

    /**
     * 流程任务执行者 当节点定义的派发类型为GROUP时候 此处存储的是GROUP名称 如果是USER类型 此处存储的是USER名称
     */
    private String processTaskActor;

    /**
     * 流程任务实际操作人 必须为USER的唯一标识
     */
    private String processTaskOperation;

    /**
     * 流程任务的表单地址
     */
    private String processTaskFormUrl;

    /**
     * 流程全局业务数据快照 当发生驳回/跳转节点时候可选择是否回滚全局业务数据
     */
    private String processBusDataSnapshot;

    /**
     * 流程任务的处理意见
     */
    private String processTaskOpinion;


    /**
     * 流程任务节点类型
     */
    private String processTaskNodeType;

    /**
     * 流程任务状态 ref: ProcessTaskStatusEnum
     */
    private String processTaskStatus;

    /**
     * 流程任务创建时间
     */
    private String processTaskCreateTime;

    /**
     * 流程任务关闭时间  当流程为TREATED【已处理】或者 REGECTED【驳回】的时候此属性才有值
     */
    private String processTaskCloseTime;

    /**
     * 流程任务关注释
     */
    private String processTaskRemark;



}
