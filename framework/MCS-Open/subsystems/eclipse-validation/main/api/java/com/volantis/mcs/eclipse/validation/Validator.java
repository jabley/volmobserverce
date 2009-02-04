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
package com.volantis.mcs.eclipse.validation;

/**
 * A Validator. Allows objects to be validated and provides the appropriate
 * error message depending on the outcome of the validation.
 */
public interface Validator {

    /**
     * Validate a specified object.
     * @param object The Object to validate
     * @param messageBuilder The ValidationMessageBuilder to use when
     * constructing any message that might be associated with the
     * returned ValidationStatus.
     * @return The message associated with the validation. If the object is
     * valid then null is returned.
     */
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-03	1444/3	allan	VBM:2003091903 Fix to plugin.xml and some javadoc

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
