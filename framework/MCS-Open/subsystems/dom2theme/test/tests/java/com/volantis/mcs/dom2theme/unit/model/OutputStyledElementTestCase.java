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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2theme.unit.model;

import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStylesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link OutputStyledElement}.
 */
public class OutputStyledElementTestCase extends TestCaseAbstract {

    private ElementMock elementMock;

    private OutputStylesMock outputStylesMock;

    protected void setUp() throws Exception {
        super.setUp();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        elementMock = new ElementMock("element",
                expectations);

        outputStylesMock = new OutputStylesMock(
                "output styles", expectations);
    }

    /**
     * Test the constructor, especially the argument validation.
     */
    public void testConstructor() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        // element cannot be null
        try {
            new OutputStyledElement(null, null);
            fail("element cannot be null");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            new OutputStyledElement(null, outputStylesMock);
            fail("element cannot be null");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // styles can be null
        new OutputStyledElement(elementMock, null);

    }

    /**
     * Test that the name accessor returns the name of the underlying element.
     */
    public void testGetName() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        elementMock.expects.getName().returns("name");

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyledElement outputElement = new OutputStyledElement(elementMock,
                outputStylesMock);

        assertEquals("name", outputElement.getName());
    }

    /**
     * Test that the class mutator sets the class attribute of the underlying
     * element.
     */
    public void testSetClass() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        elementMock.expects.setAttribute("class", "name");

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyledElement outputElement = new OutputStyledElement(elementMock,
                outputStylesMock);

        outputElement.setClass("name");
    }

    /**
     * Test that the styles accessor and clearing method work as expected. 
     */
    public void testStyles() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyledElement outputElement = new OutputStyledElement(elementMock,
                outputStylesMock);

        assertSame(outputStylesMock, outputElement.getStyles());

        outputElement.clearStyles();

        assertNull(outputElement.getStyles());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
