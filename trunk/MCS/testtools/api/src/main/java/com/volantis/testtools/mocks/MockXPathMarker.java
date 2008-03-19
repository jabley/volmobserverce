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
package com.volantis.testtools.mocks;

import org.eclipse.core.runtime.CoreException;

import java.util.Map;
import java.util.HashMap;

import com.volantis.testtools.stubs.MarkerStub;


/**
 * A mock object that emulates set/getAttribute allowing a XPath to
 * be associated with a marker.
 */
public class MockXPathMarker extends MarkerStub {
    /**
     * The type of this marker.
     */
    private String type;

    /**
     * A Map of the attributes associated with this marker.
     */
    private Map attributesMap = new HashMap();

    /**
     * Construct a new MockXPathMarker.
     * @param type The type of the marker.
     */
    public MockXPathMarker(String type) {
        this.type = type;
    }

    // javadoc inherited
    public void setAttribute(String s, Object o) throws CoreException {
        attributesMap.put(s, o);
    }

    // javadoc inherited
    public Object getAttribute(String s) throws CoreException {
        return attributesMap.get(s);
    }

    // javadoc inherited
    public String getType() throws CoreException {
        return type;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
