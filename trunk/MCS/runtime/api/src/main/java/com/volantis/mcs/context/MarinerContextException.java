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
 * $Header: /src/voyager/com/volantis/mcs/context/MarinerContextException.java,v 1.3 2002/03/22 18:24:27 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Jan-01    Adrian          VBM:2001121003 - Created this exception class
 *                              because exceptions caught in specific
 *                              environment contexts need to be rethrown as 
 *                              generic context exceptions
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Change to extend
 *                              WrappingException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.context;

/**
 * This class is used when an exception occurs inside EnvironmentContexts.
 */
public class MarinerContextException extends Exception {

  /**
   * Create a new <code>MarinerContextException</code> with no message.
   */
  public MarinerContextException () {
  }

  /**
   * Create a new <code>MarinerContextException</code>
   * with the specified message.
   * @param message The message.
   */
  public MarinerContextException (String message) {
    super (message);
  }

  /**
   * Create a new <code>MarinerContextException</code> with the specified 
   * message which was caused by the specified Throwable.
   * @param message The message.
   * @param cause The cause of this exception being thrown.
   */
  public MarinerContextException (String message, Throwable cause) {
    super (message, cause);
  }

  /**
   * Create a new <code>MarinerContextException</code> which was caused by the
   * specified Throwable.
   * @param cause The cause of this exception being thrown.
   */
  public MarinerContextException (Throwable cause) {
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

 ===========================================================================
*/
