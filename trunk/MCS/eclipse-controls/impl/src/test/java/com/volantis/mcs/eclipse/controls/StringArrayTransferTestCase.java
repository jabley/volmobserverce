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
package com.volantis.mcs.eclipse.controls;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import org.eclipse.swt.dnd.TransferData;

/**
 * Test case for StringArrayTransfer
 */
public class StringArrayTransferTestCase extends TestCaseAbstract {

    /**
     * Test javeToNative() and nativeToJava()
     */
    public void testJavaToNativeNativeToJava() {

        StringArrayTransfer transfer = StringArrayTransfer.getInstance();
        TransferData tData = transfer.getSupportedTypes()[0];
        String [] strings = { "one", "two" };

        transfer.javaToNative(strings, tData);
        String result [] = (String []) transfer.nativeToJava(tData);
        assertEquals("Expected a length of 2.", 2, result.length);
        assertEquals("First String should be \"one\"", "one", result[0]);
        assertEquals("Second String should be \"two\"", "two", result[1]);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Feb-05	6749/3	allan	VBM:2005012102 Rework issues

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 ===========================================================================
*/
