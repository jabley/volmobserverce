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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddAdapterRule;
import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import com.volantis.xml.pipeline.sax.drivers.webservice.WebServiceDriverFactory;
import com.volantis.xml.pipeline.Namespace;

/**
 * Default implementation of the {@link com.volantis.xml.pipeline.sax.drivers.webservice.WebServiceDriverFactory} class
 */
public class WebServiceDriverFactoryImpl extends WebServiceDriverFactory {

    /**
     *  DynamicRuleConfigurator for this factory
     */
    protected DynamicRuleConfigurator ruleConfigurator;

    /**
     * Creates a new <code>DefaultWebServiceDriverFactory</code> instance
     */
    public WebServiceDriverFactoryImpl() {
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
                        Namespace.WEB_SERVICE_DRIVER.getURI(), true);

                // add the rule for the request adapter process
                ruleSet.addRule("request",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess
                                            createAdapterProcess(DynamicProcess dynamicProcess) {
                                        return new RequestAdapterProcess();
                                    }
                                });

                // add the rule for the message adapter process
                ruleSet.addRule("message",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess
                                            createAdapterProcess(DynamicProcess dynamicProcess) {
                                        return new MessageAdapterProcess();
                                    }
                                });

                // add the rule for the wsdl-operation adapter process
                ruleSet.addRule("wsdl-operation",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess
                                            createAdapterProcess(DynamicProcess dynamicProcess) {
                                        return new
                                                WSDLOperationAdapterProcess();
                                    }
                                });
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 ===========================================================================
*/
