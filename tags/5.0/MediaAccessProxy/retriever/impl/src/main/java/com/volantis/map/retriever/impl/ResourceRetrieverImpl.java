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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.retriever.impl;

import com.volantis.map.retriever.DefaultSeekableInputStream;
import com.volantis.map.retriever.Representation;
import com.volantis.map.retriever.ResourceRetriever;
import com.volantis.map.retriever.ResourceRetrieverException;
import com.volantis.map.retriever.SeekableInputStream;
import com.volantis.map.retriever.CloseListener;
import com.volantis.map.retriever.http.HttpFactory;
import com.volantis.map.retriever.http.HttpHeaders;
import com.volantis.map.retriever.http.MutableHttpHeaders;
import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.http.client.HttpClientFactory;
import com.volantis.shared.net.url.http.CachedHttpContentStateBuilder;
import com.volantis.shared.net.url.http.ResponseHeaderAccessorFactory;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.mime.DefaultMimeDiscoverer;
import com.volantis.synergetics.mime.MimeDiscoverer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.methods.GetMethod;

/**
 * Implemetation ResourceRetriver component.
 * It reciever resources for others components in MAP
 */
public class ResourceRetrieverImpl implements ResourceRetriever {
        
     /**
     * Used for logging
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(
              ResourceRetrieverImpl.class);
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(
              ResourceRetrieverImpl.class);        
    
    /**
     * The timeout, in milliseconds, to apply to connections. If negative or
     * zero, no timeout is applied.
     */
    private static final Period CONNECTION_TIMEOUT = Period.INDEFINITELY;    

    /**
     * The timeout, in milliseconds, to apply to roundTrip. 
     */
    private static final Period ROUNDTRIP_TIMEOUT = Period.INDEFINITELY;    
    
    /**
     * The MimeTypeDiscoverer to use.
     */
    private MimeDiscoverer mimeDiscoverer;


    // javadoc inherited
    public MutableHttpHeaders createMutableHeaders() {
        return HttpFactory.getDefaultInstance().createMutableHTTPHeaders();
    }

    /**
     * Is is possible use another implementation MimeDiscoverer 
     * than DefaultMimeDiscoverer
     * @param mimeDiscoverer implementation of MimeDiscoverer
     */
    public ResourceRetrieverImpl(MimeDiscoverer mimeDiscoverer) {
        this.mimeDiscoverer = mimeDiscoverer;
    }

    /**
     * Default constructor
     */
    public ResourceRetrieverImpl() {
        this.mimeDiscoverer = new DefaultMimeDiscoverer();         
    }


    // javadoc inherited
    public Representation execute(URL url, HttpHeaders headers)
        throws ResourceRetrieverException, IOException {

        if(! (url.getProtocol().equalsIgnoreCase("http")
                || url.getProtocol().equalsIgnoreCase("https"))) {
            
            throw new ResourceRetrieverException(exceptionLocalizer.format(
                    "protocol-not-supported", 
                    new String [] {url.getProtocol(), "http"}));
        }
        
        // create HTTPClient
        HttpClient httpClient = prepareHttpClient();
                
        // create request method and configure it        
        final GetMethod method = new GetMethod();
        
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(url.getHost(), url.getPort(), url.getProtocol());        
        method.setHostConfiguration(hostConfig);
        method.setPath(url.getPath());
        method.setFollowRedirects(true);        
        
        // create cache information object
        SystemClock clock = SystemClock.getDefaultInstance();
        CachedHttpContentStateBuilder cacheBuilder = new CachedHttpContentStateBuilder();
        cacheBuilder.setMethodAccessor(
            ResponseHeaderAccessorFactory.getDefaultInstance().
                createHttpClientResponseHeaderAccessor(method));
        cacheBuilder.setRequestTime(clock.getCurrentTime());        
        
        // save request headers to request method
        setRequestHeaders(method, headers);

        DefaultRepresentation responseInfo = null;        
        
        // execute request
        try {
            httpClient.executeMethod(method);
        } catch (IOException e) {
            throw new ResourceRetrieverException(exceptionLocalizer.format(
                    "connection-refused", url.toString()), e);
        } 
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Requested resource at: " + url.toString());
        }
        
        // If the get failed then return immediately.
        if (method.getStatusCode() == 200) {
                
            // get response caching information
            cacheBuilder.setResponseTime(clock.getCurrentTime());
                
            // read resource stream
            InputStream stream = method.getResponseBodyAsStream();

            // a custom closer to ensure the method releases its connection
            CloseListener closer = new CloseListener() {
                public void close() {
                    method.releaseConnection();
                }
            };

            SeekableInputStream seekableStream =
                new DefaultSeekableInputStream(closer, stream, true);
                
            if(stream != null) {

                // the mimeDiscoverer should not effect the Seekable stream
                // and should restore its position but we mark it just in case.
                String mimeType = null;
                try {
                    seekableStream.mark();
                    mimeType = mimeDiscoverer.discoverMimeType(seekableStream);
                } finally {
                    seekableStream.reset();
                }
                responseInfo = new DefaultRepresentation(
                    method,
                    mimeType,
                    cacheBuilder.build(),
                    seekableStream);
            }
        } else {
            LOGGER.error(
                "request-failed",
                new Object[]{url.toString(),
                    method.getStatusCode()+""});
            throw new ResourceRetrieverException(
                "error-response-code", "" + method.getStatusCode());
        }
        return responseInfo;

        // Note that we DO NOT close the connection at this point. that is
        // done by calling close on the Represenation or the stream obtained
        // from it
    }

    /**
     * Prepare HTTPClient for request,
     * Set headers if are pass in parameters 
     */
    private HttpClient prepareHttpClient() {
        HttpClientFactory factory = HttpClientFactory.getDefaultInstance();
        HttpClientBuilder builder = factory.createClientBuilder(); 
        builder.setConnectionTimeout(CONNECTION_TIMEOUT);
        builder.setRoundTripTimeout(ROUNDTRIP_TIMEOUT);            
        return builder.buildHttpClient();
    }     

    /**
     * Set headers, cookies in http request 
     * 
     * @param method GetMethod
     * @param headers contains all headers and cookies 
     * which should be send in request
     */
    private void setRequestHeaders(GetMethod method, HttpHeaders headers) {        
        if(headers != null) {
            Enumeration enumeration = headers.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String headerName = (String) enumeration.nextElement();
                Enumeration multiValues = headers.getHeaders(headerName);
                while(multiValues.hasMoreElements()) {                        
                    method.setRequestHeader(
                        headerName, (String) multiValues.nextElement()); 
                }
            }            
        }                                      
    }
    
}
