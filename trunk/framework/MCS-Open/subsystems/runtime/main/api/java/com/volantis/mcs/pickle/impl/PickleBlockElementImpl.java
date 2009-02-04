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
 * $Header: /src/voyager/com/volantis/mcs/pickle/PickleBlockElementImpl.java,v 1.8 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Paul            VBM:2002032105 - Created to allow pickle output
 *                              to be directed to a particular pane.
 * 29-Oct-02    Chris W         VBM:2002111101 - Gets pane name from 
 *                              FormatReference rather than directly from 
 *                              MarinerPageContext. Store the 
 *                              FormatInstanceReference as a property
 *                              of this object.
 * 29-Jan-03    Chris W         VBM:2003012203 - elementStart calls
 *                              FormatIteratorFormatFilter.isSkippable to see
 *                              if tag refers to a particular instance of a
 *                              pane that is outside the max. permitted by a
 *                              spatial or temporal format iterator.
 * 07-Feb-03    Chris W         VBM:2003020609 - Code in FormatIteratorFormatFilter
 *                              moved to FormatInstance, so elementStart calls
 *                              ignore(fir) on a FormatInstance instead.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.pickle.impl;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.runtime.FormatReferenceParser;
import com.volantis.mcs.pickle.impl.AbstractPickleElementImpl;
import com.volantis.mcs.pickle.PickleBlockAttributes;

import java.io.IOException;

/**
 * Instances of this class should be used when the pickle output can be
 * directed at a specific pane.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class PickleBlockElementImpl
  extends AbstractPickleElementImpl {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2002.";

  /**
   * The pane instance which this element pushed onto the stack, or null if no
   * pane name was specified for this element.
   */
  private AbstractPaneInstance paneInstance;

  /**
   * Flag which indicates whether the elementStart method returned
   * SKIP_ELEMENT_BODY.
   */
  private boolean skipped;

  // Javadoc inherited from super class.
  public int elementStart (MarinerRequestContext context,
                           PAPIAttributes papiAttributes)
    throws PAPIException {

    MarinerPageContext pageContext
      = ContextInternals.getMarinerPageContext (context);

    PickleBlockAttributes attributes = (PickleBlockAttributes) papiAttributes;
    
    // Try and find the pane with the specified name, if it could not be
    // found then return and skip the element body.
    String paneName = attributes.getPane ();
    if (paneName != null) {
      FormatReference formatRef = FormatReferenceParser.parsePane(paneName, pageContext);
      Pane pane = pageContext.getPane(formatRef.getStem());
      NDimensionalIndex paneIndex = formatRef.getIndex();
      
      if (pane == null) {
          skipped = true;
          return SKIP_ELEMENT_BODY;
      }

        paneInstance = (AbstractPaneInstance)
                pageContext.getFormatInstance(pane, paneIndex);
        if (paneInstance.ignore()) {
            skipped = true;
            return SKIP_ELEMENT_BODY;
        }

      pageContext.pushContainerInstance (paneInstance);
    }

    // The super class will handle initialising the protocol attributes and
    // calling the protocol.
    return super.elementStart (context, papiAttributes);
  }

  // Javadoc inherited from super class.
  public int elementEnd (MarinerRequestContext context,
                         PAPIAttributes papiAttributes)
    throws PAPIException {
    
    if (skipped) {
      return CONTINUE_PROCESSING;
    }

    // Make sure that the attributes are of the correct type.
    PickleBlockAttributes attributes = (PickleBlockAttributes) papiAttributes;
    
    // The super class will call the protocol.
    super.elementEnd (context, papiAttributes);

    MarinerPageContext pageContext
      = ContextInternals.getMarinerPageContext (context);

    if (paneInstance != null) {
      pageContext.popContainerInstance (paneInstance);
    }

    return CONTINUE_PROCESSING;
  }

  // Javadoc inherited from super class.
  public void elementReset (MarinerRequestContext context) {
    paneInstance = null;
    skipped = false;

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

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 18-May-05	8196/1	ianw	VBM:2005051203 Final chunk of resolving accurev hell

 18-May-05	8196/4	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 ===========================================================================
*/
