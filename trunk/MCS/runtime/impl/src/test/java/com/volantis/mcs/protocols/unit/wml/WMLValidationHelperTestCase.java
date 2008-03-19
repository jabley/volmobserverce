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

package com.volantis.mcs.protocols.unit.wml;

import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.protocols.unit.ValidationHelperTestAbstract;
import com.volantis.mcs.protocols.wml.WMLValidationHelper;

/**
 * This class tests WMLValidationHelper
 */
public class WMLValidationHelperTestCase extends ValidationHelperTestAbstract {

    // Javadoc inherited.
    protected ValidationHelper createValidationHelper() {
        return new WMLValidationHelper();
    }

    public void testAtLeastOneMixedPattern() {
        doTest("M:M*M", "M*M");
    }

    public void testVariableSizedNumericPattern() {
        doTest("N:*#", "*N");
    }

    public void testMixedPattern() {
        doTest("M:mM", "mM");
    }

    public void testDatePattern() {
        doTest("d:DDMMYYYY", "NNNNNNNN");
        doTest("D:DDMMYYYY", "NNNNNNNN");
    }

    public void testFixedSizeNumericPattern() {
        doTest("N:####", "NNNN");
    }

    public void testSymbolPattern() {
        doTest("M:S*S", "n*n");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/4	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/1	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Sep-03	1321/1	adrian	VBM:2003082111 added wcss input validation for xhtmlmobile

 ===========================================================================
*/
