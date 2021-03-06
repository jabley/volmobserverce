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
 * $Header: /src/voyager/com/volantis/testtools/stubs/ServletResponseStub.java,v 1.1 2002/12/18 16:46:48 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Dec-02    Allan           VBM:2002121711 - Created. A stub implementation
 *                              of a ServletResponse.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.stubs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

/**
 * A stub implementation of a ServletResponse. None of the methods do anything
 * except return a value where required. This class can be used to 
 * create ServletResponse objects for test purposes. This class can also be
 * extended to provide specific behaviour in specific methods without the need
 * to implement every method in the ServletResponse interface.
 */
public class ServletResponseStub implements ServletResponse {
    public void flushBuffer() throws IOException {
    }

    public int getBufferSize() {
        return 0;
    }

    public String getCharacterEncoding() {
        return null;
    }

    public Locale getLocale() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    public PrintWriter getWriter() throws IOException {
        return null;
    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {
    }

    public void setBufferSize(int i) {
    }

    public void setContentLength(int i) {
    }

    public void setContentType(String s) {
    }

    public void setLocale(Locale locale) {
    }

    public void resetBuffer() {
    }

    public String getContentType() {
        throw new UnsupportedOperationException();
    }

    public void setCharacterEncoding(String charset) {
        throw new UnsupportedOperationException();
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
