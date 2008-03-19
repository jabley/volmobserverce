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

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;
import com.volantis.mcs.runtime.configuration.pipeline.MarkupExtensionsConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.MarkupExtensionConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.ProcessConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.RuleConfiguration;

/**
 * The Rule Set for the <pipeline-configuration>/<markup-extenstions>
 * element
 */
public class MarkupExtensionsRuleSet extends PrefixRuleSet {

    /**
     * Initializes a <code>MarkupExtensionsRuleSet</code> instance
     * @param prefix the prefix
     */
    public MarkupExtensionsRuleSet(String prefix) {
        super(prefix);
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        // <pipeline-configuration>/<markup-extenstions>
        final String pattern = prefix + "/markup-extensions";
        digester.addObjectCreate(pattern, MarkupExtensionsConfiguration.class);
        digester.addSetNext(pattern, "setMarkupExtensionsConfiguration");

        addMarkupExtensionRules(pattern, digester);
    }

    /**
     * Processes a markup-extensions element
     * @param pattern the pattern that represents the parent xml element of
     * the markup-extensions element.
     * @param digester a <code>Digester</code> instance
     */
    private void addMarkupExtensionRules(String pattern, Digester digester) {
        // <pipeline-configuration>/<markup-extenstions>/<markup-extention>
        final String extensionPattern = pattern + "/markup-extension";
        digester.addObjectCreate(extensionPattern,
                                 MarkupExtensionConfiguration.class);
        digester.addSetNext(extensionPattern, "addMarkupExtensionConfiguration");
        digester.addSetProperties(extensionPattern,
                                  new String[] {"local-name", "namespace-uri"},
                                  new String[] {"localName", "namespaceURI"});

        // add the rules for the rule element
        addRuleRules(extensionPattern, digester);

        // add the rules for the process element
        addProcessRules(extensionPattern, digester);
    }

    /**
     * Processes a rule element
     * @param pattern the pattern that represents the parent xml element of
     * the rule element.
     * @param digester a <code>Digester</code> instance
     */
    private void addRuleRules(String pattern, Digester digester) {
        // <pipeline-configuration>/<markup-extenstions>/
        // <markup-extention>/<rule>
        final String rulePattern = pattern + "/rule";
        digester.addObjectCreate(rulePattern, RuleConfiguration.class);
        digester.addSetNext(rulePattern, "setPipelinePluginConfiguration");
        digester.addSetProperties(rulePattern, "class-name", "className");
    }

    /**
     * Processes a process element
     * @param pattern the pattern that represents the parent xml element of
     * the process element.
     * @param digester a <code>Digester</code> instance
     */
    private void addProcessRules(String pattern, Digester digester) {
        // <pipeline-configuration>/<markup-extenstions>/
        // <markup-extention>/<rule>
        final String processPattern = pattern + "/process";
        digester.addObjectCreate(processPattern, ProcessConfiguration.class);
        digester.addSetNext(processPattern, "setPipelinePluginConfiguration");
        digester.addSetProperties(processPattern, "class-name", "className");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Jun-05	7418/1	doug	VBM:2005021505 Simplified pipeline initialization

 ===========================================================================
*/
