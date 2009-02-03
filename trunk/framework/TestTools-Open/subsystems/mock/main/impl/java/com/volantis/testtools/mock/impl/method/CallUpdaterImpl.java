/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.method.CallUpdater;
import com.volantis.testtools.mock.method.ExpectedCall;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodThrow;
import com.volantis.testtools.mock.method.Occurrences;

public class CallUpdaterImpl
        implements CallUpdater {

    protected final com.volantis.testtools.mock.impl.method.ExpectedCallImpl event;

    public CallUpdaterImpl(ExpectedCall event) {
        this.event = (com.volantis.testtools.mock.impl.method.ExpectedCallImpl) event;
    }

    protected Occurrences getOccurences() {
        return event.getOccurences();
    }

    // Javadoc inherited.
    public Occurrences fails(Throwable throwable) {
        setAction(new MethodThrow(throwable));
        return getOccurences();
    }

    // Javadoc inherited.
    public Occurrences does(MethodAction action) {
        setAction(action);
        return getOccurences();
    }

    protected void setAction(MethodAction action) {
        event.setAction(action);
    }

    protected void setDescription(String description) {
        event.setDescription(description);
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
