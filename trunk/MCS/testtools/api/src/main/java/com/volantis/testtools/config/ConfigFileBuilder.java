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
/*  ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/testtools/config/ConfigFileBuilder.java,v 1.4 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Oct-02    Allan           VBM:2002101101 - Created. A class that can
 *                              create a mariner-config.xml and a
 *                              mariner-config.dtd file and make these
 *                              available to others.
 * 17-Dec-02    Phil W-S        VBM:2002121001 - Add style-sheets max-age to
 *                              DTD and pre-defined value in config content.
 *                              Also added the member "properties" used to
 *                              initialize the default config content.
 * 13-Jan-03    Byron           VBM:2003010910 - Added remote-repository
 *                              connection-timeout to DTD and pre-defined value
 *                              in config content. Added timeout member
 *                              variable and supporting get/setters.
 * 29-Jan-03    Byron           VBM:2003012712 - Added urlCacheEnabled member
 *                              variable with getter and setter.
 * 05-Feb-03    Byron           VBM:2003013109 - Added pagePackaging and
 *                              relevant getter/setter. Modified getConfigDTD
 *                              and getDefaultConfigXML. Note actual changes
 *                              were incorrectly check in under previous VBM.
 * 12-Feb-03    Geoff           VBM:2003021110 - Add to do comment.
 * 12-Feb-03    Geoff           VBM:2003021110 - Remove getSingleton().
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Updated to add baseURL and
 *                              internalURL features.
 * 25-Feb-03    Mat             VBM:2003022002 - Added externalApplicationConfig
 *                              to enable config file elements to be built
 *                              for external applications such as MPS.
 * 24-Feb-03    Geoff           VBM:2003010604 - Remove dependency on Haddock!
 * 27-Feb-03    Geoff           VBM:2003010904 - Refactor to allow us to use
 *                              new ConfigValue objects to provide data -
 *                              moved packages at this point, use proper DTD,
 *                              removed unused page caching and mariner log
 *                              stuff.
 * 10-Mar-03    Steve           VBM:2003021101 - Added configuration for cookie
 *                              handling in proxy server.
 * 17-Mar-03    Geoff           VBM:2003031403 - Fix hack for finding the
 *                              DTD file so it works in MPS. Sigh.
 * 11-Mar-03    Geoff           VBM:2002112102 - Refactor value property name
 *                              for consistency.
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * 13-May-03    Chris W         VBM:2003041503 - Added a jsp element to
 *                              createConfigXML().
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.config;

import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.mcs.runtime.configuration.xml.XMLConfigurationBuilder;
import com.volantis.xml.schema.W3CSchemata;
import org.apache.log4j.Category;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class that will create a mcs-config.xml and a mcs-config.dtd file
 * and make these available to others.
 *
 * @todo this class could use further refactoring now that JDOM is redundant
 * here, no time at the moment...
 */
public class ConfigFileBuilder {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance(
            "com.volantis.testtools.config.ConfigFileBuilder");

    private static final String DEFAULT_FILE_DIR =
        System.getProperty("java.io.tmpdir");

    private static final String XML_FILE_NAME = "mcs-config.xml";
    private static final String XSD_FILE_NAME = "mcs-config.xsd";

    /**
     * String representation of a default name for named project one for
     * testing.  This is public so that other testcases can use them.
     */
    public static final String DEFAULT_NAMED_PROJECT_ONE =
            "#DefaultNamedProject";

    /**
     * String representation of a default name for named project two for
     * testing.  This is public so that other testcases can use them.
     */
    public static final String DEFAULT_NAMED_PROJECT_TWO =
            "#AnotherDefaultNamedProject";

    /**
     * String representation of the default base URL for assets.  It is
     * deliberately an absolute path for the tests to demonstrate MarinerURL
     * operations.  This is public so that other testcases can use it.
     */
    public static final String DEFAULT_ASSET_BASE_URL =
            "/volantis/";
    /**
     * The JDOM document for the config file.
     */
    private Document config;

    /**
     * The directory for the config files.
     */
    private String configFileDir = DEFAULT_FILE_DIR;

    /**
     * Used to holder plugin builders.
     */
    private Map pluginBuilders = new HashMap();

    public static final String SCHEMA_RESOURCE =
            "com/volantis/mcs/runtime/configuration/xml/" +
            XMLConfigurationBuilder.SCHEMA_NAME;

