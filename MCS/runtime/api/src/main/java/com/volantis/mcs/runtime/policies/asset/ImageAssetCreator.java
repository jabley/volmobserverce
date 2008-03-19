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

import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.compatibility.EnumerationConverter;
import com.volantis.mcs.policies.compatibility.IntObjectMap;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.content.AutoURLSequence;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.runtime.policies.ActivatedVariablePolicy;

public class ImageAssetCreator extends AbstractAssetCreator {

    private static final IntObjectMap ENCODING_MAP = EnumerationConverter.IMAGE_ENCODING;
    private static final IntObjectMap RENDERING_MAP = EnumerationConverter.IMAGE_RENDERING;

    public RepositoryObject createOldObject(
            ActivatedVariablePolicy policy, Variant variant,
            InternalDevice device) {

        // Convert the variant into an asset.
        ImageMetaData metaData = (ImageMetaData) variant.getMetaData();
        ImageAsset asset;
        if (metaData.getConversionMode() == ImageConversionMode.ALWAYS_CONVERT) {
            asset = new ConvertibleImageAsset();
            ((ConvertibleImageAsset)asset).setPreserveLeft(
                    metaData.getPreserveLeft());
            ((ConvertibleImageAsset)asset).setPreserveRight(
                    metaData.getPreserveRight());
        } else {
            asset = new DeviceImageAsset();
        }

        setAssetIdentity(policy, asset);

        asset.setEncoding(ENCODING_MAP.get(metaData.getImageEncoding()));
        asset.setPixelDepth(metaData.getPixelDepth());
        asset.setPixelsX(metaData.getWidth());
        asset.setPixelsY(metaData.getHeight());
        asset.setRendering(RENDERING_MAP.get(metaData.getRendering()));

        setValue(asset, variant);

        return asset;
    }

    protected void makeDeviceLocal(SubstantiveAsset asset) {
        ImageAsset image = (ImageAsset) asset;
        image.setLocalSrc(true);
    }

    protected void setSequence(SubstantiveAsset asset,
                               AutoURLSequence sequence) {
        ImageAsset image = (ImageAsset) asset;
        image.setSequence(true);
        image.setSequenceSize(sequence.getSequenceSize());
        image.setValue(sequence.getURLTemplate());
    }
}
