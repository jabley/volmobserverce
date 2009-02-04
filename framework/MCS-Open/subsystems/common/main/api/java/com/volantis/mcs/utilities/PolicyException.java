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
/*
 *
 * $Header: /src/voyager/com/volantis/mcs/utilities/PolicyException.java,v 1.6 2000/12/08 13:14:25 pduffin Exp $
 *
 * (c) Volantis Systems Ltd 2000. 
 */

package com.volantis.mcs.utilities;

/**
 *
 * @author  root
 */
public class PolicyException extends Exception {

 private static String mark = "(c) Volantis Systems Ltd 2000. ";

  private int errorCode;

  /**
   * Creates new <code>Policy</code> without detail message.
   */
  public PolicyException() {
  }


  /**
   * Constructs an <code>Policy</code> with the specified detail message.
   * @param msg the detail message.
   */
  public PolicyException(String msg) {
    super(msg);
  }

  public PolicyException(String msg, int errorCode) {
    super(msg);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return (errorCode);
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
