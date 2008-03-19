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
 * $Header: /src/voyager/com/volantis/mcs/papi/PAPIException.java,v 1.5 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Oct-01    Paul            VBM:2001111402 - Created
 * 30-Nov-01    Paul            VBM:2001112909 - Added copyright statement.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Change to extend
 *                              WrappingException.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

/**
 * This class is used when an exception occurs inside PAPI.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
*/
public class PAPIException extends Exception {

  /**
   * Create a new <code>PAPIException</code> with no message.
   */
  public PAPIException () {
  }

  /**
   * Create a new <code>PAPIException</code>
   * with the specified message.
   * @param message The message.
   */
  public PAPIException (String message) {
    super (message);
  }

  /**
   * Create a new <code>PAPIException</code> with the specified 
   * message which was caused by the specified Throwable.
   * @param message The message.
   * @param cause The cause of this exception being thrown.
   */
  public PAPIException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * Create a new <code>PAPIException</code> which was caused by the
   * specified Throwable.
   * @param cause The cause of this exception being thrown.
   */
  public PAPIException (Throwable cause) {
    super (cause);
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

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
