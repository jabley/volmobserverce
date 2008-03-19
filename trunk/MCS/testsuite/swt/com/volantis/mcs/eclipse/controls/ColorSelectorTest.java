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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.NamedColor;
import org.eclipse.swt.SWT;

/**
 * This is a manual test for the ColorSelector. Run the test
 * and follow the instructions which describe the successful
 * test criteria.
 */
public class ColorSelectorTest extends ControlsTestAbstract {

    /**
     * Construct a ColorSelectorTest.
     * @param title the title of the test
     */
    public ColorSelectorTest(String title) {
        super(title);
    }

    /**
     * Adds the ColorSelector widget to the shell.
     */
    public void createControl() {
        new ColorSelector(getShell(), SWT.BORDER, NamedColor.getAllColors());
    }

    /**
     * The description of the tests to carry out for the ColorSelector and
     * what the expected results are for success.
     * @return success criteria
     */
    public String getSuccessCriteria() {
        String msg = "This is a test of the ColorSelector.\n" +
                "The ColorSelector must appear and function correctly for the test to pass.\n" +
                "You should initially see an aqua-coloured square, and the \"Aqua\" string in\n" +
                "the Combo box. Selecting a colour from the list should update the coloured button\n" +
                "with the appropriate colour. Entering some text in the Combo should result in the\n" +
                "colour button being updated. If an invalid colour is entered, the colour button\n" +
                "should be cleared of colour. A valid colour is either one of the colour names in the list,\n" +
                "or, a three or six digit hexadecimal number beginning with a # character.\n" +
                "Colour names and hex values are case independent.\n" +
                "Pressing the colour button should bring up a native ColorDialog. Selecting a color\n" +
                "from the dialog should result in the colour's hex value appearing in the Combo, and\n" +
                "the colour button being updated with the colour\n";
        return msg;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the ColorSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new ColorSelectorTest("ColorSelector Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 21-Nov-03	1634/3	pcameron	VBM:2003102205 Added ColorSelector, NamedColor and supporting classes

 ===========================================================================
*/
