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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.ticker;

import com.volantis.mcs.protocols.ticker.attributes.ItemDisplayAttributes;
import com.volantis.mcs.protocols.ticker.attributes.ItemPropertyAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.widgets.Dismissable;

/**
 * ItemDisplay element.
 */
public class ItemDisplayElement extends TickerElement implements Dismissable {
       
    /**
     * Creates and returns new instance of ItemDisplayElement, 
     * initalised with empty attributes.
     * @param context
     */
    public ItemDisplayElement(XDIMEContextInternal context) {
        super(TickerElements.ITEM_DISPLAY, context);
        
        protocolAttributes = new ItemDisplayAttributes();        
    }  

    /**
     * Return id of a dismissable widget
     */
    public String getDismissableId() {
        return protocolAttributes.getId();
    }
    
    /**
     * Add id and type for all item property elements in popup content
     */
    public void addItemProperty(ItemPropertyAttributes attrs) {
        ((ItemDisplayAttributes) protocolAttributes).addItemProperty(attrs);
    }    

    // javadoc inherited
    protected boolean suppressSkipForDisplayNoneStyle() {
        return true;
    }
}
