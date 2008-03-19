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

import com.volantis.mcs.utilities.MarinerURL;

/**
 * Allows dissector to create URLs without being exposed to environment
 * specific information.
 * <p>
 * A single instance of this should be used for all dissected pages, i.e. it
 * should not maintain any document specific state.
 */
public interface DissectionURLManager {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    public int getNextChangeIndex(DissectableAreaIdentity identity)
        throws DissectionException;

    public int getPreviousChangeIndex(DissectableAreaIdentity identity)
        throws DissectionException;

    public MarinerURL makeURL(DissectionContext dissectionContext,
                              MarinerURL documentURL,
                              int changeIndex)
        throws DissectionException;

    /**
     * Make a pathological URL that is at least as large as the largest URL
     * that could be created for a shard link in this page.
     * @param documentURL The document URL.
     * @return The pathological URL.
     * @throws DissectionException
     */
    public MarinerURL makePathologicalURL (DissectionContext dissectionContext,
                                           MarinerURL documentURL)
        throws DissectionException;
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
