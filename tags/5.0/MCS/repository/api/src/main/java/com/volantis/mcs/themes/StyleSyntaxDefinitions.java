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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A collection of style syntax definitions.
 */
public class StyleSyntaxDefinitions {

    /**
     * The map from name to {@link StyleSyntax}.
     */
    private final Map definitionMap = new HashMap();

    /**
     * The list of {@link StyleShorthand}.
     */
    private final List definitionList = new ArrayList();

    /**
     * Add a syntax.
     *
     * @param name              of the syntax
     */
    public void addSyntax(String name) {
        // Syntaxes should not be case sensitive.
        name = name.toLowerCase();
        final StyleSyntax syntax = new StyleSyntax(name);
        definitionMap.put(name, syntax);
        definitionList.add(syntax);
    }

    /**
     * Get the syntax by name,
     *
     * @param name of the syntax to return
     * @return The {@link StyleSyntax} with the specified name
     * @throws IllegalArgumentException if no syntax was found with that name.
     */
    public StyleSyntax getSyntax(String name) {
        // Syntaxes should not be case sensitive.
        name = name.toLowerCase();
        final StyleSyntax styleSyntax = (StyleSyntax)
                definitionMap.get(name);
        // protect us against illegal StyleSyntax names up front.
        if (styleSyntax == null) {
            throw new IllegalArgumentException(
                    "Unknown StyleSyntax '" + name + "'");
        }
        return styleSyntax;
    }

    /**
     * Iterate over the syntaxes.
     *
     * @param iteratee The object to invoke for each syntax.
     * @return The result of the last call to the iteratee.
     */
    public IterationAction iterate(StyleSyntaxIteratee iteratee) {
        final int size = definitionList.size();
        IterationAction action = IterationAction.CONTINUE;
        for (int i = 0; action == IterationAction.CONTINUE &&
                i < size; i++) {
            StyleSyntax styleSyntax = (StyleSyntax)definitionList.get(i);
            iteratee.iterate(styleSyntax);
        }
        return action;
    }

    /**
     * Get an iterator over the {@link StyleSyntax}.
     *
     * @return An iterator.
     */
    public Iterator iterator() {
        return definitionList.iterator();
    }
}
