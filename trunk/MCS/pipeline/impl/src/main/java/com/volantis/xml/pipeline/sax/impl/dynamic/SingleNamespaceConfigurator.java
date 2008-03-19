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

package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;

/**
 * Base class for those {@link DynamicRuleConfigurator} that only add
 * rules to a single namespace.
 */
public abstract class SingleNamespaceConfigurator
        implements DynamicRuleConfigurator {

    /**
     * The namespace URI.
     */
    private final String namespace;

    /**
     * Initialise.
     *
     * @param namespace The namespace URI.
     */
    public SingleNamespaceConfigurator(String namespace) {
        this.namespace = namespace;
    }

    // Javadoc inherited.
    public void configure(DynamicProcessConfiguration configuration) {

        NamespaceRuleSet ruleSet = configuration.getNamespaceRules(namespace,
                true);

        configure(ruleSet);
    }

    /**
     * Add rules to the set for the namespace.
     *
     * @param ruleSet The namespace rule set.
     */
    protected abstract void configure(NamespaceRuleSet ruleSet);
}
