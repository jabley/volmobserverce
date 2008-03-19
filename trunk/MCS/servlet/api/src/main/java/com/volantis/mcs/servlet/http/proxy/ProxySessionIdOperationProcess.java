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
package com.volantis.mcs.servlet.http.proxy;

import java.util.HashSet;
import java.util.Set;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.servlet.ServletEnvironmentInteraction;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterTuple;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;

import javax.servlet.http.HttpServletRequest;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * This class is responsible for rewriting URLs to include a JSESSIONID
 * that represents the session between the client and the proxy.
 *
 * NOTE: This file has been copied from the DSB depot. Any changes required in
 * this file may also need to be made in DSB.
 */
public class ProxySessionIdOperationProcess extends AbstractOperationProcess {

    /**
     * Key used to store the proxy session ID in pipeline context.
     */
    public static final String PROXY_SESSION_ID_KEY = "PROXY_SESSION_ID";

    /**
     * The configuration associated with this process.
     */
    private ProxySessionIdConfiguration configuration;

    /**
     * List of supproted protocols
     */
    private static final Set protocolsSupportingSessionId;
    static {
        protocolsSupportingSessionId = new HashSet();
        protocolsSupportingSessionId.add("http");
        protocolsSupportingSessionId.add("https");
        protocolsSupportingSessionId.add("wml");
    }
    
    /**
     * The request
     */
    private HttpServletRequest request = null;

    // javadoc inherited
    public void setPipeline(XMLPipeline pipeline) {
        super.setPipeline(pipeline);


        // get hold of the pipeline context
        XMLPipelineContext context = getPipelineContext();


        // get hold of the pipeline configuration
        XMLPipelineConfiguration pipelineConfiguration =
                context.getPipelineConfiguration();

        // retrieve the configuration
        Configuration config =
                pipelineConfiguration.retrieveConfiguration(
                        ProxySessionIdOperationProcess.class);

        if (config == null ||
                !(config instanceof ProxySessionIdConfiguration)) {
            // cannot get hold of the configuration. As this is fatal
            // deliver a fatal error down the pipeline
            XMLPipelineException error = new XMLPipelineException(
                    "Could not retrieve the Proxy Session Id",
                    context.getCurrentLocator());

            try {
                pipeline.getPipelineProcess().fatalError(error);
            } catch (SAXException e) {
                // cannot continue so throw a runtime exception
                throw new ExtendedRuntimeException(e);
            }
        }
        // cast the configuration to the correct type and store it away
        configuration = (ProxySessionIdConfiguration) config;

        this.request = getRequest(context);


    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                                    Attributes attrs) throws SAXException {


        ConverterTuple[] tuples =
                configuration.getTuples(namespaceURI, localName);


        if (mayContainSessionId(tuples, attrs)) {

            AttributesImpl newAttrs = new AttributesImpl(attrs);

            String attr;

            // Scoot through the attributes and do the Absolute to Relative
            // conversion for those attributes that actually need it.
            for (int i = 0; i < tuples.length; i++) {

                attr = tuples[i].getAttribute();

                int index;
                if ((index = attrs.getIndex(attr)) != -1) {
                    //
                    // We have found the attribute so we check it for
                    // a Session ID
                    //
                    String value = attrs.getValue(index);
                    
                    if (shouldUpdateSessionId(value)) {
                        // We now check for a Session Id within this
                        // attribute
                        String result = updateSessionId(value);
    
                        // We update the attribute value
                        //
                        newAttrs.setValue(index, result);
                    }


                }
            }

            super.startElement(namespaceURI, localName, qName, newAttrs);
        } else {
            super.startElement(namespaceURI, localName, qName, attrs);
        }


    }


    /**
     * Check if given url is supposed to be updated with sessionId
     *
     * @param url
     * @return
     */
    private boolean shouldUpdateSessionId(String url) {
        boolean result;
        URI uri;
        try {
            // we are intereseted URI scheme so we do not need query
            // that might be unparsable as URI
            int index = url.indexOf("?");
            if(index != -1){
                url = url.substring(0, index);
            }
            uri = new URI(url);
            String scheme = uri.getScheme();
            result = (scheme == null) || protocolsSupportingSessionId.contains(
                    scheme);
        } catch (URISyntaxException e) {
            // unable to parse URL to URI - so we assume protocol is supported
            result = true;
        }
        return result;
    }

    /**
     * Determine whether or not we need to check this element for a Session ID.
     *
     * @param  tuples the array of ConverterTuple objects.
     * @param  attrs  the attributes.
     * @return        true if prefetch is necessary, false otherwise.
     */
    private boolean mayContainSessionId(ConverterTuple[] tuples,
                                        final Attributes attrs) {
        boolean containsSessionId = false;
        if (configuration != null) {

            if (tuples != null) {
                // Check to see if any of the required attributes are listed in
                // the rules found for this namespace/element combination
                boolean found = false;

                for (int i = 0; !found && (i < tuples.length); i++) {
                    found = (attrs.getValue(tuples[i].getAttribute()) != null);
                }

                if (!found) {
                    // No appropriate attributes exist, so reset the rules for
                    // later logic
                    tuples = null;
                }
            }
            containsSessionId = (tuples != null);
        }
        return containsSessionId;
    }

    /**
     * Checks for the specified JSESSION ID param within the value
     * based on the provided configuration.
     *
     */
    protected String updateSessionId(String value) {
        //if (log.isDebugEnabled()) {
        //    log.debug("Entered checkForSessionId");
        //}

        StringBuffer url = new StringBuffer(value);
        // get the proxy session for the dsb->target session
        String serviceId = (String)
                getPipelineContext().getProperty(PROXY_SESSION_ID_KEY);
        HTTPProxySessionInterface targetProxy =
                JSessionIDHandler.getProxySession(this.request, serviceId);

        HTTPProxySessionInterface clientProxy =
                JSessionIDHandler.getClientSideSession(this.request);

        //@todo later this should check that
        // a) mcs -> target = url based
        // the received url has the target session id
        // b) mcs->target = cookie based
        // the received url matches the domain and path of the cookie.
        // currently we just rewrite all urls if path or param sessions are
        // deamed likely
        // Should probably use the UrlRewriter here.

        //update the target proxy and remove param and path style information
        targetProxy.receivedUrl(url);

        clientProxy.encodeUrl(url);

        return url.toString();
    }

    private static HttpServletRequest getRequest(XMLPipelineContext context) {
        HttpServletRequest req = null;
        EnvironmentInteraction ei =
                context.getEnvironmentInteractionTracker().getRootEnvironmentInteraction();
        if (ei instanceof ServletEnvironmentInteraction) {
            req = (HttpServletRequest) ((ServletEnvironmentInteraction) ei).getServletRequest();
        } else {
            //logger.warn("No ServletEnvironmentInteraction found. No prefetches " +
            //      "will be performed");
        }
        return req;
    }
}
