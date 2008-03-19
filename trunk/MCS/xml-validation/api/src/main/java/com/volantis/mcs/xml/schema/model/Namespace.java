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

package com.volantis.mcs.xml.schema.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A definition of the elements within a single namespace.
 */
public class Namespace {

    /**
     * The URI for the namespace.
     */
    private final String uri;

    /**
     * The map from local name to {@link ElementType}.
     */
    private final Map localNameToElementType;

    /**
     * The element prefix to be prepended to element name to get the string
     * representation.
     */
    private final String elementPrefix;

    /**
     * Initialise.
     *
     * @param uri The namespace URI.
     */
    public Namespace(String uri) {
        this(uri, null);
    }

    /**
     * Initialise.
     *
     * @param uri           The namespace URI.
     * @param typicalPrefix The typical prefix for the namespace, if null then
     *                      there is no default, otherwise if empty string then
     *                      elements typically use the default namespace,
     *                      otherwise it is the prefix (excluding : separator).
     */
    public Namespace(String uri, String typicalPrefix) {
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        this.uri = uri;
        localNameToElementType = new HashMap();

        if (typicalPrefix == null) {
            elementPrefix = "{" + uri + "}";
        } else if (typicalPrefix.equals("")) {
            elementPrefix = "";
        } else {
            elementPrefix = typicalPrefix + ":";
        }
    }

    /**
     * Add an element for the specified local name.
     * @param localName The name of the element to add.
     * @return The {@link ElementType} for the newly added element.
     * @throws IllegalStateException If an element is defined twice.
     */
    public ElementType addElement(String localName) {
        if (localName == null) {
            throw new IllegalArgumentException("localName cannot be null");
        }

        ElementType type;
        type = (ElementType) localNameToElementType.get(localName);
        if (type != null) {
            throw new IllegalStateException(
                    "Attempted to add duplicate elements called '" + localName +
                    "' to namespace '" + uri + "'");
        }

        type = new ElementType(uri, localName, elementPrefix + localName);
        localNameToElementType.put(localName, type);

        return type;
    }

    /**
     * Get the URI for the namespace.
     *
     * @return The URI for the namespace.
     */
    public String getURI() {
        return uri;
    }

    /**
     * Get the {@link ElementType} for the specified name.
     *
     * @param localName The element name.
     * @return The {@link ElementType}, or null if it could not be found.
     */
    public ElementType getElementType(String localName) {
        return (ElementType) localNameToElementType.get(localName);
    }


    String getElementPrefix() {
        return elementPrefix;
    }
}
