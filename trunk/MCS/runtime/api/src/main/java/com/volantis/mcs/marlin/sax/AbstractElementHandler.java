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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/AbstractElementHandler.java,v 1.1 2002/11/23 01:04:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Paul            VBM:2002112214 - Created as the base class of
 *                              the MarlinElementHandlers.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;

import org.xml.sax.SAXException;

/**
 * This class provides some default implementations of some methods.
 */
public abstract class AbstractElementHandler
  implements MarlinElementHandler {

  /**
   * Default implementation does nothing.
   */
  public void beforePAPIElement (PAPIContentHandlerContext context,
                                 PAPIAttributes papiAttributes)
    throws SAXException {
  }

  /**
   * Default implementation does nothing.
   */
  public void afterPAPIElement (PAPIContentHandlerContext context,
                                PAPIAttributes papiAttributes)
    throws SAXException {
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 ===========================================================================
*/
