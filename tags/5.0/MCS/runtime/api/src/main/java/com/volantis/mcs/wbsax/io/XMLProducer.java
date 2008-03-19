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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    steve           VBM:2003042908   Created.
 * 23-May-03    Steve           VBM:2003042908   Writes to an OutputSource
 * 27-May-03    Steve           VBM:2003042908   Moved tag frame stack code from
 *                              AbstractProducer into here. AbstractProducer is
 *                              then no longer required. Added calls to check
 *                              the PrintWriter state at the end of any method
 *                              that writes output.
 * 28-May-03    Mat             VBM:2003042911 - Added ? to end of xml header
 *                              on startDocument
 * 29-May-03    Geoff           VBM:2003042905 - Fix bugs, remove OutputSource.
 * 31-May-03    Geoff           VBM:2003042906 - Add encoding support. 
 * 01-Jun-03    Geoff           VBM:2003042906 - Implement WBSAXContentHander
 *                              rather than WBSAXDefaultHandler to make sure 
 *                              we deal with all events, and add support for 
 *                              opaque values.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax.io;

import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeValueCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueElementStart;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.WBSAXTerminalHandler;

import java.io.PrintWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.Stack;

/**
 * XMLProducer. Turn WBSAX events into XML.
 * 
 * Unlike WBXML this implementation does not care about outputting string 
 * tables. The String table will either be complete when passed in or it will 
 * be being generated at the same time as the document. In either case, 
 * strings should always be resolved when required.
 * 
 * This is why the XML Producer only requires the one output stream as 
 * opposed to the two required by WBXMLProducer.
 */
