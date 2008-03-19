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
package com.volantis.map.sti.model;

import java.util.ArrayList;

/**
 * Properties model class.
 */
public class Properties {
    
    /**
     * Adds property.
     * 
     * @param property
     */
    public void addProperty(Property property) {
        propertyList.add(property);
    }

    /**
     * Gets property at specified index.
     * 
     * @param index index of property to return.
     * @return property.
     */
    public Property getProperty(int index) {
        return (Property) propertyList.get(index);
    }

    /**
     * Returns number of properties.
     * 
     * @return size of properties list.
     */
    public int sizePropertyList() {
        return propertyList.size();
    }

    /**
     * Array list with properties.
     */
    protected ArrayList propertyList = new ArrayList();

    /**
     * Adds property with given name and value.
     * 
     * @param name property name
     * @param value property value
     */
    public void addProperty(String name, String value) {
        Property property = new Property();
        
        property.setName(name);
        property.setValue(value);
        
        addProperty(property);
    }
}
