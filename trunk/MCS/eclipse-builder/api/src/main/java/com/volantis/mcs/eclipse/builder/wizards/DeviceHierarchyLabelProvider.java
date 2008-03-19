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
package com.volantis.mcs.eclipse.builder.wizards;

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
    private Image image = null;


    // javadoc inherited
    public String getText(Object o) {
        // the Object argument should never be null so we do not check
        return ((Element) o).getAttributeValue(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
    }

    // javadoc inherited
    public Image getImage(Object o) {
        // lazily create the device image
        if (image == null) {
            image = EclipseCommonMessages.getPolicyIcon(
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME);
        }
        // return the image
        return image;
    }

    // javadoc inherited
    public void dispose() {
        super.dispose();
        if (image != null) {
            // dispose of the image
            image.dispose();
            image = null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 ===========================================================================
*/
