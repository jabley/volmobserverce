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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.web.httpcache;

import com.volantis.cache.Cache;
import com.volantis.shared.net.proxy.ProxyManager;
import com.volantis.shared.time.Period;
import com.volantis.xml.pipeline.sax.drivers.web.AbstractPluggableHTTPManager;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPCache;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPException;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestExecutor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPVersion;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import org.apache.log4j.Category;

/**
 * Concrete implementation of the AbstractPluggableHTTPManager that can
 * cache http responses. It delegates to a target AbstractPluggableHTTPManager
 * to populate the cache.
 *
 * This class creates an internal cache. The objects that are placed within the
 * cache can be quite complex (they contain references to the target
 * AbstractPluggableHTTPManager) but since the actual cache is not accessible
 * the life-cycle of those objects and all of thier referents are bound to the
 * scope of this class. This should allow it to work within the scope of
 * Pipeline without causing memory leaks. Pipelines should be set up to use
 * a single instance of this manager. If they are then they will use a single
 * instance of the target.
 */
public class CachingPluggableHTTPManager extends AbstractPluggableHTTPManager {

    /**
     * For logging information.
     */
    private static Category logger = Category.getInstance(
            "com.volantis.xml.pipeline.sax.drivers.web." +
            "CachingPluggableHTTPManager");

    /**
     * The actual cache
     */
    private final Cache cache;

    /**
     * The delegation target that is used to perform the actual requests.
     */
    private AbstractPluggableHTTPManager target = null;

    /**
     * Create the CachingPluggableHTTPManager
     * @param target the AbstractPluggableHTTPManager that will be used to
     * popualte the cache. This is the abstract class rather then the
     * PluggableHTTPManager as this class relies on the implemenation of the
     * AbstractPluggable.
     * @param cache the cache to use
     */
    public CachingPluggableHTTPManager(final AbstractPluggableHTTPManager target,
                                       final Cache cache) {
        this.cache = cache;
        if (target == null) {
            throw new IllegalArgumentException("target must not be null");
        }
        if (target instanceof CachingPluggableHTTPManager) {
            throw new IllegalArgumentException(
                    "Cannot chain two CachingPluggableHTTPManagers together");
        }
        this.target = target;
    }

    // javadoc inherited
    public void initialize(WebDriverConfiguration configuration,
                           Period timeout) {
        super.initialize(configuration, timeout);
        target.initialize(configuration, timeout);
    }

    /**
     * javadoc inherited.
     */
    public String encodeWithinQuery(String entity)
            throws HTTPException {
        return target.encodeWithinQuery(entity);
    }
    
    /**
     * javadoc inherited.
     */
    public String encodeWithinQuery(String entity, String encoding)
            throws HTTPException {
        return target.encodeWithinQuery(entity, encoding);
    }

    /**
     * Return an executor that potentially uses the cache to fulfill the
     * execute() method.
     *
     * rest of javadoc inherited
     */
    public HTTPRequestExecutor
            createHTTPRequestExecutor(
            final String url,
            final HTTPRequestType requestType,
            final HTTPVersion version,
            final ProxyManager proxyManager,
            final XMLPipelineContext xmlPipelineContext) throws HTTPException {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating and returning new CachingRequestExecutor.");
        }
        return new CachingRequestExecutor(cache, target, url, requestType,
            version, proxyManager, xmlPipelineContext);
    }

    /**
     * Flush the internal http cache. This implementation currently flushes the
     * entire cache.
     * @return 0 as it always succeeds. Note that the cache may not be empty
     * after the flush has completed if concurrent requests cause new entries
     * to be inserted as the flush is proceeding.
     */
    public HTTPCache getHTTPCache() {
        return new HTTPCache() {
            public int flushCache(String filterPattern) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Flushing the entire cache.");
                }
                cache.getRootGroup().flush(null);
                return 0;
            }
        };
    }
}
