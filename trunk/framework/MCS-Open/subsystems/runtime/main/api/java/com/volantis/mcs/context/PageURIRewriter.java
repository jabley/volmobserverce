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
package com.volantis.mcs.context;

import java.net.URI;

import com.volantis.mcs.integration.PageURLType;

/**
 * Allows to rewrite an URI before it's written to the page.
 */
public interface PageURIRewriter {
    /**
     * Rewrites the specified URI.
     * 
     * @param uri The URI to rewrite.
     * @param type The type of the URI to rewrite.
     * @return The rewritten URI.
     */
    URI rewrite(URI uri, PageURLType type);

    /**
     * Returns true, if this rewriter in its current state will possibly rewrite
     * the URI of specified type, or false if it will not be rewritten for sure.
     * May be used in optimisations.
     * 
     * @param type The type of the URI.
     * @return true, if the URL will possibly be rewritten.
     */
    public boolean willPossiblyRewrite(PageURLType type);
}
