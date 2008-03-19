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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/mime/MultipartApplicationContext.java,v 1.5 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Created. Implements the
 *                              PackageResources to minimize garbage and to
 *                              provide the package resources implementation
 *                              required for MIME multipart packaging.
 * 14-Feb-03    Phil W-S        VBM:2003021402 - Fix JDK1.2 compiler
 *                              compatibility issue (bug in 1.2 compiler?)
 * 21-Feb-03    Phil W-S        VBM:2003022006 - Add implementation of
 *                              initializeEncodedURLs and updated
 *                              addEncodeURLCandidate to call it if needed.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers.mime;

import com.volantis.mcs.context.DefaultApplicationContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.runtime.packagers.PackageResources;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This specialization of the ApplicationContext exists to minimize the
 * number of per-request objects involved in the MCS response packaging
 * mechanism (specifically in the MIME multi-part implementation, though
 * it could be re-packaged and renamed to make it more general). Since the
 * ApplicationContext instances are already per-request, all per-request
 * packaging state data is stored here too by having this object implement
 * the PackageResources interface and by initializing the PackageResources
 * instance to this object in the constructor. If the PackageResources
 * instance is reset via the {@link #setPackageResources} method this
 * garbage optimization is removed but the context will continue to function
 * as a normal ApplicationContext.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class MultipartApplicationContext extends DefaultApplicationContext
    implements PackageResources {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MultipartApplicationContext.class);

    /**
     * The content type registered with the PackageResources.
     */
    protected String contentType = null;

    /**
     * The map of "encoded" asset URL to plain asset URL built up via the
     * PackageResources API.
     */
    protected Map assetURLMap = new HashMap();

    /**
     * The list of "encoded" asset URLs built up via the PackageResources API.
     * Must be null until initializeEncodedURLs is directly or indirectly
     * invoked.
     */
    protected List encodedURLs = null;

    /**
     * Initialise the new instance using the given parameters. The
     * packageResources member is initialized to point to this object. This
     * doesn't prevent later calls to setPackageResources from installing
     * a different object as the PackageResources instance in the
     * Application Context.
     *
     * @param requestContext the request context
     */
    public MultipartApplicationContext(MarinerRequestContext requestContext) {
        super(requestContext);

        packageResources = this;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map getAssetURLMap() {
        return assetURLMap;
    }

    public void addAssetURLMapping(String encoded,
                                   PackageResources.Asset asset) {
        assetURLMap.put(encoded, asset);
    }

    public List getEncodedURLs() {
        return encodedURLs;
    }

    public void addEncodedURLCandidate(String encoded) {
        if (encodedURLs == null) {
            initializeEncodedURLs();
        }

        if (assetURLMap.containsKey(encoded) &&
            !encodedURLs.contains(encoded)) {
            encodedURLs.add(encoded);

            if(logger.isDebugEnabled()){
                logger.debug("Added encoded asset URL \"" + encoded + "\" to " +
                         "the set of dissection assets");
            }
        }
    }

    public void initializeEncodedURLs() {
        if (encodedURLs == null) {
            encodedURLs = new ArrayList();
        }
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

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
