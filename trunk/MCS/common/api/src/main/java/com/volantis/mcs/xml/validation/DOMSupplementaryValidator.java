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

import org.jdom.Element;

/**
 * Interface for external implementor usage when requiring validation against 
 * various elements that isn't otherwise handled by the DTD and XSD validation
 * features
 */
public interface DOMSupplementaryValidator {

    /**
     * This method will be called when an element for which the validator is
     * registered is to be validated. If the supplementary validator finds
     * an error it simply needs to report it via the given error reporter.
     * <p><strong>Implementations must not invoke the
     * {@link ErrorReporter#validationStarted} and
     * {@link ErrorReporter#validationCompleted} methods as the
     * {@link DOMValidator} that manages the supplementary validators is
     * repsonsible for invoking these methods.</strong></p>
     */
    void validate(Element element,
                  ErrorReporter errorReporter);
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Jan-04	2364/1	doug	VBM:2004010401 Fixed problem with ComboViewer set/getValue()

 09-Dec-03	2057/3	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 28-Nov-03	2055/1	doug	VBM:2003112802 Added ODOM validation interfaces

 ===========================================================================
*/
