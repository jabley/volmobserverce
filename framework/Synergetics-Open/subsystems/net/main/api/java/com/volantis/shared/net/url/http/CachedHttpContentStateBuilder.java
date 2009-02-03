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
package com.volantis.shared.net.url.http;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.log.LogDispatcher;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateParser;

/**
 * Builder to create and merge {@link CachedHttpContentState} objects
 */
public class CachedHttpContentStateBuilder {

    /**
     * For logging information.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(CachedHttpContentStateBuilder.class);

    private static final String HEADER_NAME_PRAGMA = "Pragma";
    private static final String HEADER_NAME_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_NAME_ETAG = "ETag";
    private static final String HEADER_NAME_LAST_MODIFIED = "Last-Modified";
    private static final String HEADER_NAME_DATE = "Date";
    private static final String HEADER_NAME_EXPIRES = "Expires";
    private static final String HEADER_NAME_AGE = "Age";
    private static final String HEADER_NAME_VARY = "Vary";

    /**
     * HTTP Method that contains the cache related information.
     */
    private HttpResponseHeaderAccessor methodAccessor;

    /**
     * Time when request was sent.
     */
    private Time requestTime;

    /**
     * Time when response was received.
     */
    private Time responseTime;

    /**
     * Creates an initial builder.
     */
    public CachedHttpContentStateBuilder() {
    }

    /**
     * Sets the HTTP Method that contains the cache related information.
     *
     * @param methodAccessor the method accessor
     */
    public void setMethodAccessor(
            final HttpResponseHeaderAccessor methodAccessor) {
        this.methodAccessor = methodAccessor;
    }

