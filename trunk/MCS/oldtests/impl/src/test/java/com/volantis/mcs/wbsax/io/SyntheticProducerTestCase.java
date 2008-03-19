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
package com.volantis.mcs.wbsax.io;

import com.volantis.mcs.wbsax.WBSAXTestData;
import com.volantis.mcs.wbsax.WBSAXTestCaseAbstract;
import com.volantis.mcs.wbsax.example.SyntheticExampleTestData;

import java.io.UnsupportedEncodingException;

/**
 * A test case which exercises {@link WMLProducer} and {@link WBXMLProducer}
 * based on test data provided by {@link SyntheticExampleTestData}.
 */ 
public class SyntheticProducerTestCase extends WBSAXTestCaseAbstract {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public SyntheticProducerTestCase(String s) {
        super(s);
    }
    
    // Inherit Javadoc.
    protected WBSAXTestData createTestData()
            throws UnsupportedEncodingException {
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
