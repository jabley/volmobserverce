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
 * $Header: /src/mps/com/volantis/mps/context/MPSApplicationContext.java,v 1.1 2002/11/12 20:54:51 ianw Exp $
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

package com.volantis.mps.context;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mps.message.MessageAsset;

import java.util.HashMap;
import java.util.Map;

/**
 * This ApplicationContext adds a an asset map to <code>ApplicationContext</code>
 * to enable the assembly of MultipartMime messages.
 * 
 */
public class MPSApplicationContext extends ApplicationContext {
    
    /**
     * Map to store message assets.
     */
    protected Map assetMap = new HashMap();

    /**
     * The maximum file size for assets
     */
    protected Integer maxFileSize = null;

    /**
     * The maximum MMS message size
     */
    protected Integer maxMMSize = null;

    /**
     * The copyright statement.
     */
     private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * A default value used to indicate the maximum file and message sizes are
     * not set.
     */
    protected static final int DEFAULT_UNSET_VALUE = -1;

    /** Instanciate a new <CODE>MPSApplicationContext</CODE>.
     * @param requestContext The <CODE>MarinerRequestContext</CODE>CODE> for this request.
     */
    public MPSApplicationContext(MarinerRequestContext requestContext) {
        super(requestContext);
    }  
    
    /**
     * Adds a new asset into the asset map.
     * @param mimeReference The Mime Reference for the asset as used in the 
     * page.
     * @param messageAsset The <code>MessageAsset</code>.
     */
    public void mapAsset(String mimeReference,MessageAsset messageAsset) {
        assetMap.put(mimeReference, messageAsset);
    }
    
    /**
     * Get the asset map that associates Mime References to assets.
     * @return The asset map.
     */
    public Map getAssetMap() {
        return assetMap;
    }

    /**
     * Get the maximum file size for assets in the current context
     *
     * @return An Integer representation of the maximum file size, or null
     *         if it has not been set.
     */
    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Get the maximum message size for the current context
     *
     * @return An Integer representation of the maxium message size, or
     *         null it is has not been set.
     */
    public Integer getMaxMMSize() {
        return maxMMSize;
    }

    // JavaDoc inherited
    public void setDevice(InternalDevice device) {
        // Set the device
        super.setDevice(device);

        // These two property values here are based in the
        // MarinerPageContext#constructURL method.

        // Now set the maximum message size of the device if it has been
        // specified
        int policyValue = device.getIntegerPolicyValue("maxmmsize",
                                                       DEFAULT_UNSET_VALUE);
        if (policyValue != DEFAULT_UNSET_VALUE) {
            maxMMSize = new Integer(policyValue);
        }

        // And set the maximum filesize.
        policyValue = device.getIntegerPolicyValue("maximagesize",
                                                   DEFAULT_UNSET_VALUE);
        if (policyValue != DEFAULT_UNSET_VALUE) {
            maxFileSize = new Integer(policyValue);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 30-Jul-04	133/3	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 ===========================================================================
*/
