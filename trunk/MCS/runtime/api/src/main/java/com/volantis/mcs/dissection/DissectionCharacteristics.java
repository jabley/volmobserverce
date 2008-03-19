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
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection;

/**
 * This interface allows the dissector to get hold of information that defines
 * those characteristics of the target device that affect the dissection.
 * <p>
 * Other things that may need adding to this.
 * <p>
 * Threshold levels that cause warnings to be generated when the fixed size
 * is too close to the device limit.
 */
public interface DissectionCharacteristics {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    public static final int UNLIMITED_PAGE_SIZE = -1;

    /**
     * Get the maximum page size.
     * <p>
     * The maximum page size is in terms of units as understood by the
     * {@link com.volantis.mcs.dissection.dom.DissectableDocument}.
     * <p>
     * If there is no limit then this should return
     * {@link #UNLIMITED_PAGE_SIZE}, otherwise it must be a positive (not zero)
     * value.
     * @return The maximum page size.
     */
    public int getMaxPageSize();
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
