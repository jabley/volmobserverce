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
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.wbsax.CharsetCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.StringFactory;
import com.volantis.mcs.wbsax.WBSAXString;

/**
 * A test case which exercises {@link DissectableWBDOMCompositeString} - gently!
 * <p>
 * Currently all it does is extend {@link WBDOMStringTestCase} in order to
 * ensure that the composite version of a string behaves the same way as a 
 * normal string.
 * <p>
 * This really needs a lot more testing!
 */ 
public class WBDOMCompositeStringTestCase extends WBDOMStringTestCase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private CharsetCode charset;
    private Codec codec;
    private StringFactory strings;

    public WBDOMCompositeStringTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        charset = new CharsetCode(0x6A, "UTF-8");
        codec = new Codec(charset);
        strings = new StringFactory(codec);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected DissectableString createDissectableString(String string) 
            throws Exception {
        WBSAXString wbsaxString = strings.create(string);
        DissectableWBSAXValueBuffer buffer = new DissectableWBSAXValueBuffer();
        buffer.append(wbsaxString);
        DissectableString dstring = new DissectableWBDOMCompositeString(buffer);
        return dstring;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/1	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/1	geoff	VBM:2003070403 first take at cleanup

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
