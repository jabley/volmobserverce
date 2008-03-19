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
package com.volantis.mcs.eclipse.builder.common.targets;

import java.util.Collection;

/**
 * Simple multicaster for TargetSelectionMonitor instances. When initialised
 * with an array of TargetSelectionMonitors, it acts as a single
 * TargetSelectionMonitor forwarding method calls onto the specified set of
 * TSMs.
 */
public class TargetSelectionMulticaster implements TargetSelectionMonitor {
    private TargetSelectionMonitor[] monitors;

    public TargetSelectionMulticaster(TargetSelectionMonitor[] monitors) {
        this.monitors = monitors;
    }


    public void setSelectedTargets(Collection selection) {
        for (int i = 0; i < monitors.length; i++) {
            if (monitors[i] != null) {
                monitors[i].setSelectedTargets(selection);
            }
        }
    }

    public void modifySelection(Object target, boolean selected) {
        for (int i = 0; i < monitors.length; i++) {
            if (monitors[i] != null) {
                monitors[i].modifySelection(target, selected);
            }
        }
    }
}
