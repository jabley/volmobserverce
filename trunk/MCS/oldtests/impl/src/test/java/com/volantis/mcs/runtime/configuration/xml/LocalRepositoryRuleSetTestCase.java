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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/LocalRepositoryRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              LocalRepositoryRuleSet. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MCSDatabaseConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.LocalRepositoryConfiguration;
import com.volantis.mcs.runtime.configuration.XMLRepositoryConfiguration;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link LocalRepositoryRuleSet}. 
 */ 
public class LocalRepositoryRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public LocalRepositoryRuleSetTestCase(String s) {
        super(s);
    }

    public void testNull() 
            throws ConfigurationException {
        checkLocalRepository(null);
    }

    public void testJdbc() 
            throws ConfigurationException {
        LocalRepositoryValue value = new LocalRepositoryValue();

        // optional
        value.type = "jdbc";
        // following are mandatory from dtd if type = value
        // or more strictly, all are required if any are specified
        value.vendor = "hypersonic";
        value.host = "h";
        value.port = new Integer(8080);
        value.source = "s";
        value.shortNames = Boolean.TRUE;
        checkLocalRepository(value);

    }

    public void testXml() 
            throws ConfigurationException {
        checkLocalRepository(null);
        LocalRepositoryValue value = new LocalRepositoryValue();
        value.type = "xml";
        checkLocalRepository(value);
    }
    
    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link LocalRepositoryConfiguration} object, 
     * and ensure that the values supplied are match those found.
     * 
     * @param value
     * @throws ConfigurationException
     */ 
    public void checkLocalRepository(LocalRepositoryValue value) 
            throws ConfigurationException {
        String doc = ""; 
        if (value != null) {
            doc += "  <local-repository> \n";
            if ("jdbc".equals(value.type)) {
                doc += "    <jdbc-repository";
                if (value.shortNames != null) {
                    doc += "      use-short-names=\"" + value.shortNames + "\" \n";
                }
                doc += "    >\n";

                doc += "      <mcs-database ";
				if (value.vendor != null) {
					doc += "      vendor=\"" + value.vendor + "\" \n";
				}                
                if (value.host != null) {
                    doc += "      host=\"" + value.host + "\" \n";
                }
                if (value.port != null) {
                    doc += "      port=\"" + value.port + "\" \n";
                }
                if (value.source != null) {
                    doc += "      source=\"" + value.source + "\" \n";
                }

                doc += "    /> \n";
                doc += "  </jdbc-repository>\n";                
                
            }
            if ("xml".equals(value.type)) {
                doc += "    <xml-repository/> \n";
            }
            doc += "  </local-repository> \n";
        } else {
            doc += "  <local-repository/> \n";
        }
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        configBuilder.setAddDefaultLocalRepository(false);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        LocalRepositoryConfiguration repos = config.getLocalRepository();
        assertNotNull("localRepositoryConnection", repos);
        if (value != null) {
			if ("jdbc".equals(value.type)) {
                final JDBCRepositoryConfiguration jdbcRepositoryConfig =
                        repos.getJDBCRepositoryConfiguration();
                assertEquals("", value.shortNames, jdbcRepositoryConfig.getUseShortNames());
                MCSDatabaseConfiguration ds = (MCSDatabaseConfiguration)
                        jdbcRepositoryConfig.getDataSourceConfiguration();
            	assertEquals(value.vendor, ds.getVendor());
            	assertEquals(value.host, ds.getHost());
            	assertEquals(value.port, ds.getPort());
            	assertEquals(value.source, ds.getSource());
			} else if ("xml".equals(value.type)) {
				XMLRepositoryConfiguration xmlConfig =
					repos.getXmlRepository();
				assertNotNull(xmlConfig);
			}

        }
    }
    
    /**
     * A private Value Object class for holding local repository values.
     */ 
    private class LocalRepositoryValue {
        String type;
        String vendor;
        String host;
        Integer port;
        String source;
        Boolean shortNames;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
