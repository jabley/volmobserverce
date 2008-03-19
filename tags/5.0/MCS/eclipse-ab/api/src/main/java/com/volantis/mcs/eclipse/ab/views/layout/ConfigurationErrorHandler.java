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

package com.volantis.mcs.eclipse.ab.views.layout;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

/**
 * Custom implementation of an ErrorHandler for the Configuration reader.
 */
public class ConfigurationErrorHandler implements ErrorHandler {

    // javadoc inherited.
    public void error(SAXParseException e) throws SAXException {
        throw e;
    }

    // javadoc inherited.
    public void fatalError(SAXParseException e) throws SAXException {
        throw e;
    }

    // javadoc inherited.
    public void warning(SAXParseException e) throws SAXException {
        // do nothing.
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jan-04	2483/2	byron	VBM:2003121504 Eclipse PM Layout Editor: Format Attributes View: XML Config

 ===========================================================================
*/
