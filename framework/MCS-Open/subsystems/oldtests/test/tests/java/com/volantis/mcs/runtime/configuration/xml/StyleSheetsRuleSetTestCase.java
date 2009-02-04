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

package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.StyleSheetsConfig;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;

/**
 * Test the reading of the style sheet configuration.
 */
public class StyleSheetsRuleSetTestCase extends TestCaseAbstract {

    /**
     * Initialise a new instance of this test case.
     */
    public StyleSheetsRuleSetTestCase() {
    }

    /**
     * Initialiase a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public StyleSheetsRuleSetTestCase(String s) {
        super(s);
    }

    public void testNull() throws ConfigurationException {
        checkStyles(null);
    }

    public void testEmpty() throws ConfigurationException {
        StyleSheetsValue value = new StyleSheetsValue();
        checkStyles(value);
    }

    public void testFull() throws ConfigurationException {
        StyleSheetsValue value = new StyleSheetsValue();
        value.pageLevelMaxAge = "3456";
        checkStyles(value);
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link StyleSheetsConfig} object,
     * and ensure that the values supplied are a match those found.
     *
     * @param value The test value to use.
     */
    public void checkStyles(StyleSheetsValue value)
            throws ConfigurationException {
        String doc = "";
        if (value != null) {
            doc += "  <style-sheets> \n";
            doc += "    <external-generation base-directory=\"abc\"/> \n";
            if (value.pageLevelMaxAge != null) {
                doc += "<page-level-generation\n";
                doc += "    max-age=\"" + value.pageLevelMaxAge + "\" \n";
                doc += "/> \n";
            }
            doc += " </style-sheets> \n";

        }

        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        StyleSheetsConfig styleConfig = config.getStylesheetConfiguration();
        if (value != null) {
            assertNotNull("Stylesheet configuration should not be null",
                    styleConfig);
            if (value.pageLevelMaxAge != null) {
                assertEquals("Initial value and retrieved value should match",
                        value.pageLevelMaxAge,
                        styleConfig
                        .getPageLevelCacheConfiguration().getMaxAge());
            }


        } else {
            assertNull("Stylesheet configuration should be null", styleConfig);
        }
    }

    /**
     * A private Value Object class for holding stylesheet config values.
     */
    private class StyleSheetsValue {
        String pageLevelMaxAge;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
