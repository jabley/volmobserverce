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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.devices;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jdom.Element;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

/**
 * LabelProvider for the device hierarchy document.
 */
public class DeviceHierarchyLabelProvider extends LabelProvider {

    /**
     * The image that is returned by the {@link #getImage} method.
     */
    private static Image image = EclipseCommonMessages.getPolicyIcon(
        DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME);


    // javadoc inherited
    public String getText(Object o) {
        // the Object argument should never be null so we do not check
        return ((Element) o).getAttributeValue(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
    }

    // javadoc inherited
    public Image getImage(Object o) {
        return image;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
