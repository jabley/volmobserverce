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
package com.volantis.mcs.runtime.pipeline;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineContextStub;
import com.volantis.xml.pipeline.sax.convert.URLConverter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tests {@link TranscoderURLConverter}.
 */
public class TranscoderURLConverterTestCase extends TestCaseAbstract {
    public TranscoderURLConverterTestCase(String s) {
        super(s);
    }

    /**
     * Tests that image host and port are only added when an image host has
     * been specified.
     */
    public void testNoHostInURL() throws Exception {
        doTest("http://:8080/path",
                "http://server",
                null,
                "tf.source.host",
                "tf.source.port",
                "http://server/path");
    }

    public void testToURLC() throws Exception {
        doTest("http://host:8080/path",
               "http://server",
               null,
               "tf.source.host",
               "tf.source.port",
               "http://server/path?tf.source.host=host&tf.source.port=8080");
    }

    public void testDefaultSourcePort() throws Exception {
        doTest("http://host/path",
               "http://server",
               null,
               "tf.source.host",
               "tf.source.port",
               "http://server/path?tf.source.host=host&tf.source.port=80");
    }

    public void testServerURLWithSlash() throws Exception {
        doTest("http://host/path",
               "http://server/",
               null,
               "v.imgHost",
               "v.imgPort",
               "http://server/path?v.imgHost=host&v.imgPort=80");
    }

    public void testPathWithQuery() throws Exception {
        doTest("http://host/path?query",
               "http://server",
               null,
               "tf.source.host",
               "tf.source.port",
               "http://server/path?query&tf.source.host=host&" +
               "tf.source.port=80");
    }

    public void testPathWithRef() throws Exception {
        doTest("http://host/path#ref",
               "http://server",
               null,
               "tf.source.host",
               "tf.source.port",
               "http://server/path?tf.source.host=host&" +
               "tf.source.port=80");
    }

    public void testServletRelativePath() throws Exception {
        doTest("/servlet/relative",
               "http://server",
               "http://servletHost:1066/servletPath/",
               null,
               null,
               "http://server/servlet/relative?v.imgHost=servletHost&" +
               "v.imgPort=1066");
    }

    public void testRequestRelativePath() throws Exception {
        doTest("request/relative",
               "http://server",
               "http://servletHost:1066/servletPath/",
               "tf.source.host",
               "tf.source.port",
               "http://server/servletPath/request/relative?" +
               "tf.source.host=servletHost&" +
               "tf.source.port=1066");
    }

    /**
     * Helper method to actually do the test run.
     *
     * @param input     the input URL
     * @param serverURL the server URL
     * @param baseURL   the base URL to use for relative paths
     * @param hostParam the name for the host parameter
     * @param portParam the name for the port parameter
     * @param expected  the expected result URL
     * @throws Exception if the conversion fails
     */
    protected void doTest(String input,
                          String serverURL,
                          String baseURL,
                          String hostParam,
                          String portParam,
                          String expected) throws Exception {
        URLConverter converter = new TranscoderURLConverter(hostParam,
                                                            portParam);

        XMLPipelineContext context = new XMLPipelineContextStub() {
            URL baseURI;

            public void pushBaseURI(String s) throws MalformedURLException {
                baseURI = new URL(s);
            }

            public URL popBaseURI() {
                return null;
            }

            public URL getCurrentBaseURI() {
                return baseURI;
            }
        };

        if (baseURL != null) {
            context.pushBaseURI(baseURL);
        } else {
            context.pushBaseURI("http://myhost:1664/mypath");
        }

        String actual = converter.toURLC(context,
                                         input,
                                         serverURL);

        assertEquals("Inputs [" + input + ", " + serverURL + ", " + baseURL +
                     ", " + hostParam + ", " + portParam +
                     "] do not generate result as",
                     expected,
                     actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-May-05	7984/2	pcameron	VBM:2005050306 Only add image host and port to transcoder URL when image host is specified

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Nov-04	6109/3	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names

 14-Aug-03	1096/1	adrian	VBM:2003070805 updated usages of XMLPipelineContext and PropertyContainer to match pipeline api changes

 07-Aug-03	981/1	philws	VBM:2003080605 Provide Transforce PictureIQ specific pipeline URL to URLC conversion

 ===========================================================================
*/
