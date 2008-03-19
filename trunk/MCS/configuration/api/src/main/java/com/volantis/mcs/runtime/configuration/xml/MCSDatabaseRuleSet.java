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
import com.volantis.mcs.runtime.configuration.MCSDatabaseConfiguration;

/**
 * Define the RuleSet for the MarinerDatabase.
 * <p>
 * Note that this class may also define the rule set for pooled marinerDatabase
 * datasources. This is done by setting the prefix to:
 *
 *   xxx + 'connection-pool/mcs-database'
 *
 * as opposed to
 *
 *   xxx + 'mcs-database'
 *
 * in the relevant Builder file.
 */
public class MCSDatabaseRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create the MCSDatabaseRuleSet with the specified prefix and method
     * name.
     *
     * @param prefix     the prefix, e.g.
     *                   'mcs-config/pipeline-configuration'
     */
    public MCSDatabaseRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        String pattern = prefix + "/mcs-database";
        digester.addObjectCreate(pattern, MCSDatabaseConfiguration.class);
        digester.addSetNext(pattern, "setDataSourceConfiguration");
        digester.addSetProperties(
            pattern,
            new String[] {"vendor", "source", "host", "port"},
            new String[] {"vendor", "source", "host", "port"}
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

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 24-Jun-03	497/1	byron	VBM:2003062302 Issues with Database configuring and sql connector

 13-Jun-03	316/7	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/5	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
