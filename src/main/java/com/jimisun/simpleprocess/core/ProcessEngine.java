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
