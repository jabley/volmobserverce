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
import com.volantis.mcs.utilities.FaultTypes;

/**
 * Validator that validates against a set of invalid or valid characters..
 */
public class CharacterSetValidator extends SimpleValidator {

    /**
     * Volantis copyright.
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2003. "; //$NON-NLS-1$

    /**
     * A constraint type for characters that are invalid.
     */
    public static final int INVALID_CHAR_VALIDATION = 0;

    /**
     * A constraint type for character that are valud.
     */
    public static final int VALID_CHAR_VALIDATION = 1;

    /**
     * The default IndividualCharacterValidator.
     */
    private CharacterValidator characterValidator =
            new CharacterValidator() {

                public boolean isValidChar(char c) {
                    boolean valid = false;
                    if (validationChars == null || validationChars.length == 0) {
                        valid = validationType == INVALID_CHAR_VALIDATION;
                    } else {

                        int i;
                        switch (validationType) {
                            case INVALID_CHAR_VALIDATION:
                                for (i = 0; i < validationChars.length &&
                                        validationChars[i] != c; i++)
                                    ;
                                valid = i == validationChars.length;
                                break;
                            case VALID_CHAR_VALIDATION:
                                for (i = 0; i < validationChars.length &&
                                        validationChars[i] != c; i++)
                                    ;
                                valid = i < validationChars.length;
                                break;
                        }
                    }
                    return valid;

                }
            };
    /**
     * The characters constraining this document.
     */
    private char[] validationChars;

    /**
     * The type of constraint.
     */
    private int validationType;

    /**
     * Constructs a new CharacterValidator that operates identically to
     * a SimpleValidator.
     */
    protected CharacterSetValidator() {
    }

    /**
     * Constructs a new CharacterValidator.
     * @param validationChars An array of characters to validate against.
     * @param validationType The type of validation required i.e. validate
     * using validationChars as valid or invalid characters.
     */
    public CharacterSetValidator(char[] validationChars, int validationType) {
        this.validationChars = validationChars;
        this.validationType = validationType;
    }

    /**
     * Constructs a new CharacterValidator.
     * @param characterValidator The CharacterValidator to use when validating
     * characters. Must not be null.
     * @throws IllegalArgumentException If characterValidator is null.
     */
    public CharacterSetValidator(CharacterValidator characterValidator) {
        if (characterValidator == null) {
            throw new IllegalArgumentException("characterValidator is null"); //$NON-NLS-1$
        }

        this.characterValidator = characterValidator;
    }

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: INVALID_CHARACTER; args: object, invalid character, invalid
     * character position.
     */
    // rest of javadoc inherited
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {

        ValidationStatus status = super.validate(object, messageBuilder);

        if (status.getSeverity() == Status.OK) {
            String text = (object != null) ? object.toString() : ""; //$NON-NLS-1$
            int i;
            char c = 0;
            boolean valid = true;
            for (i = 0; i < text.length() && valid; i++) {
                c = text.charAt(i);
                valid = characterValidator.isValidChar(c);
            }

            if (!valid) {
                String faultType = FaultTypes.INVALID_CHARACTER;

                Object formatArgs [] = new Object[3];

                int index = 0;
                formatArgs[index++] = object;
                if (c != 0) {
                    formatArgs[index++] = new Character(c);
                    formatArgs[index] = new Integer(i);
                }

                String message =
                        messageBuilder.buildValidationMessage(faultType,
                                formatArgs);

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

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 10-Oct-03	1512/2	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
