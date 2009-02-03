/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.concurrency.PerThreadExpectationBuilder;
import com.volantis.testtools.mock.concurrency.ThreadMatcher;
import com.volantis.testtools.mock.generated.MockObjectConfiguration;
import com.volantis.testtools.mock.impl.concurrency.AnyThreadMatcher;
import com.volantis.testtools.mock.impl.concurrency.KnownThreadMatcher;
import com.volantis.testtools.mock.impl.concurrency.NamedThreadMatcher;
import com.volantis.testtools.mock.impl.concurrency.PerThreadExpectationBuilderImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsAnyImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsBooleanImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsByteImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsCharImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsDoubleImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsFloatImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsIntImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsLongImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsObjectImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsShortImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsStringImpl;
import com.volantis.testtools.mock.impl.method.CallUpdaterReturnsVoidImpl;
import com.volantis.testtools.mock.impl.method.ExpectedCallImpl;
import com.volantis.testtools.mock.impl.method.MethodInvocationEvent;
import com.volantis.testtools.mock.impl.method.OccurrencesImpl;
import com.volantis.testtools.mock.impl.proxy.ProxyMockObjectImpl;
import com.volantis.testtools.mock.impl.value.AllExpectedValues;
import com.volantis.testtools.mock.impl.value.AnyExpectedValues;
import com.volantis.testtools.mock.impl.value.ExpectedAnyValue;
import com.volantis.testtools.mock.impl.value.ExpectedArrayOf;
import com.volantis.testtools.mock.impl.value.ExpectedEqual;
import com.volantis.testtools.mock.impl.value.ExpectedInstanceOf;
import com.volantis.testtools.mock.impl.value.ExpectedNull;
import com.volantis.testtools.mock.impl.value.ExpectedSame;
import com.volantis.testtools.mock.method.CallUpdaterReturnsAny;
import com.volantis.testtools.mock.method.CallUpdaterReturnsBoolean;
import com.volantis.testtools.mock.method.CallUpdaterReturnsByte;
import com.volantis.testtools.mock.method.CallUpdaterReturnsChar;
import com.volantis.testtools.mock.method.CallUpdaterReturnsDouble;
import com.volantis.testtools.mock.method.CallUpdaterReturnsFloat;
import com.volantis.testtools.mock.method.CallUpdaterReturnsInt;
import com.volantis.testtools.mock.method.CallUpdaterReturnsLong;
import com.volantis.testtools.mock.method.CallUpdaterReturnsObject;
import com.volantis.testtools.mock.method.CallUpdaterReturnsShort;
import com.volantis.testtools.mock.method.CallUpdaterReturnsString;
import com.volantis.testtools.mock.method.CallUpdaterReturnsVoid;
import com.volantis.testtools.mock.method.ExpectedCall;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodCall;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.method.MethodReturnFixedValue;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.method.ExpectedArguments;
import com.volantis.testtools.mock.proxy.ProxyMockObject;
import com.volantis.testtools.mock.value.CompositeExpectedValue;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.io.IOException;
import java.io.Writer;

