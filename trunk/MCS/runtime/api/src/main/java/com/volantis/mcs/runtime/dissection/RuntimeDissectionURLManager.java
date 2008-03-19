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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.dissection;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.DissectionContext;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.DissectionURLManager;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.PageURLDetailsFactory;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

public class RuntimeDissectionURLManager
    implements DissectionURLManager {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(RuntimeDissectionURLManager.class);

    private static final String LARGE_FRAGMENTATION_SPECIFIER
        = "F999.C99";

    private PageGenerationCache pageGenerationCache;

    public RuntimeDissectionURLManager(PageGenerationCache pageGenerationCache) {
        this.pageGenerationCache = pageGenerationCache;
    }

    private int getChangeIndex(DissectableAreaIdentity identity,
                               int delta) {

        String inclusionPath = identity.getInclusionPath();
        String name = identity.getName();

        FragmentationState.Change change;
        change = new FragmentationState.ShardChange(inclusionPath,
                                                    name, delta);

        return pageGenerationCache.getFragmentationStateChangeIndex(change);
    }

    public int getNextChangeIndex(DissectableAreaIdentity identity) {
        return getChangeIndex(identity, +1);
    }

    public int getPreviousChangeIndex(DissectableAreaIdentity identity) {
        return getChangeIndex(identity, -1);
    }

    private MarinerURL makeURL(MarinerPageContext context,
                               MarinerURL documentURL,
                               String fragmentationSpecifier)
        throws DissectionException {

        if (documentURL.isReadOnly()) {
            documentURL = new MarinerURL(documentURL);
        }

        // Set the request parameter.
        documentURL.setParameterValue(URLConstants.FRAGMENTATION_PARAMETER,
                                      fragmentationSpecifier);

        // Get the URLRewriter to use to encode session information in the
        // URL and use it, do this before stripping the URL as otherwise it is
        // not done properly.
        MarinerRequestContext requestContext = context.getRequestContext();
        URLRewriter sessionURLRewriter = context.getSessionURLRewriter();
        MarinerURL sessionURL
            = sessionURLRewriter.mapToExternalURL(requestContext,
                                                  documentURL);

        if (logger.isDebugEnabled()) {
            logger.debug("Encoded url is "
                         + sessionURL.getExternalForm());
        }

        // As the link we generate is always back to this page we can reduce the
        // overhead by removing the protocol, authority and all but the last part
        // of the path. We do this before URL rewriting as we have no idea what
        // the URL will look like afterwards.
        sessionURL.setProtocol(null);
        sessionURL.setAuthority(null);
        String path = sessionURL.getPath();
        int index = path.lastIndexOf('/');
        if (index != -1) {
            path = path.substring(index + 1);
            sessionURL.setPath(path);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Relative url is "
                         + sessionURL.getExternalForm());
        }

        // perform any URL rewriting that might be needed
        PageURLRewriter urlRewriter = context.getVolantisBean().
                getLayoutURLRewriter();
        MarinerURL externalURL =
                urlRewriter.rewriteURL(context.getRequestContext(),
                sessionURL,
                PageURLDetailsFactory.createPageURLDetails(PageURLType.FRAGMENT));

        return externalURL;
    }

    public MarinerURL makeURL(DissectionContext dissectionContext,
                              MarinerURL documentURL,
                              int changeIndex)
        throws DissectionException {

        RuntimeDissectionContext runtimeDissectionContext
            = (RuntimeDissectionContext) dissectionContext;

        MarinerPageContext context
            = runtimeDissectionContext.getMarinerPageContext();

        int currentIndex = context.getFragmentationIndex();

        // Generate the value of the request parameter.
        String value
            = PageGenerationCache.makeShardChangeSpecifier(currentIndex,
                                                           changeIndex);

        return makeURL(context, documentURL, value);
    }

    public MarinerURL makePathologicalURL(DissectionContext dissectionContext,
                                          MarinerURL documentURL)
        throws DissectionException {

        RuntimeDissectionContext runtimeDissectionContext
            = (RuntimeDissectionContext) dissectionContext;

        MarinerPageContext context
            = runtimeDissectionContext.getMarinerPageContext();

        return makeURL(context, documentURL, LARGE_FRAGMENTATION_SPECIFIER);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
