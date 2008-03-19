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
package com.volantis.mcs.protocols.html;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeAndOrElementStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.CSSBorderStylePropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.CSSBorderWidthPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.CSSColorPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultFontStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultFontWeightEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultStyleEmulationPropertiesRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultTextAlignEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.ElementOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.HTML3_2CSSMarginPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.HTML3_2CSSPaddingPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementAppendAttributeRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementAttributeRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementSetAttributeRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2BorderSpacingEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2ColorEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2ColorEmulationPropertyRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2FontSizeEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2PaddingEmulationPropertyRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2PixelsEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2VerticalAlignEmulationPropertyRenderer;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.Set;

/**
 * This class encapsulates protocol specific information and is based on the
 * Singleton design pattern.
 */
public class HTMLVersion3_2Configuration extends HTMLRootConfiguration {

    private static final String[] permittedChildren = {
            "tt", "i", "b", "u", "strike", "big", "small", "sub", "sup", "em",
            "strong", "dfn", "code", "samp", "kbd", "var", "cite", "a", "img",
            "applet", "font", "br", "script", "map", "input", "select",
            "textarea"
        };

    // Javadoc inherited.
    public String[] getPermittedChildren() {
        return permittedChildren;
    }

    public void initialize(InternalDevice device,
                           DeviceCapabilityManagerBuilder builder) {
        // By default, HTML_Version3_2 supports u and strike emulation
        defaultElementCapabilities.add(new DeviceElementCapability("u",
                CapabilitySupportLevel.FULL));
        defaultElementCapabilities.add(new DeviceElementCapability("strike",
                    CapabilitySupportLevel.FULL));
        super.initialize(device, builder);
    }

    // Javadoc inherited.
    public void createStyleEmulationElements() {
        super.createStyleEmulationElements();

        final Set permittedChildren = getAllPermittedChildren();

        styleEmulationElements.associateStylisticAndAntiElements(
                "b", StyleEmulationVisitor.ANTI_BOLD_ELEMENT,
                permittedChildren);

        styleEmulationElements.associateStylisticAndAntiElements(
                "i", StyleEmulationVisitor.ANTI_ITALICS_ELEMENT,
                permittedChildren);

        styleEmulationElements.associateStylisticAndAntiElements(
                "font", null, permittedChildren);

        // Groups of elements that permit stylistic elements as children into
        // sub-groups.

        // Initialize the elements that may contain stylistic elements.
        // The first group is the set of divisible other elements.
        // NOTE: We permit the null named element as a divisible element
        //       that may contain stylistic elements since we may encounter
        //       such elements as a by-product of other transformations.
        styleEmulationElements.addDivisibleElementsThatPermitStyles(
                new String[]{"big", "small", "sub", "sup", "tt", null});

        // The set of divisible phrase elements (corresponding to a set
        // defined in the HTML v3.2 dtd).
        styleEmulationElements.addDivisibleElementsThatPermitStyles(
                new String[]{"em", "strong", "dfn", "code", "samp", "kbd",
                            "var", "cite"});

        // The set of indivisible block elements (corresponding to a set
        // defined in the HTML v3.2 dtd).
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{ "p", "pre", "center", "blockquote", "li", "dt",
                            "dd", "caption", "th", "td", "form", "div"});

