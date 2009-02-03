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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2003030405 - Created. This class should
 *                              be used as the base class for integration
 *                              tests.
 * 30-May-03    Phil W-S        VBM:2003030610 - Provide variants of the
 *                              test methods that handle literal XML strings
 *                              (doLiteralsTest) and provide variants of the
 *                              doTest that take the error handler to be used
 *                              for a given test. Split out a factory method,
 *                              createErrorHandler, to permit a lock-stock-and-
 *                              barrel change of the error handler used for
 *                              the entire test case.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import com.volantis.xml.xml.serialize.OutputFormat;
import com.volantis.xml.xml.serialize.XMLSerializer;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class should be used as the base class for integration tests.
 */
public abstract class PipelineTestAbstract extends TestCaseAbstract {

    /**
     * The DOM factory class to use
     */
    public static final String DOM_FACTORY_CLASS =
            "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl";

    /**
     * The SAX factory class to use
     */
    public static final String SAX_FACTORY_CLASS =
            "com.volantis.xml.xerces.jaxp.SAXParserFactoryImpl";

    /**
     * The Transformer factory class to use
     */
    public static final String TRANSFORMER_FACTORY_CLASS =
            "com.volantis.xml.xalan.processor.TransformerFactoryImpl";

    /**
     * The package that the class being tested belongs to.
     */
    protected String packagePath;

    /**
     * The pipeline that will be used to perform the tests
     */
    protected XMLPipeline pipeline;

    /**
     * Create a new PipelineTestAbstract instance
     * @param name the name of the test.
     *
     * @deprecated Use no arg constructor.
     */
    public PipelineTestAbstract(String name) {
        this();
        setName(name);
    }

    /**
     * Initialise a new PipelineTestAbstract instance
     */
    protected PipelineTestAbstract() {

        packagePath = getClass().getPackage().getName().replace('.', '/');

        XMLUnit.setControlParser(DOM_FACTORY_CLASS);
        XMLUnit.setTestParser(DOM_FACTORY_CLASS);
        XMLUnit.setSAXParserFactory(SAX_FACTORY_CLASS);
        XMLUnit.setTransformerFactory(TRANSFORMER_FACTORY_CLASS);

        // ignore whitespace for now
        XMLUnit.setIgnoreWhitespace(true);
    }

    /**
     * Sends XML down the pipeline as specified by the PipelineCongfiguration
     * and XMLPipelineFactory and checks that the output is as expected. The
     * XML used as the input is found in the testsuite jar by looking for a
     * file named as follows:
     * <p/>
     * {TESTCASE_CLASSNAME}.input.xml  where {TESTCASE_CLASSNAME} is the name
     * of the concrete class being tested.
     * <p/>
     * Likewise the pipeline output is verified via a comparison with the
     * following file:
     * <p/>
     * {TESTCASE_CLASSNAME}.expected.xml
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory)
            throws Exception {
        doTest(pipelineFactory, createErrorHandler());
    }

    /**
     * Sends XML down the pipeline as specified by the PipelineCongfiguration
     * and XMLPipelineFactory and checks that the output is as expected. The
     * XML used as the input is found in the testsuite jar by looking for a
     * file named as follows:
     * <p/>
     * {TESTCASE_CLASSNAME}.input.xml  where {TESTCASE_CLASSNAME} is the name
     * of the concrete class being tested.
     * <p/>
     * Likewise the pipeline output is verified via a comparison with the
     * following file:
     * <p/>
     * {TESTCASE_CLASSNAME}.expected.xml
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param errorHandler    the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       ErrorHandler errorHandler) throws Exception {

        String path = getClass().getName().replace('.', '/');

        URL inputURL = getResourceURL(path + ".input.xml");
        URL expectedURL = getResourceURL(path + ".expected.xml");

        doTest(pipelineFactory, inputURL, expectedURL, errorHandler);
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected file are treated as a resource relative to
     * the current package.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param input           The relative path to the input file
     * @param expected        The relative path to the expected file
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       String input,
                       String expected) throws Exception {
        doTest(pipelineFactory, input, expected, createErrorHandler());
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected file are treated as a resource relative to
     * the current package.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param input           The relative path to the input file (relative to
     *                        the class)
     * @param expected        The relative path to the expected file (relative
     *                        to class)
     * @param errorHandler    the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       String input,
                       String expected,
                       ErrorHandler errorHandler) throws Exception {

        URL inputURL = getResourceURL(packagePath + '/' + input);
        URL expectedURL = getResourceURL(packagePath + '/' + expected);

        doTest(pipelineFactory, inputURL, expectedURL, errorHandler);
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected URLs are treated as a resource relative to
     * the current package.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param inputURL        The URL for the input file
     * @param expectedURL     The URL for the expected file
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       URL inputURL,
                       URL expectedURL) throws Exception {
        doTest(pipelineFactory,
               inputURL,
               expectedURL,
               createErrorHandler());
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected URLs are treated as a resource relative to
     * the current package.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param inputURL        The URL for the input file
     * @param expectedURL     The URL for the expected file
     * @param errorHandler the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       URL inputURL,
                       URL expectedURL,
                       ErrorHandler errorHandler) throws Exception {
        URLConnection inputConnection = inputURL.openConnection();
        InputStream inputStream = inputConnection.getInputStream();
        URLConnection expectedConnection = expectedURL.openConnection();
        InputStream expectedStream = expectedConnection.getInputStream();
        System.out.println("Base URL - " + inputURL.toExternalForm());
        doTest(pipelineFactory,
               inputURL.toExternalForm(),
               new InputStreamReader(inputStream),
               new InputStreamReader(expectedStream),
               errorHandler);
    }

    /**
     * XMLProcess the input through the pipeline and check to see whether it
     * matches the expected output. These literals must be self contained and
     * must not require access to external resources.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param inputLiteral    The string containing the input XML
     * @param expectedLiteral The string containing the expected XML
     * @throws Exception if problem encountered
     */
    public void doLiteralsTest(XMLPipelineFactory pipelineFactory,
                               String inputLiteral,
                               String expectedLiteral) throws Exception {
        doLiteralsTest(pipelineFactory,
               inputLiteral,
               expectedLiteral,
               createErrorHandler());
    }

