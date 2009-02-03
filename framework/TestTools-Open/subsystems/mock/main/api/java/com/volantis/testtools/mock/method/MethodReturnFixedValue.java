/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

/**
 * Causes a method in a mock object to return a fixed value.
 */
public class MethodReturnFixedValue
        implements MethodAction {

    /**
     * The value the method should return.
     */
    private final Object returnValue;

    /**
     * Initialise.
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive float
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(float returnValue) {
        this(new Float(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive double
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(double returnValue) {
        this(new Double(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive byte
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(byte returnValue) {
        this(new Byte(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive char
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(char returnValue) {
        this(new Character(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive short
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(short returnValue) {
        this(new Short(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive int
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(int returnValue) {
        this(new Integer(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive long
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(long returnValue) {
        this(new Long(returnValue));
    }

    /**
     * Initialise.
     *
     * <p>This is syntactic sugar to make it easier to use primitive boolean
     * values.</p>
     *
     * @param returnValue The return value.
     */
    public MethodReturnFixedValue(boolean returnValue) {
        this(returnValue ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * Update the event with the stored return value.
     */
    public Object perform(MethodActionEvent event) {
        return returnValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/2	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
