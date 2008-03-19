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

package com.volantis.xml.expression.functions;

import com.volantis.xml.expression.Function;
import com.volantis.xml.namespace.ExpandedName;

/**
 * Builder for a {@link FunctionTable}.
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface FunctionTableBuilder {

    /**
     * Add the function.
     *
     * @param name     The name of the function.
     * @param function The function.
     */
    void addFunction(ExpandedName name, Function function);

    /**
     * Add a default prefix mapping that is visible in all pipeline, at least
     * for the purpose of resolving functions and variables.
     *
     * @param prefix       The default prefix.
     * @param namespaceURI The namespace URI.
     */
    void addDefaultPrefixMappings(String prefix, String namespaceURI);

    /**
     * Build and return the table.
     *
     * @return The table.
     */
    FunctionTable buildTable();
}
