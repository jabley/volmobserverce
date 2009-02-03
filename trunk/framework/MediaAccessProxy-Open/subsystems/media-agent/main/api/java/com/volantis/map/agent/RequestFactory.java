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
package com.volantis.map.agent;

import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.net.URI;
import java.util.Map;

/**
 * Used to factor {@link Request} instances
 */
public abstract class RequestFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

     static {
        metaDefaultFactory = new MetaDefaultFactory(
                "com.volantis.map.agent.impl.DefaultRequestFactory",
                RequestFactory.class.getClassLoader());
    }

    /**
     * Create a request object for the specified resource type, srcUrl and
     * the input parameters in the supplied map. This Request object is
     * immutable.
     *
     * @param resourceType the type of resource this is
     * @param srcUrl the source url to request (or null if one does not exist)
     * @param inputParameters a name value pair mapping the input properties
     * to use.
     * @return a populated request instance
     */
    public abstract Request createRequest(
        String resourceType,
        URI srcUrl,
        Map inputParameters);

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static RequestFactory getDefaultInstance() {
        return (RequestFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Takes an old style ICS URL and creates a {@link Request} from it
     * @param uri the ICS URL. Cannot be null.
     * @return A {@link Request} instance
     */
    public abstract Request createRequestFromICSURI(URI uri);

    /**
     * Takes an old style ICS URL and creates a {@link Request} from it and passed extra parameters
     * @param uri the ICS URL. Cannot be null.
     * @return A {@link Request} instance
     */
    public abstract Request createRequestFromICSURI(URI uri, Map extraParameters);

}
