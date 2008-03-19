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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/PackageResources.java,v 1.4 2003/02/24 13:27:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-03    Phil W-S        VBM:2003013013 - Created. Provides registration
 *                              and discovery of resources that may be and that
 *                              are used in a generated (response) package.
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Add getContentType and
 *                              setContentType.
 * 21-Feb-03    Phil W-S        VBM:2003022006 - Add initializeEncodedURLs and
 *                              updated addEncodedURLCandidate and
 *                              getEncodedURLs javadoc.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers;

import com.volantis.mcs.assets.AssetGroup;

import java.util.List;
import java.util.Map;

/**
 * Provides methods for registering and discovering the resources likely to be
 * utilized and actually utilized in a generated package.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public interface PackageResources {
    class Asset {

        boolean onClientSide;

        /**
         * The asset value (probably a URL)
         */
        protected String value;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param value the asset value
         * @param onClientSide true if on client side, false otherwise.
         */
        public Asset(String value,
                     boolean onClientSide) {
            this.value = value;
            this.onClientSide = onClientSide;
        }

        /**
         * Return the asset's value.
         *
         * @return the asset's value
         */
        public String getValue() {
            return value;
        }

        /**
         * Return the asset's location type.
         *
         * @return the asset's location type
         */
        public boolean getOnClientSide() {
            return onClientSide;
        }
    }

    /**
     * Returns the content type associated with the package.
     *
     * @return the package's content type
     */
    String getContentType();

    /**
     * Allows the package's content type to be stored.
     *
     * @param contentType the content type to be associated with the package
     */
    void setContentType(String contentType);

    /**
     * Retrieve the mapping between encoded and plain asset URLs for all
     * assets that could be in the package. The map keys are the encoded
     * URLs, while the associated values are the Asset instances.
     *
     * @associates Asset
     * @return a map between encoded and plain asset URLs.
     */
    Map getAssetURLMap();

    /**
     * Add a mapping entry to the asset URL map.
     *
     * @param encoded the encoded asset URL
     * @param asset the plain asset URL
     */
    void addAssetURLMapping(String encoded, Asset asset);

    /**
     * Retrieve the full list of encoded asset URLs actually used in the
     * package. Will be null if initializeEncodedURLs has not be explicitly
     * or implicitly invoked.
     *
     * @return a list of the encoded asset URLs used in the package.
     */
    List getEncodedURLs();

    /**
     * The given URL is added to the list of encoded asset URLs used in the
     * package. Implicitly invokes initializeEncodedURLs if needed.
     *
     * @param encoded the URL to be added
     */
    void addEncodedURLCandidate(String encoded);

    /**
     * Indicates that the encodedURLs list is to be used (i.e. a subset of
     * asset URLs are required).
     */
    void initializeEncodedURLs();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
