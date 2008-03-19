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
/* (c) Volantis Systems Ltd 2003.  */

package com.volantis.testtools.config;

import com.volantis.mcs.testtools.application.DefaultAppConfigurator;
import com.volantis.mcs.testtools.application.MandatoryAppConfigurator;
import com.volantis.synergetics.testtools.HypersonicManager;

/**
 * Application configurator for MPS.
 * 
 * @author mat
 *
 */
public class MPSAppConfigurator extends MandatoryAppConfigurator {

    // The name of the hypersonic database used throughout the tests.
    public static final String SOURCE = "hypersonic-db";
    
    public PluginConfigValue value;
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // Inherit Javadoc.
    public void setUp(ConfigValue config) throws Exception {

        super.setUp(config);
        

            config.repositoryType = "odbc";
            config.repositoryUser = "sa";
            config.repositoryPassword = "";
            config.repositoryVendor = "hypersonic";
            config.repositorySource = SOURCE;
            config.repositoryHost = "haddock";
            config.repositoryPort = new Integer( 1526 );
            
            ConfigProjectPoliciesJdbcValue jdbcPolicies = 
                    new ConfigProjectPoliciesJdbcValue();
            jdbcPolicies.projectName = "#DefaultProject";
            config.defaultProjectPolicies = jdbcPolicies;
            config.standardJDBCDeviceRepositoryProject = 
                    jdbcPolicies.projectName;
            
            config.sessionProxyCookieMappingEnabled = Boolean.FALSE;

            // Dont know why these are needed but we crash without them.
            config.repositoryDbPoolMax = new Integer(999);
            config.repositoryKeepConnectionsAlive = Boolean.TRUE;
            config.repositoryConnectionPollInterval = new Integer(60);
        
        
        config.pluginConfigValues.add(value);
    }
    
    public void setPluginConfigValue(PluginConfigValue value) {
        this.value = value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 27-Oct-03	45/3	mat	VBM:2003101502 Fix MessageRecipient(s) testcases

 23-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
