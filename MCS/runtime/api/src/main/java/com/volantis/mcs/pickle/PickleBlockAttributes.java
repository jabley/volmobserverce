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
 * $Header: /src/voyager/com/volantis/mcs/pickle/PickleBlockAttributes.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Mar-03    Paul            VBM:2002032105 - Created to allow pickle output
 *                              to be written to a specific pane.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.pickle;

/**
 * The block pickle attributes.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public final class PickleBlockAttributes
  extends PickleAttributes {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2002.";

  /**
   * The name of the pane.
   */
  private String pane;

  /**
   * Set the value of the pane property.
   * @param pane The new value of the pane property.
   */
  public void setPane (String pane) {
    this.pane = pane;
  }

  /**
   * Get the value of the pane property.
   * @return The value of the pane property.
   */
  public String getPane () {
    return pane;
  }

  /**
   * Resets the internal state so it is equivalent (not necessarily identical)
   * to a new instance.
   */
  public void reset () {
    pane = null;

    super.reset ();
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

 ===========================================================================
*/
