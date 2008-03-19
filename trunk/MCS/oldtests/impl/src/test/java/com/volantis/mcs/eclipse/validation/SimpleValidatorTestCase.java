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
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for SimpleValidator.
 */
public class SimpleValidatorTestCase extends TestCaseAbstract {

    /**
     * Create a Validator to test. This method should be overridden by
     * sub-classes to provide a Validator of their own type.
     */
    protected Validator createValidator() {
        return new SimpleValidator();
    }

    /**
     * Test min chars validation. Where min chars is more than zero and
     * there are zero chars, then the status should be an INFO rather than
     * an ERROR.
     */
    public void testMinCharsValidation() {
        SimpleValidator validator = (SimpleValidator) createValidator();

        // Default should not have a min setting.
        ValidationStatus status = validator.validate(null,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("There is no min chars setting, text is null " +
                "- should be ok", Status.OK, status.getSeverity());

        validator.setMinChars(2);
        String text = "1";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Min chars is 2, number of chars is 1 - should be " +
                "an error", Status.ERROR, status.getSeverity());


        text = "22";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Min chars is 2, number of chars is 2 - should be " +
                "a ok", Status.OK, status.getSeverity());

        text = "";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Min chars is 2, number of chars is 0 - should be " +
                "an info", Status.INFO, status.getSeverity());

        status = validator.validate(null,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Min chars is 2, text is null - should be " +
                "an info", Status.INFO, status.getSeverity());

        validator.setMinChars(-1); // switch off min chars.text = null;
        status = validator.validate(null,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Min chars is -1, text is null - should be " +
                "ok", Status.OK, status.getSeverity());

    }

    /**
     * Test min chars validation. Where min chars is more than zero and
     * there are zero chars, then the status should be an INFO rather than
     * an ERROR.
     */
    public void testMaxCharsValidation() {
        SimpleValidator validator = (SimpleValidator) createValidator();

        // Default should not have a max setting. So it should run out of
        // memory rather than be in valid but that is a bit complicated to
        // test so just have a string with a few characters instead.
        String text = "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk";
        ValidationStatus status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("There is no max chars setting " +
                "- should be ok", Status.OK, status.getSeverity());

        validator.setMaxChars(1);
        text = "22";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Max chars is 1, number of chars is 2 - should be " +
                "an error", Status.ERROR, status.getSeverity());

        text = "1";
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Max chars is 1, number of chars is 1 - should be " +
                "a ok", Status.OK, status.getSeverity());

        text = null;
        status = validator.validate(text,
                new ValidationMessageBuilder(null, null, null));
        assertEquals("Max chars is 2, text is null - should be " +
                "an ok", Status.OK, status.getSeverity());
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
