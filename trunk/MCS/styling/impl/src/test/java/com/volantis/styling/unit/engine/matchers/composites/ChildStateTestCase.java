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

import com.volantis.styling.impl.engine.matchers.composites.ChildState;
import com.volantis.styling.impl.engine.matchers.composites.CompositeState;

/**
 * Test the {@link ChildState}.
 */
public class ChildStateTestCase
        extends CompositeStateTestCaseAbstract {

    protected CompositeState createState() {
        return new ChildState();
    }

    /**
     * Test that child nodes are within context.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then only the elements marked with a <code>*</code>
     * expect to know that the context matched.</p>
     *
     * <pre>
     *     a#
     *     +- b*
     *     +- c*
     *     +- d*
     * </pre>
     */
    public void testChildrenWithinContext() {
        state.beforeStartElement();
        state.contextMatched();
        for (int i = 0; i < 10; i += 1) {
            state.beforeStartElement();
            assertTrue("Child matches", state.hasDirectRelationship());
            state.afterEndElement();
        }
    }

    /**
     * Test that grand child nodes are not within context.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then only the elements marked with a <code>*</code>
     * expect to know that the context matched.</p>
     *
     * <pre>
     *     a#
     *     +- b*
     *     |  +- c
     *     +- d*
     * </pre>
     */
    public void testGrandChildrenNotWithinContext() {
        // Context
        state.beforeStartElement();
        state.contextMatched();

        // Child
        state.beforeStartElement();
        assertTrue("Child before grand child must know",
                   state.hasDirectRelationship());

        // Grand child
        state.beforeStartElement();
        assertFalse("Grand child must not know that context matched",
                    state.hasDirectRelationship());
        state.afterEndElement();

        // End Child
        state.afterEndElement();

        // Second Child
        state.beforeStartElement();
        assertTrue("Child after grand child must know",
                   state.hasDirectRelationship());
        state.afterEndElement();

        // End Context
        state.afterEndElement();
    }

    /**
     * Test that nodes that are children of an element that matches the
     * context, that is itself a child of an element that matches the context
     * know that the context matched.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then only the elements marked with a <code>*</code>
     * expect to know that the context matched.</p>
     *
     * <pre>
     *     a#
     *     +- b#
     *     |  +- c*
     *     +- d*
     * </pre>
     */
    public void testNestedElementsMatchContext() {
        // Match against a.
        state.beforeStartElement();
        state.contextMatched();

        // Match against b.
        state.beforeStartElement();
        assertTrue("Child before grand child must know", state.hasDirectRelationship());
        state.contextMatched();

        // Match against c.
        state.beforeStartElement();
        assertTrue("Child of child must know", state.hasDirectRelationship());
        state.afterEndElement();

        // End b.
        assertTrue("Child after grand child must know", state.hasDirectRelationship());
        state.afterEndElement();

        // Match against d.
        state.beforeStartElement();
        assertTrue("Child must know", state.hasDirectRelationship());
        state.afterEndElement();

        // End a.
        state.afterEndElement();
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
