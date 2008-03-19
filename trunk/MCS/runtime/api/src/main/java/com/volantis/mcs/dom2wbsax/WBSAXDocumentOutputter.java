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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-May-03    Mat             VBM:2003042907 - Created to generate WBSAX
 *                              events from an MCSDOM.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * 28-May-03    Mat             VBM:2003042907 - Changes to Javadoc, output
 *                              $$ for WMLV_LITERAL and throw an IOException
 *                              from output(Document)
 * 28-May-03    Mat             VBM:2003042911 - Added calls to startContent()
 *                              and endContent() when outputting elements
 *                              with content.
 * 30-May-03    Mat             VBM:2003042911 - Added code to add opaque
 *                              elements for special dissection types.
 * 30-May-03    Mat             VBM:2003042906 - Various changes for integration
 * 01-Jun-03    Chris W         VBM:2003042906 - In output(Element) ensure that
 *                              endElement() is always called for special
 *                              elements
 * 01-Jun-03    Chris W         VBM:2003042906 - Add magic url as an attribute
 *                              opaque value rather than a content opaque value
 * 02-Jun-03    Mat             VBM:2003042906 - Remove change where endElement()
 *                              is always called for special elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2wbsax;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.VisitorWrappingException;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.util.HashMap;

/**
 * Class to serialise the MCSDOM into WBSAX events.
 *
 * Currently only used for WML protocols.
 * @todo later this class was renamed from WBSAXOutputter to WBSAXDocumentOutputter but the corresponding test case wasn't renamed.
 */
