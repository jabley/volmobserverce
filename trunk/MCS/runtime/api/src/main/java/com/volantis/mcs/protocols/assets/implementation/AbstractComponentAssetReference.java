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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.SelectedVariant;

public abstract class AbstractComponentAssetReference
        extends AbstractAssetReference {

    /**
     * The cached text asset reference to use as the text fallback for this
     * image.
     */
    private TextAssetReference textFallback;

    protected AbstractComponentAssetReference(AssetResolver assetResolver) {
        super(assetResolver);
    }

    protected ActivatedVariablePolicy getPolicy() {

        SelectedVariant selectedVariant = getSelectedVariant();
        if (selectedVariant != null) {
            return selectedVariant.getPolicy();
        }

        return null;
    }

    // Javadoc inherited.
    public TextAssetReference getTextFallback() {
        if (textFallback == null) {
            textFallback = retrieveTextFallback();
        }
        return textFallback;
    }

    /**
     * Retrieve the text fallback asset reference for this asset. The
     * returned value will be cached by this class.
     *
     * @return the text fallback asset reference for this asset,
     */
    protected abstract TextAssetReference retrieveTextFallback();
}
