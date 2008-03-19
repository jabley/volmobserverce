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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/TableState.java,v 1.1 2002/06/25 16:58:30 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * -----------  --------------- -----------------------------------------------
 * 24-Jun-2002  Mat             VBM:2002040202 - Created to encapsulate the
 *                              state of a table for WML generation.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.utilities.ReusableStringBuffer;

/**
 * Encapsulate the alignment information for a table.  This is used to store
 * the alignment information from the td tag in WML as it has to be added to the
 * table tag.
 */
public final class TableState {

    /**
     * Holds a count of the rows.
     */
    private int row;
    /**
     * Holds the alignment information
     */
    private final ReusableStringBuffer alignBuffer;
    /**
     * Hold the context which is used to get a ReusableStringBuffer
     */
    private final MarinerPageContext context;

    /**
     * Creates a new instance of TableState
     *
     * @param context The MarinerPageContext
     */
    public TableState(MarinerPageContext context) {
        this.context = context;
        row = 0;
        alignBuffer = context.allocateRSB();
    }


    /**
     * Get the current row number
     *
     * @return The row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Increment the row number.
     */
    public void incrementRow() {
        row++;
    }

    /**
     * Get the buffer holding the align information.
     *
     * @return The buffer
     */
    public ReusableStringBuffer getBuffer() {
        return alignBuffer;
    }

    /**
     * Release the state of this instance.  Call when you have finished
     * with the Object.
     */
    public void release() {
        context.releaseRSB(alignBuffer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
