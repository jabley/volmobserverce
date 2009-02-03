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

package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import org.xml.sax.Attributes;

/**
 * A {@link DynamicElementRule} that does nothing but consume the start and
 * end element events.
 */
public final class NoopRule
        implements DynamicElementRule {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE = new NoopRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Initialise.
     *
     * <p>Private to prevent other instances being created.</p>
     */
    private NoopRule() {
    }

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) {
        return null;
    }

    // Javadoc inherited.
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object) {
    }
}
