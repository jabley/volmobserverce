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

package com.volantis.mcs.servlet;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.utilities.Convertors;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.shared.content.ContentStyle;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A response wrapper which caches the content written to it.
 */
public class CachingResponseWrapper extends HttpServletResponseWrapper
        implements CachedContent {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(CachingResponseWrapper.class);

    private ServletOutputStreamAdapter outerBinaryStream;

    private ByteArrayOutputStream innerBinaryStream;

    private PrintWriter outerTextWriter;

    private CharArrayWriter innerTextWriter;

    /**
     * The content type of this response.
     */
    private String contentType;

    /**
     * The HTTP status code of the request.
     */
    private int status;

    /**
     * Initialises an instance of this class with the given response.
     *
     * @param response the response to wrap
     */
    public CachingResponseWrapper(HttpServletResponse response) {
        super(response);
    }


    // ====================================================================
    // Cache the binary or textual content.
    // ====================================================================

    /**
     * Gets the ServletOutputStream used by this response wrapper. Note that
     * this wrapper is either using a ServletOutputStream or a Writer but not
     * both.
     *
     * @return the ServletOutputStream
     * @throws IOException           if there is a problem obtaining the
     *                               stream
     * @throws IllegalStateException if this response wrapper has already used
     *                               a PrintWriter (@see #getWriter()).
     */
    public ServletOutputStream getOutputStream()
            throws IOException, IllegalStateException {

        if (innerTextWriter != null) {
            throw new IllegalStateException("A PrintWriter is already " +
                    "being used to write the body.");
        }

        if (innerBinaryStream == null) {
            innerBinaryStream = new ByteArrayOutputStream();
            outerBinaryStream = new ServletOutputStreamAdapter(
                    innerBinaryStream);
        }

        return outerBinaryStream;
    }

    /**
     * Gets the PrintWriter used by this response wrapper. Note that this
     * wrapper is either using a ServletOutputStream or a Writer but not both.
     *
     * @return the PrintWriter
     * @throws IOException           if there is a problem obtaining the
     *                               writer
     * @throws IllegalStateException if this response wrapper has already used
     *                               a PrintWriter (@see #getWriter()).
     */
    public PrintWriter getWriter()
            throws IOException, IllegalStateException {

        if (hasBinaryContent()) {
            throw new IllegalStateException(
                    "A ServletOutputStream is already being used to " +
                    "write the body.");
        }

        if (!hasTextContent()) {
            innerTextWriter = new CharArrayWriter();
            outerTextWriter = new PrintWriter(innerTextWriter);
        }

        return outerTextWriter;
    }

    public void flushBuffer() throws IOException {

        if (hasBinaryContent()) {
            flushBinary();
        }

        if (hasTextContent()) {
            flushText();
        }
    }

    /**
     * Return the content style of any cached content, or null if there is
     * none.
     */
    public ContentStyle getContentStyle() {
        if (hasBinaryContent()) {
            return ContentStyle.BINARY;
        }
        if (hasTextContent()) {
            return ContentStyle.TEXT;
        }

        return null;
    }

    public byte[] getAsByteArray() throws IOException {
        if (!hasBinaryContent()) {
            throw new IllegalStateException("No binary content available");
        }

        flushBinary();
        final byte[] buf = innerBinaryStream.toByteArray();
        return buf;
    }

    public char[] getAsCharArray() throws IOException {
        if (!hasTextContent()) {
            throw new IllegalStateException("No text content available");
        }

        flushText();
        final char[] buf = innerTextWriter.toCharArray();
        return buf;
    }

    /**
     * Copies the content of this response wrapper to the given servlet
     * response.  If the response does not contain any content then this
     * method does nothing.
     *
     * @param response the response to be populated from this response wrapper
     * @throws IOException if there is a problem populating the response
     */
    public void writeTo(ServletResponse response)
            throws IOException {

        if (hasTextContent()) {
            flushText();
            if (innerTextWriter.size() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Writing response wrapper content as " +
                            innerTextWriter.size() + " characters using " +
                            "content type " + contentType);
                }
                response.setContentType(contentType);
                innerTextWriter.writeTo(response.getWriter());
            }
            // they got a writer but never used it.

        } else if (hasBinaryContent()) {
            flushBinary();
            if (innerBinaryStream.size() > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Writing response wrapper content as " +
                            innerBinaryStream.size() + " bytes.");
                }
                response.setContentType(contentType);
                response.setContentLength(innerBinaryStream.size());
                innerBinaryStream.writeTo(response.getOutputStream());
            }
            // they got an output stream but never used it.
        }
        // else, no content at all, nothing to do.
    }

    private void flushBinary() throws IOException {
        outerBinaryStream.flush();
    }

    private void flushText() throws IOException {
        if (outerTextWriter.checkError()) {
            // Should never happen, but just in case.
            throw new IOException("Error flushing print writer");
        }
    }

    private boolean hasTextContent() {
        return outerTextWriter != null;
    }

    private boolean hasBinaryContent() {
        return outerBinaryStream != null;
    }

    // javadoc inherited
    public void setContentLength(int len) {
        // This is specifically overridden because the default servlet is
        // likely to try and set the content length from the original XDIME
        // file's size which is clearly wrong as the content length, if set,
        // should match the final content's length (output from MCS).

        // The HTTP specification allows the content length to be optional,
        // so we simply stop this method doing anything.
    }


    // ====================================================================
    // Cache the content type.
    // ====================================================================

    // javadoc inherited
    public void setContentType(String type) {
        super.setContentType(type);

        contentType = type;
    }

    public String getContentType() {
        return contentType;
    }

    /**
     * Gets the mime type from the content type of this response.
     *
     * @return the mime type
     */
    public String getMimeTypeFromContentType() {
        return Convertors.contentTypeToMimeType(contentType);
    }


    // ====================================================================
    // Cache the status code.
    // ====================================================================

    // javadoc inherited
    public void setStatus(int status, String msg) {
        super.setStatus(status, msg);

        this.status = status;
    }

    // javadoc inherited
    public void setStatus(int status) {
        super.setStatus(status);

        this.status = status;
    }

    // javadoc inherited
    public void sendRedirect(String location) throws IOException {
        super.sendRedirect(location);

        // This method sends a temporary redirect response to the client using
        // the specified redirect location URL. This type of redirection
        // corresponds to a status code of SC_MOVED_TEMPORARILY (302), so this
        // value should be stored here. Note that SC_MOVED_TEMPORARILY has
        // been deprecated in the servlet 2.4 API in favour of the new
        // SC_FOUND (302). However, we only support the servlet 2.3 API.
        status = HttpServletResponse.SC_MOVED_TEMPORARILY;
    }

    /**
     * Gets the status code of the wrapper's response.
     *
     * @return the status code
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

 09-Dec-05	10756/2	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 08-Dec-05	10677/1	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 08-Dec-05	10677/2	geoff	VBM:2005120708 Orange pages have literal non-breaking spaces in them.

 23-May-05	8422/1	rgreenall	VBM:2005021710 HTTP HEAD requests for XDIME pages now supported.

 13-May-05	8230/1	philws	VBM:2005051009 Port FormFragmentationServlet XDIME XML changes from 3.3 (removes XDIME page caching to be added back by Doug under 2005041916)

 13-May-05	8165/1	philws	VBM:2005051009 Ensure that the FormFragmentationServlet works with XDIME XML pages

 22-Apr-05	7794/1	ianw	VBM:2005042104 Add getCharacterEncoding method to MCSResponseWrapper

 22-Apr-05	7789/1	ianw	VBM:2005042104 Add getCharacterEncoding method to MCSResponseWrapper

 11-Apr-05	7376/1	allan	VBM:2005031101 SmartClient bundler - commit for testing

 04-Feb-05	6806/3	philws	VBM:2005012610 Introduce out-of-the-box native XDIME handling

 13-Jan-05	6655/3	pcameron	VBM:2005011103 Fixes to servlet filter

 11-Jan-05	6413/13	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 ===========================================================================
*/
