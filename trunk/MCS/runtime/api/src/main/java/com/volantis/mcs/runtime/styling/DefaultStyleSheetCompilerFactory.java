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

import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.compiler.AbstractStyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetSource;

/**
 * A factory for creating style sheet compilers for default style sheets (e.g
 * the system defaults and the protocol defaults).
 */
public class DefaultStyleSheetCompilerFactory
        extends AbstractStyleSheetCompilerFactory {

    /**
     * The default instance.
     */
    private static StyleSheetCompilerFactory instance =
            new DefaultStyleSheetCompilerFactory();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static StyleSheetCompilerFactory getDefaultInstance() {
        return instance;
    }

    /**
     * Initialise.
     */
    private DefaultStyleSheetCompilerFactory() {
        super(StyleSheetSource.DEFAULT, StylingFunctions.getResolver());
    }
}
