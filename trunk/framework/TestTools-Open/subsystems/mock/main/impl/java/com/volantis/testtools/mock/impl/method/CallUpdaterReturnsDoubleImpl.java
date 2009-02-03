/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.method.CallUpdaterReturnsDouble;
import com.volantis.testtools.mock.method.MethodReturnFixedValue;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.method.ExpectedCall;

/**
 * Implementation of {@link CallUpdaterReturnsDouble}.
 */
public class CallUpdaterReturnsDoubleImpl
        extends CallUpdaterImpl
        implements CallUpdaterReturnsDouble {

    /**
     * Initialise.
     */
    public CallUpdaterReturnsDoubleImpl(ExpectedCall event) {
        super(event);
    }

    // Javadoc inherited.
    public Occurrences returns(double returnValue) {
        setAction(new MethodReturnFixedValue(returnValue));
        return getOccurences();
    }

    // Javadoc inherited.
    public CallUpdaterReturnsDouble description(String description) {
        setDescription(description);
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 ===========================================================================
*/
