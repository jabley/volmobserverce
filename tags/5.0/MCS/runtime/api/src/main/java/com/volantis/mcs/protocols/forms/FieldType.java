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
 * $Header: /src/voyager/com/volantis/mcs/protocols/forms/FieldType.java,v 1.4 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Feb-02    Paul            VBM:2001100102 - Created to encapsulate the
 *                              behaviour of different field types.
 * 04-Mar-02    Paul            VBM:2001101803 - Removed the attributes
 *                              parameter from getFieldHandler method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;

/**
 * This interface provides a general way for easily supporting different
 * types of fields. It uses the Visitor design pattern.
 *
 * @mock.generate 
 */
public interface FieldType {

  /**
   * Process the field.
   * @param protocol The protocol to use.
   * @param attributes The field attributes.
   */
  public void doField (VolantisProtocol protocol,
          XFFormFieldAttributes attributes) throws ProtocolException;

  /**
   * Get the handler for the field.
   * @param protocol The protocol to use.
   */
  public FieldHandler getFieldHandler (VolantisProtocol protocol);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
