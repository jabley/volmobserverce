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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for ODOMSelectionFilterConfiguration.
 */
public class ODOMSelectionFilterConfigurationTestCase extends TestCaseAbstract {
    /**
     * Test hashCode.
     */
    public void testHashCodeAndEqauls() {
        ODOMSelectionFilterConfiguration [] configs =
                new ODOMSelectionFilterConfiguration[4];

        configs[0] =
                new ODOMSelectionFilterConfiguration(false, false);
        configs[1] =
                new ODOMSelectionFilterConfiguration(true, false);
        configs[2] =
                new ODOMSelectionFilterConfiguration(false, true);
        configs[3] =
                new ODOMSelectionFilterConfiguration(true, true);

        for(int i1=0; i1<4; i1++) {
            for(int i2=0; i2<4; i2++) {
                if(i1==i2) {
                    assertEquals(configs[i1], configs[i1]);
                } else {
                    assertNotEquals(configs[i1], configs[i2]);
                }
            }
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

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 ===========================================================================
*/
