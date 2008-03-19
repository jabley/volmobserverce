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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathMock;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathIterateeMock;
import com.volantis.mcs.themes.MutableStylePropertiesMock;

/**
 * A test case for {@link OutputStyles}.
 */
public class OutputStylesTestCase extends TestCaseAbstract {

    private PseudoStylePathMock pseudoStylePathMock1;

    private PseudoStylePathMock pseudoStylePathMock2;

    private MutableStylePropertiesMock stylePropertiesMock1;

    private MutableStylePropertiesMock stylePropertiesMock2;

    protected void setUp() throws Exception {

        super.setUp();

        // ==================================================================
        // Create mocks.
        // ==================================================================

        pseudoStylePathMock1 = new PseudoStylePathMock("path 1", expectations);

        pseudoStylePathMock2 = new PseudoStylePathMock("path 2", expectations);

        stylePropertiesMock1 = new MutableStylePropertiesMock("props 1",
                expectations);

        stylePropertiesMock2 = new MutableStylePropertiesMock("props 2",
                expectations);
    }

    /**
     * Test that we can add some path/properties pairs and query them.
     */
    public void testAddAndGet() {

        // ==================================================================
        // Create expectations.
        // ==================================================================

        pseudoStylePathMock1.expects.isEmpty().returns(false).any();
        stylePropertiesMock1.expects.isEmpty().returns(false).any();

        pseudoStylePathMock2.expects.isEmpty().returns(false).any();
        stylePropertiesMock2.expects.isEmpty().returns(false).any();

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyles outputStyles = new OutputStyles();

        outputStyles.addPathProperties(pseudoStylePathMock1,
                stylePropertiesMock1);
        outputStyles.addPathProperties(pseudoStylePathMock2,
                stylePropertiesMock2);

        assertSame(stylePropertiesMock1,
                outputStyles.getPathProperties(pseudoStylePathMock1));
        assertSame(stylePropertiesMock2,
                outputStyles.getPathProperties(pseudoStylePathMock2));
    }

    /**
     * Test that we can add and remove some path/properties pairs and that
     * isEmpty remains correct at all times.
     */
    public void testIsEmptyAndRemove() {

        // ==================================================================
        // Create additional mocks.
        // ==================================================================

        // ==================================================================
        // Create expectations.
        // ==================================================================

        pseudoStylePathMock1.expects.isEmpty().returns(true).any();
        stylePropertiesMock1.expects.isEmpty().returns(false).any();

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyles outputStyles = new OutputStyles();

        assertTrue("", outputStyles.isEmpty());

        outputStyles.addPathProperties(pseudoStylePathMock1,
                stylePropertiesMock1);

        assertFalse("", outputStyles.isEmpty());

        outputStyles.removePathProperties(pseudoStylePathMock1);

        assertTrue("", outputStyles.isEmpty());
    }

    /**
     * Test that the iterate method calls next on all the contained paths.
     */
    public void testIterate() {

        // ==================================================================
        // Create additional mocks.
        // ==================================================================

        PseudoStylePathIterateeMock pathIterateeMock =
                new PseudoStylePathIterateeMock("iteratee", expectations);

        // ==================================================================
        // Create expectations.
        // ==================================================================

        pseudoStylePathMock1.expects.isEmpty().returns(false).any();
        stylePropertiesMock1.expects.isEmpty().returns(false).any();

        pseudoStylePathMock2.expects.isEmpty().returns(false).any();
        stylePropertiesMock2.expects.isEmpty().returns(false).any();

        pathIterateeMock.expects.next(pseudoStylePathMock1);

        pathIterateeMock.expects.next(pseudoStylePathMock2);

        // ==================================================================
        // Do the test.
        // ==================================================================

        OutputStyles outputStyles = new OutputStyles();

        outputStyles.addPathProperties(pseudoStylePathMock1,
                stylePropertiesMock1);
        outputStyles.addPathProperties(pseudoStylePathMock2,
                stylePropertiesMock2);

        outputStyles.iterate(pathIterateeMock);
    }

    // NOTE: we should test equals and hashcode as well but this would require
    // polluting the model with constructor that takes a map. So I haven't
    // bothered. Currently they are implemented as passthroughs to collections
    // classes anyway so there's not much point.

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 19-Jul-05	8668/6	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 18-Jul-05	8668/4	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
