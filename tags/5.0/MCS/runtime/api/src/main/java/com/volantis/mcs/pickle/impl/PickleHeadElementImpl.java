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
 * $Header: /src/voyager/com/volantis/mcs/pickle/PickleHeadElementImpl.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Paul            VBM:2002032105 - Created to allow pickle output
 *                              to be written to the head of the page.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.pickle.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.pickle.impl.AbstractPickleElementImpl;
import com.volantis.mcs.pickle.PickleHeadAttributes;

/**
 * Instances of this class should be used when the pickle output can be
 * directed to the head of the page.
 * <p>
 * In some circumstances the head of the page does not become part of the
 * generated content. If the current page is a portlet which is not being
 * generated within the context of a mariner portal then the head does not
 * become part of the generated page's content.
 * </p>
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class PickleHeadElementImpl
  extends AbstractPickleElementImpl {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2002.";

  // Javadoc inherited from super class.
  public int elementStart (MarinerRequestContext context,
                           PAPIAttributes papiAttributes)
    throws PAPIException {

    MarinerPageContext pageContext
      = ContextInternals.getMarinerPageContext (context);

    VolantisProtocol protocol = pageContext.getProtocol ();
    protocol.pushHeadBuffer ();

    // Make sure that the attributes are of the correct type.
    PickleHeadAttributes attributes = (PickleHeadAttributes) papiAttributes;
    
    return super.elementStart (context, papiAttributes);
  }

  // Javadoc inherited from super class.
  public int elementEnd (MarinerRequestContext context,
                         PAPIAttributes papiAttributes)
    throws PAPIException {
    
    // Make sure that the attributes are of the correct type.
    PickleHeadAttributes attributes = (PickleHeadAttributes) papiAttributes;
    
    super.elementEnd (context, papiAttributes);

    MarinerPageContext pageContext
      = ContextInternals.getMarinerPageContext (context);

    VolantisProtocol protocol = pageContext.getProtocol ();
    protocol.popHeadBuffer ();

    return CONTINUE_PROCESSING;
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

 18-May-05	8196/1	ianw	VBM:2005051203 Final chunk of resolving accurev hell

 18-May-05	8196/4	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 ===========================================================================
*/
