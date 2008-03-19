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
 * $Header: /src/voyager/com/volantis/mcs/internal/InternalSessionURLRewriter.java,v 1.2 2003/02/11 12:50:18 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from Metis.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.internal;

import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.integration.URLRewriter;

import com.volantis.mcs.utilities.MarinerURL;



import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

public class InternalSessionURLRewriter implements URLRewriter {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(InternalSessionURLRewriter.class);

    /**
     * Encode the URL with session id if necessary.
     */
  public MarinerURL mapToExternalURL (MarinerRequestContext requestContext,
                                      MarinerURL url) {
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
