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
package com.volantis.mcs.protocols.href;

import java.util.HashMap;

/**
 * HTML specific HREF rules for HTML derived protocols.
 */
public class HtmlRuleSet extends HashMap {

    /**
     * Create a HashMap initialised with a transformation ruleset
     */
    public HtmlRuleSet() {
        put("a", HrefVisitor.IGNORE);
        put("abbr", HrefVisitor.INSIDE);
        put("address", HrefVisitor.INSIDE);
        put("blockquote", HrefVisitor.PUSH_DOWN);
        put("caption", HrefVisitor.INSIDE);
        put("cite", HrefVisitor.OUTSIDE);
        put("code", HrefVisitor.OUTSIDE);
        put("dd", HrefVisitor.INSIDE);
        put("dfn", HrefVisitor.OUTSIDE);
        put("div", HrefVisitor.INSIDE);
        put("dl", HrefVisitor.PUSH_DOWN);
        put("dt", HrefVisitor.INSIDE);
        put("em", HrefVisitor.OUTSIDE);
        put("h1", HrefVisitor.INSIDE);
        put("h2", HrefVisitor.INSIDE);
        put("h3", HrefVisitor.INSIDE);
        put("h4", HrefVisitor.INSIDE);
        put("h5", HrefVisitor.INSIDE);
        put("h6", HrefVisitor.INSIDE);
        put("kbd", HrefVisitor.INSIDE);
        put("li", HrefVisitor.INSIDE);
        put("object", HrefVisitor.OUTSIDE);
        put("ol", HrefVisitor.IGNORE);
        put("p", HrefVisitor.INSIDE);
        put("pre", HrefVisitor.INSIDE);
        put("q", HrefVisitor.INSIDE);
        put("samp", HrefVisitor.INSIDE);
        put("span", HrefVisitor.INSIDE);
        put("strong", HrefVisitor.INSIDE);
        put("sub", HrefVisitor.INSIDE);
        put("sup", HrefVisitor.INSIDE);
        put("table", HrefVisitor.PUSH_DOWN);
        put("td", HrefVisitor.INSIDE);
        put("th", HrefVisitor.INSIDE);
        put("tr", HrefVisitor.PUSH_DOWN);
        put("ul", HrefVisitor.IGNORE);
        put("var", HrefVisitor.INSIDE);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
