package com.jimisun.simpleprocess.common;

/**
 * 流程任务状态枚举类
 */
public interface ProcessTaskStatusEnum {

    /**
     * 未处理
     */
    String UNTREATED = "untreated";

    /**
     * 已处理
     */
    String TREATED = "treated";

    /**
     * 驳回
     */
    String REGECTED = "rejected";


    /**
     * 取消
     */
    String CANCEL = "cancel";


}
