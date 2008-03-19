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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Filters out the headers that cause conditional GET to return a 304 response.
 */
public class MCSRequestWrapper
        extends HttpServletRequestWrapper {

    private static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
                    public boolean hasMoreElements() {
                        return false;
                    }

                    public Object nextElement() {
                        throw new NoSuchElementException();
                    }
                };

    public MCSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    private boolean filterHeader(String header) {

        // Filter out any headers that start with "if-" in any case.
        return header.length() >= 3 &&
                Character.toLowerCase(header.charAt(0)) == 'i' &&
                Character.toLowerCase(header.charAt(1)) == 'f' &&
                header.charAt(2) == '-';
    }

    public String getHeader(String header) {
        if (filterHeader(header)) {
            return null;
        }
        return super.getHeader(header);
    }

    public long getDateHeader(String header) {
        if (filterHeader(header)) {
            return -1;
        }
        return super.getDateHeader(header);
    }

    public int getIntHeader(String header) {
        if (filterHeader(header)) {
            return -1;
        }
        return super.getIntHeader(header);
    }

    public Enumeration getHeaders(String header) {
        if (filterHeader(header)) {
            return EMPTY_ENUMERATION;
        }

        return super.getHeaders(header);
    }

    public Enumeration getHeaderNames() {
        Enumeration enumeration = super.getHeaderNames();
        List list = new ArrayList();
        while (enumeration.hasMoreElements()) {
            String header = (String) enumeration.nextElement();
            if (!filterHeader(header)) {
                list.add(header.toLowerCase());
            }
        }

        return Collections.enumeration(list);
    }

    public String getMethod() {
        String originalMethod = super.getMethod();
        return ("HEAD".equals(originalMethod)) ? 
                    "GET" : originalMethod;
    }       
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Nov-05	10213/1	pduffin	VBM:2005093004 Committing header filters

 ===========================================================================
*/
