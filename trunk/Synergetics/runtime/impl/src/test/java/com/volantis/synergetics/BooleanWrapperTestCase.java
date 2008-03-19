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
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-May-03    Adrian          VBM:2003051901 - Created this to test 
 *                              BooleanWrapper. BooleanWrapper only really has 
 *                              a setter and getter for a wrapped boolean so 
 *                              this testcase is probably unnecessary but is 
 *                              included for completeness. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics;

import junit.framework.TestCase;

/**
 * This class tests BooleanWrapper.
 */
public class BooleanWrapperTestCase extends TestCase {

    /**
     * Construct a new instance of BooleanWrapperTestCase
     */
    public BooleanWrapperTestCase(String name) {
        super(name);
    }

    /**
     * Test the constructor of BooleanWrapper.
     */
    public void testBooleanWrapper() throws Exception {
        BooleanWrapper wrapper = new BooleanWrapper(false);
        assertTrue("Expected BooleanWrapper to have a false value.",
                   !wrapper.getValue());

        wrapper = new BooleanWrapper(true);
        assertTrue("Expected BooleanWrapper to have a true value.",
                   wrapper.getValue());
    }

    /**
     * Test that method setValue(boolean)
     */
    public void testSetValue() throws Exception {
        BooleanWrapper wrapper = new BooleanWrapper(false);
        wrapper.setValue(true);
        assertTrue("Expected BooleanWrapper to have a true value.",
                   wrapper.getValue());

        wrapper = new BooleanWrapper(true);
        wrapper.setValue(true);
        assertTrue("Expected BooleanWrapper to have a true value.",
                   wrapper.getValue());

        wrapper = new BooleanWrapper(true);
        wrapper.setValue(false);
        assertTrue("Expected BooleanWrapper to have a false value.",
                   !wrapper.getValue());

        wrapper = new BooleanWrapper(false);
        wrapper.setValue(false);
        assertTrue("Expected BooleanWrapper to have a false value.",
                   !wrapper.getValue());
    }

    /**
     * Test the method getValue()
     */
    public void testGetValue() throws Exception {
        BooleanWrapper wrapper = new BooleanWrapper(false);
        wrapper.setValue(true);
        assertTrue("Expected BooleanWrapper to have a true value.",
                   wrapper.getValue());

        wrapper = new BooleanWrapper(true);
        wrapper.setValue(true);
        assertTrue("Expected BooleanWrapper to have a true value.",
                   wrapper.getValue());

        wrapper = new BooleanWrapper(true);
        wrapper.setValue(false);
        assertTrue("Expected BooleanWrapper to have a false value.",
                   !wrapper.getValue());

        wrapper = new BooleanWrapper(false);
        wrapper.setValue(false);
        assertTrue("Expected BooleanWrapper to have a false value.",
                   !wrapper.getValue());
    }
}
