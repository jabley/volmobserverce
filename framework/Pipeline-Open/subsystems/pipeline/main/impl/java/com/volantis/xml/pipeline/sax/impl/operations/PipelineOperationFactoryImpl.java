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
package com.volantis.xml.pipeline.sax.impl.operations;

import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.adapter.AdapterProcess;
import com.volantis.xml.pipeline.sax.impl.cache.CacheRuleConfigurator;
import com.volantis.xml.pipeline.sax.content.ContentRule;
import com.volantis.xml.pipeline.sax.convert.AbsoluteToRelativeURLAdapterProcess;
import com.volantis.xml.pipeline.sax.convert.ElementCaseAdapterProcess;
import com.volantis.xml.pipeline.sax.convert.URLToURLCAdapterProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddAdapterRule;
import com.volantis.xml.pipeline.sax.impl.operations.tryop.TryRuleConfigurator;
import com.volantis.xml.pipeline.sax.impl.operations.value.ValueOfRule;
import com.volantis.xml.pipeline.sax.impl.cache.CacheRuleConfigurator;
import com.volantis.xml.pipeline.sax.operations.PipelineOperationFactory;
import com.volantis.xml.pipeline.sax.operations.diselect.DIRulesRegisterer;
import com.volantis.xml.pipeline.sax.impl.operations.debug.SerializeRule;
import com.volantis.xml.pipeline.sax.operations.evaluate.EvaluateRule;
import com.volantis.xml.pipeline.sax.operations.foreach.ForEachRule;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import com.volantis.xml.pipeline.sax.impl.operations.transform.TransformAdapterProcess;

/**
 * Default implementation of the {@link PipelineOperationFactory} class
 */
public class PipelineOperationFactoryImpl
        extends PipelineOperationFactory {

    /**
     *  DynamicRuleConfigurator for this factory
     */
    protected DynamicRuleConfigurator ruleConfigurator;

    /**
     * Creates a new <code>PipelineOperationFactoryImpl</code> instance
     */
    public PipelineOperationFactoryImpl() {
        ruleConfigurator = createRuleConfigurator();
    }

    // javadoc inherited
    public TransformConfiguration createTransformConfiguration() {
        return new DefaultTransformConfiguration();
    }

    // javadoc inherited
    public DynamicRuleConfigurator getRuleConfigurator() {
        return ruleConfigurator;
    }

    /**
     * Factor the DynamicRuleConfigurator that the
     * {@link #getRuleConfigurator} will return
     *
     * @return a DynamicRuleConfigurator instance
     */
    protected DynamicRuleConfigurator createRuleConfigurator() {
        // create and return the DynamicRuleConfigurator
        return new DynamicRuleConfigurator() {
            // javadoc inherited
            public void configure(DynamicProcessConfiguration configuration) {

                CacheRuleConfigurator.getDefaultInstance()
                        .configure(configuration);

                // obtain the namespace rule set that this configuration
                // will populate
                NamespaceRuleSet ruleSet = configuration.getNamespaceRules(
                        Namespace.PIPELINE.getURI(), true);
                
                // add the rule for the content adapter process
                ruleSet.addRule("content", ContentRule.getDefaultInstance());

                // add the rule for the transform process
                ruleSet.addRule("transform",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess createAdapterProcess(
                                            DynamicProcess dynamicProcess) {
                                        return new TransformAdapterProcess();
                                    }
                                });

                // add the rules for the URL to URLC process. This has two
                // aliases. The former, which is the original name, is now
                // deprecated as "DMS" is not a valid name in terms of
                // Product Management naming.
                // @todo later delete this deprecated rule when PM deems that it can be removed
                ruleSet.addRule("convertImageURLToDMS",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess createAdapterProcess(
                                            DynamicProcess dynamicProcess) {
                                        return new URLToURLCAdapterProcess();
                                    }
                                });

                ruleSet.addRule("convertImageURLToTranscoder",
                                new AbstractAddAdapterRule() {
                                    public AdapterProcess createAdapterProcess(
                                            DynamicProcess dynamicProcess) {
                                        return new URLToURLCAdapterProcess();
                                    }
                                });

                // add the rule for the convertElementCase process
                ruleSet.addRule("convertElementCase",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess createAdapterProcess(
                                            DynamicProcess dynamicProcess) {
                                        return new ElementCaseAdapterProcess();
                                    }
                                });


                // add the rule for the convertAbsoluteToRelativeURL process
                ruleSet.addRule("convertAbsoluteToRelativeURL",
                                new AbstractAddAdapterRule() {
                                    // javadoc inherited
                                    public AdapterProcess createAdapterProcess(
                                            DynamicProcess dynamicProcess) {
                                        return new AbsoluteToRelativeURLAdapterProcess();
                                    }
                                });

                // Add the rules for the try operation.
                TryRuleConfigurator.getDefaultInstance()
                        .configure(configuration);

                // add the rule for the evaluation process
                ruleSet.addRule("evaluate", EvaluateRule.getDefaultInstance());

                // add the debug rules
                ruleSet.addRule("serialize",
                        SerializeRule.getDefaultInstance());

                // add the for-each rule.
                ruleSet.addRule("for-each", ForEachRule.getDefaultInstance());

                // add the rule for value element.
                ruleSet.addRule("value-of", ValueOfRule.getDefaultInstance());

                // Add the rules for DISelect operations.
                DIRulesRegisterer.getDefaultInstance().register(configuration);
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 04-Nov-04	6109/1	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Oct-04	906/1	doug	VBM:2004101313 Added an Evaluate pipeline process

 13-Aug-03	331/5	adrian	VBM:2003081001 implemented try operation

 13-Aug-03	331/2	adrian	VBM:2003081001 implemented try operation

 12-Aug-03	323/3	byron	VBM:2003080802 Provide ConvertElementCase pipeline process

 11-Aug-03	275/3	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 08-Aug-03	308/4	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 07-Aug-03	268/1	chrisw	VBM:2003072905 Public API changed for transform configuration

 06-Aug-03	301/3	doug	VBM:2003080503 Refactored URLToURLCConverter process to use DynamicElementRules

 06-Aug-03	301/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 ===========================================================================
*/
