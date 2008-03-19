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

import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.styling.debug.DebugStylingWriter;

/**
 * Matches the default namespace.
 *
 * <p>At the moment there are two default namespaces, CDM and XHTML 2 that are
 * hard coded in here. They may be moved out into the configuration at some
 * point in future.</p>
 */
public class DefaultNamespaceMatcher
        extends AbstractSimpleMatcher {

    /**
     * The default instance.
     */
    private static final SimpleMatcher DEFAULT_INSTANCE =
            new DefaultNamespaceMatcher();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static SimpleMatcher getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    // Javadoc inherited.
    public MatcherResult matchesWithinContext(MatcherContext context) {
        String contextElementNamespace = context.getNamespace();
        return contextElementNamespace.equals(XDIMESchemata.CDM_NAMESPACE) ||
                contextElementNamespace.equals(XDIMESchemata.XHTML2_NAMESPACE)
                ? MatcherResult.MATCHED : MatcherResult.FAILED;
    }

    // Javadoc inherited.
    public void debug(DebugStylingWriter writer) {
        // Nothing to write.
    }
}
