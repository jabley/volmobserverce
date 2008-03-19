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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/ServletRequestHeadersTestCase.java,v 1.3 2003/03/11 12:54:34 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Byron           VBM:2003021204 - Created
 * 14-Feb-03    Byron           VBM:2003021204 - Updated the test cases (added
 *                              more and split the parser testRemoteQuotes into
 *                              several independent methods).
 * 11-Mar-03    Phil W-S        VBM:2003031102 - Make testParseAcceptMimeTypes
 *                              test for values with leading and trailing
 *                              whitespace (as well as the parameters).
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

/**
 * This class tests the servlet request headers methods.
 * @author Byron Wild
 */
public class HTTPHeadersHelperTestCase extends TestCaseAbstract {

    private static final String mimeType = "wap.wml";

    /**
     * Test that an empty header causes hasAcceptMimeType to return false.
     */
    public void testHasAcceptMimeTypeWithEmptyHeader() {

        MockHttpHeaders headers = new MockHttpHeaders();
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertFalse("Empty headers should return false", hasMimeType);
    }

    /**
     * Test that a single, non matching header causes hasAcceptMimeType to
     * return false.
     */
    public void testHasAcceptMimeTypeWithNonMatchingHeader() {
        MockHttpHeaders headers = new MockHttpHeaders();
        headers.addHeader("accept", "text/html");
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertFalse("These headers should not contain the mime type: " +
                mimeType, hasMimeType);
    }

    /**
     * Test that a single, exactly matching header causes hasAcceptMimeType
     * to return true.
     */
    public void testHasAcceptMimeTypeWithExactlyMatchingSingleHeader() {
        MockHttpHeaders headers = new MockHttpHeaders();
        headers.addHeader("accept", mimeType);
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertTrue("These headers should contain the mime type: " +
                mimeType, hasMimeType);
    }

    /**
     * Test that multiple values in a single accept header, where one of the
     * values is an exact match to the header required, causes
     * hasAcceptMimeType to return true.
     */
    public void testHasAcceptMimeTypeWithMatchingSingleHeaderWithMultipleValues() {
        MockHttpHeaders headers = new MockHttpHeaders();
        headers.addHeader("accept", "text/html, " + mimeType);
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertTrue("These headers should contain the mime type: " +
                mimeType, hasMimeType);
    }

    /**
     * Test that multiple values in a single accept header, where none of the
     * values is an exact match to the header required, causes
     * hasAcceptMimeType to return true.
     */
    public void testHasAcceptMimeTypeWithNonMatchingSingleHeaderWithMultipleValues() {
        MockHttpHeaders headers = new MockHttpHeaders();
        headers.addHeader("accept", "text/html, text/xml");
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertFalse("These headers should not contain the mime type: " +
                mimeType, hasMimeType);
    }

    /**
     * Test that multiple accept headers where one is an exact match to the
     * header required causes hasAcceptMimeType to return true.
     */
    public void testHasAcceptMimeTypeWithMatchingMultipleHeader() {
        MockHttpHeaders headers = new MockHttpHeaders();
        headers.addHeader("accept", "text/html");
        headers.addHeader("accept", mimeType);
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertTrue("These headers should contain the mime type: " +
                mimeType, hasMimeType);
    }

    /**
     * Test that multiple accept headers where none is an exact match to the
     * header required causes hasAcceptMimeType to return false.
     */
    public void testHasAcceptMimeTypeWithNonMatchingMultipleHeader() {
        MockHttpHeaders headers = new MockHttpHeaders();
        headers.addHeader("accept", "text/html");
        headers.addHeader("accept", "text/xml");
        boolean hasMimeType =
                HTTPHeadersHelper.hasAcceptMimeType(headers, mimeType);
        assertFalse("These headers should not contain the mime type: " +
                mimeType, hasMimeType);
    }

    /**
     *
     * @throws java.lang.Exception
     */
    public void testParseAcceptMimeTypes() throws Exception {
        ArrayList list = new ArrayList();

        String result[] = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNull("Null list should return no results", result);

        list.add("");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Empty list should return no results", result);

        list.clear();
        list.add("text/xml");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 1 result", 1, result.length);
        assertEquals("Result should match", "text/xml", result[0]);

        list.clear();
        list.add("text/xml,application/xml");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 2 results", 2, result.length);
        assertEquals("Result should match", "text/xml", result[0]);
        assertEquals("Result should match", "application/xml", result[1]);

        list.clear();
        list.add("text/xml;q=0.9");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 1 result", 1, result.length);
        assertEquals("Result should match", "text/xml", result[0]);

        list.clear();
        list.add("text/xml;q=0.9,application/xhtml+xml");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 2 results", 2, result.length);
        assertEquals("Result should match", "text/xml", result[0]);
        assertEquals("Result should match", "application/xhtml+xml", result[1]);

        list.clear();
        list.add("text/vnd.wap.wml");
        list.add("text/vnd.wap.wmlscript");
        list.add("application/vnd.wap.wmlc, " +
                 "application/xhtml+xml, " +
                 "text/plain, " +
                 "text/css, " +
                 "application/vnd.wap.wmlscriptc, " +
                 "image/vnd.wap.wbmp, " +
                 "image/bmp, " +
                 "image/gif, " +
                 "image/jpeg, " +
                 "image/png, " +
                 "audio/amr, " +
                 "audio/iMelody, " +
                 "audio/midi, " +
                 "audio/mid, " +
                 "application/x-pmd, " +
                 "application/vnd.wap.multipart.mixed, " +
                 "application/vnd.wap.multipart.alternative, " +
                 "application/vnd.wap.mms-message");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 20 results", 20, result.length);
        assertEquals("Result[0] not as",
                     "text/vnd.wap.wml",
                     result[0]);
        assertEquals("Result[3] not as",
                     "application/xhtml+xml",
                     result[3]);

        list.clear();
        list.add("text/vnd.wap.wml,");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 1 result", 1, result.length);
        assertEquals("Result[0] not as",
                     "text/vnd.wap.wml",
                     result[0]);

        list.clear();
        list.add("text/vnd.wap.wml ;q=0.7,");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 1 result", 1, result.length);
        assertEquals("Result[0] not as",
                     "text/vnd.wap.wml",
                     result[0]);

        list.clear();
        list.add("text/vnd.wap.wml, ;, text/plain;  ,  ");
        result = HTTPHeadersHelper.parseAcceptMimeTypes(list);
        assertNotNull("Should return results", result);
        assertEquals("Should return 2 results", 2, result.length);
        assertEquals("Result[0] not as",
                     "text/vnd.wap.wml",
                     result[0]);
        assertEquals("Result[1] not as",
                     "text/plain",
                     result[1]);
    }

