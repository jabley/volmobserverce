/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generated;

import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.MockObject;
import com.volantis.testtools.mock.test.MockTestHelper;
import com.volantis.testtools.mock.method.CallUpdaterReturnsBoolean;
import com.volantis.testtools.mock.method.CallUpdaterReturnsInt;
import com.volantis.testtools.mock.method.CallUpdaterReturnsObject;
import com.volantis.testtools.mock.method.CallUpdaterReturnsString;
import com.volantis.testtools.mock.method.ExpectedCall;
import com.volantis.testtools.mock.method.CallUpdaterReturnsAny;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.method.MethodCall;

import java.util.Map;
import java.util.HashMap;

/**
 * The base object for all the generated mock objects.
 */
public class MockObjectBase
        implements MockObject {

    protected static final MockFactory _mockFactory = MockFactory.getDefaultInstance();

    private static final Map SIGNATURE_2_IDENTIFIER = new HashMap();
    /**
     * An identifier for the {@link #clone()} method.
     */
    public static final MethodIdentifier CLONE_METHOD
            = MethodIdentifier.getMethodIdentifier(
                    SIGNATURE_2_IDENTIFIER,
                    MockObjectBase.class,
                    Object.class,
                    "clone",
                    new Class[0],
                    null);
    /**
     * An identifier for the {@link #equals(Object p0)} method.
     */
    public static final MethodIdentifier EQUALS_METHOD
            = MethodIdentifier.getMethodIdentifier(
                    SIGNATURE_2_IDENTIFIER,
                    MockObjectBase.class,
                    Object.class,
                    "equals",
                    new Class[]{
                        Object.class,
                    },
                    new String[]{
                        "p0",
                    });
    /**
     * An identifier for the {@link #hashCode()} method.
     */
    public static final MethodIdentifier HASH_CODE_METHOD
            = MethodIdentifier.getMethodIdentifier(
                    SIGNATURE_2_IDENTIFIER,
                    MockObjectBase.class,
                    Object.class,
                    "hashCode",
                    new Class[0],
                    null);
    /**
     * An identifier for the {@link #toString()} method.
     */
    public static final MethodIdentifier TO_STRING_METHOD
            = MethodIdentifier.getMethodIdentifier(
                    SIGNATURE_2_IDENTIFIER,
                    MockObjectBase.class,
                    Object.class,
                    "toString",
                    new Class[0],
                    null);

    public static MethodIdentifier _getMethodIdentifier(String signature) {
        MethodIdentifier methodIdentifier =
                (MethodIdentifier) SIGNATURE_2_IDENTIFIER.get(signature);
        if (methodIdentifier == null) {
            throw new IllegalArgumentException(
                    "Could not find identifier for " + signature);
        }
        return methodIdentifier;
    }

    public static void _addMethodIdentifiers(Map signature2Identifier) {
        signature2Identifier.putAll(SIGNATURE_2_IDENTIFIER);
    }

    /**
     * The expectations of this mock object.
     */
    protected ExpectationContainer _container;

    /**
     * The public object used to enable various optional behaviour of this
     * object.
     */
    public final MockObjectConfiguration configuration;

    /**
     * The class that is being mocked.
     */
    protected final Class _mockedClass;

    /**
     * The identifier of this mock.
     */
    protected final String _identifier;

    /**
     * Initialise.
     *
     * @param mockedClass The mocked class, may not be null.
     * @param identifier  The identifier for the object, may be null.
     */
    public MockObjectBase(Class mockedClass, String identifier) {
        this(mockedClass, identifier, null);
    }
    
    /**
     * Initialise.
     *
     * @param mockedClass The mocked class, may not be null.
     * @param identifier  The identifier for the object, may be null.
     */
    public MockObjectBase(Class mockedClass, String identifier,
                          ExpectationContainer container) {

        if (mockedClass == null) {
            throw new IllegalArgumentException("mockedClass must not be null");
        }

        this._mockedClass = mockedClass;

        if (identifier == null) {
            this._identifier = mockedClass.getName() + "#"
                    + Integer.toHexString(System.identityHashCode(this));
        } else {
            this._identifier = "{" + identifier + "}";
        }
        if (container == null) {
            this._container = _mockFactory.createNoExpectations();
        } else {
            this._container = container;
        }
        this.configuration = _mockFactory.createConfiguration();
    }

    // Javadoc inherited.
    public String _getIdentifier() {
        return _identifier;
    }

    // Javadoc inherited.
    public ExpectationContainer _getExpectationContainer() {
        return _container;
    }

    protected Object _doMethodCall(MethodCall call)
            throws Throwable {
        if (_container == null) {
            return MockTestHelper.getGlobalExpectationContainer()
                    .doMethodCall(call);
        } else {
            return _container.doMethodCall(call);
        }
    }

    /**
     * A mock implementation of {@link Object#equals(Object)}.
     *
     * <p>Behaviour depends on setting of
     * {@link MockObjectConfiguration#equalsShouldCheckExpectations()}.</p>
     */
    public boolean equals(Object p0) {
        return GeneratedHelper.equalsMock(this, configuration, p0);
    }

    /**
     * A mock implementation of {@link Object#hashCode()}.
     *
     * <p>Behaviour depends on setting of
     * {@link MockObjectConfiguration#hashCodeShouldCheckExpectations()}.</p>
     */
    public int hashCode() {
        return GeneratedHelper.hashCodeMock(this, configuration);
    }

    /**
     * A mock implementation of {@link Object#toString()}.
     *
     * <p>Behaviour depends on setting of
     * {@link MockObjectConfiguration#toStringShouldCheckExpectations()}.</p>
     */
    public String toString() {
        return GeneratedHelper.toStringMock(this, configuration);
    }

    /**
     * Generic method for expecting a call.
     *
     * @param methodIdentifier The identifier of the method that is expected to
     * be called.
     *
     * @param arguments The arguments to the method, null if it has no
     * arguments.
     */
    public CallUpdaterReturnsAny expects(MethodIdentifier methodIdentifier,
                                         Object[] arguments) {

        ExpectedCall _call =
                _mockFactory.createExpectedCall(
                        this, methodIdentifier, arguments, 1);
        _container.add(_call);
        return _mockFactory.createReturnsAny(_call);
    }

    private static class ExpectsFuzzyImpl {

        protected final Object mock;
        protected final ExpectationContainer container;

        public ExpectsFuzzyImpl(Object mock, ExpectationContainer container) {
            this.mock = mock;
            this.container = container;
        }

        protected void _add(ExpectedCall call) {
            container.add(call);
        }

        public Object _getMock() {
            return mock;
        }
    }

    public static interface Expects {

        CallUpdaterReturnsObject _clone();

        CallUpdaterReturnsBoolean _equals(Object p0);

        CallUpdaterReturnsInt _hashCode();

        CallUpdaterReturnsString _toString();

        Object _getMock();
    }

    public static interface Fuzzy {
        Object _getMock();

        CallUpdaterReturnsAny _equals(Object p0);
    }

    public static class ExpectsImpl
            extends ExpectsFuzzyImpl
            implements Expects {

        public ExpectsImpl(Object mock, ExpectationContainer container) {
            super(mock, container);
        }

        public CallUpdaterReturnsObject _clone() {
            ExpectedCall _call = _mockFactory.createExpectedCall(
                    mock, CLONE_METHOD, null, 1);
            _add(_call);
            return _mockFactory.createReturnsObject(_call);
        }

        public CallUpdaterReturnsBoolean _equals(Object p0) {
            final Object[] _arguments = new Object[]{
                p0,
            };

            ExpectedCall _call = _mockFactory.createExpectedCall(
                    mock, EQUALS_METHOD, _arguments, 1);
            _add(_call);
            return _mockFactory.createReturnsBoolean(_call);
        }

        public CallUpdaterReturnsInt _hashCode() {

            ExpectedCall _call = _mockFactory.createExpectedCall(
                    mock, HASH_CODE_METHOD, null, 1);
            _add(_call);
            return _mockFactory.createReturnsInt(_call);
        }

        public CallUpdaterReturnsString _toString() {

            ExpectedCall _call = _mockFactory.createExpectedCall(
                    mock, TO_STRING_METHOD, null, 1);
            _add(_call);
            return _mockFactory.createReturnsString(_call);
        }
    }

    public static class FuzzyImpl
            extends ExpectsFuzzyImpl
            implements Fuzzy {
        
        public FuzzyImpl(Object mock, ExpectationContainer container) {
            super(mock, container);
        }

        public CallUpdaterReturnsAny _equals(Object p0) {
            final Object[] _arguments = new Object[]{
                p0,
            };

            ExpectedCall _call = _mockFactory.createFuzzyCall(
                    mock, EQUALS_METHOD, _arguments, 1);
            _add(_call);
            return _mockFactory.createReturnsAny(_call);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 20-May-05	8277/4	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/5	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/3	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
