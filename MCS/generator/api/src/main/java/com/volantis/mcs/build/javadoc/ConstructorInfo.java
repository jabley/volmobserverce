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
 * $Header: /src/voyager/com/volantis/mcs/build/doclets/ConstructorInfo.java,v 1.3 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Added a list of parameters to
 *                              the constructor which does not take a
 *                              ConstructorDoc.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.javadoc;

import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.Parameter;
import com.volantis.mcs.build.javadoc.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains information about a constructor in an object which
 * is used for generating code.
 */
public class ConstructorInfo {

  /**
   * The class to which this object belongs.
   */
  private ClassInfo classInfo;

  /**
   * The underlying JavaDoc object.
   */
  private ConstructorDoc constructorDoc;

  /**
   * The list of the parameters (represented by ParameterInfo objects)
   * of the constructor which this object represents.
   */
  private List parameters;

  /**
   * Create a new <code>ConstructorInfo</code>.
   * @param classInfo The class information for the class which the constructor
   * which this object represents belongs.
   * @param constructorDoc The underlying JavaDoc object representing a class.
   */
  public ConstructorInfo (ClassInfo classInfo, ConstructorDoc constructorDoc) {
    this.classInfo = classInfo;
    this.constructorDoc = constructorDoc;

    parameters = new ArrayList ();
    Parameter [] parametersDoc = constructorDoc.parameters ();
    for (int i = 0; i < parametersDoc.length; i += 1) {
      parameters.add (new ParameterInfo (classInfo, parametersDoc [i]));
    }
  }

  /**
   * Create a new <code>ConstructorInfo</code>.
   */
  public ConstructorInfo (List parameters) {
    this.parameters = parameters;
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
   * Get the list of parameters.
   * @return The list of parameters.
   */
  public List getParameters () {
    return parameters;
  }

  /**
   * Add the specified parameter to the list of parameters.
   * @param parameter The parameter to add.
   */
  public void addParameter (ParameterInfo parameter) {
    parameters.add (parameter);
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
