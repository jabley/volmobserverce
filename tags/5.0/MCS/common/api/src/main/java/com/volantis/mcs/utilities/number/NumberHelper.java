/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/utilities/number/NumberHelper.java,v 1.1 2003/02/10 12:42:15 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Feb-03    Geoff           VBM:2003012917 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.utilities.number;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * A helper class which implements functionality for performing various common
 * operations on known types of Numbers in a common way.
 * <p>
 * In particular, it allows us to: 
 * <ul>
 *  <li>compare Numbers using known type conversion. 
 *  <li>create Numbers of known type from a string
 *  <li>find out if Numbers of a known type are decimal or not
 *  <li>find out if Numbers are positive or not
 * </ul>
 * <p>
 * This is abstract and is the root of a small inheritance heirarchy which 
 * mirrors the structure defined by Number and it's subclasses.
 * <p>
 * We provide a mechanism to create the appropriate type of 
 * {@link NumberHelper} which matches the {@link Number} one wishes to operate 
 * upon. 
 * <p>
 * These Helper classes contain no Number state, and thus are implemented
 * using a combination of the Singleton and Abstract Factory patterns.  
 */ 
public abstract class NumberHelper {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Returns an instance of {@link NumberHelper} which operates
     * appropriately on {@link Number}s of the class of the example 
     * {@link Number} provided.
     *  
     * @param example an example {@link Number} (which is used to provide it's 
     *      class only, and is <b>NOT</b> used by the returned helper).
     * @return the {@link NumberHelper} subclass which is designed to operate 
     *      on the class of the example {@link Number} provided.
     */ 
    public static NumberHelper getInstance(Number example) {
        if (example instanceof BigInteger) {
            return new BigIntegerHelper();
        } else if (example instanceof BigDecimal) {
            return new BigDecimalHelper();
        } else {
            throw new UnsupportedOperationException("Not implemented for " + 
                    example.getClass().getName());
        }
    }
    
    /**
     * Return a new {@link Number} object initialised to the value of the 
     * string. 
     * <p>
     * The {@link Number} will be of a type which matches this 
     * {@link NumberHelper} subclass.  
     * 
     * @param value the string containing a number value.
     * @return a Number.
     */ 
    public abstract Number valueOf(String value);

    /**
     * Compare two {@link Number}s, returning true if the first one is less 
     * than the second.
     * <p>
     * The {@link Number}s will be converted to the type which matches this 
     * {@link NumberHelper} subclass before being compared, if necessary.  
     * 
     * @param x the first Number.
     * @param y the second Number.
     * @return true if x < y.
     */ 
    public boolean isLessThan(Number x, Number y) {
        return compareTo(x, y) < 0;
    }
    
    // NOTE: will probably need isLessThanEqual one day, but not yet :-).
    
    /**
     * Compare two {@link Number}s, returning true if the first one is greater 
     * than the second.
     * <p>
     * The {@link Number}s will be converted to the type which matches this 
     * {@link NumberHelper} subclass before being compared, if necessary.  
     * 
     * @param x the first Number.
     * @param y the second Number.
     * @return true if x > y.
     */ 
    public boolean isGreaterThan(Number x, Number y){
        return compareTo(x, y) > 0;
    }
    
    /**
     * Compare two {@link Number}s, returning true if the first one is greater 
     * than or equal to the second.
     * <p>
     * The {@link Number}s will be converted to the type which matches this 
     * {@link NumberHelper} subclass before being compared, if necessary.  
     * 
     * @param x the first Number.
     * @param y the second Number.
     * @return true if x >= y.
     */ 
    public boolean isGreaterThanEqual(Number x, Number y){
        return compareTo(x, y) >= 0;
    }
    
    /**
     * Compares two {@link Number}s.  This method is
     * provided in addition to individual methods for each of the six
     * boolean comparison operators (&lt;, ==, &gt;, &gt;=, !=, &lt;=).  The
     * suggested idiom for performing these comparisons is:
     * <tt>(x.compareTo(y)</tt> &lt;<i>op</i>&gt; <tt>0)</tt>,
     * where &lt;<i>op</i>&gt; is one of the six comparison operators.
     *
     * @param x the first Number.
     * @param y the second Number.
     * @return -1, 0 or 1 as <tt>x</tt> is numerically less than, equal
     *         to, or greater than <tt>y</tt>.
     */
    protected abstract int compareTo(Number x, Number y);
    
    /**
     * Returns true if x is a positive number (or zero).
     * 
     * @param x value to test for positiveness.
     * @return true if x is a positive number.
     */ 
    public boolean isPositive(Number x) {
        return compareTo(x, getZero()) >= 0;
    }

    /**
     * Returns the (+ve) zero value for this Number.
     * 
     * @return zero.
     */ 
    protected abstract Number getZero();
    
    /**
     * Returns true if this is a decimal number.
     * 
     * @return true if this is a decimal number.
     */
    public abstract boolean isDecimal();
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
