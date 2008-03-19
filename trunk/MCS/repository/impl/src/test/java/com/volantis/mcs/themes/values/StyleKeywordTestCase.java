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

package com.volantis.mcs.themes.values;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSSystemFontKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class StyleKeywordTestCase
 extends TestCaseAbstract {

    public void testEquals() {

        StyleValue kw1 = MCSSystemFontKeywords.CAPTION;
        StyleValue kw2 = MCSSystemFontKeywords.CAPTION;
        assertEquals(kw1, kw2);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/