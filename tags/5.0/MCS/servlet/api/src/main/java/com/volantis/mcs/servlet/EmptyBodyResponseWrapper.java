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

package com.volantis.mcs.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wrapper for HTTP response that ensures that the response body 
 * is always empty. Should be used for generating response to
 * HEAD request.  
 */
public class EmptyBodyResponseWrapper extends HttpServletResponseWrapper {

    /**
     * Internal counter for bytes written to the response body
     */
    private int byteCount = 0;

    /**
     * Internal output stream that keeps track of the length 
     * of written data by incrementing byte counter.
     * The actual data are ignored.
     */
    private ServletOutputStream outputStream = new ServletOutputStream() {
        
        public void write(int b) {
            byteCount++;
        }

        public void write(byte[] b, int off, int len) {
            byteCount += len;
        }
    };
    
    /**
     * Print writer based on internal output stream
     */
    private PrintWriter writer = new PrintWriter(outputStream);
    
    /**
     * Flag indicating that the content length has been set 
     * externally by a call to setContentLength()
     */
    private boolean contentLengthSet = false;
   
    public EmptyBodyResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    // javadoc inherited
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    // javadoc inherited
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    /**
     * Allows to externally set content length. 
     * 
     * @see javax.servlet.ServletResponse#setContentLength(int)
     */
    public void setContentLength(int len) {        
        // when content length is set externally, 
        // we respect it, so need to mark a flag
        super.setContentLength(len);
        contentLengthSet = true;
    }

    /**
     * Set content length onbasis of data written to the response,
     * unless it has already been set by a call to setContentLength(),
     * in which case this method does nothing.
     */
    public void updateContentLength() {
        // Update content length if it was not set externally
        if (!contentLengthSet) {
            super.setContentLength(byteCount);            
        }
    }
}
