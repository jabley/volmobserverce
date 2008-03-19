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

import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;

/**
 * This class provides a standard concrete implementation of an
 * {@link com.volantis.mcs.protocols.assets.ImageAssetReference}
 */
public final class DefaultComponentImageAssetReference
        extends AbstractComponentImageAssetReference {

    /**
     * The policy identity to use for this asset reference.  It is accessible
     * using {@link #getPolicyReference}.
     */
    private final RuntimePolicyReference policyReference;

    /**
     * Create a new initialised instance of this class using the values
     * provided.
     *
     * @param reference The identity of the image this is a reference for
     * @param resolver  The asset resolver to use with this asset reference
     */
    public DefaultComponentImageAssetReference(
            RuntimePolicyReference reference,
            AssetResolver resolver) {
        super(resolver);
        policyReference = reference;
    }

    // JavaDoc inherited
    protected RuntimePolicyReference getPolicyReference() {
        return policyReference;
    }

    // JavaDoc inherited
    public TextAssetReference retrieveTextFallback() {
        // Return a fallback text asset reference which falls back from this 
        // image asset.
        FallbackComponentTextAssetReference reference = null;

        ActivatedVariablePolicy policy = getPolicy();
        if (policy != null) {
            reference = new FallbackComponentTextAssetReference(
                assetResolver, policy, null);
        }
        return reference;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        boolean equal = true;

        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            equal = false;
        }

        if (equal) {
            final DefaultComponentImageAssetReference ref =
                    (DefaultComponentImageAssetReference) o;

            if ((policyIdentity != null &&
                    !policyIdentity.equals(ref.policyIdentity)) ||
                        (policyIdentity == null
                            && ref.policyIdentity != null)) {
                equal = false;
            }
        }

        return equal;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (policyIdentity != null ?
                                            policyIdentity.hashCode() : 0);
        return result;
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/7	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3735/5	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3753/4	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
