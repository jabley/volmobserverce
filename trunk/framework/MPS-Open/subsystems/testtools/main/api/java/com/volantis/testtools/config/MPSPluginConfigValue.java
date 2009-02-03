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
 * $Header: /src/mps/com/volantis/testtools/config/ConfigValueMps.java,v 1.1 2003/03/20 10:15:37 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-03    Geoff           VBM:2003032001 - Created; a simple ValueObject 
 *                              which contains data needed to configure MPS.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.config;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple ValueObject which contains the data needed to configure MPS.
 */ 
public class MPSPluginConfigValue implements PluginConfigValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private static final String DEFAULT_RECIPIENT_INFO = 
        "com.volantis.mps.recipient.DefaultRecipientResolver";
    
    private static final String DEFAULT_BASE_URL = 
        "http://localhost:8080";
    
    public String internalBaseUrl = DEFAULT_BASE_URL;
    
    public String messageRecipientInfo = DEFAULT_RECIPIENT_INFO;
    
    /**
     * A list of {@link ConfigValueChannel} classes.
     */ 
    public List channels = new ArrayList();
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
