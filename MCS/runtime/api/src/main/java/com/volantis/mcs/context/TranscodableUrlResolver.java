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
package com.volantis.mcs.context;

import com.volantis.mcs.repository.RepositoryException;

/**
 * To resolve transcodable URLs.
 */
public class TranscodableUrlResolver {
    private final MarinerRequestContext requestContext;

    public TranscodableUrlResolver(final MarinerRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Resolves a transcodable image URL.
     *
     * @param transcodableUrl the URL to resolve
     * @return the resolved URL
     * @throws RepositoryException if there is an error resolving the URL.
     */
    public String resolve(final String transcodableUrl)
            throws RepositoryException {
        return ContextInternals.constructImageURL(
            requestContext, transcodableUrl);
    }

    /**
     * Returns the {@link MarinerRequestContext} associated with this resolver.
     */
    MarinerRequestContext getRequestContext() {
        return requestContext;
    }
}
