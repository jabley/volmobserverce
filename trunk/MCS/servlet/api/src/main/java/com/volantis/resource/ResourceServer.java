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
package com.volantis.resource;

import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.project.ProjectConfigurationWriter;
import com.volantis.mcs.runtime.ExternalPathToInternalURLMapper;
import com.volantis.mcs.runtime.configuration.project.AbstractPoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.runtime.configuration.project.XmlPoliciesConfiguration;
import com.volantis.mcs.runtime.project.FileProjectLoader;
import com.volantis.mcs.runtime.project.ProjectLoader;
import com.volantis.mcs.runtime.project.ProjectLoadingOptimizer;
import com.volantis.mcs.runtime.project.SelectingProjectLoader;
import com.volantis.mcs.runtime.project.URLProjectLoader;
import com.volantis.mcs.servlet.ServletExternalPathToInternalURLMapper;
import com.volantis.mcs.servlet.ServletResourceMapper;
import com.volantis.mcs.servlet.ServletResourceMapperImpl;
import com.volantis.mcs.servlet.http.HttpRequestStandardizer;
import org.jibx.runtime.JiBXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Servlet for requests for remote resources. Resources requested are returned
 * with any relative URLs returned as absolute references. References are
 * converted using the location of the mcs-project.xml file
 *
 * <p>Every request that comes in must be for a resource that belongs to a
 * specific project. Therefore the first thing that is done is to search for
 * and load the project. If the project could not be found then the request
 * fails.</p>
 *
 * <p>Then if the request is for the project definition then the client
 * configuration is sent back.</p>
 *
 * <p>Otherwise if the request is for a policy then it is loaded from the
 * project.</p>
 *
 * <p>Finally, if the request is for something else then it is simply
 * returned.</p>
 *
 * <p>Project loading is done using the same {@link ProjectLoader}s used by
 * MCS, (apart from the RemoteProjectLoader).
 *
 * <p>The resource server only ever serves policies from projects so does not
 * need to be optimized for the situation where projects do not exist.</p>
 *
 */
