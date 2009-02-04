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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/StyleSheetConfigurationTestCase.java,v 1.2 2002/12/18 11:22:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Dec-02    Phil W-S        VBM:2002121001 - Created. No tests for simple
 *                              accessor/modifier methods.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This tests the StyleSheetConfiguration class
 */
public class StyleSheetConfigurationTestCase extends TestCaseAbstract {

    public void testNoMaxAge() throws Exception {
        assertEquals("NO_MAX_AGE not as",
                     -1,
                     StyleSheetConfiguration.NO_MAX_AGE);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
