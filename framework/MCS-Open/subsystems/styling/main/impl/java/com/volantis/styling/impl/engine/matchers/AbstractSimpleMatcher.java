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

package com.volantis.styling.impl.engine.matchers;

/**
 * Base for all simple, i.e. not composite, matchers.
 *
 * <p>Checks to make sure that the matcher is within context before performing
 * the matcher specific test.</p>
 */
public abstract class AbstractSimpleMatcher
        implements SimpleMatcher {

    public final MatcherResult matches(MatcherContext context) {
//        if (context.hasDirectRelationship()) {
            return matchesWithinContext(context);
//        } else {
//            return MatcherResult.FAILED;
//        }
    }

    public String getMatchableElement() {
        return null;
    }

    public String[] getMatchableClasses() {
        return null;
    }

    public boolean isMatchAny() {
        return true;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 ===========================================================================
*/
