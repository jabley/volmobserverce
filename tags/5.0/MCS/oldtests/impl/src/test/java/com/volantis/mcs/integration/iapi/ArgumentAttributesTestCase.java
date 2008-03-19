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

package com.volantis.mcs.integration.iapi;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.integration.iapi.ArgumentAttributes;

/**
 * This class tests ArgumentAttributes
 */
public class ArgumentAttributesTestCase extends TestCaseAbstract {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Construct a new instance of ArgumentAttributesTestCase.
     * @param name The name of the testcase.
     */
    public ArgumentAttributesTestCase(String name) {
        super(name);
    }

    /**
     * Test the reset() method resets the member fields to null.     
     */ 
    public void testReset() throws Exception {
        ArgumentAttributes attrs = new ArgumentAttributes();
        attrs.setName("myName");
        attrs.setValue("myValue");
        
        assertEquals("Value expected for name attribute.", 
                "myName", attrs.getName());
        assertEquals("Value expected for value attribute.",
                "myValue", attrs.getValue());
        
        attrs.reset();
        
        assertNull("Null expected for name attribute.", attrs.getName());
        assertNull("Null expected for value attribute.", attrs.getValue());        
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
