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

package com.volantis.styling.impl.engine.matchers.composites;

import java.util.BitSet;

/**
 * Defines the context as being those elements that are siblings of and
 * immediately follow after the element that matched the context.
 *
 * <p>The context does not include children of an elements siblings so it is
 * necessary to record for each element that has been started but not yet ended
 * whether any of its children matched the contextual matcher. This is done by
 * using a bit set that is indexed by the current depth. Only the bit at the
 * current depth affects whether an element is in context.</p>
 *
 * <p>As the context only contains the following element it is necessary to
 * check the contextual matcher on every element even within context.</p>
 */
public class ImmediatelyPrecedingSiblingState
        implements CompositeState {

    /**
     * Indicates whether the contextual matcher matched at each level of the
     * document.
     *
     * <p>It is indexed by the current depth.</p>
     */
    private BitSet matched;

    /**
     *
     */
    private boolean savedMatched;

    /**
     * The current depth.
     */
    private int depth;

    /**
     * Initialise.
     */
    public ImmediatelyPrecedingSiblingState() {
        matched = new BitSet();
        depth = 0;
    }

    // Javadoc inherited.
    public boolean hasDirectRelationship() {
        return savedMatched;
    }

    // Javadoc inherited.
    public boolean hasPotentialIndirectRelationship() {
        return true;
    }

    // Javadoc inherited.
    public boolean supportsIndirectRelationships() {
        return true;
    }

    // Javadoc inherited.
    public void contextMatched() {
        if (depth <= 0) {
            throw new IllegalStateException(
                    "Context cannot match outside element");
        }
        matched.set(depth - 1);
    }

    // Javadoc inherited.
    public void beforeStartElement() {
        savedMatched = matched.get(depth);
        matched.clear(depth);

        depth += 1;
    }

    // Javadoc inherited.
    public void afterEndElement() {
        matched.clear(depth);
        depth -= 1;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Added support and tests for immediately preceding sibling selectors and multiple pseudo element selectors in the styling engine

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
