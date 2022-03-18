package com.jimisun.simpleprocess;

import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilationFailedException;

import javax.script.*;
import java.io.IOException;
import java.util.Date;

/**
 * groovy 测试类
 */
public class Test1 {

    public static void main(String[] args) throws IOException, ScriptException, NoSuchMethodException {


        testGroovy1();
    }


    // 调用evaluate方法直接执行一段Groovy
    public static void testGroovy1() throws CompilationFailedException, IOException {
        GroovyShell groovyShell = new GroovyShell();
        groovyShell.evaluate("println 'My First Groovy shell.'");
    }



}
