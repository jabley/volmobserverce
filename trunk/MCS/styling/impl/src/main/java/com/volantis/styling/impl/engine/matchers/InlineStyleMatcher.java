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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.styling.impl.engine.matchers;

import com.volantis.styling.debug.DebugStylingWriter;

/**
 * A {@link Matcher} that only matches the current element
 */
public class InlineStyleMatcher extends AbstractSimpleMatcher {

    private int elementId;

    public InlineStyleMatcher(int elementId) {
        this.elementId = elementId;
    }

    /**
     * A match is made if the elementId matches the elementId from the matcher
     * context.
     * @param context
     * @return true iff the elementId is equal to the context element id.
     */
    public MatcherResult matchesWithinContext(MatcherContext context) {
        MatcherResult result;
        if (context.getElementId() == elementId) {
            result = MatcherResult.MATCHED;
        } else {
            result = MatcherResult.FAILED;
        }
        return result;
    }

    //javadoc inherited
    public void debug(DebugStylingWriter writer) {
        writer.print("InlineStyleMatcher elementId: "+elementId);
    }
}
