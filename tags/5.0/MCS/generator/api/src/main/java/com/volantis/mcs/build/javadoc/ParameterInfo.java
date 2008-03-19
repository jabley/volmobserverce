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
 * $Header: /src/voyager/com/volantis/mcs/build/doclets/ParameterInfo.java,v 1.3 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Added a new constructor which
 *                              does not require a Parameter parameter.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.javadoc;

import com.sun.javadoc.Parameter;
import com.volantis.mcs.build.javadoc.ClassInfo;

/**
 * This class contains information about a parameter in an object which is used
 * for generating code.
 */
public class ParameterInfo {

  /**
   * The class to which this object belongs.
   */
  private ClassInfo classInfo;

  /**
   * The name of the parameter this object represents.
   */
  private String name;

  /**
   * The type of the parameter this object represents.
   */
  private TypeInfo type;

  /**
   * The underlying JavaDoc object.
   */
  private Parameter parameter;

  /**
   * Create a new <code>ParameterInfo</code>.
   * @param classInfo The class information for the class which the parameter
   * which this object represents belongs.
   * @param parameterDoc The underlying JavaDoc object representing a class.
   */
  public ParameterInfo (ClassInfo classInfo, Parameter parameter) {
    this.classInfo = classInfo;
    this.parameter = parameter;

    this.name = parameter.name ();
    this.type = new TypeInfo (parameter.type ());
  }

  public ParameterInfo (ClassInfo classInfo, String name, TypeInfo type) {
    this.classInfo = classInfo;
    this.name = name;
    this.type = type;
  }

  /**
   * Set the class information of the class which the constructor
   * which this object represents belongs.
   * @param classInfo The class information for the class which the constructor
   * which this object represents belongs.
   */
  public void setClassInfo (ClassInfo classInfo) {
    this.classInfo = classInfo;
  }

  /**
   * Get the name of the parameter this object represents.
   * @return The name.
   */
  public String getName () {
    return name;
  }

  /**
   * Get the type of the parameter this object represents.
   * @return The type.
   */
  public TypeInfo getType () {
    return type;
  }

  /**
   * Get the name of the type (including dimension) of the parameter
   * this object represents.
   * @return The name of the type.
   */
  public String getTypeName () {
    return type.getName ();
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
