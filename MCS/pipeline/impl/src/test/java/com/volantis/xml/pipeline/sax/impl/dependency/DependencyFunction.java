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

package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.dependency.Freshness;
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.time.Period;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.functions.AbstractFunction;

/**
 * A function that will add a dependency.
 */
public class DependencyFunction
        extends AbstractFunction {

    // Javadoc inherited.
    protected String getFunctionName() {
        return "dependency";
    }

    // Javadoc inherited.
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {

        Freshness freshness = Freshness.FRESH;
        Freshness revalidated = Freshness.FRESH;
        boolean cacheable = true;
        Period timeToLive = Period.INDEFINITELY;

        int argCount = arguments.length;
        String value;
        if (argCount > 0) {
            value = arguments[0].stringValue().asJavaString();
            freshness =
                    (Freshness) DependencyRule.STRING_2_FRESHNESS.get(value);
            if (freshness == null) {
                throw new ExpressionException("Unknown freshness value: " +
                        value);
            }
        }

        if (argCount > 1) {
            // Get the revalidated.
            value = arguments[1].stringValue().asJavaString();
            revalidated =
                    (Freshness) DependencyRule.STRING_2_FRESHNESS.get(value);
            if (revalidated == null) {
                throw new ExpressionException(
                        "Unknown revalidated value: " + value);
            }
        }

        if (argCount > 2) {
            // Get the cacheable.
            value = arguments[2].stringValue().asJavaString();
            if (value != null) {
                cacheable = "true".equalsIgnoreCase(value);
            }
        }

        if (argCount > 3) {
            // Get the time to live.
            value = arguments[3].stringValue().asJavaString();
            if (value != null && !value.equals("indefinitely")) {
                timeToLive = Period.inSeconds(Integer.parseInt(value));
            }
        }

        Cacheability cacheability =
                cacheable ? Cacheability.CACHEABLE : Cacheability.UNCACHEABLE;

        Dependency dependency = new TestDependency(cacheability, timeToLive,
                freshness, revalidated);
        DependencyContext dependencyContext = context.getDependencyContext();
        dependencyContext.addDependency(dependency);

        ExpressionFactory factory = context.getFactory();
        return factory.createStringValue("freshness = " + freshness + ", " +
                "revalidate = " + revalidated + ", " +
                "cacheable = " + cacheable + ", " +
                "time-to-live = " + timeToLive);
    }
}
