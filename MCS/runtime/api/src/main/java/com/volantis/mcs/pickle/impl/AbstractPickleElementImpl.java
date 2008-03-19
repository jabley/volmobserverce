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
 * $Header: /src/voyager/com/volantis/mcs/pickle/AbstractPickleElementImpl.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Paul            VBM:2002032105 - Created to be a base class
 *                              for all pickle elements.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.pickle.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;

import com.volantis.mcs.papi.impl.AbstractElementImpl;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;

import com.volantis.mcs.protocols.CustomMarkupAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.pickle.PickleAttributes;

/**
 * The base class for all pickle elements.
 */
public abstract class AbstractPickleElementImpl
  extends AbstractElementImpl {


  /**
   * The attributes to pass to the protocol.
   */
  private CustomMarkupAttributes pattributes;

  /**
   * Create a new <code>AbstractPickleElementImpl</code>.
   */
  public AbstractPickleElementImpl () {
    pattributes = new CustomMarkupAttributes ();
  }

  // Javadoc inherited from super class.
  public int elementStart (MarinerRequestContext context,
                           PAPIAttributes papiAttributes)
    throws PAPIException {

    MarinerPageContext pageContext
      = ContextInternals.getMarinerPageContext (context);

      if (pageContext.insideXDIMECPElement()) {
          throw new PAPIException(EXCEPTION_LOCALIZER.format(
                  "xdime-not-allowed-inside-xdimecp"));
      }

    PickleAttributes attributes = (PickleAttributes) papiAttributes;

    pattributes.setElementName (attributes.getElementName ());
    pattributes.setAttributes (attributes.getAttributes ());
    
    VolantisProtocol protocol = pageContext.getProtocol ();
    protocol.writeOpenElement (pattributes);

    // Push this element.
    pageContext.pushElement (this);
    
    return PROCESS_ELEMENT_BODY;
  }

  // Javadoc inherited from super class.
  public int elementEnd (MarinerRequestContext context,
                         PAPIAttributes papiAttributes)
    throws PAPIException {
    
    MarinerPageContext pageContext
      = ContextInternals.getMarinerPageContext (context);

    PickleAttributes attributes = (PickleAttributes) papiAttributes;
    
    // Pop this element.
    pageContext.popElement (this);

    VolantisProtocol protocol = pageContext.getProtocol ();
    protocol.writeCloseElement (pattributes);
    
    return CONTINUE_PROCESSING;
  }

  // Javadoc inherited from super class.
  public void elementReset (MarinerRequestContext context) {
    pattributes.resetAttributes ();

    super.elementReset (context);
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

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 ===========================================================================
*/
