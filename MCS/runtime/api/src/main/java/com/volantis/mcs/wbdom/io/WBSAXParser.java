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
 * 30-May-03    Steve           VBM:2003042917   Created.
 * 30-May-03    Mat             VBM:2003042906 - Filled in 
 *                              addContentValueOpaque() methods
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.WBDOMDocument;
import com.volantis.mcs.wbdom.WBDOMElement;
import com.volantis.mcs.wbdom.WBDOMNode;
import com.volantis.mcs.wbdom.WBDOMText;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.WBDOMFactory;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.AttributeValueCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.ElementNameCode;
import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXDefaultHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * WBSAXParser. Convert WBSAX events into a WBDOM.
 *
 * @author steve
 */
public class WBSAXParser extends WBSAXDefaultHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBSAXParser.class);

    protected WBDOMFactory factory;

    private WBDOMDocument document;

    private WBDOMElement current;
    
    private EmptyElementNameVisitor emptyVisitor; 

    public WBSAXParser(WBDOMFactory factory, 
            SerialisationConfiguration configuration) {
        this.factory = factory;
        this.emptyVisitor = new EmptyElementNameVisitor(configuration);
    }

    /**
     * Return the generated DOM document
     * @return the Document created by this content handler
     */
    public WBDOMDocument getDocument() {
        return document;
    }

    /**
     * Add an attribute to the current element of the DOM
     * @param start the attribute start code
     *
     * @throws WBSAXException 
     */
    public void addAttribute(AttributeStartCode start)
            throws WBSAXException {
        current.addAttribute(factory.createCodeAttribute(start));
    }

    /**
     * Add an attribute value to the current DOM node
     * @param part the code of the attribute value
     *
     * @throws WBSAXException
     */
    public void addAttributeValue(AttributeValueCode part)
            throws WBSAXException {
        current.getLastAttribute().getValueBuffer().append(part);
    }

    /**
     * Add an attribute value to the current DOM node
     * @param part the attribute value as a string reference
     *
     * @throws WBSAXException
     */
    public void addAttributeValue(StringReference part)
            throws WBSAXException {
        try {
            current.getLastAttribute().getValueBuffer().append(part);
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Add an attribute value to the current DOM node
     * @param part the attribute value as a string
     *
     * @throws WBSAXException
     */
    public void addAttributeValue(WBSAXString part)
            throws WBSAXException {
        try {
            current.getLastAttribute().getValueBuffer().append(part);
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Add an entity as an attribute value
     * @param entity the entity code
     *
     * @throws WBSAXException
     */
    public void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException {
        current.getLastAttribute().getValueBuffer().append(entity);
    }

    /**
     * Add an extension code as an attribute value part. This actually
     * has no meaning in WML
     * @param code the extension code
     *
     * @throws WBSAXException
     */
    public void addAttributeValueExtension(Extension code)
            throws WBSAXException {
        current.getLastAttribute().getValueBuffer().append(code);
    }

    /**
     * Add an extension code and string as an attribute value. WML uses
     * this to denote variables.
     * @param code the extension code
     * @param value a reference to the variable name
     *
     * @throws WBSAXException
     */
    public void addAttributeValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        current.getLastAttribute().getValueBuffer().append(code, value);
    }

    /**
     * Add an extension code and string as an attribute value. WML uses
     * this to denote variables.
     * @param code the extension code
     * @param value the variable name
     *
     * @throws WBSAXException
     */
    public void addAttributeValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        try {
            current.getLastAttribute().getValueBuffer().append(code, value);
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Add a string to the content 
     * @param part a reference to the string to add
     *
     * @throws WBSAXException
     */
    public void addContentValue(StringReference part)
            throws WBSAXException {
        try {
            getCurrentText().getBuffer().append(part);
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
        }
    }


    /**
     * Add a string to the content 
     * @param part the string to add
     *
     * @throws WBSAXException
     */
    public void addContentValue(WBSAXString part)
            throws WBSAXException {
        try {
            getCurrentText().getBuffer().append(part);
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Add an entity to the content of the current node
     * @param entity the entity to add
     *
     * @throws WBSAXException
     */
    public void addContentValueEntity(EntityCode entity)
            throws WBSAXException {
        getCurrentText().getBuffer().append(entity);
    }


    /**
     * Add an extension value to the content of the current node.
     * This has no meaning in WMLC
     *
     * @param code the extension code
     *
     * @throws WBSAXException
     */
    public void addContentValueExtension(Extension code)
            throws WBSAXException {
        getCurrentText().getBuffer().append(code);
    }

    /**
     * Add an extension value and string to the content od the current node.
     * This is used for adding variable references to the content
     * @param code the extension code
     * @param value a reference to the variable name
     *
     * @throws WBSAXException
     */
    public void addContentValueExtension(Extension code, StringReference value)
            throws WBSAXException {
        getCurrentText().getBuffer().append(code, value);
    }

    /**
     * Add an extension value and string to the content od the current node.
     * This is used for adding variable references to the content
     * @param code the extension code
     * @param value the name of the variable
     *
     * @throws WBSAXException
     */
    public void addContentValueExtension(Extension code, WBSAXString value)
            throws WBSAXException {
        try {
            getCurrentText().getBuffer().append(code, value);
        } catch (WBDOMException e) {
            throw new WBSAXException(e);
        }
    }

    /**
     * Finished with the current element
     *
     * @throws WBSAXException
     */
    public void endElement() throws WBSAXException {
        // If the element has no children...
        if (!current.hasChildren()) {
            // Then we must decide if it should be sized and written as an
            // empty tag or as a start and end tag. This information is static
            // so it's good to do this now rather than during sizing/output
            try {
                current.accept(emptyVisitor);
                EmptyElementType type = emptyVisitor.getEmptyElementType();
                if (logger.isDebugEnabled()) {
                    logger.debug("Marking empty tag '" + current.getName() + 
                            "' as " + type);
                }
                current.setEmptyType(type);
            } catch (WBDOMException e) {
                throw new WBSAXException(e);
            }
        }
        // NOTE: we could do the above without a visitor at element creation
        // time, which would be a lot simpler. However, I already coded it 
        // this way, and also if we did it that then we would always need to
        // know if an element is empty when we create it, which is true for
        // WBSAX, but not for SAX. So, it wouldn't be as flexible and it could
        // well be painful for testing via SAX or when we create the WBDOM 
        // directly from the DOMOutputBuffers rather than the MCSDOM.

        current = current.getParent();
    }

    /**
     * Start generating the DOM
     *
     * @param version the WMLC version code
     * @param publicId the code of the Public ID and DTD
     * @param codec character set definition
     * @param stringTable the table holding encoded strings
     *
     * @throws WBSAXException
     */
    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        document = factory.createDocument(version, publicId, codec, 
                stringTable, strings);
    }

    /**
     * Start generating the DOM
     *
     * @param version the WMLC version code
     * @param publicId the code of the Public ID and DTD
     * @param codec character set definition
     * @param stringTable the table holding encoded strings
     *
     * @throws WBSAXException
     */
    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {
        throw new UnsupportedOperationException(
                "This version of startDocument is not supported." +
                "use the publicIdCode version instead.");
    }

    /**
     * Create a new element and make it current
     *
     * @param name the name of the elements
     * @param attributes whether or not the element has attributes
     * @param content whether or not the element has content
     *
     * @throws WBSAXException
     */
    public void startElement(ElementNameCode name, boolean attributes,
            boolean content)
            throws WBSAXException {
        // Create a new element
        WBDOMElement element = null;
        element = factory.createCodeElement(name);
        pushElement(element);
        // If we do not have attributes or content
        if (!attributes && !content) {
            // then we better call endElement ourselves.
            endElement();
        }
    }

    /**
     * Create a new element and make it current
     *
     * @param name the name of the elements
     * @param attributes whether or not the element has attributes
     * @param content whether or not the element has content
     *
     * @throws WBSAXException
     */
    public void startElement(StringReference name, boolean attributes,
            boolean content)
            throws WBSAXException {
        // Create a new element
        WBDOMElement element = null;
        element = factory.createLiteralElement(name);
        pushElement(element);
        // If we do not have attributes or content
        if (!attributes && !content) {
            // then we better call endElement ourselves.
            endElement();
        }
    }


    /**
     * Add an element to the DOM. If this is the first element, it becomes
     * the root node of the DOM tree, otherwise it is added as a child of the
     * current node. The new node becomes the current node.
     * @param element the element to add.
     */
    protected void pushElement(WBDOMElement element) {
        // If the document is empty
        if (current == null) {
            // Then this is the root element
            document.setRootElement(element);
        } else {
            // Else, add this element to it's parent.
            current.addChild(element);
        }
        // And use the newly created element as the current element.
        current = element;
    }

    /**
     * Return the current text node to write to within the current element.
     * If the element has no text node, one is created and assigned to the element
     * @return the Text node to write to.
     * @throws WBSAXException
     */
    protected WBDOMText getCurrentText() throws WBSAXException {
        WBDOMNode node = current.getLastChild();
        WBDOMText text;
        if (node instanceof WBDOMText) {
            // The current child is a text, use that
            text = (WBDOMText) node;
        } else {
            // There is no current child, or at least it's not a text,
            // so create a new one, add it to the element, and use that.
            text = factory.createText();
            current.addChild(text);
        }
        return text;
    }

    /**
     * Add an opaque value into the attributes
     * @param part the opaque value to add
     * @throws WBSAXException
     */
    public void addAttributeValueOpaque(OpaqueValue part)
            throws WBSAXException {
        current.getLastAttribute().getValueBuffer().append(part);
    }

    /**
     * Add an opaque value to the content
     * @param part the opaque value
     * @throws WBSAXException
     */
    public void addContentValueOpaque(OpaqueValue part) throws WBSAXException {
        getCurrentText().getBuffer().append(part);
    }

}    

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 ===========================================================================
*/
