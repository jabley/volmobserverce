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
package com.volantis.xml.utilities.sax;

import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.XMLFilterImpl;
import com.volantis.xml.sax.ExtendedSAXParseException;

/**
 * An XMLFilter that can parse document fragments. In addition to the standard
 * SAX properties it has another property
 * "http://www.volantis.com/DocumentFragmentParser/trimWhitespace"
 * that determines if whitespace is trimmed or not. Whitespace trimming
 * applies to leading and trailing whitespace around the document. It
 * also applies whitespace compression between nodes (where the whitespace
 * is reduced to a single character).
 */
public class DocumentFragmentFilter extends XMLFilterImpl {
    /**
     * The property that defines whether this parser will pass on leading
     * whitespace characters to the client E.g.
     * <pre>
     * &lt;fragment&gt; &lt;-------- inserted root element
     *      text
     *      &lt;p&gt;Para1&lt;/p&gt;
     * &lt;/fragment&gt;
     * </pre>
     * In the above example this flag will determine if the whitespace between
     * the &lt;fragment&gt; and the initial text will reach the client. If not
     * set then the client will receive a SAX characters event including the
     * whitespace and the text. If set then the client will receive a SAX
     * characters event without the whitespace. In addition, whitespace between
     * nodes will be reduced to a single value and whitespace after the end
     * will be removed.
     */
    public static final String TRIM_WHITESPACE =
            "http://www.volantis.com/DocumentFragmentParser/trimWhitespace";

    /**
     * Indicates whether the whitespace triming feature is set or not. Default
     * is not to trim
     */
    private boolean trimWhitespace = false;

    /**
     * When triming is enabled, the trailing whitespace wants to be consumed
     * too. Note, however, that between character events and other character
     * events and/or start element events, suppressed whitespace must be
     * re-inserted to ensure that whitespacing works correctly.
     */
    private boolean needsWhitespace = false;

    /**
     * When triming is enabled, any output of characters must be indicated
     * so that interceding whitespace blocks can be ignored correctly but
     * whitespace needs met when start and end element events are found.
     */
    private boolean lastWasChars = false;

    private boolean trailingTrimmed = false;

    /**
     * If need be, a special whitespace characters event can be used when
     * {@link #needsWhitespace} is true. This is the source of the whitespace
     * that will be output in that case.
     */
    private static final char[] WHITESPACE = {' '};

    /**
     * Depth count to remove fragment root element
     */
    private int depth = 0;

    /**
     * The DocumentFragmentLocator member
     */
    private DocumentFragmentLocator wrappedLocator;

    /**
     * Parses an input source containing a document fragment by first wrapping
     * it in a DocumentFragmentInputSource
     *
     * @param input
     */
    public void parse(InputSource input) throws SAXException, IOException {
        doParse(new DocumentFragmentInputSource(input));
    }

    /**
     * Parses a system Id to a document frgament by ensuring it is wrapped in a
     * root element by creating a new DocumentFragmentInputSource from the
     * supplied system ID
     *
     * @param systemId
     */
    public void parse(String systemId) throws SAXException, IOException {
        doParse(new DocumentFragmentInputSource(systemId));
    }

    private void doParse(DocumentFragmentInputSource fragInputSource)
            throws SAXException, IOException {
        // reset internal state in case this parser is re-used
        needsWhitespace = false;
        lastWasChars = false;
        trailingTrimmed = false;

        try {
            super.parse(fragInputSource);
        } catch (SAXParseException saxe) {
            // this exception is caught and fixed because if the
            // http://apache.org/xml/features/continue-after-fatal-error
            // property is not set this exception will be available externally
            throw new ExtendedSAXParseException(saxe.getLocalizedMessage(),
                                        wrappedLocator);
        }
    }

    /**
     * Sets the document locator to a class that corrects the line number
     * information
     *
     * @param locator
     */
    public void setDocumentLocator(Locator locator) {
        wrappedLocator = new DocumentFragmentLocator(locator);

        super.setDocumentLocator(wrappedLocator);
    }

