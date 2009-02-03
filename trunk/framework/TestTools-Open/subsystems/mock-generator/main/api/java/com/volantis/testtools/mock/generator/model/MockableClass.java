/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import com.volantis.testtools.mock.generator.GenerationHelper;
import com.volantis.testtools.mock.generator.ObjectMethodAction;
import com.volantis.testtools.mock.generator.ClassRenamer;
import com.volantis.testtools.mock.generator.QDox129;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

public class MockableClass {

    /**
     * Comparator for comparing two XMembers.
     */
    final static Comparator METHOD_COMPARATOR =
            new Comparator() {
                public int compare(Object o1, Object o2) {
                    AbstractJavaEntity m1 = (AbstractJavaEntity) o1;
                    AbstractJavaEntity m2 = (AbstractJavaEntity) o2;

                    return m1.getName().compareTo(m2.getName());
                }

                public boolean equals(Object obj) {
                    // dumb
                    return obj == this;
                }
            };

//    static final Log log = LogFactory.getLog(MockableClass.class);

    private final JavaClass javaClass;
    private final ClassRenamer renamer;
    private final QDox129 qdox129;
    private JavaClass baseMockedClass;
    private final Collection constructors;
    private List fuzzyMethods;
    private List normalMethods;
    private boolean hasIdentifiers;
    private Set implementedMockedInterfaces;
    private Set baseMocks;
    private String mockPackage;

    public MockableClass(
            JavaClass javaClass, DocletTag tag, ClassRenamer renamer,
            QDox129 qdox129) {

        this.javaClass = javaClass;
        this.renamer = renamer;
        this.qdox129 = qdox129;

        // Find the base mock class.
        String baseMockedClassName = tag.getNamedParameter("base");
        baseMockedClass = findBaseMockedClass(
                baseMockedClassName, this.javaClass);

        this.implementedMockedInterfaces = getInterfaces(tag, javaClass);

        // Sort the methods into groups by name and number of parameters.
        // Needed to determine uniqueness of method name and whether a fuzzy
        // method is required.
        Map name2MethodSet = findMockableMethods();

        List normalMethods = new ArrayList();
        List fuzzyMethods = new ArrayList();
        boolean hasIdentifiers = false;

        Set baseMocks = new TreeSet();

        for (Iterator i = name2MethodSet.values().iterator(); i.hasNext();) {
            MethodSet methodSet = (MethodSet) i.next();

            List methods = methodSet.getMockableMethods();
            for (Iterator j = methods.iterator(); j.hasNext();) {
                MockableMethod method = (MockableMethod) j.next();
                String bindSignature = method.getBindSignature();

                // Separate the object methods that need to be mocked as they
                // are handled specially.
                ObjectMethodAction action =
                        GenerationHelper.getObjectMethodActionBySignature(
                                bindSignature);
                if (action != null) {
                    method = action.createMockableMethod(method);
                }

                if (method != null) {
                    hasIdentifiers |= method.getDeclareIdentifier();
                    normalMethods.add(method);

                    String baseMock = method.getMockClass();
                    baseMocks.add(baseMock);
                }
            }

            int parameterCount = methodSet.getParameterCount();
            if (parameterCount == 0 || methodSet.isAllInherited()) {
                // No fuzzy method is needed as either the parent handles them
                // all, or it does not have any parameters.
            } else {
                boolean addFuzzy = true;
                String name = methodSet.getName();
                if (parameterCount == 1 && name.equals("equals")) {
                    // Don't add a fuzzy method for equals as that is already
                    // handled in the base.
                    addFuzzy = false;
                }

                if (addFuzzy) {
                    // Get the first method although any method would do. The
                    // only things of interest are the name, and the number of
                    // parameters which the same for all methods in the set.
                    MockableMethod method = (MockableMethod) methods.get(0);
                    fuzzyMethods.add(method);
                }
            }
        }

        baseMocks.remove(renamer.rename(javaClass.getFullyQualifiedName()));
        if (baseMocks.isEmpty()) {
            baseMocks.add("com.volantis.testtools.mock.generated.MockObjectBase");
        }

        this.fuzzyMethods = fuzzyMethods;
        this.normalMethods = normalMethods;
        this.constructors = findConstructors();
        this.hasIdentifiers = hasIdentifiers;
        this.baseMocks = baseMocks;
        this.mockPackage = renamer.renamePackage(javaClass.getPackage());
    }

