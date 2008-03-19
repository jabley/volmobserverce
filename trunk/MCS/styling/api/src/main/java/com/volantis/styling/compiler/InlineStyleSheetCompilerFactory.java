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
package com.volantis.styling.compiler;

import com.volantis.styling.StylingFactory;

/**
 * A factory for creating style sheet compilers for style sheets generated from
 * inline style values.
 */
public class InlineStyleSheetCompilerFactory
        extends AbstractStyleSheetCompilerFactory {

    /**
     * Create the styling factor and the configuration.
     * The configuration uses a specialised SpecificityCalculator for the inline
     * style values
     * @param resolver
     */
    public InlineStyleSheetCompilerFactory(final FunctionResolver resolver) {
        this(StyleSheetSource.THEME, resolver);
    }    

    /**
     * Create the styling factor and the configuration.
     * The configuration uses a specialised SpecificityCalculator for the inline
     * style values
     * @param resolver
     */
    public InlineStyleSheetCompilerFactory(
            Source source, final FunctionResolver resolver) {
        super(source, resolver);

        configuration.setSpecificityCalculator(
                StylingFactory.getDefaultInstance().
                        createInlineSpecificityCalculator());
    }

    public StyleSheetCompiler createStyleSheetCompiler() {
        return stylingFactory.createStyleSheetCompiler(configuration);
    }
}
