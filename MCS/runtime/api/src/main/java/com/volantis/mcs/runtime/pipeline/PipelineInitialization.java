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
package com.volantis.mcs.runtime.pipeline;

import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.CacheConfiguration;
import com.volantis.mcs.runtime.configuration.CacheOperationConfiguration;
import com.volantis.mcs.runtime.configuration.MCSConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.MarkupExtensionConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.MarkupExtensionsConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.PipelineConfiguration;
import com.volantis.mcs.runtime.configuration.pipeline.PipelinePluginConfiguration;
import com.volantis.mcs.runtime.pipeline.extensions.PipelineConfigurationExtensionFactory;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.DelegatingPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.cache.CacheProcessConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.convert.AbsoluteToRelativeURLConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterTuple;
import com.volantis.xml.pipeline.sax.convert.URLToURLCConfiguration;
import com.volantis.xml.pipeline.sax.convert.URLToURLCTuple;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfigurationImpl;
import com.volantis.xml.pipeline.sax.drivers.uri.FetchRule;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverFactory;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.pipeline.sax.dynamic.NamespaceRuleSet;
import com.volantis.xml.pipeline.sax.dynamic.rules.NoopRule;
import com.volantis.xml.pipeline.sax.operations.PipelineOperationFactory;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.url.URLContentCacheConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Factory class for creating the Pipeline factories and configurations
 * specifically for the MCS environment.
 */
public class PipelineInitialization {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
                LocalizationFactory.createLogger(PipelineInitialization.class);

    /**
     * An extra namespace required within Mariner to handle the additional "vt"
     * markup provided for pipeline use.
     */
    private static final Namespace CDM = new Namespace("cdm",
            XDIMESchemata.CDM_NAMESPACE) {
    };

    /**
     * An extra namespace required within Mariner to handle the
     * request-specific expression functions.
     *
     * @see com.volantis.mcs.expression.ExpressionSupport
     */
    private static final Namespace REQUEST = new Namespace("request") {
    };

    /**
     * An extra namespace required within Mariner to handle the
     * service-specific expression functions.
     *
     * @see com.volantis.mcs.expression.ExpressionSupport
     */
    private static final Namespace SERVICE = new Namespace("service") {
    };

    /**
     * An extra namespace required within Mariner to handle the device-specific
     * expression functions.
     *
     * @see com.volantis.mcs.expression.ExpressionSupport
     */
    private static final Namespace DEVICE = new Namespace("device") {
    };

    /**
     * An extra namespace required within Mariner to handle the layout-specific
     * expression functions.
     *
     * @see com.volantis.mcs.expression.ExpressionSupport
     */
    private static final Namespace LAYOUT = new Namespace("layout") {
    };

    /**
     * An extra namespace required within Mariner to handle the branding
     * specific expression functions.
     *
     * @see com.volantis.mcs.expression.ExpressionSupport
     */
    private static final Namespace BRANDING = new Namespace("branding") {
    };

    /**
     * The value of the debugOutputDir as specified in the MCS config file
     */
    private String debugOutputDir;

    /**
     * Used when calculating the file path that the debug process will generate
     */
    private static int debugOutputCounter;

    /**
     * The pipeline configuration
     */
    private XMLPipelineConfiguration pipelineConfiguration;

    /**
     * Check that nameSpaces have been initialised
     */
    private static boolean namespaceInitialised = false;

