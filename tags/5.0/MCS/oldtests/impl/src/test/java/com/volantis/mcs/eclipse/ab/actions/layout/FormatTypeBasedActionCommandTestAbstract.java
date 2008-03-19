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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;

/**
 * Tests {@link FormatTypeBasedActionCommand}.
 */
public abstract class FormatTypeBasedActionCommandTestAbstract
        extends LayoutActionCommandTestAbstract {

    // @todo implement a test for checkFormatType

    /**
     * Returns a format type based action command instance for testing.
     *
     * @param formatType the format type needed to initialize the action
     *                   command
     * @return the new action command
     */
    protected abstract FormatTypeBasedActionCommand createCommand(
            FormatType formatType, ODOMSelectionManager selectionManager);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/3	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
