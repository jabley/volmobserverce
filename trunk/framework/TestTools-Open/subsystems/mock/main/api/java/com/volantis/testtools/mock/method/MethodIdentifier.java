/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

import com.volantis.testtools.mock.MockObject;
import junit.framework.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * A class that identifies a method within a mock object class.
 */
public class MethodIdentifier {

    /**
     * Get a method identifier.
     *
     * @param mockClass      The mock object class on which the method is
     *                       defined.
     * @param mockedClass    The class that is being mocked up, may be null in
     *                       which case it is inferred.
     * @param name           The name of the method.
     * @param parameterTypes The parameters of the method, null implies that it
     *                       has no parameters.
     * @return A new method identifier instance.
     */
    public static MethodIdentifier getMethodIdentifier(
            Map signature2Identifier,
            Class mockClass,
            Class mockedClass,
            String name,
            Class[] parameterTypes,
            String[] parameterNames) {

        // Make sure that the class is a mock object.
        if (!MockObject.class.isAssignableFrom(mockClass)) {
            throw new IllegalArgumentException
                    ("Class " + mockClass + " must be a MockObject");
        }

        if (mockedClass == null) {
            throw new IllegalArgumentException("mockedClass cannot be null");
        }

        Method method;
        if (parameterTypes == null) {
            method = getUniqueMethod(mockedClass, name);

            if (parameterNames != null) {
                parameterTypes = method.getParameterTypes();
                if (parameterTypes.length < parameterNames.length) {
                    Assert.fail("Too many named parameters");
                }
            }

        } else {
            // Make sure that the method actually exists.

            // First look for public methods in either this class, or inherited
            // from either an interface, or a super class.
            method = getPublicInheritedMethod(mockedClass, name,
                    parameterTypes);

            // If it could not be found then look for package, private, or
            // protected methods in this class, or its super classes.
            for (Class clazz = mockedClass; clazz != null && method == null;
                 clazz = clazz.getSuperclass()) {

                method = getDeclaredMethod(clazz, name, parameterTypes);
            }

            if (method == null) {
                Method[] methods = mockedClass.getDeclaredMethods();
                System.out.println("Declared Methods for " + mockedClass);
                printMethodDescriptions(mockedClass, methods);

                methods = mockedClass.getMethods();
                System.out.println("Methods for " + mockedClass);
                printMethodDescriptions(mockedClass, methods);

                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class parameterType = parameterTypes[i];
                    if (i != 0) {
                        buffer.append(", ");
                    }
                    buffer.append(parameterType);
                }
                throw new IllegalArgumentException
                        ("Method " + name + "(" + buffer + ") does not" +
                                " exist in class " + mockedClass);
            }
        }

        MethodIdentifier identifier =
                new MethodIdentifier(method, parameterNames);

