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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.eclipse.core.runtime.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the selection validator object.
 */
public class SelectionValidatorTestCase extends TestCaseAbstract {

    protected List invalid;
    protected List valid;
    protected List info;


    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        invalid = new ArrayList();
        invalid.add("Here");
        invalid.add("<(#~))_$)");
        invalid.add("Not a device Name");

        valid = new ArrayList();
        valid.add("Master");
        valid.add("Nokia");
        valid.add("PC");
        valid.add("Unix");

        info = new ArrayList();
        info.add(null);
        info.add("");
        info.add("   ");
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Create a Validator to test. This method should be overridden by
     * sub-classes to provide a Validator of their own type.
     */
    protected Validator createValidator(List list) {
        return createValidator(list, false);
    }

    /**
     * Create a Validator to test. This method should be overridden by
     * sub-classes to provide a Validator of their own type.
     */
    protected Validator createValidator(List list, boolean allowEmptyValue) {
        return new SelectionValidator(list, allowEmptyValue);
    }

    /**
     * Test a null list.
     */
    public void testNullList() throws Exception {

        SelectionValidator validator = (SelectionValidator) createValidator(null);
        doTest(validator, invalid, Status.ERROR);
    }

    private String getMsg(int code) {
        switch(code) {
            case Status.ERROR: return "ERROR";
            case Status.INFO: return "INFO";
            case Status.OK: return "OK";
            case Status.WARNING: return "WARNING";
        }
        return "UKNOWN";
    }

    private void doTest(SelectionValidator validator, List list, int expected) {
        for (int i = 0; i < list.size(); i++) {
            ValidationStatus status =
                    validator.validate(list.get(i),
                            new ValidationMessageBuilder(null, null, null));

            assertEquals("Expected '" + getMsg(expected) + "' but was '" +
                    list.get(i) + "'",
                    expected,
                    status.getSeverity());
        }
    }

    /**
     * Test items that are in a non-null list.
     */
    public void testInList() throws Exception {

        List list = new ArrayList();
        for (int i = 0; i < valid.size(); i++) {
            list.add(valid.get(i));
        }
        list.add("Nokia-7650");
        list.add("Sony-Ericsson");
        doTest((SelectionValidator) createValidator(list), valid, Status.OK);
    }

    /**
     * Test items that are NOT in a non-null list.
     */
    public void testNotInList() throws Exception {

        List list = new ArrayList();
        for (int i = 0; i < valid.size(); i++) {
            list.add(valid.get(i));
        }
        list.add("PC");
        list.add("Unix");
        doTest((SelectionValidator) createValidator(list), invalid, Status.ERROR);
    }

    /**
     * Test items that are in a non-null list and allow empty values is true.
     */
    public void testInfo() throws Exception {
        doTest((SelectionValidator) createValidator(valid, true), info, Status.OK);
    }

    /**
     * Test items that are in a non-null list and allow empty values is true.
     */
    public void testInfoShowInfo() throws Exception {
        doTest((SelectionValidator) createValidator(valid, false), info, Status.INFO);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Oct-03	1587/1	byron	VBM:2003101503 Create the Device Selector Tree View

 20-Oct-03	1604/1	byron	VBM:2003092302 Implement a validator based on a selection

 ===========================================================================
*/
