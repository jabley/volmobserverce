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

import com.volantis.shared.metadata.jpox.PersistenceConvertor;
import com.volantis.shared.metadata.jpox.Bundle;
import com.volantis.shared.metadata.impl.MetaDataObjectImpl;
import com.volantis.shared.metadata.impl.inhibitor.InhibitorImpl;
import com.volantis.shared.inhibitor.Inhibitor;

/**
 * Default implemenation of the persistence convertor
 */
public class DefaultPersistenceConvertor extends PersistenceConvertor {

    // Javadoc inherited
    public Inhibitor convertToInhibitor(Bundle bundle) {
        BundleDAO bun = (BundleDAO) bundle;
        MetadataDAOVisitor visitor = new MetadataDAOVisitor(bun);
        InhibitorImpl inhibitorImpl = visitor.getNextAsInhibitor();
        return inhibitorImpl;
    }

    // Javadoc inherited
    public Bundle convertToBundleDAO(Inhibitor inhibitor) {
        BundleDAO bundle;
        if (null != inhibitor) {
            MetadataDAOVisitor visitor = new MetadataDAOVisitor();
            ((MetaDataObjectImpl) inhibitor).visitInhibitor(visitor);
            bundle = new BundleDAO(visitor);
        } else {
            bundle = new BundleDAO();
        }
        return bundle;
    }
}
