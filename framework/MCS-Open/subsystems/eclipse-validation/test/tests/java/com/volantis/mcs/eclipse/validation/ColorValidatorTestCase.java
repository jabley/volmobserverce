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
package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the color validator object.
 */
public class ColorValidatorTestCase extends TestCaseAbstract {

    private List invalidColors;
    private List standardColors;
    private List systemColors;
    private List validHex;
    private List invalidHex;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        
        invalidColors = new ArrayList();
        invalidColors.add("SortOfGreenishBrowneyBlue");
        invalidColors.add("Chicken");
        invalidColors.add("Soup");

        standardColors = new ArrayList();
        standardColors.add("Red");
        standardColors.add("Black");
        standardColors.add("Blue");
        standardColors.add("Green");

        systemColors = new ArrayList();
        systemColors.add("HighlightText");
        systemColors.add("MenuText");
        systemColors.add("ThreeDLightShadow");
        systemColors.add("AppWorkspace");

        validHex = new ArrayList();
        validHex.add("#abcdef");
        validHex.add("#aFDE79");
        validHex.add("#08c");
        validHex.add("#512347");

        invalidHex = new ArrayList();
        invalidHex.add("abcdef");
        invalidHex.add("#a");
        invalidHex.add("#ab");
        invalidHex.add("#abcd");
        invalidHex.add("#abcde");
        invalidHex.add("#abcdeff");

        // Make sure a display has been created before running the tests.
        Display.getDefault();
    }

    protected void tearDown()
            throws Exception {

        // Dispose of the previously created display.
        Display current = Display.getCurrent();
        if (current != null) {
            current.dispose();
        }

        super.tearDown();
    }

    /**
     * Create a ColorValidator with the specified colors.
     * @param colors the NamedColors to test
     * @return the ColorValidator
     */
    private Validator createValidator(NamedColor[] colors) {
        return new ColorValidator(colors);
    }

    private String getMsg(int code) {
        switch (code) {
            case Status.ERROR:
                return "ERROR";
            case Status.INFO:
                return "INFO";
            case Status.OK:
                return "OK";
            case Status.WARNING:
                return "WARNING";
        }
        return "UKNOWN";
    }

    /**
     * Carry out specific tests of the validator.
     * @param validator the validator to use
     * @param list the list of objects to validate
     * @param expected the expected result
     */
    private void doTest(ColorValidator validator, List list, int expected) {
        for (int i = 0; i < list.size(); i++) {
            ValidationStatus status =
                    validator.validate(list.get(i),
                            new ValidationMessageBuilder(null, null, null));

            assertEquals("Expected '" + getMsg(expected) + "' but was '" +
                    list.get(i) + "'",
                    expected,
                    status.getSeverity());
        }
    }

    /**
     * Test standard color items.
     */
    public void testStandardColors() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < standardColors.size(); i++) {
            list.add(standardColors.get(i));
        }
        list.add("Aqua");
        list.add("Fuchsia");
        doTest((ColorValidator) createValidator(NamedColor.getStandardColors()), list, Status.OK);
    }

    /**
     * Test items that are not standard colors.
     */
    public void testNotInStandardColors() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < systemColors.size(); i++) {
            list.add(systemColors.get(i));
        }
        list.add("YellowGreen");
        list.add("MidnightBlue");
        doTest((ColorValidator) createValidator(NamedColor.getStandardColors()), list, Status.ERROR);
    }

    /**
     * Test system color items.
     */
    public void testSystemColors() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < systemColors.size(); i++) {
            list.add(systemColors.get(i));
        }
        //list.add("InactiveBorder");
        //list.add("WindowFrame");
        doTest((ColorValidator) createValidator(NamedColor.getSystemColors()), list, Status.OK);
    }

    /**
     * Test items that are not system colors.
     */
    public void testNotInSystemColors() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < standardColors.size(); i++) {
            list.add(standardColors.get(i));
        }
        list.add("Blah");
        list.add("Volantis");
        doTest((ColorValidator) createValidator(NamedColor.getSystemColors()), list, Status.ERROR);
    }

    /**
     * Test invalid color items.
     */
    public void testInvalidColors() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < invalidColors.size(); i++) {
            list.add(invalidColors.get(i));
        }
        list.add("Cat");
        list.add("Dog");
        doTest((ColorValidator) createValidator(NamedColor.getAllColors()), list, Status.ERROR);
    }

    /**
     * Test valid hex values
     */
    public void testValidHex() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < validHex.size(); i++) {
            list.add(validHex.get(i));
        }
        list.add("#071acb");
        list.add("#96f");
        doTest((ColorValidator) createValidator(NamedColor.getAllColors()), list, Status.OK);
    }

    /**
     * Test invalid hex values
     */
    public void testInvalidHex() throws Exception {
        List list = new ArrayList();
        for (int i = 0; i < invalidHex.size(); i++) {
            list.add(invalidHex.get(i));
        }
        list.add("######");
        list.add("#aaaaaaaa");
        list.add("#1673653763763");
        doTest((ColorValidator) createValidator(NamedColor.getAllColors()), list, Status.ERROR);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Nov-03	2024/1	pcameron	VBM:2003111704 Added ColorListSelectionDialog

 25-Nov-03	1634/5	pcameron	VBM:2003102205 A few changes to ColorSelector

 21-Nov-03	1634/3	pcameron	VBM:2003102205 Added ColorSelector, NamedColor and supporting classes

 ===========================================================================
*/
