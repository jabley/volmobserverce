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
package com.volantis.mcs.wbsax;

/**
 * This class splits an incoming WBSAX event stream into two identical 
 * outgoing WBSAX event streams.
 * <p>
 * This class is similar in concept to the Unix "tee" command. Unlike 
 * {@link WBSAXFilterHandler}, this is not designed explicitly to be 
 * subclassed. It will ordinarily be used as is, plumbing fashion, to create
 * an additional copy of an input stream, often for debugging purposes.
 * <p>
 * If the first handler generates an error, currently the second handler
 * will not be called. This is, perhaps, not ideal, but I do not have time to
 * implement this now. This behaviour may be changed later if required.
 */ 
public class WBSAXTeeHandler implements WBSAXContentHandler {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private WBSAXContentHandler handler1;
    
    private WBSAXContentHandler handler2;

    /**
     * Create an instance of this class, copying the input WBSAX stream into
     * two output streams using the handlers provided.
     * 
     * @param handler1 the handler for the first output stream.
     * @param handler2 the handler for the second output stream.
     */ 
    public WBSAXTeeHandler(WBSAXContentHandler handler1, 
            WBSAXContentHandler handler2) {
        this.handler1 = handler1;
        this.handler2 = handler2;
    }

    // All these methods inherit javadoc.
    
    public void initialise(ElementNameFactory elementNames,
            AttributeStartFactory attributeStarts,
            AttributeValueFactory attributeValues) {
        handler1.initialise(elementNames, attributeStarts, attributeValues);
        handler2.initialise(elementNames, attributeStarts, attributeValues);
    }

    public void startDocument(VersionCode version, PublicIdCode publicId, 
            Codec codec, StringTable stringTable, StringFactory strings) 
            throws WBSAXException {
        handler1.startDocument(version, publicId, codec, stringTable, strings);
        handler2.startDocument(version, publicId, codec, stringTable, strings);
    }

    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings) 
            throws WBSAXException {
        handler1.startDocument(version, publicId, codec, stringTable, strings);
        handler2.startDocument(version, publicId, codec, stringTable, strings);
    }

    public void endDocument() throws WBSAXException {
        handler1.endDocument();
        handler2.endDocument();
    }

    public void startElement(ElementNameCode name, boolean attributes, 
            boolean content) throws WBSAXException {
        handler1.startElement(name, attributes, content);
        handler2.startElement(name, attributes, content);
    }

    public void startElement(StringReference name, boolean attributes, 
            boolean content) throws WBSAXException {
        handler1.startElement(name, attributes, content);
        handler2.startElement(name, attributes, content);
    }

    public void startElement(OpaqueElementStart magic, boolean content)
            throws WBSAXException {
        handler1.startElement(magic, content);
        handler2.startElement(magic, content);
    }

    public void endElement() throws WBSAXException {
        handler1.endElement();
        handler2.endElement();
    }

    public void startAttributes() throws WBSAXException {
        handler1.startAttributes();
        handler2.startAttributes();
    }

    public void addAttribute(AttributeStartCode start) 
            throws WBSAXException {
        handler1.addAttribute(start);
        handler2.addAttribute(start);
    }

    public void addAttribute(StringReference name) 
            throws WBSAXException {
        handler1.addAttribute(name);
        handler2.addAttribute(name);
    }

    public void addAttributeValue(AttributeValueCode part) 
            throws WBSAXException {
        handler1.addAttributeValue(part);
        handler2.addAttributeValue(part);
    }

    public void addAttributeValue(StringReference part) 
            throws WBSAXException {
        handler1.addAttributeValue(part);
        handler2.addAttributeValue(part);
    }

    public void addAttributeValue(WBSAXString part) 
            throws WBSAXException {
        handler1.addAttributeValue(part);
        handler2.addAttributeValue(part);
    }

    public void addAttributeValueExtension(Extension code)
            throws WBSAXException {
        handler1.addAttributeValueExtension(code);
        handler2.addAttributeValueExtension(code);
    }

    public void addAttributeValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        handler1.addAttributeValueExtension(code, value);
        handler2.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        handler1.addAttributeValueExtension(code, value);
        handler2.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException {
        handler1.addAttributeValueEntity(entity);
        handler2.addAttributeValueEntity(entity);
    }

    public void addAttributeValueOpaque(OpaqueValue part) 
            throws WBSAXException {
        handler1.addAttributeValueOpaque(part);
        handler2.addAttributeValueOpaque(part);
    }

    public void endAttributes()
            throws WBSAXException {
        handler1.endAttributes();
        handler2.endAttributes();
    }

    public void startContent() throws WBSAXException {
        handler1.startContent();
        handler2.startContent();
    }

    public void addContentValue(StringReference part)
            throws WBSAXException {
        handler1.addContentValue(part);
        handler2.addContentValue(part);
    }

    public void addContentValue(WBSAXString part)
            throws WBSAXException {
        handler1.addContentValue(part);
        handler2.addContentValue(part);
    }

    public void addContentValueExtension(Extension code)
            throws WBSAXException {
        handler1.addContentValueExtension(code);
        handler2.addContentValueExtension(code);
    }

    public void addContentValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        handler1.addContentValueExtension(code, value);
        handler2.addContentValueExtension(code, value);
    }

    public void addContentValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        handler1.addContentValueExtension(code, value);
        handler2.addContentValueExtension(code, value);
    }

    public void addContentValueEntity(EntityCode entity)
            throws WBSAXException {
        handler1.addContentValueEntity(entity);
        handler2.addContentValueEntity(entity);
    }

    public void addContentValueOpaque(OpaqueValue part) 
            throws WBSAXException {
        handler1.addContentValueOpaque(part);
        handler2.addContentValueOpaque(part);
    }

    public void endContent()
            throws WBSAXException {
        handler1.endContent();
        handler2.endContent();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/3	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
