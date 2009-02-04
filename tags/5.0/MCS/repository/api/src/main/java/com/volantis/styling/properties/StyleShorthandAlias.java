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

import java.util.Collections;
import java.util.List;

import com.volantis.mcs.themes.StyleShorthand;

/**
 *  Contains list of similar shorthand properties and matched shorthand property 
 */
public class StyleShorthandAlias {
    
    private StyleShorthand shorthand;
    private List /*of String */ aliases;
    
    public StyleShorthandAlias(StyleShorthand shorthand, List aliases) {
        this.shorthand = shorthand;
        this.aliases = Collections.unmodifiableList(aliases);
    }

    public List /*of String */ getAliasShorthands() {
        return this.aliases;
    }

    public StyleShorthand getShorthand() {
        return this.shorthand;
    }
}