        StringBuffer buffer = new StringBuffer();
        buffer.append(method.getName()).append("(");
        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameterType = parameterTypes[i];
            if (i > 0) {
                buffer.append(",");
            }
            buffer.append(parameterType.getName());
        }
        buffer.append(")");
        String signature = buffer.toString();

        if (signature2Identifier.containsKey(signature)) {
            throw new IllegalStateException(
                    "Identifier for " + signature + " already exists");
        }
        signature2Identifier.put(signature, identifier);
        
        return identifier;
    }

    /**
     * Get a method identifier with the specified signature.
     *
     * @param signature2Identifier The map to search.
     * @param signature The signature to use.
     * @return A new method identifier instance.
     */
    public static MethodIdentifier getMethodIdentifier(
            Map signature2Identifier,
            String signature) {

        MethodIdentifier identifier =
                (MethodIdentifier) signature2Identifier.get(signature);
        if (identifier == null) {
            throw new IllegalArgumentException(
                    "Could not find identifier for " + signature);
        }
        return identifier;
    }

    /**
     * Get a method identifier for a method uniquely identified by name.
     * @param method The method for which this identifier is required.
     * @return A new method identifier instance.
     */
    public static MethodIdentifier getMethodIdentifier(Method method) {
        return new MethodIdentifier(method, null);
    }

    private static Method getDeclaredMethod(
            Class clazz, String name, Class[] parameterTypes) {

        Method method;
        try {
            method = clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            method = null;
        }

        return method;
    }

    private static Method getPublicInheritedMethod(
            Class clazz, String name, Class[] parameterTypes) {
        Method method;
        try {
            method = clazz.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            method = null;
        }

        return method;
    }

    private static void printMethodDescriptions(Class clazz, Method[] methods) {
        for (int i = 0; i < methods.length; i++) {
            Method declared = methods[i];
            Class[] parameters = declared.getParameterTypes();
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < parameters.length; j++) {
                Class parameter = parameters[j];
                if (j != 0) {
                    buffer.append(", ");
                }
                buffer.append(parameter);
            }

            String source = "";
            final Class declaringClass = declared.getDeclaringClass();
            if (declaringClass != clazz) {
                source = " from " + declaringClass;
            }

            System.out.println("    " + declared.getName() +
                    "(" + buffer + ")" + source);
        }
    }

    /**
     * Get the unique method with the specified name.
     *
     * <p>If the method does not exist, or is not unique then fail.</p>
     *
     * @param mockedClass The class being mocked.
     * @param name        The name of the method.
     * @return The <code>Method</code>.
     */
    private static Method getUniqueMethod(Class mockedClass, String name) {
        // Make sure that the method actually exists.
        Method[] methods = mockedClass.getMethods();
        Method matched = null;
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (method.getName().equals(name)) {
                if (matched != null) {
                    throw new IllegalArgumentException
                            ("Ambiguous method " + name + " in class " +
                                    mockedClass);
                }
                matched = method;
            }
        }

        if (matched == null) {
            throw new IllegalArgumentException
                    ("Method " + name + " does not exist in class " +
                            mockedClass);
        }
        return matched;
    }

    /**
     * The method object.
     */
    private final Method method;

    /**
     * The parameters of the method.
     */
    private final String[] parameterNames;

    /**
     * Initialise a new method identifier.
     *
     * @param method         The method.
     * @param parameterNames The names of the parameters, may be null as they
     *                       are not always available.
     */
    private MethodIdentifier(Method method, String[] parameterNames) {

        this.method = method;
        this.parameterNames = parameterNames;
    }

    public Class getDeclaringClass() {
        return method.getDeclaringClass();
    }

    public String getName() {
        return method.getName();
    }

    public Class[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public Class getReturnType() {
        return method.getReturnType();
    }

    /**
     * Get the index of a parameter using its name and check against the type.
     *
     * @param name          The name of the parameter.
     * @param expectedClass The expected type of the parameter.
     * @return The index of the parameter.
     * @throws IllegalArgumentException If the parameter does not exist, or is
     *                                  not of the expected class.
     */
    public int getParameterIndexByName(String name, Class expectedClass) {
        Class[] parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            if (name.equals(parameterName)) {
                Class actualClass = parameterTypes[i];
                if (actualClass != expectedClass) {
                    throw new IllegalArgumentException
                            ("Expected class " + expectedClass
                                    + " for parameter " + name
                                    + " but found class " + actualClass
                                    + " at position " + i);
                }
                return i;
            }
        }

        throw new IllegalArgumentException("Parameter " + name + " not found");
    }

    /**
     * Get the index of a paremeter using its type.
     *
     * <p>It will return the index of the first parameter with the specified
     * type.</p>
     *
     * @param expectedClass The expected type of the parameter.
     * @return The index of the parameter.
     * @throws IllegalArgumentException If the paremeter of the specified type
     *                                  does not exist.
     */
    public int getParameterIndexByType(Class expectedClass) {
        Class[] parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterNames.length; i++) {
            Class actualClass = parameterTypes[i];
            if (actualClass == expectedClass) {
                return i;
            }
        }

        throw new IllegalArgumentException
                ("Parameter with class " + expectedClass + " not found");
    }

    /**
     * Implemented to make sure that this identifier can be compared against
     * others.
     *
     * todo: Look to see whether this is needed, should two identifiers be equal
     * only if they are the same object.
     *
     * @param o The other object.
     * @return True if it is equal and false if it is not.
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodIdentifier)) return false;

        final MethodIdentifier methodIdentifier = (MethodIdentifier) o;

        if (!getDeclaringClass().equals(methodIdentifier.getDeclaringClass()))
            return false;
        if (!getName().equals(methodIdentifier.getName())) return false;
        if (!Arrays.equals(getParameterTypes(),
                methodIdentifier.getParameterTypes())) return false;

        return true;
    }

    // Javadoc inherited.
    public int hashCode() {
        int result;
        result = getDeclaringClass().hashCode();
        result = 29 * result + getName().hashCode();
        Class[] parameterTypes = getParameterTypes();
        if (parameterTypes != null) {
            for (int i = 0; i < parameterTypes.length; i++) {
                Class parameter = parameterTypes[i];
                result = 29 * result + parameter.hashCode();
            }
        }
        return result;
    }

    // Javadoc inherited.
    public String toString() {
        StringBuffer buffer = new StringBuffer(100);
        buffer.append(
                MethodHelper.getShorterName(getDeclaringClass().getName()))
                .append("#");
        buffer.append(getName()).append("(");
        Class[] parameterTypes = getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class parameter = parameterTypes[i];
            String name = MethodHelper.getShorterName(parameter.getName());
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(name);
        }
        buffer.append(")");

        return buffer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 14-Jun-05	7997/1	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 20-May-05	8277/3	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3514/4	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 06-Apr-04	3703/3	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
