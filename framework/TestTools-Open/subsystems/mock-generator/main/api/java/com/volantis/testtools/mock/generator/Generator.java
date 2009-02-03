/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator;

import com.volantis.testtools.mock.generator.model.MockableClass;
import com.volantis.testtools.mock.generator.model.MockableMethod;
import com.volantis.testtools.mock.generator.model.MockableParameter;
import com.volantis.testtools.mock.generator.model.MockableConstructor;
import com.volantis.testtools.mock.generator.model.MockableExecutable;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.JavaClass;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Generator {

    private final Writer writer;
    private final MockableClass mockableClass;
    private StringWriter stringWriter;

    public Generator(MockableClass mockableClass, Writer writer) {
        this.mockableClass = mockableClass;
        this.writer = writer;
        stringWriter = new StringWriter();
    }

    public Generator out(String out) throws IOException {
//        writer.write(out);
        stringWriter.write(out);
        return this;
    }

    public void generate() throws IOException {
        String fullyQualifiedName = mockableClass.getFullyQualifiedName();
        out("// Generated MockObject for ").out(fullyQualifiedName).out(". Do not edit!\n");
        String mockPackage = mockableClass.getMockPackage();
        if (mockPackage != null && !mockPackage.equals("")) {
            out("package ").out(mockPackage).out(";\n");
        }
        String extendsClass;
        String implementsInterface;
        if (mockableClass.isInterface()) {
            extendsClass = mockableClass.getBaseMockClass();
            implementsInterface = fullyQualifiedName;
        } else {
            extendsClass = fullyQualifiedName;
            implementsInterface = "com.volantis.testtools.mock.MockObject";
        }
        String mockClassName = mockableClass.getName() + "Mock";
        out("public class ").out(mockClassName).out("\n");
        out("        extends ").out(extendsClass).out("\n");
        out("        implements ").out(implementsInterface).out(" {\n");
        out("\n");
        if (!mockableClass.isInterface()) {
            out("    protected static final com.volantis.testtools.mock.MockFactory _mockFactory = com.volantis.testtools.mock.MockFactory.getDefaultInstance();\n");
        }
        out("\n");
        if (mockableClass.getHasIdentifiers()) {
            out("    private static final java.util.Map SIGNATURE_2_IDENTIFIER = new java.util.HashMap();\n");
            out("    static {\n");
            for (Iterator i = mockableClass.getBaseMocks().iterator(); i.hasNext();) {
                String baseMock = (String) i.next();
                out("        ").out(baseMock).out("._addMethodIdentifiers(SIGNATURE_2_IDENTIFIER);\n");
            }
            out("    }\n");
            out("    \n");
            out("    public static com.volantis.testtools.mock.method.MethodIdentifier _getMethodIdentifier(java.lang.String signature) {\n");
            out("        return com.volantis.testtools.mock.method.MethodIdentifier.getMethodIdentifier(SIGNATURE_2_IDENTIFIER, signature);\n");
            out("    }\n");
            out("\n");
            out("    public static void _addMethodIdentifiers(java.util.Map signature2Identifier) {\n");
            out("        signature2Identifier.putAll(SIGNATURE_2_IDENTIFIER);\n");
            out("    }\n");
            out("\n");
            for (Iterator i = mockableClass.getNormalMethods().iterator();
                 i.hasNext();) {
                MockableMethod method = (MockableMethod) i.next();
                if (method.getDeclareIdentifier()) {
                    out("    /**\n");
                    out("     * An identifier for the {@link #").out(method.getBindSignature()).out(" method.\n");
                    out("     */\n");
                    out("    public static final com.volantis.testtools.mock.method.MethodIdentifier ").out(method.getUniqueIdentifier()).out("\n");

                    String identifierReference = method.getIdentifierReference();
                    if (identifierReference != null) {
                        out("            = ").out(identifierReference).out(";\n");
                    } else {
                        out("            = com.volantis.testtools.mock.method.MethodIdentifier.getMethodIdentifier(\n");
                        out("                    SIGNATURE_2_IDENTIFIER,\n");
                        out("                    ").out(mockClassName).out(".class,\n");
                        out("                    ").out(fullyQualifiedName).out(".class,\n");
                        out("                    \"").out(method.getName()).out("\",\n");
                        out("                    new java.lang.Class[] {\n");
                        List parameters = method.getParameters();
                        if (parameters != null) {
                            for (Iterator j = parameters.iterator(); j.hasNext();) {
                                MockableParameter parameter = (MockableParameter) j.next();
                                out("                        ").out(parameter.getType()).out(".class,\n");
                            }
                        }
                        out("                    },\n");
                        if (parameters != null) {
                            out("                    new java.lang.String[] {\n");
                            for (Iterator j = parameters.iterator(); j.hasNext();) {
                                MockableParameter parameter = (MockableParameter) j.next();
                                out("                        \"").out(parameter.getName()).out("\",\n");
                            }
                            out("                    });\n");
                        } else {
                            out("                    null);\n");
                        }
                    }
                }
            }

        } else {
            out("    public static com.volantis.testtools.mock.method.MethodIdentifier _getMethodIdentifier(java.lang.String signature) {\n");
            out("        return com.volantis.testtools.mock.generated.MockObjectBase._getMethodIdentifier(signature);\n");
            out("    }\n");
            out("\n");
            out("    public static void _addMethodIdentifiers(java.util.Map signature2Identifier) {\n");
            out("        com.volantis.testtools.mock.generated.MockObjectBase._addMethodIdentifiers(signature2Identifier);\n");
            out("    }\n");
        }
        out("\n");
        if (!mockableClass.isInterface()) {
            out("    /**\n");
            out("     * The container that holds the expectations for this object.\n");
            out("     */\n");
            out("    protected final com.volantis.testtools.mock.ExpectationContainer _container;\n");
            out("\n");
            out("    /**\n");
            out("     * The public object used to enable various optional behaviour of this\n");
            out("     * object.\n");
            out("     */\n");
            out("    public final com.volantis.testtools.mock.generated.MockObjectConfiguration configuration;\n");
            out("\n");
            out("    /**\n");
            out("     * The class that is being mocked.\n");
            out("     */\n");
            out("    protected final java.lang.Class _mockedClass;\n");
            out("\n");
            out("    /**\n");
            out("     * The identifier of this mock.\n");
            out("     */\n");
            out("    protected final java.lang.String _identifier;\n");
        }
        out("\n");
        out("    /**\n");
        out("     * The object used to add expectations to this object.\n");
        out("     */\n");
        out("    public final Expects expects;\n");
        out("\n");
        out("    /**\n");
        out("     * The object used to add fuzzy expectations to this object.\n");
        out("     */\n");
        out("    public final Fuzzy fuzzy;\n");
        out("\n");
        for (Iterator i = mockableClass.getConstructors().iterator();
             i.hasNext();) {
            MockableConstructor constructor = (MockableConstructor) i.next();

            out("    /**\n");
            out("     * Initialise.\n");
            out("     *\n");
            out("     * @param _container The container to which this object adds its\n");
            out("     * expectations.\n");
            out("     */\n");
            String parametersDeclaration = declareParameters(constructor, ",");
            String throwsClause = throwsClause(constructor);
            out("    public ").out(mockClassName).out("(com.volantis.testtools.mock.ExpectationContainer _container").out(
                    parametersDeclaration).out(") ").out(throwsClause).out(" {\n");
            out("\n");
            out("        this(").out(fullyQualifiedName).out(".class, null, _container").out(passParameters(constructor, "             ",  ",")).out(");\n");
            out("    }\n");
            out("\n");
            out("    /**\n");
            out("     * Initialise.\n");
            out("     *\n");
            out("     * @param _identifier The identifier of the mock object.\n");
            out("     * @param _container The container to which this object adds its\n");
            out("     * expectations.\n");
            out("     */\n");
            out("    public ").out(mockClassName).out("(\n");
            out("        java.lang.String _identifier, com.volantis.testtools.mock.ExpectationContainer _container").out(
                    parametersDeclaration).out(") ").out(throwsClause).out(" {\n");
            out("\n");
            out("        this(").out(fullyQualifiedName).out(".class, _identifier, _container").out(passParameters(constructor, "             ", ",")).out(");\n");
            out("    }\n");
            out("\n");
            out("    /**\n");
            out("     * Initialise.\n");
            out("     *\n");
            out("     * <p>Objects initialised with this cannot have any expectations. Any\n");
            out("     * attempts to add expectations will fail, as will any calls to the\n");
            out("     * mocked up methods.</p>\n");
            out("     */\n");
            out("    public ").out(mockClassName).out("(java.lang.String _identifier").out(
                    parametersDeclaration).out(") ").out(throwsClause).out(" {\n");
            out("\n");
            out("        this(").out(fullyQualifiedName).out(".class, _identifier, null").out(passParameters(constructor, "             ", ",")).out(");\n");
            out("    }\n");
            out("\n");
            out("    /**\n");
            out("     * Initialise.\n");
            out("     *\n");
            out("     * <p>Provided to allow other mock objects to extend.</p>\n");
            out("     */\n");
            out("    protected ").out(mockClassName).out("(\n");
            out("        java.lang.Class _mockedClass, java.lang.String _identifier, com.volantis.testtools.mock.ExpectationContainer _container").out(
                    parametersDeclaration).out(") ").out(throwsClause).out(" {\n");
            out("\n");
            if (mockableClass.isInterface()) {
                out("        super(_mockedClass, _identifier, _container\n");
                out("              ").out(passParameters(constructor, "              ", ",")).out(");\n");
            } else {
                out("        super(").out(passParameters(constructor, "              ", "")).out(");\n");
            out("\n");
            out("        if (_mockedClass == null) {\n");
            out("            throw new IllegalArgumentException(\n");
            out("                    \"_mockedClass must not be null\");\n");
            out("        }\n");
            out("\n");
            out("        this._mockedClass = _mockedClass;\n");
            out("        if (_identifier == null) {\n");
            out("            this._identifier = _mockedClass.getName() + \"#\"\n");
            out("                    + java.lang.Integer.toHexString(System.identityHashCode(this));\n");
            out("        } else {\n");
            out("            this._identifier = \"{\" + _identifier + \"}\";\n");
            out("        }\n");
            out("        if (_container == null) {\n");
            out("            this._container = _mockFactory.createNoExpectations();\n");
            out("        } else {\n");
            out("            this._container = _container;\n");
            out("        }\n");
            out("        this.configuration = _mockFactory.createConfiguration();\n");
            }
            out("\n");
            out("        this.expects = (Expects) _createExpects(this, this._container);\n");
            out("        this.fuzzy = (Fuzzy) _createFuzzy(this, this._container);\n");
            out("    }\n");
        }
        out("\n");
        out("    protected java.lang.Object _createExpects(java.lang.Object mock, com.volantis.testtools.mock.ExpectationContainer container) {\n");
        out("        return new ExpectsImpl(mock, container);\n");
        out("    }\n");
        out("\n");
        out("    protected java.lang.Object _createFuzzy(java.lang.Object mock, com.volantis.testtools.mock.ExpectationContainer container) {\n");
        out("        return new FuzzyImpl(mock, container);\n");
        out("    }\n");
        out("\n");
        if (!mockableClass.isInterface()) {
            out("    // Javadoc inherited.\n");
            out("    public java.lang.String _getIdentifier() {\n");
            out("        return _identifier;\n");
            out("    }\n");
            out("\n");
            out("    // Javadoc inherited.\n");
            out("    public com.volantis.testtools.mock.ExpectationContainer _getExpectationContainer() {\n");
            out("        return _container;\n");
            out("    }\n");
            out("\n");
            out("    protected java.lang.Object _doMethodCall(com.volantis.testtools.mock.method.MethodCall call)\n");
            out("            throws Throwable {\n");
            out("        if (_container == null) {\n");
            out("            return com.volantis.testtools.mock.test.MockTestHelper.getGlobalExpectationContainer()\n");
            out("                    .doMethodCall(call);\n");
            out("        } else {\n");
            out("            return _container.doMethodCall(call);\n");
            out("        }\n");
            out("    }\n");
        }

        for (Iterator i = mockableClass.getNormalMethods().iterator(); i.hasNext();) {
            MockableMethod method = (MockableMethod) i.next();

            out("\n");
            Type returnType = method.getReturnType();
            final String returnTypeStr =
                CodeUtils.fixInnerClassName(returnType.toString());
            out("    public ").out(returnTypeStr).out(" ").out(method.getName()).out("(").out(declareParameters(method, "")).out(") ").out(throwsClause(method)).out("{\n");
            String bindSignature = method.getBindSignature();
            List parameters = method.getParameters();
            if (bindSignature.equals("equals(java.lang.Object)")) {
                out("        return com.volantis.testtools.mock.generated.GeneratedHelper.equalsMock(this, configuration, ").out(((MockableParameter) parameters.get(0)).getName()).out(");\n");
            } else if (bindSignature.equals("hashCode()")) {
                out("        return com.volantis.testtools.mock.generated.GeneratedHelper.hashCodeMock(this, configuration);\n");
            } else if (bindSignature.equals("toString()")) {
                out("        return com.volantis.testtools.mock.generated.GeneratedHelper.toStringMock(this, configuration);\n");
            } else {
                out("        com.volantis.testtools.mock.method.MethodCall _call = _mockFactory.createMethodCall(\n");
                out("                this, ").out(method.getUniqueIdentifier()).out(",\n");
                if (parameters != null) {
                    out("                new java.lang.Object[]{\n");
                    for (Iterator j = parameters.iterator(); j.hasNext();) {
                        MockableParameter parameter = (MockableParameter) j.next();
                        out("                    ").out(parameter.box()).out(",\n");
                    }
                    out("                });\n");
                } else {
                    out("                null);\n");
                }
                out("\n");
                out("        try {\n");
                if (returnType.toString().equals("void")) {
                    out("            _doMethodCall(_call);\n");
                } else {
                    out("            java.lang.Object _result = _doMethodCall(_call);\n");
                    out("            return ").out(method.unbox(returnType, "_result")).out(";\n");
                }
                out("        }\n");
                out("        catch(Throwable _throwable) {\n");
                out("            // If result is a subclass of Error, RuntimeException then rethrow\n");
                out("            // it, if it is one of the exceptions that this method declares\n");
                out("            // then cast and rethrow it, otherwise rethrow it as an undeclared\n");
                out("            // throwable exception.\n");
                out("            if (_throwable instanceof RuntimeException) {\n");
                out("                throw (RuntimeException) _throwable;\n");
                out("            } else if (_throwable instanceof Error) {\n");
                out("                throw (Error) _throwable;\n");
                out("            }\n");
                Set throwables = method.getThrowables();
                if (throwables != null) {
                    for (Iterator j = throwables.iterator(); j.hasNext();) {
                        JavaClass throwable = (JavaClass) j.next();
                        out("            else if (_throwable instanceof ").out(throwable.getFullyQualifiedName()).out(") {\n");
                        out("                throw (").out(throwable.getFullyQualifiedName()).out(") _throwable;\n");
                        out("            }\n");
                    }
                }
                out("            else {\n");
                out("                throw new java.lang.reflect.UndeclaredThrowableException(_throwable);\n");
                out("            }\n");
                out("        }\n");
            }
            out("    }\n");
        }
        if (!mockableClass.isInterface()) {
            out("\n");
            out("    /**\n");
            out("     * Generic method for expecting a call.\n");
            out("     *\n");
            out("     * @param methodIdentifier The identifier of the method that is expected to\n");
            out("     * be called.\n");
            out("     *\n");
            out("     * @param arguments The arguments to the method, null if it has no\n");
            out("     * arguments.\n");
            out("     */\n");
            out("    public com.volantis.testtools.mock.method.CallUpdaterReturnsAny expects(com.volantis.testtools.mock.method.MethodIdentifier methodIdentifier,\n");
            out("                                         java.lang.Object[] arguments) {\n");
            out("\n");
            out("        com.volantis.testtools.mock.method.ExpectedCall _call =\n");
            out("                _mockFactory.createExpectedCall(\n");
            out("                        this, methodIdentifier, arguments, 1);\n");
            out("        _container.add(_call);\n");
            out("        return _mockFactory.createReturnsAny(_call);\n");
            out("    }\n");
        }
        out("\n");
        out("    public static interface Expects\n");
        String expectsPrefix = "        extends ";
        for (Iterator i = mockableClass.getBaseMocks().iterator(); i.hasNext();) {
            String baseMock = (String) i.next();
            out(expectsPrefix).out(baseMock).out(".Expects");
            expectsPrefix = ",\n                ";
        }
        out(" {\n");

        for (Iterator i = mockableClass.getNormalMethods().iterator(); i.hasNext();) {
            MockableMethod method = (MockableMethod) i.next();
            if (method.getAddExpectsFuzzy()) {
                out("\n");
                out("        ").out(method.getCallUpdaterQualifiedName()).out(" ").out(method.getName()).out("(").out(declareParameters(method, "")).out(");\n");
            }
        }
        out("    }\n");
        out("\n");
        out("    public static class ExpectsImpl\n");
        out("        extends ").out(mockableClass.getBaseMockClass()).out(".ExpectsImpl\n");
        out("        implements Expects {\n");
        out("        \n");
        out("        public ExpectsImpl(java.lang.Object mock, com.volantis.testtools.mock.ExpectationContainer container) {\n");
        out("            super(mock, container);\n");
        out("        }\n");
        out("\n");

        for (Iterator i = mockableClass.getNormalMethods().iterator(); i.hasNext();) {
            MockableMethod method = (MockableMethod) i.next();
            if (method.getAddExpectsFuzzy()) {
                out("\n");
                out("        public ").out(method.getCallUpdaterQualifiedName()).out(" ").out(method.getName()).out("(").out(declareParameters(method, "")).out(") {\n");

                out("\n");
                out("            com.volantis.testtools.mock.method.ExpectedCall _call = _mockFactory.createExpectedCall(\n");
                out("                    mock, ").out(method.getUniqueIdentifier()).out(",\n");
                List parameters = method.getParameters();
                if (parameters != null) {
                    out("                    new java.lang.Object[]{\n");
                    for (Iterator j = parameters.iterator(); j.hasNext();) {
                        MockableParameter parameter = (MockableParameter) j.next();
                        out("                        ").out(parameter.box()).out(",\n");
                    }
                    out("                    }, 1);\n");
                } else {
                    out("                    null, 1);\n");
                }
                out("            _add(_call);\n");
                out("            return ").out(method.getCallUpdaterCreationCode()).out(";\n");
                out("        }\n");
                out("\n");
            }
        }
        out("    }\n");
        out("\n");
        out("    public static interface Fuzzy\n");
        String fuzzyPrefix = "        extends ";
        for (Iterator i = mockableClass.getBaseMocks().iterator(); i.hasNext();) {
            String baseMock = (String) i.next();
            out(fuzzyPrefix).out(baseMock).out(".Fuzzy");
            fuzzyPrefix = ",\n                ";
        }
        out(" {\n");

        for (Iterator i = mockableClass.getFuzzyMethods().iterator(); i.hasNext();) {
            MockableMethod method = (MockableMethod) i.next();
            if (method.getAddExpectsFuzzy()) {
                out("\n");
                String identifier;
                String prefix;
                if (method.isUnique()) {
                    identifier = "";
                    prefix = "";
                } else {
                    identifier = "com.volantis.testtools.mock.method.MethodIdentifier _methodIdentifier";
                    prefix = ",";
                }

                // The method may be unique in this class but it is possible that a derived
                // class may add another method that has the same name and number of parameters
                // but with different types and a different return type that would clash with
                // the fuzzy method. Therefore, make sure that all fuzzy methods can support
                // any return type.
                out("        com.volantis.testtools.mock.method.CallUpdaterReturnsAny ").out(method.getName()).out("(").out(identifier).out(fuzzyParameters(method, prefix)).out(");\n");
            }
        }
        out("    }\n");
        out("\n");
        out("    public static class FuzzyImpl\n");
        out("        extends ").out(mockableClass.getBaseMockClass()).out(".FuzzyImpl\n");
        out("        implements Fuzzy\n");
        out("    {\n");
        out("        public FuzzyImpl(java.lang.Object mock, com.volantis.testtools.mock.ExpectationContainer container) {\n");
        out("            super(mock, container);\n");
        out("        }\n");
        out("\n");

        for (Iterator i = mockableClass.getFuzzyMethods().iterator(); i.hasNext();) {
            MockableMethod method = (MockableMethod) i.next();
            if (method.getAddExpectsFuzzy()) {
                out("\n");
                String identifier;
                String prefix;
                if (method.isUnique()) {
                    identifier = "";
                    prefix = "";
                } else {
                    identifier = "com.volantis.testtools.mock.method.MethodIdentifier _methodIdentifier";
                    prefix = ",";
                }

                out("        public com.volantis.testtools.mock.method.CallUpdaterReturnsAny ").out(method.getName()).out("(").out(identifier).out(fuzzyParameters(method, prefix)).out(") {\n");

                out("\n");
                out("            com.volantis.testtools.mock.method.ExpectedCall _call = _mockFactory.createFuzzyCall(\n");
                out("                    mock, ").out((method.isUnique() ? method.getUniqueIdentifier() : "_methodIdentifier")).out(",\n");
                List parameters = method.getParameters();
                if (parameters != null) {
                    out("                    new java.lang.Object[]{\n");
                    for (Iterator j = parameters.iterator(); j.hasNext();) {
                        MockableParameter parameter = (MockableParameter) j.next();
                        out("                        ").out(parameter.getName()).out(",\n");
                    }
                    out("                    }, 1);\n");
                } else {
                    out("                    null, 1);\n");
                }
                out("            _add(_call);\n");
                out("            return _mockFactory.createReturnsAny(_call);\n");
                out("        }\n");
            }
        }

        out("    }\n");
        out("\n");
        out("    public static class MockProxy {\n");
        out("\n");
        out("        public static MockProxy get(java.lang.String identifier) {\n");
        out("            com.volantis.testtools.mock.ExpectationContainer global =\n");
        out("                    com.volantis.testtools.mock.test.MockTestHelper.getGlobalExpectationContainer();\n");
        out("\n");
        out("            com.volantis.testtools.mock.proxy.ProxyMockObject object = com.volantis.testtools.mock.test.MockTestHelper.getProxyObject(\n");
        out("                    ").out(mockClassName).out(".class, identifier);\n");
        out("            MockProxy proxy = (MockProxy) object.getObject();\n");
        out("            if (proxy == null) {\n");
        out("                proxy = new MockProxy(object, global);\n");
        out("                object.setObject(proxy);\n");
        out("            }\n");
        out("\n");
        out("            return proxy;\n");
        out("        }\n");
        out("\n");
        out("        public final Expects expects;\n");
        out("\n");
        out("        public final Fuzzy fuzzy;\n");
        out("\n");
        out("        private MockProxy(java.lang.Object object, com.volantis.testtools.mock.ExpectationContainer expectations) {\n");
        out("            this.expects = new ExpectsImpl(object, expectations);\n");
        out("            this.fuzzy = new FuzzyImpl(object, expectations);\n");
        out("        }\n");
        out("    }\n");
        out("}\n");

        writer.write(stringWriter.getBuffer().toString());
    }

    private String throwsClause(MockableExecutable executable) {
        StringBuffer buffer = new StringBuffer();
        Set throwables = executable.getThrowables();
        if (throwables != null) {
            buffer.append("\n");
            buffer.append("            throws ");
            for (Iterator i = throwables.iterator(); i.hasNext();) {
                JavaClass throwable = (JavaClass) i.next();
                buffer.append(throwable.getFullyQualifiedName());
                if (i.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
        return buffer.toString();
    }

    private String declareParameters(MockableExecutable executable, String prefix) {
        StringBuffer buffer = new StringBuffer();
        List parameters = executable.getParameters();
        if (parameters != null) {
            if (!prefix.equals("")) {
                buffer.append(prefix).append("\n");
                buffer.append("            ");
            }
            for (Iterator i = parameters.iterator(); i.hasNext();) {
                MockableParameter parameter = (MockableParameter) i.next();
                buffer.append(parameter.getType())
                        .append(" ").append(parameter.getName());
                if (i.hasNext()) {
                    buffer.append(",\n            ");
                }
            }
        }
        return buffer.toString();
    }

    private String passParameters(
            MockableExecutable executable, String indent, String prefix) {

        StringBuffer buffer = new StringBuffer();
        List parameters = executable.getParameters();
        if (parameters != null) {
            if (!prefix.equals("")) {
                buffer.append(prefix).append("\n");
                buffer.append(indent);
            }
            for (Iterator i = parameters.iterator(); i.hasNext();) {
                MockableParameter parameter = (MockableParameter) i.next();
                buffer.append(indent).append(parameter.getName());
                if (i.hasNext()) {
                    buffer.append(",\n").append(indent);
                }
            }
        }
        return buffer.toString();
    }

    private String fuzzyParameters(MockableMethod method, String prefix) {
        StringBuffer buffer = new StringBuffer();
        List parameters = method.getParameters();
        if (parameters != null) {
            if (!prefix.equals("")) {
                buffer.append(prefix).append("\n");
                buffer.append("            ");
            }
            for (Iterator i = parameters.iterator(); i.hasNext();) {
                MockableParameter parameter = (MockableParameter) i.next();
                buffer.append("java.lang.Object").append(" ").append(parameter.getName());
                if (i.hasNext()) {
                    buffer.append(",\n            ");
                }
            }
        }
        return buffer.toString();
    }
}
