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

import com.volantis.synergetics.testtools.HypersonicManager;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.convert.AbsoluteToRelativeURLConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterTuple;
import com.volantis.xml.pipeline.sax.convert.TestURLCGenerator;
import com.volantis.xml.pipeline.sax.convert.URLToURLCConfiguration;
import com.volantis.xml.pipeline.sax.convert.URLToURLCTuple;
import com.volantis.xml.pipeline.sax.drivers.web.SimpleScriptModule;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfigurationImpl;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.operations.PipelineOperationFactory;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.proxy.DefaultProxy;
import com.volantis.xml.pipeline.testtools.sqlconnector.HypersonicHelper;

/**
 * Helper class that sets up a pipeline factory for integration testing
 */
public class IntegrationTestHelper {



    /**
     * Returns an XMLPipelineFactory that can be use to factor pipeline
     * related objects.
     * @return an XMLPipelineFactory
     */
    public XMLPipelineFactory getPipelineFactory()  {
        XMLPipelineFactory pipelineFactory = new TestPipelineFactory();
        return pipelineFactory;
    }

    /**
     * Returns an XMLPipelineFactory that can be use to factor pipeline
     * related objects.  The expression process of the pipeline will evaluate
     * %{} and {} expressions.
     * @return an XMLPipelineFactory
     */
    public XMLPipelineFactory getMultiExprPipelineFactory()  {
        XMLPipelineFactory pipelineFactory =
                new MultiExprTestPipelineFactory();
        return pipelineFactory;
    }

    /**
     * Return the configuration needed for an Operation Pipeline
     * @return an XMLPipelineConfiguration instance
     * @throws Exception if an error occurs
     */
    public XMLPipelineConfiguration
            getPipelineConfiguration() throws Exception {

        XMLPipelineConfiguration pipelineConfig =
                getPipelineFactory().createPipelineConfiguration();

        setUpPipelineConfiguration(pipelineConfig);

        return pipelineConfig;
    }

    /**
     * Populates the XMLPipelineConfiguration passed in. This method is needed
     * as the TestPipelineFactory creates its own XMLPipelineConfiguration when
     * it creates a XMLPipelineContext
     * @param pipelineConfig XMLPipelineConfiguration to be populated
     */
    public void setUpPipelineConfiguration(
            XMLPipelineConfiguration pipelineConfig) {

        // store away the configuration for the web driver process
        pipelineConfig.storeConfiguration(WebDriverConfiguration.class,
                                          createWebDriverConfiguration());

        // store away the configuration for the web service driver process
        pipelineConfig.storeConfiguration(WSDriverConfiguration.class,
                                          createWSDriverConfiguration());

        // store away the configuration for the transform process
        pipelineConfig.storeConfiguration(TransformConfiguration.class,
          PipelineOperationFactory.getDefaultInstance().createTransformConfiguration());

        // store away the configuration for the url to urlc converter process
        pipelineConfig.storeConfiguration(
                URLToURLCConfiguration.class,
                createURLToURLCConverterConfiguration());

        // store away the configuration for the url to urlc converter process
        pipelineConfig.storeConfiguration(
                AbsoluteToRelativeURLConfiguration.class,
                createAbsoluteToRelativeURLConfiguration());

        // create the dynamic configuration
        DynamicProcessConfiguration dynamicConfiguration =
                getPipelineFactory().createDynamicProcessConfiguration();

        // get hold of all the rules need for testing
        DynamicRuleConfigurator ruleConfigurator =
                getPipelineFactory().getRuleConfigurator();

        // configure the dynamic configuration
        ruleConfigurator.configure(dynamicConfiguration);

        // store the dynamic configuration away in the pipeline configuration
        pipelineConfig.storeConfiguration(
                DynamicProcessConfiguration.class,
                dynamicConfiguration);

    }

    /**
     * Factor the <code>WSDriverConfiguration<code> that the web driver
     * process will require for testing
     * @return a WebDriverConfiguration instance
     */
    public WSDriverConfiguration createWSDriverConfiguration() {
        return new WSDriverConfiguration();
    }

    /**
     * Factor the <code>WebDriverConfiguration<code> that the web driver
     * process will require for testing
     * @return a WebDriverConfiguration instance
     */
    public WebDriverConfiguration createWebDriverConfiguration() {
        // create a configuration
        WebDriverConfiguration config = new WebDriverConfigurationImpl();

        // create a proxy
        DefaultProxy proxy = new DefaultProxy();
        proxy.setId("proxyRef");
        proxy.setPort(8080);
        proxy.setHost("volantis");
        config.putProxy(proxy);

        // create a script module
        SimpleScriptModule script = new SimpleScriptModule();
        script.setId("scriptRef");
        config.putScriptModule(script);

        // return the configuration
        return config;
    }

