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

/**
 * Defines the context as being those elements that are descendants of an
 * element that matched the context.
 *
 * <p>This keeps track of the depth at which the contextual matcher matched, if
 * it has matched at all. As soon as the depth rises above that position then
 * it does not match. Once it is within context it does not try and match the
 * context.</p>
 *
 * <p>Descendant matchers do not differentiate between direct and indirect
 * relationships. If an element has a direct relationship then it also has an
 * indirect relationship.</p>
 */
public class DescendantState implements CompositeState {

    /**
     * The depth at which the contextual matcher matched.
     */
    private int depthMatched;

    /**
     * The current depth.
     */
    private int depth;

    /**
     * A special value that is used to indicate that no contextual matcher
     * has matched.
     */
    private static final int INVALID_DEPTH = -2;

    /**
     * The initial depth.
     */
    private static final int INITIAL_DEPTH = -1;

    /**
     * Initialise.
     */
    public DescendantState() {
        //matched = new BitSet();
        depth = INITIAL_DEPTH;
        depthMatched = INVALID_DEPTH;
    }

    // Javadoc inherited.
    public boolean hasDirectRelationship() {
        return depthMatched != INVALID_DEPTH;
    }

    public boolean hasPotentialIndirectRelationship() {
        return hasDirectRelationship();
    }

    // Javadoc inherited.
    public boolean supportsIndirectRelationships() {
        return false;
    }

    // Javadoc inherited.
    public void contextMatched() {
        if (hasDirectRelationship()) {
            throw new IllegalStateException(
                    "Context must not be matched within context");
        }
        if (depth <= INITIAL_DEPTH) {
            throw new IllegalStateException(
                    "Context cannot match outside element");
        }
        depthMatched = depth;
    }

    // Javadoc inherited.
    public void beforeStartElement() {
        depth += 1;
    }

    // Javadoc inherited.
    public void afterEndElement() {
        if (depthMatched == depth) {
            depthMatched = INVALID_DEPTH;
        }
        if (depth <= INITIAL_DEPTH) {
            throw new IllegalStateException("Unbalanced depth change events");
        }
        depth -= 1;
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
