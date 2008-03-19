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

package com.volantis.xml.expression.dependency;

import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.DependencyImpl;
import com.volantis.shared.dependency.Freshness;
import com.volantis.xml.expression.ExpressionContext;

/**
 * Base implementation for all dependencies created within expressions.
 */
public class ExpressionDependency
        extends DependencyImpl {

    /**
     * Extract the expression context from the dependency context.
     *
     * @param context The dependency context.
     * @return The expression context.
     */
    private ExpressionContext getExpressionContext(DependencyContext context) {
        return (ExpressionContext) context.getProperty(ExpressionContext.class);
    }

    /**
     * Extracts {@link ExpressionContext} from {@link DependencyContext} and
     * invokes {@link #freshness(ExpressionContext)}.
     *
     * @param context The {@link DependencyContext}.
     * @return The result of calling {@link #freshness(ExpressionContext)}.
     */
    public final Freshness freshness(DependencyContext context) {
        return freshness(getExpressionContext(context));
    }

    /**
     * Default implementation.
     *
     * <p>If this is overridden to return {@link Freshness#REVALIDATE} then the
     * {@link #revalidate(ExpressionContext)} method must also be
     * overridden.</p>
     *
     * @param context The expression context within which this dependency is
     *                being checked.
     * @return {@link Freshness#FRESH}.
     */
    protected Freshness freshness(ExpressionContext context) {
        return Freshness.FRESH;
    }

    /**
     * Extracts {@link ExpressionContext} from {@link DependencyContext} and
     * invokes {@link #revalidate(ExpressionContext)}.
     *
     * @param context The {@link DependencyContext}.
     * @return The result of calling {@link #revalidate(ExpressionContext)}.
     */
    public final Freshness revalidate(DependencyContext context) {
        return revalidate(getExpressionContext(context));
    }

    /**
     * Default implementation.
     *
     * @param context The expression context within which this dependency is
     *                being checked.
     * @throws IllegalStateException As this should never be called unless
     *                               {@link #freshness(ExpressionContext)}
     *                               returns {@link Freshness#REVALIDATE}.
     */
    protected Freshness revalidate(ExpressionContext context) {
        throw new IllegalStateException(
                "This method must be overridden by " + getClass() +
                " as freshness() returned Freshness.REVALIDATE");
    }
}
