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
 * $Header: /src/voyager/com/volantis/mcs/bundles/MultiplexedBundle.java,v 1.4 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *  2-Nov-00    Paul            Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 14-Mar-02    Allan           VBM:2002030615 - Added getKeys().
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bundles;

import java.util.Enumeration;
import java.util.MissingResourceException;

/**
 * A MultiplexedBundle is an EnhancedBundle implementation which forwards calls
 * on to a list of EnhancedBundles until either the resource is found, or all
 * of them have been tried.
 *
 * This is a very useful class as it makes it very easy to supply a set of
 * default resources for a class which can be overridden by a user of that
 * class.
 */
public class MultiplexedBundle
  implements EnhancedBundle {

  private static String mark = "(c) Volantis Systems Ltd 2000. ";

  private EnhancedBundle [] bundles;

  /**
   * Create a MultiplexedBundle.
   * @param bundle The bundle to which all requests are forwarded.
   * @param prefix The string to prefix the key with. If it does not end with a
   * "." then one is added.
   */
  public MultiplexedBundle () {
  }

  /**
   * Create a MultiplexedBundle.
   * @param bundle The default bundle.
   */
  public MultiplexedBundle (EnhancedBundle bundle) {
    if (bundle != null) {
      addBundle (bundle);
    }
  }

  /**
   * Create a MultiplexedBundle.
   * @param bundle1 The default bundle.
   * @param bundle2 The overriding bundle.
   */
  public MultiplexedBundle (EnhancedBundle bundle1, EnhancedBundle bundle2) {
    if (bundle1 != null) {
      addBundle (bundle1);
    }
    if (bundle2 != null) {
      addBundle (bundle2);
    }
  }

  /**
   * Add a bundle at the head of the bundles. It will be searched before all
   * the existing bundles and so can override any resources in them.
   */
  public void addBundle (EnhancedBundle bundle) {
    if (bundles == null) {
      bundles = new EnhancedBundle [1];
    } else {
      EnhancedBundle [] newBundles = new EnhancedBundle [bundles.length + 1];
      System.arraycopy (bundles, 0, newBundles, 1, bundles.length);
      bundles = newBundles;
    }

    bundles [0] = bundle;
  }

  // --------------------------------------------------------------------------
  // Implementation of EnhancedBundle interface.
  // --------------------------------------------------------------------------

  public Object getObject (String key)
    throws MissingResourceException {

    // Iterate through the bundles until we find one which has the resource.
    for (int i = 0; i < bundles.length; i += 1) {
      try {
        return bundles [i].getObject (key);
      }
      catch (MissingResourceException mre) {
      }
    }

    // The resource is missing.
    throw new MissingResourceException ("Missing",
                                        MultiplexedBundle.class.getName (),
                                        key);
  }

  public String getString (String key)
    throws MissingResourceException {

    return (String) getObject (key);
  }

  public Object getObject (String key, Object defaultValue) {

    // Iterate through the bundles until we find one which has the resource.
    for (int i = 0; i < bundles.length; i += 1) {
      try {
        return bundles [i].getObject (key);
      }
      catch (MissingResourceException mre) {
      }
    }

    return defaultValue;
  }

  public Object getObject (String key, Class type)
    throws MissingResourceException {

    // Iterate through the bundles until we find one which has the resource.
    for (int i = 0; i < bundles.length; i += 1) {
      try {
        return bundles [i].getObject (key, type);
      }
      catch (MissingResourceException mre) {
      }
    }

    // The resource is missing.
    throw new MissingResourceException ("Missing",
                                        MultiplexedBundle.class.getName (),
                                        key);
  }

  public Object getObject (String key, Class type, Object defaultValue) {

    // Iterate through the bundles until we find one which has the resource.
    for (int i = 0; i < bundles.length; i += 1) {
      try {
        return bundles [i].getObject (key, type);
      }
      catch (MissingResourceException mre) {
      }
    }

    return defaultValue;
  }

  public String getString (String key, String defaultValue) {
    return (String) getObject (key, defaultValue);
  }

  public String getStringOrWarning (String key) {
    String s = (String) getString (key, null);
    if (s == null) {
      s = "missing key '" + key + "' in " + this;
      //s = "missing key '" + key + "'";
    }
    return s;
  }

  public String getStringOrKey (String key) {
    return (String) getObject (key, key);
  }

  public String formatMessage (String key, Object [] arguments) {
    String message = getStringOrKey (key);
    return java.text.MessageFormat.format (message, arguments);
  }

    /**
     * This implementation of getKeys() assumes that every bundle in this
     * MultiplexedBundle contains at least one element.
     */
    // rest of javadoc inherited
    public Enumeration getKeys() {
        return new Enumeration() {
                int bundle = 0;
                Enumeration current = bundles[0].getKeys();

                public boolean hasMoreElements() {
                    return current.hasMoreElements() ||
                        bundle+1 < bundles.length;
                }

                public Object nextElement() {
                    if(current.hasMoreElements()) {
                        return current.nextElement();
                    }

                    // No more elements in the current enumeration so
                    // see if there is another enumeration
                    bundle++;
                    if(bundle < bundles.length) {
                        current = bundles[bundle].getKeys();
                        return current.nextElement();
                    } else {
                        // No more bundles
                        return null;
                    }
                }
            };
    }

  // --------------------------------------------------------------------------
  // End of implementation of EnhancedBundle interface.
  // --------------------------------------------------------------------------

  public String toString () {
    StringBuffer buffer = new StringBuffer ();
    buffer.append ("(").append (super.toString ());
    if (bundles != null) {
      for (int i = 0; i < bundles.length; i += 1) {
        buffer
          .append (" bundle ")
          .append (i)
          .append ("=")
          .append (bundles [i]);
      }
    }
    buffer.append (")");

    return buffer.toString ();
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