public class ResourceServer
        extends HttpServlet {

    private static final String XDIME_MARKUP_EXTENSION = ".xdime";

    /**
     * Name of the project configuration file
     */
    public static final String PROJECT_FILE_NAME = "mcs-project.xml";

    /**
     * List of {@link ProjectDefinition}s that have already been loaded.
     */
    private final List projects = new ArrayList();

    /**
     * The mcs project configuration file request header.
     */
    public static final String MCS_PROJECT_HEADER = "x-mcs-project-config";

    /**
     * The loader to use to load the project.
     */
    private ProjectLoader projectLoader;

    /**
     * The mapper to use to map requests to local URLs.
     */
    private ServletResourceMapper resourceMapper;

    /**
     * The mapper to use to map between external paths and internal URLs.
     */
    private ExternalPathToInternalURLMapper externalInternalMapper;

    // Javadoc inherited.
    public void init(ServletConfig servletConfig)
            throws ServletException {

        ServletContext servletContext = servletConfig.getServletContext();
        init(servletConfig,
                new ServletExternalPathToInternalURLMapper(servletContext),
                new ServletResourceMapperImpl(servletContext));
    }

    /**
     * Internal method provided for test cases.
     *
     * @param servletConfig
     * @param urlMapper
     * @param servletResourceMapper
     * @throws ServletException
     */
    protected void init(
            ServletConfig servletConfig,
            ExternalPathToInternalURLMapper urlMapper,
            ServletResourceMapper servletResourceMapper)
            throws ServletException {

        super.init(servletConfig);

        ProjectLoader urlProjectLoader = new URLProjectLoader();
        ProjectLoader fileProjectLoader = new FileProjectLoader();
        ProjectLoader remoteProjectLoader = new ProjectLoader() {
            public RuntimeProjectConfiguration loadProjectConfiguration(
                    String url, ProjectLoadingOptimizer optimizer) {
                throw new UnsupportedOperationException("Remote projects " +
                        "not supported");
            }
        };

        projectLoader = new SelectingProjectLoader(urlProjectLoader,
                fileProjectLoader, remoteProjectLoader);

        resourceMapper = servletResourceMapper;
        externalInternalMapper = urlMapper;
    }

    // Javadoc inherited.
    protected void doGet(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse)
            throws ServletException, IOException {

        processRequest(httpServletRequest, httpServletResponse);
    }

    /**
     * Process the request for a remote resource.
     *
     * @param request  HttpServletRequest from which to retrieve the resource
     *                 location
     * @param response HttpServletResponse to which the output should be
     *                 written
     * @throws IOException if there was a problem processing the request
     */
    private void processRequest(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        // Get the project configurations for the specified request.
        HttpRequestStandardizer requestStandardizer =
                new HttpRequestStandardizer(request);

        ProjectDefinition definition =
                getProjectDefinition(requestStandardizer);

        if (definition == null) {
            // A resource server is incorrectly configured without a project
            // configuration file, so send an error if none can be found.
            response.sendError(HttpServletResponse.SC_NOT_FOUND);

        } else {
            // Get the full url to the project's root directory that the client
            // can use to identify the project.
            final String projectURL = definition.getExternalProjectRoot();

            // Always send the header back indicating where to find the project
            // for the resource so that MCS knows that it belongs to a project.
            // Without this MCS will not load the project associated with this
            // policy and therefore may not work as expected.
            response.setHeader(MCS_PROJECT_HEADER, projectURL);

            if (request.getHeader(MCS_PROJECT_HEADER) != null) {

                // The configuration is always sent back as UTF-8.
                response.setContentType("text/xml; charset=utf-8");

                // Write the modified client as a string.
                Writer writer = response.getWriter();
                String clientProject = definition.getClientConfigurationAsXML();
                writer.write(clientProject);
                writer.close();

            } else {
                // The resource belongs to the project so we need to create a
                // path to the resource that is relative to the project.
                String contextRelativePath =
                        requestStandardizer.getContextRelativePathInfo();

                // This is a normal request for a resource in a project.
                InputStream resourceAsStream = getResourceAsStream(
                        definition, contextRelativePath);
                if (resourceAsStream == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                } else {
                    response.setContentType(
                            determineContentType(contextRelativePath));
                    writeResource(resourceAsStream, response.getOutputStream());
                }
            }
        }
    }

    /**
     * Determine the content type of the resource that has been requested. Will
     * default to "text/xml" if the type cannot be identified.
     *
     * @param resourceName name of the resource that has been requested
     * @return String content type of the resource that will be returned
     */
    protected String determineContentType(String resourceName) {

        String contentType = "text/xml";

        if (isPolicyResource(resourceName)) {
            contentType = "text/xml";
        } else if (resourceName.endsWith(XDIME_MARKUP_EXTENSION)) {
            contentType = "x-application/vnd.xdime+xml";
        } else {
            // Use the standard mime types defined in the deployment
            // descriptor.
            contentType = getServletContext().getMimeType(resourceName);
        }
        return contentType;
    }

    /**
     * Write the contents of the resource input stream to the response's output
     * stream.
     *
     * @param resourceAsStream stream which should be written out
     * @param outputStream     to which to write the contents of resourceAsStream
     * @throws IOException if there was a problem writing out the request
     */
    private void writeResource(
            InputStream resourceAsStream,
            OutputStream outputStream) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(resourceAsStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        // todo Do this in bigger chunks.
        int i = bis.read();
        while (i != -1) {
            bos.write(i);
            i = bis.read();
        }
        bos.flush();
        bos.close();
        bis.close();
    }

    /**
     * Retrieve the {@link InputStream} which corresponds to the specified
     * resource.
     *
     * <p>The path may either be to a policy, or some other file, usually a
     * .xdime file. If it refers to a policy then the context relative path is
     * converted into a project relative path that is then passed to a
     * ResourceAccessor that finds the policy. Otherwise the stream
     * associated with the context relative path is returned.</p>
     *
     * @param definition          project definition.
     * @param contextRelativePath path of the resource requested
     * @return InputStream containing the specified, resolved policy's contents
     *         or null if the policy could not be found.
     */
    private InputStream getResourceAsStream(
            ProjectDefinition definition,
            String contextRelativePath) {


        InputStream resourceAsStream = null;

        // If the specified resource is a policy resource, then remap the
        // URL using the root location for policies.
        if (isPolicyResource(contextRelativePath)) {

            String projectRelativePath =
                    definition.getProjectRelativePath(contextRelativePath);

            // The requested resource path is relative to the context
            // and what we need to do is find the relative

            ResourceAccessor accessor =
                    definition.getPolicyResourceAccessor();
            if (accessor != null) {
                resourceAsStream = accessor.getResourceAsStream(
                        projectRelativePath);
            }
        } else {
            ServletContext servletContext = getServletContext();
            resourceAsStream = servletContext.getResourceAsStream(
                    contextRelativePath);
        }

        return resourceAsStream;
    }

    /**
     * Get project definition for the specified request.
     *
     * todo this contains similar code to ProjectManager.
     *
     * <p>The list of existing projects is searched first to see whether any of
     * them contain the context relative path in the request. If one does then
     * it is returned. Otherwise the context relative path is converted into
     * a local URL and passed to the {@link ProjectLoader} to find. If that
     * loads a configuration then it is used to construct a definition,
     * otherwise the project cannot be found so the request will fail.</p>
     *
     * @param requestStandardizer The request, or rather an object that
     *                            standardizes information returned by the
     *                            request.
     * @return The project configuration.
     * @throws ServletException If there was a problem accessing the
     *                          configuration.
     */
    private ProjectDefinition getProjectDefinition(
            final HttpRequestStandardizer requestStandardizer)
            throws ServletException, IOException {


        // Get the path to the resource being requested.
        String resourcePath = requestStandardizer.getContextRelativePathInfo();

        // Check the cache for a project configuration file.
        ProjectDefinition definition = null;
        synchronized (projects) {

            boolean found = false;
            for (Iterator i = projects.iterator(); !found && i.hasNext();) {
                definition = (ProjectDefinition) i.next();
                if (definition.containsPolicy(resourcePath)) {
                    found = true;
                }
            }

            if (!found) {

                // Get a URL for the resource.
                String localURLAsString =
                        resourceMapper.getLocalURL(resourcePath);

                // It's not in the cache, so load and cache it.
                RuntimeProjectConfiguration configuration = null;
                try {
                    configuration = projectLoader.loadProjectConfiguration(
                            localURLAsString, null);
                } catch (JiBXException e) {
                    throw new ServletException(e);
                }

                if (configuration == null) {
                    definition = null;
                } else {
                    definition = createProjectDefinition(
                            requestStandardizer, configuration);
                    projects.add(definition);
                }
            }
        }

        return definition;
    }

    /**
     * Create a project definition from the configuration.
     *
     * <p>This creates an accessor for accessing the policies, finds the
     * external root as well as the context relative root, creates a client
     * side version of the configuration and then creates the definition.</p>
     *
     * @param request The request.
     * @param local   The local configuration used on the resource server.
     * @return The packaged up local and client configuration.
     * @throws ServletException If there was a problem accessing the
     *                          configuration.
     */
    private ProjectDefinition createProjectDefinition(
            HttpRequestStandardizer request, RuntimeProjectConfiguration local)
            throws ServletException, IOException {

        // Get the internal URL to the project file.
        String internalURLAsString = local.getLocation();

        // Get the internal project root.
        String internalProjectRoot = getProjectRoot(internalURLAsString);

        URL internalProjectRootURL = new URL(internalProjectRoot);

        // Get a context relative path to the project root.
        String contextRelativeProjectRoot =
                externalInternalMapper.mapInternalURLToExternalPath(
                        internalProjectRoot);

        // Get the external URL to the project.
        URL hostURL = request.getHostURL();
        String contextPath = request.getContextPath();

        // Make the external path host relative.
        String hostRelativeProjectRoot = contextPath + contextRelativeProjectRoot;

        // Create an external URL by resolving the external path against the
        // request URI.
        URL externalURL = new URL(hostURL, hostRelativeProjectRoot);
        String externalProjectRoot = externalURL.toExternalForm();

        // Sort out the policies accessor.
        AbstractPoliciesConfiguration policies = local.getPolicies();
        ResourceAccessor policiesAccessor = null;

        // We only support xml policy sources for remote projects at
        // the moment - anything else is treated as if no policy source
        // was specified.
        if (policies instanceof XmlPoliciesConfiguration) {
            XmlPoliciesConfiguration xmlPolicies =
                    (XmlPoliciesConfiguration) policies;
            String directory = xmlPolicies.getDirectory();
            if (!directory.endsWith("/")) {
                directory = directory + "/";
            }

            // Resolve the directory against the location of the project file.
            URL policiesRoot = new URL(internalProjectRootURL, directory);
            policiesAccessor = new XMLPoliciesResourceAccessor(policiesRoot);

        } else if (policies == null) {
            // Default to looking in the project root for the policies.
            policiesAccessor = new XMLPoliciesResourceAccessor(
                    internalProjectRootURL);
        }

        String clientAsString = createClientConfiguration(local);

        return new ProjectDefinition(local, clientAsString,
                externalProjectRoot, policiesAccessor,
                contextRelativeProjectRoot);
    }

    /**
     * Get the project root from the path.
     *
     * <p>This simply finds the last / and removes everything after it but not
     * the / itself.</p>
     *
     * @param projectFilePath The path to the project file.
     * @return The project root.
     */
    private String getProjectRoot(String projectFilePath) {
        String projectRoot;
        int index = projectFilePath.lastIndexOf("/");
        if (index == -1) {
            throw new IllegalStateException("Project file path not valid, " +
                    "expected to have at least one /, '" +
                    projectFilePath + "'");
        } else {
            projectRoot = projectFilePath.substring(0, index + 1);
        }
        return projectRoot;
    }

    /**
     * Create a client side version of the configuration from the local one,
     * marshall it as a string to send back to the client and wrap it and the
     * local configuration up in an new ProjectConfigurations object and
     * return it.
     *
     * @param local The local configuration used on the resource server.
     * @return The packaged up local and client configuration.
     * @throws ServletException If there was a problem accessing the
     *                          configuration.
     */
    private String createClientConfiguration(
            RuntimeProjectConfiguration local)
            throws ServletException {

        // Create the configuration for the client.
        RuntimeProjectConfiguration client = new RuntimeProjectConfiguration();

        // Copy only those properties that are supported for remote projects.
        client.setAssets(local.getAssets());
        client.setDefaultProjectLayoutLocator(
                local.getDefaultProjectLayoutLocator());
        client.setFallbackProjectName(local.getFallbackProjectName());
        client.setName(local.getName());
        client.setProjectThemes(local.getProjectThemes());

        // The client never knows where the policies are stored, they are
        // always accessed through this class.
        client.setPolicies(null);

        StringWriter stringWriter = new StringWriter();
        ProjectConfigurationWriter writer = new ProjectConfigurationWriter();
        try {
            writer.writeProject(client, stringWriter);
        } catch (JiBXException e) {
            throw new ServletException(e);
        }
        String clientAsString = stringWriter.toString();
        return clientAsString;
    }

    /**
     * Determine if the resource specified by the given name corresponds to a
     * policy resource.
     *
     * @param resourceName String name which may refer to a policy resource
     * @return true if the resource specified by the given name corresponds to
     *         policy resource and false otherwise.
     */
    private boolean isPolicyResource(String resourceName) {

        boolean result = false;
        String extension = resourceName.substring(
                resourceName.lastIndexOf('.') + 1);
        FileExtension fileExtension =
                FileExtension.getFileExtensionForExtension(extension);
        if (fileExtension != null && fileExtension.isPolicyFileExtension()) {
            result = true;
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	9990/3	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 08-Nov-05	9990/1	ibush	VBM:2005102516 Enable Local and Remote Project Loading

 ===========================================================================
*/
