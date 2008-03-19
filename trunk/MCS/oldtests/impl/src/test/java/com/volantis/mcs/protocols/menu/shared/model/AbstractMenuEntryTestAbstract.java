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

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuEntry;

/**
 * This is a unit test for the AbstractMenuEntry class which forms part of the
 * menu model.  It is abstract because it tests an abstract class.  Anything
 * that extends {@link AbstractMenuEntry} should unit test by extending this
 * test class.
 */
public abstract class AbstractMenuEntryTestAbstract extends
        AbstractModelElementTestAbstract {

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructors() throws Exception {
        // Test illegal construction
        try {
            createTestInstance((ElementDetails)null);
            fail("The creation of the class should have caused an exception");
        } catch (IllegalArgumentException iae) {
            // Test passes - exception gets thrown :-)
        }
    }

    /**
     * This method tests the getContainer() method of the AbstractMenuEntry
     * class.  It makes use of the test class provided by
     * {@link #createTestInstance} to create
     * a concrete instance for testing purposes.
     */
    public void testGetContainer() {
        AbstractMenuEntry testClass = (AbstractMenuEntry) createTestInstance();

        // Test null values
        MenuEntry parentObject = null;
        testClass.setContainer(parentObject);
        MenuEntry testObject = testClass.getContainer();

        assertNull("The test object should be null", testObject);
        assertEquals("The parent and test objects should be the same",
                parentObject, testObject);

        // Test non-null values;
        ElementDetails elementDetails = testClass.getElementDetails();
        ((ConcreteElementDetails)elementDetails).setElementName(
                "Second Style Element");
        parentObject = (AbstractMenuEntry) createTestInstance(elementDetails);
        testClass.setContainer(parentObject);
        testObject = testClass.getContainer();

        assertNotNull("The test object should not be null", testObject);
        assertEquals("The parent and test objects should be the same",
                parentObject, testObject);

    }

    /**
     * This method tests the getPane() method of the AbstractMenuEntry
     * class.  It makes use of the test class provided by
     * {@link #createTestInstance} to create
     * a concrete instance for testing purposes.
     */
    public void testGetPane() {
        AbstractMenuEntry testClass = (AbstractMenuEntry) createTestInstance();

        // Test null values
        FormatReference basePane = null;
        testClass.setPane(basePane);
        FormatReference testPane = testClass.getPane();

        assertNull("The test pane should be null", testPane);
        assertEquals("The base and test objects should be the same",
                basePane, testPane);

        // Test non-null values
        basePane = new FormatReference();
        basePane.setStem("Test stem text");
        testClass.setPane(basePane);
        testPane = testClass.getPane();

        assertNotNull("The test pane should not be null", testPane);
        assertEquals("The base and test objects should be the same",
                basePane, testPane);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 11-Mar-04	3306/8	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/6	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/4	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
