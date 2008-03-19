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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.impl.persistence;

import com.volantis.shared.metadata.jpox.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implemenation of the Bundle interface
 */
public class BundleDAO implements Bundle {

    private List entries;

    BundleDAO() {
        entries = new ArrayList();
    }

    BundleDAO(MetadataDAOVisitor visitor) {
        this.entries = visitor.getEntries();
    }

    List getEntries() {
        return entries;
    }

    // Javadoc inherited
    public int hashCode() {
        return null == entries ? 97123 :  entries.hashCode();
    }

    // javadoc inherited
    public boolean equals(Object obj) {
        boolean equal = this == obj;
        if (!equal && null != obj && getClass()==obj.getClass()) {
            BundleDAO otherBundle = (BundleDAO) obj;

            equal = null != entries ?
                entries.equals(otherBundle.getEntries()) :
                otherBundle.getEntries() == null;
        }
        return equal;
    }
}
