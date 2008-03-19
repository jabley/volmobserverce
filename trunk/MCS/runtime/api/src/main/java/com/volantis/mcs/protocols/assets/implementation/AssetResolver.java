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

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;

/**
 * This class provides a means of resolving an asset.  It is a callback facade
 * to any implementation that is capable of doing the asset resolution.
 *
 * @mock.generate
 */
public interface AssetResolver {

    /**
     * Retrieve the URL for an asset as a String.  This method should resolve
     * the URL based on the given asset, the current request context, and any
     * asset groups for the asset.
     *
     * @param asset The asset which should have its URL computed
     * @return      A string that contains the URL for the provided asset.
     *
     * @deprecated Use {@link #computeURLAsString(SelectedVariant)}
     */
    String computeURLAsString(Asset asset);


    /**
     * Get the textual content of a variant.
     * 
     * @param selected The selected variant.
     * @return The textual content, may be null.
     */
    String getContentsFromVariant(SelectedVariant selected);

    /**
     * Retrieve the URL of the best variant of the referenced policy.
     *
     * @param reference The reference of the policy.
     * @param requiredEncodings
     * @return The URL of the best variant.
     */
    String retrieveVariantURLAsString(
            RuntimePolicyReference reference,
            EncodingCollection requiredEncodings);

    /**
     * Retrieve the URL of the best variant of the referenced policy.
     *
     * @param selected The selected variant.
     * @return The URL of the best variant.
     */
    String retrieveVariantURLAsString(SelectedVariant selected);

    /**
     * Select the best variant.
     *
     * @param reference The reference to a policy.
     * @param requiredEncodings The required encodings, may be null.
     *
     * @return The selected variant.
     */
    SelectedVariant selectBestVariant(RuntimePolicyReference reference,
                                      EncodingCollection requiredEncodings);

    String rewriteURLWithPageURLRewriter(String url, PageURLType urlType);

    String computeURLAsString(SelectedVariant selectedVariant);

    AssetGroup getAssetGroup(Asset asset);

    BaseLocation getBaseLocation(SelectedVariant selected);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 12-Jul-05	8862/3	pduffin	VBM:2005062108 Updated based on review comments

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 09-Mar-05	7022/1	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 10-May-04	4257/2	geoff	VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/2	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
