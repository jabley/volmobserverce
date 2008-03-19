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

import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;

/**
 * This class provides a standard concrete implementation of an
 * {@link com.volantis.mcs.protocols.assets.LinkAssetReference}
 */
public final class DefaultComponentTextAssetReference
        extends AbstractComponentAssetReference
        implements TextAssetReference {

    /**
     * The reference to the policy.
     */
    private final RuntimePolicyReference reference;

    /**
     * Create a new initialised instance of this class using the values
     * provided.
     *
     * @param reference The identity of the link this is a reference for
     * @param resolver  The asset resolver to use with this asset reference
     */
    public DefaultComponentTextAssetReference(RuntimePolicyReference reference,
            AssetResolver resolver) {
        super(resolver);
        this.reference = reference;
    }

    // JavaDoc inherited
    public boolean isPolicy() {
        return true;
    }

    // JavaDoc inherited
    public String getText(TextEncoding encoding) {
        return getTextFromSelectedVariant(encoding);

    }

    // JavaDoc inherited
    protected RuntimePolicyReference getPolicyReference() {
        return reference;
    }

    // JavaDoc inherited
    public TextAssetReference retrieveTextFallback() {
        // Return a fallback text asset reference which falls back from this 
        // link asset.
        FallbackComponentTextAssetReference reference = null;

        ActivatedVariablePolicy policy = getPolicy();
        if (policy != null) {
            reference = new FallbackComponentTextAssetReference(
                assetResolver, policy, null);
        }
        return reference;
    }
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
