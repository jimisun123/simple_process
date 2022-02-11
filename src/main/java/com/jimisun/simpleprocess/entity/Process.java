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
     * 流程ID
     */
    @TableId(type = IdType.INPUT)
    private String processId;

    /**
     * 业务key
     */
    private String busKey;

    /**
     * 流程模板KEY
     */
    private String processTemplateKey;

    /**
     * 当前节点KEY
     */
    private String currentNodeKey;

    /**
     * 当前节点名称
     */
    private String currentNodeName;

    /**
     * 当前节点执行类型
     */
    private String currentNodeExecuteType;

    /**
     * 当前节点分发类型
     */
    private String currentNodeDistributeType;

    /**
     * 流程状态
     */
    private Integer processStatus;

    /**
     * 流程数据
     */
    private String processData;
}
