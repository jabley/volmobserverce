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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MenuItemGroupAttributes.java,v 1.3 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- ---------------------------------------------------
 * 28-Mar-2003  Sumit       VBM:2003032714 - Contains and groups menu items 
 * 10-Aug-2003  Sumit       VBM:2003032713 - Implements the visitable 
 *                          interface
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The menu item group attributes class that maintains menu groups for 
 * a menu
 *
 * @deprecated Old style menu needs to be removed.
 */
public final class MenuItemGroupAttributes extends MCSAttributes
        implements MenuItemCollector, MenuChildVisitable {

    private ArrayList items;

    /**
     * Create a new <code>MenuItemGroupAttributes</code>.
     */
    public MenuItemGroupAttributes() {
        initialise();
    }

    /**
     * Add a menu item to the collection of menu items.
     */
    public void addItem(MenuItem item) {
        items.add(item);
    }

    // Inherit Javadoc.
    public String getElementName() {
        return "menuitemgroup";
    }

    /**
     * Get the collection of menu items.
     */
    public Collection getItems() {
        return items;
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("menuitemgroup");

        if (items == null) {
            items = new ArrayList();
        } else {
            items.clear();
        }

    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Nested menu item groups are not supported 
     */
    public void addGroup(MenuItemGroupAttributes item) {
        throw new UnsupportedOperationException();
    }
    
    public void visit(MenuChildRendererVisitor visitor, DOMOutputBuffer dom, 
                MenuAttributes attributes, boolean notLast, 
                boolean iteratorPane, MenuOrientation orientation) throws ProtocolException {
        visitor.renderMenuChild(dom, attributes, this, notLast, iteratorPane, orientation);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
