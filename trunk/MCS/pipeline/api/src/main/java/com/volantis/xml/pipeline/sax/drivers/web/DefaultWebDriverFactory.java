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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.drivers.web.rules.ContentRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.CookieRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.GetOperationRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.HeaderRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.ParameterRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.ProxyRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.ScriptRule;
import com.volantis.xml.pipeline.sax.drivers.web.rules.PostOperationRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.rules.NoopRule;

/**
 * Default Implementation of the {@link WebDriverFactory} class
 */
public class DefaultWebDriverFactory extends WebDriverFactory {

    /**
     *  DynamicRuleConfigurator for this factory
     */
    protected DynamicRuleConfigurator ruleConfigurator;

    /**
     * Creates a new <code>DefaultWebDriverFactory</code> instance
     */
    public DefaultWebDriverFactory() {
        ruleConfigurator = createRuleConfigurator();
    }

    // javadoc inherited
    public DynamicRuleConfigurator getRuleConfigurator() {
        return ruleConfigurator;
    }

    // javadoc inherited
    public WebDriverRequest createRequest() {
        return new WebDriverRequestImpl();
    }

    // javadoc inherited
    public WebDriverResponse createResponse() {
        return new WebDriverResponseImpl();
    }

    // javadoc inherited
    public WebDriverConfiguration createConfiguration() {
        return new WebDriverConfigurationImpl();
    }

    // javadoc inherited
    public HTTPCacheConfiguration createHTTPCacheConfiguration() {
        return null;
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
                        Namespace.WEB_DRIVER.getURI(), true);

                // add the rule for the parameters adapter process
                addRule(ruleSet, WebDriverElements.PARAMETERS,
                        NoopRule.getDefaultInstance());

                // add the rule for the parameter adapter process
                addRule(ruleSet, WebDriverElements.PARAMETER, new ParameterRule());

                // add the rule for the headers adapter process
                addRule(ruleSet, WebDriverElements.HEADERS, NoopRule.getDefaultInstance());

                // add the rule for the header adapter process
                addRule(ruleSet, WebDriverElements.HEADER, new HeaderRule());

                // add the rule for the cookies adapter process
                addRule(ruleSet, WebDriverElements.COOKIES, NoopRule.getDefaultInstance());

                // add the rule for the cookie adapter process
                addRule(ruleSet, WebDriverElements.COOKIE, new CookieRule());

                // add the rule for the content adapter process
                addRule(ruleSet, WebDriverElements.CONTENT, new ContentRule());

                // add the rule for the script adapter process
                addRule(ruleSet, WebDriverElements.SCRIPT, new ScriptRule());

                // add the rule for the proxy adapter process
                addRule(ruleSet, WebDriverElements.PROXY, new ProxyRule());

                // add the rule for the get adapter process
                addRule(ruleSet, WebDriverElements.GET, new GetOperationRule());

                // add the rule for the post adapter process
                addRule(ruleSet, WebDriverElements.POST, new PostOperationRule());
                                    }
        };
            }

    private void addRule(
            NamespaceRuleSet ruleSet, final ImmutableExpandedName name,
            final DynamicElementRule rule) {

        ruleSet.addRule(name.getLocalName(), rule);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Jul-04	751/5	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/2	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 18-Jul-03	215/9	steve	VBM:2003070806 Made singleton more threadsafe

 17-Jul-03	215/7	steve	VBM:2003070806 Move back again as Accurev does not like this

 17-Jul-03	215/4	steve	VBM:2003070806 Move DefaultWebDriverFactory to WebDriverFactory

 17-Jul-03	215/1	steve	VBM:2003070806 Implement we driver

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
