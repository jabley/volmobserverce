/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.ModelBuilder;
import com.thoughtworks.qdox.model.ClassLibrary;
import com.thoughtworks.qdox.model.DefaultDocletTagFactory;
import com.thoughtworks.qdox.parser.structs.ClassDef;
import com.thoughtworks.qdox.parser.structs.FieldDef;
import com.thoughtworks.qdox.parser.structs.MethodDef;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Collections;

/**
 * Work around http://jira.codehaus.org/browse/QDOX-129
 */
public class QDox129 {

    private DefaultDocletTagFactory docletTagFactory;
    private final ClassLoader dependencyClassLoader;

    public QDox129(final ClassLoader dependencyClassLoader) {
        this.dependencyClassLoader = dependencyClassLoader;
        docletTagFactory = new DefaultDocletTagFactory();
    }

    public JavaClass recreateBinary(JavaClass naffBinaryClass) {
        return recreateBinary(naffBinaryClass, null);
    }

    public JavaClass recreateBinary(JavaClass naffBinaryClass,
                                    String packageName) {
        String name = naffBinaryClass.getFullyQualifiedName();

        if (packageName != null) {
            if (name.indexOf('.') != -1) {
                throw new IllegalStateException(
                    "Class " + name + " is already in a package");
            }
            name = packageName + "." + name; 
        }

        ClassLibrary classLibrary =
                naffBinaryClass.getSource().getClassLibrary();
        Class clazz = classLibrary.getClass(name);

        // First see if the class exists at all.
        if (clazz == null) {
            try {
                clazz = getClass().getClassLoader().loadClass(name);
            } catch (ClassNotFoundException e) {
                // do nothing
            }
            if (clazz == null) {
                try {
                    clazz = dependencyClassLoader.loadClass(name);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Cannot find class " + name);
                }
            }
        }

        // Create a new builder and mimic the behaviour of the parser.
        // We're getting all the information we need via reflection instead.
        ModelBuilder binaryBuilder =
                new ModelBuilder(classLibrary, docletTagFactory);

        // Set the package name and class name
        if (packageName == null) {
            binaryBuilder.addPackage(getPackageName(name));
        } else {
            binaryBuilder.addPackage(packageName);
        }

        ClassDef classDef = new ClassDef();
        classDef.name = getClassName(name);

        // Set the extended class and interfaces.
        Class[] interfaces = clazz.getInterfaces();
        if (clazz.isInterface()) {
            // It's an interface
            classDef.type = ClassDef.INTERFACE;
            for (int i = 0; i < interfaces.length; i++) {
                Class anInterface = interfaces[i];
                classDef.extendz.add(anInterface.getName());
            }
        } else {
            // It's a class
            for (int i = 0; i < interfaces.length; i++) {
                Class anInterface = interfaces[i];
                classDef.implementz.add(anInterface.getName());
            }
            Class superclass = clazz.getSuperclass();
            if (superclass != null) {
                classDef.extendz.add(superclass.getName());
            }
        }

        addModifiers(classDef.modifiers, clazz.getModifiers());

        binaryBuilder.beginClass(classDef);

        // add the constructors
        //
        // This also adds the default constructor if any which is different
        // to the source code as that does not create a default constructor
        // if no constructor exists.
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (int i = 0; i < constructors.length; i++) {
            addMethodOrConstructor(constructors[i], binaryBuilder);
        }

        // add the methods
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            addMethodOrConstructor(methods[i], binaryBuilder);
        }

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            addField(fields[i], binaryBuilder);
        }

        binaryBuilder.endClass();
        JavaSource binarySource = binaryBuilder.getSource();
        // There is always only one class in a "binary" source.
        JavaClass result = binarySource.getClasses()[0];
        return result;
    }

    private void addModifiers(Set set, int modifier) {
        String modifierString = Modifier.toString(modifier);
        for (StringTokenizer stringTokenizer = new StringTokenizer(modifierString); stringTokenizer.hasMoreTokens();) {
            set.add(stringTokenizer.nextToken());
        }
    }
    private void addField(Field field, ModelBuilder binaryBuilder) {
        FieldDef fieldDef = new FieldDef();
        Class fieldType = field.getType();
        fieldDef.name = field.getName();
        fieldDef.type = getTypeName(fieldType);
        fieldDef.dimensions = getDimension(fieldType);
        binaryBuilder.addField(fieldDef);
    }

    private void addMethodOrConstructor(Member member, ModelBuilder binaryBuilder) {
        MethodDef methodDef = new MethodDef();
        // The name of constructors are qualified. Need to strip it.
        // This will work for regular methods too, since -1 + 1 = 0
        int lastDot = member.getName().lastIndexOf('.');
        methodDef.name = member.getName().substring(lastDot + 1);

        addModifiers(methodDef.modifiers, member.getModifiers());
        Class[] exceptions;
        Class[] parameterTypes;
        if (member instanceof Method) {
            methodDef.constructor = false;

            // For some stupid reason, these methods are not defined in Member,
            // but in both Method and Construcotr.
            exceptions = ((Method) member).getExceptionTypes();
            parameterTypes = ((Method) member).getParameterTypes();

            Class returnType = ((Method) member).getReturnType();
            methodDef.returns = getTypeName(returnType);
            methodDef.dimensions = getDimension(returnType);

        } else {
            methodDef.constructor = true;

            exceptions = ((Constructor) member).getExceptionTypes();
            parameterTypes = ((Constructor) member).getParameterTypes();
        }
        for (int j = 0; j < exceptions.length; j++) {
            Class exception = exceptions[j];
            methodDef.exceptions.add(exception.getName());
        }
        for (int j = 0; j < parameterTypes.length; j++) {
            FieldDef param = new FieldDef();
            Class parameterType = parameterTypes[j];
            param.name = "p" + j;
            param.type = getTypeName(parameterType);
            param.dimensions = getDimension(parameterType);
            methodDef.params.add(param);
        }
        binaryBuilder.addMethod(methodDef);
    }

    private static final int getDimension(Class c) {
        return c.getName().lastIndexOf('[') + 1;
    }

    private static String getTypeName(Class c) {
        return c.getComponentType() != null ? c.getComponentType().getName() : c.getName();
    }

    private String getPackageName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot == -1 ? "" : fullClassName.substring(0, lastDot);
    }

    private String getClassName(String fullClassName) {
        int lastDot = fullClassName.lastIndexOf('.');
        return lastDot == -1 ? fullClassName : fullClassName.substring(lastDot + 1);
    }
}
