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
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.shared.throwable.ExtendedRuntimeException;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.AttributesImpl;

import java.net.MalformedURLException;

/**
 * An <code>XMLProcess</code> that updates the state of the
 * <code>XMLPipelineContext</code>. In particular it updates the
 * base uri by looking for the "base" attribute for every
 * {@link com.volantis.xml.pipeline.sax.XMLProcess#startElement} event
 * received. In addition the <code>NamespacePrefixTraker</code> that is
 * returned via the {@link XMLPipelineContext#getNamespacePrefixTracker}
 * method is update whenever a
 * {@link com.volantis.xml.pipeline.sax.XMLProcess#startPrefixMapping} event
 * and {@link com.volantis.xml.pipeline.sax.XMLProcess#endPrefixMapping}
 * event is received.
 */
public class ContextManagerProcess extends XMLProcessImpl {

    /**
     * String that identifies the base attribute as specified in the
     * XML Base specification
     */
    public static final String BASE_ATTRIBUTE = "base";

    /**
     * Locator for this Context manager. This may be null.
     */
    Locator locator = null;

    /**
     * If true the {@link #startProcess} allows a null value for the Locator
     */
    private boolean allowNullLocator = false;

    /**
     * This flag is used to check that we have recieved exactly one call
     * to startDocument before a call to endDocument
     */
    private boolean inDocument = false;

    /**
     * Creates a new <code>ContextManagerProcess</code>
     */
    public ContextManagerProcess() {
        this(false);
    }

    public ContextManagerProcess(boolean allowNullLocator) {
        this.allowNullLocator = allowNullLocator;
    }

    // Javadoc inherited
    public void startDocument() throws SAXException {
        if (inDocument) {
            fatalError(new XMLPipelineException(
                    "A nested startDocument event was recieved.", locator));
        }
        inDocument = true;

        try {
            if (locator != null) {
                XMLPipelineContext pipelineContext = getPipelineContext();
                // store the locator away in the XMLPipelineContext. The Locator
                // is useful when reporting errors
                pipelineContext.pushLocator(locator);
                pipelineContext.pushBaseURI(locator.getSystemId());
            } else if (!allowNullLocator) {
                warning(new XMLPipelineException("The document does not " +
                                                 "have an associated Locator.", null));
            }
        } catch (MalformedURLException e) {
            // report the fatal error down the pipeline
            fatalError(new XMLPipelineException(
                    "Locators systemId is invalid " +
                    locator.getSystemId(),
                    locator, e));
        }
    }

    // Javadoc inherited
    public void setDocumentLocator(Locator locator) {
        // An XMLReader is not obliged to call this method. However,
        // XMLReaders are strongly encouraged to do so. If this method is
        // called then we use the locator to calculate the initail Base URI.
        this.locator = locator;
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        Attributes attributes = atts;
        // obtain the pipeline context
        XMLPipelineContext pipelineContext = getPipelineContext();
        try {
            // retrieve the base uri if any
            String baseURI = atts.getValue(NamespaceSupport.XMLNS,
                                           BASE_ATTRIBUTE);
            // push the base URI onto the context - OK to push null
            pipelineContext.pushBaseURI(baseURI);

            if (baseURI != null) {
                AttributesImpl cloneAttrs = new AttributesImpl(atts);
                int index = cloneAttrs.
                        getIndex(NamespaceSupport.XMLNS, BASE_ATTRIBUTE);
                cloneAttrs.removeAttribute(index);
                attributes = cloneAttrs;
            }
        } catch (MalformedURLException e) {
            fatalError(new XMLPipelineException(
                    "base uri attribute is malformed",
                    pipelineContext.getCurrentLocator(),
                    e));
        }
        // forward this event to the next process
        super.startElement(namespaceURI, localName, qName, attributes);
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {

        // forward this event to the next process
        super.endElement(namespaceURI, localName, qName);

        // pop the URI that was pushed when the startElement event was
        // received
        getPipelineContext().popBaseURI();
    }

    // Javadoc inherited
    public void endDocument() throws SAXException {
        if (!inDocument) {
            fatalError(new XMLPipelineException("An endDocument event " +
                                                "was recieved without a matching startDocument event.",
                                                locator));
        }
        inDocument = false;

        // pop the locator and base URI that was pushed when the
        // setDocumentLocator event was received
        if (locator != null) {
            XMLPipelineContext pipelineContext = getPipelineContext();
            pipelineContext.popLocator();
            pipelineContext.popBaseURI();
            locator = null;
        }
    }

    // Javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {

        // retrieve the NamespacePrefixTracker from the pipeline context
        NamespacePrefixTracker namespaceManager =
                getPipelineContext().getNamespacePrefixTracker();

        // forward this event to the tracker
        namespaceManager.startPrefixMapping(prefix, uri);

        // forward this event to the next process
        super.startPrefixMapping(prefix, uri);
    }

    // Javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        // retrieve the NamespacePrefixTracker from the pipeline context
        NamespacePrefixTracker namespaceManager =
                getPipelineContext().getNamespacePrefixTracker();

        // forward this event to the tracker
        namespaceManager.endPrefixMapping(prefix);

        // forward this event to the next process
        super.endPrefixMapping(prefix);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 21-Nov-03	470/1	steve	VBM:2003112005 Locator stacking fix

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/
