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

import com.volantis.shared.inhibitor.Inhibitor;
import com.volantis.shared.metadata.MetaDataObject;
import com.volantis.shared.metadata.jpox.Bundle;
import com.volantis.shared.metadata.jpox.PersistenceConvertor;
import com.volantis.synergetics.jdo.JDOPersistence;

import java.util.Properties;

/**
 * This is used by test cases
 */
public class MetadataJDOPersistence {

    private JDOPersistence persistence;

    /**
     * Create a JDO Persistence instance with the specified drivername,
     * connection url, username and password.
     *
     * @param persistence the JDO persistence object to delegate to
     * @throws Exception
     */
    private MetadataJDOPersistence(JDOPersistence persistence)
        throws Exception {
        this.persistence = persistence;
    }

    public static MetadataJDOPersistence getInstance(Properties properties)
        throws Exception {
        return new MetadataJDOPersistence(JDOPersistence.getInstance(properties));
    }

    public void destroy() {
        persistence.destroy();
    }

    public Object persistObject(MetaDataObject object) {
        Bundle bundle = PersistenceConvertor.
            getDefaultInstance().convertToBundleDAO(object);
        return persistence.persistObject(bundle);
    }

    public Inhibitor retrieveObject(Object id) {
        Bundle bundle = (Bundle)persistence.retrieveObject(id);
        return PersistenceConvertor.
            getDefaultInstance().convertToInhibitor(bundle);
    }
}
