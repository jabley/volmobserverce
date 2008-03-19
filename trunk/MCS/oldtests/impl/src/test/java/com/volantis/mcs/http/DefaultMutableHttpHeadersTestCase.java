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
package com.volantis.mcs.http;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.runtime.impl.DefaultMutableHttpHeaders;
import java.util.Enumeration;

/**
 * Test case for {@link DefaultMutableHttpHeaders}.
 */
public class DefaultMutableHttpHeadersTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Test empty (newly created) headers.
     */
    public void testEmpty() {

        MutableHttpHeaders headers = new DefaultMutableHttpHeaders();

        assertNull("", headers.getHeader("unknown"));

        Enumeration names = headers.getHeaderNames();
        assertNotNull("", names);
        assertEquals("", false, names.hasMoreElements());

        Enumeration values = headers.getHeaders("unknown");
        assertNotNull("", values);
        assertEquals("", false, values.hasMoreElements());
    }

    /**
     * Test a single header with a single value.
     */
    public void testSingleHeader() {

        MutableHttpHeaders headers = new DefaultMutableHttpHeaders();

        headers.addHeader("h1", "v1");

        assertEquals("", "v1", headers.getHeader("h1"));

        Enumeration names = headers.getHeaderNames();
        assertNotNull("", names);
        assertEquals("", true, names.hasMoreElements());
        assertEquals("", "h1", names.nextElement());
        assertEquals("", false, names.hasMoreElements());

        Enumeration values = headers.getHeaders("h1");
        assertNotNull("", values);
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v1", values.nextElement());
        assertEquals("", false, values.hasMoreElements());
    }

    /**
     * Test adding the same header twice with different case.
     */
    public void testHeaderNameCase() {

        MutableHttpHeaders headers = new DefaultMutableHttpHeaders();

        headers.addHeader("h1", "v1");
        headers.addHeader("H1", "V2");

        assertEquals("", "v1", headers.getHeader("h1"));

        Enumeration names = headers.getHeaderNames();
        assertNotNull("", names);
        assertEquals("", true, names.hasMoreElements());
        assertEquals("", "h1", names.nextElement());
        assertEquals("", false, names.hasMoreElements());

        Enumeration values = headers.getHeaders("H1");
        assertNotNull("", values);
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v1", values.nextElement());
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "V2", values.nextElement());
        assertEquals("", false, values.hasMoreElements());
    }

    /**
     * Test multiple headers with single values.
     */
    public void testMultipleHeaders() {

        MutableHttpHeaders headers = new DefaultMutableHttpHeaders();

        headers.addHeader("h1", "v1");
        headers.addHeader("h2", "v2");

        assertEquals("", "v1", headers.getHeader("h1"));
        assertEquals("", "v2", headers.getHeader("h2"));

        Enumeration names = headers.getHeaderNames();
        assertNotNull("", names);
        assertEquals("", true, names.hasMoreElements());
        assertEquals("", "h1", names.nextElement());
        assertEquals("", true, names.hasMoreElements());
        assertEquals("", "h2", names.nextElement());
        assertEquals("", false, names.hasMoreElements());

        Enumeration values = headers.getHeaders("h1");
        assertNotNull("", values);
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v1", values.nextElement());
        assertEquals("", false, values.hasMoreElements());

        values = headers.getHeaders("h2");
        assertNotNull("", values);
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v2", values.nextElement());
        assertEquals("", false, values.hasMoreElements());
    }

    /**
     * Test a header with multiple values.
     */
    public void testMultipleHeaderValues() {

        MutableHttpHeaders headers = new DefaultMutableHttpHeaders();

        headers.addHeader("h1", "v1");
        headers.addHeader("h1", "v2");
        headers.addHeader("h1", "v3");

        assertEquals("", "v1", headers.getHeader("h1"));

        Enumeration names = headers.getHeaderNames();
        assertNotNull("", names);
        assertEquals("", true, names.hasMoreElements());
        assertEquals("", "h1", names.nextElement());
        assertEquals("", false, names.hasMoreElements());

        Enumeration values = headers.getHeaders("h1");
        assertNotNull("", values);
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v1", values.nextElement());
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v2", values.nextElement());
        assertEquals("", true, values.hasMoreElements());
        assertEquals("", "v3", values.nextElement());
        assertEquals("", false, values.hasMoreElements());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Aug-04	5058/1	geoff	VBM:2004080208 Implement the missing mutable http headers for device repository

 ===========================================================================
*/
