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
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * The base class for the Preview control that
 * provides a titled Group into which Preview
 * controls can be placed.
 */
public abstract class Preview extends Composite {

    /**
     * The title for the Preview Control.
     */
    private static final String PREVIEW_TITLE =
            ControlsMessages.getString("Preview.title");

    /**
     * The group for this control. The only child.
     */
    private Group group;

    /**
     * The left margin spacing for ImagePreview.
     */
    private static final int MARGIN_LEFT =
            ControlsMessages.getInteger("Preview.marginLeft").intValue();

    /**
     * The right margin spacing for ImagePreview.
     */
    private static final int MARGIN_RIGHT =
            ControlsMessages.getInteger("Preview.marginRight").intValue();

    /**
     * The top margin spacing for ImagePreview.
     */
    private static final int MARGIN_TOP =
            ControlsMessages.getInteger("Preview.marginTop").intValue();

    /**
     * The bottom margin spacing for ImagePreview.
     */
    private static final int MARGIN_BOTTOM =
            ControlsMessages.getInteger("Preview.marginBottom").intValue();

    /**
     * The default set of FormData for the Preview's control.
     */
    private static final FormData formData = new FormData();

    /**
     * Create a new Preview with a title taken from the
     * Preview.title property.
     * @param parent The parent of the Preview control.
     * @param style The style of the Preview control.
     */
    public Preview(Composite parent, int style) {
        super(parent, style);
        group = new Group(parent, SWT.NONE);
        group.setText(PREVIEW_TITLE);
        group.setLayout(new FormLayout());
        formData.left = new FormAttachment(0, MARGIN_LEFT);
        formData.right = new FormAttachment(100, -MARGIN_RIGHT);
        formData.top = new FormAttachment(0, MARGIN_TOP);
        formData.bottom = new FormAttachment(100, -MARGIN_BOTTOM);
        getChild().setLayoutData(formData);
        initAccessible();
    }

    /**
     * Get the Group component.
     * @return The Group component.
     */
    public Group getGroup() {
        return group;
    }

    /*
     * Get the child component of the Group.
     * @return The child component of the Group.
     */
    public abstract Control getChild();

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener al = new StandardAccessibleListener() {
            public void getName(AccessibleEvent ae) {
                ae.result = PREVIEW_TITLE;
            }
        };
        al.setControl(this);
        getAccessible().addAccessibleListener(al);
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

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Oct-03	1608/4	pcameron	VBM:2003101607 Refactored Preview controls and made UnavailablePreview center the label and text

 20-Oct-03	1576/3	pcameron	VBM:2003100802 Changed UnavailablePreview message and javadoc for Preview

 20-Oct-03	1576/1	pcameron	VBM:2003100802 Added Preview and UnavailablePreview functionality

 ===========================================================================
*/
