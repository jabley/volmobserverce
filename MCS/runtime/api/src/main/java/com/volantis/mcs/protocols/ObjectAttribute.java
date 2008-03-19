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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import java.util.HashMap;
import java.util.Map;

/**
 * An object attribute.
 */
public class ObjectAttribute extends MCSAttributes {

    /**
     * The URI of the object to render.
     */
    private String srcUri;

    /**
     * The source type of the source URI
     */
    private String srcType;

    /**
     * The map with object parameters. Instantiated on first access.
     */
    private Map paramMap;

    /**
     * The container for text received from mtxt mcs policy 
     */
    private String textContainer; 
	/**
     * The needs finalizer flag.
     */
    private boolean needsFinalizer;

    
    /**
     * Indicates whether this is a convertible image asset 
     */
    private boolean convertibleImageAsset;

    /**
     * Element finalizer, as provided by protocol.
     */
    private ElementFinalizer finalizer;
    
    /**
     * The buffer containing object body.
     */
    private OutputBuffer bodyBuffer;

	
    /**
     * Holds caption content of object
     */
    private OutputBuffer captionContent;


    /**
     * This constructor delegates all its work to the initialise method, no
     * extra initialisation should be added here, instead it should be added to
     * the initialise method.
     */
    public ObjectAttribute() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its state
     * immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor and
     * also from resetAttributes.
     */
    private void initialise() {
        setSrc(null);
        
        needsFinalizer = false;
        finalizer = null;
	bodyBuffer = null;
	textContainer = null;
    }

    /**
     * Set the src URI.
     * 
     * @param uri
     */
    public void setSrc(String uri) {
        srcUri = uri;
    }

    /**
     * Return the src URI.
     * 
     * @return srcURI
     */
    public String getSrc() {
        return srcUri;
    }

    /**
     * Set the source type.
     * 
     * @param type
     */
    public void setSrcType(String type) {
        srcType = type;
    }

    /**
     * Return the source type.
     * 
     * @return srcType
     */
    public String getSrcType() {
        return srcType;
    }

    /**
     * Returns the parameter map.
     * 
     * @return the parameter map.
     */
    public Map getParamMap() {
        if (paramMap == null) {
            paramMap = new HashMap();
        }
        return paramMap;
    }

    /**
     * put value into param map
     * @param paramMap
     */
    public void setParamMap(Map paramMap){
        this.paramMap = paramMap;
    }

    /**
     * Returns the buffer with object body, possibly null.
     * 
     * @return Returns the bodyBuffer.
     */
    public OutputBuffer getBodyBuffer() {
        return bodyBuffer;
    }

    /**
     * Sets the buffer with object body.
     * 
     * @return Returns the bodyBuffer.
     */
    public void setBodyBuffer(OutputBuffer bodyBuffer) {
        this.bodyBuffer = bodyBuffer;
    }

    // Javadoc inherited
    public boolean needsFinalizer() {
        return needsFinalizer;
    }
    
    // Javadoc inherited
    public void setFinalizer(ElementFinalizer finalizer) {
        this.finalizer = finalizer;
    }

    /**
     * @return the textContainer
     */
    public String getTextContainer() {
        return textContainer;
    }

    /**
     * @param textContainer the textContainer to set
     */
    public void setTextContainer(String textContainer) {
        this.textContainer = textContainer;
    }

    /**
     * Marks element described by this attributes as needing finalizer. Passing
     * marked attributes to protocol writeOpenObject() method, the protocol CAN
     * provide a finalizer, which can be accessed using getFinalizer() method.
     */
    public void setNeedsFinalizer() {
        needsFinalizer = true;
    }

    /**
     * Returns element finalizer provided by the protocol.
     * 
     * @returns element finalizer
     */
    public ElementFinalizer getFinalizer() {
        return finalizer;
    }

    /**
     * get buffer that stores caption content
     * @return
     */
    public OutputBuffer getCaptionContent() {
        return captionContent;
    }

    /**
     * set caption content buffer
     * @param captionContent
     */
    public void setCaptionContent(OutputBuffer captionContent) {
        this.captionContent = captionContent;
    }
    /**
     * Indicates whether this is a convertible image asset 
     */
    public boolean isConvertibleImageAsset() {
        return convertibleImageAsset;
    }

    /**
     * Sets whether this is a convertible image asset 
     * @param convertibleImageAsset  
     *      Set this boolean to true to signify a convertible image 
     *      asset.
     */
    public void setConvertibleImageAsset(boolean convertibleImageAsset) {
        this.convertibleImageAsset = convertibleImageAsset;
    }
}

/*
 * ===========================================================================
 * Change History
 * ===========================================================================
 * $Log$
 * 
 * 30-Sep-05 9562/1 pabbott VBM:2005092011 Add XHTML2 Object element
 * 
 * ===========================================================================
 */
