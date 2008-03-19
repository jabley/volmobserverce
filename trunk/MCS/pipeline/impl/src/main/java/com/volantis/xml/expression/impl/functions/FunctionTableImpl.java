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

package com.volantis.xml.expression.impl.functions;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;

import java.util.Iterator;
import java.util.Map;

/**
 * Implementation of {@link FunctionTable}.
 */
public class FunctionTableImpl
        implements FunctionTable {

    /**
     * The map from {@link ImmutableExpandedName} to {@link Function}.
     */
    private final Map functions;

    /**
     * The map of default prefix mappings, from {@link String} to
     * {@link String}.
     */
    private final Map mappings;

    /**
     * Initialise.
     *
     * @param functions The functions.
     * @param mappings  The prefix mappings.
     */
    public FunctionTableImpl(Map functions, Map mappings) {
        this.functions = functions;
        this.mappings = mappings;
    }

    // Javadoc inherited.
    public void registerFunctions(ExpressionContext context) {
        // Register the functions.
        for (Iterator i = functions.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            ImmutableExpandedName name = (ImmutableExpandedName) entry.getKey();
            Function function = (Function) entry.getValue();
            context.registerFunction(name, function);
        }

        // Register the default prefixes.
        NamespacePrefixTracker tracker = context.getNamespacePrefixTracker();
        for (Iterator i = mappings.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String prefix = (String) entry.getKey();
            String namespaceURI = (String) entry.getValue();
            tracker.startPrefixMapping(prefix, namespaceURI);
        }
    }
}
