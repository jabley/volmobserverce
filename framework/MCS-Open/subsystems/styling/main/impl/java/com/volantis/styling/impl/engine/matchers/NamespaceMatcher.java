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


/**
 * Matches the namespace of the current element.
 */
public class NamespaceMatcher
        extends AbstractSimpleMatcher {

    private final String namespace;

    /**
     * Initialise.
     *
     * @param namespace The namespace against which the current element must
     * match, may not be null.
     */
    public NamespaceMatcher(String namespace) {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace cannot be null");
        }

        this.namespace = namespace;
    }

    // Javadoc inherited.
    public MatcherResult matchesWithinContext(MatcherContext context) {
        String contextElementNamespace = context.getNamespace();
        boolean matched = namespace.equals(contextElementNamespace);
        return matched ? MatcherResult.MATCHED : MatcherResult.FAILED;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        writer.print(namespace + "|");
    }

    public int hashCode() {
        return 217 + namespace.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            return namespace.equals(((NamespaceMatcher) obj).namespace);
        } else {
            return false;
        }
    }

    public String toString() {
        return namespace + "|";
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
