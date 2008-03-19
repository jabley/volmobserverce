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

import com.volantis.shared.net.url.CachedUrlContentState;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.shared.time.Comparator;

/**
 * CachedUrlContentState implementation for HTTP/HTTPS contents.
 */
public class CachedHttpContentState implements CachedUrlContentState {
    /**
     * Computed estimate for the initial age.
     */
    private final Period correctedInitialAge;

    /**
     * The computed freshness lifetime.
     */
    private final Period freshnessLifetime;

    /**
     * True iff the content is cacheable.
     */
    private final boolean cacheable;

    /**
     * True iff a cache-related header was found.
     */
    private final boolean cacheHeaderFound;

    /**
     * True iff Pragma/no-cache directive was set.
     */
    private final boolean pragmaNoCache;

    /**
     * True iff Cache-Control/public directive was set.
     */
    private final boolean ccPublic;

    /**
     * True iff Cache-Control/private directive was set.
     */
    private final boolean ccPrivate;

    /**
     * True iff Cache-Control/no-cache directive was set.
     */
    private final boolean ccNoCache;

    /**
     * True iff Cache-Control/no-store directive was set.
     */
    private final boolean ccNoStore;

    /**
     * Value of the Cache-Control/max-age directive or null, if it is not set.
     */
    private final Period ccMaxAge;

    /**
     * Value of the Cache-Control/s-maxage directive or null, if it is not set.
     */
    private final Period ccSMaxAge;

    /**
     * Value of the ETag header or null, if it is not set.
     */
    private final String eTag;

    /**
     * Value of the Last-Modified header or null, if it is not set.
     */
    private final Time lastModified;

    /**
     * Value of the Expires header or null, if it is not set.
     */
    private final Time expires;

    /**
     * Value of the Age header or null, if it is not set.
     */
    private final Period age;

    /**
     * Time when response was received in milliseconds.
     */
    private final Time responseTime;

    /**
     * The value of the Vary header.
     */
    private final String vary;

    /**
     * Creates a new CachedHttpContentState object using the information stored
     * in the specified {@link CachedHttpContentInfo}.
     *
     * @param info the base values
     */
    public CachedHttpContentState(final CachedHttpContentInfo info) {
        cacheHeaderFound = info.isCacheHeaderFound();
        pragmaNoCache = info.isPragmaNoCache();
        ccNoCache = info.isCcNoCache();
        ccNoStore = info.isCcNoStore();
        ccPrivate = info.isCcPrivate();
        ccPublic = info.isCcPublic();
        age = info.getAge();
        ccMaxAge = info.getCcMaxAge();
        ccSMaxAge = info.getCcSMaxAge();
        eTag = info.getETag();
        expires = info.getExpires();
        lastModified = info.getLastModified();
        responseTime = info.getResponseTime();
        vary = info.getVary();

        // compute derived fields
        cacheable = !ccNoStore && !ccPrivate &&
            (!info.isHttpsProtocol() || ccPublic) && cacheHeaderFound &&
            (vary == null || vary.length() == 0);

        // compute freshness lifetime
        // see http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.2.4
        final Period lifetime;
        if (pragmaNoCache || ccNoCache || !cacheable) {
            lifetime = Period.ZERO;
        }
        // Cache-Control/s-maxage overwrites both Cache-Control/max-age
        // and Expires
        else if (ccSMaxAge != null) {
            lifetime = ccSMaxAge;
        // Cache-Control/max-age overwrites Expires
        } else if (ccMaxAge != null) {
            lifetime = ccMaxAge;
        } else if (expires != null && info.getDate() != null) {
            lifetime = expires.getPeriodSince(info.getDate());
        // if freshness lifetime cannot be computed, return 0
        } else {
            lifetime = Period.ZERO;
        }
        freshnessLifetime = Period.max(Period.ZERO, lifetime);

        // compute corrected initial age
        // see http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.2.3
        Period apparentAge = Period.ZERO;
        if (info.getDate() != null) {
            apparentAge = Period.max(Period.ZERO,
                responseTime.getPeriodSince(info.getDate()));
        }
        Period correctedReceivedAge = apparentAge;
        if (age != null) {
            correctedReceivedAge = Period.max(apparentAge, age);
        }
        final Period responseDelay = Period.max(Period.ZERO,
            responseTime.getPeriodSince(info.getRequestTime()));
        correctedInitialAge = correctedReceivedAge.add(responseDelay);
    }

