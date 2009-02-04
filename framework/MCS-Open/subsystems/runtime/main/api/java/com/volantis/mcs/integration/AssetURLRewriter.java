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
 * $Header: /src/voyager/com/volantis/mcs/integration/AssetURLRewriter.java,v 1.5 2003/04/01 15:10:52 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Jan-02    Doug            VBM:2002011406 - Created.
 *                              Interface that allows asset URL's to be
 *                              rewritten
 * 18-Mar-02    Ian             VBM:2002031203 - Changed log4j Category from
 *                              class to string.
 * 19-Nov-02    Phil W-S        VBM:2002111816 - Updated the javadoc to be
 *                              appropriate for public API consumption.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 01-Apr-03    Phil W-S        VBM:2003032105 - Ensure that the javadoc covers
 *                              the read-only issue for the inbound MarinerURL.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.integration;

import com.volantis.mcs.utilities.MarinerURL;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.repository.RepositoryException;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;

/**
 * This interface allows implementations to be written that perform URL
 * rewriting for Assets.
 * <p>A custom implementation of this interface can be created to perform
 * extended URL rewriting. This implementation must be registered in the
 * <strong>mcs-config.xml</strong> file using the
 * <strong>asset-url-rewriter</strong> attribute of the <strong>plugin</strong>
 * element in order to be used.</p>
 * <p>The custom implementation must be written to be thread safe, since only
 * a single instance will be constructed for a given invocation of Mariner.
 * Ideally, the implementation should be written without the need for
 * synchronization (i.e. it should not hold internal state between
 * invocations).</p>
 * <p>The custom implementation must be a public class and must provide a
 * public no argument constructor.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface AssetURLRewriter {
    /**
     * Allows an Asset URL to be rewritten. <p>If the <code>marinerURL</code>
     * is read-only (which can be determined by calling {@link
     * MarinerURL#isReadOnly}), any Asset URL rewriting must be performed on an
     * explicitly constructed alternative MarinerURL instance.</p><p>If the
     * <code>marinerURL</code> is not read-only rewriting can be performed
     * using this instance or an explicitly constructed alternative one.</p>
     * <p>The rewritten MarinerURL should then be returned.</p>
     *
     * @param requestContext the Mariner request context
     * @param asset          the asset associated with the URL
     * @param assetGroup     the asset&apos;s associated asset group
     * @param marinerURL     the URL that is to be rewritten
     * @return the rewritten URL as a MarinerURL object
     * @throws RepositoryException           if an error occurs during
     *                                       rewriting
     * @throws UnsupportedOperationException if an attempt is made to modify
     *                                       a read-only MarinerURL instance
     */
    public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                      Asset asset,
                                      AssetGroup assetGroup,
                                      MarinerURL marinerURL)
        throws RepositoryException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
