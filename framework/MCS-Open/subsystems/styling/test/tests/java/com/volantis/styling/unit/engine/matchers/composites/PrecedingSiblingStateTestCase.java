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
import com.volantis.styling.impl.engine.matchers.composites.PrecedingSiblingState;

/**
 * Test the {@link PrecedingSiblingState}.
 */
public class PrecedingSiblingStateTestCase
        extends CompositeStateTestCaseAbstract {

    protected CompositeState createState() {
        return new PrecedingSiblingState();
    }

    /**
     * Test that following nodes are within context.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then only the elements marked with a <code>*</code>
     * are within context.</p>
     *
     * <pre>
     *     a#
     *     b*
     *     c*
     *     d*
     * </pre>
     */
    public void testFollowingSiblingsWithinContext() {
        state.beforeStartElement();
        state.contextMatched();
        state.afterEndElement();
        for (int i = 0; i < 10; i += 1) {
            state.beforeStartElement();
            assertTrue("Following sibling matches",
                       state.hasDirectRelationship());
            state.afterEndElement();
        }
    }

    /**
     * Test that children/nephews/nieces are not within context.
     *
     * <p>e.g. if the element marked with a <code>#</code> matches the
     * contextual selector then only the elements marked with a <code>*</code>
     * are within context.</p>
     *
     * <pre>
     *     a#
     *     +- b
     *     c*
     *     +- d
     * </pre>
     */
    public void testNephewsNotWithinContext() {
        // Context
        state.beforeStartElement();
        state.contextMatched();

        // Child
        state.beforeStartElement();
        assertFalse("Child is not within context", state.hasDirectRelationship());
        state.afterEndElement();

        // Close context
        state.afterEndElement();

        // Sibling
        state.beforeStartElement();
        assertTrue("Sibling is within context", state.hasDirectRelationship());

        // Nephew
        state.beforeStartElement();
        assertFalse("Nephew is not within context", state.hasDirectRelationship());
        state.afterEndElement();

        // Close sibling
        state.afterEndElement();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
