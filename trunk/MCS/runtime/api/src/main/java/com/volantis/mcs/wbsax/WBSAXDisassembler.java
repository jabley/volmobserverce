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
 * 30-May-03    Mat             VBM:2003042906 - Implement dissection 
 *                              WBSAX serialiser with debug.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.io.IOException;
import java.io.Writer;
import java.io.PrintWriter;

/**
 * A filter for WBSAX events which generates a textual description of the 
 * events (including sizing information) being filtered.
 * <p>
 * This class is "transparent", i.e. it has passes the events to it's contained 
 * {@link WBSAXContentHandler} exactly as they arrived, so it may be inserted 
 * into existing WBSAX pipelines to "collect" events for debugging purposes.
 * <p>
 * This was originally written in about an hour on a deadline day and is a bit
 * ugly, but is reasonably functional. Could use a bit more work to make the
 * output a bit more readable.
 * <p>
 * This has now been extended to add sizing information, for all but Opaque
 * element starts and values. This is useful for checking rendering sizes. 
 */ 
public class WBSAXDisassembler extends WBSAXFilterHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static final byte UNPRINTABLE_REPLACEMENT_BYTE = '*';

    /** Where we are outputting our dissasembly information to */
    private Writer out;

    /** Cached string table if it was incomplete at start document time */
    private StringTable stringTable;

    /** Calulated size of the document, so far */
    private int size;
    
    /** Calculated size of the current event, so far */
    private int increment;

    /**
     * Create a WBSAX disassember which generates output to System.out
     *  
     * @param handler
     */ 
    public WBSAXDisassembler(WBSAXContentHandler handler) {
        this(handler, new PrintWriter(System.out));
    }
    
    /**
     * Create a WBSAX disassember which generates output to the destination
     * of your choice.
     *  
     * @param handler
     */ 
    public WBSAXDisassembler(WBSAXContentHandler handler, Writer out) {
        super(handler, new NullReferenceResolver());
        this.out = out;
    }
    
    //
    // Start and End Document
    //
    
    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        print("startDoc(code)", "version=" + toByte(version) + 
                ",publicId=" + publicId.getInteger() + 
                    "(" + publicId.getDtd() + ")" );
        this.stringTable = stringTable;
        printStringTableStart(stringTable);
        increment(1); // version id, single byte
        increment(publicId.getBytes().length);
        increment(codec.getCharset().getBytes().length);
        // string table length cannot be calculated yet.
        printSize();
        super.startDocument(version, publicId, codec, stringTable, 
                strings);
    }

    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        print("startDoc(literal)", "version=" + toByte(version) + 
                ", publicId=" + publicId.resolveLogicalIndex() +
                    "(" + publicId.resolveString() + ")");
        this.stringTable = stringTable;
        printStringTableStart(stringTable);
        increment(1); // version id, single byte
        increment(publicId.resolvePhysicalIndex().getBytes().length);
        increment(codec.getCharset().getBytes().length);
        // string table length cannot be calculated yet.
        printSize();
        super.startDocument(version, publicId, codec, stringTable, 
                strings);
    }

    /**
     * Print the sting table during start document, if possible.
     * 
     * @param stringTable
     * @throws WBSAXException
     */ 
    private void printStringTableStart(StringTable stringTable) 
            throws WBSAXException {
        // If the client provided a string table
        if (stringTable != null) {
            if (stringTable.isComplete()) {
                printStringTableImpl(stringTable);
            } else {
                print(null, "(stringTable incomplete, will check again at end)");
                // Save it away for checking at the end
                this.stringTable = stringTable;
            }
        } else {
            print(null, "(string table is null)");
        }
    }

    /**
     * Print the string table during end document, if possible.
     * <p>
     * This uses the string table cached during {@link #printStringTableStart}.
     * 
     * @throws WBSAXException
     */ 
    private void printStringTableEnd() 
            throws WBSAXException {
        // If table was present and incomplete at start time
        if (stringTable != null) {
            if (stringTable.isComplete()) {
                printStringTableImpl(stringTable);
            } else {
                print(null, "ERROR: stringTable is incomplete at end!");
            }
        } else {
            print(null, "(string table was null or complete at start)");
        }
    }

    /**
     * Helper method to print a string table which is non-null and complete.
     * 
     * @param stringTable
     * @throws WBSAXException
     */ 
    private void printStringTableImpl(StringTable stringTable) throws WBSAXException {
        if (stringTable.size() == 0) {
            print(null, "stringtable present but empty");
            increment(1); // size of length of 0 takes 1 byte 
        } else {
            byte [] content = stringTable.getContent();
            int length = content.length;
            // make it printable for ease of use
            // otherwise IDEA can print these chars but you can't then cut 
            // and paste in into Linux for example...
            byte [] printable = new byte[content.length];
            System.arraycopy(content, 0, printable, 0, content.length);
            for (int i = 0; i < printable.length; i++) {
                char c = (char) printable[i];
                if (c < 32 || c > 127) {
                    printable[i] = UNPRINTABLE_REPLACEMENT_BYTE;
                }
            }
            print(null, "stringtable size=" + length + " content='" + 
                    new String(printable) + "'");
            // size of the length as a mbi_int32
            increment(stringTable.length().getBytes().length);
            // size of actual content
            increment(length);
        }
    }

    public void endDocument() throws WBSAXException {
        print("endDoc", null);
        printStringTableEnd();
        printSize();
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.endDocument();
    }

    //
    // Start and End Element
    //
    
    public void startElement(ElementNameCode name, boolean attributes,
            boolean content) throws WBSAXException {
        print("startElement(code)", 
                "name=" + name.getInteger() + 
                    "(" + name.getName() + ")," +
                "attributes=" + attributes + ",content=" + content);
        increment(1); // code
        if (attributes) { 
            increment(1); // end attributes
        }
        if (content) {
            increment(1); // end content
        }
        printSize();
        super.startElement(name, attributes, content);
    }

    public void startElement(StringReference name, boolean attributes,
            boolean content) throws WBSAXException {
        print("startElement(ref)", 
                "ref=" + name.resolveLogicalIndex() + 
                    "(" + name.resolveString() + ")," +
                "attributes=" + attributes + ",content=" + content);
        increment(1); // code
        increment(name.resolvePhysicalIndex().getBytes().length); // reference
        if (attributes) { 
            increment(1); // end attributes
        }
        if (content) {
            increment(1); // end content
        }
        printSize();
        super.startElement(name, attributes, content);
    }

    public void startElement(OpaqueElementStart magic, boolean content)
            throws WBSAXException {
        // Disable sizing of opaque elements since our opaque elements
        // cannot currently be rendered (passthrough only).
        //int size = magic.getBytes().length; // content including attrs
        //if (content) size ++;
        print("startElement(opaque)", 
                "name=" + magic.getName() + "," +
                "content=" + content);
        printSizeUnknown();
        super.startElement(magic, content);
    }

    public void endElement() throws WBSAXException {
        print("endElement", null);
        super.endElement();
    }

    //
    // Attributes
    //
    
    public void startAttributes() throws WBSAXException {
        print("startAttrs", null);
        super.startAttributes();
    }

    public void addAttribute(AttributeStartCode start)
            throws WBSAXException {
        print("addAttr(code)", "start=" + start.getInteger() + ":" + 
                start.getName() + ":" + start.getValuePrefix());
        increment(1);
        printSize();
        super.addAttribute(start);
    }

    public void addAttribute(StringReference name)
            throws WBSAXException {
        print("addAttr(ref)", param(name));
        printSize();
        super.addAttribute(name);
    }

    public void addAttributeValue(AttributeValueCode part)
            throws WBSAXException {
        print("addAttrValue", "code=" + toByte(part));
        increment(1);
        printSize();
        super.addAttributeValue(part);
    }

    public void addAttributeValue(StringReference part)
            throws WBSAXException {
        print("addAttrValue", param(part));
        printSize();
        super.addAttributeValue(part);
    }

    public void addAttributeValue(WBSAXString part)
            throws WBSAXException {
        print("addAttrValue", param(part));
        printSize();
        super.addAttributeValue(part);
    }

    public void addAttributeValueExtension(Extension code)
            throws WBSAXException {
        print("addAttrValue", param(code));
        printSize();
        super.addAttributeValueExtension(code);
    }

    public void addAttributeValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        print("addAttrValue", param(code, value));
        printSize();
        super.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        print("addAttrValue", param(code, value));
        printSize();
        super.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException {
        print("addAttrValue", param(entity));
        printSize();
        super.addAttributeValueEntity(entity);
    }

    public void addAttributeValueOpaque(OpaqueValue part)
            throws WBSAXException {
        print("addAttrValue", param(part));
        // Disable sizing of opaque values since our opaque values
        // cannot be rendered "normally".
        // printSize();
        printSizeUnknown();
        super.addAttributeValueOpaque(part);
    }

    public void endAttributes()
            throws WBSAXException {
        print("endAttrs", null);
        super.endAttributes();
    }

    //
    // Content
    //
    
    public void startContent() throws WBSAXException {
        print("startContent", null);
        super.startContent();
    }

    public void addContentValue(StringReference part)
            throws WBSAXException {
        print("addContentValue", param(part));
        printSize();
        super.addContentValue(part);
    }

    public void addContentValue(WBSAXString part)
            throws WBSAXException {
        print("addContentValue", param(part));
        printSize();
        super.addContentValue(part);
    }

    public void addContentValueExtension(Extension code)
            throws WBSAXException {
        print("addContentValue", param(code));
        printSize();
        super.addContentValueExtension(code);
    }

    public void addContentValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        print("addContentValue", param(code, value));
        printSize();
        super.addContentValueExtension(code, value);
    }

    public void addContentValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        print("addContentValue", param(code, value));
        printSize();
        super.addContentValueExtension(code, value);
    }

    public void addContentValueEntity(EntityCode entity)
            throws WBSAXException {
        print("addContentValue", param(entity));
        printSize();
        super.addContentValueEntity(entity);
    }

    public void addContentValueOpaque(OpaqueValue part)
            throws WBSAXException {
        print("addContentValue", param(part));
        // Disable sizing of opaque values since our opaque values
        // cannot be rendered "normally".
        //printSize();
        printSizeUnknown();
        super.addContentValueOpaque(part);
    }

    public void endContent()
            throws WBSAXException {
        print("endContent", null);
        super.endContent();
    }

    //
    // Document and Event Sizing
    //
    
    void increment(int increment) {
        this.increment += increment;
    }

    private void printSize() {
        int increment = this.increment;
        this.increment = 0;
        this.size += increment;
        print("              size:", "this:" + increment + " total:" + size);
    }
    
    private void printSizeUnknown() {
        if (this.increment > 0) {
            throw new IllegalStateException();
        }
        print("              size:", "this: <unknown> total:" + size);
    }

    //
    // Shared implementation for attribute and content values.
    //
    
    private String param(StringReference part) throws WBSAXException {
        // code + reference
        increment(1 + part.resolvePhysicalIndex().getBytes().length);
        return "ref='" + part.resolveString() + "'";
    }

    private String param(WBSAXString part) throws WBSAXException {
        increment(1 + part.getBytes().length); // code + string
        return "string='" + part + "'";
    }
    
    private String param(Extension code) {
        increment(1); // code
        return "ext=" + code.intValue();
    }

    private String param(Extension code, StringReference value) 
            throws WBSAXException {
        return param(code) + ":" + param(value);
    }
    
    private String param(Extension code, WBSAXString value) 
            throws WBSAXException {
        return param(code) + ":" + param(value);
    }

    private String param(EntityCode code) {
        increment(1); // code
        return "entity=" + code.getInteger();
    }

    private String param(OpaqueValue part) throws WBSAXException {
        // Disable sizing of opaque values since our opaque values
        // cannot be rendered "normally".
        //increment(part.getBytes().length); // just the content 
        return "opaque='" + part + "'";
    }

    //
    // Utility.
    //
    
//    private void print(String method, String param1, String param2, 
//            String param3) {
//        print(method, param1 + ":" + param2 + ":" + param3);
//    }
//    
//    private void print(String method, String param1, String param2) {
//        print(method, param1 + ":" + param2);
//    }
//    
    private void print(String method, String params) {
        if (method == null) {
            method = "";
        }
        if (params == null) {
            params = "";
        }
        StringBuffer buf = new StringBuffer(method);
        buf.setLength(20);
        for (int i = method.length(); i < 20; i++) {
            buf.setCharAt(i, ' ');
        }
        buf.append(params);
        try {
            out.write(buf.toString());
            out.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String toByte(SingleByteInteger version) {
        return new Integer(version.getInteger()).toString();
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

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 04-Aug-03	942/1	geoff	VBM:2003080404 port from metis

 04-Aug-03	937/1	geoff	VBM:2003080404 avoid trying to render opaque values in WBSAX disassembler

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/3	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	751/4	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 12-Jun-03	388/1	mat	VBM:2003061101 Improve WMLC debugging and tidy up WMLRoot

 ===========================================================================
*/
