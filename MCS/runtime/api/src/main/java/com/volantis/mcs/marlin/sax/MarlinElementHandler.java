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
 * $Header: /src/voyager/com/volantis/mcs/marlin/sax/MarlinElementHandler.java,v 1.1 2002/11/23 01:04:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Paul            VBM:2002112214 - Created to define methods to
 *                              use when mapping from SAX 2 events to PAPI.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIElement;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This interface defines the methods which should be implemented by classes
 * which create and initialize PAPIAttributes and PAPIElement objects.
 * <p>
 * It acts as an adapter or bridge between the SAX2 events and the PAPI.
 * <p>
 * Separate implementations of this interface must be created for every element
 * supported by PAPI. Each implementation must support the flyweight pattern
 * in that if they need to contain any state they must only contain intrinsic
 * and not extrinsic state.
 * <p>
 * Intrinsic state is independent of the context within which the object is
 * used.
 * <p>
 * Extrinsic state is dependent on the context within which the object is used.
 */
public interface MarlinElementHandler {

  /**
   * Create an instance of the appropriate PAPIAttributes class.
   * @param context The context within which this method is called.
   * @return The newly created instance of the appropriate PAPIAttributes
   * class.
   */
  public PAPIAttributes createPAPIAttributes (PAPIContentHandlerContext context);

  /**
   * Release an instance of the appropriate PAPIAttributes class.
   * @param context The context within which this method is called.
   * @param attributes The PAPIAttributes object to release.
   */
  //public void releasePAPIAttributes (PAPIContentHandlerContext context,
  //PAPIAttributes attributes);

  /**
   * Create an instance of the appropriate PAPIElement class.
   * @param context The context within which this method is called.
   * @return The newly created instance of the appropriate PAPIElement class.
   */
  public PAPIElement createPAPIElement (PAPIContentHandlerContext context);

  /**
   * Release an instance of the appropriate PAPIElement class.
   * @param context The context within which this method is called.
   * @param element The PAPIElement object to release.
   */
  //public void releasePAPIElement (PAPIContentHandlerContext context,
  //PAPIElement element);

  /**
   * Initialize the PAPIAttributes object from the saxAttributes object.
   * @param context The context within which this method is called.
   * @param saxAttributes The set of SAX2 attributes from which the
   * PAPIAttributes should be initialized.
   * @param attributes The PAPIAttributes object to initialize. This is
   * guaranteed to be the same type as was returned from the
   * {@link #createPAPIAttributes} method.
   */
  public void initializePAPIAttributes (PAPIContentHandlerContext context,
                                        Attributes saxAttributes,
                                        PAPIAttributes attributes)
    throws SAXException;

  /**
   * Perform any initialisation before the PAPIElement is used.
   * <p>
   * This method is called after the PAPIElement and PAPIAttributes objects
   * have been initialised but before they are used. It must ensure that the
   * context has been properly prepared for the element.
   *
   * @param context The context within which this method is called and which
   * this method may change.
   * @param papiAttributes The PAPIAttributes object which contains the
   * attributes for the element.
   */
  public void beforePAPIElement (PAPIContentHandlerContext context,
                                 PAPIAttributes papiAttributes)
    throws SAXException;

  /**
   * Perform any cleaning up after the PAPIElement is used.
   * <p>
   * This method is called after the PAPIElement and PAPIAttributes objects
   * have been used but before they are reset.
   *
   * @param context The context within which this method is called and which
   * this method may change.
   * @param papiAttributes The PAPIAttributes object which contains the
   * attributes for the element.
   */
  public void afterPAPIElement (PAPIContentHandlerContext context,
                                PAPIAttributes papiAttributes)
    throws SAXException;

  /**
   * Return whether or not the element can contain character data.
   * @return True if the element can contain character data and false otherwise.
   */
  public boolean canContainCharacterData ();
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

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 ===========================================================================
*/
