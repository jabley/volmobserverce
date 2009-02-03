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

package com.volantis.xml.pipeline.sax.url;

import com.volantis.cache.Cache;
import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentManager;
import com.volantis.shared.net.url.URLContentManagerConfiguration;
import com.volantis.shared.net.url.URLContentManagerFactory;
import com.volantis.shared.time.Period;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfigurationImpl;

import java.io.IOException;
import java.net.URL;

/**
 * A decorator for a {@link URLContentManager} that can be stored in and
 * retrieved from the pipeline configuration.
 */
public class PipelineURLContentManager
        implements URLContentManager, Configuration {

    /**
     * The underlying {@link URLContentManager}.
     */
    private final URLContentManager delegate;
    private static final Class MANAGER_KEY = URLContentManager.class;

    /**
     * Initialise.
     *
     * @param managerConfig the configuration
     */
    public PipelineURLContentManager(
            final URLContentManagerConfiguration managerConfig) {
        URLContentManagerFactory factory =
                URLContentManagerFactory.getDefaultInstance();
        this.delegate = factory.createContentManager(managerConfig);
    }

    /**
     * Initialise.
     *
     * @param delegate The manager to which this should delegate.
     */
    public PipelineURLContentManager(URLContentManager delegate) {
        this.delegate = delegate;
    }

    // Javadoc inherited.
    public URLContent getURLContent(String url, Period timeout,
                                    URLConfiguration configuration)
            throws IOException {
        return delegate.getURLContent(url, timeout, configuration);
    }

    // Javadoc inherited.
    public URLContent getURLContent(
                URL url, Period timeout, URLConfiguration configuration)
            throws IOException {
        return delegate.getURLContent(url, timeout, configuration);
    }

    /**
     * Retrieve the content manager from the configuration associated with the
     * specified context.
     *
     * <p>If the content manager cannot be found then it will create one using
     * the {@link ConnectionConfiguration} object if present to determine the
     * default timeout. As the configuration is shared across multiple threads
     * this synchronizes on the configuration to ensure that the manager is
     * created atomically.</p>
     *
     * @param context The context from which the manager is to be retrieved.
     */
    public static URLContentManager retrieve(XMLPipelineContext context) {

        XMLPipelineConfiguration configuration =
                context.getPipelineConfiguration();
        PipelineURLContentManager manager;

        // Synchronize on the configuration to ensure that if a manager is
        // created then it is updated atomically.
        synchronized(configuration) {
            manager = (PipelineURLContentManager)
                    configuration.retrieveConfiguration(MANAGER_KEY);
            if (manager == null) {
                manager = createManager(configuration);

                configuration.storeConfiguration(MANAGER_KEY, manager);
            }
        }

        return manager;
    }

    /**
     * Create the manager from the configuration.
     *
     * <p>This uses the {@link ConnectionConfiguration} object, if any, that is
     * stored in the pipeline configuration to set the default timeout for the
     * manager, if it was not present then there is no limit by default.</p>
     *
     * @param configuration The pipeline configuration.
     * @return The newly created {@link PipelineURLContentManager}.
     */
    private static PipelineURLContentManager createManager(
            XMLPipelineConfiguration configuration) {

        ConnectionConfigurationImpl connectionConfiguration =
                (ConnectionConfigurationImpl)
                configuration.retrieveConfiguration(
                        ConnectionConfiguration.class);

        Period timeout = Period.INDEFINITELY;
        if (connectionConfiguration != null) {
            timeout = connectionConfiguration.getTimeoutAsPeriod();
        }

        // get the cache configuration
        URLContentCacheConfiguration urlContentCacheConfiguration =
            (URLContentCacheConfiguration) configuration.retrieveConfiguration(
                URLContentCacheConfiguration.class);
        if (urlContentCacheConfiguration == null) {
            urlContentCacheConfiguration =
                new URLContentCacheConfiguration(configuration);
            configuration.storeConfiguration(URLContentCacheConfiguration.class,
                urlContentCacheConfiguration);
        }
        final Cache cache = urlContentCacheConfiguration.getCache();

        final URLContentManagerConfiguration managerConfig =
            new URLContentManagerConfiguration();
        managerConfig.setDefaultTimeout(timeout);
        managerConfig.setCache(cache);

        return new PipelineURLContentManager(managerConfig);
    }
}
