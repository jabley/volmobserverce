/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.CompositeExpectation;
import com.volantis.testtools.mock.Expectation;
import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.expectations.ExpectationBuilderInternal;
import com.volantis.testtools.mock.expectations.Expectations;
import com.volantis.testtools.mock.method.MethodCall;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.test.MockTestHelper;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class of all implementations of {@link com.volantis.testtools.mock.expectations.ExpectationBuilderInternal}.
 *
 * <p>This manages a stack of {@link CompositeExpectation}s that can be pushed
 * and popped to help construct a hierarchical collection of expectations.</p>
 */
public abstract class ExpectationBuilderImpl
        implements ExpectationBuilderInternal {

    private final String description;

    /**
     * The root container, this is the first container added within the
     * builder.
     */
    private ExpectationContainer root;

    /**
     * The composite expectation into which expectations are going to be added.
     */
    private CompositeExpectation current;

    /**
     * The stack of composite expectations.
     */
    private List expectationStack;

    /**
     * Initialise.
     */
    protected ExpectationBuilderImpl() {
        this(null);
    }

    /**
     * Initialise.
     *
     * @param description Description of the builder, used for debugging.
     */
    protected ExpectationBuilderImpl(String description) {
        this.description = description;
        expectationStack = new ArrayList();

        // Add this builder to the set of expectations needed to be verified
        // at the end of a test.
        MockTestHelper.addExpectations(this);
    }

    // Javadoc inherited.
    public void beginSet() {
        CompositeExpectation expectation;
        if (current instanceof ExpectationSet) {
            expectation = current;
        } else {
            expectation = new ExpectationSet("Nested Inside: " + this);
        }
        push(expectation);
    }

    // Javadoc inherited.
    public void endSet() {
        pop();
    }

    // Javadoc inherited.
    public void beginSequence() {
        CompositeExpectation expectation;
        if (current instanceof ExpectationSequence) {
            expectation = current;
        } else {
            expectation = new ExpectationSequence("Nested Inside: " + this);
        }

        push(expectation);
    }

    // Javadoc inherited.
    public void endSequence() {
        pop();
    }

    /**
     * Push the composite expectation on the stack.
     *
     * @param expectation The expectation to push.
     */
    private void push(CompositeExpectation expectation) {
        expectationStack.add(current);
        current = expectation;
        if (root == null) {
            root = current;
        }
    }

    /**
     * Pop the top most composite expectation from the stack and add it to
     * its containing expectation, if any.
     *
     * <p>If the popped expectation is the same as the current one then it has
     * already been added to it.</p>
     */
    private void pop() {
        CompositeExpectation expectation = current;
        current = (CompositeExpectation) expectationStack.remove(
                expectationStack.size() - 1);
        if (null != current && expectation != current) {
            current.addExpectation(expectation);
        }
    }

    private Occurrences beginRepeated() {
        final RepeatingExpectation expectation = new RepeatingExpectation();
        push(expectation);
        return expectation.getOccurences();
    }

    private void endRepeated() {
        pop();
    }

    public void add(Expectation expectation) {
        current.addExpectation(expectation);
    }

    public Object doMethodCall(MethodCall methodCall)
            throws Throwable {
        return root.doMethodCall(methodCall);
    }

    public void dump(Writer writer)
            throws IOException {

        if (null == root) {
            writer.write("No expectations\n");
        } else {
            root.dump(writer);
        }
    }

    public Occurrences add(Expectations expectations) {
        Occurrences occurrences = beginRepeated();
        expectations.addTo(this);
        endRepeated();
        return occurrences;
    }

    public void addSequence(Expectations expectations) {
        beginSequence();
        expectations.add();
        endSequence();
    }

    public void addSet(Expectations expectations) {
        beginSet();
        expectations.add();
        endSet();
    }

    public void verify() {
        root.verify();
    }

    // Javadoc inherited.
    public String toString() {
        return description == null ? super.toString() : description;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 14-Jun-05	7997/4	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 20-May-05	8277/4	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
