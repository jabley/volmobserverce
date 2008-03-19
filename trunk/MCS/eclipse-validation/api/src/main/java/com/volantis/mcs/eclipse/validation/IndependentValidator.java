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
 * A validator that stores references to a Validator and
 * ValidationMessageBuilder and uses these to create validation messages for
 * an object.
 */
public class IndependentValidator {
    /** The validator used to validate objects */
    private Validator validator;

    /** Message builder to build messages for invalid objects */
    private ValidationMessageBuilder builder;

    /**
     * Create an IndependantValidator. The constructor is passed a Validator
     * and ValidatorMessage neither of which are allowed to be <code>null</code>
     * The Validator will be used to validate objects using its validate method.
     * 
     * @param validator the Validator used to validate objects
     * @param builder the ValidatorMessageBuilder passed to the Validator when
     * objects are validated.
     * @throws IllegalArgumentException if either parameter is <code>null</code>
     * @see Validator
     * @see ValidationMessageBuilder
     */
    public IndependentValidator(Validator validator,
        ValidationMessageBuilder builder) {
        if (validator == null) {
            throw new IllegalArgumentException("Validator cannot be null.");
        }

        if (builder == null) {
            throw new IllegalArgumentException(
                "ValidationMessageBuilder cannot be null.");
        }

        this.validator = validator;
        this.builder = builder;
    }

    /**
     * Validate an object.
     * <p>This is simply a call to the Validator using the
     * ValidationMessageBuilder that were both passed in the constructor.
     * @param o the object to validate
     * @return a <code>ValidationStatus</code> containing the validation
     * message for the object if it is invalid. If the object is valid,
     * <code>null</code> is returned.
     * @see ValidationStatus
     */
    public ValidationStatus validate(Object o) {
        return validator.validate(o, builder);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 17-Nov-03	1897/4	steve	VBM:2003111011 Renamed classes, Changed test case inheritance, Put spaces in the right place.

 15-Nov-03    1897/1    steve    VBM:2003111011 IndependantValidator implementation

 ===========================================================================
*/
