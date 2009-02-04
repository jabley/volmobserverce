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
package com.volantis.mcs.protocols.builder;

import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.mcs.css.version.ManualCSS2VersionFactory;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DefaultProtocolSupportFactory;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.extractor.ExtractorConfigurator;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * A builder which creates configured protocols.
 * <p>
 * The protocol created should be ready for use.
 * <p>
 * This is suitable for use by protocol integration test cases and as the
 * basis for {@link NamedProtocolBuilder} which normal runtime code uses. 
 */
public final class ProtocolBuilder {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ProtocolBuilder.class);

    // todo: this should be passed into the constructor to enable testing.
    private final ProtocolSupportFactory supportFactory =
            new DefaultProtocolSupportFactory();

    /**
     * Initialise.
     */
    public ProtocolBuilder() {
    }

    /**
     * Build a configured, ready for use protocol using the protocol factory
     * and device supplied.
     *
     * @param protocolFactory used to create the protocol and it's
     *      configuration.
     * @param device used to initialise the protocol configuration with device
     *      specific data.
     * @return the configured, ready to use protocol.
     */
    public VolantisProtocol build(ProtocolFactory protocolFactory,
            final InternalDevice device) {

        if (protocolFactory == null) {
            throw new IllegalArgumentException("protocolFactory cannot be null");
        }
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }

        return createProtocol(protocolFactory, device);
         
    }

    /**
     * Create a protocol that uses the configuration.
     * @param protocolFactory
     * @param device
     * @return protocol
     */
    private VolantisProtocol createProtocol(ProtocolFactory protocolFactory,
                                            final InternalDevice device) {

        // Get/Create the configuration.
        ProtocolConfiguration configuration =
                getProtocolConfiguration(protocolFactory, device);

        VolantisProtocol protocol = protocolFactory.createProtocol(
                supportFactory, configuration);

        if (protocol == null) {
            throw new IllegalStateException();
        }

        return protocol;
    }

    /**
     * Get the protocol configuration for a device. If the device is null a new
     * configuration will be created, otherwise the device will be checked
     * for an existing configuration before a new one is created.
     *
     * @param protocolFactory
     * @param device
     * @return configuration
     */
    private ProtocolConfiguration getProtocolConfiguration(
            ProtocolFactory protocolFactory, InternalDevice device) {

        ProtocolConfiguration configuration;

        synchronized(device) {
            configuration = (ProtocolConfiguration)
                    device.getProtocolConfiguration();

            if (configuration == null) {
                configuration = protocolFactory.createConfiguration();
                initialiseDeviceProtocolConfiguration(device, configuration);
                device.setProtocolConfiguration(configuration);
            }
        }

        return configuration;
    }

    /**
     * Initialise the protocol configuration with device specific information.
     *
     * @param device the device to configure the protocol for. Note that this
     *      may be null when called from a testcase (at least for now).
     * @param configuration the protocol configuration.
     */
    private void initialiseDeviceProtocolConfiguration(
            InternalDevice device, ProtocolConfiguration configuration) {

        ProtocolConfigurationImpl config = (ProtocolConfigurationImpl)
                configuration;

        boolean supportsJavascript = device.getBooleanPolicyValue(
                DevicePolicyConstants.SUPPORTS_JAVASCRIPT);
        config.setCanSupportJavaScript(supportsJavascript);

        try {
            int devicePixelsX;
            devicePixelsX = device.getIntegerPolicyValue(
                DevicePolicyConstants.USABLE_WIDTH_IN_PIXELS);
            config.setDevicePixelsX(devicePixelsX);
        } catch(NumberFormatException e){
            // default value will be used.
        }

        // Version of CFW since the device is supported
        String since = device.getPolicyValue(
                DevicePolicyConstants.SUPPORTS_VFC_SINCE);

        if (since == null) {
            since = DevicePolicyConstants.SUPPORTS_VFC_SINCE_DEFAULT;
        }
        config.setFrameworkClientSupportedSince(since);

        // Whether the device has viewport support
        String viewportVirtualSupport = device.getPolicyValue(
                DevicePolicyConstants.X_BROWSER_VIEWPORT_VIRTUAL_SUPPORT);
        config.setViewportVirtualSupport(viewportVirtualSupport);

        // Framework Client support configuration
        config.setFrameworkClientSupported(configurePolicyValue(
                device,
                DevicePolicyConstants.SUPPORTS_VFC,
                DevicePolicyConstants.SUPPORTS_VFC_DEFAULT,
                config.getFrameworkClientSupported()));

        // todo: later: configurators should be registered externally (for testing)
        // todo: later: create a ProtocolBuilderFactory to pass in dependencies.
        CSSConfigurator cssConfigurator = new CSSConfigurator();
        cssConfigurator.initialise(config, device);

        // ???else, test cases with null device assume no javascript support???
        // else, the device is null. Currently this may happen for
        // integration test cases but in future we should avoid this.
        // todo: integration test cases to provide non-null device here.

        // Complete the configuration initialisation now that the css version
        // has been initialised (do this before a default version is supplied
        // if it's null). This must be done even if the device is null to
        // ensure that everything is initialised (even if empty).
        DeviceCapabilityManagerBuilder builder =
                new DeviceCapabilityManagerBuilder(device);
        config.initialize(device, builder);

        // If we did not try and set the CSS or we did but no CSS was found,
        // then we will have a null CSS version. This should only ever happen
        // in testing, but means that all the subsequent code must check for
        // null, which is very tedious. Instead we sub in a testing CSS version
        // which prevents us having to do this.
        if (config.getCssVersion() == null) {

            // assume the most common case for test cases for now.
            DefaultCSSVersion cssVersion =
                    new ManualCSS2VersionFactory().createCSSVersion();
            cssVersion.markImmutable();
            config.setCssVersion(cssVersion);

            if (logger.isDebugEnabled()) {
                logger.debug("Using testing base CSS Version: " +
                        configuration.getCssVersion());
            }
        }

        ExtractorConfigurator configurator = new ExtractorConfigurator();
        ExtractorConfiguration extractor =
                configurator.createConfiguration(device,
                        configuration.getCssVersion());
        config.setExtractorConfiguration(extractor);

        // CSS media
        String media = device.getPolicyValue(DevicePolicyConstants.CSS_MEDIA_SUPPORTED);

        // If media is specified (not null and not empty),
        // set the value on protocol.
        if ((media != null) && !media.equals("")) {
            config.setCSSMedia(media);
        }
    }
       
    /**
     * Configure policy value reading data from DevicePolicy
     */
    private boolean configurePolicyValue(
            InternalDevice device, 
            String policyName,
            String useDefault,
            boolean defaultValue) {
        
        boolean result = defaultValue;
        
        // Handle device override of the default value
        final String deviceValue = device.getPolicyValue(policyName);

        if (deviceValue != null && !deviceValue.equalsIgnoreCase(useDefault)) {
            // The device policy value is different to the default client framework
            // support for the protocol, so override the protocol accordingly
            result = Boolean.valueOf(deviceValue).booleanValue();

            if (logger.isDebugEnabled()) {
                logger.debug("Feature " + policyName +
                        " explicitly set to " + result + " by the device");
            }
        }        
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 31-Oct-05	10048/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/10	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/5	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 24-Oct-05	9565/10	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/5	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 14-Oct-05	9825/2	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 22-Sep-05	9540/3	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 20-Sep-05	9472/11	ibush	VBM:2005090808 Add default styling for sub/sup elements

 19-Sep-05	9472/9	ibush	VBM:2005090808 Add default styling for sub/sup elements

 15-Sep-05	9472/7	ibush	VBM:2005090808 Add default styling for sub/sup elements

 15-Sep-05	9472/5	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 ===========================================================================
*/
