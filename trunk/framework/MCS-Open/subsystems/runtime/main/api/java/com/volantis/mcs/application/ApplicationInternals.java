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
 * $Header: /src/voyager/com/volantis/mcs/application/ApplicationInternals.java,v 1.2 2002/11/14 12:59:45 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Nov-02    Paul            VBM:2002091806 - Created to provide access
 *                              to package private methods to other mariner
 *                              packages.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.application;

import com.volantis.mcs.runtime.Volantis;

/**
 * This class provides methods to access package private methods from other
 * mariner packages.
 */
public class ApplicationInternals {

  /**
   * Set the value of the volantis bean property.
   * @param application The MarinerApplication which contains the reference
   * to the Volantis bean.
   * @param volantisBean The new value of the volantis bean property.
   */
  public static void setVolantisBean (MarinerApplication application,
                                      Volantis volantisBean) {
    application.setVolantisBean (volantisBean);
  }

  /**
   * Get the value of the volantis bean property.
   * @param application The MarinerApplication which contains the reference
   * to the Volantis bean.
   * @return The value of the volantis bean property.
   */
  public static Volantis getVolantisBean (MarinerApplication application) {
    return application.getVolantisBean ();
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

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
