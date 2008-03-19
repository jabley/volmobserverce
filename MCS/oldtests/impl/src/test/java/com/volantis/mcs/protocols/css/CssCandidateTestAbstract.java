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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import junit.framework.TestCase;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Base class for all CSS Candidate tests. 
 */
public abstract class CssCandidateTestAbstract
        extends TestCaseAbstract {

    /**
     * The object to use to create style values.
     */
    protected StyleValueFactory styleValueFactory;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        styleValueFactory = StyleValueFactory.getDefaultInstance();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 ===========================================================================
*/
