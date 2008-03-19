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

package com.volantis.mcs.dom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dom.impl.NodeSequenceImpl;

/**
 * Base class for test cases for {@link NodeSequence}s.
 */
public abstract class NodeSequenceTestAbstract
        extends TestCaseAbstract {

    protected NodeIterateeMock iterateeMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        iterateeMock = new NodeIterateeMock("iterateeMock", expectations);
    }

    /**
     * Create an empty sequence.
     *
     * @return An empty sequence.
     */
    protected abstract NodeSequence createEmptySequence();

    /**
     * Ensure that an empty sequence does nothing.
     */
    public void testEmpty() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        NodeSequence sequence = createEmptySequence();
        sequence.forEach(iterateeMock);
    }
}
