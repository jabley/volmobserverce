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

package com.volantis.mcs.expression.functions;

import com.volantis.shared.dependency.Freshness;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.dependency.ExpressionDependency;

/**
 * A dependency that depends solely upon a named value within the dependency.
 */
public abstract class ContextValueDependency
        extends ExpressionDependency {

    /**
     * The name of the value.
     */
    protected final String name;

    /**
     * The value at the time the dependency was created.
     */
    private final Object value;

    /**
     * Initialise.
     *
     * @param name  The name of the value.
     * @param value The value at the time this is called.
     */
    protected ContextValueDependency(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    // Javadoc inherited.
    protected Freshness freshness(ExpressionContext context) {
        Object value = getCurrentValue(context);

        if (ObjectHelper.equals(this.value, value)) {
            return Freshness.FRESH;
        } else {
            return Freshness.STALE;
        }
    }

    /**
     * Get the current value of the named value within the context.
     *
     * @param context The context.
     * @return The current value.
     */
    protected abstract Object getCurrentValue(ExpressionContext context);
}
