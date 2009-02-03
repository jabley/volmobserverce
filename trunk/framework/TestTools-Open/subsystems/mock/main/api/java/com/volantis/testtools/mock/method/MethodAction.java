/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

/**
 * The action to perform if a {@link ExpectedCall} is satisfied.
 */
public interface MethodAction {

    /**
     * Perform the action.
     *
     * <p>Any exceptions thrown and values returned are passed back to the
     * caller.</p>
     *
     * @param event The event that triggered the action.
     */
    public Object perform(MethodActionEvent event)
            throws Throwable;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/2	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Apr-04	3703/3	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
