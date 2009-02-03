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

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link FunctionTableBuilder}.
 */
public class FunctionTableBuilderImpl
        implements FunctionTableBuilder {

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
     */
    public FunctionTableBuilderImpl() {
        functions = new HashMap();
        mappings = new HashMap();
    }

    // Javadoc inherited.
    public void addFunction(ExpandedName name, Function function) {
        functions.put(name.getImmutableExpandedName(), function);
    }

    // Javadoc inherited.
    public void addDefaultPrefixMappings(String prefix, String namespaceURI) {
        mappings.put(prefix, namespaceURI);
    }

    // Javadoc inherited.
    public FunctionTable buildTable() {
        return new FunctionTableImpl(functions, mappings);
    }
}
