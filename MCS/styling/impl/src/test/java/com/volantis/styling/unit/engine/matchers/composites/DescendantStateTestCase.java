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
import com.volantis.styling.impl.engine.matchers.composites.DescendantState;

/**
 * Test the {@link DescendantState}.
 */
public class DescendantStateTestCase
        extends CompositeStateTestCaseAbstract {

    protected CompositeState createState() {
        return new DescendantState();
    }

    /**
     * Test that state remembers that the context has been matched within
     * descendant nodes.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then all the elements marked with a <code>*</code>
     * expect to know that the context matched.</p>
     *
     * <pre>
     *     a#
     *     +- b*
     *        +- c*
     *           +- d*
     * </pre>
     */
    public void testDescendantsKnowContextMatched() {
        state.beforeStartElement();
        state.contextMatched();
        for (int i = 0; i < 10; i += 1) {
            state.beforeStartElement();
            assertTrue("Descendants should know context matched",
                       state.hasDirectRelationship());
        }
    }

    /**
     * Test that the context matched state is cleared outside the element.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then all the elements marked with a <code>*</code>
     * do not expect to know that the context matched.</p>
     *
     * <pre>
     *     a*
     *     +- b#
     *     c*
     * </pre>
     */
    public void testContextMatchedClearedOutsideElement() {
        state.beforeStartElement();
        state.beforeStartElement();
        state.contextMatched();
        state.afterEndElement();
        assertFalse("Context matched state should be cleared outside the" +
                    " element", state.hasDirectRelationship());
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
