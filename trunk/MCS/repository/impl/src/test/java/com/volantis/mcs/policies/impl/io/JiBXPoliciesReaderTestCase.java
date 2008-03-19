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

package com.volantis.mcs.policies.impl.io;

import com.volantis.mcs.css.parser.CSSParser;
import com.volantis.mcs.css.parser.CSSParserFactory;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.policies.ButtonImagePolicyBuilder;
import com.volantis.mcs.policies.CacheControlBuilder;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.RolloverImagePolicyBuilder;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.variants.InternalVariantBuilder;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.AxisMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartMetaDataBuilder;
import com.volantis.mcs.policies.variants.chart.ChartType;
import com.volantis.mcs.policies.variants.content.AutoURLSequenceBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.EmbeddedContentBuilder;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.image.ImageConversionMode;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContentBuilder;
import com.volantis.mcs.policies.variants.script.ScriptEncoding;
import com.volantis.mcs.policies.variants.script.ScriptMetaDataBuilder;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.themes.CSSStyleSheet;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import org.jibx.runtime.JiBXException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class JiBXPoliciesReaderTestCase
        extends JIBXTestAbstract {

    private InternalPolicyFactory factory = (InternalPolicyFactory)
            PolicyFactory.getDefaultInstance();

    /**
     * Test a variable policy with invalid stuff in it.
     */
    public void testInvalidVariablePolicy() throws Exception {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.RESOURCE);

        VariantBuilder variantBuilder;

        // Variant with only type set.
        variantBuilder = factory.createVariantBuilder(VariantType.AUDIO);
        policyBuilder.addVariantBuilder(variantBuilder);

        validateModel = false;
        doPolicyBuilderTest(policyBuilder, "xml/invalidVariablePolicy-expected.xml");
    }

    /**
     * Test that the variable policy with just about everything in it can be
     * stored and retrieved.
     */
    public void testVariableResourcePolicy() throws Exception {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.RESOURCE);
        policyBuilder.setCategorizationScheme("size");

        CacheControlBuilder cacheControlBuilder =
                factory.createCacheControlBuilder();
        cacheControlBuilder.setCacheThisPolicy(true);
        cacheControlBuilder.setRetainDuringRetry(true);
        cacheControlBuilder.setRetryFailedRetrieval(true);
        cacheControlBuilder.setRetryInterval(10);
        cacheControlBuilder.setRetryMaxCount(5);
        cacheControlBuilder.setTimeToLive(6);

        policyBuilder.setCacheControlBuilder(cacheControlBuilder);

        TargetedSelectionBuilder targeted =
                factory.createTargetedSelectionBuilder();
        targeted.addCategory("category1");
        targeted.addCategory("category2");
        targeted.addDevice("PC");
        targeted.addDevice("Master");

        VariantBuilder variantBuilder;

        // URL Content.
        URLContentBuilder urlContent = factory.createURLContentBuilder();
        urlContent.setBaseURLPolicyReference(
                factory.createPolicyReference("/abc.mgrp", PolicyType.BASE_URL));
        urlContent.setURL("/xyz.gif");

        // Variant with targeted selection and video meta data.
        VideoMetaDataBuilder videoBuilder =
                factory.createVideoMetaDataBuilder();
        videoBuilder.setVideoEncoding(VideoEncoding.MPEG4);
        videoBuilder.setHeight(100);
        videoBuilder.setWidth(50);

        variantBuilder = factory.createVariantBuilder(VariantType.VIDEO);
        variantBuilder.setSelectionBuilder(targeted);
        variantBuilder.setMetaDataBuilder(videoBuilder);
        variantBuilder.setContentBuilder(urlContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        // Variant with audio meta data.
        AudioMetaDataBuilder audioBuilder =
                factory.createAudioMetaDataBuilder();
        audioBuilder.setAudioEncoding(AudioEncoding.ADPCM32);

        variantBuilder = factory.createVariantBuilder(VariantType.AUDIO);
        variantBuilder.setSelectionBuilder(factory.createEncodingSelectionBuilder());
        variantBuilder.setMetaDataBuilder(audioBuilder);
        variantBuilder.setContentBuilder(urlContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        // Variant with generic image selection, image meta data and automatic URL sequence content
        GenericImageSelectionBuilder generic =
                factory.createGenericImageSelectionBuilder();
        generic.setWidthHint(50);

        ImageMetaDataBuilder imageBuilder =
                factory.createImageMetaDataBuilder();
        imageBuilder.setConversionMode(ImageConversionMode.ALWAYS_CONVERT);
        imageBuilder.setImageEncoding(ImageEncoding.BMP);
        imageBuilder.setHeight(50);
        imageBuilder.setPixelDepth(24);
        imageBuilder.setRendering(ImageRendering.COLOR);
        imageBuilder.setWidth(100);

        AutoURLSequenceBuilder autoURL =
                factory.createAutoURLSequenceBuilder();
        autoURL.setBaseURLPolicyReference(
                factory.createPolicyReference("/abc.mgrp", PolicyType.BASE_URL));
        autoURL.setURLTemplate("/xyz{}.gif");
        autoURL.setSequenceSize(4);

        variantBuilder = factory.createVariantBuilder(VariantType.IMAGE);
        variantBuilder.setSelectionBuilder(generic);
        variantBuilder.setMetaDataBuilder(imageBuilder);
        variantBuilder.setContentBuilder(autoURL);
        policyBuilder.addVariantBuilder(variantBuilder);

        // Create embedded content.
        EmbeddedContentBuilder embeddedContent =
                factory.createEmbeddedContentBuilder();
        embeddedContent.setData(" some textual content ");

        // Variant with text meta data.
        TextMetaDataBuilder textBuilder = factory.createTextMetaDataBuilder();
        textBuilder.setTextEncoding(TextEncoding.PLAIN);

        variantBuilder = factory.createVariantBuilder(VariantType.TEXT);

        targeted = factory.createTargetedSelectionBuilder();
        targeted.addCategory("category3");
        targeted.addCategory("category4");

        variantBuilder.setSelectionBuilder(targeted);
        variantBuilder.setMetaDataBuilder(textBuilder);
        variantBuilder.setContentBuilder(embeddedContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        doPolicyBuilderTest(policyBuilder, "xml/variableResourcePolicy-expected.xml");

    }

    private void doPolicyBuilderTest(
            PolicyBuilder policyBuilder, String expectedResourcePath)
            throws Exception {

        String result = writeStructure(policyBuilder);

        System.out.println("Result: " + result);

        Reader reader = new InputStreamReader(getClass().getResourceAsStream(
                expectedResourcePath));
        StringBuffer buffer = new StringBuffer();
        int read;
        while ((read = reader.read()) != -1) {
            buffer.append((char) read);
        }
        String expected = buffer.toString();

        assertXMLEquals("Output", expected, result);

        PolicyBuilder policyRead = (PolicyBuilder)
                unmarshallFromString(result, null);

//        assertEquals("Policies", policyBuilder, policyRead);

        String result2 = writeStructure(policyRead);

        assertEquals("Structure", result, result2);
    }

    public void testVariableScriptPolicy() throws Exception {
        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.SCRIPT);
        policyBuilder.setCategorizationScheme("size");

        CacheControlBuilder cacheControlBuilder =
                factory.createCacheControlBuilder();
        cacheControlBuilder.setCacheThisPolicy(true);
        cacheControlBuilder.setRetainDuringRetry(true);
        cacheControlBuilder.setRetryFailedRetrieval(true);
        cacheControlBuilder.setRetryInterval(10);
        cacheControlBuilder.setRetryMaxCount(5);
        cacheControlBuilder.setTimeToLive(6);

        policyBuilder.setCacheControlBuilder(cacheControlBuilder);

        TargetedSelectionBuilder targeted;

        VariantBuilder variantBuilder;

        // Variant with script meta data and embedded content.
        EmbeddedContentBuilder embeddedContent = factory.createEmbeddedContentBuilder();
        embeddedContent.setData(" some textual content ");

        ScriptMetaDataBuilder scriptBuilder =
                factory.createScriptMetaDataBuilder();
        scriptBuilder.setCharacterSet("iso-8859-1");
        scriptBuilder.setScriptEncoding(ScriptEncoding.JAVASCRIPT_1_2);

        variantBuilder = factory.createVariantBuilder(VariantType.SCRIPT);

        targeted = factory.createTargetedSelectionBuilder();
        targeted.addDevice("PC");
        targeted.addDevice("Master");

        variantBuilder.setSelectionBuilder(targeted);
        variantBuilder.setMetaDataBuilder(scriptBuilder);
        variantBuilder.setContentBuilder(embeddedContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        doPolicyBuilderTest(policyBuilder, "xml/variableScriptPolicy-expected.xml");
    }

    /**
     * Test a variable policy containing a chart.
     */
    public void testVariableChartPolicy() throws Exception {

        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.CHART);

        // Variant with default selection and chart meta data
        ChartMetaDataBuilder chartBuilder =
                factory.createChartMetaDataBuilder();
        chartBuilder.setChartType(ChartType.BAR);

        chartBuilder.setHeightHint(50);
        chartBuilder.setWidthHint(75);

        AxisMetaDataBuilder xAxisBuilder = factory.createAxisBuilder();
        xAxisBuilder.setInterval(2);
        xAxisBuilder.setTitle("X Axis");
        chartBuilder.setXAxisBuilder(xAxisBuilder);

        AxisMetaDataBuilder yAxisBuilder = factory.createAxisBuilder();
        yAxisBuilder.setInterval(4);
        yAxisBuilder.setTitle("Y Axis");
        chartBuilder.setYAxisBuilder(yAxisBuilder);

        InternalVariantBuilder variantBuilder = (InternalVariantBuilder)
                factory.createVariantBuilder(VariantType.CHART);
        variantBuilder.setSelectionBuilder(factory.createDefaultSelectionBuilder());
        variantBuilder.setMetaDataBuilder(chartBuilder);
        variantBuilder.setVariantIdentifier("UniqueIdentifier");
        policyBuilder.addVariantBuilder(variantBuilder);

        doPolicyBuilderTest(policyBuilder, "xml/variableChartPolicy-expected.xml");
    }

    /**
     * Test a variable policy containing a layout.
     */
    public void testVariableLayoutPolicy() throws Exception {

        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.LAYOUT);

        // Variant with layout content.
        InternalLayoutContentBuilder layoutContent =
                factory.createLayoutContentBuilder();

        CanvasLayout layout = new CanvasLayout();
        Pane pane = new Pane(layout);
        pane.setName("Fred");
        pane.setBackgroundColour("#f60");
        layout.setRootFormat(pane);

        layoutContent.setLayout(layout);

        VariantBuilder variantBuilder =
                factory.createVariantBuilder(VariantType.LAYOUT);
        variantBuilder.setSelectionBuilder(factory.createDefaultSelectionBuilder());
        variantBuilder.setContentBuilder(layoutContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        doPolicyBuilderTest(policyBuilder, "xml/variableLayoutPolicy-expected.xml");
    }

    /**
     * Test a variable policy containing a theme.
     */
    public void testVariableThemePolicy() throws Exception {

        VariablePolicyBuilder policyBuilder = factory.createVariablePolicyBuilder(PolicyType.THEME);

        // Variant with CSS theme content.
        StyleSheetFactory styleSheetFactory = StyleSheetFactory.getDefaultInstance();
        CSSStyleSheet cssStyleSheet = styleSheetFactory.createCSSStyleSheet();
        cssStyleSheet.setCSS(".class {color: red}");

        InternalThemeContentBuilder themeContent =
                factory.createThemeContentBuilder();
        themeContent.setStyleSheet(cssStyleSheet);
        themeContent.setImportParent(true);

        VariantBuilder variantBuilder;

        variantBuilder = factory.createVariantBuilder(VariantType.THEME);
        variantBuilder.setSelectionBuilder(factory.createDefaultSelectionBuilder());
        variantBuilder.setContentBuilder(themeContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        // Variant with theme content.
        CSSParserFactory cssParserFactory = CSSParserFactory.getDefaultInstance();
        CSSParser cssParser = cssParserFactory.createStrictParser();
        StringReader cssReader = new StringReader(
                ".class {color: red} cdm|p {color: red}");
        StyleSheet styleSheet = cssParser.parseStyleSheet(cssReader, null);

        themeContent = factory.createThemeContentBuilder();
        themeContent.setStyleSheet(styleSheet);
        themeContent.setImportParent(false);

        variantBuilder = factory.createVariantBuilder(VariantType.THEME);
        TargetedSelectionBuilder targeted =
                factory.createTargetedSelectionBuilder();
        List devices = targeted.getModifiableDeviceReferences();
        devices.add(factory.createDeviceReference("Master"));
        variantBuilder.setSelectionBuilder(targeted);
        variantBuilder.setContentBuilder(themeContent);
        policyBuilder.addVariantBuilder(variantBuilder);

        doPolicyBuilderTest(policyBuilder, "xml/variableThemePolicy-expected.xml");
    }

    public void testVariableImagePolicy() throws Exception {

        VariablePolicyBuilder policyBuilder =
                factory.createVariablePolicyBuilder(PolicyType.IMAGE);

        CacheControlBuilder control = createCacheControlBuilder();
        policyBuilder.setCacheControlBuilder(control);

        policyBuilder.addAlternatePolicy(factory.createPolicyReference(
                "/fallback-image-component-name.mimg", PolicyType.IMAGE));
        policyBuilder.addAlternatePolicy(factory.createPolicyReference(
                "/fallback-text-component-name.mtxt", PolicyType.TEXT));

        AutoURLSequenceBuilder auto;
        TargetedSelectionBuilder targeted;
        VariantBuilder variantBuilder;

        // Add a null variant.
        variantBuilder = factory.createVariantBuilder(VariantType.NULL);
        policyBuilder.addVariantBuilder(variantBuilder);

        targeted = factory.createTargetedSelectionBuilder();
        targeted.addDevice("devName1");
        variantBuilder.setSelectionBuilder(targeted);

        // Add an image variant selected by device with sequence of assets.
        variantBuilder = factory.createVariantBuilder(VariantType.IMAGE);
        policyBuilder.addVariantBuilder(variantBuilder);

        targeted = factory.createTargetedSelectionBuilder();
        targeted.addDevice("devName2");
        variantBuilder.setSelectionBuilder(targeted);

        ImageMetaDataBuilder imageBuilder;
        imageBuilder = factory.createImageMetaDataBuilder();
        imageBuilder.setWidth(100);
        imageBuilder.setHeight(80);
        imageBuilder.setPixelDepth(24);
        imageBuilder.setRendering(ImageRendering.COLOR);
        imageBuilder.setImageEncoding(ImageEncoding.PNG);
        variantBuilder.setMetaDataBuilder(imageBuilder);

        auto = factory.createAutoURLSequenceBuilder();
        auto.setSequenceSize(3);
        auto.setBaseURLPolicyReference(factory.createPolicyReference("/assetGrpName1.mgrp", PolicyType.BASE_URL));
        auto.setBaseLocation(BaseLocation.DEVICE);
        auto.setURLTemplate("http://www.volantis.com/idunno{}.png");
        variantBuilder.setContentBuilder(auto);

        // Create a normal image selected generically.
        variantBuilder = factory.createVariantBuilder(VariantType.IMAGE);
        policyBuilder.addVariantBuilder(variantBuilder);

        GenericImageSelectionBuilder generic =
                factory.createGenericImageSelectionBuilder();
        generic.setWidthHint(64);
        variantBuilder.setSelectionBuilder(generic);

        imageBuilder = factory.createImageMetaDataBuilder();
        imageBuilder.setWidth(70);
        imageBuilder.setHeight(50);
        imageBuilder.setPixelDepth(8);
        imageBuilder.setRendering(ImageRendering.GRAYSCALE);
        imageBuilder.setImageEncoding(ImageEncoding.BMP);
        variantBuilder.setMetaDataBuilder(imageBuilder);

        URLContentBuilder url = factory.createURLContentBuilder();
        url.setBaseURLPolicyReference(factory.createPolicyReference("/assetGrpName2.mgrp", PolicyType.BASE_URL));
        url.setBaseLocation(BaseLocation.CONTEXT);
        url.setURL("http://www.volantis.com/idunno2");
        variantBuilder.setContentBuilder(url);

        // Create a convertible image.
        variantBuilder = factory.createVariantBuilder(VariantType.IMAGE);
        policyBuilder.addVariantBuilder(variantBuilder);

        DefaultSelectionBuilder defaultSelection =
                factory.createDefaultSelectionBuilder();
        variantBuilder.setSelectionBuilder(defaultSelection);

        imageBuilder = factory.createImageMetaDataBuilder();
        imageBuilder.setWidth(72);
        imageBuilder.setHeight(90);
        imageBuilder.setPixelDepth(2);
        imageBuilder.setRendering(ImageRendering.GRAYSCALE);
        imageBuilder.setImageEncoding(ImageEncoding.WBMP);
        imageBuilder.setConversionMode(ImageConversionMode.ALWAYS_CONVERT);
        imageBuilder.setPreserveLeft(10);
        imageBuilder.setPreserveRight(100);
        variantBuilder.setMetaDataBuilder(imageBuilder);

        auto = factory.createAutoURLSequenceBuilder();
        auto.setBaseURLPolicyReference(factory.createPolicyReference("/assetGrpName3.mgrp", PolicyType.BASE_URL));
        auto.setSequenceSize(5);
        auto.setURLTemplate("http://www.volantis.com/idunno3{}.png");
        variantBuilder.setContentBuilder(auto);

        doPolicyBuilderTest(policyBuilder, "xml/variableImagePolicy-expected.xml");
    }

    private CacheControlBuilder createCacheControlBuilder() {
        CacheControlBuilder control = factory.createCacheControlBuilder();
        control.setCacheThisPolicy(true);
        control.setTimeToLive(100);
        control.setRetryInterval(10);
        control.setRetryMaxCount(5);
        control.setRetryFailedRetrieval(false);
        control.setRetainDuringRetry(true);
        return control;
    }

    public void testRolloverImagePolicy() throws Exception {

        RolloverImagePolicyBuilder builder =
                factory.createRolloverImagePolicyBuilder();
        CacheControlBuilder cacheControl = createCacheControlBuilder();
        cacheControl.setRetryFailedRetrieval(true);
        builder.setCacheControlBuilder(cacheControl);

        PolicyReference textFallback =
                factory.createPolicyReference(
                        "/fallback-text-component-name.mtxt", PolicyType.TEXT);

        builder.addAlternatePolicy(textFallback);
        builder.setNormalPolicy(factory.createPolicyReference(
                        "/normal-image-component-name.mimg", PolicyType.IMAGE));
        builder.setOverPolicy(factory.createPolicyReference(
                        "/over-image-component-name.mimg", PolicyType.IMAGE));

        doPolicyBuilderTest(builder, "xml/rolloverImagePolicy-expected.xml");
    }

    public void testButtonImagePolicy() throws Exception {

        ButtonImagePolicyBuilder builder =
                factory.createButtonImagePolicyBuilder();
        CacheControlBuilder cacheControl = createCacheControlBuilder();
        cacheControl.setRetryFailedRetrieval(true);
        builder.setCacheControlBuilder(cacheControl);

        PolicyReference textFallback =
                factory.createPolicyReference(
                        "/fallback-text-component-name.mtxt", PolicyType.TEXT);

        builder.addAlternatePolicy(textFallback);
        builder.setUpPolicy(factory.createPolicyReference(
                        "/up-image-component-name.mimg", PolicyType.IMAGE));
        builder.setDownPolicy(factory.createPolicyReference(
                        "/down-image-component-name.mimg", PolicyType.IMAGE));
        builder.setOverPolicy(factory.createPolicyReference(
                        "/over-image-component-name.mimg", PolicyType.IMAGE));

        doPolicyBuilderTest(builder, "xml/buttonImagePolicy-expected.xml");
    }
}
