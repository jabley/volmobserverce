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

package com.volantis.mcs.ibm.websphere.portalserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.InputStreamReader;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import com.ibm.websphere.servlet.response.HttpServletResponseProxy;
import com.ibm.websphere.servlet.response.ServletOutputStreamAdapter;

/**
 * A wrapper for the response.  This class is used when passing the response
 * along the filter chain.
 *
 */
public class MCSPortalResponseWrapper extends HttpServletResponseProxy {

    /**
     * The copyright statement.
     */
    private static String mark =
        "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MCSPortalResponseWrapper.class);

    /**
     * The response
     */
    private HttpServletResponse response;

    /**
     * The OutputStream to write to
     */
    private ByteArrayOutputStream outputStream;

    /**
     * A PrintWriter for the output stream
     */
    private PrintWriter printWriter;

    /**
     * The character encoding in which the contents of this response are
     * stored.
     */
    private String characterEncoding;

    /**
     * The HTTP status of the request
     */
    private int status;

    /**
     * The default encoding to use if the supplied encoding is invalid.
     */
    private static final String DEFAULT_ENCODING = "ISO-8859-1";

    /**
     * Create the ResponseWrapper.
     *
     * @param response The response
     * @param encoding The encoding
     */
    public MCSPortalResponseWrapper(HttpServletResponse response, String encoding) {

        this.response = response;
        outputStream = new ByteArrayOutputStream();
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(outputStream, encoding));
        } catch (UnsupportedEncodingException e) {
            logger.warn("defaulting-to", new Object[]{encoding, DEFAULT_ENCODING});
            encoding = DEFAULT_ENCODING;
            try {
                printWriter = new PrintWriter(new OutputStreamWriter(outputStream, encoding));
            } catch (UnsupportedEncodingException e1) {
                // This won't happen as we know ISO-8859-1 is valid.
            }
        }

        // Remember the character encoding for future use.
        characterEncoding = encoding;
    }


    /**
     * Return the ProxiedHttpServletResponse
     */
    protected HttpServletResponse getProxiedHttpServletResponse() {
        return response;
    }

    /**
     * Return the WrappedPortalResponse
     *
     * @return The WrappedPortalResponse
     */
    public HttpServletResponse getWrappedPortalResponse() {
        return getProxiedHttpServletResponse();
    }

    /**
     * Get the OutputStream
     *
     * @return The OutputStream
     * @throws IOException An IO problem
     */
    public ServletOutputStream getOutputStream()
        throws IOException {
        return new ServletOutputStreamAdapter(outputStream);
    }

    /**
     * Get the reader to use to access the contents of the response.
     *
     * <p>This method must only be called after the response has been completed
     * and will return a new reader each time that it is called.</p>
     *
     * @return A reader configured to use the correct encoding.
     * @throws IOException
     */
    public Reader getReader()
        throws IOException {

        // Create an input stream to wrap the byte array.
        InputStream inputStream = new ByteArrayInputStream(toByteArray());

        // Create a reader to access the input stream using the appropriate
        // character encoding.
        Reader reader = new InputStreamReader(inputStream, characterEncoding);

        return reader;
    }

    /**
     * Get the PrintWriter
     *
     * @return The PrintWriter
     * @throws IOException An IO problem
     *
     */
    public PrintWriter getWriter()
        throws IOException {
        return printWriter;
    }

    /**
     * Flush the PrintWriter & OutputStream
     *
     * @throws IOException An IO problem
     */
    public void flushBuffer()
        throws IOException {
        printWriter.flush();
        outputStream.flush();
    }

    /**
     * Return the OutputStream as a ByteArray
     *
     * @return The ByteArray
     */
    public byte[] toByteArray() {
        return outputStream.toByteArray();
    }

    /**
     * Return the contents of the response as a String.
     *
     * <p>This method is provide for debugging purposes only.</p>
     *
     * @return String representation
     */
    public String getContentsAsString()
        throws IOException {

        try {
            printWriter.flush();
            outputStream.flush();
        } catch (IOException ioexception) {
            logger.warn("unexpected-ioexception", ioexception);
            // OK to carry on though.
        }

        String output = outputStream.toString(characterEncoding);

        return output;
    }

    /**
     * @deprecated Method setStatus is deprecated
     */
    public void setStatus(int arg0, String arg1) {
        super.setStatus(arg0, arg1);
        status = arg0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#setStatus(int)
     */
    public void setStatus(int arg0) {
        super.setStatus(arg0);
        status = arg0;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletResponse#sendRedirect(java.lang.String)
     */
    public void sendRedirect(String arg0)
        throws IOException {
        super.sendRedirect(arg0);
        status = 302;
    }


    /**
     * Get the status.
     *
     * @return The status
     */
    public int getStatus() {
        return status;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Sep-04	5622/1	ianw	VBM:2004052202 ported forward IBM charset fixes

 23-Sep-04	5620/1	ianw	VBM:2004052202 ported forward IBM charset fixes

 21-Apr-04	3988/1	pduffin	VBM:2004042107 Attempted fix for Japanese character problem in MCSPortalFilter

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 17-Sep-03	1360/6	mat	VBM:2003090502 Integrate with Websphere portalserver.

 17-Sep-03	1360/4	mat	VBM:2003090502 Integrate with WebSphere Portal

 15-Sep-03	1360/1	mat	VBM:2003090502 Integrate with Websphere portalserver.

 ===========================================================================
*/
