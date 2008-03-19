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

package com.volantis.mcs.protocols.hr;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManager;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.Iterator;
import java.util.Map;

/**
 * Factory class used to obtain an HR emulator
 */
public final class HorizontalRuleEmulatorFactory {

    // factory instance
    private static final HorizontalRuleEmulatorFactory instance =
            new HorizontalRuleEmulatorFactory();

    // prevent direct instantiation
    private HorizontalRuleEmulatorFactory() {
        // empty
    }

    /**
     * Get the singleton instance of this class
     *
     * @return An &lt;code>HorizontalRuleEmulatorFactory&lt;/code> instance
     */
    public static HorizontalRuleEmulatorFactory getInstance() {
        return instance;
    }

    /**
     * Get an instance of an &lt;code>HorizontalRuleEmulator&lt;/code>
     * If the internal device is null or the internal device shows a 
     * hr support value of full the method will return null
     * 
     * @param device
     *
     * @return An instance of &lt;code>HorizontalRuleEmulator&lt;/code> or
     *         &lt;code>null &lt;/code>
     */
    public HorizontalRuleEmulator getEmulator(InternalDevice device, 
                                      HorizontalRuleAttributes attributes, 
                                      boolean supportsCSS) {
        
        HorizontalRuleEmulator emulator = null;
        
        if (device != null) {                    
            ProtocolConfiguration config =
                    (ProtocolConfiguration) device.getProtocolConfiguration();
            if (config != null) {
                DeviceCapabilityManager deviceCapabilityManager =
                        config.getDeviceCapabilityManager();

                if (deviceCapabilityManager != null) {
                    emulator = determineEmulator(
                            attributes, deviceCapabilityManager, supportsCSS);
                }
            }
        }
        
        return emulator;
    }

    /**
     * Determine the emulator to use
     * @param attributes
     * @param deviceCapabilityManager
     * @return HorizontalRuleEmulator, null if no emulator is required
     */
    private HorizontalRuleEmulator determineEmulator(
            HorizontalRuleAttributes attributes,
            DeviceCapabilityManager deviceCapabilityManager,
            boolean supportsCSS) {

        HorizontalRuleEmulator emulator = null;

        DeviceElementCapability hrElementCapability =
                deviceCapabilityManager.getDeviceElementCapability("hr", true);

        if (attributes != null) {

            Map hrPropertiesAndSupportTypes = getPropertyAndSupportTypes(
                    attributes.getStyles(), hrElementCapability);

            CapabilitySupportLevel hrSupportLevel =
                    hrElementCapability.getElementSupportLevel();            

            if (hrPropertiesAndSupportTypes != null &&
                    emulationRequired(hrPropertiesAndSupportTypes,
                                      hrSupportLevel)) {

                if (hrSupportLevel == CapabilitySupportLevel.PARTIAL) {
                    emulator = investigateHREmulator(hrPropertiesAndSupportTypes);
                } else if (!supportsCSS) {
                // Does the device support the bgcolor attribute?
                emulator = new TableAttrHREmulator();
                }

                if (emulator == null) {
                    DeviceElementCapability divElementCapability =
                    deviceCapabilityManager.getDeviceElementCapability("div", true);

                    CapabilitySupportLevel divSupportLevel =
                            divElementCapability.getElementSupportLevel();
                    if (divSupportLevel == CapabilitySupportLevel.FULL) {
                        emulator = new HorizontalRuleEmulatorWithBorderStylingOnDIV(
                                HorizontalRuleEmulatorWithBorderStylingOnDIV.
                                    BORDER_BOTTOM_PROPERTY);
                    } else if (divSupportLevel == CapabilitySupportLevel.PARTIAL) {
                        emulator = investigateDivEmulators(attributes, divElementCapability);
                    }
                }
            }
        }
        return emulator;
    }

