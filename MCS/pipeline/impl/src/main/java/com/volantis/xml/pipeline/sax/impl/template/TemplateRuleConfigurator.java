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

package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.impl.dynamic.SingleNamespaceConfigurator;
import com.volantis.xml.pipeline.sax.impl.template.apply.ApplyRule;
import com.volantis.xml.pipeline.sax.impl.template.binding.BindingRule;
import com.volantis.xml.pipeline.sax.impl.template.binding.BindingsRule;
import com.volantis.xml.pipeline.sax.impl.template.documentation.DocumentationRule;
import com.volantis.xml.pipeline.sax.impl.template.parameter.DeclarationsRule;
import com.volantis.xml.pipeline.sax.impl.template.parameter.ParameterRule;
import com.volantis.xml.pipeline.sax.impl.template.value.ComplexValueRule;
import com.volantis.xml.pipeline.sax.impl.template.value.SimpleValueRule;
import com.volantis.xml.pipeline.sax.impl.template.value.ValueInsertRule;
import com.volantis.xml.pipeline.sax.impl.template.definition.BodyRule;

/**
 * A rule configurator for template rules.
 */
public class TemplateRuleConfigurator
        extends SingleNamespaceConfigurator {

    /**
     * The default instance.
     */
    private static final DynamicRuleConfigurator DEFAULT_INSTANCE =
            new TemplateRuleConfigurator();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicRuleConfigurator getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static final DynamicElementRule APPLY_RULE = new ApplyRule();
    public static final DynamicElementRule BINDINGS_RULE = new BindingsRule();
    public static final DynamicElementRule BINDING_RULE = new BindingRule();
    public static final DynamicElementRule SIMPLE_VALUE_RULE =
            new SimpleValueRule();
    public static final DynamicElementRule COMPLEX_VALUE_RULE =
            new ComplexValueRule();
    public static final DynamicElementRule DEFINITION_RULE =
            new StateUpdatingRule(TemplateSchema.DEFINITION);
    public static final DynamicElementRule BODY_RULE = new BodyRule();
    public static final DynamicElementRule DOCUMENTATION_RULE =
            new DocumentationRule();
    public static final DynamicElementRule DECLARATIONS_RULE =
            new DeclarationsRule();
    public static final DynamicElementRule PARAMETER_RULE = new ParameterRule();
    public static final DynamicElementRule VALUE_RULE = new ValueInsertRule();

    public TemplateRuleConfigurator() {
        super(Namespace.TEMPLATE.getURI());
    }

    // Javadoc inherited.
    protected void configure(NamespaceRuleSet ruleSet) {

        // add the rule for the apply element.
        ruleSet.addRule("apply", APPLY_RULE);

        // add the rule for the bindings element.
        ruleSet.addRule("bindings", BINDINGS_RULE);

        // add the rule for the binding element.
        ruleSet.addRule("binding", BINDING_RULE);

        // add the rule for the simpleValue element.
        ruleSet.addRule("simpleValue", SIMPLE_VALUE_RULE);

        // add the rule for the complexValue element.
        ruleSet.addRule("complexValue", COMPLEX_VALUE_RULE);

        // add the rule for the definition element.
        ruleSet.addRule("definition", DEFINITION_RULE);

        // add the rule for the body element.
        ruleSet.addRule("body", BODY_RULE);

        // add the rule for the documentation element.
        ruleSet.addRule("documentation", DOCUMENTATION_RULE);

        // add the rule for the parameters element.
        ruleSet.addRule("declarations", DECLARATIONS_RULE);

        // add the rule for the parameter element.
        ruleSet.addRule("parameter", PARAMETER_RULE);

        // add the rule for the value element.
        ruleSet.addRule("value", VALUE_RULE);
    }
}
