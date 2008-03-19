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
package com.volantis.mcs.xml.validation.sax;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidationProvider;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.sax.ExtendedSAXParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * The only way to actually determine the errors in the given document
 * (sub-)tree is to serialize the document (sub-)tree as SAX events which
 * can interact with the SAX parser's validation mechanism, to pick up the
 * validation errors (not warnings or fatal errors), interpret the error
 * message to resolve the error's contextual information and process that
 * information in the appropriate SAX event notification.
 * <p/>
 * <p>Within the Volantisized XERCES SAX parser implementation, XSD and DTD
 * errors are generated using messages defined in the following resource
 * bundles:</p>
 * <p/>
 * <dl>
 * <p/>
 * <dt>XSD errors</dt>
 * <p/>
 * <dd>com.volantis.xml.xerces.impl.msg.XMLSchemaMessages</dd>
 * <p/>
 * <dt>DTD errors</dt>
 * <p/>
 * <dd>com.volantis.xml.xerces.impl.msg.XMLMessage</dd>
 * <p/>
 * </dl>
 * <p/>
 * <p>The sequencing of error notifications to SAX events is:</p>
 * <p/>
 * <dl>
 * <p/>
 * <dt>attribute errors</dt>
 * <p/>
 * <dd>generated immediately before the associated {@link #startElement}
 * event.</dd>
 * <p/>
 * <dt>element errors</dt>
 * <p/>
 * <dd>generated between the {@link #startElement} and {@link #endElement}
 * events, but not "within" any nested nodes.</dd>
 * <p/>
 * <dt>constraint errors</dt>
 * <p/>
 * <dd>generated immediately before the {@link #endElement} event for the
 * element containing the constraint</dd>
 * <p/>
 * </dl>
 * <p/>
 * <p>Each supported error must be handled by getting the message string out
 * of the resource bundle (accounting for any localization). Each parameter
 * substitution must then be converted into a regular expression marker such
 * as ".*", resulting an an "error template". When processing a given error
 * report, the error string must be processed (via a regular expression
 * matcher) against each available "error template". The regular expression
 * markers can be used to populate the (optional) element name,
 * (optional) attribute name and (optional) constraint name parameters.</p>
 * <p/>
 * <p/>
 * <p>The SAX model uses character and line number information to locate
 * errors. This is not adequate for our purposes. What is actually required is
 * a fully qualified, explicitly defined, absolute XPath.</p>
 * <p/>
 * <p>The ContentHandler aspect of this class must maintain the notion of the
 * current XPath, including any predication of element and text nodes. It
 * should maintain as little state data as possible (essentially only
 * maintaining state for the elements in the "branch" that is currently being
 * processed.)</p>
 * <p/>
 * <p>All sequential characters events must be deemed to be for the name text
 * node.</p>
 */
public class SAXValidator implements ContentHandler,
        ErrorHandler,
        DOMSupplementaryValidationProvider {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SAXValidator.class);

    /**
     * This is the "root" node within which validation is being performed.
     *
     * @supplierRole root
     * @supplierCardinality 1
     */
    private Element root;

    /**
     * Flag to indicate that validation has been terminated.
     */
    private boolean validationTerminated = false;

    /**
     * Flag to indicate if validation should be fast or not. If not
     * fast the startElement method will sleep for 1 millisecond
     * each time it is invoked. Sleeping for 1 millisecond prevents
     * the parser from consuming 100% cpu during validation which may
     * be preferable under some circumstances. Fast validation is about
     * 3 times fasters than sleeping for 1 millisecond.
     */
    private boolean fastValidation = true;

    /**
     * This is the XPath for the "root" node.
     *
     * @supplierRole rootXPath
     * @supplierCardinality 1
     */
    private XPath rootXPath;

    /**
     * Stores lists of {@link DOMSupplementaryValidator}s against element
     * names for which they are to be invoked.
     *
     * @supplierCardinality 0..*
     * @supplierRole supplementaryValidators
     * @associates DOMSupplementaryValidator
     */
    private Map supplementaryValidators;

    /**
     * All errors found are reported to this object.
     *
     * @supplierRole errorReporter
     * @supplierCardinality 1
     */
    private ErrorReporter errorReporter;

    /**
     * The locater that will be provided via the {@link #setDocumentLocator}
     * method
     */
    private Locator locator;

    /**
     * This is used to track the XPath of the element that is currently
     * being processed
     *
     * @supplierRole tracker
     * @supplierCardinality 1
     */
    private XPathTracker tracker;

    /**
     * This is the list of pending error message.
     *
     * @supplierRole pendingErrors
     * @supplierCardinality 0..*
     * @associates PendingError
     * @link aggregation
     */
    private List pendingErrors;

    /**
     * This will be used perform lookups for {@link DOMSupplementaryValidator}s
     * that have been registered against a particular element
     */
    private ExpandedName supplementaryLookupName;

    /**
     * Initializes a <code>SAXValidator</code> with the given arguments
     *
     * @param errorReporter used to report validation errors
     * @throws IllegalArgumentException if the root element errorReporter are
     *                                  null
     */
    public SAXValidator(ErrorReporter errorReporter) {
        if (errorReporter == null) {
            throw new IllegalArgumentException(
                    "Validation requires an error reporter to be available");
        }
        this.errorReporter = errorReporter;
    }

    /**
     * Allows validation to be forcibly terminated.
     */
    public void terminateValidation() {
        validationTerminated = true;
    }


    /**
     * Indicate if validation should be fast or not. If not
     * fast the startElement method will sleep for 1 millisecond
     * each time it is invoked. Sleeping for 1 millisecond prevents
     * the parser from consuming 100% cpu during validation which may
     * be preferable under some circumstances - eg where validation is
     * taking a long time. Fast validation is about
     * 3 times fasters than non-fast validation.
     *
     * @param fastValidation true if fast validation is required; false
     */
    public void setFastValidation(boolean fastValidation) {
        this.fastValidation = fastValidation;
    }

    /**
     * Sets the "root" node within which validation is being performed.
     *
     * @param root the root node
     * @throws IllegalArgumentException if the root parameter is null
     */
    public void setRoot(Element root) {
        if (root == null) {
            throw new IllegalArgumentException(
                    "Validation requires the root node of the DOM (sub-)tree to " +
                            "be validated");
        }
        this.root = root;
        this.rootXPath = new XPath(root);
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    // javadoc inherited
    public void startDocument() {
        if (root == null) {
            // Force an exception to be thrown
            setRoot(null);
        }
        tracker = new XPathTracker(root);
        errorReporter.validationStarted(root, rootXPath);
    }

    // javadoc inherited
    public void endDocument() {
        errorReporter.validationCompleted(rootXPath);

        // if we haven't processed all the errors by this stage we should
        // throw an exception
        if (!tracker.isEmpty() ||
                ((pendingErrors != null) &&
                        !pendingErrors.isEmpty())) {
            throw new IllegalStateException(
                    "There should be no pending errors when the end of the " +
                            "document is reached");
        }
    }

    // javadoc inherited
    public void startPrefixMapping(String str, String str1) {
        // Does nothing
    }

    // javadoc inherited
    public void endPrefixMapping(String str) {
        // Does nothing
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {

        if (validationTerminated) {
            // Throwing an exception forces parser termination - the only way to
            // do it. In this case there is no problem - just we want to stop
            // parsing. So we throw this TerminateParsingException which should
            // be specifically caught and ignored higher up.
            throw new TerminateParsingException("Validation terminated");
        }

        if (!fastValidation) {
            try {
                // This prevents 100% cpu utilization during validation but
                // roughly triples validation time
                Thread.sleep(1);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }

        try {
            tracker.startElement(namespaceURI, localName, qName);
        } catch (XPathException e) {
            throw new ExtendedSAXParseException(
                    "Could not process startElement event",
                    locator,
                    e);
        } catch (IllegalStateException e) {
            // This situation can arise because validation now depends upon
            // obtaining the list of child elements and iterating over them
            // rather than explicitly searching for children using xpath. As
            // a result it is possible that the list of children is out of date
            // for example if two quick editions occur that change the children.
            // This is not a problem because the IllegalStateException merely
            // indicates that the document being validated is out of date and
            // therefore does not need to be validated as it is no longer
            // current. In this situation parsing should be terminated by
            // throwing a TerminateParsingException and specifically catching
            // this and ignoring it high.
            terminateValidation();
            throw new TerminateParsingException("Document out-of-date");
        }

        // Report any "supplementary" errors before any validation errors.
        // This is because we currently throw away (!) any errors after the
        // first one which occurs for each xpath and the supplementary errors
        // are higher level than the schema validation errors.
        try {
            invokeSupplementaryValidators(namespaceURI, localName);
        } catch (XPathException e) {
            throw new ExtendedSAXParseException(
                    "Could not process endElement event",
                    locator,
                    e);
        }

        // report any pending errors. As attribute errors are generated before
        // a startElement event attribute errors may be pending, hence
        // why the attributes are being passed in
        reportPendingErrors(tracker.currentElement(), attributes);
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXParseException {

        // report any pending errors. As attribute errors are generated before
        // a startElement they should all have been handled,  hence the
        // null value for the attributes arg
        reportPendingErrors(tracker.currentElement(), null);

        // inform the tracker of the end element.
        tracker.endElement(namespaceURI, localName, qName);
    }

    // javadoc inherited
    public void characters(char[] chars, int offset, int length) {
        // report any pending errors. As attribute errors are generated before
        // a startElement they should all have been handled,  hence the
        // null value for the attributes arg
        reportPendingErrors(tracker.currentElement(), null);
    }

    // javadoc inherited
    public void ignorableWhitespace(char[] param, int i, int i1) {
        // Does nothing
    }

    // javadoc inherited
    public void processingInstruction(String str, String str1) {
        // Does nothing
    }

    // javadoc inherited
    public void skippedEntity(String str) {
        // Does nothing
    }

    // javadoc inherited
    public void warning(SAXParseException e) {
        // Does nothing
    }

    // javadoc inherited
    public void error(SAXParseException e) {
        // lazily create the pending error list
        if (pendingErrors == null) {
            pendingErrors = new ArrayList();
        }

        // log the error
        if (logger.isDebugEnabled()) {
            logger.debug("validation-error", e);
        }

        // add the error message to the list of pending errors
        pendingErrors.add(e.getMessage());
    }

    // javadoc inherited
    public void fatalError(SAXParseException e) {
        // Does nothing
    }

    // javadoc inherited
    public void addSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator) {
        // lazily create the map that will contain the supplementary validators
        if (supplementaryValidators == null) {
            supplementaryValidators = new HashMap();
        }

        // retrieve the list of validators for the named element
        List existingValidators = getSupplementaryValidators(namespaceURI,
                elementName);

        if (existingValidators == null) {
            // lazily create the list if it does not exist
            existingValidators = new ArrayList();
            // store the list in the map
            supplementaryValidators.put(
                    new ExpandedName(namespaceURI, elementName),
                    existingValidators);
        }
        // finally add the supplementary validator to the list.
        existingValidators.add(supplementaryValidator);
    }

    // javadoc inherited
    public void removeSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator) {

        if (supplementaryValidators != null) {
            // retrieve the list of supplementary validators for the given
            // element name
            List existingValidators =
                    getSupplementaryValidators(namespaceURI, elementName);

            if (existingValidators != null) {
                // remove the validator from the list
                existingValidators.remove(supplementaryValidator);
            }
        }
    }

    /**
     * Returns the {@link ErrorReporter} that is being used to report errors
     *
     * @return the ErrorReporter that is being used.
     */
    public ErrorReporter getErrorReporter() {
        return errorReporter;
    }

    /**
     * Sets the {@link ErrorReporter} that will be used to report errors
     *
     * @param errorReporter the ErrorReporter to use.
     */
    public void setErrorReporter(ErrorReporter errorReporter) {
        if (errorReporter == null) {
            throw new IllegalArgumentException(
                    "An errorReporter may not be null");
        }
        this.errorReporter = errorReporter;
    }


    /**
     * Returns the XPath that identifies the current location in the sax
     * event stream.
     *
     * @return the XPath that identifies the current location in the sax
     *         event stream.
     */
    private XPath getCurrentXPath() {
        XPath currentPath = tracker.currentXPath();
        if (currentPath == null) {
            currentPath = rootXPath;
        } else {
            currentPath = new XPath(rootXPath, currentPath);
        }
        return currentPath;
    }

    /**
     * Method that reports any pending errors via the {@link ErrorReporter}
     *
     * @param element    the Element with which Attributes are associated.
     * @param attributes Attributes that may be passed in if the caller
     *                   is the {@link #startElement} method.
     */
    private void reportPendingErrors(Element element, Attributes attributes) {
        // if the pendingErrors list is null we clearly have no errors
        // to report
        if (pendingErrors != null) {
            // iterate of the pending errors reporting each one via the
            // ErrorReporter

            XPath currentPath = getCurrentXPath();
            try {
                for (int i = 0; i < pendingErrors.size(); i++) {
                    String errorMessage = (String) pendingErrors.get(i);

                    ErrorDetails details =
                            new ErrorDetails(element, currentPath,
                                    errorMessage, null, null, attributes);
                    errorReporter.reportError(details);
                }
            } finally {
                // clear the error list as they have all been reported.
                pendingErrors.clear();
            }
        }
    }

    /**
     * Invokes any {@link DOMSupplementaryValidator} instances that have been
     * registered against the given element and namespaceURI
     *
     * @param namespaceURI the namespace URI
     * @param elementName  the name of the element
     * @throws com.volantis.mcs.xml.xpath.XPathException
     *          if an error occurs
     */
    private void invokeSupplementaryValidators(String namespaceURI,
                                               String elementName)
            throws XPathException {
        if (supplementaryValidators != null) {
            // Invoke any supplementary validators that are are registered
            // for the specified element
            List validators = getSupplementaryValidators(namespaceURI,
                    elementName);

            if (validators != null) {
                Element element = tracker.currentElement();
                DOMSupplementaryValidator validator;
                for (int i = 0; i < validators.size(); i++) {
                    // ask the validator to validate the element
                    validator = (DOMSupplementaryValidator) validators.get(i);
                    validator.validate(element, errorReporter);
                }
            }
        }
    }

    /**
     * Used to obtain the list of {@link DOMSupplementaryValidator} objects
     * that have been registered for the named element.
     *
     * @param namespaceURI the namespace URI for the element
     * @param elementName  the name of the element
     * @return A List of {@link DOMSupplementaryValidator} objects of null if
     *         none have been registered.
     */
    private List getSupplementaryValidators(String namespaceURI,
                                            String elementName) {
        if (supplementaryLookupName == null) {
            supplementaryLookupName = new ExpandedName(namespaceURI,
                    elementName);
        } else {
            supplementaryLookupName.setNamespaceURI(namespaceURI);
            supplementaryLookupName.setName(elementName);
        }
        return (List) supplementaryValidators.get(supplementaryLookupName);
    }

    /**
     * Used to create keys for storing {@link DOMSupplementaryValidator}
     * instances in a map.
     */
    private static class ExpandedName {

        /**
         * The namespace URI of the element
         */
        private String namespaceURI;

        /**
         * The name of the element
         */
        private String name;

        /**
         * Initializes a <code>ExpandedName</code> with the given parameters
         *
         * @param namespaceURI the namespaceURI of the element. An empty
         *                     string refers to the default namespace. Must not be null.
         * @param name         the name of the element. Must not be null
         */
        public ExpandedName(String namespaceURI, String name) {
            this.namespaceURI = namespaceURI;
            this.name = name;
        }

        /**
         * Sets the name of the element
         *
         * @param name the name. Cannot be null.
         * @throws IllegalArgumentException if name parameter is null
         */
        public void setName(String name) {
            if (name == null) {
                throw new IllegalArgumentException(
                        "elements name cannot be null");
            }
            this.name = name;
        }

        /**
         * Sets the namespaceURI of the element
         *
         * @param namespaceURI the URI of the namespace. Cannot be null.
         * @throws IllegalArgumentException if name parameter is null
         */
        public void setNamespaceURI(String namespaceURI) {
            if (namespaceURI == null) {
                throw new IllegalArgumentException(
                        "elements name cannot be null");
            }
            this.namespaceURI = namespaceURI;
        }

        // javadoc inherited
        public boolean equals(Object o) {
            boolean isEqual = false;
            if (o != null && o.getClass() == getClass()) {

                // cast the testee to the correct type
                ExpandedName other = (ExpandedName) o;
                // equal iff both the elementName and namespaceURI memebers
                // are equal.
                isEqual = name.equals(other.name) &&
                        namespaceURI.equals(other.namespaceURI);
            }
            return isEqual;
        }

        // javadoc inherited
        public int hashCode() {
            return name.hashCode() + namespaceURI.hashCode();
        }

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05      7294/1  geoff   VBM:2005022311 Remote Repository Exceptions

 04-Mar-05      7247/1  geoff   VBM:2005022311 Remote Repository Exceptions

 21-Dec-04      6524/1  allan   VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04      6416/3  ianw    VBM:2004120703 New Build

 08-Dec-04      6416/1  ianw    VBM:2004120703 New Build

 02-Dec-04      6354/4  adrianj VBM:2004112605 Refactor XML validation error reporting (rework)

 02-Dec-04      6354/2  adrianj VBM:2004112605 Refactor XML validation error reporting

 29-Nov-04      6232/2  doug    VBM:2004111702 Refactored Logging framework

 13-May-04      4351/1  allan   VBM:2004051011 Fix NullPointerException in StandardElementHandler

 18-Feb-04      3068/1  allan   VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04      2820/1  doug    VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Jan-04      2323/1  doug    VBM:2003120701 Added better validation error messages

 02-Jan-04      2350/1  richardc        VBM:2004010201 Avoid NullPointerException when validating a top-level element

 10-Dec-03      2057/12 doug    VBM:2003112803 Addressed several rework issues

 09-Dec-03      2057/8  doug    VBM:2003112803 Added SAX XSD Validation mechanism

 ===========================================================================
*/
