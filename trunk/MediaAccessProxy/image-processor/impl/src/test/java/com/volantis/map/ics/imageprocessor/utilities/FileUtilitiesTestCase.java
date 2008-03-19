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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.utilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

public class FileUtilitiesTestCase extends TestCase {


    public void testGetExtension() {
        File file = new File("test.svg");

        String expectedExtension = "svg";
        String actualExtension = FileUtilities.getExtension(file);
        String message = "Extensions are not the same";
        assertEquals(message, expectedExtension, actualExtension);
    }


    public static Test suite() {
        return new TestSuite(FileUtilitiesTestCase.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Mar-05	311/1	rgreenall	VBM:2005012701 Calling writeImage in concrete implementations of AbstractImageWriter no longer results in the image being converted twice.

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 ===========================================================================
*/
