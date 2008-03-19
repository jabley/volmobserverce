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
 * This code was automatically generated by PropertyValueLookupUtilities
 * on 3/14/08 7:12 PM
 *
 * YOU MUST NOT MODIFY THIS FILE
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.components;

import java.util.HashMap;
import java.util.Map;

public class LinkComponentUtilities {

  /**
   * An array of the allowable values for the abstract cacheable repository
   * object cacheThisPolicy.
   */
  private static Object [] cacheThisPolicyArray;

  /**
   * An array of the allowable values for the abstract cacheable repository
   * object retainDuringRetry.
   */
  private static Object [] retainDuringRetryArray;

  /**
   * An array of the allowable values for the abstract cacheable repository
   * object retryFailedRetrieval.
   */
  private static Object [] retryFailedRetrievalArray;

  static {
    Object internal;
    String external;

    cacheThisPolicyArray = new Boolean [] {
      new Boolean ("false"),
      new Boolean ("true"),
    };

    retainDuringRetryArray = new Boolean [] {
      new Boolean ("false"),
      new Boolean ("true"),
    };

    retryFailedRetrievalArray = new Boolean [] {
      new Boolean ("false"),
      new Boolean ("true"),
    };
  }

  public static Object [] getCacheThisPolicyArray () {
    return cacheThisPolicyArray;
  }

  public static Object [] getRetainDuringRetryArray () {
    return retainDuringRetryArray;
  }

  public static Object [] getRetryFailedRetrievalArray () {
    return retryFailedRetrievalArray;
  }
}
