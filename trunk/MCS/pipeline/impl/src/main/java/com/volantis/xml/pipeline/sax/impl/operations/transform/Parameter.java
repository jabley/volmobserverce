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

package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.sax.ExtendedSAXParseException;
import com.volantis.xml.xalan.xsltc.trax.SAX2DOM;
import com.volantis.xml.xerces.dom.DocumentImpl;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * This state represents the state when a transformation element has been
 * encountered. This element may have a href attribute that specifies the
 * transformation or nested inline stylesheet. This state can only progress to
 * the previous state.
 */
public class Parameter extends TransformationState {

    /**
     * Constant that identifies the name attribute
     */
    private static final String NAME_ATTRIBUTE = "name";

    /**
     * Constant that identifies the value attribute
     */
    private static final String VALUE_ATTRIBUTE = "value";

    /**
     * This ContentHandler is used to transform SAX events to a w3c DOM
     */
    private SAX2DOM sax2dom;

    /**
     * The root node added to the SAX2DOM document so that we can process
     * document fragments.
     */
    private Node rootNode;

    /**
     * Attribute denoting whether we use xslt or xsltc.
     */
    private String name;

    /**
     * Flag that indicates whether or not an inline value is specified
     */
    private boolean inline = false;

    public Parameter(TransformAdapterProcess transformAdapterProcess) {
        super(transformAdapterProcess);
    }

    // javadoc inherited
    public void initialise(String namespaceURI, String localName,
                           String qName, Attributes atts)
            throws SAXException {

        name = atts.getValue(NAME_ATTRIBUTE);
        String value = atts.getValue(VALUE_ATTRIBUTE);

        if (value != null) {
            // value is not inline
            inline = false;
            operationProcess.addParameter(name, value);
        } else {
            // value is inline as a child of this element
            try {
                DocumentImpl document = new DocumentImpl();
                rootNode = document.createElement("parameter-value-root");
                document.appendChild(rootNode);
                sax2dom = new SAX2DOM(rootNode);

                sax2dom.startDocument();
            } catch (ParserConfigurationException e) {
                fatalError(new ExtendedSAXParseException("Failure parsing parameter.",
                        adapterProcess.getPipelineContext().getCurrentLocator(), e));
            }
            inline = true;
        }
    }

    /**
     * Report a fatalError
     * @throws SAXException if the error cannot be handled by any ErrorHandler
     */
    private void reportUnexpectedFatalError() throws SAXException {
        fatalError(new ExtendedSAXParseException(
                "The <parameter> element cannot contain an inline " +
                "value if one was provided via the value attribute",
                adapterProcess.getPipelineContext().getCurrentLocator()));
    }

    // javadoc inherited
    public void endElement(String namespaceURI, String localName,
                           String qName)
            throws SAXException {

        if (PARAMETER_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {
            if (inline) {
                // value is inline and has all been seen so set it on the
                // operation process.
                sax2dom.endDocument();
                if (rootNode.getFirstChild() != null) {
                	operationProcess.addParameter(name, rootNode.getChildNodes());
                }
            }
            // transformation state finished
            adapterProcess.popState(this);
        } else {
            if (inline) {
                sax2dom.endElement(namespaceURI, localName, qName);
            } else {
                reportUnexpectedFatalError();
            }
        }
    }

    // javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
        if (inline) {
            sax2dom.startElement(namespaceURI, localName, qName, atts);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
        try {
            reportUnexpectedFatalError();
        } catch (SAXException e) {
            throw new ExtendedRuntimeException(e);
        }
    }

    // javadoc inherited
    public void startDocument()
            throws SAXException {
        reportUnexpectedFatalError();
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
        reportUnexpectedFatalError();
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if (inline) {
            sax2dom.startPrefixMapping(prefix, uri);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        if (inline) {
            sax2dom.endPrefixMapping(prefix);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (inline) {
            sax2dom.characters(ch, start, length);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        if (inline) {
            sax2dom.ignorableWhitespace(ch, start, length);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        if (inline) {
            sax2dom.processingInstruction(target, data);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void skippedEntity(String name) throws SAXException {
        if (inline) {
            sax2dom.skippedEntity(name);
        } else {
            reportUnexpectedFatalError();
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

 30-Apr-04	686/1	adrian	VBM:2004042802 Add parameter support for transforms

 ===========================================================================
*/
