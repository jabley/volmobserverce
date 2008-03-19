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
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.ManagementConfiguration;

/**
 * Test case for ManagementRuleSet. Tests to ensure that ManagmentConfiguration
 * objects are created/not created as is appropriate.
 */
public class ManagementRuleSetTestCase extends TestCaseAbstract {

    /**
     * Standard test case constructor.
     */
    public ManagementRuleSetTestCase(String name) {
        super(name);
    }

    /**
     * Test that the managementConfiguration is constructed when the xml
     * element is there.
     * @throws Exception
     */
    public void testExists() throws Exception {
        ManagementValue value = new ManagementValue();
        value.pageTracking = Boolean.TRUE;
        ManagementConfiguration management = buildConfiguration(value);
        assertNotNull(management);
        assertNotNull(management.getPageTrackingConfiguration());
    }

    /**
     * Ensure that the ManagementConfiguration object is not constructed when
     * the xml elements do not exist.
     * @throws Exception
     */
    public void testDoesNotExist() throws Exception {
        ManagementValue value = new ManagementValue();
        ManagementConfiguration management = buildConfiguration(null);
        assertNull(management);
    }

    /**
     * Helper method that performs some of the grunt work.
     * @param value object containing the management values you wish to use
     * in the test
     * @return a ManagementConfiguration obejct or null if the <management>
     * elements do not exist.
     * @throws Exception
     */
    private ManagementConfiguration buildConfiguration(ManagementValue value) throws Exception {
        String doc = "";
        if (value != null) {
            doc += "  <management> \n";
            doc += "      <page-tracking " +
                             "enabled=\""+value.pageTracking+"\" \n";
            doc += "  /> \n";
            doc += "  </management>\n";
        }

        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        return config.getManagementConfiguration();
    }

    /**
     * private class for passing around Management values.
     */
    private class ManagementValue {

        Boolean pageTracking = null;


    }



}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
