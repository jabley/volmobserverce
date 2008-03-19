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

package com.volantis.styling.unit.engine.matchers.composites;

import com.volantis.styling.impl.engine.matchers.composites.CompositeState;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class CompositeStateTestCaseAbstract
        extends TestCaseAbstract {

    protected CompositeState state;

    protected void setUp() throws Exception {
        super.setUp();

        state = createState();
    }

    protected abstract CompositeState createState();

    /**
     * Test that an unused state does not match the context.
     */
    public void testUnusedStateDoesNotMatchContext() {
        assertFalse("State should not indicate that context has matched",
                    state.hasDirectRelationship());
    }

    /**
     * Test that the context matched state cannot be updated outside an
     * element.
     */
    public void testContextMatchedFailsOutsideElement() {
        try {
            state.contextMatched();
            fail("Context matched allowed outside element");
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * Test that the context matched state is stored properly.
     */
    public void te2stContextMatched() {
        state.beforeStartElement();
        state.contextMatched();
        assertTrue("State should have stored that context has matched",
                   state.hasDirectRelationship());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
