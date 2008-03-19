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

import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.ChartAsset;
import com.volantis.mcs.assets.ConvertibleImageAsset;
import com.volantis.mcs.assets.DeviceAudioAsset;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.GenericImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.assets.SubstantiveAsset;
import com.volantis.mcs.assets.TextAsset;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.chart.AxisMetaData;
import com.volantis.mcs.policies.variants.chart.ChartMetaData;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.content.AutoURLSequence;
import com.volantis.mcs.policies.variants.content.Content;
import com.volantis.mcs.policies.variants.content.EmbeddedContent;
import com.volantis.mcs.policies.variants.content.URLContent;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.metadata.Encoding;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.script.ScriptMetaData;
import com.volantis.mcs.policies.variants.selection.DefaultSelection;
import com.volantis.mcs.policies.variants.selection.EncodingSelection;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaData;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaData;

/**
 * Test converting from old components / assets to new policies.
 */
public class Asset2VariantTestCase
        extends Old2NewConverterTestAbstract {

    /**
     * Test that a device image asset can be converted to a variants.
     */
    public void testDeviceImageAsset() {
        DeviceImageAsset asset = createAndInitialiseDeviceImageAsset();

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkDeviceTargetedImageSelectionAndMetaData(asset, variant);

        checkURLContent(variant);
    }

    private void checkURLContent(Variant variant) {
        URLContent content = (URLContent) variant.getContent();
        PolicyReference reference = content.getBaseURLPolicyReference();
        assertNull("Base URL", reference);

        String url = content.getURL();
        assertEquals("URL", "/xyz", url);
    }

    /**
     * Check that the selection and meta data of a device targeted image
     * variants are correct.
     *
     * @param asset   The source asset.
     * @param variant The resulting variants.
     */
    private void checkDeviceTargetedImageSelectionAndMetaData(
            DeviceImageAsset asset, Variant variant) {

        checkImageVariant(asset, variant, ImageConversionMode.NEVER_CONVERT);

        // Check the device specific aspects.
        checkTargetedSelection(variant);

    }

    private void checkImageVariant(
            ImageAsset asset, Variant variant,
            ImageConversionMode conversionMode) {
        assertEquals("Variant type", VariantType.IMAGE,
                variant.getVariantType());

        checkImageMetaData(asset, variant, conversionMode);
    }

    /**
     * Create an initialise a device image asset for testing.
     *
     * @return The newly create device image asset.
     */
    private DeviceImageAsset createAndInitialiseDeviceImageAsset() {
        DeviceImageAsset asset = new DeviceImageAsset();
        initialiseImageAsset(asset);
        asset.setDeviceName(DEVICE_NAME);
        return asset;
    }

    /**
     * Test that an image asset sequence can be converted to a variants.
     */
    public void testDeviceImageAssetSequence() {
        DeviceImageAsset asset = createAndInitialiseDeviceImageAsset();
        initialiseImageAssetSequence(asset);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkDeviceTargetedImageSelectionAndMetaData(asset, variant);

        // Check that the content has been set to sequence.
        AutoURLSequence content = (AutoURLSequence)
                variant.getContent();
        PolicyReference reference = content.getBaseURLPolicyReference();
        assertNull("Base URL", reference);

        String url = content.getURLTemplate();
        assertEquals("URL Template", "image{index}.gif", url);
    }

    /**
     * Check that the image variants meta data is correct.
     *
     * @param asset          The source asset.
     * @param variant        The variants.
     * @param conversionMode
     */
    private void checkImageMetaData(
            ImageAsset asset, Variant variant,
            ImageConversionMode conversionMode) {

        ImageMetaData metaData = (ImageMetaData) variant.getMetaData();
        assertEquals("Pixel depth", asset.getPixelDepth(),
                metaData.getPixelDepth());
        assertEquals("Width", asset.getPixelsX(), metaData.getWidth());
        assertEquals("Height", asset.getPixelsY(), metaData.getHeight());

        checkEncoding(metaData.getImageEncoding(), ImageEncoding.BMP);
        assertEquals("Rendering", ImageRendering.GRAYSCALE,
                metaData.getRendering());

        assertEquals("Conversion Mode", conversionMode,
                metaData.getConversionMode());
    }

    private void checkEncoding(Encoding actualEncoding, Encoding expectedEncoding) {
        assertEquals("Encodings", expectedEncoding, actualEncoding);
    }

    /**
     * Initialise the meta data of the image asset to test.
     *
     * @param asset The asset to initialise.
     */
    private void initialiseImageAsset(ImageAsset asset) {
        initialiseSubstantiveAsset(asset);

        asset.setName("/foo.mimg");
        asset.setEncoding(ImageAsset.BMP);
        asset.setPixelDepth(4);
        asset.setPixelsX(100);
        asset.setPixelsY(200);
        asset.setRendering(ImageAsset.MONOCHROME);
    }

    private void initialiseImageAssetSequence(ImageAsset asset) {
        initialiseImageAsset(asset);

        asset.setValue("image{index}.gif");
        asset.setSequence(true);
        asset.setSequenceSize(4);
    }

    /**
     * Initialise the substantive asset.
     *
     * @param asset
     */
    private void initialiseSubstantiveAsset(SubstantiveAsset asset) {
        asset.setAssetGroupName(null);
        asset.setValue("/xyz");
    }

    /**
     * Test that converting a generic image asset works properly.
     */
    public void testGenericImageAsset() {
        GenericImageAsset asset = new GenericImageAsset();
        initialiseImageAsset(asset);
        asset.setWidthHint(50);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkImageVariant(asset, variant, ImageConversionMode.NEVER_CONVERT);

//        checkDeviceTargetedImageSelectionAndMetaData(asset, variants);

        checkURLContent(variant);
    }

    /**
     * Test that converting a convertible image asset works properly.
     */
    public void testConvertibleImageAsset() {
        ConvertibleImageAsset asset = new ConvertibleImageAsset();
        initialiseImageAsset(asset);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkImageVariant(asset, variant, ImageConversionMode.ALWAYS_CONVERT);

//        checkDeviceTargetedImageSelectionAndMetaData(asset, variants);

        checkURLContent(variant);
    }

    public void testGenericAudioAsset() {
        AudioAsset asset = new AudioAsset();
        initialiseAudioAsset(asset);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkEncodingSelection(variant);
        checkAudioMetaDataAndContent(variant);
    }

    private void checkEncodingSelection(Variant variant) {
        EncodingSelection selection = (EncodingSelection)
                variant.getSelection();
        assertNotNull("Selection", selection);
    }

    private void checkAudioMetaDataAndContent(Variant variant) {
        AudioMetaData metaData = (AudioMetaData) variant.getMetaData();

        // Check the encodings.
        checkEncoding(metaData.getAudioEncoding(), AudioEncoding.AMR);

        checkURLContent(variant);
    }

    private void initialiseAudioAsset(AudioAsset asset) {
        initialiseSubstantiveAsset(asset);

        asset.setName("/foo.mauc");
        asset.setEncoding(AudioAsset.AMR_AUDIO);
    }

    public void testDeviceAudioAsset() {
        DeviceAudioAsset asset = new DeviceAudioAsset();
        initialiseAudioAsset(asset);
        asset.setDeviceName(DEVICE_NAME);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkAudioMetaDataAndContent(variant);
        checkTargetedSelection(variant);
    }

    public void testDynamicVisualAsset() {
        DynamicVisualAsset asset = new DynamicVisualAsset();
        asset.setPixelsX(50);
        asset.setPixelsY(40);
        asset.setEncoding(DynamicVisualAsset.MACROMEDIA_FLASH);
        initialiseSubstantiveAsset(asset);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        VideoMetaData metaData = (VideoMetaData) variant.getMetaData();

        assertEquals("Width", asset.getPixelsX(), metaData.getWidth());
        assertEquals("Height", asset.getPixelsY(), metaData.getHeight());
        checkEncoding(metaData.getVideoEncoding(), VideoEncoding.MACROMEDIA_FLASH);

        checkEncodingSelection(variant);

        checkURLContent(variant);
    }

    public void testChartAsset() {
        ChartAsset asset = new ChartAsset();
        asset.setName("/foo.mcht");
        asset.setHeightHint(50);
        asset.setType(ChartAsset.COLUMN_CHART);
        asset.setWidthHint(40);
        asset.setXInterval(5);
        asset.setXTitle("X Title");
        asset.setYInterval(3);
        asset.setYTitle("Y Title");

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        ChartMetaData metaData = (ChartMetaData) variant.getMetaData();
        assertEquals("HeightHint", asset.getHeightHint(),
                metaData.getHeightHint());
        assertEquals("Type", ChartType.COLUMN, metaData.getChartType());
        assertEquals("WidthHint", asset.getWidthHint(),
                metaData.getWidthHint());
        AxisMetaData xAxis = metaData.getXAxis();
        assertEquals("X Interval", asset.getXInterval(),
                xAxis.getInterval());
        assertEquals("X Title", asset.getXTitle(), xAxis.getTitle());
        AxisMetaData yAxis = metaData.getYAxis();
        assertEquals("Y Interval", asset.getYInterval(),
                yAxis.getInterval());
        assertEquals("Y Title", asset.getYTitle(), yAxis.getTitle());

        DefaultSelection selection = (DefaultSelection) variant.getSelection();
        assertNotNull("Selection", selection);

        Content content = variant.getContent();
        assertNull("Content", content);
    }

    public void testURLScriptAsset() {
        ScriptAsset asset = new ScriptAsset();
        initialiseScriptAsset(asset);
        initialiseSubstantiveAsset(asset);
        asset.setValueType(ScriptAsset.URL);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkTargetedSelection(variant);
        checkScriptAssetMetaData(variant, asset);

        checkURLContent(variant);
    }

    private void initialiseScriptAsset(ScriptAsset asset) {
        asset.setDeviceName(DEVICE_NAME);
        asset.setProgrammingLanguage(ScriptAsset.JAVA_SCRIPT_1_2);
        asset.setMimeType("text/javascript");
        asset.setCharacterSet("us-ascii");
    }

    private void checkScriptAssetMetaData(Variant variant, ScriptAsset asset) {
        ScriptMetaData metaData = (ScriptMetaData) variant.getMetaData();
        checkEncoding(metaData.getScriptEncoding(), ScriptEncoding.JAVASCRIPT_1_2);
        assertEquals(asset.getCharacterSet(), metaData.getCharacterSet());
    }

    public void testLiteralScriptAsset() {
        ScriptAsset asset = new ScriptAsset();
        initialiseScriptAsset(asset);
        asset.setValueType(ScriptAsset.LITERAL);
        asset.setValue("/xyz");

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        checkTargetedSelection(variant);
        checkScriptAssetMetaData(variant, asset);

        checkTextualContent(variant);
    }

    private void checkTextualContent(Variant variant) {
        EmbeddedContent content = (EmbeddedContent) variant.getContent();
        assertEquals("Content", "/xyz", content.getData());
    }

    public void testURLTextAsset() {
        TextAsset asset = new TextAsset();
        asset.setValueType(TextAsset.URL);
        initialiseTextAsset(asset);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        TextMetaData metaData = (TextMetaData) variant.getMetaData();
        checkTextAsset(metaData);

        checkURLContent(variant);
    }

    private void initialiseTextAsset(TextAsset asset) {
        asset.setDeviceName(DEVICE_NAME);
        asset.setEncoding(TextAsset.PLAIN);
        asset.setLanguage(TextAsset.DEFAULT_LANGUAGE);
        initialiseSubstantiveAsset(asset);
    }

    public void testLiteralTextAsset() {
        TextAsset asset = new TextAsset();
        asset.setValueType(TextAsset.LITERAL);
        initialiseTextAsset(asset);

        Variant variant = converter.asset2VariantBuilder(asset).getVariant();

        TextMetaData metaData = (TextMetaData) variant.getMetaData();
        checkTextAsset(metaData);

        checkTextualContent(variant);
    }

    private void checkTextAsset(TextMetaData metaData) {
        checkEncoding(metaData.getTextEncoding(), TextEncoding.PLAIN);
    }
}
