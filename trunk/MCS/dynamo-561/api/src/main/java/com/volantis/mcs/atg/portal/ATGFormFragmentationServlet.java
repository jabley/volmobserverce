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
 * $Header: /src/voyager/com/volantis/mcs/atg/portal/Attic/ATGFormFragmentationServlet.java,v 1.1.2.5 2002/09/24 10:22:25 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Jul-02    Chris W         VBM:2002080209 Created.
 * 06-Aug-02    Chris W         VBM:2002080209 HTTP 1.1 spec requires an absolute
 *                              url when redirecting to page i.e. a url that
 *                              begins http:// Previously the redirect only
 *                              included the path and query string. This broke
 *                              some browsers e.g. yospace WAP phone emulator
 * 13-Sep-02    Chris W         VBM:2002080209 Mostly rewritten so it can pass
 *                              form field data to the Remap servlet. This is
 *                              needed when we use the combined Volantis and ATG
 *                              form tags with form fragments within ATG Portal.
 * 16-Sep-02    Chris W         VBM:2002081511 - refactored to share common
 *                              methods with ATGFormFragmentationServlet by
 *                              creating PostDataServletUtils. This class also
 *                              now POSTs the form data to the Remap servlet
 *                              rather than redirecting to it.
 * 24-Sep-02    Chris W         VBM:2002081511 - Excess debugging removed. 
 *                              dispatchPage passes request parameter vformname
 *                              to Remap servlet.
 * -----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.portal;

import com.volantis.mcs.atg.dynamo.PostDataServletUtils;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.protocols.forms.SessionFormData;
import com.volantis.mcs.servlet.FormFragmentationServlet;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.utilities.HttpPostClient;
import com.volantis.mcs.utilities.HttpResponseHeader;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Servlet to intercept form fragmentation buttons, store the parameters in the
 * session context and then route the calling jsp back to the correct fragment or
 * action jsp. This ATG Portal servlet specific servlet routes to are
 * completely different urls from those that the normal servlet routes to.
 */
public class ATGFormFragmentationServlet extends FormFragmentationServlet
{
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(ATGFormFragmentationServlet.class);

    
    /**
     * Utility class. Its methods are thread safe.
     */ 
    private PostDataServletUtils servletUtils = new PostDataServletUtils();
    
    /** Creates a new instance of ATGFormFragmentationServlet */
    public ATGFormFragmentationServlet()
    {
        super();
    }
    
    /**
     * Dispatch to the target URL
     * Note that the behaviour is completely different from the
     * superclass.
     * a. On entry to this method, targetURL of a fragment is
     *    /portal/communityname/pagename?...     
     * b. The context path /portal/layoutTemplates refers to the templates web
     *    application rather than the the communityname/pagename
     * c. We redirect to /portal/communityname/pagename i.e. we send a HTTP 302
     *    to the browser which then requests the correct page.
     *     
     * NB ATG Portal Framework uses the parameters to determine which page should
     *    be displayed. As gears only have two modes shared and full page, a
     *    further parameter must be passed in order for the shared or full page
     *    jsps to include the appropriate content. yuck!
     *
     * As we redirect to ATG Portal Framework which forwards to the relevant
     * JSP the request parameters storing the values of form fields are lost.
     * This is remedied by adding vform to the query string. Then, as if by
     * magic, MarinerPageContext adds the form parameters to the pure request
     * URI.
     * 
     * When the targetURL is the MarinerATGRemap servlet we must extract the form
     * fields from the session and add them to the request parameters. (The
     * MarinerATGRemap servlet expects the form fields to be in the request
     * parameters rather than in the session.
     *
     * @param targetURL The URL we should dispatch to
     * @param request The HttpServletRequest object
     * @param response The HttpServletResponse object
     * @see com.volantis.mcs.atg.dynamo.Remap
     */
    protected void dispatchPage(String targetURL, HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        MarinerURL marinerTargetURL = new MarinerURL(targetURL);
        String formSpecifier = (String)request.getParameter("vform");
        String formName = (String)request.getParameter("vformname");
        SessionFormData formData =
                getFormDataManager(request).getSessionFormData(formSpecifier);

        // If the form's action is to go to the Remap servlet, add the form
        // fields to the request parameters.   
        if (marinerTargetURL.getExternalForm().endsWith("MarinerATGRemap")) {

            // Add vform parameter to query string
            marinerTargetURL.addParameterValue("vform", formSpecifier);
            marinerTargetURL.addParameterValue("vformname", formName);
        
            // Send the POST request to ATG.        
            HttpPostClient postClient =
                    sendPostData(request, marinerTargetURL, formData);
        
            // Get ATG's response
            BufferedReader responseReader =
                    (BufferedReader)postClient.getReader();
            HttpResponseHeader responseHeader =
                    new HttpResponseHeader(responseReader);
                
            // Dispatch the response to the user's browser
            servletUtils.dispatchUserResponse(request, response,
                    responseReader, responseHeader);
            
            // Remove the field values stored in the session context when the
            // button was pressed (if any data has been stored).
            clearFragmentedFormData(formData, request.getSession(true));
        } else {
            // Forward to the appropriate destination
            String redirectionURL = getRedirectionURL(request, marinerTargetURL);
            if (logger.isDebugEnabled()) {
                logger.debug( "Dispatching to " + redirectionURL );
            }
            response.sendRedirect(redirectionURL);
        }               
    }
    
