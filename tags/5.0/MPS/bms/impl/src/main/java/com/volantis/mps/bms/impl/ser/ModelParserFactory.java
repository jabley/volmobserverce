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

package com.volantis.mps.bms.impl.ser;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * <p>Abstract Factory for creating the ModelParser implementation.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 */
public abstract class ModelParserFactory {

    private static MetaDefaultFactory metaDefaultFactory = new MetaDefaultFactory(
            "com.volantis.mps.bms.impl.ser.DefaultModelParserFactory",
            ModelParserFactory.class.getClassLoader());

    /**
     * Return the default instance of the ModelParserFactory.
     *
     * @return the default instance of the ModelParserFactory
     */
    public static ModelParserFactory getDefaultInstance() {
        return (ModelParserFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Return a ModelParser implementation - not null.
     *
     * @return a ModelParser implementation.
     */
    public abstract ModelParser createModelParser();
}
