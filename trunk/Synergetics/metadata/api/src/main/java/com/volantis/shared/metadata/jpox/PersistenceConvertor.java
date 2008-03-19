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
package com.volantis.shared.metadata.jpox;

import com.volantis.shared.inhibitor.Inhibitor;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * This class allows inhibitors to be converted to bundles and vice versa for
 * database persistence needs
 */
public abstract class PersistenceConvertor {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory DEFAULT_INSTANCE =
        new MetaDefaultFactory(
            "com.volantis.shared.metadata.impl.persistence." +
                "DefaultPersistenceConvertor",
            PersistenceConvertor.class.getClassLoader());

    /**
     * @return the default instance of the service.
     */
    public static PersistenceConvertor getDefaultInstance() {
        return (PersistenceConvertor)
            PersistenceConvertor.DEFAULT_INSTANCE.getDefaultFactoryInstance();
    }

    /**
     * Convert an {@link Inhibitor} into a {@link Bundle}
     * @param inhibitor the inhibitor to convert
     * @return a Bundle
     */
    public abstract Bundle convertToBundleDAO(Inhibitor inhibitor);


    /**
     * Convert a {@link Bundle} into an {@link Inhibitor}
     *
     * @param bundle the bundle to convert
     * @return an inhibitor
     */
    public abstract Inhibitor convertToInhibitor(Bundle bundle);

}
