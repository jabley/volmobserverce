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

package com.volantis.shared.net.http;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.log.LogDispatcher;
import our.apache.commons.httpclient.Header;
import our.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

/**
 * Implementation of {@link HttpGetMethod} that wraps a HTTP client
 * {@link GetMethod}.
 */
public class HttpGetMethodImpl
        implements HttpGetMethod {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HttpGetMethodImpl.class);

    /**
     * The system clock.
     */
    private final SystemClock clock;

    /**
     * The underlying {@link GetMethod}.
     */
    private final GetMethod method;

    /**
     * The destination URL.
     */
    private final String url;

    /**
     * The connection timeout in milliseconds.
     */
    private final Period connectionTimeout;

    /**
     * Executes methods.
     */
    private final MethodExecuter executer;

    /**
     * Initialise.
     *
     * @param clock             The system clock.
     * @param executer          Executes methods.
     * @param method            The underlying {@link our.apache.commons.httpclient.methods.GetMethod}.
     * @param url               The destination URL.
     * @param connectionTimeout The connection timeout in milliseconds.
     */
    public HttpGetMethodImpl(
            SystemClock clock, MethodExecuter executer, GetMethod method,
            String url, Period connectionTimeout) {
        this.clock = clock;
        this.executer = executer;
        this.method = method;
        this.url = url;
        this.connectionTimeout = connectionTimeout;
    }

    // Javadoc inherited.
    public HttpStatusCode execute() throws IOException {

        // Log the time it took to retrieve the object and if the object
        // retrieval was successful if debug logging is enabled.
        Time startTime = clock.getCurrentTime();

        HttpStatusCode code = null;
        try {
            code = executer.execute(method);
        } catch (InterruptedIOException e) {
            // Connection timed out.
                code = HttpStatusCode.RESPONSE_TIMED_OUT;
                if (logger.isErrorEnabled()) {
                    logger.error("Received exception but assumed timed out", e);
                }

        }

        if (logger.isDebugEnabled()) {
            if (code == HttpStatusCode.RESPONSE_TIMED_OUT) {
                String message = "Remote request to '" + url +
                        "' timed-out " +
                        "at " + connectionTimeout + "milliseconds.";
                logger.debug(message);
            } else {
                Time now = clock.getCurrentTime();
                Period retrievalDuration = now.getPeriodSince(startTime);
                String message = "Remote request to '" + url +
                        "' retrieval time was " +
                        retrievalDuration + ".";
                logger.debug(message);
            }
        }
        return code;
    }

    // Javadoc inherited.
    public void addRequestHeader(String headerName, String value) {
        method.addRequestHeader(headerName, value);
    }

    // Javadoc inherited.
    public InputStream getResponseBodyAsStream()
            throws IOException {

        return method.getResponseBodyAsStream();
    }

    // Javadoc inherited.
    public void releaseConnection() {
        method.releaseConnection();
    }

    // Javadoc inherited.
    public Header getResponseHeader(String headerName) {
        return method.getResponseHeader(headerName);
    }
}
