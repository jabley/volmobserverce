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
 * $Header: /src/mps/com/volantis/mps/message/ProtocolIndependentMessage.java,v 1.1 2002/11/14 10:33:21 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002091806 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.message;

import java.net.URL;
import java.util.Map;

/**
 *
 * @author  ianw
 */
public class ProtocolIndependentMessage {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    protected String mimeType;
    
    protected String message;
    
    protected Map assetMap;
    
    protected URL baseURL;

    /**
     * The maximum file size supported by this message.  Can be null.
     */
    protected Integer maxFileSize;

    /**
     * The maximum message size of this message.  Can be null.
     */
    protected Integer maxMMSize;

    /**
     * The character encoding for this message. Can be <code>null</code>
     */
    protected String characterEncoding;

    /** Creates a new instance of MarinerMessage */
    public ProtocolIndependentMessage(String message,
                                      String mimeType,
                                      Map assetMap) {

        this(message, mimeType, assetMap, null);
    }

    /**
     * Creates a new <code>ProtocolIndependentMessage</code> message
     * @param message 
     * @param mimeType
     * @param assetMap
     * @param characterEncoding
     */
    public ProtocolIndependentMessage(String message,
                                      String mimeType,
                                      Map assetMap,
                                      String characterEncoding) {
        this.message =  message;
        this.mimeType = mimeType;
        this.assetMap = assetMap;
        this.characterEncoding = characterEncoding;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public Map getAssetMap() {
        return assetMap;
    }
    
    public void setBaseURL(URL baseURL) {
        this.baseURL = baseURL;
    }
    
    public URL getBaseURL() {
        return baseURL;
    }

    /**
     * Get the maximum file size.
     *
     * @return An Integer object representing the maximum file size, or null
     *         if it has not been specified.
     */
    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Set the maximum file size.
     *
     * @param maxFileSize The new file size to use.  May be null.
     */
    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * Get the maximum message size.
     *
     * @return An Integer object representing the maximum message size, or null
     *         if it has not been specified.
     */
    public Integer getMaxMMSize() {
        return maxMMSize;
    }

    /**
     * Set the maximum message size.
     *
     * @param maxMMSize The new message size to use.  May be null.
     */
    public void setMaxMMSize(Integer maxMMSize) {
        this.maxMMSize = maxMMSize;
    }

    /**
     * Get the character encoding specified for this message
     *
     * @return  A <code>String</code> representing the character encoding
     * for this message, or <code>null</code> if none has been specified
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * Set the character encoding for this message
     *
     * @param charEnc The character encoding, e.g. Big5, Shift_JS
     */
    public void setCharacterEncoding(String charEnc) {
        this.characterEncoding = charEnc;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 30-Jul-04	133/3	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 ===========================================================================
*/
