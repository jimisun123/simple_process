package com.jimisun.simpleprocess.core;


import java.util.Map;

/**
 * 流程引擎接口
 */
public interface ProcessEngine {


    /**
     * 创建流程实例
     *
     * @param processTemplateKey
     * @param processBusKey
     * @param processBusData
     * @param processAttributeData
     * @return
     */
    public Boolean createProcess(String processTemplateKey, String processBusKey, Map processBusData, Map processAttributeData);


    /**
     * 修改流程实例状态
     * 暂停流程 ： 暂停流程会将流程实例对状态该为暂停，保持其流程任务
     * 关闭流程 ： 关闭流程会将流程实例对状态该为关闭，并关闭其流程任务
     *
     * @param processId
     * @param operator
     * @param opinion
     * @return
     */
    Boolean changeProcessStatus(String processId, String operator, String opinion, String status);


    /**
     * 完成流程任务
     *
     * @param processTaskId
     * @param operator
     * @param opinion
     * @return
     */
    public Boolean executeProcessTask(String processTaskId, String operator, String opinion);


    /**
     * 驳回任务
     *
     * @param processTaskId
     * @param operator
     * @param opinion
     * @return
     */
    public Boolean rejectTask(String processTaskId, String operator, String opinion);


}
