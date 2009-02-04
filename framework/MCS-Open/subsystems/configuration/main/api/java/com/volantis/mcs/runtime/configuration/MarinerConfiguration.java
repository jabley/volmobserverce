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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/MarinerConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration
 *                              information about various top level items.
 * 28-Apr-03    Sumit           VBM:2003041502 - Added xml-process containment
 *                              information about various top level items.
 * 30-Apr-03    Chris W         VBM:2003041503 - Added jsp attribute and get
 *                              & set methods.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.pipeline.PipelineConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Holds configuration information about various top level items.
 * <p>
 * Used to hold items which do not justify a separate object of their own.
 *
 * @todo This class needs to have it's JavaDoc sorted - in a big way!
 */
public class MarinerConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";


    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MarinerConfiguration.class);

    // Simple attributes

    private Boolean imdRepositoryEnabled;

    private String pageMessageHeading;

    private String chartImagesBase;

    private String modeSetsBase;

    private String scriptsBase;

    private String urlRewriterPluginClass;

    private String pageURLRewriterPluginClass;

    private Integer stylesheetMaxAge;

    private String assetURLRewriterPluginClass;

    private String assetTranscoderPluginClass;

    private Boolean pagePackagingMimeEnabled;

    private Boolean sessionProxyCookieMappingEnabled;

    // Compound attributes

    private JNDIConfiguration jndiConfiguration;

    private LocalRepositoryConfiguration localRepository;

    private DataSourcesConfiguration dataSourcesConfiguration;

    private ProjectsConfiguration projects;

    private PolicyCaches policies;

    private StyleSheetsConfig stylesheetConfiguration;

    private AgentConfiguration agent;

    private AssetsConfiguration defaultAssetUrls;

    private RemotePoliciesConfiguration remotePolicies;

    private AppServerConfiguration appServer;

    private DebugConfiguration debug;

    private ProtocolsConfiguration protocols;


    private HTTPProxyConfiguration httpProxyConfiguration;

    /**
     * The servlet filter configuration.
     */
    private ServletFilterConfiguration servletFilterConfiguration;

    /**
     * is the management system enabled.
     */
    private ManagementConfiguration managementConfiguration;

    private MediaAccessProxyConfiguration mapConfiguration;

    /**
     * Devices and device repository configuration object
     */
    private DevicesConfiguration devices;

    private Map applicationPlugins = new HashMap();

    private List markupPlugins = new ArrayList();

    private IntegrationPluginConfigurationContainer markupPlugins2 =
            new IntegrationPluginConfigurationContainer();

    private IntegrationPluginConfigurationContainer selectionMethodPlugins =
            new IntegrationPluginConfigurationContainer();

    /**
     * Stores the pipeline configuration data.
     */
    private PipelineConfiguration pipelineConfiguration;

    /**
     * Return the ManagementConfiguration
     * @return
     */
    public ManagementConfiguration getManagementConfiguration() {
        return this.managementConfiguration;
    }

    /**
     * Set the ManagementConfiguration property.
     */
    public void setManagementConfiguration(
            ManagementConfiguration managementConfiguration){
        this.managementConfiguration = managementConfiguration;
    }

    public Boolean getImdRepositoryEnabled() {
        return imdRepositoryEnabled;
    }

    public void setImdRepositoryEnabled(Boolean imdRepositoryEnabled) {
        this.imdRepositoryEnabled = imdRepositoryEnabled;
    }

    public String getPageMessageHeading() {
        return pageMessageHeading;
    }

    public void setPageMessageHeading(String pageMessageHeading) {
        this.pageMessageHeading = pageMessageHeading;
    }

    /**
     * Logs a warning now that logging configuration is not done with the
     * mcs-config.xml file.
     */
    public void setLog4jConfigFile(String log4jConfigFile) {
        logger.warn("mcs-config-logging-deprecated");
    }

    public String getChartImagesBase() {
        return chartImagesBase;
    }

    public void setChartImagesBase(String chartImagesBase) {
        this.chartImagesBase = chartImagesBase;
    }

    public String getModeSetsBase() {
        return modeSetsBase;
    }

    public void setModeSetsBase(String modeSetsBase) {
        this.modeSetsBase = modeSetsBase;
    }

    public String getScriptsBase() {
        return scriptsBase;
    }

    public void setScriptsBase(String scriptsBase) {
        this.scriptsBase = scriptsBase;
    }

    public String getUrlRewriterPluginClass() {
        return urlRewriterPluginClass;
    }

    public void setUrlRewriterPluginClass(String urlRewriterPluginClass) {
        this.urlRewriterPluginClass = urlRewriterPluginClass;
    }

    public String getPageURLRewriterPluginClass() {
        return pageURLRewriterPluginClass;
    }

    public void setPageURLRewriterPluginClass(
            String pageURLRewriterPluginClass) {
        this.pageURLRewriterPluginClass = pageURLRewriterPluginClass;
    }

    public Integer getStylesheetMaxAge() {
        return stylesheetMaxAge;
    }

    public void setStylesheetMaxAge(Integer stylesheetMaxAge) {
        this.stylesheetMaxAge = stylesheetMaxAge;
    }

    public String getAssetURLRewriterPluginClass() {
        return assetURLRewriterPluginClass;
    }

    public void setAssetURLRewriterPluginClass(
            String assetURLRewriterPluginClass) {
        this.assetURLRewriterPluginClass = assetURLRewriterPluginClass;
    }

    public String getAssetTranscoderPluginClass() {
        return assetTranscoderPluginClass;
    }

    public void setAssetTranscoderPluginClass(
        String assetTranscoderPluginClass) {
        this.assetTranscoderPluginClass = assetTranscoderPluginClass;
    }

    public Boolean getPagePackagingMimeEnabled() {
        return pagePackagingMimeEnabled;
    }

    public void setPagePackagingMimeEnabled(Boolean pagePackagingMimeEnabled) {
        this.pagePackagingMimeEnabled = pagePackagingMimeEnabled;
    }

    public Boolean getSessionProxyCookieMappingEnabled() {
        return sessionProxyCookieMappingEnabled;
    }

    public void setSessionProxyCookieMappingEnabled(
            Boolean sessionProxyCookieMappingEnabled) {
        this.sessionProxyCookieMappingEnabled =
                sessionProxyCookieMappingEnabled;
    }

    public LocalRepositoryConfiguration getLocalRepository() {
        return localRepository;
    }

    public void setLocalRepository(
            LocalRepositoryConfiguration localRepository) {
        this.localRepository = localRepository;
    }

    public ProjectsConfiguration getProjects() {
        return projects;
    }

    public void setProjects(ProjectsConfiguration projects) {
        this.projects = projects;
    }

    // javadoc unnecessary
    public PipelineConfiguration getPipelineConfiguration() {
        return pipelineConfiguration;
    }

    // javadoc unnecessary
    public void setPipelineConfiguration(PipelineConfiguration pipelineConfiguration) {
        this.pipelineConfiguration = pipelineConfiguration;
    }

    public DevicesConfiguration getDevices() {
        return devices;
    }

    public void setDevices(DevicesConfiguration devices) {
        this.devices = devices;
    }

    public PolicyCaches getPolicies() {
        return policies;
    }

    public void setPolicies(PolicyCaches policies) {
        this.policies = policies;
    }

    public StyleSheetsConfig getStylesheetConfiguration() {
        return stylesheetConfiguration;
    }

    public void setStylesheetConfiguration(
            StyleSheetsConfig stylesheetConfiguration) {
        this.stylesheetConfiguration = stylesheetConfiguration;
    }

    public AgentConfiguration getAgent() {
        return agent;
    }

    public void setAgent(AgentConfiguration agent) {
        this.agent = agent;
    }

    public AssetsConfiguration getDefaultAssetUrls() {
        return defaultAssetUrls;
    }

    public void setDefaultAssetUrls(AssetsConfiguration defaultAssetUrls) {
        this.defaultAssetUrls = defaultAssetUrls;
    }

    public RemotePoliciesConfiguration getRemotePolicies() {
        return remotePolicies;
    }

    public void setRemotePolicies(RemotePoliciesConfiguration remotePolicies) {
        this.remotePolicies = remotePolicies;
    }

    public AppServerConfiguration getAppServer() {
        return appServer;
    }

    public void setAppServer(AppServerConfiguration appServer) {
        this.appServer = appServer;
    }

    public DebugConfiguration getDebug() {
        return debug;
    }

    public void setDebug(DebugConfiguration debug) {
        this.debug = debug;
    }

    /**
     * Return the current protocol configuration
     * @return the current protocol configuration
     */
    public ProtocolsConfiguration getProtocols() {
        return protocols;
    }

    /**
     * Set the protocol configuration. This is called by the digester rule set.
     * @param protocols the protocols configuration to use.
     */
    public void setProtocols(ProtocolsConfiguration protocols) {
        this.protocols = protocols;
    }


    public void addApplicationPlugin(ApplicationPluginConfiguration plugin) {
        applicationPlugins.put(plugin.getName(), plugin);
    }

    public ApplicationPluginConfiguration getApplicationPlugin(String name) {
        return (ApplicationPluginConfiguration) applicationPlugins.get(name);
    }

    /**
     * Add a {@link MarkupPluginConfiguration}
     * @param config a {@link MarkupPluginConfiguration}
     */
    public void addMarkupPlugin(MarkupPluginConfiguration config) {
        markupPlugins.add(config);
        markupPlugins2.addPlugin(config);
    }

    /**
     * Get the Iterator of the List of {@link MarkupPluginConfiguration}
     * @return the Iterator of the List of {@link MarkupPluginConfiguration}
     */
    public Iterator getMarkupPluginsListIterator() {
        return markupPlugins.iterator();
    }

    public IntegrationPluginConfigurationContainer getMarkupPlugins() {
        return markupPlugins2;
    }

    public IntegrationPluginConfigurationContainer getSelectionMethodPlugins() {
        return selectionMethodPlugins;
    }

    /**
     * Get the DataSourcesConfiguration
     * @return the DataSourcesConfiguration.
     */
    public DataSourcesConfiguration getDataSourcesConfiguration() {
        return dataSourcesConfiguration;
    }

    /**
     * Set the DataSourcesConfiguration
     * @param configuration The DataSourcesConfiguration to set.
     */
    public void setDataSourcesConfiguration(
            DataSourcesConfiguration configuration) {
        dataSourcesConfiguration = configuration;
    }

    /**
     * This method returns the JNDIConfiguration object.
     * @return The JNDIConfiguration object
     */
    public JNDIConfiguration getJndiConfiguration() {
        return jndiConfiguration;
    }

    /**
     * Sets the JNDIConfiguration object
     * @param configuration
     */
    public void setJndiConfiguration(JNDIConfiguration configuration) {
        jndiConfiguration = configuration;
    }

    /**
     * Get the ServletFilterConfiguration.
     * @return the ServletFilterConfiguration
     */
    public ServletFilterConfiguration getServletFilterConfiguration() {
        return servletFilterConfiguration;
    }

    /**
     * Sets the ServletFilterConfiguration object.
     * @param config the configuration to set
     */
    public void setServletFilterConfiguration(
            ServletFilterConfiguration config) {
        servletFilterConfiguration = config;
    }


    /**
     * Get the HTTPProxyConfiguration.
     *
     * @return the HTTPProxyConfiguration.
     */
    public HTTPProxyConfiguration getHTTPProxyConfiguration() {
        return httpProxyConfiguration;
    }

    /**
     * Sets the HTTPProxyConfiguration object.
     *
     * @param config the configuration to set
     */
    public void setHTTPProxyConfiguration(HTTPProxyConfiguration config) {
        httpProxyConfiguration = config;
    }

    /**
     * Gets the MediaAccessProxyConfiguration.
     *
     * @return the MediaAccessProxyConfiguration.
     */
    public MediaAccessProxyConfiguration getMediaAccessProxyConfiguration() {
        return mapConfiguration;
    }

    /**
     * Sets the MediaAccessProxyConfiguration object.
     *
     * @param config the MAP configuration to set
     */
    public void setMediaAccessProxyConfiguration(
            final MediaAccessProxyConfiguration config) {
        mapConfiguration = config;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	7890/2	pduffin	VBM:2005042705 Committing results of supermerge

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 26-Apr-05	7759/3	pcameron	VBM:2005040505 Logging initialisation changed

 21-Apr-05	7665/1	pcameron	VBM:2005040505 Logging initialisation changed

 01-Apr-05	6798/5	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 11-Jan-05	6413/4	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 09-Sep-04	5466/1	claire	VBM:2004090905 New Build Mechanism: Refactor business logic out of MarinerConfiguration

 01-Jul-04	4702/4	matthew	VBM:2004061402 merge problems

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 29-Jun-04	4733/5	allan	VBM:2004062105 Merge issues.

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 04-May-04	4023/1	ianw	VBM:2004032302 Added support for short length tables

 25-Mar-04	3386/10	steve	VBM:2004030901 Supermerged and merged back with Proteus

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 16-Mar-04	2867/11	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/8	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 15-Mar-04	2736/7	steve	VBM:2003121104 Supermerged

 02-Mar-04	2736/4	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 26-Jan-04	2724/1	geoff	VBM:2004011911 Add projects to config

 23-Jan-04  2736/1  steve   VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04  2685/1  steve   VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 15-Oct-03	1517/4	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 11-Mar-04	3370/3	steve	VBM:2004030901 Null exception if protocols element is missing in config

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 07-Aug-03	906/7	chrisw	VBM:2003072905 Public API changed for transform configuration

 07-Aug-03	906/4	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	906/2	chrisw	VBM:2003072905 implemented compilable attribute on transform

 05-Aug-03	921/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 30-Jun-03	492/4	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector - part 3

 25-Jun-03	492/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 30-Jun-03	629/1	philws	VBM:2003062508 Rename sql-connector to sql-driver

 16-Jun-03	366/1	doug	VBM:2003041502 Integration with pipeline JSPs

 13-Jun-03	316/7	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/5	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
