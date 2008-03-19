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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ProtocolConfiguration.java,v 1.3 2003/02/18 13:59:43 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-May-02    Paul            VBM:2002042202 - Created to encapsulate
 *                              protocol specific information.
 * 25-Sep-02    Byron           VBM:2002091904 - Added getDeviceThemeProxy() and
 *                              associated attribute
 * 13-Feb-03    Phil W-S        VBM:2003021306 - Added the assetURLLocations
 *                              member, basic initialization of this in the
 *                              constructor and added the
 *                              addCandidateElmentAssetURLs method.
 * 17-Feb-03    Byron           VBM:2003020610 - Modified
 *                              addCandidateElementAssetURLs so that the loop
 *                              can terminate correclty.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.css.version.CSSProperty;
import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.xml.XMLDTDBuilder;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityConstants;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManager;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeAndOrElementStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultStyleEmulationPropertiesRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.ElementOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.OnOffKeywordElementPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertiesRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.dissection.Dissector;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.href.NoOpRuleSet;
import com.volantis.mcs.protocols.html.css.emulator.renderer.MarqueeEmulationAttributePropertyRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.MarqueeEmulationRenderer;
import com.volantis.mcs.protocols.trans.DefaultStyleEmulationElementConfiguration;
import com.volantis.mcs.protocols.trans.StyleEmulationElementConfiguration;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextLineThroughStyleKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of a ProtocolConfiguration
 *
 * @mock.generate
 */
