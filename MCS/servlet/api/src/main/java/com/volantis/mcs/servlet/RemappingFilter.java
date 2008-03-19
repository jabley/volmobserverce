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
package com.volantis.mcs.servlet;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.RemoteProjectURLRemapper;
import com.volantis.mcs.runtime.GenericURLRemapper;
import com.volantis.mcs.runtime.URLNormalizer;
import com.volantis.mcs.runtime.pipeline.PipelineInitialization;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.mcs.service.ServiceDefinitionHelper;
import com.volantis.mcs.servlet.http.proxy.HTTPRequestResponseHandler;
import com.volantis.mcs.servlet.http.proxy.JSessionIDHandler;
import com.volantis.mcs.servlet.http.proxy.ProxySessionIdConfiguration;
import com.volantis.mcs.servlet.http.proxy.ProxySessionIdOperationProcess;
import com.volantis.shared.environment.EnvironmentFactory;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.shared.net.http.HTTPFactory;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.cookies.Cookie;
import com.volantis.shared.net.http.cookies.CookieVersion;
import com.volantis.shared.net.http.headers.Header;
import com.volantis.shared.net.http.parameters.RequestParameter;
import com.volantis.shared.servlet.DefaultServletEnvironmentFactory;
import com.volantis.shared.servlet.http.HttpServletEnvironmentInteractionImpl;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.url.URLPrefixRewriteManager;
import com.volantis.synergetics.url.URLPrefixRewriteOperation;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterConfiguration;
import com.volantis.xml.pipeline.sax.convert.ConverterTuple;
import com.volantis.xml.pipeline.sax.convert.URLRewriteProcess;
import com.volantis.xml.pipeline.sax.convert.URLRewriteProcessConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestOperationProcess;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestPreprocessor;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPRequestType;
import com.volantis.xml.pipeline.sax.drivers.web.HTTPResponsePreprocessor;
import com.volantis.xml.pipeline.sax.drivers.web.PluggableHTTPManager;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverAccessor;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfigurationImpl;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverRequest;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverRequestImpl;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverResponse;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverResponseImpl;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicRuleConfigurator;
import com.volantis.xml.xml.serialize.OutputFormat;
import com.volantis.xml.xml.serialize.XMLSerializer;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.AttributesImpl;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * Filter which determines if the specified URL refers to a location in a
 * project which has registered with MCS (using {@link GenericURLRemapper}).
 * If so it remaps the URL, retrieves the remote resource, and returns it for
 * processing.
 */
public class RemappingFilter implements Filter {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(RemappingFilter.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(RemappingFilter.class);

    /**
     * {@link GenericURLRemapper} instance which is used to determine if
     * the URL should be remapped to refer to a location in a project.
     */
    private GenericURLRemapper urlRemapper;
              
    /**
     * The factory used for the pipeline creation
     */
    private XMLPipelineFactory pipelineFactory;

    private FilterConfig filterConfig;

    /**
     * The TUPLES that are required for the URL rewriting.
     */
    private static final ConverterTuple[] TUPLES = new ConverterTuple[]{
        new ConverterTuple(null, "a", "href"),
        new ConverterTuple(null, "form", "action"),
        new ConverterTuple(null, "frame", "src"),
        // Removed the link from the list as currently that is only a link to
        // a policy and as such does not need rewriting as it will be handled
        // separately by the policy code.
        //new ConverterTuple(null, "link", "href"),
        new ConverterTuple(null, "xfform", "action"),
        new ConverterTuple(null, "submission", "action"),

        // This is commented out because we should really be adding this but
        // cannot due to a limitation in the current framework. The is a bug
        // since we cannot associate the 'base' attribute with 'any' element.
        // See URLRewriteProcess#startProcess. todo fix this.
        /*, new ConverterTuple(NamespaceSupport.XMLNS, "",
                ContextManagerProcess.BASE_ATTRIBUTE)
        */
    };


    // Javadoc inherited.
    public void init(FilterConfig filterConfig) throws ServletException {

        //store the filterconfig
        this.filterConfig = filterConfig;
        
        pipelineFactory = XMLPipelineFactory.getDefaultInstance();

        // If a particular RemoteProjectURLRemapper implementation has been
        // specified, then attempt to load it.
        String remapper = filterConfig.getInitParameter("remapper");
        if (remapper != null) {
            try {
                Class remapperClass = Class.forName(remapper);
                urlRemapper =
                        (GenericURLRemapper) remapperClass.newInstance();
            } catch (ClassNotFoundException e) {
                LOGGER.warn("using-default-project-manager", e);
            } catch (IllegalAccessException e) {
                LOGGER.warn("using-default-project-manager", e);
            } catch (InstantiationException e) {
                LOGGER.warn("using-default-project-manager", e);
            }
        }

        // If the RemoteProjectURLRemapper is not set (either deliberately or
        // because the specified class could not be loaded) then use the
        // default implementation.
        if (urlRemapper == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Using the default RemoteProjectURLRemapper " +
                        "implementation");
            }

            urlRemapper = new RemoteProjectURLRemapper();
        }

