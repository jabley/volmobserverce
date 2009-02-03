/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.expression.functions;

/**
 * The round function returns the number that is closest to the argument 
 * and that is an integer.
 *
 * <ul>
 *
 * <li>If there are two such numbers, then the one that is closest to
 * positive infinity is returned.</li>
 *
 * <li>If the argument is NaN, then NaN is returned.</li>
 *
 * <li>If the argument is positive infinity, then positive infinity is
 * returned.</li>
 *
 * <li>If the argument is negative infinity, then negative infinity is
 * returned.</li>
 *
 * <li>If the argument is positive zero, then positive zero is returned.</li>
 *
 * <li>If the argument is negative zero, then negative zero is returned.</li>
 *
 * <li>If the argument is less than zero, but greater than or equal to -0.5,
 * then negative zero is returned.</li>
 *
 * </ul>
 *
 * See <a href="http://www.w3.org/TR/xquery-operators/#func-round">fn:round</a>
 *
 * <p>The result is always a double value to be consistent with XPath.</p>
 */
public class RoundFunction extends OneArgumentNumericFunction {
    
    /**
     * Function name
     */
    private static final String NAME = "round";

    // javadoc inherited
    protected String getName() {
        return NAME;
    }

    // javadoc inherited    
    protected double getValue(double arg) {
        return Math.round(arg);
    }

    // javadoc inherited 
    protected int getValue(int arg) {
        return arg;
    }
    

}