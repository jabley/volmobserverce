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
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.WBDOMWBSAXTestCaseAbstract;
import com.volantis.mcs.wbdom.TestSerialisationConfiguration;
import com.volantis.mcs.wbsax.WBSAXTestData;
import com.volantis.mcs.wbsax.example.SyntheticExampleTestData;

/**
 * A {@link com.volantis.mcs.wbdom.WBDOMWBSAXTestCaseAbstract} which uses the 
 * test data from {@link SyntheticExampleTestData}.
 */ 
public class SyntheticExampleTestCase extends WBDOMWBSAXTestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public SyntheticExampleTestCase(String s) {
        super(s);
    }

    // Inherit Javdoc.
    protected SerialisationConfiguration createConfiguration() {
        TestSerialisationConfiguration conf = 
                new TestSerialisationConfiguration();
        // Register abc as empty, with a dummy code, since this is only
        // testing code.
        conf.registerEmptyElement(-1, "abc");
        return conf;
    }

    // Inherit Javdoc.
    protected WBSAXTestData createTestData()
            throws Exception {
        return new SyntheticExampleTestData();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/3	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC (more documentation)

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 ===========================================================================
*/
