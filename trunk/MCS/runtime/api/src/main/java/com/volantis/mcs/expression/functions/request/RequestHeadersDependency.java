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

package com.volantis.mcs.expression.functions.request;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.ContextValueDependency;
import com.volantis.xml.expression.ExpressionContext;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * A dependency on a request header with multiple values.
 */
public class RequestHeadersDependency
        extends ContextValueDependency {

    /**
     * Initialise.
     *
     * @param name  The name of the header.
     * @param value The values of the header.
     */
    public RequestHeadersDependency(String name, List value) {
        super(name, value);
    }

    // Javadoc inherited.
    protected Object getCurrentValue(ExpressionContext context) {

        MarinerRequestContext requestContext = (MarinerRequestContext)
                context.getProperty(MarinerRequestContext.class);

        EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(requestContext);

        Enumeration enumeration = environmentContext.getHeaders(name);

        // The number of headers isn't known until the enumeration has
        // been traversed so build up a dynamic array of the header
        // values
        List headers = new ArrayList();

        // Add each header, as a string, to the list.
        while (enumeration.hasMoreElements()) {
            headers.add(enumeration.nextElement());
        }

        return headers;
    }
}
