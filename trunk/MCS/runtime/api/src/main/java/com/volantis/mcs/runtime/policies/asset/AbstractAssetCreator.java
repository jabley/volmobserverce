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

package com.volantis.mcs.runtime.policies.asset;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.content.BaseURLRelative;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.EmbeddedContent;
import com.volantis.mcs.policies.variants.content.URLContent;
import com.volantis.mcs.policies.variants.content.AutoURLSequence;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;

public abstract class AbstractAssetCreator
        implements OldObjectCreator {

    protected void setValue(SubstantiveAsset asset, Variant variant) {
        Content content = variant.getContent();

        if (content instanceof URLContent) {
            URLContent urlContent = (URLContent) variant.getContent();
            setAssetGroupName(asset, urlContent);
            asset.setValue(urlContent.getURL());

            BaseLocation baseLocation = urlContent.getBaseLocation();
            if (baseLocation == BaseLocation.DEVICE) {
                makeDeviceLocal(asset);
            }

        } else if (content instanceof AutoURLSequence) {
            setSequence(asset, (AutoURLSequence) content);
        } else if (content instanceof EmbeddedContent) {
            EmbeddedContent embeddedContent = (EmbeddedContent) content;
            makeLiteralValue(asset);
            asset.setValue(embeddedContent.getData());
        } else {
            throw new IllegalStateException("Unknown content " + content);
        }
    }

    protected void makeDeviceLocal(SubstantiveAsset asset) {
        throw new IllegalStateException(
                "Device local values not supported on " + asset);
    }

    protected void setSequence(SubstantiveAsset asset,
                               AutoURLSequence sequence) {
        throw new IllegalStateException(
                "Sequence values not supported on " + asset);
    }

    /**
     * Set the asset group name in the asset.
     *
     * <p>The asset group may belong in a different project to the asset so
     * store the project that contains the asset group in the asset as
     * well.</p>
     *
     * @param asset
     * @param relative
     */
    private void setAssetGroupName(
            SubstantiveAsset asset, BaseURLRelative relative) {

        RuntimePolicyReference reference = (RuntimePolicyReference)
                relative.getBaseURLPolicyReference();
        if (reference != null) {
            // Set the project so that references to asset groups can be
            // resolved properly.
            asset.setAssetGroupProject(reference.getProject());
            asset.setAssetGroupName(reference.getName());
        }
    }

    protected void makeLiteralValue(SubstantiveAsset asset) {
        throw new IllegalStateException(
                "Literal values not supported on " + asset);
    }

    /**
     * Set the asset identity.
     *
     * <p>Sets the name and project for the asset from the containing
     * policy.</p>
     *
     * @param policy The containing policy.
     * @param asset The asset to modify.
     */
    protected void setAssetIdentity(
            ActivatedVariablePolicy policy, Asset asset) {
        asset.setName(policy.getName());
        asset.setProject(policy.getActualProject());
    }
}
