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
import com.volantis.mcs.protocols.capability.DeviceCapabilityConstants;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeAndOrElementStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.AttributeOnlyStyleEmulationPropertyRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.DefaultTextAlignEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationElementSetAttributeRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2ColorEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.html.css.emulator.renderer.HTML3_2ColorEmulationPropertyRenderer;
import com.volantis.mcs.themes.StylePropertyDetails;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class encapsulates protocol specific information and is based on the
 * Singleton design pattern.
 */
public class HTML_iModeConfiguration extends HTMLRootConfiguration {

    private static final DeviceElementCapability DEFAULT_MARQUEE_CAPABILITY;
    private static final DeviceElementCapability DEFAULT_BLINK_CAPABILITY =
            new DeviceElementCapability("blink", CapabilitySupportLevel.FULL);

    static{

        final Map MARQUEE_PROPERTIES = new HashMap();
        MARQUEE_PROPERTIES.put(DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT,
                CapabilitySupportLevel.FULL);
        MARQUEE_PROPERTIES.put(DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT,
                CapabilitySupportLevel.FULL);
        MARQUEE_PROPERTIES.put(DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT,
                CapabilitySupportLevel.FULL);
        MARQUEE_PROPERTIES.put(DeviceCapabilityConstants.MARQUEE_LOOP_ATT,
                CapabilitySupportLevel.FULL);
        DEFAULT_MARQUEE_CAPABILITY = new DeviceElementCapability("marquee",
                CapabilitySupportLevel.FULL, MARQUEE_PROPERTIES);
    }

    /**
     * Private constructor to comply to singleton design pattern.
     */
    public HTML_iModeConfiguration() {
        super(true);

        this.canSupportEvents = false;
    }

    // Javadoc inherited.
    public void initialize(InternalDevice device,
                           DeviceCapabilityManagerBuilder builder) {

        defaultElementCapabilities.add(DEFAULT_MARQUEE_CAPABILITY);
        defaultElementCapabilities.add(DEFAULT_BLINK_CAPABILITY);
        super.initialize(device, builder);
    }

    // Javadoc inherited.
    public void createStyleEmulationElements() {
        super.createStyleEmulationElements();

        final Set permittedChildren = getAllPermittedChildren();

        styleEmulationElements.associateStylisticAndAntiElements(
                "font", null, permittedChildren);

        // Initialize the elements that may contain stylistic elements.
        // The first group is the set of divisible other elements.
        // NOTE: We permit the null named element as a divisible element
        //       that may contain stylistic elements since we may encounter
        //       such elements as a by-product of other transformations.
        styleEmulationElements.addDivisibleElementsThatPermitStyles(
                new String[]{ null });

        // The set of divisible phrase elements (corresponding to a set
        // defined in the HDML iMode v2.0 (delta) dtd).
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{"p", "li", "pre", "dt", "dd", "center",
                             "blockquote", "body", "form"});

        // The set of indivisible heading elements (corresponding to a set
        // defined in the HDML iMode v2.0 (delta) dtd).
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{"h1", "h2", "h3", "h4", "h5", "h6"});

        // The set of indivisible other elements
        styleEmulationElements.addIndivisibleElementsThatPermitStyles(
                new String[]{"a", "blink", "marquee", "div"});

        // Initialize the mergeable elements.
        styleEmulationElements.addMergeableElement("font");
    }

    // Javadoc inherited.
    public String[] getPermittedChildren() {
        return new String[]{
            "plaintext", "a", "img", "font", "br", "input", "select",
            "textarea", "blink",
        };
    }

    // Javadoc inherited.
    protected void registerAttributeOnlyStyleEmulationPropertyRenderers(
            InternalDevice device) {
        super.registerAttributeOnlyStyleEmulationPropertyRenderers(device);
        // Register the style emulation property renderers in order of opening.
        // text-align
        AttributeOnlyStyleEmulationPropertyRenderer textAlignRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[]{"p", "div", "table", "tr", "td", "img",
                                     "hr"}, "align",
                        new DefaultTextAlignEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.TEXT_ALIGN, textAlignRenderer);

        // background-color
        AttributeOnlyStyleEmulationPropertyRenderer backgroundColorRenderer =
                new AttributeOnlyStyleEmulationPropertyRenderer(
                        new StyleEmulationElementSetAttributeRenderer(
                        new String[]{"table", "tr", "td"}, "bgcolor",
                        new HTML3_2ColorEmulationAttributeValueRenderer()));
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.BACKGROUND_COLOR,
                backgroundColorRenderer);
    }

    // Javadoc inherited.
    protected void registerAttributeAndOrElementStyleEmulationPropertyRenderers(
            InternalDevice device) {
        super.registerAttributeAndOrElementStyleEmulationPropertyRenderers(device);
        // color
        AttributeAndOrElementStyleEmulationPropertyRenderer colorRenderer =
                new AttributeAndOrElementStyleEmulationPropertyRenderer(
                        new HTML3_2ColorEmulationPropertyRenderer());
        styleEmulationPropertyRendererSelector.register(
                StylePropertyDetails.COLOR, colorRenderer);
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

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 18-May-05	8273/1	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	5877/8	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/6	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/4	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 27-Sep-04	5661/1	tom	VBM:2004091403 Added stylesheet support to iMode and fixed bgcol in cells

 24-Sep-04	5581/1	tom	VBM:2004091403 Introduced stylesheet emulation for font and repaired background

 ===========================================================================
*/
