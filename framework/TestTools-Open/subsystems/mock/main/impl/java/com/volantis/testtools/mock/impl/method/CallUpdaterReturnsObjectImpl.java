/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.method.CallUpdaterReturnsObject;
import com.volantis.testtools.mock.method.MethodReturnFixedValue;
import com.volantis.testtools.mock.method.Occurrences;
import com.volantis.testtools.mock.method.ExpectedCall;

/**
 * Implementation of {@link CallUpdaterReturnsObject}.
 */
public class CallUpdaterReturnsObjectImpl
        extends CallUpdaterImpl
        implements CallUpdaterReturnsObject {

    /**
     * Initialise.
     */
    public CallUpdaterReturnsObjectImpl(ExpectedCall event) {
        super(event);
    }

    // Javadoc inherited.
    public Occurrences returns(Object returnValue) {
        event.checkReturnType(returnValue);
        setAction(new MethodReturnFixedValue(returnValue));
        return getOccurences();
    }

    // Javadoc inherited.
    public CallUpdaterReturnsObject description(String description) {
        setDescription(description);
        return this;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 ===========================================================================
*/
