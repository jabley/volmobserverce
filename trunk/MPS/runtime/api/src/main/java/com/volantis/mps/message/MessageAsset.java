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
 * $Header: /src/mps/com/volantis/mps/message/MessageAsset.java,v 1.2 2002/11/15 17:11:57 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002091806 - Created
 * 15-Nov-02    sumit           VBM:2002102403 - Added support for Text messages
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.message;


import com.volantis.mcs.assets.AssetGroup;

public class MessageAsset {

    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * Constant representing a client side asset.
     */
    final public static int ON_DEVICE = AssetGroup.ON_DEVICE;
    
    /**
     * Constant representing a sever side asset.
     */
    final public static int ON_SERVER = AssetGroup.ON_SERVER;
    
    /**
     * The location of the asset, either on the device or the server.
     */
    private int locationType;
    
    /**
     * The url of this asset.
     */
    private String url;
    
    /**
     * The text of this asset.
     */
    private String text;
    
    /**
     * If this message is a text message 
     */
    private boolean textMessage=false;
    
    /** Creates a new instance of MessageAsset 
     * @param locattionType the location of the asset.
     * @param url The URL of the asset.
     */
    public MessageAsset(int locationType, String url) {

        this.locationType = locationType;
        this.url = url;
    }
    
    public MessageAsset(String text) {
        this.text = text;
        textMessage=true;
        locationType = ON_DEVICE;
    }
    
    /**
     * Get the locationType for this asset.
     * @return the locationType.
     */
    public int getLocationType() {
        return locationType;
    }
    
    /**
     * Get the URL for this asset.
     * @return the URL.
     */
    public String getUrl() {
        return url;
    }
    
    public String getText() {
        return text;
    }
    
    public boolean isTextMessage() {
        return textMessage;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
