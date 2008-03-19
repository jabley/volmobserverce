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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.widgets.Control;

/**
 * A simple AccessibleListener implementation that delegates help requests
 * to the tooltip for the control with which it is associated.
 */
public class StandardAccessibleListener extends AccessibleAdapter {
    /**
     * The control for which this is the AccessibleListener
     */
    private Control control;

    /**
     * No-argument constructor for StandardAccessibleListener, so that it can
     * be overridden by an anonymous class.
     * <p>Note that if this is used, the {@link #setControl} method should be
     * called to provide information about the associated control.
     */
    public StandardAccessibleListener() {
    }

    /**
     * Standard constructor for StandardAccessibleListener.
     * @param control The control for which this is the AccessibleListener
     */
    public StandardAccessibleListener(Control control) {
        this.control = control;
    }

    /**
     * Specify the control with which this AccessibleListener is associated.
     * @param control The control for this listener
     */
    public void setControl(Control control) {
        this.control = control;
    }

    // Javadoc inherited
    public void getHelp(AccessibleEvent ae) {
        if (control != null) {
            ae.result = control.getToolTipText();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 ===========================================================================
*/
