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
 * $Header: /src/voyager/com/volantis/mcs/atg/portal/Attic/FragmentURLRewriter.java,v 1.1.2.3 2003/01/24 10:13:00 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jul-02    Chris W         VBM:2002080209 - Created
 * 15-Aug-02    Chris W         VBM:2002081511 - Copied existing class to 
 *                              separate package for Dynamo5.6.1 We want to be
 *                              able to support the older Dynamo5.1.1 (?) version
 *                              as well. Ensured that the MarinerATGRemap servlet's
 *                              url is not rewritten.
 * 24-Jan-03    Chris W         VBM:2003012310 - L3 request #101656.
 *                              If there is a jsessionid in the url that we wish
 *                              to rewrite then we add it to the rewritten url. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.portal;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.utilities.MarinerURL;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides a mechanism to convert urls generated by Mariner
 * fragments into urls that can be processed by ATG Portal Server.
 *
 * e.g. The portal page test in the helloworld community is requested from ATG
 * by the path /helloworld/test.
 * By default Mariner tries to obtain the first child fragment from
 * /portal/layoutTemplates/volantis/shared_page_template.jsp?vfrag=f0
 * However this jsp can be successfully requested by a browser without first
 * going through ATG's Portal Framework.
 * The correct path is /portal/helloworld/test?vfrag=f0
 *
 */
public class FragmentURLRewriter implements com.volantis.mcs.integration.URLRewriter
{

    
    /** Creates a new instance of FragmentURLRewriter */
    public FragmentURLRewriter()
    {
    }

   /**
   * Map a MarinerURL object using some external mapping to another
   * MarinerURL. 
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
  public MarinerURL mapToExternalURL(MarinerRequestContext context,
                                     MarinerURL url)
  {            
      MarinerServletRequestContext servletContext = (MarinerServletRequestContext)context;
      HttpServletRequest request = servletContext.getHttpRequest();
      //HttpServletResponse response = servletContext.getHttpResponse();          
            
      /* The are two ways to get the original request URI.
       *
       * 1a. Get the Portal Application Framework environment via a non-published
       * ATG factory method
       * 1b. Get the original request URI via a published ATG API method call
       * 1c. Set the path of the MarinerURL to be the original request URI
       * Caveat: If we can't get the Portal Application Framework environment, an
       * error is logged and the URL remains unchanged.
       *
       * 2. Get atg.paf.OriginalRequestURI from the request attributes.
       * Cavert: Relies on atg.paf.OriginalRequestURI always being present.
       */
      
      /* This is the code for solution 1 
      try
      {
        GearEnvironment pafEnv = EnvironmentFactory.getGearEnvironment(request, response);
        String requestURI = pafEnv.getOriginalRequestURI();        
        url.setPath(requestURI);
        
        logger.debug("chrisw URLRewriter: requestURI="+requestURI);
      }      
      catch (EnvironmentException e)
      {
        // SHOULD LOG ERROR RATHER THAN DEBUG MESSAGE  
        logger.debug("chrisw URLRewriter: could not get pafEnv ", e);
      }
      */
      
      // This is the code for solution 2
      
      // Don't remap url if its the MarinerATGRemapServlet
      if (url.getExternalForm().indexOf("MarinerATGRemap") != -1)
      {
          return url;
      }
      else
      {
          // If a jsessionid is appended to the url's path, ensure that it
          // still appears at the end of the rewritten url's path.          
          //logger.debug("chrisw getPath: "+url.getPath());           
          url.setPath((String)request.getAttribute("atg.paf.OriginalRequestURI")
                       + getJSessionId(url.getPath()));          
      }
      
      return url;
  }

    /**
     * Returns the jsessionid i.e. ";jsessionid=728dg72a637bc6" from the path
     * of a url or "" if the path does not contain a jsessionid
     * @param path a String containing the path
     * @return String the jsessionid
     */
    private String getJSessionId(String path)
    {
        int pos = path.indexOf(';');
        if (pos != -1)
        {
            return path.substring(pos);
        }
        else
        {
            return "";
        }
    }

  /**
   * Map a MarinerURL object from some external mapping to a mariner
   * MarinerURL. 
   * At the moment this method does not need to perform any changes
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
  public MarinerURL mapToMarinerURL(MarinerRequestContext context,
                                    MarinerURL url)
  {
      return url;
  }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 27-Apr-04	3843/1	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
