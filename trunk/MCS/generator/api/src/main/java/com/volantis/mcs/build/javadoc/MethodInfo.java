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
 * $Header: /src/voyager/com/volantis/mcs/build/doclets/MethodInfo.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
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

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Parameter;
import com.volantis.mcs.build.javadoc.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains information about a method in an object which is used
 * for generating code.
 */
public class MethodInfo {

  /**
   * The class to which this object belongs.
   */
  private ClassInfo classInfo;

  /**
   * The name of the method this object represents.
   */
  private String name;

  /**
   * The return type of the method this object represents.
   */
  private TypeInfo returnType;

  /**
   * The comment associated with the method this object represents.
   */
  private String comment;

  /**
   * The underlying JavaDoc object.
   */
  private MethodDoc methodDoc;

  /**
   * The list of the parameters (represented by ParameterInfo objects) in
   * this method.
   */
  private List parameters;

  /**
   * A map from parameter name to ParameterInfo.
   */
  private Map parameterMap;

  /**
   * Flag which indicates whether this method is inherited from another class.
   */
  //private boolean inherited;

  /**
   * Create a new <code>MethodInfo</code>.
   */
  private MethodInfo () {
    parameterMap = new HashMap ();
    parameters = new ArrayList ();
  }

  /**
   * Create a new <code>MethodInfo</code>.
   * @param classInfo The class information for the class which the method
   * which this object represents belongs.
   * @param methodDoc The underlying JavaDoc object representing a class.
   */
  public MethodInfo (ClassInfo classInfo, MethodDoc methodDoc) {
    this ();

    this.classInfo = classInfo;
    this.methodDoc = methodDoc;

    this.name = methodDoc.name ();
    this.returnType = new TypeInfo (methodDoc.returnType ());
    this.comment = methodDoc.getRawCommentText ();

    Parameter [] parametersDoc = methodDoc.parameters ();
    for (int i = 0; i < parametersDoc.length; i += 1) {
      addParameter (new ParameterInfo (classInfo, parametersDoc [i]));
    }
  }

  /**
   * Create a new <code>MethodInfo</code>.
   * @param name The name of the method which this object represents.
   * @param returnType The return type of the method which this object
   * represents.
   * @param comment The comment associated with the method which this object
   * represents.
   */
  public MethodInfo (String name,
                     TypeInfo returnType,
                     String comment) {
    this ();

    this.name = name;
    this.returnType = returnType;
    this.comment = comment;
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
   * Add the specified parameter to the list of parameters.
   * @param parameter The parameter to add.
   */
  public void addParameter (ParameterInfo parameter) {
    parameters.add (parameter);
    parameterMap.put (parameter.getName (), parameter);
  }

  /**
   * Get the list of parameters.
   * @return The list of parameters.
   */
  public List getParameters () {
    return parameters;
  }

  /**
   * Get the parameter with the specified name.
   * @param parameterName The name of the parameter.
   * @return The specified parameter, or null if it could not be found.
   */
  public ParameterInfo getParameter (String parameterName) {
    return (ParameterInfo) parameterMap.get (parameterName);
  }

  /**
   * Get the name of the method this object represents.
   * @return The name.
   */
  public String getName () {
    return name;
  }

  /**
   * Get the return type of the method this object represents.
   * @return The return type.
   */
  public TypeInfo getReturnType () {
    return returnType;
  }

  /**
   * Get the name of the return type (including dimension) of the method
   * this object represents.
   * @return The name of the return type.
   */
  public String getReturnTypeName () {
    return returnType.getName ();
  }

  /**
   * Get the comment associated wth the method this object represents.
   * @return The comment.
   */
  public String getComment () {
    return comment;
  }

  /*
  public void setInherited (boolean inherited) {
    this.inherited = inherited;
  }

  public boolean isInherited () {
    return inherited;
  }
  */
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
