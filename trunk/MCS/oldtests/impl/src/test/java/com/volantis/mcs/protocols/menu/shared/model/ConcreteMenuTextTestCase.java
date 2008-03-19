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
/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.ElementDetails;

/**
 * This is a unit test for the ConcreteMenuText class which forms part of the
 * menu model.
 *
 */
public class ConcreteMenuTextTestCase extends AbstractModelElementTestAbstract {

    /**
     * String to use for the tests
     */
    private final OutputBuffer testText = new DOMOutputBuffer();

    /**
     * This method tests the getText() method.  Since this is never supposed to
     * be null it tests the exception state as well as get/set combinations.
     */
    public void testGetText() {

        OutputBuffer testString;

        ConcreteMenuText testInstance = createTestClass(null);

        // Test invalid state
        try {
            testString = testInstance.getText();
            fail("The retrieval should trigger a state exception");
        } catch (IllegalStateException iae) {
            // Test passes - exception was thrown as intended :-)
        }

        // Test valid state
        testInstance.setText(testText);
        testString = testInstance.getText();
        assertNotNull("The test string should not be null", testString);
        assertEquals("The strings should be the same", testText, testString);
    }

    /**
     * A utility method to create an instance of ConcreteMenuText.  The
     * parameter can be used to set the initial text string for the instance.
     * If this is null, no text will be set but subsequent calls of getText()
     * on the returned object will throw exceptions if there have been no
     * setText() calls.
     *
     * @param menuText The text to display on the menu
     * @return         An initialised instance of ConcreteMenuText
     */
    private ConcreteMenuText createTestClass(OutputBuffer menuText) {
        ConcreteMenuText testInstance = (ConcreteMenuText)
                createTestInstance(createElementDetails(ELEMENT, TEST_STYLES));
        if (menuText != null) {
            testInstance.setText(menuText);
        }

        return testInstance;
    }

    // JavaDoc inherited
    protected AbstractModelElement createTestInstance(
            ElementDetails elementDetails) {
        return new ConcreteMenuText(elementDetails);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9609/2	ibush	VBM:2005082215 Move on/off color values for menu items

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 11-Mar-04	3306/7	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
