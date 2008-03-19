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
 * $Header: /src/voyager/com/volantis/mcs/internal/InternalResponse.java,v 1.3 2003/03/14 17:52:38 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * 14-Mar-03    Chris W         VBM:2003020607 - Fixed JavaDoc
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.internal;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;

/**
 * This class provides a basic response to enable API based invocation of
 * Mariner.
 */
public class InternalResponse {
        
    /**
     * The ByteArrayOutputStream used to write the response
     */
    private ByteArrayOutputStream outputStream;
    
    /**
     * The StringWriter used to write the response
     */
    private StringWriter writer;
    
    /**
     * The response's content type
     */
    private String contentType;

    /**
     * Represents the value for the Cache-Control max-age header that would
     * be associated with an HTTP response.
     */
    private String cacheControlMaxAge;

    /** Creates a new instance of InternalResponse */
    public InternalResponse() {
    }
    
    /**
     * Returns the OutputStream through which the response is written
     * @return OutputStream
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException {
        if (writer==null) {
            outputStream = new ByteArrayOutputStream();
        } else {
            throw new IllegalStateException("Can't initialise output stream after writer");
        }
        return outputStream;
    }

    /**
     * Returns the Writer through which the response is written
     * @return Writer
     * @throws IOException
     */
    public Writer getWriter() throws IOException {
        if (outputStream==null) {
            writer = new StringWriter();
        } else {
            throw new IllegalStateException("Can't initialise writer after output stream");
        }
        return writer;
    }
    
    /**
     * Returns the content type of this response
     * @return String
     */
    public String getContentType() {
        return contentType;
    }
    
    /**
     * Sets the content type of this response
     * @param contentType A String containing the content type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Returns the cache control max age
     * @return String
     */
    public String getCacheControlMaxAge() {
        return cacheControlMaxAge;
    }

    /**
     * Sets the cache control max age
     * @param cacheControlMaxAge A String containing the cache control max age
     */
    public void setCacheControlMaxAge(String cacheControlMaxAge) {
        this.cacheControlMaxAge = cacheControlMaxAge;
    }

    /**
     * Returns the response as a String
     * @return String
     */
    public String getResponseAsString() {
        if (writer != null) {
            return writer.toString();
        } else if (outputStream != null) {
            return outputStream.toString();
        }
        return null;
    }
    
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
