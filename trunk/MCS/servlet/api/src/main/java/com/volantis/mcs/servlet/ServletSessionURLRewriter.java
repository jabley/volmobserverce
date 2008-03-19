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
 * $Header: /src/voyager/com/volantis/mcs/servlet/ServletSessionURLRewriter.java,v 1.3 2002/08/23 10:55:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Dec-01    Paul            VBM:2001121702 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 * 				to string.
 * 23-Aug-02	Steve		VBM:2002082001 Catch NullPointerException on
 *				response.encodeURL() for Weblogic. This only
 *			        occurs in Weblogic if there is no Session
 *				cookie set in the browser and the path is null
 *        			for instance if <form action="#".... > is
 *				called.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.servlet;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.integration.URLRewriter;

import com.volantis.mcs.utilities.MarinerURL;

import javax.servlet.http.HttpServletResponse;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

public class ServletSessionURLRewriter
  implements URLRewriter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ServletSessionURLRewriter.class);

    /**
     * Encode the URL with session id if necessary.
     */
  public MarinerURL mapToExternalURL (MarinerRequestContext requestContext,
                                      MarinerURL url) {

    MarinerServletRequestContext context
      = (MarinerServletRequestContext) requestContext;

    HttpServletResponse response = context.getHttpResponse ();
    
    // Encode the path part of this URL with the session info, this will
    // not work on those application servers which do not adhere to the
    // specification in the way that they encode the URL. The specification
    // clearly states that the session id must be encoded as a path parameter
    // called jsessionid but some application servers encode it as a query
    // parameter, and some encode it using a differently named path
    // parameter and some will post process the page and encode anything it
    // thinks is a relative URL.
    String path = url.getPath ();

    // Weblogic throws a null pointer exception if there is no path
    // which is the case in the instance of <form action="#" ... >
    // and there is no cookie with a session ID.
    try {
	String encoded = response.encodeURL (path);
    	url.setPath (encoded);
    }
    catch( NullPointerException npe )
    {
	logger.warn("path-null-pointer", new Object[]{path});
    }

    return url;
  }

  public MarinerURL mapToMarinerURL (MarinerRequestContext requestContext,
                                     MarinerURL url) {

    throw new UnsupportedOperationException ();
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
