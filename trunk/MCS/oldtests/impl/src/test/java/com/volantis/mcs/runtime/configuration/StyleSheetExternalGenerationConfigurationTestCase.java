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

package com.volantis.mcs.runtime.configuration;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the holding of configuration information about external stylesheets.
 */
public class StyleSheetExternalGenerationConfigurationTestCase
        extends TestCaseAbstract {

    /**
     * Initialise a new instance of this test case.
     */
    public StyleSheetExternalGenerationConfigurationTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public StyleSheetExternalGenerationConfigurationTestCase(String s) {
        super(s);
    }

    /**
     * Test the set/get base URL methods.
     */
    public void testBaseURL() throws Exception {
        StyleSheetExternalGenerationConfiguration config =
                new StyleSheetExternalGenerationConfiguration();
        String value = "base url";
        config.setBaseUrl(value);
        String result = config.getBaseUrl();
        assertNotNull("Result should not be null", result);
        assertEquals("Initial value and result should match", value, result);
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

 28-Jun-04	4726/4	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
