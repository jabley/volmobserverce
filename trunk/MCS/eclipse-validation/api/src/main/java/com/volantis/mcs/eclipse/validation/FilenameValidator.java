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

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import com.volantis.mcs.utilities.FaultTypes;

/**
 * Provide a validator for validating file names.
 */
public class FilenameValidator extends SimpleValidator {

    private static String mark = "(c) Volantis Systems Ltd 2003."; //$NON-NLS-1$

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: INVALID_FILENAME; args: object
     */
    // javadoc inherited.
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {

        ValidationStatus status = super.validate(object, messageBuilder);

        if (status.getSeverity() == ValidationStatus.OK) {
            if(!(new Path("")).isValidPath(object.toString())) {

                String faultType = FaultTypes.INVALID_FILENAME;
                Object formatArgs [] = new Object[1];
                formatArgs[0] = object;
                String message = messageBuilder.buildValidationMessage(
                        faultType, formatArgs);

                status = new ValidationStatus(Status.ERROR, message);
            }
        }
        return status;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05	9802/1	rgreenall	VBM:2005092606 Fixed issue where absolute paths were always considered to be invalid.

 03-Oct-05	9614/1	rgreenall	VBM:2005092606 Fixed issue where absolute paths were always considered to be invalid.

 08-Sep-05	9467/1	rgreenall	VBM:2005082306 mergevbm from 330: Enhanced validation for reserved filename characters in Windows.

 08-Sep-05	9459/1	rgreenall	VBM:2005082306 Enhanced validation for reserved filename characters in Windows.

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 ===========================================================================
*/