    /**
     * Initializes the new instance using the given parameters.
     *
     */
    public PipelineInitialization(MarinerConfiguration marinerConfiguration,
                                  MCSConfiguration mcsConfiguration,
                                  PluggableAssetTranscoder assetTranscoder) {

        if (null == marinerConfiguration) {
            throw new NullPointerException(
                    "MarinerConfiguration cannot be null");
        }
        if (null == mcsConfiguration) {
            throw new NullPointerException(
                    "MCSConfiguration cannot be null");
        }
        if (null == assetTranscoder) {
            throw new NullPointerException(
                    "PluggableAssetTranscoder cannot be null");
        }

        // --------------------------------------------------------------------
        // The order in which these objects are instantiated is VERY IMPORTANT!
        // --------------------------------------------------------------------

        // Make sure that the required Namespace URI configuration is
        // defined
        defineNamespaceURIs();

        // store the debug output dir away.
        this.debugOutputDir = marinerConfiguration.getPipelineConfiguration().
                getDebugOutputDirectory();
        // initialize the configuration
        pipelineConfiguration =
                createPipelineConfiguration(marinerConfiguration,
                        mcsConfiguration,
                        assetTranscoder);
    }

    /**
     * Returns an XMLPipelineFactory that can be use to factor
     * pipeline related objects.
     * @return an XMLPipelineFactory
     */
    public XMLPipelineFactory getPipelineFactory() {
        return new MCSPipelineFactory();
    }

    /**
     * Gets the pipeline configuration.
     *
     * @return the pipeline configuration
     */
    public XMLPipelineConfiguration getPipelineConfiguration() {
        return pipelineConfiguration;
    }

