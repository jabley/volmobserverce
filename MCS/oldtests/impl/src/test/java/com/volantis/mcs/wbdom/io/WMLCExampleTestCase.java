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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-May-03    Steve           VBM:2003042917 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbsax.WBSAXTestData;
import com.volantis.mcs.wbsax.example.WMLCExampleTestData;
import com.volantis.mcs.wbdom.TestSerialisationConfiguration;

import java.io.UnsupportedEncodingException;

/**
 * A {@link com.volantis.mcs.wbdom.WBDOMWBSAXTestCaseAbstract} which uses the 
 * test data from {@link WMLCExampleTestData}.
 */ 
public class WMLCExampleTestCase extends WBXMLExpandedExampleTestCase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public WMLCExampleTestCase(String s) {
        super(s);
    }

    protected WBSAXTestData createTestData()
            throws UnsupportedEncodingException {
        return new WMLCExampleTestData();
    }
    
    public void testXMLToXML() throws Exception {
        // disable since the ExampleSAXConsumer can't handle $ variables in
        // the input at the moment.
    }

    protected SerialisationConfiguration createConfiguration() {
        TestSerialisationConfiguration conf = 
                new TestSerialisationConfiguration();
        conf.registerEmptyElement(0x26, "br");
        conf.registerEmptyElement(0x2B, "go");
        conf.registerEmptyElement(0x2F, "input");
        conf.registerUrlAttribute("go", "href");
        return conf;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/1	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 03-Jul-03	709/1	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/3	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
