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
 * $Header: /src/voyager/com/volantis/mcs/build/doclets/TypeInfo.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.javadoc;

import com.sun.javadoc.Type;

/**
 * This class contains information about a type in an object which is used
 * for generating code.
 */
public class TypeInfo {

  /**
   * The name of the type (including dimension) this object represents.
   */
  private String name;

  /**
   * Create a new <code>TypeInfo</code>.
   * @param typeDoc The underlying JavaDoc object representing a class.
   */
  public TypeInfo (Type typeDoc) {

    // Get the unqualified type with dimension.
    name = typeDoc.typeName ();
    String dimension = typeDoc.dimension ();
    if (dimension.length () != 0) {
      name += " " + dimension;
    }
  }
    
  /**
   * Get the name of the type (including dimension) this object represents.
   * @return The name of the type.
   */
  public String getName () {
    return name;
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

 02-Sep-03	1295/2	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
