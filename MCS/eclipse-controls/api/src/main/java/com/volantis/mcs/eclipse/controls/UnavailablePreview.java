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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


/**
 * A specialisation of Preview that provides a
 * centered Label with text.
 */
public class UnavailablePreview extends Preview {

    /**
     * The single child composite of the Preview's group.
     */
    private Composite comp;

    /**
     * The text of the label.
     */
    private static final String LABEL_TEXT =
            ControlsMessages.getString("UnavailablePreview.message"); //$NON-NLS-1$

    /**
     * Creates an UnavailablePreview control specialisation
     * of Preview.
     */
    public UnavailablePreview(Composite parent, int style) {
        super(parent, style);

    }

    /**
     * Creates a composite child of the Preview's group. A label with text
     * taken from the UnavailablePreview.message property is added to the
     * composite. The layout of the composite, and layout data of the label
     * are set to center the label.
     * @return The composite control.
     */
    public Control getChild() {
        if (comp == null) {
            /**
             * An intermediate composite and GridLayout provides the simplest
             * solution for having a centered label with centered text within
             * the Preview. The composite obeys the margins for the Preview
             * (since the Preview gives it layoutData as Formdata), but can
             * have its own layout. Note that you would have to be careful to
             * preserve the margins of the Preview if you wanted the composite
             * to have different FormData. The GridLayout and GridData center
             * the label, keeping it at its preferred size.
             *
             * These layout behaviours can nearly be achieved by using
             * FormAttachments in the Preview so that the label completely fills
             * the group's area (minus the margins). However, there is no obvious
             * way to center the text vertically within the label. There is a
             * "custom" CLabel provided by SWT which does center the text vertically,
             * but it introduces an ellipsis within the label text if the text is too
             * wide for the display. This latter solution was rejected as not
             * fullfilling the Look & Feel guidelines.
             */
            comp = new Composite(getGroup(), SWT.NONE);
            comp.setLayout(new GridLayout());
            // You must use SWT.CENTER to center the text horizontally
            // within the label.
            Label label = new Label(comp, SWT.CENTER);
            label.setText(LABEL_TEXT);
            GridData gd = new GridData();
            gd.horizontalAlignment = GridData.CENTER;
            gd.verticalAlignment = GridData.CENTER;
            gd.grabExcessHorizontalSpace = true;
            gd.grabExcessVerticalSpace = true;
            label.setLayoutData(gd);
        }
        return comp;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Oct-03	1608/4	pcameron	VBM:2003101607 Refactored Preview controls and made UnavailablePreview center the label and text

 20-Oct-03	1576/1	pcameron	VBM:2003100802 Added Preview and UnavailablePreview functionality

 ===========================================================================
*/
