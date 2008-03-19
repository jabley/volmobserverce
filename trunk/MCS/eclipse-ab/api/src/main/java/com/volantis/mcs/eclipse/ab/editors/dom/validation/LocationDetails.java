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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.xml.xpath.XPathException;
import org.jdom.Element;

/**
 * Definition for classes that wish to provide location information about
 * errors reported by an ErrorReporter.
 */
public interface LocationDetails {
    /**
     * Get the String describing the location of an error.
     * @param rootElement the root Element of the document containing the
     * error.
     * @param invalidElement the element that is invalid
     * @return the String describing the location of the error
     * @throws com.volantis.mcs.xml.xpath.XPathException if there is a problem using the XPath
     */ 
    public String getLocationDetailsString(Element rootElement, Element invalidElement)
            throws XPathException;
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 ===========================================================================
*/
