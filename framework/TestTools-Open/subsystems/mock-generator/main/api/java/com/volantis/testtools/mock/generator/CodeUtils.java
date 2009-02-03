/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package com.volantis.testtools.mock.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods.
 */
public class CodeUtils {

    private final static Map wrappers = new HashMap();

    static {
        wrappers.put("boolean", "java.lang.Boolean");
        wrappers.put("char", "java.lang.Character");
        wrappers.put("byte", "java.lang.Byte");
        wrappers.put("short", "java.lang.Short");
        wrappers.put("int", "java.lang.Integer");
        wrappers.put("long", "java.lang.Long");
        wrappers.put("float", "java.lang.Float");
        wrappers.put("double", "java.lang.Double");
    }

    public static String wrapValue(String name, String type) {
        if (wrappers.containsKey(type)) {
            StringBuffer result = new StringBuffer();

            result.append("new ");
            result.append(wrappers.get(type));
            result.append('(');
            result.append(name);
            result.append(')');
            return result.toString();
        } else {
            return name;
        }
    }

    public static String unwrapValue(String name, String type) {
        StringBuffer result = new StringBuffer();

        if (wrappers.containsKey(type)) {
            result.append("((");
            result.append(wrappers.get(type));
            result.append(')');
            result.append(name);
            result.append(").");
            result.append(type);
            result.append("Value()");
        } else {
            result.append('(');
            result.append(type);
            result.append(')');
            result.append(name);
        }
        return result.toString();
    }

    public static String fixInnerClassName(final String className) {
        return className.replaceAll("\\$", "\\.");
    }
}
