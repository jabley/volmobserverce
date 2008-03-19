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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MenuChildRendererVisitor.java,v 1.2 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 09-Apr-2003  Sumit       VBM:2003032713 - Rendering interface for visitors
 *                          to render menu child
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.protocols;

/**
 * Rendering interface for visitors to render menu child
 */
public interface MenuChildRendererVisitor {
    public void renderMenuChild(DOMOutputBuffer dom, MenuAttributes attributes, 
        MenuItem child, boolean notLast, boolean iteratorPane,
                MenuOrientation orientation) throws ProtocolException;
    public void renderMenuChild(DOMOutputBuffer dom, MenuAttributes attributes, 
        MenuItemGroupAttributes child, boolean notLast, boolean iteratorPane,
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
