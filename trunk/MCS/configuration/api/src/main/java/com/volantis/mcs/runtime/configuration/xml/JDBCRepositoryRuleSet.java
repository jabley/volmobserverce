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

import our.apache.commons.digester.Digester;

import com.volantis.mcs.runtime.configuration.JDBCRepositoryConfiguration;

/**
 * Provide the rule set for jdbc repository configuration objects.

 */
public class JDBCRepositoryRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2004. ";


    public JDBCRepositoryRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <jdbc-repository>
        final String pattern = prefix + "/jdbc-repository";        
        digester.addObjectCreate(pattern, JDBCRepositoryConfiguration.class);
        digester.addSetNext(pattern, "setJDBCRepositoryConfiguration");
        digester.addSetProperties(pattern,
            new String[] {/*"vendor",*/ "use-short-names"},
            new String[] {/*"vendor",*/ "useShortNames"}
        );
        // NOTE: vendor is not implemented at the moment, for some reason?

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
            anonymouseDataSourceConfigurationRuleSet =
                new AnonymousDataSourceConfigurationRuleSet(pattern);
                
        anonymouseDataSourceConfigurationRuleSet.addRuleInstances(digester);
        
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 04-May-04	4023/2	ianw	VBM:2004032302 Added support for short length tables

 26-Apr-04	4045/1	ianw	VBM:2004042605 Fixed Null Pointer with Anonymous datasources

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