public class MockFactoryImpl
        extends MockFactory {

    private static final ExpectationContainer NO_EXPECTATIONS_CONTAINER
            = new ExpectationContainer() {
                public void add(Expectation expectation) {
                    throw new IllegalStateException("No expectations expected");
                }

                public Object doMethodCall(MethodCall methodCall) {
                    throw new IllegalStateException("No expectations expected");
                }

                public void dump(Writer writer)
                        throws IOException {
                    writer.write("No expectations" + Character.LINE_SEPARATOR);
                }

                public void verify() {
                }
            };

    // Javadoc inherited.
    public ExpectationBuilder createUnorderedBuilder() {
        return createUnorderedBuilder(null);
    }

    // Javadoc inherited.
    public ExpectationBuilder createUnorderedBuilder(String description) {
        return new UnorderedExpectationBuilder(description);
    }

    // Javadoc inherited.
    public ExpectationBuilder createOrderedBuilder() {
        return createOrderedBuilder(null);
    }

    // Javadoc inherited.
    public ExpectationBuilder createOrderedBuilder(String description) {
        return new OrderedExpectationBuilder(description);
    }

    public PerThreadExpectationBuilder createPerThreadBuilder() {
        return new PerThreadExpectationBuilderImpl(null);
    }

    // Javadoc inherited.
    public PerThreadExpectationBuilder createPerThreadBuilder(
            String description) {
        return new PerThreadExpectationBuilderImpl(description);
    }

    // Javadoc inherited.
    public ThreadMatcher createKnownThreadMatcher(
            String description, Thread thread) {
        return new KnownThreadMatcher(description, thread);
    }

    // Javadoc inherited.
    public ThreadMatcher createKnownThreadMatcher(String description) {
        return new KnownThreadMatcher(description);
    }

    // Javadoc inherited.
    public ThreadMatcher createAnyThreadMatcher(String description) {
        return new AnyThreadMatcher(description);
    }

    // Javadoc inherited.
    public ThreadMatcher createNamedThreadMatcher(
            String description, String name) {
        return new NamedThreadMatcher(description, name);
    }

    // Javadoc inherited.
    public ExpectedCall createExpectedCall(
            Object object, MethodIdentifier methodIdentifier,
            Object[] arguments, int callDepth) {

        if (object == null) {
            throw new IllegalArgumentException("object cannot be null");
        }
        if (methodIdentifier == null) {
            throw new IllegalArgumentException("methodIdentifier cannot be null");
        }

        ExpectedArguments expectedArguments =
                new ExpectedArguments(methodIdentifier, arguments, true);

        return new ExpectedCallImpl(object, methodIdentifier, expectedArguments);
    }

    // Javadoc inherited.
    public ExpectedCall createFuzzyCall(
            Object object, MethodIdentifier methodIdentifier,
            Object[] arguments, int callDepth) {

        if (object == null) {
            throw new IllegalArgumentException("object cannot be null");
        }
        if (methodIdentifier == null) {
            throw new IllegalArgumentException("methodIdentifier cannot be null");
        }

        ExpectedArguments expectedArguments =
                new ExpectedArguments(methodIdentifier, arguments, false);

        return new ExpectedCallImpl(object, methodIdentifier, expectedArguments);
    }

    // javadoc inherited
    public Occurrences createOccurrences() {
        return new OccurrencesImpl();
    }

    // Javadoc inherited.
    public MethodCall createMethodCall(
            Object source, MethodIdentifier identifier, Object[] arguments) {

        return new MethodInvocationEvent(source, identifier, arguments);
    }

    // Javadoc inherited.
    public ExpectationContainer createNoExpectations() {
        return MockFactoryImpl.NO_EXPECTATIONS_CONTAINER;
    }

    // Javadoc inherited.
    public ExpectedValue expectsInstanceOf(Class instanceOf) {
        return new ExpectedInstanceOf(instanceOf);
    }

    // Javadoc inherited
    public ExpectedValue expectsNull() {
        return ExpectedNull.INSTANCE;
    }

    // Javadoc inherited.
    public ExpectedValue expectsAny() {
        return ExpectedAnyValue.INSTANCE;
    }

    // Javadoc inherited
    public ExpectedValue expectsSame(Object obj) {
        return new ExpectedSame(obj);
    }

    // Javadoc inherited
    public ExpectedValue expectsEqual(Object obj) {
        return new ExpectedEqual(obj);
    }

    // Javadoc inherited
    public CompositeExpectedValue expectsAnyDefined() {
        return new AnyExpectedValues();
    }

    // javadoc inherited
    public CompositeExpectedValue expectsAll() {
        return new AllExpectedValues();
    }

    // Javadoc inherited.
    public ExpectedValue expectsArrayOf(Class componentClass) {
        return new ExpectedArrayOf(componentClass);
    }

    public ExpectedValue expectsToStringOf(final String toString) {
        return new ExpectedValue() {
            public boolean matches(Object object) {
                return String.valueOf(object).equals(toString);
            }
        };
    }

    // Javadoc inherited.
    public ProxyMockObject createProxyMockObject(
            Class mockedInterface, String identifier) {

        return new ProxyMockObjectImpl(mockedInterface, identifier);
    }

    // Javadoc inherited.
    public MockObjectConfiguration createConfiguration() {
        return new MockObjectConfigurationImpl();
    }

    public MethodAction createReturnFixedValue(Object object) {
        return new MethodReturnFixedValue(object);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsAny createReturnsAny(ExpectedCall call) {
        return new CallUpdaterReturnsAnyImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsBoolean createReturnsBoolean(ExpectedCall call) {
        return new CallUpdaterReturnsBooleanImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsByte createReturnsByte(ExpectedCall call) {
        return new CallUpdaterReturnsByteImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsChar createReturnsChar(ExpectedCall call) {
        return new CallUpdaterReturnsCharImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsDouble createReturnsDouble(ExpectedCall call) {
        return new CallUpdaterReturnsDoubleImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsFloat createReturnsFloat(ExpectedCall call) {
        return new CallUpdaterReturnsFloatImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsInt createReturnsInt(ExpectedCall call) {
        return new CallUpdaterReturnsIntImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsLong createReturnsLong(ExpectedCall call) {
        return new CallUpdaterReturnsLongImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsObject createReturnsObject(ExpectedCall call) {
        return new CallUpdaterReturnsObjectImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsShort createReturnsShort(ExpectedCall call) {
        return new CallUpdaterReturnsShortImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsString createReturnsString(ExpectedCall call) {
        return new CallUpdaterReturnsStringImpl(call);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsVoid createReturnsVoid(ExpectedCall call) {
        return new CallUpdaterReturnsVoidImpl(call);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 14-Jun-05	7997/5	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/3	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/4	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
