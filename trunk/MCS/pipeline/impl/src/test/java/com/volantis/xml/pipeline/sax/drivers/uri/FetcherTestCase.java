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

package com.volantis.xml.pipeline.sax.drivers.uri;

import com.volantis.shared.net.http.HttpServerMock;
import com.volantis.shared.net.http.WaitTransaction;
import com.volantis.shared.time.Period;
import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.ResourceNotFoundException;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineFilter;

import java.io.InterruptedIOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Test cases for {@link Fetcher}.
 */
public class FetcherTestCase
        extends PipelineTestAbstract {

    private HttpServerMock serverMock;
    private StringWriter writer;

    protected void setUp() throws Exception {
        super.setUp();

        serverMock = new HttpServerMock();

        writer = new StringWriter();

        XMLPipelineFactory factory = XMLPipelineFactory.getDefaultInstance();
        XMLPipelineFilter filter = createPipelineFilter(factory,
                createErrorHandler(), writer);

        pipeline = filter.getPipelineProcess().getPipeline();
    }

    protected void tearDown() throws Exception {

        serverMock.close();

        super.tearDown();
    }

    /**
     * Ensure that the fetcher will timeout.
     */
    public void testTimeout() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        serverMock.addTransaction(new WaitTransaction(2000));

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Fetcher fetcher = new Fetcher(pipeline);
        String href = serverMock.getURL("/fred.jsp").toExternalForm();
        fetcher.setHref(href);
        fetcher.setTimeout(Period.treatNonPositiveAsIndefinitely(500));
        try {
            fetcher.doInclude();
        } catch (ResourceNotFoundException e) {
            InterruptedIOException cause =
                    (InterruptedIOException) e.getCause();
            assertEquals("Request to '" + href + "' timed out after 500ms",
                    cause.getMessage());
        }
    }

    /**
     * Ensure that the fetcher will timeout.
     */
    public void testOk() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        serverMock.addTransaction(null, new String[]{
            "HTTP/1.0 200 OK",
            "Date: Fri, 31 Dec 1999 23:59:59 GMT",
            "Content-Type: text/plain",
            "",
            "<p>hello</p>"
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Fetcher fetcher = new Fetcher(pipeline);
        String href = serverMock.getURL("/fred.jsp").toExternalForm();
        fetcher.setHref(href);
        fetcher.setTimeout(Period.treatNonPositiveAsIndefinitely(500));
        fetcher.doInclude();

        assertXMLEqual(new StringReader("<p>hello</p>"),
                new StringReader(writer.getBuffer().toString()));
    }
}
