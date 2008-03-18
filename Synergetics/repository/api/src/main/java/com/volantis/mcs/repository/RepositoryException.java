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
 * $Header: /src/voyager/com/volantis/mcs/repository/RepositoryException.java,v 1.21 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Apr-01    Paul            Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Added object locked error.
 * 26-Jun-01    Paul            VBM:2001051103 - Added object not locked,
 *                              nested operation sets, object already exists,
 *                              and accessor error error codes.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright.
 * 16-Jul-01    Paul            VBM:2001070508 - Added OBJECT_ALREADY_LOCKED.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 15-Oct-01    Paul            VBM:2001101202 - Fixed a problem with resource
 *                              bundles.
 * 27-Nov-01    Paul            VBM:2001112205 - Added REPOSITORY_TERMINATING
 *                              constant.
 * 13-Dec-01    Allan           VBM:2001112915 - Modified method
 *                              getLocalizedSpecificMessage() to return the
 *                              result of getSpecificException().toString() if
 *                              message is null. Added new message of
 *                              INTEGRITY_CONSTRAINT_VIOLATION.
 * 14-Dec-01    Allan           VBM:2001120504 - Added INVALID_NAME.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 14-Jan-02    Allan           VBM:2002011501 - Fixed bug in
 *                              getLocalizedSpecificMessage() where a
 *                              NullPointerException would be thrown if
 *                              getSpecificMessage() return null.
 * 11-Feb-02    Paul            VBM:2001122105 - Fixed problem with retrieving
 *                              the bundle.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.repository;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ResourceBundle;

