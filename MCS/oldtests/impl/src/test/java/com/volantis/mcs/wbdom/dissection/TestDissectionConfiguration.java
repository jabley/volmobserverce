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

import com.volantis.mcs.wbdom.TestSerialisationConfiguration;
import com.volantis.mcs.wbdom.io.SerialisationConfiguration;

import java.util.ArrayList;

/**
 * A extension of {@link TestSerialisationConfiguration} to allow the 
 * registration and checking of atomic elements. 
 */ 
public class TestDissectionConfiguration 
        extends TestSerialisationConfiguration 
        implements SerialisationConfiguration, AtomicElementConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private ArrayList atomicTokens;
    
    public TestDissectionConfiguration() {
        super();
        atomicTokens = new ArrayList();
    }

    public boolean isElementAtomic(int token) {
        return atomicTokens.contains(new Integer(token));
    }
    
    public void registerAtomicElement(int token) {
        atomicTokens.add(new Integer(token));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/2	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/2	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/6	geoff	VBM:2003070403 first take at cleanup

 30-Jun-03	559/4	geoff	VBM:2003060607 changes to test atomic elements

 27-Jun-03	559/2	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
