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

import com.volantis.mcs.runtime.configuration.AnonymousDataSourceConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Provide the rule set for anonymous data source configuration objects.
 */
public class AnonymousDataSourceConfigurationRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2004. ";

    public AnonymousDataSourceConfigurationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <named-data-source>
        final String pattern = 
            prefix + "/anonymous-data-source";        
        digester.addObjectCreate(pattern,
                                     AnonymousDataSourceConfiguration.class);
        digester.addSetNext(pattern, "setDataSourceConfiguration");

        digester.addSetProperties(
            pattern,
            new String[] {"user", "password"},
            new String[] {"user", "password"}
        );

     
        // JNDI dataSource
        final JNDIDataSourceRuleSet jndiRuleSet = 
            new JNDIDataSourceRuleSet(pattern);
        jndiRuleSet.addRuleInstances(digester);

        // MCS dataSource 
        final MCSDatabaseRuleSet marinerDataSourceRuleSet =
            new MCSDatabaseRuleSet(pattern);
        marinerDataSourceRuleSet.addRuleInstances(digester);

        // JDBC dataSource 
        final JDBCDriverRuleSet jdbcDriverRuleSet =
            new JDBCDriverRuleSet(pattern);
        jdbcDriverRuleSet.addRuleInstances(digester);

    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
