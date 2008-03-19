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

import com.volantis.xml.pipeline.sax.drivers.ConnectionConfigurationImpl;
import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;

import our.apache.commons.digester.Digester;

/**
 * Provides a rule set definition for reading in the ConnectionConfiguration
 * configuration objects.
 *
 * <p>An example of the configuration may look like:</p>
 *
 * <pre>
 * &lt;pipeline-configuration>
 *     &lt;connection timeout="10"/>
 * &lt;/pipeline-configuration>
 * </pre>
 */

public class ConnectionConfigurationRuleSet extends PrefixRuleSet {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param prefix the prefix for the configuration element's XPath
     */
    public ConnectionConfigurationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // Javadoc inherited.
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/connection";
        digester.addObjectCreate(pattern, ConnectionConfigurationImpl.class);
        digester.addSetNext(pattern, "setConnectionConfiguration");
        digester.addSetProperties(pattern,
              new String[]{"timeout", "enable-caching", "max-cache-entries"},
              new String[]{"timeout", "cachingEnabled", "maxCacheEntries"});
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 30-Sep-05	9639/1	philws	VBM:2005092810 Add pipeline connection timeout configuration

 ===========================================================================
*/
