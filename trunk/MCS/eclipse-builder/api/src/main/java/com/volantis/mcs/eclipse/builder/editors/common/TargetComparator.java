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

import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DeviceReference;

import java.util.Comparator;

/**
 * Comparator for targets (device/category references).
 */
public class TargetComparator implements Comparator {
    // Javadoc inherited
    public int compare(Object o1, Object o2) {
        int match = targetToString(o1).compareTo(targetToString(o2));
        if (match == 0) {
            // If the names are equal, order them by type
            boolean dev1 = o1 instanceof DeviceReference;
            boolean dev2 = o2 instanceof DeviceReference;
            match = dev1 ? (dev2 ? 0 : 1) : (dev2 ? -1 : 0);
        }
        return match;
    }

    /**
     * Converts from a target type to the string name for that target. If the
     * value passed in is not a valid target type, ClassCastException is thrown.
     *
     * @param target The target reference
     * @return The string name of the reference
     * @throws ClassCastException if a non-target value is provided
     */
    private String targetToString(Object target) {
        if (target instanceof DeviceReference) {
            return ((DeviceReference) target).getDeviceName();
        } else {
            return ((CategoryReference) target).getCategoryName();
        }
    }
}
