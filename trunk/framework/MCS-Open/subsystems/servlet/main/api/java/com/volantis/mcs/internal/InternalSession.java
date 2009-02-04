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
 * $Header: /src/voyager/com/volantis/mcs/internal/InternalSession.java,v 1.3 2003/03/14 17:52:38 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 14-Mar-03    Chris W         VBM:2003020607 - Fixed JavaDoc
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.internal;

import java.util.HashMap;
import java.util.Map;
/**
 * This class represents the session for an internal request e.g. a request 
 * sent by MamlSaxParser.
 */
public class InternalSession {
    
    /**
     * A map of attributes stored in the session
     */
    Map attributes = new HashMap();
    /** Creates a new instance of InternalSession */
    public InternalSession() {
    }
    
    /**
     * Store an attribute in the session
     * @param key A String containing the attribute's key
     * @param value An Object representing the attribute's value
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    
    /**
     * Returns the value of the specified attribute
     * @param key A String containing the attribute's key
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