    /**
     * Returns true iff the content is cacheable.
     *
     * @return true for cacheable contents, otherwise false
     */
    public boolean isCacheable() {
        return cacheable;
    }

    // javadoc inherited
    public boolean isStale(final Time currentTime) {
        final Period currentAge = getCurrentAge(currentTime);
        return Comparator.GE.compare(currentAge, freshnessLifetime);
    }

    /**
     * Returns the age of the HTTP content at the specified time.
     *
     * @param currentTime the time at which the age is computed
     *
     * @return the age of the HTTP content
     */
    public Period getCurrentAge(final Time currentTime) {
        final Period residentTime = currentTime.getPeriodSince(responseTime);
        return residentTime.add(correctedInitialAge);
    }

    /**
     * Returns the time to live value computed by using the specified time as
     * the current time.
     *
     * <p>Returns Period.ZERO, if the TTL period were negative.</p>
     *
     * @param currentTime the current time
     * @return the time to live value from the specified time
     */
    public Period getTimeToLive(final Time currentTime) {
        return Period.max(Period.ZERO,
            freshnessLifetime.subtract(getCurrentAge(currentTime)));
    }

    /**
     * Returns the corrected initial age.
     *
     * <p>The value is computed as it is described at
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.2.3</p>
     * @return the corrected initial age
     */
    public Period getCorrectedInitialAge() {
        return correctedInitialAge;
    }

    /**
     * Returns the freshness lifetime.
     *
     * <p>The value is computed as it is described at
     * http://www.w3.org/Protocols/rfc2616/rfc2616-sec13.html#sec13.2.4</p>
     * @return the freshness lifetime
     */
    public Period getFreshnessLifetime() {
        return freshnessLifetime;
    }

    /**
     * Returns the cacheHeaderFound property.
     *
     * @return the value of the property
     */
    public boolean isCacheHeaderFound() {
        return cacheHeaderFound;
    }

    /**
     * Returns the stored value of the Pragma/no-cache directive.
     *
     * @return the value of the property
     */
    public boolean isPragmaNoCache() {
        return pragmaNoCache;
    }

    /**
     * Returns the stored value of the Cache-Control/no-cache directive.
     *
     * @return the value of the property
     */
    public boolean isCcNoCache() {
        return ccNoCache;
    }

    /**
     * Returns the stored value of the Cache-Control/no-store directive.
     *
     * @return the value of the property
     */
    public boolean isCcNoStore() {
        return ccNoStore;
    }

    /**
     * Returns the stored value of the Cache-Control/private directive.
     *
     * @return the value of the property
     */
    public boolean isCcPrivate() {
        return ccPrivate;
    }

    /**
     * Returns the stored value of the Cache-Control/public directive.
     *
     * @return the value of the property
     */
    public boolean isCcPublic() {
        return ccPublic;
    }

    /**
     * Returns the value of the Age header or null if it was not set.
     *
     * @return the value of the property
     */
    public Period getAge() {
        return age;
    }

    /**
     * Returns the stored value of the Cache-Control/max-age directive.
     *
     * @return the value of the property
     */
    public Period getCcMaxAge() {
        return ccMaxAge;
    }

    /**
     * Returns the stored value of the Cache-Control/s-maxage directive.
     *
     * @return the value of the property
     */
    public Period getCcSMaxAge() {
        return ccSMaxAge;
    }

    /**
     * Returns the value of the ETag header or null if it was not set.
     *
     * @return the value of the property
     */
    public String getETag() {
        return eTag;
    }

    /**
     * Returns the value of the Expires header or null if it was not set.
     *
     * @return the value of the property
     */
    public Time getExpires() {
        return expires;
    }

    /**
     * Returns the value of the Last-Modified header or null if it was not set.
     *
     * @return the value of the property
     */
    public Time getLastModified() {
        return lastModified;
    }

    /**
     * Returns the value of the Vary header.
     *
     * @return the value of the Vary header
     */
    public String getVary() {
        return vary;
    }
}
