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
 * A reference to an image asset.
 * <p>
 * This interface extends the generic asset reference interface to add methods
 * which are image asset reference specific.
 *
 * @mock.generate
 */
public interface ImageAssetReference extends AssetReference {
    
    /**
     * This method returns the URL string that this Asset resolves to.
     *
     * @return        The URL of this asset as a string, or null if the
     *                resolution fails.
     * 
     * @throws AssetReferenceException if there is a problem obtaining the URL.
     */
    String getURL() throws AssetReferenceException;

    /**
     * Returns a reference to this asset's text fallback asset.
     * 
     * @return a reference to this asset's text fallback asset. 
     * @throws AssetReferenceException if there is a problem obtaining the
     *                                 fallback.
     */ 
    TextAssetReference getTextFallback() throws AssetReferenceException;
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/3	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3753/1	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
