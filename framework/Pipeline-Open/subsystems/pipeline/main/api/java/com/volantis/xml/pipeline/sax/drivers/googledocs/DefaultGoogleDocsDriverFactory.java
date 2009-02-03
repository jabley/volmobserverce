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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.googledocs;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.drivers.flickr.FlickrDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.flickr.ListPhotosRule;

/**
 * Default implementation of the {@link com.volantis.xml.pipeline.sax.drivers.googledocs.GoogleDocsDriverFactory} class
 */
public class DefaultGoogleDocsDriverFactory extends GoogleDocsDriverFactory {

    /**
     *  DynamicRuleConfigurator for this factory
     */
    protected DynamicRuleConfigurator ruleConfigurator;

    /**
     * Create a new <code>DefaultGoogleDocsDriverFactory</code> instance
     */
    public DefaultGoogleDocsDriverFactory() {
        ruleConfigurator = createRuleConfigurator();
    }

    // javadoc inherited
    public DynamicRuleConfigurator getRuleConfigurator() {
        return ruleConfigurator;
    }

    /**
     * Factor the DynamicRuleConfigurator that the
     * {@link #getRuleConfigurator} will return
     * @return a DynamicRuleConfigurator instance
     */
    protected DynamicRuleConfigurator createRuleConfigurator() {
        // create and return the DynamicRuleConfigurator
        return new DynamicRuleConfigurator() {
            // javadoc inherited
            public void configure(DynamicProcessConfiguration configuration) {

                // obtain the namespace rule set that this configuration
                // will populate
                NamespaceRuleSet ruleSet = configuration.getNamespaceRules(
                        Namespace.GOOGLE_DOCS.getURI(), true);

                // add the rule for the list docs adapter process
                ruleSet.addRule("list-docs", ListDocsRule.getDefaultInstance());

                // add the rule for the authenticate adapter process
                ruleSet.addRule("authenticate", AuthenticateRule.getDefaultInstance());

                ruleSet.addRule("fetch", FetchDocRule.getDefaultInstance());
            }
        };
    }
}