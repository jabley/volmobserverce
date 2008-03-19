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
 * An interface used to specify that a class is interested in any changes
 * to the currently selected targets, either as a whole or individually.
 */
public interface TargetSelectionMonitor {
    /**
     * Called when the selected targets change en masse.
     *
     * @param selection The new selected targets - all other targets are
     *                  unselected
     */
    public void setSelectedTargets(Collection selection);

    /**
     * Called when the selection state of a single target changes.
     *
     * @param target The target to change
     * @param selected The new selection state
     */
    public void modifySelection(Object target, boolean selected);
}
