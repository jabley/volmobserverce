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

package com.volantis.mcs.protocols.ticker.attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ItemDisplay element attributes.
 */
public class ItemDisplayAttributes extends TickerAttributes {
    
    /**
     * List of item properties  
     */
    private final ArrayList itemProperties = new ArrayList();

    /**
     * Adds attributes of a item property element.
     * 
     * @param attrs The attributes of a item property element.
     */
    public void addItemProperty(ItemPropertyAttributes attrs) {
        itemProperties.add(attrs);
    }

    /**
     * Returns list of already added item property attributes.
     * 
     * @return The list of item property attributes
     */
    public List getItemProperties() {
        return Collections.unmodifiableList(itemProperties);        
    }    
}
