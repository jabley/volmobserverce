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

import com.volantis.mcs.utilities.BooleanObject;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.xml.schema.W3CSchemata;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.Text;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

/**
 * Test case for the {@link XercesBasedDOMValidator} class. These tests only
 * test against XML that does not contain any namespace declarations
 */
public class XercesBasedDOMValidatorTestCase extends AbstractValidationTestAbstract {

    /**
     * The xml that will be used to test the validator
     */
    private static final String noNamespaceTestXML =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
            "<shiporder orderid='889923' xmlns:xsi=\"" +
            W3CSchemata.XSI_NAMESPACE +
            "\" xsi:noNamespaceSchemaLocation=\"http://fred.com\">" +
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
            "</shiporder>";

    /**
     * This will be an instance of the class that we are testing
     */
    private DOMValidator validator;

    /**
     * Absolute path to the schema that will be used when validating
     */
    private final String schemaLocation;

    /**
     * This will refer to the xml that is being used to perform the test
     */
    private Document document;

    /**
     * Initializes a <code>XercesBasedDOMValidatorTestCase</code>
     */
    public XercesBasedDOMValidatorTestCase() {
        // As the schema location remains constant from test to test we
        // can calculate its path once in the constructor.
        schemaLocation = getAbsoluteSchemaLocation();

    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        document = createDocument();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        document = null;
        validator = null;
        super.tearDown();
    }

    /**
     * Factory method that factors a XercesBasedDOMValidator
     * @param errorReporter the error reporter that will be informed whenever
     * a validation method is received.
     * @return the DOMValidator
     */
    protected DOMValidator createValidator(
            ErrorReporter errorReporter)
            throws SAXException, ParserErrorException {

        JarFileEntityResolver er = new JarFileEntityResolver();
        er.addSystemIdMapping("http://fred.com", getXSDPath());
        DOMValidator xercesBasedValidator =
                new XercesBasedDOMValidator(er, errorReporter);
        xercesBasedValidator.deriveSchemaLocationFrom(document);

        return xercesBasedValidator;
    }

    /**
     * Factory method that creates the {@link Document} that will be used
     * for testing
     * @return a Document object
     * @throws JDOMException if an error occurs
     * @throws IOException if an error occurs
     */
    protected Document createDocument() throws JDOMException, IOException {
        return createDocumentFromString(noNamespaceTestXML);
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
        doTestValidator(new ExpectedError[]{}, document.getRootElement());
    }

    /**
     * Test that ensures that no errors are reported when validating a sub
     * element of a "valid" root element
     * @throws Exception if an error occurs
     */
    public void testValidSubElement() throws Exception {
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");
        // the document should be valid. Pass in an empty expected
        // errors array.
        doTestValidator(new ExpectedError[]{}, shipto);
    }

