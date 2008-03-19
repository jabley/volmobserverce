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

import com.volantis.mcs.runtime.configuration.NamedDataSourceConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Provide the rule set for named data source configuration objects.
 *
 * @todo later perhaps this class should be renamed to
 *            NamedDataSourceConfigurationRuleSet
 */
public class NamedDataSourceConfigurationRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";


    public NamedDataSourceConfigurationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <named-data-source>
        final String pattern = prefix + "/named-data-source";        
        digester.addObjectCreate(pattern, NamedDataSourceConfiguration.class);
        digester.addSetNext(pattern, "addNamedDataSource");

        digester.addSetProperties(
            pattern,
            new String[] {"name","connect-on-start-up"},
            new String[] {"name","connectAtStartUp"}
        );

        // JNDI dataSource
        final JNDIDataSourceRuleSet jndiRuleSet = new JNDIDataSourceRuleSet(pattern);
        jndiRuleSet.addRuleInstances(digester);

        // MCS dataSource 
        final MCSDatabaseRuleSet marinerDataSourceRuleSet =
            new MCSDatabaseRuleSet(pattern);
        marinerDataSourceRuleSet.addRuleInstances(digester);

        // JDBC dataSource 
        final JDBCDriverRuleSet jdbcDriverRuleSet =
            new JDBCDriverRuleSet(pattern);
        jdbcDriverRuleSet.addRuleInstances(digester);
        
        // Anonymous dataSource
        final AnonymousDataSourceConfigurationRuleSet 
            anonymousDataSourceConfigurationRuleSet =
                new AnonymousDataSourceConfigurationRuleSet(prefix);
                
        anonymousDataSourceConfigurationRuleSet.addRuleInstances(digester);
        
        // Connection Pool dataSource
        final ConnectionPoolConfigurationRuleSet 
            connectionPoolConfigurationRuleSet =
                new ConnectionPoolConfigurationRuleSet(pattern);
                
        connectionPoolConfigurationRuleSet.addRuleInstances(digester);  
        
        // Reference dataSource
        final DataSourceRuleSet 
            dataSourceConfigurationRuleSet =
                new DataSourceRuleSet(pattern);
                
        dataSourceConfigurationRuleSet.addRuleInstances(digester);                  
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Apr-05	7632/1	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 12-Apr-05	7625/1	doug	VBM:2005041110 Fixed issue with SQLDriver configuration not being read in from MCS config

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 24-Jun-03	497/2	byron	VBM:2003062302 Issues with Database configuring and sql connector

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
