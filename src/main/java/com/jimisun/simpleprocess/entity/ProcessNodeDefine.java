package com.jimisun.simpleprocess.entity;

import lombok.Data;

/**
 * 流程节点定义
 */
@Data
public class ProcessNodeDefine {

    /**
     * 节点类型 ref:NodeTypeEnum
     */
    private String nodeType;

    /**
     * 节点key
     */
    private String nodeKey;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点分发类型 GROUP USER
     */
    private String nodeDistributeType;

    /**
     * 节点执行类型 ANY ALL ref: NodeExecuteTypeEnum
     */
    private String nodeTaskExecuteType;

    /**
     * 节点执行者
     */
    private String nodeActors;

    /**
     * 节点表单地址
     */
    private String nodeFormUrl;



    /**
     * 开始网关脚本
     */
    private String openGatewayScript;

    /**
     * 下个节点KEY
     */
    private String nextNodeKey;

    /**
     * 下个节点名称
     */
    private String nextNodeName;


}
