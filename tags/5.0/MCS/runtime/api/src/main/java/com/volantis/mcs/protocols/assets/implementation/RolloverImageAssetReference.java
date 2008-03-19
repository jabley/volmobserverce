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

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class provides a standard concrete implementation of an
 * {@link com.volantis.mcs.protocols.assets.ImageAssetReference} specifically
 * for rollover image references.
 * 
 * @todo this should probably have Component in the name to be consistent.
 */
public final class RolloverImageAssetReference
        extends AbstractComponentImageAssetReference {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RolloverImageAssetReference.class);

    /**
     * The policy reference to use for this asset reference.  It is accessible
     * using {@link #getPolicyReference}.
     */
    private RuntimePolicyReference imageReference = null;

    /**
     * The rollover reference which this asset reference should use to find
     * it's identity.
     */
    private RolloverComponentReference rolloverReference = null;

    /**
     * Indicates whether this is an asset reference for the normal url part
     * of a rollover or the over part.  True indicates it is a normal reference
     * whilst false indicates it is an over reference.
     */
    private final boolean isNormal;

    /**
     * Create a new initialised instance of this class using the values
     * provided.
     *
     * @param resolver The asset resolver to use with this asset reference
     * @param rollover The identity of the rollover this is a reference for
     * @param isNormal True if this is a normal reference, false if over
     */
    public RolloverImageAssetReference(AssetResolver resolver,
                                       RolloverComponentReference rollover,
                                       boolean isNormal) {
        super(resolver);
        rolloverReference = rollover;
        this.isNormal = isNormal;
    }

    // JavaDoc inherited
    protected RuntimePolicyReference getPolicyReference() {

        if (imageReference == null) {
            if (isNormal) {
                imageReference = rolloverReference.getNormalReference();
            } else {
                imageReference = rolloverReference.getOverReference();
            }
        }

        if (imageReference == null) {
            logger.warn("component-reference-not-resolved", rolloverReference);
        }

        return imageReference;
    }

    // JavaDoc inherited
    protected TextAssetReference retrieveTextFallback() {
        // Return a fallback text asset reference which falls back from
        // the original rollover component rather than the image which it 
        // had. This is the same as the old functionality, but we could 
        // enhance it in future to use both...
        FallbackComponentTextAssetReference reference = null;

        RolloverImagePolicy policy = rolloverReference.getPolicy();
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
            final RolloverImageAssetReference ref =
                                        (RolloverImageAssetReference) o;

            if ((imageIdentity != null &&
                    !imageIdentity.equals(ref.imageIdentity)) ||
                        (imageIdentity == null
                            && ref.imageIdentity != null)) {
                equal = false;
            } else if ((rolloverReference != null &&
                    !rolloverReference.equals(ref.rolloverReference)) ||
                        (rolloverReference == null
                            && ref.rolloverReference != null)) {
                equal = false;
            } else if (isNormal != ref.isNormal) {
                equal = false;
            }
        }

        return equal;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (imageIdentity != null ?
                                            imageIdentity.hashCode() : 0);
        result = 29 * result + (rolloverReference != null ?
                                            rolloverReference.hashCode() : 0);
        result = 29 * result + (isNormal ? 100 : 1);
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

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/7	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3735/5	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3753/5	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 07-Apr-04	3753/1	claire	VBM:2004040612 Increasing laziness of reference resolution

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 ===========================================================================
*/
