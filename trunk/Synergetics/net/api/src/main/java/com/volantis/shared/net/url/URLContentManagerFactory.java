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

package com.volantis.shared.net.url;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * A factory for {@link URLContentManager} related classes.
 */
public abstract class URLContentManagerFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
            new MetaDefaultFactory(
                "com.volantis.shared.net.impl.url.URLContentManagerFactoryImpl",
                URLContentManagerFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static URLContentManagerFactory getDefaultInstance() {
        return (URLContentManagerFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create a {@link URLContentManager} for managing content retrieved from
     * a remote URL.
     *
     * @param configuration configuration object for the additional parameters,
     * may not be null
     * @return The newly created  {@link URLContentManager}.
     */
    public abstract URLContentManager createContentManager(
        URLContentManagerConfiguration configuration);
}
