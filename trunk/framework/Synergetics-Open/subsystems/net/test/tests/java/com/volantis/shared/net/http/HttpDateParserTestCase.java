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
package com.volantis.shared.net.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Date;

/**
 * Test case for the {@link HttpDateParser} class
 */
public class HttpDateParserTestCase extends TestCaseAbstract {
    /**
     * Tests that a date in the RFC1123 format is parsed correctly
     * @throws Exception if an error occurs
     */
    public void testRFC1123DateStr() throws Exception {
        Date date = HttpDateParser.parse("Sun, 06 Nov 1994 08:49:37 GMT");
        assertNotNull("Expected a non null date", date);    
    }

    /**
     * Tests that a date in the RFC1036 format is parsed correctly
     * @throws Exception if an error occurs
     */
    public void testRFC1036DateStr() throws Exception {
        // not teh HttpDateParser treats two digit years as belonging in the
        // 21 st centuary
        Date date = HttpDateParser.parse("Saturday, 06-Nov-94 08:49:37 GMT");
        assertNotNull("Expected a non null date", date);
    }

    /**
     * Tests that a date in the ANSI C's asctime() format is parsed correctly
     * @throws Exception if an error occurs
     */
    public void testANSICDateStr() throws Exception {
        Date date = HttpDateParser.parse("Sun Nov  6 08:49:37 1994");
        assertNotNull("Expected a non null date", date);
    }

    /**
     * Tests that a date in a format other than those supported is not parsed
     * @throws Exception if an error occurs
     */
    public void testOtherDateStr() throws Exception {
        Date date = HttpDateParser.parse("Nov 06 08:49:37 1994 GMT");
        assertNull("Expected a null date", date);
    }
}


