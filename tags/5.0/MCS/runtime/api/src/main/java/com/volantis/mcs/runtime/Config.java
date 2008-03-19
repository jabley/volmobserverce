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
 * $Header: /src/voyager/com/volantis/mcs/utilities/Config.java,v 1.23 2003/03/20 12:03:16 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Aug-01    Allan           VBM:2001082701 - Add a leading / to calls
 *                              to getResouceAsStream(). Added this
 *                              change history.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 02-Aug-02    Sumit           VBM:2002080506 - Added support for specifying
 *                              an external config file
 * 15-Nov-02    Doug            VBM:2002071507 - Changed the resolveEntity()
 *                              method of the anonymous XMLHandler instantiated
 *                              in the constructor so that the systemID
 *                              paramater is checked to see if it ends with
 *                              the dtd filename. This is needed as xerces2
 *                              parser resolves the entity using a some default
 *                              resolution algorithm BEFORE allowing any
 *                              registered EntityResolvers to resolve the
 *                              entity.
 * 14-Mar-03    Geoff           VBM:2002112102 - Sucked out most of the impl.
 *                              and emulated it using the new config stuff and
 *                              XmlServletConfigurationBuilder.
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.runtime.configuration.AgentConfiguration;
import com.volantis.mcs.runtime.configuration.AppServerConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.DebugConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.configuration.xml.MpsPluginRuleSet;
import com.volantis.mcs.runtime.configuration.xml.XMLServletConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Read in a configuration file and store the name/value pairs
 * in a HashMap. Note: this is not thread-safe (since at the
 * moment it does not need to be.)
 *
 * @deprecated this exists only for backwards compatibility. All new code
 * should use {@link MarinerConfiguration} directly. See
 * {@link Volantis#marinerConfig}.
 */
public class Config extends HashMap /* implements ElementListener*/ {

    /**
     * Suffix to append to element names for storing their attributes in the
     * map.
     */
    private static final String ATTRIBUTES_KEY_APPEND = ".attributes";

    /**
     * Root of the configuration object heirarchy that is parsed from the
     * XML file.
     */
    private MarinerConfiguration mariner;

    /**
     * Construct a new Config.
     *
     * @param cc the ConfigContext for this Config
     */
    public Config(ConfigContext cc) throws ConfigurationException {

        // Parse the xml to get the root config object.
        XMLServletConfigurationBuilder configBuilder =
                new XMLServletConfigurationBuilder(cc);
        
        // Add the digester parsing rules for the MPS plugin.
        // @todo make this part of the internal API and move to MPS
        configBuilder.addApplicationPluginRuleSet(new MpsPluginRuleSet());
        mariner = configBuilder.buildConfiguration();

        // Create references to all the simple sub-objects
        AgentConfiguration agent = mariner.getAgent();
        AppServerConfiguration appServer = mariner.getAppServer();
        DebugConfiguration debug = mariner.getDebug();
        ProtocolsConfiguration protocols = mariner.getProtocols();

        // Build the map of element attribute values just the same as the
        // previous XMLHandler based Config did for backwards compatibility.
        AttributeMap attrs;
        if (mariner != null) {

            attrs = makeAttrs(Volantis.CONFIG_PAGEMESSAGES_ELEMENT);
            attrs.put("heading", mariner.getPageMessageHeading());

            attrs = makeAttrs(Volantis.CONFIG_SCRIPTS_ELEMENT);
            attrs.put("base", mariner.getScriptsBase());

            attrs = makeAttrs(Volantis.CONFIG_MODESETS_ELEMENT);
            attrs.put("base", mariner.getModeSetsBase());

            attrs = makeAttrs(Volantis.CONFIG_CHARTIMAGES_ELEMENT);
            attrs.put("base", mariner.getChartImagesBase());

            attrs = makeAttrs(Volantis.PLUGINS_ELEMENT);
            attrs.put("url-rewriter", mariner.getUrlRewriterPluginClass());
            attrs.put("asset-url-rewriter",
                      mariner.getAssetURLRewriterPluginClass());
            attrs.put("asset-transcoder",
                      mariner.getAssetTranscoderPluginClass());

            attrs = makeAttrs(Volantis.CONFIG_PAGE_PACKAGING_MIME);
            attrs.put("enabled", mariner.getPagePackagingMimeEnabled());

            attrs = makeAttrs(Volantis.CONFIG_MAP_COOKIES_ELEMENT);
            attrs.put("enabled", mariner.getSessionProxyCookieMappingEnabled());

        }

        if (debug != null) {
            attrs = makeAttrs(Volantis.CONFIG_DEBUG_ELEMENT);
            attrs.put("logPageOutput", debug.getLogPageOutput());
            attrs.put("comments", debug.getComments());
        }

        if (protocols != null) {
            attrs = makeAttrs(Volantis.CONFIG_PROTOCOLS_ELEMENT);
            attrs.put("preferred-output-format",
                    protocols.getPreferredOutputFormat());
        }


        if (agent != null) {
            attrs = makeAttrs(Volantis.CONFIG_AGENT_ELEMENT);
            attrs.put("enabled", "true"); // corresponds to Enabled.
            attrs.put("password", agent.getPassword());
            attrs.put("port", agent.getPort());
        }

        if (appServer != null) {
            attrs = makeAttrs(Volantis.CONFIG_WEBAPP_ELEMENT);
            attrs.put("base-url", appServer.getBaseUrl());
            attrs.put("internal-url", appServer.getInternalUrl());
            attrs.put("app-server-name", appServer.getAppServerName());
            attrs.put("use-server-connection-pool", appServer.getUseServerConnectionPool());
            attrs.put("jndi-provider", appServer.getJndiProvider());
            attrs.put("datasource", appServer.getDatasource());
            attrs.put("page-base", appServer.getPageBase());
            attrs.put("datasource-vendor", appServer.getDatasourceVendor());
            attrs.put("user", appServer.getUser());
            attrs.put("password", appServer.getPassword());
            attrs.put("anonymous", appServer.getAnonymous());
        }
    }


    /**
     * @deprecated for temporary use by Volantis to retrieve the configuration
     * created. Do not use this method for anything else!
     *
     * @return the root object of the configuration created.
     */
    MarinerConfiguration getMarinerConfiguration() {
        return mariner;
    }

    /**
     * Construct a new {@link AttributeMap} for the named element, inserting
     * it into the hash.
     * <p>
     * NOTE: this will discard any existing {@link AttributeMap} of that name.
     *
     * @param elementName the fully qualified name of the element, in x.y.z
     *      format.
     * @return the attribute map created.
     */
    private AttributeMap makeAttrs(String elementName) {
        AttributeMap attrMap = new AttributeMap();
        put(elementName + ATTRIBUTES_KEY_APPEND, attrMap);
        return attrMap;
    }

    //
    // NOTE: these two shared by MCS and MPS.
    //

    // MCS: 5 Usages in Volantis, 2 in VolantisTestCase.
    // MPS: 5 Usages, 1 in Session (channels), 4 in various test cases (odbc).
    /**
     * Given an attribute name, return the attributes assoicated with that
     * element.
     * @param elementName the name of the element whose attributes are required
     * @return a Map of the requested attributes or null if there are no
     * attributes associated with the named element
     */
    public Map getAttributes(String elementName) {
        return (Map) get(elementName + ATTRIBUTES_KEY_APPEND);
    }


    // MCS: 40 Usages in 5 files, mostly in Volantis, few in app server impls.
    // MPS: 2 Usages, 1 in MessageRequestor, 1 in MessageRecipientHelper.
    /**
     * Given an element name and an attribute name, return the value
     * of that attribute within the specified element.
     *
     * @param elementName the name of the element whose attributes we are
     * interested in
     * @param attributeName the name of the attribute whose value we are
     * interested in
     * @return the value of the specified attribute or null if the
     * attribute is not found
     */
    public String getAttributeValue(String elementName, String attributeName) {
        Object o = get(elementName + ATTRIBUTES_KEY_APPEND);
        Map attributes;

        if (o == null || !(o instanceof Map)) {
            return null;
        }

        attributes = (Map) o;

        return (String) attributes.get(attributeName);
    }

    //
    // NOTE: These two MCS only.
    //

    // MCS: 6 usages, 4 in Volantis, 1 each in 2 App server impls.
    public boolean getBooleanAttributeValue(String elementName,
            String attributeValue) {
        String val = getAttributeValue(elementName, attributeValue);

        if (val == null) {
            return false;
        }

        return val.equalsIgnoreCase("true");
    }

    // MCS: One usage in Volantis.
    public int getIntAttributeValue(String elementName, String attributeValue) {
        String val = getAttributeValue(elementName, attributeValue);

        if (val == null) {
            return 0;
        }

        return Integer.parseInt(val);
    }

    /**
     * A convenience class to make sure that all values returned as attributes
     * are Strings, even though they were inserted as Booleans and Ints.
     *
     * Alternative impl would be just override get() but that would be slower.
     */
    private static class AttributeMap extends HashMap {
        private void put(String key, Boolean value) {
            put(key, valueOf(value));
        }

        private void put(String key, Integer value) {
            put(key, valueOf(value));
        }

        private static String valueOf(Boolean value) {
            if (value == null) {
                return null;
            } else {
                return value.toString();
            }
        }

        private static String valueOf(Integer value) {
            if (value == null) {
                return null;
            } else {
                return value.toString();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	8005/5	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 05-May-05	7890/2	pduffin	VBM:2005042705 Committing results of supermerge

 28-Apr-05	7922/3	pduffin	VBM:2005042801 Removed User and UserFactory classes

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 26-Apr-05	7759/2	pcameron	VBM:2005040505 Logging initialisation changed

 21-Apr-05	7665/2	pcameron	VBM:2005040505 Logging initialisation changed

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 29-Jun-04	4733/6	allan	VBM:2004062105 Merge issues.

 28-Jun-04	4733/3	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 29-Jun-04	4726/8	claire	VBM:2004060803 Moved all stylesheet initialisation to use MarinerConfig not Config

 28-Jun-04	4726/6	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 16-Mar-04	2867/6	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/3	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 02-Mar-04	2736/4	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 25-Feb-04	3118/4	geoff	VBM:2003121717 External CSS caching can not be enabled. (supermerge and add testcase javadoc)

 18-Feb-04	3118/1	geoff	VBM:2003121717 External CSS caching can not be enabled.

 18-Feb-04	3111/1	geoff	VBM:2003121717 External CSS caching can not be enabled.

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 29-Jan-04	2749/1	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 23-Jan-04	2736/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 15-Oct-03	1517/5	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 21-Aug-03	1231/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 30-Jun-03	623/1	mat	VBM:2003063005 Add app server config values

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
