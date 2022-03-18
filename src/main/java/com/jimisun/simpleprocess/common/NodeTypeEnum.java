package com.jimisun.simpleprocess.common;

/**
 * 节点类型枚举
 */
public interface NodeTypeEnum {

    /**
     * 开始节点
     * 主要作用 ： 标志作用，找到下个流程节点
     */
    String START = "start";

    /**
     * 结束节点
     * 主要作用 ： 标志作用
     */
    String END = "end";

    /**
     * 普通任务
     * 主要作用 ： 创建用户任务
     */
    String TASK = "task";

    /**
     * 分支任务
     * 主要作用 ： 创建用户分支任务
     */
    String BRANCH_TASK = "branch_task";

    /**
     * 开始网关
     * 主要作用： 根据条件/脚本创建分支任务
     */
    String OPEN_GATEWAY = "open_gateway";

    /**
     * 关闭网关
     * 主要作用 ： 标志作用
     */
    String CLOSE_GATEWAY = "close_gateway";

}
