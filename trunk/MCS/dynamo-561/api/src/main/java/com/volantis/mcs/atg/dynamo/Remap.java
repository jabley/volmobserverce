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
 * $Header: /src/voyager/com/volantis/mcs/atg/dynamo561/Attic/Remap.java,v 1.1.2.11 2003/04/16 15:01:17 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *              Steve           VBM 2001090306 - Initial Release
 * 31-Jan-02    Paul            VBM:2001122105 - Fixed compile error in
 *                              logging.
 * 15-Aug-02    Chris W         VBM:2002081511 - Copied existing class to
 *                              separate package for Dynamo5.6.1 We want to be
 *                              able to support the older Dynamo5.1.1 (?) version
 *                              as well. Added method to get the servlet context
 *                              for ATG Portal server 5.6.1 which is different
 *                              to other web apps.
 * 20-Aug-02    Chris W         VBM:2002081511 - Send the form parameters to
 *                              ATG Dynamo using POST rather than GET.
 * 27-Aug-02    Chris W         VBM:2002081511 - Now handles graphical submit
 *                              buttons correctly by reading the values from the
 *                              _IMAGEBUTTONS session attribute in
 *                              getDynamoAttributes() method.
 *                              Also, we stop the context path being added to any
 *                              redirect by manually creating all responses rather
 *                              than calling response.sendRedirect() etc
 * 13-Sep-02    Chris W         VBM:2002081511 - clearSession() added. This clears
 *                              the _IMAGEBUTTONS, _D and _V variables from the
 *                              session once they are no longer needed. This stops
 *                              the session from growing too big thus causing
 *                              performance issues.
 * 16-Sep-02    Chris W         VBM:2002081511 - refactored to share common
 *                              methods with ATGFormFragmentationServlet by
 *                              creating PostDataServletUtils.
 * 24-Sep-02    Chris W         VBM:2002081511 - Excess debugging removed.
 *                              Instead of reading the form's hidden fields
 *                              directly in the session, we use the
 *                              HiddenFieldsSessionMap class. This makes it easier
 *                              to deal with multiple forms on a page.
 * 09-Oct-02    Chris W         VBM:2002100904 - Some debugging reintroduced in
 *                              getParameterValues() and getDynamoAttributes() as
 *                              requested by VBM.
 * 09-Apr-03    Chris W         VBM:2003032701 - Stopped clearing down the
 *                              session, so browser back will work. Surrounded
 *                              debug statements with isDebugEnabled(). Tidied
 *                              imports.
 * 16-Apr-03    Chris W         VBM:2003040304 - added doExpiredSession() to
 *                              deal with situations when the user's session
 *                              is null.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.dynamo;

import com.volantis.mcs.utilities.HttpPostClient;
import com.volantis.mcs.utilities.HttpResponseHeader;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * ATG Dynamo uses hidden form fields in order to set the attributes of nucleus
 * components. Typically these hidden fields are name _D:/nucleus/Component/name
 * We cannot send these hidden fields to WAP devices etc. So the tag classes
 * e.g. com.volantis.mcs.atg.dynamo.VolantisDSPFormTag store these values in
 * the session and rewrite the form's action to call this servlet.
 *
 * This servlet expands and fills in the hidden fields, creates a HttpPostClient
 * to HTTP POST the form data to ATG, receives the response and sends in back to
 * the browser.
 */
public class Remap extends HttpServlet {

    /**
     * Used for logging
     */
    private static LogDispatcher logger =
            LocalizationFactory.createLogger(Remap.class);
    
    /**
     * The request parameter storing the url we redirect to if the session
     * has expired
     */
    private static final String EXPIRED_SESSION_URL = "expiredSessionUrl";

    /**
     * Utility class. Its methods are thread safe.
     */ 
    private PostDataServletUtils servletUtils = new PostDataServletUtils();
     
