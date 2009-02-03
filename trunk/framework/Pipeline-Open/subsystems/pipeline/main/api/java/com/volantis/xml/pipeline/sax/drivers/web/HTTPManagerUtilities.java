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
package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.HTTPMessageEntity;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.url.URLConfigurationFactory;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;


/**
 * A class containing utility methods used by the *PluggableHTTPManagers.
 */
public class HTTPManagerUtilities {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HTTPManagerUtilities.class);

    /**
     * A SimpleHTTPMessageEntityMerger for use by this class.
     */
    private static HTTPMessageEntityMerger HEADER_MERGER =
            new SimpleHTTPMessageEntityMerger(WebRequestHeader.class);


    /**
     * A SimpleHTTPMessageEntityMerger for use by this class.
     */
    private static HTTPMessageEntityMerger PARAMETER_MERGER =
            new SimpleHTTPMessageEntityMerger(WebRequestParameter.class);

    /**
     * A CookieMerger for use by this class.
     */
    private static HTTPMessageEntityMerger COOKIE_MERGER =
            new SimpleCookieMerger();

    /**
     * Stop this class from being instantiated.
     */
    private HTTPManagerUtilities() {
    }

    /**
     * Constructs the full url from the path url and the request parameters.
     * If parameters already exist in the query string this doesn't add the original ones
     * as this is likely a redirect so the specified url will be returned, otherwise  
     * this returns a URL with all the request parameters added as a query
     * string. 
     * This method is necessary due the backward compability but currently is deprecated.
     * 
     * @param url The url to which the request parameters should be added. This
     * url may or may not already contain query parameters.
     * @param requestParameters a list of HTTPMessageEntity objects that
     * represent the query parameters to add to the request.
     * @return the original url with the specified request parameters added or only the 
     * orginal url if the parameters already exist in the query string.
     * @deprecated use {@link com.volantis.xml.pipeline.sax.drivers.web.HTTPManagerUtilities#createQueryURL(String, List, AbstractPluggableHTTPManager, boolean)} 
     * instead
     */
    public static String createQueryURL(String url, List requestParameters,
                                        AbstractPluggableHTTPManager manager) {
        return createQueryURL(url,requestParameters,manager,false);
    }
    
    /**
     * Constructs the full url from the path url and the request parameters.
     * When the given url doesn't contain any parameters in the query string 
     * this will returns a URL with all the request parameters added as a query string.
     * If the parameters already exist in the query string and <code>forceAppend</code> flag
     * is switched on it will append the original ones from <code>requestParameters<code> 
     * list  to the end of the current query string. When <code>forceAppend</code> flag 
     * is switched off and parameters already exist in the query string this doesn't add the original 
     * ones as this is likely a redirect so the specified url will be returned. 
     *
     * @param url The url to which the request parameters should be added. This
     * url may or may not already contain query parameters.
     * @param requestParameters a list of HTTPMessageEntity objects that
     * represent the query parameters to add to the request.
     * @param forceAppend determine whether the method should append parameters to the query string
     * in case when the parameters already exist in the query string.
     * @return the original url with the specified request parameters added.
     */
    public static String createQueryURL(String url, List requestParameters,
            AbstractPluggableHTTPManager manager, boolean forceAppend) {
        return createQueryURL(url, requestParameters, manager, forceAppend, null);
    }
    
    /**
     * Constructs the full url from the path url and the request parameters.
     * When the given url doesn't contain any parameters in the query string 
     * this will returns a URL with all the request parameters added as a query string.
     * If the parameters already exist in the query string and <code>forceAppend</code> flag
     * is switched on it will append the original ones from <code>requestParameters<code> 
     * list  to the end of the current query string. When <code>forceAppend</code> flag 
     * is switched off and parameters already exist in the query string this doesn't add the original 
     * ones as this is likely a redirect so the specified url will be returned. 
     *
     * @param url The url to which the request parameters should be added. This
     * url may or may not already contain query parameters.
     * @param requestParameters a list of HTTPMessageEntity objects that
     * represent the query parameters to add to the request.
     * @param forceAppend determine whether the method should append parameters to the query string
     * in case when the parameters already exist in the query string.
     * @return the original url with the specified request parameters added.
     */
    public static String createQueryURL(String url, List requestParameters,
            AbstractPluggableHTTPManager manager, boolean forceAppend, String encoding) {
        StringBuffer sb = new StringBuffer();
        sb.append(url); // add the url

        try {
            // see if there are any parameters to add
            if (requestParameters != null && requestParameters.size() > 0) {

                int idx = url.lastIndexOf('?');
                if (idx == -1 || forceAppend) {
                    if (idx == -1) {
                        // create a new query string
                        sb.append('?');
                    } else {
                        // url already contains parameters in the query string
                        sb.append('&');
                    }
                    
                    Iterator it = requestParameters.iterator();
                    while (it.hasNext()) {
                        HTTPMessageEntity e = (HTTPMessageEntity) it.next();
                        
                        // Add the parameter name.
                        sb.append(manager.encodeWithinQuery(
                                e.getName(), encoding));
                        
                        // Add the parameter value (if there was one).
                        String value = e.getValue();
                        if (value != null) {
                            value = value.trim();
                            sb.append("=");
                            sb.append(manager.encodeWithinQuery(
                                    value, encoding));
                        }

                        // if more parameters exist then add the &
                        if (it.hasNext()) {
                            sb.append('&');
                        }
                    }
                }
            }
        } catch (HTTPException e) {
            logger.error("cannot-encode-url-parameters", e);
        }

        return sb.toString();
    }


    /**
     * Returns an HTTPMessageEntites that contains the request headers
     *
     * @param request a <code>WebDriverRequest<code> that identifies any user
     *                defined request headers. Could be null.
     * @param xmlPipelineContext the XML pipeline context.
     * @return an HTTPMessageEntites that contains the request headers
     * @throws HTTPException if an error occurs.
     */
    public static HTTPMessageEntities retrieveRequestHeaders(
            final WebDriverRequest request,
            XMLPipelineContext xmlPipelineContext) throws HTTPException {
        // will store any headers
        final HTTPMessageEntities headers =
                HTTPFactory.getDefaultInstance()
                .createHTTPMessageEntities();
        // create an entity adder that adds request headers to the executor
        DerivableHTTPMessageEntityAdder headerAdder =
                new DerivableHTTPMessageEntityAdder() {
                    // javadoc inherited
                    public void addDerivableHTTPMessageEntity(
                            DerivableHTTPMessageEntity header)
                            throws HTTPException {
                        // obtain the outgoing headers
                        Header[] headerArray = acquireOutgoingHeaders(header,
                                                                      request);
                        for (int i = 0; i < headerArray.length; i++) {
                            headers.add(headerArray[i]);
                        }

                    }
                };
        // add the headers
        addHTTPMessageEntities(WebRequestHeader.class, headerAdder,
                               xmlPipelineContext);

        if (headers.size() == 0 && request != null &&
                request.getHeaders() != null) {

            for(Iterator i = request.getHeaders().iterator();
                i.hasNext(); ) {

                Header header = (Header) i.next();
                headers.add(header);
            }
        }

        // add visited MCS instances header
        final String instances = (String) xmlPipelineContext.getProperty(
            URLConfigurationFactory.VISITED_MCS_INSTANCES_HEADER_NAME);
        if (instances != null) {
            final Header header = HTTPFactory.getDefaultInstance().createHeader(
                URLConfigurationFactory.VISITED_MCS_INSTANCES_HEADER_NAME);
            header.setValue(instances);
            headers.put(header);
        }

        // return the headers
        return headers;
    }

    /**
     * Acquire the headers to use in the outgoing request for a given
     * header. This will take into consideration any headers of the same
     * name in the WebRequest with the result that multiple headers may be
     * returned.
     * @param header The WebRequestHeader whose values to retrieve.
     * @param request the WebDriverRequest that will be inspected for headers
     * of the same name.
     * @return The outgoing header values for the specified header.
     * @throws HTTPException if an error occurs.
     */
    protected static Header[] acquireOutgoingHeaders(
            DerivableHTTPMessageEntity header,
            WebDriverRequest request) throws HTTPException {

        HTTPMessageEntities headers = null;
        if (request != null) {
            headers = request.getHeaders();
        }
        final Header[] values = (Header[]) aquireOutgoingHTTPMessageEntitys(
            header, headers, HEADER_MERGER);
        return values;
    }

    /**
     * Given a DerivableHTTPMessageEntity that specifies the name and
     * possibly other properties, return the actual entities to use. Since a
     * "from" may result in multiple HTTPMesssageEntity objects being
     * referenced there may be multiple entities for a given
     * DerivableHTTPMessageEntity. (NOTE: the spelling is deliberate to
     * distinguish from the collection class.)
     * @param entity The DerivableHTTPMessageEntity whose outgoing
     * HTTPMessageEntity objects to retreive.
     * @return The outgoing versions of the specified entity.
     * @throws HTTPException if an error occurs.
     */
    private static HTTPMessageEntity[] aquireOutgoingHTTPMessageEntitys(
            DerivableHTTPMessageEntity entity,
            HTTPMessageEntities requestEntities,
            HTTPMessageEntityMerger merger)
            throws HTTPException {

        HTTPMessageEntity values [] = null;

        String from = entity.getFrom();
        if (from == null || requestEntities == null) {
            if (entity.getName() == null && (from == null)) {
                throw new HTTPException(
                        "Entity " + entity + " has neither a name " +
                        "nor a from property. This is not valid");
            }
            if (requestEntities == null && from != null) {
                logger.warn("request-entities-null-ignoring-from-property",
                            entity.getName());
            }

            values = (HTTPMessageEntity[])
                Array.newInstance(entity.getClass(), 1);
            values[0] = entity;

        } else {
            // Find the alternative or source entitites in the request that
            // are specified by the "from" property. We need to create an
            // identity for this whose name is derived from "from" and whose
            // other identity properties if any are derived from entity e.g.
            // for cookies.
            HTTPMessageEntity requestEntityArray [] =
                    requestEntities.retrieve(entity.getFromIdentity());

            if (requestEntityArray.length == 0) {
                throw new HTTPException(
                        "Entity " + entity.getName() + "has neither " +
                        "a from nor a value property. This is not valid");
            }

            values = (HTTPMessageEntity[]) Array.newInstance(entity.getClass(),
                                                             requestEntityArray.length);
            // Now merge each requestEntity with the original entity.
            for (int i = 0; i < requestEntityArray.length; i++) {
                HTTPMessageEntity merged =
                        merger.mergeHTTPMessageEntities(entity,
                                                        requestEntityArray[i]);
                values[i] = merged;
            }
        }
        return values;
    }

    /**
     * Returns an HTTPMessageEntites that contains the request cookies
     *
     * @param request a <code>WebDriverRequest<code> that identifies any user
     *                defined cookies headers. Could be null.
     * @param xmlPipelineContext
     *                the XML pipeline context.
     * @return an HTTPMessageEntites that contains the request cookies
     * @throws HTTPException if an error occurs.
     */
    public static HTTPMessageEntities retrieveRequestCookies(
            final WebDriverRequest request,
            XMLPipelineContext xmlPipelineContext) throws HTTPException {
        // will store any cookies
        final HTTPMessageEntities cookies =
                HTTPFactory.getDefaultInstance()
                .createHTTPMessageEntities();

        DerivableHTTPMessageEntityAdder cookieAdder =
                new DerivableHTTPMessageEntityAdder() {
                    // javadoc inherited
                    public void addDerivableHTTPMessageEntity(
                            DerivableHTTPMessageEntity cookie)
                            throws HTTPException {
                        Cookie[] cookieArray = acquireOutgoingCookies(cookie,
                                                                      request);
                        for (int i = 0; i < cookieArray.length; i++) {
                            cookies.add(cookieArray[i]);
                        }
                    }
                };
        // add all the cookies from the request
        addHTTPMessageEntities(WebRequestCookie.class, cookieAdder,
                               xmlPipelineContext);

        if (cookies != null) {
            if (cookies.size() == 0 && request != null &&
                    request.getCookies() != null) {

                for(Iterator i = request.getCookies().iterator();
                    i.hasNext(); ) {

                    Cookie cookie = (Cookie) i.next();
                    cookies.add(cookie);
                }
            }
        }
        // return the cookies
        return cookies;
    }

    /**
     * Returns an HTTPMessageEntites that contains the request parameters
     *
     * @param request a <code>WebDriverRequest<code> that identifies any user
     *                defined parameters headers. Could be null.
     * @param xmlPipelineContext
     *                the XML pipeline context.
     * @return an HTTPMessageEntites that contains the request parameters
     * @throws HTTPException if an error occurs.
     */
    public static HTTPMessageEntities retrieveRequestParameters(
            final WebDriverRequest request,
            XMLPipelineContext xmlPipelineContext) throws HTTPException {
        // will store any parameters
        final HTTPMessageEntities parameters =
                HTTPFactory.getDefaultInstance().createHTTPMessageEntities();

        DerivableHTTPMessageEntityAdder parameterAdder =
                new DerivableHTTPMessageEntityAdder() {
                    // javadoc inherited
                    public void addDerivableHTTPMessageEntity(
                            DerivableHTTPMessageEntity parameter)
                            throws HTTPException {
                        RequestParameter[] paramaterArray =
                                acquireOutgoingParameters(parameter,
                                                          request);
                        for (int i = 0; i < paramaterArray.length; i++) {
                            parameters.add(paramaterArray[i]);
                        }
                    }
                };
        // add the parameters
        addHTTPMessageEntities(WebRequestParameter.class, parameterAdder,
                               xmlPipelineContext);

        if (parameters != null) {
            if (parameters.size() == 0 && request != null &&
                    request.getRequestParameters() != null) {

                for (Iterator i = request.getRequestParameters().iterator();
                    i.hasNext(); ) {

                    RequestParameter parameter = (RequestParameter)i.next();
                    parameters.add(parameter);
                }
            }
        }
        // return the parameters
        return parameters;
    }

    /**
     * Find the specified <code>HTTPMessageEntities</code> in the
     * XMLPipelineContext and add them using the specified
     * DerivableHTTPMessageEntityAdder.
     *
     * @param entityType the class of the entity type that is to be added.
     * @param adder      The DerivableHTTPMessageEntityAdder that will add a
     *                   name and value representing a paramater to something.
     * @param xmlPipelineContext
     *                   the XML pipeline context.
     * @throws HTTPException if an error occurs.
     */
    protected static void addHTTPMessageEntities(Class entityType,
                                                 DerivableHTTPMessageEntityAdder adder,
                                                 XMLPipelineContext xmlPipelineContext)
            throws HTTPException {
        HTTPMessageEntities entities =
                (HTTPMessageEntities) xmlPipelineContext.getProperty(
                        entityType);

        if (entities != null && entities.size() > 0) {
            // If there are any headers defined as properties in the context
            // then merge them with the headers defined in the WebDriverRequest.
            Iterator iterator = entities.iterator();
            while (iterator.hasNext()) {
                DerivableHTTPMessageEntity entity =
                        (DerivableHTTPMessageEntity) iterator.next();
                adder.addDerivableHTTPMessageEntity(entity);
            }
        }
    }

    /**
     * Acquire the parameters to use in the outgoing request for a given
     * parameter. This will take into consideration any parameters of the same
     * name in the WebRequest with the result that multiple parameters may be
     * returned.
     * @param param The WebRequestParameter whose values to retrieve.
     * @param request the WebDriverRequest that will be inspected for paramaters
     * of the same name.
     * @return The outgoing parameter values for the specified parameter.
     * @throws HTTPException if an error occurs.
     */
    protected static RequestParameter[] acquireOutgoingParameters(
            DerivableHTTPMessageEntity param,
            WebDriverRequest request) throws HTTPException {

        HTTPMessageEntities params = null;
        if (request != null) {
            params = request.getRequestParameters();
        }
        final RequestParameter[] values = (RequestParameter[])
                aquireOutgoingHTTPMessageEntitys(param,
                                                 params,
                                                 PARAMETER_MERGER);
        return values;
    }


    /**
     * Acquire the cookies to use in the outgoing request for a given
     * cookie. This will take into consideration any cookies of the same
     * name in the WebRequest with the result that multiple cookies may be
     * returned. It is left to the caller to figure out what to do with
     * multiple cookies of the same name/domain/path.
     * @param cookie The WebRequestHeader whose values to retrieve.
     * @return The outgoing cookie values for the specified cookie.
     * @throws HTTPException if an error occurs.
     */
    protected static Cookie[] acquireOutgoingCookies(
            DerivableHTTPMessageEntity cookie,
            WebDriverRequest request) throws HTTPException {

        HTTPMessageEntities cookies = null;
        if (request != null) {
            cookies = request.getCookies();
        }

        final Cookie[] values = (Cookie[]) aquireOutgoingHTTPMessageEntitys(
            cookie, cookies, COOKIE_MERGER);

        return values;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Mar-05      7501/2  matthew VBM:2005031708 refactor AbstractPluggableHTTPManager

 24-Mar-05      7443/3  matthew VBM:2005031708 refactor AbstractPluggableHTTPManager

 ===========================================================================
*/
