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

import com.volantis.mcs.policies.CacheControl;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.runtime.policies.PolicyFetcher;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;

import java.util.List;

/**
 * This class provides a container for image asset references for all components
 * of a rollover image.  These are resolved on demand using the necessary
 * information provided when an instance of the class is created.
 */
public final class RolloverComponentReference {


    private static final RolloverImagePolicy NOT_SET = new RolloverImagePolicy() {
        public RolloverImagePolicyBuilder getRolloverImagePolicyBuilder() {
            return null;
        }

        public PolicyReference getNormalPolicy() {
            return null;
        }

        public PolicyReference getOverPolicy() {
            return null;
        }

        public List getAlternatePolicies() {
            return null;
        }

        public PolicyReference getAlternatePolicy(PolicyType policyType) {
            return null;
        }

        public PolicyBuilder getPolicyBuilder() {
            return null;
        }

        public String getName() {
            return null;
        }

        public CacheControl getCacheControl() {
            return null;
        }

        public PolicyType getPolicyType() {
            return null;
        }
    };

    /**
     * The asset reference to the normal image in this rollover.
     */
    private RolloverImageAssetReference normal;

    /**
     * The asset reference to the over image in this rollover.
     */
    private RolloverImageAssetReference over;

    /**
     * The reference to the normal image in this rollover.
     */
    private RuntimePolicyReference normalReference;

    /**
     * The reference to the over image in this rollover.
     */
    private RuntimePolicyReference overReference;

    private final PolicyFetcher fetcher;
    /**
     * The asset resolver which is used when ever anything needs resolving
     * with respect to the assets in this rollover.
     */
    private final AssetResolver assetResolver;

    /**
     * The identity for this rollover image component which is needed to obtain
     * the necesary asset references.
     */
    private final RuntimePolicyReference policyReference;

    /**
     * The policy.
     */
    private RolloverImagePolicy policy = NOT_SET;

    /**
     * Used to indicate if a resolution has been tried, regardless of succes.
     */
    private boolean triedToResolveIdentities = false;

    /**
     * Create a new instace of <code>RolloverComponentReference</code>,  It
     * is constructed and initialised using the provided information.
     *
     * @param fetcher
     * @param assetResolver     The resolver to use for the assets contained
     *                          within this rollover reference.
     * @param reference The component identity of the rollover that
     */
    public RolloverComponentReference(
            PolicyFetcher fetcher, AssetResolver assetResolver,
            RuntimePolicyReference reference) {

        this.fetcher = fetcher;
        this.assetResolver = assetResolver;
        this.policyReference = reference;
    }

    /**
     * Provides the asset reference for the normal rollover image.
     *
     * @return The normal image rollover image asset reference.
     */
    public ImageAssetReference getNormal() {
        if (normal == null) {
            normal = new RolloverImageAssetReference(assetResolver, this, true);
        }
        return normal;
    }

    /**
     * Provides the asset reference for the over rollover image.
     *
     * @return The over image rollover image asset reference.
     */
    public ImageAssetReference getOver() {
        if (over == null) {
            over = new RolloverImageAssetReference(assetResolver, this, false);
        }
        return over;
    }

    /**
     * Returns the identity of the original rollover component.
     * 
     * @return the identity, will never be null.
     */ 
    public PolicyReference getRolloverIdentity() {
        return policyReference;
    }
    
    /**
     * Provides the image component identity for the normal image in the
     * rollover.  If it has not been resolved this method will do so.  Once
     * resolved the same value is always returned.
     *
     * @return The normal image component identity
     */
    public RuntimePolicyReference getNormalReference() {
        if (!triedToResolveIdentities) {
            resolveIdentities();
        }
        return normalReference;
    }

    /**
     * Provides the image component identity for the over image in the
     * rollover.  If it has not been resolved this method will do so.  Once
     * resolved the same value is always returned.
     *
     * @return The over image component identity
     */
    public RuntimePolicyReference getOverReference() {
        if (!triedToResolveIdentities) {
            resolveIdentities();
        }
        return overReference;
    }

    public RolloverImagePolicy getPolicy() {

        if (policy == NOT_SET) {
            policy = (RolloverImagePolicy) fetcher.fetchPolicy(policyReference);
        }

        return policy;
    }

    /**
     * This method resolves the provided policyIdentity using the
     * assetResolver to obtain suitable rollver identities for both the
     * normal and over images.  Once called it resolves both the normal
     * and over identities.
     */
    private void resolveIdentities() {
        triedToResolveIdentities = true;
        RolloverImagePolicy rollover = getPolicy();
        if (rollover != null) {

            // Get references to the contained policies.
            normalReference = (RuntimePolicyReference) rollover.getNormalPolicy();
            overReference = (RuntimePolicyReference) rollover.getOverPolicy();
        }
    }

    /**
     * String representation of this reference, used for logging.
     *
     * @return identity of reference
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(policyReference.getName());

        if (!triedToResolveIdentities){
            sb.append("(not resolved)");
        }
        return sb.toString();
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        boolean equal = true;

        if (o == null || !(o.getClass().equals(this.getClass()))) {
            equal = false;
        }

        if (equal) {
            final RolloverComponentReference ref =
                    (RolloverComponentReference) o;

            if ((policyIdentity != null &&
                    !policyIdentity.equals(ref.policyIdentity)) ||
                        (policyIdentity == null
                            && ref.policyIdentity != null)) {
                equal = false;
            }
            else if ((normal != null && !normal.equals(ref.normal)) ||
                    (normal == null && ref.normal != null)) {
                equal = false;
            }
            else if ((over != null && !over.equals(ref.over)) ||
                    (over == null && ref.over != null)) {
                equal = false;
            }
            else if ((normalIdentity != null &&
                    !normalIdentity.equals(ref.normalIdentity)) ||
                    (normalIdentity == null && ref.normalIdentity != null)) {
                equal = false;
            }
            else if ((overIdentity != null
                    && !overIdentity.equals(ref.overIdentity)) ||
                    (overIdentity == null && ref.overIdentity != null)) {
                equal = false;
            }
        }

        return equal;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result;
        result = (normal != null ? normal.hashCode() : 0);
        result = 29 * result + (over != null ? over.hashCode() : 0);
        result = 29 * result + (normalIdentity != null ?
                                            normalIdentity.hashCode() : 0);
        result = 29 * result + (overIdentity != null ?
                                            overIdentity.hashCode() : 0);
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

 23-Nov-05	10410/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 23-Nov-05	10408/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 23-Nov-05	10400/1	pabbott	VBM:2005101703 Recover if rollover image asset not found

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 11-May-04	4257/5	geoff	VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection (supermerge)

 10-May-04	4257/2	geoff	VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection

 10-May-04	4253/3	claire	VBM:2004051006 Fix invalid rollover component names NPE

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/5	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3753/4	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 07-Apr-04	3753/1	claire	VBM:2004040612 Increasing laziness of reference resolution

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
