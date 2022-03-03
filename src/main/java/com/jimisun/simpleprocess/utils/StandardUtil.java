package com.jimisun.simpleprocess.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.jimisun.simpleprocess.entity.Process;

import java.util.Date;
import java.util.Map;

/**
 * 标准Util
 */
public class StandardUtil {

    public static String getStandardId() {
        return IdUtil.simpleUUID();
    }

    public static String getStandarStrTime() {
        Date date = new Date();
        String format = DateUtil.format(date, "yyyy/MM/dd HH:mm:ss");
        return format;
    }

    public static Process setProcessAttribute(Process process, Map map) {
        String processAttributeData = process.getProcessAttributeData();
        if (ObjectUtil.isNotEmpty(processAttributeData)) {
            Map oldMap = JSONUtil.toBean(processAttributeData, Map.class);
            oldMap.putAll(map);
            process.setProcessAttributeData(JSONUtil.toJsonStr(oldMap));
        }else {
            process.setProcessAttributeData(JSONUtil.toJsonStr(map));
        }
        return process;
    }
}
