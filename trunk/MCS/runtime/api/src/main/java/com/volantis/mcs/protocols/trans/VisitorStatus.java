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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/VisitorStatus.java,v 1.2 2002/09/25 16:58:33 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

/**
 * Instances of this class are attached to the DOM elements during transformation pre-processing to prevent the elements from being pre-processed more than once. The action can be used for debugging purposes to identify the circumstance of the DOM element's pre-processing. 
 */
public class VisitorStatus {

    /**
     * The default instance.
     */
    private static final VisitorStatus DEFAULT_INSTANCE =
            new VisitorStatus();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static VisitorStatus getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Initializes the new instance using the given parameters. 
     */
    public VisitorStatus() {
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
