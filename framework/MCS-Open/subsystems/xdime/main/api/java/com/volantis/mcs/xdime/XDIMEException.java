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


package com.volantis.mcs.xdime;


public class XDIMEException extends Exception {

    /**
     * Constructor which takes a message.
     * @param message the exception message
     */
    public XDIMEException(String message) {
        super(message);
    }

    /**
     * Constructor which takes an exception.
     * @param e the exception
     */
    public XDIMEException(Exception e) {
        super(e);
    }

    /**
     * Constructor which takes a message and an exception.
     * @param message the exception message
     * @param e the exception
     *
     */
    public XDIMEException(String message, Exception e) {
        super(message, e);
        // todo do something with the exception
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 25-Jul-05	9060/1	tom	VBM:2005071304 Interim Commit so Emma can see the changes we have made

 18-Jul-05	9021/1	ianw	VBM:2005071114 interim commit of XDIME API for DISelect integration

 ===========================================================================
*/
