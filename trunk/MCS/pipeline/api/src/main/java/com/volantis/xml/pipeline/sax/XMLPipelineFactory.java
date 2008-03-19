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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Doug            VBM:2003030405 - Created, Interface for a
 *                              Factory that creates XMLPipeline related
 *                              objects.
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.recorder.PipelineRecorder;
import org.xml.sax.SAXException;

/**
 * A class for creating XML pipeline framework objects.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public abstract class XMLPipelineFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        metaDefaultFactory =
                new MetaDefaultFactory(
                        "com.volantis.xml.pipeline.sax.impl.XMLPipelineFactoryImpl",
                        XMLPipelineFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static XMLPipelineFactory getDefaultInstance() {
        return (XMLPipelineFactory)
                metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create a pipeline context from the specified configuration and the
     * environment interaction.
     * <p>The returned pipeline context contains a reference to the pipeline
     * configuration and in order to protect it against the pipeline
     * configuration being modified while it is running this method makes the
     * configuration immutable.</p>
     *
     * @param configuration The pipeline configuration.
     * @param environmentInteraction The environment interaction.
     * @return A new pipeline context.
     */
    public abstract XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration configuration,
            EnvironmentInteraction environmentInteraction);

    /**
     * Create a pipeline context from the specified configuration and the
     * expression context.
     * <p>The returned pipeline will share the following objects with the
     * ExpressionContext.</p>
     * <ul>
     * <li>{@link com.volantis.xml.namespace.NamespacePrefixTracker}</li>
     * <li>{@link
     * com.volantis.shared.environment.EnvironmentInteractionTracker}</li>
     * </ul>
     * @param configuration The pipeline configuration.
     * @param expressionContext The expression context.
     * @return A new pipeline context.
     */
    public abstract XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration configuration,
            ExpressionContext expressionContext);

    /**
     * Create a pipeline that runs within the specified pipeline context.
     * @param context The context.
     * @return A new XMLPipeline.
     */
    public abstract XMLPipeline createPipeline(XMLPipelineContext context);

    /**
     * Create a dynamic pipeline that runs within the specified pipeline
     * context.
     * <p>The returned pipeline contains a {@link DynamicProcess} that is
     * configured from the {@link DynamicProcessConfiguration} that is in the
     * {@link XMLPipelineConfiguration} referenced from the context.</p>
     * @param context The context.
     * @return A new XMLPipeline.
     */
    public abstract
            XMLPipeline createDynamicPipeline(XMLPipelineContext context);

    /**
     * Create a new XMLProcess that will update the context based on the
     * events that it receives.
     * @return An XMLProcess that will update the context and consume any events
     * that are not allowed within the pipeline event stream.
     * @see #createContextAnnotatingProcess
     * @see <a href="package-summary.html#contextual-information">Contextual Information</a>
     */
    public abstract XMLProcess createContextUpdatingProcess();

    /**
     * Create a new XMLProcess that will update the context based on the
     * events that it receives.
     *
     * <p>Equivalent to calling:
     * <code>createContextAnnotatingProcess(false)</code></p>
     *
     * @return An XMLProcess that will update the context and consume any
     *         events that are not allowed within the pipeline event stream.
     * @see #createContextUpdatingProcess()
     * @see <a href="package-summary.html#contextual-information">Contextual
     *      Information</a>
     * @see #createContextAnnotatingProcess(boolean)
     */
    public abstract XMLProcess createContextAnnotatingProcess();

    /**
     * Create a new XMLProcess that will update the context based on the
     * events that it receives.
     *
     * @param setBaseURIOnRoot If true causes an <code>xml:base</code>
     *                         attribute to be added to the root element.
     * @return An XMLProcess that will update the context and consume any
     *         events that are not allowed within the pipeline event stream.
     * @see #createContextUpdatingProcess()
     * @see <a href="package-summary.html#contextual-information">Contextual
     *      Information</a>
     */
    public abstract XMLProcess createContextAnnotatingProcess(
            boolean setBaseURIOnRoot);

    /**
     * Create an XMLReader that processes the SAX events in the pipeline before
     * passing them onto the various handlers.
     * @param pipeline The pipeline to use to process the events.
     * @return An XMLPipelineReader that wraps the specified pipeline.
     * @throws SAXException If there was a problem creating the XMLReader.
     */
    public abstract XMLPipelineReader createPipelineReader(XMLPipeline pipeline)
            throws SAXException;

    /**
     * Create an XMLFilter that processes the SAX events in the pipeline before
     * passing them onto the various handlers.
     * @param pipeline The pipeline to use to process the events.
     * @return An XMLPipelineFilter that wraps the specified pipeline.
     * @throws SAXException If there was a problem creating the
     * XMLPipelineFilter.
     */
    public abstract XMLPipelineFilter createPipelineFilter(XMLPipeline pipeline)
            throws SAXException;

    /**
     * Create an XMLFilter that processes the SAX events in the pipeline before
     * passing them onto the various handlers.
     *
     * @param pipeline The pipeline to use to process the events.
     * @param setBaseURIOnRoot true if the base URI should bbe set on the root
     *                         element
     * @return An XMLPipelineFilter that wraps the specified pipeline.
     *
     * @throws SAXException If there was a problem creating the
     *                      XMLPipelineFilter.
     */
    public abstract XMLPipelineFilter createPipelineFilter(
        XMLPipeline pipeline, boolean setBaseURIOnRoot)
        throws SAXException;

    /**
     * Return an encapsulation of all the rules for all the built in operations
     * conditioners and drivers in a dynamic pipeline.
     * @return A DynamicRuleConfigurator that encapsulates all the rules for
     * all the built in operations and conditioners.
     */
    public abstract DynamicRuleConfigurator getRuleConfigurator();

    /**
     * Create an instance of an XMLProcess that can control the flow of events
     * through it.
     * <p>The returned process should be added to the pipeline just like any
     * other process.</p>
     * @return An XMLProcess that supports flow control.
     */
    public abstract XMLProcess createFlowControlProcess();

    /**
     * Create a new DynamicProcessConfiguration instance.
     * @return A new DynamicProcessConfiguration instance.
     */
    public abstract
            DynamicProcessConfiguration createDynamicProcessConfiguration();

    /**
     * Create a dynamic process.
     * <p>The behaviour of the dynamic process is determined by the
     * {@link
     * com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration}
     * instance that is stored in the pipeline configuration.</p>
     * @param configuration The pipeline configuration.
     * @return A new dynamic process.
     */
    public abstract DynamicProcess createDynamicProcess(
            XMLPipelineConfiguration configuration);

    /**
     * Create a dynamic process.
     * <p>The behaviour of the dynamic process is determined by the
     * {@link
     * com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration}
     * instance that is stored in the pipeline configuration.</p>
     * @param configuration The pipeline configuration.
     * @return A new dynamic process.
     */
    public abstract DynamicProcess createDynamicProcess(
            DynamicProcessConfiguration configuration);

    /**
     * Create a pipeline configuration object.
     * @return A newly created pipeline configuration object.
     */
    public abstract XMLPipelineConfiguration createPipelineConfiguration();

    /**
     * Return an <code>NamespaceFactory</code> instance
     * @return a NamespaceFactory object
     */
    public abstract NamespaceFactory getNamespaceFactory();

    /**
     * Return an <code>ExpressionFactory</code> instance
     * @return a ExpressionFactory object
     */
    public abstract ExpressionFactory getExpressionFactory();

    /**
     * Create a pipeline recorder that can be used to record the stream of
     * events flowing down the pipeline.
     *
     * @return The pipeline recorder.
     *
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    public abstract PipelineRecorder createPipelineRecorder();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 05-Feb-04	525/1	adrian	VBM:2004011902 fixed rework issues for baseuri support work

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 20-Jan-04	529/2	adrian	VBM:2004011904 Pipeline API updates in preparation for fully integrating ContextUpdating/Annotating processes

 20-Jan-04	527/1	adrian	VBM:2004011903 Added ContextAnnotationProcess and supporting classes

 01-Aug-03	258/6	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 25-Jun-03	102/1	sumit	VBM:2003061906 request:getParameter XPath function support

 06-Jun-03	26/2	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
