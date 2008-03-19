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

import com.volantis.xml.namespace.MutableExpandedName;

/**
 * Find a {@link Setter} from a set of them.
 */
public class SetterFinder {

    /**
     * The set of {@link Setter}s.
     */
    private final Setters setters;

    /**
     * The name used to perform the search.
     */
    private final MutableExpandedName searchName;

    /**
     * Initialise.
     *
     * @param setters The set of {@link Setter}s.
     */
    public SetterFinder(Setters setters) {
        this.setters = setters;
        searchName = new MutableExpandedName();
    }

    /**
     * Find an element setter.
     *
     * @param namespaceURI The namespace URI of the element.
     * @param localName    The local name of the element.
     * @return The setter, or null.
     */
    public Setter findElementSetter(String namespaceURI, String localName) {
        searchName.setNamespaceURI(namespaceURI);
        searchName.setLocalName(localName);
        return setters.getElementSetter(searchName);
    }

    /**
     * Find an unqualified attribute setter.
     *
     * @param name The name of the attribute.
     * @return The setter, or null.
     */
    public Setter findAttributeSetter(String name) {
        return setters.getAttributeSetter(name);
    }
}
