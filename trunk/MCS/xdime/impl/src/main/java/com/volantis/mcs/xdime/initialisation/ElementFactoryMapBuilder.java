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

package com.volantis.mcs.xdime.initialisation;

import com.volantis.mcs.xml.schema.model.ElementType;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for {@link ElementFactoryMap}.
 */
public class ElementFactoryMapBuilder {

    /**
     * The map from {@link ElementType} to {@link ElementFactory}.
     */
    private final Map type2Factory;

    /**
     * Initialise.
     */
    public ElementFactoryMapBuilder() {
        this.type2Factory = new HashMap();
    }

    /**
     * Add a mapping from the {@link ElementType} to {@link ElementFactory}.
     *
     * @param elementType The {@link ElementType}.
     * @param factory     The {@link ElementFactory}.
     */
    public void addMapping(ElementType elementType, ElementFactory factory) {
        if (elementType == null) {
            throw new IllegalArgumentException("elementType cannot be null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("factory cannot be null");
        }
        type2Factory.put(elementType, factory);
    }

    /**
     * Build a {@link ElementFactoryMap}.
     *
     * @return The newly built {@link ElementFactoryMap}.
     */
    public ElementFactoryMap buildFactoryMap() {
        return new ElementFactoryMap(type2Factory);
    }
}
