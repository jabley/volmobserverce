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

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.protocols.assets.AssetReferenceException;
import com.volantis.mcs.protocols.assets.ImageAssetReference;

/**
 * This class provides a partial implementation of an
 * {@link ImageAssetReference}.  It contains code that is common to all
 * implementations.
 */
public abstract class AbstractComponentImageAssetReference
        extends AbstractComponentAssetReference
        implements ImageAssetReference {

    /**
     * Construct an instance of this class using the asset resolver provided.
     *
     * @param assetResolver used to resolve the asset related stuff.
     */
    protected AbstractComponentImageAssetReference(AssetResolver assetResolver) {
        super(assetResolver);
    }

    // JavaDoc inherited
    public String getURL() throws AssetReferenceException {
        String url = assetResolver.computeURLAsString(getSelectedVariant());
        return assetResolver
                .rewriteURLWithPageURLRewriter(url, PageURLType.IMAGE);

    }

/*  Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        boolean equal = true;

        // This is the top level equals method implementation, so check that
        // the object to be compared is of the correct type
        if (o == null || !(o.getClass().equals(this.getClass()))) {
            equal = false;
        }

        if (equal) {
            final AbstractComponentImageAssetReference ref =
                                    (AbstractComponentImageAssetReference) o;

            if ((asset != null && !asset.equals(ref.asset)) ||
                    (asset == null && ref.asset != null)) {
                    equal = false;
            }
        }
        
        return equal;
    }

    // JavaDoc inherited
    public int hashCode() {
        return (asset != null ? asset.hashCode() : 0);
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