        // The set of indivisible heading elements (corresponding to a set
        // defined in the HTML v3.2 dtd).
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{"h1", "h2", "h3", "h4", "h5", "h6"});

        // The set of indivisible other elements
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{"a", "address", "applet", "body"});

        // Initialize the mergeable elements.
        styleEmulationElements.addMergeableElement("font");
    }

    // Javadoc inherited.
    protected void registerAttributeOnlyStyleEmulationPropertyRenderers(
            InternalDevice device) {
        super.registerAttributeOnlyStyleEmulationPropertyRenderers(device);
        // Register the style emulation property renderers in order of opening.

        // First, those that just add attributes. These require access to
        // the the protocol element which has just been rendered for this
        // XDIME/PAPI element in order to add atttributes to it.

        // text-align
        AttributeOnlyStyleEmulationPropertyRenderer textAlignRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                     new StyleEmulationElementSetAttributeRenderer(
                        new String[] {"p", "div", "table", "tr", "td", "th",
                                      "img", "input", "h1", "h2", "h3", "h4",
                                      "h5", "h6", "hr"}, "align",
                        new DefaultTextAlignEmulationAttributeValueRenderer())
                );
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.TEXT_ALIGN, textAlignRenderer);

        // vertical-align
        AttributeOnlyStyleEmulationPropertyRenderer verticalAlignRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                    new HTML3_2VerticalAlignEmulationPropertyRenderer()
                );
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.VERTICAL_ALIGN,
                verticalAlignRenderer);

        // padding
        registerPaddingPropertyRenderers();

        // margin
        registerMarginPropertyRenderers();

        // border-spacing
        AttributeOnlyStyleEmulationPropertyRenderer borderSpacingRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[] {"table"}, "cellspacing",
                        new HTML3_2BorderSpacingEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.BORDER_SPACING,
                borderSpacingRenderer);

        // border-width
        AttributeOnlyStyleEmulationPropertyRenderer borderWidthRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[] {"table", "img"}, "border",
                        new HTML3_2PixelsEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.BORDER_TOP_WIDTH,
                borderWidthRenderer);

        // width
        AttributeOnlyStyleEmulationPropertyRenderer widthRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[]{"table", "td", "th", "hr"}, "width",
                        new HTML3_2PixelsEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.WIDTH,
                widthRenderer);

        // height
        AttributeOnlyStyleEmulationPropertyRenderer heightRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[]{"td", "hr", "th"}, "height",
                        new HTML3_2PixelsEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.HEIGHT,
                heightRenderer);

        // background-color
        AttributeOnlyStyleEmulationPropertyRenderer backgroundColor =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[] {"body", "table", "tr", "td", "th"}, "bgcolor",
                        new HTML3_2ColorEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.BACKGROUND_COLOR,
                backgroundColor);

        // HTML 3.2 provides very limited means with which to emulate border
        // styling properties.  We can only specify a border on a table
        // and have have no control over the color.
        // For devices that support border styling we will allow the actual
        // css propeties to be inserted into the document using the
        // style attribute.
        registerBorderCSSPropertyRenderers(styleEmulationPropertyRendererSelector);
    }

    // Javadoc inherited.
    protected void registerAttributeAndOrElementStyleEmulationPropertyRenderers(
            InternalDevice device) {
        super.registerAttributeAndOrElementStyleEmulationPropertyRenderers(device);

        // Next, those that add elements and/or attributes. These require
        // access to the the protocol element which has just been rendered for
        // this XDIME/PAPI element only when rendering attributes.

        // color
        AttributeAndOrElementStyleEmulationPropertyRenderer colorRenderer =
                new AttributeAndOrElementStyleEmulationPropertyRenderer(
                        new HTML3_2ColorEmulationPropertyRenderer());
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.COLOR,
                colorRenderer);

        // font-size
        AttributeAndOrElementStyleEmulationPropertyRenderer fontSizeRenderer =
                new AttributeAndOrElementStyleEmulationPropertyRenderer(
                        new StyleEmulationElementAttributeRenderer("font", "size",
                        new HTML3_2FontSizeEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.FONT_SIZE,
                fontSizeRenderer);

    }

    // Javadoc inherited.
    protected void registerElementOnlyStyleEmulationPropertyRenderers(
            InternalDevice device) {
        super.registerElementOnlyStyleEmulationPropertyRenderers(device);

        // Finally, those that just add elements. These have no dependency
        // on the protocol element which has just been rendered for this
        // XDIME/PAPI element, as they do not need to add anything to it.
        // Thus, if, for example, a 'font' element has been added it will not
        // disturb these renderers.

        // font-weight
        ElementOnlyStyleEmulationPropertyRenderer fontWeightRenderer =
                new ElementOnlyStyleEmulationPropertyRenderer(
                        new DefaultFontWeightEmulationPropertyRenderer());
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.FONT_WEIGHT,
                fontWeightRenderer);

        // font-style
        ElementOnlyStyleEmulationPropertyRenderer fontStyleRenderer =
                new ElementOnlyStyleEmulationPropertyRenderer(
                        new DefaultFontStyleEmulationPropertyRenderer());
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.FONT_STYLE,
                fontStyleRenderer);
    }

    /**
     * Select the correct Padding Emulation Renderer.
     */
    private void registerPaddingPropertyRenderers() {

        final String[] elementsAllowingPaddingStyling = new String[]{"td"};

        // Can we use inline CSS?
        String[] top =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.PADDING_TOP);
        String[] bottom =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.PADDING_BOTTOM);
        String[] left =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.PADDING_LEFT);
        String[] right =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.PADDING_RIGHT);


        if (top.length == 1 && bottom.length == 1 && left.length == 1 &&
            right.length == 1) {

            // CSS padding emulation renderer
            registerPaddingPropertyRenderer(
                styleEmulationPropertyRendererSelector,
                StylePropertyDetails.PADDING_TOP,
                "padding-top");
            registerPaddingPropertyRenderer(
                styleEmulationPropertyRendererSelector,
                StylePropertyDetails.PADDING_BOTTOM,
                "padding-bottom");

            registerPaddingPropertyRenderer(
                styleEmulationPropertyRendererSelector,
                StylePropertyDetails.PADDING_LEFT,
                "padding-left");

            registerPaddingPropertyRenderer(
                styleEmulationPropertyRendererSelector,
                StylePropertyDetails.PADDING_RIGHT,
                "padding-right");

        } else {

            // General HTML 3.2 renderer
            AttributeOnlyStyleEmulationPropertyRenderer paddingRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                    new HTML3_2PaddingEmulationPropertyRenderer());
            styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.PADDING_TOP, paddingRenderer);
        }

    }

    /**
     * If inline CSS padding-top is suported on the device but margin-top
     * or margin-bottom are not then register a margin property renderer
     * which uses padding to emulate the margin on the top and bottom only.
     */
    private void registerMarginPropertyRenderers() {

        final String[] elementsAllowingPaddingStyling = new String[]{"td"};

        // Can we use inline CSS?
        String[] marginTop =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.MARGIN_TOP);
        String[] paddingTop =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.PADDING_TOP);
        String[] marginBottom =
            getElementsThatSupportStyleProperty(elementsAllowingPaddingStyling,
                StylePropertyDetails.MARGIN_BOTTOM);

        // Margin top is not supported but padding is
        if (marginTop.length == 0 && paddingTop.length == 1) {

            registerMarginPropertyRendererThatUsesPadding(
                styleEmulationPropertyRendererSelector,
                StylePropertyDetails.MARGIN_TOP,
                "margin-top");

        }

        // Margin bottom is not supported but padding is
        if (marginBottom.length == 0 && paddingTop.length == 1) {

            registerMarginPropertyRendererThatUsesPadding(
                styleEmulationPropertyRendererSelector,
                StylePropertyDetails.MARGIN_BOTTOM,
                "margin-bottom");

        }

        //todo should handle situation where inline CSS margin-xxx is supported
        // but that is not part of the curent work
    }

    /**
     * Registers renderers that actually insert CSS border properties using
     * the style attribute.  Such renderers are only registered if
     * the device supports the CSS that they generate.
     *
     * @param propertiesRenderer the renderer that holds all of the emulation
     * mappings.
     */
    private void registerBorderCSSPropertyRenderers(
            DefaultStyleEmulationPropertiesRenderer propertiesRenderer) {

        String[] elementsAllowingBorderStyling = new String[] {"td"};

        registerBorderStylePropertyRenderers(propertiesRenderer, elementsAllowingBorderStyling);
        registerBorderColorPropertyRenderers(propertiesRenderer, elementsAllowingBorderStyling);
        registerBorderWidthPropertyRenderers(propertiesRenderer, elementsAllowingBorderStyling);
    }

    private void registerBorderColorPropertyRenderer(
            DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
            StyleProperty styleProperty,
            String[] elementNames,
            String cssPropertyName) {

        // Only register the renderer against the element names that
        // the requesting device supports border-color styling on.
        String[] elementsSupportingBorderStyleProperty =
                getElementsThatSupportStyleProperty(elementNames,
                                                    styleProperty);

        AttributeOnlyStyleEmulationPropertyRenderer borderColorRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementAppendAttributeRenderer(
                        elementsSupportingBorderStyleProperty, "style",
                        new CSSColorPropertyRenderer(cssPropertyName)));

        propertiesRenderer.register(styleProperty, borderColorRenderer);
    }

    private void registerBorderWidthPropertyRenderer(
            DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
            StyleProperty styleProperty,
            String[] elementNames,
            String cssPropertyName) {


        // Only register the renderer against the element names that
        // the requesting device supports border-width-styling on.

        String[] elementsSupportingBorderWidthProperty =
                getElementsThatSupportStyleProperty(elementNames,
                                                    styleProperty);

        AttributeOnlyStyleEmulationPropertyRenderer borderWidthRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementAppendAttributeRenderer(
                        elementsSupportingBorderWidthProperty, "style",
                        new CSSBorderWidthPropertyRenderer(cssPropertyName)));

        propertiesRenderer.register(styleProperty, borderWidthRenderer);
    }

    /**
     * Register Padding Property Renderer against the given elements.
     * We assume that the list of elements has been filtered/checked against
     * those that the device supports.
     *
     * @param propertiesRenderer
     * @param styleProperty
     * @param cssPropertyName
     */
    private void registerPaddingPropertyRenderer(
        DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
        StyleProperty styleProperty,
        String cssPropertyName) {


        AttributeAndOrElementStyleEmulationPropertyRenderer paddingRenderer =
                new AttributeAndOrElementStyleEmulationPropertyRenderer(
                        new HTML3_2CSSPaddingPropertyRenderer(cssPropertyName));

        propertiesRenderer.register(styleProperty, paddingRenderer);
    }

    /**
     * Register Padding Property Renderer against the given elements.
     * We assume that the list of elements has been filtered/checked against
     * those that the device supports.
     *
     * @param propertiesRenderer
     * @param styleProperty
     * @param cssPropertyName
     */
    private void registerMarginPropertyRendererThatUsesPadding(
        DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
        StyleProperty styleProperty,
        String cssPropertyName) {


        AttributeAndOrElementStyleEmulationPropertyRenderer paddingRenderer =
                new AttributeAndOrElementStyleEmulationPropertyRenderer(
                        new HTML3_2CSSMarginPropertyRenderer(cssPropertyName));

        propertiesRenderer.register(styleProperty, paddingRenderer);
    }

    /**
     * Returns the elements that may be styled
     * (from the list of supplied elements) using the supplied
     * <code>styleProperty</code> on the requesting device.
     *
     * @param elements the elements to be tested for supporting the supplied
     * <code>styleProperty</code>.
     *
     * @param styleProperty the style property to test for support.
     *
     * @return the elements in the supplied <code>elements</code> that support
     * the supplied <code>styleProperty</code> on the requesting device.
     */
    private String[] getElementsThatSupportStyleProperty(
            String[] elements,
            StyleProperty styleProperty) {

        // Collection of elements that support the supplied style property.
        ArrayList elementsSupportingStyling = new ArrayList();

        // Iterate over the supplied element names and check their
        // support for the supplied style property.
        for (int currentElementIndex = 0;
             currentElementIndex < elements.length; currentElementIndex++) {

            // Inspect the currect element name
            String currentElementName = elements[currentElementIndex];

            DeviceElementCapability currentElementCapability =
                    deviceCapabilityManager.getDeviceElementCapability(
                            currentElementName, true);
            CapabilitySupportLevel currentElementsSupportForStyleProperty =
                currentElementCapability.getSupportType(styleProperty);

            // Note that we can safely use "==", rather than Object.equals(),
            // as CapabilitySupportLevel is a type safe enumeration
            // and each enumeration value is static.
            if (CapabilitySupportLevel.FULL == currentElementsSupportForStyleProperty) {
                elementsSupportingStyling.add(currentElementName);
            }
        }

        // Copy the elements in the list to a string array.
        elements = new String[elementsSupportingStyling.size()];
        return (String[])elementsSupportingStyling.toArray(elements);
    }

    private void registerBorderStylePropertyRenderer(
            DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
            StyleProperty styleProperty,
            String[] elementNames,
            String cssPropertyName) {

        // Only register the renderer against the element names that
        // the requesting device supports border-style styling on.

        String[] elementsSupportingBorderStyleProperty =
                getElementsThatSupportStyleProperty(elementNames,
                                                    styleProperty);

        AttributeOnlyStyleEmulationPropertyRenderer borderStyleRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementAppendAttributeRenderer(
                        elementsSupportingBorderStyleProperty, "style",
                        new CSSBorderStylePropertyRenderer(cssPropertyName)));

        propertiesRenderer.register(styleProperty, borderStyleRenderer);
    }

    private void registerBorderWidthPropertyRenderers(DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
                                                      String[] elementsToBeStyled) {

        // border-top-width
        registerBorderWidthPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_TOP_WIDTH,
                                           elementsToBeStyled, "border-top-width");
        // border-bottom-width
        registerBorderWidthPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                                            elementsToBeStyled, "border-bottom-width");

        // border-left-style
        registerBorderWidthPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_LEFT_WIDTH,
                                            elementsToBeStyled, "border-left-width");

        // border-right-style
        registerBorderWidthPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_RIGHT_WIDTH,
                                            elementsToBeStyled, "border-right-width");
    }

    private void registerBorderColorPropertyRenderers(DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
                                                      String[] elementsToBeStyled) {

        // border-top-color
        registerBorderColorPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_TOP_COLOR,
                                            elementsToBeStyled, "border-top-color");
        // border-bottom-color
        registerBorderColorPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_BOTTOM_COLOR,
                                            elementsToBeStyled, "border-bottom-color");

        // border-left-color
        registerBorderColorPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_LEFT_COLOR,
                                            elementsToBeStyled, "border-left-color");

        // border-right-color
        registerBorderColorPropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_RIGHT_COLOR,
                                            elementsToBeStyled, "border-color");

    }

    private void registerBorderStylePropertyRenderers(DefaultStyleEmulationPropertiesRenderer propertiesRenderer,
                                                      String[] elementsToBeStyled) {
        // border-top-style
        registerBorderStylePropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_TOP_STYLE,
                                              elementsToBeStyled, "border-top-style");

        // border-bottom-style
        registerBorderStylePropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_BOTTOM_STYLE,
                                              elementsToBeStyled, "border-bottom-style");

        // border-left-style
        registerBorderStylePropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_LEFT_STYLE,
                                              elementsToBeStyled, "border-left-style");

        // border-right-style
        registerBorderStylePropertyRenderer(propertiesRenderer, StylePropertyDetails.BORDER_RIGHT_STYLE,
                                              elementsToBeStyled, "border-right-style");
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/3	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 14-Sep-05	9496/3	pduffin	VBM:2005091211 Addressing review comments

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 15-Jun-05	8788/1	rgreenall	VBM:2005050501 Merge from 331

 15-Jun-05	8792/1	rgreenall	VBM:2005050501 Style emulation support for <td> element.

 19-May-05	8335/1	philws	VBM:2005051705 Port Palm WCA style emulation from 3.3

 19-May-05	8305/1	philws	VBM:2005051705 Provide style emulation rendering for HTML Palm WCA version 1.1

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	5877/10	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/8	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/6	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 21-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - fix anti-size in WML

 20-Jul-04	4897/5	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/2	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 14-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 29-Jun-04	4720/9	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 29-Jun-04	4720/7	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 29-Jun-04	4720/5	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
