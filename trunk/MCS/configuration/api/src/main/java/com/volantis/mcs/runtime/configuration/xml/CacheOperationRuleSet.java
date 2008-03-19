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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.CacheOperationConfiguration;

import our.apache.commons.digester.Digester;

/**
 * Define the RuleSet for the cache-operation element.
 */
public class CacheOperationRuleSet extends PrefixRuleSet {

    /**
     * Initialise a new instance of this rule set.  This rule set matches
     * cache-operation elements (in terms of attributes)
     *
     * @param prefix         The prefix to use before all patterns
     */
    public CacheOperationRuleSet(final String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        // <caching-operation/cache>
        final String pattern = prefix + "/caching-operation";

        digester.addObjectCreate(pattern, CacheOperationConfiguration.class);
        digester.addSetNext(pattern, "setCacheOperationConfiguration");

        digester.addSetProperties(pattern,
            new String[] {"expiry-mode"}, new String[] {"expiryMode"});

        // add the cache rule set for the
        //<pipeline-configuration>/<caching-operation>/<cache>
        CacheRuleSet cacheRuleSet =
                    new CacheRuleSet(pattern, "/cache");
        cacheRuleSet.addRuleInstances(digester);
    }
}
