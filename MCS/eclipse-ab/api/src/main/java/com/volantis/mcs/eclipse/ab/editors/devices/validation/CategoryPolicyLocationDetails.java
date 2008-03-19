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

import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.eclipse.ab.editors.devices.DevicesMessages;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import org.jdom.Element;
import org.jdom.Namespace;

import java.text.MessageFormat;

/**
 * A LocationDetails that provides location details for a policy element
 * that is an descendant of a category element in the policy definitions
 * document.
 */
public class CategoryPolicyLocationDetails implements LocationDetails {
    /**
     * The message to use for the location details.
     */
    private static final String MESSAGE =
            DevicesMessages.getString("CategoryPolicyLocationDetails." +
            "categoryPolicyLocation");

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
     * Construct a new CategoryPolicyLocationDetails.
     * @param dram the DeviceRepositoryAccessorManager
     */
    public CategoryPolicyLocationDetails(DeviceRepositoryAccessorManager dram) {
        this.dram = dram;
    }

    /**
     * Provide the location details for the device policy definitions
     * policy element that is specifed by the given xPath
     * @param invalidElement
     * @return the location details for the category policy specified by
     * the given XPath
     * @throws com.volantis.mcs.xml.xpath.XPathException if a category policy is not specified by the
     * given XPath
     */
    public String getLocationDetailsString(Element rootElement, Element invalidElement)
            throws XPathException {

        StringBuffer xpathString = new StringBuffer();

        xpathString.append("ancestor::").
                append(MCSNamespace.DEVICE_DEFINITIONS.getPrefix()).
                append(":").
                append(DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME);

        XPath policyXPath = new XPath(xpathString.toString(), new Namespace [] {
            MCSNamespace.DEVICE_DEFINITIONS });
        Element policy = policyXPath.selectSingleElement(invalidElement);

        if (policy == null) {
            throw new XPathException("Selected element was null for xPath: " +
                    invalidElement.toString());
        }

        String policyName =
                policy.getAttributeValue(DeviceRepositorySchemaConstants.
                POLICY_NAME_ATTRIBUTE);

        xpathString = new StringBuffer();
        xpathString.append("ancestor::").
                append(MCSNamespace.DEVICE_DEFINITIONS.getPrefix()).
                append(":").
                append(DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME);

        XPath categoryXPath = new XPath(xpathString.toString(),
                new Namespace [] { MCSNamespace.DEVICE_DEFINITIONS });
        Element category = categoryXPath.selectSingleElement(policy);

        String categoryName =
                category.getAttributeValue(DeviceRepositorySchemaConstants.
                CATEGORY_NAME_ATTRIBUTE);

        String localizedName = dram.getLocalizedPolicyCategory(categoryName);
        if(localizedName!=null && localizedName.length()>0) {
            categoryName = localizedName;
        }

        localizedName = dram.getLocalizedPolicyName(policyName);
        if(localizedName!=null && localizedName.length()>0) {
            policyName = localizedName;
        }

        String message = FORMAT.format(new Object[]{categoryName,
                                                    policyName});

        return message;
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

 10-Sep-04	5488/2	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/1	allan	VBM:2004081803 Validation for range min and max values

 ===========================================================================
*/