    /**
     * Returns the absolute url that we redirect to by HTTP 302.
     * @param request the HttpServletRequest
     * @param marinerTargetURL the MarinerURL containing the destination
     * @return String the absolute URL
     */
    private String getRedirectionURL( HttpServletRequest request,
                                            MarinerURL marinerTargetURL)
    {
        // Convert relative url to absolute one
        StringBuffer sb = new StringBuffer("http://");
        sb.append(request.getHeader("HOST"));
        
        String externalURL = marinerTargetURL.getExternalForm();
        
        // Make sure the target URL starts with a /
        if( externalURL.startsWith( "/" ) == false )
        {
            //theTargetURL = "/" + theTargetURL;
            sb.append("/");
        }
        sb.append(externalURL);
        
        return sb.toString();
    }

    /**
     * Remove the field values that are stored (in the session context) on
     * button press. This fixes a bugette; if you submit a form that is
     * returned to you for some reason (e.g. by entering bad data) and then
     * click reset, the same form is submitted again.
     *
     * @param formData      the data of this fragmented form that has been
     */
    private void clearFragmentedFormData(SessionFormData formData,
                                         HttpSession session)
    {
        String formName = formData.getFormDescriptor().getName();
        String[] imageButtons =
                (String[]) session.getAttribute(formName + ":_ATGBUTTONS");
        if (imageButtons != null)
        {
            for (int i=0; i<imageButtons.length; i++)
            {
                String buttonName = (String)imageButtons[i];
                if (formData.getFieldValue(buttonName) != null)
                {
                    formData.removeFieldValue(buttonName);
                    String formSpecifier = formData.getFormSpecifier();
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removed " + formSpecifier + ", " +
                                buttonName + " from MarinerSessionContext");
                    }
                    formData.removeFieldValue(buttonName+".x");
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removed " + formSpecifier + ", " +
                                buttonName + ".x" + " from MarinerSessionContext");
                    }
                    formData.removeFieldValue(buttonName+".y");
                    if (logger.isDebugEnabled()) {
                        logger.debug("Removed " + formSpecifier + ", " +
                                buttonName + ".y" + " from MarinerSessionContext");
                    }
                }       
            }
        }
    }

    /**
     * POSTs the data submitted on the form to ATG Dynamo.
     * @param request The HttpServletRequest
     * @param targetURL
     * @param formData
     * @return HttpPostClient The HttpPostClient object that sent the request
     */ 
    private HttpPostClient sendPostData(HttpServletRequest request,
            MarinerURL targetURL, SessionFormData formData)
            throws UnknownHostException, IOException {
        HttpPostClient postClient = new HttpPostClient();        
        postClient.setHost(request.getServerName());        
        postClient.setPort(request.getServerPort());        
        postClient.setUri(targetURL.getPath()+"?"+targetURL.getQuery());
        postClient.setRequestHeaders(servletUtils.getHeaders(request));
        postClient.setPostData(getPostData(formData));
        postClient.connect();
        return postClient;
    }

    /**
     * Returns the POST data string
     *
     * @param formData contains form field values.
     * @return String post data
     */
    private String getPostData(SessionFormData formData) {

        StringBuffer sb = new StringBuffer();
        Enumeration fieldNames = formData.getFormFields();
        if (fieldNames != null) {
            // Flag to determine whether or not we need to put an ampersand
            // between the name value pairs.
            boolean firstTime = true;

            while (fieldNames.hasMoreElements()) {

                String fieldName = (String) fieldNames.nextElement();
                String[] fieldValues = formData.getFieldValues(fieldName);

                // If session attribute starts with i_ then it's one we created
                // for storing the form data.
                if (fieldName.startsWith("i_")) {
                    for (int i = 0; i < fieldValues.length; i++) {
                        if (!firstTime) {
                            sb.append("&");
                        } else {
                            firstTime = false;
                        }
                        sb.append(servletUtils.encodedValue(fieldName));
                        sb.append("=");
                        sb.append(servletUtils.encodedValue(fieldValues[i]));
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Given the HttpServletRequest, return the {@link FormDataManager} which
     * is responsible for keeping track of the form data that has been recieved
     * for fragmented forms.
     *
     * @param request
     * @return FormDataManager
     */
    private FormDataManager getFormDataManager(HttpServletRequest request)
    {
        MarinerRequestContext requestContext
                    = MarinerServletRequestContext.getCurrent( request );
        MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(requestContext);
        return pageContext.getFormDataManager();       
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6565/1	adrianj	VBM:2004122902 Created Dynamo 7 version of Volantis custom tags

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 27-Apr-04	3843/3	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
