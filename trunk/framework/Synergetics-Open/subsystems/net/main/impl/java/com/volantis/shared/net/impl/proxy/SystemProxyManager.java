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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.net.impl.proxy;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.net.proxy.ProxyBuilder;
import com.volantis.shared.net.proxy.ProxyFactory;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.system.SystemProperties;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * An abstraction of the proxy specified by system properties.
 *
 * <p>This creates a proxy manager from the various standard system propertoes.
 * It attempts to find a HTTP specific proxy using the http.proxyHost and
 * http.proxyPort preoprties first. If no host can be found then the generic
 * proxy properties of proxyHost and proxyPort are checked. It treats the
 * pairs of properties as single values, i.e. it does not combine the port
 * from one with the host from another. If no host can be found from either
 * pair then there is no system proxy.</p>
 *
 * <p>The proxy may require authentication, i.e. user name and password and
 * so they are retrieved from the volantis.pipeline.proxyPassword and
 * volantis.pipeline.proxyUser system properties.</p> 
 */
public class SystemProxyManager
        implements ProxyManager {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(SystemProxyManager.class);

    /**
     * Localize the exception log messages
     */
    private static final MessageLocalizer MESSAGE_LOCALIZER =
            LocalizationFactory.createMessageLocalizer(
                    SystemProxyManager.class);

    /**
     * Localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    SystemProxyManager.class);

    private final SystemProperties systemProperties;

    /**
     * The system proxy.
     */
    private final Proxy systemProxy;

    /**
     * The regular expression pattern that matches excluded hosts
     */
    private final Pattern excludedHostRE;

    /**
     * Initialise.
     */
    public SystemProxyManager() {
        this(ProxyFactory.getDefaultInstance(),
                SystemProperties.getDefaultInstance());
    }

    /**
     * Initialise.
     *
     * @param factory          The factory for creating {@link Proxy} related
     *                         instances.
     * @param systemProperties The system properties.
     */
    public SystemProxyManager(
            ProxyFactory factory,
            SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
        ProxyBuilder builder = factory.createProxyBuilder();
        Proxy sysProxy = null;
        Pattern exHostRE = null;

        if (initialiseAddress(builder)) {
            builder.setUser(getSystemPropertyAsString(HTTPSystemPropertyKeys.PROXY_USER));
            builder.setPassword(getSystemPropertyAsString(HTTPSystemPropertyKeys.PROXY_PASSWORD));

            sysProxy = builder.buildProxy();

            // Get the list of hosts that do not require the use of a proxy. It is
            // in the form of a glob list, i.e. uses * for wildcard and | for
            // choices
            exHostRE = compileExcludedHostList();
        }

        this.systemProxy = sysProxy;
        this.excludedHostRE = exHostRE;
    }

    /**
     * Initialise the address (host, port) of the builder.
     *
     * @param builder The builder to initialise.
     * @return True if the address was found, false otherwise.
     */
    private boolean initialiseAddress(ProxyBuilder builder) {
        String host = getSystemPropertyAsString(
                HTTPSystemPropertyKeys.HTTP_PROXY_HOST);
        boolean result = true;
        int port;
        if (host == null) {
            host = getSystemPropertyAsString(
                    HTTPSystemPropertyKeys.PROXY_HOST);
            if (host == null) {
                result = false;
            } else {
                port = getPort(HTTPSystemPropertyKeys.PROXY_PORT);
                builder.setPort(port);
            }
        } else {
            port = getPort(HTTPSystemPropertyKeys.HTTP_PROXY_PORT);
            builder.setPort(port);
        }
        builder.setHost(host);
        return result;
    }

    /**
     * Get the system proxy.
     *
     * @return The system proxy, or null if none has been specified.
     */
    public Proxy getSystemProxy() {
        return systemProxy;
    }

    // Javadoc inherited.
    public Proxy getProxyForHost(String host) {
        Proxy result = null;
        if (excludedHostRE == null) {
            result = systemProxy;
        } else {
            Matcher matcher = excludedHostRE.matcher(host);

            if (!matcher.matches()) {
                result = systemProxy;
            }
        }
        return result;
    }

    /**
     * @param key   The system property key.
     * @return the port set in the specified system property key, or null if
     *         the property is empty, has not been set, or does not contain a
     *         number
     */
    private int getPort(String key) {
        int result = 80;
        String value = getSystemPropertyAsString(key);
        if (value != null) {
            try {
                result = Integer.parseInt(value);
                if (result < 1 || result > 65535) {
                    result = 80;
                }
            } catch (NumberFormatException nfe) {
                LOGGER.warn(MESSAGE_LOCALIZER.format(
                        "proxy-port-system-property-not-parsable"));
            }
        }
        return result;
    }

    /**
     * Get the value of a system property.
     *
     * @param key The system property key.
     * @return The system property value, or null if the property was not set,
     *         or was empty.
     */
    private String getSystemPropertyAsString(final String key) {
        String result = systemProperties.getProperty(key, "").trim();
        if ("".equals(result)) {
            result = null;
        }
        return result;
    }

    /**
     * Compile the excluded host list into a regular expression.
     *
     * @return The regular expression, or null if there was not host list.
     */
    private Pattern compileExcludedHostList() {
        Pattern excludedHostRE = null;

        String excludedHostList = getSystemPropertyAsString(
                HTTPSystemPropertyKeys.PROXY_EXCLUDE);
        if (excludedHostList != null) {

            // Create a REProgram from the glob list of excluded hosts.
            String expr = createREFromGlobList(excludedHostList);
            excludedHostRE = Pattern.compile(expr);

        }
        return excludedHostRE;
    }

    /**
     * Perform a simple substitution to turn the "|" seperated list of
     * wildcarded host names into a regular expression
     *
     * @param excludesList the list of hostnames to convert
     * @return a regular expression that can be used to match hostnames to
     *         the list provided in excludesList
     */
    private String createREFromGlobList(String excludesList) {
        // make the output buffer likely to be long enough
        StringBuffer expr = new StringBuffer(excludesList.length() + 50);
        expr.append('^');
        for (int i = 0; i < excludesList.length(); i++) {
            char c = excludesList.charAt(i);
            if (c == '.') {
                expr.append("\\.");
            } else if (c == '*') {
                expr.append(".*");
            } else if (c == '|') {
                expr.append("$|^");
            } else {
                expr.append(c);
            }
        }
        expr.append('$');
        return expr.toString();
    }
}
