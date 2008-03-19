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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.net.http.headers;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for HeaderUtils.
 */
public class HeaderUtilsTestCase extends TestCaseAbstract {
    /**
     * Check that the character set retrieved from the content type matches
     * the specified expected character set.
     *
     * @param failureMessage The assertion message to use if the check fails.
     * @param contentType The content type that may contain a character set.
     * @param expectedCharSet The expected character set, should be null if
     * the supplied content type contains no character set.
     */
    private void checkGetCharSetFromContentType(String failureMessage,
                                               String contentType,
                                               String expectedCharSet) {

        String actualCharSet = HeaderUtils
                .getCharSetFromContentType(contentType);

        assertEquals(failureMessage, expectedCharSet, actualCharSet);
    }

    /**
     * Test that separateHeaderMultiValues works as expected.
     */
    public void testSeparateHeaderMultiValues() {
        String value = "value1, value2, value3";
        String values [] = HeaderUtils.separateHeaderMultiValues(value);
        assertEquals("Incorrect number of header values", 3, values.length);
        assertEquals("Value 1 incorrect", "value1", values[0]);
        assertEquals("Value 2 incorrect", "value2", values[1]);
        assertEquals("Value 3 incorrect", "value3", values[2]);

        value = null;
        values = HeaderUtils.separateHeaderMultiValues(value);
        assertEquals("Incorrect number of header values", 0, values.length);

        value = "value";
        values = HeaderUtils.separateHeaderMultiValues(value);
        assertEquals("Incorrect number of header values", 1, values.length);
    }
    /**
     * Test that the
     * {@link com.volantis.shared.net.http.headers.HeaderUtils#getCharSetFromContentType} works.
     */
    public void testGetCharSetFromContentType() {

        // Check that it works when there are no parameters at all.
        checkGetCharSetFromContentType("No charset expected",
                "text/html", null);

        // Check that it works when there is no charset parameter.
        checkGetCharSetFromContentType("No charset expected",
                "text/html;fred=abc", null);

        // Check that it works when there is only a charset parameter.
        checkGetCharSetFromContentType("Unexpected charset value",
                "text/html;charset=iso-8859-1",
                "iso-8859-1");

        // Check that it works when there is only a charset parameter with lots
        // of white noise..
        checkGetCharSetFromContentType("Unexpected charset value",
                "text/html;     charset=iso-8859-1    ",
                "iso-8859-1");

        // Check that it works when there is a charset parameter whose value
        // is upper case and preserves the case.
        checkGetCharSetFromContentType("Unexpected charset value",
                "text/html;charset=ISO-8859-1",
                "ISO-8859-1");

        // Check that it works when there are a number of parameters before
        // and after the charset parameter.
        checkGetCharSetFromContentType("Unexpected charset value",
                "text/html;foo=\"hello world\";charset=utf-8;bar=xyz",
                "utf-8");

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Apr-05	7679/1	allan	VBM:2005041320 SmartClient Packager - minimal testing

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 ===========================================================================
*/
