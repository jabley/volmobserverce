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

package com.volantis.xml.expression;

import com.volantis.xml.namespace.ExpandedName;

/**
 * Internal methods on the expression scope.
 *
 * <p>These methods are not part of the public API as it is not clear whether
 * that is necessary  or not, they are for internal use only at the moment.</p>
 */
public interface InternalExpressionScope
        extends ExpressionScope {

    /**
     * Declare a new variable in this scope.
     *
     * <p>The variable is not initialised and any attempt to get its value
     * before it is explicitly set will result in an exception being
     * thrown.</p>
     *
     * @param name The name of the variable.
     * @return The variable.
     */
    public Variable declareVariable(ExpandedName name);

}
