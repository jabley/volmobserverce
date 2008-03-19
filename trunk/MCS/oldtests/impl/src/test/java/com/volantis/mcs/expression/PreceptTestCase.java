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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Simple test case
 */
public class PreceptTestCase extends TestCaseAbstract {
    public PreceptTestCase(String s) {
        super(s);
    }

    public void testLiterals() throws Exception {
        assertEquals("Literal not as",
                     "matchfirst",
                     Precept.MATCH_FIRST.toString());
        assertEquals("Literal not as",
                     "matchevery",
                     Precept.MATCH_EVERY.toString());
        assertSame("Should have MATCH_FIRST",
                   Precept.MATCH_FIRST,
                   Precept.literal("matchfirst"));
        assertSame("Should have MATCH_EVERY",
                   Precept.MATCH_EVERY,
                   Precept.literal("matchevery"));
        assertNull("Should not have found a literal but got " +
                   Precept.literal("null"),
                   Precept.literal("null"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Aug-03	1019/1	philws	VBM:2003080807 Provide MCS core extensions for handling the select markup element's state

 ===========================================================================
*/
