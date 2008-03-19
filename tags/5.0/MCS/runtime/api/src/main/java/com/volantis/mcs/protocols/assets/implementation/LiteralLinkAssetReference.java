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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * Wraps a literal string URL as a reference to an link asset.
 */
public final class LiteralLinkAssetReference
        implements LinkAssetReference {

    /**
     * The literal URL.
     */
    private final String url;
    
    /**
     * The asset resolver, used to rewrite the url. 
     */
    private final AssetResolver resolver;
    
    /**
     * The URL type. 
     */
    private final PageURLType urlType;

    /**
     * Initialise this new instance.
     *
     * @param url The literal URL to wrap.
     */
    public LiteralLinkAssetReference(String url) {
        this(url, null, null);
    }

    /**
     * Initialise this new instance.
     *
     * @param url The literal URL to wrap.
     * @param resolver The asset resolver used to rewrite the URL.
     * @param urlType The URL type, used by the URL rewriter.
     */
    public LiteralLinkAssetReference(String url, AssetResolver resolver, PageURLType urlType) {
        this.url = url;
        this.resolver = resolver;
        this.urlType = urlType;
    }

    /**
     * Return the literal URL.
     */
    // Javadoc inherited.
    public String getURL() {
        if (resolver == null || urlType == null) {
            return url;
        } else {
            return resolver.rewriteURLWithPageURLRewriter(url, urlType);
        }
    }

    public TextAssetReference getTextFallback() {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
