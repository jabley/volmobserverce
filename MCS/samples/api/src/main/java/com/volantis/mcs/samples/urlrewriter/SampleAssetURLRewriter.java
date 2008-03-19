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
 * $Header: /src/voyager/com/volantis/mcs/samples/SampleAssetURLRewriter.java,v 1.3 2003/04/01 15:10:52 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Nov-02    Phil W-S        VBM:2002111816 - Created. Demonstrates how
 *                              to write a custom asset URL rewriter to
 *                              add a session ID and query parameters.
 * 01-Apr-03    Phil W-S        VBM:2003032105 - Updated after changes made
 *                              under 2003012712 that can mean that the
 *                              MarinerURL passed in can be read-only.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.samples.urlrewriter;

import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.servlet.MarinerServletRequestContext;

import javax.servlet.http.HttpServletResponse;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This sample shows how to write a custom asset URL rewriter to include the
 * session ID (as needed) on the asset URL. In addition, it demonstrates how
 * a query parameter can also be added (whether or not the given URL is
 * set read-only).
 * <p>To use this class the mcs-config.xml must be updated to add or
 * update the plugins element, setting the asset-url-rewriter attribute
 * to "com.volantis.mcs.samples.urlrewriter.SampleAssetURLRewriter" (this attribute must,
 * at the time of writing this sample, identify the fully qualified java
 * class name for the rewriter). Finally, the class must be on the class path
 * for the invocation of the Mariner application.</p>
 * <p>The {@link com.volantis.mcs.integration.AssetURLRewriter} specifically
 * states that this class must be public, must have a public no argument
 * constructor and must implement that interface. It also states that the
 * implementation must be thread safe.</p>
 */
public class SampleAssetURLRewriter implements AssetURLRewriter {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(SampleAssetURLRewriter.class);

    /**
     * Explicitly provided to conform to the requirements defined in
     * the AssetURLRewriterManager.
     */
    public SampleAssetURLRewriter() {
    }

    public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                      Asset asset,
                                      AssetGroup assetGroup,
                                      MarinerURL marinerURL)
        throws RepositoryException {
        MarinerURL url = marinerURL;

        try {
            // Encoding a URL using the servlet response will add the
            // session ID if needed. If the session ID is not needed, the
            // original URL is returned unchanged.
            String externalURL = marinerURL.getExternalForm();
            String encodedURL;
            MarinerServletRequestContext context =
                (MarinerServletRequestContext)requestContext;
            HttpServletResponse servletResponse = context.getHttpResponse();
            encodedURL = servletResponse.encodeURL(externalURL);

            if (!encodedURL.equals(externalURL)) {
                // The URL was modified, so convert it back into a mariner
                // URL
                url = new MarinerURL(encodedURL);
            } else if (url.isReadOnly()) {
                // The given URL is read-only so a clone must be made to
                // allow the query parameter to be added (below)
                url = new MarinerURL(url);
            }
        } catch (Exception e) {            
            url = marinerURL;
        }

        // Add a query parameter, just to demonstrate this capability
        url.setParameterValue("query", "dearie");

        return url;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Jul-04	4801/3	allan	VBM:2004070113 Sample PageURLRewriter java and jsp.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
