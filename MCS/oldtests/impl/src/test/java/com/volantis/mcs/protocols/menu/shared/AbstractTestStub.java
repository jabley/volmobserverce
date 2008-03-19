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
package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.shared.TestStub;

/**
 * Default implementation of {@link com.volantis.mcs.protocols.menu.shared.TestStub}. 
 */ 
public class AbstractTestStub implements TestStub {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The unique identifier for this stub, or null if it is anonymous.
     */ 
    private String stubId;

    /**
     * Create an anonymous test stub. Any attempt to get the id of this stub
     * will throw an exception.
     */ 
    public AbstractTestStub() {
    }

    /**
     * Create an identified test stub. Any attempt to get the id of this stub
     * will return the id provided.
     * 
     * @param stubId
     */ 
    public AbstractTestStub(String stubId) {
        if (stubId == null) {
            throw new IllegalArgumentException(
                    "Attempt to create an identified stub with a null id");
        }
        this.stubId = stubId;
    }

    // Javadoc inherited.
    public String getStubId() {
        if (stubId == null) {
            throw new UnsupportedOperationException(
                    "Attempt to identify an anonymous test stub");
        }
        return stubId;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Apr-04	3920/4	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
