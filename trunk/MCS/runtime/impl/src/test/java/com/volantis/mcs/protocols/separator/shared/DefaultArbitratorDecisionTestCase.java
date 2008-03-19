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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.separator.shared;

import com.volantis.mcs.protocols.separator.SeparatorRendererMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class DefaultArbitratorDecisionTestCase
    extends TestCaseAbstract {

    private SeparatorRendererMock mockRenderer1;

    protected DefaultArbitratorDecision createDefaultDecision() {
        return new DefaultArbitratorDecision();
    }

    protected void setUp() throws Exception {
        super.setUp();

        mockRenderer1 = new SeparatorRendererMock(
                "mockRenderer1", expectations);
    }

    /**
     * Test that the defer method works.
     */
    public void testDefer()
        throws Exception {

        DefaultArbitratorDecision decision = createDefaultDecision();
        decision.defer(mockRenderer1);

        assertSame("chosen renderer not same", mockRenderer1, decision.getChosenSeparator());
        assertTrue("decision not deferred", decision.isDecisionDeferred());
    }

    /**
     * Test thet the defer methods fails if it is not allowed.
     */
    public void testDeferDisallowed()
        throws Exception {

        DefaultArbitratorDecision decision = createDefaultDecision();
        decision.setAllowDeferral(false);

        try {
            decision.defer(mockRenderer1);
            fail("Expected exception");
        } catch (IllegalStateException e) {
            assertEquals("exception message mismatch", "Deferral not allowed",
                         e.getMessage());
        }

    }

    /**
     * Test that the use method works.
     */
    public void testUse()
        throws Exception {

        DefaultArbitratorDecision decision = createDefaultDecision();
        decision.defer(mockRenderer1);

        assertSame("chosen renderer not same", mockRenderer1, decision.getChosenSeparator());
        assertFalse("decision deferred", !decision.isDecisionDeferred());
    }

    /**
     * Test that the use method works.
     */
    public void testIgnore()
        throws Exception {

        DefaultArbitratorDecision decision = createDefaultDecision();
        decision.ignore();

        assertNull("renderer not null", decision.getChosenSeparator());
        assertFalse("decision not deferred", decision.isDecisionDeferred());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/2	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/2	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3610/3	pduffin	VBM:2004032509 Added separator API and default implementation

 ===========================================================================
*/
