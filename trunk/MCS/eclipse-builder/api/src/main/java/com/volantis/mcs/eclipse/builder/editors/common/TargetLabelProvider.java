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
package com.volantis.mcs.eclipse.builder.editors.common;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.CategoryReference;

/**
 * A {@link LabelProvider} for labelling targets (device and category
 * references).
 */
public class TargetLabelProvider extends LabelProvider {
    /**
     * Icon for device references.
     */
    private static final Image DEVICE_IMAGE = EclipseCommonMessages.getPolicyIcon(
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME);

    /**
     * Icon for category references.
     */
    private static final Image CATEGORY_IMAGE = EclipseCommonMessages.getPolicyIcon(
                DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME);

    // Javadoc inherited
    public Image getImage(Object o) {
        if (o instanceof DeviceReference) {
            return DEVICE_IMAGE;
        } else if (o instanceof CategoryReference) {
            return CATEGORY_IMAGE;
        } else {
            return null;
        }
    }

    // Javadoc inherited
    public String getText(Object o) {
        if (o instanceof DeviceReference) {
            return ((DeviceReference) o).getDeviceName();
        } else if (o instanceof CategoryReference) {
            return ((CategoryReference) o).getCategoryName();
        } else {
            return null;
        }
    }
}
