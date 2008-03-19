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
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * This state represents the state encountered when a transformation
 * element has been encountered. This element may have a href attribute
 * that specifies the the transformation or nested inline stylesheet.
 * This state can only progress to the previous state.
 */
public class Transformation extends TransformationState {

    /**
     * Attribute denoting whether we use xslt or xsltc.
     */
    protected Boolean compilable;

    /**
     * Flag that indicates whether or not an inline stylesheet is specified
     */
    private boolean inline = false;

    public Transformation(TransformAdapterProcess transformAdapterProcess) {
        super(transformAdapterProcess);
    }

    // javadoc inherited
    public void initialise(String namespaceURI, String localName,
                           String qName, Attributes atts)
            throws SAXException {

        String sCompilable = atts.getValue(COMPILABLE_ATTRIBUTE);
        if (null != sCompilable) {
            compilable = Boolean.valueOf(sCompilable);
        } else {
            compilable = null;
        }

        String href = atts.getValue(HREF_ATTRIBUTE);
        if (href != null) {
            // stylesheet is not inline
            inline = false;
            operationProcess.addTemplate(href, compilable, true);
        } else {
            // stylesheet is inline and proceeds this element
            inline = true;
            operationProcess.loadTemplatesHandler(compilable, true);
        }
    }

    // javadoc inherited
    public void endElement(String namespaceURI, String localName,
                           String qName)
            throws SAXException {

        if (TRANSFORMATION_ELEMENT.equals(localName) &&
                namespaceURI.equals(adapterProcess.getProcessNamespaceURI())) {
            if (inline) {
                // style sheet is inline and has been processed
                // inform the operation that it has seen all of the
                // stylesheet
                operationProcess.unloadTemplatesHandler(compilable, true);
            }
            // transformation state finished
            adapterProcess.popState(this);
        } else {
            if (inline) {
                operationProcess.endElement(namespaceURI, localName, qName);
            } else {
                // SHOULD NOT GET THIS
            }
        }
    }

    /**
     * Report a fatalError
     * @throws SAXException if the error cannot be handled by any
     * ErrorHandler
     */
    private void reportUnexpectedFatalError() throws SAXException {
        fatalError(new ExtendedSAXParseException(
                "transformation element cannot contain an inline " +
                "stylesheet if one was provided via an href attribute",
                adapterProcess.getPipelineContext().getCurrentLocator()));
    }

    // javadoc inherited
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
            throws SAXException {
        if (inline) {
            operationProcess.startElement(namespaceURI, localName, qName, atts);
        } else {
            reportUnexpectedFatalError();
        }

    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
        if (inline) {
            operationProcess.setDocumentLocator(locator);
        } else {
            try {
                reportUnexpectedFatalError();
            } catch (SAXException e) {
                throw new ExtendedRuntimeException(
                        "Failed to forward a setDocument locator event", e);
            }
        }
    }

    // javadoc inherited
    public void startDocument()
            throws SAXException {
        if (inline) {
            operationProcess.startDocument();
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void endDocument()
            throws SAXException {
        if (inline) {
            operationProcess.endDocument();
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        if (inline) {
            operationProcess.startPrefixMapping(prefix, uri);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void endPrefixMapping(String prefix)
            throws SAXException {
        if (inline) {
            operationProcess.endPrefixMapping(prefix);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (inline) {
            operationProcess.characters(ch, start, length);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void ignorableWhitespace(char ch[], int start, int length)
            throws SAXException {
        if (inline) {
            operationProcess.ignorableWhitespace(ch, start, length);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void processingInstruction(String target, String data)
            throws SAXException {
        if (inline) {
            operationProcess.processingInstruction(target, data);
        } else {
            reportUnexpectedFatalError();
        }
    }

    // javadoc inherited
    public void skippedEntity(String name)
            throws SAXException {
        if (inline) {
            operationProcess.skippedEntity(name);
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

 28-Apr-04	683/3	adrian	VBM:2004042607 Added copyright statements

 27-Apr-04	683/1	adrian	VBM:2004042607 Refactored states out of transform adapter process

 ===========================================================================
*/
