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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MSSReader extends MessageStoreServlet {

    /*
     * This overriden method to avoid use of the <code>getReader(...)</code> method of
     * <code>HttpServletRequest</code>. The reason behind this is that the HttpUnit
     * package currently in use to test the message store servlet functionality
     * currently does not implement this method (for whatever bizarre reason).
     */
    protected String readMessage(HttpServletRequest request,
                             HttpServletResponse response)
        throws ServletException, IOException {
        Reader reader = new InputStreamReader(request.getInputStream());

        char[] buf = new char[1024];
        int read = reader.read(buf);
        StringBuffer sb = new StringBuffer();
        while(read != -1) {
            sb.append(buf, 0, read);
            read = reader.read(buf);
        }

        return sb.toString().trim();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 ===========================================================================
*/
