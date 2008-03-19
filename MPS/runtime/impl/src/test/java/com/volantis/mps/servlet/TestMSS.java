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

import com.volantis.mcs.context.MarinerContextException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

/**
 * A simple extension to the <code>MessageStoreServlet</code> to allow it
 * to be tested with creating an entire and valid MCS context.
 */
public class TestMSS extends MessageStoreServlet {
    // JavaDoc inherited
    protected void processXML(HttpServletRequest request,
                              HttpServletResponse response,
                              InputStream msgStream)
            throws IOException, MarinerContextException, SAXException {
        //super.processXML(request, response, msgStream);

        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1028];
        int read = msgStream.read(buffer);

        while(read != -1) {
            os.write(buffer, 0, read);
            read = msgStream.read(buffer);
        }

        // flush the stream
        os.flush();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	829/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
