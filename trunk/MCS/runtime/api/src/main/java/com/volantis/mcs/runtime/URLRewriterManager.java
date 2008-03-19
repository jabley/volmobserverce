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
 * $Header: /src/voyager/com/volantis/mcs/runtime/URLRewriterManager.java,v 1.3 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Nov-01    Doug            VBM:2001112004 - Created.  
 * 28-Nov-01    Paul            VBM:2001112202 - This class was moved from the
 *                              plugins.URLRewriter package.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Moved from integration.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.integration.URLRewriter;

import com.volantis.mcs.utilities.MarinerURL;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Class to manage URLRewriter interfaces
 *
 */
public class URLRewriterManager
  implements URLRewriter {

  /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(URLRewriterManager.class);

  private URLRewriter urlRewriter;

  /**
   * Creates a new URLRewriterManager instance. Uses the
   * rewriterClass string to detirmine the class of the 
   * URLWriter that is to be loaded. If this string is null
   * then a default implementation of URLWriter is used.
   * This default implementation performs no rewriting.
   *
   * @param rewriterClass the URLRewriter interface to use
   * @exception ClassNotFoundException if an error occurs
   * @exception InstantiationException if an error occurs
   * @exception IllegalAccessException if an error occurs
   */
  public URLRewriterManager(String rewriterClass) 
    throws ClassNotFoundException, InstantiationException, 
           IllegalAccessException {

    if(null != rewriterClass) {
      urlRewriter = 
        (URLRewriter)Class.forName(rewriterClass).newInstance();
    } else {
      urlRewriter = new DefaultURLRewriter();
    }
    if(logger.isDebugEnabled()){
        logger.debug ("Using the URLRewriter " + urlRewriter);
    }
  }

  /**
   * Map a MarinerURL object using some external mapping to another
   * MarinerURL. Delegate this operation to the URLRewriter that was 
   * loaded at initialisation.
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
  public MarinerURL mapToExternalURL(MarinerRequestContext context,
                                     MarinerURL url) {
    return urlRewriter.mapToExternalURL(context, url);
  }


  /**
   * Map a MarinerURL object from some external mapping to a mariner
   * MarinerURL. Delegate this operation to the URLRewriter that was 
   * loaded at initilialisation.
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
  public MarinerURL mapToMarinerURL(MarinerRequestContext context,
                                    MarinerURL url) {
    return urlRewriter.mapToMarinerURL(context, url);
  }

  /**
   * Default implementation of URLRewriter interface.
   * Does nothing
   *
   */
  public static class DefaultURLRewriter implements URLRewriter{
    public MarinerURL mapToExternalURL(MarinerRequestContext context,
                                       MarinerURL url) {
      return url;
    }
	
    public MarinerURL mapToMarinerURL(MarinerRequestContext context,
                                      MarinerURL url) {
      return url;
    }
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

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
