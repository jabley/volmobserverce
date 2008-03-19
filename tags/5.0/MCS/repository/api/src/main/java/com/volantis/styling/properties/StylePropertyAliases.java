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

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.shared.iteration.IterationAction;

/**
 * List of property similar to CSS property or property defined for MCS 
 */

public class StylePropertyAliases {
    
    private static final ArrayList propertyAliases;
    
    static {        
        propertyAliases = new ArrayList();
        
        ArrayList aliases = new ArrayList();
        // Similarities in order of preference
        aliases.add("border-top-left-radius");
        aliases.add("-moz-border-radius-topleft");
        aliases.add("-webkit-border-top-left-radius");        
        propertyAliases.add(new StylePropertyAlias(StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS, aliases));
        
        aliases = new ArrayList();
        // Similarities in order of preference
        aliases.add("border-top-right-radius");
        aliases.add("-moz-border-radius-topright");
        aliases.add("-webkit-border-top-right-radius");        
        propertyAliases.add(new StylePropertyAlias(StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS, aliases));    

        aliases = new ArrayList();
        // Similarities in order of preference
        aliases.add("border-bottom-right-radius");
        aliases.add("-moz-border-radius-bottomright");
        aliases.add("-webkit-border-bottom-right-radius");        
        propertyAliases.add(new StylePropertyAlias(StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS, aliases));
        
        aliases = new ArrayList();
        // Similarities in order of preference
        aliases.add("border-bottom-left-radius");
        aliases.add("-moz-border-radius-bottomleft");
        aliases.add("-webkit-border-bottom-left-radius");        
        propertyAliases.add(new StylePropertyAlias(StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS, aliases));            
    }
    
    public static void iterate(StylePropertyAliasIteratee iteratee) {
        IterationAction action = IterationAction.CONTINUE;
        Iterator i =  propertyAliases.iterator();
        while (IterationAction.CONTINUE == action && i.hasNext()) {
            StylePropertyAlias alias = (StylePropertyAlias)i.next();
            action = iteratee.visit(alias);
        }        
    }
}