    /**
     * Investigate the use of a div element to emulate the hr element.
     * @param attributes - the attributes of the hr being emulated
     * @param divElementCapability - the capabilities of the div element
     * @return HorizontalRuleEmulator. This may be of type
     * HorizontalRuleEmulatorWithBorderStylingOnDIV or
     * HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs or null and is
     * dependant on the support given in the DeviceElementCapability
     * corresponding the the attributes required.
     */
    private HorizontalRuleEmulator investigateDivEmulators(
            HorizontalRuleAttributes attributes,
            DeviceElementCapability divElementCapability) {

        HorizontalRuleEmulator emulator = null;

        Styles styles = attributes.getStyles();

        boolean requiresColor = false;
        boolean requiresWidth = false;
        boolean requiresMarginTop = false;
        boolean requiresMarginBottom = false;

        if (styles != null && styles.getPropertyValues() != null) {
            MutablePropertyValues propertyValues = styles.getPropertyValues();
            requiresColor = propertyValues.
                    getComputedValue(StylePropertyDetails.COLOR) != null;
            requiresWidth = propertyValues.
                    getComputedValue(StylePropertyDetails.WIDTH) != null;
            requiresMarginTop = propertyValues.
                    getComputedValue(StylePropertyDetails.MARGIN_TOP) != null;
            requiresMarginBottom = propertyValues.
                    getComputedValue(StylePropertyDetails.MARGIN_BOTTOM) != null;
        } else { // no styles are required to be applied
            emulator = new HorizontalRuleEmulatorWithBorderStylingOnDIV(
                    HorizontalRuleEmulatorWithBorderStylingOnDIV.
                    BORDER_BOTTOM_PROPERTY
            );
        }

        if (!requiresColor || (requiresColor
                && divElementCapability.getSupportType(
                        StylePropertyDetails.BORDER_TOP_COLOR)
                        != CapabilitySupportLevel.NONE) &&
            !requiresWidth || (requiresWidth
                && divElementCapability.getSupportType(
                        StylePropertyDetails.BORDER_TOP_WIDTH)
                        != CapabilitySupportLevel.NONE)) {

            if (requiresMarginBottom || requiresMarginTop) {
                emulator = investigateMultipleDivEmulator(styles);
            }
            if (emulator == null) {
                emulator = new HorizontalRuleEmulatorWithBorderStylingOnDIV(
                        HorizontalRuleEmulatorWithBorderStylingOnDIV
                        .BORDER_TOP_PROPERTY);
            }
        } else if (!requiresColor || (requiresColor
                && divElementCapability.getSupportType(
                        StylePropertyDetails.BORDER_BOTTOM_COLOR)
                        != CapabilitySupportLevel.NONE) &&
            !requiresWidth || (requiresWidth
                && divElementCapability.getSupportType(
                        StylePropertyDetails.BORDER_BOTTOM_WIDTH)
                        != CapabilitySupportLevel.NONE)) {
            if (requiresMarginBottom || requiresMarginTop) {
                emulator = investigateMultipleDivEmulator(styles);
            }
            if (emulator == null) {
                emulator = new HorizontalRuleEmulatorWithBorderStylingOnDIV(
                        HorizontalRuleEmulatorWithBorderStylingOnDIV
                        .BORDER_BOTTOM_PROPERTY);
            }
        }


        return emulator;
    }

    /**
     * Investigate the use of multiple divs to emulate the hr.
     * This method assumes that emulation is required and support is avaliable
     * for the attributes other than margin top or bottom
     * @param styles
     * @return
     */
    private HorizontalRuleEmulator investigateMultipleDivEmulator(Styles styles) {
        HorizontalRuleEmulator emulator = null;

        MutablePropertyValues propertyValues = styles.getPropertyValues();

        StyleValue marginTopValue = propertyValues.getComputedValue(
                StylePropertyDetails.MARGIN_TOP);
        
        StyleValue marginBottomValue = propertyValues.getComputedValue(
                StylePropertyDetails.MARGIN_BOTTOM);

        // Legal values for margin-bottom and margin-top are either length or keyword 'auto'.
        // Acording to the CSS specs if 'margin-top' or 'margin-bottom' are 'auto', 
        // their used value is 0. 
        double marginTopLength = 0;
        if (StyleValueType.LENGTH.equals(marginTopValue.getStyleValueType())) {            
            marginTopLength = ((StyleLength)marginTopValue).getNumber();            
        }
        double marginBottomLength = 0;
        if (StyleValueType.LENGTH.equals(marginBottomValue.getStyleValueType())) {            
            marginBottomLength = ((StyleLength)marginBottomValue).getNumber();            
        }
        
        if (marginTopLength > 0 ||
                marginBottomLength > 0) {
            emulator =
                    new HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs();
        }

        return emulator;
    }

