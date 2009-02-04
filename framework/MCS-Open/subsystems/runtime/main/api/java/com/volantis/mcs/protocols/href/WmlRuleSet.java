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

import com.volantis.mcs.protocols.wml.WMLConstants;

import java.util.HashMap;

/**
 * WML specific HREF rule set which defines the HREF rules for WML
 * derived protocols.
 */
public class WmlRuleSet extends HashMap {

    /**
     * Create a HashMap initialised with a transformation ruleset
     */
    public WmlRuleSet() {
        put("a", HrefVisitor.IGNORE);
        put("abbr", HrefVisitor.INSIDE);
        put("anchor", HrefVisitor.IGNORE);
        put("br", HrefVisitor.IGNORE);
        put("em", HrefVisitor.OUTSIDE);
        put("head", HrefVisitor.INSIDE);
        put("strong", HrefVisitor.INSIDE);
        put(WMLConstants.BLOCH_ELEMENT, HrefVisitor.IGNORE);
        put("table", HrefVisitor.PUSH_DOWN);
        put("td", HrefVisitor.INSIDE);
        put("tr", HrefVisitor.PUSH_DOWN);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 22-Sep-05	9128/5	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 21-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
