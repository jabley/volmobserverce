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
package com.volantis.mcs.atg.dynamo;


import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import junit.framework.TestCase;

import com.volantis.mcs.testtools.request.stubs.HttpServletRequestStub;

public class PostDataServletUtilsTestCase extends TestCase
{
    
    public PostDataServletUtilsTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    /** Test of encodedValue(String value) */
    public void testEncodeValue() {
        PostDataServletUtils testClass = new PostDataServletUtils();
        
        // Null value should return "";
        String valueToEncode = null;
        String result = testClass.encodedValue(valueToEncode);
        assertEquals( "", result );
        
        // Test URL Encoding
        valueToEncode="http://www.wibble.com";
        result = testClass.encodedValue(valueToEncode);
        assertEquals( "http%3A%2F%2Fwww.wibble.com", result );
    }
    
    public void testGetHeaders() {
        PostDataServletUtils testClass = new PostDataServletUtils();
        MockHttpServletRequest request = new MockHttpServletRequest();
        
        request.addHeader("Content-Length","50");
        request.addHeader("Content-Type","text/html");
        request.addHeader("x-wibble","wibble");
        request.addHeader("x-multi-wibble","wibble1");
        request.addHeader("x-multi-wibble","wibble2");
        Collection result = testClass.getHeaders(request);
        //should be 2 1 for x-wibble + 1 for x-multi-wibble
        assertEquals(2,result.size());
        
        boolean foundXWibble = false;
        boolean foundXMultiWibble = false;
        
        Object[] resultArray = result.toArray();
        for (int n = 0; n < resultArray.length; n++) {
            if ("x-wibble: wibble".equals(resultArray[n])) {
                foundXWibble = true;
            } 
            if ("x-multi-wibble: wibble1, wibble2".equals(resultArray[n])) {
                foundXMultiWibble = true;
            } 
        }
        assertTrue("x-wibble not found",foundXWibble);
        assertTrue("x-multi-wibble not found",foundXMultiWibble);        
    }
    
    // Mock HttpServletRequest to aid in testing.
    private class MockHttpServletRequest extends HttpServletRequestStub {

       Hashtable headers = new Hashtable();
        
        public MockHttpServletRequest() {
        }


        /**
         * Returns the appropriate header value
         * @param name The name of the header.
         * @return The value of the header.
         */
        public String getHeader(String name) {
            Vector values = (Vector)headers.get(name);
            if (values != null) {
                return (String)values.firstElement();
            }
            return null;
        }
        
        /**
         * Add the headers to the request
         * @param name The name of the header
         * @param value The value of the header
         */
        public void addHeader(String name, String value) {
            Vector existing = (Vector)headers.get(name);
            if (existing == null) {
                Vector multiValue = new Vector();
                multiValue.add(value);
                headers.put(name,multiValue);
            } else  {
                existing.add(value);
                headers.put(name,existing);
            }
        }

        public Enumeration getHeaderNames() {
            return headers.keys();
        }

        public Enumeration getHeaders(String name) {
            Vector values = (Vector)headers.get(name);
            if (values != null) {
                return values.elements();
            }
            return null;
        }

    }   
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 10-Jan-05	6565/1	adrianj	VBM:2004122902 Created Dynamo 7 version of Volantis custom tags

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4737/1	allan	VBM:2004062202 Restrict volantis initialization.

 27-Apr-04	3843/1	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
