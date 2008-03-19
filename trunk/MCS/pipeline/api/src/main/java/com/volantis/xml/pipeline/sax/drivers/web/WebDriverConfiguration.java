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

import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.proxy.Proxy;
import com.volantis.shared.net.proxy.ProxyManager;

import java.util.List;

import org.xml.sax.XMLFilter;

/**
 * Encapsulates the configuration information for the WEB driver.
 *
 * <strong>This interface is a facade provided for use by user code and as such
 * must not be implemented by user code.</strong>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface WebDriverConfiguration extends Configuration {

    /**
     * Get the List of suffixes representing resources that can change the
     * current url context.
     *
     * <p>e.g. html, wml, xml, php, cgi etc). This
     * List is the live list that will be used at runtime. It can be
     * modified.</p>
     *
     * The default value of the list contains the following values:
     * .asp, .cgi, .dll, .exe, .htm, .html, .htmls, .htmlx, .jsp, .php, .ps,
     * .shtml, .wml, .xml.
     *
     * The list is used to determine the resource part of urls.
     *
     * @return the List (of String) suffixes representing resources that can
     * change the current url context (e.g. html, wml, xml, php, cgi etc).
     */
    List getContextChangingResourceSuffixes();

    /**
     * Determine whether the web driver
     * should try to re-map redirected urls (response code 302).
     * @return true if the web driver should attempt to
     * re-map redirected urls; false otherwise.
     */
    boolean remapRedirects();

    /**
     * Set whether the web driver should try to re-map redirected urls
     * (response code 302).
     * @param remapRedirects true if web driver should attempt remap
     * redirected urls; false otherwise.
     */
    void setRemapRedirects(boolean remapRedirects);

    /**
     * Determine whether the web driver should follow remapped urls.
     * If this value is true and there is no appropriate remapping then
     * determination of whether or not to follow the original unmapped
     * redirect url is decided by the followRedirects flag. If this value is
     * false and a remap of a redirect fails then the redirect will not be
     * followed regardless of the followRedirects flag.
     * @return true if redirects should be followed even if the remapping
     * of a redirect url fails; false otherwise.
     */
    boolean followUnsuccessfulRedirectRemaps();

    /**
     * Set whether the web driver should follow remapped urls.
     * If this value is true and there is no appropriate remapping then
     * determination of whether or not to follow the original unmapped
     * redirect url is decided by the followRedirects flag. If this value is
     * false and a remap of a redirect fails then the redirect will not be
     * followed regardless of the followRedirects flag.
     * @param followUnsuccessfulRedirectRemaps
     */
    void setFollowUnsuccessfulRedirectRemaps(boolean
            followUnsuccessfulRedirectRemaps);

    /**
     * Get the URLPrefixRewriteManager that is used to rewrite redirected
     * urls when remapRedirects is true. If the the value returned from this
     * method is null then no attempt will be made to remap redirect urls
     * @return the URLPrefixRewriteManager that remaps redirected urls.
     */
    URLPrefixRewriteManager getRedirectRewriteManager();

    /**
     * Get the URLPrefixRewriteManager that is used to rewrite redirected
     * urls when remapRedirects is true. If the the value returned from this
     * method is null then no attempt will be made to remap redirect urls
     * @param redirectRewriteManager the URLPrefixRewriteManager that remaps
     * redirected urls.
     */
    void setRedirectRewriteManager(
            URLPrefixRewriteManager redirectRewriteManager);

    /**
     * Get the value of the ignoreContentEnabled flag. The default value
     * is false.
     * @return true if the ignorable content should be provided to the web
     * driver; false otherwise.
     */
    boolean isIgnoreContentEnabled();

    /**
     * Set the value of the ignoreContentEnabled flag.
     * @param enabled Whether or not the ignored content should be provided
     * to the web driver.
     */
    void setIgnoreContentEnabled(boolean enabled);

    /**
     * Retrieve the specified Proxy.
     * @param id The id of the proxy to retreive.
     * @return The specified Proxy or null if none was found.
     * @deprecated
     */
    Proxy retrieveProxy(String id);

    /**
     * Retrieve the proxy manager with the specified id.
     *
     * @param id The id of the manager to retrieve.
     * @return The manager, or null if no manager was registered.
     * @volantis-api-exclude-from PublicAPI
     * @volantis-api-exclude-from ProfessionalServicesAPI
     */
    ProxyManager getProxyManager(String id);

    /**
     * Retrieve the XMLFilter for handling scripts of a specified content type.
     * @param id The id of the ScriptModule from which to retrieve the filter.
     * @param contentType The contentType.
     * @return The XMLFilter that represents the script that handles the
     * specified content type.
     */
    XMLFilter retrieveScriptFilter(String id, String contentType);

    /**
     * Put a Proxy into this configuration. If a Proxy with the same id
     * already exists in this configuration then it will be replaced.
     * @param proxy The Proxy to put into this configuration.
     * @return The Proxy replaced by proxy or null if none were replaced.
     */
    Proxy putProxy(Proxy proxy);

    /**
     * Put a ScriptModule into this configuration. If a ScriptModule with the
     * same id already exists in this configuration then it will be replaced.
     * @param module The ScriptModule to put into this configuration.
     * @return The ScriptModule replaced by module or null if none were
     * replaced.
     */
    ScriptModule putScriptModule(ScriptModule module);

    /**
     * Set the value that determines if {@link com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestOperationProcess}
     * automatically and silently follows HTTP response 302 redirects.
     *
     * @param follows If true then the process automatically redirects.  If it
     * is false then the redirect is not followed and it is up to the client to
     * perform the redirect.
     */
    void setFollowRedirects(boolean follows);

    /**
     * Get the value that determines if {@link com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestOperationProcess}
     * automatically and silently follows HTTP response 302 redirects.
     *
     * @return true if the process automatically redirects otherwise false
     * perform the redirect.
     */
    boolean getFollowRedirects();

    /**
     * This method takes the single boolean parameter <code>ignore<code>, this
     * has the same effect as the <i>consume</i> and <i>ignore</i>
     * functionality for the content type. If set to true, when a request
     * returns a non 200 status code (Excluding 3XX redirects which are handled
     * separately) then the content, headers and cookies are returned directly
     * to the HTTPRequestOperationProcess's calling process without passing
     * through the rest of the pipeline. If {@link #setIgnoreContentEnabled}
     * has been called with a true parameter then the ignored error content
     * will be saved to the WebDriverResponse ignored content buffer.  If set
     * to false then content will always be passed to the pipeline before the
     * headers and cookies are passed back to the caller.
     * @param ignore if true then errored content is ignored and not passed
     * through the pipeline.
     */
    void setIgnoreErroredContent(boolean ignore);

    /**
     * Returns the current value of the ignoreErroredContent flag. If the value
     * has not been explicitly set then it should default to true.
     * @return the current value of the ignoreErroredContent flag
     */
    boolean getIgnoreErroredContent();

    /**
     * Add a mapping between a content type String and a
     * {@link com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory} instance.  This will be used
     * by the web driver to determine the appropriate conditioner to use for
     * the content returned.  The contentType must be in the form
     * <code>text/html</code> and must not include extensions eg
     * <code>text/html; charset="utf8"</code>
     *
     * @param contentType The content type String used as a key to the
     * {@link com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory}
     * @param factory The {@link com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory} to use to
     * create the {@link ContentConditioner} to process the content of the
     * specified content type.
     */
    void addWebDriverConditionerFactory(
            String contentType, WebDriverConditionerFactory factory);

    /**
     * Get the {@link com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory} to use to create the
     * {@link com.volantis.xml.pipeline.sax.conditioners.ContentConditioner} to condition content of the specified content
     * type.
     * @param contentType The content type for which we want the associated
     * {@link com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory}.  This should be in the form
     * <code>text/html</code> and should not include extensions eg
     * <code>text/html; charset="utf8"</code>
     *
     * @return the {@link com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory} to use to create
     * the {@link com.volantis.xml.pipeline.sax.conditioners.ContentConditioner} to condition content of the specified
     * content type, or null if there is not a mapping to a
     * WebDriverConditionerFactory for the specified content type.
     */
    WebDriverConditionerFactory
            getWebDriverConditionerFactory(String contentType);

    /**
     * This method takes the single parameter <code>encoding<code> which
     * contains the encoding that will be employed on the content InputSource
     * used by the Web Driver. If no value is explicity specified then the
     * web driver defaults to using the encoding of the response.
     * <p>
     * This method should be used with caution.  It is intended to be used to
     * overcome the exceptional circumstance whereby the encoding returned by
     * the web server is incorrect.
     *
     * @param encoding The specific character encoding to set when retrieving
     * the content from the remote web server
     */
    void setCharacterEncoding(String encoding);

    /**
     * Returns the specific character encoding that will be set when retrieving
     * the content from the remote web server.
     * @return the character encoding.
     */
    String getCharacterEncoding();

    /**
     * Returns whether or not the response contains markup (true or false).
     *
     * @return true if the response contains markup, false otherwise.
     */
    boolean getResponseContainsPipelineMarkup();

    /**
     * Set whether or not the response contains markup.
     *
     * @param responseContainsPipelineMarkup true if the response contains
     *                                       markup, false otherwise.
     */
    void setResponseContainsPipelineMarkup(
            boolean responseContainsPipelineMarkup);

    /**
     * Returns the HTTPCacheConfiguration
     * @return the HTTPCacheConfiguration
     */
    HTTPCacheConfiguration getHTTPCacheConfiguration();

    /**
     * Sets the HTTPCacheConfiguration
     * @param httpCacheConfiguration the HTTPCacheConfiguration
     */
    void setHTTPCacheConfiguration(
            HTTPCacheConfiguration httpCacheConfiguration);

    /**
     * Returns the HTTPCache that the Web Driver is using or null if
     * caching is not supported
     * @return the HTTPCache that the Web Driver is using or null if
     * caching is not supported
     */

    HTTPCache getHTTPCache();

    /**
     * Sets the timeout for all requests.
     * @param timeout the timeout in milliseconds
     */
    void setTimeout(long timeout);

    /**
     * Gets the timeout for all requests.
     * @return the timeout in milliseconds. A value of -1 indicates no
     * timeout has been set.
     */
    long getTimeoutInMillis();

    /**
     * Indicates whether the timeout has been set.
     *
     * @return True if it has false otherwise.
     */
    boolean timeoutWasSet();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/2	matthew	VBM:2005092809 Allow proxy configuration via system properties

 22-Mar-05	7474/1	pcameron	VBM:2005031018 Added timeout to web driver configuration

 22-Mar-05	7472/1	pcameron	VBM:2005031018 Added timeout to web driver configuration

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Sep-04	872/1	matthew	VBM:2004083107 add JSessionID proxy handling

 08-Sep-04	854/6	matthew	VBM:2004083107 allow httpProcessor to obtain pre and post request processors from Pipline context

 08-Sep-04	854/4	matthew	VBM:2004083107 allow dsb to see

 05-Sep-04	854/1	matthew	VBM:2004083107 need to see changes from another stream

 08-Sep-04	869/1	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	865/2	doug	VBM:2004090707 Add web driver request preprocessing

 07-Sep-04	858/1	doug	VBM:2004090610 Added preprocessing of response capability

 20-Jul-04	751/11	doug	VBM:2004061405 Added getHTTPCache() method to WebDriverConfiguration interface

 01-Jul-04	751/9	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	751/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 ===========================================================================
*/
