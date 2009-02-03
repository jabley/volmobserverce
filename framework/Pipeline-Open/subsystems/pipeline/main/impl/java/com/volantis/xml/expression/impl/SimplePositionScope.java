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
package com.volantis.xml.expression.impl;

import com.volantis.xml.expression.PositionScope;

/**
 * This is a simple, generic implementation of the PositionScope interface.
 * Note that the class object for this class is used as the key for holding
 * position scopes in the {@link com.volantis.xml.expression.impl.SimpleExpressionContext}.
 */
public class SimplePositionScope implements PositionScope {
    /**
     * The position in the context. In order to conform with XPath this should
     * be pre-incremented as required.
     */
    private int position = 0;

    /**
     * The previous position context on the "stack".
     */
    private PositionScope previous;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param previous the previous position context on the "stack". May be
     *                 null
     */
    public SimplePositionScope(PositionScope previous) {
        this.previous = previous;
    }

    // javadoc inherited
    public int get() {
        return position;
    }

    // javadoc inherited
    public void increment() {
        position++;
    }

    /**
     * Returns the previous position context.
     *
     * @return the previous position context
     */
    public PositionScope getPrevious() {
        return previous;
    }
}
