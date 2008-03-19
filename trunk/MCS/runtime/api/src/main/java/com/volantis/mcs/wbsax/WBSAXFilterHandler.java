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
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * A "filter" content handler ala {@link java.io.FilterWriter}. 
 * <p>
 * This class is designed to allow subclasses to implement content handlers
 * which filter WBSAX event streams without having to implement the entire
 * WBSAX content handler interface. 
 * <p>
 * That is, they can simply override the events they are interested in and 
 * this class will ensure that all the others are passed though as normal.
 * <p>
 * This class uses a {@link ReferenceResolver} instance to control the way 
 * that string references are resolved during filtering. If (and only if) the 
 * subclass does not disturb the string table as part of its filtering 
 * operation, then it may use {@link NullReferenceResolver}, which will pass 
 * through string references untouched. If the subclass does change the string 
 * table then it may use  {@link CopyReferenceResolver} which will resolve the 
 * string from the input string table using the reference and add it into the 
 * output string table as a new reference.
 * <p>
 * Note that there are outstanding to dos for this class that mean that
 * currently subclasses must override the various {@link #startDocument} 
 * methods if they need to modify the string table.
 */ 
public abstract class WBSAXFilterHandler implements WBSAXContentHandler {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The handler we are filtering.
     */ 
    protected WBSAXContentHandler handler;
    
    /**
     * The reference resolver we will use. This must be set by the subclass
     * before we get underway. 
     * <p>
     * We do not require this at construction time because the string table 
     * which is necessary to create the reference resolver is often not 
     * available at this point.
     */ 
    protected ReferenceResolver resolver;

    /**
     * Construct an instance of this class, providing a 
     * {@link ReferenceResolver} at a later point.
     * 
     * @param handler
     */ 
    protected WBSAXFilterHandler(WBSAXContentHandler handler) {
        this.handler = handler;
    }

    /**
     * Construct an instance of this class.
     * 
     * @param handler
     */ 
    protected WBSAXFilterHandler(WBSAXContentHandler handler, 
            ReferenceResolver resolver) {
        this.handler = handler;
        this.resolver = resolver;
    }

    // All these methods inherit Javadoc.
    
    public void initialise(ElementNameFactory elementNames,
            AttributeStartFactory attributeStarts,
            AttributeValueFactory attributeValues) {
        handler.initialise(elementNames, attributeStarts, attributeValues);
    }

    public void startDocument(VersionCode version, PublicIdCode publicId, 
            Codec codec, StringTable stringTable, StringFactory strings) 
            throws WBSAXException {
        StringTable outputStringTable = resolver.resolve(stringTable);
        handler.startDocument(version, publicId, codec, outputStringTable, 
                strings);
    }

    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings) 
            throws WBSAXException {
        // Resolve the string table before we resolve the public id.
        StringTable outputStringTable = resolver.resolve(stringTable);
        handler.startDocument(version, resolver.resolve(publicId), codec, 
                outputStringTable, strings);
    }

    public void endDocument() throws WBSAXException {
        resolver.markComplete();
        handler.endDocument();
    }

    public void startElement(ElementNameCode name, boolean attributes, 
            boolean content) throws WBSAXException {
        handler.startElement(name, attributes, content);
    }

    public void startElement(StringReference name, boolean attributes, 
            boolean content) throws WBSAXException {
        handler.startElement(resolver.resolve(name), attributes, content);
    }

    public void startElement(OpaqueElementStart magic, boolean content)
            throws WBSAXException {
        handler.startElement(magic, content);
    }

    public void endElement() throws WBSAXException {
        handler.endElement();
    }

    public void startAttributes() throws WBSAXException {
        handler.startAttributes();
    }

    public void addAttribute(AttributeStartCode start) 
            throws WBSAXException {
        handler.addAttribute(start);
    }

    public void addAttribute(StringReference name) 
            throws WBSAXException {
        handler.addAttribute(resolver.resolve(name));
    }

    public void addAttributeValue(AttributeValueCode part) 
            throws WBSAXException {
        handler.addAttributeValue(part);
    }

    public void addAttributeValue(StringReference part) 
            throws WBSAXException {
        handler.addAttributeValue(resolver.resolve(part));
    }

    public void addAttributeValue(WBSAXString part) 
            throws WBSAXException {
        handler.addAttributeValue(part);
    }

    public void addAttributeValueExtension(Extension code)
            throws WBSAXException {
        handler.addAttributeValueExtension(code);
    }

    public void addAttributeValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        handler.addAttributeValueExtension(code, resolver.resolve(value));
    }

    public void addAttributeValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        handler.addAttributeValueExtension(code, value);
    }

    public void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException {
        handler.addAttributeValueEntity(entity);
    }

    public void addAttributeValueOpaque(OpaqueValue part) 
            throws WBSAXException {
        handler.addAttributeValueOpaque(part);
    }

    public void endAttributes()
            throws WBSAXException {
        handler.endAttributes();
    }

    public void startContent() throws WBSAXException {
        handler.startContent();
    }

    public void addContentValue(StringReference part)
            throws WBSAXException {
        handler.addContentValue(resolver.resolve(part));
    }

    public void addContentValue(WBSAXString part)
            throws WBSAXException {
        handler.addContentValue(part);
    }

    public void addContentValueExtension(Extension code)
            throws WBSAXException {
        handler.addContentValueExtension(code);
    }

    public void addContentValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        handler.addContentValueExtension(code, resolver.resolve(value));
    }

    public void addContentValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        handler.addContentValueExtension(code, value);
    }

    public void addContentValueEntity(EntityCode entity)
            throws WBSAXException {
        handler.addContentValueEntity(entity);
    }

    public void addContentValueOpaque(OpaqueValue part) 
            throws WBSAXException {
        handler.addContentValueOpaque(part);
    }

    public void endContent()
            throws WBSAXException {
        handler.endContent();
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

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
