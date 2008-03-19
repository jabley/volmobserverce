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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.styling.engine.Attributes;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@link Attributes} implementation that provides access to attributes in
 * a special fixed namespace.
 */
public class SingleNamespaceAttributes
     implements Attributes {

    private final String namespace;
    private final Map attributes;

    public SingleNamespaceAttributes(String namespace) {
        this.attributes = new HashMap();
        this.namespace = namespace;
    }

    // Javadoc inherited.
    public String getAttributeValue(String namespace, String localName) {

        if (this.namespace == null) {
            if (namespace != null) {
                return null;
            }
        } else if (!this.namespace.equals(namespace)) {
            return null;
        }

        return (String) attributes.get(localName);
    }

    public void setAttributeValue(String localName, String value) {
        attributes.put(localName, value);
    }

    public void clear() {
        attributes.clear();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
