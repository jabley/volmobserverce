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

package com.volantis.mcs.runtime.policies;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.objects.RepositoryObject;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.video.VideoMetaData;
import com.volantis.mcs.runtime.policies.asset.AudioAssetCreator;
import com.volantis.mcs.runtime.policies.asset.ChartAssetCreator;
import com.volantis.mcs.runtime.policies.asset.ImageAssetCreator;
import com.volantis.mcs.runtime.policies.asset.LinkAssetCreator;
import com.volantis.mcs.runtime.policies.asset.ScriptAssetCreator;
import com.volantis.mcs.runtime.policies.asset.TextAssetCreator;
import com.volantis.mcs.runtime.policies.asset.VideoAssetCreator;
import com.volantis.mcs.runtime.policies.asset.OldObjectCreator;
import com.volantis.mcs.runtime.policies.layout.RuntimeLayoutCreator;
import com.volantis.mcs.runtime.policies.theme.RuntimeThemeCreator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Select a policy variants.
 */
public class PolicyVariantSelector {

    private static final Map VARIANT_TYPE_2_FILTER;
    private static final Map VARIABLE_TYPE_2_SELECTOR;
    private static final Map VARIANT_TYPE_2_OLD_CREATOR;

    static {
        Map map = new HashMap();

        VariantFilter passThrough = new VariantFilter() {
            public Variant filter(
                    InternalDevice device, Variant variant,
                    EncodingCollection requiredEncodings) {
                return variant;
            }
        };

        // A policy type specific selector that always returns null.
        VariablePolicyTypeSpecificSelector nullSelector =
                new VariablePolicyTypeSpecificSelector() {

                    public Variant selectVariant(
                            SelectionContext context,
                            ActivatedVariablePolicy variablePolicy) {
                        return null;
                    }
                };

        map.put(VariantType.AUDIO, new EncodingFilter() {
            protected EncodingCollection getSupportedEncodings(
                    InternalDevice device) {
                return device.getSupportedAudioEncodings();
            }

            protected Encoding getEncoding(Variant variant) {
                AudioMetaData metaData = (AudioMetaData) variant.getMetaData();
                return metaData.getAudioEncoding();
            }
        });

        map.put(VariantType.CHART, passThrough);

        map.put(VariantType.IMAGE, new ImageEncodingFilter());

        map.put(VariantType.LAYOUT, passThrough);
        map.put(VariantType.LINK, passThrough);
        map.put(VariantType.NULL, passThrough);
        map.put(VariantType.SCRIPT, passThrough);
        map.put(VariantType.TEXT, passThrough);
        map.put(VariantType.THEME, passThrough);

        map.put(VariantType.VIDEO, new EncodingFilter() {
            protected EncodingCollection getSupportedEncodings(
                    InternalDevice device) {
                return device.getSupportedVideoEncodings();
            }

            protected Encoding getEncoding(Variant variant) {
                VideoMetaData metaData = (VideoMetaData) variant.getMetaData();
                return metaData.getVideoEncoding();
            }
        });

        VARIANT_TYPE_2_FILTER = map;

        map = new HashMap();

        map.put(PolicyType.AUDIO, nullSelector);
        map.put(PolicyType.CHART, nullSelector);
        map.put(PolicyType.IMAGE, new ImageSpecificSelector());
        map.put(PolicyType.LAYOUT, nullSelector);
        map.put(PolicyType.LINK, nullSelector);
        map.put(PolicyType.RESOURCE, nullSelector);
        map.put(PolicyType.SCRIPT, nullSelector);
        map.put(PolicyType.TEXT, nullSelector);
        map.put(PolicyType.THEME, nullSelector);
        map.put(PolicyType.VIDEO, new VideoSpecificSelector());

        VARIABLE_TYPE_2_SELECTOR = map;

        map = new HashMap();

        map.put(VariantType.AUDIO, new AudioAssetCreator());
        map.put(VariantType.CHART, new ChartAssetCreator());
        map.put(VariantType.IMAGE, new ImageAssetCreator());
        map.put(VariantType.LAYOUT, new RuntimeLayoutCreator());
        map.put(VariantType.LINK, new LinkAssetCreator());
        map.put(VariantType.SCRIPT, new ScriptAssetCreator());
        map.put(VariantType.TEXT, new TextAssetCreator());
        map.put(VariantType.THEME, new RuntimeThemeCreator());
        map.put(VariantType.VIDEO, new VideoAssetCreator());

        VARIANT_TYPE_2_OLD_CREATOR = map;
    }