    public static final String SCHEMA_FILE =
            "architecture/built/output/classes/api/" + SCHEMA_RESOURCE;

    /**
     * Get the value of configFileDir.
     * @return value of configFileDir.
     */
    public String getConfigFileDir() {
        return configFileDir;
    }

    /**
     * Set the value of configFileDir.
     * @param v  Value to assign to configFileDir.
     */
    public void setConfigFileDir(String  v) {
        this.configFileDir = v;
    }

    /**
     * Output the config xml file. Note that this config file is non validating
     * and has no dtd reference.
     */
    void outputXML() throws IOException {
        File xmlFile = new File(configFileDir, XML_FILE_NAME);
        xmlFile.deleteOnExit();
        FileWriter writer = new FileWriter(xmlFile);

        // Output the JDOM to the file.
        // Add indenting but not newlines as we added them in manually.
        XMLOutputter outputter = new XMLOutputter("  ", false);
        outputter.output(config, writer);

        writer.close();
    }

    /**
     * Output the config dtd file.
     */
    void outputDTD() throws IOException {
        File dtdFile = new File(configFileDir, XSD_FILE_NAME);
        // @todo deleteOnExit is problematic for single vm mode testing
        // And this is not the only place we use it...
        dtdFile.deleteOnExit();

        FileWriter writer = new FileWriter(dtdFile);

        String dtd = getConfigDTD();

        writer.write(dtd, 0, dtd.length());
        writer.close();
    }