    /**
     * Investigate the emulation of the hr using styling attributes for the
     * emulation
     * @param propertiesAndSupportTypes
     * @return
     */
    private HorizontalRuleEmulator investigateHREmulator(
            Map propertiesAndSupportTypes) {

        HorizontalRuleEmulator emulator = null;

        boolean requiresColor = propertiesAndSupportTypes.
                get(StylePropertyDetails.COLOR) != null;
        boolean requiresHeight = propertiesAndSupportTypes.
                get(StylePropertyDetails.HEIGHT) != null;

        if (!requiresColor || (requiresColor
                && propertiesAndSupportTypes.get(
                        StylePropertyDetails.BORDER_TOP_COLOR)
                        != CapabilitySupportLevel.NONE) &&
            !requiresHeight || (requiresHeight
                && propertiesAndSupportTypes.get(
                        StylePropertyDetails.BORDER_TOP_WIDTH)
                        != CapabilitySupportLevel.NONE)) {
            emulator = new HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule();
        } else if (!requiresColor || (requiresColor
                && propertiesAndSupportTypes.get(
                        StylePropertyDetails.BORDER_BOTTOM_COLOR)
                        != CapabilitySupportLevel.NONE) &&
            !requiresHeight || (requiresHeight
                && propertiesAndSupportTypes.get(
                        StylePropertyDetails.BORDER_BOTTOM_WIDTH)
                        != CapabilitySupportLevel.NONE)) {
            emulator = new HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule();

        } else if (requiresColor && requiresHeight) {
            emulator = new HorizontalRuleEmulatorWithMultipleHRs();
        }

        return emulator;
    }

    /**
     * determine if any emulation is obviously required
     * @param propertiesAndSupportTypes
     * @param hrSupportLevel
     * @return
     */
    private boolean emulationRequired(Map propertiesAndSupportTypes,
                                      CapabilitySupportLevel hrSupportLevel) {
        boolean emulationRequired = false;

        //if the hr doesn't have any attributes or is fully supported then no
        //emulator is required. If the hr is patially supported the
        //capability support of each attribute needs to be tested to see if it
        //is supported.
        //todo - if an attribute is only partially supported the test needs
        //todo - to progress to a lower level to test for emulation being
        //todo - required
        if (!(hrSupportLevel == CapabilitySupportLevel.FULL) &&
                propertiesAndSupportTypes.size() > 0) {
            if (hrSupportLevel == CapabilitySupportLevel.PARTIAL) {
                for (Iterator iterator =
                        propertiesAndSupportTypes.values().iterator();
                     !emulationRequired && iterator.hasNext();) {
                    CapabilitySupportLevel capabilitySupportLevel =
                            (CapabilitySupportLevel) iterator.next();
                    if (capabilitySupportLevel == CapabilitySupportLevel.NONE) {
                        emulationRequired = true;
                    }
                }
            } else {
                emulationRequired = true;
            }
        }

        return emulationRequired;
    }

    /**
     * get the properties from the styles object mapped to the support type
     * provided for the property.
     * @param styles
     * @param dec
     * @return map of StylePropert -> CapabilitySupportLevel
     */
    private Map getPropertyAndSupportTypes(Styles styles,
                                           DeviceElementCapability dec) {
        Map propertiesAndSupportTypes = null;
        if (styles != null) {
            MutablePropertyValues propertyValues = styles.getPropertyValues();

            if (propertyValues != null) {
                propertiesAndSupportTypes = dec
                        .getSupportType(propertyValues);
            }
        }
        return propertiesAndSupportTypes;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10812/1	pduffin	VBM:2005121322 Committing changes ported forward from 3.5

 13-Dec-05	10808/1	pduffin	VBM:2005121322 Fixed horizontal rule emulation for SonyEricsson-P900

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Oct-05	9565/10	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/8	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/4	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 ===========================================================================
*/
