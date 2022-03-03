package com.jimisun.simpleprocess.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.util.ScriptException;


import java.util.Iterator;
import java.util.Map;

public class GroovyUtil {


    /**
     * eval函数
     *
     * @param script groovy脚本
     * @param textMap 条件入参
     * @return
     */
    public static Object eval(String script, Map<String, Object> textMap) {
        try {
            Binding binding = new Binding();
            if (ObjectUtil.isNotEmpty(textMap)){
                for (Iterator<String> iter = textMap.keySet().iterator(); iter.hasNext(); ) {
                    String key = iter.next();
                    binding.setVariable(key, textMap.get(key));
                }
            }
            GroovyShell shell = new GroovyShell(binding);
            return shell.evaluate(script);
        } catch (Exception e) {
            try {
                throw new ScriptException("groovy脚本执行异常", e);
            } catch (ScriptException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    public static void main(String[] args) {
        Object eval = eval("'hello'.split(',')", null);
        System.out.println(eval);
    }

}
