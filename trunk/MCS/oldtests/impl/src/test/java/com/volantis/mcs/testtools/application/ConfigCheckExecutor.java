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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/testtools/application/ConfigCheckExecutor.java,v 1.3 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; An executor which
 *                              checks that the config values used by
 *                              AppManager match the values actually used by
 *                              Volantis.
 * 11-Mar-03    Geoff           VBM:2002112102 - Add checks for property names
 *                              that had been forgotten.
 * 25-Mar-03    Geoff           VBM:2003042306 - Use new AppExecutor and
 *                              AppContext rather than dodgy direct call to
 *                              AppManager.
 * 08-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * 13-May-03    Chris W         VBM:2003041503 - Added checkJSP() to check
 *                              level of JSP support
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.testtools.application;

import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.mcs.application.ApplicationInternals;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.integration.TestMarkupPlugin;
import com.volantis.mcs.integration.iapi.InvokeAttributes;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.runtime.PluggableAssetTranscoderManager;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.VolantisInternals;
import com.volantis.mcs.runtime.configuration.AnonymousDataSourceConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.ConnectionPoolConfiguration;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryConfiguration;
import com.volantis.mcs.runtime.configuration.MCSDatabaseConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.PolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyQuotaConfiguration;
import com.volantis.mcs.runtime.configuration.project.AbstractPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.JdbcPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.XmlPoliciesConfiguration;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginManager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.VolantisEnvironment;
import com.volantis.testtools.config.ConfigProjectPoliciesJdbcValue;
import com.volantis.testtools.config.ConfigProjectPoliciesXmlValue;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.config.ConfigValuePolicyCache;
import com.volantis.testtools.config.ConfigValueRemotePolicy;
import com.volantis.testtools.config.ConfigValueRemoteQuota;
import junit.framework.Assert;
import junitx.util.PrivateAccessor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An executor which checks that the config values used by {@link AppManager}
 * match the values actually used by Volantis, as far as is possible.
 * <p>
 * Used with {@link AppManager#useAppWith}.
 * <p>
 * Note that, due to the existing design of Volantis, it is not currently
 * possible to check all settings, but a majority are checked.
 */
class ConfigCheckExecutor implements AppExecutor {

    private Volantis volantis;

    private ConfigValue config;

    /**
     * Construct an instance of this class, checking the Volantis instance
     * against the config value provided by the appManager.
     *
     * @param volantis to be checked.
     */
    public ConfigCheckExecutor(Volantis volantis) {
        this.volantis = volantis;
    }

    /**
     * Check that the initialised volantis object matches the
     * configuration we created, as far as is practicable.
     * A lot of stuff we cannot easily test without rewriting Volantis,
     * but the simple stuff is here.
     */
    public void execute(AppContext context) throws Exception {
        // First find out what we configured volantis with.
        config = context.getConfigValue();

        // Then, check that Volantis...

        // These values we can access (reasonably) directly.

        // Simple properties.
        Assert.assertEquals(config.pageMessageHeading,
                volantis.getPageHeadingMsg());
        Assert.assertEquals(
                defaultBoolean(config.pagePackagingMimeEnabled, false),
                volantis.isPagePackagingMimeEnabled());
        Assert.assertEquals(
                defaultBoolean(config.sessionProxyCookieMappingEnabled, false),
                volantis.isCookieMappingEnabled());
        // Base directories.
        checkBaseDirs();
        // Default asset url prefixes.
        checkAssetUrls();
        // App Server related.
        checkAppServer();
        // Stylesheet related.
        checkStylesheet();
        // Debug related.
        checkDebug();

        // These values are only stored as hashtable "properties" and we
        // access them via private method abuse; not very typesafe, but
        // unfortunately that was the original "design".

        // Local repository.
        checkLocalRepositoryProperties();
        // Device repository.
        checkDeviceRepositoryProperties();
        // (Local) policy caches.
        Iterator policies = config.policyCaches.keySet().iterator();
        while (policies.hasNext()) {
            String policy = (String) policies.next();
            checkPolicyCacheProperties(policy, config);
        }
        // Remote policy caches.
        if (config.remotePolicyCaches != null) {
            checkRemotePolicyCacheProperties(config.remotePolicyCaches);
        }
        // Remote policy quotas.
        if (config.remotePolicyQuotaList != null) {
            checkRemotePolicyQuotaProperties(config.remotePolicyQuotaList);
        }

        checkMCSPlugins();

        checkOtherPlugins();
    }

    /**
     * Check the configuration of markup plugins.
     */
    private void checkMCSPlugins() {
        MarinerApplication application = new MarinerApplication(){};
        ApplicationInternals.setVolantisBean(application, volantis);      
        MarinerRequestContext context = new TestMarinerRequestContext();
        ContextInternals.setMarinerApplication(context, application);
        for (int pluginCount=0; pluginCount < 6; pluginCount++) {
            MarkupPluginManager manager = volantis.getMarkupPluginManager();
            InvokeAttributes attrs = new InvokeAttributes();
            attrs.setName("myPlugin" + pluginCount);
            MarkupPlugin plugin = manager.getMarkupPlugin(
                    context, attrs.getName());

            if (plugin instanceof TestMarkupPlugin){
                Map args = ((TestMarkupPlugin)plugin).initializeArgs;
                for (int argCount = 0; argCount < 3; argCount++) {
                    Assert.assertEquals("Wrong value for argument value.",
                            "argValue" + argCount,
                            args.get("myArg" + argCount));
                    argCount++;
                }
            }
        }
    }

    private void checkOtherPlugins() throws Exception {
        PluggableAssetTranscoder transcoder = volantis.getAssetTranscoder();

        if (transcoder instanceof PluggableAssetTranscoderManager) {
            transcoder = (PluggableAssetTranscoder)
                PrivateAccessor.getField(transcoder,
                                         "transcoder");
        }

        if (config.assetTranscoderPluginClass != null) {
            try {
                Assert.assertSame(
                    "Configured asset transcoder class not as expected",
                    transcoder.getClass(),
                    Class.forName(config.assetTranscoderPluginClass));
            } catch (ClassNotFoundException e) {
                Assert.fail("Configured asset transcoder class is not " +
                            "valid (" + config.assetTranscoderPluginClass +
                            ")");
            }
        } else {
            Assert.assertSame("Default asset transcoder class not as expected",
                              transcoder.getClass(),
                              com.volantis.mcs.integration.transcoder.ICSWithGIF.class);
        }
    }

    /**
     * Check the base directory values.
     */
    private void checkBaseDirs() {
        Assert.assertEquals(defaultString(config.chartImagesBase, "chartimages/"),
                volantis.getChartImagesBase());
        Assert.assertEquals(defaultString(config.modesetsBase, "modesets/"),
                volantis.getModeSetsBase());
        Assert.assertEquals(defaultString(config.scriptsBase, "scripts/"),
                volantis.getScriptsBase());
    }

    /**
     * Check the asset url values.
     */
    private void checkAssetUrls() {
        Assert.assertNotNull(volantis.getDefaultProject());
        Assert.assertEquals(defaultSlash(config.audioUrlPrefix, "/audio/"),
                volantis.getDefaultProject().
                getPrefixURL(VariantType.AUDIO).getExternalForm());
        Assert.assertEquals(defaultSlash(config.dynvisUrlPrefix, "/dynvis/"),
                volantis.getDefaultProject().
                getPrefixURL(VariantType.VIDEO).getExternalForm());
        Assert.assertEquals(defaultSlash(config.imageUrlPrefix, "/images/"),
                volantis.getDefaultProject().
                getPrefixURL(VariantType.IMAGE).getExternalForm());
        Assert.assertEquals(defaultSlash(config.scriptUrlPrefix, "/scripts/"),
                volantis.getDefaultProject().
                getPrefixURL(VariantType.SCRIPT).getExternalForm());
        Assert.assertEquals(defaultSlash(config.textUrlPrefix, "/text/"),
                volantis.getDefaultProject().
                getPrefixURL(VariantType.TEXT).getExternalForm());
    }

    /**
     * Check the app server / web app values.
     */
    private void checkAppServer() {
        Assert.assertEquals(createMarinerURL(config.internalUrl),
                volantis.getInternalURL());
        Assert.assertEquals(createMarinerURL(config.baseUrl),
                volantis.getBaseURL());
        Assert.assertEquals(config.pageBase, volantis.getPageBase());
    }

    /**
     * Check the stylesheet values.
     */
    private void checkStylesheet() {
        Assert.assertEquals(createMarinerURL(config.styleBaseUrl),
                volantis.getStyleSheetConfiguration().getBaseURL());
    }

    /**
     * Check the debug values.
     */
    private void checkDebug() {
        Assert.assertEquals(defaultBoolean(config.debugComments, false),
                VolantisEnvironment.commentsEnabled());
        Assert.assertEquals(defaultBoolean(config.debugLogPageOutput, false),
                VolantisEnvironment.logPageOutput());
    }

    /**
     * Check the local repository properties values.
     */
    private void checkLocalRepositoryProperties() throws Exception {

        VolantisInternals internals = new VolantisInternals(volantis);
        MarinerConfiguration marinerConfig = internals.getMarinerConfig();

        AbstractPoliciesConfiguration defaultPolicies =
                marinerConfig.getProjects().getDefaultProject().getPolicies();

        // Note: the jdbc repository values are sometimes set even for
        // XML, but it's not a simple decision. We just do this for now,
        // we can make it more complex later if necessary.
        if ("xml".equals(config.repositoryType)) {
            if (config.defaultProjectPolicies != null) {
                ConfigProjectPoliciesXmlValue xml =
                        (ConfigProjectPoliciesXmlValue)
                        config.defaultProjectPolicies;
                Assert.assertTrue("default policies should be JDBC",
                        defaultPolicies instanceof XmlPoliciesConfiguration);
                XmlPoliciesConfiguration xmlPolicies =
                        (XmlPoliciesConfiguration) defaultPolicies;
                Assert.assertEquals(xml.projectDir, xmlPolicies.getDirectory());
            }
        } else {
            if (config.defaultProjectPolicies != null) {
                ConfigProjectPoliciesJdbcValue jdbc =
                        (ConfigProjectPoliciesJdbcValue)
                        config.defaultProjectPolicies;
                Assert.assertTrue("default policies should be XML",
                        defaultPolicies instanceof JdbcPoliciesConfiguration);
                JdbcPoliciesConfiguration jdbcPolicies =
                        (JdbcPoliciesConfiguration) defaultPolicies;
                Assert.assertEquals(jdbc.projectName, jdbcPolicies.getName());
            }


            JDBCRepositoryConfiguration jdbcConfig =
                marinerConfig.getLocalRepository()
                    .getJDBCRepositoryConfiguration();

            ConnectionPoolConfiguration connectionPool = null;
            AnonymousDataSourceConfiguration anonymousDataSource = null;
            MCSDatabaseConfiguration mcsDatabase = null;

            if (jdbcConfig.getDataSourceConfiguration()
                instanceof ConnectionPoolConfiguration) {
                // Pooled datasource
                connectionPool =
                    (ConnectionPoolConfiguration)jdbcConfig
                        .getDataSourceConfiguration();
                if (connectionPool.getDataSourceConfiguration()
                    instanceof AnonymousDataSourceConfiguration) {
                    // anonymous pooled
                    anonymousDataSource =
                        (AnonymousDataSourceConfiguration)connectionPool
                            .getDataSourceConfiguration();
                    if (anonymousDataSource.getDataSourceConfiguration()
                        instanceof MCSDatabaseConfiguration) {
                        // MCS Database anonymous pooled
                        mcsDatabase =
                            (MCSDatabaseConfiguration)anonymousDataSource
                                .getDataSourceConfiguration();
                    }
                } else if (
                    connectionPool.getDataSourceConfiguration()
                        instanceof MCSDatabaseConfiguration) {
                    // MCS Database pooled
                    mcsDatabase =
                        (MCSDatabaseConfiguration)connectionPool
                            .getDataSourceConfiguration();
                }
            } else if (
                jdbcConfig.getDataSourceConfiguration()
                    instanceof AnonymousDataSourceConfiguration) {
                // anonymous
                anonymousDataSource =
                    (AnonymousDataSourceConfiguration)jdbcConfig
                        .getDataSourceConfiguration();
                if (anonymousDataSource.getDataSourceConfiguration()
                    instanceof MCSDatabaseConfiguration) {
                    // MCS Database anonymous
                    mcsDatabase =
                        (MCSDatabaseConfiguration)anonymousDataSource
                            .getDataSourceConfiguration();
                }
            }

            if (config.repositoryUser != null) {
                Assert.assertEquals(config.repositoryUser,
                        anonymousDataSource.getUser());
            }
            if (config.repositoryPassword != null) {
                Assert.assertEquals(config.repositoryPassword,
                        anonymousDataSource.getPassword());
            }
            if (config.repositoryVendor != null) {
                Assert.assertEquals(config.repositoryVendor,
                        mcsDatabase.getVendor());
            }
            if (config.repositorySource != null) {
                Assert.assertEquals(config.repositorySource,
                        mcsDatabase.getSource());
            }
            if (config.repositoryHost != null) {
                Assert.assertEquals(config.repositoryHost,
                        mcsDatabase.getHost());
            }
            if (config.repositoryPort != null) {
                Assert.assertEquals(config.repositoryPort,
                        mcsDatabase.getPort());
            }
            if (config.repositoryDbPoolMax != null) {
                Assert.assertEquals(config.repositoryDbPoolMax,
                        connectionPool.getMaximum());
            }
            if (config.repositoryKeepConnectionsAlive != null) {
                Assert.assertEquals(config.repositoryKeepConnectionsAlive,
                        connectionPool.getKeepAlive());
            }
            if (config.repositoryConnectionPollInterval != null) {
                Assert.assertEquals(config.repositoryConnectionPollInterval,
                        connectionPool.getPollInterval());
            }
        }
    }

    /**
     * Check the device repository properties values.
     */
    private void checkDeviceRepositoryProperties()
            throws ConfigurationException {

        // We have implemented VolantisInternals as a workaround rather than
        // use reflection because this is all sh*te anyway.
        VolantisInternals internals = new VolantisInternals(volantis);
        DeviceRepositoryLocation location =
                internals.getDeviceRepositoryLocation();

        // This cannot be tested because of a hack in Volantis which
        // throws away this value if the file does not exist.
//        if (config.standardFileDeviceRepositoryLocation != null) {
//            Assert.assertEquals(config.standardFileDeviceRepositoryLocation,
//                    props.get(XMLRepository.DEVICE_REPOSITORY_PROPERTY));
//        }
        if (config.standardJDBCDeviceRepositoryProject != null) {
            Assert.assertEquals(config.standardJDBCDeviceRepositoryProject,
                    location.getDeviceRepositoryName());
        }
    }

    /**
     * Check the local policy cache properties values.
     *
     * NOTE: Uses intropection to call the (usually) private method
     * {@link VolantisInternals#getPolicyCacheConfiguration}.
     */
    private void checkPolicyCacheProperties(final String policy,
            ConfigValue config) throws Exception {
        ConfigValuePolicyCache cache = (ConfigValuePolicyCache)
                config.policyCaches.get(policy);

        // We have implemented VolantisInternals as a workaround rather than
        // use the commented code below to get around a bug to do with
        // reflection in the IBM JDK 1.4.1.
        VolantisInternals internals = new VolantisInternals(volantis);
        PolicyCacheConfiguration configuration =
                internals.getPolicyCacheConfiguration(cache.policyType);

        // NOTE: this fails in IBM JDK 1.4.1 with IllegalAccessException
        // the *second* time it is run. It looks like a VM bug to me.
//        ReflectionManager accessMgr = new ReflectionManager(
//                Volantis.class.getDeclaredMethod(
//                "getPolicyCacheAttributes", new Class[] {String.class})
//        );
//        Map props = (Map)accessMgr.useAsAccessible(new ReflectionExecutor() {
//            public Object execute(AccessibleObject object) throws Exception {
//                 return ((Method)object).invoke(volantis,
//                         new Object[] {policy});
//            }
//        });
        Assert.assertEquals(cache.strategy,
                configuration.getStrategy());
        Assert.assertEquals(cache.maxEntries.toString(),
                configuration.getMaxEntries().toString());
        Assert.assertEquals(cache.timeout.toString(),
                configuration.getTimeout().toString());

        // Not checking for extra, unused stuff here, probably should...
    }

    /**
     * Check the remote policy cache properties values.
     * <p>
     * NOTE: we can't really figure out if it "worked" ok, but we can
     * at least figure out if we passed the correct values into the objects
     * that use these values.
     */
    private void checkRemotePolicyCacheProperties(ConfigValueRemotePolicy cache)
            throws Exception {

        // Note this only works for the top level one - child ones need
        // to add the parent element to the name provided to this method.

        // We have implemented VolantisInternals as a workaround rather than
        // use the commented code below to get around a bug to do with
        // reflection in the IBM JDK 1.4.1.
        VolantisInternals internals = new VolantisInternals(volantis);

        MarinerConfiguration marinerConfig = internals.getMarinerConfig();
        RemotePoliciesConfiguration configuration =
                marinerConfig.getRemotePolicies();
        RemotePolicyCacheConfiguration cacheConfiguration =
                configuration.getPolicyCache();

        // @todo this nicely demonstrates the bug re remote cache nesting
        // docs says the global and individual are at different nesting levels
        // and yet the tests pass with the same temporary for both, and with an
        // xml file that has them at the same level.

        Assert.assertNotNull("policy props for ", cacheConfiguration);

        // You can see how inconsistent the names of the attributes are. Yuck.
        Assert.assertEquals(cache.cachePolicies,
                cacheConfiguration.getDefaultCacheThisPolicy());
        Assert.assertEquals(cache.defaultTimeToLive,
                cacheConfiguration.getDefaultTimeToLive());
        Assert.assertEquals(cache.defaultRetryFailedRetrieval,
                cacheConfiguration.getDefaultRetryFailedRetrieval());
        Assert.assertEquals(cache.defaultRetryInterval,
                cacheConfiguration.getDefaultRetryInterval());
        Assert.assertEquals(cache.defaultRetryMaxCount,
                cacheConfiguration.getDefaultRetryMaxCount());
        Assert.assertEquals(cache.defaultRetainDuringRetry,
                cacheConfiguration.getDefaultRetainDuringRetry());

        Assert.assertEquals(cache.maxCacheSize,
                cacheConfiguration.getMaxCacheSize());
        Assert.assertEquals(cache.maxTimeToLive,
                cacheConfiguration.getMaxTimeToLive());
        Assert.assertEquals(cache.allowRetryFailedRetrieval,
                cacheConfiguration.getAllowRetryFailedRetrieval());
        Assert.assertEquals(cache.minRetryInterval,
                cacheConfiguration.getMinRetryInterval());
        Assert.assertEquals(cache.maxRetryMaxCount,
                cacheConfiguration.getMaxRetryMaxCount());
        Assert.assertEquals(cache.allowRetainDuringRetry,
                cacheConfiguration.getAllowRetainDuringRetry());

        // Not checking for extra, unused stuff here, probably should...
    }

    /**
     * Check the remote policy quota properties values.
     * <p>
     * NOTE: we can't really figure out if it "worked" ok, but we can
     * at least figure out if we passed the correct values into the objects
     * that use these values.
     */
    private void checkRemotePolicyQuotaProperties(List quotaList)
            throws Exception {

        // We have implemented VolantisInternals as a workaround rather than
        // use the commented code below to get around a bug to do with
        // reflection in the IBM JDK 1.4.1.
        VolantisInternals internals = new VolantisInternals(volantis);

        MarinerConfiguration marinerConfig = internals.getMarinerConfig();
        RemotePoliciesConfiguration configuration =
                marinerConfig.getRemotePolicies();
        Iterator i = configuration.getQuotaIterator();
        Map quotaMap = new HashMap();
        while (i.hasNext()) {
            RemotePolicyQuotaConfiguration quota =
                    (RemotePolicyQuotaConfiguration) i.next();
            quotaMap.put(quota.getUrl(), quota);
        }

        // NOTE: this fails in IBM JDK 1.4.1 with IllegalAccessException
        // the *second* time it is run. It looks like a VM bug to me.
//        ReflectionManager accessMgr = new ReflectionManager(
//                Volantis.class.getDeclaredMethod(
//                "getRemotePolicyCacheAttributes", new Class[] {String.class})
//        );
//        Map quotaMap = (Map)accessMgr.useAsAccessible(new ReflectionExecutor() {
//            public Object execute(AccessibleObject object) throws Exception {
//                 return ((Method)object).invoke(volantis,
//                         new Object[] {quotaElement});
//            }
//        });

        Assert.assertNotNull("quota map", quotaMap);

        Iterator itr = quotaList.iterator();
        while (itr.hasNext()) {
            ConfigValueRemoteQuota quota = (ConfigValueRemoteQuota) itr.next();
            RemotePolicyQuotaConfiguration config =
                    (RemotePolicyQuotaConfiguration) quotaMap.get(quota.url);
            Assert.assertEquals("Percentage for URL " + quota.url,
                    quota.percentage, config.getPercentage());
        }

        // Not checking for extra, unused stuff here, probably should...
    }

    /**
     * Returns value, or defaultValue if value is null, for Strings.
     *
     * @param value
     * @param defaultValue
     * @return the value
     */
    private static String defaultString(String value, String defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    /**
     * Returns value, or defaultValue if value is null, for Booleans.
     *
     * @param value
     * @param defaultValue
     * @return the boolean value
     */
    private static boolean defaultBoolean(Boolean value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        } else {
            return value.booleanValue();
        }
    }


    /**
     * Returns value, or defaultValue if value is null, whilst ensuring that
     * the returned value always ends with a "/".
     *
     * @param url
     * @param defaultUrl
     * @return the url
     */
    private static String defaultSlash(String url, String defaultUrl) {
        url = defaultString(url, defaultUrl);
        if (!url.endsWith("/")) {
            url += "/";
        }
        return url;
    }

    /**
     * Create a {@link MarinerURL} if the value is non-null, else return null.
     *
     * @param value the value to use to create the Mariner URL
     * @return a Mariner URL or null.
     */
    private static MarinerURL createMarinerURL(String value) {
        if (value != null) {
            return new MarinerURL(value);
        } else {
            return null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 25-Jan-05	6712/3	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 09-Nov-04	6156/1	geoff	VBM:2004110904 ICS GIF support shopuld be on by default in MCS v3.2.3

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 29-Jul-04	4991/1	byron	VBM:2004070510 VTS classes need renaming in MCS to ICS

 28-Jun-04	4726/4	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 16-Apr-04	3362/5	steve	VBM:2003082208 supermerged

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 23-Mar-04	3362/2	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 17-Feb-04	3041/1	claire	VBM:2004021208 Refactored RuntimeProject and added a unit test

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 13-Jan-04	2573/1	andy	VBM:2003121907 renamed file variables to directory

 13-Oct-03	1547/3	philws	VBM:2003101002 Make use of PrivateAccessor instead of adding a test-only method

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 12-Sep-03	1295/7	geoff	VBM:2003082109 Build all jars and run the junit testsuite with IBM JDK 1.4.1 (build script changes)

 03-Sep-03	1295/4	geoff	VBM:2003082109 rework issues

 02-Sep-03	1295/2	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 21-Aug-03	1231/1	geoff	VBM:2003030502 policy-cache max-entries attribute is ignored

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 10-Jul-03	761/2	adrian	VBM:2003070801 Added integration test to Volantis testcase to test markup plugin configuration

 25-Jun-03	544/2	geoff	VBM:2003061007 Allow JSPs to create binary output

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
