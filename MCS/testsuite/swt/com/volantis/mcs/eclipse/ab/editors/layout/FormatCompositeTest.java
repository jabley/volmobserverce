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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Test case for FormatComposite.
 */
public class FormatCompositeTest extends ControlsTestAbstract {

    /**
     * Create a new FormatCompositeTest
     */
    public FormatCompositeTest(String title) {
        super(title);
    }

    /**
     * Create a Control containing a StyleGroup and input fields allowing
     * the text, font size and border width to be changed.
     */
    public void createControl() {
        Composite composite = new Composite(getShell(), SWT.NONE);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);

        final ODOMElement pane = new ODOMElement("paneFormat");
        pane.setAttribute("name", "name of pane\nanother line");
        pane.setAttribute("backgroundColor", "yellow");
        final FormatComposite formatComposite = new FormatComposite(composite,
                 pane, null, null) {
            ColorRegistry colorRegistry;
            public String getLocalizedText() {
                return "Pane (" + getElement().getAttributeValue("name") + ")";
            }

            public ColorRegistry getColorRegistry() {
                if(colorRegistry==null) {
                    colorRegistry = new ColorRegistry();
                }
                return colorRegistry;
            }
        };
        GridData data = new GridData(GridData.FILL_BOTH);
        formatComposite.setLayoutData(data);

        data = new GridData(GridData.FILL_BOTH);
        formatComposite.setLayoutData(data);
        FormatComposite groupChild = new FormatComposite(formatComposite, pane,
                null, null);
        data = new GridData(GridData.FILL_BOTH);
        groupChild.setLayoutData(data);
        groupChild = new FormatComposite(formatComposite, pane,
                null, null);
        data = new GridData(GridData.FILL_BOTH);
        groupChild.setLayoutData(data);
    }

    public String getSuccessCriteria() {
        String message = "This test will display a Pane containing two" +
                "child panes in a single column. \n\nSelecting any pane with " +
                "the mouse should set the border color to active selection" +
                " and set the border of the previously selected pane (if any)" +
                " to inactive selection. \n\nResizing the window should resize the" +
                " panes consistently. \n\nThe background color for all the panes" +
                " should be yellow. \n\nThe text of the child panes should" +
                " be \"Pane\" and these should be labelled \"name of pane\" " +
                " with the label in the center. \nThe outer pane should have a " +
                " text of \"Pane (name of pane)\". \n\nThe pane borders should " +
                " red and a single pixel wide. \n\nThe text for the panes " +
                "should appear close to the left of the pane and vertically" +
                "centered with the border line.";
        return message;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the ColorSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new FormatCompositeTest("FormatComposite Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-04	5663/1	tom	VBM:2004081003 Replaced ColorRegistry with Eclipse V3.0.0 Version

 25-Aug-04	5266/1	philws	VBM:2004081007 Fix layout context menu handling

 18-Aug-04	5264/1	allan	VBM:2004081008 Use GC.textExtent() instead of GC.stringExtent()

 03-Aug-04	4902/3	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 ===========================================================================
*/
