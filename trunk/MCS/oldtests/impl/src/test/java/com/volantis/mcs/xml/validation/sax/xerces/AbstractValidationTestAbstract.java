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
package com.volantis.mcs.xml.validation.sax.xerces;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;

/**
 * Base class for DOMValidator test cases
 */
public abstract class AbstractValidationTestAbstract extends TestCaseAbstract {

    /**
     * JDOMFactory
     */
    protected JDOMFactory factory = new DefaultJDOMFactory();

    // javadoc not needed
    protected void setJDOMFactory(JDOMFactory factory) {
        this.factory = factory;
    }

    /**
     * Provides the absolute path to the schema that we will validate against.
     * This schema will reside in the same jar as this class and will be
     * @return the absolute path to the xsd file.
     */
    protected String getAbsoluteSchemaLocation() {
        String xsdFile = getXSDPath();

        // obtain the URL to a the xsd file.
        URL url = getResourceURL(xsdFile);
        if (url == null) {
            // fatal - cannot continue with this test. Note for Idea
            // users - if you use the "Build->Compile" menu item to
            // compile this class this exception may well be thrown.
            // this is because the .class file does not reside in the same
            // jar as the XercesBasedDOMValidatorTestCase.xsd file.
            fail("Could not locate the " + xsdFile + " schema file");
        }
        return url.toExternalForm();
    }

    /**
     * Returns the absolute path to the XSD file for the test.
     * @return the xsd path
     */
    protected String getXSDPath() {
        String qualifiedName = getClass().getName();
        return qualifiedName.replace('.', '/') + ".xsd";
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
     * Factory method that creates a SAXBuilder instance
     * @return SAZBuidler instance
     */
    protected SAXBuilder createSAXBuilder() {
         return new SAXBuilder("com.volantis.xml.xerces.parsers.SAXParser",
                               false);
    }

    /**
     * Converts the string argument to a Document object.
     * @param xml the xml that is to be used to create the Document
     * @return a Document object
     * @throws JDOMException if an error occurs
     * @throws IOException if an error occurs
     */
    protected Document createDocumentFromString(String xml)
            throws IOException, JDOMException  {


        // Create a non-validating builder. Validation is done separately
        SAXBuilder builder = createSAXBuilder();
        builder.setFactory(factory);

        // return the jdom document representation of xml string.
        return  builder.build(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Returns the {@link Namespace} that is associated with evey element in
     * the test xml.
     * @return a Namespace instance
     */
    protected abstract Namespace getNamespace();

    /**
     * Create a QName for the given localName. If we are not using namespaces
     * then the string returned will be the localName
     * @param localName the localName
     * @return the qname
     */
    protected String getQName(String localName) {
        Namespace ns = getNamespace();
        String qName = localName;

        if (ns != null && !"".equals(ns.getPrefix())) {
            qName = ns.getPrefix() + ":" + localName;
        }
        return qName;
    }

    /**
     * Helper method that returns a named child element of the specified
     * parent. It takes into account the namespace that is associated with
     * each element in the test xml.
     * @param parent the parent element
     * @param name the name of the child that is to be returned
     * @return the element for the named child or null if it does not exist
     */
    protected Element getChild(Element parent, String name) {
        Namespace ns = getNamespace();
        return (ns == null)
                ? parent.getChild(name) : parent.getChild(name, ns);
    }

    /**
     * Helper method that returns the list of child elements of the specified
     * parent. It takes into account the namespace that is associated with
     * each element in the test xml.
     * @param parent the parent element
     * @param name the name of the children
     * @return the list of child element. It the parent does not have any
     * children with the specified name an empty list will be returned.
     */
    protected List getChildren(Element parent, String name) {
        Namespace ns = getNamespace();
        return (ns == null)
                ? parent.getChildren(name) : parent.getChildren(name, ns);
    }

    /**
     * Add a named child element to the specified parent. If the
     * {@link #getNamespace} method returns a non null Namepsace that
     * namespace will be associated with the child
     * @param parent the parent element
     * @param name the name of the child element that will be created
     */
    protected void addChild(Element parent, String name) {
        Namespace ns = getNamespace();
        if (ns == null) {
            parent.addContent(factory.element(name));
        } else {
            parent.addContent(factory.element(name, ns));
        }
    }

    /**
     * This class encapsultes an Error that is reported via an
     * {@link ErrorReporter}.
     */
    protected static class ExpectedError {

        /**
         * The xpath to the invalid element/attribute
         */
        private String xpath;

        /**
         * The error key
         */
        private String key;

        /**
         * The error parameter
         */
        private String param;

        /**
         * Initializes a <code>ExpectedError</code> with the given parameters
         * @param xpath the xpath to the invalid element/attribute
         * @param key the error key
         * @param param the error parameter
         */
        public ExpectedError(String xpath, String key, String param) {
            this.xpath = xpath;
            this.key = key;
            this.param = param;
        }

        /**
         * Check that this expected errors xpath equals the one passed in
         * @param actualXPath the actual xpath
         */
        public void assertXPath(String actualXPath) {
            assertEquals("The reported errors xpath is not as expected",
                         xpath,
                         actualXPath);
        }

        /**
         * Check that this expected errors key equals the one passed in
         * @param actualKey the actual key
         */
        public void assertKey(String actualKey) {
            assertEquals("The reported errors key is not as expected",
                         key,
                         actualKey);
        }

        /**
         * Check that this expected errors param equals the one passed in
         * @param actualParam the actual param
         */
        public void assertParam(String actualParam) {
            assertEquals("The reported errors param is not as expected",
                         param,
                         actualParam);
        }
    }

    /**
     * Implementation of the {@link com.volantis.mcs.xml.validation.ErrorReporter} checks reported
     * errors against an array of expected errors
     */
    protected static class TestErrorReporter implements ErrorReporter {

        /**
         * The expected errors
         */
        private ExpectedError[] expectedErrors;

        /**
         * The number of errors reported
         */
        private int errorCount;

        /**
         * Initializes a <code>TestErrorReporter</code> with the given
         * parameter
         * @param expectedErrors an array of ExpectedErrors
         */
        public TestErrorReporter(ExpectedError[] expectedErrors) {
            this.expectedErrors = expectedErrors;
            this.errorCount = 0;
        }

        // javadoc inherited
        public void reportError(ErrorDetails details) {

            XPath xpath = details.getXPath();
            String key = details.getKey();
            String param = details.getParam();

            if (errorCount >= expectedErrors.length) {
                // we have received an error and there is no unprocessed
                // expected errors. The client has not expected this error at
                // all. Fail the test
                fail("Unexpected error has been reported xpath[" +
                     xpath.getExternalForm() + "], key[" + key +
                     "] and param[" + param + "]");
            }
            // check the reported error against the one that the client expects
            ExpectedError expected = expectedErrors[errorCount++];
            expected.assertXPath(xpath.getExternalForm());
            expected.assertKey(key);
            expected.assertParam(param);
        }

        /**
         * Check that the expected number of errors were reported.
         * @param expectedCount the expected count.
         */
        public void assertErrorCount(int expectedCount) {
            assertEquals("Error count not as ", expectedCount, errorCount);
        }

        // javadoc inherited
        public void validationCompleted(XPath xpath) {

        }

        // javadoc inherited
        public void validationStarted(Element root, XPath xpath) {

        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2219/2	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
