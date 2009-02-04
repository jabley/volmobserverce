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

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.assets.ImageAssetReferenceMock;
import com.volantis.mcs.protocols.assets.implementation.LiteralImageAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;

/**
 * This class tests a {@link ConcreteMenuIcon}.
 */
public class ConcreteMenuIconTestCase
        extends AbstractModelElementTestAbstract {

    /**
     * This method tests the getNormalURL() method.  Since this is never
     * supposed to be null it tests the exception state as well as get/set
     * combinations.
     */
    public void testGetNormalURL() {
        // String to use for the tests
        final ImageAssetReference testNormalURL =
                new LiteralImageAssetReference("normal");

        ImageAssetReference testString;

        ConcreteMenuIcon testInstance = createTestClass();

        // Test invalid state
        try {
            testString = testInstance.getNormalURL();
            fail("The retrieval should trigger a state exception");
        } catch (IllegalStateException iae) {
            // Test passes - exception was thrown as intended :-)
        }

        // Test valid state
        testInstance.setNormalURL(testNormalURL);
        testString = testInstance.getNormalURL();
        assertNotNull("The test string should not be null", testString);
        assertEquals("The strings should be the same",
                testNormalURL, testString);
    }

    /**
     * This method tests the getOverURL() method.
     */
    public void testGetOverURL() {
        ConcreteMenuIcon testInstance = createTestClass();

        // Test null valued
        ImageAssetReference testString = testInstance.getOverURL();
        assertNull("The test string should be null", testString);

        // Test specific value
        final ImageAssetReferenceMock overURL =
                new ImageAssetReferenceMock("overURL",
                        expectations);
        testInstance.setOverURL(overURL);
        testString = testInstance.getOverURL();
        assertNotNull("The test string should not be null", testString);
        assertEquals("The strings should be the same",
                overURL, testString);
    }

    /**
     * A utility method for creating basic instances of ConcreteMenuIcon for
     * the various test methods.
     *
     * @return An initialised instance of ConcreteMenuIcon
     */
    private ConcreteMenuIcon createTestClass() {
        return (ConcreteMenuIcon) createTestInstance(
                createElementDetails(ELEMENT, TEST_STYLES));
    }

    // JavaDoc inherited
    protected AbstractModelElement createTestInstance(
            ElementDetails elementDetails) {
        return new ConcreteMenuIcon(elementDetails);
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

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 07-Apr-04	3735/5	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3735/3	geoff	VBM:2004033102 Enhance Menu Support: Address some issues with asset references

 07-Apr-04	3753/4	claire	VBM:2004040612 Fixed supermerge, tabs, and JavaDoc

 07-Apr-04	3753/1	claire	VBM:2004040612 Increasing laziness of reference resolution

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 11-Mar-04	3306/7	claire	VBM:2004022706 Removed uncessary specialisation equals and hashcode methods

 11-Mar-04	3306/5	claire	VBM:2004022706 Updating menu model test cases

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