public class WBSAXDocumentOutputter
        extends RecursingDOMVisitor
        implements DocumentOutputter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WBSAXDocumentOutputter.class);

    /**
     * Shared context for processing the DOM.
     */
    private final WBSAXProcessorContext context;

    /**
     * A Map of special element names to special element processors.
     * <p>
     * This is set up in the constructor and then used to look up element
     * processors for special elements by name.
     * <p>
     * Currently we have two types of special elements; the set of those used
     * for dissection and also the accesskey annotation element.
     */
    private final HashMap specialElementProcessors = new HashMap();

    /**
     * The default element processor to use if no special element processor
     * was found for the element name to be processed.
     */
    private final WBSAXElementProcessor defaultElementProcessor;

    /**
     * Constructor for the WBSAXDocumentOutputter.
     *
     * @param context
     */
    public WBSAXDocumentOutputter(WBSAXProcessorContext context) {

        // Save away our shared context.
        this.context = context;

        // Create the default processors.
        // In future these will need to be configurable...
        defaultElementProcessor = new WBSAXElementProcessor(context);
        // Allow text processing to proceed before we have an element
        // This is only really required by the test case...
        context.pushElementProcessor(defaultElementProcessor);

    }

    public void addSpecialElementProcessor(String elementName,
            WBSAXElementProcessor elementProcessor) {
        specialElementProcessors.put(elementName, elementProcessor);
    }

    /**
     * Process the given document into WBSAX events.
     * @param document The document to process.
     * @throws java.io.IOException A problem outputting the document.
     */
    public void output(Document document) throws IOException {
        try {

            if (logger.isDebugEnabled()) {
                logger.debug("Starting document ...");
            }
            context.getContentHandler().startDocument(
                context.getConfiguration().getVersionCode(),
                context.getConfiguration().getPublicIdCode(),
                context.getStrings().getCodec(), context.getStringTable(),
                    context.getStrings());

            if (logger.isDebugEnabled()) {
                logger.debug("Processing document content ...");
            }
            document.forEachChild(this);

            if (logger.isDebugEnabled()) {
                logger.debug("Ending document ...");
            }
            context.getStringTable().markComplete();
            context.getContentHandler().endDocument();

        } catch (VisitorWrappingException e) {
            throw (IOException) e.getCause();
        } catch (WBSAXException e) {
            logger.error("wbsax-exception-caught", e);
            throw new IOException(e.getLocalizedMessage());
        }
    }

    /**
     * Process an element into WBSAX events.
     *
     * @param element The element to process.
     * @throws java.io.IOException A problem processing
     */
    public void output(Element element) throws IOException {

        try {
            element.accept(this);
        } catch (VisitorWrappingException e) {
            throw (IOException) e.getCause();
        }
    }

    // Javadoc inherited.
    public void visit(Element element) {
        try {
            outputElement(element);
        } catch (IOException e) {
            throw new VisitorWrappingException(e);
        }
    }


    // Javadoc inherited.
    public void visit(Text text) {
        try {
            outputText(text);
        } catch (IOException e) {
            throw new VisitorWrappingException(e);
        }
    }

    /**
     * Start the element and add its attributes.
     * It is possible for an element to have a null name.  If this is the case,
     * the element is ignored and only the children are written.
     * If the element name is null, this method will return false indicating
     * that the close element need no be written.
     *
     * @param element The element to start
     * @param content Whether the element has children
     *
     * @throws com.volantis.mcs.wbsax.WBSAXException A WBSAX problem
     */
    private void elementStart(Element element, boolean content)
        throws WBSAXException {

        String name = element.getName();
        // NOTE: a null name is considered valid.

        // Find the processor for the new element.
        WBSAXElementProcessor processor = (WBSAXElementProcessor)
                specialElementProcessors.get(name);
        if (processor == null) {
            processor = defaultElementProcessor;
        }

        context.pushElementProcessor(processor);

        if (logger.isDebugEnabled()) {
            logger.debug("Starting element " + element.getName() + " using " +
                    "processor " + processor);
        }

        // And finally process the element.
        processor.elementStart(element, content);

    }

    /**
     * End the element.
     *
     * @throws com.volantis.mcs.wbsax.WBSAXException A WBSAX problem
     */
    private void elementEnd(Element element, boolean content)
            throws WBSAXException {

        if (logger.isDebugEnabled()) {
            logger.debug("Ending element " + element.getName() + " using " +
                    "processor " + context.getElementProcessor());
        }

        context.getElementProcessor().elementEnd(element, content);

        context.popElementProcessor();

    }

    /**
     * Process an element into WBSAX events.
     *
     * @param element The element to process.
     * @throws java.io.IOException A problem processing
     */
    private void outputElement(Element element) throws IOException {

        // Get the first child in the list of children.
        boolean content = !element.isEmpty();

        // If we are generating the final output (no WBDOM) and
        // the element was empty but we aren't allowed to write it as
        // such then pretend is has content.
        if (context.isFinalOutput() && !content &&
                context.getConfiguration().getEmptyElementType(
                        element.getName()) ==
                    EmptyElementType.StartAndEndTag) {
            if (logger.isDebugEnabled()) {
                logger.debug("Making empty tag '" + element.getName() +
                        "' appear as start and end tag for direct output");
            }
            content = true;
        }

        try {
            // Output the element
            // Do the start of the element.
            elementStart(element, content);
            if (content) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Processing element content start using " +
                            "processor " + context.getElementProcessor());
                }
                context.getElementProcessor().contentStart();
                element.forEachChild(this);
                if (logger.isDebugEnabled()) {
                    logger.debug("Processing element content end using " +
                            "processor " + context.getElementProcessor());
                }
                context.getElementProcessor().contentEnd();
            }
            // Do the end of the element, this must be called even if we are 
            // ignoring so it may deregister any value processors it had.
            elementEnd(element, content);
        } catch (WBSAXException e) {
            logger.error("unexpected-exception", e);
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Process the contents of a text node.  This method checks whether the character
     * is representable in the chosen character set, and creates an entity for any which
     * are not.
     *
     * @param text The text to process.
     * @throws java.io.IOException A problem in the processing.
     */
    private void outputText(Text text) throws IOException {

        // Ensure that we die if anyone is attempts to provide pre-encoded
        // text nodes to us, since WBXML requires we do all the encoding.
        // Any pre-encoded text would thus be double encoded and broken.
        if (text.isEncoded()) {
            throw new IllegalStateException(
                    "WML does not support encoded text nodes");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Processing text " + text.getContents() + " using " +
                    "processor " + context.getElementProcessor());
        }

        try {
            context.getElementProcessor().text(text.getContents(), 
                    text.getLength());
        } catch (WBSAXException e) {
            logger.error("unexpected-exception", e);
            throw new IOException(e.getMessage());
        }
    
    }

    // Javadoc inherited.
    public void flush() {

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/2	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 14-Apr-04	3862/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 14-Apr-04	3847/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 25-Feb-04	2974/6	steve	VBM:2004020608 Merging Hell

 25-Feb-04	2974/4	steve	VBM:2004020608 Merging Hell

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 17-Feb-04	2974/2	steve	VBM:2004020608 SGML Quote handling

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 06-Oct-03	1469/9	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/7	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 17-Sep-03	1412/2	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 12-Sep-03	1301/1	byron	VBM:2003082107 Support Openwave GUI Browser extensions - string literal bug fix

 09-Sep-03	1336/2	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/3	geoff	VBM:2003071405 works and tested but no design review yet

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 05-Jun-03	285/18	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
