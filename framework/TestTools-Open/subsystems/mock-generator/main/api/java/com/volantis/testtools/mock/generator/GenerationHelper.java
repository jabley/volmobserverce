/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.JavaClass;
import com.volantis.testtools.mock.generator.model.MockableParameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class GenerationHelper {


    private static final String EQUALS_SIGNATURE = "equals(java.lang.Object)";
    private static final String HASH_CODE_SIGNATURE = "hashCode()";
    private static final String TO_STRING_SIGNATURE = "toString()";
    private static final String CLONE_SIGNATURE = "clone()";
    private static final String FINALIZE_SIGNATURE = "finalize()";

    private static final Map OBJECT_METHOD_2_ACTION;
    private static final Set SPECIAL_METHODS;
    static {
        IgnoreObjectMethod ignoreObjectMethod = new IgnoreObjectMethod();

        OBJECT_METHOD_2_ACTION = new HashMap();
        OBJECT_METHOD_2_ACTION.put(EQUALS_SIGNATURE, new SimpleObjectMethod(
                "com.volantis.testtools.mock.generated.MockObjectBase.EQUALS_METHOD",
                "boolean",
                null));
        OBJECT_METHOD_2_ACTION.put(HASH_CODE_SIGNATURE, new SimpleObjectMethod(
                "com.volantis.testtools.mock.generated.MockObjectBase.HASH_CODE_METHOD", "int",
                null));
//                          new ParameterLessObjectMethod(
//                                  "return System.identityHashCode(this);",
//                                  "configuration.hashCodeShouldCheckExpectations()"));
        OBJECT_METHOD_2_ACTION.put(TO_STRING_SIGNATURE, new SimpleObjectMethod(
                "com.volantis.testtools.mock.generated.MockObjectBase.TO_STRING_METHOD", "java.lang.String",
                null));
//                          new ParameterLessObjectMethod(
//                                  "return _getIdentifier();",
//                                  "configuration.toStringShouldCheckExpectations()"));
        OBJECT_METHOD_2_ACTION.put(CLONE_SIGNATURE, new SimpleObjectMethod(
                "com.volantis.testtools.mock.generated.MockObjectBase.CLONE_METHOD", "java.lang.Object",
                null));
//                          new SimpleObjectMethod());
        OBJECT_METHOD_2_ACTION.put(FINALIZE_SIGNATURE, ignoreObjectMethod);

        SPECIAL_METHODS = new HashSet();
        SPECIAL_METHODS.add("equals");
        SPECIAL_METHODS.add("hashCode");
        SPECIAL_METHODS.add("toString");
        SPECIAL_METHODS.add("clone");
        SPECIAL_METHODS.add("finalize");
    }

    public static final CallUpdaterInfo ANY_RETURN_TYPE = builtIn("Any");

    private static final Map TYPE_2_CALL_UPDATER_INFO;

    static {
        TYPE_2_CALL_UPDATER_INFO = new HashMap();

        TYPE_2_CALL_UPDATER_INFO.put("boolean", builtIn("Boolean"));
        TYPE_2_CALL_UPDATER_INFO.put("byte", builtIn("Byte"));
        TYPE_2_CALL_UPDATER_INFO.put("char", builtIn("Char"));
        TYPE_2_CALL_UPDATER_INFO.put("double", builtIn("Double"));
        TYPE_2_CALL_UPDATER_INFO.put("float", builtIn("Float"));
        TYPE_2_CALL_UPDATER_INFO.put("int", builtIn("Int"));
        TYPE_2_CALL_UPDATER_INFO.put("long", builtIn("Long"));
        TYPE_2_CALL_UPDATER_INFO.put("java.lang.Object", builtIn("Object"));
        TYPE_2_CALL_UPDATER_INFO.put("short", builtIn("Short"));
        TYPE_2_CALL_UPDATER_INFO.put("void", builtIn("Void"));
        TYPE_2_CALL_UPDATER_INFO.put("java.lang.String", builtIn("String"));
    }

    private static CallUpdaterInfo builtIn(String suffix) {
        return new CallUpdaterInfo(
                "com.volantis.testtools.mock.method.CallUpdaterReturns" + suffix,
                "_mockFactory.createReturns" + suffix + "(_call)");
    }

    /**
     * Checks to see whether the method overrides a method on {@link Object}
     * and if so how it is handled.
     *
     * @param method The method to check.
     *
     * @return An {@link ObjectMethodAction} instance, or null if it is not an
     * object method.
     */
    public static ObjectMethodAction getObjectMethodAction(JavaMethod method) {

        String name = method.getName();
        if (!SPECIAL_METHODS.contains(name)) {
            return null;
        }

        JavaParameter[] parameters = method.getParameters();
        if (parameters.length > 1) {
            return null;
        }

        String bindSignature = getBindSignature(method);

        return getObjectMethodActionBySignature(bindSignature);
    }

    /**
     * Checks to see whether the method overrides a method on {@link Object} and
     * if so how it is handled.
     *
     * @param bindSignature The bind signature, includes name, parameter types
     *                      but not return types, parameter names, or
     *                      modifiers.
     * @return An {@link ObjectMethodAction} instance, or null if it is not an
     *         object method.
     */
    public static ObjectMethodAction getObjectMethodActionBySignature(
            String bindSignature) {

        return (ObjectMethodAction) OBJECT_METHOD_2_ACTION.get(bindSignature);
    }

    public static String getBindSignature(JavaMethod method) {
        String name = method.getName();
        JavaParameter[] parameters = method.getParameters();
        StringBuffer buffer = new StringBuffer();
        buffer.append(name).append("(");
        for (int i = 0; i < parameters.length; i++) {
            JavaParameter parameter = parameters[i];
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(
                CodeUtils.fixInnerClassName(parameter.getType().toString()));
        }
        buffer.append(")");
        return buffer.toString();
    }

    public static CallUpdaterInfo getBuiltInCallUpdaterInfo(
            String typeAsString) {
        return (CallUpdaterInfo) TYPE_2_CALL_UPDATER_INFO.get(typeAsString);
    }

    public static StringBuffer getDimensionsAsString(Type type) {
        int d = type.getDimensions();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < d; i += 1) {
            buffer.append("[]");
        }
        return buffer;
    }

    public static Set getThrowables(JavaMethod javaMethod) {
        Set throwables = new HashSet();
        Type[] exceptions = javaMethod.getExceptions();
        for (int j = 0; j < exceptions.length; j++) {
            JavaClass throwable = exceptions[j].getJavaClass();
            throwables.add(throwable);
        }
        return throwables;
    }


    public static List getParameters(JavaMethod method) {
        List parameters;
        JavaParameter[] javaParameters = method.getParameters();
        if (javaParameters.length == 0) {
            parameters = null;
        } else {
            parameters = new ArrayList();
            for (int i = 0; i < javaParameters.length; i++) {
                JavaParameter javaParameter = javaParameters[i];
                parameters.add(new MockableParameter(javaParameter));
            }
        }
        return parameters;
    }

    public static String uniqueMethodIdentifer(
            String name, List parameters) {

        StringBuffer sbuf = new StringBuffer();
        sbuf.append(name);

        if (parameters == null || parameters.size() == 0) {
            sbuf.append("_");
        } else {
            for (Iterator i = parameters.iterator(); i.hasNext();) {
                MockableParameter parameter = (MockableParameter) i.next();

                // Add a separator between the name and the type.
                sbuf.append("_");

                String componentName = parameter.getComponentName();
                int length = componentName.length();
                for (int j = 0; j < length; j += 1) {
                    char c = componentName.charAt(j);
                    if (c == '_') {
                        // Replace any _ in the type with __
                        sbuf.append("__");
                    } else if (c == '.') {
                        // Replace any . in the type with _
                        sbuf.append("_");
                    } else {
                        sbuf.append(c);
                    }
                }

                // Add an indicator of the number of dimensions.
                sbuf.append(parameter.getDimensions());
            }
        }

        sbuf.append("_METHOD");

        return sbuf.toString();
    }
}
