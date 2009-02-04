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

 public class Response {

     private String headerResponse;
     private String[] attrsHeader;

     public Response(byte[] data, int end) {

         String dataString  = new String(data, 0, end);
         headerResponse =
                 dataString.substring(0, dataString.indexOf("\n\r\n"));
         attrsHeader = headerResponse.split("\n");

     }

    public String toString() {
        return headerResponse;
    }

    public String getResponseLine() {
        return attrsHeader[0];
    }

    public String getConnection() {
        return getAttribute("Connection");
    }

    public String getTransfEncoding() {
        return getAttribute("Transfer-Encoding");
    }

    public String getContentLength() {
        return getAttribute("Content-Length");    
    }

    public String getAttribute(String key) {
        String value = "";

        int index = 0;
        while( index < attrsHeader.length ) {
            if(attrsHeader[index].startsWith(key))
                break;
            index++;
        }

        if( index >= attrsHeader.length ) {
            value = null;
        } else {
            value = attrsHeader[index].split(": ")[1];
            value = value.trim();
        }

        return value;
    }
 }
