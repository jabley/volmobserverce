/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.method.CallUpdaterReturnsVoid;
import com.volantis.testtools.mock.method.MaxOcurrences;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.method.ExpectedCall;

/**
 * Implementation of {@link CallUpdaterReturnsVoid}.
 */
public class CallUpdaterReturnsVoidImpl
        extends CallUpdaterImpl
        implements CallUpdaterReturnsVoid {

    /**
     * Initialise.
     */
    public CallUpdaterReturnsVoidImpl(ExpectedCall event) {
        super(event);
    }

    // Javadoc inherited.
    public CallUpdaterReturnsVoid description(String description) {
        setDescription(description);
        return this;
    }

    public void any() {
        Occurrences occurrences = getOccurences();
        occurrences.any();
    }

    public void atLeast(int minimum) {
        Occurrences occurrences = getOccurences();
        occurrences.atLeast(minimum);
    }

    public void fixed(int number) {
        Occurrences occurrences = getOccurences();
        occurrences.fixed(number);
    }

    public MaxOcurrences min(int minimum) {
        Occurrences occurrences = getOccurences();
        return occurrences.min(minimum);
    }

    public void optional() {
        Occurrences occurrences = getOccurences();
        occurrences.optional();
    }

    public void unbounded() {
        Occurrences occurrences = getOccurences();
        occurrences.unbounded();
    }

    public void max(int maximum) {
        Occurrences occurrences = getOccurences();
        occurrences.max(maximum);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 ===========================================================================
*/
