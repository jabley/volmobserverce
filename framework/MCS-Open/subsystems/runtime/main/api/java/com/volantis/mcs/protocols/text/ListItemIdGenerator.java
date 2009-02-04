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
 * $Header: /src/voyager/com/volantis/mcs/protocols/text/ListItemIdGenerator.java,v 1.2 2003/01/29 14:30:57 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Nov-02    Geoff           VBM:2002103005 - Created. 
 * 14-Nov-02    Geoff           VBM:2002103005 - Phase 2, remove unneeded code
 *                              for attributes of list items, we don't need it.
 * 27-Nov-02    Geoff           VBM:2002103005 - Use refactored writer.
 * 29-Jan-03    Adrian          VBM:2003012104 - Refactored from 
 *                              SMSListItemIdGenerator - made addNextListItemId
 *                              method public instead of package private. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.text;

/**
 * Abstract class for generating list item ids for emulated OL and UL lists
 * for SMS.
 */ 
public abstract class ListItemIdGenerator {

    /**
     * Add a list item id to the supplied writer, using the attributes 
     * supplied.
     * 
     * @param writer The writer to add the list item id to.
     */ 
    public abstract void addNextListItemId(QuietLogicalWhitespaceWriter writer);
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
