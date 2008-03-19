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
package com.volantis.mcs.xdime.xhtml2;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.ResponseCallback;
import com.volantis.map.common.param.Parameters;
import com.volantis.mcs.protocols.ElementFinalizer;
import com.volantis.mcs.protocols.ObjectAttribute;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.integration.PageURLType;

/**
 * The callback that will be invoked by the Media Access Proxy when the
 * requested information becomes available.
 */
class MAPResponseCallback implements ResponseCallback {

    private final ObjectAttribute objectAttribute;

    private final URLRewriter urlRewriter;

    public MAPResponseCallback(ObjectAttribute objectAttribute,
                                  URLRewriter urlRewriter) {
        this.objectAttribute = objectAttribute;
        this.urlRewriter = urlRewriter;
    }

    // Javadoc inherited
    public void execute(Parameters params) throws Exception {
        // Rewrite source URL from MediaAgent response, passing it through
        // additional page rewriter.
        objectAttribute.setSrc(urlRewriter.rewrite(
            params.getParameterValue(
                MediaAgent.OUTPUT_URL_PARAMETER_NAME)));

        // Clear the srcType attribute, since the actual MIME type will be
        // known at the moment of the transcoding.
        objectAttribute.setSrcType(null);

        // Get the finalizer and execute it.
        ElementFinalizer finalizer = objectAttribute.getFinalizer();

        finalizer.finalizeElement(objectAttribute);
    }

    /**
     * URL Rewriter.
     */
    interface URLRewriter {

        String rewrite(String url);
    }

    /**
     * Creates and returns an instance of URLRewriter, which should be used
     * just before the URL is passed to the protocol.
     *
     * @param context The XDIME context used.
     * @return The rewriter
     */
    public static URLRewriter createURLRewriter(XDIMEContextInternal context) {
        final MarinerPageContext pageContext =
            ContextInternals.getMarinerPageContext(
                context.getInitialRequestContext());

        return new URLRewriter() {
            public String rewrite(String url) {
                return pageContext.getAssetResolver()
                    .rewriteURLWithPageURLRewriter(url, PageURLType.OBJECT);
            }
        };
    }

}
