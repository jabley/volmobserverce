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
package com.volantis.mcs.runtime.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Holds configuration information about the overall devices configuration.
 * <p>
 * This corresponds to the mcs-config/devices element.
 */
public class DevicesConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The standard repository to use in the current instance of MCS.
     */
    private RepositoryDeviceConfiguration standardRepository;

    /**
     * Any custom repositories to be used in this instance of MCS.  This list
     * will contain instances of DeviceConfiguration which represent the
     * paths (either absolute or relative) to the given repository.
     */
    private List customRepositories = new ArrayList();

    /**
     * The configuration object for logging abstract and unknown devices and
     * sending e-mail notifications with the logged entries.
     */
    private UnknownDevicesLoggingConfiguration unknownDevicesLogging;

    /**
     * Name of device to use in case of a request from unknown device
     */
    private String defaultDeviceName;

    /**
     * Returns the standard device repository.
     */
    public RepositoryDeviceConfiguration getStandardDeviceRepository() {
        return standardRepository;
    }

    /**
     * Sets the default device repository.
     */
    public void setStandardDeviceRepository(RepositoryDeviceConfiguration 
            deviceRepository) {
        this.standardRepository = deviceRepository;
    }

    /**
     * Returns the custom repositories.
     */
    public Iterator getCustomDeviceRepositoriesListIterator() {
        return customRepositories.iterator();
    }

    /**
     * Adds a custom device repository.
     */
    public void addDeviceRepository(RepositoryDeviceConfiguration 
            deviceRepository) {
        customRepositories.add(deviceRepository);
    }

    /**
     * Returns the configuration object for abstract and unknown devices.
     * @return the stored configuration
     */
    public UnknownDevicesLoggingConfiguration getUnknownDevicesLogging() {
        return unknownDevicesLogging;
    }

    /**
     * Sets the configuration object for abstract or unknown devices.
     * @param unknownDevicesLogging the configuration to store
     */
    public void setUnknownDevicesLogging(
            final UnknownDevicesLoggingConfiguration unknownDevicesLogging) {
        this.unknownDevicesLogging = unknownDevicesLogging;
    }

    /**
     * Returns the configured name of default device to use in case of a request from unknown device
     * @return the name of default device
     */
    public String getDefaultDeviceName() {
        return defaultDeviceName;
    }

    /**
     * Sets the name of default device to use in case of a request from unknown device
     * @param defaultDeviceName the name of default device
     */
    public void setDefaultDeviceName(String defaultDeviceName) {
        this.defaultDeviceName = defaultDeviceName;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build                      

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 ===========================================================================
*/