    public Remap()
    {
        super();
    }


    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest( HttpServletRequest request, HttpServletResponse response)
        throws ServletException, java.io.IOException
    {
        String formName = request.getParameter("vformname");
        
        if (logger.isDebugEnabled()) {
            logger.debug("formName: "+formName);
        }        
                
        // Read the parameter values and store them in a map
        Map parameterTable = getParameterValues(request);
                
        HttpSession session = request.getSession(false);
        
        if (logger.isDebugEnabled()) {
            logger.debug("HttpSession="+session);
        }
        
        // If the session is null assume it has expired
        if (session == null) {
            doExpiredSession(request, response);
            return;                
        }
            
        // Add the remapped variables to the parameter list and jump to where we should have been        
        String DARGS = getDARGSAttribute(request, session, formName);
        
        // Read all the attributes from the session
        Hashtable passTable = getDynamoAttributes(session, parameterTable,
                                                 formName);                
                                                 
        // Send the POST request to ATG.        
        HttpPostClient postClient = sendPostData(request, session, 
                                            passTable, DARGS, formName);
    
        // Get ATG's response
        BufferedReader responseReader = 
                                   (BufferedReader)postClient.getReader();        
        HttpResponseHeader responseHeader = 
                                    new HttpResponseHeader(responseReader);
            
        // Dispatch the response to the user's browser
        servletUtils.dispatchUserResponse(request, response, 
                                            responseReader, responseHeader);
    }

    /**
     * Private helper that retrieves the value of an ATG nucleus component, in
     * order to know where to redirect too when the session has expired.
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void doExpiredSession(
        HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException
    {
        String url = request.getParameter(EXPIRED_SESSION_URL);                           
        
        if (url == null) {
            url = request.getHeader("Referer");
            if (logger.isDebugEnabled()) {
                logger.debug(EXPIRED_SESSION_URL + " could not be found in " +
                             "request parameters.");
            }
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("User's session has expired. Redirecting to "+url);
        }
                
        response.sendRedirect(url);            
    }

    /**
     * Retrieves the _DARGS attribute from the form data
     */
    private String getDARGSAttribute(HttpServletRequest request,
                                        HttpSession session, String formName)
    {
        HiddenFieldsSessionMap sessionMap = (HiddenFieldsSessionMap)session.getAttribute("_HIDDENATGFIELDS");
        return sessionMap.getAttribute(formName, "_DARGS");
    }
    
    /**
     * POSTs the data submitted on the form to ATG Dynamo.
     * @param request The HttpServletRequest
     * @param session The user's HttpSession
     * @param passTable A HashTable containing the POST data as name value pairs
     * @param DARGS The value of the special Dynamo attribute DARGS
     * @return HttpPostClient The HttpPostClient object that sent the request
     */ 
    private HttpPostClient sendPostData(HttpServletRequest request, HttpSession session,
        Hashtable passTable, String DARGS, String formName)
        throws UnknownHostException, IOException
    {
        HttpPostClient postClient = new HttpPostClient();
        postClient.setHost(request.getServerName());
        postClient.setPort(request.getServerPort());
        
        HiddenFieldsSessionMap sessionMap = (HiddenFieldsSessionMap)session.getAttribute("_HIDDENATGFIELDS");
        postClient.setUri((String)sessionMap.getAttribute(formName, "_URL"));
        
        postClient.setRequestHeaders(servletUtils.getHeaders(request));
        postClient.setPostData(getPostData(passTable, DARGS));
        postClient.connect();
        return postClient;
    }
    
    /** Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet(HttpServletRequest request, 
                         HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost(HttpServletRequest request, 
                          HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Read the request parameter values and store them in a map
     * @param request The HttpServletRequest
     * @return Map
     */
    private Map getParameterValues(HttpServletRequest request)
    {        
        Map parameterTable = new Hashtable();
        Enumeration paramNames = request.getParameterNames();
        
        if(logger.isDebugEnabled()) {
            logger.debug("request parameter attributes");
        }
        
        while( paramNames.hasMoreElements() )
        {
            String paramName = (String)paramNames.nextElement();
            String[] tpValue = request.getParameterValues( paramName );
                        
            parameterTable.put( paramName, tpValue );

            if(logger.isDebugEnabled()) {
                for (int i=0; i<tpValue.length; i++) {
                    logger.debug(paramName+"="+tpValue[i]);
                }
            }    
        }
        
        return parameterTable;
    }
    
