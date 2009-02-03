/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.Event;
import com.volantis.testtools.mock.proxy.ProxyMockObject;
import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.impl.AbstractSingleExpectation;
import com.volantis.testtools.mock.impl.RepeatingExpectation;
import com.volantis.testtools.mock.method.ExpectedArguments;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.testtools.mock.method.MethodHelper;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.value.ExpectedValue;

import java.io.PrintWriter;

/**
 * An expectation that a method will be invoked.
 */
public class ExpectedMethodInvocationImpl
        extends AbstractSingleExpectation {

    /**
     * The source of the event.
     */
    private final Object source;

    /**
     * The identifier of the method.
     */
    private final MethodIdentifier method;

    /**
     * The expected arguments.
     */
    private final ExpectedArguments arguments;

    /**
     * The Throwable that encapsulates the stack when this expectation was
     * set.
     */
    private final Throwable stack;

    /**
     * The action to perform.
     */
    private MethodAction action;

    /**
     * An optional identifier for expectation.
     */
    private String description;
    private final RepeatingExpectation repeating;

    /**
     * Initialise.
     *
     * @param source    The expected source of the event (the object upon which
     *                  the method was invoked). If null then it will match any object.
     * @param method    The identifier of the expected method.
     * @param arguments The expected arguments.
     */
    public ExpectedMethodInvocationImpl(
            Object source,
            MethodIdentifier method,
            ExpectedArguments arguments,
            RepeatingExpectation repeating) {

        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        if (method == null) {
            throw new IllegalArgumentException("method cannot be null");
        }

        // Record the stack when this expectation was created.
        stack = new Throwable();

        this.source = source;
        this.method = method;
        this.arguments = arguments;
        this.repeating = repeating;
    }

    /**
     * Set the action.
     *
     * @param action
     */
    public void setAction(MethodAction action) {
        if (this.action != null) {
            throw new IllegalStateException("Cannot change action");
        }
        this.action = action;
    }

    public void setDescription(String description) {
        if (this.description != null) {
            throw new IllegalStateException("Cannot change description");
        }
        this.description = description == null ? "" : "[" + description + "]";
    }

    public void debug(Report report) {
        PrintWriter out = report.getPrintWriter();

        final String shortDeclaringClassName
                = MethodHelper.getShorterName(
                        method.getDeclaringClass().getName());
        report
                .append("Call to ")
                .append(source)
                .append(".")
                .append(shortDeclaringClassName)
                .append("#").append(method.getName()).append("(");

        Object[] arguments = this.arguments.getExpectedValues();
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            if (i > 0) {
                report.append(", ");
            }
            report.append(argument);
        }
        report.append(")");
        if (description != null) {
            report.append(" ").append(description);
        }
        out.println();
    }

    protected boolean checkExpectation(Event event) {

        // If this method does not return void then make sure that an
        // action has been set.
        if (Void.TYPE != method.getReturnType()
                && action == null) {
            throw new RuntimeException("No return value has been set", stack);
        }

        // Check that the event is a method invocation one.
        if (!(event instanceof MethodActionEvent)) {
            return false;
        }

        // Check that the method identifier matches.
        MethodInvocationEvent invocation = (MethodInvocationEvent) event;
        if (!invocation.getMethod().equals(method)) {
            return false;
        }

        // Check that the source is correct.
        if (!(source instanceof ProxyMockObject) && source != event.getSource()) {
            return false;
        }

        // Check all the arguments.
        Object[] actualValues = invocation.getArguments();
        ExpectedValue[] expectedValues = arguments.getExpectedValues();

        // If the lengths are not the same then it is an error.
        if (actualValues.length != expectedValues.length) {
            return false;
        }

        // Assume that all the actual value matched.
        boolean matched = true;
        for (int i = 0; i < expectedValues.length; i++) {
            ExpectedValue expectedValue = expectedValues[i];
            Object actualValue = actualValues[i];
            if (!expectedValue.matches(actualValue)) {
                matched = false;
            }
        }

        if (!matched) {
            return matched;
        }

        // Pass the action, if any back through the stack to be processed.
        invocation.setAction(action);

        return true;
    }

    // Javadoc inherited.
    public String toString() {
        StringBuffer buffer = new StringBuffer(40);
        buffer.append("Invoked Method: ").append(method.getName()).append("(");
        ExpectedValue[] expectedValues = arguments.getExpectedValues();
        for (int i = 0; i < expectedValues.length; i++) {
            ExpectedValue expectedValue = expectedValues[i];
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(expectedValue);
        }
        buffer.append(")");

        return buffer.toString();
    }



    public Occurrences getOccurences() {
        return repeating.getOccurences();
    }

    public void checkReturnType(Object returnValue) {
        if (returnValue == null) {
            return;
        }

        Class providedReturnType = returnValue.getClass();
        Class actualReturnType = method.getReturnType();
        if (!actualReturnType.isAssignableFrom(providedReturnType)) {
            throw new IllegalArgumentException(
                    returnValue + " is not assignable to " + actualReturnType +
                    " and thus cannot be used as the return type for" +
                    " this method");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 14-Jun-05	7997/6	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/4	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3514/4	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
