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

/**
 * Property element model class.
 */
public class Property {
    
    /**
     * Name of property.
     */
    protected String name;

    /**
     * Value of property.
     */
    protected String value;

    /**
     * Getter for name of property.
     * 
     * @return name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for name of property.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets value of property.
     * 
     * @return value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Setter for value of property.
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

}
