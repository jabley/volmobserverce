/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.operations.diselect;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.operations.diselect.DIRulesRegisterer;
import com.volantis.xml.pipeline.sax.impl.operations.value.ValueOfRule;

/**
 * The default DIRulesRegister
 */
public final class DefaultDIRulesRegisterer extends DIRulesRegisterer {

    // javadoc inherited
    public void register(DynamicProcessConfiguration dynamicConfiguration) {

        NamespaceRuleSet namespaceRules =
                dynamicConfiguration.getNamespaceRules(
                        Namespace.DISELECT.getURI(),
                        true);

        namespaceRules.addRule("value", ValueOfRule.getDefaultInstance());
    }
}
