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

package com.volantis.xml.pipeline.sax.impl.operations.tryop;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.impl.dynamic.SingleNamespaceConfigurator;

/**
 * The rule configurator for the try operation.
 */
public class TryRuleConfigurator
        extends SingleNamespaceConfigurator {

    /**
     * The default instance.
     */
    private static final DynamicRuleConfigurator DEFAULT_INSTANCE =
            new TryRuleConfigurator();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicRuleConfigurator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final DynamicElementRule TRY_RULE = new TryRule();
    private static final DynamicElementRule PREFERRED_RULE = new PreferredRule();
    private static final DynamicElementRule ALTERNATIVE_RULE = new AlternativeRule();

    public TryRuleConfigurator() {
        super(Namespace.PIPELINE.getURI());
    }

    protected void configure(NamespaceRuleSet ruleSet) {

        // add the rule for the try element.
        ruleSet.addRule("try", TRY_RULE);

        // add the rule for the preferred element.
        ruleSet.addRule("preferred", PREFERRED_RULE);

        // add the rule for the alternative element.
        ruleSet.addRule("alternative", ALTERNATIVE_RULE);
    }
}
