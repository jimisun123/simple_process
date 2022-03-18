package com.jimisun.simpleprocess.core;

import com.jimisun.simpleprocess.dao.ProcessMapper;
import com.jimisun.simpleprocess.entity.Process;
import com.jimisun.simpleprocess.utils.StandardUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 流程扩展功能
 */
@Component
public class ProcessEngineExt {

    @Resource
    private ProcessMapper processMapper;


    public void ProcessNodeCompleteExt(String processId, String nodeKey) {
        System.out.println("接收到流程节点的完成事件" + processId + "," + nodeKey);

        //一下业务为demo  实际请替换成真正业务。。。。。。。。。。。。。。。。。。
        if (nodeKey.equals("start")) {
            Process process = processMapper.selectById(processId);
            HashMap params = new HashMap();
            params.put("yysusers", "zhangsan,lisi,wangwu,zhaoliu");
            process = StandardUtil.setProcessAttribute(process, params);
            processMapper.updateById(process);
        }
    }


    public void ProcessTaskRejectCompleteExt(String processTaskId) {
        System.out.println("接收到流程任务的驳回完成事件" + processTaskId);
    }
}
