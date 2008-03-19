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

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.DataSourcesConfiguration;

/**
 * This defines the rules used to read the <data-sources> configuration rules.
 *
 * Other child RuleSets are used, for example NamedDataSource.
 */
public class DataSourcesRuleSet
    extends PrefixRuleSet {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2004. ";

    public DataSourcesRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        // <data-sources>
        final String pattern = prefix + "/data-sources";
        digester.addObjectCreate(pattern, DataSourcesConfiguration.class);
        digester.addSetNext(pattern, "setDataSourcesConfiguration");
        
        // Named Datasource Rule set.
        final NamedDataSourceConfigurationRuleSet ruleSet = 
            new NamedDataSourceConfigurationRuleSet(pattern );
        ruleSet.addRuleInstances(digester);

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
