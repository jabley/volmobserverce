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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLRootConfiguration.java,v 1.3 2003/03/17 11:21:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to encapsulate
 *                              XHTMLBasic configuration information.
 * 13-Feb-03    Byron           VBM:2003021309 - Modified constructor to
 *                              populate assetURLLocations list.
 * 12-Mar-03    Phil W-S        VBM:2003031110 - Add cardElements member,
 *                              update constructor to do the standard
 *                              population of this set and add
 *                              isPermittedCardChild method to check an element
 *                              against this set. Fix file indent.
 * 20-May-03    Mat             VBM:2003042907 - Added AttributeStartFactory,
 *                              AttributeValueFactory, ElementNameValue
 *                              properties.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom2wbsax.WBSAXProcessorConfiguration;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.protocols.TagEmulationFactory;
import com.volantis.mcs.protocols.EmulateEmphasisTag;
import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultFontStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultFontWeightEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultVerticalAlignEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.ElementOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementSetAttributeRenderer;
import com.volantis.mcs.protocols.href.WmlRuleSet;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.protocols.wml.css.emulator.renderer.WMLFontSizeEmulationPropertyRenderer;
import com.volantis.mcs.protocols.wml.css.emulator.renderer.WMLImageAltEmulationPropertyRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbdom.dissection.AtomicElementConfiguration;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.wbsax.AttributeStartFactory;
import com.volantis.mcs.wbsax.AttributeValueFactory;
import com.volantis.mcs.wbsax.ElementNameFactory;
import com.volantis.mcs.wbsax.ElementRegistrar;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.TokenTable;
import com.volantis.mcs.wbsax.VersionCode;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * This class defines properties of the WMLRoot protocol.
 *
 * @mock.generate base="ProtocolConfigurationImpl"
 */