    /**
     * Sets the time when request was sent.
     *
     * @param requestTime the time of request
     */
    public void setRequestTime(final Time requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * Time when response was received.
     *
     * @param responseTime the time of response
     */
    public void setResponseTime(final Time responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * Creates an {@link CachedHttpContentState} object from the specified
     * paremeters.
     *
     * @return the created object, never returns null
     */
    public CachedHttpContentState build() {
        final CachedHttpContentInfo info = new CachedHttpContentInfo();
        mergeInfo(info);
        return new CachedHttpContentState(info);
    }

    /**
     * Merges the information stored in the specified HTTP method and the
     * request/response times into an existing state.
     *
     * <p>Creates a new state object the specified state object is left
     * unchanged.</p>
     *
     * @param prevState the state to be used as a base state
     * @return the merged object, never returns null
     */
    public CachedHttpContentState merge(
            final CachedHttpContentState prevState) {

        // Restore the necessary bits from the state
        final CachedHttpContentInfo info = new CachedHttpContentInfo();
        info.setAge(prevState.getAge());
        info.setCacheHeaderFound(prevState.isCacheHeaderFound());
        info.setPragmaNoCache(prevState.isPragmaNoCache());
        info.setCcMaxAge(prevState.getCcMaxAge());
        info.setCcNoCache(prevState.isCcNoCache());
        info.setCcNoStore(prevState.isCcNoStore());
        info.setCcPrivate(prevState.isCcPrivate());
        info.setCcPublic(prevState.isCcPublic());
        info.setCcSMaxAge(prevState.getCcSMaxAge());
        info.setETag(prevState.getETag());
        info.setExpires(prevState.getExpires());
        info.setLastModified(prevState.getLastModified());
        info.setVary(prevState.getVary());

        mergeInfo(info);
        return new CachedHttpContentState(info);
    }

    /**
     * Merges the information stored in the specified HTTP method and the
     * request/response times into the specified CachedHttpContentInfo object.
     *
     * @param info the CachedHttpContentInfo object to merge into
     */
    private void mergeInfo(final CachedHttpContentInfo info) {

        // check if all the fields were set
        checkFields();

        final String protocol = methodAccessor.getProtocol();
        info.setProtocol(protocol);
        final Header pragmaHeader = getHeader(HEADER_NAME_PRAGMA);
        if (pragmaHeader != null) {
            info.setPragmaNoCache(false);
            HeaderElement[] values;
            try {
                values = pragmaHeader.getValues();
            } catch (HttpException e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("parse-header-error", HEADER_NAME_PRAGMA, e);
                }
                values = null;
            }
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    final HeaderElement element = values[i];
                    final String name = element.getName();
                    if (name.equalsIgnoreCase("no-cache")) {
                        info.setPragmaNoCache(true);
                        info.setCacheHeaderFound(true);
                    }
                }
            }
        }
        final Header cacheControlHeader = getHeader(HEADER_NAME_CACHE_CONTROL);
        if (cacheControlHeader != null) {
            // clear the previous values
            info.setCcNoCache(false);
            info.setCcNoStore(false);
            info.setCcPublic(false);
            info.setCcPrivate(false);
            info.setCcMaxAge(null);
            info.setCcSMaxAge(null);
            info.setCacheHeaderFound(true);

            HeaderElement[] values;
            try {
                values = cacheControlHeader.getValues();
            } catch (HttpException e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("parse-header-error", HEADER_NAME_CACHE_CONTROL,
                        e);
                }
                values = null;
            }
            if (values != null) {
                for (int i = 0; i < values.length; i++) {
                    final HeaderElement element = values[i];
                    final String name = element.getName();
                    if (name.equalsIgnoreCase("no-cache")) {
                        info.setCcNoCache(true);
                    } else if (name.equalsIgnoreCase("no-store")) {
                        info.setCcNoStore(true);
                    } else if (name.equalsIgnoreCase("public")) {
                        info.setCcPublic(true);
                    } else if (name.equalsIgnoreCase("private")) {
                        info.setCcPrivate(true);
                    } else if (name.equalsIgnoreCase("private")) {
                        info.setCcPrivate(true);
                    } else if (name.equalsIgnoreCase("max-age")) {
                        info.setCcMaxAge(getDirectiveAsPeriod(
                            HEADER_NAME_CACHE_CONTROL, element));
                    } else if (name.equalsIgnoreCase("s-maxage")) {
                        info.setCcSMaxAge(getDirectiveAsPeriod(
                            HEADER_NAME_CACHE_CONTROL, element));
                    }
                }
            }
        }

        final Header varyHeader = getHeader(HEADER_NAME_VARY);
        if (varyHeader != null) {
            info.setVary(varyHeader.getValue());
        }

        final Header eTagHeader = getHeader(HEADER_NAME_ETAG);
        if (eTagHeader != null) {
            info.setCacheHeaderFound(true);
            info.setETag(eTagHeader.getValue());
        }

        final Header lastModifiedHeader = getHeader(HEADER_NAME_LAST_MODIFIED);
        if (lastModifiedHeader != null) {
            info.setLastModified(getHeaderAsTime(HEADER_NAME_LAST_MODIFIED));
        }

        info.setDate(getHeaderAsTime(HEADER_NAME_DATE));

        final Header expiresHeader = getHeader(HEADER_NAME_EXPIRES);
        if (expiresHeader != null) {
            info.setCacheHeaderFound(true);
            info.setExpires(getHeaderAsTime(HEADER_NAME_EXPIRES));
        }

        final Header ageHeader = getHeader(HEADER_NAME_AGE);
        if (ageHeader != null) {
            info.setCacheHeaderFound(true);
            info.setAge(getHeaderAsPeriod(HEADER_NAME_AGE));
        }

        info.setResponseTime(responseTime);
        info.setRequestTime(requestTime);
    }

    private Header getHeader(final String headerName) {
        final String headerValue =
            methodAccessor.getResponseHeaderValue(headerName);
        final Header header;
        if (headerValue != null) {
            header = new Header(headerName, headerValue);
        } else {
            header = null;
        }
        return header;
    }

    /**
     * Checks if all of the fields contain non-null values. Throws
     * IllegalStateException if there is a field with null value.
     */
    private void checkFields() {
        if (methodAccessor == null) {
            throw new IllegalStateException("HTTP Method was not set");
        }
        if (requestTime == null) {
            throw new IllegalStateException("Request time was not set");
        }
        if (responseTime == null) {
            throw new IllegalStateException("Response time was not set");
        }
    }

    /**
     * Returns the value of the specified header directive as an Integer object.
     *
     * <p>Returns null, if the value cannot be parsed.</p>
     *
     * @param headerName the name of the header
     * @param element the header directive
     * @return the Integer object or null
     */
    private static Period getDirectiveAsPeriod(final String headerName,
                                               final HeaderElement element) {
        try {
            final int seconds = Integer.parseInt(element.getValue());
            return Period.inSeconds(seconds);
        } catch (NumberFormatException nfe) {
            LOGGER.warn("parse-directive-failure",
                headerName + ": " + element.getName(), nfe);
            return null;
        }
    }

    /**
     * Returns the value of the specified header as an Integer object.
     *
     * <p>Returns null, if the value cannot be parsed or the header doesn't
     * exist.</p>
     *
     * @param headerName the name of the header
     * @return the Integer object or null
     */
    private Period getHeaderAsPeriod(final String headerName) {
        final Header header = getHeader(headerName);
        Period result = null;
        if (header != null) {
            try {
                final int seconds = Integer.parseInt(header.getValue());
                result = Period.inSeconds(seconds);
            } catch (NumberFormatException e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("parse-header-error", headerName, e);
                }
            }
        }
        return result;
    }

    /**
     * Returns the value of the specified header as time.
     *
     * <p>Returns null, if the value cannot be parsed or the header doesn't
     * exist.</p>
     *
     * @param headerName the name of the header
     * @return the time or null
     */
    private Time getHeaderAsTime(final String headerName) {
        final Header header = getHeader(headerName);
        Time result = null;
        if (header != null) {
            try {
                result = Time.inMilliSeconds(
                    DateParser.parseDate(header.getValue()).getTime());
            } catch (DateParseException e) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("parse-header-error", headerName, e);
                }
            }
        }
        return result;
    }
}