    /**
     * Factor the XMLPipelineConfiguration for all MCS pipelines
     * @return a XMLPipelineConfiguration instance
     */
    protected XMLPipelineConfiguration createPipelineConfiguration(
            MarinerConfiguration marinerConfiguration,
            MCSConfiguration mcsConfiguration,
            PluggableAssetTranscoder assetTranscoder) {

        XMLPipelineFactory pipelineFactory =
                XMLPipelineFactory.getDefaultInstance();
        // factor an empty XMLPipelineConfiguration instance. This is the
        // configuration that is required to run the pipeline.
        XMLPipelineConfiguration pipelineConfig =
                pipelineFactory.createPipelineConfiguration();

        PipelineConfiguration configFileInfo =
                marinerConfiguration.getPipelineConfiguration();

        // Create the caches
        final CacheProcessConfiguration cacheProcessConfiguration;
        final CacheOperationConfiguration cacheOperationConfiguration =
            configFileInfo.getCacheOperationConfiguration();
        if (cacheOperationConfiguration != null) {
            // @todo this seems a bit inefficient, iterating over the same
            // collection twice. But the collection is expected to be small.
            // Leaving for now.
            int cacheSize = calculateTotalMaxEntries(
                    cacheOperationConfiguration.getCacheConfigurations());
            cacheProcessConfiguration = new CacheProcessConfiguration(cacheSize);
            createCaches(cacheProcessConfiguration,
                cacheOperationConfiguration.getCacheConfigurations());
            cacheProcessConfiguration.setFixedExpiryMode(
                cacheOperationConfiguration.isFixedExpiryMode());
        } else {
            cacheProcessConfiguration = new CacheProcessConfiguration(1);
        }

        pipelineConfig.storeConfiguration(CacheProcessConfiguration.class,
                cacheProcessConfiguration);

        // Store the web services driver configuration in the pipeline
        // configurations class so that registration of web services
        // process' may occur.
        WSDriverConfiguration wsDriverConfiguration =
                (configFileInfo.getWsDriverConfiguration() != null)
                ? configFileInfo.getWsDriverConfiguration()
                : new WSDriverConfiguration();

        pipelineConfig.storeConfiguration(WSDriverConfiguration.class,
                wsDriverConfiguration);

        WebDriverConfiguration webDriverConfiguration =
                (configFileInfo.getWebDriverConfiguration() != null)
                ? configFileInfo.getWebDriverConfiguration()
                : WebDriverFactory.getDefaultInstance().createConfiguration();
        pipelineConfig.storeConfiguration(WebDriverConfiguration.class,
                webDriverConfiguration);

        // Store the transformation configuration in the pipeline
        // configurations class so that registration of transformation
        // process may occur. The transformation configuration determines
        // whether xslt or xsltc is used by default.

        TransformConfiguration transformConfiguration =
                (configFileInfo.getTransformConfiguration() != null)
                ? configFileInfo.getTransformConfiguration()
                : PipelineOperationFactory.getDefaultInstance().
                createTransformConfiguration();

        if(LOGGER.isInfoEnabled()){
            if(transformConfiguration.isTemplateCacheRequired()){
                LOGGER.info("transformation-cache-enabled");
            } else {
                LOGGER.info("transformation-cache-disabled");
            }
        }

        // store away the configuration for transforms
        pipelineConfig.storeConfiguration(TransformConfiguration.class,
                transformConfiguration);

        // Store the connection configuration away in the pipeline
        // configuration. This may be null if no configuration is specified
        ConnectionConfigurationImpl connectionConfiguration =
                (ConnectionConfigurationImpl)
                configFileInfo.getConnectionConfiguration();
        pipelineConfig.storeConfiguration(
                ConnectionConfiguration.class,
                connectionConfiguration);

        // Store the URLContentCache configuration if we have a connection
        // configuration.
        final URLContentCacheConfiguration urlContentCacheConfig =
            new URLContentCacheConfiguration(pipelineConfig);
        pipelineConfig.storeConfiguration(URLContentCacheConfiguration.class,
            urlContentCacheConfig);


        // Store the URL to URLC configuration in the pipeline
        // configurations instance so that registration of converter(s)
        // may occur.
        URLToURLCTuple[] tuples =
                {new URLToURLCTuple(null, "img", "url", "urlc"),
                 new URLToURLCTuple(null, "logo", "url", "urlc")};

        pipelineConfig.storeConfiguration(
                URLToURLCConfiguration.class,
                new URLToURLCConfiguration(
                        new TranscoderURLConverter(
                                assetTranscoder.getHostParameter(),
                                assetTranscoder.getPortParameter()),
                        tuples));


        // store away the configuration for the absolute to relative URL
        // converter process
        pipelineConfig.storeConfiguration(
                AbsoluteToRelativeURLConfiguration.class,
                createAbsoluteToRelativeURLConfiguration());

        // create the dynamic configuration
        DynamicProcessConfiguration dynamicConfiguration =
                pipelineFactory.createDynamicProcessConfiguration();

        // get hold of all the rules need for testing
        DynamicRuleConfigurator ruleConfigurator =
                pipelineFactory.getRuleConfigurator();

        // configure the dynamic configuration
        ruleConfigurator.configure(dynamicConfiguration);

        // add the rules that are specified via markup extensions
        addMarkupExtensionRules(configFileInfo, dynamicConfiguration);

        // add the extra MCS only rules
        addAdditionalRules(dynamicConfiguration);

        // store the dynamic configuration away in the pipeline configuration
        pipelineConfig.storeConfiguration(
                DynamicProcessConfiguration.class,
                dynamicConfiguration);

        // allow extensions to add information to the pipeline configuration
        PipelineConfigurationExtensionFactory.getDefaultInstance().
                extendPipelineConfiguration(pipelineConfig,
                        marinerConfiguration,
                        mcsConfiguration,
                        assetTranscoder);

        return pipelineConfig;
    }

    private void createCaches(
            final CacheProcessConfiguration cacheProcessConfiguration,
            final Iterator cacheConfigurations) {

        while (cacheConfigurations.hasNext()) {
            CacheConfiguration
                    cacheConfiguration =
                    (CacheConfiguration) cacheConfigurations.next();
            cacheProcessConfiguration.createCache(
                cacheConfiguration.getName(),
                cacheConfiguration.getMaxEntries(),
                cacheConfiguration.getMaxAge());
        }
    }

