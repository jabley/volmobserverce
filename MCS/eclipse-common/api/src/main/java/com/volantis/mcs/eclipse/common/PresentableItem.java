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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common;

/**
 * A PresentableItem provides two values - a real value and a value to be used
 * to present the real value.
 */
public class PresentableItem {
    /**
     * The real value.
     */
    public final Object realValue;

    /**
     * The presentable value.
     */
    public final String presentableValue;

    /**
     * Construct a new PresentableItem.
     * @param realValue The real value.
     * @param presentableValue The presentable value.
     */
    public PresentableItem(Object realValue, String presentableValue) {
        this.realValue = realValue;
        this.presentableValue = presentableValue;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Dec-03	2208/4	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 ===========================================================================
*/
