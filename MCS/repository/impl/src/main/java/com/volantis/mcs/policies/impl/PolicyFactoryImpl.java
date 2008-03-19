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

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.accessors.xml.jibx.JiBXWriter;
import com.volantis.mcs.accessors.xml.jibx.PolicyJiBXReader;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.policies.impl.jdbc.JDBCPolicyBuilderManager;
import com.volantis.mcs.policies.impl.variants.VariantBuilderImpl;
import com.volantis.mcs.policies.impl.variants.audio.AudioMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.variants.chart.AxisMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.variants.chart.ChartMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.variants.content.AutoURLSequenceBuilderImpl;
import com.volantis.mcs.policies.impl.variants.content.EmbeddedContentBuilderImpl;
import com.volantis.mcs.policies.impl.variants.content.URLContentBuilderImpl;
import com.volantis.mcs.policies.impl.variants.image.GenericImageSelectionBuilderImpl;
import com.volantis.mcs.policies.impl.variants.image.ImageMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.variants.layout.LayoutContentBuilderImpl;
import com.volantis.mcs.policies.impl.variants.script.ScriptMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.variants.selection.CategoryReferenceImpl;
import com.volantis.mcs.policies.impl.variants.selection.DefaultSelectionImpl;
import com.volantis.mcs.policies.impl.variants.selection.DeviceReferenceImpl;
import com.volantis.mcs.policies.impl.variants.selection.EncodingSelectionImpl;
import com.volantis.mcs.policies.impl.variants.selection.TargetedSelectionBuilderImpl;
import com.volantis.mcs.policies.impl.variants.selection.VariantSelectionTypes;
import com.volantis.mcs.policies.impl.variants.text.TextMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.variants.theme.ThemeContentBuilderImpl;
import com.volantis.mcs.policies.impl.variants.video.VideoMetaDataBuilderImpl;
import com.volantis.mcs.policies.impl.xml.XMLPolicyBuilderManager;
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
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.EncodingSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.SelectionType;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.project.PolicyBuilderManager;

public class PolicyFactoryImpl
        extends InternalPolicyFactory {

    private static final DefaultSelectionImpl DEFAULT_SELECTION =
            new DefaultSelectionImpl();

    private static final EncodingSelectionImpl ENCODING_SELECTION =
            new EncodingSelectionImpl();

    public VariablePolicyBuilder createVariablePolicyBuilder(
            VariablePolicyType type) {
        return new VariablePolicyBuilderImpl(type);
    }

    public RolloverImagePolicyBuilder createRolloverImagePolicyBuilder() {
        return new RolloverImagePolicyBuilderImpl();
    }

    public ButtonImagePolicyBuilder createButtonImagePolicyBuilder() {
        return new ButtonImagePolicyBuilderImpl();
    }

    public BaseURLPolicyBuilder createBaseURLPolicyBuilder() {
        return new BaseURLPolicyBuilderImpl();
    }

    public PolicyReference createPolicyReference(
            String name, PolicyType expectedType) {
        return new PolicyReferenceImpl(name, expectedType);
    }

    public CacheControlBuilder createCacheControlBuilder() {
        return new CacheControlBuilderImpl();
    }

    public VariantBuilder createVariantBuilder(VariantType variantType) {
        return new VariantBuilderImpl(variantType);
    }

    // Selection.
    public DefaultSelectionBuilder createDefaultSelectionBuilder() {
        return DEFAULT_SELECTION;
    }

    public TargetedSelectionBuilder createTargetedSelectionBuilder() {
        return new TargetedSelectionBuilderImpl();
    }

    public EncodingSelectionBuilder createEncodingSelectionBuilder() {
        return ENCODING_SELECTION;
    }

    public GenericImageSelectionBuilder createGenericImageSelectionBuilder() {
        return new GenericImageSelectionBuilderImpl();
    }

    public DeviceReference createDeviceReference(String deviceName) {
        return new DeviceReferenceImpl(deviceName);
    }

    public CategoryReference createCategoryReference(String categoryName) {
        return new CategoryReferenceImpl(categoryName);
    }

    // Meta Data
    public AudioMetaDataBuilder createAudioMetaDataBuilder() {
        return new AudioMetaDataBuilderImpl();
    }

    public ChartMetaDataBuilder createChartMetaDataBuilder() {
        return new ChartMetaDataBuilderImpl();
    }

    public AxisMetaDataBuilder createAxisBuilder() {
        return new AxisMetaDataBuilderImpl();
    }

    public ImageMetaDataBuilder createImageMetaDataBuilder() {
        return new ImageMetaDataBuilderImpl();
    }

    public ScriptMetaDataBuilder createScriptMetaDataBuilder() {
        return new ScriptMetaDataBuilderImpl();
    }

    public TextMetaDataBuilder createTextMetaDataBuilder() {
        return new TextMetaDataBuilderImpl();
    }

    public VideoMetaDataBuilder createVideoMetaDataBuilder() {
        return new VideoMetaDataBuilderImpl();
    }

    // Content
    public URLContentBuilder createURLContentBuilder() {
        return new URLContentBuilderImpl();
    }

    public EmbeddedContentBuilder createEmbeddedContentBuilder() {
        return new EmbeddedContentBuilderImpl();
    }

    public SelectionType[] getSelectionTypes(VariantType variantType) {
        return VariantSelectionTypes.getValidSelectionTypes(variantType);
    }

    public AutoURLSequenceBuilder createAutoURLSequenceBuilder() {
        return new AutoURLSequenceBuilderImpl();
    }

    public InternalThemeContentBuilder createThemeContentBuilder() {
        return new ThemeContentBuilderImpl();
    }

    public InternalLayoutContentBuilder createLayoutContentBuilder() {
        return new LayoutContentBuilderImpl();
    }

    public JiBXReader createPolicyReader() {
        return new PolicyJiBXReader(VariablePolicyBuilderImpl.class);
    }

    // todo: remove this method when we sort out validation.
    public JiBXReader createDangerousNonValidatingPolicyReader() {

        return new JiBXReader(VariablePolicyBuilderImpl.class);
    }

    public JiBXWriter createPolicyWriter() {
        return new JiBXWriter();
    }

    public PolicyBuilderManager createXMLPolicyBuilderManager(
            InternalProject project) {
        return new XMLPolicyBuilderManager(project);
    }

    public PolicyBuilderManager createJDBCPolicyBuilderManager(
            InternalProject project) {
        return new JDBCPolicyBuilderManager(project);
    }
}
