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

import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import org.jdom.Element;

/**
 * This supplementary validator checks all layout constraints and reports
 * constraint violations to the given error reporter.
 *
 * <p>It should be registered on the root node of layout documents.</p>
 */
public class LayoutConstraintsValidator implements DOMSupplementaryValidator {
    /**
     * The checker used to check for constraint violations. This does not
     * terminate the checking early in case of a violation
     */
    DOMConstraint constraint = new LayoutConstraints(false);

    // javadoc inherited
    public void validate(Element element,
                         ErrorReporter errorReporter) {
        // The violation status returned is not relevant in this context
        constraint.violated(element, errorReporter);
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

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
