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
package com.volantis.mcs.protocols.capability;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Set;
import java.util.Iterator;

/**
 * This class creates DeviceCapabilityManagers for a given device.
 */
public class DeviceCapabilityManagerBuilder {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    DeviceCapabilityManagerBuilder.class);

    /**
     * The DeviceCapabilityManager which is being built;
     */
    private final DeviceCapabilityManager manager;

    /**
     * The device for which the capabilities will be mapped
     */
    private final InternalDevice device;

    /**
     * Initialize a new instance using the given parameters.
     *
     * @param device    which describes the device

     */
    public DeviceCapabilityManagerBuilder(InternalDevice device) {
        manager = new DeviceCapabilityManager();
        this.device = device;
    }

    /**
     * create the DeviceCapabilityManager
     * @return
     */
    public DeviceCapabilityManager build () {
        // @todo later this null check should be removed. However, it causes
        // @todo over 2000 tests to fail, and so will not be removed right now...
        if (device != null) {
            addAllElementCapabilities();
            addAllCSSCapabilities();
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The device capability manager cannot be " +
                    "initialised properly because the device is null");
        }
        return manager;
    }

    /**
     * Specify the default device element capabilities that should be set for
     * this protocol. They may be replaced with device specific values during
     * {@link #build} if specified in the device repository. However, they will
     * not be overridden by the default values defined in the parent (for the
     * same element).
     * <p/>
     * This is necessary to allow new device policies to be added to the
     * repository and still preserve the default behaviour (without requiring
     * the device team to manually enter a value for it for every device). It
     * should no longer be necessary when the device repository is modularised.
     *
     * @param elementCapabilities   array of default DeviceElementCapabilities
     */
    public void addDefaultValues(Set elementCapabilities) {
        for (Iterator i = elementCapabilities.iterator(); i.hasNext(); ) {
            addDefaultValue((DeviceElementCapability) i.next());
        }
    }

    /**
     * Add a default device element capability to the builder. If a default
     * value has already been specified, it will not be overwritten. However,
     * if a device specific value is found when building the DCM, it will
     * overwrite the default value.
     *
     * @param dec   default device element capability
     */
    public void addDefaultValue(DeviceElementCapability dec) {
        // Check if a default value has already been set
        final DeviceElementCapability existing =
                manager.getDeviceElementCapability(dec.getElementType(), false);

        // Don't overwrite it if this is the case.
        if (existing == null) {
            manager.addDeviceElementCapability(dec);
        }
    }

    /**
     * add all the element capabilities for the device to the
     *  DeviceCapabilityManager
     */
    private void addAllElementCapabilities() {
        addHrElementCapabilities();
        addDivElementCapabilities();
        addTrElementCapabilities();
        addTdElementCapabilities();
        addThElementCapabilities();
        addMarqueeElementCapabilities();
        addBlinkElementCapabilities();
        addStrikeElementCapabilities();
        addUnderlineElementCapabilities();
    }

    private void addAllCSSCapabilities() {
        addVerticalAlignKeywordsCapabilities();
    }

    /**
     * Add a DeviceElementCapability for the tr element
     */
    private void addTrElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("tr", true);

        dec.addSupportedStyleProperty(StylePropertyDetails.VERTICAL_ALIGN,
                getSupportLevel(DeviceCapabilityConstants.TR_VERTICAL_ALIGN));

        dec.addSupportedAttribute("valign",
                getSupportLevel(DeviceCapabilityConstants.TR_ATTRIBUTE_VALIGN));

        manager.addDeviceElementCapability(dec);
    }

    /**
     * Add a DeviceElementCapability for the td element
     */
    private void addTdElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("td", true);

        dec.setElementSupportLevel(CapabilitySupportLevel.FULL);

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_TOP,
                getSupportLevel(DeviceCapabilityConstants.TD_MARGIN_TOP));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_BOTTOM,
                getSupportLevel(DeviceCapabilityConstants.TD_MARGIN_BOTTOM));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_LEFT,
                getSupportLevel(DeviceCapabilityConstants.TD_MARGIN_LEFT));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_RIGHT,
                getSupportLevel(DeviceCapabilityConstants.TD_MARGIN_RIGHT));

        dec.addSupportedStyleProperty(StylePropertyDetails.PADDING_TOP,
                getSupportLevel(DeviceCapabilityConstants.TD_PADDING_TOP));

        dec.addSupportedStyleProperty(StylePropertyDetails.PADDING_BOTTOM,
                getSupportLevel(DeviceCapabilityConstants.TD_PADDING_BOTTOM));

        dec.addSupportedStyleProperty(StylePropertyDetails.PADDING_LEFT,
                getSupportLevel(DeviceCapabilityConstants.TD_PADDING_LEFT));

        dec.addSupportedStyleProperty(StylePropertyDetails.PADDING_RIGHT,
                getSupportLevel(DeviceCapabilityConstants.TD_PADDING_RIGHT));

        dec.addSupportedStyleProperty(StylePropertyDetails.VERTICAL_ALIGN,
                getSupportLevel(DeviceCapabilityConstants.TD_VERTICAL_ALIGN));

        dec.addSupportedAttribute("valign",
                getSupportLevel(DeviceCapabilityConstants.TD_ATTRIBUTE_VALIGN));

        manager.addDeviceElementCapability(dec);
    }


    /**
     * Add a DeviceElementCapability for the th element
     */
    private void addThElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("th", true);

        dec.setElementSupportLevel(CapabilitySupportLevel.FULL);

        dec.addSupportedStyleProperty(StylePropertyDetails.VERTICAL_ALIGN,
                getSupportLevel(DeviceCapabilityConstants.TH_VERTICAL_ALIGN));

        dec.addSupportedAttribute("valign",
                getSupportLevel(DeviceCapabilityConstants.TH_ATTRIBUTE_VALIGN));

        manager.addDeviceElementCapability(dec);
    }

    /**
     * Add a DeviceElementCapability for the marquee element
     */
    private void addMarqueeElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("marquee", true);
        dec.setElementSupportLevel(
                getSupportLevel(DeviceCapabilityConstants.MARQUEE_SUPPORTED));

        dec.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_DIRECTION_ATT,
                getSupportLevel(DeviceCapabilityConstants.MARQUEE_DIRECTION));

        dec.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_LOOP_ATT,
                getSupportLevel(DeviceCapabilityConstants.MARQUEE_LOOP));

        dec.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_BGCOLOR_ATT,
                getSupportLevel(DeviceCapabilityConstants.MARQUEE_BGCOLOR));

        dec.addSupportedAttribute(
                DeviceCapabilityConstants.MARQUEE_BEHAVIOR_ATT,
                getSupportLevel(DeviceCapabilityConstants.MARQUEE_BEHAVIOR));

        manager.addDeviceElementCapability(dec);
    }

    /**
     * Add a DeviceElementCapability for the blink element
     */
    private void addBlinkElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("blink", true);
        dec.setElementSupportLevel(
                getSupportLevel(DeviceCapabilityConstants.BLINK_SUPPORTED));
        manager.addDeviceElementCapability(dec);
    }

    /**
     * Add a DeviceElementCapability for the u element
     */
    private void addUnderlineElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("u", true);
        dec.setElementSupportLevel(
                getSupportLevel(DeviceCapabilityConstants.U_SUPPORTED));
        manager.addDeviceElementCapability(dec);
    }

    /**
     * Add a DeviceElementCapability for the strike element
     */
    private void addStrikeElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("strike", true);
        dec.setElementSupportLevel(
                getSupportLevel(DeviceCapabilityConstants.STRIKE_SUPPORTED));
        manager.addDeviceElementCapability(dec);
    }

    /**
     * Add a DeviceElementCapability for the hr element
     */
    private void addHrElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("hr", true);

        dec.setElementSupportLevel(
                getSupportLevel(DeviceCapabilityConstants.HR_SUPPORTED));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_TOP_COLOR,
                getSupportLevel(DeviceCapabilityConstants.HR_BORDER_TOP_COLOR));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_TOP_STYLE,
                getSupportLevel(DeviceCapabilityConstants.HR_BORDER_TOP_STYLE));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_TOP_WIDTH,
                getSupportLevel(DeviceCapabilityConstants.HR_BORDER_TOP_WIDTH));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_BOTTOM_COLOR,
                getSupportLevel(
                        DeviceCapabilityConstants.HR_BORDER_BOTTOM_COLOR));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_BOTTOM_STYLE,
                getSupportLevel(
                        DeviceCapabilityConstants.HR_BORDER_BOTTOM_STYLE));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                getSupportLevel(
                        DeviceCapabilityConstants.HR_BORDER_BOTTOM_WIDTH));

        dec.addSupportedStyleProperty(StylePropertyDetails.COLOR,
                getSupportLevel(DeviceCapabilityConstants.HR_COLOR));

        dec.addSupportedStyleProperty(StylePropertyDetails.WIDTH,
                getSupportLevel(DeviceCapabilityConstants.HR_WIDTH));

        dec.addSupportedStyleProperty(StylePropertyDetails.HEIGHT,
                    getSupportLevel(DeviceCapabilityConstants.HR_HEIGHT));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_TOP,
                getSupportLevel(DeviceCapabilityConstants.HR_MARGIN_TOP));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_BOTTOM,
                getSupportLevel(DeviceCapabilityConstants.HR_MARGIN_BOTTOM));

        manager.addDeviceElementCapability(dec);

    }

    /**
     * Add a DeviceElementCapability for the hr element
     */
    private void addDivElementCapabilities() {

        DeviceElementCapability dec =
                manager.getDeviceElementCapability("div", true);
        dec.setElementSupportLevel(getSupportLevel(
                DeviceCapabilityConstants.DIV_SUPPORTED));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_TOP,
                getSupportLevel(DeviceCapabilityConstants.DIV_MARGIN_TOP));

        dec.addSupportedStyleProperty(StylePropertyDetails.MARGIN_BOTTOM,
                getSupportLevel(DeviceCapabilityConstants.DIV_MARGIN_BOTTOM));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_TOP_COLOR,
                getSupportLevel(DeviceCapabilityConstants.DIV_BORDER_TOP_COLOR));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_TOP_STYLE,
                getSupportLevel(DeviceCapabilityConstants.DIV_BORDER_TOP_STYLE));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_TOP_WIDTH,
                getSupportLevel(DeviceCapabilityConstants.DIV_BORDER_TOP_WIDTH));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_BOTTOM_COLOR,
                getSupportLevel(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_COLOR));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_BOTTOM_STYLE,
                getSupportLevel(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_STYLE));

        dec.addSupportedStyleProperty(StylePropertyDetails.BORDER_BOTTOM_WIDTH,
                getSupportLevel(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_WIDTH));

        manager.addDeviceElementCapability(dec);
    }

    /**
     * Adds vertical-align keywords support levels
     */
    private void addVerticalAlignKeywordsCapabilities() {

        DeviceCSSCapability cssCapability = manager.getDeviceCSSCapability();

        cssCapability.addSupportedStyleKeyword(
                StylePropertyDetails.VERTICAL_ALIGN,
                VerticalAlignKeywords.BOTTOM,
                getSupportLevel(DeviceCapabilityConstants.VERTICAL_ALIGN_BOTTOM));

        cssCapability.addSupportedStyleKeyword(
                StylePropertyDetails.VERTICAL_ALIGN,
                VerticalAlignKeywords.SUB,
                getSupportLevel(DeviceCapabilityConstants.VERTICAL_ALIGN_SUB));

        cssCapability.addSupportedStyleKeyword(
                StylePropertyDetails.VERTICAL_ALIGN,
                VerticalAlignKeywords.SUPER,
                getSupportLevel(DeviceCapabilityConstants.VERTICAL_ALIGN_SUPER));

        cssCapability.addSupportedStyleKeyword(
                StylePropertyDetails.VERTICAL_ALIGN,
                VerticalAlignKeywords.TOP,
                getSupportLevel(DeviceCapabilityConstants.VERTICAL_ALIGN_TOP));
    }

    /**
     * Utility method used to avoid code duplication.

     * @param policyName    name of the policy to be retrieved from the device
     *                      repository
     * @return CapabilitySupportLevel for specified policy 
     */
    protected CapabilitySupportLevel getSupportLevel(String policyName) {
        return CapabilitySupportLevel.getSupportLevel(
                device.getPolicyValue(policyName));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
