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
package com.volantis.xml.pipeline.sax.drivers.web.httpcache;

import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.HTTPMessageEntityIdentity;
import com.volantis.shared.net.http.HttpServerMock;
import com.volantis.shared.net.http.SimpleHTTPFactory;
import com.volantis.shared.time.Period;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfigurationImpl;
import com.volantis.xml.pipeline.sax.drivers.web.AbstractPluggableHTTPManager;
import com.volantis.xml.pipeline.sax.drivers.web.AbstractPluggableHTTPManagerTestAbstract;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPClientPluggableHTTPManager;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestExecutor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPResponseAccessor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPVersion;
import com.volantis.xml.pipeline.sax.drivers.web.PluggableHTTPManager;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfigurationImpl;
import com.volantis.xml.pipeline.sax.url.URLContentCacheConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import our.apache.commons.httpclient.util.DateParser;

public class CachingPluggableHTTPManagerTestCase
        extends AbstractPluggableHTTPManagerTestAbstract {

    /**
     * Factory to create HTTPMessageEntities objects
     */
    public static final HTTPFactory factory = new SimpleHTTPFactory();

    /**
     * Age header identity
     */
    private static final HTTPMessageEntityIdentity AGE_HEADER =
            SimpleHTTPFactory.getDefaultInstance()
            .createHeader("Age").getIdentity();
    private HttpServerMock serverMock;

    protected void setUp() throws Exception {
        super.setUp();

        serverMock = new HttpServerMock();
    }

    protected void tearDown() throws Exception {
        super.tearDown();

        serverMock.close();
        serverMock = null;
    }

    /**
     * Utility class used to perform the request
     * @param testInstance the PluggableHTTPManager to test
     * @param fakeResponse the array of Strings representing the headers/body
     * of the faked response
     * @param type the type of the request (GET/POST/HEAD)
     * @param params the parameters to put on the request url. should be
     * of the form "?story=volcompany&tnav=volabout" or null if no parameters
     * are wanted
     * @return the accessor.
     * @throws Exception
     */
    protected HTTPResponseAccessor performRequest(
            AbstractPluggableHTTPManager testInstance,
            String[] fakeResponse,
            HTTPRequestType type,
            String params) throws Exception {

        if (fakeResponse != null) {
            serverMock.addTransaction(null, fakeResponse);
        }

        String url = serverMock.getURLAsString("/story.jsp");
        if (params != null) {
            url = url + params;
        }
        HTTPRequestExecutor request = testInstance.createHTTPRequestExecutor(
            url, type, HTTPVersion.HTTP_1_1, null, null);
        HTTPResponseAccessor result = request.execute();
        request.release();

        return result;
    }

    /**
     * Simple multithreaded test of the cache. This test attempts to find
     * deadlocks by loading the httpCache system. As such it contains no real
     * assert statements. It should not be included in the standard build which
     * is why there is an assert statement which will cause it always to fail.
     *
     * @throws Exception
     */
    /**  public void doTestMultithreaded() throws Exception {

     final int NUM_THREADS = 1000;
     final String[] fakeResponse = {
     "HTTP/1.1 200 OK",
     // add on a second to the date (just because this response is
     // actually being created before the cache is touched and is
     // therefore older then the initial cache entry.
     "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
     "Content-Type: text/html",
     "",
     "<HTML>",
     "<HEAD>",
     "<TITLE>Fake response</TITLE>",
     "</HEAD>",
     "<BODY>",
     "<H1>Fake response</H1>"
     };

     final AbstractPluggableHTTPManager manager = createTestInstance();
     // create all threads
     Set set = new HashSet();
     for (int i = 0; i < NUM_THREADS; i++) {
     Runnable r = new Runnable() {
     public void run() {
     try {
     performRequest(manager, fakeResponse, 8081, HTTPRequestType.GET);
     } catch (Exception e) {
     e.printStackTrace();
     }
     }
     };

     Thread t = new Thread(r);
     set.add(t);
     }

     // start all threads
     Iterator i = set.iterator();
     while (i.hasNext()) {
     ((Thread) i.next()).start();
     }

     // wait until all threads have completed.
     i = set.iterator();
     while (i.hasNext()) {
     ((Thread) i.next()).join();
     }

     assertFail("This test should not be included in the main build " +
     "process. Please comment it out. No deadlock was found");

     }*/


    /**
     * Test the age of the response is calculated correctly with an Age header
     * in the test.
     * @throws Exception
     */
    public void testPageTimeOut() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Age: 2",
            "Cache-Control: max-age=3",
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor respA =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        // sleep for 1.2 seconds. This should give an age of 1 second due to
        // the conservative nature of the age calculation. This will be added
        // to the age header in the response and should exceed the timeout
        // set for this cached entry.
        Thread.sleep(1200);
        HTTPResponseAccessor respB =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        assertTrue("The content should be the same",
            equals(respA.getResponseStream(), respB.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }

    /**
     * Test the age of the response is calculated correctly with an Age header
     * in the test.
     * @throws Exception
     *
     * @todo Fix this so it is not time dependent.
     */
    public void TIME_DEPENDENT_testAgeOfResponseWithAge() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Age: 2",
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        // sleep for 1.2 seconds. This should give an age of 1 second due to
        // the conservative nature of the age calculation. This will be added
        // to the age header in the response
        Thread.sleep(1200);
        HTTPMessageEntities headers = resp.getHeaders();
        HTTPMessageEntity[] ages = headers.retrieve(AGE_HEADER);

        assertNotNull("There should be an age header", ages);
        // check that we have calcualted the age to the page to be 1 second
        // (its always rounded down)
        assertEquals("Age should be 3 second", 3,
                Integer.parseInt(ages[0].getValue()));

    }

    /**
     * Test the response times out using Expires header.
     * @throws Exception
     */
    public void testExpiresHeader() throws Exception {

        // create an Expires header that is one second (or so) after now
        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Expires: " + getStringDate(new Date(System.currentTimeMillis() + 2000)),
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor respA =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        // sleep for 2.2 seconds. This should give an age of 1 second due to
        // the conservative nature of the age calculation. (and the fact that the
        // creation date is 1 second in the future
        Thread.sleep(2200);
        HTTPResponseAccessor respB =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        assertTrue("The content should be the same",
            equals(respA.getResponseStream(), respB.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }

    /**
     * Test the age of the response is calculated correctly
     * @throws Exception
     *
     * @todo Fix this so it is not time dependent.
     */
    public void TIME_DEPENDENT_testAgeOfResponse() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        // sleep for 1.2 seconds. This should give an age of 1 second due to
        // the conservative nature of the age calculation.
        Thread.sleep(1200);
        HTTPMessageEntities headers = resp.getHeaders();
        HTTPMessageEntity[] ages = headers.retrieve(AGE_HEADER);

        // check that we have calcualted the age to the page to be about 1
        // second (its always rounded down)
        int age = Integer.parseInt(ages[0].getValue());
        assertTrue("Age should be greater then 0 and less then 2",
                age > 0 && age <= 2);

    }

    /**
     * Test that the cache is actually caching GET requests and there responses.
     * @throws Exception
     */
    public void testHasCachedGet() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "Cache-control: max-age=5",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };

        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        HTTPResponseAccessor resp2 =
                performRequest(manager, null,
                                HTTPRequestType.GET, null);

        assertTrue(
            "Accessors should be equal as they should both be from the cache",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
    }

    /**
     * Returns true if the two input stream contain the same contents.
     *
     * @param is1 the first input stream
     * @param is2 the first input stream
     * @return true iff the two input stream contain the same bytes
     * @throws IOException if there is an error reading any of the streams
     */
    private boolean equals(final InputStream is1, final InputStream is2)
            throws IOException {
        boolean result;
        if (is1 == null || is2 == null) {
            result = is1 == is2;
        } else {
            result = true;
            for (int value = is1.read(); value != -1 && result; value = is1.read() ) {
                result = value == is2.read();
            }
            if (result) {
                result = is2.read() == -1;
            }
        }
        return result;
    }

    /**
     * Test that the cache is not caching GET requests and thier
     * responses even after the  max-age header has expired.
     *
     * @throws Exception
     */
    public void testHasNotCachedGetWithMaxAgeExpired() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "Cache-Control: max-age=1", // 1 second
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        Thread.sleep(3000);   // wait three seconds so that max-age expires.
        HTTPResponseAccessor resp2 =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        assertTrue("The content should be the same",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }

    /**
     * Test that the cache is actually caching GET requests and thier
     * responses even if a max-age header exists (as long as the maxAge is not
     * exceeded)
     *
     * @throws Exception
     */
    public void testHasCachedGetWithMaxAge() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "Cache-Control: max-age=1000", // 1000 second timeout
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        //   Thread.sleep(1000);   // wait one second
        HTTPResponseAccessor resp2 =
                performRequest(manager, null,
                                HTTPRequestType.GET, null);

        assertTrue(
            "Accessors should be equal as they should both be from the cache",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
    }

    /**
     * Test that the cache is not caching GET requests if the response from the
     * server contains a no-cache or private directive.
     * @throws Exception
     */
    public void testHasNotCachedGetWithNoCache() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "Cache-Control: no-cache",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        HTTPResponseAccessor resp2 =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, null);

        assertTrue("The content should be the same",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }

    /**
     * Test that the cache is not caching POST requests
     * @throws Exception
     */
    public void testHasNotCachedPost() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.POST, null);

        HTTPResponseAccessor resp2 =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.POST, null);

        assertTrue("The content should be the same",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }

    /**
     * Test that the cache is not caching POST requests
     * @throws Exception
     */
    public void testHasNotCachedParamsAsSame() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, "?wibble=wobble");

        HTTPResponseAccessor resp2 =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, "?wibble=wont");

        assertTrue("The content should be the same",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }

    /**
     * Test that the cache is not caching GET requests that are HTTP 1.0 and
     * have request parameters
     * @throws Exception
     */
    public void testHasNotCachedHTTP10WithParams() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.0 200 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            "",
            "<HTML>",
            "<HEAD>",
            "<TITLE>Fake response</TITLE>",
            "</HEAD>",
            "<BODY>",
            "<H1>Fake response</H1>"
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, "?wibble=wobble");

        HTTPResponseAccessor resp2 =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.GET, "?wibble=wont");

        assertTrue("The content should be the same",
            equals(resp.getResponseStream(), resp2.getResponseStream()));
        assertFalse("The server used up all of the transactions.",
            serverMock.hasTransactions());
    }


    /**
     * Test that the cache is not caching POST requests and handles errors on
     * post.
     * @throws Exception
     */
    public void testHandleError() throws Exception {

        String[] fakeResponse = {
            "HTTP/1.1 400 OK",
            // add on a second to the date (just because this response is
            // actually being created before the cache is touched and is
            // therefore older then the initial cache entry.
            "Date: " + getStringDate(new Date(System.currentTimeMillis() + 1000)),
            "Content-Type: text/html",
            ""
        };
        AbstractPluggableHTTPManager manager = createTestInstance();
        HTTPResponseAccessor resp =
                performRequest(manager, fakeResponse,
                                HTTPRequestType.POST, null);
        assertEquals("response should be a 400", 400, resp.getStatusCode());

    }

    /**
     * Utility method to return the data as a string to insert into
     * fakeResponses
     * @param date the date to format into a string
     */
    private String getStringDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DateParser.PATTERN_RFC1123);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return dateFormat.format(date);
    }

    // convieniance method to create and initialize teh manager.
    private AbstractPluggableHTTPManager createTestInstance() {
        AbstractPluggableHTTPManager manager = (AbstractPluggableHTTPManager)
                createTestablePluggableHTTPManager();
        manager.initialize(createTestWebDriverConfig(), Period.INDEFINITELY);
        return manager;
    }

    // javadoc inherited
    protected PluggableHTTPManager
            createTestablePluggableHTTPManager() {
        AbstractPluggableHTTPManager delegate =
                new HTTPClientPluggableHTTPManager();
        XMLPipelineConfiguration config = new XMLPipelineConfiguration() {
            public void storeConfiguration(Object key, Configuration configuration) {
            }

            public Configuration retrieveConfiguration(Object key) {
                Configuration config = null;
                if (key.equals(ConnectionConfiguration.class)) {
                    final ConnectionConfigurationImpl connectionConfig =
                        new ConnectionConfigurationImpl();
                    connectionConfig.setCachingEnabled(true);
                    connectionConfig.setMaxCacheEntries(1000);
                    config = connectionConfig;
                }
                return config;
            }
        };
        URLContentCacheConfiguration urlConfig =
            new URLContentCacheConfiguration(config);
        return new CachingPluggableHTTPManager(delegate, urlConfig.getCache());
    }


    // create a test WebDriverConfiguration.
    protected WebDriverConfiguration createTestWebDriverConfig() {
        return new WebDriverConfigurationImpl() {
            // javadoc inherited
            public String getLocation() {
                return System.getProperty("java.io.tmpdir");
            }

            // javadoc inherited
            public int getMaxEntries() {
                return 100;
            }

            // javadoc inherited
            public int getPersistentCacheMaxSize() {
                return 10000;
            }

            // javadoc inherited
            public void setTimeout(long timeout) {
            }

            // javadoc inherited
            public long getTimeoutInMillis() {
                return -1;
            }
        };
    }
}
