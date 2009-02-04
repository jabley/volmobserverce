/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.styles;

import com.volantis.mcs.protocols.capability.CapabilitySupportLevel;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManager;
import com.volantis.mcs.protocols.capability.DeviceElementCapability;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

public class DeviceAwareValueHandlerToPropertyAdapter
        extends ValueHandlerToPropertyAdapter {

    /**
     * Does current protocol support CSS.
     */
    private boolean supportsCSS;

    /**
     * Current device's capabilities.
     */
    private final DeviceCapabilityManager capabilities;

    /**
     * Processed element name (needed to check device capabilities).
     */
    private final String elementName;

    /**
     * Processed element name (needed to check device capabilities).
     */
    private final String attributeName;

    /**
     * Create adapter using supplied arguments. 
     * @param supportsCSS - does underlying protocol supports CSS. This cannot 
     *                      be currently obtained from device capabilities
     *                      not protocol, nor protocol configuration (can probably
     *                      be from device repository)
     * @param capabilities - current device's capabilities
     * @param elementName - processed element name (needed for various capabilities lookup)
     * @param styleProperty - processed style property (needed for various capabilities lookup)
     * @param attributeName - attribute name to which property is to be eventually converted 
     * @param valueHandler - style property value handler
     * @param propertyUpdater - property updater used to update style properties
     *                          when conversion to attribute should occur
     */
    public DeviceAwareValueHandlerToPropertyAdapter(
            boolean supportsCSS,
            DeviceCapabilityManager capabilities,
            String elementName,
            StyleProperty styleProperty,
            String attributeName,
            ValueHandler valueHandler,
            PropertyUpdater propertyUpdater) {
        super(styleProperty, valueHandler, propertyUpdater);
        this.capabilities = capabilities;
        this.elementName = elementName;
        this.attributeName = attributeName;
    }

    // javadoc inherited
    public String getAsString(MutablePropertyValues propertyValues) {
        if (capabilities == null) {
            // Bahave as before. Note, usally this is not the case.
            // Alternatively we can update old tests to pass in the test device. 
            return super.getAsString(propertyValues);
        }
        StyleValue styleValue = propertyValues.getComputedValue(getStyleProperty());
        String string = getValueHandler().getAsString(styleValue);
        if (string != null) {
            StyleKeyword styleKeyword = StyleKeywords.getKeywordByName(string);
            if (styleKeyword != null) {
                // This is valid style keyword, check if device supports it
                // (turn on backword compatibility mode)
                if (supportsCSS && isKeywordSupported(styleKeyword, true)) {
                    // No property changes, no conversion
                    //    - style property supported by device
                    string = null;
                } else {
                    if (isAttributeSupported(attributeName)) {
                        // Shouldn't we explicitly clear properties instead of
                        // rely on external value updater?
                        getSignificantUpdater().update(
                                getStyleProperty(), propertyValues);
                    } else {
                        // No property changes, no conversion
                        //    - attribute not supported by device
                        string = null;
                    }
                }
            }
        }
        return string;
    }

    /**
     * Check if style keyword is supported by the device
     * @param styleKeyword style keyword
     * @param backwordCompatible compatibility with previous version
     * @return true if device supports specified style keyword
     */
    private boolean isKeywordSupported(StyleKeyword styleKeyword,
            boolean backwordCompatible) {

        boolean keywordSupported = false;

        DeviceElementCapability elementCapability =
            capabilities.getDeviceElementCapability(elementName);

        CapabilitySupportLevel propertySupportType =
            elementCapability.getSupportType(getStyleProperty());

        // For compatibility with previous versions we assume keyword
        // is supported only if compatibility level is FULL.
        if (propertySupportType != null) {
            if (backwordCompatible) {
                keywordSupported = 
                    (propertySupportType == CapabilitySupportLevel.FULL);
            } else {
                if (propertySupportType == CapabilitySupportLevel.FULL) {
                    keywordSupported = true;
                } else if (propertySupportType == CapabilitySupportLevel.NONE) {
                    keywordSupported = false;
                } else {
                    CapabilitySupportLevel keywordSupportType =
                    capabilities.getDeviceCSSCapability().getKeywordSupportType(
                            getStyleProperty(), styleKeyword);
                    keywordSupported = 
                        (keywordSupportType != CapabilitySupportLevel.NONE);
                }
            }
        }

        return keywordSupported;
    }

    /**
     * Check if attribute is supported by the device
     * @param attributeName attribute name
     * @return true if device supports specified attribute
     */
    private boolean isAttributeSupported(String attributeName) {
        boolean attributeSupported = true;

        DeviceElementCapability elementCapability =
            capabilities.getDeviceElementCapability(elementName);

        CapabilitySupportLevel attributeSupportType =
            elementCapability.getSupportType(attributeName);

        if (attributeSupportType != null) {
            if (attributeSupportType == CapabilitySupportLevel.FULL) {
                attributeSupported = true;
            } else if (attributeSupportType == CapabilitySupportLevel.NONE) {
                attributeSupported = false;
            } else {
                // Default processing for compatibility
                attributeSupported = true;
            }
        }

        return attributeSupported;
    }
}

