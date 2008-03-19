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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/XMLConfigurationBuilder.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a
 *                              ConfigurationBuilder which builds the
 *                              configuration from an InputSource which refers
 *                              to an "instance" of mariner-config.dtd.
 * 25-Apr-03    Sumit           VBM:2003041502 - Added support for XML pipeline
 *                              in the mariner configuration files
 *                              to an "instance" of mariner-config.dtd.
 * 30-Apr-03    Chris W         VBM:2003041503 - JspRuleSet added in constructor
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;


import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.ConfigurationBuilder;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.xml.digester.MarinerDigester;
import com.volantis.mcs.runtime.configuration.xml.pipeline.PipelineConfigurationRuleSet;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import our.apache.commons.digester.Digester;

/**
 * A {@link ConfigurationBuilder} which builds the configuration from an
 * {@link InputSource} which refers to an "instance" of mcs-config.dtd.
 *
 * NOTE: There are many inconsistencies in the mcs-config, which I have not
 * attempted to "refactor" as the whole thing is scheduled to be dumped in an
 * upcoming version. These include, but are not limited to:
 * <li>
 *   <ul>Element and attribute names follow multiple different naming styles,
 *      some use "namesLikeThis", some use "names-like-this", some use
 *      "nameslikethis" (Digester can handle this very easily).
 *   <ul>Elements may be disabled in multiple different ways. Some should
 *      merely be commented out, others may also have an enabled="false"
 *      attribute set to achive the same result (Automagic support for ignoring
 *      elements with enabled="false" set has been implemented via the various
 *      Enabled classes).
 * </li>
 * NOTE: some of the above problems were fixed as part of the move from
 * mariner-config to mcs-config, but there are still some issues remaining.
 */
public class XMLConfigurationBuilder implements ConfigurationBuilder {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(XMLConfigurationBuilder.class);

    private static final SchemaValidator CONFIG_VALIDATOR;
    static {
        SchemaValidator validator = new SchemaValidator();
        validator.addSchema(ConfigSchemas.MCS_3_5_CONFIG_SCHEMA);
        CONFIG_VALIDATOR = validator;
    }

    public static final String SCHEMA_NAMESPACE =
            "http://www.volantis.com/xmlns/mcs/config";

    public static final String SCHEMA_NAME = "mcs-config.xsd";

    public static final String SCHEMA_LOCATION =
            "http://www.volantis.com/schema/config/v1.0/" + SCHEMA_NAME;

    private static final String ROOT_ELEMENT = "mcs-config";

    private Digester digester;

    private InputSource inputSource;

