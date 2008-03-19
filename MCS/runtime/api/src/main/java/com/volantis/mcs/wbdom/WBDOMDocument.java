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
 * $Header: /src/voyager/com/volantis/mcs/dom/Document.java,v 1.5 2002/05/23 09:49:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Geoff           VBM:2003042905 - Created; the root of the 
 *                              WBDOM. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.dom.DocumentAnnotation;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * A WBDOM Document.
 * <p>
 * WBDOM documents contain a tree structured DOM where the individual nodes 
 * are composed of collections of WBSAX values.
 * 
 * @see WBDOMNode
 * @see WBDOMElement
 * @see WBDOMAttribute
 * @see WBDOMText
 * @see com.volantis.mcs.wbsax.WBSAXValueVisitor
 */
public class WBDOMDocument {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(WBDOMDocument.class);

    /**
     * The WBSAX version code of this document 
     */ 
    private VersionCode version;
  
    /**
     * The WBSAX public id code of this document.
     */ 
    private PublicIdCode publicId;
    
    /**
     * The codec which controls any character conversion we need to do.
     */ 
    private Codec codec;
    
    /**
     * The "input" string table, that is, the string table used whilst
     * creating the WBSAX events which are stored in the document.
     * <p>
     * Generally speaking we will need a new "output" string table when 
     * serialising the document because the string table will need to be
     * different for each serialisation - it will be a subset for instance if
     * we do dissection.
     */ 
    private StringTable stringTable;

    /**
     * The factory to use for creating inline strings.
     * <p>
     * We will only require this if we are dynmaically modifying the output
     * during serialisation, for example during optimising.
     */ 
    private StringFactory strings;
    
    /**
     * The single element that contains all the contents of the document.
     */
    private WBDOMElement rootElement;

    /**
     * Some "user data" associated with the document. At the moment this is
     * used only by the dissector, but this could change.
     */
    private DocumentAnnotation annotation;

    /**
     * Create an instance of this class.
     * 
     * @param version
     * @param publicId
     * @param codec
     * @param stringTable
     * @param strings
     */ 
    public WBDOMDocument(VersionCode version, PublicIdCode publicId, 
            Codec codec, StringTable stringTable, StringFactory strings) {
        this.version = version;
        this.publicId = publicId;
        this.codec = codec;
        this.stringTable = stringTable; // may be null.
        this.strings = strings;
    }

    /**
     * Get the coded version of this document.
     * 
     * @return the version.
     */ 
    public VersionCode getVersion() {
        return version;
    }

    /**
     * Get the coded public id of this document.
     * 
     * @return the public id.
     */ 
    public PublicIdCode getPublicId() {
        return publicId;
    }

    /**
     * Get the codec that the document is using for character conversion.
     * 
     * @return the codec.
     */ 
    public Codec getCodec() {
        return codec;
    }

    /**
     * Get the string factory for creating inline strings.
     * 
     * @return the string factory
     */ 
    public StringFactory getStringFactory() {
        return strings;
    }

    /**
     * Get the "input" string table.
     * 
     * @return the string table, or null if there was none.
     */ 
    public StringTable getStringTable() {
        return stringTable;
    }

    /**
     * Set the root element provided.
     *  
     * @param rootElement
     */ 
    public void setRootElement(WBDOMElement rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Get the root element for this document.
     * 
     * @return the root element, or null if none has been set.
     */ 
    public WBDOMElement getRootElement() {
        return rootElement;
    }

    /**
     * Set the document's annotation.
     * 
     * @param annotation The annotation.
     */
    public void setAnnotation(DocumentAnnotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Get the document's annotation.
     * 
     * @return The annotation, or null if none has been set.
     */
    public DocumentAnnotation getAnnotation() {
        return annotation;
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

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/3	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
