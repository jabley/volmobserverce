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
 * $Header: /src/voyager/com/volantis/mcs/bundles/PrefixedBundle.java,v 1.3 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *  2-Nov-00    Paul            Created.
 * 14-Mar-02    Allan           VBM:2002030615 - Added getKeys().
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bundles;

import java.util.Enumeration;
import java.util.MissingResourceException;

/**
 * A PrefixedBundle is an EnhancedBundle wrapper which forwards calls on to
 * the wrapped EnhancedBundle after first prefixing the key with its prefix.
 * <p>
 * This is a very useful class as it makes it very easy to build a hierarchy
 * of reusable components which can be easily translated.
 * <p>
 * Assume that you have a component A, which contains two identical components
 * G1 and G2, which each contain three components Q, R and S and all of these
 * components have strings which need to be internationalised.
 * <p>
 * The component hierarchy looks something like this.
 * <pre>
 *                    A
 *
 *          G1                  G2
 *
 *      Q   R   S           Q   R   S
 * </pre>
 * With the help of a PrefixedBundle you can create a very similar hierarchy of
 * bundles.
 * <pre>
 *   EnhancedBundle bundle;
 *   getStringOrKey (bundle, "name");
 *   ComponentA a = createComponentA (new PrefixedBundle ("A", bundle));
 *
 *   public ComponentA createComponentA (EnhancedBundle bundle) {
 *     getStringOrKey (bundle, "name");
 *     ComponentG g1 = createComponentX (new PrefixedBundle ("G1", bundle));
 *     ComponentG g2 = createComponentY (new PrefixedBundle ("G2", bundle));
 *   }
 *
 *   public ComponentG createComponentG (EnhancedBundle bundle) {
 *     getStringOrKey (bundle, "name");
 *     ComponentQ q = createComponentQ (new PrefixedBundle ("Q", bundle));
 *     ComponentR r = createComponentR (new PrefixedBundle ("R", bundle));
 *     ComponentS s = createComponentS (new PrefixedBundle ("S", bundle));
 *   }
 *
 *   public ComponentQ createComponentQ (EnhancedBundle bundle) {
 *     getStringOrKey (bundle, "name");
 *   }
 *
 *   public ComponentR createComponentR (EnhancedBundle bundle) {
 *     getStringOrKey (bundle, "name");
 *   }
 *
 *   public ComponentS createComponentS (EnhancedBundle bundle) {
 *     getStringOrKey (bundle, "name");
 *   }
 *
 * </pre>
 * The above code requires a properties file similar to the following.
 * <pre>
 *    name = top
 *    A.name = A
 *    A.G1.name = G1
 *    A.G1.Q.name = Q1
 *    A.G1.R.name = R1
 *    A.G1.S.name = S1
 *    A.G2.name = G2
 *    A.G2.Q.name = Q2
 *    A.G2.R.name = R2
 *    A.G2.S.name = S2
 * </pre>
 * If the code was used with above then the name hierarchy would look something
 * like this.
 * <pre>
 *                    A
 *
 *          G1                  G2
 *
 *      Q1  R1  S1          Q2  R2  S2
 * </pre>
 * <p>
 * One big advantage of this is that it allows a component to look very
 * different depending on where it is being used.
 * <p>
 * This is also one of the problems with this approach in that it requires that
 * all properties must be set for every instance of the component which has a
 * different prefixed bundle but this problem can be solved by providing a set
 * of default properties.
 */
