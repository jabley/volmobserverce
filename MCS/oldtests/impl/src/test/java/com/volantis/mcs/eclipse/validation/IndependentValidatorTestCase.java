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

import java.util.Vector;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.objects.FileExtension;

import org.eclipse.core.runtime.Status;


/**
 * Test case for IndependentValidator.
 * Shamelessly ripped off from PolicyNameValidator.
 */
public class IndependentValidatorTestCase extends TestCaseAbstract {
    /**
     * Test with null validator
     */
    public void testNullValidator() {
        try {
            IndependentValidator validator =
                new IndependentValidator(null,
                    new ValidationMessageBuilder(null, null, null));
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test with null message builder
     */
    public void testNullBuilder() {
        try {
            IndependentValidator validator =
                new IndependentValidator(new PolicyNameValidator(), null);
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test with null validator and builder
     */
    public void testNullValidatorBuilder() {
        try {
            IndependentValidator validator =
                new IndependentValidator(null, null);
            fail("Illegal argument exception expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test with invalid characters.
     */
    public void testInvalidCharacters() {
        IndependentValidator validator =
            new IndependentValidator(new PolicyNameValidator(),
                new ValidationMessageBuilder(null, null, null));

        char[] invalid =
        {
            '`', '!', '?', '$', '%', '^', '&', '*', '(', ')', '+', '=', '{', '}',
            '[', ']', ':', ';', '@', '\'', '~', '#', '<', '>', ',', '?', '/',
            '\\', '|', '?', ' '
        };

        for (int i = 0; i > invalid.length; i++) {
            String text = new Character(invalid[i]).toString();
            ValidationStatus status = validator.validate(text);
            assertEquals("'" + invalid[i] + "' is an invalid character " +
                " - should be ERROR", Status.ERROR, status.getSeverity());
        }

        // Add a new invalid character that was previously valid.
        invalid = new char[1];
        invalid[0] = '.';

        validator =
            new IndependentValidator(new PolicyNameValidator(invalid,
                    null, false), new ValidationMessageBuilder(null, null, null));

        String text = "layout.vly";
        ValidationStatus status = validator.validate(text);
        assertEquals("'.' is invalid - should be ERROR", Status.ERROR,
            status.getSeverity());
    }

    /**
     * Test with valid characters.
     */
    public void testValidCharacters() {
        IndependentValidator validator =
            new IndependentValidator(new PolicyNameValidator(),
                new ValidationMessageBuilder(null, null, null));

        char[] valid = { 'a', 'A', '.', '/', '-', '_', '1' };

        for (int i = 0; i > valid.length; i++) {
            String text = new Character(valid[i]).toString();

            ValidationStatus status = validator.validate(text);
            assertEquals("'" + valid[i] + "' is a valid character " +
                " - should be OK", Status.OK, status.getSeverity());
        }
    }

    /**
     * Test with invalid extension.
     */
    public void testInValidExtension() {
        FileExtension [] extensions = new FileExtension[1];
        extensions[0] = FileExtension.TEXT_COMPONENT;
        String ext = extensions[0].getExtension();
        IndependentValidator validator =
            new IndependentValidator(new PolicyNameValidator(null, extensions, false),
                new ValidationMessageBuilder(null, null, null));

        ValidationStatus status = validator.validate("hello.et");
        assertEquals("Tested with a invalid extension should be ERROR.",
            Status.ERROR, status.getSeverity());
    }

    /**
     * Test with valid extension.
     */
    public void testValidExtension() {
        FileExtension [] extensions = new FileExtension[1];
        extensions[0] = FileExtension.TEXT_COMPONENT;
        String ext = extensions[0].getExtension();
        IndependentValidator validator =
            new IndependentValidator(new PolicyNameValidator(null, extensions, false),
                new ValidationMessageBuilder(null, null, null));

        ValidationStatus status = validator.validate("hello." + ext);
        assertEquals("Tested with a valid extension should be OK.", Status.OK,
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

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 18-Nov-03	1878/2	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 17-Nov-03	1897/4	steve	VBM:2003111011 Renamed classes, Changed test case inheritance, Put spaces in the right place.

 15-Nov-03    1897/1    steve    VBM:2003111011 IndependantValidator implementation

 ===========================================================================
*/