    private Map findMockableMethods() {
        Map name2MethodSet = new TreeMap();
        Set excluded = new HashSet();

        findMockableMethods(javaClass, name2MethodSet, excluded,
                SourceType.MOCKED, javaClass);

        return name2MethodSet;
    }

    private void findMockableMethods(
            JavaClass javaClass, Map name2MethodSet, Set excluded,
            SourceType closestMockedType, JavaClass closedMockedClass) {

        if (javaClass == baseMockedClass) {
            if (javaClass.isInterface()) {
                closestMockedType = SourceType.BASE_INTERFACE;
            } else {
                closestMockedType = SourceType.BASE_CLASS;
            }
            closedMockedClass = javaClass;
        } else if (javaClass.isInterface() &&
                implementedMockedInterfaces.contains(javaClass)) {
            closestMockedType = SourceType.MOCKED_INTERFACE;
            closedMockedClass = javaClass;
        }

        JavaMethod[] methods = javaClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            JavaMethod javaMethod = methods[i];

            String bindSignature =
                    GenerationHelper.getBindSignature(javaMethod);

            if (javaMethod.isConstructor() || javaMethod.isStatic()) {
                // Ignore constructors.
                continue;
            } else if (javaMethod.isFinal()) {
                // Ignore final methods and also exclude them from being picked
                // up on any other classes.
                excluded.add(bindSignature);
                continue;
            } else if (excluded.contains(bindSignature)) {
                // Ignore any previously excluded methods.
                continue;
            }

            JavaParameter[] parameters = javaMethod.getParameters();
            int parameterCount = (parameters == null ? 0 : parameters.length);
            String name = javaMethod.getName();
            String key = name + " " + parameterCount;

            MethodSet methodSet = (MethodSet) name2MethodSet.get(key);
            if (methodSet == null) {
                methodSet = new MethodSet(name, parameterCount, renamer, isInterface());
                name2MethodSet.put(key, methodSet);
            }

            methodSet.addMethod(javaMethod, bindSignature, closestMockedType,
                    closedMockedClass);
        }

        JavaClass superClass = resolveClass(javaClass.getSuperClass(),
            javaClass.getPackage());
        if (superClass != null) {

            findMockableMethods(superClass, name2MethodSet, excluded,
                    closestMockedType, closedMockedClass);
        }

