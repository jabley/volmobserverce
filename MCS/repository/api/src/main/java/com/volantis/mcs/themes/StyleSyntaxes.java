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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

import com.volantis.shared.iteration.IterationAction;

/**
 * The style syntaxes which are explicitly supported.
 */
public class StyleSyntaxes {

    /**
     * The syntax definitions.
     */
    private static final StyleSyntaxDefinitions definitions;

    static {
        definitions = new StyleSyntaxDefinitions();
        definitions.addSyntax("color.triplets");
    }

    /**
     * The 'color.triplets' syntax.
     */
    public static final StyleSyntax COLOR_TRIPLETS =
            definitions.getSyntax("color.triplets");
    
    /**
     * Iterate over the shorthands.
     *
     * @param iteratee The object to invoke for each shorthand.
     * @return The result of the last call to the iteratee.
     */
    public IterationAction iterate(StyleSyntaxIteratee iteratee) {
        return definitions.iterate(iteratee);
    }

    /**
     * Get the syntax definitions.
     */
    public static StyleSyntaxDefinitions getDefinitions() {
        return definitions;
    }
}
