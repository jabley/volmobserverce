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

package com.volantis.mcs.runtime.styling;

import com.volantis.mcs.runtime.layouts.MCSContainerInstanceFunction;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.FunctionResolver;
import com.volantis.styling.compiler.FunctionResolverBuilder;

/**
 * The MCS specific styling functions.
 */
public class StylingFunctions {

    private static final FunctionResolver resolver;
    static {
        StylingFactory factory = StylingFactory.getDefaultInstance();
        FunctionResolverBuilder builder =
                factory.createFunctionResolverBuilder();
        builder.addFunction("mcs-container-instance",
                new MCSContainerInstanceFunction());
        resolver = builder.getResolver();
    }

    public static final FunctionResolver getResolver() {
        return resolver;
    }
}
