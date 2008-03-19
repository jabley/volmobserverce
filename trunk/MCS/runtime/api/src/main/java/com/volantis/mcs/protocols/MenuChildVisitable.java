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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MenuChildVisitable.java,v 1.3 2003/04/24 16:43:22 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 09-Apr-2003  Sumit       VBM:2003032713  - Allows menu items and groups
 *                          to be visited
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.protocols;

/**
 *Allows menu items and groups to be visited
 */
public interface MenuChildVisitable {
    /**
     * Allows a protocol to render an implementor if this interface
     * @param visitor - The visitor that will render this menu child 
     * @param dom - the DOM output buffer to write to
     * @param attributes - the Menu attributes for this menu
     * @param notLast - Is this the last menu child in the menu?
     * @param iteratorPane - Are we rendering to an iterator pane?
     * @param orientation - Does this menu have to be rendered vertically?
     */
    
    public void visit(MenuChildRendererVisitor visitor, DOMOutputBuffer dom, 
            MenuAttributes attributes, boolean notLast, boolean iteratorPane,
            MenuOrientation orientation) throws ProtocolException;

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
