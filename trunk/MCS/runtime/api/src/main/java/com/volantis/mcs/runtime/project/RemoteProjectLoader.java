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
package com.volantis.mcs.runtime.project;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.configuration.project.RuntimeProjectConfiguration;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.shared.net.http.client.HttpClient;
import com.volantis.shared.net.http.client.HttpClientBuilder;
import com.volantis.shared.net.http.client.HttpClientFactory;
import com.volantis.synergetics.log.LogDispatcher;
import our.apache.commons.httpclient.Header;
import our.apache.commons.httpclient.HostConfiguration;
import our.apache.commons.httpclient.HttpMethod;
import our.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.net.URL;

/**
 * Loads and unloads project configuration files from remote locations and adds
 * them to and removes them from the request context project stack
 */
public class RemoteProjectLoader
        extends URLProjectLoader {


    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RemoteProjectLoader.class);

    /**
     * The mcs project configuration file request header. This should be used
     * when requesting the mcs configuration file from a resource server, and
     * it's value in the response header will be the actual URL of the remote
     * resource server.
     * NB: If changing this constant, you must also change
     * com.volantis.resource.ResourceServer#MCS_PROJECT_HEADER. It
     * is defined twice because the resource server must be independent of MCS.
     */
    public static final String MCS_PROJECT_HEADER = "x-mcs-project-config";

    public RemoteProjectLoader() {
    }

    // Javadoc inherited.
    protected RuntimeProjectConfiguration loadConfiguration(Path projectFile) {

        RuntimeProjectConfiguration configuration = null;

        // Connect to the resource server and get the response.
        try {
            String urlAsString = projectFile.toExternalForm();
            HttpMethod method = connectAndExecute(urlAsString);
            if (method != null) {
                try {
                    // If the get failed then return immediately.
                    if (method.getStatusCode() == 200) {

                        // Check to see whether the special MCS_PROJECT_HEADER
                        // was returned. If it was then it is the location of
                        // the project file rather than the URL that was
                        // passed in.
                        Header projectHeader =
                                method.getResponseHeader(MCS_PROJECT_HEADER);
                        if (projectHeader != null) {
                            urlAsString = projectHeader.getValue();
                        }

                        // Unmarshall the response into a project
                        // configuration.
                        configuration = createProjectConfiguration(
                                method.getResponseBodyAsStream(), urlAsString);
                    }
                } finally {
                    // Make sure to release the connection, this will also
                    // close the stream.
                    method.releaseConnection();
                }
            }
        } catch (IOException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }

        return configuration;
    }

    /**
     * Connect to the url given and return an {@link HttpMethod} which contains
     * the response.
     *
     * @param urlAsString
     * @return HttpMethod containing the response information
     * @throws IOException if there was a problem connecting
     */
    protected HttpMethod connectAndExecute(final String urlAsString)
            throws IOException {

        URL url = new URL(urlAsString);
        GetMethod method = null;

        if (!"".equals(url.getHost())) {
            HostConfiguration hostConfig = new HostConfiguration();

            hostConfig.setHost(url.getHost(), url.getPort());

            method = new GetMethod();
            method.setHostConfiguration(hostConfig);
            method.setPath(url.getPath());
            method.setRequestHeader(MCS_PROJECT_HEADER, url.toExternalForm());
            if (url.getPort() == -1) {
                method.addRequestHeader("Host", url.getHost());
            } else {
                method.addRequestHeader("Host",
                        url.getHost() + ":" + url.getPort());
            }

            HttpClientFactory factory = HttpClientFactory.getDefaultInstance();
            HttpClientBuilder builder = factory.createClientBuilder();
            HttpClient httpClient = builder.buildHttpClient();
            httpClient.executeMethod(method);
        }
        return method;
    }

    /**
     * This utility method enables us to convert a relative baseURL into a
     * fully qualified URL using the projects baseURL.
     *
     * @param projectBaseURL The project Base URL.
     * @param url            The URL that needs to be made absolute
     * @return A fully qualified Asset Base URL.
     */
    protected String resolveAbsoluteURL(String projectBaseURL, String url) {
        // Lets start off by assuming that they have specified a fully
        // qualified URL.
        MarinerURL result = new MarinerURL(url);
        //Then again they may not have
        if (!result.isAbsolute()) {
            // So lets resolve it properly if we have a base URL;
            if (projectBaseURL != null) {
                result = new MarinerURL(projectBaseURL, url);
            }
        }
        return result.getExternalForm();
    }
}
