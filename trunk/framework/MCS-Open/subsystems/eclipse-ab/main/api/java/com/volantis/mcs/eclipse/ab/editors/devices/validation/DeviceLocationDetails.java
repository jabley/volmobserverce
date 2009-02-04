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

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;

import org.jdom.Element;
import org.jdom.Namespace;

import java.text.MessageFormat;

/**
 * Generic location details that finds a device name in the XPath that
 * specifies the erroneous element and uses this device name as the
 * format arg to a pre-defined message.
 */
public class DeviceLocationDetails implements LocationDetails {
    /**
     * The MessageFormat used to create the location details string.
     */
    private final MessageFormat format;

    /**
     * The Namespace to locate the device element.
     */
    private final Namespace namespace;

    /**
     * Constructor a new DeviceLocationDetails
     *
     * @param format    the MessageFormat to apply the device name to in order
     *                  to obtain the location details.
     * @param namespace the Namespace to locate the device element.
     * @throws IllegalArgumentException if format or namespace are null.
     */
    public DeviceLocationDetails(MessageFormat format, Namespace namespace) {
        if (format == null) {
            throw new IllegalArgumentException("Cannot be null: format");
        }
        if (namespace == null) {
            throw new IllegalArgumentException("Cannot be null: namespace");
        }
        this.format = format;
        this.namespace = namespace;
    }

    /**
     * Expects to find a device element in the hierarchy of the given
     * invalidElement. The device name on this device element is then applied
     * to the MessageFormat associated with this DeviceLocationDetails to produce
     * the returned location details
     *
     * @param rootElement    the root Element  - unusued in this implementation
     * @param invalidElement the element that is invalid
     * @return the location details of the specified invalidElement
     * @throws com.volantis.mcs.xml.xpath.XPathException
     *          if there is a problem obtainin the device name
     */
    public String getLocationDetailsString(Element rootElement,
                                           Element invalidElement)
            throws XPathException {

        StringBuffer xPathString = new StringBuffer();

        xPathString.append("ancestor-or-self::").
                append(namespace.getPrefix()).
                append(":").
                append(DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME);

        XPath deviceXPath = new XPath(xPathString.toString(), new Namespace[]{
                namespace});
        Element device = deviceXPath.selectSingleElement(invalidElement);

        if (device == null) {
            throw new XPathException(
                    "Device element was null when selected based on element: " +
                            invalidElement.toString());
        }

        String deviceName = device.
                getAttributeValue(DeviceRepositorySchemaConstants.
                        DEVICE_NAME_ATTRIBUTE);

        return format.format(new Object[]{deviceName});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Sep-04	5488/2	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 ===========================================================================
*/
