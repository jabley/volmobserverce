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
 * $Header: /src/voyager/com/volantis/testtools/marinerurl/Handler.java,v 1.1 2003/02/28 17:07:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-03    Steve           VBM:2003021815 This class opens the connection
 *                              to the marinerurl protocol. Because of the 
 *                              design of java.set.URL, the package must be
 *                              called marinerurl ( to create a marinerurl 
 *                              protocol ) and this class must be called 
 *                              Handler within that package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.testurl;

import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/** 
 * Java protocol handler for the testurl: namespace. 
 */
public class Handler extends URLStreamHandler {
    /** 
     * Create a URLConnection object with data from the stream
     * registered with the testurl: host.
     * 
     * @param u  The URL to connect to
     * @return the connection to the URL
     * @throws IOException if an error occurs while connecting
     */
    protected URLConnection openConnection( URL u ) throws IOException {
        URLConnection uc = new TestURLConnection( u );
        uc.connect();
        return uc;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Aug-03	956/4	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
