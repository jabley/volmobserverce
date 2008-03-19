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

package com.volantis.styling.properties;

import java.util.ArrayList;
import java.util.Iterator;

import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.shared.iteration.IterationAction;
/**
 * Class contain list of property shorthand similar to CSS property or property defined for MCS, like mcs-border-radius 
 */
public class StyleShorthandAliases {
    
    private static final ArrayList shorthandAliases;
    static {        
        shorthandAliases = new ArrayList();
        
        ArrayList aliases = new ArrayList();
        aliases.add("border-radius");
        aliases.add("-moz-border-radius");
        aliases.add("-webkit-border-radius");        
        shorthandAliases.add(new StyleShorthandAlias(StyleShorthands.MCS_BORDER_RADIUS, aliases));
        
    }
    
    public static void iterate(StyleShorthandAliasIteratee iteratee) {
        IterationAction action = IterationAction.CONTINUE;
        Iterator i =  shorthandAliases.iterator();
        while (IterationAction.CONTINUE == action && i.hasNext()) {
            StyleShorthandAlias alias = (StyleShorthandAlias)i.next();
            action = iteratee.visit(alias);
        }        
    }
}
