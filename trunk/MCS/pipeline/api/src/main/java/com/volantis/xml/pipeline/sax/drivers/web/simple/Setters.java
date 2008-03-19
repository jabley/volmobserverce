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

package com.volantis.xml.pipeline.sax.drivers.web.simple;

import com.volantis.xml.namespace.ExpandedName;

import java.util.Map;
import java.util.Iterator;

/**
 * A collection of {@link Setter}s.
 *
 * <p>This consists of separate attribute and element setters. They are stored
 * in the same map as the former uses the {@link String} name of the attribute
 * as a key whereas the latter uses a {@link ExpandedName} as a key.</p>
 */
public class Setters {

    /**
     * The map of element {@link Setter}s.
     */
    private final Map elementSetters;

    /**
     * The map of attribute {@link Setter}s.
     */
    private final Map attributeSetters;

    /**
     * Initialise.
     *
     * @param builder The builder from which this is constructed.
     */
    public Setters(SettersBuilder builder) {
        this.elementSetters = builder.elementSetters;
        this.attributeSetters = builder.attributeSetters;
    }

    /**
     * Get the element setter.
     *
     * @param name The name of the element.
     * @return The setter, or null.
     */
    public Setter getElementSetter(ExpandedName name) {
        return (Setter) elementSetters.get(name);
    }

    /**
     * Get the attribute setter.
     *
     * @param name The name of the attribute.
     * @return The setter, or null.
     */
    public Setter getAttributeSetter(String name) {
        return (Setter) attributeSetters.get(name);
    }

    public Iterator elements() {
        return elementSetters.keySet().iterator();
    }
}
