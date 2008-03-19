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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.devrep.repository.impl.devices.policy.types.DefaultIntPolicyType;
import com.volantis.devrep.repository.impl.devices.policy.types.DefaultTextPolicyType;
import com.volantis.mcs.devices.DeviceRepositoryException;

import java.util.ArrayList;
import java.util.List;

/**
 * This tests all DefaultOrderedSetPolicyValue classes.
 */
public class DefaultOrderedSetPolicyValueTestCase
        extends DefaultSetPolicyValueTestAbstract {

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultOrderedSetPolicyValueTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultOrderedSetPolicyValueTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // JavaDoc inherited
    protected List getValueList() {
        int size = keywords.length;
        List valueList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            valueList.add(new DefaultTextPolicyValue(keywords[i]));
        }
        return valueList;
    }

    // JavaDoc inherited
    public String getAsStringValue() {
        StringBuffer buffer = new StringBuffer();
        int size = keywords.length;
        for (int i = 0; i < size; i++) {
            buffer.append(keywords[i]);
            if (i < size -1) {
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }

    // javadoc inherited
    public DefaultPolicyValue getTestInstance() {
        StringBuffer buffer = new StringBuffer();
        int size = keywords.length;
        for (int i = 0; i < size; i++) {
            buffer.append(keywords[i]);
            if (i < size - 1) {
                buffer.append(", ");
            }
        }
        DefaultOrderedSetPolicyValue dospv = null;
        try {
            dospv = new DefaultOrderedSetPolicyValue(
                    new DefaultTextPolicyType(),
                    buffer.toString());
        } catch (DeviceRepositoryException e) {
            fail("A DeviceRepositoryException should not occur.");
        }

        return dospv;
    }

    /**
     * Tests that an ordered set cannot be constructed from invalid values.
     */
    public void testInvalidSetValues() {
        try {
            DefaultOrderedSetPolicyValue value =
                    new DefaultOrderedSetPolicyValue(
                            new DefaultIntPolicyType(),
                            "1,2,3.14259265,4,5");
            fail("A DeviceRepositoryException should have been thrown");
        } catch (DeviceRepositoryException e) {
            // This exception should be thrown.
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Sep-04	5569/2	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/2	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
