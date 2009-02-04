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
import com.volantis.mcs.integration.iapi.InvokeAttributes;

/**
 * This class tests InvokeAttributes
 */
public class InvokeAttributesTestCase extends TestCaseAbstract {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a new instance of InvokeAttributesTestCase
     * @param name The name of the testcase
     */ 
    public InvokeAttributesTestCase(String name) {
        super(name);
    }

    /**
     * Test the reset method     
     */ 
    public void testReset() throws Exception {
        InvokeAttributes attrs = new InvokeAttributes();
        attrs.setMethodName("initialize");
        attrs.setName("myPlugin");
        
        assertEquals("Unexpected value for methodName.", 
                "initialize", attrs.getMethodName());
        assertEquals("Unexpected value for name.", 
                "myPlugin", attrs.getName());
        
        attrs.reset();
        
        assertNull("methodName attribute should be null.",
                attrs.getMethodName());
        
        assertNull("name should be null.", attrs.getName());
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
