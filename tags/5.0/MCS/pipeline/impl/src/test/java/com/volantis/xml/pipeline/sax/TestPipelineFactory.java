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
package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.extensions.PipelineExtensionFactory;
import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddAdapterRule;
import com.volantis.xml.pipeline.sax.expression.VariableDeclarationAdapterProcess;
import com.volantis.xml.pipeline.sax.expression.VariableScopeAdapterProcess;
import com.volantis.xml.pipeline.sax.impl.XMLPipelineFactoryImpl;
import com.volantis.xml.pipeline.sax.template.AlternateComplexityRule;
import com.volantis.xml.pipeline.sax.template.Counter;
import com.volantis.xml.pipeline.sax.template.CounterAdapterProcess;
import com.volantis.xml.pipeline.sax.template.ReenterRule;
import com.volantis.xml.pipeline.sax.tryop.FailOnExecuteRule;
import com.volantis.xml.pipeline.sax.tryop.GenerateErrorRule;

/**
 * Implementation of the PipelineFactory interface for use in test cases.
 */ 
public class TestPipelineFactory
        extends XMLPipelineFactoryImpl
        implements Counter {

    /**
     * Constant that identifies the namespace to use for the
     * special integration processes
     */
    public static final Namespace INTEGRATION =
        new Namespace("integration") {
        };

    /**
     * Counter variable
     */ 
    private long counter = 0;

    // javadoc inherited
    public long get() {
        return counter++;
    }

    /**
     * Factory method that creates a PipelineContext with an empty 
     * <code>XMLPipelineConfiguration</code> and a null 
     * <code>EnvironmentInteraction</code>.
     * @return an XMLPipelineContext instance
     */    
    public XMLPipelineContext createPipelineContext() {
         // create an empty configuration
        XMLPipelineConfiguration pipelineConfiguration =
                createPipelineConfiguration();


        // OK to us a null EnvironmentInteraction for this test
        EnvironmentInteraction interaction = null;        
        return createPipelineContext(pipelineConfiguration, interaction);        
        
    }

    // javadoc inherited
    public DynamicRuleConfigurator getRuleConfigurator() {
        final DynamicRuleConfigurator standardRules 
                = super.getRuleConfigurator();
        
        return new DynamicRuleConfigurator() {
            public void configure(DynamicProcessConfiguration configuration) {
                standardRules.configure(configuration);



                // add the integration specific rules
                // obtain the namespace rule set that this configuration
                // will populate
                NamespaceRuleSet ruleSet = configuration.getNamespaceRules(
                        INTEGRATION.getURI(), true);
            
                // add the rule for the variableDeclaration adapter process
                ruleSet.addRule("variableDeclaration",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess
                                            createAdapterProcess(DynamicProcess dynamicProcess) {
                                        return new 
                                                VariableDeclarationAdapterProcess();
                                    }
                                });

                // add the rule for the variableScope adapter process
                ruleSet.addRule("variableScope",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess
                                            createAdapterProcess(DynamicProcess dynamicProcess) {
                                        return new 
                                                VariableScopeAdapterProcess();
                                    }
                                });                
            
                // add the rule for the counter adapter process
                ruleSet.addRule("counter",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess
                                            createAdapterProcess(DynamicProcess dynamicProcess) {
                                        return new CounterAdapterProcess(
                                                TestPipelineFactory.this);
                                    }
                                });

                ruleSet.addRule("reenter", new ReenterRule());

                ruleSet.addRule("alternate-complexity", new AlternateComplexityRule());

                // add the generate error rule
                ruleSet.addRule("generateError", new GenerateErrorRule());
                
                // add the FailOnExecute rule
                ruleSet.addRule("failOnExecute", new FailOnExecuteRule());
            }
        };
    }      
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/3	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 01-Aug-03	258/1	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
