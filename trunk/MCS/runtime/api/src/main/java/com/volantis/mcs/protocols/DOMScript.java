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
 * $Header: /src/voyager/com/volantis/mcs/protocols/DOMScript.java,v 1.2 2003/04/23 09:44:19 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Apr-03    Geoff           VBM:2003040305 - Created; represents a Script
 *                              Asset, containing XML, at runtime.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MCSDOMContentHandler;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;

/**
 * Represents a Script Asset, containing XML, at runtime. 
 * <p>
 * When one of these objects is created, it parses the text of the script 
 * asset, as a XML fragment, into a tree of DOM nodes. If the XML is invalid, 
 * it will throw a {@link ProtocolException}.
 * <p>
 * This then allows the {@link #appendTo} method to add that tree of nodes into
 * the {@link DOMOutputBuffer}, unlike the parent class which simply adds the
 * entire text as a single {@link Text} node "blob".
 */ 
public class DOMScript extends Script {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DOMScript.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(DOMScript.class);

    /**
     * Name of the synthetic root element that we use to parse XML fragments.
     */ 
    private static final String ROOT_ELEMENT = "root";
    
    /**
     * The value of the script, as a DOM node.
     */ 
    private Node nodeValue;
    
    /**
     * Creates a DOMScript, from the scriptObject. If the scriptObject is a 
     * script component id, it will look up the text content from the related 
     * script asset. If not, it will just use {@link Object#toString} to turn
     * the object into text content. Once the text value has been obtained,
     * this will parsed as XML (into a DOM). If the parse fails, a  
     * ProtocolException containing the SAXException will be thrown.
     * 
     * @param scriptObject The object from which the text is to be retrieved.
     * @return the newly created DOMScript.
     * 
     * @throws ProtocolException if an error which needs to stop page creation
     *      happens during script creation.
     */ 
    public static Script createScript(ScriptAssetReference scriptObject)
            throws ProtocolException {
        return createWith(scriptObject, new ScriptCreator() {
            public Script create(String value)
                    throws ProtocolException {
                return new DOMScript(value);
            }
        });
    }
    
    /**
     * Creates a DOMScript, parsing the XML into a DOM. If the parse fails,
     * we throw a ProtocolException containing the SAXException.
     * 
     * @param value the string value of the asset.
     *
     * @throws ProtocolException if the XML parse failed.
     */
    private DOMScript(String value)
            throws ProtocolException {
        
        super(value);
        
        // Create the SAX and DOM Parsers, and link them.
        MCSDOMContentHandler domParser = new MCSDOMContentHandler();
        // Note: explicit package for SAXParser to be clear about it's source.
        XMLReader saxParser = 
                new com.volantis.xml.xerces.parsers.SAXParser();
        // No entity resolver, so SAX parser will do default entity resolution.
        // Paul says this is OK. 
        // No dtd handler, so any dtd events will be ignored, which is fine 
        // since we do not expect to get any for XML "fragments".
        // Use the DOM parsers nodeValue handler.
        saxParser.setContentHandler(domParser);
        // Use a specific error handler since we have specific needs here. 
        saxParser.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException e) {
                // Log and continue.
                logger.warn("parsing-problem", new Object[]{DOMScript.this}, 
                        adjust(e));
            }

            public void error(SAXParseException e) throws SAXException {
                // Adjust and throw the exception, it will be logged above.
                throw adjust(e);
            }

            public void fatalError(SAXParseException e) throws SAXException {
                // Adjust and throw the exception, it will be logged above.
                throw adjust(e);
            }

            /**
             * Adjusts a SAXParseException so that it's column number does not 
             * include the synthetic root element that we add before parsing.
             * 
             * @param e original exception
             * @return the adjusted exception.
             */ 
            private SAXParseException adjust(SAXParseException e) {
                int colNo = e.getColumnNumber() - ROOT_ELEMENT.length() - 2;
                return new ExtendedSAXParseException(e.getLocalizedMessage(), null, null,
                        e.getLineNumber(), colNo);
            }
        });

        // Find the XML we need to parse. Note that we wrap the task in an 
        // element to convert even simple text into valid XML.
        String validXml = "<" + ROOT_ELEMENT + ">" + value + 
                "</" + ROOT_ELEMENT + ">";
        try {
            // Parse the XML.
            StringReader stringReader = new StringReader(validXml);
            InputSource source = new InputSource(stringReader);
            saxParser.parse(source);
        } catch (SAXException e) {
            throw new ProtocolException(
                        exceptionLocalizer.format("parsing-error", this), e);
        } catch (IOException e) {
            // NOTE: we are currently reading from a string, so this ought 
            // never to happen anyway.
            throw new ProtocolException(
                        exceptionLocalizer.format("parsing-error", this), e);
        }
            
        // Add the parsed nodeValue into the dom, minus the root element.
        Document document = domParser.getDocument();
        Element root = document.getRootElement();

        // Save our instance variables.
        nodeValue = root.getHead();
    }
    
    // Inherit Javadoc.
    public void appendTo(DOMOutputBuffer dom) {
        dom.addNode(nodeValue);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
