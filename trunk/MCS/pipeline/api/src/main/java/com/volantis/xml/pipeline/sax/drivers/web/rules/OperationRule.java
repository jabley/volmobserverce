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

package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.drivers.ConnectionConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestOperationProcess;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPVersion;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The base rule for the webd:get and webd:post operations.
 */
public class OperationRule
        extends AbstractAddProcessRule {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(OperationRule.class);

    /**
     * The type of request.
     */
    private final HTTPRequestType requestType;

    /**
     * Initialise.
     *
     * @param requestType The type of request.
     */
    public OperationRule(HTTPRequestType requestType) {
        if (requestType == null) {
            throw new IllegalArgumentException("requestType cannot be null");
        }
        this.requestType = requestType;
    }

    // Javadoc inherited.
    protected XMLProcess createProcess(
            DynamicProcess dynamicProcess, ExpandedName elementName,
            Attributes attributes) throws SAXException {

        HTTPRequestOperationProcess operation =
                new HTTPRequestOperationProcess();

        operation.setRequestType(requestType);

        // The fully specified URL of the web server including protocol
        // and ports e.g. http://www.volantis.com:8080/index.html.
        // This value is required.
        String url = attributes.getValue("url");
        if (url != null) {

            operation.setUrlString(url);

            String value;

            // The version of the HTTP protocol to use when making the request.
            // If not specified then HTTP 1.1 is assumed.
            if ((value = attributes.getValue("version")) != null) {
                operation.setHTTPVersion(HTTPVersion.httpVersion(value));
            }
            // This attribute is used to identify the particular instance of
            // the connector and can be used in conjunction with Mariner xPath
            // expressions to obtain the headers, parameters and cookies
            // returned in the response to the request.
            if ((value = attributes.getValue("id")) != null) {
                operation.setId(value);
            }

            // This flag is used to determine whether the operation process
            // silently follows HTTP 302 response codes.
            if ((value = attributes.getValue("followRedirects")) != null) {
                operation.setFollowRedirects(value);
            }

            // This flag is used to determine whether the operation process
            // sends http error content throught the pipeline or ignores it.
            if ((value = attributes.getValue("ignoreErroredContent")) != null) {
                operation.setIgnoreErroredContent(value);
            }

            // Set the timeout on the operation.
            Period period = determineTimeout(dynamicProcess, attributes);
            operation.setTimeout(period);

        } else {
            forwardError(dynamicProcess, "URL is required. Value is: " + url);
        }

        return operation;
    }

    /**
     * Determine the timeout to use.
     *
     * <p>This will first look at the timeout on the operation element, if that
     * is not specified then it will look at the web driver configuration
     * timeout and if that is not specified then it will look at
     * the connection configuration timeout.</p>
     *
     * @param dynamicProcess The process to use to report errors.
     * @param attributes     The attributes to check for the timeout.
     * @return The timeout.
     * @throws SAXException If there was a problem.
     */
    private Period determineTimeout(
            DynamicProcess dynamicProcess,
            Attributes attributes) throws SAXException {

        String value = attributes.getValue("timeout");
        Period timeout = Period.INDEFINITELY;
        if (value != null) {
            int timeoutInSeconds = -1;

            // The attribute should contain an integer number of seconds
            try {
                timeoutInSeconds = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // todo should make this a recoverable error.
                logger.warn("invalid-timeout", value);
                throw e;
            }

            // Convert the timeout in seconds into a Period.
            timeout = Period.treatNonPositiveAsIndefinitely(
                    timeoutInSeconds * 1000);
        } else {
            // get hold of the pipeline context
            XMLPipelineContext context = dynamicProcess.getPipelineContext();

            // get hold of the pipeline configuration
            XMLPipelineConfiguration pipelineConfiguration =
                    context.getPipelineConfiguration();

            WebDriverConfiguration configuration = (WebDriverConfiguration)
                    pipelineConfiguration.retrieveConfiguration(
                            WebDriverConfiguration.class);

            if (configuration == null) {
                // cannot get hold of the configuration. As this is fatal
                // deliver a fatal error down the pipeline
                SAXParseException exception = forwardFatalError(dynamicProcess,
                        "Could not retrieve the Web Driver configuration");
                throw exception;

            } else {
                long timeoutInMillis = -1;
                if (configuration.timeoutWasSet()) {
                    timeoutInMillis = configuration.getTimeoutInMillis();
                } else {
                    // There is no configured timeout, so let's take the one,
                    // if any, specified in the generic connection timeout
                    // (remembering to convert from seconds to milliseconds).
                    ConnectionConfiguration connectionConfiguration =
                            (ConnectionConfiguration) pipelineConfiguration.
                            retrieveConfiguration(
                                    ConnectionConfiguration.class);

                    if ((connectionConfiguration != null) &&
                            (connectionConfiguration.getTimeout() > 0)) {
                        timeoutInMillis =
                                connectionConfiguration.getTimeout() * 1000;
                    }
                }

                timeout = Period.treatNonPositiveAsIndefinitely(
                        timeoutInMillis);
            }
        }

        return timeout;
    }
}
