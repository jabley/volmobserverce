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

package com.volantis.mcs.xml.xpath;

/** The exception thrown by {@link com.volantis.mcs.xml.xpath.XPath} event methods.
 */
public class XPathException extends Exception {

    /**
     * Create an instance of this class with the message specified.
     *
     * @param message the exception message to use.
     */
    public XPathException(String message) {
        super(message);
    }

    /**
     * Create an instance of this class with the cause specified.
      *
     * @param cause
     */
    public XPathException(Throwable cause) {
        super(cause);
    }

    /**
     * Create an instance of this class with the message and cause specified.
     *
     * @param message the exception message to use.
     * @param cause the exception which caused this exception.
     */
    public XPathException(String message, Throwable cause) {
        super(message, cause);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 20-Nov-03	1919/1	byron	VBM:2003111504 Provide ODOM XPath facilities

 ===========================================================================
*/
