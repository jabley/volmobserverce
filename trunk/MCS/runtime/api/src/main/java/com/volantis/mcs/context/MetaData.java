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
package com.volantis.mcs.context;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

/**
 * Meta data for a given element or for a whole page.
 *
 * <p>Meta data can store values against property names. Each property name can
 * be associated with a value or with a list of values (but the two must not be
 * mixed for a given property).</p>
 *
 * <p>{@link #setPropertyValue(String, Object)} can be used to set the value for
 * single-value properties and {@link #addPropertyValue(String, Object)} to add
 * a value to multi-value properties.</p>
 */
public class MetaData {
    private Map metaData;

    /**
     * Creates a new meta data object with no properties stored.
     */
    public MetaData() {
    }

    /**
     * Returns the value or list of values stored for the given property.
     *
     * @param property the name of the property to look up
     * @return the value(s) of the property or null
     */
    public Object getPropertyValue(final String property) {
        if (property == null) {
            throw new IllegalArgumentException("Property name cannot be null");
        }
        return metaData == null ? null : metaData.get(property);
    }

    /**
     * Sets the value of a single-value property.
     *
     * @param property the name of the property
     * @param value the new value
     * @return the old value, may return null
     */
    public Object setPropertyValue(final String property, final Object value) {
        if (property == null) {
            throw new IllegalArgumentException("Property name cannot be null");
        }
        if (metaData == null) {
            metaData = new HashMap();
        }
        return metaData.put(property, value);
    }

    /**
     * Adds the specified value to the list of values stored for the property.
     *
     * @param property the name of the property
     * @param value the value to add
     */
    public void addPropertyValue(final String property, final Object value) {
        if (property == null) {
            throw new IllegalArgumentException("Property name cannot be null");
        }
        if (metaData == null) {
            metaData = new HashMap();
        }
        List values = (List) metaData.get(property);
        if (values == null) {
            values = new LinkedList();
            metaData.put(property, values);
        }
        values.add(value);
    }
}
