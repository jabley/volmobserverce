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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/TransMapper.java,v 1.2 2002/09/25 16:58:33 philws Exp $
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

import com.volantis.mcs.dom.Element;

/**
 * This interface provides the method that should be invoked to perform a table
 * (tree or element) remapping when a container validator returns the REMAP
 * container action.
 */
public interface TransMapper {
    /**
     * This method should be invoked to perform the remapping of table elements
     * into alternative non-table ones when a table is found that cannot
     * otherwise be successfully transformed. The remapping is expected to be
     * limited to the hierarchy rooted at the given DOM element (and will
     * include this given element). If additional new elements are required,
     * the given DOM pool can be used to allocate them (if non-null).
     */
    void remap(Element element, ElementHelper helper);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
