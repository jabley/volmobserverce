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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * A specialisation of Preview that provides a
 * preview of text.
 */
public class TextPreview extends Preview {

    /**
     * The single child Text of the Preview's group.
     */
    private Text textControl;


    /**
     * Creates a TextPreview control specialisation
     * of Preview.
     * @param text The text to be displayed. Throws an IllegalArgumentException if null.
     */
    // rest of javadoc inherited
    public TextPreview(Composite parent, int style, String text) {
        super(parent, style);
        if (text == null) {
            throw new IllegalArgumentException("Text argument cannot be null"); //$NON-NLS-1$
        }
        textControl.setText(text);
    }

    /**
     * Creates a Text child of the Preview's group.
     * @return The Text control.
     */
    public Control getChild() {
        if (textControl == null) {
            textControl = new Text(getGroup(), SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
            /**
             * Without setting the background, it is white on Linux and grey on Windows. It
             * should be white on Windows. Setting to null is the preferred way of setting
             * the background colour to the correct system colour for the widget.
             * @todo Verify that this gives a white background on Windows.
             */
            textControl.setBackground(null);
        }
        return textControl;
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

 24-Oct-03	1627/4	pcameron	VBM:2003102206 Added a null check for text and some javadoc

 24-Oct-03	1627/1	pcameron	VBM:2003102206 Added a TextPreview

 ===========================================================================
*/
