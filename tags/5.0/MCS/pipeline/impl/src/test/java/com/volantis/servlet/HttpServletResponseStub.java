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
 * $Header: /src/voyager/com/volantis/testtools/stubs/HttpServletResponseStub.java,v 1.1 2002/12/18 16:46:48 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-May-03    Sumit           VBM:2003030612 - A stub for HttpServletResponse
 * ----------------------------------------------------------------------------
 */
package com.volantis.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

public class HttpServletResponseStub extends ServletResponseStub
 implements HttpServletResponse {
    
    public void addCookie(Cookie cookie) {
    }

    public void addDateHeader(String s, long l) {
    }

    public void addHeader(String s, String s1) {
    }

    public void addIntHeader(String s, int i) {
    }

    public boolean containsHeader(String s) {
        return false;
    }

    public String encodeRedirectURL(String s) {
        return null;
    }

    public String encodeRedirectUrl(String s) {
        return null;
    }

    public String encodeURL(String s) {
        return null;
    }

    public String encodeUrl(String s) {
        return null;
    }

    public void sendError(int i) throws IOException {
    }

    public void sendError(int i, String s) throws IOException {
    }

    public void sendRedirect(String s) throws IOException {
    }

    public void setDateHeader(String s, long l) {
    }

    public void setHeader(String s, String s1) {
    }

    public void setIntHeader(String s, int i) {
    }

    public void setStatus(int i) {
    }

    public void setStatus(int i, String s) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
