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

package com.volantis.mps.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.servletunit.ServletRunner;
import com.volantis.testtools.servletunit.ServletUnitClient;
import com.volantis.mps.message.store.MessageStoreMessageEnumeration;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.HttpException;
import com.meterware.httpunit.GetMethodWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.swing.*;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * This provides an integration level test for the Message Store Servlet.
 * It utilizes the {@link ServletRunner} framework for testing the servlet
 * without needing to deploy to tomcat or some other container.
 * <p>
 * This does not rely on config files existing in the build area as that
 * creates a dependency for the test case on the build not changing
 * <strong>AND</strong> does not isolate each test case.
 */
public class MessageStoreServletTestCase extends TestCaseAbstract {

    /**
     * This contains a list of files that should be deleted at the end
     * of this test case running to ensure things are tidied up OK.
     */
    protected List filesToDelete = new ArrayList();

    /**
     * The location specified as the absolute path of the directory into
     * which temporary log4j files created by the servlet should be put.
     * This will be tidied up as part of the test tear down.
     */
    protected static final String LOG_FILE_DIR =
            MessageStoreTestHelper.TMPDIR +
            MessageStoreTestHelper.FILE_SEPARATOR +
            "msslog4jfiles" + Math.random() +
            MessageStoreTestHelper.FILE_SEPARATOR;

    /**
     * A valid XML message used for testing
     */
    private static final String VALID_TEST_XML =
            "<?xml version='1.0' encoding='UTF-8'?>\n<root></root>";

    /**
     * An invlaid XML message for testing
     */
    private static final String INVALID_TEST_XML =
            "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<root><non-closed-element></root>";

    /**
     * URL of the MSS which creates entire MCS session
     */
    private static final String FULL_TEST_URL = "http://localhost:8080/mss";

    /**
     * URL of servlet which does not create a full MCS session
     */
    private static final String PARTIAL_TEST_URL = "http://localhost:8080/msstest";


    /**
     * URL of servlet which tests provides a substitution for the getReader(...)
     */
    private static final String READER_TEST_URL = "http://localhost:8080/mssreader";

    /**
     * Initialise a new instance of this test case.
     */
    public MessageStoreServletTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public MessageStoreServletTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        // Create the message store directory so that the servlet init succeeds
        MessageStoreTestHelper.createDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        // Create the log directory
        MessageStoreTestHelper.createDir(new File(LOG_FILE_DIR));
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        // Tidy up the message store
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        // Remove the log directory
        MessageStoreTestHelper.deleteDir(new File(LOG_FILE_DIR));

        // Delete any other files created
        for (Iterator i = filesToDelete.iterator(); i.hasNext();) {
            File file = (File) i.next();
            file.delete();
        }

