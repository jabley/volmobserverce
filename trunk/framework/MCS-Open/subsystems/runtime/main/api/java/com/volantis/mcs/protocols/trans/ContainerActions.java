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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/ContainerActions.java,v 1.3 2002/10/15 11:13:14 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Sep-02    Phil W-S        VBM:2002091901 - Created.
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Add the INVERSE_REMAP action.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

/**
 * This interface defines a number of constants that can be used to determine
 * how to manage tables nested within containers that are not table cell DOM
 * elements.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public interface ContainerActions {
    /**
     * This value should be used to indicate that a nested table (within an
     * container from which the table cannot be promoted) should be re-mapped
     * into appropriate alternative markup. 
     */
    int REMAP = 0;

    /**
     * This value should be used to indicate that a nested table (within an
     * container from which the table cannot be promoted) should be retained
     * if possible, causing the containing table(s) to be re-mapped into
     * appropriate alternative markup. 
     */
    int INVERSE_REMAP = 10;

    /**
     * This value should be used to indicate that a nested table (found within
     * a container from which the table cannot be promoted) should be left
     * as-is. 
     */
    int RETAIN = 20;

    /**
     * This value should be used to indicate that a nested table is to be
     * promoted out of the given container. 
     */
    int PROMOTE = 30;
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
