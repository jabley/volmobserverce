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

package com.volantis.mcs.protocols.css.reference;

import com.volantis.mcs.cache.Cache;
import com.volantis.mcs.cache.CacheIdentity;
import com.volantis.mcs.cache.css.CSSCacheEntry;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.css.CSSModule;
import com.volantis.mcs.protocols.css.PlaceHolder;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.css.WritableCSSEntity;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;

/**
 * A reference to some external CSS.
 */
public final class ExternalCSSReference
        implements CssReference {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ExternalCSSReference.class);
    /**
     * The CSS protocol module.
     */
    private final CSSModule cssModule;

    /**
     * The CSS cache into which the generated css is to be stored.
     */
    private final Cache cssCache;

    /**
     * The configuration for style sheet generation.
     */
    private final StyleSheetConfiguration configuration;

    /**
     * The path to the context.
     */
    private final MarinerURL contextPathURL;

    /**
     * The place holder for the reference to the CSS.
     */
    private PlaceHolder placeHolder;
    
    /**
     * The page context. 
     */
    private final MarinerPageContext context;

    /**
     * Initialise.
     *
     * @param cssModule
     * @param cssCache
     * @param configuration
     * @param contextPathURL
     */
    public ExternalCSSReference(CSSModule cssModule, Cache cssCache,
                                StyleSheetConfiguration configuration,
                                MarinerURL contextPathURL,
                                MarinerPageContext context) {
        this.cssModule = cssModule;
        this.cssCache = cssCache;
        this.configuration = configuration;
        this.contextPathURL = contextPathURL;
        this.context = context;
    }

    // Javadoc inherited.
    public void writePlaceHolderMarkup(OutputBuffer buffer) {
        placeHolder = cssModule.addExternalPlaceHolder(buffer);
    }

    // Javadoc inherited.
    public void updateMarkup(final String css) {
        WritableCSSEntity entity = new GeneratedWritableCSSEntity(css);

        // Generate TTL for CSS Cache Entry (in milliseconds)
        final ResponseCachingDirectives cachingDirectives =
            context.getEnvironmentContext().getCachingDirectives();
        long timeToLive = -1;
        if (cachingDirectives != null) {
            final Period ttl = cachingDirectives.getTimeToLive();
            if (ttl != null) {
                timeToLive = ttl.inMillis();
            }
        }

        // Create and store CSS Cache Entry.
        final CacheIdentity identity =
            cssCache.store(new CSSCacheEntry(entity, timeToLive));

        MarinerURL baseURL = getBaseURL();
        baseURL.setParameterValue("key", identity.getBase64KeyAsString());

        final String sessionType = configuration.getCssSessionType();
        if (sessionType != null && StyleSheetConfiguration.INCLUDE_SESSION_ID.equals(sessionType)) {
            baseURL = context.getSessionURLRewriter().mapToExternalURL(
                    context.getRequestContext(), baseURL);
        }
        String url = baseURL.getExternalForm();
        if (logger.isDebugEnabled()) {
            logger.debug("Generated CSS is accessible from: " + url);

        }

        cssModule.updateExternalPlaceHolder(placeHolder, url);
    }

    /**
     * Get the base URL for the CSS servlet.
     *
     * @return The base URL for the CSS servlet.
     */
    private MarinerURL getBaseURL() {
        MarinerURL baseURL = configuration.getBaseURL();
        if (baseURL == null) {
            baseURL = new MarinerURL(contextPathURL, "CSSServlet");
        } else {
            baseURL = new MarinerURL(baseURL);
        }
        return baseURL;
    }

    private static class GeneratedWritableCSSEntity implements WritableCSSEntity {
        private final String css;

        public GeneratedWritableCSSEntity(String css) {
            this.css = css;
        }

        public void write(Writer writer) throws IOException {
            writer.write(css);
        }
    }
}