    /**
     * Using the form attributes stored in the session and the request
     * parameters extracted in getParameterValues, reconstruct the parameters
     * Dynamo needs when a form is submitted and store them in a map
     * @param session The HttpSession
     * @param parameterTable The parameter values extracted from the request
     * @param formName The name of the form as defined in the formName attribute
     * of the xfform tag.
     * @return Map
     */
    private Hashtable getDynamoAttributes(HttpSession session,
                                    Map parameterTable, String formName)
    {
        Hashtable passTable = new Hashtable();        
        
        // Added value of vform request parameter if present
        // This allows an ATG Form Handlers to reset a fragmented form's values.
        if (parameterTable.containsKey("vform"))
        {
            passTable.put("vform", (String[])parameterTable.get("vform"));
        }
        
        Enumeration attrNames = session.getAttributeNames();
        
        if(logger.isDebugEnabled()) {
            logger.debug("session attributes are");
            if( attrNames != null )
            {
                while( attrNames.hasMoreElements() )
                {
                    String attrName = (String)attrNames.nextElement();                
                    Object attrValue = session.getAttribute( attrName );                
                
                    logger.debug(attrName+"="+attrValue);                
                }
            }
        }

        HiddenFieldsSessionMap sessionMap = (HiddenFieldsSessionMap)session.getAttribute("_HIDDENATGFIELDS");
        if (sessionMap != null)
        {
            Iterator fieldNames = sessionMap.getAttributeNames(formName);
            
            while (fieldNames.hasNext())
            {
                String fieldName = (String)fieldNames.next();
                String[] passValue = null;                
                
                // If the attribute name begins with _V it is a preset variable
                if( fieldName.startsWith( "_V" ) )
                {
                    String passName = sessionMap.getAttribute(formName, fieldName);
                    // Look up the variable from the parameters
                    String varname = "i_" + fieldName.substring( 2 );
                    
                    if( parameterTable.containsKey( varname ) )
                    {                        
                        passValue = (String[])parameterTable.get( varname );
                        passTable.put( passName, passValue );                        
                    }                    
                } else if( fieldName.startsWith( "_D" ) ) {
                    String passName = sessionMap.getAttribute(formName, fieldName);
                    String varname = "i_" + fieldName.substring( 2 );
                    
                    if( parameterTable.containsKey( varname ) )
                    {
                        passTable.put( passName, new String[] {" "} );                    
                    }                    
                }                
                // Deal with IE and image submit buttons.
                // IE sends i_button.x=anInt&i_button.y=anInt
                // Mozilla: i_button.x=anInt&i_button.y=anInt&i_button=valueOfCaptionAttribute
                // So earlier, we stored a hashtable containing the name and the
                // value of the caption. If a button has been pressed we add the
                // appropriate name value pairs to passTable
                else if( "_BUTTONS".equals(fieldName))
                {
                    Map buttons = sessionMap.getButtons(formName);                    
                    Iterator buttonNames = buttons.keySet().iterator();
                    
                    while (buttonNames.hasNext())
                    {
                        String buttonName = (String)buttonNames.next();
                     
                        String passName;
                                                
                        if (   parameterTable.containsKey(buttonName+".x")
                            && parameterTable.containsKey(buttonName+".y"))
                        {
                            // buttonName was pressed
                            
                            String realButtonName = buttonName.substring(2);
                            
                            // Get appropriate _V session attribute
                            passName = (String)sessionMap.getAttribute(formName, "_V"+realButtonName);
                            String value = (String)buttons.get(buttonName);
                            
                            // Have to put value in an array as passTable needs
                            // to deal with multi-valued form fields e.g. xfmuselects
                            passValue = new String[1];
                            passValue[0] = value;
                            
                            passTable.put(passName, passValue);
                            
                            // Get appropriate _D session attribute
                            passName = (String)sessionMap.getAttribute(formName, "_D"+realButtonName);
                            
                            passTable.put(passName, new String[] {""});                                                       
                        }
                    }
                }
            }
        }
        
        return passTable;
    }

    /**
     * Returns the POST data string
     * @param passTable the Map containing the name value pairs.
     * @return String
     */
    private String getPostData(Hashtable passTable, String DARGS)
    {
        // Add the remapped variables to the parameter list and jump to where we should have been
        StringBuffer sb = new StringBuffer( "_DARGS=" + servletUtils.encodedValue( DARGS ) );

        Enumeration keyNames = passTable.keys();
        if( keyNames != null )
        {
            while( keyNames.hasMoreElements() )
            {
                String attrName = (String)keyNames.nextElement();
                String[] attrValue = (String[])passTable.get( attrName );
                for (int i=0; i<attrValue.length; i++)
                {
                    sb.append("&");
                    sb.append(servletUtils.encodedValue( attrName ));
                    sb.append("=");
                    sb.append(servletUtils.encodedValue( attrValue[i] ));
                }
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Clears the temporary variables we put in the session to process the form    
     * @param session The user's HttpSession object
     * @param formName The name of the form
     */
    private void clearSession(HttpSession session, String formName)
    {
        HiddenFieldsSessionMap sessionMap = (HiddenFieldsSessionMap)session.getAttribute("_HIDDENATGFIELDS");
        
        if (sessionMap != null)
        {
            sessionMap.removeAttributes(formName);                
        }        
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

 27-Apr-04	3843/1	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
