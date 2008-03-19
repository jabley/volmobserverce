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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/gui/validation/NumberDocumentTestAbstract.java,v 1.1 2003/02/10 12:42:15 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Nov-02    Allan           VBM:2002110506 - Testcase for NumberDocument.
 * 12-Nov-02    Allan           VBM:2002111206 - Moved from
 *                              com.volantis.mcs.gui to
 *                              com.volanti.mcs.gui.validation.
 * 26-Nov-02    Adrian          VBM:2002111503 - Changed references to
 *                              IntDocument to NumberDocument and updated
 *                              testConstructors, testSetContentObjectInValid
 *                              and testSetContentObjectValid
 * 04-Feb-03    Geoff           VBM:2003012917 - Use new BigIntegerDocument 
 *                              and BigDecimalDocument rather than old 
 *                              NumberDocument, and create tests for them, and 
 *                              to verify that getContentObject returns the 
 *                              right type of number depending on the 
 *                              NumberDocument type, and made Abstract.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.validation;

import org.eclipse.core.runtime.Status;

/**
 * Abstract test case for {@link NumberDocument} and subtypes.
 */
public abstract class NumberValidatorTestAbstract
    extends SimpleValidatorTestCase {

    private ValidationMessageBuilder messageBuilder =
            new ValidationMessageBuilder(null, null, null);

    protected abstract NumberValidator createNumberValidator();

    protected abstract NumberValidator createNumberValidator(String min);

    protected abstract NumberValidator createNumberValidator(String min, String max);
    
    protected abstract void assertEquals(String desc, Number x, double y);
    
    protected abstract void assertInstanceOfNumberValidator(Object x);

    protected abstract void assertInstanceOfNumber(Object x); 

    public void testConstructors() {
        NumberValidator validator = createNumberValidator();
        assertInstanceOfNumberValidator(validator);

        validator = createNumberValidator();
        assertInstanceOfNumberValidator(validator);
        assertNull(validator.minValue);
        assertNull(validator.maxValue);

        validator = createNumberValidator("1");
        assertEquals("minvalue", validator.minValue, 1);
        assertNull(validator.maxValue);

        validator = createNumberValidator("1", "10");
        assertEquals("minValue", validator.minValue, 1);
        assertEquals("maxValue", validator.maxValue, 10);
    }

    /**
     * Test the validate method with invalid content i.e. these
     * tests are successfull only if the content is valid.
     */
    public void testValidateInValid() {
        NumberValidator validator = createNumberValidator();
        ValidationStatus status = validator.validate("hello", messageBuilder);
        assertEquals("\"hello\" is not valid and should have a Status.ERROR" +
                " severity", Status.ERROR, status.getSeverity());

        validator = createNumberValidator("-10");
        status = validator.validate("-11", messageBuilder);
        assertEquals("\"-11\" is not valid (< min)" +
                " and should have a Status.ERROR severity",
                Status.ERROR, status.getSeverity());

        validator = createNumberValidator("0");
        status = validator.validate("-", messageBuilder);
        assertEquals("\"-\" is not valid where minValue>=0" +
                " and should have a Status.ERROR severity",
                Status.ERROR, status.getSeverity());

        validator = createNumberValidator("0", "10");
        status = validator.validate("11", messageBuilder);
        assertEquals("\"11\" is not valid (> max)" +
                " and should have a Status.ERROR severity",
                Status.ERROR, status.getSeverity());;
    }

    /**
     * Test the setContentObject method valid content i.e. these tests 
     * are not successfull if they fail.
     */
    public void testSetContentObjectValid() {
        NumberValidator validator = createNumberValidator();
        ValidationStatus status = validator.validate("1234", messageBuilder);
        assertEquals("Status should be OK for 1234.",
                Status.OK, status.getSeverity());

        validator = createNumberValidator();
        status = validator.validate("-", messageBuilder);
        assertEquals("Status should be OK for -.",
                Status.OK, status.getSeverity());

        validator = createNumberValidator("-10");
        status = validator.validate("-10", messageBuilder);
        assertEquals("Status should be OK for -10.",
                Status.OK, status.getSeverity());

        validator = createNumberValidator("-10");
        status = validator.validate("-", messageBuilder);
        assertEquals("Status should be OK for -10.",
                Status.OK, status.getSeverity());
        
        validator = createNumberValidator("10");
        status = validator.validate("10", messageBuilder);
        assertEquals("Status should be OK for 10.",
                Status.OK, status.getSeverity());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Feb-04	3203/1	ianw	VBM:2004022411 Removed GUI package from MCS

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
