package com.jimisun.simpleprocess.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;

import java.util.Date;

/**
 * 标准Util
 */
public class StandardUtil {

    public static String getStandardId(){
        return IdUtil.simpleUUID();
    }

    public static String getStandarStrTime(){
        Date date = new Date();
        String format = DateUtil.format(date, "yyyy/MM/dd HH:mm:ss");
        return format;
    }
}
