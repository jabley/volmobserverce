/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.expectations;

import java.io.PrintWriter;

public interface Report {

    Report append(int i);

    Report append(String s);

    Report append(Object o);

    PrintWriter getPrintWriter();

    void addStackTrace();

    Object getMarker();

    void setMarker(Object marker);

    /**
     * Start a new block indenting by some more.
     */
    public void beginBlock();

    /**
     * End a block.
     */
    public void endBlock();

    /**
     * Called to indicate to the Report that a new Expectation is being
     * examined. It should always be matched by a call to
     * {@link #handleEventEffect(com.volantis.testtools.mock.EventEffect)}.
     */
    public void startExpectation();

    /**
     * Called to allow the Report instance to decide how to report the
     * EventEffect after checking an Expectation.
     *
     * @param effect
     */
    public void handleEventEffect(com.volantis.testtools.mock.EventEffect effect);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
