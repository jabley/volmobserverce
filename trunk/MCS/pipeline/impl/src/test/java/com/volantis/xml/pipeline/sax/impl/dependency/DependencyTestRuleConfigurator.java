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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.dynamic.SingleNamespaceConfigurator;

public class DependencyTestRuleConfigurator
        extends SingleNamespaceConfigurator {

    /**
     * The default instance.
     */
    private static final DynamicRuleConfigurator DEFAULT_INSTANCE =
            new DependencyTestRuleConfigurator();

    public static final String NAMESPACE =
            "http://www.volantis.com/test/dependency";

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicRuleConfigurator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static final AggregatorRule AGGREGATOR_RULE = new AggregatorRule();
    public static final DependencyRule DEPENDENCY_RULE = new DependencyRule();

    public DependencyTestRuleConfigurator() {
        super(NAMESPACE);
    }

    protected void configure(NamespaceRuleSet ruleSet) {
        ruleSet.addRule("aggregator", AGGREGATOR_RULE);
        ruleSet.addRule("dependency", DEPENDENCY_RULE);
    }
}