    /**
     * Test removing of quotes
     * @throws java.lang.Exception
     */
    public void testRemoveQuotesNoQuotes() throws Exception {
        String input = "Test";
        assertEquals("Quote should be removed", "Test",
                HTTPHeadersHelper.removeQuotes("Test"));
    }

    /**
     * Test removing of quotes
     * @throws java.lang.Exception
     */
    public void testRemoveQuotesWithQuotes() throws Exception {
        String input = "Tes\"in quotes\"t";
        assertEquals("Quotes should be removed", "Test",
                HTTPHeadersHelper.removeQuotes(input));
    }

    /**
     * Test removing of quotes
     * @throws java.lang.Exception
     */
    public void testRemoveQuotesWith2Quotes() throws Exception {
        String input = "Tes\"in quotes\"t 2Quotes\"1\"";
        assertEquals("Quotes should be removed", "Test 2Quotes",
                HTTPHeadersHelper.removeQuotes(input));
    }

    /**
     * Test removing of quotes
     * @throws java.lang.Exception
     */
    public void testRemoveQuotesWith2QuotesAndSpecialQuote() throws Exception {
        String input = "Tes\"in quo\\\"tes\"t This\"1\"";
        assertEquals("Quotes should be removed", "Test This",
                HTTPHeadersHelper.removeQuotes(input));
    }

    /**
     * Test removing of quotes
     * @throws java.lang.Exception
     */
    public void testRemoveQuotesWith2QuotesAndSingleQuote() throws Exception {
        String input = "Tes\"in quot'es\"t the\"1\" end";
        assertEquals("Quotes should be removed", "Test the end",
                HTTPHeadersHelper.removeQuotes(input));
    }

    /**
     * Test removing of quotes
     * @throws java.lang.Exception
     */
    public void testRemoveQuotesWith2QuotesOneDoubleQuote() throws Exception {
        String input = "Test One \"Double Quote";
        assertEquals("Quotes should be removed", "Test One ",
                HTTPHeadersHelper.removeQuotes(input));
    }

    /**
     * Utility class to allow HTTPHeadersHelper to be tested. It's a cut down
     * version of DefaultMutableHttpHeaders (which I would have used but it
     * would cause a circular dependency).
     */
    class MockHttpHeaders implements HttpHeaders {

        /**
         * A case insensitive map of (header name -> List) for each of the
         * headers we are managing. Each list contains one or more header values.
         */
        private TreeMap headerMap = new TreeMap();

        // Javadoc inherited.
        public void addHeader(String name, String value) {

            List values = (List) headerMap.get(name);
            if (values == null) {
                values = new ArrayList();
                headerMap.put(name, values);
            }
            values.add(value);
        }

        // Javadoc inherited.
        public Enumeration getHeaders(String name) {

            List values = getHeaderValues(name);
            return enumerate(values);
        }

        /**
         * Helper method to get the values for a header as a list.
         *
         * @param name the name of the header
         * @return the values list, or null if there were no value for this header.
         */
        private List getHeaderValues(String name) {

            List values = (List) headerMap.get(name);
            return values;
        }
        /**
         * Helper method to create an enumeration for a collection.
         *
         * @param values the collection of values to enumerate over, may be null
         *               or empty.
         * @return the enumeration for the values provided.
         */
        private Enumeration enumerate(Collection values) {

            if (values == null) {
                values = Collections.EMPTY_LIST;
            }
            return new Vector(values).elements();
        }

        /**
         * This method is not implemented and will always return null.
         *
         * @param name this variable is not used
         * @return null
         */
        public String getHeader(String name) {
            return null;
        }

        /**
         * This method is not implemented and will always return null.
         *
         * @return null
         */
        public Enumeration getHeaderNames() {
            return null;
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-May-05	8415/2	emma	VBM:2005042012 Bug fix merged from 330 - now retrieves all available accept headers. Also added HttpHeadersHelper

 23-May-05	8413/2	emma	VBM:2005042012 Bug fix merged from 323 - now retrieves all available accept headers. Also added HttpHeadersHelper

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
