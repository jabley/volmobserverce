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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.type;

import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.type.constraint.Constraint;

/**
 * Describes a verification error.
 */
public class VerificationError {

    /**
     * Error type for constraint violations.
     */
    public static final ErrorType TYPE_CONSTRAINT_VIOLATION =
        new ErrorType("Constraint violation");

    /**
     * Error type for cases when meta data doesn't implement the expected
     * interface.
     */
    public static final ErrorType TYPE_INVALID_IMPLEMENTATION =
        new ErrorType("Invalid implementation");

    /**
     * Error type for cases when meta data value is null.
     */
    public static final ErrorType TYPE_NULL_VALUE = new ErrorType("Null value");

    /**
     * Error type for cases when the meta data value is not expected.
     */
    public static final ErrorType TYPE_UNEXPECTED_VALUE =
        new ErrorType("Unexpected value");

    /**
     * Error type for cases when choice name is invalid.
     */
    public static final ErrorType CHOICE_NAME_INVALID =
        new ErrorType("Invalid choice name");

    /**
     * Error type for cases when choice name is null.
     */
    public static final ErrorType CHOICE_NAME_NULL =
        new ErrorType("Null choice name");

    /**
     * Type of the error.
     */
    private final ErrorType type;

    /**
     * Path to the invalid meta data value.
     */
    private final String location;

    /**
     * The meta data value that has the problem.
     */
    private final MetaDataValue invalidValue;

    /**
     * The constraint the value violates.
     */
    private final Constraint constraint;

    /**
     * Human readable error message.
     */
    private final String message;

    /**
     * Creates a new error instance.
     *
     * @param type the of the error
     * @param location XPath like path to the invalid meta data value
     * @param invalidValue the invalid meta data value
     * @param constraint the constraint the value violates
     * @param message the human readable error message
     */
    public VerificationError(final ErrorType type, final String location,
                             final MetaDataValue invalidValue,
                             final Constraint constraint, final String message) {
        this.type = type;
        this.location = location;
        this.invalidValue = invalidValue;
        this.constraint = constraint;
        this.message = message;
    }

    /**
     * Returns the type of the error.
     */
    public ErrorType getType() {
        return type;
    }

    /**
     * Returns the location of the invalid meta data value.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the invalid meta data value or null if the error type is
     * {@link #TYPE_NULL_VALUE}.
     */
    public MetaDataValue getInvalidValue() {
        return invalidValue;
    }

    /**
     * Returns the violated constraint or null if the error type is
     * {@link #TYPE_INVALID_IMPLEMENTATION}.
     */
    public Constraint getConstraint() {
        return constraint;
    }

    /**
     * Returns the human readable error message.
     */
    public String getMessage() {
        return message;
    }

    // javadoc inherited
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        final VerificationError error = (VerificationError) other;

        if (constraint == null?
                error.constraint != null: !constraint.equals(error.constraint)) {
            return false;
        }
        if (invalidValue == null?
                error.invalidValue != null: !invalidValue.equals(error.invalidValue)) {
            return false;
        }
        if (!location.equals(error.location)) return false;
        if (!message.equals(error.message)) return false;
        return type.equals(error.type);
    }

    // javadoc inherited
    public int hashCode() {
        int result = type.hashCode();
        result = 29 * result + location.hashCode();
        result = 29 * result + (invalidValue != null ? invalidValue.hashCode() : 0);
        result = 29 * result + (constraint != null ? constraint.hashCode() : 0);
        result = 29 * result + message.hashCode();
        return result;
    }

    /**
     * Type safe enumeration for error types.
     */
    public static class ErrorType {

        /**
         * The name of the error.
         */
        private final String name;

        public ErrorType(final String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