public class XMLProducer extends WBSAXTerminalHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /** Tag Stack */
    private Stack tagFrameStack;
            
    /** The string table */
    protected StringTable stringTable;
    
    /** The output buffer we are writing to */
    protected Writer out = null;

    protected Writer enc;
    
    /** Flag denoting whether we are outputting an attribute */
    private boolean inAttribute = false;
    
    /**
     * Create a XMLProducer
     *
     * @param out the writer to write to
     */
    public XMLProducer(Writer out, Writer encoded) {
        tagFrameStack = new Stack();
        this.out = new PrintWriter( out );
        this.enc = new PrintWriter(encoded);
    }

    /**
     * Close an attribute if it is open
     */
    private void closeAttribute() throws IOException {
        if( inAttribute ) {
            out.write( '"' );
            inAttribute = false;            
        }
    }
    
    /**
     * Write an attribute to the output stream.
     *
     * @param start the LHS of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttribute(AttributeStartCode start) 
        throws WBSAXException {
        try {
            closeAttribute();
            out.write( ' ' );
            enc.write( start.getName() );
            out.write( "=\"");
            String prefix = start.getValuePrefix();
            if (prefix != null) {
                enc.write( prefix );
            }
            inAttribute = true;
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Write an attribute to the output stream.
     *
     * @param name the attribute name
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttribute(StringReference name)
        throws WBSAXException {
        try {
            closeAttribute();
            out.write( ' ' );    
            enc.write( name.resolveString().getString() );
            out.write( "=\"");
            inAttribute = true;
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Write an attribute value to the output stream
     *
     * @param valuePart holds the code of the attribute as an integer
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(AttributeValueCode valuePart)
        throws WBSAXException {
        try {
            enc.write( valuePart.getValue() );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Write an entity as an attribute value
     *
     * @param entity the code of the entity
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueEntity(EntityCode entity)
        throws WBSAXException {
        try {
            out.write( "&#" );
            enc.write( String.valueOf(entity.getInteger()) );
            out.write( ';' );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    public void addAttributeValueOpaque(OpaqueValue part) 
            throws WBSAXException {
        try {
            enc.write(part.getString());
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    public void endAttributes()
            throws WBSAXException {
    }

    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, WBSAXString part)
        throws WBSAXException {
            throw new UnsupportedOperationException( 
                    "Document Extensions not supported" );
    }

    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part a reference to the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, 
            StringReference part) throws WBSAXException {
        throw new UnsupportedOperationException( 
                "Document Extensions not supported" );
    }


    /**
     * Write an extended attribute code
     *
     * @param code the extension code
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code) 
        throws WBSAXException {
        throw new UnsupportedOperationException( 
                "Document Extensions not supported" );
    }

    /**
     * Output a string attribute value
     *
     * @param part a reference to the attribute value
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(StringReference part)
        throws WBSAXException {
        try {
            enc.write( part.resolveString().getString() );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output a string attribute value
     *
     * @param part the attribute value
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(WBSAXString part)
        throws WBSAXException {
        try {
            enc.write( part.getString() );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output an entity to the element content.
     *
     * @param entity the code of the entity
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueEntity(EntityCode entity)
        throws WBSAXException {
        try {
            out.write( "&#" );
            enc.write( String.valueOf(entity.getInteger()) );
            out.write( ';' );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
        try {
            enc.write(part.getString());
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    public void endContent()
            throws WBSAXException {
        // do nothing.
    }

    /**
     * Output an extended string to the content of the element
     *
     * @param code the extension code
     * @param part a string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code, WBSAXString part)
        throws WBSAXException {
            throw new UnsupportedOperationException( 
                    "Document Extensions not supported" );
    }

    /**
     * Output an extended string to the content of the element
     *
     * @param code the extension code
     * @param part a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code, StringReference part)
        throws WBSAXException {
            throw new UnsupportedOperationException( 
                    "Document Extensions not supported" );
    }

    /**
     * Output an extension code to the content of the element
     *
     * @param code the extension code
     * @param code a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code)
        throws WBSAXException {
            throw new UnsupportedOperationException( 
                    "Document Extensions not supported" );
    }
    
    /**
     * Output a string to the content of the element
     *
     * @param part a reference to the string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValue(StringReference part)
        throws WBSAXException {
        try {
            enc.write( part.resolveString().getString() );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Output a string to the content of the element
     *
     * @param part a string holding the content
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValue(WBSAXString part)
        throws WBSAXException {
        try {
            enc.write( part.getString() );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * The element is complete
     *
     * @throws WBSAXException if an output error occurs
     */
    public void endElement() throws WBSAXException {
        try {
            TagFrame tagFrame = getCurrentFrame();
            if (tagFrame.hasContent()) {
                out.write("</");
                enc.write(tagFrame.getName());
                out.write('>');
            } else {
                closeAttribute();
                out.write("/>");
            }
            tagFrameStack.pop();
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    public void startAttributes() throws WBSAXException {
    }

    /**
     * Start receiving content for this element
     *
     * @throws WBSAXException if an output error occurs
     */
    public void startContent() throws WBSAXException {
        try {
            closeAttribute();
            out.write( '>' );
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }


    /**
     * An element has been opened
     *
     * @param code the code of the opened element
     * @param attributes whether or not the element has attrbutes
     * @param content whether or not the element has body content
     *
     * @throws WBSAXException if there is an output error
     */
    public void startElement(ElementNameCode code, boolean attributes,
        boolean content) throws WBSAXException {
        String name = code.getName();
        writeStartElement(name, attributes, content);
    }
    
    /**
     * An element has been opened
     *
     * @param reference reference to a string holding the name of the element
     * @param attributes whether or not the element has attrbutes
     * @param content whether or not the element has body content
     *
     * @throws WBSAXException if there is an output error
     */
    public void startElement(StringReference reference, boolean attributes,
        boolean content) throws WBSAXException {
        String name = reference.resolveString().getString();
        writeStartElement(name, attributes, content);
    }

    /**
     * Write out the start element, with the plain string name given.
     * 
     * @param name the name of the element as a plain java string.
     * @param content 
     * @param attributes
     * @throws WBSAXException
     */ 
    private void writeStartElement(String name, boolean attributes, 
            boolean content) throws WBSAXException {
        try {
            out.write("<");
            enc.write(name);
            inAttribute = false;
            tagFrameStack.push(new TagFrame(name, content));
            if (!attributes && !content) {
                endElement();
            }
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }

    public void startElement(OpaqueElementStart element, boolean content)
            throws WBSAXException {
        throw new UnsupportedOperationException( 
                "Opaque elements not supported" );
    }


    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId holds the code of the document type
     * @param charset which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec charset, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        writeXmlDeclaration(charset);
    }

    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId a reference to a string holding the document type
     * @param charset which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    public void startDocument(VersionCode version, StringReference publicId,
            Codec charset, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        writeXmlDeclaration(charset);
    }

    /**
     * Private helper method to write the XML declaration, including an
     * encoding= attribute for the charset provided.
     * 
     * @param charset the charset for this xml document.
     * @throws WBSAXException if there was a problem writing.
     */ 
    private void writeXmlDeclaration(Codec charset) throws WBSAXException {
        try {
            out.write("<?xml version=\"1.0\" encoding=\"" + 
                    charset.getCharset().getCharsetName() + "\"?>");
        } catch (IOException e) {
            throw new WBSAXException(e);
        }
    }
    
    public void endDocument() throws WBSAXException {
        // do nothing.
    }

    /**
     * Returns the tag frame associated with the last tag which was started.
     * 
     * @return the current Tag frame
     */ 
    private TagFrame getCurrentFrame() {
        if (tagFrameStack.isEmpty()) {
            throw new IllegalStateException(
                    "Attempt to access empty tag stack");
        } else {
            return ((TagFrame)(tagFrameStack.peek()));
        }
    }

    /**
     * Internal class to hold the current tag frame
     */
    private static class TagFrame {
        
        /** Name */
        private String name;

        /** Whether or not the element has body content */
        private boolean content;

        /**
         * Constructor
         *
         * @param name element name
         * @param content Whether or not the element has content
         */
        TagFrame(String name, boolean content) {
            this.name=name;
            this.content=content;
        }

        /**
         * Get the element name
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns whether or not the element has body content
         *
         * @return boolean flag
         */
        public boolean hasContent() {
            return content;
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 24-Jul-03	807/3	geoff	VBM:2003071405 now with fixed architecture

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 30-Jun-03	644/1	geoff	VBM:2003061001 port from mimas to metis

 30-Jun-03	638/3	geoff	VBM:2003061001 fix a bug I introduced

 30-Jun-03	638/1	geoff	VBM:2003061001 stop WBSAX XML producers using PrintWriter to avoid accidental flush()

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
