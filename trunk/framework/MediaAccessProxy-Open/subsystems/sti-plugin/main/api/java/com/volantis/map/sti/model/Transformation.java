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
 * Transformation element model class. Describes one transformation attributes.
 */
public class Transformation {
    
    /**
     * Type attribute.
     */
    protected String type;

    /**
     * Attributes.
     */
    protected Properties attributes;

    /**
     * Order of transformations.
     */
    protected int order;

    /**
     * Getter for type of transformation.
     * 
     * @return type of transformation.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for type of transformation.
     * 
     * @param type type of transformation.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for attributes of this transformation.
     * 
     * @return attributes of this transformation.
     */
    public Properties getAttributes() {
        return this.attributes;
    }

    /**
     * Setter for attributes of this transformation.
     * 
     * @param attributes attributes of this transformation.
     */
    public void setAttributes(Properties attributes) {
        this.attributes = attributes;
    }

    /**
     * Getter for order of this transformation.
     * 
     * @return order of this transformation.
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * Setter for order of this transformation.
     * 
     * @param order order of this transformation.
     */
    public void setOrder(int order) {
        this.order = order;
    }
    
    /**
     * Adds attribute of given name and value.
     *  
     * @param name attribute name
     * @param value attribute value
     */
    public void addAttribute(String name, String value) {
        if (attributes == null) {
            attributes = new Properties();
        }
        
        attributes.addProperty(name, value);
    }
}
