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
package com.volantis.vdp.scs.util;

import java.util.Date;

/**
 * Contains an exemplary HTTP reponses.
 */
public class Samples {

    /**
     * The examplary HTTP response. 
     * @return response as string
     */
    public static String sampleHTTPResponse() {
        String retBody;
        String retHead;
        
        retBody = "<html><head><title>test</title></head>";
        retBody += "<body>" + new Date().toString() + "</body></html>";

        retHead = "HTTP/1.1 200 OK\n";
        retHead += "Content-Length: " + retBody.length() + "\n\n";
        return retHead+retBody;
    }

    /**
     * The response is sent to the client when the sps will not send response to the SecureConnectionServer in certain time.
     * @return response as string
     */
    public static String gwTimeout() {
        String retBody;
        String retHead;

        retBody = "<html><head><title>Connection Timeout</title></head>";
        retBody += "<body>Connection Timeout</body></html>";

        retHead = "HTTP/1.1 200 OK\n";
        retHead += "Content-Length: " + retBody.length() + "\n\n";

        return retHead + retBody;
    }

    /**
     * The response is sent to the client when the SecureConnectionServer connected with destination server.
     * This is used only during HTTPS session.
     * @return response as string
     */
    public static String connectionEstablishedResponse(String protocol) {
        String response;

        response = protocol + " 200 Connection established\r\n";
        response += "Proxy-agent: SecureConnectionServer-Proxy/0.1\r\n";
        response += "\r\n";

        return response;
    }

}
