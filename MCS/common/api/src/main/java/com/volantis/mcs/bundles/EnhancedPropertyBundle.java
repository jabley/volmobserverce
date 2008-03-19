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
 * $Header: /src/voyager/com/volantis/mcs/bundles/EnhancedPropertyBundle.java,v 1.3 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Oct-00    Paul            Created.
 * 14-Mar-02    Allan           VBM:2002030615 - Added getKeys(). Fixed up 
 *                              imports.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bundles;

import java.io.InputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

/**
 * An implementation of EnhancedBundle based on a PropertyResourceBundle.
 */
public class EnhancedPropertyBundle
  extends AbstractEnhancedBundle {

  private static String mark = "(c) Volantis Systems Ltd 2000. ";

  private PropertyResourceBundle propertyBundle;
  private String resourceName;

  public EnhancedPropertyBundle (InputStream inputStream,
                                 String resourceName)
    throws IOException {

    propertyBundle = new PropertyResourceBundle (inputStream);
    this.resourceName = resourceName;
  }

  protected Object handleGetObject (String key)
    throws MissingResourceException {
    return propertyBundle.getObject (key);
  }

  public String toString () {
    return "(" + super.toString () + " resourceName=" + resourceName + ")";
  }


    // javadoc inherited
    public Enumeration getKeys() {
        return propertyBundle.getKeys();
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
