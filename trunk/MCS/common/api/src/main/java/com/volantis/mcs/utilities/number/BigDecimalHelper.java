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
 * $Header: /src/voyager/com/volantis/mcs/utilities/number/BigDecimalHelper.java,v 1.1 2003/02/10 12:42:15 geoff Exp $
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

import java.math.BigDecimal;

/**
 * An implementation of {@link NumberHelper} for {@link BigDecimal}.
 */ 
public class BigDecimalHelper extends NumberHelper {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    private static BigDecimal ZERO = new BigDecimal("0");

    /** 
     * Package protected so that no one else can instantiate it 
     */
    BigDecimalHelper() {
    }

    // Inherit javadoc.
    public Number valueOf(String value) {
        return new BigDecimal(value);
    }

    // Inherit javadoc.
    protected int compareTo(Number x, Number y) {
        if (! (x instanceof BigDecimal && y instanceof BigDecimal)) {
            throw new IllegalStateException();
        }
        return ((BigDecimal)x).compareTo((BigDecimal)y);
    }

    // Inherit javadoc.
    protected Number getZero() {
        return ZERO;
    }

    // Inherit javadoc.
    public boolean isDecimal() {
        return true;
    }
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
