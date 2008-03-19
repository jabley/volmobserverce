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

import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.runtime.Status;

public class DeviceRepositoryFileValidator
        extends FilenameValidator {

    /**
     * The device repository file extension.
     */
    private static final String DEVICE_REPOSITORY_FILE_EXT = "." + //$NON-NLS-1$
            FileExtension.DEVICE_REPOSITORY;


    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: INVALID_DEVICE_REPOSITORY_FILENAME; args: object
     */
    // javadoc inherited.
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {

        ValidationStatus status = super.validate(object, messageBuilder);

        if (status.getSeverity() == ValidationStatus.OK) {
            String repositoryName = object.toString();
            // Check that the file is a file and can be read
            status = ValidationUtils.checkFile(repositoryName,
                    ValidationUtils.FILE_IS_FILE |
                    ValidationUtils.FILE_CAN_READ);
            if (status.getSeverity() == ValidationStatus.OK) {
                // Now check to see if the file name ends with .mdpr
                if (!repositoryName.endsWith(DEVICE_REPOSITORY_FILE_EXT)) {
                    Object formatArgs [] = new Object[]{object};
                    String faultType = FaultTypes.INVALID_DEVICE_REPOSITORY_FILENAME;
                    String message = messageBuilder.buildValidationMessage(
                            faultType, formatArgs);
                    status = new ValidationStatus(Status.ERROR, message);
                }
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

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4231/3	tom	VBM:2004042704 Fixedup the 2004032606 change

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 30-Oct-03	1639/5	byron	VBM:2003101602 Create a MCS Project properties page - fixed javadoc

 30-Oct-03	1639/3	byron	VBM:2003101602 Create a MCS Project properties page - addressed requirement change and other minor issues

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 ===========================================================================
*/
