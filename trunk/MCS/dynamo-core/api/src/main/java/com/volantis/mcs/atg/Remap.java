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
 * $Header: /src/voyager/com/volantis/mcs/atg/Remap.java,v 1.6 2002/03/18 12:41:12 ianw Exp $
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
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg;

import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

//import atg.servlet.*;

/** 
 *
 * @author  steve
 */
public class Remap extends HttpServlet 
{   
    Hashtable parameterTable = null;
    Hashtable passTable = null;
    String url = null;
    String DARGS = null;
    
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
        // Read the parameter values and store them in a map
        parameterTable = new Hashtable();
        Enumeration paramNames = request.getParameterNames();
        while( paramNames.hasMoreElements() )
        {
            String paramName = (String)paramNames.nextElement();
            String pValue = request.getParameter( paramName );
            if( pValue == null ) pValue = new String("");
            parameterTable.put( paramName, pValue );
        }
        
        // Read all the attributes from the session
        passTable = new Hashtable();
        HttpSession session = request.getSession();
        Enumeration attrNames = session.getAttributeNames();
        if( attrNames != null )
        {   
            while( attrNames.hasMoreElements() )
            {
                String attrName = (String)attrNames.nextElement();
                Object attrValue = session.getAttribute( attrName );
                String passValue = null;

                // If the attribute name begins with _V it is a preset variable
                if( attrName.startsWith( "_V" ) )
                {
                    String passName = passName = attrValue.toString();
                    // Look up the variable from the parameters
                    String varname = "i_" + attrName.substring( 2 );
                    if( parameterTable.containsKey( varname ) )
                    {
                        passValue = (String)parameterTable.get( varname );
                        passTable.put( passName, passValue );
                    }
                } else if( attrName.equals("_DARGS") ) {
                    DARGS = attrValue.toString();
                } else if( attrName.startsWith( "_D" ) ) {
                    String passName = passName = attrValue.toString();
                    String varname = "i_" + attrName.substring( 2 );
                    if( parameterTable.containsKey( varname ) )
                    {
                        passTable.put( passName, new String( " " ) );
                    }
                } else if( attrName.equals( "_URL" ) ) {
                    url = attrValue.toString();
                }
            }
        }

        // Add the remapped variables to the parameter list and jump to where we should have been
        String pString = new String( "?_DARGS=" + encodedValue( DARGS ) );

        Enumeration keyNames = passTable.keys();
        if( keyNames != null )
        {
            while( keyNames.hasMoreElements() )
            {
                String attrName = (String)keyNames.nextElement();
                String attrValue = (String)passTable.get( attrName );
                pString = pString + "&" + encodedValue( attrName ) + "=" + encodedValue( attrValue );
            }
        }
        
        System.out.println( "Redirecting to " + java.net.URLDecoder.decode(url + pString) );

        gotoPage( url + pString, request, response );
    } 

    /** Handles the HTTP <code>GET</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doGet( HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    } 

    /** Handles the HTTP <code>POST</code> method.
    * @param request servlet request
    * @param response servlet response
    */
    protected void doPost( HttpServletRequest request, HttpServletResponse response)
    throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    private String encodedValue( String s )
    {
        if( s != null )
        {
            return( java.net.URLEncoder.encode(s) );
        } else {
            return new String( "" );
        }
    }
    
    private void gotoPage( String url, HttpServletRequest request, HttpServletResponse response )
    throws ServletException, java.io.IOException {
        response.setStatus( response.SC_MOVED_TEMPORARILY );
        response.sendRedirect( url );
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
