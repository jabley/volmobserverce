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
import com.volantis.mcs.runtime.configuration.JDBCDriverConfiguration;
import com.volantis.mcs.runtime.configuration.ParameterConfiguration;

/**
 * Define the RuleSet for the JDBCDriver.
 * <p>
 * Note that this class may also define the rule set for pooled jdbcDriver
 * datasources. This is done by setting the prefix to:
 *
 *   xxx + 'connection-pool/jdbc-driver'
 *
 * as opposed to
 *
 *   xxx + 'jdbc-driver'
 *
 * in the Builder.
 */
public class JDBCDriverRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";


    public JDBCDriverRuleSet(String prefix){
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <jdbc-driver>
        final String pattern = prefix + "/jdbc-driver";
        digester.addObjectCreate(pattern,  JDBCDriverConfiguration.class);
        digester.addSetNext(pattern, "setDataSourceConfiguration");
        digester.addSetProperties(
            pattern,
            new String[] {"driver-class", "database-url"},
            new String[] {"driverClass", "databaseURL"}
        );

        final String patternParameter = pattern + "/parameter";
        digester.addObjectCreate(patternParameter, ParameterConfiguration.class);
        digester.addSetNext(patternParameter, "addParameter");
        digester.addSetProperties(
            patternParameter,
            new String[] {"name", "value"},
            new String[] {"name", "value"}
        );

    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/2	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 24-Jun-03	497/1	byron	VBM:2003062302 Issues with Database configuring and sql connector

 13-Jun-03	316/9	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/6	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
