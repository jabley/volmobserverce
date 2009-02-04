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
package com.volantis.mcs.protocols.assets;

import com.volantis.mcs.policies.variants.text.TextEncoding;


/**
 * A reference to a text asset.
 * <p>
 * This interface extends the generic asset reference interface to add methods
 * which are text asset reference specific.
 *
 * @mock.generate 
 */
public interface TextAssetReference extends AssetReference {

    boolean isPolicy();

    /**
     * Returns the text of the text asset that this reference points to.
     *
     * @param encoding the encoding of the text to return.
     * @return the text, or null if this reference does not resolve to a text
     *         asset.
     */
    String getText(TextEncoding encoding);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 ===========================================================================
*/
