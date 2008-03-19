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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;

import java.io.File;

import com.volantis.mcs.utilities.FaultTypes;

/**
 * Provide a validator for validating project container file names. These files
 * must be valid files and also cannot be absolute and contain '..' characters.
 */
public class ProjectContainerNameValidator
        extends FilenameValidator {

    /**
     * Collection of reserved characters that may not be used in paths or
     * filenames in windows.  These characters are reserved on the windows
     * platform, however Path.isValidPath()
     * does not return false when these characters are used when running on
     * Windows.  This is almost certainly a bug and extra checking for
     * reserved characters is required so that invalid file names/paths
     * on Windows may not be created from the repository manager gui.
     *
     * File naming conventions on Unix recommend avoiding these characters
     * anyway.
     */
    private static final char[] RESERVED_CHARACTERS = new char[]
        {':', '*', '?', '<', '>', '|', '"'};

    /**
     * An optional project to use for checking full pathnames.
     */
    private IProject project;

    /**
     * Default constructor enforces minumum of 1 character in the file name.
     * @param project the project. Can be null.
     */
    public ProjectContainerNameValidator(IProject project) {
        this.project = project;
        setMinChars(1);
        setStatusOnEmpty(Status.ERROR);
    }

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     *
     * The following fault types are used:
     *
     *  Fault type: NOT_IN_PROJECT; args: object
     *  Fault type: INVALID_DIRECTORY; args: object
     *  Fault type: NOT_WRITEABLE; args: object
     */
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {
        ValidationStatus status = super.validate(object, messageBuilder);
        if (status.isOK()) {
            String faultType = null;
            String name = object.toString();
            if (name != null) {
                File file = new File(name);
                if (file.isAbsolute() || name.startsWith("..")) { //$NON-NLS-1$
                    faultType = FaultTypes.NOT_IN_PROJECT;
                } else if (containsReservedCharacters(name)) {
                    faultType = FaultTypes.INVALID_FILENAME;
                } else {
                    if (project != null) {
                        File fullPath = new File(project.getLocation().toOSString() +
                                File.separatorChar + name);
                        if (fullPath.exists() && fullPath.isFile()) {
                            faultType = FaultTypes.INVALID_DIRECTORY;
                        } else if (fullPath.exists() && !fullPath.canWrite()) {
                            faultType = FaultTypes.NOT_WRITEABLE;
                        }
                    }
                }
            }
            if (faultType != null) {
                String errorMessage = messageBuilder.
                        buildValidationMessage(faultType, new Object[]{object});
                status = new ValidationStatus(Status.ERROR, errorMessage);
            }
        }
        return status;
    }

    /**
     * Returns true if the supplied policySourcePath contains any of
     * the reserved characters contained in {@link #RESERVED_CHARACTERS}
     *
     * @param filePath the file name or path that is to be inspected for
     * reserved characters.
     *
     * @return true if the supplied path contains reserved characters;
     * otherwise false.
     */
    private boolean containsReservedCharacters(String filePath) {

        // test for the presence of any of the reserved characters.
        boolean reservedCharacterFound = false;

        for(int currentCharacter = 0;
            currentCharacter < RESERVED_CHARACTERS.length &&
                !reservedCharacterFound; currentCharacter++) {

            if (filePath.indexOf(
                    RESERVED_CHARACTERS[currentCharacter]) != -1) {
                reservedCharacterFound = true;
            }
        }
        return reservedCharacterFound;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Oct-05	9802/1	rgreenall	VBM:2005092606 Fixed issue where absolute paths were always considered to be invalid.

 03-Oct-05	9614/1	rgreenall	VBM:2005092606 Fixed issue where absolute paths were always considered to be invalid.

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 31-Dec-03	2321/3	pcameron	VBM:2003121807 A few tweaks

 31-Dec-03	2321/1	pcameron	VBM:2003121807 Rework issues and enhanced validation for container name

 22-Dec-03	2273/1	byron	VBM:2003121807 Need to make policy source settable by the user

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 30-Oct-03	1639/5	byron	VBM:2003101602 Create a MCS Project properties page - fixed javadoc

 30-Oct-03	1639/3	byron	VBM:2003101602 Create a MCS Project properties page - addressed requirement change and other minor issues

 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page - addressed various review issues

 ===========================================================================
*/
