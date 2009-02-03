/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;


/**
 * Base class used by mock objects for their type safe returns.
 */
public class AbstractDelegatingCallUpdater
        implements CallUpdater {

    protected final CallUpdaterReturnsObject updater;

    public AbstractDelegatingCallUpdater(CallUpdaterReturnsObject updater) {
        this.updater = updater;
    }

    public Occurrences does(MethodAction action) {
        return updater.does(action);
    }

    public Occurrences fails(Throwable throwable) {
        return updater.fails(throwable);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/3	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrencess of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 ===========================================================================
*/
