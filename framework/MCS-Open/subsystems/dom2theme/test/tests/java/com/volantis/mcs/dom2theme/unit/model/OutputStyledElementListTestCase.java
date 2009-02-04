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
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIterateeMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementMock;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.iteration.IterationAction;

/**
 * A test case for {@link OutputStyledElementList}.
 */
public class OutputStyledElementListTestCase
        extends TestCaseAbstract {

    private ElementMock elementMock;

    private OutputStyledElementMock outputElementMock1;

    private OutputStyledElementMock outputElementMock2;

    private OutputStyledElementIterateeMock iterateeMock;

    protected void setUp() throws Exception {

        super.setUp();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        elementMock = new ElementMock("element", expectations);

        outputElementMock1 =
                new OutputStyledElementMock("output element", expectations,
                        elementMock, null);
        outputElementMock2 =
                new OutputStyledElementMock("output element 2", expectations,
                        elementMock, null);
        iterateeMock =
                new OutputStyledElementIterateeMock("iteratee", expectations);
    }

    /**
     * Test that the iterate method can iterate through the entire content.
     */
    public void testIterateContinue() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        iterateeMock.expects.next(outputElementMock1).returns(
                IterationAction.CONTINUE);
        iterateeMock.expects.next(outputElementMock2).returns(
                IterationAction.CONTINUE);

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyledElementList elementList = new OutputStyledElementList();

        elementList.add(outputElementMock1);
        elementList.add(outputElementMock2);

        elementList.iterate(iterateeMock);
    }

    /**
     * Test that the iterate method can break out of the iteration midway.
     */
    public void testIterateBreak() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        iterateeMock.expects.next(outputElementMock1).returns(
                IterationAction.BREAK);

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyledElementList elementList = new OutputStyledElementList();

        elementList.add(outputElementMock1);
        elementList.add(outputElementMock2);

        elementList.iterate(iterateeMock);
    }

    /**
     * Test that the add method rejects null elements.
     */
    public void testNullElement() {

        OutputStyledElementList elementList = new OutputStyledElementList();

        try {
            elementList.add(null);
            fail("element may not be null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test that the iterate method rejects null iteratees.
     */
    public void testNullIteratee() {

        OutputStyledElementList elementList = new OutputStyledElementList();

        try {
            elementList.iterate(null);
            fail("iteratee may not be null");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jul-05	8668/6	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/4	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
