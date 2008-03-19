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
 * $Header: /src/voyager/com/volantis/mcs/utilities/Volantis.java,v 1.292 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.utilities;


import javax.servlet.ServletContext;

/**
 * The Old Volantis root JavaBean. This bean persists for the entire life of the
 * application. Typically this is the life of the web server in which it is
 * running. The bean holds references and resources that must persist.
 */
public class Volantis {

    /**
     * Initialize this Volantis bean.
     *
     * @param servletContext the current ServletContext
     * @throws UnsupportedOperationException always since this method of
     * initializing the Volantis bean is no longer supported.
     * @deprecated use MarinerServletApplication - either getInstance() if
     * calling from java code or initialize() if calling from jsp:useBean code.
     */
    public void initialize(ServletContext servletContext) {
        String message = "This method of initializing the Volantis bean is " +
                "no longer supported. Use MarinerServletApplication - " +
                "either getInstance() if calling from java code or " +
                "initialize() if calling from jsp:useBean code.";

        throw new UnsupportedOperationException(message);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 ===========================================================================
*/
