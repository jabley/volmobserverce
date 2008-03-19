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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/StyleSheetExternalGenerationRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              StyleSheetExternalGenerationRuleSet.
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.StyleSheetExternalGenerationConfiguration;
import com.volantis.mcs.runtime.configuration.StyleSheetsConfig;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link StyleSheetExternalGenerationRuleSet}.
 */ 
public class StyleSheetExternalGenerationRuleSetTestCase
        extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public StyleSheetExternalGenerationRuleSetTestCase(String s) {
        super(s);
    }
    
    public void testNull() throws ConfigurationException {
        checkStylesheet(null);
    }
    
    public void testEmpty() throws ConfigurationException {
        StylesheetValue value = new StylesheetValue();
        // defaults
        checkStylesheet(value);
    }

    public void testStylesheet() throws ConfigurationException {
        checkStylesheet(null);
        StylesheetValue value = new StylesheetValue();
        // defaults
        value.baseUrl = "bu";
        checkStylesheet(value);
    }

    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link StyleSheetExternalGenerationConfiguration}
     * object, and ensure that the values supplied are match those found.
     * 
     * @param value the example values to use.
     * @throws ConfigurationException if the was a problem.
     */ 
    private void checkStylesheet(StylesheetValue value) 
            throws ConfigurationException {
        
        String doc = ""; 
        if (value != null) {
            doc += "  <style-sheets> \n";
            doc += "    <external-generation \n";
            if (value.baseUrl != null) {
                doc += "      base-url=\"" + value.baseUrl + "\" \n";
            }
            // TODO: 2005081902 get config schema change and remove this
            doc += "      base-directory=\"/dummy\" \n";
            doc += "    /> \n";
            doc += "  </style-sheets> \n";
        }

        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        StyleSheetsConfig styleConfig = config.getStylesheetConfiguration();
        if (value != null) {
            StyleSheetExternalGenerationConfiguration style =
                    styleConfig.getExternalCacheConfiguration();
            assertNotNull("stylesheetConfiguration", style);
            assertEquals(value.baseUrl, style.getBaseUrl());
        } else {
            assertNull("stylesheetConfiguration", styleConfig);
        }
    }

    /**
     * A private Value Object class for holding stylesheet values.
     */ 
    private static class StylesheetValue {
        String baseUrl;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4726/6	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/3	claire	VBM:2004060803 Implementation of internal style sheet caching

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
