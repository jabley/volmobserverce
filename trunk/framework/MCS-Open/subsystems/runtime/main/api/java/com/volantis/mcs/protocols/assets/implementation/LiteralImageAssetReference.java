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

import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * Wraps a literal string URL as a reference to an image asset.
 */
public final class LiteralImageAssetReference
        implements ImageAssetReference {

    /**
     * The literal URL.
     */
    private final String url;

    /**
     * Initialise this new instance.
     *
     * @param url The literal URL to wrao.
     */
    public LiteralImageAssetReference(String url) {
        this.url = url;
    }

    // Javadoc inherited.
    public String getURL() throws AssetReferenceException {
        return url;
    }

    /**
     * Always return null.
     *
     * @return Null.
     * @throws AssetReferenceException
     */
    public TextAssetReference getTextFallback() throws AssetReferenceException {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 ===========================================================================
*/
