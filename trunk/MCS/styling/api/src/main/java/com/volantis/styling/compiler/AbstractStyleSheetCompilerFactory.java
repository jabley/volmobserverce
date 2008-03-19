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

package com.volantis.styling.compiler;

import com.volantis.styling.PseudoStyleEntitiesImpl;
import com.volantis.styling.StylingFactory;

/**
 * Base class for all {@link StyleSheetCompilerFactory}.
 */
public abstract class AbstractStyleSheetCompilerFactory
        implements StyleSheetCompilerFactory {

    /**
     * The factory to use to create style sheet compiler instances.
     */
    protected final StylingFactory stylingFactory;

    /**
     * The configuration.
     */
    protected final CompilerConfiguration configuration;

    /**
     * Initialise.
     *
     * @param source The source of the style sheet.
     * @param resolver
     */
    public AbstractStyleSheetCompilerFactory(
            final Source source, final FunctionResolver resolver) {

        stylingFactory = StylingFactory.getDefaultInstance();
        configuration = stylingFactory.createCompilerConfiguration();
        configuration.setSupportedPseudoEntities(new PseudoStyleEntitiesImpl());
        if (resolver != null) {
            configuration.addFunctionResolver(resolver);
        }
        configuration.setSource(source);
    }

    // Javadoc inherited.
    public StyleSheetCompiler createStyleSheetCompiler() {
        return stylingFactory.createStyleSheetCompiler(configuration);
    }
}