    /**
     * Tests that the validator reports the correct error when an unexpected
     * element is encountered.
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownElement() throws Exception {
        // the document should be valid. Pass in an empty expected
        // errors array.
        Element root = document.getRootElement();
        addChild(root, "fred");
        // calculate the expcted error path /shiporder/fred
        String expectedErrorPath = "/" + getQName("shiporder") +
                                   "/" + getQName("fred");
        ExpectedError[] error = {
            new ExpectedError(expectedErrorPath,
                              FaultTypes.INVALID_ELEMENT_LOCATION,
                              getQName("fred"))};
        // perform the validation checking the errors reported against
        // an expected error.
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when an unexpected
     * element is encountered.
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownElementPredicatedPath()
            throws Exception {
        // the document should be valid. Pass in an empty expected
        // errors array.
        Element root = document.getRootElement();
        List children = getChildren(root, "item");
        // there should be 2 item elements
        assertEquals("there should be 2 item elements", 2, children.size());
        Element item = (Element) children.get(1);
        // add an invalid element to the item
        addChild(item, "undeclared");

        // calculate the expcted error path /shiporder/item[2]/undeclared
        String expectedErrorPath = "/" + getQName("shiporder") +
                                   "/" + getQName("item[2]") +
                                   "/" + getQName("undeclared");

        ExpectedError[] error = {
            new ExpectedError(expectedErrorPath,
                              FaultTypes.INVALID_ELEMENT_LOCATION,
                              getQName("undeclared"))};
        // perform the validation checking the errors reported against
        // an expected error.
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when an elements
     * content is invalid
     * @throws Exception if an error occurs
     */
    public void testValidationOfInvalidElementContent() throws Exception {

        Element root = document.getRootElement();
        // only a positive integer value is allowed as the content for the
        // quantity element.
        Element item = getChild(root, "item");
        Element quantity = getChild(item, "quantity");
        // set the content of the quantity element to a string
        quantity.addContent(new Text("hello"));

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
        // perform the validation checking the errors reported against
        // an expected error.
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when an attributes
     * content is invalid
     * @throws Exception if an error occurs
     */
    public void testValidationOfInvalidAttributeContent() throws Exception {

        Element root = document.getRootElement();
        // the shiporder elements orderid attribute only allows a value that
        // consists of six numeric digits.
        assertEquals("shiporder element has unexpected value",
                     "889923",
                     root.getAttributeValue("orderid"));

        // set the attribute value so that it is invalid
        root.setAttribute("orderid", "abcdefg");

        // calculate the expcted error path /shiporder/@orderid
        String expectedErrorPath = "/" + getQName("shiporder") +
                                   "/@orderid";

        // this is the error that we expect
        ExpectedError[] error = {
            new ExpectedError(
                expectedErrorPath,
                FaultTypes.INVALID_SCHEMA_PATTERN_VALUE,
                "orderid"),
            new ExpectedError(
                expectedErrorPath,
                FaultTypes.INVALID_ATTRIBUTE_CONTENT,
                "orderid")
        };

        // perform the validation, checking the errors reported against
        // an expected error.
        doTestValidator(error, root);
    }

    /**
     * Tests that the validator reports the correct error when a sub
     * element has an unexpected child element
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownSubElement() throws Exception {
        // the document should be valid. Pass in an empty expected
        // errors array.
        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");
        addChild(shipto, "undeclared");

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
        doTestValidator(error, shipto);
    }

    /**
     * Tests that the validator reports the correct error when an unknown
     * attribute is encountered
     * @throws Exception if an error occurs
     */
    public void testValidationOfUnknownAttribute() throws Exception {
        // the document should be valid. Pass in an empty expected
        // errors array.
        Element root = document.getRootElement();
        root.setAttribute("undeclared", "true");

        // calculate the expcted error path /shiporder/item[2]/undeclared
        String expectedErrorPath = "/" + getQName("shiporder") +
                                   "/@undeclared";

        ExpectedError[] error = {
            new ExpectedError(
                    expectedErrorPath,
                    FaultTypes.INVALID_ATTRIBUTE_LOCATION,
                    "undeclared")};
        // perform the validation checking the errors reported against
        // an expected error.
        doTestValidator(error, root);
    }


    /**
     * Helper method that validates the element passed in and checks that
     * any errors reported match those passed in via the ExpectedError array
     * @param expectedErrors an array of ExpectedErrors. If empty the client
     * does not expect any errors
     * @param element the element that is to be validated
     * @throws SAXException if an error occurs
     * @throws ParserErrorException if an error occurs
     */
    private void doTestValidator(ExpectedError[] expectedErrors,
                                 Element element)
            throws SAXException, ParserErrorException {

        validator = createValidator(new TestErrorReporter(expectedErrors));
        validator.validate(element);
    }

    /**
     * Tests that a registered supplementary validator is invoked when
     * expected.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorInvoked() throws Exception {
        Element root = document.getRootElement();
        final Element shipto = getChild(root, "shipto");

        final BooleanObject firstBO = new BooleanObject();
        firstBO.setValue(false);

        // create a validator.
        DOMSupplementaryValidator firstSupplementartyValidator
                = new DOMSupplementaryValidator() {

            // javadoc inherited
            public void validate(Element element,
                                 ErrorReporter errorReporter) {
                firstBO.setValue(true);
                // check that it is the correct element
                assertEquals("Elment should be the 'shipto' element",
                             shipto,
                             element);
            }
        };

        final BooleanObject secondBO = new BooleanObject();
        secondBO.setValue(false);

        // create another validator.
        DOMSupplementaryValidator secondSupplementartyValidator
                = new DOMSupplementaryValidator() {

            // javadoc inherited
            public void validate(Element element,
                                 ErrorReporter errorReporter) {
                secondBO.setValue(true);
                // check that it is the correct element
                assertEquals("Elment should be the 'shipto' element",
                             shipto,
                             element);
            }
        };

        // create the validator. Do not expect an standard errors
        validator = createValidator(new TestErrorReporter(
                                            new ExpectedError[] {}));

        // register the fist suplementary validator
        validator.addSupplementaryValidator(shipto.getNamespaceURI(),
                                            shipto.getName(),
                                            firstSupplementartyValidator);

        // register the second suplementary validator
        validator.addSupplementaryValidator(shipto.getNamespaceURI(),
                                            shipto.getName(),
                                            secondSupplementartyValidator);

        // validate the root element.
        validator.validate(root);

        // check that the first supplementary validator was invoked
        assertTrue("The first supplementary validator was not invoked",
                   firstBO.getValue());

        // check that the second supplementary validator was invoked
        assertTrue("The second supplementary validator was not invoked",
                   secondBO.getValue());
    }

    /**
     * Test that a supplementary validator is still invoked even if a "normal"
     * error has been reported.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorInvokedWhenNormalError()
            throws Exception {

        Element root = document.getRootElement();
        final Element shipto = getChild(root, "shipto");

        // add an invalid attribute to the shipto so that we get a
        // standard validation error reported
        shipto.setAttribute("undeclared", "true");
        final BooleanObject bo = new BooleanObject();
        bo.setValue(false);

        // create a validator.
        DOMSupplementaryValidator supplementartyValidator
                = new DOMSupplementaryValidator() {

            // javadoc inherited
            public void validate(Element element,
                                 ErrorReporter errorReporter) {
                bo.setValue(true);
                // check that it is the correct element
                assertEquals("Element should be the 'shipto' element",
                             shipto,
                             element);
            }
        };

        final BooleanObject erBO = new BooleanObject();
        bo.setValue(false);

        ErrorReporter er = new ErrorReporter() {

            // javadoc inherited
            public void reportError(ErrorDetails details) {
                // ensure the supplementary validator has not been invoked
                assertTrue("Supplementary validator should be executed " +
                            "before standard errors have been reported",
                            bo.getValue());
                erBO.setValue(true);
            }

            // javadoc inherited
            public void validationCompleted(XPath xpath) {
            }

            // javadoc inherited
            public void validationStarted(Element root, XPath xpath) {
            }
        };

        // create the validator. Do not expect an standard errors
        validator = createValidator(er);

        // register the uplementary validator
        validator.addSupplementaryValidator(shipto.getNamespaceURI(),
                                            shipto.getName(),
                                            supplementartyValidator);

        // validate the root element.
        validator.validate(root);

        // check that the error reported was invoked
        assertTrue("The error reported was not invoked  ",
                   erBO.getValue());

        // check that the supplementary validator was invoked
        assertTrue("The supplementary validator was not invoked",
                   bo.getValue());

    }

    /**
     * Test that a supplementary validator is not invoked for an element in
     * a different namespace but the same name.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorNotInvokedWhenDifferentNamesapce()
            throws Exception {

        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");

        final BooleanObject bo = new BooleanObject();
        bo.setValue(false);

        // create a validator.
        DOMSupplementaryValidator supplementartyValidator
                = new DOMSupplementaryValidator() {

            // javadoc inherited
            public void validate(Element element,
                                 ErrorReporter errorReporter) {
                bo.setValue(true);
            }
        };

        // create the validator. Do not expect any standard errors
        validator = createValidator(new TestErrorReporter(
                                            new ExpectedError[] {}));

        validator.addSupplementaryValidator("bogus namepsace",
                                            shipto.getName(),
                                            supplementartyValidator);

        // validate the root element.
        validator.validate(root);

        // check that the supplementary validator was invoked
        assertFalse("The supplementary validator was invoked",
                    bo.getValue());
    }

    /**
     * Test that a supplementary validator is removed correctly.
     * @throws Exception if an error occurs
     */
    public void testSupplementaryValidatorRemoval()
            throws Exception {

        Element root = document.getRootElement();
        Element shipto = getChild(root, "shipto");

        final BooleanObject bo = new BooleanObject();
        bo.setValue(false);

        // create a validator.
        DOMSupplementaryValidator supplementartyValidator
                = new DOMSupplementaryValidator() {

            // javadoc inherited
            public void validate(Element element,
                                 ErrorReporter errorReporter) {
                bo.setValue(true);
            }
        };

        // create the validator. Do not expect any standard errors
        validator = createValidator(new TestErrorReporter(
                                            new ExpectedError[] {}));

        // add the validator
        validator.addSupplementaryValidator(shipto.getNamespaceURI(),
                                            shipto.getName(),
                                            supplementartyValidator);

        // validate the root element.
        validator.validate(root);

        // check that the supplementary validator was invoked
        assertTrue("The supplementary validator was not invoked",
                   bo.getValue());

        bo.setValue(false);

        // remove the validator
        validator.removeSupplementaryValidator(shipto.getNamespaceURI(),
                                               shipto.getName(),
                                               supplementartyValidator);
        // validate the root element
        validator.validate(root);

        // check that the supplementary validator was removed
        assertFalse("The supplementary validator was not removed",
                    bo.getValue());

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/2	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 21-Dec-04	6524/2	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2219/2	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2160/6	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 10-Dec-03	2084/5	doug	VBM:2003120201 Fixed problem with the INVALID_ELEMENT_CONTENT ParserError

 10-Dec-03	2084/3	doug	VBM:2003120201 Xerces based DOMValidator implementation

 09-Dec-03	2084/1	doug	VBM:2003120201 xerces based DOMValidator implementation

 ===========================================================================
*/
