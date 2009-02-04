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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.HTTPProxyConfiguration;

/**
 *
 */

public class HTTPProxyRuleSet  extends PrefixRuleSet {


    /**
     * Create the HTTPProxyRuleSet with the specified prefix and method
     * name.
     *
     * @param prefix     the prefix, e.g.
     *                   'mcs-config'
     */
    public HTTPProxyRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        String pattern = prefix + "/http-proxy";
        digester.addObjectCreate(pattern, HTTPProxyConfiguration.class);
        digester.addSetNext(pattern, "setHTTPProxyConfiguration");
        digester.addSetProperties(
            pattern,
            new String[] {"host", "port"},
            new String[] {"host", "port"}
        );
    }
}
