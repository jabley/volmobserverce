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

import com.volantis.styling.debug.DebugStylingWriter;

import java.util.List;

/**
 * Matcher that contains a sequence of other matchers.
 *
 * <p>Returns {@link MatcherResult#MATCHED} if all the contained matchers
 * match, otherwise it returns the return code of the first matcher not to
 * match.</p>
 */
public class MatcherSequence
        extends AbstractSimpleMatcher {

    /**
     * The sequence of {@link Matcher}s.
     */
    private final SimpleMatcher[] sequence;

    public MatcherSequence(List list) {
        if (list == null) {
            throw new IllegalArgumentException("list cannot be null");
        }

        sequence = new SimpleMatcher[list.size()];
        list.toArray(sequence);
    }

    // Javadoc inherited.
    public MatcherResult matchesWithinContext(MatcherContext context) {

        // Assume that it matched, that way an empty sequence will match.
        MatcherResult result = MatcherResult.MATCHED;

        // Iterate over until the end of the array is reached, or one of the
        // nested matchers failed to match.
        for (int i = 0; i < sequence.length && result == MatcherResult.MATCHED;
             i++) {

            SimpleMatcher matcher = sequence[i];
            result = matcher.matchesWithinContext(context);
        }

        return result;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        for (int i = 0; i < sequence.length; i++) {
            Matcher matcher = sequence[i];
            writer.print(matcher);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10583/1	pduffin	VBM:2005112205 Fixed issues with styling using nested child selectors

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
