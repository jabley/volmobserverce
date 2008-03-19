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

package com.volantis.mcs.xdime.cp.xslt;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.xalan.processor.TransformerFactoryImpl;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

import com.volantis.synergetics.testtools.JDOMUtils;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.xerces.parsers.SAXParser;

/**
 * Base class for test cases that test XSLT documents.
 */
public class XSLTTestAbstract
        extends TestCaseAbstract {

    private SAXTransformerFactory saxTransformerFactory;

    protected void setUp() throws Exception {
        super.setUp();

        // Get a factory for creating transformer related objects.
        saxTransformerFactory = new TransformerFactoryImpl();
    }

    /**
     * Get a Templates object from an input source.
     * @param xslt The InputSource for the XSLT.
     * @return The Templates object that represents the XSLT.
     */
    protected Templates getTemplates(InputSource xslt)
            throws TransformerConfigurationException {

        Source source = getSourceFromInputSource(xslt);

        return saxTransformerFactory.newTemplates(source);
    }

    /**
     * Do the transform and test the result.
     * @param xslt The XSLT to apply.
     * @param input The input document that should be transformed.
     * @param expected The expected output document.
     */
    protected void doTransform(String failureMessage,
                               InputSource xslt, InputSource input,
                               InputSource expected)
            throws TransformerException,
            ParserConfigurationException,
            IOException,
            SAXException {

        Templates templates = getTemplates(xslt);

        doTransform(failureMessage, templates, input, expected);
    }

    protected void doTransform(String failureMessage,
                               Templates templates, InputSource input,
                               InputSource expected)
            throws TransformerException,
            ParserConfigurationException,
            IOException,
            SAXException {

        // Get an XMLFilter that will apply the transform.
        XMLFilter filter = saxTransformerFactory.newXMLFilter(templates);

        // Parse the input, pass it through the filter and create a DOM.
        XMLReader parser = new SAXParser();

        parser.setFeature("http://xml.org/sax/features/validation", true);
        parser.setFeature("http://apache.org/xml/features/validation/schema", true);

        filter.setParent(parser);

//        // todo validate input and output.
//        final PrintWriter writer = new PrintWriter(System.out);
//        filter.setContentHandler(new MyContentHandler(writer));
//        filter.setErrorHandler(new MyErrorHandler());
//
//        filter.parse(input);
//        if (true) {
//            writer.flush();
//            throw new RuntimeException();
//        }

        // Get the root node of the actual transformation result.
        Element actualRoot = getElement(filter, input);

        // Parse the expected result.
        parser = new SAXParser();

        Element expectedRoot = getElement(parser, expected);

        String actualString = JDOMUtils.convertToString(actualRoot);
        String expectedString = JDOMUtils.convertToString(expectedRoot);

        System.out.println("Expected: " + expectedString);
        System.out.println("Actual  : " + actualString);

        assertXMLEquals(failureMessage, expectedString, actualString);
    }

    protected void doTransform(String failureMessage,
                               String xslt, String input,
                               String expected)
            throws TransformerException,
            ParserConfigurationException,
            IOException,
            SAXException {

        doTransform(failureMessage,
                getInputSourceForString(xslt),
                getInputSourceForString(input),
                getInputSourceForString(expected));
    }

    protected void doPipelineTransform(String failureMessage,
                                       String xslt,
                                       String input,
                                       InputSource expected)
            throws TransformerException,
            ParserConfigurationException,
            IOException,
            SAXException {

        // @todo this test uses com.volantis.mcs.runtime.configuration.XMLProcessConfigurations which no longer exists.
        // Until we figure out what now has to be done, it is safest to comment out the test code for future reference.
/*
        XMLProcessConfigurations configurations =
                new XMLProcessConfigurations();

        TransformConfiguration configuration = new DefaultTransformConfiguration();
        configurations.setTransformConfiguration(configuration);

        // create the pipeline intitialization
        PipelineInitialization pi = new PipelineInitialization(configurations);

        XMLPipelineFactory factory = pi.getPipelineFactory();

        // create the pipeline context
        XMLPipelineContext pipelineContext = null;

        XMLPipelineConfiguration pipelineConfiguration =
                pi.getPipelineConfiguration();

        // create a new pipeline context
        EnvironmentInteraction rootInteraction = null;
        pipelineContext =
                factory.createPipelineContext(pipelineConfiguration,
                        rootInteraction);

        XMLPipeline pipeline = factory.createDynamicPipeline(pipelineContext);
        // create the pipeline fiter
        XMLPipelineFilter pipelineFilter = null;
        try {
            pipelineFilter =
                    factory.createPipelineFilter(pipeline);
        } catch (SAXException se) {
            throw new ExtendedRuntimeException(se);
        }

        XMLReader parser = new SAXParser();
        pipelineFilter.setParent(parser);


        InputSource pipelineMarkup = getInputSourceForString(
                "<pipeline:transform xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-pipeline\"" +
                "                    xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\"" +
                "                    href=\"" + xslt + "\">" +
                "    <urid:fetch href=\"" + input + "\"/>" +
                "</pipeline:transform>");

//        InputSource pipelineMarkup = getInputSourceForString(
//                "<urid:fetch xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\"" +
//                "            href=\"" + input + "\"/>");
//
//        final PrintWriter writer = new PrintWriter(System.out);
//        //XMLSerializer serializer = new XMLSerializer(writer, new OutputFormat());
//        //pipelineFilter.setContentHandler(serializer.asContentHandler());
//
//        pipelineFilter.setContentHandler(new MyContentHandler(writer));
//
//        pipelineFilter.parse(pipelineMarkup);
//        if (true) {
//            writer.flush();
//            throw new RuntimeException();
//        }


        // Get the root node of the actual transformation result.
        Element actualRoot = getElement(pipelineFilter, pipelineMarkup);

        // Parse the expected result.
        parser = new SAXParser();

        Element expectedRoot = getElement(parser, expected);

        String actualString = JDOMUtils.convertToString(actualRoot);
        String expectedString = JDOMUtils.convertToString(expectedRoot);

        System.out.println("Expected: " + expectedString);
        System.out.println("Actual  : " + actualString);

        assertXMLEquals(failureMessage, expectedString, actualString);
*/
    }

    private static Element getElement(XMLReader reader, InputSource input)
            throws SAXException, IOException {

        SAXHandler sax2dom = new SAXHandler();
        //SAX2DOM sax2dom = new SAX2DOM();

        reader.setContentHandler(sax2dom);
        reader.setDTDHandler(sax2dom);
        reader.setProperty("http://xml.org/sax/properties/lexical-handler",
                sax2dom);
        reader.setProperty("http://xml.org/sax/properties/declaration-handler",
                sax2dom);
        reader.parse(input);

        Document document = sax2dom.getDocument();
        return document.getRootElement();
    }

    private static Source getSourceFromInputSource(InputSource is) {
        Reader reader = is.getCharacterStream();
        if (reader != null) {
            return new StreamSource(reader, is.getSystemId());
        }

        InputStream stream = is.getByteStream();
        if (stream != null) {
            return new StreamSource(stream, is.getSystemId());
        }

        return new StreamSource(is.getSystemId());
    }

    protected InputSource getInputSourceForString(String string) {
        return new InputSource(new StringReader(string));
    }

    protected InputSource getInputSourceForString(String systemId,
                                                  String string) {
        InputSource is = new InputSource(new StringReader(string));
        is.setSystemId(systemId);
        return is;
    }


    protected String getSystemIdForClassResource(String resource) {
        URL url = getClass().getResource(resource);
        String before = url.toExternalForm();
        String after;
        int index = before.indexOf("/unit/");
        if (index == -1) {
            after = before;
        } else {
            after = before.substring(0, index) + "/impl/"
                    + before.substring(index + 6);
        }
        return after;
    }


    protected InputSource getInputSourceForClassResource(String resource)
            throws IOException {

        String after = getSystemIdForClassResource(resource);

        URL url = getClass().getResource(resource);
        /*
        String before = url.toExternalForm();
        String after;
        int index = before.indexOf("/unit/");
        if (index == -1) {
            after = before;
        } else {
            after = before.substring(0, index) + "/impl/"
                    + before.substring(index + 6);
        }
        */
        InputStream is = url.openStream();
        InputSource source = new InputSource(is);
        source.setSystemId(after);
        return source;
    }

    private static class MyContentHandler implements ContentHandler {
        private final PrintWriter writer;

        public MyContentHandler(PrintWriter writer) {
            this.writer = writer;
        }

        public void startElement(String s1, String s2, String s3, Attributes attrs) {
            writer.println("startElement (" + s1 + ", " + s2 + ", " + s3 + ")");
            for (int i = 0; i < attrs.getLength(); i += 1) {
                writer.println("    Attr " + i + " " + attrs.getQName(i) + " = " + attrs.getValue(i) + " (" + attrs.getURI(i) + ")");
            }
        }

        public void endElement(String s1, String s2, String s3) {
            writer.println("endElement (" + s1 + ", " + s2 + ", " + s3 + ")");
        }

        public void startPrefixMapping(String s1, String s2) {
            writer.println("startPrefixMapping (" + s1 + ", " + s2 + ")");
        }

        public void endPrefixMapping(String s1) {
            writer.println("endPrefixMapping (" + s1 + ")");
        }

        public void processingInstruction(String s1, String s2) {
        }

        public void setDocumentLocator(Locator l) {
        }

        public void startDocument() {
            writer.println("<pre>");
        }

        public void endDocument() {
            writer.println("</pre>");
        }

        public void skippedEntity(String s) {
        }

        public void characters(char[] s, int start, int off) {
            writer.println("characters " + new String(s, start, off));
        }

        public void ignorableWhitespace(char[] s, int start, int off) {
        }
    }

    private class MyErrorHandler implements ErrorHandler {
        public void warning(SAXParseException e) throws SAXException {
            e.printStackTrace(System.out);
        }

        public void error(SAXParseException e) throws SAXException {
            e.printStackTrace(System.out);
        }

        public void fatalError(SAXParseException e) throws SAXException {
            e.printStackTrace(System.out);
            throw e;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Jun-05	8796/7	pcameron	VBM:2005061504 Added mcs-transcode parameter to XDIME CP

 07-Sep-04	5380/2	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and reverted to interim si namespace

 21-Jun-04	4645/11	pcameron	VBM:2004060306 Committed for integration

 17-Jun-04	4630/3	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4645/8	pcameron	VBM:2004060306 Committed for integration

 08-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
*/
