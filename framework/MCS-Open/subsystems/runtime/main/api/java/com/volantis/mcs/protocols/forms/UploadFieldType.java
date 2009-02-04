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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.forms;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFUploadAttributes;

/**
 * This class defines the behaviour of text fields.
 */ 
public final class UploadFieldType
  implements FieldType {

  /**
   * The reference to the single allowable instance of this class.
   */
  private static final UploadFieldType singleton;

  // Initialise the static fields.
  static {
    // Always initialise to prevent a synchronization problem if we do it
    // lazily.
    singleton = new UploadFieldType ();
  }

  /**
   * Get the single allowable instance of this class.
   * @return The single allowable instance of this class.
   */
  public static UploadFieldType getSingleton () {
    return singleton;
  }

  /**
   * Protect the constructor to prevent any other instances being created.
   */
  private UploadFieldType () {
  }

  // Javadoc inherited from super class.
  public void doField (VolantisProtocol protocol,
          XFFormFieldAttributes attributes) throws ProtocolException {

    protocol.doUpload((XFUploadAttributes) attributes);
  }

  // Javadoc inherited from super class.
  public FieldHandler getFieldHandler (VolantisProtocol protocol) {
    return protocol.getFieldHandler (this);
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 ===========================================================================
*/
