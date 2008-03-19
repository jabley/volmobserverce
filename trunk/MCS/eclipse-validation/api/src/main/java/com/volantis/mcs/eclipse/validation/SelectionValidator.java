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

import org.eclipse.core.runtime.Status;

import java.util.Collection;

import com.volantis.mcs.utilities.FaultTypes;

/**
 * A Validator for selections.
 */
public class SelectionValidator
        implements Validator {

    /**
     * Volantis copyright.
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2003. "; //$NON-NLS-1$

    /**
     * The collection of items to validate against.
     */
    private Collection collection = null;

    /**
     * Default validation OK status.
     */
    private static final ValidationStatus VALIDATION_OK =
            new ValidationStatus(Status.OK, null);

    /**
     * Permit empty values to be valid. E.g. an object that toString()'s to
     * an empty string is valid (even if it doesn't exist in the collection).
     * In this case, the validation status should return an INFO status.
     */
    private boolean allowEmptyValues = true;


    /**
     * Construct the object with the list of objects to use in the validation.
     *
     * @param collection the list of object to use in the validation.
     */
    public SelectionValidator(Collection collection) {
        this(collection, true);
    }

    /**
     * Construct the object with the list of objects to use in the validation.
     *
     * @param collection       the list of object to use in the validation.
     * @param allowEmptyValues Permit empty values to be valid.
     */
    public SelectionValidator(Collection collection, boolean allowEmptyValues) {
        this.collection = collection;
        this.allowEmptyValues = allowEmptyValues;
    }

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages.
     *
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: NOT_IN_SELECTION; args: object
     * Fault type: CANNOT_BE_NULL: args: object
     */
    // rest of javadoc inherited
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {

        // Verify that the object exists in the collection.
        ValidationStatus status = null;
        boolean objectIsEmpty = (object == null) ||
                (object.toString().trim().length() == 0);
        if (objectIsEmpty) {
            if (allowEmptyValues) {
                status = VALIDATION_OK;
            } else {
                String faultType = FaultTypes.CANNOT_BE_NULL;
                Object formatArgs [] = new Object[1];
                formatArgs[0] = object;
                String message = messageBuilder.buildValidationMessage(
                        faultType, formatArgs);
                status = new ValidationStatus(Status.INFO, message);
            }
        } else {
            if ((collection == null) || !collection.contains(object)) {
                // ERROR
                String faultType = FaultTypes.NOT_IN_SELECTION;

                Object formatArgs [] = new Object[1];
                formatArgs[0] = object;
                String message = messageBuilder.buildValidationMessage(
                        faultType, formatArgs);
                status = new ValidationStatus(Status.ERROR, message);
            } else {
                // OK
                status = VALIDATION_OK;
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

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Oct-03	1587/1	byron	VBM:2003101503 Create the Device Selector Tree View

 20-Oct-03	1604/3	byron	VBM:2003092302 Implement a validator based on a selection

 20-Oct-03	1604/1	byron	VBM:2003092302 Implement a validator based on a selection

 ===========================================================================
*/
