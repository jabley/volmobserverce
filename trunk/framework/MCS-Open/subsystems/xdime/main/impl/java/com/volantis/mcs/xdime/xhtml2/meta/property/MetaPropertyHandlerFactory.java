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
package com.volantis.mcs.xdime.xhtml2.meta.property;

import com.volantis.mcs.protocols.TimedRefreshInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for property handlers.
 */
public class MetaPropertyHandlerFactory {

    public static final String FRAGMENT_LINK_LABEL = "fragment-link-label";
    public static final String ENCLOSING_FRAGMENT_LINK_LABEL =
        "enclosing-fragment-link-label";

    public static final String CACHE_SCOPE = "cache-scope";

    public static final String MAX_AGE = "max-age";

    public static final String CACHE_AUTO = "cache-auto";

    public static final String NO_CACHE = "no-cache";

    public static final String CACHE_MAX_AGE = "cache-max-age";

    public static final String CACHE_EXPIRES = "cache-expires";

    public static final String DESCRIPTION = "description";

    public static final String KEYWORDS = "keywords";

    public static final String AUTHOR = "creator";       

    private static final Map PROPERTY_HANDLERS;

    static {
        PROPERTY_HANDLERS = new HashMap();
        PROPERTY_HANDLERS.put(FRAGMENT_LINK_LABEL,
            new FragmentLinkMetaPropertyHandler());
        PROPERTY_HANDLERS.put(ENCLOSING_FRAGMENT_LINK_LABEL,
            new FragmentLinkMetaPropertyHandler());
        PROPERTY_HANDLERS.put(CACHE_SCOPE, new CacheScopeMetaPropertyHandler());
        PROPERTY_HANDLERS.put(MAX_AGE, new MaxAgeMetaPropertyHandler());
        PROPERTY_HANDLERS.put(CACHE_AUTO, new CacheAutoMetaPropertyHandler());
        PROPERTY_HANDLERS.put(NO_CACHE, new NoCacheMetaPropertyHandler());
        PROPERTY_HANDLERS.put(CACHE_MAX_AGE,
            new CacheMaxAgeMetaPropertyHandler());
        PROPERTY_HANDLERS.put(CACHE_EXPIRES,
            new CacheExpiresMetaPropertyHandler());
        PROPERTY_HANDLERS.put(TimedRefreshInfo.NAME,
            new RefreshMetaPropertyHandler());

        // search engines meta tags
        SearchEngineMetaPropertyHandler searchEngineMetaHandler = new SearchEngineMetaPropertyHandler();
        PROPERTY_HANDLERS.put(DESCRIPTION, searchEngineMetaHandler);
        PROPERTY_HANDLERS.put(KEYWORDS, searchEngineMetaHandler);
        PROPERTY_HANDLERS.put(AUTHOR, searchEngineMetaHandler);                
    }

    private MetaPropertyHandlerFactory() {
        // hide it
    }

    /**
     * Returns the property handler registered for the specified name.
     *
     * May return null.
     *
     * @param name the name of the handler
     * @return the property handler or null
     */
    public static MetaPropertyHandler getHandler(final String name) {
        return (MetaPropertyHandler) PROPERTY_HANDLERS.get(name);
    }
}
