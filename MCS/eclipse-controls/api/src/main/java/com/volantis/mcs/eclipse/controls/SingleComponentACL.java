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

import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * An AccessibleControlListener that masks any child controls.
 * <p>This class works by implementing those methods that relate to child
 * components in such a way that only the single control is exposed. Where
 * appropriate, this class should be overridden to proovide an
 * implementation of the getValue() method.</p>
 */
public class SingleComponentACL extends AccessibleControlAdapter {
    /**
     * The role this component plays (defined as a public static final field
     * in the SWT {@link ACC} class.
     */
    private int role;

    /**
     * The control for which this is the AccessibleControlListener
     */
    private Control control;

    /**
     * No-argument constructor for SingleComponentACL, so that it can be
     * overridden by an anonymous class.
     * <p>Note that if this is used, the {@link #setControl} and
     * {@link #setRole} methods should be called to provide information
     * about the control.
     */
    public SingleComponentACL() {
    }

    /**
     * Constructor for SingleComponentACL.
     * @param control The control for which this is the
     *                AccessibleControlListener
     * @param role The SWT role played by this control
     */
    public SingleComponentACL(Control control, int role) {
        this.control = control;
        this.role = role;
    }

    /**
     * Specify the accessibility role carried out by this control.
     * <p>Legal values for this role are defined as public static variables
     * on the {@link ACC} class.
     * @param role The role forr this control.
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * Specify the control which this AccessibleControlListener is
     * associated with.
     * @param control The control for this listener
     */
    public void setControl(Control control) {
        this.control = control;
    }

    // Javadoc inherited
    public void getChildAtPoint(AccessibleControlEvent event) {
        Point testPoint = control.toControl(new Point(event.x, event.y));
        if (control.getBounds().contains(testPoint)) {
            event.childID = ACC.CHILDID_SELF;
        }
    }

    // Javadoc inherited
    public void getLocation(AccessibleControlEvent event) {
        Rectangle location = control.getBounds();
        Point pt = control.toDisplay(new Point(location.x, location.y));
        event.x = pt.x;
        event.y = pt.y;
        event.width = location.width;
        event.height = location.height;
    }

    // Javadoc inherited
    public void getChildCount(AccessibleControlEvent event) {
        event.detail = 0;
    }

    // Javadoc inherited
    public void getRole(AccessibleControlEvent event) {
        event.detail = role;
    }

    // Javadoc inherited
    public void getState(AccessibleControlEvent event) {
        event.detail = ACC.STATE_NORMAL;
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
