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
package com.volantis.xml.pipeline.sax.impl;

import com.volantis.shared.environment.EnvironmentFactory;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.*;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.SimpleXMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.uri.URIDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.webservice.WebServiceDriverFactory;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.pipeline.sax.impl.dynamic.*;
import com.volantis.xml.pipeline.extensions.PipelineExtensionFactory;
import com.volantis.xml.pipeline.sax.impl.flow.SimpleFlowControlManager;
import com.volantis.xml.pipeline.sax.impl.recorder.ContextPipelineRecorder;
import com.volantis.xml.pipeline.sax.operations.PipelineOperationFactory;
import com.volantis.xml.pipeline.sax.performance.MonitoringConfiguration;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import com.volantis.xml.pipeline.sax.servlet.ServletOperationFactory;
import com.volantis.xml.pipeline.sax.template.TemplateFactory;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;

/**
 * A default XML implementation of the XMLPipelineFactory interface
 */
public class XMLPipelineFactoryImpl
        extends InternalXMLPipelineFactory {

    /**
     * The default configuration for performance monitoring
     */
    private static final Configuration monitoringConfiguration =
            new MonitoringConfiguration();

    /**
     *  DynamicRuleConfigurator for this factory
     */
    private final DynamicRuleConfigurator ruleConfigurator;

    private final InternalXMLPipelineFactory wrapper;

    /**
     * Creates a new <code>XMLPipelineFactoryImpl</code> instance
     */
    public XMLPipelineFactoryImpl() {
        this(null);
    }

    public XMLPipelineFactoryImpl(InternalXMLPipelineFactory wrapper) {
        this.wrapper = wrapper == null ? this : wrapper;
        ruleConfigurator = createRuleConfigurator();
    }

    // javadoc inherited
    public XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration configuration,
            EnvironmentInteraction environmentInteraction) {


        // factor a NamespacePrefixTracker
        NamespacePrefixTracker namespaceTracker =
                wrapper.getNamespaceFactory().createPrefixTracker();

        // factor an EnvironmentInteractionTracker
        EnvironmentFactory environmentFactory =
                EnvironmentFactory.getDefaultInstance();

        EnvironmentInteractionTracker envTracker =
                environmentFactory.createInteractionTracker();

        // push the root EnvironmentInteraction onto the tracker
        if (environmentInteraction != null) {
            envTracker.pushEnvironmentInteraction(environmentInteraction);
        }

        // factor an Expression context passing in both the Environment
        // Interaction and Namespace prefix trackers
        ExpressionContext expressionContext =
                wrapper.getExpressionFactory().
                createExpressionContext(envTracker, namespaceTracker);

        // Construct and return  XMLPipelineContextImpl
        // using the arguments provided
        return createPipelineContext(configuration, expressionContext);
    }

    //  javadoc inherited
    public XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration configuration,
            ExpressionContext expressionContext) {
        // Construct and return  XMLPipelineContextImpl
        // using the arguments provided
        return new XMLPipelineContextImpl(
                wrapper, configuration, expressionContext);
    }

    // javadoc ineherited
    public XMLProcess createContextUpdatingProcess() {

        return new ContextManagerProcess();
    }

    // javadoc inherited
    public XMLProcess createContextAnnotatingProcess() {
        return new ContextAnnotatingProcess();
    }

    // Javadoc inherited.
    public XMLProcess createContextAnnotatingProcess(boolean setBaseURIOnRoot) {
        return new ContextAnnotatingProcess(setBaseURIOnRoot);
    }

    // Javadoc inherited
    public XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline)
        throws SAXException {
        return createPipelineFilter(pipeline, false);
    }

    // javadoc inherited
    public XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline,
                                                  boolean setBaseURIOnRoot)
            throws SAXException {

        // adapat the pipeline to a filter via the appropriate
        // adapter class
        DefaultXMLPipelineProcess outerPipeline =
                new DefaultXMLPipelineProcess(pipeline.getPipelineContext());

        XMLPipelineProcess xmlProcess =
                (XMLPipelineProcess)pipeline.getPipelineProcess();
        XMLProcess next = xmlProcess.getNextProcess();
        outerPipeline.setNextProcess(next);

        outerPipeline.addTailProcess(wrapper.createContextUpdatingProcess());

        outerPipeline.addTailXMLPipelineProcess(xmlProcess);

        final XMLProcess cap =
            wrapper.createContextAnnotatingProcess(setBaseURIOnRoot);
        outerPipeline.addTailProcess(cap);

        return new XMLPipelineFilterAdapter(outerPipeline) {
            // javadoc inherited from the XMLReader interface
            public void parse(InputSource input)
                    throws IOException,
                    SAXException {
                super.parse(input);
                cap.stopProcess();
            }
        };
    }

    // javadoc inherited
    public XMLPipelineReader createPipelineReader(XMLPipeline pipeline)
            throws SAXException {
        XMLReader reader = XMLReaderFactory.createXMLReader(false);
        XMLPipelineFilter filter = wrapper.createPipelineFilter(pipeline);
        filter.setParent(reader);
        return filter;
    }

    // javadoc inherited
    public DynamicRuleConfigurator getRuleConfigurator() {
        return ruleConfigurator;
    }

    // javadoc inherited
    public XMLProcess createFlowControlProcess() {
        return new FlowControlProcess();
    }

    // javadoc inherited
    public DynamicProcessConfiguration createDynamicProcessConfiguration() {
        // create an empty configuration
        return new SimpleDynamicProcessConfiguration();
    }

    // javadoc inherited
    public DynamicProcess
            createDynamicProcess(XMLPipelineConfiguration configuration) {
        // retrieve the DynamicProcessConfiguration from the pipeline
        // configuration
        DynamicProcessConfiguration dynamicConfig =
                SimpleDynamicProcess.getDynamicConfiguration(configuration);
        return wrapper.createDynamicProcess(dynamicConfig);
    }

    // javadoc inherited
    public DynamicProcess createDynamicProcess(
            DynamicProcessConfiguration configuration) {
        return new SimpleDynamicProcess(configuration);
    }

    // javadoc inherited
    public XMLPipelineConfiguration createPipelineConfiguration() {
        SimpleXMLPipelineConfiguration simpleXMLPipelineConfiguration =
                new SimpleXMLPipelineConfiguration();
        simpleXMLPipelineConfiguration.storeConfiguration(
                MonitoringConfiguration.class,
                monitoringConfiguration);
        return simpleXMLPipelineConfiguration;
    }

    // javadoc inherited
    public XMLPipeline createPipeline(XMLPipelineContext context) {
        return new XMLPipelineProcessImpl(context);
    }

    // javadoc inherited
    public XMLPipeline createDynamicPipeline(XMLPipelineContext context) {

        try {
            return new SimpleDynamicProcess(context);
        } catch (SAXException e) {
            throw new ExtendedRuntimeException(
                    "Could not create a pipeline", e);
        }
    }

    // javadoc inherited
    public NamespaceFactory getNamespaceFactory() {
        return NamespaceFactory.getDefaultInstance();
    }

    // javadoc inherited
    public ExpressionFactory getExpressionFactory() {
        return ExpressionFactory.getDefaultInstance();
    }

    // Javadoc inherited.
    public PipelineRecorder createPipelineRecorder() {
        return new ContextPipelineRecorder();
    }

    // Javadoc inherited.
    public InternalXMLPipelineFactory createWrappableFactory(
            InternalXMLPipelineFactory wrapper) {
        return new XMLPipelineFactoryImpl(wrapper);
    }

    // Javadoc inherited.
    public FlowControlManager createFlowControlManager() {
        return new SimpleFlowControlManager();
    }

    /**
     * Factor the DynamicRuleConfigurator that the
     *
     * {@link #getRuleConfigurator} will return
     * @return a DynamicRuleConfigurator instance
     */
    private DynamicRuleConfigurator createRuleConfigurator() {
        // create and return the DynamicRuleConfigurator
        return new DynamicRuleConfigurator() {
            // javadoc inherited
            public void configure(DynamicProcessConfiguration configuration) {


                // add the rules for the pipeline processes
                PipelineOperationFactory.getDefaultInstance().
                        getRuleConfigurator().configure(configuration);

                // add the rules URI driver processes
                URIDriverFactory.getDefaultInstance().
                        getRuleConfigurator().configure(configuration);

                // add the rules for the Web Driver processes
                WebDriverFactory.getDefaultInstance().
                        getRuleConfigurator().configure(configuration);

                // add the rules for the template processes
                TemplateFactory.getDefaultInstance().
                        getRuleConfigurator().configure(configuration);

                // add the rules for the web service processes
                WebServiceDriverFactory.getDefaultInstance().
                        getRuleConfigurator().configure(configuration);

                // add the rules for the servlet processes
                ServletOperationFactory.getDefaultInstance().
                        getRuleConfigurator().configure(configuration);

                // delegate to the extension factory to see if custom
                // pipeline extensions need adding
                PipelineExtensionFactory.getDefaultInstance().
                        extendRules(configuration);
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

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/5	adrian	VBM:2004011905 removed rogue import statement

 30-Jan-04	531/3	adrian	VBM:2004011905 removed rogue import statement

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 20-Jan-04	529/2	adrian	VBM:2004011904 Pipeline API updates in preparation for fully integrating ContextUpdating/Annotating processes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 11-Aug-03	275/3	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 06-Aug-03	301/4	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 04-Aug-03	217/6	allan	VBM:2003071702 Filter nested anchors. Fixed merge conflicts.

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 01-Aug-03	258/3	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 22-Jul-03	225/1	doug	VBM:2003071805 Refactored the XMLPipeline interface to reflect the new public API

 14-Jul-03	185/1	steve	VBM:2003071402 Refactor exceptions into throwable package

 23-Jun-03	95/2	doug	VBM:2003061605 Document Event Filtering changes

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