    /**
     * XMLProcess the input through the pipeline and check to see whether it
     * matches the expected output. These literals must be self contained and
     * must not require access to external resources.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param inputLiteral    The string containing the input XML
     * @param expectedLiteral The string containing the expected XML
     * @param errorHandler    the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doLiteralsTest(XMLPipelineFactory pipelineFactory,
                               String inputLiteral,
                               String expectedLiteral,
                               ErrorHandler errorHandler) throws Exception {
        doTest(pipelineFactory,
               null,
               new StringReader(inputLiteral),
               new StringReader(expectedLiteral),
               errorHandler);
    }

    /**
     * XMLProcess the input through the pipeline and check to see whether
     * it matches the expected output.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param baseURI         the base URI if the input contains relative URLs
     * @param input           The reader for the input XML
     * @param expected        The reader for the expected XML
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       String baseURI,
                       Reader input,
                       Reader expected) throws Exception {
        doTest(pipelineFactory, baseURI, input, expected, createErrorHandler());
    }

    /**
     * XMLProcess the input through the pipeline and check to see whether
     * it matches the expected output.
     *
     * @param pipelineFactory the XMLPipelineFactory used to create the
     *                        pipeline
     * @param baseURI         the base URI if the input contains relative URLs
     * @param input           The reader for the input XML
     * @param expected        The reader for the expected XML
     * @param errorHandler the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doTest(XMLPipelineFactory pipelineFactory,
                       String baseURI,
                       Reader input,
                       Reader expected,
                       ErrorHandler errorHandler) throws Exception {

        // create the actual parser
        XMLReader parser = XMLReaderFactory.createXMLReader(false);

        // create a ContentHandler that will write the content into a buffer.
        StringWriter outputWriter = new StringWriter();

        XMLPipelineFilter filter = createPipelineFilter(pipelineFactory, errorHandler, outputWriter);

        // chain the parser to the XMLPipeline
        filter.setParent(parser);


        // Create the required input source
        input = createPipelineInputFilter(input);
        InputSource inputSource = new InputSource(input);

        // Handle relative URL info
        if (baseURI != null) {
            inputSource.setSystemId(baseURI);
        }

        // send the input file down the pipeline
        filter.parse(inputSource);
        outputWriter.flush();

        String output = outputWriter.getBuffer().toString();
        viewResults(output);
        // compare the output to the expected output
        expected = createExpectedFilter(expected);
        assertXMLEqual(expected, new StringReader(output));
    }

    protected XMLPipelineFilter createPipelineFilter(
            XMLPipelineFactory pipelineFactory, ErrorHandler errorHandler,
            Writer outputWriter) throws IOException, SAXException {

        ContentHandler handler = getSerializingContentHandler(outputWriter);

        // Create a XMLPipelineContext
        XMLPipelineContext pipelineContext =
                pipelineFactory.createPipelineContext(
                        createPipelineConfiguration(),
                        createRootEnvironmentInteraction());

        // extract the expression context
        ExpressionContext expressionContext
                = pipelineContext.getExpressionContext();

        // allow subclasses to register functions that are specific to
        // their tests
        registerExpressionFunctions(expressionContext);

        // create a dynamic pipeine
        XMLPipeline pipeline =
                    pipelineFactory.createDynamicPipeline(pipelineContext);

        // create a pipeline filter from the dynamic pipeline
        XMLPipelineFilter filter =
            pipelineFactory.createPipelineFilter(pipeline);

        // Provide a ContentHandler and ErrorHandler for the pipeline.
        filter.setContentHandler(handler);
        filter.setErrorHandler(errorHandler);
        return filter;
    }

    protected void assertXMLEqual(Reader expected, Reader actual)
            throws Exception {

        String expectedString = readString(expected);
        String actualString = readString(actual);
        boolean failed = true;
        try {
            XMLAssert.assertXMLEqual(expectedString, actualString);
            failed = false;
        } finally {
            if (failed) {
                System.out.println("Expected - " + expectedString);
                System.out.println("Actual - " + actualString);
            }
        }
    }

    private String readString(Reader reader)
            throws IOException {

        StringBuffer buffer = new StringBuffer();
        char[] ch = new char[128];
        int chRead;
        while ((chRead = reader.read(ch)) != -1) {
            buffer.append(ch, 0, chRead);
        }
        return buffer.toString();
    }

    protected ContentHandler getSerializingContentHandler(Writer outputWriter)
            throws IOException {

        OutputFormat format = new OutputFormat();
        format.setPreserveSpace(true);
        format.setOmitXMLDeclaration(true);
        XMLSerializer serializer = new XMLSerializer(format);
        serializer.setOutputCharStream(outputWriter);
        ContentHandler sHandler = serializer.asContentHandler();

        // The AtributeNoramilizingContentHandler modifies the base attribute
        // so that it is relative to the working dir. If we didn't do this
        // the test case would be dependant on and absolute file path.
        ContentHandler handler =
                getAttributeNormalizingContentHandler(sHandler);
        return handler;
    }

    /**
     * Classes derived from PipelineTestAbstract can override this method to
     * allow access to the result of the pipeline processing prior to the xml
     * comparison. This is very useful for debugging.
     *
     * @param result A string representation of the result of the pipline
     * processing.
     */
    public void viewResults(String result) {
        // do nothing
    }