    /**
     * Factor the <code>URLToURLCConfiguration<code> that the url to urlc
     * converter process will require for testing
     * @return a URLToURLCConfiguration instance
     */
    public ConverterConfiguration createURLToURLCConverterConfiguration() {

        // create the Tuples
        URLToURLCTuple[] tuples =
                {new URLToURLCTuple(null, "img", "url", "urlc"),
                 new URLToURLCTuple(null, "logo", "url", "urlc"),
                 new URLToURLCTuple("http://www.volantis.com/tuple",
                                    "img", "src", null)};

        // return the test configuration
        return new URLToURLCConfiguration(new TestURLCGenerator(),
                                          tuples);

    }


    /**
     * Create a new <code>AbsoluteToRelativeURLConfiguration</code> object with
     * default testable data.
     *
     * @return      an <code>AbsoluteToRelativeURLConfiguration</code>
     *              instance.
     */
    private AbsoluteToRelativeURLConfiguration
        createAbsoluteToRelativeURLConfiguration() {

        ConverterTuple[] tuples = {
            new ConverterTuple(null, "a", "href"),
            new ConverterTuple(null, "form", "action"),
            new ConverterTuple(
                "http://www.volantis.com/tuple", "image", "src")
        };

        return new AbsoluteToRelativeURLConfiguration(tuples);
    }

    /**
     * Helper function that registers an expression function with the
     * given expression context. The function will take no arguments
     * and return an empty {@link Sequence} when invoked. The function
     * will be registered under the "integration" namespace.
     * @param context the ExpressionContext
     */
    public void registerEmptySequenceFunction(
            ExpressionContext context) {

        // Function that returns an empty sequence
        Function emptySequenceFn = new Function() {
            // javadoc inherited
            public Value invoke(ExpressionContext context,
                                Value[] arguments) {
                // return the empty sequence
                return Sequence.EMPTY;
            }
        };

        Namespace integrationNamespace = Namespace.literal("integration");

        // register this function with the expression context
        context.registerFunction(
                new ImmutableExpandedName(
                        integrationNamespace.getURI(),
                        "emptySequence"),
                emptySequenceFn);

        // ensure the prefix tracker is aware of this namespace
        NamespacePrefixTracker tracker = context.getNamespacePrefixTracker();
        tracker.startPrefixMapping(integrationNamespace.toString(),
                                   integrationNamespace.getURI());
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/2	matthew	VBM:2005092809 Allow proxy configuration via system properties

 15-Jun-05	8751/1	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 01-Apr-05	6798/2	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-04	751/5	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 07-Nov-03	447/4	doug	VBM:2003110405 Addressed some minor rework issues

 06-Nov-03	447/2	doug	VBM:2003110405 Added transaction support

 04-Nov-03	438/4	doug	VBM:2003091803 Added parameter value processes

 27-Oct-03	421/2	doug	VBM:2003101601 Added support for sql update statements

 11-Aug-03	275/2	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 08-Aug-03	308/5	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	308/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 07-Aug-03	268/6	chrisw	VBM:2003072905 Public API changed for transform configuration

 06-Aug-03	301/8	doug	VBM:2003080503 Refactored URLToURLCConverter process to use DynamicElementRules

 06-Aug-03	301/4	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 04-Aug-03	217/2	allan	VBM:2003071702 Filter nested anchors. Fixed merge conflicts.

 31-Jul-03	238/4	byron	VBM:2003072309 Create the adapter process for parent task v4

 30-Jul-03	238/2	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 01-Aug-03	258/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 30-Jun-03	154/4	sumit	VBM:2003062506 Changed includeServerResources to includeJSP and includeServlet

 30-Jun-03	154/1	sumit	VBM:2003062506 Changed includeServerResources to includeJSP and includeServlet

 30-Jun-03	157/2	philws	VBM:2003062508 Rename sql-connector to sql-driver

 30-Jun-03	137/4	byron	VBM:2003022823 Support web service integration within a JSP page - update

 27-Jun-03	137/1	byron	VBM:2003022823 Support web service integration within a JSP page

 27-Jun-03	127/6	doug	VBM:2003062306 Column Conditioner Modifications

 27-Jun-03	127/4	doug	VBM:2003062306 Column Conditioner Modifications

 24-Jun-03	109/4	philws	VBM:2003061913 Change pipeline:includeURI to urid:fetch and add new TLD for it

 19-Jun-03	90/2	adrian	VBM:2003061606 Added Expression support to Tag attributes

 17-Jun-03	92/2	philws	VBM:2003061611 Fix namespace definitions for integration tests

 16-Jun-03	78/2	philws	VBM:2003061205 Add JSP test cases and debug some issues

 13-Jun-03	68/4	allan	VBM:2003022821 Fix testcase problems with IntegrationTestHelper and HypersonicManager

 13-Jun-03	68/2	allan	VBM:2003022821 SQL Connector JSP tags.

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 ===========================================================================
*/
