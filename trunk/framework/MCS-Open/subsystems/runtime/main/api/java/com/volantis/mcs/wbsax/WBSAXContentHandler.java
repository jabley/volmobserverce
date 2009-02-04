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
 * 18-May-03    Geoff           VBM:2003042904 - Created; defines events for 
 *                              streaming WBXML. 
 * 20-May-03    Geoff           VBM:2003052102 - rename InlineString to 
 *                              WBSAXString, change startDocument() to take
 *                              Codec rather than CharsetCode.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Defines events for streaming WBXML. 
 * <p>
 * Equivalent to the SAX's {@link org.xml.sax.ContentHandler} interface.   
 * <p>
 * This is a good deal more complex that SAX because it reflects the complex,
 * fiddly structure of WBXML.
 * <p>
 * The calling sequence is as you might expect, but there is a slight twist,
 * as the {@link #endElement} method is only called if the 
 * {@link #startElement} method is passed true for at least one of it's 
 * boolean parameters.
 * <p>
 * One thing to be aware of is the behaviour of the string table wrt 
 * completion marking and caching of string table values. Please see 
 * {@link StringTable}.
 * 
 * @todo consider adding input StringReferenceFactory to startDocument methods 
 *      as well since it can be useful for resolving existing string references  
 */ 
public interface WBSAXContentHandler {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Initalise the WBSAX handler with the "static" code factories.
     * <p>
     * This is primarily useful for WBSAX filters which must share these with
     * the class which creates the events which are passed into a filter chain. 
     * 
     * @param elementNames the element name codes to use.
     * @param attributeStarts the attribute start codes to use.
     * @param attributeValues the attribute value codes to use.
     */ 
    void initialise(ElementNameFactory elementNames, 
            AttributeStartFactory attributeStarts, 
            AttributeValueFactory attributeValues);
    
    //
    // Document methods.
    //
    
    /**
     * Start a WBXML document, using the version, coded public id, charset and 
     * string table provided.
     * <p>
     * If the input side is to provide byte data during the run, the codec
     * must contain the charset code that the byte data is encoded with. See
     * {@link Codec} for more info.
     * <p>
     * The string table may or may not be marked as completed at this point,
     * which will affect the way the output side must deal with caching. See 
     * {@link StringTable} for more info. 
     * 
     * @param version the WBXML version used. 
     * @param publicId the public id used, as a code.
     * @param codec the char/byte codec, contains the charset code.
     * @param stringTable the string table to resolve string references 
     *      against, may be null if there is no string table info.
     * @param strings the string factory used to create strings, 
     *      initialised with the codec supplied. Useful for filters to avoid
     *      creating a new one for each filter in a pipeline. 
     * @throws WBSAXException if there was a problem
     */ 
    void startDocument(VersionCode version, PublicIdCode publicId, 
            Codec codec, StringTable stringTable, StringFactory strings) 
            throws WBSAXException;

    /**
     * Start a WBXML document, using the version, literal public id, charset 
     * and string table provided.
     * <p>
     * If the input side is to provide byte data during the run, the codec
     * must contain the charset code that the byte data is encoded with. See
     * {@link Codec} for more info. 
     * <p>
     * The string table may or may not be marked as completed at this point,
     * which will affect the way the output side must deal with caching. See 
     * {@link StringTable} for more info. 
     * 
     * @param version the WBXML version used. 
     * @param publicId the public id used, as a literal.
     * @param codec the char/byte codec, contains the charset code.
     * @param stringTable the string table to resolve string references 
     *      against, may be null if there is no string table info.
     * @param strings the string factory used to create strings, 
     *      initialised with the codec supplied. Useful for filters to avoid
     *      creating a new one for each filter in a pipeline. 
     * @throws WBSAXException if there was a problem.
     */ 
    void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings) 
            throws WBSAXException;
    
    /**
     * End a WBXML document. The string table provided at start document time
     * must be marked completed before this is called.
     * 
     * @throws WBSAXException if there was a problem.
     */ 
    void endDocument() throws WBSAXException;

    
    //
    // Code page methods.
    //
    
    // No support for code pages at the moment, since our usage only requires
    // code page 0 (the default). 
    // Note that switch page is not supported under WML 1.1 but is in 1.3.
    
    //void switchTagCodePage(PageCode codePage) throws WBSAXException;
    //void switchAttributeCodePage(PageCode codePage) throws WBSAXException;

    
    //
    // Element methods.
    //
    
    /**
     * Start a WBXML element using the coded element name and flags provided.
     * 
     * @param name the element name to use, as a code.
     * @param attributes true if this element will contain attribute data.
     * @param content true if this element will contain content data.
     * @throws WBSAXException if there was a problem.
     */ 
    void startElement(ElementNameCode name, boolean attributes, 
            boolean content) throws WBSAXException;

    /**
     * Start a WBXML element using the literal element name and flags provided.
     * 
     * @param name the element name to use, as a reference to a literal.
     * @param attributes true if this element will contain attribute data.
     * @param content true if this element will contain content data.
     * @throws WBSAXException if there was a problem.
     */ 
    void startElement(StringReference name, boolean attributes, 
            boolean content) throws WBSAXException;
    
    /**
     * Start a WBXML element using the opaque element start and flag provided.
     * 
     * @param element the opaque element start to use, which contains the name 
     *      and any attributes it needs.
     * @param content true if this element will contain content data.
     * @throws WBSAXException if there was a problem.
     */ 
    void startElement(OpaqueElementStart element, boolean content) 
            throws WBSAXException;

    /**
     * End a WBXML element. Only called if {@link #startElement} was passed
     * true to one or both of it's boolean flags.
     * 
     * @throws WBSAXException if there was a problem.
     */ 
    void endElement() throws WBSAXException;
    

    //
    // Processing instruction methods.
    //
    
    // No support for processing instructions at the moment.
    

    //
    // Attribute methods.
    // 

    /**
     * Start a WBXML attribute section.
     * 
     * @throws WBSAXException if there was a problem.
     */ 
    void startAttributes() throws WBSAXException;
    
    /**
     * Add a WBXML attribute using the attribute start code provided. 
     * 
     * @param start the attribute start code containing the attribute name 
     *      and optional value prefix to add.
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttribute(AttributeStartCode start) 
            throws WBSAXException;

    /**
     * Add a WBXML attribute using the literal attribute name provided. 
     * 
     * @param name the attribute name to use, as a reference to a literal.
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttribute(StringReference name) 
            throws WBSAXException;
    
    /**
     * Add content to the value of a WBXML attribute, using the coded value 
     * provided.
     * 
     * @param part part of the value of an attribute, as a code. 
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValue(AttributeValueCode part) 
            throws WBSAXException;

    /**
     * Add content to the value of a WBXML attribute, using the literal 
     * reference provided.
     * 
     * @param part part of the value of an attribute, as a reference to a 
     *      literal. 
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValue(StringReference part) 
            throws WBSAXException;

    /**
     * Add content to the value of a WBXML attribute, using the inline literal
     * value provided.
     * 
     * @param part part of the value of an attribute, as a reference to a 
     *      literal. 
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValue(WBSAXString part) 
            throws WBSAXException;
    
    /**
     * Add content to the value of a WBXML attribute, using the extension code
     * provided.
     *  
     * @param code the extension code.
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValueExtension(Extension code)
            throws WBSAXException;

    /**
     * Add content to the value of a WBXML attribute, using the extension code
     * and literal reference provided.
     *  
     * @param code the extension code.
     * @param value the value of the extension, as a reference to a literal.
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValueExtension(Extension code, StringReference value)
            throws WBSAXException;

    /**
     * Add content to the value of a WBXML attribute, using the extension code
     * and inline literal value provided.
     *  
     * @param code the extension code.
     * @param value the value of the extension, as an inline literal.
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValueExtension(Extension code, WBSAXString value)
            throws WBSAXException;
    
    /**
     * Add content to the value of a WBXML attribute, using the entity code
     * provided.
     *  
     * @param entity the entity code.
     * @throws WBSAXException if there was a problem.
     */ 
    void addAttributeValueEntity(EntityCode entity)
            throws WBSAXException;

    /**
     * Note that opaque data is not supported under WML 1.1 but is in 1.3. 
     */ 
    void addAttributeValueOpaque(OpaqueValue part) throws WBSAXException;

    /**
     * End a WBXML attribute section.
     * 
     * @throws WBSAXException
     */ 
    void endAttributes()
            throws WBSAXException;
    
    
    //
    // Content methods.
    //
    
    /**
     * Start a WBXML content section.
     * 
     * @throws WBSAXException if there was a problem.
     */ 
    void startContent() throws WBSAXException;
    
    /**
     * Add content to a WBXML element, using the literal reference provided.
     * 
     * @param part part of the content of an element, as a reference to a 
     *      literal. 
     * @throws WBSAXException if there was a problem.
     */ 
    void addContentValue(StringReference part)
            throws WBSAXException;

    /**
     * Add content to a WBXML element, using the inline literal value provided.
     * 
     * @param part part of the content of an element, as an inline literal. 
     * @throws WBSAXException if there was a problem.
     */ 
    void addContentValue(WBSAXString part)
            throws WBSAXException;

    /**
     * Add content to a WBXML element, using the extension code provided.
     *  
     * @param code the extension code.
     * @throws WBSAXException if there was a problem.
     */ 
    void addContentValueExtension(Extension code)
            throws WBSAXException;

    /**
     * Add content to a WBXML element, using the extension code and literal 
     * value provided.
     *  
     * @param code the extension code.
     * @param value the value of the extension, as a reference to a literal.
     * @throws WBSAXException if there was a problem.
     */ 
    void addContentValueExtension(Extension code, StringReference value)
            throws WBSAXException;

    /**
     * Add content to a WBXML element, using the extension code and inline 
     * literal value provided.
     *  
     * @param code the extension code.
     * @param value the value of the extension, as an inline literal.
     * @throws WBSAXException if there was a problem.
     */ 
    void addContentValueExtension(Extension code, WBSAXString value)
            throws WBSAXException;
    
    /**
     * Add content to a WBXML element, using the entity code provided.
     *  
     * @param entity the entity code.
     * @throws WBSAXException if there was a problem.
     */ 
    void addContentValueEntity(EntityCode entity)
            throws WBSAXException;

    /**
     * Note that opaque data is not supported under WML 1.1 but is in 1.3. 
     */ 
    void addContentValueOpaque(OpaqueValue part) throws WBSAXException;

    /**
     * End a WBXML content section.
     * 
     * @throws WBSAXException if there was a problem.
     */ 
    void endContent()
            throws WBSAXException;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
