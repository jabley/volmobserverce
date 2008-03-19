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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-May-03    Geoff           VBM:2003042904 - Created; abstract base class 
 *                              for coded Attributes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Abstract base class for coded Attributes. 
 */ 
public abstract class AttributeCode extends SingleByteInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The maximum value for attribute start tokens. 
     * <p>
     * The attribute token space is split into two halves; less than or equal
     * to this value is valid for attribute start tokens, greater then this 
     * value is valid for attribute value tokens.
     */ 
    protected static final int ATTRIBUTE_START_MAX = 0x7F;

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
