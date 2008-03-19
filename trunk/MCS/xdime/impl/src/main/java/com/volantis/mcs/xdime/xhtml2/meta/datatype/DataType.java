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
package com.volantis.mcs.xdime.xhtml2.meta.datatype;

import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * Class to store data types.
 */
public class DataType {
    private final ImmutableExpandedName type;

    /**
     * Creates a data type instance.
     *
     * @param type the data type, cannot be null or empty
     */
    DataType(final ImmutableExpandedName type) {
        if (type == null ||
            isEmpty(type.getNamespaceURI()) || isEmpty(type.getLocalName())) {

            throw new IllegalArgumentException(
                "Type must have namespace URI and local name.");
        }
        this.type = type;
    }

    public ImmutableExpandedName getExpandedName() {
        return type;
    }

    // javadoc inherited
    public boolean equals(final Object obj) {
        return obj != null && obj.getClass().equals(getClass()) &&
            type.equals(((DataType) obj).type);
    }

    // javadoc inherited
    public int hashCode() {
        return type.hashCode();
    }

    private static boolean isEmpty(final String string) {
        return string == null || string.length() == 0;
    }
}
