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
 * $Header: /src/voyager/com/volantis/mcs/bundles/AbstractEnhancedBundle.java,v 1.5 2002/11/25 14:51:49 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Oct-00    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Added support for
 *                              MessageFormat.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class.
 * 04-Apr-02    Allan           VBM:2002030615 - Added getKeys(). 
 * 25-Nov-02    Payal           VBM:2002111804 - Modified getImageIcon() to set
 *                              the description for the icon.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bundles;

import java.awt.*;

import java.io.*;

import java.net.*;

import java.text.MessageFormat;

import java.util.*;

import javax.swing.*;

/**
 * Abstract implmentation of EnhancedBundle.
 */
public abstract class AbstractEnhancedBundle
  implements EnhancedBundle {

  private static String mark = "(c) Volantis Systems Ltd 2000. ";

  protected EnhancedBundle parent;

  private Map imageCache;
  private Map iconCache;
  private Map formatCache;

  public AbstractEnhancedBundle () {
  }

  public void setParent (EnhancedBundle bundle) {
    this.parent = parent;
  }

  public Object getObject (String key)
    throws MissingResourceException {

    Object value;
    try {
      value = handleGetObject (key);
    }
    catch (MissingResourceException mre) {
      // If there is no parent bundle then rethrow the exception.
      if (parent == null) {
        throw mre;
      }

      // Get the value out of the parent, if the resource is missing then
      // it will throw an error.
      value = parent.getObject (key);
    }

    return value;
  }

  public String getString (String key)
    throws MissingResourceException {

    return (String) getObject (key);
  }

  public Object getObject (String key, Object defaultValue) {
    try {
      return getObject (key);
    }
    catch (MissingResourceException e) {
      return defaultValue;
    }
  }

  public Object getObject (String key, Class type)
    throws MissingResourceException {

    Object result;

    if (type == Image.class) {
      result = getImage (key);
    } else if (type == ImageIcon.class) {
      result = getImageIcon (key);
    } else if (type == MessageFormat.class) {
      result = getMessageFormat (key);
    } else {
      result = null;
    }

    if (result == null) {

      // If there is no parent bundle then throw an exception.
      if (parent == null) {
        throw new MissingResourceException ("Missing",
                                            getClass ().getName (),
                                            key);
      }

      // Get the value out of the parent, if the resource is missing then
      // it will throw an error.
      result = parent.getObject (key, type);
    }

    return result;
  }

  public Object getObject (String key, Class type, Object defaultValue) {
    try {
      return getObject (key, type);
    }
    catch (MissingResourceException e) {
      return defaultValue;
    }
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
    MessageFormat format = getMessageFormat (key);
    return format.format (arguments);
  }

  protected MessageFormat getMessageFormat (String key) {

    MessageFormat format = null;

    if (formatCache != null) {
      format = (MessageFormat) formatCache.get (key);
      if (format != null) {
        return format;
      }
    }
    else {
      formatCache = new HashMap ();
    }

    String message = getStringOrKey (key);
    format = new MessageFormat (message);
    formatCache.put (key, format);

    return format;
  }

  protected Image getImage (String key) {

    Image image = null;

    if (imageCache != null) {
      image = (Image) imageCache.get (key);
      if (image != null) {
        return image;
      }
    }
    else {
      imageCache = new HashMap ();
    }

    Toolkit toolkit = Toolkit.getDefaultToolkit ();
    String name = getString (key, null);

    if (name != null) {
      URL url = ClassLoader.getSystemResource (name);
      if (url != null) {
        image = toolkit.createImage (url);
        if (image != null) {
          imageCache.put (key, image);
        }
      }
    }

    return image;
  }

  protected ImageIcon getImageIcon (String key) {
      ImageIcon icon = (ImageIcon) findIcon (key);
      if (icon == null) {
          Image image = getImage (key + "/image");
          if (image != null) {
              icon = new ImageIcon (image);
              icon.setDescription(getString(key + "/text", null));
          }
      }
      return icon;
  }

  protected abstract Object handleGetObject (String key)
    throws MissingResourceException;

  protected Icon findIcon (String key) {
    if (iconCache == null) {
      return null;
    }

    return (Icon) iconCache.get (key);
  }

  protected void addIcon (String key, Icon icon) {
    if (iconCache == null) {
      iconCache = new HashMap ();
    }

    iconCache.put (key, icon);
  }

    // javadoc inherited
    public abstract Enumeration getKeys();
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
