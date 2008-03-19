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

/**
 * Internal methods on the expression context.
 *
 * <p>These methods are not part of the public API as it is not clear whether
 * that is necessary  or not, they are for "internal" use only at the
 * moment.</p>
 *
 * @volantis-api-include-in InternalAPI
 */
public interface InternalExpressionContext
        extends ExpressionContext {

    /**
     * Push a new scope onto the current frame.
     *
     * <p>The new scope does not hide variables in the outer scope, like
     * pushing a stack frame does but it can shadow them if a new variable
     * is declared with the name of a variable in the outer scope.</p>
     */
    void pushBlockScope();

    /**
     * Pop a block scope from the current frame.
     */
    void popBlockScope();

    /**
     * Pushes a new {@link PositionScope} into the expression context.
     */
    void pushPositionScope();

    /**
     * Pops the current {@link PositionScope} from the expression context.
     */
    void popPositionScope();

    /**
     * Returns the current position scope.
     *
     * @return the current position scope. Will not be null.
     */
    PositionScope getPositionScope();
}