        // Give the remapper the filter config so that it can initialise.
        urlRemapper.initialise(filterConfig);

        /**
         * This is initialised by MCS, but we're using a separate pipeline so
         * it needs to be done here as well.
         */
        PipelineInitialization.defineNamespaceURIs();
    }

    // Javadoc inherited.
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

          HttpServletRequest httpRequest = (HttpServletRequest) request;
          HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get the application server independent path which was used to call
        // this filter.
        final String filterPath = getFilterPath(httpRequest);
        final String url = httpRequest.getRequestURL().toString();
        String remappedURL = url;
        String transformURL = null;

        ServiceDefinition serviceDef =
                ServiceDefinitionHelper.retrieveService(request);
              
        if(serviceDef != null && 
            serviceDef != ServiceDefinition.DEFAULT_SERVICE_DEFINITION) {            
            //remappedURL = serviceDef.getRemoteTarget();
            transformURL = serviceDef.getTransform();
            // ok we have a service lets use that service remapper
            urlRemapper = new ServiceURLRemapper(serviceDef);
        }
                
        // we do not apply a transform when there is no service
        remappedURL = urlRemapper.remapURL(url, filterPath);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Remapping from original request URL: " + url +
                    " to :" + remappedURL);
        }

        // Check if the URL was remapped.
        if (!remappedURL.equals(url)) {
            retrieveResource(httpResponse,
                             remappedURL,
                             httpRequest,
                             transformURL,
                             urlRemapper);
        } else {
            // Only pass it on the next filter if we haven't already populated
            // the response.
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Retrieves and processes the remote resource as specified by the remote
     * URL
     * @param httpResponse the response
     * @param remappedURL the remapped URL for the XDIME
     * @param httpRequest the request
     * @param transformURL an optional URL that specifies a transform (xslt)
     * that will be applied to the file specified by the remappedURL param
     * @throws IOException if an error occurs
     * @throws ServletException if an error occurs
     */
    private void retrieveResource(final HttpServletResponse httpResponse,
                                  String remappedURL,
                                  HttpServletRequest httpRequest,
                                  String transformURL,
                                  GenericURLRemapper urlRemapper)
            throws IOException, ServletException {

        // Get the application server independent path which was used to call
        // this filter.
        final String filterPath = getFilterPath(httpRequest);
        
        // The supplied URL has been remapped so we must therefore
        // be processing a request for a remote MCS project.

        // Get the name of the project being requested.
        String remoteProjectName = urlRemapper.getRemoteProjectName(filterPath);

        // If so, we need to go and get the resource.
        remappedURL = httpResponse.encodeRedirectURL(remappedURL);
        try {
            
            // create MarinerRequestContext instance
            MarinerRequestContext marinerRequestContext = null;
            try {                    
                MarinerServletRequestContext msrc = 
                    new MarinerServletRequestContext(
                        filterConfig.getServletContext(), httpRequest, httpResponse);
                marinerRequestContext =                 
                    MarinerServletRequestContext.findInstance(httpRequest);                    
            } catch (Exception e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.warn("Cannot create MarinerRequestContext");
                }
                }

            // Create the web driver configuration - this information
            // comes from mcs-config.xml in normal MCS operation.
            WebDriverConfiguration webdConfig = 
                createWebDriverConfig();
            
            // set encoding if available
            // ported from 4.2.0, see vbm 2007070313
            if (marinerRequestContext != null) {
                webdConfig.setCharacterEncoding(
                        marinerRequestContext.getCharacterEncoding());
            }

            // Create a pipeline configuration which knows about the web
            // driver configuration that we've specified, and the default
            // MCS dynamic pipeline configuration.
            XMLPipelineConfiguration configuration =
                    createPipelineConfiguration(webdConfig);

            final XMLPipelineContext pipelineContext =
                    createPipelineContext(configuration,
                            httpRequest, httpResponse);

            setUpJSessionPreprocessors(pipelineContext, httpRequest,
                                       remoteProjectName);

            storeRemoteProject(pipelineContext, remoteProjectName);

            // Push the URL of the resource that we need to retrieve onto
            // the context - this allows relative references in any
            // returned markup to be resolved.
            pipelineContext.pushBaseURI(remappedURL);

                // Create a pipeline 
                XMLPipeline pipeline = pipelineFactory.createPipeline
                    (pipelineContext);
            XMLFilter filter = pipelineFactory.createPipelineFilter(
                    pipeline, true);

            // Create a serializer that will output to the response.
            ServletOutputStream outputStream = httpResponse.getOutputStream();
            XMLSerializer serializer = getSerializer(outputStream);
            filter.setContentHandler(serializer.asContentHandler());

            // Create and initialise the request process.
            HTTPRequestOperationProcess requestOperation =
                    new HTTPRequestOperationProcess();
            requestOperation.setFollowRedirects(
                    Boolean.toString(webdConfig.getFollowRedirects()));
            requestOperation.setUrlString(remappedURL);
            requestOperation.setRequestType(HTTPRequestType.GET);
            // todo this is wrong as the request operation timeout is in
            // todo seconds but the configuration is in milliseconds.
            Period timeout = Period.treatNonPositiveAsIndefinitely(
                    webdConfig.getTimeoutInMillis());
            requestOperation.setTimeout(timeout);

            // This is necessary in order for the request process to be
            // able to pick up the headers, parameters, cookies etc.
            WebDriverAccessor webdAccessor = createWebDriverAccessor(
                    createWebDriverRequest(httpRequest,
                                           marinerRequestContext,
                                           remoteProjectName,
                                           urlRemapper),
                    new WebDriverResponseImpl());
            pipelineContext.setProperty(WebDriverAccessor.class,
                    webdAccessor , false);

            ProxySessionIdOperationProcess proxySessionOperationProcess =
                    new ProxySessionIdOperationProcess();

            XMLProcess urlRewriterProcess =
                    createURLRewriterProcess(httpRequest.getContextPath(),
                                             urlRemapper);

            // Add them to the pipeline, which will start the processes.
            if (transformURL != null) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "href", "href", "string", transformURL);

                pipeline.getPipelineProcess().startElement(Namespace.PIPELINE.getURI(),
                                                       "transform",
                                                       "transform",
                                                       atts);
            }
            pipeline.addHeadProcess(urlRewriterProcess);
            pipeline.addHeadProcess(proxySessionOperationProcess);
            pipeline.addHeadProcess(requestOperation);

            // Removing them from the pipeline will stop the processes.
            pipeline.removeHeadProcess();
            pipeline.removeHeadProcess();
            pipeline.removeHeadProcess();
            if (transformURL != null) {
                pipeline.getPipelineProcess().endElement(Namespace.PIPELINE.getURI(),
                                                       "transform",
                                                       "transform");
            }

            // FLush and close the stream.
            outputStream.flush();
            outputStream.close();

            // Do this so that the pipeline is in a balanced state, despite
            // the fact that AT THE MOMENT no one else using this pipeline.
            pipelineContext.popBaseURI();

            WebDriverResponse webdResponse =
                webdAccessor.getResponse(pipelineContext, null );
            forwardCookies(webdResponse,
                           httpResponse,
                           httpRequest,
                           remoteProjectName,
                           urlRemapper);


            //@todo decide if I need to set this now I'm propagating parameters etc
            httpResponse.setContentType("x-application/vnd.xdime+xml");
        } catch (SAXException e) {
            throw new ServletException(EXCEPTION_LOCALIZER.format(
                    "remote-resource-not-available", remappedURL), e);
        }
    }

    /**
     * <p>
     * Retrieve the path which was used to call this filter.
     * </p>
     * <p>
     * Different application servers interpret the servlet spec differently
     * when it comes to what {@link HttpServletRequest#getServletPath} and
     * {@link HttpServletRequest#getPathInfo} should return when queried from
     * a {@link Filter}.
     * </p>
     * <p>
     * Some servers treat this situation identically to being called from a
     * {@link javax.servlet.Servlet} (i.e. Tomcat) whereas some consider the
     * servlet path to be empty, and the pathInfo to contain the data
     * (i.e. Weblogic).
     * </p>
     * <p>
     * For example, if MCS is running on <i>http://localhost:8080/volantis</i>
     * and a request is made to <i>http://localhost:8080/volantis/welcome/welcome.xdime</i>
     * then when we call H{@link HttpServletRequest#getServletPath} and
     * {@link HttpServletRequest#getPathInfo} from the RemappingFilter, the
     * following values are returned:
     * <table>
     * <tr>
     * <th>Server</th>
     * <th>servletPath</th>
     * <th>pathInfo</th></tr>
     * <tr>
     * <td>Tomcat:</td>
     * <td>"/welcome/welcome.xdime"</td>
     * <td>null</td>
     * </tr>
     * <tr>
     * <td>Weblogic:</td>
     * <td>""</td>
     * <td>"/welcome/welcome.xdime"</td>
     * </tr>
     * </table>
     * </p>
     * <p>
     * The solution is to use servletPath if pathInfo is null, and to
     * concatenate the two if not. (For the purposes of remapping the URL, we
     * are not interested in whether the path came from the servletPath or
     * pathInfo).
     * </p>
     * <p>
     * NB: This was added in order to fix vbm 2007041622.
     * </p>
     * @param httpRequest to use to determine the filter path.
     * @return String path which was used to call this filter
     */
    protected String getFilterPath(HttpServletRequest httpRequest) {
        final String servletPath = httpRequest.getServletPath();
        final String pathInfo = httpRequest.getPathInfo();

        String filterPath = servletPath;
        if (pathInfo != null) {
            filterPath += pathInfo;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Remapping request URL when servletPath: " +
                    servletPath + ", and pathInfo: " + pathInfo);
        }
        return filterPath;
    }

    /**
     * Creates a ServletEnvironmentInteraction instance appropriate for the
     * current request/response pair.
     * 
     * NOTE: The HttpServletEnvironmentInteractionImpl is being used here in an
     * inappropriate context, as this is a filter and not a servlet. At this
     * point this is ok as the object is only being used to retrieve the
     * request. todo introduce a new EnvironmentInteraction Heirachy for Filter
     * environments.
     * 
     * @return an initialized ServletEnvironmentInteraction instance
     */
    private EnvironmentInteraction createRootEnvironmentInteraction(
            HttpServletRequest request,
            HttpServletResponse response) {

        return new HttpServletEnvironmentInteractionImpl(
                new DefaultServletEnvironmentFactory().createEnvironment(
                        filterConfig.getServletContext()),
                null,
                null,
                request,
                response);
    }

    /**
     * Create the XML URL rewrite process and return it. The rewrite process
     * will rewrite values which match the remote project locations in the
     * urlRemapper to the contextpath/project name value.
     *
     * @return the newly created URL rewrite process.
     */
    private XMLProcess createURLRewriterProcess(
            String contextPath, GenericURLRemapper urlRemapper) {
        XMLProcess process = null;
        if (contextPath != null) {
            URLRewriteProcessConfiguration urlRewriteConfig =
                    new URLRewriteProcessConfiguration();

            ConverterConfiguration convertConfig =
                    urlRewriteConfig.getConverterConfiguration();
            convertConfig.setTuples(TUPLES);

            URLPrefixRewriteManager rewriteManager =
                    urlRewriteConfig.getURLPrefixRewriteManager();

            //Add rewritableURLPrefix values for all the remote projects.
            //The values are constructed by mapping the remote project location
            //to the contextPath/project.
            //e.g. if there is a project mapping jsp->http://localhost:8080
            //and a context value of 'volantis', http://localhost:8080/blah/blah
            //will be remapped to http://localhost:8080/volantis/jsp/blah/blah
            Iterator iterator = urlRemapper.pathPrefixIterator();
            while (iterator.hasNext()) {
                String project = (String) iterator.next();
                URL remoteMCSProjectLocation =
                        urlRemapper.getRemoteSiteRootURL(project);

                rewriteManager.addRewritableURLPrefix(
                        remoteMCSProjectLocation.toExternalForm(),
                            contextPath+"/"+project,
                                      URLPrefixRewriteOperation.REPLACE_PREFIX);
            }

            process = new URLRewriteProcess(urlRewriteConfig);
        }
        return process;
    }
    
    /**
     * Registers request and response preprocessors that handle the
     * JSessions between the client, MCS and the target.
     *
     * @param pipelineContext the Pipeline configuration.
     * @param httpRequest the current servlet request.
     * @param mcsRemoteProjectName the name of the remote project
     * being accessed used as a key for storing information about the client
     * and the target sessions in the session associated with the current
     * request.
     */
    public void setUpJSessionPreprocessors(
            final XMLPipelineContext pipelineContext,
            final HttpServletRequest httpRequest,
            final String mcsRemoteProjectName) {

        HTTPRequestResponseHandler handler =
                new HTTPRequestResponseHandler(httpRequest,
                                               mcsRemoteProjectName);

        // set up the request preprocessor
        pipelineContext.setProperty(
                HTTPRequestPreprocessor.class, handler, false);
        pipelineContext.setProperty(
                HTTPResponsePreprocessor.class, handler, false);

    }

    /**
     * Forward cookie sent back from the target server to the original client.
     *
     * @param webdResponse the webd response.
     * @param httpResponse the servlet response.
     * @param httpRequest  the servlet request.
     * @param remoteProjectName the name of the remote project being accessed.
     * @param urlRemapper used to remap url's
     */
    private void forwardCookies(WebDriverResponse webdResponse,
                                HttpServletResponse httpResponse,
                                HttpServletRequest httpRequest,
                                String remoteProjectName,
                                GenericURLRemapper urlRemapper) {

        // Get the cookies sent from the remote target server, if any
        HTTPMessageEntities cookies = webdResponse.getCookies();


        if (cookies != null) {
            Iterator cookiesIter = cookies.iterator();
            while (cookiesIter.hasNext()) {
                Cookie cookie = (Cookie)cookiesIter.next();

                // Not setting the domain explicitly as the domain of the
                // created cookie will default to the host that made the
                // request to the remote resource, in this case this is
                // the host running MCS, which is correct.
                String newDomain = null;
                
                String originalCookiePath = cookie.getPath();
                // If the cookie path is unspecified then use the
                // root path, as per RFC 2109
                if (originalCookiePath == null ||
                    originalCookiePath.length() == 0) {
                    originalCookiePath = "/";
                }
                
                // Now rewrite the path attribute relative to MCS. 
                String cookiePathRelativeToMCS = 
                        rewriteCookiePath(originalCookiePath, 
                                          httpRequest,
                                          remoteProjectName,
                                          urlRemapper);
                
                javax.servlet.http.Cookie sunCookie =
                        copyCookieData(cookie, newDomain,
                                       cookiePathRelativeToMCS);

                httpResponse.addCookie(sunCookie);

            }
        }
    }

    /**
     * Rewrites the supplied cookie path relative to MCS.
     *
     * @param originalCookiePath the original path.
     * @param request the current request.
     * @param remoteProjectName the current remote project name
     * @param urlRemapper used to remap url's
     *
     * @return the rewritten cookie path.
     */
    private String rewriteCookiePath(String originalCookiePath,
                                     HttpServletRequest request,
                                     String remoteProjectName,
                                     GenericURLRemapper urlRemapper) {

        CookiePathRewriter cookiePathRewriter = new CookiePathRewriter();
        URL remoteProjectRoot = 
                urlRemapper.getRemoteSiteRootURL(remoteProjectName);
        String newPath = cookiePathRewriter.rewritePath(remoteProjectRoot,
                                                        remoteProjectName,
                                                        originalCookiePath,
                                                        request.getContextPath());
        return newPath;
    }

    /**
     * Creates a <CODE>Cookie</CODE> from a Volantis cookie
     * object and copies its values across, replacing the domain and path.
     *
     * @param sourceCookie The original cookie to replicate
     * @param newDomain The new domain for the output cookie
     * @param newPath The new path for the output cookie
     * @return A new <CODE>Cookie</CODE> object containing the new domain
     *         and path with data from the source cookie; or null if the
     *         supplied sourceCookie has an illegal name according to RFC 2109.
     */
    private javax.servlet.http.Cookie copyCookieData(
            com.volantis.shared.net.http.cookies.Cookie sourceCookie,
            String newDomain, String newPath) {

        javax.servlet.http.Cookie destCookie;
        try {
            destCookie = new javax.servlet.http.Cookie(sourceCookie.getName(),
                                    sourceCookie.getValue());
        } catch (IllegalArgumentException e) {
            // the supplied sourceCookie has an illegal name according to
            // RFC 2109.

            // Returning null as we are unable to create a Cookie
            return null;

        }
        destCookie.setComment(sourceCookie.getComment());
        destCookie.setMaxAge(sourceCookie.getMaxAge());
        destCookie.setPath(newPath);
        destCookie.setSecure(sourceCookie.isSecure());

        // Can't set null as a domain value explicitly, so if we're using
        // the default value, just don't set domain.
        if (newDomain != null) {
            destCookie.setDomain(newDomain);
        }

        return destCookie;
    }


    /**
     * Create and initialise a {@link WebDriverConfiguration} which can be used
     * to configure a {@link PluggableHTTPManager}.
     * <p/>
     * The following properties will not be changed from their default value:
     * <ul>
     * <li>no http cache configuration</li>
     * <li>not handling ignorable content</li>
     * <li>using response's character encoding</li>
     * <li>ignoring errored content</li>
     * <li>no timeout</li>
     * <li>No proxies</li>
     * <li>no script modules</li>
     * <li>No conditioner factories</li>
     * </ul>
     *
     * @return WebDriverConfiguration which can be used to configure a
     * {@link PluggableHTTPManager}.
     */
    private WebDriverConfiguration createWebDriverConfig() {

        WebDriverConfigurationImpl webdConfig =
                new WebDriverConfigurationImpl();

        // indicate that the remapping filter should automatically
        // silently follow HTTP response 302 redirects
        webdConfig.setFollowRedirects(true);

        // indicate that the webdriver should attempt to remap
        // redirected URLs. This will only work if there is a rewrite
        // manager
        webdConfig.setRemapRedirects(true);

        URLRewriteProcessConfiguration urlRewriteConfig =
                    new URLRewriteProcessConfiguration();

        webdConfig.setRedirectRewriteManager(
                urlRewriteConfig.getURLPrefixRewriteManager());

        // indicate that if a redirect url cannot be remapped, we
        // should not attempt to follow the original url.
        webdConfig.setFollowUnsuccessfulRedirectRemaps(false);
                  
        return webdConfig;
    }



    private TransformConfiguration createTransformConfig() {

        TransformConfiguration config =
                new DefaultTransformConfiguration();

        config.setTemplateCacheRequired(false);
        config.setTemplateCompilationRequired(false);

        return config;
    }


    /**
     * Create a {@link WebDriverRequest} which has been populated using the
     * supplied {@link HttpServletRequest}.
     *
     * @param httpRequest   from which to populate the web driver request
     * @param remoteProjectName
     * @param urlRemapper used to remap url's
     * @return WebDriverRequest populated using information from the http
     * servlet request
     */
    private WebDriverRequest createWebDriverRequest(
            HttpServletRequest httpRequest,
            MarinerRequestContext marinerRequestContext,
            String remoteProjectName,
            GenericURLRemapper urlRemapper) {

        WebDriverRequest request = new WebDriverRequestImpl();

        final HTTPFactory httpFactory = HTTPFactory.getDefaultInstance();

        HTTPMessageEntities headers = httpFactory.createHTTPMessageEntities();
        Enumeration headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            final String name = (String) headerNames.nextElement();
            final Header header = httpFactory.createHeader(name);
            header.setValue(httpRequest.getHeader(name));
            headers.add(header);
        }

        HTTPMessageEntities parameters = httpFactory.createHTTPMessageEntities();
        // get parameters from marinerRequestContext if available
        // ported from 4.2.0, see vbm 2007070313
        Enumeration parameterNames = (marinerRequestContext != null) ?
            marinerRequestContext.getParameterNames() :
            httpRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = (String) parameterNames.nextElement();
            final RequestParameter parameter =
                    httpFactory.createRequestParameter(name);
            final String value = (marinerRequestContext != null) ?
                    marinerRequestContext.getParameter(name) : 
                    httpRequest.getParameter(name);   
            parameter.setValue(value);
            parameters.add(parameter);
        }

        // HTTP-Client will only send cookies if a domain and path is supplied.
        // Unfortunately when using Netscape cookies, (Version 0), domain
        // and path information is not sent in the COOKIE header, hence when
        // Netscape cookies are used we do not have values for the domain
        // and path.
        URL remoteMCSProjectLocation =
                urlRemapper.getRemoteSiteRootURL(remoteProjectName);

        // However, the fact that non JSESSIONID cookies have been sent to
        // MCS indicates that they should be sent to the target server.  So
        // we can just set the domain equal to the remote target host
        // and the path to "/".
        final String defaultDomain = remoteMCSProjectLocation.getHost();
        final String defaultPath = "/";

        HTTPMessageEntities cookies = httpFactory.createHTTPMessageEntities();
        javax.servlet.http.Cookie[] httpCookies = httpRequest.getCookies();

        if (httpCookies != null) {
            for (int i = 0; i < httpCookies.length; i++) {
                javax.servlet.http.Cookie httpCookie = httpCookies[i];
                final Cookie cookie = httpFactory.createCookie(httpCookie.getName(),
                        defaultDomain, defaultPath);
                cookie.setMaxAge(httpCookie.getMaxAge());
                cookie.setSecure(httpCookie.getSecure());

                cookie.setValue(httpCookie.getValue());
                cookie.setVersion(CookieVersion.getCookieVersion(httpCookie.getVersion()));
                cookies.add(cookie);
            }
        }

        request.setHeaders(headers);
        request.setRequestParameters(parameters);
        request.setCookies(cookies);

        return request;
    }

    /**
     * Create a pipeline which has been configured with the supplied
     * {@link WebDriverConfiguration} and the default MCS dynamic rules.
     *
     * @param webdConfig    Web driver configuration with which to configure
     *                      the pipeline configuration
     * @return XMLPipelineConfiguration
     */
    private XMLPipelineConfiguration createPipelineConfiguration(
            WebDriverConfiguration webdConfig) {

        final XMLPipelineConfiguration XMLPipelineConfig =
                pipelineFactory.createPipelineConfiguration();

        XMLPipelineConfig.storeConfiguration(
                WebDriverConfiguration.class, webdConfig);

        XMLPipelineConfig.storeConfiguration(TransformConfiguration.class,
                                             createTransformConfig());

        // create the dynamic configuration
        DynamicProcessConfiguration dynamicConfiguration =
                pipelineFactory.createDynamicProcessConfiguration();

        // get hold of all the rules need for testing
        DynamicRuleConfigurator ruleConfigurator =
                pipelineFactory.getRuleConfigurator();

        // configure the dynamic configuration
        ruleConfigurator.configure(dynamicConfiguration);

        // store the dynamic configuration away in the pipeline configuration
        XMLPipelineConfig.storeConfiguration(
                DynamicProcessConfiguration.class,
                dynamicConfiguration);

        XMLPipelineConfig.storeConfiguration(
                ProxySessionIdOperationProcess.class,
                createProxySessionIdConfiguration());

        return XMLPipelineConfig;
    }

    /**
     * Factory method that creates a {@link XMLPipelineContext} instance.
     *
     * @param XMLPipelineConfig    the pipeline configuration
     * @return XMLPipelineContext
     */
    private XMLPipelineContext createPipelineContext(
            XMLPipelineConfiguration XMLPipelineConfig,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        // Create a NamespacePrefixTracker...
        NamespacePrefixTracker namespaceTracker =
                pipelineFactory.getNamespaceFactory().createPrefixTracker();

        // ...and an EnvironmentInteractionTracker.
        EnvironmentFactory environmentFactory =
                EnvironmentFactory.getDefaultInstance();
        EnvironmentInteractionTracker envTracker =
                environmentFactory.createInteractionTracker();

        //create a root environment interaction to encapsulate the request
        EnvironmentInteraction rootEnvironmentInteraction =
                createRootEnvironmentInteraction(
                        httpRequest, httpResponse);
        envTracker.pushEnvironmentInteraction(rootEnvironmentInteraction);

        final ExpressionContext exprContext =
                pipelineFactory.getExpressionFactory().
                createExpressionContext(envTracker, namespaceTracker);

      

        XMLPipelineContext xmlPipelineContext =
                pipelineFactory.createPipelineContext(
                        XMLPipelineConfig, exprContext);


        return xmlPipelineContext;
    }

    /**
     * Stores the service ID in the pipeline context so that proxy sessions
     * can be retrieved based on the service.
     * @param context The context to store the service ID in
     * @param serviceId The service ID to store
     */
    public void storeRemoteProject(XMLPipelineContext context,
                                      String serviceId) {
        context.setProperty(ProxySessionIdOperationProcess.PROXY_SESSION_ID_KEY,
                serviceId, false);
    }

    /**
     * create a configuration for the ProxySessionIDOperationProcess
     *
     * @return the configuration.
     */
    private Configuration createProxySessionIdConfiguration() {
        ConverterTuple[] tuples = {
            new ConverterTuple(null, "a", "href"),
            new ConverterTuple(null, "xfform", "action"),
        };
        ProxySessionIdConfiguration config = new ProxySessionIdConfiguration(
                tuples, JSessionIDHandler.TARGET_SESSION);
        //config.setServerNode(serverNode);
        return config;

    }

    // Javadoc inherited.
    public void destroy() {
    }

    /**
     * Create a WebDriverAccessor
     *
     * @param request The WebDriverRequest to associate with the created
     * WebDriverAccessor
     * @param response The WebDriverResponse to associate with the created
     * WebDriverAccessor.
     * @return a WebDriverAccessor
     */
    protected WebDriverAccessor createWebDriverAccessor(
            final WebDriverRequest request,
            final WebDriverResponse response) {

        return new WebDriverAccessor() {
            public WebDriverRequest getRequest(
                    XMLPipelineContext pipelineContext) {
                return request;
            }

            public WebDriverResponse getResponse(
                    XMLPipelineContext pipelineContext, String id) {
                return response;
            }
        };
    }

    protected XMLSerializer getSerializer(OutputStream os)
            throws IOException {

        OutputFormat format = new OutputFormat();
        format.setPreserveSpace(true);
        format.setOmitXMLDeclaration(false); // was true
        XMLSerializer serializer = new XMLSerializer(format);

        serializer.setOutputByteStream(os);
        
        return serializer;
    }

    /**
     * URLRemapper that uses a ServiceDefintion to do the remapping
     */
    private class ServiceURLRemapper extends GenericURLRemapper {
        /**
         * The service
         */
        private ServiceDefinition service;

        /**
         * List of all services. Currently this only contains the current
         * service.
         * @todo Populate this with all PMSS services
         */
        private List services;

        /**
         * Creates a new ServiceURLRemapper instance
         * @param service the service
         */
        public ServiceURLRemapper(ServiceDefinition service) {
            this.service = service;
            services = new ArrayList();
            services.add(service.getName());
        }

        // javadoc inherited
        public void initialise(FilterConfig filterConfig) {
        }

        // javadoc inherited
        public Iterator pathPrefixIterator() {
            return services.iterator();
        }

        // javadoc inherited
        public URL getRemoteSiteRootURL(String pathPrefix) {
            try {
                return new URL(service.getRemoteTarget());
            } catch(MalformedURLException e) {
                throw new IllegalStateException(
                        "The service " + service.getName() + " has an "+
                        "invalid remote target URL " +
                        service.getRemoteTarget());
            }
        }
    }
}