public class PrefixedBundle
  implements EnhancedBundle {

  private static String mark = "(c) Volantis Systems Ltd 2000. ";

  protected EnhancedBundle bundle;
  private String prefix;

  /**
   * Create a PrefixedBundle which forwards requests to the specified bundle
   * after prefixing with the specified prefix.
   * @param bundle The bundle to which all requests are forwarded.
   * @param prefix The string to prefix the key with. If it does not end with a
   * "." then one is added.
   */
  public PrefixedBundle (EnhancedBundle bundle, String prefix) {
    this (prefix, bundle);
  }

  /**
   * Create a PrefixedBundle which forwards requests to the specified bundle
   * after prefixing with the specified prefix.
   * @param prefix The string to prefix the key with. If it does not end with a
   * "." then one is added.
   * @param bundle The bundle to which all requests are forwarded.
   */
  public PrefixedBundle (String prefix, EnhancedBundle bundle) {
    this.bundle = bundle;
    if (prefix.endsWith (".")) {
      this.prefix = prefix.substring (0, prefix.length () - 2);
    }
    else {
      this.prefix = prefix;
    }
  }

  /**
   * Get the bundle which this object forwards request to.
   * @return The EnhancedBundle which this object forwards requests to.
   */
  public EnhancedBundle getBundle () {
    return bundle;
  }

  /**
   * Get the string with which the key is prefixed.
   * @return The string with which the key is prefixed.
   */
  public String getPrefix () {
    return prefix;
  }

  // --------------------------------------------------------------------------
  // Implementation of EnhancedBundle interface.
  // --------------------------------------------------------------------------

  public Object getObject (String key)
    throws MissingResourceException {

    return bundle.getObject (getPrefixedKey (key));
  }

  public String getString (String key)
    throws MissingResourceException {

    return (String) getObject (key);
  }

  public Object getObject (String key, Object defaultValue) {

    return bundle.getObject (getPrefixedKey (key), defaultValue);
  }

  public Object getObject (String key, Class type)
    throws MissingResourceException {

    return bundle.getObject (getPrefixedKey (key), type);
  }

  public Object getObject (String key, Class type, Object defaultValue)
    throws MissingResourceException {

    return bundle.getObject (getPrefixedKey (key), type, defaultValue);
  }

  public String getString (String key, String defaultValue) {
    return (String) getObject (key, defaultValue);
  }

  public String getStringOrWarning (String key) {

    return bundle.getStringOrWarning (getPrefixedKey (key));
  }

  public String getStringOrKey (String key) {

    return bundle.getStringOrKey (getPrefixedKey (key));
  }

  public String formatMessage (String key, Object [] arguments) {

    return bundle.formatMessage (getPrefixedKey (key), arguments);
  }

    /**
     * Return an enumeration of the keys of the bundle with the prefix
     * removed.
     * @return an Enumeration of the bundle keys without the prefix.
     */
    public Enumeration getKeys() {
        final Enumeration bundleKeys = bundle.getKeys();

        return new Enumeration() {
                private Object next;

                private void setNextElement() {
                    String key = (String) bundleKeys.nextElement();
                    while(bundleKeys.hasMoreElements() && 
                          key.indexOf(prefix) == -1) {

                        key = (String) bundleKeys.nextElement();
                    }

                    next = bundleKeys.hasMoreElements() ? key : null;
                }

                public boolean hasMoreElements() {
                    if(!bundleKeys.hasMoreElements()) {
                        return false;
                    }

                    setNextElement();
                    
                    return next != null;
                }

                public Object nextElement() {
                    if(next == null) {
                        setNextElement();
                    }

                    if(next == null) {
                        return null;
                    }

                    String key = (String) next;
                    return key.substring(prefix.length()+1);
                }
            };
    }

  // --------------------------------------------------------------------------
  // End of implementation of EnhancedBundle interface.
  // --------------------------------------------------------------------------

  protected String getPrefixedKey (String key) {
    String prefixedKey;
    if (key==null || key.length () == 0) {
      prefixedKey = prefix;
    } else if (key.charAt (0) == '/') {
      prefixedKey = prefix + key;
    } else {
      prefixedKey = prefix + "." + key;
    }

    return prefixedKey;
  }

  public String toString () {
    StringBuffer buffer = new StringBuffer ();
    buffer
      .append ("(")
      .append (super.toString ())
      .append (" prefix=\"")
      .append (prefix)
      .append ("\" bundle=")
      .append (bundle)
      .append (")");

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
