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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Provide XDIME processing facilities required by various servlets
 * and servlet filters.
 */
public interface XDIMERequestProcessor {

    /**
     * Processes the response as XDIME.
     *
     * @param servletContext the request context
     * @param request the servlet request
     * @param response the servlet response
     * @param xdimeContent a Reader to allow the XDIME to be read
     * @param characterSet the character encoding
     * @throws IOException if an error occurs
     * @throws ServletException if an error occurs
     */
    public void processXDIME(final ServletContext servletContext,
                             final ServletRequest request,
                             final ServletResponse response,
                             final CachedContent xdimeContent,
                             final String characterSet,
                             final boolean disableResponseCaching)
            throws IOException, ServletException;

    /**
     * Returns true if the specified MIME type is one that represents XDIME.
     *
     * @param mimeType the MIME type to test
     * @return true if the given mimeType is one that represents XDIME
     */
    public boolean isXDIME(String mimeType);    
}
