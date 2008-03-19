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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.metadata.value.CollectionValue;
import com.volantis.shared.metadata.value.MetaDataValue;

import java.util.Collection;
import java.util.Iterator;

/**
 * Implementation of {@link CollectionValue}.
 */
abstract class CollectionValueImpl
        extends CompositeValueImpl
        implements CollectionValue {

    // Javadoc inherited.
    public String getAsString() {
        Collection collection = getContentsAsCollection();
        String s;
        if (collection.isEmpty()) {
            s = "";
        } else {
            StringBuffer buffer = new StringBuffer();
            for (Iterator i = collection.iterator(); i.hasNext();) {
                MetaDataValue value = (MetaDataValue) i.next();
                if (buffer.length() != 0) {
                    buffer.append(" ");
                }
                buffer.append(value.getAsString());
            }
            s = buffer.toString();
        }

        return s;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
