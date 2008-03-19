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

package com.volantis.mcs.protocols.corners;

import java.util.HashMap;

/**
 * HTML specific mcs-border-radius individual property rules for HTML derived protocols.
 */
public class CornersRuleSet extends HashMap {
    /**
     * Create a HashMap initialised with a transformation ruleset for mcs-border-radius
     * Elements not included in this map will be ignored
     */
    public CornersRuleSet() {
        put("div", CornersVisitor.INSERT);
        put("p", CornersVisitor.INSERT);
        put("ul", CornersVisitor.INSERT);
        put("ol", CornersVisitor.INSERT);
        put("li", CornersVisitor.INSERT);
        put("h1", CornersVisitor.INSERT);
        put("h2", CornersVisitor.INSERT);
        put("h3", CornersVisitor.INSERT);
        put("h4", CornersVisitor.INSERT);
        put("h5", CornersVisitor.INSERT);
        put("h6", CornersVisitor.INSERT);        
        put("span", CornersVisitor.INSERT);
        put("strong", CornersVisitor.INSERT);
        put("sub", CornersVisitor.INSERT);
        put("sup", CornersVisitor.INSERT);    
        put("a", CornersVisitor.INSERT);
        put("abbr", CornersVisitor.INSERT);
        put("address", CornersVisitor.INSERT);
        put("blockquote", CornersVisitor.INSERT);
        put("caption", CornersVisitor.INSERT);
        put("cite", CornersVisitor.INSERT);
        put("code", CornersVisitor.INSERT);        
    }
}
