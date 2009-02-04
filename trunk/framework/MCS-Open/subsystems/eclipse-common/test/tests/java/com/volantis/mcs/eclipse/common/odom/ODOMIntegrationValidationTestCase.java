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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.validation.sax.xerces.AbstractValidationTestAbstract;
import com.volantis.mcs.xml.validation.sax.xerces.XercesBasedDOMValidator;
import com.volantis.xml.schema.W3CSchemata;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

/**
 * Test case that ensures that {@link ODOMObservable}s are validated
 * whenever they are changed.
 */
public class ODOMIntegrationValidationTestCase
        extends AbstractValidationTestAbstract {

    /**
     * This will be an instance of the class that we are testing
     */
    protected DOMValidator validator;

    /**
     * This will refer to the xml that is being used to perform the test
     */
    private Document document;

    /**
     * ErrorReporter
     */
    private TestErrorReporter reporter;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        reporter = new TestErrorReporter(null);
        validator = createValidator(reporter);
        factory = new ODOMFactory();
        setJDOMFactory(factory);
        validator.enable(false);
        document = createDocument();
        validator.deriveSchemaLocationFrom(document);
        validator.enable(true);

    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        document = null;
        validator = null;
        super.tearDown();
    }

    /**
     * Factory method that factors a XercesBasedDOMValidator
     * @return a DOMValidator instance
     */
    protected DOMValidator createValidator(ErrorReporter errorReporter)
            throws SAXException, ParserErrorException {

        // We must create the validator with an error reporter. Each
        // test will replace this error reporter with one that is
        // appropriate to its test.
        DOMValidator xercesBasedValidator = new XercesBasedDOMValidator(
                null, errorReporter);
        return xercesBasedValidator;
    }

    /**
     * Set the error reportor that the validator will use.
     * @param errorReporter the ErrorReporter
     */
    protected void setErrorReporter(TestErrorReporter errorReporter) {
        this.reporter = errorReporter;
        validator.setErrorReporter(reporter);
    }

    // javadoc inherited
    protected Document createDocument() throws JDOMException, IOException {
        return createDocumentFromString(
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<shiporder xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " +
                        "xsi:noNamespaceSchemaLocation=\"" +
                            getAbsoluteSchemaLocation() + "\" orderid='889923'>" +
                    "<orderperson>John Smith</orderperson>" +
                    "<shipto>" +
                        "<name>Ola Nordmann</name>" +
                        "<address>Langgt 23</address>" +
                        "<city>4000 Stavanger</city>" +
                        "<country>Norway</country>" +
                    "</shipto>" +
                    "<item>" +
                        "<title>Empire Burlesque</title>" +
                        "<note>Special Edition</note>" +
                        "<quantity>1</quantity>" +
                        "<price>10.90</price>" +
                    "</item>" +
                    "<item>" +
                        "<title>Hide your heart</title>" +
                        "<quantity>1</quantity>" +
                        "<price>9.90</price>" +
                    "</item>" +
                "</shiporder>");
    }

    // javadoc inherited
    protected Namespace getNamespace() {
        return null;
    }

    /**
     * Test that ensures that no errors are reported when validating a
     * "valid" document
     * @throws Exception if an error occurs
     */
    public void testValidElement() throws Exception {
        // the document should be valid. Pass in an empty expected
        // errors array.
        ODOMElement root = (ODOMElement) document.getRootElement();

        setErrorReporter(new TestErrorReporter(new ExpectedError[]{}));

        validator.validate(root);
        reporter.assertErrorCount(0);
    }

    /**
     * Test that ensures that no errors are reported when validating a sub
     * element of a "valid" root element
     * @throws Exception if an error occurs
     */
    public void testValidSubElement() throws Exception {
        Element root = document.getRootElement();
        ODOMElement shipto = (ODOMElement) getChild(root, "shipto");
        // the document should be valid. Pass in an empty expected
        // errors array.
        setErrorReporter(new TestErrorReporter(new ExpectedError[]{}));

        validator.validate(shipto);
        reporter.assertErrorCount(0);
    }

    /**
     * Tests that the validator reports the correct error when an unexpected
     * element is encountered.
     * @throws Exception if an error occurs
     */
    public void testValidatationOfUnkownElement() throws Exception {

        ODOMElement root = (ODOMElement) document.getRootElement();
        // calculate the expcted error path /shiporder/fred
        String expectedErrorPath = "/" + getQName("shiporder") +
                "/" + getQName("fred");
        ExpectedError[] error = {
            new ExpectedError(expectedErrorPath,
                              FaultTypes.INVALID_ELEMENT_LOCATION,
                              getQName("fred"))};

        // set up the error reporter
        setErrorReporter(new TestErrorReporter(error));

        // add the invalid child to the root element this should cause
        addChild(root, "fred");

        validator.validate(root);

        // check that the correct number of errors were reported
        reporter.assertErrorCount(1);
    }

    /**
     * Tests that the validator reports the correct error when an unexpected
     * element is encountered.
     * @throws Exception if an error occurs
     */
    public void testValidatationOfUnkownElementPredicatedPath()
            throws Exception {

        Element root = document.getRootElement();
        List children = getChildren(root, "item");
        // there should be 2 item elements
        assertEquals("there should be 2 item elements", 2, children.size());
        ODOMElement item = (ODOMElement) children.get(1);

        // calculate the expcted error path /shiporder/item[2]/undeclared
        String expectedErrorPath = "/" + getQName("shiporder") +
                "/" + getQName("item[2]") +
                "/" + getQName("undeclared");

        ExpectedError[] error = {new ExpectedError(
                expectedErrorPath,
                FaultTypes.INVALID_ELEMENT_LOCATION,
                getQName("undeclared"))};

        // set up the error reporter
        setErrorReporter(new TestErrorReporter(error));

        // add an invalid element to the item this will generate the
        // error message
        addChild(item, "undeclared");

        validator.validate(item);

        // check that the error was reported
        reporter.assertErrorCount(1);
    }

    /**
     * Tests that the validator reports the correct error when an elements
     * content is invalid
     * @throws Exception if an error occurs
     */
    public void testValidatationOfInvalidElementContent() throws Exception {

        Element root = document.getRootElement();
        // only a positive integer value is allowed as the content for the
        // quantity element.
        Element item = getChild(root, "item");
        Element quantity = getChild(item, "quantity");

        // calculate the expcted error path /shiporder/item[1]/quantity
        String expectedErrorPath = "/" + getQName("shiporder") +
                "/" + getQName("item[1]") +
                "/" + getQName("quantity");

        ExpectedError[] error = {
            new ExpectedError(expectedErrorPath,
                              FaultTypes.INVALID_SCHEMA_DATA_TYPE,
                              "1hello"),
            new ExpectedError(expectedErrorPath,
                              FaultTypes.INVALID_ELEMENT_CONTENT,
                              getQName("quantity"))
        };

        // set up the error reporter
        setErrorReporter(new TestErrorReporter(error));

        List contentNodes = quantity.getContent();
        assertEquals("quantity element should have one text node",
                     1,
                     contentNodes.size());
        Text text = (Text) contentNodes.get(0);

        // append an invalid value to the text node.
        // This should cause validation to fail
        text.append("hello");

        validator.validate(quantity);

        // check the error count
        reporter.assertErrorCount(2);
    }

    /**
     * Tests that the validator reports the correct error when an attributes
     * content is invalid
     * @throws Exception if an error occurs
     */
    public void testValidatationOfInvalidAttributeContent() throws Exception {

        Element root = document.getRootElement();
        // the shiporder elements orderid attribute only allows a value that
        // consists of six numeric digits.
        assertEquals("shiporder element has unexpected value",
                     "889923",
                     root.getAttributeValue("orderid"));

        // calculate the expcted error path /shiporder/@orderid
        String expectedErrorPath = "/" + getQName("shiporder") +
                "/@orderid";

        // this is the error that we expect
        ExpectedError[] errors = {
            new ExpectedError(
                    expectedErrorPath,
                    FaultTypes.INVALID_SCHEMA_PATTERN_VALUE,
                    "orderid"),
            new ExpectedError(
                    expectedErrorPath,
                    FaultTypes.INVALID_ATTRIBUTE_CONTENT,
                    "orderid")
        };

        // set up the error reporter
        setErrorReporter(new TestErrorReporter(errors));

        Attribute orderid = root.getAttribute("orderid");
        assertNotNull(
                "shiporder element should have an 'orderid' attribute",
                orderid);
        // set the attribute value so that it is invalid. This should
        // cause 2 errors to be reported.
        orderid.setValue("abcdefg");

        validator.validate(root);

        // check that 2 errors were indeed reported
        reporter.assertErrorCount(2);
    }

    /**
     * Tests that the validator reports the correct error when a sub
     * element has an unexpected child element
     * @throws Exception if an error occurs
     */
    public void testValidatationOfUnkownSubElement() throws Exception {
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");

        // calculate the expcted error path /shiporder/shipto/undeclared
        String expectedErrorPath = "/" + getQName("shiporder") +
                "/" + getQName("shipto") +
                "/" + getQName("undeclared");

        // this is the error that is expected when the shipto element
        // is validated
        ExpectedError[] error = {
            new ExpectedError(expectedErrorPath,
                              FaultTypes.INVALID_ELEMENT_LOCATION,
                              getQName("undeclared"))};

        TestErrorReporter reporter = new TestErrorReporter(error);
        validator.setErrorReporter(reporter);

        // add the invalid element
        addChild(shipto, "undeclared");

        validator.validate(shipto);

        // check that a single error was reported
        reporter.assertErrorCount(1);
    }

    /**
     * Tests that the validator reports the correct error when an unknown
     * attribute is encountered
     * @throws Exception if an error occurs
     */
    public void testValidatationOfUnkownAttribute() throws Exception {
        // the document should be valid. Pass in an empty expected
        // errors array.
        Element root = document.getRootElement();

        // calculate the expcted error path /shiporder/item[2]/undeclared
        String expectedErrorPath = "/" + getQName("shiporder") +
                "/@undeclared";

        ExpectedError[] error = {
            new ExpectedError(
                    expectedErrorPath,
                    FaultTypes.INVALID_ATTRIBUTE_LOCATION,
                    "undeclared")};

        // set up the error reporter
        setErrorReporter(new TestErrorReporter(error));

        // add the invalid attribute
        root.setAttribute("undeclared", "true");

        validator.validate(root);

        reporter.assertErrorCount(1);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Dec-04	6524/3	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 07-Jan-04	2447/1	philws	VBM:2004010609 Fix test issues

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
