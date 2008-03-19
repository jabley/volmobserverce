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
 * $Header: /src/voyager/com/volantis/testtools/server/HTTPResourceServer.java,v 1.2 2003/02/18 12:45:15 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Created. An HTTP server that
 *                              serves resources. The path from the URL is the
 *                              resource name.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.server;

import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 * 
 * @deprecated Do not make modifications as the testcase has been defuncted as 
 *  it was not reliable. Use testurl instead. 
 */
public class HTTPResourceServer extends HTTPServer {
    public HTTPResourceServer() {
        this(8088);
    }

    public HTTPResourceServer(int port) {
        this(port, 0);
    }

    public HTTPResourceServer(int port, int maxConnections) {
        super(port, maxConnections);
    }

    protected void handleConnection(Socket server)
        throws IOException {
        OutputStream out = server.getOutputStream();
        PrintWriter pout = new PrintWriter(out, true);
        BufferedReader in = SocketUtil.getReader(server);
        String failureReason = null;
        int failureCode = 0;
        String httpVersion = "HTTP/1.0";
        String uri = null;
        String command = in.readLine();
        URL url = null;

        if (command != null) {
            StringTokenizer tokenizer = new StringTokenizer(command);

            if (tokenizer.countTokens() != 3) {
                failureCode = 400;
                failureReason = "Illformed Request-Line";
            } else {
                String method = tokenizer.nextToken();

                if (!method.equalsIgnoreCase("get")) {
                    failureCode = 501;
                    failureReason = "Only supports GET method";
                } else {
                    uri = tokenizer.nextToken();
                    httpVersion = tokenizer.nextToken();

                    try {
                        url = getURL(uri);
                    } catch (IOException e) {
                        failureCode = 404;
                        failureReason = "resource not found";
                    }
                }
            }
        } else {
            failureCode = 400;
            failureReason = "Null request";
        }

        if (url != null) {
            InputStream stream = null;
            try {
                URLConnection connection = url.openConnection();
                byte[] chunk = new byte[1024];
                int read = 0;
                pout.println(httpVersion + " 200 ");
                pout.println("Content-Type: " + connection.getContentType());
                pout.println("Content-Length: " + connection.getContentLength());
                pout.println("Content-Encoding: " + connection.getContentEncoding());
                pout.println();
                stream = connection.getInputStream();
                read = stream.read(chunk);
                while (read != -1) {
                    out.write(chunk, 0, read);
                    read = stream.read(chunk);
                }
            } catch (IOException e) {
                failureCode = 500;
                failureReason = "problem reading the resource content";
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } else {
            failureCode = 404;
            failureReason = "resource not found";
        }

        if (failureCode != 0) {
            pout.println(httpVersion + " " + failureCode + " " + failureReason);
            pout.println();
        }

        doDelay();
        server.close();
    }

    protected URL getURL(String uri) throws IOException {
        URL url = this.getClass().getResource(uri);

        return url;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-03	1599/1	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 06-Aug-03	956/1	geoff	VBM:2003080601 finally fix bodgy marinerurlregistry

 ===========================================================================
*/
