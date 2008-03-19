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
import org.eclipse.core.runtime.Status;

/**
 * Test case for PolicyNameValidator.
 */
public class PolicyNameValidatorTestCase extends SimpleValidatorTestCase {

    /**
     * Create a Validator to test. This method should be overridden by
     * sub-classes to provide a Validator of their own type.
     */
    protected Validator createValidator() {
        return new PolicyNameValidator();
    }

    /**
     * Test with invalid characters.
     */
    public void testInvalidCharacters() {
        PolicyNameValidator validator =
                (PolicyNameValidator) createValidator();

        char invalid [] = {'`', '!', '?', '$', '%', '^', '&', '*', '(', ')',
                           '+', '=', '{', '}', '[', ']', ':', ';', '@',
                           '\'', '~', '#', '<', '>', ',', '?', '/', '\\',
                           '|', '?', ' '};

        for (int i = 0; i > invalid.length; i++) {
            String text = new Character(invalid[i]).toString();

            ValidationStatus status =
                    validator.validate(text,
                            new ValidationMessageBuilder(null, null, null));

            assertEquals("'" + invalid[i] + "' is an invalid character " +
                    " - should be ERROR", Status.ERROR, status.getSeverity());
        }

        // Add a new invalid character that was previously valid.
        invalid = new char[1];
        invalid[0] = '.';

        validator = new PolicyNameValidator(invalid, null, false);
        String text = "layout." + FileExtension.LAYOUT.getExtension();
        ValidationStatus status =
                validator.validate(text,
                        new ValidationMessageBuilder(null, null, null));
        assertEquals("'.' is invalid - should be ERROR", Status.ERROR,
                status.getSeverity());
    }

    /**
     * Test with valid characters.
     */
    public void testValidCharacters() {
        PolicyNameValidator validator =
                (PolicyNameValidator) createValidator();

        char valid [] = {'a', 'A', '.', '/', '-', '_', '1'};

        for (int i = 0; i > valid.length; i++) {
            String text = new Character(valid[i]).toString();

            ValidationStatus status =
                    validator.validate(text,
                            new ValidationMessageBuilder(null, null, null));

            assertEquals("'" + valid[i] + "' is a valid character " +
                    " - should be OK", Status.OK, status.getSeverity());
        }
    }


    /**
     * Test with valid characters.
     */
    public void testInValidExtension() {
        FileExtension [] extensions = new FileExtension[1];
        extensions[0] = FileExtension.TEXT_COMPONENT;
        PolicyNameValidator validator = new PolicyNameValidator(null,
                extensions, false);

        ValidationStatus status =
                validator.validate("hello.et",
                        new ValidationMessageBuilder(null, null, null));

        assertEquals("Tested with a invalid extension should be ERROR.",
                Status.ERROR,
                status.getSeverity());
    }

    /**
     * Test with single extension.
     */
    public void testValidExtension() {
        FileExtension [] extensions = new FileExtension[1];
        extensions[0] = FileExtension.TEXT_COMPONENT;
        PolicyNameValidator validator = new PolicyNameValidator(null,
                extensions, false);

        ValidationStatus status =
                validator.validate("hello." +
                                   FileExtension.TEXT_COMPONENT.getExtension(),
                        new ValidationMessageBuilder(null, null, null));

        assertEquals("Tested with a valid extension should be OK.",
                Status.OK,
                status.getSeverity());
    }

    /**
     * Test with multiple extensions.
     */
    public void testValidExtensions() {
        FileExtension [] extensions = new FileExtension[2];
        extensions[0] = FileExtension.IMAGE_COMPONENT;
        extensions[1] = FileExtension.AUDIO_COMPONENT;
        PolicyNameValidator validator =
                new PolicyNameValidator(null, extensions, false);

        ValidationStatus status;
        ValidationMessageBuilder vmb =
                new ValidationMessageBuilder(null, null, null);
        status =
                validator.validate(
                        "hello." + FileExtension.AUDIO_COMPONENT.getExtension(),
                        vmb);

        assertEquals(
                "Tested.1 with a valid extension should be OK.",
                Status.OK,
                status.getSeverity());

        status =
                validator.validate("i." +
                                   FileExtension.IMAGE_COMPONENT.getExtension(), vmb);

        assertEquals(
                "Tested.2 with a valid extension should be OK.",
                Status.OK,
                status.getSeverity());

        status =
                validator.validate(
                        "hellothisislong." + FileExtension.LAYOUT.getExtension(),
                        vmb);

        assertEquals(
                "Tested.3 with an invalid extension should FAIL.",
                Status.ERROR,
                status.getSeverity());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Feb-04	3068/3	allan	VBM:2004021115 Validate fallback extensions in wizards.

 12-Feb-04	2962/3	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 27-Nov-03	2013/2	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 18-Nov-03	1878/7	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 17-Nov-03	1878/4	richardc	VBM:2003110901 No extensions IS allowed + updated JUnit

 14-Nov-03	1878/2	richardc	VBM:2003110901 VBM 2003110901

 20-Oct-03	1502/3	allan	VBM:2003092202 Add extension tests.

 20-Oct-03	1502/1	allan	VBM:2003092202 Completed validation for PolicySelector.

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
