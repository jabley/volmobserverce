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

import java.util.HashMap;
import java.util.Map;

/**
 * A builder for the {@link Setters}.
 */
public class SettersBuilder {

    /**
     * The map of element setters.
     */
    final Map elementSetters;

    /**
     * The map of attribute setters.
     */
    final Map attributeSetters;

    /**
     * Initialise.
     */
    public SettersBuilder() {
        elementSetters = new HashMap();
        attributeSetters = new HashMap();
    }

    /**
     * Add a setter that applies to both elements and unqualified
     * attributes.
     *
     * <p>The attribute setter is stored using the local name of the
     * element.</p>
     *
     * @param name   The name of the element.
     * @param setter The setter.
     */
    public void addCombinedSetter(ExpandedName name, Setter setter) {
        elementSetters.put(name, setter);
        addAttributeSetter(name.getLocalName(), setter);
    }

    /**
     * Add a setter that applies only to unqualified attributes.
     *
     * @param name   The name of the attribute.
     * @param setter The setter.
     */
    public void addAttributeSetter(String name, Setter setter) {
        attributeSetters.put(name, setter);
    }

    /**
     * Build the {@link Setters} from this.
     *
     * @return The newly built {@link Setters}.
     */
    public Setters buildSetters() {
        return new Setters(this);
    }
}
