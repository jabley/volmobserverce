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

import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoElementMock;
import com.volantis.styling.PseudoStyleEntityIterateeMock;
import com.volantis.styling.PseudoStyleEntityVisitorMock;
import com.volantis.styling.StatefulPseudoClassSetMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link PseudoStylePath}.
 */
public class PseudoStylePathTestCase extends TestCaseAbstract {

    private PseudoElementMock pseudoElementMock;

    private PseudoStyleEntityIterateeMock pseudoStyleEntityIterateeMock;

    private PseudoStyleEntityVisitorMock pseudoStyleEntityVisitorMock;
    private StatefulPseudoClassSetMock pseudoClassSetMock;

    protected void setUp() throws Exception {

        super.setUp();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        pseudoClassSetMock = new StatefulPseudoClassSetMock(
                "pseudoClassSetMock", expectations);

        pseudoElementMock = new PseudoElementMock("pseudo element",
                expectations);

        pseudoStyleEntityIterateeMock = new PseudoStyleEntityIterateeMock(
                "iteratee", expectations);

        pseudoStyleEntityVisitorMock = new PseudoStyleEntityVisitorMock(
                "visitor", expectations);
    }

    /**
     * Test that the isEmpty method works correctly.
     */
    public void testIsEmpty() {

        PseudoStylePath path = PseudoStylePath.EMPTY_PATH;

        assertEquals(true, path.isEmpty());

        path = PseudoStylePath.EMPTY_PATH.addPseudoClassSet(pseudoClassSetMock);

        assertEquals(false, path.isEmpty());
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

        pseudoStyleEntityIterateeMock.expects.next(pseudoElementMock).returns(
                IterationAction.CONTINUE);

        pseudoStyleEntityIterateeMock.expects.next(pseudoClassSetMock).returns(
                IterationAction.CONTINUE);

        // ==================================================================
        // Do the test.
        // ==================================================================

        PseudoStylePath path = PseudoStylePath.EMPTY_PATH
                .addPseudoElement(pseudoElementMock)
                .addPseudoClassSet(pseudoClassSetMock);

        path.iterate(pseudoStyleEntityIterateeMock);
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

        pseudoStyleEntityIterateeMock.expects.next(pseudoElementMock).
                returns(IterationAction.BREAK);

        // ==================================================================
        // Do the test.
        // ==================================================================

        PseudoStylePath path = PseudoStylePath.EMPTY_PATH
                .addPseudoElement(pseudoElementMock)
                .addPseudoClassSet(pseudoClassSetMock);

        path.iterate(pseudoStyleEntityIterateeMock);
    }

    /**
     * Test that the path can accept a visitor (i.e. calls accept on the
     * contained pseudo style entities).
     */
    public void testAccept() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        pseudoElementMock.expects.accept(pseudoStyleEntityVisitorMock);

        pseudoClassSetMock.expects.accept(pseudoStyleEntityVisitorMock);

        // ==================================================================
        // Do the test.
        // ==================================================================

        PseudoStylePath path = PseudoStylePath.EMPTY_PATH
                .addPseudoElement(pseudoElementMock)
                .addPseudoClassSet(pseudoClassSetMock);

        path.accept(pseudoStyleEntityVisitorMock);

    }

    /**
     * Test that the equals method works correctly.
     */
    public void testEquals() {

        // ==================================================================
        // Create mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        // todo: later: add assertions for equals when mock framework supports
        // expectations on equals.

        // ==================================================================
        // Do the test.
        // ==================================================================

        PseudoStylePath path = PseudoStylePath.EMPTY_PATH;

        PseudoStylePath other = PseudoStylePath.EMPTY_PATH;

        assertTrue(path.equals(other));
        assertTrue(other.equals(path));

        path = PseudoStylePath.EMPTY_PATH.addPseudoClassSet(pseudoClassSetMock);

        assertTrue(!path.equals(other));
        assertTrue(!other.equals(path));

        other = PseudoStylePath.EMPTY_PATH.addPseudoClassSet(pseudoClassSetMock);

        assertTrue(path.equals(other));
        assertTrue(other.equals(path));

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

 19-Jul-05	8668/4	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/2	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
