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
package com.volantis.mcs.policies.variants.metadata;

import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.util.List;

/**
 * Factory to create encoding collections.
 */
public abstract class EncodingCollectionFactory {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory =
        new MetaDefaultFactory(
            "com.volantis.mcs.policies.variants.metadata.DefaultEncodingCollectionFactory",
            EncodingCollectionFactory.class.getClassLoader());

    /**
     * @return the default instance of the factory.
     */
    public static EncodingCollectionFactory getDefaultInstance() {
        return (EncodingCollectionFactory)
            metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Creates an encoding collection from the specified list of encodings.
     */
    public abstract EncodingCollection createEncodingCollection(List encodings);

    /**
     * Creates an encoding collection that only contains the specified encoding.
     */
    public abstract EncodingCollection createEncodingCollection(Encoding encoding);
}
