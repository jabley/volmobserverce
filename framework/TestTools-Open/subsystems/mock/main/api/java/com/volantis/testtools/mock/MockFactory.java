/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

import com.volantis.testtools.mock.concurrency.PerThreadExpectationBuilder;
import com.volantis.testtools.mock.concurrency.ThreadMatcher;
import com.volantis.testtools.mock.generated.MockObjectConfiguration;
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
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.proxy.ProxyMockObject;
import com.volantis.testtools.mock.value.CompositeExpectedValue;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Method;

/**
 * todo Document this.
 */
public abstract class MockFactory {

    /**
     * The default instance.
     */
    private static final MockFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = MockFactory.class.getClassLoader();
            Class implClass = loader.loadClass(
                    "com.volantis.testtools.mock.impl.MockFactoryImpl");
            defaultInstance = (MockFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static MockFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Create an {@link ExpectationBuilder} that contains expectations that can
     * be matched in any order.
     *
     * @return The builder.
     */
    public abstract ExpectationBuilder createUnorderedBuilder();

    /**
     * Create an {@link ExpectationBuilder} that contains expectations that can
     * be matched in any order.
     *
     * @param description A description of the builder, for debugging purposes.
     * @return The builder.
     */
    public abstract ExpectationBuilder createUnorderedBuilder(String description);

    /**
     * Create an {@link ExpectationBuilder} that contains expectations that can
     * be matched in the order in which they were added.
     *
     * @return The builder.
     */
    public abstract ExpectationBuilder createOrderedBuilder();


    /**
     * Create an {@link ExpectationBuilder} that contains expectations that can
     * be matched in the order in which they were added.
     *
     * @param description A description of the builder, for debugging purposes.
     * @return The builder.
     */
    public abstract ExpectationBuilder createOrderedBuilder(String description);

    /**
     * Create an {@link ExpectationBuilder} that contains a number of builders
     * that are intended to be used by different threads.
     *
     * @return The per thread builder.
     */
    public abstract PerThreadExpectationBuilder createPerThreadBuilder();

    /**
     * Create an {@link ExpectationBuilder} that contains a number of builders
     * that are intended to be used by different threads.
     *
     * @param description A description of the builder, for debugging purposes.
     * @return The per thread builder.
     */
    public abstract PerThreadExpectationBuilder createPerThreadBuilder(
            String description);

    /**
     * Create a {@link ThreadMatcher} for a known thread.
     *
     * <p>Use this when the thread to be matched is known in advance. These
     * have the highest possible precedence so are always used first.</p>
     *
     * <p>Note: It is not possible to add two known thread matchers for the
     * same thread to a single {@link PerThreadExpectationBuilder}.</p>
     *
     * @param description The description of the matcher.
     * @param thread      The thread to which this applies.
     * @return The newly created {@link ThreadMatcher}.
     */
    public abstract ThreadMatcher createKnownThreadMatcher(
            String description, Thread thread);

    /**
     * Create a {@link ThreadMatcher} for the current thread.
     *
     * <p>Use this when the thread to be matched is known in advance. These
     * have the highest possible precedence so are always used first.</p>
     *
     * <p>Equivalent to:</p>
     * <pre>
     * {@link #createKnownThreadMatcher(String,Thread)
     * createKnownThreadMatcher}(description, Thread.currentThread());</pre>
     *
     * @param description The description of the matcher.
     * @return The newly created {@link ThreadMatcher}.
     */
    public abstract ThreadMatcher createKnownThreadMatcher(String description);

    /**
     * Create a {@link ThreadMatcher} for any thread.
     *
     * <p>Use this when nothing about the thread is known in advance and all
     * other threads that are used in the test have been matched by something
     * else. These have the lowest possible precedence so are always used
     * last.</p>
     *
     * @param description The description of the matcher.
     * @return The newly created {@link ThreadMatcher}.
     */
    public abstract ThreadMatcher createAnyThreadMatcher(String description);

    /**
     * Create a {@link ThreadMatcher} for a named thread.
     *
     * <p>Use this when the name of the thread is known in advance. These have
     * a medium precedence.</p>
     *
     * @param description The description of the matcher.
     * @param name        The name of the thread.
     * @return The newly created {@link ThreadMatcher}.
     */
    public abstract ThreadMatcher createNamedThreadMatcher(
            String description, String name);

    /**
     * Create an {@link ExpectedValue} that will match against any object that
     * is an instance of the specific class.
     *
     * @param instanceOf The class to use to perform the test.
     * @return The {@link ExpectedValue}.
     */
    public abstract ExpectedValue expectsInstanceOf(Class instanceOf);

    /**
     * Create an {@link ExpectedValue} that will match against any object.
     *
     * @return The {@link ExpectedValue}.
     */
    public abstract ExpectedValue expectsAny();

    /**
     * Returns an object that matches any of its contained values.
     *
     * @return an object that matches any of its contained values.
     */
    public abstract CompositeExpectedValue expectsAll();

    /**
     * Returns an object that will match any of the provided Expected values
     *
     * @return an object that will match any of the provided Expected values
     */
    public abstract CompositeExpectedValue expectsAnyDefined();

    /**
     * Create an {@link ExpectedValue} that will match any object that is an
     * array of the specified class.
     *
     * @param componentClass The component class of the array.
     * @return The {@link ExpectedValue}.
     */
    public abstract ExpectedValue expectsArrayOf(Class componentClass);

    /**
     * Return an {@link ExpectedValue} that expects <code>null</code>.
     *
     * @return The {@link ExpectedValue}.
     */
    public abstract ExpectedValue expectsNull();

    /**
     * Return an {@link ExpectedValue} that will only match the provided
     * object.
     *
     * @param obj the object for comparison
     * @return The {@link ExpectedValue}.
     */
    public abstract ExpectedValue expectsSame(Object obj);

    /**
     * Return an object that expects something equal to the provided object.
     *
     * @param obj the object for comparison
     * @return an object that expects something equal to the provided object.
     */
    public abstract ExpectedValue expectsEqual(Object obj);

    /**
     * Returns a value that matches if the object being tested has a to string
     * value that matches the supplied value.
     *
     * @param toString The expected string representation of the object.
     * @return True if the string representation of the object matches, false
     *         otherwise.
     */
    public abstract ExpectedValue expectsToStringOf(String toString);

    /**
     * Create an expected call.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * <p>The arguments are each wrapped in a {@link 
     *
     * @param object           The object on which the call is expected.
     * @param methodIdentifier The identifier of the method.
     * @param arguments        The arguments to the method.
     * @param callDepth        The call depth at which the expectation was set.
     * @return An expected call.
     */
    public abstract ExpectedCall createExpectedCall(
            Object object, MethodIdentifier methodIdentifier,
            Object[] arguments,
            int callDepth);

    /**
     * Create an expected fuzzy call.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * <p>The main difference between this and {@link #createExpectedCall} is
     * that this will leave arguments that are instances of
     * {@link ExpectedValue} alone whereas the above will wrap them in
     *
     * @param object           The object on which the call is expected.
     * @param methodIdentifier The identifier of the method.
     * @param arguments        The arguments to the method.
     * @param callDepth        The call depth at which the expectation was set.
     * @return An expected call.
     */
    public abstract ExpectedCall createFuzzyCall(
            Object object, MethodIdentifier methodIdentifier,
            Object[] arguments,
            int callDepth);

    /**
     * <p><strong>For Mock Framework Use Only</strong></p>
     */
    public abstract MethodCall createMethodCall(
            Object source, MethodIdentifier identifier, Object[] arguments);

    /**
     * <p><strong>For Mock Framework Use Only</strong></p>
     */
    public abstract ExpectationContainer createNoExpectations();

    /**
     * Create a proxy for an object that has not been created yet.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @param mockedInterface The interface that is being mocked.
     * @param identifier      The identifier for the mock object.
     * @return The proxy object.
     */
    public abstract ProxyMockObject createProxyMockObject(
            Class mockedInterface, String identifier);

    /**
     * Returns an Occurrences instance
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return an Occurrences instance
     */
    public abstract Occurrences createOccurrences();

    /**
     * Create a configuration object for a mock object.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return The newly instantiated configuration object.
     */
    public abstract MockObjectConfiguration createConfiguration();

    /**
     * Create an action that simply returns the specified object.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @param object The object the action will return.
     * @return The action.
     * @deprecated Not use by Mock Framework so will be removed in future.
     */
    public abstract MethodAction createReturnFixedValue(Object object);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is unknown.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsAny createReturnsAny(ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is boolean.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsBoolean createReturnsBoolean(
            ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is byte.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsByte createReturnsByte(ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is char.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsChar createReturnsChar(ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is double.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsDouble createReturnsDouble(
            ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is float.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsFloat createReturnsFloat(
            ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is int.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsInt createReturnsInt(ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is long.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsLong createReturnsLong(ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is Object.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsObject createReturnsObject(
            ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is short.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsShort createReturnsShort(
            ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is string.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsString createReturnsString(
            ExpectedCall call);

    /**
     * Get an object that can be used to update the expected call state where
     * the return type is void.
     *
     * <p><strong>For Mock Framework Use Only</strong></p>
     *
     * @return An object for updating the expected call state.
     */
    public abstract CallUpdaterReturnsVoid createReturnsVoid(ExpectedCall call);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/4	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
