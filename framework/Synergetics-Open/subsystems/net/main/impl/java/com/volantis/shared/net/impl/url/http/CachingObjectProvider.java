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
package com.volantis.shared.net.impl.url.http;

import com.volantis.cache.CacheEntry;
import com.volantis.cache.Cache;
import com.volantis.cache.group.Group;
import com.volantis.cache.provider.CacheableObjectProvider;
import com.volantis.cache.provider.ProviderResult;
import com.volantis.shared.net.impl.url.InternalURLContentManager;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.http.CachedHttpContentState;
import com.volantis.shared.net.url.http.CachedHttpContentStateBuilder;
import com.volantis.shared.net.url.http.HttpContent;
import com.volantis.shared.net.url.http.HttpUrlConfiguration;
import com.volantis.shared.net.url.http.RuntimeHttpException;
import com.volantis.shared.net.url.http.CachedHttpContent;
import com.volantis.shared.net.url.http.CacheableDependency;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.DateFormats;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.text.DateFormat;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;

/**
 * CacheableObjectProvider that returns HttpContents.
 *
 * <p>Sends validation headers and handles 304 Not Modified responses for
 * effective content retrival.</p>
 */
public class CachingObjectProvider implements CacheableObjectProvider {
    /**
     * Constant for the If-Modified-Since header name.
     */
    private static final String HEADER_IF_MODIFIED_SINCE =
        "If-Modified-Since";

    /**
     * Constant for the If-None-Match header name.
     */
    private static final String HEADER_IF_NONE_MATCH = "If-None-Match";

    /**
     * The cache for which this object provider is used.
     */
    private final Cache cache;

    /**
     * Cache group for retrieved entries.
     */
    private final Group group;

    /**
     * The content manager to retrieve the HTTP contents.
     */
    private final InternalURLContentManager manager;

    /**
     * Timeout period used for HTTP(S) requests.
     */
    private final Period timeout;

    /**
     * HTTP configuration to be used to retrieve the content.
     */
    private final HttpUrlConfiguration httpConfig;

    /**
     * Creates an instance.
     *
     * @param cache the cache that will use this object provider
     * @param group the cache group to be used for retrieved entries
     * @param manager manager to retrieve the HTTP contents
     * @param timeout timeout period used for HTTP(S) requests
     */
    public CachingObjectProvider(final Cache cache,
                                 final Group group,
                                 final InternalURLContentManager manager,
                                 final Period timeout,
                                 final HttpUrlConfiguration httpConfig) {
        this.cache = cache;
        this.group = group;
        this.manager = manager;
        this.timeout = timeout;
        this.httpConfig = httpConfig;
    }

    // javadoc inherited
    public ProviderResult retrieve(
            final SystemClock clock, final Object key, final CacheEntry entry) {

        final URL url = (URL) key;

        // create the content retriever that will actually get the content
        // (eihter just validating the existing value or retrieving the content)
        final CacheAwareHttpContentRetriever contentRetriever =
            new CacheAwareHttpContentRetriever(cache, manager, entry, clock);

        ProviderResult result;
        try {
            // get the content
            final URLContent content =
                contentRetriever.retrieve(url, timeout, httpConfig);
            // store the cache entry state in the result
            final CachedHttpContentState state =
                contentRetriever.getCacheState();
            result =
                new ProviderResult(content, group, state.isCacheable(), state);
        } catch (IOException e) {
            result = new ProviderResult(e, group, false, null);
        }
        return result;
    }

    /**
     * HttpContentRetriever to retrieve HTTP contents for the cache. Handles
     * validation headers and 304 Not Modified responses.
     */
    public class CacheAwareHttpContentRetriever
            extends AbstractHttpContentRetriever {

        /**
         * The cache to be updated.
         */
        private final Cache cache;

        /**
         * The cache entry to update, may be null if the entry was previously
         * marked as being uncacheable.
         */
        private final CacheEntry entry;

        /**
         * Clock to be used to perform age related calculations.
         */
        private final SystemClock clock;

        /**
         * Cache entry state for the retrieved HTTP content. Stored for later
         * retrieval.
         */
        private CachedHttpContentState cacheState;

        /**
         * Builder to create the cache entry state.
         */
        private CachedHttpContentStateBuilder builder;

        /**
         * Creates a new instance.
         *
         * @param cache the cache this content is stored in
         * @param manager content manager to retrieve URL content from the web
         * @param entry cache entry to update, may be null if the entry was
         * previously marked as being uncacheable
         * @param clock to be used to perform age related calculations
         */
        public CacheAwareHttpContentRetriever(final Cache cache,
                final InternalURLContentManager manager, final CacheEntry entry,
                final SystemClock clock) {
            super(manager);
            this.cache = cache;
            this.entry = entry;
            this.clock = clock;
            builder = new CachedHttpContentStateBuilder();
        }

        // javadoc inherited
        protected void preExecute(final HttpMethod method) {
            if (entry != null) {
                final CachedHttpContentState existingState =
                    (CachedHttpContentState) entry.getExtensionObject();
                if (existingState != null) {
                    // this is a validation
                    // set the If-Modified-Since header using the stored
                    // last modified value (if there is any)
                    final Time lastModified = existingState.getLastModified();
                    if (lastModified != null) {
                        final String lastModifiedAsString;
                        // SimpleDateFormat is not thread safe so create a
                        // new instance
                        DateFormat RFC1123 = DateFormats.RFC_1123_GMT.create();

                        lastModifiedAsString = RFC1123.format(
                            new Date(lastModified.inMillis()));

                        method.setRequestHeader(HEADER_IF_MODIFIED_SINCE,
                            lastModifiedAsString);
                    }

                    // set the If-None-Match header with the eTag value, if
                    // there is any
                    final String eTag = existingState.getETag();
                    if (eTag != null && eTag.trim().length() > 0) {
                        method.setRequestHeader(HEADER_IF_NONE_MATCH, eTag);
                    }
                }
            }
            // record request time
            builder.setMethodAccessor(new HttpClientResponseHeaderAccessor(method));
            builder.setRequestTime(clock.getCurrentTime());
        }

        // javadoc inherited
        protected HttpContent createHttpContent(
                final URL url, final HttpMethod method) throws IOException {

            // record response time
            builder.setResponseTime(clock.getCurrentTime());

            // get the content either from the response or from the cache
            final HttpContent content;
            final int statusCode = method.getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                cacheState = builder.build();
                final HttpContent returnedContent =
                    new HttpClientHttpContent(method);
                if (!cacheState.isCacheable()) {
                    content = returnedContent;
                } else {
                    content = new CachedHttpContent(
                        returnedContent, cacheState, clock,
                        new CacheableDependency(url, cacheState, clock, cache,
                            CachingObjectProvider.this));
                }
            } else if (statusCode == HttpStatus.SC_NOT_MODIFIED) {
                // not modified, so it is OK to get the content from the cache
                final CachedHttpContent cachedContent =
                    (CachedHttpContent) entry.getValue();
                // 304 Not Modified is only possible if it is a revalidation
                // cache entry holds the exisiting cache config as an
                // extension object
                cacheState = builder.merge(
                    ((CachedHttpContentState) entry.getExtensionObject()));
                cachedContent.combineHeaders(
                    new HttpClientHttpContent(method), cacheState);
                content = cachedContent;
            } else {
                method.releaseConnection();
                throw new IOException("Unexpected status code - " + statusCode);
            }
            return content;
        }

        /**
         * Returns the cache entry state stored after creating the HTTP content.
         *
         * @return the cache entry state
         */
        public CachedHttpContentState getCacheState() {
            return cacheState;
        }
    }
}
