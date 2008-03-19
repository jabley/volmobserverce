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
package com.volantis.shared.net.url.http.utils;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

import our.apache.commons.httpclient.cookie.CookieSpecBase;
import our.apache.commons.httpclient.cookie.MalformedCookieException;
import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.Cookie;
import our.apache.commons.httpclient.Header;
import our.apache.commons.httpclient.HttpException;
import our.apache.commons.httpclient.HeaderElement;
import our.apache.commons.httpclient.NameValuePair;
import our.apache.commons.httpclient.protocol.Protocol;

/**
 * Utility functions that work on HttpClient objects.
 */
public class HttpClientUtils {

    /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(HttpClientUtils.class);

    /**
     * The HTTP Client thing used for creating an HTTP Client Header out of
     * an array of HTTP Client cookies.
     */
    private static final CookieSpecBase COOKIE_SPEC = new CookieSpecBase();

    private HttpClientUtils() {
    }


    /**
     * Use HttpClient's CookieSpecBase to parse the Set-Cookie header into
     * an array of HttpClient cookies.
     * @param method The method that the cookie header came from.
     * @param header The Set-Cookie header.
     * @return The array of cookies encapsulated by the Set-Cookie header.
     * @throws MalformedCookieException If ther is a malformed cookie in the
     * header.
     */
    public static Cookie[] createCookieArray(final HttpMethod method,
                                             final Header header)
            throws MalformedCookieException {

        final String path = method.getPath();
        final HostConfiguration hostDetails = method.getHostConfiguration();
        final String host = hostDetails.getHost();
        final int port = hostDetails.getPort();
        final Protocol protocol = hostDetails.getProtocol();
        final boolean isSecure = protocol.isSecure();
        return COOKIE_SPEC.parse(host, port, path, isSecure, header);
    }


    /**
     * HttpClient does not provide access to the maxAge property of its cookie
     * so we need to calculate it from the expiry date and the current time on
     * the client.
     *
     * <p>This is named "Response" because this implies that there must be a max
     * age and therefore an expiry date on the cookie - i.e. the cookie has
     * originated from a response header.</p>
     *
     * @param cookie The HttpClient cookie whose max age to calculate.
     * @return the number of seconds that this cookie has to survive. This value
     * will be -1 (i.e. indefinite) if there is no expiry date set in the
     * cookie.
     */
    public static int calculateResponseCookieMaxAge(
            our.apache.commons.httpclient.Cookie cookie) {
        return calculateResponseCookieMaxAge(cookie, System.currentTimeMillis());
    }

    /**
     * HttpClient does not provide access to the maxAge property of its cookie
     * so we need to calculate it from the expiry date and the current time on
     * the client.
     *
     * <p>This is named "Response" because this implies that there must be a max
     * age and therefore an expiry date on the cookie - i.e. the cookie has
     * originated from a response header.</p>
     *
     * @param cookie The HttpClient cookie whose max age to calculate.
     * @return the number of seconds that this cookie has to survive. This value
     * will be -1 (i.e. indefinite) if there is no expiry date set in the
     * cookie.
     */
    public static int calculateResponseCookieMaxAge(
            our.apache.commons.httpclient.Cookie cookie, long baseTime) {

        Date expiryDate = cookie.getExpiryDate();
        int maxAge = -1;
        if (expiryDate != null) {
            long expiryMillis = expiryDate.getTime();

            long ageDiffMillis = expiryMillis - baseTime;
            long maxAgeInSeconds = ageDiffMillis / 1000;

            // We need to represent the max age as an int (The constructor
            // of the HTTPClient Cookie only accepts the max age as an int).

            // However it has been observed that some sites send cookies
            // that expire a long distance into the furture, e.g 2036!!!!
            // This is a long way off at time of writing (2006) and as such
            // the calculated value for maxAgeInSeconds cannot be represented
            // as a int and we therefore get a negative value when we attempt
            // to cast to an int.  This results in the max age being set
            // to zero, hence the cookie does not get sent on subsequent
            // requests, which is incorrect.

            // To solve this issue we will check if the value for
            // maxAgeInSeconds can be represented as an int.  If so, fine - go
            // ahead and cast to an int. Otherwise we will set the max age in
            // seconds to maximum value that can be represented with an int.

            if (maxAgeInSeconds <= Integer.MAX_VALUE &&
                    maxAgeInSeconds >= Integer.MIN_VALUE) {
                maxAge = (int) maxAgeInSeconds;
            } else if (maxAgeInSeconds > Integer.MAX_VALUE) {
                maxAge = Integer.MAX_VALUE;
            } else if (maxAgeInSeconds < Integer.MIN_VALUE) {
                maxAge = Integer.MIN_VALUE;
            }

            if (maxAge < 0) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Cookie max-age is negative: " + maxAge);
                }
                // If we get a negative value when an expiry date has been
                // set, then this means that the expiry date exists in the
                // past and the cookie should therefore not be sent on
                // subsequent request, hence we are setting a value of
                // 0 indicating that the cookie has expired.
                maxAge = 0;
            }
        }
        return maxAge;
    }

    /**
     * Returns a list of header values as java.lang.String objects.
     *
     * @param header The HttpClient header.
     */
    public static List getHeaderValueList(final Header header)
            throws HttpException {

        final List result;
        final HeaderElement[] elements = header.getValues();
        if (elements != null && elements.length > 0) {
            result = new LinkedList();
            for (int i = 0; i < elements.length; i++) {
                final HeaderElement element = elements[i];
                result.add(toString(element));
            }
        } else {
            result = Collections.EMPTY_LIST;
        }
        return result;
    }

    /**
     * Returns the String representation of the specified header element.
     *
     * @param element the header element, must not be null
     * @return the String representation of the header element
     */
    public static String toString(final HeaderElement element) {

        final StringBuffer buffer = new StringBuffer();
        // append name
        buffer.append(element.getName());
        // append value
        final String value = element.getValue();
        if (value != null) {
            buffer.append('=');
            buffer.append(value);
        }
        // append parameters
        final NameValuePair[] parameters = element.getParameters();
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                final NameValuePair parameter = parameters[i];
                buffer.append(';');
                buffer.append(parameter.getName());
                final String parameterValue = parameter.getValue();
                if (parameterValue != null) {
                    buffer.append('=');
                    buffer.append(parameterValue);
                }
            }
        }
        return buffer.toString();
    }
}
