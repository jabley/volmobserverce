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
 * 16-May-03    steve           VBM:2003042908  Created.
 * 23-May-03    Steve           VBM:2003042908  Writes to an OutputSource
 *                              Changed Extension methods to use an if to test
 *                              constant values.
 * 27-May-03    Steve           VBM:2003042908  Moved the default character 
 *                              set code to here. startElement should write a 
 *                              LITERAL code. No need to extend AbstractProducer
 *                              extend WBSAXDefaultHandler instead.
 * 30-May-03    Mat             VBM:2003042911 - Flush output stream in endDocument()
 * 29-May-03    Geoff           VBM:2003042905 - Fix bugs, remove OutputSource.
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
import com.volantis.mcs.wbsax.GlobalToken;
import com.volantis.mcs.wbsax.OpaqueElementStart;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXTerminalHandler;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * WBXMLProducer. Turn WBSAX events into WBXML
 */
public class WBXMLProducer extends WBSAXTerminalHandler {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            WBXMLProducer.class);

    /** The string table */
    protected StringTable stringTable;

    /**
     * Output stream for the document body. If the string table is complete
     * this will simply reference the output stream, otherwise a
     * ByteArrayOutputStream will be created and body will point to that.
     */
    private OutputStream body;

    /** The output buffer we are writing to */
    private OutputStream out;

    /** Flag denoting if string table has yet to be written */
    private boolean stringTablePending;

    /**
     * Create a WBXMLProducer
     *
     * @param out the output stream to write to
     */
    public WBXMLProducer(OutputStream out) {
        this.out = out;
    }

    /**
     * Set the string table for this document
     *
     * @param stringTable the string table
     */
    protected void setStringTable(StringTable stringTable) {
        this.stringTable=stringTable;
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
            body.write(start.getInteger());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
    }

    public void addAttribute(StringReference name)
            throws WBSAXException {
        throw new UnsupportedOperationException(
                "Attributes with string reference values not supported");
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
            body.write(GlobalToken.STR_T);
            body.write(part.resolvePhysicalIndex().getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
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
            body.write(GlobalToken.STR_I);
            body.write(part.getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
    }

    /**
     * Output an encoded attribute value
     *
     * @param valuePart the attribute value
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValue(AttributeValueCode valuePart)
        throws WBSAXException {
        try {
            body.write(valuePart.getInteger());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
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
            body.write(GlobalToken.ENTITY);
            body.write(entity.getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
    }

    public void addAttributeValueOpaque(OpaqueValue part) 
            throws WBSAXException {
        try {
            body.write(part.getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
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
        addContentValueExtension( code, part );
    }

    /**
     * Write an extended attribute value
     *
     * @param code the extension code
     * @param part a reference to the value of the attribute
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addAttributeValueExtension(Extension code, StringReference part)
        throws WBSAXException {
        addContentValueExtension( code, part );
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
        addContentValueExtension( code );
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
            body.write(GlobalToken.STR_T);
            body.write(part.resolvePhysicalIndex().getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
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
            body.write(GlobalToken.STR_I);
            body.write(part.getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
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
            body.write(GlobalToken.ENTITY);
            body.write(entity.getBytes());
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format(
                                    "wbsax-ioexception-unexpected"),
                        e);
        }
    }

    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
        try {
            body.write(part.getBytes());
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
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
        try {
            int token;

            int iValue = code.intValue();

            if( iValue ==  Extension.ZERO.intValue() ) {
                token=GlobalToken.EXT_I_0;
            } else if( iValue ==  Extension.ONE.intValue() ) {
                token=GlobalToken.EXT_I_1;
            } else if( iValue ==  Extension.TWO.intValue() ) {
                token=GlobalToken.EXT_I_2;
            } else {
                throw new IllegalArgumentException("Illegal extension code");
            }

            body.write(token);
            body.write(part.getBytes());
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format(
                                    "wbsax-ioexception-unexpected"),
                        e);
        }
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
        try {
            int token;

            int iValue = code.intValue();

            if( iValue ==  Extension.ZERO.intValue() ) {
                token=GlobalToken.EXT_T_0;
            } else if( iValue ==  Extension.ONE.intValue() ) {
                token=GlobalToken.EXT_T_1;
            } else if( iValue ==  Extension.TWO.intValue() ) {
                token=GlobalToken.EXT_T_2;
            } else {
                throw new IllegalArgumentException("Illegal extension code");
            }

            body.write(token);
            body.write(part.resolvePhysicalIndex().getBytes());
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format(
                                    "wbsax-ioexception-unexpected"),
                        e);
        }
    }

    /**
     * Output an extension code to the content of the element
     *
     * @param code the extension code
     *
     * @throws WBSAXException if an output error occurs
     */
    public void addContentValueExtension(Extension code)
        throws WBSAXException {
        try {
            int token;

            int iValue = code.intValue();

            if( iValue ==  Extension.ZERO.intValue() ) {
                token=GlobalToken.EXT_0;
            } else if( iValue ==  Extension.ONE.intValue() ) {
                token=GlobalToken.EXT_1;
            } else if( iValue ==  Extension.TWO.intValue() ) {
                token=GlobalToken.EXT_2;
            } else {
                throw new IllegalArgumentException("Illegal extension code");
            }

            body.write(token);
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format(
                                    "wbsax-ioexception-unexpected"),
                        e);
        }
    }

    /**
     * Attributes for this element are complete
     *
     * @throws WBSAXException if an output error occurs
     */
    public void endAttributes()
        throws WBSAXException {
        try {
            body.write(GlobalToken.END);
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
    }

    public void startContent() throws WBSAXException {
        // do nothing.
    }

    /**
     * Content for this element is complete
     *
     * @throws WBSAXException if an output error occurs
     */
    public void endContent()
        throws WBSAXException {
        try {
            body.write(GlobalToken.END);
        } catch (IOException ioe) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-write-error"),
                        ioe);
        }
    }

    /**
     * The document is complete. If the string table has been built up while
     * the document was being written, then the document content will have
     * been written to a ByteArrayOutputStream pointed to by
     * <code>body</code>. This method will write the now completed  string
     * table to the main output stream followed by the body  content. If the
     * string table was complete at the start of the document, then it will
     * have already been written along with the body content so there is
     * nothing for this method to do.
     *
     * @throws WBSAXException if an output error occurs
     */
    public void endDocument()
        throws WBSAXException {
        try {
            if (stringTablePending) {
                writeStringTable();
                out.write(((ByteArrayOutputStream)body).toByteArray());
            }
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-document-close-error"),
                        e);
        } finally {
            try {
                out.flush();
            } catch (IOException e1) {
                throw new WBSAXException(
                            exceptionLocalizer.format(
                                        "wbsax-output-flush-error"),
                            e1);
            }
        }
    }

    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId holds the code of the document type
     * @param codec which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    public void startDocument(VersionCode version, PublicIdCode publicId,
        Codec codec, StringTable stringTable, StringFactory strings)
        throws WBSAXException {
        writeHeader(version, publicId.getBytes(), codec, stringTable);
    }

    /**
     * Start a new document.
     *
     * @param version the WBXML version code
     * @param publicId a reference to a string holding the document type
     * @param codec which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    public void startDocument(VersionCode version, StringReference publicId,
        Codec codec, StringTable stringTable, StringFactory strings)
        throws WBSAXException {
        writeHeader(version, publicId.resolvePhysicalIndex().getBytes(),
            codec, stringTable);
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
        try {
            body.write(makeCode(code.getInteger(), attributes, content));
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-element-start-error"),
                        e);
        }
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
        try {
            // LITERAL may be turned into LITERAL_A, LITERAL_C or LITERAL_AC
            body.write(makeCode(GlobalToken.LITERAL, attributes, content));
            body.write(reference.resolvePhysicalIndex().getBytes());
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-element-start-error"),
                        e);
        }
    }

    public void startElement(OpaqueElementStart element, boolean content)
            throws WBSAXException {
        throw new UnsupportedOperationException("Opaque elements not supported");
    }

    public void endElement() throws WBSAXException {
        // do nothing?
    }

    public void startAttributes() throws WBSAXException {
        // do nothing.
    }

    /**
     * Create the tag code
     *
     * @param code The initial tag code
     * @param attributes Whether the tag has attributes
     * @param content Whether the tag has body content
     *
     * @return The code with the attribute and content bits set.
     */
    private int makeCode(int code, boolean attributes, boolean content) {
        int cd=code;

        if (attributes) {
            cd=cd | 0x80;
        }

        if (content) {
            cd=cd | 0x40;
        }

        return cd;
    }

    /**
     * Write the header for a new document.
     *
     * @param version the WBXML version code
     * @param publicID a byte array holding the document type
     * @param codec which character set is being used
     * @param stringTable the document string table
     *
     * @throws WBSAXException if there is an error writing the header
     */
    private void writeHeader(VersionCode version, byte[] publicID, Codec codec,
        StringTable stringTable) throws WBSAXException {
        try {
            out.write(version.getInteger());
            out.write(publicID);
            out.write(codec.getCharset().getBytes());
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format("wbsax-document-start-error"),
                        e);
        }

        setStringTable(stringTable);

        if ((stringTable==null) || (stringTable.isComplete())) {
            writeStringTable();
            body = out;
            stringTablePending=false;
        } else {
            body = new ByteArrayOutputStream();
            stringTablePending=true;
        }
    }

    /**
     * Output the string table to the output stream
     *
     * @throws WBSAXException if an output error occurs
     */
    private void writeStringTable()
        throws WBSAXException {
        try {
            if (stringTable == null || stringTable.size() <= 0) {
                out.write(0);
            } else {
                out.write(stringTable.length().getBytes());
                out.write(stringTable.getContent());
            }
        } catch (IOException e) {
            throw new WBSAXException(
                        exceptionLocalizer.format(
                                    "wbsax-string-table-write-error"),
                        e);
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

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
