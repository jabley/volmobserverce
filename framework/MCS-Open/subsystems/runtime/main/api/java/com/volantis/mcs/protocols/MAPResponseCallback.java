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
 * (c) Copyright Volantis Systems Ltd. 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.ResponseCallback;
import com.volantis.map.common.param.Parameters;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.MAPAttributes;

/**
 * The callback that will be invoked by the Media Access Proxy when the
 * requested information becomes available.
 */
public abstract class MAPResponseCallback implements ResponseCallback {

    protected MAPAttributes mapAttribute;
    protected URLRewriter urlRewriter;

    public MAPResponseCallback(MAPAttributes mapAttribute,
                                  URLRewriter urlRewriter) {
        this.mapAttribute = mapAttribute;
        this.urlRewriter = urlRewriter;
    }

    // Javadoc inherited
    public void execute(Parameters params) throws Exception {

        setSpecificAttributes(params);

        // Get the finalizer and execute it.
        ElementFinalizer finalizer = mapAttribute.getFinalizer();

        finalizer.finalizeElement(mapAttribute);
        mapAttribute.resetAttributes();
    }

    // set appropriate attributes depending on XDIME1 or XDIME2
    // implemented in child class
    protected abstract void setSpecificAttributes(Parameters params) throws Exception;

    /**
     * URL Rewriter.
     */
    public interface URLRewriter {

        String rewrite(String url);
    }

    /**
     * Creates and returns an instance of URLRewriter, which should be used
     * just before the URL is passed to the protocol.
     *
     * @param context The XDIME context used.
     * @return The rewriter
     */
    public static URLRewriter createURLRewriter(MarinerPageContext pageContext, PageURLType pageURLType) {
        final MarinerPageContext context = pageContext;
        final PageURLType pageType = pageURLType;

        return new URLRewriter() {
            public String rewrite(String url) {
                return context.getAssetResolver()
                    .rewriteURLWithPageURLRewriter(url, pageType);
            }
        };
    }

}

