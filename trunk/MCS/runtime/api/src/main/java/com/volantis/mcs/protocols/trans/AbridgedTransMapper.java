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
 * $Header: /src/voyager/com/volantis/mcs/protocols/trans/AbridgedTransMapper.java,v 1.2 2002/10/15 11:13:14 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Created. Allows the optional
 *                              specification of elements that should not be
 *                              recursed through during a DOM visitation.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Element;

/**
 * This interface provides the method that should be invoked to perform a
 * table (tree or element) remapping when a container validator returns the
 * <code>INVERSE_REMAP</code> container action. The <code>REMAP</code>
 * container action is handled by the <code>remap</code> method on
 * <code>TransMapper</code>.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com>Phil W-S</a>
 */
public interface AbridgedTransMapper extends TransMapper {
    /**
     * This method should be invoked to perform the remapping of table
     * elements into alternative non-table ones when a table is found that
     * cannot otherwise be successfully transformed. The remapping is expected
     * to be limited to the hierarchy rooted at the given DOM element (and
     * will include this given element). However, if abridged is set true then
     * elements with names specific to the protocol's specialization of this
     * class will not have their children included in the DOM traversal.
     * If additional new elements are required, the given DOM pool can be
     * used to allocate them (if non-null). 
     */
    void remap(Element element,
               ElementHelper helper,
               boolean abridge);
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
