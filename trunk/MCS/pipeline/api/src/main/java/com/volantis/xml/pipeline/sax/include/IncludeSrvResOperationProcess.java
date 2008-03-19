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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 28-May-2003  Sumit       VBM:2003030612 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.include;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;

/**
 * The operation process that includes external resources. It must be running
 * within a Servlet or JSP environment.
 *
 * <p><strong>Before creating this process the ServletRequest and the
 * ServletResponse must be stored as properties in the XMLPipelineContext used
 * by the pipeline this process is part of.</strong> The attribute key/value
 * pairs must be:
 *
 * <pre>
 * <table border="1">
 *     <tr>
 *         <th>Key</th>
 *         <th>Value</th>
 *     </tr>
 *     <tr>
 *         <td><tt>ServletRequest.class</tt></td>
 *         <td>the current ServletRequest</td>
 *     </tr>
 *     <tr>
 *         <td><tt>ServletResponse.class</tt></td>
 *         <td>the current ServletResponse</td>
 *      </tr>
 * </table>
 * </pre>
 *
 * <p>XML markup handled by this process might look like this:</p>
 *
 * <pre>
 *     &lt;includeServerResource path="header.jsp"&gt;
 *         &lt;param name="a" value="1"/&gt;
 *         &lt;param name="b" value="2"/&gt;
 *     &lt;/includeServerResource&gt;
 * </pre>
 */
public class IncludeSrvResOperationProcess extends AbstractOperationProcess {

    /**
     * The Volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * A map of the parameter values
     */
    private Map parameters;

    /**
     * Path to the server resource to be included
     */
    private String path;

    public IncludeSrvResOperationProcess() {
        parameters = new HashMap();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path.toString();
    }

    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    public String getParameter(String name) {
        return (String)parameters.get(name);
    }

    /**
     * Performs the actual inclusion
     * @throws SAXException if an error occurs
     */
    public void doIncludeServerResource() throws SAXException {
        // retrieve the pipeline context
        XMLPipelineContext context = getPipelineContext();
        if (!context.inErrorRecoveryMode()) {
            // Get the Servleet request and response from the header
            ServletRequest request = (ServletRequest)context.getProperty(
                    ServletRequest.class);

            ServletResponse response = (ServletResponse)context.getProperty(
                    ServletResponse.class);

            // this process cannot execute with either of these two objects so
            // throw an exception if either one or both is missing
            if (request == null || response == null) {
                Locator locator = context.getCurrentLocator();
                XMLProcessingException xmlException =
                        new XMLProcessingException(
                                "Null request and response objects in context",
                                locator);
                fatalError(xmlException);
            }

            // Get valid a RequestDispatcher to dispatch to
            RequestDispatcher dispatcher = getRequestDispatcher(request);
            try {
                dispatcher.include(request, response);
            } catch (Exception e) {
                Locator locator = getPipelineContext().getCurrentLocator();
                XMLProcessingException xmlException =
                        new XMLProcessingException("Failed to include URI ",
                                                   locator, e);
                fatalError(xmlException);
            }
        }
    }

    /**
     * Searches the request for an attribute javax.servlet.include.request_uri.
     * If it exists it returns a RequestDispatcher from that URI otherwise it
     * returns a URI from path.
     * @param request
     * @return
     */
    private RequestDispatcher getRequestDispatcher(ServletRequest request) {
        String parentURI =
                (String)request.getAttribute("javax.servlet.include.request_uri");
        return request.getRequestDispatcher(getFullPath(parentURI));
    }

    /**
     * @param parentURI URI to append this objects path+params. Can be null
     * @return A String representing the full URI including parameters
     */
    private String getFullPath(String parentURI) {

        StringBuffer fullPath = new StringBuffer();

        if (parentURI != null) {
            fullPath.append(parentURI);
            if (!parentURI.endsWith("/")) {
                fullPath.append("/");
            }
        }
        loadParamsFromURI(path, parameters, fullPath);
        appendParameters(fullPath, parameters);
        return fullPath.toString();
    }

    /**
     * Parses a URI string and stores it's parameter values into the
     * parameter map unless the parameter already exists in the map.
     * @param uri The url to parse - maybe incomplete
     * @param parameters The map containing the parameters
     * @return Any portion of the url before the query section
     */
    private String loadParamsFromURI(String uri, Map parameters,
                                     StringBuffer path) {
        String query = null;

        if (uri.indexOf("?") > -1) {
            // URI contains a query part so parse this out
            // and append the non query part to the StringBuffer
            query = uri.substring(uri.indexOf("?") + 1, uri.length());
            path.append(uri.substring(0, uri.indexOf("?")));
        } else {
            // URI does not contain a query part
            path.append(uri);
        }
        if (query != null) {
            StringTokenizer st = new StringTokenizer(query, "&");
            while (st.hasMoreTokens()) {
                storeNVPair(st.nextToken(), parameters);
            }
        }
        return path.toString();
    }

    /**
     * Appends a list of parameters and values to a path. This function expects
     * there to be a '?' at the end of the incoming path
     * @param fullPath Path variable to append to
     * @param parameters Map conating the query parameters
     */
    private void appendParameters(StringBuffer fullPath, Map parameters) {
        Iterator itr = parameters.keySet().iterator();
        if (itr.hasNext()) {
            fullPath.append("?");
        }

        while (itr.hasNext()) {
            String name = (String)itr.next();
            String value = getParameter(name);
            fullPath.append(name);
            fullPath.append("=");
            fullPath.append(value);
            if (itr.hasNext()) {
                fullPath.append("&");
            }
        }
    }

    /**
     * Parses a name, value pair of the format name=value and stores it in
     * the map provided unless the map already contains a key of the same name
     * @param equalsSep name=value formatted string
     * @param store Map to store the pared value in
     */
    private void storeNVPair(String equalsSep, Map store) {
        if (equalsSep.indexOf('=') != -1) {
            String name = equalsSep.substring(0, equalsSep.indexOf('='));
            String value = equalsSep.substring(equalsSep.indexOf('=') + 1);
            if (!store.containsKey(name)) {
                store.put(name, value);
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 19-Dec-03	489/2	doug	VBM:2003120807 Ensured that our current xml processes are recoverable when inside a try op

 15-Aug-03	372/1	philws	VBM:2003071704 Fix includeJSP to work from XML file

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 31-Jul-03	217/3	allan	VBM:2003071702 Fixed javadoc issue with contains and containsIdentity.

 31-Jul-03	217/1	allan	VBM:2003071702 Made HTTPMessageEntities into a set.

 10-Jul-03	179/4	sumit	VBM:2003070407 Fixed IncludeSrvRes process to handle params

 10-Jul-03	179/2	sumit	VBM:2003070407 Fixed IncludeSrvRes process to handle params

 ===========================================================================
*/