/**
 * The RepositoryException class encapsulates exceptions thrown when there
 * is an error in a repository operation.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class RepositoryException
        extends Exception {

    private static String mark = "(c) Volantis Systems Ltd 2001.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RepositoryException.class);

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    RepositoryException.class);

    // Common errors.
    // Repository could not be instantiated.
    // Could not get connection.
    // Could not read version.

    /**
     * An error occurred
     * @deprecated Will be removed in 3.4
     */
    public static final int ERROR = 0;

    /** There is an error in the configuration
     * @deprecated Will be removed in 3.4
     */
    public static final int CONFIGURATION_ERROR = 1;

    /** There was an error during connection
     * @deprecated Will be removed in 3.4
     */
    public static final int CONNECT_ERROR = 2;

    /** There was an authorisation error
     * @deprecated Will be removed in 3.4
     */
    public static final int AUTHORISATION_ERROR = 3;

    /** Access to the repository has been terminated
     * @deprecated Will be removed in 3.4
     */
    public static final int REPOSITORY_TERMINATED = 4;

    /** There was an error during repository terminatino
     * @deprecated Will be removed in 3.4
     */
    public static final int TERMINATION_ERROR = 5;

    /** The requested operation is not supported
     * @deprecated Will be removed in 3.4
     */
    public static final int UNSUPPORTED_OPERATION = 6;

    /** An internal exception occurred
     * @deprecated Will be removed in 3.4
     */
    public static final int INTERNAL_EXCEPTION = 7;

    /** The object does not exist
     * @deprecated Will be removed in 3.4
     */
    public static final int OBJECT_DOES_NOT_EXIST = 8;

    /** The object is locked
     * @deprecated Will be removed in 3.4
     */
    public static final int OBJECT_LOCKED = 9;

    /** The object is not locked
     * @deprecated Will be removed in 3.4
     */
    public static final int OBJECT_NOT_LOCKED = 10;

    /** The request caused a nested operation set
     * @deprecated Will be removed in 3.4
     */
    public static final int NESTED_OPERATION_SETS = 11;

    /** The object already exists
     * @deprecated Will be removed in 3.4
     */
    public static final int OBJECT_ALREADY_EXISTS = 12;

    /** An error occurred within the associated repository accessor
     * @deprecated Will be removed in 3.4
     */
    public static final int ACCESSOR_ERROR = 13;

    /** The object is already locked
     * @deprecated Will be removed in 3.4
     */
    public static final int OBJECT_ALREADY_LOCKED = 14;

    /** Access to the repository is in the process of terminating
     * @deprecated Will be removed in 3.4
     */
    public static final int REPOSITORY_TERMINATING = 15;

    /**
     * The is an integrity constraint violation.
     * @deprecated Will be removed in 3.4
     */
    public static final int INTEGRITY_CONSTRAINT_VIOLATION = 16;

    /**
     * The is an integrity constraint violation.
     * @deprecated Will be removed in 3.4
     */
    public static final int INVALID_NAME = 17;


    /** Constructor
     * @param errorCode The error code for the exception
     * @param args Arguments associated with the exception
     * @param specificKey A repository-specific string giving more details about
     * the error.
     * @param specificArgs An optional set of repository-specific arguments
     * which will be used to format the repository-specific message after it has
     * been translated.
     * @param specificException A repsoitory-specific exception
     *
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public RepositoryException(int errorCode,
                               Object[] args,
                               String specificKey,
                               Object[] specificArgs,
                               Exception specificException) {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Constructor
     * @param errorCode The error code for the exception
     * @param specificKey A repository-specific string giving more details about
     * the error.
     * @param specificArgs An optional set of repository-specific arguments which
     * will be used to format the repository-specific message after it has been
     * translated.
     * @param specificException A repsoitory-specific exception
     *
     *  @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public RepositoryException(int errorCode,
                               String specificKey,
                               Object[] specificArgs,
                               Exception specificException) {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Constructor
     * @param errorCode The error code for the exception
     * @param specificKey A repository-specific string giving more details about
     * the error.
     * @param specificArgs An optional set of repository-specific arguments which
     * will be used to format the repository-specific message after it has been
     * translated.
     *
     *  @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public RepositoryException(int errorCode,
                               String specificKey,
                               Object[] specificArgs) {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Constructor
     * @param errorCode The error code for the exception
     * @param specificKey A repository-specific string giving more details about
     * the error.
     *
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public RepositoryException(int errorCode,
                               String specificKey) {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message   The error message associated with the exception. If
     *                  localization is required this message must be
     *                  pre-localized.
     * @param exception The associated exception
     * @volantis-api-exclude-from PublicAPI
     */
    public RepositoryException(String message,
                               Exception exception) {
        super(message, exception);
    }

    /** Constructor
     * @param exception The associated exception
     * @volantis-api-exclude-from PublicAPI
     */
    public RepositoryException(Exception exception) {
        super(exception);
    }

    /** Constructor
     * @param errorCode The error code for the exception
     * @param args Arguments associated with the exception
     *
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     *
     */
    public RepositoryException(int errorCode,
                               Object[] args) {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Constructor
     * @param errorCode The error code for the exception
     *
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public RepositoryException(int errorCode) {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message The error message associated with the exception. If
     *                localization is required this message must be
     *                pre-localized.
     * @volantis-api-exclude-from PublicAPI
     */
    public RepositoryException(String message) {
        super(message);
    }

    /** Constructor
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public RepositoryException() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /**
     * Get the ResourceBundle associated with this exception.
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public ResourceBundle getBundle() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Return the general message
     * @return The general message
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public String getGeneralMessage() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Retrieve the localised general error message
     * @return The localised general error message
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public String getLocalizedGeneralMessage() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Retrieve the error code
     * @return The error code
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public int getErrorCode() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /**
     * Get the localized specific message for this exception. If this cannot be
     * found then the non-localized specificException toString() is returned.
     * @return the localized specific message for this exception.
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public String getLocalizedSpecificMessage() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Retrieve the arguments
     * @return The arguments
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public Object[] getArgs() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Retrieve the key
     * @return The key
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public String getKey() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Retrieve the repository-specific key
     * @return The repository-specific key
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public String getSpecificKey() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** Retrieve the repository-specific arguments
     * @return The repository-specific arguments
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public Object[] getSpecificArgs() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }

    /** retrieve the repository-specific exception
     * @return The repository-specific exception
     * @deprecated Will be removed in 3.4. Throws UnsupportedOperationException
     */
    public Exception getSpecificException() {
        throw new UnsupportedOperationException(
                EXCEPTION_LOCALIZER.format("removed-from-public-api"));
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 17-Oct-03	1573/1	geoff	VBM:2003101404 XMLDeviceRepositoryAccessor does not validate the xml

 ===========================================================================
*/
