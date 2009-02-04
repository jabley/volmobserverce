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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.devices;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.mcs.servlet.AbstractMarinerServlet;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.runtime.Volantis;

/**
 * This is a servlet to resolve a device against the headers recieved
 * including the user agent and any secondary id header. There is probably
 * a much better way to do this, but I do not know what it is and this works.
 * 
 */
public class ResolveDeviceServlet extends AbstractMarinerServlet {
    
    /** Simply pass on for processing */
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
        throws ServletException, IOException {
        process( request, response );
    }

    /** Simply pass on for processing */
    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response)
        throws ServletException, IOException {
        process( request, response );
    }

    /**
     * Resolve the device from initialising the bean and request context and
     * pass that device name back in the response.
     */
    protected void process( HttpServletRequest request,
                            HttpServletResponse response)
        throws ServletException, IOException {
            
        Volantis bean = getVolantisBean();
        MarinerServletRequestContext context = 
                                getMarinerRequestContext( request, response );
                                
        String dev = context.getDeviceName();
            
        response.setContentType( "text/plain" );
        PrintWriter out = response.getWriter();
        if( dev == null ) {
            out.print( "Device Not Found" );
        } else {
            out.print( "Device is " + dev );
        }            
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 02-Sep-03	1290/1	steve	VBM:2003082105 Secondary ID Header implementation

 ===========================================================================
*/
