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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * Utility class for getting combo descriptor sets.
 */
public class ComboDescriptorUtil {

    private static final String DEFAULT_BASE_LOCATION = EditorMessages.getString("BaseLocation.default.name");
    private static final String DEVICE_BASE_LOCATION = EditorMessages.getString("BaseLocation.device.name");
    private static final String CONTEXT_BASE_LOCATION = EditorMessages.getString("BaseLocation.context.name");
    private static final String HOST_BASE_LOCATION = EditorMessages.getString("BaseLocation.host.name");

    public static Map getBaseLocationDescriptors() {
        Map comboDescriptors = new HashMap();

        List baseLocationTypes = new ArrayList();
        baseLocationTypes.add(BaseLocation.DEFAULT);
        baseLocationTypes.add(BaseLocation.DEVICE);
        baseLocationTypes.add(BaseLocation.CONTEXT);
        baseLocationTypes.add(BaseLocation.HOST);

        ILabelProvider labelProvider = new LabelProvider() {
            public String getText(Object o) {
                if (BaseLocation.DEFAULT.equals(o)) {
                    return DEFAULT_BASE_LOCATION;
                } else if (BaseLocation.DEVICE.equals(o)) {
                    return DEVICE_BASE_LOCATION;
                } else if (BaseLocation.CONTEXT.equals(o)) {
                    return CONTEXT_BASE_LOCATION;
                } else if (BaseLocation.HOST.equals(o)) {
                    return HOST_BASE_LOCATION;
                } else {
                    return "";
                }
            }
        };

        ComboDescriptor baseLocationTypesDescriptor =
                new ComboDescriptor(baseLocationTypes, labelProvider);

        comboDescriptors.put(PolicyModel.BASE_LOCATION,
                baseLocationTypesDescriptor);

        return comboDescriptors;

    }

}
