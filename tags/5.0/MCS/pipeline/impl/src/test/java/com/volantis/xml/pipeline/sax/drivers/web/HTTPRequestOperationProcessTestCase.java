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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.synergetics.BooleanWrapper;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import com.volantis.xml.pipeline.sax.config.MockDynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.conditioners.HTMLResponseConditioner;
import com.volantis.xml.pipeline.sax.drivers.web.conditioners.XMLResponseConditioner;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcessTestAbstract;
import com.volantis.xml.pipeline.testtools.XMLHelpers;
import junitx.util.PrivateAccessor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.jdom.Document;
import org.jdom.input.SAXHandler;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Unit test case for HTTPRequestOperationProcess.
 */
public class HTTPRequestOperationProcessTestCase
        extends AbstractOperationProcessTestAbstract {

    /**
     * Construct a new instance of this test case.
     * @param name The name of this test.
     */
    public HTTPRequestOperationProcessTestCase(String name) {
        super(name);
    }

    /**
     * Create a WebDriverConfiguration for use by this testcase.
     * @return a WebDriverConfiguration.
     */
    public WebDriverConfiguration createWebDriverConfiguration() {
        return new WebDriverConfigurationImpl();
    }

    /**
     * Create a WebDriverAccessor for use by this testcase.
     * @param request The WebDriverRequest to associate with the created
     * WebDriverAccessor
     * @param response The WebDriverResponse to associate with the created
     * WebDriverAccessor.
     * @return a WebDriverAccessor
     */
    protected WebDriverAccessor
            createWebDriverAccessor(final WebDriverRequest request,
                                    final WebDriverResponse response) {

        return new WebDriverAccessor() {
            public WebDriverRequest
                    getRequest(XMLPipelineContext pipelineContext) {
                return request;
            }

            public WebDriverResponse
                    getResponse(XMLPipelineContext pipelineContext,
                                String id) {
                return response;
            }
        };
    }

    // javadoc inherited
    protected void registerConfiguration(
            XMLPipelineConfiguration configuration) {
        // register the configuration that this process requires
        configuration.storeConfiguration(WebDriverConfiguration.class,
                                         createWebDriverConfiguration());
    }

    // javadoc inherited
    protected XMLProcess createTestableProcess() {
        HTTPRequestOperationProcess process =
                new HTTPRequestOperationProcess();
        initializeProcess(process);
        return process;
    }

    /**
     * Test the setUrlString() method.
     */
    public void testSetUrlString() throws Exception {
        // Create a testable instance
        HTTPRequestOperationProcess process =
                (HTTPRequestOperationProcess) createTestableProcess();

        // Now test a variety of protocols
        String protocol = "http";
        checkProtocolMatch(process, protocol);
        protocol = "https";
        checkProtocolMatch(process, protocol);
        protocol = "ftp";
        checkProtocolMatch(process, protocol);
    }

    private void checkProtocolMatch(HTTPRequestOperationProcess process,
                                    String protocol)
            throws NoSuchFieldException {

        // Set the url (and therefore the protocol) on the process
        final String fullURL = protocol + "://www.volantis.com/";
        process.setUrlString(fullURL);

        // Check the url was as intended
        assertEquals("URLs should match " + fullURL,
                     fullURL,
                     process.getUrlString());

        // Check the protocol was set correctly
        String retrievedProtocol =
                (String) PrivateAccessor.getField(process, "protocolString");
        assertEquals("Protocols should match " + protocol,
                     protocol,
                     retrievedProtocol);
    }

    /**
     * Test the hasResource() method.
     */
    public void testHasResource() throws Throwable {
        HTTPRequestOperationProcess process =
                (HTTPRequestOperationProcess) createTestableProcess();

        String method = "hasResource";
        Class paramTypes [] = { String.class };

        Object args [] = {"http://news.bbc.co.uk"};
        Boolean result = (Boolean) PrivateAccessor.invoke(process, method,
                paramTypes, args);
        assertFalse("http://news.bbc.co.uk has no recognized resource",
                result.booleanValue());

        args = new String [] {"http://news.bbc.co.uk/hello.unknownprefix"};
        result = (Boolean) PrivateAccessor.invoke(process, method,
                paramTypes, args);
        assertFalse("http://news.bbc.co.uk/hello.unknownprefix has no " +
                "recognized resource",
                result.booleanValue());

        // Iterate over all the configured resource suffixes
        List resourceSuffixes = createWebDriverConfiguration().
                getContextChangingResourceSuffixes();
        int size = resourceSuffixes.size();
        for(int i=0; i<size; i++) {
            String suffix = (String) resourceSuffixes.get(i);
            args = new String [] {"http://news.bbc.co.uk/hello" + suffix};
            result = (Boolean) PrivateAccessor.invoke(process, method,
                    paramTypes, args);
            assertTrue(args[0] + " has a recognized resource",
                    result.booleanValue());
        }
    }

    /**
     * Override testStopProcess() since it is not possible for the parent
     * class to run stopProcess() successfully since it does not know how
     * to set up the RequestOperationProcess.
     */
    public void testStopProcess() {
        // Do nothing. stopProcess() is tested by several different methods.
    }

    /**
     * Test retrieveContentAction.
     */
    public void testRetrieveContentAction() throws Throwable {

        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();

        XMLPipelineContext context = process.getPipelineContext();

        // Testing empty map
        String contentType = "text/html; charset=\"utf-8\"";

        String method = "retrieveContentAction";

        Class paramTypes [] = {String.class};
        Object args [] = {contentType};

        testConsume(process, method, paramTypes, args);

        // Testing empty map with null content type
        args[0] = null;

        testIgnore(process, method, paramTypes, args);

        // Testing non-empty map with all ignores and item in the map
        Map map = new HashMap();

        args[0] = contentType;
        map.put("text/html", new Content("text/html", ContentAction.IGNORE));
        context.setProperty(Content.class, map, false);

        testIgnore(process, method, paramTypes, args);

        // Testing non-empty map with all ignores and item not in the map
        args[0] = "image/gif";

        testConsume(process, method, paramTypes, args);

        // Testing non-empty map with all ignores and null content type
        args[0] = null;

        testIgnore(process, method, paramTypes, args);

        // Testing non-empty map with all consumes and item not in the map
        args[0] = "image/gif";
        map.remove("text/html");
        map.put("text/html", new Content("text/html", ContentAction.CONSUME));

        testIgnore(process, method, paramTypes, args);

        // Testing non-empty map with all consumes and item in the map
        args[0] = "text/html";

        testConsume(process, method, paramTypes, args);

        // Testing non-empty map with all consumes and null content type
        args[0] = null;

        testIgnore(process, method, paramTypes, args);

        // Testing non-empty map with both actions and item in the map (consume)
        args[0] = "text/html";
        map.put("text/xml", new Content("text/xml", ContentAction.IGNORE));

        testConsume(process, method, paramTypes, args);

        // Testing non-empty map with both actions and item in the map (ignore)
        args[0] = "text/xml";

        testIgnore(process, method, paramTypes, args);

        // Testing non-empty map with both actions and item not in the map
        args[0] = "image/gif";

        testConsume(process, method, paramTypes, args);

        // Testing non-empty map with both actions and null content type
        args[0] = null;

        testIgnore(process, method, paramTypes, args);
    }

    /**
     * Utility method for testing that ContentAction.CONSUME is being triggered.
     * @param processName The process to operate on for the test
     * @param methodName The method to call in that process
     * @param paramTypesArray The types of parameter for the given method
     * @param argsArray The actual parameters for the method
     * @throws Throwable
     */
    private void testConsume(HTTPRequestOperationProcess processName,
                             String methodName, Class[] paramTypesArray,
                             Object[] argsArray) throws Throwable {
        ContentAction action = (ContentAction)
                PrivateAccessor.invoke(processName, methodName, paramTypesArray,
                        argsArray);

        assertNotNull(action);

        assertSame("Action should be ContentAction.CONSUME",
                ContentAction.CONSUME, action);
    }

    /**
     * Utility method for testing that ContentAction.IGNORE is being triggered.
     * @param processName The process to operate on for the test
     * @param methodName The method to call in that process
     * @param paramTypesArray The types of parameter for the given method
     * @param argsArray The actual parameters for the method
     * @throws Throwable
     */
    private void testIgnore(HTTPRequestOperationProcess processName,
                            String methodName, Class[] paramTypesArray,
                            Object[] argsArray) throws Throwable {
        ContentAction action = (ContentAction)
                PrivateAccessor.invoke(processName, methodName, paramTypesArray,
                        argsArray);

        assertNotNull(action);

        assertSame("Action should be ContentAction.IGNORE",
                ContentAction.IGNORE, action);
    }

    /**
     * Command interface for testing the
     * {@link HTTPRequestOperationProcess#processResponse} method.
     */
    protected interface ProcessResponseTestCommand {
        public void execute(ByteArrayInputStream is,
                            String input,
                            HTTPRequestOperationProcess process,
                            XMLPipelineContext context,
                            WebDriverAccessor accessor,
                            XMLProcess consumer,
                            SAXHandler handler,
                            WebDriverConfiguration config) throws Exception;
    }

    /**
     * Carry out the standard initialisation of an
     * {@link HTTPRequestOperationProcess} and execute a test against it.
     * @param inputText The text to be used as input to the processing
     * @param comm Command object containing code to be tested
     * @throws Exception if an error occurs
     */
    private void executeProcessResponseTestCommand(String inputText,
                                    ProcessResponseTestCommand comm)
            throws Exception {
        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();
        SAXHandler handler = new SAXHandler();
        XMLProcess consumer = XMLHelpers.createSAXHandlerProcess(handler);

        WebDriverAccessor accessor = createWebDriverAccessor(
                new WebDriverRequestImpl(), new WebDriverResponseImpl());
        XMLPipelineContext context = process.getPipelineContext();
        context.setProperty(WebDriverAccessor.class, accessor, false);

        XMLPipelineConfiguration pipelineConfig =
                context.getPipelineConfiguration();
        WebDriverConfiguration config = (WebDriverConfiguration)pipelineConfig.
                retrieveConfiguration(WebDriverConfiguration.class);
        config.setIgnoreContentEnabled(true);

        ByteArrayInputStream is =
                new ByteArrayInputStream(inputText.getBytes());
        comm.execute(is,
                inputText,
                process,
                context,
                accessor,
                consumer,
                handler,
                config);
    }

    /**
     * Test processResponse with an text/html content type with a nested
     * anchor.
     */
    public void testProcessResponseHTMLNestedAnchor() throws Throwable {
        String docType = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.0 Strict//EN\"" +
                "http://www.w3.org/TR/REC-html40/loose.dtd\">";

        String input = docType +
                "<html><head><title>The title<title>" +
                "</head><h2>A Heading<h2><p>A paragraph<br>" +
                "Link <a href=nothing1.html>Nothing 1 <a href=nested>" +
                "nested anchor</a><p>an anchored p</a></html>";

        ProcessResponseTestCommand comm = new ProcessResponseTestCommand() {
            public void execute(ByteArrayInputStream is,
                                String inputText,
                                HTTPRequestOperationProcess process,
                                XMLPipelineContext context,
                                WebDriverAccessor accessor,
                                XMLProcess consumer,
                                SAXHandler handler,
                                WebDriverConfiguration config) throws Exception {
                // Initialise the contexts for IGNORE/CONSUME
                String contentType = "text/html";
                Map map = new HashMap();

                // Test with IGNORE set
                map.put(contentType, new Content(contentType, ContentAction.IGNORE));
                context.setProperty(Content.class, map, false);

                process.setNextProcess(consumer);
                process.processResponse(null, is, 200, "text/html", null);

                Document jdomDoc;

                try {
                    jdomDoc = handler.getDocument();
                    fail("This shouldn't happen as an exception is expected on" +
                            "a null document");
                } catch (Throwable t) {
                    // expected condition
                }

                WebDriverResponse response =
                        accessor.getResponse(context,process.getId());
                ByteArrayInputStream rs =
                        (ByteArrayInputStream) response.getIgnoredContent();

                // Recreate stream as the existing one has been consumed
                is = new ByteArrayInputStream(inputText.getBytes());

                int inputsize = is.available();
                byte[] inputBytes = new byte[inputsize];
                is.read(inputBytes, 0, inputsize - 1);

                int outputsize = rs.available();
                byte[] outputBytes = new byte[outputsize];
                rs.read(outputBytes, 0, outputsize - 1);

                assertTrue("ByteArrays of input and ignored streams should match\n"
                        + "input :size=" + inputsize + ", output: size=" + outputsize,
                        Arrays.equals(inputBytes, outputBytes));

                // The expected string adds empty text nodes using \n characters
                // because JTidy does this.
                String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<html>\n<head>\n<meta name=\"generator\" " +
                        "content=\"HTML Tidy, see www.w3.org\"/>\n" +
                        "<title>The title</title>\n<title />\n</head>\n<body>\n" +
                        "<h2>A Heading</h2>\n\n<p>A paragraph<br/>\n" +
                        "Link <a href=\"nothing1.html\">Nothing 1 nested anchor</a>" +
                        "</p>\n\n<p>an anchored p</p>\n</body>\n</html>\n";

                // Test with CONSUME set
                map.remove(contentType);
                map.put(contentType, new Content(contentType, ContentAction.CONSUME));
                context.setProperty(Content.class, map, false);

                is = new ByteArrayInputStream(inputText.getBytes());
                process.processResponse(null, is, 200, "text/html", null);

                jdomDoc = handler.getDocument();

                XMLHelpers.assertEquals(expected, jdomDoc);
            }
        };
        executeProcessResponseTestCommand(input, comm);
    }

    /**
     * Test processResponse with a null content type (should ignore the
     * content).
     */
    public void testProcessResponseNullContentType() throws Throwable {
        String docType = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.0 Strict//EN\"" +
                "http://www.w3.org/TR/REC-html40/loose.dtd\">";

        String input = docType +
                "<html><head><title>The title<title>" +
                "</head><h2>A Heading<h2><p>A paragraph<br>" +
                "Link <a href=nothing1.html>Nothing 1 <a href=nested>" +
                "nested anchor</a><p>an anchored p</a></html>";

        ProcessResponseTestCommand comm = new ProcessResponseTestCommand() {
            public void execute(ByteArrayInputStream is,
                                String input,
                                HTTPRequestOperationProcess process,
                                XMLPipelineContext context,
                                WebDriverAccessor accessor,
                                XMLProcess consumer,
                                SAXHandler handler,
                                WebDriverConfiguration config) throws Exception {
                // Initialise the contexts to consume text/html
                String contentType = "text/html";
                Map map = new HashMap();
                map.put(contentType, new Content(contentType, ContentAction.CONSUME));
                context.setProperty(Content.class, map, false);

                process.setNextProcess(consumer);
                process.processResponse(null, is, 200, null, null);

                try {
                    Document jdomDoc = handler.getDocument();
                    fail("This shouldn't happen as an exception is expected on" +
                            "a null document");
                } catch (Throwable t) {
                    // expected condition
                }

                WebDriverResponse response =
                        accessor.getResponse(context,process.getId());
                ByteArrayInputStream rs =
                        (ByteArrayInputStream) response.getIgnoredContent();

                // Recreate stream as the existing one has been consumed
                is = new ByteArrayInputStream(input.getBytes());

                int inputsize = is.available();
                byte[] inputBytes = new byte[inputsize];
                is.read(inputBytes, 0, inputsize - 1);

                int outputsize = rs.available();
                byte[] outputBytes = new byte[outputsize];
                rs.read(outputBytes, 0, outputsize - 1);

                assertTrue("ByteArrays of input and ignored streams should match\n"
                        + "input :size=" + inputsize + ", output: size=" + outputsize,
                        Arrays.equals(inputBytes, outputBytes));
            }
        };

        executeProcessResponseTestCommand(input, comm);
    }

    /**
     * Test processResponse with an text/html content type with a nested
     * anchor and a non-nested anchor.
     */
    public void testProcessResponseHTMLNonNestedAnchor() throws Throwable {
        String docType = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.0 Strict//EN\"" +
                "http://www.w3.org/TR/REC-html40/loose.dtd\">";

        String input = docType +
                "<html><head><title>The title<title>" +
                "</head><h2>A Heading<h2><p>A paragraph<br>" +
                "Link <a href=nothing1.html>Nothing 1</a> <a href=nested>" +
                "nested anchor</a><p>a p</html>";

        ProcessResponseTestCommand comm = new ProcessResponseTestCommand() {
            public void execute(ByteArrayInputStream is,
                                String input,
                                HTTPRequestOperationProcess process,
                                XMLPipelineContext context,
                                WebDriverAccessor accessor,
                                XMLProcess consumer,
                                SAXHandler handler,
                                WebDriverConfiguration config) throws Exception {
                // Initialise the contexts for IGNORE/CONSUME
                String contentType = "text/html";
                Map map = new HashMap();

                // Test with IGNORE set
                map.put(contentType, new Content(contentType, ContentAction.IGNORE));
                context.setProperty(Content.class, map, false);

                process.setNextProcess(consumer);
                process.processResponse(null, is, 200, "text/html", null);

                Document jdomDoc;

                try {
                    jdomDoc = handler.getDocument();
                    fail("This shouldn't happen as an exception is expected on" +
                            "a null document");
                } catch (Throwable t) {
                    // expected condition
                }

                WebDriverResponse response =
                        accessor.getResponse(context,process.getId());
                ByteArrayInputStream rs =
                        (ByteArrayInputStream) response.getIgnoredContent();

                // Recreate stream as the existing one has been consumed
                is = new ByteArrayInputStream(input.getBytes());

                int inputsize = is.available();
                byte[] inputBytes = new byte[inputsize];
                is.read(inputBytes, 0, inputsize - 1);

                int outputsize = rs.available();
                byte[] outputBytes = new byte[outputsize];
                rs.read(outputBytes, 0, outputsize - 1);

                assertTrue("ByteArrays of input and ignored streams should match\n"
                        + "input :size=" + inputsize + ", output: size=" + outputsize,
                        Arrays.equals(inputBytes, outputBytes));

                // The expected string adds empty text nodes using \n characters
                // because JTidy does this.
                String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<html>\n<head>\n<meta name=\"generator\" " +
                        "content=\"HTML Tidy, see www.w3.org\"/>\n" +
                        "<title>The title</title>\n<title />\n</head>\n<body>\n" +
                        "<h2>A Heading</h2>\n\n<p>A paragraph<br/>\n" +
                        "Link <a href=\"nothing1.html\">Nothing 1</a> " +
                        "<a href=\"nested\">nested anchor</a>" +
                        "</p>\n\n<p>a p</p>\n</body>\n</html>\n";

                // Test with CONSUME set
                map.remove(contentType);
                map.put(contentType, new Content(contentType, ContentAction.CONSUME));
                context.setProperty(Content.class, map, false);

                is = new ByteArrayInputStream(input.getBytes());
                process.processResponse(null, is, 200, "text/html", null);

                jdomDoc = handler.getDocument();

                XMLHelpers.assertEquals(expected, jdomDoc);
            }
        };
        executeProcessResponseTestCommand(input, comm);
    }

    /**
     * Test processResponse with an text/html content type with a nested
     * anchor.
     */
    public void testProcessResponseIgnoreFlagTrue() throws Throwable {
        String docType = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.0 Strict//EN\"" +
                "http://www.w3.org/TR/REC-html40/loose.dtd\">";

        String input = docType +
                "<html><head><title>The title<title>" +
                "</head><h2>A Heading<h2><p>A paragraph<br>" +
                "Link <a href=nothing1.html>Nothing 1 <a href=nested>" +
                "nested anchor</a><p>an anchored p</a></html>";

        ProcessResponseTestCommand comm = new ProcessResponseTestCommand() {
            public void execute(ByteArrayInputStream is,
                                String input,
                                HTTPRequestOperationProcess process,
                                XMLPipelineContext context,
                                WebDriverAccessor accessor,
                                XMLProcess consumer,
                                SAXHandler handler,
                                WebDriverConfiguration config) throws Exception {
                // Initialise the contexts for IGNORE/CONSUME content types
                String contentType = "text/html";
                Map map = new HashMap();

                // Test with IGNORE content type set
                map.put(contentType, new Content(contentType, ContentAction.IGNORE));
                context.setProperty(Content.class, map, true);

                process.setNextProcess(consumer);
                process.processResponse(null, is, 200, "text/html", null);

                Document jdomDoc;

                try {
                    jdomDoc = handler.getDocument();
                    fail("This shouldn't happen as an exception is expected on" +
                            "a null document");
                } catch (Throwable t) {
                    // expected condition
                }

                WebDriverResponse response =
                        accessor.getResponse(context,process.getId());
                ByteArrayInputStream rs =
                        (ByteArrayInputStream) response.getIgnoredContent();

                // Recreate stream as the existing one has been consumed
                is = new ByteArrayInputStream(input.getBytes());

                int inputsize = is.available();
                byte[] inputBytes = new byte[inputsize];
                is.read(inputBytes, 0, inputsize - 1);

                int outputsize = rs.available();
                byte[] outputBytes = new byte[outputsize];
                rs.read(outputBytes, 0, outputsize - 1);

                assertTrue("ByteArrays of input and ignored streams should match\n"
                        + "input :size=" + inputsize + ", output: size=" + outputsize,
                        Arrays.equals(inputBytes, outputBytes));

                // Test with CONSUME set on the content type but still with the
                // ignore flag set so it should again copy the content to the ignore
                // buffer
                map.remove(contentType);
                map.put(contentType, new Content(contentType, ContentAction.CONSUME));
                context.setProperty(Content.class, map, false);
                // we want to ignore the content so set the status code to 201
                is = new ByteArrayInputStream(input.getBytes());
                process.processResponse(null, is, 201, "text/html", null);

                jdomDoc = handler.getDocument();

                try {
                    jdomDoc = handler.getDocument();
                    fail("This shouldn't happen as an exception is expected on" +
                            "a null document");
                } catch (Throwable t) {
                    // expected condition
                }

                response = accessor.getResponse(context,process.getId());
                rs = (ByteArrayInputStream) response.getIgnoredContent();

                outputsize = rs.available();
                outputBytes = new byte[outputsize];
                rs.read(outputBytes, 0, outputsize - 1);

                assertTrue("ByteArrays of input and ignored streams should match\n"
                        + "input :size=" + inputsize + ", output: size=" + outputsize,
                        Arrays.equals(inputBytes, outputBytes));
            }
        };

        executeProcessResponseTestCommand(input, comm);
    }

    /**
     * Test that the method processResponse correctly sets the char encoding
     * value from WebDriverConfig where it has been set.
     */
    public void testProcessResponseConfigDefinesCharEncoding() throws Throwable {
        String docType = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.0 Strict//EN\"" +
                "http://www.w3.org/TR/REC-html40/loose.dtd\">";

        String input = docType +
                "<html><head><title>The title<title>" +
                "</head><h2>A Heading<h2><p>A paragraph<br>" +
                "Link <a href=nothing1.html>Nothing 1 <a href=nested>" +
                "nested anchor</a><p>an anchored p</a></html>";

        ProcessResponseTestCommand comm = new ProcessResponseTestCommand() {
            public void execute(ByteArrayInputStream is,
                                String input,
                                HTTPRequestOperationProcess process,
                                XMLPipelineContext context,
                                WebDriverAccessor accessor,
                                XMLProcess consumer,
                                SAXHandler handler,
                                WebDriverConfiguration config) throws Exception {
                final String characterEncoding = "mycharencoding";
                config.setCharacterEncoding(characterEncoding);

                final BooleanWrapper charEncodingSet = new BooleanWrapper(false);

                final XMLFilterImpl xmlFilter = new XMLFilterImpl();
                final XMLResponseConditioner xmlConditioner =
                        new XMLResponseConditioner(xmlFilter) {
                            // javadoc inherited
                            public void condition(InputSource source, XMLProcess consumer)
                                    throws IOException, SAXException {
                                String encoding = source.getEncoding();
                                charEncodingSet.setValue(
                                        encoding.equals(characterEncoding));
                            }
                };

                WebDriverConditionerFactory factory =
                        new WebDriverConditionerFactory() {
                    public ContentConditioner createConditioner(XMLFilter filter) {
                        return xmlConditioner;
                    }
                };

                String contentType = "xml/xdime";
                config.addWebDriverConditionerFactory(contentType, factory);

                process.setNextProcess(consumer);
                process.processResponse(null, is, 200, contentType, null);

                assertTrue("Expected char encoding to be set.",
                        charEncodingSet.getValue());
            }
        };
        executeProcessResponseTestCommand(input, comm);
    }

    /**
     * Test the process response if the responseContainsPipelineMarkup config
     * value is set or not set.
     */
    public void testProcessResponseConfigDefinesResponseContainsPipelineMarkup()
            throws Throwable {

        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();
        SAXHandler handler = new SAXHandler();
        XMLProcess consumer = XMLHelpers.createSAXHandlerProcess(handler);

        WebDriverAccessor accessor = createWebDriverAccessor(
                new WebDriverRequestImpl(), new WebDriverResponseImpl());
        XMLPipelineContext context = process.getPipelineContext();
        context.setProperty(WebDriverAccessor.class, accessor, false);

        XMLPipelineConfiguration pipelineConfig =
                context.getPipelineConfiguration();
        WebDriverConfiguration config = (WebDriverConfiguration)pipelineConfig.
                retrieveConfiguration(WebDriverConfiguration.class);
        pipelineConfig.storeConfiguration(DynamicProcessConfiguration.class,
                new MockDynamicProcessConfiguration());

        // Set the configuration:
        // ignoreContentEnabled = true
        // responseContaintsPipelineMarkup = false
        config.setIgnoreContentEnabled(false);
        config.setResponseContainsPipelineMarkup(false);

        process.setNextProcess(consumer);

        String docType = "<!DOCTYPE html PUBLIC " +
                "\"-//W3C//DTD HTML 4.0 Strict//EN\"" +
                "http://www.w3.org/TR/REC-html40/loose.dtd\">";

        String input = docType +
                "<html><head><title>The title<title>" +
                "</head><h2>A Heading<h2><p>A paragraph<br>" +
                "Link <a href=nothing1.html>Nothing 1 <a href=nested>" +
                "nested anchor</a><p>an anchored p</a></html>";

        String contentType = "text/html; charset=\"utf-8\"";
        ByteArrayInputStream is = new ByteArrayInputStream(input.getBytes());
        assertEquals("Next process should match",
                consumer, process.getNextProcess());

        process.processResponse(null, is, 200, "text/html", null);
        assertEquals("Next process should match",
                consumer, process.getNextProcess().getNextProcess());

        // Now modify the configuration so that the pipeline process is inserted.
        config.setResponseContainsPipelineMarkup(true);
        process.setNextProcess(consumer);

        is = new ByteArrayInputStream(input.getBytes());
        process.processResponse(null, is, 200, "text/html", null);
        assertNotEquals(consumer, process.getNextProcess());
        assertEquals("Next process should match",
                consumer, process.getNextProcess().getNextProcess().
                getNextProcess());
    }


    /**
     * Test the GZIP response content encoding handling.
     */
    public void testGZIPResponse() throws Exception {
        HTTPRequestOperationProcess process =
                (HTTPRequestOperationProcess) createTestableProcess();

        SAXHandler handler = new SAXHandler();
        XMLProcess consumer = XMLHelpers.createSAXHandlerProcess(handler);
        process.setNextProcess(consumer);

        final String input =
                "<html>" +
                "<head>" +
                    "<title>The title<title>" +
                "</head>" +
                "<h2>A Heading<h2>" +
                "<p>A paragraph<br>Link " +
                    "<a href=nothing1.html>Nothing 1 " +
                        "<a href=nested>nested anchor</a>" +
                        "<p>an anchored p" +
                    "</a>" +
                "</html>";

        // Create a compressed gzip response stream.
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(input.getBytes());
        gzip.flush();
        gzip.close();
        InputStream responseStream = new ByteArrayInputStream(out.toByteArray());
        process.processResponse(null, responseStream, 200, "text/html", "gzip");

        assertEquals("Next process should match",
                consumer, process.getNextProcess().getNextProcess());

        XMLOutputter outputter = new XMLOutputter();
        outputter.setTrimAllWhite(true);
        outputter.setNewlines(false);
        outputter.setLineSeparator("");
        final String expected =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<html>" +
                "<head>" +
                    "<meta name=\"generator\" content=\"HTML Tidy, see www.w3.org\" />" +
                    "<title>The title</title>" +
                    "<title />" +
                "</head>" +
                "<body>" +
                    "<h2>A Heading</h2>" +
                        "<p>A paragraph<br />\nLink " +
                            "<a href=\"nothing1.html\">Nothing 1 nested anchor</a>" +
                        "</p>" +
                        "<p>an anchored p</p>" +
                "</body>" +
            "</html>";
        String actual = outputter.outputString(handler.getDocument());
        assertEquals("Response read should match original input\n",
                     expected, actual);
    }

    /**
     * Test the method shouldIgnoreContent.
     */
    public void testShouldIgnoreContent() throws Exception {
        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();

        XMLPipelineContext context = process.getPipelineContext();
        XMLPipelineConfiguration pipelineConfig =
                context.getPipelineConfiguration();

        WebDriverConfiguration config = (WebDriverConfiguration)pipelineConfig.
                retrieveConfiguration(WebDriverConfiguration.class);

        // Config ignore = true
        // process ignore = not set
        boolean ignore = process.shouldIgnoreContent(200);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(300);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(399);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(404);
        assertTrue("Should ignore content.", ignore);
        ignore = process.shouldIgnoreContent(304);
        assertTrue("Should ignore content.", ignore);

        // config ignore = false
        // process ignore = not set
        config.setIgnoreErroredContent(false);
        ignore = process.shouldIgnoreContent(200);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(300);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(399);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(404);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(304);
        assertTrue("Should ignore content.", ignore);

        // config ignore = false
        // process ignore = true
        process.setIgnoreErroredContent("true");
        ignore = process.shouldIgnoreContent(200);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(300);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(399);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(404);
        assertTrue("Should ignore content.", ignore);
        ignore = process.shouldIgnoreContent(304);
        assertTrue("Should ignore content.", ignore);

        // config ignore = true
        // process ignore = true
        config.setIgnoreErroredContent(true);
        ignore = process.shouldIgnoreContent(200);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(300);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(399);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(404);
        assertTrue("Should ignore content.", ignore);
        ignore = process.shouldIgnoreContent(304);
        assertTrue("Should ignore content.", ignore);

        process = (HTTPRequestOperationProcess) createTestableProcess();
        context = process.getPipelineContext();
        pipelineConfig = context.getPipelineConfiguration();
        config = (WebDriverConfiguration)pipelineConfig.
                retrieveConfiguration(WebDriverConfiguration.class);

        process.setIgnoreErroredContent("false");
        // config ignore = true
        // process ignore = false
        config.setIgnoreErroredContent(false);
        ignore = process.shouldIgnoreContent(200);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(300);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(399);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(404);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(304);
        assertTrue("Should ignore content.", ignore);

        config.setIgnoreErroredContent(false);
        // config ignore = false
        // process ignore = false
        config.setIgnoreErroredContent(false);
        ignore = process.shouldIgnoreContent(200);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(300);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(399);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(404);
        assertFalse("Should not ignore content.", ignore);
        ignore = process.shouldIgnoreContent(304);
        assertTrue("Should ignore content.", ignore);
    }

    /**
     * Test the method createContentConditioner.
     */
    public void testCreateContentConditioner() throws Throwable {
        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();

        WebDriverAccessor accessor = createWebDriverAccessor(
                new WebDriverRequestImpl(), new WebDriverResponseImpl());
        XMLPipelineContext context = process.getPipelineContext();
        context.setProperty(WebDriverAccessor.class, accessor, false);

        // get hold of the pipeline configuration
        XMLPipelineConfiguration pipelineConfiguration =
                context.getPipelineConfiguration();

        WebDriverConfiguration
                configuration = (WebDriverConfiguration)pipelineConfiguration.
                retrieveConfiguration(WebDriverConfiguration.class);

        final XMLFilterImpl xmlFilter = new XMLFilterImpl();
        final XMLResponseConditioner xmlConditioner =
                new XMLResponseConditioner(xmlFilter);

        WebDriverConditionerFactory factory =
                new WebDriverConditionerFactory() {
            public ContentConditioner createConditioner(XMLFilter filter) {
                return xmlConditioner;
            }
        };

        String customContentType = "xml/xdime";
        configuration.addWebDriverConditionerFactory(customContentType, factory);

        String method = "createContentConditioner";
        Class paramTypes [] = {String.class, XMLFilter.class};

        Object args [] = {customContentType, xmlFilter};
        ContentConditioner actual = (ContentConditioner)
                PrivateAccessor.invoke(process, method, paramTypes, args);
        assertSame("Unexpected conditioner returned.", xmlConditioner, actual);

        // check that we still get the custom conditioner when the content type
        // has extra attributes.
        args[0] = customContentType + "; charset=\"utf8\"";
        actual = (ContentConditioner)
                PrivateAccessor.invoke(process, method, paramTypes, args);
        assertSame("Unexpected conditioner returned.", xmlConditioner, actual);

        // Check that we get an HTMLResponseConditioner for text/html
        args[0] = "text/html; charset=\"utf8\"";
        actual = (ContentConditioner)
                PrivateAccessor.invoke(process, method, paramTypes, args);
        assertEquals("Unexpected conditioner returned.",
                HTMLResponseConditioner.class, actual.getClass());

        // Check that we get an XMLResponseConditioner for anything else
        args[0] = "text/plain; charset=\"utf8\"";
        actual = (ContentConditioner)
                PrivateAccessor.invoke(process, method, paramTypes, args);
        assertEquals("Unexpected conditioner returned.",
                XMLResponseConditioner.class, actual.getClass());
    }

    /**
     * Test the addRequestCookiesToHttpMethod method with cookies from the
     * context only.
     */
    /*
    public void testAddRequestCookiesToHttpMethodNoWebRequest()
            throws Throwable {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        String name1 = "name1";
        String name2 = "name2";
        String value1a = "value1a";
        String value1b = "value1b";
        String value2 = "value2";

        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
        WebRequestCookie cookie = new WebRequestCookie();
        cookie.setName(name1);
        cookie.setValue(value1a);
        entities.add(cookie);

        cookie = new WebRequestCookie();
        cookie.setName(name1);
        cookie.setValue(value1b);
        entities.add(cookie);

        cookie = new WebRequestCookie();
        cookie.setName(name2);
        cookie.setValue(value2);
        entities.add(cookie);

        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();

        XMLPipelineContext context = process.getPipelineContext();

        context.setProperty(WebRequestCookie.class, entities, false);

        String url = "http://www.volantis.com/index.jsp";
        process.setUrlString(url);
        GetMethod getMethod = invokeGetMethod(process, url);
        assertNotNull(getMethod);

        String method = "addRequestCookiesToHttpMethod";

        Class[] paramTypes = {HttpMethod.class};
        Object[] args = {getMethod};

        PrivateAccessor.invoke(process, method, paramTypes, args);

        our.apache.commons.httpclient.Header cookies [] =
                getMethod.getRequestHeaders();


        assertEquals("There should be 3 cookies", 3, cookies.length);

        // I'm assuming the order the cookies went in are the order
        // they come out which seems to be true at the time of writing.
        assertEquals("Checking cookie 0", "name1=value1a",
                cookies[0].getValue());
        assertEquals("Checking cookie 1", "name1=value1b",
                cookies[1].getValue());
        assertEquals("Checking cookie 2", "name2=value2",
                cookies[2].getValue());
    }
*/

    /**
     * Test the addRequestCookiesToHttpMethod method with a WebDriverRequest
     * that contains cookies that are referenced from the markup but where the
     * markup cookies do not overlap with the WebDriverRequest cookies.
     */
/*
    public void testAddRequestCookiesToHttpMethodWithWebRequest()
            throws Throwable {
        HTTPFactory factory = HTTPFactory.getDefaultInstance();
        String name1 = "name1";
        String name2 = "name2";
        String value1 = "value1";
        String value2 = "value2";

        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
        WebRequestCookie cookie = new WebRequestCookie();
        cookie.setName(name1);
        cookie.setFrom(name1);
        entities.add(cookie);

        cookie = new WebRequestCookie();
        cookie.setName(name2);
        cookie.setFrom(name2);
        entities.add(cookie);

        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
                createTestableProcess();

        XMLPipelineContext context = process.getPipelineContext();

        context.setProperty(WebRequestCookie.class, entities, false);

        WebDriverRequest request = new WebDriverRequestImpl();
        WebDriverAccessor accessor = createWebDriverAccessor(request, null);

        context.setProperty(WebDriverAccessor.class, accessor, false);

        HTTPMessageEntities requestEntities =
                factory.createHTTPMessageEntities();
        request.setCookies(requestEntities);

        cookie = new WebRequestCookie();
        cookie.setName(name1);
        cookie.setValue(value1);
        requestEntities.add(cookie);

        cookie = new WebRequestCookie();
        cookie.setName(name2);
        cookie.setValue(value2);
        requestEntities.add(cookie);

        String url = "http://www.volantis.com/index.jsp";
        process.setUrlString(url);
        GetMethod getMethod = invokeGetMethod(process, url);
        assertNotNull(getMethod);

        String method = "addRequestCookiesToHttpMethod";

        Class[] paramTypes = {HttpMethod.class};
        Object[] args =  {getMethod};

        PrivateAccessor.invoke(process, method, paramTypes, args);

        our.apache.commons.httpclient.Header cookies [] =
                getMethod.getRequestHeaders();


        assertEquals("There should be 2 cookies", 2, cookies.length);

        // I'm assuming the order the cookies went in are the order
        // they come out which seems to be true at the time of writing.
        assertEquals("Checking cookie 0", "name1=value1",
                cookies[0].getValue());
        assertEquals("Checking cookie 2", "name2=value2",
                cookies[1].getValue());
    }
*/
//    /**
//     * Test the addRequestHeadersToHttpMethod method with headers from the
//     * context only.
//     */
//    public void testAddRequestHeadersToHttpMethodNoWebRequest()
//            throws Throwable {
//        HTTPFactory factory = HTTPFactory.getDefaultInstance();
//        String name1 = "name1";
//        String name2 = "name2";
//        String value1a = "value1a";
//        String value1b = "value1b";
//        String value2 = "value2";
//
//        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
//        WebRequestHeader header = new WebRequestHeader();
//        header.setName(name1);
//        header.setValue(value1a);
//        entities.add(header);
//
//        header = new WebRequestHeader();
//        header.setName(name1);
//        header.setValue(value1b);
//        entities.add(header);
//
//        header = new WebRequestHeader();
//        header.setName(name2);
//        header.setValue(value2);
//        entities.add(header);
//
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        context.setProperty(WebRequestHeader.class, entities, false);
//
//
//        String url = "http://www.volantis.com/index.jsp";
//        process.setUrlString(url);
//        GetMethod getMethod = invokeGetMethod(process, url);
//        assertNotNull(getMethod);
//
//        String method = "addRequestHeadersToHttpMethod";
//
//        Class paramTypes [] = {HttpMethod.class};
//        Object args [] = {getMethod};
//
//        PrivateAccessor.invoke(process, method, paramTypes, args);
//
//        our.apache.commons.httpclient.Header methodHeaders [] =
//                getMethod.getRequestHeaders();
//
//        assertEquals("There should be 3 headers", 3, methodHeaders.length);
//        assertNotNull(getMethod.getRequestHeader(name1));
//        assertNotNull(getMethod.getRequestHeader(name2));
//        assertEquals("Value should match", "value2",
//                getMethod.getRequestHeader(name2).getValue());
//    }
//
//    private GetMethod invokeGetMethod(HTTPRequestOperationProcess process,
//                                      String url) throws Throwable {
//        Class paramTypes [] = {String.class};
//        Object args [] = {url};
//        return (GetMethod) PrivateAccessor.invoke(process,
//                "createGetMethod", paramTypes, args);
//    }

//    private PostMethod invokePostMethod(HTTPRequestOperationProcess process,
//                                   String url) throws Throwable {
//        Class paramTypes [] = {String.class};
//        Object args [] = {url};
//        return (PostMethod) PrivateAccessor.invoke(process,
//                "createPostMethod", paramTypes, args);
//    }

//    /**
//     * Test the addRequestHeadersToHttpMethod method with a WebDriverRequest
//     * that contains headers that are referenced from the markup but where the
//     * markup headers do not overlap with the WebDriverRequest headers.
//     */
//    public void testAddRequestHeadersToHttpMethodWithWebRequest()
//            throws Throwable {
//        HTTPFactory factory = HTTPFactory.getDefaultInstance();
//        String name1 = "name1";
//        String name2 = "name2";
//        String value1a = "value1a";
//        String value1b = "value1b";
//        String value2 = "value2";
//
//        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
//        WebRequestHeader header = new WebRequestHeader();
//        header.setName(name1);
//        header.setFrom(name1);
//        entities.add(header);
//
//        header = new WebRequestHeader();
//        header.setName(name1);
//        header.setFrom(name1);
//        entities.add(header);
//
//        header = new WebRequestHeader();
//        header.setName(name2);
//        header.setFrom(name2);
//        entities.add(header);
//
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        context.setProperty(WebRequestHeader.class, entities, false);
//
//
//        WebDriverRequest request = new WebDriverRequestImpl();
//        WebDriverAccessor accessor = createWebDriverAccessor(request, null);
//
//        context.setProperty(WebDriverAccessor.class, accessor, false);
//
//        HTTPMessageEntities requestEntities =
//                factory.createHTTPMessageEntities();
//        request.setHeaders(requestEntities);
//
//        header = new WebRequestHeader();
//        header.setName(name1);
//        header.setValue(value1a);
//        requestEntities.add(header);
//
//
//        header = new WebRequestHeader();
//        header.setName(name1);
//        header.setValue(value1b);
//        requestEntities.add(header);
//
//        header = new WebRequestHeader();
//        header.setName(name2);
//        header.setValue(value2);
//        requestEntities.add(header);
//
//        String url = "http://www.volantis.com/index.jsp";
//        process.setUrlString(url);
//        GetMethod getMethod = invokeGetMethod(process, url);
//        assertNotNull(getMethod);
//
//        String method = "addRequestHeadersToHttpMethod";
//
//        Class paramTypes [] = {HttpMethod.class};
//        Object args [] = {getMethod};
//
//        PrivateAccessor.invoke(process, method, paramTypes, args);
//
//        our.apache.commons.httpclient.Header methodHeaders [] =
//                getMethod.getRequestHeaders();
//
//        assertEquals("There should be 3 headers", 3, methodHeaders.length);
//        assertNotNull(getMethod.getRequestHeader(name1));
//        assertNotNull(getMethod.getRequestHeader(name2));
//        assertEquals("Value should match", "value2",
//                getMethod.getRequestHeader(name2).getValue());
//    }

//    /**
//     * Test that HttpClient headers and cookies from an HttpMethod response are
//     * correctly added to the WebDriverResponse.
//     */
//    public void testAddHeadersToWebDriverResponse() throws Throwable {
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        WebDriverRequest request = new WebDriverRequestImpl();
//        WebDriverResponseImpl response = new WebDriverResponseImpl();
//        WebDriverAccessor accessor = createWebDriverAccessor(request, response);
//        context.setProperty(WebDriverAccessor.class, accessor, false);
//
//        String cookieName = "Set-Cookie";
//        String cookieValue = "name1=value1,name2=value2";
//        final our.apache.commons.httpclient.Header cookieHeader =
//                new our.apache.commons.httpclient.Header();
//        cookieHeader.setName(cookieName);
//        cookieHeader.setValue(cookieValue);
//
//        String headerName = "Other-Header";
//        String headerValue = "o-h-value";
//        final our.apache.commons.httpclient.Header otherHeader =
//                new our.apache.commons.httpclient.Header();
//        otherHeader.setName(headerName);
//        otherHeader.setValue(headerValue);
//
//        HttpMethod httpMethod =
//                new GetMethod("https://wwww.volantis.com/index.jsp") {
//            public Header[] getResponseHeaders() {
//                return new Header[] {cookieHeader, otherHeader};
//            }
//        };
//
//        String method = "addHeadersToWebDriverResponse";
//        Class paramTypes [] = {HttpMethod.class, List.class};
//        Object args [] = {httpMethod, new ArrayList()};
//        PrivateAccessor.invoke(process, method, paramTypes, args);
//
//        HTTPMessageEntities responseHeaders = response.getHeaders();
//        HTTPMessageEntities responseCookies = response.getCookies();
//
//        assertTrue("Expected one header.", responseHeaders.size() == 1);
//        assertTrue("Expected two cookies.", responseCookies.size() == 2);
//
//        Iterator headers = responseHeaders.iterator();
//        while (headers.hasNext()) {
//            HeaderImpl header = (HeaderImpl) headers.next();
//            assertEquals("Unexpected header name.",
//                    headerName, header.getName());
//            assertEquals("Unexpected header value.",
//                    headerValue, header.getValue());
//        }
//
//        CookieImpl[] cookies = new CookieImpl[2];
//
//        int count = 0;
//        Iterator cookiesIterator = responseCookies.iterator();
//        while (cookiesIterator.hasNext()) {
//            CookieImpl cookie = (CookieImpl) cookiesIterator.next();
//            cookies[count] = cookie;
//            count++;
//        }
//
//        assertEquals("Checking cookie 1 name", "name1",
//                cookies[0].getName());
//        assertEquals("Checking cookie 1 value", "value1",
//                cookies[0].getValue());
//        assertEquals("Checking cookie 2 name", "name2",
//                cookies[1].getName());
//        assertEquals("Checking cookie 2 value", "value2",
//                cookies[1].getValue());
//    }

//    /**
//     * Test that HttpClient headers and cookies from an HttpMethod response are
//     * correctly added to the WebDriverResponse with additional headers.
//     */
//    public void testAddHeadersToWebDriverResponseWithAdditionalHeaders()
//            throws Throwable {
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        WebDriverRequest request = new WebDriverRequestImpl();
//        WebDriverResponseImpl response = new WebDriverResponseImpl();
//        WebDriverAccessor accessor = createWebDriverAccessor(request, response);
//        context.setProperty(WebDriverAccessor.class, accessor, false);
//
//        String cookieName = "Set-Cookie";
//        String cookieValue = "name1=value1,name2=value2";
//        final our.apache.commons.httpclient.Header cookieHeader =
//                new our.apache.commons.httpclient.Header();
//        cookieHeader.setName(cookieName);
//        cookieHeader.setValue(cookieValue);
//
//        String headerName = "Other-Header";
//        String headerValue = "o-h-value";
//        final our.apache.commons.httpclient.Header otherHeader =
//                new our.apache.commons.httpclient.Header();
//        otherHeader.setName(headerName);
//        otherHeader.setValue(headerValue);
//
//        HttpMethod httpMethod =
//                new GetMethod("https://wwww.volantis.com/index.jsp") {
//            public Header[] getResponseHeaders() {
//                return new Header[] {cookieHeader, otherHeader};
//            }
//        };
//
//        our.apache.commons.httpclient.Cookie cookieArray [] = {
//            new our.apache.commons.httpclient.Cookie("additionalDomain",
//                    "additionalName", "additionalValue")
//        };
//
//        ArrayList additionalCookies = new ArrayList();
//        additionalCookies.add(cookieArray);
//
//        String method = "addHeadersToWebDriverResponse";
//        Class paramTypes [] = {HttpMethod.class, List.class};
//
//        Object args [] = {httpMethod, additionalCookies};
//        PrivateAccessor.invoke(process, method, paramTypes, args);
//
//        HTTPMessageEntities responseHeaders = response.getHeaders();
//        HTTPMessageEntities responseCookies = response.getCookies();
//
//        final int expectedCookies = 3;
//        assertEquals("Expected one header.", 1, responseHeaders.size());
//        assertEquals("Expected number of cookies to match",  expectedCookies,
//                responseCookies.size());
//
//        Iterator headers = responseHeaders.iterator();
//        while (headers.hasNext()) {
//            HeaderImpl header = (HeaderImpl) headers.next();
//            assertEquals("Unexpected header name.",
//                    headerName, header.getName());
//            assertEquals("Unexpected header value.",
//                    headerValue, header.getValue());
//        }
//
//        CookieImpl[] cookies = new CookieImpl[expectedCookies];
//
//        int count = 0;
//        Iterator cookiesIterator = responseCookies.iterator();
//        while (cookiesIterator.hasNext()) {
//            CookieImpl cookie = (CookieImpl) cookiesIterator.next();
//            cookies[count] = cookie;
//            count++;
//        }
//
//        assertEquals("Checking cookie 1 name", "additionalName", cookies[0].getName());
//        assertEquals("Checking cookie 1 value", "additionalValue", cookies[0].getValue());
//        assertEquals("Checking cookie 2 name", "name1", cookies[1].getName());
//        assertEquals("Checking cookie 2 value", "value1", cookies[1].getValue());
//        assertEquals("Checking cookie 3 name", "name2", cookies[2].getName());
//        assertEquals("Checking cookie 3 value", "value2", cookies[2].getValue());
//    }

//    /**
//     * Test the createPostMethod with no WebDriverRequest but with
//     * parameters from the context.
//     */
//    public void testCreatePostMethodNoWebRequest() throws Throwable {
//        HTTPFactory factory = HTTPFactory.getDefaultInstance();
//        String name1 = "name1";
//        String name2 = "name2";
//        String value1a = "value1a";
//        String value1b = "value1b";
//        String value2 = "value2";
//
//        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
//        WebRequestParameter param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1a);
//        entities.add(param);
//
//        param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1b);
//        entities.add(param);
//
//        param = new WebRequestParameter();
//        param.setName(name2);
//        param.setValue(value2);
//        entities.add(param);
//
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        context.setProperty(WebRequestParameter.class, entities, false);
//
//        String url = "http://www.volantis.com/index.jsp";
//        process.setUrlString(url);
//        PostMethod postMethod = invokePostMethod(process, url);
//        assertNotNull(postMethod);
//
//        assertEquals("Testing method path", "/index.jsp", postMethod.getPath());
//        assertEquals("Testing host", "www.volantis.com",
//                postMethod.getHostConfiguration().getHost());
//        assertEquals("Testing protocol", "http",
//                postMethod.getHostConfiguration().getProtocol().getScheme());
//
//        NameValuePair params [] = postMethod.getParameters();
//
//        assertEquals("There should be 3 params", 3, params.length);
//
//
//        // I'm assuming the order the params went in are the order
//        // they come out which seems to be true at the time of writing.
//        assertEquals("Testing param 0 name", "name1", params[0].getName());
//        assertEquals("Testing param 0 value", "value1a", params[0].getValue());
//        assertEquals("Testing param 1 name", "name1", params[1].getName());
//        assertEquals("Testing param 1 value", "value1b", params[1].getValue());
//        assertEquals("Testing param 2 name", "name2", params[2].getName());
//        assertEquals("Testing param 2 value", "value2", params[2].getValue());
//    }

//    /**
//     * Test the createGetMethod with no WebDriverRequest but with
//     * parameters from the context.
//     */
//    public void testCreateGetMethodNoWebRequest() throws Throwable {
//        HTTPFactory factory = HTTPFactory.getDefaultInstance();
//        String name1 = "name1";
//        String name2 = "name2";
//        String value1a = "value1a";
//        String value1b = "value1b";
//        String value2 = "value2";
//
//        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
//        WebRequestParameter param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1a);
//        entities.add(param);
//
//        param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1b);
//        entities.add(param);
//
//        param = new WebRequestParameter();
//        param.setName(name2);
//        param.setValue(value2);
//        entities.add(param);
//
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        context.setProperty(WebRequestParameter.class, entities, false);
//
//        String url = "http://www.volantis.com/index.jsp";
//        process.setUrlString(url);
//        GetMethod getMethod = invokeGetMethod(process, url);
//        assertNotNull(getMethod);
//
//        assertEquals("Testing method path", "/index.jsp", getMethod.getPath());
//        assertEquals("Testing query string",
//                "name1=value1a&name1=value1b&name2=value2",
//                getMethod.getQueryString());
//        assertEquals("Testing host", "www.volantis.com",
//                getMethod.getHostConfiguration().getHost());
//        assertEquals("Testing protocol", "http",
//                getMethod.getHostConfiguration().getProtocol().getScheme());
//    }

//    /**
//     * Test the createGetMethod with a WebDriverRequest that contains
//     * parameters that are referenced from the markup but where the markup
//     * parameters do not overlap with the WebDriverRequest paramaters.
//     */
//    public void testCreateGetMethodWithWebRequestNoOverlap() throws Throwable {
//        HTTPFactory factory = HTTPFactory.getDefaultInstance();
//        String name1 = "name1";
//        String name2 = "name2";
//        String value1a = "value1a";
//        String value1b = "value1b";
//        String value2 = "value2";
//
//        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
//        WebRequestParameter param = new WebRequestParameter();
//        param.setName(name1);
//        param.setFrom(name1);
//        entities.add(param);
//
//        param = new WebRequestParameter();
//        param.setName(name2);
//        param.setFrom(name2);
//        entities.add(param);
//
//        WebDriverRequest request = new WebDriverRequestImpl();
//        WebDriverAccessor accessor = createWebDriverAccessor(request, null);
//
//        HTTPMessageEntities requestEntities =
//                factory.createHTTPMessageEntities();
//        request.setRequestParameters(requestEntities);
//
//        param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1a);
//        requestEntities.add(param);
//
//
//        param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1b);
//        requestEntities.add(param);
//
//
//        param = new WebRequestParameter();
//        param.setName(name2);
//        param.setValue(value2);
//        requestEntities.add(param);
//
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        context.setProperty(WebDriverAccessor.class, accessor, false);
//        context.setProperty(WebRequestParameter.class, entities, false);
//
//        String url = "http://www.volantis.com/index.jsp";
//        process.setUrlString(url);
//        GetMethod getMethod = invokeGetMethod(process, url);
//
//        assertNotNull(getMethod);
//
//        assertEquals("Testing method path", "/index.jsp", getMethod.getPath());
//        assertEquals("Testing query string",
//                name1 + "=" + value1a + "&" + name1 + "=" + value1b + "&" +
//                name2 + "=" + value2, getMethod.getQueryString());
//        assertEquals("Testing host", "www.volantis.com",
//                getMethod.getHostConfiguration().getHost());
//        assertEquals("Testing protocol", "http",
//                getMethod.getHostConfiguration().getProtocol().getScheme());
//    }

//    /**
//     * Test the createGetMethod with a WebDriverRequest that contains
//     * parameters that are referenced from the markup and where a markup
//     * parameter value does overlap with a WebDriverRequest paramater value.
//     */
//    public void testCreateGetMethodWithWebRequestOverlap() throws Throwable {
//        HTTPFactory factory = HTTPFactory.getDefaultInstance();
//        String name1 = "name1";
//        String name2 = "name2";
//        String value1a = "value1a";
//        String value1b = "value1b";
//        String value2 = "value2";
//        String overlap = "overlap";
//
//        HTTPMessageEntities entities = factory.createHTTPMessageEntities();
//        WebRequestParameter param = new WebRequestParameter();
//        param.setName(name1);
//        param.setFrom(name1);
//        param.setValue(overlap);
//        entities.add(param);
//
//        param = new WebRequestParameter();
//        param.setName(name2);
//        param.setFrom(name2);
//        entities.add(param);
//
//        WebDriverRequest request = new WebDriverRequestImpl();
//        WebDriverAccessor accessor = createWebDriverAccessor(request, null);
//
//        HTTPMessageEntities requestEntities =
//                factory.createHTTPMessageEntities();
//        request.setRequestParameters(requestEntities);
//
//        param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1a);
//        requestEntities.add(param);
//
//
//        param = new WebRequestParameter();
//        param.setName(name1);
//        param.setValue(value1b);
//        requestEntities.add(param);
//
//
//        param = new WebRequestParameter();
//        param.setName(name2);
//        param.setValue(value2);
//        requestEntities.add(param);
//
//        HTTPRequestOperationProcess process = (HTTPRequestOperationProcess)
//                createTestableProcess();
//
//        XMLPipelineContext context = process.getPipelineContext();
//
//        context.setProperty(WebDriverAccessor.class, accessor, false);
//        context.setProperty(WebRequestParameter.class, entities, false);
//
//        String url = "http://www.volantis.com/index.jsp";
//        process.setUrlString(url);
//        GetMethod getMethod = invokeGetMethod(process, url);
//        assertNotNull(getMethod);
//
//        assertEquals("Testing method path", "/index.jsp", getMethod.getPath());
//        assertEquals("Testing query string",
//                name1 + "=" + overlap + "&" + name1 + "=" + overlap +
//                "&" + name2 + "=" + value2, getMethod.getQueryString());
//        assertEquals("Testing host", "www.volantis.com",
//                getMethod.getHostConfiguration().getHost());
//        assertEquals("Testing protocol", "http",
//                getMethod.getHostConfiguration().getProtocol().getScheme());
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 04-Jan-05	6569/3	pduffin	VBM:2004123101 Removed break from loop as our coding standards do not allow them except in switches. Added java doc to private test case helper method.

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Sep-04	890/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 23-Sep-04	888/1	byron	VBM:2004092003 DSB:HTTPS Requests not working

 16-Aug-04	822/5	adrianj	VBM:2004081214 Handle null content-type and empty response (rework)

 16-Aug-04	822/3	adrianj	VBM:2004081214 Handle null content-type and empty response (rework)

 16-Aug-04	822/1	adrianj	VBM:2004081214 Handle null content-type and empty response

 20-Jul-04	786/1	claire	VBM:2004071304 Refactor web driver to support HTTPS

 12-Jul-04	751/5	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 24-Jun-04	759/3	matthew	VBM:2004061101 small change to allow setIgnoreContentEnabled to work with 304 status responses

 16-Jun-04	753/1	byron	VBM:2004061401 Refactor pipeline WEBDriver to optionally process response as pipeline markup

 02-Jun-04	736/1	allan	VBM:2004060201 Fixes for dci redirection and remapping

 01-Jun-04	720/1	byron	VBM:2004052802 DCI: Have web driver track cookies set on redirects

 27-May-04	715/1	byron	VBM:2004052006 DCI: Handle relative urls in content obtained via a redirect

 31-Mar-04	656/1	adrian	VBM:2004031602 Updated pipeline with DSB enhancement requests

 26-Mar-04	644/1	adrian	VBM:2004031907 Add support to WebDriver to provide a character encoding

 25-Mar-04	640/1	adrian	VBM:2004031906 Added mechanism to allow custom conditioners to be used in WebDriver

 24-Mar-04	629/3	adrian	VBM:2004031901 enable web driver to ignore errored content

 22-Mar-04	619/1	adrian	VBM:2004031804 Set header and cookie values correctly in web driver

 23-Mar-04	631/1	allan	VBM:2004032205 Patch performance fixes from Pipeline/MCS 3.0GA

 23-Mar-04	626/3	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	626/1	allan	VBM:2004032205 Pipeline performance enhancements.

 19-Jan-04	537/1	claire	VBM:2004011919 Enhanced processResponse test cases and fixed byte array sizing

 19-Jan-04	514/5	claire	VBM:2004011514 processResponse updated and test cases changed

 16-Jan-04	514/1	claire	VBM:2004011514 WebResponseDriver updated with appropriate content

 16-Jan-04	518/1	claire	VBM:2004011515 Removed RuntimeException with null cookie expiry date

 15-Jan-04	508/1	claire	VBM:2004011501 Improved the behaviour of retrieveContentType based on content preferences

 13-Aug-03	331/2	adrian	VBM:2003081001 implemented try operation

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 13-Aug-03	351/2	byron	VBM:2003070709 Addressed setAttribute with null name and added test case

 05-Aug-03	294/3	allan	VBM:2003070709 Added test for non-nested anchor tags

 05-Aug-03	294/1	allan	VBM:2003070709 Added test for non-nested anchor tags

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	217/11	allan	VBM:2003071702 Tidied two lines and fix merge conflicts

 04-Aug-03	217/9	allan	VBM:2003071702 Filter nested anchors.

 01-Aug-03	217/7	allan	VBM:2003071702 Added more tests. Fixed a couple of bugs

 31-Jul-03	217/5	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/3	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 31-Jul-03	217/1	allan	VBM:2003071702 Ensure correct array types created. Add our-commons-logging to build

 ===========================================================================
*/
