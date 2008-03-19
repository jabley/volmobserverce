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

import java.util.List;

import junitx.util.PrivateAccessor;

/**
 * Test case for the CharacterValidator.
 */
public class CharacterSetValidatorTestCase extends SimpleValidatorTestCase {
    /**
     * Create a Validator to test. This method should be overridden by
     * sub-classes to provide a Validator of their own type.
     */
    protected Validator createValidator() {
        return new CharacterSetValidator();
    }

    /**
     * Test invalid type validation.
     */
    public void testInvalidCharacterValidation() throws Exception {
        CharacterSetValidator validator =
                (CharacterSetValidator) createValidator();

        char[] invalidChars = {'a', 'b', 'c'};
        Integer validationType =
                new Integer(CharacterSetValidator.INVALID_CHAR_VALIDATION);

        PrivateAccessor.setField(validator, "validationChars", invalidChars);
        PrivateAccessor.setField(validator, "validationType", validationType);

        String text = "abc";
        ValidationStatus status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));

        assertEquals("a, b and c are all invalid characters" +
                "should be ERROR", Status.ERROR, status.getSeverity());

        text = "java";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));

        assertEquals("a is an invalid character" +
                "should be ERROR", Status.ERROR, status.getSeverity());

        text = "bottle";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));

        assertEquals("b is an invalid character" +
                "should be ERROR", Status.ERROR, status.getSeverity());

        text = "c";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));

        assertEquals("c is an invalid character" +
                "should be ERROR", Status.ERROR, status.getSeverity());

        text = "hello";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));

        assertEquals("there are no invalid characters" +
                "should be OK", Status.OK, status.getSeverity());

        text = "";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));

        assertEquals("empty string is valid" +
                "should be OK", Status.OK, status.getSeverity());

    }

    /**
     * Test valid type validation.
     */
    public void testValidCharacterValidation() throws Exception {
        CharacterSetValidator validator = (CharacterSetValidator) createValidator();
        char[] validChars = {'a', 'b', 'c'};
        Integer validationType =
                new Integer(CharacterSetValidator.VALID_CHAR_VALIDATION);

        PrivateAccessor.setField(validator, "validationChars",
                validChars);
        PrivateAccessor.setField(validator, "validationType", validationType);

        String text = "abc";
        ValidationStatus status =
                validator.validate(text,
                        new ValidationMessageBuilder(null, null, null));

        assertEquals("a, b and c are all valid characters" +
                "should be OK", Status.OK, status.getSeverity());

        text = "java";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("j and v are invalid characters " +
                "should be ERROR", Status.ERROR, status.getSeverity());

        text = "t";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("t is an invalid character" +
                "should be ERROR", Status.ERROR, status.getSeverity());

        text = "";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("empty string is valid" +
                "should be OK", Status.OK, status.getSeverity());

        text = "*";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("* is an invalid character" +
                "should be ERROR", Status.ERROR, status.getSeverity());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