    /**
     * Return the total number of entries for the cache, based on the summation
     * of all of the CacheConfiguration maxEntries field.
     *
     * @param cacheConfigurations
     * @return integer for the total number of cache entries; >= 0.
     */
    private int calculateTotalMaxEntries(Iterator cacheConfigurations) {
        long result = 0;

        boolean unlimitedCache = false;

        while(!unlimitedCache && cacheConfigurations.hasNext()) {
            com.volantis.mcs.runtime.configuration.CacheConfiguration
                    cacheConfiguration =
                    (com.volantis.mcs.runtime.configuration.CacheConfiguration)
                            cacheConfigurations.next();

            if (!CacheConfiguration.UNLIMITED.equals(
                    cacheConfiguration.getMaxEntries())) {
                result += Long.parseLong(cacheConfiguration.getMaxEntries());
            } else {
                result = Integer.MAX_VALUE;
                unlimitedCache = true;
            }
        }

        return (result >= Integer.MAX_VALUE || 0 == result)
                ? Integer.MAX_VALUE : (int) result;
    }

    /**
     * Registers those rules that have been specified via the
     * /pipeline-configuratin/markup-extensions/markup-extension elements in
     * the MCS config file
     * @param pipelineConfiguration the PipelineConfiguration
     * @param dynamicConfiguration the DynamicProcessConfiguration
     */
    private void addMarkupExtensionRules(
                PipelineConfiguration pipelineConfiguration,
                DynamicProcessConfiguration dynamicConfiguration) {

        MarkupExtensionsConfiguration markupExtensions =
                    pipelineConfiguration.getMarkupExtensionsConfiguration();
        if (markupExtensions != null) {
            for (Iterator i=markupExtensions.getMarkupExtensionConfgurations();
                 i.hasNext();) {
                MarkupExtensionConfiguration markupExtension =
                            (MarkupExtensionConfiguration) i.next();

                String namespaceURI = markupExtension.getNamespaceURI();
                String localName = markupExtension.getLocalName();
                PipelinePluginConfiguration pluginConfiguration =
                            markupExtension.getPipelinePluginConfiguration();
                String className = pluginConfiguration.getClassName();

                // add the appropriate rule
                PipelinePluginConfiguration.PipelinePluginType pluginType =
                            pluginConfiguration.getType();
                DynamicElementRule rule = null;

                if (pluginType == PipelinePluginConfiguration.
                                PipelinePluginType.RULE) {
                    // the className specifies the class of rule that should
                    // be registered. Note if an error occurs trying to
                    // instantiate the rule an appropriate error will be logged
                    // with the offending rule being ignored. The rest of the
                    // initialisation will continue

                    try {
                        Class ruleClass = Class.forName(className);
                        rule = (DynamicElementRule) ruleClass.newInstance();
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("pipeline-init-class-not-found",
                                     className,
                                     e);
                    } catch (InstantiationException e) {
                        LOGGER.error("pipeline-init-cannot-instantiate-class",
                                     className,
                                     e);
                    } catch (IllegalAccessException e) {
                        LOGGER.error("pipeline-init-illegal-access",
                                     className,
                                     e);
                    }

                } else if (pluginType ==  PipelinePluginConfiguration.
                                PipelinePluginType.PROCESS) {
                    // the classname specifies the class of XMLProcess that
                    // a rule should add to the pipeline
                    rule = new IntrospectingAddProcessRule(className);
                }

                if (rule != null) {
                    // get hold of the rule set for the given namespace (creating
                    // it if it does not exist)
                    NamespaceRuleSet ruleSet =
                                dynamicConfiguration.getNamespaceRules(
                                            namespaceURI, true);
                    // finally add the rule to the rule set
                    ruleSet.addRule(localName, rule);
                }

            }
        }
    }
    /**
     * Method that adds MCS specific rules to the
     * <code>DynamicProcessConfiguration</code>
     * @param dynamicConfiguration the DynamicProcessConfiguration
     */
    protected void addAdditionalRules(
            DynamicProcessConfiguration dynamicConfiguration) {

        // the "vt" markup processes. These are each registered twice to
        // account for the fact that the MarlinContentHandler assumes the
        // default namespace is valid for "vt" markup, but we also allow
        // for explicit reference to the actual namespace for this markup.
        // These are only required in the XML environment since the JSP
        // environment handles everything in the hand-written JSP tags
        // directly

        // get the NamespaceRulesSet for the empty namespace
        NamespaceRuleSet rules
                = dynamicConfiguration.getNamespaceRules("", true);

        // add the rule for the usePipeline process
        rules.addRule("usePipeline", NoopRule.getDefaultInstance());

        // An alias for the urid:fetch process
        rules.addRule("include", FetchRule.getDefaultInstance());

        // get hold of the rule set for the CDM namespace
        rules = dynamicConfiguration.getNamespaceRules(CDM.getURI(),
                true);


        // add the rule for the usePipeline process
        rules.addRule("usePipeline", NoopRule.getDefaultInstance());

        // An alias for the urid:fetch process
        rules.addRule("include", FetchRule.getDefaultInstance());
    }

