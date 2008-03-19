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

package com.volantis.mcs.protocols.assets;

/**
 * A reference to a link asset.
 * <p>
 * This interface extends the generic asset reference interface to add methods
 * which are link asset reference specific.
 */
public interface LinkAssetReference extends AssetReference {

    /**
     * This method returns the URL string that this Asset resolves to.
     *
     * @return        The URL of this asset as a string, or null if the
     *                resolution fails.
     */
    String getURL();

    /**
     * Returns a reference to this asset's text fallback asset.
     *
     * @return a reference to this asset's text fallback asset.
     */
    TextAssetReference getTextFallback();
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
