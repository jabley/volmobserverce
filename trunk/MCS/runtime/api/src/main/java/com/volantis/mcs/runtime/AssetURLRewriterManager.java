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
 * $Header: /src/voyager/com/volantis/mcs/runtime/AssetURLRewriterManager.java,v 1.2 2002/11/20 13:55:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Nov-02    Phil W-S        VBM:2002111816 - Created. Is used when custom
 *                              asset URL rewriter(s) are to be invoked
 *                              in a chain with the default rewriter.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.repository.RepositoryException;

import java.util.ArrayList;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Allows the default and custom asset URL rewriters to be chained together
 * to perform the over-all URL rewriting. The default rewriter is always
 * invoked first, with the custom rewriter(s) chained after it.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class AssetURLRewriterManager implements AssetURLRewriter {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AssetURLRewriterManager.class);

    /**
     * This is an ordered sequence of asset URL rewriters used to perform
     * the URL rewriting
     * @supplierRole rewriters
     * @link aggregation
     * @supplierCardinality 0..*
     * @associationAsClass <{ArrayList}>
     * @associates <{AssetURLRewriter}>
     */
    protected ArrayList rewriters = new ArrayList();

    /**
     * Initialize the new instance using the given parameters.
     *
     * @param customAssetURLRewriterConfig the configuration value for
     *        the registered custom rewriter(s)
     * @throws ClassNotFoundException
     */
    public AssetURLRewriterManager(String customAssetURLRewriterConfig)
        throws ClassNotFoundException, InstantiationException,
               IllegalAccessException {
        initialize(customAssetURLRewriterConfig);
    }

    protected void initialize(String customAssetURLRewriterConfig)
        throws ClassNotFoundException, InstantiationException,
               IllegalAccessException {
        // A default asset URL rewriter is always the first rewriter in the
        // chain
        rewriters.add(createDefaultRewriter());

        if ((customAssetURLRewriterConfig != null) &&
            (!"".equals(customAssetURLRewriterConfig))) {
            // The configuration value is currently a single, fully qualified
            // java class name that should:
            //
            // 1. exist on the class path used to execute Mariner
            // 2. implement the AssetURLRewriter interface
            // 3. be public
            // 4. have a public zero argument constructor
            // 5. be a thread safe implementation (this cannot be checked)
            try {
                Class customClass =
                    Class.forName(customAssetURLRewriterConfig);
                AssetURLRewriter rewriter =
                    (AssetURLRewriter)customClass.newInstance();

                rewriters.add(rewriter);
            } catch (ClassNotFoundException e) {
                logger.error("custom-asset-url-rewriter-missing", e);
                throw e;
            } catch (InstantiationException e) {
                logger.error("custom-asset-url-rewriter-creation-error", e);
                throw e;
            } catch (IllegalAccessException e) {
                logger.error("custom-asset-url-rewriter-not-accessible", e);
                throw e;
            } catch (ClassCastException e) {
                logger.error("custom-asset-url-rewriter-must-implement", new Object[]{AssetURLRewriter.class.getName()},
                             e);
                throw e;
            }
        }
    }

    /**
     * Factory method for the default asset URL rewriter.
     *
     * @return newly created instance of the default asset URL rewriter.
     */
    protected AssetURLRewriter createDefaultRewriter() {
        return new DefaultAssetURLRewriter();
    }

    public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                      Asset asset,
                                      AssetGroup assetGroup,
                                      MarinerURL marinerURL)
        throws RepositoryException {
        MarinerURL url = marinerURL;
        int i;
        int size = rewriters.size();
        AssetURLRewriter rewriter;

        // Execute the chain of rewriters to generate the final asset URL
        for (i = 0;
             i < size;
             i++) {
            rewriter = (AssetURLRewriter)rewriters.get(i);
            url = rewriter.rewriteAssetURL(requestContext,
                                           asset,
                                           assetGroup,
                                           url);
        }

        return url;
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
