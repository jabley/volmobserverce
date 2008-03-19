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

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Create a control that is has a label followed by a horizontal line.
 * e.g.
 * This is the label -----------------------------------------------------------
 */
public class HorizontalLabelLine extends Composite {

    /**
     * Store the line color.
     */
    private Color lineColor = null;

    /**
     * The label used to display the text in this HorizontalLabelLine.
     */
    private Label label = null;

    /**
     * Create an instance of this class with the specified parameters.
     *
     * @param composite the parent composite.
     * @param style     the style
     * @param labelText the value of the label's text.
     */
    public HorizontalLabelLine(Composite composite,
                               int style,
                               String labelText) {
        this(composite, style, labelText, null);
    }

    /**
     * Create an instance of this class with the specified parameters.
     *
     * @param composite the parent composite.
     * @param style     the style
     * @param labelText the value of the label's text.
     * @param lineColor the colour of the line (if null the default will be
     *                  Black).
     */
    public HorizontalLabelLine(Composite composite,
                               int style,
                               String labelText,
                               Color lineColor) {
        super(composite, style);
        this.lineColor = lineColor;

        // Create the label with the specified text.
        label = new Label(this, SWT.NONE);
        label.setText(labelText);

        // Create the horizontal line.
        Label line = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
        line.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (lineColor == null) {
            lineColor = line.getDisplay().getSystemColor(SWT.COLOR_BLACK);
        }
        line.setBackground(lineColor);

        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        initAccessible();
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL(this, ACC.ROLE_LABEL);
        getAccessible().addAccessibleControlListener(acl);
        StandardAccessibleListener al = new StandardAccessibleListener() {
            public void getName(AccessibleEvent ae) {
                ae.result = label.getText();
            }
        };
        al.setControl(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/2	adrianj	VBM:2004102602 Accessibility support for custom controls

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 ===========================================================================
*/