        super.tearDown();
    }

    /**
     * Create a web.xml file to initialise the servlet within the test
     * servlet container.
     *
     * @param configFileLocation The location of the config file the servlet
     *                           should use.
     *
     * @return An input stream that reads from the web.xml file created.
     */
    protected InputStream createWebXML(String configFileLocation) {
        final String webXML =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "<!DOCTYPE web-app PUBLIC " +
                "\"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN\" " +
                "\"http://java.sun.com/dtd/web-app_2_3.dtd\">\n" +
                "\n" +
                "<web-app>\n" +
                "    <display-name>Message Store Servlet</display-name>\n" +
                "    <description>Message Store Servlet</description>\n" +
                "\n" +
                "    <servlet>\n" +
                "        <servlet-name>MSS</servlet-name>\n" +
                "        <servlet-class>com.volantis.mps.servlet." +
                "MessageStoreServlet</servlet-class>\n" +
                "        <init-param>\n" +
                "            <param-name>config.file</param-name>\n" +
                "            <param-value>" + configFileLocation +
                "</param-value>\n" +
                "        </init-param>\n" +
                "    </servlet>\n" +
                "\n" +
                "    <servlet-mapping>\n" +
                "        <servlet-name>MSS</servlet-name>\n" +
                "        <url-pattern>/mss/*</url-pattern>\n" +
                "    </servlet-mapping>\n" +
                "\n" +
                "    <servlet>\n" +
                "        <servlet-name>MSSTest</servlet-name>\n" +
                "        <servlet-class>com.volantis.mps.servlet.TestMSS" +
                "</servlet-class>\n" +
                "        <init-param>\n" +
                "            <param-name>config.file</param-name>\n" +
                "            <param-value>" + configFileLocation +
                "</param-value>\n" +
                "        </init-param>\n" +
                "    </servlet>\n" +
                "\n" +
                "    <servlet-mapping>\n" +
                "        <servlet-name>MSSTest</servlet-name>\n" +
                "        <url-pattern>/msstest/*</url-pattern>\n" +
                "    </servlet-mapping>\n" +
                "\n" +
                "   <servlet>\n" +
                "        <servlet-name>MSSReader</servlet-name>\n" +
                "        <servlet-class>com.volantis.mps.servlet.MSSReader" +
                "</servlet-class>\n" +
                "        <init-param>\n" +
                "            <param-name>config.file</param-name>\n" +
                "            <param-value>" + configFileLocation +
                "</param-value>\n" +
                "        </init-param>\n" +
                "    </servlet>\n" +
                "\n" +
                "    <servlet-mapping>\n" +
                "        <servlet-name>MSSReader</servlet-name>\n" +
                "        <url-pattern>/mssreader/*</url-pattern>\n" +
                "    </servlet-mapping>\n" +
                "</web-app>\n";
        return new StringBufferInputStream(webXML);
    }

    /**
     * Create a log4j configuration file for use with the servlet during
     * testing.  It must be valid if the initialisation of the servlet is
     * to succeed.
     *
     * @return The absolute path to the log4j file created.
     *
     * @throws IOException If there was a problem creating the config file
     */
    protected String createLog4jConfigFile() throws IOException {
        String location = LOG_FILE_DIR + "logfile" + Math.random() + ".xml";

        final String contents =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">\n" +
                "<log4j:configuration xmlns:log4j=" +
                "\"http://jakarta.apache.org/log4j/\">\n" +
                "<appender name=\"DEFAULT-LOG\" " +
                "class=\"org.apache.log4j.FileAppender\">\n" +
                "    <param name=\"File\" value=\"" + location + "\"/>\n" +
                "    <param name=\"Append\" value=\"false\"/>\n" +
                "    <layout class=\"org.apache.log4j.PatternLayout\">\n" +
                "        <param name=\"ConversionPattern\"" +
                "               value=\"%-4r [%t] %-5p %l %x - %m%n\"/>\n" +
                "    </layout>\n" +
                "</appender>\n" +
                "\n" +
                "<appender name=\"ASYNC\" " +
                "class=\"org.apache.log4j.AsyncAppender\">\n" +
                "    <param name=\"LocationInfo\" value=\"true\"/>\n" +
                "    <appender-ref ref=\"DEFAULT-LOG\"/>\n" +
                "</appender>\n" +
                "\n" +
                "<root>\n" +
                "    <priority value=\"warn\"/>\n" +
                "    <appender-ref ref=\"ASYNC\"/>\n" +
                "</root>" +
                "\n" +
                "</log4j:configuration>";
        File configFile = new File(MessageStoreTestHelper.TMPDIR,
                                   "mss-log4j-" + Math.random() + ".xml");
        filesToDelete.add(configFile);

        FileWriter writer = new FileWriter(configFile);
        writer.write(contents);
        writer.flush();
        writer.close();
        return configFile.getAbsolutePath();
    }

    /**
     * Create a config file for the MSS servlet.  Any changes to the config
     * for MSS require that this method be updated.  All object parameters
     * may be null to force the use of a default.
     *
     * @param location The message store location
     * @param timeout  The timeout of entries in the message store
     * @param idSize   The id size to use for generated IDs.  If less than
     *                 10 another value is used
     * @param validate True if the stored XML should be validated
     * @param log4j    The location of the log4j config file to use
     *
     * @return The absolute path to the config file generated
     *
     * @throws IOException If there was a problem creating the config file
     */
    protected String createConfigFile(String location,
                                      String timeout,
                                      int idSize,
                                      boolean validate,
                                      String log4j) throws IOException {
        final String defaultLocation =
                MessageStoreTestHelper.TMPDIR +
                MessageStoreTestHelper.FILE_SEPARATOR +
                "store";

        final String contents = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<messageStoreServer\n" +
                "    xmlns=\"http://www.volantis.com/xmlns/mss/config\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.volantis.com/xmlns/mss/config http://www.volantis.com/schema/config/v1.0/mss-config.xsd\"\n" +
                ">\n" +
                "\n" +
                "    <environment log4jConfigurationFile=\"" +
                (log4j == null ? createLog4jConfigFile() : log4j) + "\"/>\n" +
                "\n" +
                "    <message-store\n" +
                "        location=\"" +
                (location == null ? defaultLocation : location) + "\"\n" +
                "        timeout=\"" +
                (timeout == null ? "unlimited" : timeout) + "\"\n" +
                "        id-size=\"" + (idSize < 10 ? 12 : idSize) + "\"\n" +
                "        validate=\"" + validate + "\"" +
                "/>\n" +
                "</messageStoreServer>";
        File configFile = new File(MessageStoreTestHelper.TMPDIR,
                                   "mss-config-" + Math.random() + ".xml");
        filesToDelete.add(configFile);

        FileWriter writer = new FileWriter(configFile);
        writer.write(contents);
        writer.flush();
        writer.close();
        return configFile.getAbsolutePath();
    }

    /**
     * Create a config file for MSS containing all default values
     *
     * @return The abolsute path to the config file generated

     * @throws IOException If there was a problem creating the config file
     */
    protected String createDefaultConfigFile() throws IOException {
        return createConfigFile(MessageStoreTestHelper.MESSAGE_STORE_LOCATION,
                                null,
                                -1,
                                false,
                                null);
    }

    /**
     * Create a default web xml file with the config file in the default
     * location with default values.
     *
     * @return An input stream that reads from the web.xml file created
     *
     * @throws IOException If there was a problem creating the config file
     */
    protected InputStream createDefaultWebXML() throws IOException {
        return createWebXML(createDefaultConfigFile());
    }

    /**
     * This tests the creation and initialisation of the servlet.
     */
    public void testInstantiation() throws Exception {
        ServletRunner runner = new ServletRunner(createWebXML(
                createConfigFile(null, null, 10, false, null)));

        ServletUnitClient client = runner.newClient();

        WebRequest request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");
        request.setHeaderField("Accept", "text/html");

        // Try with a invalid location specified in the config file
        try {
            client.getResponse(request);
            fail("Runtime exception because of invalid location expected");
        } catch (RuntimeException e) {
            // This is expected...
        }

        // Now try where the init() etc. should succeed
        runner = new ServletRunner(createDefaultWebXML());
        client = runner.newClient();

        request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");
        request.setHeaderField("Accept", "text/html");
        client.getResponse(request);
    }


    /**
     * This tests the storage operation using a POST request
     * to a non-validating message store
     *
     * @throws Exception
     */
    public void testStore() throws Exception {

        ServletRunner runner = new ServletRunner(createDefaultWebXML());
        ServletUnitClient client = runner.newClient();

        WebRequest request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);

        WebResponse response = client.getResponse(request);
        String id = response.getHeaderField(
                MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);

        // Check the ID has been returned OK
        assertNotNull("ID should exist in the headers", id);

        // Look for the file too
        File storedFile =
                new File(MessageStoreTestHelper.MESSAGE_STORE_LOCATION,
                         id + ".xml");
        assertTrue("Stored file should exist", storedFile.exists());

        // Check file contents as a further test
        FileReader reader = new FileReader(storedFile);
        StringBuffer fileContents = new StringBuffer();
        int character = reader.read();
        while (character != -1) {
            fileContents.append((char) character);
            character = reader.read();
        }
        String message = fileContents.toString();
        assertEquals("XML should match", VALID_TEST_XML, message);
    }

    /**
     * This tests the storage operation using a POST request
     * to a validating message store
     *
     * @throws Exception
     */
    public void testValidatingStore() throws Exception {
        // create validating message store
        ServletRunner runner = new ServletRunner(createWebXML(
                createConfigFile(MessageStoreTestHelper.MESSAGE_STORE_LOCATION,
                                 null,
                                 10,
                                 true,
                                 null)));

        // tets with valid XML
        ServletUnitClient client = runner.newClient();
        WebRequest request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        WebResponse response = client.getResponse(request);
        String id = response.getHeaderField(
                MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);

        // Check the ID has been returned OK
        assertNotNull("ID should exist in the headers", id);
        // Look for the file
        File storedFile =
                new File(MessageStoreTestHelper.MESSAGE_STORE_LOCATION,
                         id + ".xml");
        assertTrue("Stored file should exist", storedFile.exists());


        // And using invalid XML
        request = createPostRequest(INVALID_TEST_XML, READER_TEST_URL);
        client = runner.newClient();
        try {
            response = client.getResponse(request);
            fail("This is invalid XML and should cause an exception");
        } catch (HttpException he) {
            // Test success
        }

    }

    /**
     * This tests the retreival operation on both a valid ID and an invalid ID.
     * The invalid ID should return a 404 error
     *
     * @throws Exception
     */

    public void testNonExistentID() throws Exception {
        ServletRunner runner = new ServletRunner(createDefaultWebXML());
        ServletUnitClient client = runner.newClient();

        WebRequest request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        WebResponse response = client.getResponse(request);
        String id = response.getHeaderField(
                MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);

        // Check the ID has been returned OK
        assertNotNull("ID should exist in the headers", id);

        // Use a fake servlet class to avoid the need to initialise and run
        // an entire MCS session.
        request = new GetMethodWebRequest(PARTIAL_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");
        request.setParameter(MessageStoreServlet.MESSAGE_RETRIEVE_PARAM_NAME,
                             id);

        client = runner.newClient();
        response = client.getResponse(request);
        assertEquals("The response code should indicate success",
                     MessageStoreMessageEnumeration.SUCCESS.getValue(),
                     response.getResponseCode());

        // create invalid ID
        String invalidID = "123h23h29t";

        // get the invalid ID
        request = new GetMethodWebRequest(FULL_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");
        request.setParameter(MessageStoreServlet.MESSAGE_RETRIEVE_PARAM_NAME,
                             invalidID);

        client = runner.newClient();

        try {
            client.getResponse(request);
            fail("Getting an invalid ID is not allowed and should throw a 404");
        } catch (HttpException he) {
            // exception should be thrown
            // check that the reponse code is correct.
            assertEquals("Reponse code is not correct",
                         HttpServletResponse.SC_NOT_FOUND,
                         he.getResponseCode());
        }


    }

    /**
     * This tests the storage operation using a GET request
     * to a validating message store. This operation is not allowed.
     *
     * @throws Exception
     */

    public void testGetStore() throws Exception {
        ServletRunner runner = new ServletRunner(createDefaultWebXML());
        ServletUnitClient client = runner.newClient();

        WebRequest getRequest = new GetMethodWebRequest(FULL_TEST_URL);
        getRequest.setParameter(MessageStoreServlet.MESSAGE_STORE_PARAM_NAME,
                             VALID_TEST_XML);

        try {
            client.getResponse(getRequest);
            fail("Storing a message is not allowed using the GET method");
        } catch (HttpException he) {
            // exception should be thrown
            // check that the reponse code is correct.
            assertEquals("Reponse code is not correct",
                         HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                         he.getResponseCode());
        }
    }


    /**
     * Tests a mixed request specifying both an ID and XML content in a GET and
     * a POST request.
     *
     * @throws Exception
     */

    public void testMixedRequest() throws Exception {
        ServletRunner runner = new ServletRunner(createDefaultWebXML());
        ServletUnitClient client = runner.newClient();

        WebRequest request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        WebResponse response = client.getResponse(request);
        String id = response.getHeaderField(
                MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);

        // Check the ID has been returned OK
        assertNotNull("ID should exist in the headers", id);

        // try a POST with XML content and ID
        request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");
        request.setParameter(MessageStoreServlet.MESSAGE_RETRIEVE_PARAM_NAME,
                             id);
        response = client.getResponse(request);
        assertEquals("The response code should indicate success",
                     MessageStoreMessageEnumeration.SUCCESS.getValue(),
                     response.getResponseCode());

    }

    public void testEmptyMessageBody() throws Exception {
        // emtpy message
        final String emptyMsg = "";

        // create MSS
        ServletRunner runner = new ServletRunner(createDefaultWebXML());
        ServletUnitClient client = runner.newClient();

        WebRequest request = createPostRequest(emptyMsg, READER_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");

        try {
            client.getResponse(request);
        } catch (HttpException he) {
            // exception should be thrown
            // check that the reponse code is correct.
            assertEquals("Reponse code is not correct",
                         HttpServletResponse.SC_BAD_REQUEST,
                         he.getResponseCode());
        }
    }



    /**
     * This tests the retrieval operation using a GET request
     */

    public void testRetrieve() throws Exception {
        ServletRunner runner = new ServletRunner(createDefaultWebXML());
        ServletUnitClient client = runner.newClient();

        WebRequest request = createPostRequest(VALID_TEST_XML, READER_TEST_URL);
        WebResponse response = client.getResponse(request);
        String id = response.getHeaderField(
                MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);

        // Check the ID has been returned OK
        assertNotNull("ID should exist in the headers", id);

        // Use a fake servlet class to avoid the need to initialise and run
        // an entire MCS session.
        request = new GetMethodWebRequest(PARTIAL_TEST_URL);
        request.setHeaderField("User-Agent", "Mozilla/5.0 Firefox...");
        request.setParameter(MessageStoreServlet.MESSAGE_RETRIEVE_PARAM_NAME,
                             id);

        client = runner.newClient();
        response = client.getResponse(request);
        assertEquals("The response code should indicate success",
                     MessageStoreMessageEnumeration.SUCCESS.getValue(),
                     response.getResponseCode());
        String message = response.getText();
        assertEquals("The response XML should match the supplied",
                     VALID_TEST_XML,
                     message);
    }

    /**
     * Utility method to create a POST WebRequest with specified XML and destined
     * for the specified URL.
     *
     * @param xml   XML message content
     * @param url   URL endpoint for the request
     *
     * @return      A WebRequest object
     *
     * @throws Exception
     */
    private WebRequest createPostRequest(String xml, String url) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        WebRequest req = new PostMethodWebRequest(url, bais, "text/xml");
        return req;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	829/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 19-Oct-04	205/1	matthew	VBM:2004100705 Change the message retrieval parameter from 'id' to 'pageid'

 19-Oct-04	202/1	matthew	VBM:2004100702 Stop MessageStoreServlet from responding to get requests that attempt to store a message

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