public class ProtocolConfigurationImpl
        implements ProtocolConfiguration {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(ProtocolConfigurationImpl.class);

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Flag which indicates whether empty elements need a space before the
     * />.
     */
    protected boolean emptyElementRequiresSpace;

    /**
     * The set of elements which are always empty.
     */
    protected final Set alwaysEmptyElements;

    /**
     * The set of elements which must never be split.
     */
    protected final Set atomicElements;

    /**
     * The set of elements which are blocky. Subclasses are expected to
     * populate this data structure.
     */
    protected final Set blockyElements;

    /**
     * The set of element attributes which the protocol may generate that
     * may contain asset URLs.
     */
    protected final ElementAttributeMapper assetURLLocations;

    /**
     * The old style dissector that is used by this protocol.
     */
    private final Dissector dissector;

    /**
     * The set of style properties which have been deemed important (i.e.
     * their values should not be lost during optimization) for this protocol.
     */
    protected final Set importantProperties;

    /**
     * flag value to show if a protocol supports subscript elements
     */
    protected boolean canSupportSubScriptElement;

    /**
     * flag value to show if a protocol supports superscript elements
     */
    protected boolean canSupportSuperScriptElement;

    /**
     * This flag specifies whether this protocol supports events at all, if
     * they do then the following bit masks need setting accordingly.
     */
    protected boolean canSupportEvents;

    /**
     * Flag which specifies whether the protocol supports Framework Client installer
     */
    private boolean canSupportFileUpload = false;


    /**
     * These bit masks define the events which are supported by this
     * protocol.
     */
    protected int supportedGeneralEvents;

    /**
     * flag value to indicate a protocols support of javascript.
     */
    private boolean canSupportJavaScript;

    /**
     * Set that includes the Href ruleset used by the href transformer
     */
    protected HashMap hrefRuleSet;

    /**
     * Set that includes the highlight ruleset used by the highlight transformer
     */
    protected HashMap highlightRuleSet;

    /**
     * Set that includes the corners ruleset used by the Corners transformer
     */
    protected HashMap cornersRuleSet;    
    
    /**
     * The object which manages the capabilities of the device
     */
    protected DeviceCapabilityManager deviceCapabilityManager;

    /**
     * The object which describes the CSS version that the protocol supports.
     */
    private CSSVersion cssVersion;

    /**
     * The style sheet extractor configuration.
     */
    private ExtractorConfiguration extractorConfiguration;


    /**
     * Flag which specifies whether the protocol supports Framework Client
     */
    private boolean supportsFrameworkClient = false;
    
    /**
     * Flag which specifies version of Client Framework since
     * it is supported by the protocol
     */
    private String supportsFrameworkClientSince =
        DevicePolicyConstants.SUPPORTS_VFC_SINCE_DEFAULT;

    /**
     * Flag which specifies whether the protocol supports Framework Client installer
     */
    private boolean supportsFrameworkClientInstalller = false;
    
    /**
     * Variable holds pixelsX for device, default value is -1.
     */
    private int devicePixelsX = -1;
    
    /**
      * The style property to rule mapping.
      */
    protected final DefaultStyleEmulationPropertiesRenderer
            styleEmulationPropertyRendererSelector;

    /**
     * The style emulation element configuration instance.
     */
    protected DefaultStyleEmulationElementConfiguration styleEmulationElements;

    private String cssMedia;
    
    /**
     * The maximum frame rate supported, expressed in Hz.
     * Value Double.POSITIVE_INFINITY means no frame rate limit.
     */
    private double maxClientFrameRate;

    protected final Set defaultElementCapabilities = new HashSet();

    /**
     * Need to maintain a list of the emulation elements that can be used by
     * this protocol. This is reasonable, because even though they are not
     * supported by the protocol by default, they will only be used if a) the
     * device explicitly says it supports them and b) explicitly says it
     * doesn't support the equivalent CSS.
     * This list is added to the list of elements that are allowable children
     * of stylistic elements (i.e. b, i) which is used to configure the
     * StyleEmulationElementConfiguration. If the emulation elements were not
     * included, then they would be removed rather than pushed down if they
     * were not valid in that position.
     * */
    private final Set fakePermittedChildren = new HashSet();

    /**
     * The default capability for the td element.
     */
    private final DeviceElementCapability DEFAULT_TD_CAPABILITY =
                new DeviceElementCapability("td", CapabilitySupportLevel.FULL);

    /**
     * Map of elements which cannot contain form links (i.e. links between form
     * fragments) and the child element which should be inserted in order to
     * resolve this. Used by {@link DOMProtocol#validateFormLinkParent}.
     * <p/>
     * NB: This may not be a one step process. For example, in XHTMLBasic,
     * table contain a form link so a child tr should be inserted. However a tr
     * also cannot contain a form link, so a child td should be inserted. The
     * td can contain a form link, so our work is done.
     */
    protected final Map invalidFormLinkParents = new HashMap();

    /**
     * The optional DTD.
     */
    protected DTD dtd;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param supportsDissection Specifies whether the protocol supports
     *                           dissection.
     */
    protected ProtocolConfigurationImpl(boolean supportsDissection) {
        alwaysEmptyElements = new HashSet();
        atomicElements = new HashSet();
        assetURLLocations = new ElementAttributeMapper();
        importantProperties = new HashSet();

        blockyElements = new HashSet();

        if (supportsDissection) {
            dissector = new Dissector(this);
        } else {
            dissector = null;
        }

        hrefRuleSet = new NoOpRuleSet();
        highlightRuleSet = new NoOpRuleSet();
        cornersRuleSet = new NoOpRuleSet();

        styleEmulationPropertyRendererSelector =
                new DefaultStyleEmulationPropertiesRenderer();
    }

    /**
     * Initialise. 
     *
     */
    public ProtocolConfigurationImpl() {
        this(false);
    }

    /**
     * Initialize the device (for example, by configuring the style emulation
     * renderers that are required).
     *
     * @param device    encapsulates the device information
     * @param builder   used to
     */
    public void initialize(InternalDevice device,
                           DeviceCapabilityManagerBuilder builder) {

        defaultElementCapabilities.add(DEFAULT_TD_CAPABILITY);

        builder.addDefaultValues(defaultElementCapabilities);
        deviceCapabilityManager = builder.build();
        registerStyleEmulationPropertyRenderers(device);
        createStyleEmulationElements();
        registerMaxClientFrameRate(device);

        this.dtd = createDTD(device);
    }

    /**
     * Create the DTD for this protocol for the specified device.
     *
     * @param device The device for which the DTD is to be created.
     * @return The DTD, or null if DTD is not supported.
     */
    protected DTD createDTD(InternalDevice device) {
        DTDBuilder builder = new XMLDTDBuilder();
        builder.setMaximumLineLength(getMaximumLineLength(device));

        addContentModel(builder, device);

        return builder.buildDTD();
    }

    protected void addContentModel(DTDBuilder builder, InternalDevice device) {

        builder.setEmptyTagRequiresSpace(emptyElementRequiresSpace);

        builder.addIgnorableElement(DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT);
        builder.addIgnorableElement(DissectionConstants.KEEPTOGETHER_ELEMENT);
        builder.addIgnorableElement(null);
    }

    /**
     * Get the maximum line length for the requesting device.
     *
     * @return the maximum line length in characters for the requesting
     *         device.
     */
    protected int getMaximumLineLength(InternalDevice device) {
        String lineLength = device.getPolicyValue(
                DevicePolicyConstants.MAXIMUM_LINE_CHARS);

        int result = 0;

        if ((lineLength != null) && (lineLength.length() > 0)) {
            result = Integer.parseInt(lineLength);
        }

        return result;
    }

    //javadoc inherited
    public DeviceCapabilityManager getDeviceCapabilityManager() {
        return deviceCapabilityManager;
    }

    /**
     * Set the deviceCapabilityManager
     * @param deviceCapabilityManager
     */
    public void setDeviceCapabilityManager(
            DeviceCapabilityManager deviceCapabilityManager) {
        this.deviceCapabilityManager = deviceCapabilityManager;
    }

    /**
     * Get the value of the emptyElementRequiresSpace property.
     *
     * @return The value of the emptyElementRequiresSpace property.
     */
    public boolean getEmptyElementRequiresSpace() {
        return emptyElementRequiresSpace;
    }

    /**
     * This method is called with the name of an element and returns true if
     * it is always empty and false otherwise.
     *
     * @param name The name of an element.
     * @return True if the element is empty and false otherwise.
     */
    public boolean isElementAlwaysEmpty(String name) {
        return alwaysEmptyElements.contains(name);
    }

    /**
     * This method is called with the name of an element and returns true if
     * the element is atomic and should not be split and false if it can be.
     *
     * @param name The name of an element.
     * @return True if the element is atomic and false if it can be split.
     */
    public boolean isElementAtomic(String name) {
        return atomicElements.contains(name);
    }

    /**
     * Finds candidate element attribute values which could be an asset URL
     * and submits them to the given PackageResources object.
     * This utilizes the {@link #assetURLLocations} member to determine if the
     * given element has one or more candidate attributes.
     *
     * @param element          the DOM element to be checked for candidate attributes
     * @param packageResources the PackageResources instance to which candidate
     *                         asset URLs should be submitted
     */
    public void addCandidateElementAssetURLs(
            Element element,
            PackageResources packageResources) {
        if (packageResources != null) {
            Set assetURLattributes =
                    assetURLLocations.getElementAttributes(element.getName());

            if (assetURLattributes != null) {
                Attribute elementAttribute = element.getAttributes();

                while (elementAttribute != null) {
                    if (assetURLattributes.contains(
                            elementAttribute.getName())) {
                        packageResources.addEncodedURLCandidate(
                                elementAttribute.getValue());
                    }
                    elementAttribute = elementAttribute.getNext();
                }
            }
        }
    }

    public ValidationHelper getValidationHelper() {
        return null;
    }

    public Dissector getDissector() {
        return dissector;
    }

    // Javadoc inherited.
    public boolean optimisationWouldLoseImportantStyles(Element element) {

        boolean wouldLoseStyles = false;

        // check if there are any significant properties for this protocol, and
        // that the supplied element has Styles
        if (importantProperties != null && element != null &&
                element.getStyles() != null) {
            wouldLoseStyles = StylePropertyAnalyser.getInstance().
                hasVisuallyImportantProperty(importantProperties, element);
        }

        return wouldLoseStyles;
    }

    /**
     * Get the default style value associated with a given style property
     * on a given element
     *
     * @param elementType
     * @param property
     * @return String - default style value
     *         todo refactor to use data structure supporting all elements
     */
    public boolean isElementAttributeDefaultStyle(
            String elementType,
            StyleProperty property,
            StyleValue styleValue) {
        boolean defaultValue = false;
        if (elementType != null) {
            if (elementType.equals("sub")) {
                StyleValue fontSizeValue =
                    STYLE_VALUE_FACTORY.getString(null, ".83em");
                StyleValue subValue = STYLE_VALUE_FACTORY.getString(null, "sub");
                if (property.equals(StylePropertyDetails.FONT_SIZE)
                        && styleValue.equals(fontSizeValue)) {
                    defaultValue = true;
                } else if (property.equals(
                        StylePropertyDetails.VERTICAL_ALIGN)
                        && styleValue.equals(subValue)) {
                    defaultValue = true;
                }
            } else if (elementType.equals("sup")) {
                StyleValue fontSizeValue =
                    STYLE_VALUE_FACTORY.getString(null, ".83em");
                StyleValue supValue =
                    STYLE_VALUE_FACTORY.getString(null, "sup");
                if (property.equals(StylePropertyDetails.FONT_SIZE)
                        && styleValue.equals(fontSizeValue)) {
                    defaultValue = true;
                } else if (property.equals(
                        StylePropertyDetails.VERTICAL_ALIGN)
                        && styleValue.equals(supValue)) {
                    defaultValue = true;
                }
            }
        }
        return defaultValue;
    }

    // javadoc inherited
    public boolean isElementBlocky(String name) {
        return blockyElements.contains(name);
    }

    /**
     * Does the protocol support events
     *
     * @return boolean
     */
    public boolean supportsEvents() {
        return canSupportEvents;
    }

    public int getSupportedGeneralEvents() {
        return supportedGeneralEvents;
    }

    /**
     * Describes if a protocol supports the use of the SubScript element
     *
     * @return true iff the device does support the SubScript element
     */
    public boolean getCanSupportSubScriptElement() {
        return canSupportSubScriptElement;
    }

    /**
     * Describes if a protocol supports the use of the SuperScript element
     *
     * @return true iff the device does support the SuperScript element
     */
    public boolean getCanSupportSuperScriptElement() {
        return canSupportSuperScriptElement;
    }


    /**
     * Set the value of canSupportJavaScript. This field determines the value
     * of the canSupportEvents.
     *
     * @param canSupportJavaScript
     */
    public void setCanSupportJavaScript(boolean canSupportJavaScript) {
        this.canSupportJavaScript = canSupportJavaScript;
        this.canSupportEvents = canSupportJavaScript;
    }

    /**
     * determines if the protocol supports javascript
     *
     * @return
     */
    public boolean supportsJavaScript() {
        return canSupportJavaScript;
    }


    /**
     * Return the ruleset used by the Href transformer to handle href
     * attributes on XHTML2 elements.
     *
     * @return
     */
    public HashMap getHrefTransformationRules() {
        return hrefRuleSet;
    }

    // Javadoc inherited.
    public CSSVersion getCssVersion() {
        return cssVersion;
    }

    // Javadoc inherited.
    public StyleEmulationElementConfiguration
            getStyleEmulationElementConfiguration() {
        return styleEmulationElements;
    }

    /**
     * @see #cssVersion
     */
    public void setCssVersion(CSSVersion cssVersion) {
        this.cssVersion = cssVersion;
    }

    public ExtractorConfiguration getExtractorConfiguration() {
        return extractorConfiguration;
    }

    public void setExtractorConfiguration(
            ExtractorConfiguration extractorConfiguration) {
        this.extractorConfiguration = extractorConfiguration;
    }

    /**
     * Set whether or not this protocol supports client framework.
     */
    public void setFrameworkClientSupported(boolean isFrameworkClientSupported){
        this.supportsFrameworkClient = isFrameworkClientSupported;
    }
    
    /**
     * Returns whether or not this protocol supports client framework 
     * in current MCS version and false otherwise. 
     * @return true if Framework Client is supported; false otherwise
     */
    public boolean isFrameworkClientSupported(){
        return (this.supportsFrameworkClient        
                || this.supportsFrameworkClientSince.equals("1.0")
                || this.supportsFrameworkClientSince.equals("1.1")
                || this.supportsFrameworkClientSince.equals("1.2")
                || this.supportsFrameworkClientSince.equals("1.3"));
    }
    
    /**
     * Returns whether or not this protocol supports client framework.
     *
     * @return true if Framework Client is supported; false otherwise
     */
    public boolean getFrameworkClientSupported(){
        return this.supportsFrameworkClient;        
    }

    /**
     * Set version of CFW since from this protocol supports client framework.
     */
    public void setFrameworkClientSupportedSince(String frameworkClientSupportedSince){
        this.supportsFrameworkClientSince = frameworkClientSupportedSince;
    }
    
    /**
     * Returns version of CFW since from this protocol supports client framework.
     * 
     * @return version of CFW of none if device is not supported
     */
    public String getFrameworkClientSupportedSince() {
        return this.supportsFrameworkClientSince;
    }
    
    /**
     * Return the style emulation property renderer selector for this
     * protocol. It may be null if no styles need to be emulated for this
     * protocol.
     */
    public StyleEmulationPropertiesRenderer
            getStyleEmulationPropertyRendererSelector() {

        return styleEmulationPropertyRendererSelector;
    }

    /**
     * Initialize the rule mappings for this protocol.
     * @param device
     */
    private void registerStyleEmulationPropertyRenderers(
            InternalDevice device) {

        // Can't configure style property emulation if we have no information
        // about what the device supports.
        if (device == null) {
            LOGGER.warn("cannot-configure-style-emulators");
            return;
        }

        // Register the style emulation property renderers in order of opening.

        // First, those that just add attributes. These require access to
        // the the protocol element which has just been rendered for this
        // XDIME/PAPI element in order to add atttributes to it.
        registerAttributeOnlyStyleEmulationPropertyRenderers(device);

        // Next, those that add elements and/or attributes. These require
        // access to the the protocol element which has just been rendered for
        // this XDIME/PAPI element only when rendering attributes.
        registerAttributeAndOrElementStyleEmulationPropertyRenderers(device);

        // Finally, those that just add elements. These have no dependency
        // on the protocol element which has just been rendered for this
        // XDIME/PAPI element, as they do not need to add anything to it.
        // Thus, if, for example, a 'font' element has been added it will not
        // disturb these renderers.
        registerElementOnlyStyleEmulationPropertyRenderers(device);
    }

    /**
     * Registers the style property emulators that just add attributes. These
     * require access to the the protocol element which has just been rendered
     * for this XDIME/PAPI element in order to add atttributes to it. These
     * renderers should be registered first.
     *
     * @param device
     */
    protected void registerAttributeOnlyStyleEmulationPropertyRenderers(
            InternalDevice device) {
        // There are currently no completely generic attribute only style
        // property emulation renderers.
    }

    /**
     * Registers the style property emulators that can add elements and/or
     * attributes. These require access to the the protocol element which has
     * just been rendered for this XDIME/PAPI element only when rendering
     * attributes.ust add attributes. These renderers should be registered
     * after the attribute only emulators and before the element only ones.
     *
     * @param device
     */
    protected void registerAttributeAndOrElementStyleEmulationPropertyRenderers(
            InternalDevice device) {
        registerMarqueeEmulationRenderers(device);
    }

    /**
     * Registers the style property emulators that just add elements. These
     * have no dependency on the protocol element which has just been rendered
     * for this XDIME/PAPI element, as they do not need to add anything to it.
     * Thus, if, for example, a 'font' element has been added it will not
     * disturb these renderers.just add attributes. These renderers should be
     * registered last.
     *
     * @param device
     */
    protected void registerElementOnlyStyleEmulationPropertyRenderers(
            InternalDevice device) {
        // mcs-text-underline-style
        addElementOnlyEmulationRenderer("u",
                new StyleProperty[] {
                    /*StylePropertyDetails.MCS_TEXT_UNDERLINE_COLOR,*/
                    StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE,
                },
                new OnOffKeywordElementPropertyRenderer(
                        MCSTextUnderlineStyleKeywords.SOLID, "u",
                        MCSTextUnderlineStyleKeywords.NONE,
                        StyleEmulationVisitor.ANTI_UNDERLINE_ELEMENT));

        // mcs-text-blink
        addElementOnlyEmulationRenderer("blink",
                new StyleProperty[] {StylePropertyDetails.MCS_TEXT_BLINK},
                new OnOffKeywordElementPropertyRenderer(
                        MCSTextBlinkKeywords.BLINK, "blink",
                        MCSTextBlinkKeywords.NONE,
                        StyleEmulationVisitor.ANTI_BLINK_ELEMENT));

        // mcs-text-underline
        addElementOnlyEmulationRenderer("strike",
                new StyleProperty[] {
                    /*StylePropertyDetails.MCS_TEXT_LINE_THROUGH_COLOR,*/
                    StylePropertyDetails.MCS_TEXT_LINE_THROUGH_STYLE,
                },
                new OnOffKeywordElementPropertyRenderer(
                        MCSTextLineThroughStyleKeywords.SOLID, "strike",
                        MCSTextLineThroughStyleKeywords.NONE,
                        StyleEmulationVisitor.ANTI_STRIKE_ELEMENT));
    }

    /**
     * Register the appropriate marquee emulation renderers, depending on the
     * the level of support for the marquee element specified in the device
     * repository.
     *
     * @param device
     */
    private void registerMarqueeEmulationRenderers(InternalDevice device) {

        if (device == null) {
            // Can't configure any marquee style property emulators if the
            // device is null.
            return;
        }

        // If the device doesn't support the emulation element, then we
        // can't emulate it (regardless of whether the css is supported).
        if (canEmulateElement("marquee")) {
            // We can emulate the styles - check if we need to...
            DeviceElementCapability dec = deviceCapabilityManager.
                    getDeviceElementCapability("marquee", true);

            boolean supportsDisplayKeyword;
            final CSSVersion cssVersion = getCssVersion();
            if (cssVersion == null) {
                supportsDisplayKeyword = false;
            } else {
                CSSProperty display = cssVersion.getProperty(
                        StylePropertyDetails.DISPLAY);
                // In for the marquee css properties to have an effect, the
                // display must be set to marquee. If this keyword isn't
                // supported, then all marquee properties must be emulated.
                supportsDisplayKeyword = display.supportsKeyword(
                        DisplayKeywords.MCS_MARQUEE);
            }

            // The value of mcs-marquee-style determines if the other marquee
            // styles apply. So, if the mcs-marquee-style property css isn't
            // supported by the device, then we need to emulate all of the
            // others in order to display the requested marquee effects.
            boolean marqueeStyleCssSupported = supportsDisplayKeyword &&
                    cssVersion.getProperty(
                            StylePropertyDetails.MCS_MARQUEE_STYLE) != null;
            boolean behaviourSupported = CapabilitySupportLevel.FULL.equals(
                    dec.getSupportType(DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT));
            // Emulate only if the css is not supported, and the attribute is.
            if (!marqueeStyleCssSupported && behaviourSupported) {

                // This renderer must be added before the other marquee
                // renderers; it ensures there is a marquee element, and the
                // others will not add attribute values to any other element
                AttributeAndOrElementStyleEmulationPropertyRenderer renderer =
                        new AttributeAndOrElementStyleEmulationPropertyRenderer(
                                new MarqueeEmulationRenderer(
                                        DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT, false));
                styleEmulationPropertyRendererSelector.register(
                        StylePropertyDetails.MCS_MARQUEE_STYLE, renderer);

                // If we're going to be rendering this element, then need
                // to support it when transforming...
                fakePermittedChildren.add("marquee");


                // The rest of the marquee styles must be emulated if the
                // marquee style is emulated (and cannot be if it is not).

                // Marquee repetition.
                boolean marqueeLoopCssSupported = marqueeStyleCssSupported &&
                        (cssVersion.getProperty(
                                StylePropertyDetails.MCS_MARQUEE_REPETITION) != null);
                boolean loopSupported = CapabilitySupportLevel.FULL.equals(
                        dec.getSupportType(DeviceCapabilityConstants.MARQUEE_LOOP_ATT));
                if (!marqueeLoopCssSupported && loopSupported) {

                    AttributeAndOrElementStyleEmulationPropertyRenderer loopRenderer =
                            new AttributeAndOrElementStyleEmulationPropertyRenderer(
                                    new MarqueeEmulationAttributePropertyRenderer(
                                            DeviceCapabilityConstants.MARQUEE_LOOP_ATT, false));
                    styleEmulationPropertyRendererSelector.register(
                            StylePropertyDetails.MCS_MARQUEE_REPETITION,
                            loopRenderer);
                }

                // Marquee direction.
                boolean marqueeDirCssSupported = marqueeStyleCssSupported &&
                        (cssVersion.getProperty(StylePropertyDetails.MCS_MARQUEE_DIRECTION) != null);
                boolean directionSupported = CapabilitySupportLevel.FULL.equals(
                        dec.getSupportType(DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT));
                if (!marqueeDirCssSupported && directionSupported) {

                    AttributeAndOrElementStyleEmulationPropertyRenderer dirRenderer =
                            new AttributeAndOrElementStyleEmulationPropertyRenderer(
                                    new MarqueeEmulationAttributePropertyRenderer(
                                            DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT, false));
                    styleEmulationPropertyRendererSelector.register(
                            StylePropertyDetails.MCS_MARQUEE_DIRECTION, dirRenderer);
                }

                // Marquee background colour.
                if (!supportsDisplayKeyword &&
                        CapabilitySupportLevel.FULL.equals(dec.getSupportType(
                                DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT))) {
                    AttributeAndOrElementStyleEmulationPropertyRenderer bgColorRenderer
                            = new AttributeAndOrElementStyleEmulationPropertyRenderer(
                                    new MarqueeEmulationAttributePropertyRenderer(
                                            DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT, false));
                    styleEmulationPropertyRendererSelector.register(
                            StylePropertyDetails.BACKGROUND_COLOR, bgColorRenderer);
                }
            }
        }
    }

    /**
     * Determine if the named element can be emulated, given the current
     * device's capabilities.
     *
     * @param elementName   of the element we wish to emulate
     * @return true if the device can emulate styles using the named element,
     * and false otherwise
     */
    private boolean canEmulateElement(String elementName) {

        // Start by assuming that we don't need to emulate the element.
        boolean canEmulate = false;
        DeviceElementCapability dec = deviceCapabilityManager.
                getDeviceElementCapability(elementName, true);
        if (dec.getElementSupportLevel() != null &&
            dec.getElementSupportLevel() != CapabilitySupportLevel.NONE) {
            canEmulate = true;
        }
        return canEmulate;
    }

    /**
     * Add an element only renderer if:
     * <ul>
     * <li>the css to represent the given style property is not supported by
     * the device.</li>
     * <li>the element used for emulating the supplied style property is
     * supported by the device.</li>
     * </ul>
     *
     * @param elementName   of the element that is used to emulate the given
     *                      style property
     * @param properties    properties to be emulated if the css to represent
     *                      them is not supported by the device
     * @param renderer      property renderer to wrap in an element only
     */
    private void addElementOnlyEmulationRenderer(String elementName,
                                                   StyleProperty[] properties,
                                                   StyleEmulationPropertyRenderer renderer) {

        // If the device doesn't support the emulating element, then we can't
        // emulate it (regardless of whether the css is supported).
        if (canEmulateElement(elementName)) {
            // Check if we need to emulate these style properties (i.e. if the
            // css is not supported by the device).
            boolean cssNotSupported = cssVersion == null;
            for (int i = 0; i < properties.length; i++) {
                StyleProperty property = properties[i];
                cssNotSupported = cssNotSupported ||
                        cssVersion.getProperty(property) == null;
                if (cssNotSupported) {
                    ElementOnlyStyleEmulationPropertyRenderer elementRenderer =
                            new ElementOnlyStyleEmulationPropertyRenderer(
                                    renderer);
                    styleEmulationPropertyRendererSelector.register(
                            property, elementRenderer);

                    // If we're going to be rendering this element, then need
                    // to support it when transforming...
                    fakePermittedChildren.add(elementName);
                }
            }
        }
    }

    /**
     * Return the ruleset used by the highlight transformer to handle mcs-effect-style:highlight
     * on XHTML2 elements.
     *
     * @return
     */
    public HashMap getHighlightTransformationRules() {
        return highlightRuleSet;
    }

    /**
     * Return the ruleset used by the corners transformer to handle mcs-border-radius
     * on XHTML2 elements.
     *
     * @return
     */
    public HashMap getCornersTransformationRules() {
        return cornersRuleSet;
    }
    
    
    /**
     * Set whether or not this protocol supports client framework installer.
     */
    public void setFrameworkClientInstallerSupported(boolean isSupported){
        this.supportsFrameworkClientInstalller = isSupported;
    }

    /**
     * Returns whether or not this protocol supports client framework installer.
     *
     * @return true if Framework Client is supported; false otherwise
     */
    public boolean isFrameworkClientInstallerSupported(){
        return this.supportsFrameworkClientInstalller;
    }
    
    /**
     * 
     */
    public int getDevicePixelsX(){
        return this.devicePixelsX;
    }
    
    /**
     * 
     * @param pixelsX
     */
    public void setDevicePixelsY(int pixelsX){
        this.devicePixelsX = pixelsX;
    }

    /**
     * Sets the status of file upload support.
     * 
     * @param canSupportFileUpload Set to true if file upload is supported.
     */
    protected void setCanSupportFileUpload(boolean canSupportFileUpload) {
        this.canSupportFileUpload = canSupportFileUpload;
    }

    // Javadoc inherited.
    public boolean isFileUploadSupported() {
        return this.canSupportFileUpload;
    }

    /**
     * Return the combination of:
     * <ul>
     * <li>the list of elements that are valid children of stylistic elements
     * (e.g. i, b) as returned by {@link #getPermittedChildren()} </li>
     * AND
     * <li>the set of "fake" permitted elements which has been constructed for
     * this device (see {@link #fakePermittedChildren}) which are not normally
     * supported by this protocol</li>
     * </ul>
     *
     * @return Set of all the elements that are valid children of stylistic
     * elements, according to this device's configuration
     */
    protected Set getAllPermittedChildren() {
        final String[] permittedChildren = getPermittedChildren();
        return mergePermittedChildren(permittedChildren);
    }

    /**
     * Convenience method which merges the array of elements that have been
     * defined as valid children of stylistic elements (see
     * {@link #getPermittedChildren()}) with the set of "fake" permitted
     * elements which has been constructed for this device (see {@link
     * #fakePermittedChildren}).
     *
     * @param permittedChildren     the array of elements that have been
     *                              defined as valid children of stylistic
     *                              elements
     * @return Set of all the elements that are valid children of stylistic
     * elements, according to this device's configuration
     */
    protected Set mergePermittedChildren(String[] permittedChildren) {

        HashSet permittedChildrenSet = null;
        if (permittedChildren != null && fakePermittedChildren != null) {

            final int realLength = permittedChildren.length;
            final int fakeLength = fakePermittedChildren.size();
            permittedChildrenSet = new HashSet(realLength + fakeLength);

            for (int i = 0; i < permittedChildren.length; i++) {
                if (!permittedChildrenSet.contains(permittedChildren[i])) {
                    permittedChildrenSet.add(permittedChildren[i]);
                }
            }
            // we need to include the emulation elements in this set of
            // permitted children, otherwise they would be removed rather than
            // pushed down. This is reasonable, because even though they are
            // not supported by the device, they will only be used if a) the
            // device explicitly says it supports them and b) explicitly says
            // it doesn't support the equivalent CSS.
            permittedChildrenSet.addAll(fakePermittedChildren);
        }
        return permittedChildrenSet;
    }

    // Javadoc inherited.
    public void createStyleEmulationElements() {

        styleEmulationElements =
                new DefaultStyleEmulationElementConfiguration();

        final Set permittedChildren = getAllPermittedChildren();

        if (fakePermittedChildren.contains("blink")) {
            styleEmulationElements.associateStylisticAndAntiElements(
                "blink", StyleEmulationVisitor.ANTI_BLINK_ELEMENT,
                    permittedChildren);
        }

        if (fakePermittedChildren.contains("marquee")) {
            styleEmulationElements.associateStylisticAndAntiElements(
                "marquee", null, permittedChildren);
            // The only one of the fake emulation elements that is an
            // indivisible element that permits stylistic elements as children
            // is marquee.
            styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                    new String[]{"marquee"});
        }

        if (fakePermittedChildren.contains("u")) {
            styleEmulationElements.associateStylisticAndAntiElements(
                "u", StyleEmulationVisitor.ANTI_UNDERLINE_ELEMENT,
                    permittedChildren);
        }

        if (fakePermittedChildren.contains("strike")) {
            styleEmulationElements.associateStylisticAndAntiElements(
                "strike", StyleEmulationVisitor.ANTI_STRIKE_ELEMENT,
                    permittedChildren);
        }

        // All other elements will be considered to be indivisible elements
        // that do no permit stylistic elements as children. This will have the
        // effect that they will be unchanged by the StyleEmulationTransformer
    }

    // Javadoc inherited.
    public String[] getPermittedChildren() {
        return new String[]{};
    }

    // Javadoc inherited
    public String getCSSMedia() {
        return cssMedia;
    }

    /**
     * Sets target media types for generated CSS.
     *
     * @param cssMedia The cssMedia to set.
     */
    public void setCSSMedia(String cssMedia) {
        this.cssMedia = cssMedia;
    }    
    
    /**
     * Registers maximum frame rate, reading from device policies.
     *
     * @param device The device to read policy from.
     */
    private void registerMaxClientFrameRate(InternalDevice device) {
        // Default value is infinity, which means no frame rate limit.
        maxClientFrameRate = Double.POSITIVE_INFINITY;

        // No device specified -> nothing to register.
        if (device == null) {
            return;
        }

        // Get string value from policy.
        String valueString = device.getPolicyValue(DevicePolicyConstants.CLIENT_FRAME_RATE_MAX);

        // Convert string to double.
        if (valueString != null) {
            try {
                maxClientFrameRate = Integer.valueOf(valueString).doubleValue();
            } catch (NumberFormatException e) {
                // Skip parse errors
            }
        }
    }

    // Javadoc inherited.
    public double getMaxClientFrameRate() {
        return maxClientFrameRate;
    }

    // Javadoc inherited.
    public boolean isInvalidFormLinkParent(String parentName) {
        return invalidFormLinkParents.containsKey(parentName);
    }

    // Javadoc inherited.
    public String getChildForInvalidFormLinkElement(String elementName) {
        return (String) invalidFormLinkParents.get(elementName);
    }

    public DTD getDTD() {
        return dtd;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 31-Oct-05	10048/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/13	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/8	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/4	ibush	VBM:2005081219 HR Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 29-Sep-05	9565/8	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/4	ibush	VBM:2005081219 HR Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 22-Nov-05	10381/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 24-Oct-05	9565/13	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/8	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/4	ibush	VBM:2005081219 HR Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 22-Sep-05	9540/3	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 21-Sep-05	9128/1	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 15-Sep-05	9472/3	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/3	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 02-Sep-05	9408/4	pabbott	VBM:2005083007 Move over to using JiBX accessor

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Jan-04	2659/2	allan	VBM:2003112801 RuleSection basics (read only)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/
