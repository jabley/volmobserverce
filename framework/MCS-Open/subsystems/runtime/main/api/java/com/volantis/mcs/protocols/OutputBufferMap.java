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
 * $Header: /src/voyager/com/volantis/mcs/protocols/OutputBufferMap.java,v 1.3 2002/03/22 18:24:27 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Mar-02    Paul            VBM:2002030607 - Created to provide a general
 *                              way of retrieving arbitrary OutputBuffers by
 *                              name.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Added debugging.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements a map from name to OutputBuffer.
 */
public class OutputBufferMap {

  /**
   * The OutputBuffer factory.
   */
  private OutputBufferFactory factory;

  /**
   * The map of the buffers.
   */
  private Map buffers;

  /**
   * Create a new <code>OutputBufferMap</code>.
   */
  public OutputBufferMap () {
  }

  /**
   * Set the <code>OutputBufferFactory</code> which is needed before the map
   * is initialised.
   * @param factory The <code>OutputBufferFactory</code>.
   */
  public void setOutputBufferFactory (OutputBufferFactory factory) {
    this.factory = factory;
  }

  /**
   * Get the buffer with the specified name.
   * @param name The name of the buffer to create.
   * @param create Controls whether the buffer should be created if it does
   * not exist already.
   */
  public OutputBuffer getBuffer (String name, boolean create) {
    OutputBuffer buffer;

    // If the map does not exist then the buffer cannot exist.
    if (buffers == null) {
      // If we have been asked to create the buffer then we need to create
      // the map as well.
      if (create) {
        buffers = new HashMap ();
      }
      buffer = null;
    } else {
      // See whether the buffer exists in the map.
      buffer = (OutputBuffer) buffers.get (name);
    }
    
    // If the buffer could not be found and we have been asked to create the
    // buffer if necessary then do so and add it into the map.
    if (buffer == null && create) {
      buffer = factory.createOutputBuffer ();
      buffers.put (name, buffer);
    }
    
    return buffer;
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 ===========================================================================
*/
