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
package com.volantis.mcs.eclipse.validation;

import org.eclipse.core.runtime.Status;

import java.io.File;
import java.text.MessageFormat;

/**
 * Class for general validation related utilities.
 */
public class ValidationUtils {
    /**
     * The resource prefix for resource messages associated with this class.
     */
    private static final String RESOURCE_PREFIX = "ValidationUtils.";
    /**
     * A constant valid status.
     */
    private static final ValidationStatus VALID_STATUS =
            new ValidationStatus(Status.OK, "");
    /**
     * Constants for use in specifying the validation for files.
     */
    public static final int FILE_EXISTS = 1;
    public static final int FILE_CAN_READ = 2;
    public static final int FILE_CAN_WRITE = 4;
    public static final int FILE_IS_FILENAME = 8;
    public static final int FILE_IS_FILE = 9; // to be a file it must exist
    public static final int FILE_PARENT_EXISTS = 16;

    /**
     * Do some checks on a file and provide a validation status as the
     * result.
     * @param filename the File.
     * @param flags the flags indicating the validation that is required i.e.
     * {@link #FILE_CAN_READ}, {@link #FILE_CAN_WRITE}, {@link #FILE_EXISTS}.
     * Use arithmetic OR to make combine checks e.g.
     * FILE_CAN_WRITE | FILE_EXISTS will validate that the file exists and is
     * writeable.
     *
     * @return the ValidationStatus of the File based on the checking
     */
    public static ValidationStatus checkFile(String filename, int flags) {
        String message;
        ValidationStatus status = null;
        File file = new File(filename);
        if ((flags & FILE_EXISTS) == FILE_EXISTS) {

            if (!file.exists()) {
                message =
                        ValidationMessages.
                        getString(RESOURCE_PREFIX +
                        "error.doesNotExist");
                String args [] = {filename};
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        if (status == null && (flags & FILE_CAN_READ) == FILE_CAN_READ) {

            if (!file.canRead()) {
                message =
                        ValidationMessages.
                        getString(RESOURCE_PREFIX +
                        "error.cannotRead");
                String args [] = {filename};
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        if (status == null && (flags & FILE_CAN_WRITE) == FILE_CAN_WRITE) {
            File testFile = file;
            if (!testFile.exists()) {
                // If the file does not exist then check the parent directory.
                testFile = testFile.getParentFile() != null ?
                        testFile.getParentFile() : testFile;
            }

            if (testFile.exists() && !testFile.canWrite()) {

                message =
                        ValidationMessages.
                        getString(RESOURCE_PREFIX +
                        "error.cannotWrite");
                String args [] = {filename};
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        if (status == null && (flags & FILE_IS_FILENAME) == FILE_IS_FILENAME) {
            if(file.isDirectory()) {
                message = ValidationMessages.
                        getString(RESOURCE_PREFIX +
                        "error.isNotAFileName");
                String args [] = {filename};
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        if (status == null && (flags & FILE_IS_FILE) == FILE_IS_FILE) {
            // We know the file exists so we don't need to check that.
            if(!file.isFile()) {
                message = ValidationMessages.
                        getString(RESOURCE_PREFIX +
                        "error.isNotAFile");
                String args [] = {filename};
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        if (status == null && (flags & FILE_PARENT_EXISTS) == FILE_PARENT_EXISTS) {
            File testFile = file.getParentFile();

            if (testFile == null || !testFile.exists()) {
                message =
                        ValidationMessages.
                        getString(RESOURCE_PREFIX +
                        "error.pathDoesNotExist");
                String arg = "";
                if (testFile != null) {
                    arg = testFile.getPath();
                }
                String args [] = {arg};
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        return status == null ? VALID_STATUS : status;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Aug-04	5171/4	adrian	VBM:2004070202 Fixed button enablement in Device Repository Import Wizard

 13-Aug-04	5171/1	adrian	VBM:2004070202 Fixed button enablement in Device Repository Import Wizard

 16-Apr-04	3740/6	allan	VBM:2004040508 Rework issues.

 15-Apr-04	3740/4	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 07-Apr-04	3740/1	allan	VBM:2004040508 Make update client available for testing.

 ===========================================================================
*/
