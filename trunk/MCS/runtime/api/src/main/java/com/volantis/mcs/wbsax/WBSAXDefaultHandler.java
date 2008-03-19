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
 * 18-May-03    Geoff           VBM:2003042904 - Created; default 
 *                              implementation of WBSAXContentHandler with no 
 *                              behaviour.
 * 20-May-03    Geoff           VBM:2003052102 - rename InlineString to 
 *                              WBSAXString, change startDocument() to take
 *                              Codec rather than CharsetCode.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * A default implementation of {@link WBSAXContentHandler} with no behaviour.
 * <p>
 * Equivalent to SAX's {@link org.xml.sax.helpers.DefaultHandler} class.
 */ 
public class WBSAXDefaultHandler implements WBSAXContentHandler {
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // All these methods inherit Javadoc.
    
    public void initialise(ElementNameFactory elementNames,
            AttributeStartFactory attributeStarts,
            AttributeValueFactory attributeValues) {
    }

    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
    }

    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
    }

    public void endDocument() throws WBSAXException {
    }

    public void startElement(ElementNameCode tag, boolean attributes, boolean content)
            throws WBSAXException {
    }

    public void startElement(StringReference tag, boolean attributes, 
            boolean content) throws WBSAXException {
    }

    public void startElement(OpaqueElementStart element, boolean content)
            throws WBSAXException {
    }

    public void endElement() throws WBSAXException {
    }

    public void startAttributes() throws WBSAXException {
    }

    public void addAttribute(AttributeStartCode start)
            throws WBSAXException {
    }

    public void addAttribute(StringReference name)
            throws WBSAXException {
    }

    public void addAttributeValue(AttributeValueCode valuePart)
            throws WBSAXException {
    }

    public void addAttributeValue(StringReference part)
            throws WBSAXException {
    }

    public void addAttributeValue(WBSAXString part)
            throws WBSAXException {
    }

    public void addAttributeValueExtension(Extension code, WBSAXString part)
            throws WBSAXException {
    }

    public void addAttributeValueExtension(Extension code, 
            StringReference part) throws WBSAXException {
    }

    public void addAttributeValueExtension(Extension code)
            throws WBSAXException {
    }

    public void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException {
    }

    public void addAttributeValueOpaque(OpaqueValue part) throws WBSAXException {
    }

    public void endAttributes()
            throws WBSAXException {
    }

    public void startContent() throws WBSAXException {
    }

    public void addContentValue(StringReference part)
            throws WBSAXException {
    }

    public void addContentValue(WBSAXString part)
            throws WBSAXException {
    }

    public void addContentValueExtension(Extension code, WBSAXString part)
            throws WBSAXException {
    }

    public void addContentValueExtension(Extension code, StringReference part)
            throws WBSAXException {
    }

    public void addContentValueExtension(Extension code)
            throws WBSAXException {
    }

    public void addContentValueEntity(EntityCode entity)
            throws WBSAXException {
    }

    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
    }

    public void endContent()
            throws WBSAXException {
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

 ===========================================================================
*/
