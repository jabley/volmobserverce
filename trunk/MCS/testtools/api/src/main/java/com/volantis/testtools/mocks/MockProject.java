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
package com.volantis.testtools.mocks;

import com.volantis.testtools.stubs.ProjectStub;

import java.util.List;
import java.util.ArrayList;

/**
 * An implementation of ProjectStub that provides useful functionality.
 */
public class MockProject extends ProjectStub {
    /**
     * A list of natures associated with this project.
     */
    private List natures = new ArrayList();

    /**
     * Add a new nature to this project.
     */
    public void addNature(String nature) {
        natures.add(nature);
    }

    /**
     * Override hasNature to use the local natures list.
     */
    // rest of javadoc inherited
    public boolean hasNature(String nature) {
        return natures.contains(nature);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 ===========================================================================
*/
