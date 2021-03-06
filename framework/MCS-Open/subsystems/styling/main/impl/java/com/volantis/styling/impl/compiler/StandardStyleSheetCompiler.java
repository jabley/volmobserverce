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

package com.volantis.styling.impl.compiler;

import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.impl.engine.matchers.DefaultNamespaceMatcher;
import com.volantis.styling.impl.sheet.StylesDelta;
import com.volantis.styling.impl.sheet.StandardStylesDelta;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.mcs.themes.PropertyValue;

public class StandardStyleSheetCompiler
        extends StyleSheetCompilerImpl {

    private static final MatcherFactory MATCHER_FACTORY =
            new MatcherFactoryImpl(DefaultNamespaceMatcher.getDefaultInstance());

    public StandardStyleSheetCompiler(CompilerConfiguration configuration) {
        super(configuration, MATCHER_FACTORY);
    }

    protected StylesDelta createStylesDelta(
            PseudoStyleEntity[] entities, PropertyValue[] values) {
        return new StandardStylesDelta(entities, values);
    }
}
