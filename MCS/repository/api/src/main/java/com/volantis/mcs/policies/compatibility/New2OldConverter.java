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

package com.volantis.mcs.policies.compatibility;

import com.volantis.mcs.accessors.xml.jibx.ComponentContainer;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.GenericImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.StrictValidator;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.BaseURLPolicy;
import com.volantis.mcs.policies.ButtonImagePolicy;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyVisitor;
import com.volantis.mcs.policies.RolloverImagePolicy;
import com.volantis.mcs.policies.VariablePolicy;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.BaseURLRelativeBuilder;
import com.volantis.mcs.policies.variants.content.ContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.project.Project;

import java.util.List;

/**
 * Converts new policies into old components and assets.
 *
 * todo: later: operate on policy builders instead of policies (?)
 */
public class New2OldConverter
        implements PolicyVisitor {

    private final ModelFactory modelFactory = ModelFactory.getDefaultInstance();

    private final StrictValidator strictValidator;

    public New2OldConverter() {
        this.strictValidator = modelFactory.createStrictValidator();
    }

    public RepositoryObject new2old(Policy policy) {

        // Validate the policy before converting it.
        strictValidator.validate((Validatable) policy);

        throw new NullPointerException();
    }

    public void visit(BaseURLPolicy policy) {
        AssetGroup assetGroup = new AssetGroup();
        assetGroup.setName(policy.getName());
        final BaseLocation baseLocation = policy.getBaseLocation();
        if (baseLocation == BaseLocation.DEVICE) {
            assetGroup.setLocationType(AssetGroup.ON_DEVICE);
        } else if (baseLocation == BaseLocation.CONTEXT) {
            assetGroup.setLocationType(AssetGroup.CONTEXT);
        } else if (baseLocation == BaseLocation.HOST) {
            assetGroup.setLocationType(AssetGroup.HOST);
        }
        //assetGroup.
    }

    public void visit(ButtonImagePolicy policy) {
    }

    public void visit(RolloverImagePolicy policy) {
    }

    public void visit(VariablePolicy policy) {
    }

    public ComponentContainer new2oldContainer(VariablePolicy policy) {
        throw new NullPointerException();
    }

    public Asset variantBuilder2Asset(
            VariantBuilder variantBuilder,
            String name, Project project) {
        VariantType variantType = variantBuilder.getVariantType();

        Asset asset;
        if (variantType == VariantType.IMAGE) {

            SelectionBuilder selection = variantBuilder.getSelectionBuilder();
            ImageMetaDataBuilder image = (ImageMetaDataBuilder)
                    variantBuilder.getMetaDataBuilder();
            ContentBuilder content = variantBuilder.getContentBuilder();
            if (selection instanceof DefaultSelectionBuilder) {

                if (image.getConversionMode() !=
                        ImageConversionMode.ALWAYS_CONVERT) {
                    throw new IllegalArgumentException("Default selection only supported for " +
                            "convertible image assets");
                }

                ConvertibleImageAsset imageAsset = new ConvertibleImageAsset();
                setImageMetaData(imageAsset, image);
                setImageContent(imageAsset, content);
                imageAsset.setPreserveLeft(image.getPreserveLeft());
                imageAsset.setPreserveRight(image.getPreserveRight());
                asset = imageAsset;

            } else if (selection instanceof TargetedSelectionBuilder) {
                TargetedSelectionBuilder targeted =
                        (TargetedSelectionBuilder) selection;
                int categoryCount = targeted.getModifiableCategoryReferences().size();
                List devices = targeted.getModifiableDeviceReferences();
                int deviceCount = devices.size();
                if (categoryCount != 0) {
                    throw new IllegalArgumentException(
                            "Categories not supported by assets");
                } else if (deviceCount == 0) {
                    throw new IllegalArgumentException(
                            "Device must be specified");
                } else if (deviceCount > 1) {
                    throw new IllegalArgumentException(
                            "Multiple devices not supported");
                }

                DeviceReference reference = (DeviceReference) devices.get(0);
                String deviceName = reference.getDeviceName();

                DeviceImageAsset imageAsset = new DeviceImageAsset();
                imageAsset.setDeviceName(deviceName);
                setImageMetaData(imageAsset, image);
                setImageContent(imageAsset, content);
                asset = imageAsset;

            } else if (selection instanceof GenericImageSelectionBuilder) {
                GenericImageSelectionBuilder generic =
                        (GenericImageSelectionBuilder) selection;

                GenericImageAsset imageAsset = new GenericImageAsset();
                imageAsset.setWidthHint(generic.getWidthHint());
                setImageMetaData(imageAsset, image);
                setImageContent(imageAsset, content);
                asset = imageAsset;

            } else {
                throw new IllegalArgumentException("Selection " + selection +
                        " not supported by image assets");
            }

        } else {
            throw new UnsupportedOperationException();
        }

        asset.setName(name);
        asset.setProject(project);

        return asset;
    }

    private void setImageMetaData(ImageAsset asset,
                                  ImageMetaDataBuilder imageBuilder) {
        
        asset.setEncoding(EnumerationConverter.IMAGE_ENCODING
                .get(imageBuilder.getImageEncoding()));
        asset.setRendering(EnumerationConverter.IMAGE_RENDERING
                .get(imageBuilder.getRendering()));
        asset.setPixelDepth(imageBuilder.getPixelDepth());
        asset.setPixelsX(imageBuilder.getWidth());
        asset.setPixelsY(imageBuilder.getHeight());
    }

    private void setImageContent(
            ImageAsset asset, ContentBuilder contentBuilder) {
        if (contentBuilder instanceof AutoURLSequenceBuilder) {
            AutoURLSequenceBuilder auto =
                    (AutoURLSequenceBuilder) contentBuilder;
            setAssetGroupName(asset, auto);
            asset.setSequence(true);
            asset.setSequenceSize(auto.getSequenceSize());
            asset.setValue(auto.getURLTemplate());
        } else if (contentBuilder instanceof URLContentBuilder) {
            URLContentBuilder url = (URLContentBuilder) contentBuilder;
            setAssetGroupName(asset, url);
            asset.setValue(url.getURL());
        } else {
            throw new IllegalArgumentException("Content " + contentBuilder +
                    " not supported for images");
        }
    }

    private void setAssetGroupName(
            SubstantiveAsset asset, BaseURLRelativeBuilder baseURLBuilder) {

        PolicyReference reference = baseURLBuilder.getBaseURLPolicyReference();
        if (reference != null) {
            asset.setAssetGroupName(reference.getName());
        }
    }

    public RepositoryObject variablePolicyBuilder2Component(VariablePolicyBuilder policy) {
        throw new UnsupportedOperationException();
    }
}
