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
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.DevicesConfiguration;
import com.volantis.mcs.runtime.configuration.FileRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.RepositoryDeviceConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case for {@link DevicesRuleSet}.
 */ 
public class DevicesRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Test the <devices>/<standard>/<file-repository> element.
     * 
     * @throws ConfigurationException
     */ 
    public void testStandardFileRepository() throws ConfigurationException {
        String repositoryElement = "<file-repository location=\"devices.mdpr\"/>";
        RepositoryDeviceConfiguration standard = 
                checkStandardDeviceRepository(repositoryElement);

        assertTrue(standard instanceof FileRepositoryDeviceConfiguration);
        FileRepositoryDeviceConfiguration file = 
                (FileRepositoryDeviceConfiguration) standard;
        assertEquals("", "devices.mdpr", file.getLocation());
    }

    /**
     * Test the <devices>/<standard>/<jdbc-repository> element.
     * 
     * @throws ConfigurationException
     */ 
    public void testStandardJdbcRepository() throws ConfigurationException {
        String repositoryElement = "<jdbc-repository project=\"jdbc-project\"/>";
        RepositoryDeviceConfiguration standard = 
                checkStandardDeviceRepository(repositoryElement);

        assertTrue(standard instanceof JDBCRepositoryDeviceConfiguration);
        JDBCRepositoryDeviceConfiguration jdbc = 
                (JDBCRepositoryDeviceConfiguration) standard;
        assertEquals("", "jdbc-project", jdbc.getProject());
    }

    /**
     * Helper method to test <devices>/<standard>/{device-repository} 
     * elements.
     * 
     * @param repositoryElement the repository element text to parse.
     * @return the configuration constructed from the element text passed in.
     * @throws ConfigurationException  
     */ 
    private RepositoryDeviceConfiguration checkStandardDeviceRepository(
            String repositoryElement) throws ConfigurationException {
        String doc = 
                "<devices>\n" +
                "  <standard>\n" +
                "    " + repositoryElement + "\n" +
                "  </standard>\n" +
                "  <logging>\n" +
                "     <log-file>/tmp/devices-log.log</log-file>\n" +
                "     <e-mail>\n" +
                "       <e-mail-sending>disable</e-mail-sending>\n" +
                "     </e-mail>\n" +
                "  </logging>\n" +
                "</devices>\n";
        
        // Parse the XML and create a configuration object tree from it.
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        configBuilder.setAddDefaultDevices(false);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        
        // Check we got back the top level objects.
        assertNotNull(config);
        DevicesConfiguration devices = config.getDevices();
        assertNotNull(devices);

        // Test the default project.
        RepositoryDeviceConfiguration standard = 
                devices.getStandardDeviceRepository();
        assertNotNull(standard);
        return standard;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 ===========================================================================
*/
