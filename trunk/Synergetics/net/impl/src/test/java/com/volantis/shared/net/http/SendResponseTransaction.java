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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.net.http;

import junit.framework.AssertionFailedError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * A transaction that consumes the request, checks it if the expected input is
 * provided and sends the response.
 */
public class SendResponseTransaction
        implements HttpTransaction {

    /**
     * The expected input.
     */
    private final String[] expectedInput;

    /**
     * The response content.
     */
    private final String[] responseContent;

    /**
     * Initialise.
     *
     * @param expectedInput   The expected input.
     * @param responseContent The response.
     */
    public SendResponseTransaction(
            String[] expectedInput, String[] responseContent) {
        this.expectedInput = expectedInput;
        this.responseContent = responseContent;
    }

    // Javadoc inherited.
    public void process(Socket socket) throws IOException {

        InputStream input = socket.getInputStream();
        Reader reader = new InputStreamReader(input, "us-ascii");
        BufferedReader r = new BufferedReader(reader);

        StringBuffer errors = new StringBuffer();
        int l;
        for (l = 0; true; l += 1) {
            String s = r.readLine();
            if (s == null || s.equals("")) {
                break;
            }

            if (expectedInput != null) {
                if (l >= expectedInput.length) {
                    errors.append("Line '").append(s)
                            .append("' was not expected, too much input\n");
                } else {
                    String expectedLine = expectedInput[l];
                    if (!expectedLine.equals(s)) {
                        errors.append("Expected '").append(expectedLine)
                                .append("' found '").append(s)
                                .append("'\n");
                    }
                }
            }

            System.out.println(s);
        }

        if (expectedInput != null && l != expectedInput.length) {
            errors.append("Expected " + expectedInput.length +
                    " lines, received " + l + "\n");
        }

        if (errors.length() > 0) {
            throw new AssertionFailedError(errors.toString());
        }

        OutputStream output = socket.getOutputStream();
        Writer writer = new OutputStreamWriter(output, "us-ascii");
        PrintWriter w = new PrintWriter(writer);
        for (int i = 0; i < responseContent.length; i++) {
            String line = responseContent[i];
            w.println(line);
        }
        w.flush();
    }
}
