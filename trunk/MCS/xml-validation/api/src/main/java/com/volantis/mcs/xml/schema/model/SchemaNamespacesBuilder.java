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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xml.schema.model;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.HashMap;
import java.util.Map;

public class SchemaNamespacesBuilder
        implements SchemaNamespaces {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    SchemaNamespacesBuilder.class);

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SchemaNamespacesBuilder.class);

    protected final Map uri2Namespace;

    public SchemaNamespacesBuilder() {
        uri2Namespace = new HashMap();
    }

    public void addNamespace(Namespace namespace) {
        uri2Namespace.put(namespace.getURI(), namespace);
    }

    public ElementType getElementType(String namespaceURI, String localName)
            throws IllegalArgumentException {
        
        Namespace ns = (Namespace) uri2Namespace.get(namespaceURI);
        if (ns == null) {
            // This is an unknown name space
            String elementName = "{" + namespaceURI + "}" + localName;
            LOGGER.error(("unknown-namespace-for-element"), elementName);
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "unknown-namespace-for-element", elementName));
        }

        ElementType elementType = ns.getElementType(localName);
        if (elementType == null) {
            // This is an unknown element
            String elementName = "{" + namespaceURI + "}" + localName;
            LOGGER.error("unknown-element", elementName);
            throw new IllegalArgumentException(EXCEPTION_LOCALIZER.format(
                    "unknown-element", elementName));
        }
        return elementType;
    }
}
