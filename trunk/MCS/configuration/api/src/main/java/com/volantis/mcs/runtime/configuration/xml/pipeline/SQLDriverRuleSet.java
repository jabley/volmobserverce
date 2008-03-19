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

package com.volantis.mcs.runtime.configuration.xml.pipeline;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.pipeline.SQLDriverConfiguration;
import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;
import com.volantis.mcs.runtime.configuration.xml.DataSourcesRuleSet;

/**
 * This defines the rules used to read the <sql-driver> configuration rules.
 *
 * Other child RuleSets are used, for example JDBCDriverRuleSet.
 */
public class SQLDriverRuleSet
    extends PrefixRuleSet {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Initalizes a <code>SQLDriverRuleSet</code> instance
     * @param prefix the prefix
     */
    public SQLDriverRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        // <sql-driver>
        final String pattern = prefix + "/sql-driver";
        digester.addObjectCreate(pattern, SQLDriverConfiguration.class);
        digester.addSetNext(pattern, "setSQLDriverConfiguration");

        DataSourcesRuleSet dataSourcesSet =
           new DataSourcesRuleSet(pattern);
        dataSourcesSet.addRuleInstances(digester);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/3	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 30-Jun-03	629/1	philws	VBM:2003062508 Rename sql-connector to sql-driver

 24-Jun-03	497/1	byron	VBM:2003062302 Issues with Database configuring and sql connector

 13-Jun-03	316/3	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/1	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
