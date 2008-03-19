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
 * $Header: /src/mps/com/volantis/mps/internal/MPSInternalApplicationContext.java,v 1.1 2002/11/15 17:11:57 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-02    sumit           VBM:2002102403 - Added to create text messages
 *                              based on application context
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.internal;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mps.context.MPSApplicationContext;
import com.volantis.mps.message.MessageAsset;

/**
 * Represents an internal request for a mariner page. Allows specific processing
 * of text in the protocol wrt to request type
 * 
 */
public class MPSInternalApplicationContext extends MPSApplicationContext {
    
    /** Creates a new instance of MPSInternalApplicationContext */
    public MPSInternalApplicationContext(MarinerRequestContext requestContext) {
        super(requestContext);
    }
    
    /**
     * Add the freshly created mimereference, value pair to our internal asset
     * map to allow the MessageRequestor to build a messgae with assets
     */
    
    public String  mapSMILAsset(String value) {
        String mimeReference = super.mapSMILAsset(value);
        mapAsset(mimeReference, new MessageAsset(value));
        return mimeReference;
    }
}
