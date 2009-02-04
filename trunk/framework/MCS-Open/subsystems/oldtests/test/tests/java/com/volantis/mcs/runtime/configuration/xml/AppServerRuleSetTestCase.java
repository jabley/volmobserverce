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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/AppServerRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              AppServerRuleSet. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.AppServerConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link AppServerRuleSet}. 
 */ 
public class AppServerRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public AppServerRuleSetTestCase(String s) {
        super(s);
    }
    
    public void testNull() throws ConfigurationException {
        checkAppServer(null);
    }

    public void testEmpty() throws ConfigurationException {
        AppServerValue value = new AppServerValue();
        // defaults
        value.useServerConnectionPool = Boolean.TRUE;
        value.anonymous = Boolean.FALSE;
        checkAppServer(value);
    }

    public void testFull() throws ConfigurationException {
        AppServerValue value = new AppServerValue();
        // defaults
        value.useServerConnectionPool = Boolean.TRUE;
        value.anonymous = Boolean.FALSE;
        // optional
        value.appServerName = "app server";
        value.baseUrl = "bu";
        value.datasource = "ds";
        value.datasourceVendor = "dsv";
        value.internalUrl = "iu";
        value.jndiProvider = "jp";
        value.pageBase = "pb";
        value.user = "user";
        value.password = "password";
        checkAppServer(value);
    }
    
    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link AppServerConfiguration} object, 
     * and ensure that the values supplied are match those found.
     * 
     * @param value
     * @throws ConfigurationException
     */ 
    public void checkAppServer(AppServerValue value) 
            throws ConfigurationException {
        String doc = ""; 
        if (value != null) {
            doc += "  <web-application \n";
            if (value.baseUrl != null) {
                doc += "    base-url=\"" + value.baseUrl + "\" \n";
            }
            if (value.internalUrl != null) {
                doc += "    internal-url=\"" + value.internalUrl + "\" \n"; 
            }
            if (value.pageBase != null) {
                doc += "    page-base=\"" + value.pageBase + "\" \n"; 
            }
            if (value.appServerName != null) {
                doc += "    app-server-name=\"" + value.appServerName + 
                        "\" \n"; 
            }
            if (value.useServerConnectionPool != null) {
                doc += "    use-server-connection-pool=\"" + 
                        value.useServerConnectionPool + "\" \n"; 
            }
            if (value.jndiProvider != null) {
                doc += "    jndi-provider=\"" + value.jndiProvider + "\" \n"; 
            }
            if (value.datasourceVendor != null) {
                doc += "    datasource-vendor=\"" + value.datasourceVendor + 
                        "\" \n"; 
            }
            if (value.datasource != null) {
                doc += "    datasource=\"" + value.datasource + "\" \n"; 
            }
            if (value.user != null) {
                doc += "    user=\"" + value.user + "\" \n"; 
            }
            if (value.password != null) {
                doc += "    password=\"" + value.password + "\" \n"; 
            }
            if (value.anonymous != null) {
                doc += "    anonymous=\"" + value.anonymous + "\" \n"; 
            }
            doc += "  /> \n";
        }
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        AppServerConfiguration appServer = config.getAppServer();
        if (value != null) {
            assertNotNull("appServerConfiguration", appServer);
            assertEquals(value.baseUrl, appServer.getBaseUrl());
            assertEquals(value.internalUrl, appServer.getInternalUrl());
            assertEquals(value.pageBase, appServer.getPageBase());
            assertEquals(value.appServerName, appServer.getAppServerName());
            assertEquals(value.useServerConnectionPool, appServer.getUseServerConnectionPool());
            assertEquals(value.jndiProvider, appServer.getJndiProvider());
            assertEquals(value.datasourceVendor, appServer.getDatasourceVendor());
            assertEquals(value.datasource, appServer.getDatasource());
            assertEquals(value.user, appServer.getUser());
            assertEquals(value.password, appServer.getPassword());
            assertEquals(value.anonymous, appServer.getAnonymous());
        } else {
            assertNull("appServerConfiguration", appServer);
        }
    }

    /**
     * A private Value Object class for holding application server values.
     */ 
    private static class AppServerValue {
        String baseUrl;
        String internalUrl;
        String pageBase;
        String appServerName;
        Boolean useServerConnectionPool;
        String jndiProvider;
        String datasourceVendor;
        String datasource;
        String user;
        String password;
        Boolean anonymous;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 ===========================================================================
*/
