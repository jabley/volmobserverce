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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.assets.Asset;

/**
 * A testing image asset reference which has no url and instead returns a
 * text asset reference which itself has no text content. 
 * <p>
 * That is, it is effectively "empty" as no value can be successfully 
 * retrieved from it or it's fallback.
 */ 
public class TestEmptyImageAssetReference implements ImageAssetReference {
    
    // Javadoc inherited.
    public String getURL() throws AssetReferenceException {
        // Return null to indicate that the fallback should be tried.
        return null;
    }

    // Javadoc inherited.
    public TextAssetReference getTextFallback()
            throws AssetReferenceException {
        // Return an empty text asset reference.
        return new TestEmptyTextAssetReference();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
