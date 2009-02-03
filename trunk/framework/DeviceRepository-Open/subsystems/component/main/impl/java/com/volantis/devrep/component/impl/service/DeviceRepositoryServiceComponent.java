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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.component.impl.service;

import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.api.devices.SimpleDeviceRepositoryFactory;
import com.volantis.mcs.devices.Device;
import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentException;

import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * This class is Component Implementation class.
 * It uses the existing factory to create a DeviceRepository object to which the OSGi service delegate
 */
public class DeviceRepositoryServiceComponent implements DeviceRepository {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(DeviceRepositoryServiceComponent.class);

    /**
     * Repository for accessing devices database.
     */
    private DeviceRepository repository = null;

    // invoked by reflection
    protected void activate(ComponentContext context) {

        String repositoryUrl = (String) context.getProperties()
                .get(PropertiesConstants.DEVICE_REPOSITORY_URL);

        // create factory
        SimpleDeviceRepositoryFactory factory = new SimpleDeviceRepositoryFactory();
        try {
            repository = factory.createDeviceRepository(repositoryUrl);
        } catch (DeviceRepositoryException e) {
            throw new ComponentException(exceptionLocalizer.format("repository-not-accessible"));
        }

        if (repository == null) {
            throw new ComponentException(exceptionLocalizer.format("repository-not-accessible"));
        }
    }

    // invoked by reflection
    protected void deactivate(ComponentContext context) {
        repository = null;
    }

    // java inherited
    public Device getDevice(String deviceName) throws DeviceRepositoryException {
        return repository.getDevice(deviceName);
    }

    // java inherited
    public Device getDevice(HttpHeaders headers) throws DeviceRepositoryException {
        return repository.getDevice(headers);
    }

    // java inherited
    public Device getDevice(HttpHeaders headers, String defaultDeviceName)
            throws DeviceRepositoryException {
        return repository.getDevice(headers, defaultDeviceName);
    }

    // java inherited
    public List getDevices(String deviceNamePattern) throws DeviceRepositoryException {
        return repository.getDevices(deviceNamePattern);
    }

    // java inherited
    public String getDeviceNameByTAC(String tac) throws DeviceRepositoryException {
        return repository.getDeviceNameByTAC(tac);
    }

    // java inherited
    public String getDeviceNameByTACFAC(String tacfac) throws DeviceRepositoryException {
        return repository.getDeviceNameByTACFAC(tacfac);
    }

    // java inherited
    public String getDeviceNameByIMEI(String imei) throws DeviceRepositoryException {
        return repository.getDeviceNameByIMEI(imei);
    }

    // java inherited
    public String getDeviceNameByUAProfURL(URL uaprofUrl) throws DeviceRepositoryException {
        return repository.getDeviceNameByUAProfURL(uaprofUrl);
    }

    // java inherited
    public List getDevicePolicyNames() throws DeviceRepositoryException {
        return repository.getDevicePolicyNames();
    }

    // java inherited
    public List getPolicyCategoryNames() throws DeviceRepositoryException {
        return repository.getPolicyCategoryNames();
    }

    // java inherited
    public List getDevicePolicyNamesByCategory(String category) throws DeviceRepositoryException {
        return repository.getDevicePolicyNamesByCategory(category);
    }

    // java inherited
    public PolicyDescriptor getPolicyDescriptor(String policyName, Locale locale)
            throws DeviceRepositoryException {
        return repository.getPolicyDescriptor(policyName, locale);
    }

    // java inherited
    public CategoryDescriptor getCategoryDescriptor(String getCategoryDescriptor, Locale locale)
            throws DeviceRepositoryException {
        return repository.getCategoryDescriptor(getCategoryDescriptor, locale);
    }

    // java inherited
    public String getFallbackDeviceName(String deviceName) throws DeviceRepositoryException {
        return repository.getFallbackDeviceName(deviceName);
    }

    // java inherited
    public List getChildrenDeviceNames(String deviceName) throws DeviceRepositoryException {
        return repository.getChildrenDeviceNames(deviceName);
    }
}
