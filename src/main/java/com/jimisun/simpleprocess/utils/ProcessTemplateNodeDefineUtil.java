package com.jimisun.simpleprocess.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jimisun.simpleprocess.entity.ProcessNodeDefine;
import com.jimisun.simpleprocess.entity.ProcessTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.List;

/**
 * 流程模板节点定义工具类
 */
@Slf4j
public class ProcessTemplateNodeDefineUtil {

    /**
     * 根据当前节点名称 获取上个node节点
     *
     * @param processTemplate 流程模板实例
     * @param currentNodeKey  当前节点key
     * @return
     */
    public static ProcessNodeDefine getPreNodeDefine(ProcessTemplate processTemplate, String currentNodeKey) {
        String processTemplateNodeDefine = processTemplate.getProcessTemplateNodeDefine();
        JSONArray nodeDefineJsonArray = null;
        try {
            nodeDefineJsonArray = JSONUtil.parseArray(processTemplateNodeDefine);
        } catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace();
            log.error("流程模板定义解析失败，可能不是一个标准的json");
        }
        JSONArray objects = JSONUtil.parseArray(nodeDefineJsonArray);
        List<ProcessNodeDefine> processNodeDefines = JSONUtil.toList(objects, ProcessNodeDefine.class);

        for (ProcessNodeDefine processNodeDefine : processNodeDefines) {
            if (ObjectUtil.isNotEmpty(processNodeDefine.getNextNodeKey()) && processNodeDefine.getNextNodeKey().equals(currentNodeKey)) {
                return processNodeDefine;
            }
        }
        return null;
    }



    /**
     * 根据当前节点名称 获取下个node节点
     *
     * @param processTemplate 流程模板实例
     * @param currentNodeKey  当前节点key
     * @return
     */
    public static ProcessNodeDefine getNextNodeDefine(ProcessTemplate processTemplate, String currentNodeKey) {
        String processTemplateNodeDefine = processTemplate.getProcessTemplateNodeDefine();
        JSONArray nodeDefineJsonArray = null;
        try {
            nodeDefineJsonArray = JSONUtil.parseArray(processTemplateNodeDefine);
        } catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace();
            log.error("流程模板定义解析失败，可能不是一个标准的json");
        }
        JSONArray objects = JSONUtil.parseArray(nodeDefineJsonArray);
        List<ProcessNodeDefine> processNodeDefines = JSONUtil.toList(objects, ProcessNodeDefine.class);
        String nextNodeKey = "";
        flag:
        for (ProcessNodeDefine processNodeDefine : processNodeDefines) {
            if (processNodeDefine.getNodeKey().equals(currentNodeKey)) {
                nextNodeKey = processNodeDefine.getNextNodeKey();
                break flag;
            }
        }
        if (StrUtil.isNotBlank(nextNodeKey)) {
            flag:
            for (ProcessNodeDefine processNodeDefine : processNodeDefines) {
                if (processNodeDefine.getNodeKey().equals(nextNodeKey)) {
                    return processNodeDefine;
                }
            }
        }
        return null;
    }

    /**
     * 根据当前节点名称 获取当前node节点
     *
     * @param processTemplate 流程模板实例
     * @param currentNodeKey  当前节点key
     * @return
     */
    public static ProcessNodeDefine getNodeDefine(ProcessTemplate processTemplate, String currentNodeKey) {
        String processTemplateNodeDefine = processTemplate.getProcessTemplateNodeDefine();
        JSONArray nodeDefineJsonArray = null;
        try {
            nodeDefineJsonArray = JSONUtil.parseArray(processTemplateNodeDefine);
        } catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace();
            log.error("流程模板定义解析失败，可能不是一个标准的json");
        }
        List<ProcessNodeDefine> processNodeDefines = JSONUtil.toList(nodeDefineJsonArray, ProcessNodeDefine.class);
        for (ProcessNodeDefine processNodeDefine : processNodeDefines) {
            if (processNodeDefine.getNodeKey().equals(currentNodeKey)) {
                return processNodeDefine;
            }
        }

        return null;
    }



}
