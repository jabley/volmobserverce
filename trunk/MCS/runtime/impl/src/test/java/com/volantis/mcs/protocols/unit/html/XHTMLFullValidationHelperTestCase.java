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

package com.volantis.mcs.protocols.unit.html;

import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.protocols.html.XHTMLFullValidationHelper;
import com.volantis.mcs.protocols.unit.ValidationHelperTestAbstract;

/**
 * Test {@link XHTMLFullValidationHelper}.
 */
public class XHTMLFullValidationHelperTestCase
        extends ValidationHelperTestAbstract {

    // Javadoc inherited.
    protected ValidationHelper createValidationHelper() {
        return new XHTMLFullValidationHelper();
    }

    public void testReplaceUpperM() {
        doTest("M:M*M", "Z*Z");
    }

    public void testReplaceLowerM() {
        doTest("M:m*m", "z*z");
    }

    public void testReplaceBoth() {
        doTest("M:m*M", "z*Z");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
