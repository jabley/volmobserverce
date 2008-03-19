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
package com.volantis.mcs.devices;

import com.volantis.mcs.devices.policy.PolicyDescriptor;
import com.volantis.mcs.devices.category.CategoryDescriptor;
import com.volantis.mcs.http.HttpHeaders;

import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * This interface provides access to the Device Repository.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface DeviceRepository {

    /**
     * This method returns a single device from the underlying device
     * repository matching the name.
     *
     * @param deviceName
     *            The name of the {@link Device}to return
     * @return The {@link Device}or null if not found.
     * @throws DeviceRepositoryException
     *             if an underlying error occurs whilst reading the repository.
     */
    public Device getDevice(String deviceName) throws DeviceRepositoryException;

    /**
     * This method returns the name of the device identified by the contents of
     * the supplied HttpHeaders.
     *
     * @param headers
     *            The HttpHeaders that will be used to identify the device.
     * @return The name of the device or null if not found.
     * @throws DeviceRepositoryException
     *             if an underlying error occurs whilst reading the repository.
     */
    public Device getDevice(HttpHeaders headers)
            throws DeviceRepositoryException;

    /**
     * This method returns the name of the device identified by the contents of
     * the supplied HttpHeaders, defaulting to the supplied device
     *
     * @param headers
     *            The HttpHeaders that will be used to identify the device.
     * @param defaultDeviceName
     *            Name of device to be used if no known device was recognized from headers  
     * @return The name of the device or default device
     * @throws DeviceRepositoryException
     *             if an underlying error occurs whilst reading the repository.
     */
    public Device getDevice(HttpHeaders headers, String defaultDeviceName)
            throws DeviceRepositoryException;


    /**
     * This method returns a selection of device names from the underlying
     * device repository matching the specified pattern. The pattern may
     * contain a single "*" wildcard character at the beginning or end of the
     * name e.g. Nokia*
     *
     * @param deviceNamePattern
     *            The pattern used to identify the Devices to return
     * @return A list of device names 
     * @throws DeviceRepositoryException
     *             if an underlying error occurs whilst reading the repository.
     */
    public List getDevices(String deviceNamePattern)
            throws DeviceRepositoryException;

    /**
     * This method returns the name of the device based on a TAC number.
     * The TAC number can be either an 8 digit Type Allocation Code, used since
     * April 2004, or a 6 digit Type Approval Code, used prior to this date.
     * Both representing the first 8 or 6 digits of the device's IMEI
     * (International Mobile Equipment Identity) code.
     *
     * @param tac This is the TAC for the handset who's device
     *            name we are trying to retrieve. It must only contain digits.
     * @return The name of the {@link Device} which matches this TAC or Null if
     *         not found.
     * @throws DeviceRepositoryException if an underlying error occurs whilst
     *                                   reading the repository.
     */
    public String getDeviceNameByTAC(String tac) throws DeviceRepositoryException;

    /**
     * This method returns the name of the device based on a 8 digit number
     * consisting of a 6 digit Type Approval Code (TAC) followed by a 2 digit
     * Final Assembly Code (FAC) code.
     * The 8 digit number represents the first part of the device's IMEI
     * (International Mobile Equipment Identity) code.
     *
     * @param tacfac This is the TAC and FAC for the handset we are trying to
                     retrieve. It must only contain digits.
     * @return The name of the {@link Device} which matches this TAC or Null if
     *         not found.
     * @throws DeviceRepositoryException if an underlying error occurs whilst
     *                                   reading the repository.
     */
    public String getDeviceNameByTACFAC(String tacfac)
            throws DeviceRepositoryException;

    /**
     * This method returns the name of the device based on an IMEI number.
     *
     * <p>This is a convenience method which will first convert the IMEI number
     * into a TAC and call getDeviceNameByTAC.</p>
     *
     * @param imei This is the International Mobile Equipment Identity
     * @return The name of the {@link Device} which matches this TAC or Null if
     *         not found.
     * @throws DeviceRepositoryException if an underlying error occurs whilst
     *                                   reading the repository.
     */
    public String getDeviceNameByIMEI(String imei)
            throws DeviceRepositoryException;

    /**
     * This method returns the name of the device based on a regular expression
     * match of the UAProf URL with the primary device pattern name.
     *
     * <p>To match the UAProfiles using the getDeviceNameByUAProfURL() method,
     * the entries need to be added to the Primary Patterns section in the
     * device repository manager.</p>
     *
     * <p>They should adhere to the following format:</p>
     *
     * <p>Profile: http://gsm\.lge\.com/html/gsm/LG-G7200.*</p>
     *
     * @param uaprofUrl The url of the device's UAProf profile
     * @return The name of the {@link Device} which matches the UAProf URL or
     *         null if not found.
     * @throws DeviceRepositoryException if an underlying error occurs while
     *                                   reading the repository.
     */
    public String getDeviceNameByUAProfURL(URL uaprofUrl)
            throws DeviceRepositoryException;

    /**
     * This method returns a list of all available device policy names that may
     * be retrieved from a device.
     *
     * @return A List of Policy names.
     */
    public List getDevicePolicyNames() throws DeviceRepositoryException;

    /**
     * This method returns a list of all the policy category names stored in the
     * device repository.
     *
     * @return A list of category names
     */
    public List getPolicyCategoryNames() throws DeviceRepositoryException;

    /**
     * This method returns a list of all available device policy names for a
     * particular category. This method will return null if the category does
     * not exist.
     *
     * @return A List of Policy names.
     * @throws DeviceRepositoryException
     *             if an underlying error occurs whilst reading the repository.
     */
    public List getDevicePolicyNamesByCategory(String category)
            throws DeviceRepositoryException;

    /**
     * This method returns the {@link PolicyDescriptor}for the given Device
     * Policy.
     *
     * @param policyName
     *            the {@link PolicyDescriptor} for the given Device Policy.
     * @return the policy descriptor, or null if it was not found.
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     */
    public PolicyDescriptor getPolicyDescriptor(String policyName, Locale locale)
    		throws DeviceRepositoryException;

    /**
     * This method returns the {@link CategoryDescriptor}for the given Device
     * Category.
     *
     * @param categoryName
     *            the {@link PolicyDescriptor} for the given Device Policy.
     * @return the category descriptor, or null if it was not found.
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     */
    public CategoryDescriptor getCategoryDescriptor(
            String categoryName, Locale locale)
            throws DeviceRepositoryException;

    /**
     * This method returns the name of the device the specified device falls
     * back to. May return null.
     *
     * @param deviceName
     *            the name of the device
     * @throws DeviceRepositoryException
     *             if there is a failure in accessing the underlying repository.
     * @throws IllegalArgumentException
     *             if the specified device name is invalid (not points to an
     *          existing device)
     */
    public String getFallbackDeviceName(String deviceName)
        throws DeviceRepositoryException;

    /**
     * Return a list of the names of the specified devices children.
     * <p>
     * The returned list contains String objects which are the names of the
     * devices which fallback to the specified device.
     * </p>
     * <p>
     * If the deviceName is null then the root device plus any orphaned devices
     * are returned.
     * </p>
     * @param deviceName The name of the device.
     * @return A list of device names.
     */
    public List getChildrenDeviceNames(String deviceName)
      throws DeviceRepositoryException;
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 26-Oct-05	9995/1	pabbott	VBM:2005101306 Update DeviceRepository javadoc

 26-Sep-05	9593/1	adrianj	VBM:2005092209 Hide experimental device policies from customer code

 10-Jan-05	6632/1	adrianj	VBM:2005010507 Removed references to Type Approval Codes

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Oct-04	6041/2	tom	VBM:2004102807 Changed public api documentation for getDeviceNameByUAProfURL()

 01-Oct-04	5714/1	matthew	VBM:2004093005 Change javadoc to reflect changes in functionality

 01-Oct-04	5712/1	matthew	VBM:2004093005 javadoc change

 06-Aug-04	5121/1	adrianj	VBM:2004080203 Implementation of public API methods for lookup by TAC

 27-Jul-04	4937/4	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - rework issues

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 23-Jul-04	4959/1	philws	VBM:2004072307 Add TAC, TAC+FAC, IMEI and UAProf URL to device name resolution API

 21-Jul-04	4930/1	geoff	VBM:2004072104 Public API for Device Repository: Basics

 ===========================================================================
*/
