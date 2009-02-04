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
package com.volantis.mcs.context;

import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.shared.environment.Environment;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;

/**
 * Test version of Environment context.
 */
public class RuntimeTestEnvironmentContext extends EnvironmentContext {

    int cacheControlMaxAge = 0;

    String contentType;

    File streamFile = null;

    OutputStream outputStream;

    InputStream inputStream;

    Writer writer;

    MarinerURL contextPathURL = null;

    String responseFilename = null;

    String realPath;

    /**
     * This constructor will use a common file for temporary storage of
     * response data when using the response OutputStream (rather than the
     * response Writer). The file will be deleted on exit of the VM.
     */
    public RuntimeTestEnvironmentContext() {
        this.responseFilename =
            getClass().getName() + ".response";
        this.sessionContext = new MarinerSessionContext();
    }

    /**
     * This constructor allows a specific file to be used for temporary storage
     * of response data when using the response OutputStream (rather than the
     * response Writer). The file will be deleted on exit of the VM.
     *
     * @param responseFilename the filename, without path, for the temporary
     *                         file used to store response data when using the
     *                         response OutputStream
     */
    public RuntimeTestEnvironmentContext(String responseFilename) {
        this.responseFilename = responseFilename;
        this.sessionContext = new MarinerSessionContext();
    }

    public void initialise(MarinerPageContext context) {
        this.marinerPageContext = context;
    }

    public void initialiseResponse() {
    }

    public void initialiseSession() throws RepositoryException {
    }

    public void setContentType(String mimeType) {
        this.contentType = mimeType;
    }

    public void setCacheControlMaxAge(int maxAge) {
        this.cacheControlMaxAge = maxAge;
    }

    /**
     * Returns a StringWriter from which the response data can be obtained.
     *
     * @return the response writer (actually a StringWriter)
     * @throws com.volantis.mcs.context.MarinerContextException
     *
     */
    public Writer getResponseWriter()
        throws MarinerContextException {
        if (writer == null) {
            writer = new StringWriter();

            try {
                if (contentType != null) {
                    writer.write("Content-Type: " + contentType + "\n");
                }

                writer.write("Cache-Control: max-age=" +
                    cacheControlMaxAge + "\n");
            } catch (IOException e) {
            }
        }

        return writer;
    }

    public void setResponseWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * The response OutputStream is created as a file output stream using the
     * default temporary filename or one specified on construction of the
     * environment context.
     *
     * @return the response output stream (actually a FileOutputStream)
     * @throws com.volantis.mcs.context.MarinerContextException
     *
     */
    public OutputStream getResponseOutputStream()
        throws MarinerContextException {
        if (outputStream == null) {
            streamFile = new File(getTempDir() + File.separator +
                responseFilename);
            streamFile.deleteOnExit();

            try {
                outputStream =
                    new FileOutputStream(streamFile);
                Writer writer = new OutputStreamWriter(outputStream);

                if (contentType != null) {
                    writer.write("Content-Type: " + contentType + "\n");
                }
                writer.write("Cache-Control: max-age=" +
                    cacheControlMaxAge + "\n");
                writer.flush();
            } catch (IOException e) {
            }
        }

        return outputStream;
    }

    /**
     * Returns an InputStream from which the response data can be retrieved if
     * the response OutputStream was used to generate the response data.
     *
     * @return an InputStream from which the response data can be retrieved
     */
    public InputStream getResponseInputStream() {
        if (inputStream == null) {
            try {
                outputStream.close();

                inputStream = new FileInputStream(streamFile);
            } catch (IOException e) {
            }
        }
        return inputStream;
    }

    public void sendRedirect(MarinerURL url)
        throws IOException {
    }

    public URLRewriter getSessionURLRewriter() {
        return null;
    }

    public MarinerSessionContext getSessionContext()
        throws RepositoryException {
        return sessionContext;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getRealPath(String separator) {
        return realPath;
    }

    public void setContextPathURL(MarinerURL contextPathURL) {
        this.contextPathURL = contextPathURL;
    }

    public void setContextPathURL(String contextPathURL) {
        this.contextPathURL = new MarinerURL(contextPathURL);
    }

    public MarinerURL getContextPathURL() {
        return contextPathURL;
    }

    public String getExtraPathInfo() {
        return null;
    }

    public String getTempDir() {
        return System.getProperty("java.io.tmpdir");
    }

    public String getResourceAsString(String resourceName)
        throws MarinerContextException {
        return null;
    }

    public MarinerSessionContext createSessionContext()
        throws RepositoryException {
        return new MarinerSessionContext();
    }

    //javadoc inherited
    public void initalisePipelineContextEnvironment(
        XMLPipelineContext pipelineContext) {

    }

    public EnvironmentInteraction createRootEnvironmentInteraction() {

        EnvironmentInteraction rootInteraction = null;
//        ServletEnvironmentFactory servletEnvironmentFactory = null;
//        ServletEnvironment servletEnvironment = null;

//        servletEnvironmentFactory =
//            ServletEnvironmentFactory.getDefaultInstance();


//        servletEnvironment = servletEnvironmentFactory.createEnvironment(null);
                                                                                                                                                                                                    
        // create the root interaction
//        rootInteraction =
//            servletEnvironmentFactory.createEnvironmentInteraction(
//                servletEnvironment,
//                null,
//                null,
//                null,
//                null);

        rootInteraction = new EnvironmentInteraction() {
            // javadoc inherited
            public EnvironmentInteraction getParentInteraction() {
                throw new UnsupportedOperationException();
            }

            // javadoc inherited
            public void setParentInteraction(EnvironmentInteraction parent) {
                throw new UnsupportedOperationException();
            }

            // javadoc inherited
            public Environment getEnvironment() {
                throw new UnsupportedOperationException();
            }
        };

        return rootInteraction;
    }


    // javadoc inherited
    public String getHeader(String header) {
        return null;
    }

    // javadoc inherited
    public Enumeration getHeaders(String header) {
        return null;
    }

    // javadoc inherited
    public ResponseCachingDirectives getCachingDirectives() {
        return null;
    }
}
