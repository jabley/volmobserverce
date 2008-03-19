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
package com.volantis.mcs.eclipse.controls.forms;

import com.volantis.mcs.eclipse.controls.ControlsMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Base class for all sections in form editors. Provides common function.
 */
public abstract class FormSection extends Composite {
    /**
     * The default minimum width for all form sections.
     */
    private static final int DEFAULT_MIN_WIDTH = ControlsMessages.getInteger(
            "FormSection.minWidth").intValue();

    /**
     * The minimum width for this form section.
     */
    private int minWidth;

    public FormSection(Composite parent, int style) {
        super(parent, style);

        minWidth = DEFAULT_MIN_WIDTH;

        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    }

    /**
     * Set the minimum width for this form section.
     * @param newMinWidth The minimum width for this form section
     */
    protected void setMinWidth(int newMinWidth) {
        minWidth = newMinWidth;
    }

    // Javadoc inherited
    public Point computeSize(int wHint, int hHint) {
        return new Point(minWidth, super.computeSize(wHint, hHint).y);
    }

    // Javadoc inherited
    public Point computeSize(int wHint, int hHint, boolean changed) {
        return new Point(minWidth, super.computeSize(wHint, hHint, changed).y);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/1	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 07-May-04	4172/2	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 11-Feb-04	2862/2	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 04-Dec-03	2102/1	allan	VBM:2003112101 Create the AlertsActionsSection.

 03-Dec-03	2093/3	allan	VBM:2003110402 Set the dispose listener on this.

 03-Dec-03	2093/1	allan	VBM:2003110402 Created FormSection.

 ===========================================================================
*/