public class WMLRootConfiguration extends ProtocolConfigurationImpl
        implements SerialisationConfiguration,
        AtomicElementConfiguration, ElementRegistrar,
        WBSAXProcessorConfiguration {

    private static final String[] permittedChildren = {
        "br", "img", "anchor", "a", "table", "em",
        "strong", "b", "i", "u", "big", "small"
    };

    /**
     * The mappings between element names and the prefix used to access all
     * values relating to the wml tag emulation.
     */
    private static final Map emulatedElements;

    // Initialise the static fields.
    static {
        emulatedElements = new HashMap();
        emulatedElements.put("big", DevicePolicyConstants.EMULATE_WML_BIG_TAG);
        emulatedElements.put("b", DevicePolicyConstants.EMULATE_WML_BOLD_TAG);
        emulatedElements.put("em",
                             DevicePolicyConstants.EMULATE_WML_EMPHASIZE_TAG);
        emulatedElements.put("i", DevicePolicyConstants.EMULATE_WML_ITALIC_TAG);
        emulatedElements.put("small",
                             DevicePolicyConstants.EMULATE_WML_SMALL_TAG);
        emulatedElements.put("strong",
                             DevicePolicyConstants.EMULATE_WML_STRONG_TAG);
        emulatedElements.put("u",
                             DevicePolicyConstants.EMULATE_WML_UNDERLINE_TAG);
        emulatedElements.put(WMLConstants.LINK_ELEMENT,
                             DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING);
        emulatedElements.put(WMLConstants.ANCHOR_ELEMENT,
                             DevicePolicyConstants.EMULATE_WML_LINK_HIGHLIGHTING);
        emulatedElements.put(WMLConstants.CARD_ELEMENT,
                             DevicePolicyConstants.EMULATE_WML_CARD_TITLE);
    }

    /**
     * The set of elements which may appear as direct children of a card.
     */
    protected final Set cardElements = new HashSet();

    /**
     * The element name factory
     */
    private final ElementNameFactory elementNameFactory;

    /**
     * The attributeValueFactory
     */
    private final AttributeValueFactory attributeValueFactory;

    /**
     * The attributeStartFactory
     */
    protected final AttributeStartFactory attributeStartFactory;

    /**
     * The VersionCode for the protocol.
     */
    private VersionCode versionCode;

    /**
     * The PublicIdCode for the protocol.
     */
    protected PublicIdCode publicIdCode;

    private final boolean[] alwaysEmptyElementTokens = new boolean[256];

    private final boolean[] atomicElementTokens = new boolean[256];

    private Map elementName2EmulatedTag;

    /**
     * Initialise.
     *
     * @param supportsDissection Specifies whether the protocol supports
     */
    private WMLRootConfiguration(boolean supportsDissection) {
        super(supportsDissection);

        // NB. If you modify atomicElements consider modifying
        // associateAtomicElement invocations below.
        atomicElements.add("a");
        atomicElements.add("anchor");
        atomicElements.add("do");

        alwaysEmptyElements.add("access");
        alwaysEmptyElements.add("br");
        alwaysEmptyElements.add("setvar");
        alwaysEmptyElements.add("img");
        alwaysEmptyElements.add("input");
        alwaysEmptyElements.add("meta");
        alwaysEmptyElements.add("noop");
        alwaysEmptyElements.add("postfield");
        alwaysEmptyElements.add("timer");

        assetURLLocations.add("a", "href");
        assetURLLocations.add("input", "src");
        assetURLLocations.add("img", "src");
        assetURLLocations.add("go", "href");

        cardElements.add("onevent");
        cardElements.add("timer");
        cardElements.add("do");
        cardElements.add(WMLConstants.BLOCH_ELEMENT);

        elementNameFactory = new ElementNameFactory();
        attributeStartFactory = new AttributeStartFactory();
        attributeValueFactory = new AttributeValueFactory();

        hrefRuleSet = new WmlRuleSet();
    }

    /**
     * initialise
     */
    public WMLRootConfiguration() {
        this(true);
    }

    public void initialize(InternalDevice device,
                           DeviceCapabilityManagerBuilder builder) {
        // By default, WMLRoot supports underline emulation
        defaultElementCapabilities.add(new DeviceElementCapability("u",
                CapabilitySupportLevel.FULL));
        super.initialize(device, builder);

        elementName2EmulatedTag = new HashMap();
        
        TagEmulationFactory factory = new TagEmulationFactory(device);
        for (Iterator i = emulatedElements.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String elementName = (String) entry.getKey();
            String prefix = (String) entry.getValue();
            EmulateEmphasisTag tag = factory.getTagEmphasisEmulation(prefix);
            elementName2EmulatedTag.put(elementName, tag);
        }
    }

    public EmulateEmphasisTag getEmulateEmphasisTag(String elementName) {
        return (EmulateEmphasisTag) elementName2EmulatedTag.get(elementName);
    }

    // Javadoc inherited.
    public void createStyleEmulationElements() {
        super.createStyleEmulationElements();

        final Set allPermittedChildren = getAllPermittedChildren();
        styleEmulationElements.associateStylisticAndAntiElements("b",
                StyleEmulationVisitor.ANTI_BOLD_ELEMENT, allPermittedChildren);

        styleEmulationElements.associateStylisticAndAntiElements("i",
                StyleEmulationVisitor.ANTI_ITALICS_ELEMENT,
                allPermittedChildren);

        styleEmulationElements.associateStylisticAndAntiElements("big",
                StyleEmulationVisitor.ANTI_SIZE_ELEMENT,
                allPermittedChildren);

        styleEmulationElements.associateStylisticAndAntiElements("small",
                StyleEmulationVisitor.ANTI_SIZE_ELEMENT,
                allPermittedChildren);

        // Groups of elements that permit stylistic elements as children into
        // sub-groups.

        // NOTE: We permit the null named element as a divisble element
        //       that may contain stylistic elements since we may encounter
        //       such elements as a by-product of other transformations.
        styleEmulationElements.addDivisibleElementsThatPermitStyles(
                new String[]{null});

        // The set of divisible phrase elements (corresponding to a set
        // defined in the WML 1,1 dtd).
        styleEmulationElements.addDivisibleElementsThatPermitStyles(
                new String[]{"em", "strong"});

        // The set of indivisible block elements (corresponding to a set
        // defined in the WML 1.1 dtd).
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{WMLConstants.BLOCH_ELEMENT, "td", "fieldset"});
    }

    /**
     * @param tokens
     */
    protected void useTokenTable(TokenTable tokens) {
        tokens.registerAttrStarts(attributeStartFactory);
        tokens.registerAttrValues(attributeValueFactory);
        tokens.registerTags(elementNameFactory);
        versionCode = tokens.getVersion();
        publicIdCode = tokens.getPublicId();

        // Also register this data with us so that we can translate the
        // string based content of this class into token based content.
        tokens.registerTags(this);
    }

    public boolean isPermittedCardChild(String element) {
        return cardElements.contains(element);
    }

    /**
     * Getter for the AttributeStartFactory
     *
     * @return The attributeStartFactory
     */
    public AttributeStartFactory getAttributeStartFactory() {
        return attributeStartFactory;
    }

    /**
     * Getter for the elementNameFactory
     *
     * @return The elementNameFactory
     */
    public ElementNameFactory getElementNameFactory() {
        return elementNameFactory;
    }

    /**
     * Getter for the version code.
     *
     * @return The VersionCode for this protocol
     */
    public VersionCode getVersionCode() {
        return versionCode;
    }

    /**
     * Getter for the public ID code.
     *
     * @return the public ID code for this protocol
     */
    public PublicIdCode getPublicIdCode() {
        return publicIdCode;
    }

    public void registerElement(int token, String name) {
        if (isElementAlwaysEmpty(name)) {
            alwaysEmptyElementTokens[token] = true;
        }
        if (isElementAtomic(name)) {
            atomicElementTokens[token] = true;
        }
    }

    public boolean isElementAtomic(int token) {
        return atomicElementTokens[token];
    }

    public EmptyElementType getEmptyElementType(int token) {
        if (alwaysEmptyElementTokens[token]) {
            return EmptyElementType.EmptyTag;
        } else {
            return EmptyElementType.StartAndEndTag;
        }
    }

    // javadoc inherited.
    public EmptyElementType getEmptyElementType(String name) {
        if (isElementAlwaysEmpty(name)) {
            return EmptyElementType.EmptyTag;
        } else {
            return EmptyElementType.StartAndEndTag;
        }
    }

    // javadoc inherited.
    public boolean isURLAttribute(String elementName, String attributeName) {
        Set assetURLattributes =
                assetURLLocations.getElementAttributes(elementName);
        return assetURLattributes != null &&
                assetURLattributes.contains(attributeName);
    }

    // Javadoc inherited.
    protected void registerAttributeOnlyStyleEmulationPropertyRenderers(
            InternalDevice device) {
        super.registerAttributeOnlyStyleEmulationPropertyRenderers(device);
        // Register the style emulation property renderers in order of opening.

        // First, those that just add attributes. These require access to
        // the the protocol element which has just been rendered for this
        // XDIME/PAPI element in order to add attributes to it.

        // Handle text align specially since we delay rendering of p's until
        // style emulation. Disable this (maybe temporarily) until we decide
        // exactly how text-align on p's is best rendered in the long term.
        // See WMLDOMTransformer.fixCard().
//        // text-align
//        styleEmulationPropertyRendererSelector.register(
//                StylePropertyDetails.TEXT_ALIGN,
//                new StyleEmulationElementSetAttributeRenderer(
//                        new String[]{"p"}, "align",
//                        new DefaultTextAlignEmulationAttributeValueRenderer()));
        // vertical-align
        AttributeOnlyStyleEmulationPropertyRenderer verticalAlignRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[]{"img"}, "align",
                        new DefaultVerticalAlignEmulationAttributeValueRenderer()));

        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.VERTICAL_ALIGN,
                verticalAlignRenderer);
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

        // font-size
        ElementOnlyStyleEmulationPropertyRenderer fontSizeRenderer =
                new ElementOnlyStyleEmulationPropertyRenderer(
                        new WMLFontSizeEmulationPropertyRenderer());
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.FONT_SIZE,
                fontSizeRenderer);

        //mcs-image-saving
        if (device != null) {
            // If the device supports being able to control whether an image
            // can not be saved then register an emulator to handle it.
            String policy = device.getPolicyValue(
                    DevicePolicyConstants.WML_IMAGE_NOSAVE);
            if (DevicePolicyConstants.WML_IMAGE_NOSAVE__ALT_NO_SAVE.equals(policy)) {

                AttributeOnlyStyleEmulationPropertyRenderer imageSavingRenderer =
                        new AttributeOnlyStyleEmulationPropertyRenderer(
                                new WMLImageAltEmulationPropertyRenderer());
                styleEmulationPropertyRendererSelector.register(
                        StylePropertyDetails.MCS_IMAGE_SAVING,
                        imageSavingRenderer);
            }
        }
    }

    public ValidationHelper getValidationHelper() {
        return WMLValidationHelper.getDefaultInstance();
    }

    // Javadoc inherited.
    public String[] getPermittedChildren() {
        return permittedChildren;
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

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 14-Oct-05	9825/3	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 03-Oct-05	9522/4	ibush	VBM:2005091502 no_save on images

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 21-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 15-Sep-05	9472/4	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9496/3	pduffin	VBM:2005091211 Addressing review comments

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 24-Jun-05	8833/5	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	5877/10	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/8	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/6	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 19-Oct-04	5843/1	geoff	VBM:2004100710 Invalid WML is being generated since introduction of theme style options (R599)

 21-Jul-04	4752/9	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - fix anti-size in WML

 20-Jul-04	4897/4	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 14-Jul-04	4783/5	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 14-Jul-04	4752/7	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 14-Jul-04	4752/5	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 29-Jun-04	4720/9	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 29-Jun-04	4720/7	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 28-Jun-04	4720/5	byron	VBM:2004061604 Core Emulation Facilities - rework issues

 25-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/3	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
