package com.jimisun.simpleprocess.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程实例
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "process")
public class Process {

    /**
     * 流程实例ID
     */
    @TableId(type = IdType.INPUT)
    private String processId;


    /**
     * 流程模版KEY
     */
    private String processTemplateKey;


    /**
     * 流程实例业务KEY
     */
    private String processBusKey;


    /**
     * 流程实例全局业务数据 此处存储map 也就是说后节点在存储相同的key的数据到map中会覆盖原有的数据  参考activity7
     */
    private String processBusData;

    /**
     * 流程实例全局属性数据 此处存储map 此处主要用户在task||branch_task||open_gateway类型的节点中使用
     */
    private String processAttributeData;

    /**
     * 流程实例状态  ref: ProcessStatusEnum
     */
    private String processStatus;


    /**
     * 流程开始时间
     */
    private String processOpenTime;


    /**
     * 流程结束时间
     */
    private String processCloseTime;


}
