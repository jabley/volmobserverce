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

import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.volantis.mcs.eclipse.builder.editors.common.TargetSelectionListener;
import com.volantis.mcs.eclipse.builder.editors.common.TargetSelectionEvent;

/**
 * Common parent class for GUI components handling selection of targets
 * (devices or categories).
 */
public abstract class TargetSelectionComponent extends Composite implements TargetSelectionMonitor {
    /**
     * The target selection listeners.
     */
    private List targetSelectionListeners = new ArrayList();

    // Javadoc not required
    public TargetSelectionComponent(Composite parent, int style) {
        super(parent, style);
    }

    // Javadoc not required
    public void addTargetSelectionListener(TargetSelectionListener toAdd) {
        targetSelectionListeners.add(toAdd);
    }

    // Javadoc not required
    public void removeTargetSelectionListener(
            TargetSelectionListener toRemove) {
        targetSelectionListeners.remove(toRemove);
    }

    /**
     * Helper method to fire an event for the selection/deselection of
     * a given target.
     *
     * @param target The target whose state has changed
     * @param selected True if the target has been selected, false if it
     *                 has been deselected
     */
    protected void fireSelectionEvent(Object target, boolean selected) {
        if (!targetSelectionListeners.isEmpty()) {
            TargetSelectionEvent tse = new TargetSelectionEvent(this, target, selected);
            Iterator it = targetSelectionListeners.iterator();
            while (it.hasNext()) {
                TargetSelectionListener listener = (TargetSelectionListener) it.next();
                listener.targetSelectionChanged(tse);
            }
        }
    }
}
