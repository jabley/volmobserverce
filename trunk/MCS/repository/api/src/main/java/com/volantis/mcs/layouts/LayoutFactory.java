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
 * $Header: /src/voyager/com/volantis/mcs/layouts/LayoutFactory.java,v 1.5 2002/12/09 15:48:39 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Jan-01    Paul            Created.
 * 09-Jul-01    Paul            VBM:2001062810 - Cleaned up.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.common.LayoutType;


/**
 * This interface defines the methods needed to create Layouts and Formats.
 *
 * There is a single method for creating a Layout.
 *
 * There are a few methods for creating a Format from its type stored in the
 * database and also to complete its initialisation after its attributes have
 * been set and also after its children have been created.
 *
 * The createDeviceLayout method is called once to create a Layout and
 * then the format methods are called multiple times to create the Formats
 * which are part of that Layout.
 *
 * Formats are read from the database as follows:
 *
 * <ol>
 * <li>The type of the root format is read.</il>
 * <li>The root format is instantiated by calling the createFormat method with a
 * parent of null and an index of -1.</li>
 * <li>The attributes of the root format are read and set.</li>
 * <li>The formatAttributesHaveBeenSet method is called which can do attribute
 * dependent initialisation</li>
 * <li>The types and index of the format's children are read.</li>
 * <li>Each child is created by calling the createFormat method which adds
 * the child into the hierarchy and then their attributes are set.</li>
 * <li>The childrenHaveBeenCreated method is called which can do children
 * dependent initialisation</li>
 * </ol>
 */
/**
 * This interface contains a single method which is called when a Layout object
 * is to be created.
 */
public interface LayoutFactory {

  /**
   * Create a device specific Layout depending on the type.
   * @param type Type of Layout to create.
   * @return The device specific Layout.
   */
  public Layout createDeviceLayout (LayoutType type)
    throws LayoutException;

  /**
   * Create different Format objects depending on the type. This method is
   * reponsible for instantiating a new format object and adding it to its
   * parent.
   *
   * @param type The type of Format object to create.
   * @param parent The parent format object, or null if this is the root format.
   * @param index The index of the Format object within the parent.
   * @return The new Format or null if the type was not recognised.
   */
  public Format createFormat (FormatType type, Format parent, int index)
    throws LayoutException;

  /**
   * Do any attribute dependent initialisation.
   * @param format A Format returned by the create method.
   */
  public void formatAttributesHaveBeenSet (Format format)
    throws LayoutException;

  /**
   * Do any children dependent initialisation.
   * @param format A Format returned by the create method.
   */
  public void formatChildrenHaveBeenCreated (Format format)
    throws LayoutException;
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
