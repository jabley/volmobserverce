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
 * $Header: /src/voyager/com/volantis/mcs/integration/URLRewriter.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
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
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.integration;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.utilities.MarinerURL;

/**
 * This interface provides a mechanism to map a url to another url and
 * back again.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @mock.generate 
 */
public interface URLRewriter {
    
  /**
   * Map a MarinerURL object using some external mapping to another
   * MarinerURL. 
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
  public MarinerURL mapToExternalURL(MarinerRequestContext context,
                                     MarinerURL url);
    
  /**
   * Map a MarinerURL object from some external mapping to a mariner
   * MarinerURL. 
   * @param context a MarinerRequestContext object
   * @param url the MarinerURL object to map from
   * @return a MarinerURL object
   */
  public MarinerURL mapToMarinerURL(MarinerRequestContext context,
                                    MarinerURL url);
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

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
