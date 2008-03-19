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

package com.volantis.shared.net.impl.url;

import com.volantis.shared.net.impl.url.http.HttpProtocolHandler;
import com.volantis.shared.net.impl.url.file.FileProtocolHandler;
import com.volantis.shared.net.proxy.Proxy;
import com.volantis.shared.net.proxy.ProxyFactory;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentManager;
import com.volantis.shared.net.url.URLContentManagerConfiguration;
import com.volantis.shared.time.Period;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implementation of {@link URLContentManager}.
 *
 * <p>Delegates the actual creation of the {@link URLContent} instances to a
 * {@link URLProtocolHandler}. It uses a specialized handler if available,
 * otherwise it uses a default handler that simply uses the standard
 * <code>URLConnection</code>.</p>
 */
public class URLContentManagerImpl
        implements InternalURLContentManager {

    /**
     * The default handler, used for all protocols that do not have a handler
     * registered.
     */
    private static final URLProtocolHandler DEFAULT_HANDLER =
            new DefaultProtocolHandler();

    /**
     * The proxy manager, defaults to the system proxy manager.
     */
    private final ProxyManager proxyManager;

    /**
     * The map from protocol (as {@link String}) to {@link URLProtocolHandler}.
     */
    private final Map protocol2Handler;

    /**
     * The default timeout to use if no timeout is specified on the input.
     */
    private final Period defaultTimeout;

    /**
     * Initialise.
     *
     * @param config configuration object with initialisation parameters
     */
    public URLContentManagerImpl(final URLContentManagerConfiguration config) {
        if (config == null) {
            throw new IllegalArgumentException(
                "URL content manager configuration cannot be null");
        }
        if (config.getDefaultTimeout() == null) {
            throw new IllegalArgumentException("defaultTimeout cannot be null");
        }

        this.defaultTimeout = config.getDefaultTimeout();

        // Use the default system proxy, if any.
        this.proxyManager = ProxyFactory.getDefaultInstance()
                .getSystemProxyManager();

        Map map = new TreeMap(String.CASE_INSENSITIVE_ORDER);

        final HttpProtocolHandler httpHandler =
            new HttpProtocolHandler(this, config.getCache());
        map.put("http", httpHandler);
        map.put("https", httpHandler);

        // configure a file handler.
        FileProtocolHandler fileProtocolHandler = new FileProtocolHandler();
        map.put("file", fileProtocolHandler);
        protocol2Handler = map;
    }

    // Javadoc inherited.
    public URLContent getURLContent(final URL url, Period timeout,
                                    final URLConfiguration configuration)
            throws IOException {

        String protocol = url.getProtocol();
        URLProtocolHandler handler = (URLProtocolHandler)
                protocol2Handler.get(protocol);
        if (handler == null) {
            handler = DEFAULT_HANDLER;
        }

        if (timeout == null) {
            timeout = defaultTimeout;
        }

        return handler.connect(url, timeout, configuration);
    }

    // Javadoc inherited.
    public URLContent getURLContent(final String url, final Period timeout,
                                    final URLConfiguration configuration)
            throws IOException {

        return getURLContent(new URL(url), timeout, configuration);
    }

    // Javadoc inherited.
    public Proxy getProxy(String host) {
        if (proxyManager == null) {
            return null;
        } else {
            return proxyManager.getProxyForHost(host);
        }
    }
}
