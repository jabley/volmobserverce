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
 * $Header: /src/mps/com/volantis/mps/servlet/MPSServletApplicationContext.java,v 1.2 2002/11/20 18:00:59 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-02    sumit           VBM:2002110104 - Added to create text messages
 *                              based on application context
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.servlet;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.synergetics.utilities.Base64;
import com.volantis.mps.context.MPSApplicationContext;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  sumit
 */
public class MPSServletApplicationContext extends MPSApplicationContext {
    
    /** Creates a new instance of MPSServletApplicationContext */
    public MPSServletApplicationContext(MarinerRequestContext requestContext) {
        super(requestContext);
    }
    
    /**
     * Map a SMIL textual reference to a value. This context will add the
     * base64 encoded version of the value to the response headers for
     * retrieval by MPS
     * @param mimeReference The reference used in the page.
     * @param value The value for the textual component.
     */
    public String  mapSMILAsset(String value) {
        String mimeReference = super.mapSMILAsset(value);
        HttpServletResponse response = 
            (HttpServletResponse)((MarinerServletRequestContext)requestContext).getResponse();
        value=Base64.encodeString(value);
        response.setHeader("textAsset."+
                            mimeReference,
                            value);
        return mimeReference;
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	927/1	matthew	VBM:2005100401 Base64 has moved

 ===========================================================================
*/
