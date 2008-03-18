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

import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.policy.values.SimplePolicyValue;

/**
 * This tests all DefaultStructurePolicyValue classes.
 */
public class DefaultStructurePolicyValueTestCase
        extends DefaultCompositePolicyValueTestAbstract {
    protected static final String[] keys = new String[] {
        "key 1", "key 2", "key 3", "key 4"
    };

    /**
     * An array of SimplePolicyValues used for testing.
     */
    protected static final SimplePolicyValue[] values;

    static {
        values = new SimplePolicyValue[]{
            new DefaultBooleanPolicyValue("true"),
            new DefaultTextPolicyValue("Hello"),
            null,
            new DefaultTextPolicyValue("Goodbye")
        };
        DefaultIntPolicyValue dipv = null;
        try {
            dipv = new DefaultIntPolicyValue("100");
        } catch (DeviceRepositoryException e) {
            // Safe to catch since exception can't occur with valid integer.
        }
        values[2] = dipv;
    }

    /**
     * Initialise a new instance of this test case.
     */
    public DefaultStructurePolicyValueTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public DefaultStructurePolicyValueTestCase(String s) {
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

    public void testGetAsString() throws Exception {
        // This (as implemented higher in the hierarchy) does not make sense
        // here as it cannot be guaranteed which order the key=value pairs
        // will be processed as they are put into a hash map.
    }

    // JavaDoc inherited
    public String getAsStringValue() {
        int size = keys.length;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i ++) {
            buffer.append("[");
            buffer.append(keys[i]);
            buffer.append("=");
            buffer.append(values[i].getAsString());
            buffer.append("]");
            if (i < size - 1) {
                buffer.append(" ");
            }
        }
        return buffer.toString();
    }

    // JavaDoc inherited
    public DefaultPolicyValue getTestInstance() {
        DefaultStructurePolicyValue value = new DefaultStructurePolicyValue();
        int size = keys.length;
        for (int i = 0; i < size; i++) {
            value.addField(keys[i], values[i]);
        }
        value.complete();
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5755/1	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 21-Sep-04	5569/1	pcameron	VBM:2004091719 NumberFormatException handled within device repository PAPI

 20-Sep-04	5563/1	pcameron	VBM:2004091719 NumberFormatExcetion handled within device repository PAPI

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
