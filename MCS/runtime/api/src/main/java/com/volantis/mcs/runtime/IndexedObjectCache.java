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
 * $Header: /src/voyager/com/volantis/mcs/runtime/IndexedObjectCache.java,v 1.3 2002/03/18 12:41:19 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-02    Paul            VBM:2002021203 - Created to generalise the
 *                              caching model used to cache fragmentation
 *                              states.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a cache of objects which are accessible using a simple
 * index. It is used for generating simple keys from more complicated objects.
 * <p>
 * At the moment it is simply an array of objects but this will become
 * inefficient if there are lots of objects being added so it may be better
 * to have a map which is used to look up the index for an object.
 * </p>
 */
public class IndexedObjectCache {

  /**
   * The list of objects.
   */
  private ArrayList cache;

  /**
   * Create a new <code>IndexedObjectCache</code>.
   */
  public IndexedObjectCache () {
    cache = new ArrayList ();
  }

  /**
   * Get the object for a particular index, if the index is not valid then
   * null is returned.
   * @param index The index of the object to retrieve.
   * @return The object, or null if the index was not valid.
   */
  public Object getObject (int index) {
    synchronized (cache) {
      if (index < 0 || index >= cache.size ()) {
        return null;
      }

      return cache.get (index);
    }
  }

  /**
   * Get the index for the object. If the object is not present in the list
   * then add it to the list.
   * @param object The Object for which an index is required.
   * @return The index of the object, or -1 if the object is null.
   */
  public int getIndex (Object object) {
    if (object == null) {
      return -1;
    }

    synchronized (cache) {
      int count = cache.size ();
      for (int i = 0; i < count; i += 1) {
        Object o = cache.get (i);
        if (o.equals (object)) {
          return i;
        }
      }

      cache.add (object);
      return count;
    }
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
