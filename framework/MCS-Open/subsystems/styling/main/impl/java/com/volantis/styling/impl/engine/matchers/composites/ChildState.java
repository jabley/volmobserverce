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
 * Defines the context as being only those elements that are direct children of
 * an element that matched the context.
 *
 * <p></p>
 */
public class ChildState implements CompositeState {

    private BitSet matched;
    private int depth;
    private static int INITIAL_DEPTH = -1;

    public ChildState() {
        matched = new BitSet();
        depth = INITIAL_DEPTH;
    }

    // Javadoc inherited.
    public boolean hasDirectRelationship() {
        return depth > INITIAL_DEPTH + 1 && matched.get(depth - 1);
    }

    public boolean hasPotentialIndirectRelationship() {
        // todo when can use at least 1.4 then use isEmpty instead.
        return matched.length() > 0;
    }

    // Javadoc inherited.
    public boolean supportsIndirectRelationships() {
        return true;
    }

    // Javadoc inherited.
    public void contextMatched() {
        if (depth <= INITIAL_DEPTH) {
            throw new IllegalStateException(
                    "Context cannot match outside element");
        }
        matched.set(depth);
    }

    // Javadoc inherited.
    public void beforeStartElement() {
        depth += 1;
    }

    // Javadoc inherited.
    public void afterEndElement() {
        matched.clear(depth);//todo move this below the test.
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

 06-Dec-05	10613/3	pduffin	VBM:2005112205 Porting forward changes from MCS 3.5

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
