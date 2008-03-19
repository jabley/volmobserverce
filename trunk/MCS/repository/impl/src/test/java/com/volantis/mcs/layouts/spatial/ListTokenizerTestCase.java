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

package com.volantis.mcs.layouts.spatial;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test that the list tokenizer works correctly.
 */
public class ListTokenizerTestCase
        extends TestCaseAbstract {

    /**
     * Test the extraction of tokens into an array.
     */
    public void testExtractTokens() throws Exception {
        ListTokenizer listTokenizer = new ListTokenizer();

        String styleClasses = null;
        String[] result = listTokenizer.extractTokens(styleClasses);
        assertNull("Result should be null", result);

        styleClasses = "";
        result = listTokenizer.extractTokens(styleClasses);
        assertNull("Result should be null", result);

        styleClasses = "   ";
        result = listTokenizer.extractTokens(styleClasses);
        assertNull("Result should be null", result);

        styleClasses = "sc1";
        result = listTokenizer.extractTokens(styleClasses);
        assertNotNull("Result shouldn't be null", result);
        assertEquals("Token count should match", 1, result.length);
        assertEquals("Token 1 should match", "sc1", result[0]);

        styleClasses = "sc1 sc2 sc3";
        result = listTokenizer.extractTokens(styleClasses);
        assertNotNull("Result shouldn't be null", result);
        assertEquals("Token count should match", 3, result.length);
        assertEquals("Token 1 should match", "sc1", result[0]);
        assertEquals("Token 2 should match", "sc2", result[1]);
        assertEquals("Token 3 should match", "sc3", result[2]);

        styleClasses = " sc1 sc2   sc3 sc4-59035-59    ";
        result = listTokenizer.extractTokens(styleClasses);
        assertNotNull("Result shouldn't be null", result);
        assertEquals("Token count should match", 4, result.length);
        assertEquals("Token 1 should match", "sc1", result[0]);
        assertEquals("Token 2 should match", "sc2", result[1]);
        assertEquals("Token 3 should match", "sc3", result[2]);
        assertEquals("Token 3 should match", "sc4-59035-59", result[3]);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 ===========================================================================
*/
