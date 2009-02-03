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

import com.volantis.shared.time.Time;
import com.volantis.shared.time.Period;

/**
 * Modifiable object to collect cache-related information.
 */
public class CachedHttpContentInfo {

    private static final String PROTOCOL_HTTPS = "https";

    /**
     * True iff a cache-related header was found.
     */
    private boolean cacheHeaderFound;

    /**
     * True iff Pragma/no-cache directive was set.
     */
    private boolean pragmaNoCache;

    /**
     * True iff Cache-Control/public directive was set.
     */
    private boolean ccPublic;

    /**
     * True iff Cache-Control/private directive was set.
     */
    private boolean ccPrivate;

    /**
     * True iff Cache-Control/no-cache directive was set.
     */
    private boolean ccNoCache;

    /**
     * True iff Cache-Control/no-store directive was set.
     */
    private boolean ccNoStore;

    /**
     * Value of the Cache-Control/max-age directive or null, if it is not set.
     */
    private Period ccMaxAge;

    /**
     * Value of the Cache-Control/s-maxage directive or null, if it is not set.
     */
    private Period ccSMaxAge;

    /**
     * Value of the ETag header or null, if it is not set.
     */
    private String eTag;

    /**
     * Value of the Last-Modified header or null, if it is not set.
     */
    private Time lastModified;

    /**
     * Value of the Date header or null, if it is not set.
     */
    private Time date;

    /**
     * Value of the Expires header or null, if it is not set.
     */
    private Time expires;

    /**
     * Value of the Age header or null, if it is not set.
     */
    private Period age;

    /**
     * The protocol used.
     */
    private String protocol;

    /**
     * Time when response was received in milliseconds.
     */
    private Time responseTime;

    /**
     * Time when request was sent in milliseconds.
     */
    private Time requestTime;

    /**
     * The value of the Vary header.
     */
    private String vary;

    /**
     * Returns the cacheHeaderFound property.
     *
     * @return the value of the property
     */
    public boolean isCacheHeaderFound() {
        return cacheHeaderFound;
    }

    /**
     * Sets the cacheHeaderFound property.
     *
     * @param cacheHeaderFound the new value of the property.
     */
    public void setCacheHeaderFound(final boolean cacheHeaderFound) {
        this.cacheHeaderFound = cacheHeaderFound;
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
     * Sets the stored value of the Pragma/no-cache directive.
     *
     * @param pragmaNoCache the new value of the property
     */
    public void setPragmaNoCache(final boolean pragmaNoCache) {
        this.pragmaNoCache = pragmaNoCache;
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
     * Sets the stored value of the Cache-Control/no-store directive.
     *
     * @param ccNoStore the new value of the property
     */
    public void setCcNoStore(final boolean ccNoStore) {
        this.ccNoStore = ccNoStore;
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
     * Sets the stored value of the Cache-Control/public directive.
     *
     * @param ccPublic the new value of the property
     */
    public void setCcPublic(final boolean ccPublic) {
        this.ccPublic = ccPublic;
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
     * Sets the stored value of the Cache-Control/private directive.
     *
     * @param ccPrivate the new value of the property
     */
    public void setCcPrivate(final boolean ccPrivate) {
        this.ccPrivate = ccPrivate;
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
     * Sets the stored value of the Cache-Control/no-cache directive.
     *
     * @param ccNoCache the new value of the property
     */
    public void setCcNoCache(final boolean ccNoCache) {
        this.ccNoCache = ccNoCache;
    }

    /**
     * Returns the stored value of the Cache-Control/max-age directive.
     *
     * <p>The value is in seconds.</p>
     *
     * @return the value of the property
     */
    public Period getCcMaxAge() {
        return ccMaxAge;
    }

    /**
     * Sets the stored value of the Cache-Control/max-age directive.
     *
     * @param ccMaxAge the new value of the property in seconds
     */
    public void setCcMaxAge(final Period ccMaxAge) {
        this.ccMaxAge = ccMaxAge;
    }

    /**
     * Returns the stored value of the Cache-Control/s-maxage directive.
     *
     * <p>The value is in seconds.</p>
     *
     * @return the value of the property
     */
    public Period getCcSMaxAge() {
        return ccSMaxAge;
    }

    /**
     * Sets the stored value of the Cache-Control/s-maxage directive.
     *
     * @param ccSMaxAge the new value of the property in seconds
     */
    public void setCcSMaxAge(final Period ccSMaxAge) {
        this.ccSMaxAge = ccSMaxAge;
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
     * Sets the stored value of the ETag header.
     *
     * @param eTag the new value of the property
     */
    public void setETag(final String eTag) {
        this.eTag = eTag;
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
     * Sets the stored value of the Last-Modified header.
     *
     * @param lastModified the new value of the property
     */
    public void setLastModified(final Time lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Returns the value of the Date header or null if it was not set.
     *
     * @return the value of the property
     */
    public Time getDate() {
        return date;
    }

    /**
     * Sets the stored value of the Date header.
     *
     * @param date the new value of the property
     */
    public void setDate(final Time date) {
        this.date = date;
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
     * Sets the stored value of the Expires header.
     *
     * @param expires the new value of the property
     */
    public void setExpires(final Time expires) {
        this.expires = expires;
    }

    /**
     * Returns the value of the Age header or null if it was not set.
     *
     * <p>The value is in seconds.</p>
     *
     * @return the value of the property
     */
    public Period getAge() {
        return age;
    }

    /**
     * Sets the stored value of the Age header.
     *
     * @param age the new value of the property in seconds
     */
    public void setAge(final Period age) {
        this.age = age;
    }

    /**
     * Sets the protocol.
     *
     * @param protocol the new value of the property
     */
    public void setProtocol(final String protocol) {
        this.protocol = protocol;
    }

    /**
     * Returns the time when the response was received in milliseconds.
     *
     * @return the time in milliseconds
     */
    public Time getResponseTime() {
        return responseTime;
    }

    /**
     * Sets the time when the response was received.
     *
     * @param responseTime the time in milliseconds
     */
    public void setResponseTime(final Time responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * Returns the time when the request was sent in milliseconds.
     *
     * @return the time in milliseconds
     */
    public Time getRequestTime() {
        return requestTime;
    }

    /**
     * Sets the time when the request was sent.
     *
     * @param requestTime the time in milliseconds
     */
    public void setRequestTime(final Time requestTime) {
        this.requestTime = requestTime;
    }

    /**
     * Returns true iff the protocol is HTTPS.
     *
     * @return true if HTTPS protocol was used
     */
    public boolean isHttpsProtocol() {
        return PROTOCOL_HTTPS.equalsIgnoreCase(protocol);
    }

    /**
     * Sets the value of the Vary header.
     *
     * @param vary the value of the Vary header
     */
    public void setVary(final String vary) {
        this.vary = vary;
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
