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

package com.volantis.mcs.policies;

import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.EncodingSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for creating policy related objects.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User specializations of this class are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public abstract class PolicyFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.mcs.policies.impl.PolicyFactoryImpl",
                        PolicyFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static PolicyFactory getDefaultInstance() {
        return (PolicyFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     * @volantis-api-exclude-from InternalAPI
     */
    protected PolicyFactory() {
    }

    /**
     * Create a new {@link VariablePolicyBuilder}.
     *
     * @param type The type of variable policy to build, may be null.
     * @return A new {@link VariablePolicyBuilder}.
     */
    public abstract VariablePolicyBuilder createVariablePolicyBuilder(
            VariablePolicyType type);

    /**
     * Create a new {@link RolloverImagePolicyBuilder}.
     *
     * @return A new {@link RolloverImagePolicyBuilder}.
     */
    public abstract RolloverImagePolicyBuilder createRolloverImagePolicyBuilder();

    /**
     * Create a new {@link ButtonImagePolicyBuilder}.
     *
     * @return A new {@link ButtonImagePolicyBuilder}.
     */
    public abstract ButtonImagePolicyBuilder createButtonImagePolicyBuilder();

    /**
     * Create a new {@link BaseURLPolicyBuilder}.
     *
     * @return A new {@link BaseURLPolicyBuilder}.
     */
    public abstract BaseURLPolicyBuilder createBaseURLPolicyBuilder();

    /**
     * Create a new {@link PolicyReference}.
     *
     * @param name         The name of the policy.
     * @param expectedType The expected type of the policy, or null if any type
     *                     is acceptable.
     * @return A new {@link PolicyReference}.
     */
    public abstract PolicyReference createPolicyReference(
            String name, PolicyType expectedType);

    /**
     * Create a new {@link CacheControlBuilder}.
     *
     * @return A new {@link CacheControlBuilder}.
     */
    public abstract CacheControlBuilder createCacheControlBuilder();

    /**
     * Create a new {@link VariantBuilder}.
     *
     * @param variantType The type of variant being built, may be null.
     * @return A new {@link VariantBuilder}.
     */
    public abstract VariantBuilder createVariantBuilder(
            VariantType variantType);

    /**
     * Create a new {@link DefaultSelectionBuilder}.
     *
     * @return A new {@link DefaultSelectionBuilder}.
     */
    public abstract DefaultSelectionBuilder createDefaultSelectionBuilder();

    /**
     * Create a new {@link TargetedSelectionBuilder}.
     *
     * @return A new {@link TargetedSelectionBuilder}.
     */
    public abstract TargetedSelectionBuilder createTargetedSelectionBuilder();

    /**
     * Create a new {@link EncodingSelectionBuilder}.
     *
     * @return A new {@link EncodingSelectionBuilder}.
     */
    public abstract EncodingSelectionBuilder createEncodingSelectionBuilder();

    /**
     * Create a new {@link GenericImageSelectionBuilder}.
     *
     * @return A new {@link GenericImageSelectionBuilder}.
     */
    public abstract GenericImageSelectionBuilder createGenericImageSelectionBuilder();

    /**
     * Create a reference to the named device.
     *
     * @param deviceName The device to reference.
     * @return A reference to the device.
     */
    public abstract DeviceReference createDeviceReference(String deviceName);

    /**
     * Create a reference to the named category.
     *
     * @param categoryName The category to reference.
     * @return A reference to the category.
     */
    public abstract CategoryReference createCategoryReference(
            String categoryName);

    /**
     * Create a new {@link AudioMetaDataBuilder}.
     *
     * @return A new {@link AudioMetaDataBuilder}.
     */
    public abstract AudioMetaDataBuilder createAudioMetaDataBuilder();

    /**
     * Create a new {@link ChartMetaDataBuilder}.
     *
     * @return A new {@link ChartMetaDataBuilder}.
     */
    public abstract ChartMetaDataBuilder createChartMetaDataBuilder();

    /**
     * Create a new {@link AxisMetaDataBuilder}.
     *
     * @return A new {@link AxisMetaDataBuilder}.
     */
    public abstract AxisMetaDataBuilder createAxisBuilder();

    /**
     * Create a new {@link ImageMetaDataBuilder}.
     *
     * @return A new {@link ImageMetaDataBuilder}.
     */
    public abstract ImageMetaDataBuilder createImageMetaDataBuilder();

    /**
     * Create a new {@link ScriptMetaDataBuilder}.
     *
     * @return A new {@link ScriptMetaDataBuilder}.
     */
    public abstract ScriptMetaDataBuilder createScriptMetaDataBuilder();

    /**
     * Create a new {@link TextMetaDataBuilder}.
     *
     * @return A new {@link TextMetaDataBuilder}.
     */
    public abstract TextMetaDataBuilder createTextMetaDataBuilder();

    /**
     * Create a new {@link VideoMetaDataBuilder}.
     *
     * @return A new {@link VideoMetaDataBuilder}.
     */
    public abstract VideoMetaDataBuilder createVideoMetaDataBuilder();

    /**
     * Create a new {@link URLContentBuilder}.
     *
     * @return A new {@link URLContentBuilder}.
     */
    public abstract URLContentBuilder createURLContentBuilder();

    /**
     * Create a new {@link EmbeddedContentBuilder}.
     *
     * @return A new {@link EmbeddedContentBuilder}.
     */
    public abstract EmbeddedContentBuilder createEmbeddedContentBuilder();

    /**
     * Create a new {@link AutoURLSequenceBuilder}.
     *
     * @return A new {@link AutoURLSequenceBuilder}.
     */
    public abstract AutoURLSequenceBuilder createAutoURLSequenceBuilder();

    /**
     * Retrieve a list of valid selection types for a given variant type.
     *
     * @param variantType The variant type to query
     * @return The valid selection types for the provided variant type
     */
    public abstract SelectionType[] getSelectionTypes(VariantType variantType);
}