    /**
     * Defines the required Namespace URIs for the various namespaces
     * in the MCS pipeline environment.
     */
    public static void defineNamespaceURIs() {
        if (!namespaceInitialised) {
            final String COMMON_PREFIX = "http://www.volantis.com/xmlns/";

            // These URIs are structured slightly differently
            BRANDING.setURI(COMMON_PREFIX + "mcs/branding");
            REQUEST.setURI(COMMON_PREFIX + "mariner/request");
            SERVICE.setURI(COMMON_PREFIX + "mariner/service");
            DEVICE.setURI(COMMON_PREFIX + "mariner/device");
            LAYOUT.setURI(COMMON_PREFIX + "mariner/layout");
            namespaceInitialised = true;
        }
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
        };

        return new AbsoluteToRelativeURLConfiguration(tuples);
    }

    /**
     * An {@link XMLPipelineFactory} implementation that uses the MCS
     * pipeline configuration information when factoring a
     * {@link XMLPipelineConfiguration}
     */
    private class MCSPipelineFactory extends DelegatingPipelineFactory {

        //  javadoc inherited
        public XMLPipelineContext createPipelineContext(
                XMLPipelineConfiguration configuration,
                ExpressionContext expressionContext) {
            XMLPipelineContext context =
                    super.createPipelineContext(configuration,
                            expressionContext);
            context.setDebugOutputFilesPrefix(createDebugOutputDirPrefix());
            return context;
        }

        // javadoc inherited
        public XMLPipelineContext createPipelineContext(
                XMLPipelineConfiguration configuration,
                EnvironmentInteraction environmentInteraction) {
            XMLPipelineContext context =
                    super.createPipelineContext(configuration,
                            environmentInteraction);
            context.setDebugOutputFilesPrefix(createDebugOutputDirPrefix());
            return context;
        }

        // javadoc inherited
        public XMLPipelineConfiguration createPipelineConfiguration() {
            return pipelineConfiguration;
        }


        /**
         * Increments the counter that is used in the name of the files that
         * the {@link com.volantis.xml.pipeline.sax.impl.operations.debug.SerializeProcess}
         * writes to.
         * @return the incremented counter
         */
        private synchronized int incrementCounter() {
            if (++debugOutputCounter > 9999) {
                debugOutputCounter = 0;
            }
            return debugOutputCounter;
        }

        /**
         * Creates the next available Debug Output Directory prefix. This is
         * used by the
         * {@link com.volantis.xml.pipeline.sax.impl.operations.debug.SerializeProcess}
         * to create a unique file name for the serialized output.
         * The format of this prefix is
         * yyyMMdd-hhmmss-0001-
         * where 0001 is a rolling counter.
         * @return the debug output directory prefix.
         */
        private String createDebugOutputDirPrefix() {
            String debugOutputDirPrefix = null;
            if (debugOutputDir != null) {
                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyyMMdd-hhmmss");
                String timestamp = dateFormat.format(new Date());
                StringBuffer buffer = new StringBuffer();
                int count = incrementCounter();
                buffer.append(debugOutputDir)
                        .append(timestamp)
                        .append('-');
                // write out the rolling debugOutputCounter in four digit
                // string form. For example - if the debugOutputCounter
                // is 9 we would write out 0009.
                if (count < 1000) {
                    buffer.append(0);
                }
                if (count < 100) {
                    buffer.append(0);
                }
                if (count < 10) {
                    buffer.append(0);
                }
                buffer.append(count).append('-');
                debugOutputDirPrefix = buffer.toString();
            }
            return debugOutputDirPrefix;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/3	geoff	VBM:2005100507 Mariner Export fails with NPE

 04-Oct-05	9724/1	philws	VBM:2005092810 Port forward of the generic pipeline connection timeout functionality

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 04-Oct-05	9679/1	philws	VBM:2005092810 Provide a connection timeout mechanism and configuration for pipeline operations

 18-May-05	7950/1	allan	VBM:2005041317 Some testcases for smart server

 11-Apr-05	7376/5	allan	VBM:2005031101 SmartClient bundler - commit for testing

 04-Apr-05	6798/10	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 01-Apr-05	6798/8	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Nov-04	6109/2	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names

 12-Aug-04	5181/1	allan	VBM:2004081106 Support branding post MCS 3.0.

 10-Jun-04	4683/1	ianw	VBM:2004052407 Reduced object creation in MarinerExpressionSupport

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 08-Jan-04	2461/1	steve	VBM:2003121701 Patch pane name changes from Proteus2

 07-Jan-04	2389/3	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/1	steve	VBM:2003121701 Pre-test save

 21-Nov-03	2000/1	steve	VBM:2003111907 Patched from Proteus2

 21-Nov-03	1987/1	steve	VBM:2003111907 usePipeline tag and element fixes.

 11-Aug-03	1029/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 08-Aug-03	1001/5	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process - create external Tuple classes

 08-Aug-03	1001/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 08-Aug-03	906/11	chrisw	VBM:2003072905 resolved merge conflict in PipelineInitialisation

 07-Aug-03	906/8	chrisw	VBM:2003072905 Public API changed for transform configuration

 07-Aug-03	906/5	chrisw	VBM:2003072905 Public API changed for transform configuration

 07-Aug-03	981/4	philws	VBM:2003080605 Fix merge problems

 07-Aug-03	981/1	philws	VBM:2003080605 Provide Transforce PictureIQ specific pipeline URL to URLC conversion

 07-Aug-03	921/6	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy - rework issue merge conflicts resolved take 3

 06-Aug-03	954/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 04-Aug-03	880/6	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 01-Aug-03	880/3	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 01-Aug-03	923/1	philws	VBM:2003080103 Fix the DEVICE and REQUEST namespace URIs

 31-Jul-03	828/1	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 02-Jul-03	601/9	doug	VBM:2003062306 Fix SQL conditioner namespace issue

 01-Jul-03	607/3	doug	VBM:2003062709 PipelineInitialization updates

 01-Jul-03	658/4	philws	VBM:2003062506 Provide includeJSP and includeServlet instead of includeServerResource

 30-Jun-03	625/5	byron	VBM:2003022823 Support web service integration within a JSP page

 30-Jun-03	492/8	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector - part 3

 30-Jun-03	492/5	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector - part 2

 25-Jun-03	492/2	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 30-Jun-03	629/4	philws	VBM:2003062508 Fix merge problems

 30-Jun-03	629/1	philws	VBM:2003062508 Rename sql-connector to sql-driver

 30-Jun-03	552/3	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 25-Jun-03	473/3	philws	VBM:2003061913 Renaming of pipeline:includeURI, renaming of vt:include to vt:usePipeline and intro of new vt:include

 23-Jun-03	487/3	sumit	VBM:2003062001 Added inculdeSrvRes to the PipelineInitialisation

 23-Jun-03	487/1	sumit	VBM:2003062001 Added inculdeSrvRes to the PipelineInitilisation

 19-Jun-03	452/1	philws	VBM:2003061905 MCS Template Model implementation

 16-Jun-03	366/1	doug	VBM:2003041502 Integration with pipeline JSPs

 ===========================================================================
*/
