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
package com.volantis.mcs.eclipse.ab.editors.devices.validation;

import com.volantis.mcs.eclipse.ab.editors.devices.DevicesMessages;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

import java.text.MessageFormat;

import org.jdom.Element;
import org.jdom.Namespace;

/**
 * A LocationDetails that provides location details for a policy element
 * that is an descendant of a device document.
 */
public class DevicePolicyLocationDetails implements LocationDetails {
    /**
     * The message to use for the location details.
     */
    private static final String MESSAGE =
            DevicesMessages.getString("DevicePolicyLocationDetails." +
                    "devicePolicyLocation");

    /**
     * The MessageFormat for formatting the location details.
     */
    private static final MessageFormat FORMAT = new MessageFormat(MESSAGE);

    /**
     * The DeviceRepositoryAccessorManager used to get the localized names
     * for policies used in messages.
     */
    private final DeviceRepositoryAccessorManager dram;

    /**
     * Construct a new DevicePolicyLocationDetails.
     *
     * @param dram the DeviceRepositoryAccessorManager
     */
    public DevicePolicyLocationDetails(DeviceRepositoryAccessorManager dram) {
        this.dram = dram;
    }

    /**
     * Provide the location details for the device
     * policy element that is specifed by the given invalidElement
     *
     * @param rootElement    the root element of the document containing the
     *                       invalid element
     * @param invalidElement the Element that is invalid
     * @return the location details for the category policy specified by
     *         the given invalidElement
     * @throws com.volantis.mcs.xml.xpath.XPathException
     *          if a category policy is not derivable from the invalidElement
     */
    public String getLocationDetailsString(Element rootElement,
                                           Element invalidElement)
            throws XPathException {

        if (!rootElement.getName().equals(DeviceRepositorySchemaConstants.
                DEVICE_ELEMENT_NAME)) {
            throw new IllegalArgumentException("Expected a rootElement named " +
                    DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME +
                    " but got a rootElement named " + rootElement.getName());
        }

        StringBuffer xpathString = new StringBuffer();

        xpathString.append("ancestor-or-self::").
                append(MCSNamespace.DEVICE.getPrefix()).
                append(":").
                append(DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME);

        XPath policyXPath = new XPath(xpathString.toString(), new Namespace[]{
                MCSNamespace.DEVICE});
        Element policy = policyXPath.selectSingleElement(invalidElement);

        if (policy == null) {
            throw new XPathException(
                    "Selected element was null when selected based on element: " +
                            invalidElement.toString());
        }

        String policyName =
                policy.getAttributeValue(DeviceRepositorySchemaConstants.
                        POLICY_NAME_ATTRIBUTE);

        String categoryName = dram.getPolicyCategory(policyName);

        String localizedName = dram.getLocalizedPolicyName(policyName);
        if (localizedName != null && localizedName.length() > 0) {
            policyName = localizedName;
        }

        localizedName = dram.getLocalizedPolicyCategory(categoryName);
        if (localizedName != null && localizedName.length() > 0) {
            categoryName = localizedName;
        }

        // The rootElement should be a device element
        String deviceName = rootElement.
                getAttributeValue(DeviceRepositorySchemaConstants.
                        DEVICE_NAME_ATTRIBUTE);

        return FORMAT.format(new Object[]{deviceName,
                categoryName,
                policyName});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 10-Sep-04	5488/2	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 ===========================================================================
*/
