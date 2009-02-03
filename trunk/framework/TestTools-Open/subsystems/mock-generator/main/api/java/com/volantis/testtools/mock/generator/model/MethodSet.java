/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;
import com.volantis.testtools.mock.generator.CallUpdaterInfo;
import com.volantis.testtools.mock.generator.GenerationHelper;
import com.volantis.testtools.mock.generator.ClassRenamer;
import com.volantis.testtools.mock.generator.CodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodSet {

    private final Map bindSignature2Method;

    private boolean allInherited;
    private final String name;
    private final int parameterCount;
    private final ClassRenamer renamer;
    private final boolean isInterface;

    public MethodSet(
            String name, int parameterCount, ClassRenamer renamer,
            boolean isInterface) {
        this.name = name;
        this.parameterCount = parameterCount;
        this.renamer = renamer;
        this.isInterface = isInterface;
        this.allInherited = true;
        bindSignature2Method = new HashMap();
    }

    public boolean isAllInherited() {
        return allInherited;
    }

    public void addMethod(
            JavaMethod javaMethod, String bindSignature, SourceType mockedType,
            JavaClass mockedClass) {

        MergedMethod mergedMethod = (MergedMethod)
                bindSignature2Method.get(bindSignature);
        if (mergedMethod == null) {
            mergedMethod = new MergedMethod();
            bindSignature2Method.put(bindSignature, mergedMethod);
        }

        // Merge the information about the java method into the method
        mergedMethod.merge(javaMethod, mockedType, mockedClass);

        allInherited = allInherited && (mockedType == SourceType.BASE_INTERFACE);
    }

    public List getMockableMethods() {
        List methods = new ArrayList();
        boolean unique = bindSignature2Method.size() == 1;
        for (Iterator i = bindSignature2Method.entrySet().iterator();
             i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String bindSignature = (String) entry.getKey();
            MergedMethod mergedMethod = (MergedMethod) entry.getValue();

            JavaMethod javaMethod = mergedMethod.javaMethod;
            String name = javaMethod.getName();
            List parameters = GenerationHelper.getParameters(javaMethod);

            SourceType mockedType = mergedMethod.mockedType;
            JavaClass mockedClass = mergedMethod.mockedClass;
            String uniqueIdentifier = GenerationHelper.uniqueMethodIdentifer(
                    name, parameters);
            String identifierReference;
            boolean declareIdentifier;
            if (mockedType == SourceType.BASE_INTERFACE && isInterface) {
                declareIdentifier = false;
                identifierReference = null;
            } else if (mockedType.isMockAvailable()) {
                declareIdentifier = true;
                identifierReference =
                        renamer.rename(mockedClass.getFullyQualifiedName()) +
                        "." + uniqueIdentifier;
            } else {
                declareIdentifier = true;
                identifierReference = null;
            }

            MockableMethod method =
                    new MockableMethod(name,
                            parameters,
                            bindSignature,
                            mergedMethod.throwables,
                            javaMethod.getReturns(),
                            getCallUpdaterInfo(javaMethod.getReturns()),
                            unique,
                            uniqueIdentifier, declareIdentifier,
                            identifierReference, true, null,
                            renamer.rename(mockedClass.getFullyQualifiedName()));
            
            methods.add(method);
        }

        return methods;
    }

    private static CallUpdaterInfo getCallUpdaterInfo(Type type) {
        CallUpdaterInfo info = GenerationHelper.getBuiltInCallUpdaterInfo(
                CodeUtils.fixInnerClassName(type.toString()));
        if (info == null) {
            info = GenerationHelper
                    .getBuiltInCallUpdaterInfo("java.lang.Object");
        }

        return info;
    }

    public int getParameterCount() {
        return parameterCount;
    }

    public String getName() {
        return name;
    }

    private static class MergedMethod {

        private JavaMethod javaMethod;

        private SourceType mockedType;

        private JavaClass mockedClass;

        private Set throwables;

        public void merge(
                JavaMethod javaMethod, SourceType mockedType,
                JavaClass mockedClass) {

            // If this is the first method, then set it as the current result.
            if (this.javaMethod == null) {
                this.javaMethod = javaMethod;
                this.mockedType = mockedType;
                this.mockedClass = mockedClass;

                throwables = GenerationHelper.getThrowables(javaMethod);
//            } else if (mockedType == SourceType.MOCKED) {
                // This method may either be from further up or further down
                // the class hierarchy but the one that is lowest should win.

            } else /*if (this.mockedType != SourceType.MOCKED)*/ {
                // The current method is inherited and so is the method being
                // merged so merge the exceptions together.

                Set set1 = this.throwables;
                Set set2 = GenerationHelper.getThrowables(javaMethod);

                // Create a new set and merge the throwables into the set.
                this.throwables = new HashSet();
                mergeThrowables(throwables, set1, set2);
                mergeThrowables(throwables, set2, set1);
            }

            // Choose the best source for the method. The best sources are
            // in order:
            // 1) The base class
            // 2) A mocked interface
            // 3) Any other.
            if (mockedType.isBetterThan(this.mockedType)) {
                this.mockedType = mockedType;
                this.mockedClass = mockedClass;
            }
        }

        private void mergeThrowables(Set throwables, Set set1, Set set2) {
            for (Iterator i = set1.iterator(); i.hasNext();) {

                JavaClass specific = (JavaClass) i.next();
                for (JavaClass throwable = specific;
                     !throwable.getFullyQualifiedName().equals("java.lang.Object");
                     throwable = throwable.getSuperJavaClass()) {

                    if (set2.contains(throwable)) {
                        throwables.add(specific);
                        break;
                    }
                }
            }
        }
    }
}