    /**
     */
    public void buildConfigDocument(ConfigValue value)
            throws JDOMException, IOException {
        // Build the a JDOM Document from the xml of a mariner config.
        // Not sure this is strictly necessary any more ...?
        String configXml = createConfigXml(value);
        System.out.println( configXml );
        StringReader reader = new StringReader(configXml);
        SAXBuilder builder = new SAXBuilder();
        try {
            config = builder.build(reader);
        } catch (JDOMException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error parsing created XML config " + configXml,
                             e);
            }
            throw e;
        }
        // And write it out.
        outputXML();
        outputDTD();
    }

    private String getConfigDTD() throws IOException {
        // Finding the config DTD can be a little bit tricky and a lot hacky.
        // @todo need to find a portable way of doing this?
        //
        // If running "normally" in MCS, you can find it in the webapp dir
        // If running in Idea with the project files above the voyager dir
        // (which I assume others do too), then it's also in a voyager/webapp
        // found it!
        // If running "normally" in MPS, you can't find it in the webapp dir,
        // as it's in the voyager/webapp dir.
        // If running in Idea with the project files above the mps dir, then
        // it's in the mps/voyager/webapp dir. Sigh.
        // Finally, the correct solution is used: we look for the file as a
        // resource.

        File currentDir = new File(System.getProperty("user.dir"));
        // Try and find "Idea" mps voyager directory.
        File voyagerDir = new File(currentDir, "mps/voyager");
        if (!voyagerDir.exists()) {
            // Try and find "normal" MPS or "Idea" MCS voyager directory.
            voyagerDir = new File(currentDir, "voyager");
        }
        if (!voyagerDir.exists()) {
            // Try and find "normal" MPS voyager directory.
            voyagerDir = currentDir;
        }

        Reader reader = null;
        StringWriter sw = new StringWriter();
        File dtdFile = new File(voyagerDir,
                                SCHEMA_FILE);

        if (!dtdFile.exists()) {
            // Look to see if we can find this as a resource
            InputStream stream =
                    getClass().getClassLoader().getResourceAsStream(
                            SCHEMA_RESOURCE);

            if (stream == null) {
                throw new IllegalStateException(
                        "can't find schema resource " +
                        SCHEMA_RESOURCE +
                        " or schema file " + dtdFile);
            } else {
                reader = new InputStreamReader(stream);
            }
        } else {
            reader = new FileReader(dtdFile);
        }

        // Read the resource or file into a string
        char [] buf = new char[1024];
        int read;
        try {
            while ((read = reader.read(buf)) != -1) {
                sw.write(buf, 0, read);
            }
        } finally {
            reader.close();
        }

        return sw.toString();
    }

    /**
     * Create an attribute name=value pair, if the value is not null.
     *
     * @param name name of the attribute
     * @param value value of the attribute, may be null.
     * @return the name=value pair, or empty string if the value is null.
     */
    private String attr(String name, Object value) {
        if (value != null) {
            return name + "=\"" + String.valueOf(value) + "\" ";
        } else {
            return "";
        }
    }

    /**
     * Create an attribute name=value pair, translating null values into "".
     *
     * @param name name of the attribute
     * @param value value of the attribute, may be null.
     * @return the name=value pair, or empty string if the value is null.
     */
    private String attrMan(String name, Object value) {
        if (value == null) {
            value = "";
        }
        return name + "=\"" + String.valueOf(value) + "\" ";
    }

    /**
     * Register a plugin builder for a pluginValue class.
     *
     * @param builder The builder
     * @param pluginClass The plugin value associated with the builder.
     */
    public void registerPluginBuilder(PluginConfigFileBuilder builder, Class pluginClass) {
        pluginBuilders.put(pluginClass, builder);
    }


    /**
     * Provide String representation of a config file.
     */
    protected String createConfigXml(ConfigValue value) {
        // NOTE: container[Tag](attrs, content) & empty[Tag](attrs) methods for
        // creating tags would be nice too and would make this more readable.
        String config =
            "<mcs-config " +
                "xmlns=\"" + XMLConfigurationBuilder.SCHEMA_NAMESPACE + "\" " +
                "xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " +
                "xsi:schemaLocation=\"" +
                    XMLConfigurationBuilder.SCHEMA_NAMESPACE + " " +
                    XMLConfigurationBuilder.SCHEMA_LOCATION + "\"> \n";

        config +=
            "<local-repository> \n";
        if ("xml".equals(value.repositoryType)) {
            config += "<xml-repository/>\n";
        } else {
            config += "<jdbc-repository>\n";
            // add connection pool
            config += "<connection-pool " +
                attrMan("maximum", value.repositoryDbPoolMax) +
                attrMan("keep-alive",
                    value.repositoryKeepConnectionsAlive) +
                attrMan("poll-interval",
                    value.repositoryConnectionPollInterval) + ">\n";
            // add anonymous datasource
            config += "<anonymous-data-source " +
                attrMan("user", value.repositoryUser) +
                attrMan("password", value.repositoryPassword) + ">\n";
            // add concrete datasource
            config += "<mcs-database " +
                attrMan("vendor", value.repositoryVendor) +
                attrMan("source", value.repositorySource) +
                attrMan("host", value.repositoryHost) +
                attrMan("port", value.repositoryPort) + "/>\n";
            config += "</anonymous-data-source>\n";
            config += "</connection-pool>\n";
            config += "</jdbc-repository>\n";
        }
        config += "</local-repository>\n";

        config += "<projects>";

        config +=
                "<default>\n" +
                "    " + createPolicySource(value) +
                createDefaultAssets("    ") +
                "</default>\n";

        config +=
                "<project " + attr("name", DEFAULT_NAMED_PROJECT_ONE) + ">\n" +
                "    " + createPolicySource(value) +
                createDefaultAssets("    ") +
                "</project>\n";

        config +=
                "<project " + attr("name", DEFAULT_NAMED_PROJECT_TWO) + ">\n" +
                "    " + createPolicySource(value) +
                createDefaultAssets("    ") +
                "</project>\n";

        config +=
                "</projects>\n";

        config +=
            "<secondary-repository> \n" +
            "<inline-metadata " +
                attr("enabled", "true") + "/> \n" +
            "</secondary-repository> \n" +
            "<page-messages " +
                attr("heading", value.pageMessageHeading) + "/> \n" +
            "<policy-cache> \n";

        Iterator policyCacheNames = value.policyCaches.keySet().iterator();
        while (policyCacheNames.hasNext()) {
            String name = (String) policyCacheNames.next();
            ConfigValuePolicyCache cache = (ConfigValuePolicyCache)
                    value.policyCaches.get(name);
            // @todo check the name is part of the valid set?
            // This needs to do url-cache and theme-cache in the first instance
            config +=
            "<" + name +" " +
                attr("enabled", "true") +
                attr("strategy", cache.strategy) +
                // @todo there is a bug in GenericCache where this is
                // mispelled. When it is fixed, this attribute name should be
                // changed to match the name of the value variable.
                attr("max-entries", cache.maxEntries) +
                attr("timeout", cache.timeout) +
                "/> \n";
        }

        config +=
            "</policy-cache> \n" +
            "<jsp " +
               attr("support-required", value.jspSupportRequired) +
               attr("write-direct", value.jspWriteDirect) +
               attr("resolve-character-references",
                    value.jspResolveCharacterReferences) +
                attr("evaluate-page-after-canvas",
                        value.jspEvaluatePageAfterCanvas) +
            "/> \n" +
            "<!-- Configure style sheet associated properties --> \n" +
            "<style-sheets> \n" +
                "<external-generation " +
                    attr("base-url", value.styleBaseUrl) +
                    // TODO: 2005081902 get config schema change and remove this
                    attr("base-directory", "/dummy") +
                    "/> \n" +
            "</style-sheets> \n" +
            "<scripts " +
                attr("base", value.scriptsBase) + "/> \n" +
            "<modesets " +
                attr("base", value.modesetsBase) + "/> \n" +
            "<chartimages " +
                attr("base", value.chartImagesBase) + "/> \n" +
            "<log4j " +
                attr("xml-configuration-file", value.log4jXmlConfigFile) +
                "/> \n" +
            "<plugins " +
                attr("asset-url-rewriter", value.assetUrlRewriterPluginClass) +
                " " +
                attr("asset-transcoder", value.assetTranscoderPluginClass) +
                "/> \n" +
            "<mcs-agent " +
                attr("enabled", "false") +
                attr("port", "8888") +
                attr("password", "007") +
                "/> \n" +
            "<remote-policies " +
                attr("connection-timeout", value.remoteRepositoryTimeout) +

            ">";
        // @todo this nicely demonstrates the bug re remote cache nesting
        // docs says the global and individual are at different nesting levels
        // and yet the tests pass with the same impl for both, and with an
        // xml file that has them at the same level.

        ConfigValueRemotePolicy remoteCache = (ConfigValueRemotePolicy)
                value.remotePolicyCaches;
        if (remoteCache != null) {
            config += createRemoteCache(remoteCache);
        }
        config +=
                "<remote-policy-quotas>";
        if (value.remotePolicyQuotaList != null) {
            config += createRemoteQuotas(value.remotePolicyQuotaList);
        }
        config +=
                "</remote-policy-quotas>" +
            "</remote-policies>" +
            "<web-application " +
                attr("base-url", value.baseUrl) +
                attr("internal-url", value.internalUrl) +
                attr("app-server-name", "Tomcat31") +
                attr("jndi-provider", "t3://sunfish:7801") +
                attr("use-server-connection-pool", "false") +
                attr("datasource", "mdatasource") +
                attr("user", "volantis") +
                attr("password", "fish") +
                attr("page-base", value.pageBase) +
                "/> \n" +
            "<debug " +
                attr("comments", value.debugComments) +
                attr("logPageOutput", value.debugLogPageOutput) +
                "/> \n" +
            "<page-packaging>\n" +
                "<mime-packaging " +
                    attr("enabled", value.pagePackagingMimeEnabled) +
                "/> \n" +
            "</page-packaging>\n" +
            "<session-proxy>\n" +
                "<map-cookies " +
                    attr("enabled", value.sessionProxyCookieMappingEnabled) +
                "/> \n" +
            "</session-proxy>\n" +

            "<mcs-plugins> \n" +
                createMarkupPlugins(value.markupPlugins) +
            "</mcs-plugins> \n" +

            "<application-plugins>\n";

        // Add in any application plugins that have been registered.
        Iterator i = value.pluginConfigValues.iterator();
        while (i.hasNext()) {
                PluginConfigValue pluginValue = (PluginConfigValue) i.next();
                PluginConfigFileBuilder pluginBuilder =
                        (PluginConfigFileBuilder)
                        pluginBuilders.get(pluginValue.getClass());
                if(pluginBuilder == null) {
                    logger.error(
                        "No builder registered for " + pluginValue.getClass());
                    throw new IllegalStateException(
                        "No builder registered for " + pluginValue.getClass());
                } else {
                    config += pluginBuilder.build(pluginValue) + "\n";
                }
        }

        config += "</application-plugins>\n";
        if ( value.wmlPreferredOutputFormat != null ){
            config += "<protocols>\n";
            config += "<wml "+attr("preferred-output-format",value.wmlPreferredOutputFormat)+" />\n";
            config += "</protocols>\n";
        }



        config += "<pipeline-configuration/>\n";

        config +=
                "<devices>\n" +
                "    <standard>\n";
        if (value.standardFileDeviceRepositoryLocation != null) {
            config +=
                "        <file-repository " +
                attr("location", value.standardFileDeviceRepositoryLocation) +
                         "/> \n";
        }
        if (value.standardJDBCDeviceRepositoryProject != null) {
            config +=
                "        <jdbc-repository " +
                attr("project", value.standardJDBCDeviceRepositoryProject) +
                        "/> \n";
        }
        config +=
                "    </standard>\n" +
                "    <logging>\n" +
                "        <log-file></log-file>\n" +
                "        <e-mail>\n" +
                "            <e-mail-sending>disable</e-mail-sending>\n" +
                "        </e-mail>\n" +
                "    </logging>\n" +
                "</devices>\n";

        config +=
            "</mcs-config>";
        return config;
    }

    /**
     * A utility method that creates the policy source statement based on the
     * current policy.
     * @param value The configuration information to use for the policy values.
     * @return A string of the xml representation of the policy location.
     */
    private String createPolicySource(ConfigValue value) {
        String config = "";

        if (value.defaultProjectPolicies instanceof
                                            ConfigProjectPoliciesJdbcValue) {
            ConfigProjectPoliciesJdbcValue jdbcPolicies =
                    (ConfigProjectPoliciesJdbcValue)value.defaultProjectPolicies;
            config +=
                "<jdbc-policies " +
                    attrMan("name", jdbcPolicies.projectName);
        } else if (value.defaultProjectPolicies instanceof
                                            ConfigProjectPoliciesXmlValue) {
            ConfigProjectPoliciesXmlValue xmlPolicies =
                    (ConfigProjectPoliciesXmlValue)value.defaultProjectPolicies;
            config +=
                "<xml-policies " +
                    attrMan("directory", xmlPolicies.projectDir);
        } else {
            throw new IllegalStateException("Unexpected default project " +
                    "policies type: " + value.defaultProjectPolicies);
        }
        config += " />\n";
        return config;
    }

    /**
     * A utility method that outputs the various asset urls with default values
     * for all of the prefixes.
     * @param indent The indent to use for the output.
     * @return A string of the xml representing the asset definitions.
     */
    private String createDefaultAssets(String indent) {
        StringBuffer assetString = new StringBuffer(150);
        assetString.append(indent);
        assetString.append("<assets ");
        assetString.append(attr("base-url", DEFAULT_ASSET_BASE_URL));
        assetString.append(">\n");
        assetString.append(indent);
        assetString.append(indent);
        assetString.append("<audio-assets ");
        assetString.append(attr("prefix-url", "/audio/"));
        assetString.append("/>\n");
        assetString.append(indent);
        assetString.append(indent);
        assetString.append("<dynamic-visual-assets ");
        assetString.append(attr("prefix-url", "/dynvis/"));
        assetString.append("/>\n");
        assetString.append(indent);
        assetString.append(indent);
        assetString.append("<image-assets ");
        assetString.append(attr("prefix-url", "/images/"));
        assetString.append("/>\n");
        assetString.append(indent);
        assetString.append(indent);
        assetString.append("<script-assets ");
        assetString.append(attr("prefix-url", "/scripts/"));
        assetString.append("/>\n");
        assetString.append(indent);
        assetString.append(indent);
        assetString.append("<text-assets ");
        assetString.append(attr("prefix-url", "/text/"));
        assetString.append("/>\n");
        assetString.append(indent);
        assetString.append("</assets> \n");
        return assetString.toString();
    }

    private String createMarkupPlugins(List plugins) {
        String result = new String();
        if (plugins != null) {
            for (int i = 0; i < plugins.size(); i++) {
                MarkupPluginConfiguration configuration =
                        (MarkupPluginConfiguration) plugins.get(i);
                result += "<markup-plugin " +
                        attr("name", configuration.getName()) +
                        attr("class", configuration.getClassName()) +
                        attr("scope", configuration.getScope()) +
                        "> \n" +
                        "<initialize> \n" +
                        createArguments(configuration.getArguments()) +
                        "</initialize> \n" +
                        "</markup-plugin> \n";
            }
        }
        return result;
    }

    private String createArguments(Map arguments) {
        StringBuffer result = new StringBuffer();
        for (Iterator i = arguments.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            result.append("<argument ")
                    .append(attr("name", entry.getKey()))
                    .append(attr("value", entry.getValue()))
                    .append("/> \n");
        }
        return result.toString();
    }

    private String createRemoteQuotas(List remoteQuotaList) {
        String value ="";
        for (int i = 0; i < remoteQuotaList.size(); i++) {
            ConfigValueRemoteQuota remoteQuota =
                    (ConfigValueRemoteQuota) remoteQuotaList.get(i);
            if (remoteQuota != null) {
                // Only need this "if" complexity to avoid a bug in
                // the XMLHandler hack for quotas. Sigh.
                if (remoteQuota.url != null || remoteQuota.percentage != null) {
                    value +=
                            "<remote-policy-quota " +
                                attr("URL", remoteQuota.url) +
                                attr("percentage", remoteQuota.percentage) +
                            "/> \n";
                }
            }
        }
        return value;
    }

    private String createRemoteCache(ConfigValueRemotePolicy remoteCache) {
        String value =
                // As per the example .xml file - doesn't match the code!
                // The code has an extra controlUsePersistentCache attr.
                "<remote-policy-cache " +
                    attr("cachePolicies", remoteCache.cachePolicies) +
                    attr("defaultTimeToLive", remoteCache.defaultTimeToLive) +
                    attr("defaultRetryFailedRetrieval",
                            remoteCache.defaultRetryFailedRetrieval) +
                    attr("defaultRetryInterval",
                            remoteCache.defaultRetryInterval) +
                    attr("defaultRetryMaxCount",
                            remoteCache.defaultRetryMaxCount) +
                    attr("defaultRetainDuringRetry",
                            remoteCache.defaultRetainDuringRetry) +

                    attr("maxCacheSize", remoteCache.maxCacheSize) +
                    attr("maxTimeToLive", remoteCache.maxTimeToLive) +
                    attr("allowRetryFailedRetrieval",
                            remoteCache.allowRetryFailedRetrieval) +
                    attr("minRetryInterval", remoteCache.minRetryInterval) +
                    attr("maxRetryMaxCount", remoteCache.maxRetryMaxCount) +
                    attr("allowRetainDuringRetry",
                            remoteCache.allowRetainDuringRetry) +
                "/> \n";
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Nov-04	6241/4	philws	VBM:2004111209 Enable resource-based access to the MCS configuration schema in the ConfigFileBuilder

 18-Nov-04	6241/1	philws	VBM:2004111209 Fix ConfigFileBuilder resolution of the MCS XSD location

 28-Jun-04	4726/2	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 10-Jun-04	4619/5	ianw	VBM:2004060111 Remove testcase debug for MPS

 09-Jun-04	4619/3	ianw	VBM:2004060111 Fudge config file builder to diagnose MPS problems

 09-Jun-04	4619/1	ianw	VBM:2004060111 Fudge config file builder to fix MPS problems

 25-Mar-04	3386/4	steve	VBM:2004030901 Supermerged and merged back with Proteus

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 16-Feb-04	3037/1	claire	VBM:2004021206 Improve isolation of rewriting URL test cases

 11-Feb-04	2846/3	claire	VBM:2004011915 Ensured asset url rewriting works as specified, with testcases

 10-Feb-04	2931/1	claire	VBM:2004021008 Added named projects from the config

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/1	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 31-Oct-03	1593/1	mat	VBM:2003101502 Adding pluginconfigvalue

 23-Oct-03	1585/7	mat	VBM:2003101502 Add plugin config builders to ConfigFileBuilder

 23-Oct-03	1585/5	mat	VBM:2003101502 Add plugin config builders to ConfigFileBuilder

 15-Oct-03	1517/6	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 21-Aug-03	1231/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 10-Jul-03	761/1	adrian	VBM:2003070801 Added integration test to Volantis testcase to test markup plugin configuration

 27-Jun-03	578/1	sumit	VBM:2003062504 Apply tag functionality outside of a usePipeline tag

 25-Jun-03	544/2	geoff	VBM:2003061007 Allow JSPs to create binary output

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
