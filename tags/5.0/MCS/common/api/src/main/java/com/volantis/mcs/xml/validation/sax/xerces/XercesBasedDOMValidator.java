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

import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.DOMValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.sax.AttributeResolvingParserError;
import com.volantis.mcs.xml.validation.sax.ParserError;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.validation.sax.SAXValidator;
import com.volantis.mcs.xml.validation.sax.TerminateParsingException;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import org.jdom.Attribute;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ResourceBundle;


/**
 * This validator utilizes a SAXValidator populated with XERCES ParserError
 * instances.
 */
public class XercesBasedDOMValidator implements DOMValidator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XercesBasedDOMValidator.class);

    /**
     * This will contain a list of the predefined {@link ParserError}
     * objects that represent the validation error messages that
     * the xerces parser generates.
     */
    private static ArrayList parserErrors = new ArrayList();

    /**
     * @link aggregation
     * @supplierCardinality 1
     * @supplierRole validator
     */
    private SAXValidator validator;

    /**
     * All errors found are reported to this object.
     */
    private ErrorReporter errorReporter;

    /**
     * Used to ensure that certain operations are not performed during a
     * validation.
     */
    private boolean validationInProgress;

    /**
     * Used to control whether this validator is enabled
     */
    private boolean enabled;

    /**
     * This will be used to parse the xml that is being validated
     */
    private XMLReader xmlReader;

    /**
     * Constant for the schemaLocation attribute
     */
    private static final String XSI_SCHEMA_LOCATION_ATTRIBUTE =
            "schemaLocation";

    /**
     * Constant for the noNamespaceSchemaLocation attribute
     */
    private static final String XSI_NO_NAMESPACE_SCHEMA_LOCATION_ATTRIBUTE =
            "noNamespaceSchemaLocation";

    /**
     * The name of the bundle that contains the xerces schmea error messages
     */
    private static final String XSD_MESSAGE_BUNDLE_NAME =
            "com.volantis.xml.xerces.impl.msg.XMLSchemaMessages";

    /**
     * The bundle that contains the xerces schmea error messages
     */
    private static final ResourceBundle XSD_MESSAGE_BUNDLE =
            ResourceBundle.getBundle(XSD_MESSAGE_BUNDLE_NAME);

    /**
     * Initializes a <code>XercesBasedDOMValidator</code> instance with the
     * given arguments.
     *
     * @param errorReporter the ErrorReporter that will be used to report
     *                      any validation errors.
     * @throws SAXException         if there is an error configuring the XML
     *                              reader
     * @throws ParserErrorException if initialisation of ParserErrors fails
     */
    public XercesBasedDOMValidator(ErrorReporter errorReporter)
            throws SAXException, ParserErrorException {
        this(null, errorReporter);
    }

    /**
     * An entity resolver may be provided that will handle local access to
     * specific entities via public or system IDs. In addition, the schema
     * location(s) should be specified to enable schema namespace URIs to be
     * mapped to schema system IDs which may, in turn, be handled via the
     * given entity resolver.
     * <p/>
     * <p>Neither value is mandatory since schema validation is optional and
     * DTD validation (also optional) may be performed using actual URLs.</p>
     * <p/>
     * <p>The schemaLocation is a string of the form:</p>
     * <p/>
     * <pre>
     * {&lt;schema namespace URI&gt; &lt;schema system ID&gt;}
     * </pre>
     *
     * @param entityResolver The EntityResolver to be used when parsing the
     *                       XML file
     * @param errorReporter  The ErrorReporter that will be used to report
     *                       any validation errors
     * @throws SAXException             if there is an error configuring the XML
     *                                  reader
     * @throws ParserErrorException     if initialisation of ParserErrors fails
     * @throws IllegalArgumentException if the root element errorReporter are
     *                                  null
     */
    public XercesBasedDOMValidator(EntityResolver entityResolver,
                                   ErrorReporter errorReporter)
            throws SAXException, ParserErrorException {
        if (errorReporter == null) {
            throw new IllegalArgumentException(
                    "Validation requires an error reporter to be available");
        }

        validationInProgress = false;

        synchronized (parserErrors) {
            // Ideally this should be done in a static initialization block.
            // However, the ParserError constructor can throw a
            // ParserErrorException. Therefore, populating the list here
            // so that if an exception is thrown the user can obtain a
            // sensible stack trace.
            if (parserErrors.isEmpty()) {
                //////////////////////////////////////////////////////////
                // xsd validation messages
                //////////////////////////////////////////////////////////

                // this error will occur when a given element is not allowed
                // at the given location. The param string in reported
                // errors will name that element
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-complex-type.2.4.a",
                        FaultTypes.INVALID_ELEMENT_LOCATION,
                        0,
                        false));

                // this error will occur when a given elements conent is
                // invalid
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-complex-type.2.4.b",
                        FaultTypes.INVALID_ELEMENT_CONTENT,
                        0,
                        false));

                // this error will occur when a given child element is not
                // allowed at the given location. The param string in reported
                // errors will name that element
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-complex-type.2.4.d",
                        FaultTypes.INVALID_ELEMENT_LOCATION,
                        0,
                        false));

                // this error will occur when a given attribute is not
                // allowed at the given location. The param string in
                // reported errors will name that attribute
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-complex-type.3.2.2",
                        FaultTypes.INVALID_ATTRIBUTE_LOCATION,
                        0,
                        true));

                // this error will occur when a given elements content
                // does not match its declared type. The param string
                // will name that element
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-type.3.1.3",
                        FaultTypes.INVALID_ELEMENT_CONTENT,
                        1,
                        false));

                // this error will occur when a given attributes value
                // does not match its declared type. The param string
                // will name that attribute
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-attribute.3",
                        FaultTypes.INVALID_ATTRIBUTE_CONTENT,
                        1,
                        true));

                // this error will occur when a required attribute is missing
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-complex-type.4",
                        FaultTypes.MISSING_ATTRIBUTE,
                        0,
                        true));

                // this error will occur when a given attributes value
                // is not of the declared type
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-datatype-valid.1.2.1",
                        FaultTypes.INVALID_SCHEMA_DATA_TYPE,
                        0));

                // this error will occur when a given attributes value
                // is not of the declared type
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-datatype-valid.1.2.2",
                        FaultTypes.INVALID_POLICY_NAME,
                        0));

                // this error will occur when a given elements value
                // is not of the declared type
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-datatype-valid.1.2.1",
                        FaultTypes.INVALID_SCHEMA_DATA_TYPE,
                        0,
                        false));

                // this error will occur when a required attribute is missing
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-enumeration-valid",
                        FaultTypes.INVALID_SELECTION,
                        0));

                // this error will occur when a required attribute is missing
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-enumeration-valid",
                        FaultTypes.INVALID_SELECTION,
                        0,
                        false));

                // this error will occur when a given attributes value
                // is not of the declared type
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-pattern-valid",
                        FaultTypes.INVALID_SCHEMA_PATTERN_VALUE,
                        0));

                // this error will occur when a given elements value
                // is not of the declared type
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-pattern-valid",
                        FaultTypes.INVALID_SCHEMA_PATTERN_VALUE,
                        0,
                        false));

                // this error will occur when a schema constraint key is
                // not found
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "KeyNotFound",
                        FaultTypes.SCHEMA_CONSTRAINT_VIOLATED,
                        0,
                        false));

                // this error will occur when a schema constraint key is
                // violated
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "DuplicateKey",
                        FaultTypes.DUPLICATE_NAME,
                        0,
                        false));

                // this error will occur when an attribute has exceeded
                // a max inclusive constraint. The error param will
                // contain the value that is in error. THIS ERROR MUST PRECEED
                // THE PARSER element equivalent for "cvc-maxInclusive-valid"
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-maxInclusive-valid",
                        FaultTypes.MAX_INCLUSIVE_VIOLATED,
                        0));

                // this error will occur when an elemnt has exceeded
                // a max inclusive constraint. The error param will
                // contain the value that is in error
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-maxInclusive-valid",
                        FaultTypes.MAX_INCLUSIVE_VIOLATED,
                        0,
                        false));

                // this error will occur when an attribute has exceeded
                // a min inclusive constraint. The error param will
                // contain the value that is in error. THIS ERROR MUST PRECEED
                // THE PARSER element equivalent for "cvc-minInclusive-valid"
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-minInclusive-valid",
                        FaultTypes.MIN_INCLUSIVE_VIOLATED,
                        0));

                // this error will occur when an element has exceeded
                // a min inclusive constraint. The error param will
                // contain the value that is in error
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-minInclusive-valid",
                        FaultTypes.MIN_INCLUSIVE_VIOLATED,
                        0,
                        false));

                // this error will occur when an attribute has exceeded
                // a max length constraint. The error param will
                // name the attribute that is in error. THIS ERROR MUST PRECEED
                // THE PARSER element equivalent for "cvc-maxInclusive-valid"
                parserErrors.add(new AttributeResolvingParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-maxLength-valid",
                        FaultTypes.MAX_LENGTH_VIOLATED,
                        0));

                // this error will occur when an elemnt has exceeded
                // a max length constraint. The error param will
                // contain the value that is in error
                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "cvc-maxLength-valid",
                        FaultTypes.MAX_LENGTH_VIOLATED,
                        0,
                        false));

                parserErrors.add(new ParserError(
                        XSD_MESSAGE_BUNDLE,
                        "DuplicateUnique",
                        FaultTypes.DUPLICATE_UNIQUE,
                        0,
                        false));
            }
        }

        // create the SAXValidator instance that will perform the actual
        // validation
        validator = createSAXValidator();

        this.errorReporter = errorReporter;

        // create the XMLReader instance
        xmlReader = new com.volantis.xml.xerces.parsers.SAXParser();
        xmlReader.setContentHandler(validator);
        xmlReader.setErrorHandler(validator);
        xmlReader.setEntityResolver(entityResolver);

        // set up the features that we need
        xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
        xmlReader.setFeature("http://xml.org/sax/features/validation", true);
        xmlReader.setFeature("http://apache.org/xml/features/validation/schema",
                true);
        enabled = true;
    }


    // javadoc inherited
    public void declareSchemaLocation(String schemaLocation) {
        try {
            xmlReader.setProperty("http://apache.org/xml/properties/schema/" +
                    "external-schemaLocation",
                    schemaLocation);
        } catch (SAXException e) {
            // this should never happen.
            logger.error("schema-location-could-not-be-set-on-reader",
                    schemaLocation,
                    e);
            throw new UndeclaredThrowableException(e);
        }
    }

    // javadoc inherited
    public void declareNoNamespaceSchemaLocation(String schemaLocation) {
        try {
            xmlReader.setProperty("http://apache.org/xml/properties/schema/" +
                    "external-noNamespaceSchemaLocation",
                    schemaLocation);
        } catch (SAXException e) {
            // this should never happen.
            logger.error(
                    "no-namespace-schema-location-could-not-be-set-on-reader",
                    schemaLocation,
                    e);
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Checks the root element to see if it specifies the location(s) of
     * the schema(s) to use. If found the
     * {@link #declareNoNamespaceSchemaLocation} or
     * {@link #declareSchemaLocation} method is passed the location.
     *
     * @todo We only check the root element. When the TreeWalker has been
     * developed we may wish to use this to visit all the documents elements
     * in the search for the schema location
     * @todo We do not check the namesapce of the attribute to see if it is
     * one of the XMLSchema namespaces. We may wish to check against the
     * set of known XMLSchema namespaces.
     */
    // rest of javadoc inherited
    public void deriveSchemaLocationFrom(Document document) {
        // we only look to see if there is a schema declaration on the
        // root elment.
        if (document == null) {
            throw new IllegalArgumentException("Document must not be null");
        }
        Element root = document.getRootElement();
        if (root != null) {
            List attributes = root.getAttributes();
            Attribute attribute;
            String name;
            boolean foundSchema = false;
            // attribute list cannot be null.
            for (int i = 0; i < attributes.size(); i++) {
                attribute = (Attribute) attributes.get(i);
                name = attribute.getName();
                if (XSI_NO_NAMESPACE_SCHEMA_LOCATION_ATTRIBUTE.equals(name)) {
                    // we have found a noNamespaceSchemaLocation declaration.
                    // ensure this validator is made aware of the schema
                    foundSchema = true;
                    declareNoNamespaceSchemaLocation(attribute.getValue());
                } else if (XSI_SCHEMA_LOCATION_ATTRIBUTE.equals(name)) {
                    // we have found a schemaLocation declaration.
                    // ensure this validator is made aware of the schema(s)
                    foundSchema = true;
                    declareSchemaLocation(attribute.getValue());
                }
            }
            if (!foundSchema) {
                // could not find the schemaLocation or
                // noNamespaceSchemaLocation attributes. Log a warning
                // as this could result in this validator having nothing
                // to validate against
                logger.warn("schema-location-not-defined-by-dom", document);
            }
        } else {
            logger.warn("dom-missing-root-element");
        }
    }

    // javadoc inherited
    public void enable(boolean enable) {
        this.enabled = enable;
    }

    // Javadoc not required
    protected boolean isEnabled() {
        return enabled;
    }

    // javadoc inherited
    public void validate(Element node) {
        if (enabled) {
            performValidate(node);
        }
    }

    // javadoc inherited
    public void terminateValidation() {
        validator.terminateValidation();
    }
    
    /**
     * Indicate if validation should be fast or not. Fast validation can
     * consume 100% cpu during validation potentially making the system
     * inoperable for the duration of validation but is about 3 times faster.
     *
     * @param fastValidation true if fast validation is required; false
     *                       if low cpu utilization is preferred
     */
    public void setFastValidation(boolean fastValidation) {
        validator.setFastValidation(fastValidation);
    }

    /**
     * Validates the specified {@link Element}.
     *
     * @param node the element to validate.
     */
    private void performValidate(final Element node) {
        if (node == null) {
            throw new IllegalArgumentException(
                    "cannot validate a null Element");
        }
        if (validationInProgress) {
            throw new IllegalStateException(
                    "A validation is already in progress");
        }

        // set the validationInProgress flag.
        validationInProgress = true;

        // write out the elements XML to char array
        XMLOutputter outputter = new XMLOutputter();
        CharArrayWriter writer = new CharArrayWriter();
        Document document = node.getDocument();
        try {
            if (document != null) {
                DocType docType = document.getDocType();
                if (docType != null) {
                    outputter.output(docType, writer);
                }
            }
            outputter.output(node, writer);

            // create an input source from the char array
            InputSource inputSource =
                    new InputSource(new CharArrayReader(writer.toCharArray()));

            validator.setRoot(node);
            // validate the XML
            xmlReader.parse(inputSource);
        } catch (IOException e) {
            throw new UndeclaredThrowableException(e);
        } catch (SAXException e) {
            if (e instanceof TerminateParsingException) {
                // Validation has been forcibly terminated. Take no action.
                if (logger.isDebugEnabled()) {
                    logger.debug("Validation was terminated with a " +
                            "TerminateParsingException", e);
                }
            } else {
                throw new UndeclaredThrowableException(e);
            }
        } catch (ConcurrentModificationException e) {
            // If this happens it indicates that a change has been made to
            // the document being validated while validation is in progress
            // and this change has prevented validation from completing. The
            // right course of action is to simply stop validating since this
            // change will be validated the next time validation is run.            
        } finally {
            validationInProgress = false;
        }
    }

    // javadoc inherited
    public void addSupplementaryValidator(String namespaceURI,
                                          String elementName,
                                          DOMSupplementaryValidator supplimentaryValidator) {

        // register the supplimentary validator with the SAXValidator
        validator.addSupplementaryValidator(namespaceURI,
                elementName,
                supplimentaryValidator);
    }

    // javadoc inherited
    public void removeSupplementaryValidator(String namespaceURI,
                                             String elementName,
                                             DOMSupplementaryValidator supplimentaryValidator) {

        // unregister the supplimentary validator with the SAXValidator
        validator.removeSupplementaryValidator(namespaceURI,
                elementName,
                supplimentaryValidator);
    }

    // javadoc inherited
    public ErrorReporter getErrorReporter() {
        return errorReporter;
    }

    // javadoc inherited
    public void setErrorReporter(ErrorReporter errorReporter) {
        if (validationInProgress) {
            throw new IllegalStateException(
                    "Cannot change the errorReporter because validation is " +
                            "already in progress");
        } else if (errorReporter == null) {
            throw new IllegalArgumentException(
                    "An errorReporter may not be null");
        }

        this.errorReporter = errorReporter;
    }

    /**
     * Returns true iff validation is currently in progress
     *
     * @return true iff validation is currently in progress
     */
    public boolean isValidationInProgress() {
        return validationInProgress;
    }

    /**
     * Factory method that factors a {@link SAXValidator} instance.
     *
     * @return a <code>SAXValidator</code> instance
     */
    protected SAXValidator createSAXValidator() {
        ErrorReporter passthroughErrorReporter = new ErrorReporter() {
            public void reportError(ErrorDetails details) {
                String key = details.getKey();

                if (key == null) {
                    Attributes attributes = details.getAttributes();
                    XPath currentPath = details.getXPath();
                    String errorMessage = details.getMessage();

                    // check to see if a ParserError matches
                    boolean matched = false;
                    for (int j = 0; j < parserErrors.size() && !matched; j++) {
                        ParserError parserError =
                                (ParserError) parserErrors.get(j);
                        String errorParam = parserError.matchedArgument(
                                attributes, errorMessage);

                        if (errorParam != null) {
                            // found a match if the error parameter is not null
                            matched = true;
                            XPath errorPath = currentPath;
                            if (parserError.isAttribute()) {
                                // append the attribute to the path.
                                errorPath = new XPath(currentPath,
                                        "@" + errorParam);
                            }
                            // Modify the error details to include key/param
                            details.setXPath(errorPath);
                            details.setKey(parserError.getKey());
                            details.setParam(errorParam);
                        }

                    }

                    // if we did not find a match then report the "catch all"
                    // error. The paramater for this will be the error message
                    if (!matched) {
                        // log the error
                        logger.warn("sax-gui-parse-error-catch-all",
                                errorMessage);
                        // Modify the error as a catch-all error
                        details.setKey(FaultTypes.UNKNOWN_INVALID_XML);
                        details.setParam(errorMessage);
                    }
                }

                errorReporter.reportError(details);
            }

            public void validationStarted(Element root, XPath xpath) {
                errorReporter.validationStarted(root, xpath);
            }

            public void validationCompleted(XPath xpath) {
                errorReporter.validationCompleted(xpath);
            }
        };
        return new SAXValidator(passthroughErrorReporter);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/3	adrianj	VBM:2004112605 Refactor XML validation error reporting (rework)

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 11-Aug-04	5126/1	adrian	VBM:2004080303 Added GUI support for Device TACs

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 27-Feb-04	3246/1	byron	VBM:2004021205 Lack of validation for policy file extensions

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 08-Jan-04	2431/5	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2431/3	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2431/1	allan	VBM:2004010404 Fix validation and display update issues.

 06-Jan-04	2323/4	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 17-Dec-03	2219/3	doug	VBM:2003121502 Added dom validation to the eclipse editors

 15-Dec-03	2160/9	doug	VBM:2003120702 Addressed some rework issues

 15-Dec-03	2160/7	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 10-Dec-03	2084/7	doug	VBM:2003120201 Fixed problem with the INVALID_ELEMENT_CONTENT ParserError

 10-Dec-03	2084/5	doug	VBM:2003120201 Xerces based DOMValidator implementation

 09-Dec-03	2084/1	doug	VBM:2003120201 xerces based DOMValidator implementation

 ===========================================================================
*/
