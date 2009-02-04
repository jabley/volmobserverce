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
package com.volantis.mcs.xml.validation;

import com.volantis.mcs.xml.xpath.XPath;
import org.jdom.Element;

/**
 * Implementations of this interface are invoked by the validator to report
 * an error. The error type is indicated by the given key. Depending on the
 * key supplied, the parameter may be of use.
 */
public interface ErrorReporter {

    /**
     * An error should be reported, in whatever manner is appropriate to the
     * application, when this method is invoked.
     *
     * @param details The details for the error being reported
     */
    void reportError(ErrorDetails details);

    /**
     * This method will be invoked when validation against the given XPath
     * (and all child XPaths) is commenced.
     */
    void validationStarted(Element root, XPath xpath);

    /**
     * This method will be invoked when validation against the given XPath
     * (and all child XPaths) is completed.
     */
    void validationCompleted(XPath xpath);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 28-Nov-03	2055/1	doug	VBM:2003112802 Added ODOM validation interfaces

 ===========================================================================
*/
