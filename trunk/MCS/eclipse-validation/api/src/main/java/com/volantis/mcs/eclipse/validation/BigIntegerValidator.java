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
 * $Header: /src/voyager/com/volantis/mcs/gui/validation/BigIntegerDocument.java,v 1.1 2003/02/10 12:42:15 geoff Exp $
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
package com.volantis.mcs.eclipse.validation;

import java.math.BigInteger;

/**
 * A {@link NumberValidator} for {@link BigInteger} numbers.
 */ 
public class BigIntegerValidator extends NumberValidator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003."; //$NON-NLS-1$
    
    private static final BigInteger EXAMPLE = new BigInteger("0"); //$NON-NLS-1$
    
    /**
     * Constructs a new BigIntegerDocument.
     */
    public BigIntegerValidator() {
        super(EXAMPLE);
    }

    public BigIntegerValidator(String min) {
        super(EXAMPLE, new BigInteger(min));
    }
    
    public BigIntegerValidator(String min, String max) {
        super(EXAMPLE, new BigInteger(min), new BigInteger(max));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
