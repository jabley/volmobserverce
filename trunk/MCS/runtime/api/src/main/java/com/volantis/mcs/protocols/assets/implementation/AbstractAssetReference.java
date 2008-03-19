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

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaData;
import com.volantis.mcs.protocols.assets.AssetReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.runtime.selection.SelectedVariantMarker;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * An abstract base class for asset references.
 * <p>
 * Currently this simply provides common code for caching assets.
 * <p>
 * The inheritance hierarchy for asset references is currently rather ugly;
 * we really need to split asset references using delegation to try and
 * reduce our dependency on inheritance but there's no time now...
 * <p>
 * NOTE: I have disabled equals and hashcode to try and discourage anyone
 * storing these references in a cache. This has dire consequences for these
 * operations on menu, which will be addressed in a consequence VBM.
 */
public abstract class AbstractAssetReference
        implements AssetReference {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AbstractAssetReference.class);

    private static final SelectedVariant SELECTED_VARIANT_NOT_SET =
            new SelectedVariantMarker();

    /**
     * An appropriate means through which the asset can be resolved given an
     * image component identity.
     */
    protected final AssetResolver assetResolver;

    /**
     * The selected variant.
     *
     * <p>This field is used to maintain a reference to a selected variant
     * once it has been found. After resolving it is not thrown away, or
     * re-resolved.</p>
     */
    private SelectedVariant selectedVariant = SELECTED_VARIANT_NOT_SET;

    /**
     * Construct a new initialised instance of this class.
     *
     * @param assetResolver The resolver to use when locating a real asset.
     */
    protected AbstractAssetReference(AssetResolver assetResolver) {
        this.assetResolver = assetResolver;
    }

    // JavaDoc inherited
    protected Asset getAsset() {
        SelectedVariant selectedVariant = getSelectedVariant();
        if (selectedVariant != null) {
            return (Asset) selectedVariant.getOldObject();
        }
        return null;
    }

    /**
     * Get selected variant.
     * 
     * @return the variant or null if not found.
     */
    protected SelectedVariant getSelectedVariant() {
        if (selectedVariant == SELECTED_VARIANT_NOT_SET) {
            RuntimePolicyReference reference = getPolicyReference();
            if (reference == null) {
                selectedVariant = null;
            } else {
                selectedVariant = assetResolver.selectBestVariant(reference,
                        null);
            }
        }

        return selectedVariant;
    }

    /**
     * A means of obtaining the relevant component identity which is the basis
     * for retrieving a correct asset.
     *
     * @return An image component identity that identifies the asset, or null
     */
    protected abstract RuntimePolicyReference getPolicyReference();

    protected String getTextFromSelectedVariant(TextEncoding encoding) {
        String text = null;

        SelectedVariant selectedVariant = getSelectedVariant();
        if (selectedVariant != null) {
            Variant variant = selectedVariant.getVariant();
            if (variant != null) {
                TextMetaData metaData = (TextMetaData) variant.getMetaData();
                if (metaData.getTextEncoding() == encoding) {
                    text = assetResolver.getContentsFromVariant(selectedVariant);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Text for policy " +
                                getPolicyReference() + " does not have " +
                                encoding + " as the encoding");
                    }
                }
            }
        }
        return text;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Nov-05	10410/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 23-Nov-05	10408/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 23-Nov-05	10400/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 ===========================================================================
*/
