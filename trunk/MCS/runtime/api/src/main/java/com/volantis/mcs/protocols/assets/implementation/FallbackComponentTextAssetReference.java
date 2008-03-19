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

import com.volantis.mcs.policies.ConcretePolicy;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;


/**
 * A text asset reference which resolves via fallback from an original 
 * non-text component id.
 * <p> 
 * NOTE: most of this is cut and pasted from 
 * MarinerPageContext.retrieveFallbackTextForPolicy(). Eventually
 * this should replace that code...
 */ 
public class FallbackComponentTextAssetReference 
        extends AbstractAssetReference implements TextAssetReference {

    private final TextAssetReference alternateText;

    private final RuntimePolicyReference policyReference;

    /**
     * Construct an instance of this class, using the asset resolver and 
     * original component id provided.
     * 
     * @param assetResolver the asset resolver we will use for calculating
     * @param alternateText
     */
    public FallbackComponentTextAssetReference(
            AssetResolver assetResolver,
            ConcretePolicy originalPolicy,
            TextAssetReference alternateText) {
        super(assetResolver);
        this.policyReference = (RuntimePolicyReference)
                originalPolicy.getAlternatePolicy(PolicyType.TEXT);
        this.alternateText = alternateText;
    }

    protected RuntimePolicyReference getPolicyReference() {
        return policyReference;
    }

    public boolean isPolicy() {
        return true;
    }

    // Inherit javadoc.
    public String getText(TextEncoding encoding) {
        String text = getTextFromSelectedVariant(encoding);
        if (text == null && alternateText != null) {
            text = alternateText.getText(encoding);
        }
        return text;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/1	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 ===========================================================================
*/
