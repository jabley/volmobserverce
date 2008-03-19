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
package com.volantis.xml.pipeline.testtools.mock;

import com.volantis.xml.pipeline.testtools.stubs.sax.AttributesStub;

import java.util.HashMap;

/**
 * A generally useful mock Attributes. Only some of the methods are currently
 * overriden. Those that are not will not pruduce useful results.
 */
public class MockAttributes extends AttributesStub {

    private HashMap values = new HashMap();

    /**
     * Override get value to return the a value.
     * @param name The name of the value.
     * @return The value.
     */
    public String getValue(String name) {
        return (String) values.get(name);
    }

    /**
     * Set a value in these attributes.
     * @param name The name of the value.
     * @param value The value.
     */
    public void setValue(String name, String value) {
        values.put(name, value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-03	165/1	allan	VBM:2003070101 Fix bug in MessageOperationProcess.startElement()

 ===========================================================================
*/