    /**
     * Default constructor.
     */
    public XMLConfigurationBuilder() throws ConfigurationException {

        try {
            digester = createDigester();
        } catch (SAXException e) {
            throw new ConfigurationException(
                        exceptionLocalizer.format("digester-creation-error"),
                        e);
        }

        MarinerRuleSet marinerSet = new MarinerRuleSet("");
        marinerSet.addRuleInstances(digester);

        JNDIConfigurationRuleSet jndiConfigurationSet =
                new JNDIConfigurationRuleSet(ROOT_ELEMENT);
        jndiConfigurationSet.addRuleInstances(digester);

        ManagementRuleSet managementRuleSet =
                new ManagementRuleSet(ROOT_ELEMENT);
        managementRuleSet.addRuleInstances(digester);


        DataSourcesRuleSet dataSourcesSet =
                new DataSourcesRuleSet(ROOT_ELEMENT);
        dataSourcesSet.addRuleInstances(digester);

        PolicyCachesRuleSet cacheSet = new PolicyCachesRuleSet(ROOT_ELEMENT);
        cacheSet.addRuleInstances(digester);

        LocalRepositoryRuleSet localRepositorySet =
                new LocalRepositoryRuleSet(ROOT_ELEMENT);
        localRepositorySet.addRuleInstances(digester);

        ProjectsRuleSet projectsSet = new ProjectsRuleSet(ROOT_ELEMENT);
        projectsSet.addRuleInstances(digester);

        // Add the stylesheets rule set.
        StyleSheetsRuleSet stylesheetSet = new StyleSheetsRuleSet(ROOT_ELEMENT);
        stylesheetSet.addRuleInstances(digester);

        AgentRuleSet agentSet = new AgentRuleSet(ROOT_ELEMENT);
        agentSet.addRuleInstances(digester);

        RemotePoliciesRuleSet remotePoliciesSet =
                new RemotePoliciesRuleSet(ROOT_ELEMENT);
        remotePoliciesSet.addRuleInstances(digester);

        AppServerRuleSet appServerSet =
                new AppServerRuleSet(ROOT_ELEMENT);
        appServerSet.addRuleInstances(digester);

        DebugRuleSet debugSet = new DebugRuleSet(ROOT_ELEMENT);
        debugSet.addRuleInstances(digester);

        ProtocolsRuleSet protocolsSet = new ProtocolsRuleSet(ROOT_ELEMENT);
        protocolsSet.addRuleInstances(digester);

        // RuleSet for the <pipeline-configuration> element
        PipelineConfigurationRuleSet pipelineRuleSet =
                    new PipelineConfigurationRuleSet(ROOT_ELEMENT);

        pipelineRuleSet.addRuleInstances(digester);

        final String MCS_PLUGIN_CONFIG =
                ROOT_ELEMENT + "/mcs-plugins";

        MarkupPluginRuleSet markupPluginRuleSet =
                new MarkupPluginRuleSet(MCS_PLUGIN_CONFIG);
        markupPluginRuleSet.addRuleInstances(digester);

        DevicesRuleSet deviceRules = new DevicesRuleSet(ROOT_ELEMENT);
        deviceRules.addRuleInstances(digester);

        ServletFilterRuleSet servletFilterRules =
                new ServletFilterRuleSet(ROOT_ELEMENT);
        servletFilterRules.addRuleInstances(digester);


        HTTPProxyRuleSet httpProxyRules =
                new HTTPProxyRuleSet(ROOT_ELEMENT);
        httpProxyRules.addRuleInstances(digester);
        
        MediaAccessProxyRuleSet mapRules =
                new MediaAccessProxyRuleSet(ROOT_ELEMENT);
        mapRules.addRuleInstances(digester);
    }

    /**
     * Add rule sets for application plugins.
     * <p>
     * This rule set must add rules to operate with the
     *
     * @param ruleSet a ruleset for parsing application plugins.
     */
    public void addApplicationPluginRuleSet(PrefixRuleSet ruleSet) {
        ruleSet.setPrefix(ROOT_ELEMENT + "/application-plugins");
        ruleSet.addRuleInstances(digester);
    }

    protected Digester createDigester()
            throws SAXException {
        // Use a hardcoded XML reader rather than let digester use
        // JAXP since Weblogic 6.0 doesn't like that apparently.
        return new MarinerDigester(createXMLReader());
    }

    protected XMLReader createXMLReader()
            throws SAXException {
        return CONFIG_VALIDATOR.createValidatingReader(false);
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    /**
     * Register the specified DTD URL for the specified public identifier.
     * This must be called before the first call to
     * {@link #buildConfiguration}.
     *
     * @param publicId Public identifier of the DTD to be resolved
     * @param entityURL The URL to use for reading this DTD
     */
    public void register(String publicId, String entityURL) {
        digester.register(publicId, entityURL);
    }

    public MarinerConfiguration buildConfiguration()
            throws ConfigurationException {
        MarinerConfiguration config;
        try {
            config = (MarinerConfiguration) digester.parse(inputSource);
        } catch (Exception e) {
            throw new ConfigurationException(
                        exceptionLocalizer.format("config-parse-error"), e);
        }
        return config;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/10	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 25-Jan-05	6712/2	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 11-Jan-05	6413/1	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 23-Nov-04	6241/5	philws	VBM:2004111209 Enable resource-based access to the MCS configuration schema in the ConfigFileBuilder

 18-Nov-04	6241/3	philws	VBM:2004111209 Fix ConfigFileBuilder resolution of the MCS XSD location

 02-Jul-04	4702/11	matthew	VBM:2004061402 supermerge problems

 29-Jun-04	4726/7	claire	VBM:2004060803 Fixed supermerge problems

 28-Jun-04	4726/5	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 16-Mar-04	2867/9	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/6	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 15-Mar-04	2736/8	steve	VBM:2003121104 Supermerged

 02-Mar-04	2736/5	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 23-Jan-04  2736/2  steve   VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04  2685/1  steve   VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 06-Jan-04	2271/4	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 07-Aug-03	906/4	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	906/1	chrisw	VBM:2003072905 implemented compilable attribute on transform

 05-Aug-03	921/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 25-Jun-03	492/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 12-Jun-03	316/4	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
