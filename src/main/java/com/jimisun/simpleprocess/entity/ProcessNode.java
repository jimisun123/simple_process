package com.jimisun.simpleprocess.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程节点信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "process_node")
public class ProcessNode {
    /**
     * 流程节点ID
     */
    @TableId(type = IdType.INPUT)
    private String processNodeId;
    /**
     * 流程节点key
     */
    private String processNodeKey;

    /**
     * 流程模板key
     */
    private String processTemplateKey;

    /**
     * 流程节点名称
     */
    private String processNodeName;

    /**
     * 下个流程节点key
     */
    private String nextProcessNodeKey;

    /**
     * 节点执行类型  ANY  ALL
     */
    private String nodeExecuteType;

    /**
     * 节点分发类型  GROUP USER
     */
    private String nodeDistributeType;

    /**
     * 节点执行者
     */
    private String actors;

    /**
     * 节点扩展属性
     */
    private String ext;
}
