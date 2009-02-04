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
 * $Header: /src/voyager/com/volantis/mcs/bundles/EnhancedBundle.java,v 1.3 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Oct-00    Paul            Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 04-Apr-02    Allan           VBM:2002030615 - Added getKeys().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bundles;

import java.util.Enumeration;
import java.util.Map;
import java.util.MissingResourceException;

/**
 * An EnhancedBundle is very similar to a ResourceBundle except that it is an
 * interface rather than a class with final methods. This makes it much more
 * flexible and easy to use. It also has methods for getting more data types
 * and will cache different data types.
 */
public interface EnhancedBundle {

    /**
     * Get an Enumeration of the keys in the bundle.
     * @return an Enumeration of the keys in the bundle
     */
    public Enumeration getKeys();

  /**
   * Get the object associated with the key.
   * @param key The key to use to search for the object.
   * @return Object associated with the key.
   * @throws MissingResourceException if the key could not be found.
   */
  public Object getObject (String key)
    throws MissingResourceException;

  /**
   * Get a string associated with the key. It is a helper function built around
   * {@link #getObject (String)}.
   * @param key The key to use to search for the object.
   * @return String associated with the key.
   * @see #getObject (String)
   * @throws MissingResourceException if the key could not be found.
   */
  public String getString (String key)
    throws MissingResourceException;

  /**
   * Get the object associated with the key, or if it could not be found
   * then return the default object.
   * @param key The key to use to search for the object.
   * @param defaultValue The value to return if the key could not be found.
   * @return Object associated with the key, or the default value.
   */
  public Object getObject (String key, Object defaultValue);

  /**
   * Get the object of the specified type associated with the key.
   * @param key The key to use to search for the object.
   * @param type The type of object to return.
   * @throws MissingResourceException if the key could not be found.
   */
  public Object getObject (String key, Class type)
    throws MissingResourceException;

  /**
   * Get the object of the specified type associated with the key,
   * or if it could not be found then return the default object.
   * @param key The key to use to search for the object.
   * @param type The type of object to return.
   * @param defaultValue The value to return if the key could not be found.
   */
  public Object getObject (String key, Class type, Object defaultValue);

  /**
   * Get the string associated with the key, or if it could not be found
   * then return the default string.
   * @param key The key to use to search for the object.
   * @param defaultValue The string to return if the key could not be found.
   * @return String associated with the key, or the default value.
   */
  public String getString (String key, String defaultValue);

  /**
   * Get the string associated with the key, or if it could not be found
   * then return a warning string which indicates what key was being searched
   * for.
   * @param key The key to use to search for the object.
   * @return String associated with the key, or a warning.
   */
  public String getStringOrWarning (String key);

  /**
   * Get the string associated with the key, or if it could not be found
   * then simply return the key.
   * @param key The key to use to search for the object.
   * @return String associated with the key, or the key.
   */
  public String getStringOrKey (String key);

  /**
   * Get the string associated with the key and use it as a message to
   * format with the array of arguments.
   * @param key The key to use to search for the string.
   * @param arguments The arguments to use.
   * @return Formatted string or the key.
   */
  public String formatMessage (String key, Object [] arguments);

    /**
     * Format a message using a Map of properties that map property values
     * to a property format.
     * @param pattern the pattern for the format
     * @param properties the Map of properties containing the values to be
     * inserted into the pattern to produce the formatted message
     * @return the formatted message derived from the pattern and properties
     */
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