    /**
     * Gets an attribute normalizing content handler for the pipeline.  The
     * namespace is specified as a default.
     * @param sHandler the content handler to wrap.
     * @return an AttributeNormalizingContentHandler instance.
     */
    protected ContentHandler getAttributeNormalizingContentHandler(
            ContentHandler sHandler) {
        return new AttributeNormalizingContentHandler(sHandler);
    }

    /**
     * Factory method used to obtain the error handler for the test(s).
     *
     * @return the error handler to be used while testing
     */
    protected ErrorHandler createErrorHandler() {
        return new TestErrorHandler();
    }

    /**
     * Returns a URL to a given named resource.
     * @param resource the resource to look for.
     * @return the URL to the resource
     */
    protected URL getResourceURL(String resource) {
        ClassLoader cl;
        URL url = null;

        // Use the context class loader first if any.
        cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            url = cl.getResource(resource);
        }

        // If that did not work then use the current class's class loader.
        if (url == null) {
            cl = getClass().getClassLoader();
            url = cl.getResource(resource);
        }

        // If that did not work then use the system class loader.
        if (url == null) {
            cl = ClassLoader.getSystemClassLoader();
            url = cl.getResource(resource);
        }

        return url;
    }

    /**
     * Factory method that creates the ContentHandler that the tests use.
     * Subclasses can override this in order to provide a different handler
     * @param writer a Writer
     * @return a ContentHandler instance.
     */
    protected ContentHandler createContentHandler(Writer writer) {
        return new ContentWriter(writer);
    }

    /**
     * Factory method for creating an XMLPipelineConfiguration. This default
     * impelementation simple returns the configuration that
     * {@link IntegrationTestHelper#getPipelineConfiguration} returns
     *
     * @return An XMLPipelineConfiguration instance
     */
    protected XMLPipelineConfiguration createPipelineConfiguration() {
        XMLPipelineConfiguration configuration = null;
        try {
            configuration =
                    new IntegrationTestHelper().getPipelineConfiguration();

            DynamicProcessConfiguration dynamic = (DynamicProcessConfiguration)
                    configuration.retrieveConfiguration(
                            DynamicProcessConfiguration.class);
            configureDynamicProcess(dynamic);


        } catch (Exception e) {
            fail("Unable to create XMLPipelineConfiguration for test");
        }
        return configuration;
    }

    /**
     * Override to add additional configuration for dynamic process.
     *
     * @param configuration The dynamic process configuration.
     */
    protected void configureDynamicProcess(
            DynamicProcessConfiguration configuration) {
    }

    /**
     * Factory method for creating a root <code>EnvironmentInteraction</code>
     * instance. This default implementation simply returns null
     * @return An EnvironmentInteraction instance of null if one is not
     * required.
     */
    protected EnvironmentInteraction createRootEnvironmentInteraction() {
        return null;
    }

    /**
     * Allow subclasses to register
     * {@link com.volantis.xml.expression.Function} objects that are specific
     * to their test
     * @param context the ExpressionContext
     */
    protected void registerExpressionFunctions(ExpressionContext context) {
    }

    /**
     * Sends XML down the pipeline as specified by the PipelineCongfiguration
     * and XMLPipelineFactory and checks that the output is as expected. The
     * XML used as the input is found in the testsuite jar by looking for a
     * file named as follows:
     * <p/>
     * {TESTCASE_CLASSNAME}.input.xml  where {TESTCASE_CLASSNAME} is the name
     * of the concrete class being tested.
     * <p/>
     * Likewise the pipeline output is verified via a comparison with the
     * following file:
     * <p/>
     * {TESTCASE_CLASSNAME}.expected.xml
     *
     * @param context         the pipeline context to use
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context)
            throws Exception {
        doInContextTest(context, createErrorHandler());
    }

    /**
     * Sends XML down the pipeline as specified by the PipelineCongfiguration
     * and XMLPipelineFactory and checks that the output is as expected. The
     * XML used as the input is found in the testsuite jar by looking for a
     * file named as follows:
     * <p/>
     * {TESTCASE_CLASSNAME}.input.xml  where {TESTCASE_CLASSNAME} is the name
     * of the concrete class being tested.
     * <p/>
     * Likewise the pipeline output is verified via a comparison with the
     * following file:
     * <p/>
     * {TESTCASE_CLASSNAME}.expected.xml
     *
     * @param context         the pipeline context to use
     * @param errorHandler    the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context,
                                ErrorHandler errorHandler)
            throws Exception {

        String path = getClass().getName().replace('.', '/');

        URL inputURL = getResourceURL(path + ".input.xml");
        URL expectedURL = getResourceURL(path + ".expected.xml");

        doInContextTest(context, inputURL, expectedURL, errorHandler);
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected file are treated as a resource relative to
     * the current package.
     *
     * @param context         the pipeline context to use
     * @param input           The relative path to the input file
     * @param expected        The relative path to the expected file
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context,
                                String input,
                                String expected)
            throws Exception {
        doInContextTest(context, input, expected, createErrorHandler());
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected file are treated as a resource relative to
     * the current package.
     *
     * @param context         the pipeline context to use
     * @param input           The relative path to the input file (relative to
     *                        the class)
     * @param expected        The relative path to the expected file (relative
     *                        to class)
     * @param errorHandler    the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context, String input,
                                String expected, ErrorHandler errorHandler)
            throws Exception {

        URL inputURL = getResourceURL(packagePath + '/' + input);
        URL expectedURL = getResourceURL(packagePath + '/' + expected);

        doInContextTest(context, inputURL, expectedURL, errorHandler);
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected URLs are treated as a resource relative to
     * the current package.
     *
     * @param context         the pipeline context to use
     * @param inputURL        The URL for the input file
     * @param expectedURL     The URL for the expected file
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context, URL inputURL,
                                URL expectedURL)
            throws Exception {
        doInContextTest(context, inputURL, expectedURL,
                createErrorHandler());
    }

    /**
     * XMLProcess the input file through the pipeline and check to see whether
     * it matches the expected output.
     * <p/>
     * Both the input and expected URLs are treated as a resource relative to
     * the current package.
     *
     * @param context         the pipeline context to use
     * @param inputURL        The URL for the input file
     * @param expectedURL     The URL for the expected file
     * @param errorHandler the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context, URL inputURL,
                                URL expectedURL,
                                ErrorHandler errorHandler)
            throws Exception {
        URLConnection inputConnection = inputURL.openConnection();
        InputStream inputStream = inputConnection.getInputStream();
        URLConnection expectedConnection = expectedURL.openConnection();
        InputStream expectedStream = expectedConnection.getInputStream();

        doInContextTest(context, inputURL.toExternalForm(),
               new InputStreamReader(inputStream),
               new InputStreamReader(expectedStream), errorHandler);
    }

    /**
     * XMLProcess the input through the pipeline and check to see whether
     * it matches the expected output.
     *
     * @param context         the pipeline context to use
     * @param baseURI         the base URI if the input contains relative URLs
     * @param input           The reader for the input XML
     * @param expected        The reader for the expected XML
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context, String baseURI,
                                Reader input, Reader expected)
            throws Exception {
        doInContextTest(context, baseURI, input, expected,
                createErrorHandler());
    }

    /**
     * XMLProcess the input through the pipeline and check to see whether
     * it matches the expected output.
     *
     * @param context         the pipeline context to use
     * @param baseURI         the base URI if the input contains relative URLs
     * @param input           The reader for the input XML
     * @param expected        The reader for the expected XML
     * @param errorHandler the error handler to be used
     * @throws Exception if problem encountered
     */
    public void doInContextTest(XMLPipelineContext context, String baseURI,
                                Reader input, Reader expected,
                                ErrorHandler errorHandler)
            throws Exception {
        // create a ContentHandler that will write the content into a buffer.
        CharArrayWriter outputWriter = new CharArrayWriter();
        ContentHandler handler = createContentHandler(outputWriter);

        // create the actual parser
        XMLReader parser = XMLReaderFactory.createXMLReader(false);

        // extract the expression context
        ExpressionContext expresisonContext
                = context.getExpressionContext();

        // allow subclasses to register functions that are specific to
        // their tests
        registerExpressionFunctions(expresisonContext);

        // create a dynamic pipeine
        pipeline =  context.getPipelineFactory().createDynamicPipeline(context);

        // create a pipeline filter from the dynamic pipeline
        XMLPipelineFilter filter =
            context.getPipelineFactory().createPipelineFilter(pipeline);

        // chain the parser to the XMLPipeline
        filter.setParent(parser);

        // Provide a ContentHandler and ErrorHandler for the pipeline.
        filter.setContentHandler(handler);
        filter.setErrorHandler(errorHandler);

        // Create the required input source
        input = createPipelineInputFilter(input);
        InputSource inputSource = new InputSource(input);

        // Handle relative URL info
        if (baseURI != null) {
            inputSource.setSystemId(baseURI);
        }

        // send the input file down the pipeline
        filter.parse(inputSource);
        outputWriter.flush();

        char[] outputCharacters = outputWriter.toCharArray();
        viewResults(new String(outputCharacters));
        // compare the output to the expected output
        assertXMLEqual(createExpectedFilter(expected),
                       new CharArrayReader(outputCharacters));
    }

    protected Reader createPipelineInputFilter(Reader reader)
            throws IOException {
        return reader;
    }

    protected Reader createExpectedFilter(Reader reader)
            throws IOException {
        return reader;
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8751/3	schaloner	VBM:2005060711 Added a getter method for the AttributeNormalizingContentHandler in PipelineTestAbstract

 20-Jun-05	8751/1	schaloner	VBM:2005060711 [Refactor - method signature] public DefaultHandler createDefaultHandler changed to public ContentHandler createContentHandler in PipelineTestAbstract

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Oct-04	906/3	doug	VBM:2004101313 Added an evaluate process

 07-Sep-04	860/1	byron	VBM:2004090705 Prepare Pipeline source for move to MCS

 09-Jul-04	769/1	doug	VBM:2004070502 Improved integration tests for the Web Driver

 31-Jan-04	533/1	adrian	VBM:2004011906 updated AbsoluteToRelativeURL process to resolve against the base url in the pipeline context

 30-Jan-04	531/2	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 26-Jan-04	551/5	claire	VBM:2004012204 Caching clean-up

 26-Jan-04	551/3	claire	VBM:2004012204 Fixed and optimised caching code

 26-Jan-04	551/1	claire	VBM:2004012204 Implementing caching for transforms

 03-Nov-03	435/5	doug	VBM:2003091902 Fixed merge problems

 31-Oct-03	440/2	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 29-Oct-03	435/1	doug	VBM:2003091902 Expressions that evaluate to an empty sequence are represented with null rather than empty string

 06-Aug-03	301/3	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 04-Aug-03	217/6	allan	VBM:2003071702 Filter nested anchors. Fixed merge conflicts.

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 27-Jun-03	127/3	doug	VBM:2003062306 Column Conditioner Modifications

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
