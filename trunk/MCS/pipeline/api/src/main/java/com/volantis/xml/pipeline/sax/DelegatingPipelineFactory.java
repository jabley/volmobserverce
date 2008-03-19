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

package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.flow.FlowControlManager;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import org.xml.sax.SAXException;

/**
 * An {@link InternalXMLPipelineFactory} that can be extended in order to
 * modify the behaviour of a {@link XMLPipelineFactory}.
 */
public class DelegatingPipelineFactory
        extends InternalXMLPipelineFactory {

    private final InternalXMLPipelineFactory delegate;

    public DelegatingPipelineFactory() {
        this.delegate = InternalXMLPipelineFactory.getInternalInstance()
                .createWrappableFactory(this);
    }

    // Javadoc inherited.
    public XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration configuration,
            EnvironmentInteraction environmentInteraction) {
        return delegate.createPipelineContext(configuration,
                environmentInteraction);
    }

    // Javadoc inherited.
    public XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration configuration,
            ExpressionContext expressionContext) {
        return delegate.createPipelineContext(configuration, expressionContext);
    }

    // Javadoc inherited.
    public XMLPipeline createPipeline(XMLPipelineContext context) {
        return delegate.createPipeline(context);
    }

    // Javadoc inherited.
    public XMLPipeline createDynamicPipeline(XMLPipelineContext context) {
        return delegate.createDynamicPipeline(context);
    }

    // Javadoc inherited.
    public XMLProcess createContextUpdatingProcess() {
        return delegate.createContextUpdatingProcess();
    }

    // Javadoc inherited.
    public XMLProcess createContextAnnotatingProcess() {
        return delegate.createContextAnnotatingProcess();
    }

    // Javadoc inherited.
    public XMLProcess createContextAnnotatingProcess(boolean setBaseURIOnRoot) {
        return delegate.createContextAnnotatingProcess(setBaseURIOnRoot);
    }

    // Javadoc inherited.
    public XMLPipelineReader createPipelineReader(XMLPipeline pipeline)
            throws SAXException {
        return delegate.createPipelineReader(pipeline);
    }

    // Javadoc inherited.
    public XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline)
            throws SAXException {
        return delegate.createPipelineFilter(pipeline);
    }

    // Javadoc inherited
    public XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline,
                                                  boolean setBaseURIOnRoot)
        throws SAXException {
        return delegate.createPipelineFilter(pipeline, setBaseURIOnRoot);
    }

    // Javadoc inherited.
    public DynamicRuleConfigurator getRuleConfigurator() {
        return delegate.getRuleConfigurator();
    }

    // Javadoc inherited.
    public XMLProcess createFlowControlProcess() {
        return delegate.createFlowControlProcess();
    }

    // Javadoc inherited.
    public DynamicProcessConfiguration createDynamicProcessConfiguration() {
        return delegate.createDynamicProcessConfiguration();
    }

    // Javadoc inherited.
    public DynamicProcess createDynamicProcess(
            XMLPipelineConfiguration configuration) {
        return delegate.createDynamicProcess(configuration);
    }

    // Javadoc inherited.
    public DynamicProcess createDynamicProcess(
            DynamicProcessConfiguration configuration) {
        return delegate.createDynamicProcess(configuration);
    }

    // Javadoc inherited.
    public XMLPipelineConfiguration createPipelineConfiguration() {
        return delegate.createPipelineConfiguration();
    }

    // Javadoc inherited.
    public NamespaceFactory getNamespaceFactory() {
        return delegate.getNamespaceFactory();
    }

    // Javadoc inherited.
    public ExpressionFactory getExpressionFactory() {
        return delegate.getExpressionFactory();
    }

    // Javadoc inherited.
    public PipelineRecorder createPipelineRecorder() {
        return delegate.createPipelineRecorder();
    }

    // Javadoc inherited.
    public InternalXMLPipelineFactory createWrappableFactory(
            InternalXMLPipelineFactory wrapper) {
        return delegate.createWrappableFactory(wrapper);
    }

    // Javadoc inherited.
    public FlowControlManager createFlowControlManager() {
        return delegate.createFlowControlManager();
    }
}
