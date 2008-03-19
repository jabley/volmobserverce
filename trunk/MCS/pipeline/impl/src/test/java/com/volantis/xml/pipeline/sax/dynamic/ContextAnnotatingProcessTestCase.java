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
package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImplTestCase;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessTestable;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextAnnotatingProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.pipeline.testtools.XMLHelpers;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;
import org.jdom.input.SAXHandler;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.Document;
import org.custommonkey.xmlunit.XMLAssert;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.Assert;

/**
 * The JUnit test class for ContextAnnotatingProcess
 *
 * TODO:  THIS CLASS DOES NOT CURRENTLY PROVIDE A COMPLETE TEST OF THE
 * FUNCTIONALITY OF CONTEXTANNOTATINGPROCESS.  THIS WILL BE COMPLETED UPON
 * UPDATE TO THE CONTEXTMANAGERPROCESS.
 */
public class ContextAnnotatingProcessTestCase extends XMLProcessImplTestCase {

    /**
     * The volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    /**
     * Creates a new <code>ContextAnnotatingProcessTestCase</code> instance
     * @param name the name of the test
     */
    public ContextAnnotatingProcessTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected XMLProcess createTestableProcess() {
        ContextAnnotatingProcess process = new ContextAnnotatingProcess();
        initializeProcess(process);
        return process;
    }

    /**
     * Test the ContextAnnotatingProcess
     * @throws Exception
     */
    public void testAnnotatingProcess() throws Exception {
        doTestAnnotatingProcess("SimpleNestedURI", "SimpleNestedURI");
    }

    /**
     * Test that startPrefixMapping is not fowarded
     * @param testable the process to test
     * @throws Exception on error
     */
    protected void testStartPrefixMapping(XMLProcess testable) throws Exception {

        // ensure that no runtime exception occurs when there is no
        // next process
        testable.startPrefixMapping(PREFIX, URI);

        Attributes attributes = new AttributesImpl();
        // ensure that the event is not immediatly passed to the next process
        {
            XMLProcessTestable next = createNextProcess();
            testable.setNextProcess(next);

            testable.startPrefixMapping(PREFIX, URI);

            next.assertStartPrefixMappingNotInvoked();
            testable.startElement("", "localname", "localname", attributes);
            next.assertStartPrefixMappingInvoked(PREFIX, URI);
            next.assertStartElementInvoked("", "localname", "localname", attributes);
        }

        // ensure that empty prefix events are never passed to the next process
        {
            XMLProcessTestable next = createNextProcess();
            testable.setNextProcess(next);
            testable.startPrefixMapping(PREFIX, URI);
            testable.endPrefixMapping(PREFIX);
            testable.startElement("", "localname", "localname", attributes);

            next.assertStartPrefixMappingNotInvoked();
            next.assertStartElementInvoked("", "localname", "localname", attributes);
        }

        // ensure that prefix events without a startElement are ignored
        {
            char[] characters = "abc".toCharArray();
            XMLProcessTestable next = createNextProcess();
            testable.setNextProcess(next);
            testable.startPrefixMapping(PREFIX, URI);
            testable.characters(characters, 0, characters.length);
            testable.endPrefixMapping(PREFIX);

            next.assertStartPrefixMappingNotInvoked();
            next.assertCharactersInvoked(characters, 0, characters.length);
            next.assertEndPrefixMappingNotInvoked();
        }
    }


    /**
     * Test the ContextAnnotatingProcess with the name input and expected files
     * @param input the input xml file
     * @param expected the xml file containing the expected output
     * @throws Exception
     */
    public void doTestAnnotatingProcess(String input, String expected)
            throws Exception {
        InputSource inputSource = new InputSource(getInputReader(input));
        Document expectedDoc = new SAXBuilder().
                build(getExpectedReader(expected));

        SAXHandler handler = new SAXHandler();
        XMLProcess consumer = XMLHelpers.createSAXHandlerProcess(handler);

        ContextAnnotatingProcess cap = new ContextAnnotatingProcess();
        initializeProcess(cap);
        cap.setNextProcess(consumer);

        ContextManagerProcess cmp = new ContextManagerProcess();
        cmp.setPipeline(cap.getPipeline());
        cmp.setNextProcess(cap);

        XMLReader reader = XMLReaderFactory.createXMLReader(false);
        reader.setContentHandler(cmp);

        reader.parse(inputSource);

        DOMOutputter domOutputter = new DOMOutputter();
        XMLAssert.assertXMLEqual(domOutputter.output(expectedDoc),
                domOutputter.output(handler.getDocument()));
    }


    /**
     * Utility method to get a Reader for the specified 'input' file
     * @param inputURL The name of the input file
     * @return a Reader for the specified 'input' file
     * @throws Exception
     */
    private Reader getInputReader(String inputURL)  throws Exception {
        return getReader(inputURL + ".input.xml");
    }

    /**
     * Utility method to get a Reader for the specified 'expected' file
     * @param expectedURL The name of the expected file
     * @return a Reader for the specified 'expected' file
     * @throws Exception
     */
    private Reader getExpectedReader(String expectedURL) throws Exception {
        return getReader(expectedURL + ".expected.xml");
    }

    /**
     * Utility method to get a Reader for the specified file name
     * @param url the filename to retrieve
     * @return a Reader for the specified file name
     * @throws Exception
     */
    private Reader getReader(String url) throws Exception {
        String path = getClass().getPackage().getName().replace('.', '/');
        URL inputURL = getResourceURL(path + File.separator + url);
        URLConnection inputConnection = inputURL.openConnection();
        InputStream inputStream = inputConnection.getInputStream();
        return new InputStreamReader(inputStream);
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	527/3	adrian	VBM:2004011903 Added Copyright statements to new classes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 ===========================================================================
*/
