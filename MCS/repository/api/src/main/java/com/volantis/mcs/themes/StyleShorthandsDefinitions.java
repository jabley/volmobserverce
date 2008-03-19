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
package com.volantis.mcs.themes;

import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A collection of {@link StyleShorthand}.
 */
public class StyleShorthandsDefinitions {

    /**
     * The map from name to {@link StyleShorthand}.
     */
    private final Map definitionMap = new HashMap();

    /**
     * The list of {@link StyleShorthand}.
     */
    private final List defininitionList = new ArrayList();

    /**
     * Add a shorthand.
     *
     * @param name               The name of the shorthand.
     * @param standardProperties The properties.
     */
    public void addShorthand(String name, StyleProperty[] standardProperties) {
        // Shorthands should not be case sensitive.
        name = name.toLowerCase();
        final StyleShorthand shorthand = new StyleShorthand(name,
                standardProperties);
        definitionMap.put(name, shorthand);
        defininitionList.add(shorthand);
    }

    /**
     * Get the shorthand by name,
     *
     * @param name The name of the shorthand.
     * @return The {@link StyleShorthand}.
     * @throws IllegalArgumentException If the shorthand was not found.
     */
    public StyleShorthand getShorthand(String name) {
        // Shorthands should not be case sensitive.
        name = name.toLowerCase();

        final StyleShorthand styleShorthand = (StyleShorthand)
                definitionMap.get(name);
        // protect us against illegal shorthand names up front.
        if (styleShorthand == null) {
            throw new IllegalArgumentException(
                    "Unknown shorthand '" + name + "'");
        }
        return styleShorthand;
    }

    /**
     * Iterate over the shorthands.
     *
     * @param iteratee The object to invoke for each shorthand.
     * @return The result of the last call to the iteratee.
     */
    public IterationAction iterate(StyleShorthandIteratee iteratee) {
        final int size = defininitionList.size();
        IterationAction action = IterationAction.CONTINUE;
        for (int i = 0; action == IterationAction.CONTINUE &&
                i < size; i++) {
            StyleShorthand styleShorthand = (StyleShorthand)
                    defininitionList.get(i);
            iteratee.iterate(styleShorthand);
        }
        return action;
    }

    /**
     * Get an iterator over the {@link StyleShorthand}.
     *
     * @return An iterator.
     */
    public Iterator iterator() {
        return defininitionList.iterator();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 ===========================================================================
*/
