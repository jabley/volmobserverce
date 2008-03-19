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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.cache.Cache;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.httpcache.CachingPluggableHTTPManager;
import com.volantis.xml.pipeline.sax.proxy.Proxy;
import com.volantis.xml.pipeline.sax.proxy.ProxyManagerAdapter;
import com.volantis.xml.pipeline.sax.url.URLContentCacheConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.XMLFilter;

/**
 * Encapsulates the configuration of the WebDriver.
 */
public class WebDriverConfigurationImpl implements WebDriverConfiguration {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(WebDriverConfigurationImpl.class);

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The default list of all the suffixes that represent resources that
     * can change the current url context.
     */
    private static final ArrayList DEFAULT_CONTEXT_CHANGING_RESOURCES =
            new ArrayList();

    static {
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".asp");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".cgi");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".dll");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".do");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".exe");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".htm");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".html");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".htmls");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".htmlx");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".jsp");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".php");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".ps");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".shtml");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".wml");
        DEFAULT_CONTEXT_CHANGING_RESOURCES.add(".xml");
    }

    /**
     * A map of Proxy objects associated with this configuration.
     */
    private Map proxies = new HashMap();

    /**
     * A map of {@link com.volantis.shared.net.proxy.ProxyManager objects associated with this configuration.
     */
    private Map proxyManagers = new HashMap();

    /**
     * A map of ScriptModule objects associated with this configuration.
     */
    private Map scriptModules = new HashMap();

    /**
     * This flag is used to determine whether the HTTPRequestOperationProcess
     * silently follows HTTP 302 response codes.
     */
    private boolean followRedirects = true;

    /**
     * This flag is used to determine whether the HTTPRequestOperationProcess
     * should try to re-map redirected urls (response code 302).
     */
    private boolean remapRedirects = true;

    /**
     * This flag is used to determine whether the response contains markup and
     * if so, create a pipeline process that will handle this markup.
     */
    private boolean responseContainsPipelineMarkup = false;

    /**
     * A List of all the suffixes that represent resources that can change
     * the current url context.
     */
    private final List contextChangingResourceSuffixes =
            Collections.synchronizedList(
                    new ArrayList(DEFAULT_CONTEXT_CHANGING_RESOURCES));

    /**
     * This flag is used to determine where the HTTPRequestOperationProcess
     * should follow remapped urls. If this value is true and there is no
     * appropriate remapping then determination of whether or not to follow
     * the original unmapped redirect url is decided by the followRedirects
     * flag.
     */
    private boolean followUnsuccessfulRedirectRemaps = true;

    /**
     * The URLPrefixRewriteManager that provides the ability to remap
     * redirected urls. If this is null then redirected urls will not be
     * remapped.
     */
    private URLPrefixRewriteManager redirectRewriteManager = null;

    /**
     * This flag determines how errored content is handled in the pipeline.
     * If true the content is ignored and stored in an ignored content buffer
     * in the response.  If it is false the content is passed through the
     * pipeline.
     */
    private boolean ignoreErroredContent = true;

    /**
     * A flag indicating whether or not ignored content should be provided
     * to the web driver or simply ignored.
     */
    private boolean ignoreContentEnabled = false;

    /**
     * Web Driver config should allow the specification of the char encoding
     * to use when reading the response.
     */
    private String charEncoding;

    /**
     * The HTTPCacheConfiguration
     */
    private HTTPCacheConfiguration httpCacheConfiguration;

    /**
     * This is used to map between content type strings and
     * {@link WebDriverConditionerFactory} instances.
     */
    private Map conditionerFactories = new HashMap();

    /**
     * The protocol that is used for secure connections.
     */
    protected static final String SECURE_PROTOCOL_PREFIX = "https";

    /**
     * A PluggableHTTPManager instance for possibly cached requests
     */
    private PluggableHTTPManager cachingPluggableHTTPManager;

    /**
     * A PluggableHTTPManager instance for https requests
     */
    private PluggableHTTPManager httpsPluggableHTTPManager;

    /**
     * The timeout in milliseconds for all web driver requests. The default
     * timeout is -1 which indicates no timeout.
     */
    private Long timeout = null;

    // javadoc inherited
    public List getContextChangingResourceSuffixes() {
        return contextChangingResourceSuffixes;
    }

    // javadoc inherited
    public boolean remapRedirects() {
        return remapRedirects;
    }

    // javadoc inherited
    public void setRemapRedirects(boolean remapRedirects) {
        this.remapRedirects = remapRedirects;
    }

    // javadoc inherited
    public boolean followUnsuccessfulRedirectRemaps() {
        return followUnsuccessfulRedirectRemaps;
    }

    // javadoc inherited
    public void setFollowUnsuccessfulRedirectRemaps(boolean
            followUnsuccessfulRedirectRemaps) {
        this.followUnsuccessfulRedirectRemaps =
                followUnsuccessfulRedirectRemaps;
    }

    // javadoc inherited
    public URLPrefixRewriteManager getRedirectRewriteManager() {
        return redirectRewriteManager;
    }

    // javadoc inherited
    public void setRedirectRewriteManager(
            URLPrefixRewriteManager redirectRewriteManager) {
        this.redirectRewriteManager = redirectRewriteManager;
    }

    // javadoc inherited
    public boolean isIgnoreContentEnabled() {
        return ignoreContentEnabled;
    }

    // javadoc inherited
    public void setIgnoreContentEnabled(boolean enabled) {
        ignoreContentEnabled = enabled;
    }

    // javadoc inherited
    public Proxy retrieveProxy(String id) {
        return (Proxy)proxies.get(id);
    }

    // Javadoc inherited.
    public ProxyManager getProxyManager(String id) {
        return (ProxyManager) proxyManagers.get(id);
    }

    // javadoc inherited
    public XMLFilter retrieveScriptFilter(String id, String contentType) {
        XMLFilter filter = null;
        ScriptModule module = (ScriptModule)scriptModules.get(id);
        if (module != null) {
            filter = module.selectScriptFilter(contentType);
        }

        return filter;
    }

    // javadoc inherited
    public Proxy putProxy(final Proxy proxy) {
        Proxy replaced = null;
        if (proxy != null) {
            replaced = (Proxy)proxies.put(proxy.getId(), proxy);

            // Create a ProxyManagerAdapter around an old style Proxy.
            ProxyManager manager = new ProxyManagerAdapter(proxy);

            // Store the manager with the specified id.
            proxyManagers.put(proxy.getId(), manager);
        }

        return replaced;
    }

    // javadoc inherited
    public ScriptModule putScriptModule(ScriptModule module) {
        ScriptModule replaced = null;
        if (module != null) {
            replaced = (ScriptModule)scriptModules.put(module.getId(), module);
        }

        return replaced;
    }

    // javadoc inherited
    public void setFollowRedirects(boolean follows) {
        this.followRedirects = follows;
    }

    // javadoc inherited
    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    // javadoc inherited
    public void setIgnoreErroredContent(boolean ignore) {
        ignoreErroredContent = ignore;
    }

    // javadoc inherited
    public boolean getIgnoreErroredContent() {
        return ignoreErroredContent;
    }

    // javadoc inherited
    public void addWebDriverConditionerFactory(
            String contentType, WebDriverConditionerFactory factory) {
        conditionerFactories.put(contentType, factory);
    }

    // javadoc inherited
    public WebDriverConditionerFactory
            getWebDriverConditionerFactory(String contentType) {
        return (WebDriverConditionerFactory)
                conditionerFactories.get(contentType);
    }

    // javadoc inherited
    public void setCharacterEncoding(String encoding) {
        charEncoding = encoding;
    }

    // javadoc inherited
    public String getCharacterEncoding() {
        return charEncoding;
    }

    // javadoc inherited
    public boolean getResponseContainsPipelineMarkup() {
        return responseContainsPipelineMarkup;
    }

    // javadoc inherited
    public void setResponseContainsPipelineMarkup(
            boolean responseContainsPipelineMarkup) {
        this.responseContainsPipelineMarkup = responseContainsPipelineMarkup;
    }

    // javadoc inherited
    public HTTPCacheConfiguration getHTTPCacheConfiguration() {
        return httpCacheConfiguration;
    }

    // javadoc inherited
    public void setHTTPCacheConfiguration(
            HTTPCacheConfiguration httpCacheConfiguration) {
        this.httpCacheConfiguration = httpCacheConfiguration;
    }

    // javadoc inherited
    public HTTPCache getHTTPCache() {
        return getPluggableHTTPManager(null, null).getHTTPCache();
    }

    // javadoc inherited
    public void setTimeout(long timeout) {
        this.timeout = new Long(timeout);
    }

    // javadoc inherited
    public long getTimeoutInMillis() {
        return timeout == null ? -1 : timeout.longValue();
    }

    public boolean timeoutWasSet() {
        return timeout != null;
    }

    /**
     * <p>Returns the <code>PluggableHTTPManager</code> that will be used to
     * perform the HTTPRequest.  The protocol provided as a parameter will
     * influence which instance of <code>PluggableHTTPManager</code> is
     * instantiated.</p>
     *
     * <p>This method is synchronized in order to guarentee that only one
     * instance of PluggableHTTPManager is created</p>
     *
     * @param protocol The protocol that the manager is to be used for.
     * @return a <code>PluggableHTTPManager</code> instance
     */
    synchronized PluggableHTTPManager getPluggableHTTPManager(
            final String protocol, final XMLPipelineConfiguration pipelineConfig) {

        // Only HTTPClientPluggableHTTPManager supports secure requests
        // However the CachingPluggable delegates to HTTPClient so always
        // create the secure manager.
        if (httpsPluggableHTTPManager == null) {
            httpsPluggableHTTPManager = new HTTPClientPluggableHTTPManager();
        }
        PluggableHTTPManager manager = httpsPluggableHTTPManager;

        // if a non-secure connection is required then wrap the HTTPClient
        // in a CachingPluggableHTTPManager.
        if (protocol == null || !protocol.startsWith(SECURE_PROTOCOL_PREFIX)) {
            // Default to using caching implementation otherwise.
            if (cachingPluggableHTTPManager == null) {
                // if there is a cache configuration then set up http caching
                final Cache cache = getCache(pipelineConfig);
                if (cache != null) {
                    cachingPluggableHTTPManager =
                        new CachingPluggableHTTPManager(
                            (AbstractPluggableHTTPManager)
                                httpsPluggableHTTPManager, cache);
                } else { // otherwise delegate directly to the HTTPClient.
                    if(logger.isDebugEnabled()){
                        logger.debug("Could not find CacheConfiguration: " +
                                     "Continuing without using the HTTPCache");
                    }
                    cachingPluggableHTTPManager = httpsPluggableHTTPManager;
                }
            }
            manager = cachingPluggableHTTPManager;
        }

        // Return an appropriate manager
        return manager;
    }

    /**
     * Returns the cache to be used for the specified pipeline configuration.
     *
     * @param pipelineConfiguration the pipeline configuration to read the cache
     * from
     * @return the cache stored in the specified pipeline configuration
     */
    private Cache getCache(XMLPipelineConfiguration pipelineConfiguration) {
        final Cache cache;
        if (pipelineConfiguration != null) {
            // get the cache configuration
            URLContentCacheConfiguration urlContentCacheConfiguration =
                (URLContentCacheConfiguration) pipelineConfiguration.
                    retrieveConfiguration(URLContentCacheConfiguration.class);
            if (urlContentCacheConfiguration == null) {
                urlContentCacheConfiguration =
                    new URLContentCacheConfiguration(pipelineConfiguration);
                pipelineConfiguration.storeConfiguration(
                    URLContentCacheConfiguration.class,
                    urlContentCacheConfiguration);
            }
            cache = urlContentCacheConfiguration.getCache();
        } else {
            cache = null;
        }
        return cache;
    }
}
