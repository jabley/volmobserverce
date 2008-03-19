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

package com.volantis.mcs.eclipse.controls;

/**
 * This interface may be implemented by composites that form part of an
 * attributes composite if they wish to have secondary visibility and enablement
 * management.
 */
public interface SecondaryControl {

    /**
     * Permit a secondary control to be enabled/disabled.
     *
     * @param enabled the enabled state of the secondary composite control.
     */
    public void setSecondaryEnabled(boolean enabled);

    /**
     * Update the secondary control's visibility status.
     *
     * @param visible true if this composite's secondary control(s) need to
     *                shown, false otherwise.
     */
    public void setSecondaryVisible(boolean visible);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: hide units control if element is a grid

 ===========================================================================
*/
