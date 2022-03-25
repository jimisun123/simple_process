package com.jimisun.simpleprocess.common;

/**
 * 流程实例状态枚举类
 */
public interface ProcessStatusEnum {

    /**
     * 正在执行
     */
    String RUN = "run";

    /**
     * 暂停
     */
    String PAUSE = "pause";

    /**
     * 关闭
     */
    String CLOSE = "close";

    /**
     * 结束
     */
    String END = "end";


}