        Type[] interfaces = javaClass.getImplements();
        for (int i = 0; i < interfaces.length; i++) {
            final Type interfaceType = interfaces[i];
            // if the interface is in the same package then interfaceType won't
            // have package information
            JavaClass ifc = resolveClass(interfaceType, javaClass.getPackage());

            findMockableMethods(ifc, name2MethodSet, excluded,
                    closestMockedType, closedMockedClass);
        }
    }

    private JavaClass resolveClass(Type type, String defaultPackage) {
        if (type == null) {
            return null;
        }
//        return resolver.resolveClass(type);
        JavaClass result = type.getJavaClass();
        final boolean needsQdox129Fix =
            result.getParentSource().getURL() == null;
        if (needsQdox129Fix) {
            try {
                result = qdox129.recreateBinary(result);
            } catch (Throwable t) {
                // fall back to the original JavaClass
                System.out.println("Can't recreate binary class " +
                    result.getFullyQualifiedName() +
                    "\r\n\tReason: " + t.getMessage());
            }
//            if (!recreated && result.getPackage() == null) {
//                // the package information might be missing due to a bug in qdox
//                try {
//                    result = qdox129.recreateBinary(result, defaultPackage);
//                } catch (Throwable t) {
//                    // fall back to the original JavaClass
//                    System.out.println("Wasn't able to recreate binary class " +
//                        defaultPackage + '.' + result.getFullyQualifiedName());
//                }
//            }
        }
        return result;
    }

    public Set getBaseMocks() {
        return baseMocks;
    }

    public String getName() {
        return javaClass.getName();
    }

    public boolean getExtendsMocked() {
        return baseMockedClass != null;
    }

    public String getFullyQualifiedName() {
        return javaClass.getFullyQualifiedName();
    }

    public boolean getExtendsGeneratedMock() {
        return baseMockedClass != null && isInterface();
    }

    public boolean getHasIdentifiers() {
        return hasIdentifiers;
    }

    public boolean isInterface() {
        return javaClass.isInterface();
    }

    public Collection getConstructors() {
        return constructors;
    }

    private Collection findConstructors() {

        JavaClass mockedClass = getJavaClass();

        if (mockedClass == null) {
            throw new IllegalArgumentException("currentClass == null!");
        }

        JavaMethod[] methods = mockedClass.getMethods(false);
        Arrays.sort(methods, METHOD_COMPARATOR);

        List filteredMethods = new ArrayList();
        for (int i = 0; i < methods.length; i++) {
            JavaMethod method = methods[i];

            if (method.isConstructor() && !method.isPrivate()) {
                filteredMethods.add(method);
            }
        }

        Collection filteredConstructors = filteredMethods;
        if (filteredConstructors.isEmpty()) {
            filteredConstructors = new ArrayList();
            JavaMethod constructor = new JavaMethod();
            constructor.setConstructor(true);
            constructor.setParentClass(mockedClass);
            constructor.setParameters(new JavaParameter[0]);
            filteredConstructors.add(constructor);
        }

        List constructors = new ArrayList();
        for (Iterator i = filteredConstructors.iterator(); i.hasNext();) {
            JavaMethod javaMethod = (JavaMethod) i.next();
            Set throwables = GenerationHelper.getThrowables(javaMethod);
            constructors.add(new MockableConstructor(javaMethod, throwables));
        }

        return constructors;
    }

    public JavaClass getJavaClass() {
        return javaClass;
    }

    public List getNormalMethods() {
        return normalMethods;
    }

    public List getFuzzyMethods() {
        return fuzzyMethods;
    }

    public String getMockPackage() {
        return mockPackage;
    }

    public String getBaseMockClass() {
        if (baseMockedClass == null) {
            return "com.volantis.testtools.mock.generated.MockObjectBase";
        } else {
            return renamer.rename(baseMockedClass.getFullyQualifiedName());
        }
    }

    private static JavaClass findBaseMockedClass(
            String baseMockedName, JavaClass mockedClass) {

        final JavaClass baseMockedClass;
        if (baseMockedName != null) {

            String prefixedBaseMockName = "." + baseMockedName;

            // Find the base interface, or super class that most closely
            // matches the base name.
            List matched = new ArrayList();
            List interfaces = Arrays.asList(
                    mockedClass.getImplementedInterfaces());
            List classes;
            JavaClass superClass = mockedClass.getSuperJavaClass();
            if (superClass != null) {
                classes = new ArrayList(interfaces);
                classes.add(superClass);
            } else {
                classes = interfaces;
            }

            for (Iterator i = classes.iterator(); i.hasNext();) {
                JavaClass implementedInterface = (JavaClass) i.next();
                String interfaceName =
                        implementedInterface.getFullyQualifiedName();
                if (interfaceName.equals(baseMockedName)) {
                    matched.add(implementedInterface);
                    break;
                } else if (interfaceName.endsWith(prefixedBaseMockName)) {
                    matched.add(implementedInterface);
                }
            }

            // If the list is empty, or contains more than one element then it
            // is a problem.
            int matchCount = matched.size();
            if (matchCount == 0) {
                throw new IllegalStateException(
                        "Could not find an interface that matched " +
                                prefixedBaseMockName);
            } else if (matchCount > 1) {
                throw new IllegalStateException(
                        "Base name '" + prefixedBaseMockName +
                                "' is ambiguous");
            }

            baseMockedClass = (JavaClass) matched.get(0);
        } else {
            baseMockedClass = null;
        }

        return baseMockedClass;
    }

    private static Set getInterfaces(
            DocletTag tag, JavaClass mockedClass) {
        String interfaces = tag.getNamedParameter("interfaces");
        Set interfacesList;
        if (interfaces == null) {
            interfacesList = Collections.EMPTY_SET;
        } else {
            interfacesList = findExtendsClassList(interfaces, mockedClass);
        }
        return interfacesList;
    }

    private static Set findExtendsClassList(
            String extendsList, JavaClass mockedClass) {
        Set interfacesClassList = new TreeSet();
        StringTokenizer tokenizer = new StringTokenizer(extendsList, ",");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            JavaClass extendsClass = findBaseMockedClass(token, mockedClass);
            interfacesClassList.add(extendsClass);
        }

        return interfacesClassList;
    }
}