    /**
     * Select the best variants to use within the specified context.
     *
     * @param context           The context within which the selection occurs.
     * @param policy            The reference to the policy.
     * @param requiredEncodings The optional collection of encodings that are
     *                          required. If specified then the returned variant is guaranteed to
     *                          have one of the encodings.
     * @return
     */
    public SelectedVariant selectVariant(
            SelectionContext context, ActivatedVariablePolicy policy,
            EncodingCollection requiredEncodings) {

        Variant variant;

        // Get the categorization schema.
        String categorizationScheme = policy.getCategorizationScheme();

        // First try variants targeted at specific devices, or categories.
        InternalDevice targetDevice = context.getDevice();

        // Iterate upwards over the devices, starting with the target device.
        InternalDevice device = targetDevice;
        while (device != null) {

            String deviceName = device.getName();

            // Check to see whether there is a variant targeted at the
            // specified device. If there is and it is not filtered out then
            // return it.
            variant = policy.getDeviceTargetedVariant(deviceName);
            if (variant != null) {

                // Filter the variant according to the target device (not the
                // current one which may be an ancestor) to make sure that it
                // is compatible. If it is then return it, otherwise continue.
                variant = filter(targetDevice, variant, requiredEncodings);
                if (variant != null) {
                    return createSelectedVariant(policy, variant, device);
                }
            }

            // If the device is the target device and the policy has a
            // categorization schema then check to see whether it matches
            // the category. This does not check the category of devices other
            // than the target device because categories are inherited from
            // the parent device anyway.
            if (device == targetDevice && categorizationScheme != null) {

                // Get the value of the category from the device.
                String categoryName = device.getPolicyValue(
                        categorizationScheme);

                variant = policy.getCategoryTargetedVariant(categoryName);
                if (variant != null) {

                    // Filter the variant.
                    variant = filter(targetDevice, variant, requiredEncodings);
                    if (variant != null) {
                        return createSelectedVariant(policy, variant, device);
                    }
                }
            }

            device = device.getFallbackDevice();
        }

        // Next, if any required encodings have been specified then try and
        // find a variant with one of those encodings. The encodings are
        // tried in the order they were specified.
        if (requiredEncodings != null) {
            Iterator iterator = requiredEncodings.iterator();
            while (iterator.hasNext()) {
                Encoding encoding = (Encoding) iterator.next();
                variant = policy.getVariantWithEncoding(encoding);
                if (variant != null) {

                    // Filter the variant.
                    variant = filter(targetDevice, variant, requiredEncodings);
                    if (variant != null) {
                        return createSelectedVariant(policy, variant,
                                targetDevice);
                    }
                }
            }
        }

        // Next if there is a default variant and it is acceptable for this
        // device then return it.
        variant = policy.getDefaultVariant();
        if (variant != null) {

            // Filter the variant.
            variant = filter(targetDevice, variant, requiredEncodings);
            if (variant != null) {
                return createSelectedVariant(policy, variant, targetDevice);
            }
        }

        // Lastly, try a policy specific selection mechanism.

        VariablePolicyTypeSpecificSelector selector =
                (VariablePolicyTypeSpecificSelector)
                VARIABLE_TYPE_2_SELECTOR.get(policy.getPolicyType());
        variant = selector.selectVariant(context, policy);
        if (variant != null) {
            return createSelectedVariant(policy, variant, targetDevice);
        }

        // None of the variants in this policy worked so return. The caller
        // will try and retrieve any alternates of the same type.

        return null;
    }

    private Variant filter(
            InternalDevice device, Variant variant,
            EncodingCollection requiredEncodings) {

        VariantType variantType = variant.getVariantType();
        VariantFilter filter = (VariantFilter)
                VARIANT_TYPE_2_FILTER.get(variantType);
        return filter.filter(device, variant, requiredEncodings);
    }

    private SelectedVariant createSelectedVariant(
            ActivatedVariablePolicy policy, Variant variant,
            InternalDevice device) {

        OldObjectCreator creator = (OldObjectCreator)
                VARIANT_TYPE_2_OLD_CREATOR.get(variant.getVariantType());
        RepositoryObject oldObject;
        if (creator == null) {
            oldObject = null;
        } else {
            oldObject = creator.createOldObject(policy, variant, device);
        }

        return new SelectedVariantImpl(policy, variant, device, oldObject);
    }
}
