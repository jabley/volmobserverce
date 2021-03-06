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
package com.volantis.xml.pipeline.testtools.stubs.sax;

import org.xml.sax.Locator;

/**
 * A stub for org.xml.sax.Locator.
 */ 
public class LocatorStub implements Locator {
    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return null;
    }

    public int getLineNumber() {
        return 0;
    }

    public int getColumnNumber() {
        return 0;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Jun-03	34/1	allan	VBM:2003022820 SQL Connector

 ===========================================================================
*/