    /**
     * This method prevents the first element of the document fragment from
     * generating events. This is the &lt;fragment&gt; element
     *
     * @param namespaceURI
     * @param localName
     * @param qName
     * @param atts
     */
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        if (depth++ > 0) {
            // Handle a hold-over whitespace requirement (from the end of
            // a preceding characters event)
            if (needsWhitespace) {
                super.characters(WHITESPACE, 0, 1);
                needsWhitespace = false;
            }

            lastWasChars = false;

            super.startElement(namespaceURI, localName, qName, atts);
        }

    }

    /**
     * This method prevents the last element of the document fragment from
     * generating events. This is the &lt;/fragment&gt; element
     *
     * @param namespaceURI
     * @param localName
     * @param qName
     */
    public void endElement(String namespaceURI, String localName,
                           String qName) throws SAXException {
        if (--depth != 0) {
            // Note that this specifically doesn't handle hold-over
            // whitespace: if a following characters or startElement
            // is found, any hold-over will be output. If this is the
            // last end element we don't want to output the hold-over
            // anyway. Note, however, that if the lastWasChars attribute
            // was set, a whitespace will be required if another
            // characters event follows this end element (this will also
            // output whitespace between the end element and the next start)
            if (lastWasChars && trailingTrimmed) {
                needsWhitespace = true;
            }

            super.endElement(namespaceURI, localName, qName);
        }
    }

    /**
     * This Filter needs to check if it can pass on leading characters to the
     * ContentHandler or not. If not it needs to swallow all character between
     * the <fragment> element and the first element of the document fragment
     *
     * @param characters
     * @param off
     * @param len
     */
    public void characters(char[] characters, int off, int len)
            throws SAXException {
        // If we are not within the artificial root node consume the
        // event
        if (depth > 0) {
            // See if triming is required
            if (trimWhitespace) {
                int first = findFirstNonWhiteSpace(characters,
                                                   off, len);

                // if the length of the whitespace is the same as the length
                // of the characters then we simply consume the whole event
                // and look to see if we can trim further whitespace.
                // Otherwise, we pass the non-whitespace text on through
                if (first != (off + len)) {
                    // Handle a hold-over whitespace requirement (from the
                    // end of a previous characters event)
                    if (needsWhitespace) {
                        if (first > off) {
                            first--;
                        } else {
                            // Sadly we have to resort to two events to
                            // output this characters event
                            super.characters(WHITESPACE, 0, 1);
                        }
                    } else if (lastWasChars && (first > off)) {
                        first--;
                    }

                    // Note that this resets needsWhitespace
                    int last = findLastNonWhitespace(characters,
                                                     off, len);

                    super.characters(characters,
                                     first,
                                     last - first + 1);

                    lastWasChars = true;
                    trailingTrimmed = needsWhitespace;
                } else if (lastWasChars) {
                    needsWhitespace = true;
                }
            } else {
                super.characters(characters, off, len);
            }
        }
    }

    /**
     * Returns the index of the first non-whitespace character if any. If all
     * whitespace <code>off + len</code> is returned.
     *
     * @param characters the character array to be examined
     * @param off        the start of the sub-section of interest
     * @param len        the length of the sub-section of interest
     * @return index of the first non-whitespace character or <code>off +
     *         len</code>
     */
    private int findFirstNonWhiteSpace(char[] characters, int off, int len) {
        int foundWhiteSpace = off;

        while ((foundWhiteSpace < off + len) &&
                Character.isWhitespace(characters[foundWhiteSpace])) {
            foundWhiteSpace++;
        }

        return foundWhiteSpace;
    }

    /**
     * Returns the index of the last non-whitespace character if any. If all
     * whitespace is found <code>off - 1</code> is returned.
     *
     * @param characters the character array to be examined
     * @param off        the start of the sub-section of interest
     * @param len        the length of the sub-section of interest
     * @return index of last non-whitespace character or <code>off - 1</code>
     */
    private int findLastNonWhitespace(char[] characters, int off, int len) {
        int foundWhiteSpace = off + len - 1;

        if (trimWhitespace) {
            needsWhitespace =
                    Character.isWhitespace(characters[foundWhiteSpace]);

            while ((foundWhiteSpace >= off) &&
                    Character.isWhitespace(characters[foundWhiteSpace])) {
                foundWhiteSpace--;
            }
        }

        return foundWhiteSpace;
    }

    /**
     * Resets the trimLeadingWhiteSpace flag if we encounter a processing
     * instruction
     */
    public void processingInstruction(String arg0, String arg1)
            throws SAXException {
        lastWasChars = false;
        super.processingInstruction(arg0, arg1);
    }

    /**
     * ErrorHandler method implementation. Fix column number information and
     * pass on down the filter chain
     *
     * @param e
     */
    public void error(SAXParseException e) throws SAXException {
        SAXParseException fixed = fixUpException(e);
        if (getErrorHandler() != null) {
            getErrorHandler().error(fixed);
        }
    }

    /**
     * ErrorHandler method implementation. Fix column number information and
     * pass on down the filter chain
     *
     * @param e
     */
    public void fatalError(SAXParseException e) throws SAXException {
        SAXParseException fixed = fixUpException(e);
        if (getErrorHandler() != null) {
            getErrorHandler().fatalError(fixed);
        } else {
            throw e;
        }
    }

    /**
     * ErrorHandler method implementation. Fix column number information and
     * pass on down the filter chain
     *
     * @param e
     */
    public void warning(SAXParseException e) throws SAXException {
        SAXParseException fixed = fixUpException(e);
        if (getErrorHandler() != null) {
            getErrorHandler().warning(fixed);
        }
    }

    /**
     * Corrects line number information for generated SAXExceptions.
     */
    private SAXParseException fixUpException(SAXParseException saxException) {

        // get the fixed column number
        int columnNumber = DocumentFragmentLocator.fixColumnNumber(
                saxException.getLineNumber(),
                saxException.getColumnNumber());

        // Create and return a new ExtendedSAXParseException with correct column number information
        SAXParseException except = new ExtendedSAXParseException(
                saxException.getMessage(),
                saxException.getPublicId(), saxException.getSystemId(),
                saxException.getLineNumber(), columnNumber,
                saxException);
        return except;

    }

    /**
     * Gets is a SAX feature or one specific to this Filter
     */
    public boolean getFeature(String feature)
            throws SAXNotRecognizedException, SAXNotSupportedException {
        if (TRIM_WHITESPACE.equals(feature)) {
            return trimWhitespace;
        } else {
            return super.getFeature(feature);
        }
    }

    /**
     * Sets is a SAX feature or one specific to this Filter
     */
    public void setFeature(String feature, boolean value)
            throws SAXNotRecognizedException, SAXNotSupportedException {

        if (TRIM_WHITESPACE.equals(feature)) {
            trimWhitespace = value;
        } else {
            super.setFeature(feature, value);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Jun-03	143/1	philws	VBM:2003062610 Fix the document fragment filter's whitespace trimming

 23-Jun-03	95/1	doug	VBM:2003061605 Document Event Filtering changes

 16-Jun-03	23/8	byron	VBM:2003022819 Update to get jsp TLD files with correct merge

 13-Jun-03	23/6	byron	VBM:2003022819 Integration complete

 13-Jun-03	78/1	philws	VBM:2003061205 Added some JSP test pages and fixed some tag bugs

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 ===========================================================================
*/
