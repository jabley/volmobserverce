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
 * $Header: /src/voyager/com/volantis/mcs/build/doclets/ClassInfo.java,v 1.4 2002/03/25 11:02:46 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Removed the getInherited...
 *                              methods and added a flag to the associated
 *                              get... methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - modified the method
 *                              getGetterMethod to correctly compare type with
 *                              boolean.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;

import com.volantis.mcs.build.GenerateUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class contains information about a class which is used
 * for generating code.
 */
public class ClassInfo {

  /**
   * The list of the fields (represented by FieldInfo objects) in this class.
   */
  private List fields;

  /**
   * A map from field name to FieldInfo.
   */
  private Map fieldMap;

  /**
   * The list of the methods (represented by MethodInfo objects) in this class.
   */
  private List methods;

  /**
   * A map from method name to MethodInfo.
   */
  private Map methodMap;

  /**
   * The underlying JavaDoc object.
   */
  private ClassDoc classDoc;

  /**
   * The package information for this class.
   */
  private PackageInfo packageInfo;

  /**
   * The information for the base class of this class, may be null.
   */
  private ClassInfo baseClassInfo;

  /**
   * The unqualified name of the class this object represents.
   */
  private String name;
 
  /**
   * The qualified name of the class this object represents.
   */
  private String qualifiedName;

  /**
   * The list of the constructors of the class this object represents.
   */
  private List constructors;

  /**
   * Create a new <code>ClassInfo</code>.
   */
  private ClassInfo () {

    fieldMap = new HashMap ();
    fields = new ArrayList ();

    methodMap = new HashMap ();
    methods = new ArrayList ();

    constructors = new ArrayList ();
  }

  /**
   * Create a new <code>ClassInfo</code>.
   * @param classDoc The underlying JavaDoc object representing a class.
   */
  public ClassInfo (ClassDoc classDoc) {
    this ();

    this.classDoc = classDoc;

    packageInfo = new PackageInfo (classDoc.containingPackage ());

    ClassDoc baseClassDoc = classDoc.superclass ();
    if (baseClassDoc != null) {
      baseClassInfo = new ClassInfo (baseClassDoc);
    }

    this.name = classDoc.name ();
    this.qualifiedName = classDoc.qualifiedName ();

    FieldDoc [] fieldsDoc = classDoc.fields ();
    for (int i = 0; i < fieldsDoc.length; i += 1) {
      FieldInfo field = new FieldInfo (this, fieldsDoc [i]);
      addField (field);
    }

    MethodDoc [] methodsDoc = classDoc.methods ();
    for (int i = 0; i < methodsDoc.length; i += 1) {
      MethodInfo method = new MethodInfo (this, methodsDoc [i]);
      addMethod (method);
    }

    ConstructorDoc [] constructorsDoc = classDoc.constructors ();
    for (int i = 0; i < constructorsDoc.length; i += 1) {
      constructors.add (new ConstructorInfo (this, constructorsDoc [i]));
    }
  }

  /**
   * Create a new <code>ClassInfo</code>.
   * @param packageInfo The package which contains the class this object
   * represents.
   * @param baseClassInfo The base class of this class, may be null.
   * @param name The unqualified name of the class this object represents.
   * @param qualifiedName The qualified name of the class this object
   * represents.
   */
  public ClassInfo (PackageInfo packageInfo,
                    ClassInfo baseClassInfo,
                    String name,
                    String qualifiedName) {

    this ();

    this.packageInfo = packageInfo;
    this.baseClassInfo = baseClassInfo;
    this.name = name;
    this.qualifiedName = qualifiedName;
  }

  /**
   * Get the package information associated with the class this object
   * represents.
   * @return The package information.
   */
  public PackageInfo getPackageInfo () {
    return packageInfo;
  }

  /**
   * Add the specified constructor to the list of constructors.
   * @param constructor The constructor to add.
   */
  public void addConstructor (ConstructorInfo constructor) {
    constructors.add (constructor);
  }

  /**
   * Get the list of constructors associated with the class this object
   * represents.
   * @return The list of constructors.
   */
  public List getConstructors () {
    return constructors;
  }

  /**
   * Add the specified field to the list of fields.
   * @param field The field to add.
   */
  public void addField (FieldInfo field) {
    fields.add (field);
    fieldMap.put (field.getName (), field);
  }

  /**
   * Get the list of fields associated with the class this object
   * represents.
   * @return The list of fields.
   */
  public List getFields () {
    return fields;
  }

  /**
   * Get the list of all the instance fields (including inherited ones)
   * associated with the class this object represents.
   * @return The list of fields.
   */
  public List getAllInstanceFields () {
    List fields = new ArrayList ();
    ClassInfo classInfo = this;
    
    while (classInfo != null) {
      int index = 0;
      for (Iterator i = classInfo.getFields ().iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();

        // Ignore static fields.
        if (field.isStatic ()) {
          continue;
        }
        
        fields.add (index, field);
        index += 1;
      }

      classInfo = classInfo.getBaseClassInfo ();
    }

    return fields;
  }

  /**
   * Get the field with the specified name.
   * @param fieldName The name of the field.
   * @param searchBaseClasses True if the method should search the base
   * classes and false otherwise.
   * @return The specified field, or null if it could not be found.
   */
  public FieldInfo getField (String fieldName,
                             boolean searchBaseClasses) {

    if (searchBaseClasses) {
      ClassInfo classInfo = this;
      while (classInfo != null) {
        FieldInfo fieldInfo = classInfo.getField (fieldName, false);
        if (fieldInfo != null) {
          return fieldInfo;
        }
        
        classInfo = classInfo.getBaseClassInfo ();
      }

      return null;
    }

    return (FieldInfo) fieldMap.get (fieldName);
  }

  /**
   * Add the specified method to the list of methods.
   * @param method The method to add.
   */
  public void addMethod (MethodInfo method) {
    //System.out.println ("Adding method " + method.getName ()
    //+ " to " + this);
    methods.add (method);
    methodMap.put (method.getName (), method);
  }

  /**
   * Get the list of methods associated with the class this object
   * represents.
   * @return The list of methods.
   */
  public List getMethods () {
    return methods;
  }

  /**
   * Get the method with the specified name.
   * @param methodName The name of the method.
   * @param searchBaseClasses True if the method should search the base
   * classes and false otherwise.
   * @return The specified method, or null if it could not be found.
   */
  public MethodInfo getMethod (String methodName,
                               boolean searchBaseClasses) {

    if (searchBaseClasses) {
      ClassInfo classInfo = this;
      while (classInfo != null) {
        MethodInfo methodInfo = classInfo.getMethod (methodName, false);
        if (methodInfo != null) {
          //System.out.println ("Found method named " + methodName
          //+ " in " + classInfo);
          return methodInfo;
        }
        //System.out.println ("Did not find method named " + methodName
        //+ " in " + classInfo);

        classInfo = classInfo.getBaseClassInfo ();
      }

      return null;
    }

    return (MethodInfo) methodMap.get (methodName);
  }

  /**
   * Get the information about the base class of the class this object
   * represents.
   * @return The information about the base class.
   */
  public ClassInfo getBaseClassInfo () {
    return baseClassInfo;
  }

  /**
   * Get the name of the class this object represents.
   * @return The name.
   */
  public String getName () {
    return name;
  }

  /**
   * Get the qualified name of the class this object represents.
   * @return The name.
   */
  public String getQualifiedName () {
    return qualifiedName;
  }

  /**
   * Get the information about the getter method for the specified field.
   * @param fieldName The name of the field whose getter method is to be
   * retrieved.
   * @param searchBaseClasses True if the method should search the base
   * classes and false otherwise.
   * @return The method information, or null if it could not be found.
   */
  public MethodInfo getGetterMethod (String fieldName,
                                     boolean searchBaseClasses) {
    FieldInfo fieldInfo = getField (fieldName, searchBaseClasses);
    if (fieldInfo == null) {
      //System.out.println ("Could not find field " + fieldName);
      return null;
    }

    // Try get first.
    String titled = GenerateUtilities.getTitledString (fieldName);
    String getterMethodName = "get" + titled;

    // Get the getter method.
    MethodInfo methodInfo = getMethod (getterMethodName, searchBaseClasses);
    if (methodInfo != null) {
      return methodInfo;
    }

    // If the field is boolean then try an is method.
    if (fieldInfo.getTypeName().equals ("boolean")) {
      getterMethodName = "is" + titled;
      methodInfo = getMethod (getterMethodName, searchBaseClasses);
    }

    return methodInfo;
  }

  /**
   * Get the information about the setter method for the specified field.
   * @param fieldName The name of the field whose setter method is to be
   * retrieved.
   * @param searchBaseClasses True if the method should search the base
   * classes and false otherwise.
   * @return The method information, or null if it could not be found.
   */
  public MethodInfo getSetterMethod (String fieldName,
                                     boolean searchBaseClasses) {
    FieldInfo fieldInfo = getField (fieldName, searchBaseClasses);
    if (fieldInfo == null) {
      return null;
    }

    // Try get first.
    String titled = GenerateUtilities.getTitledString (fieldName);
    String setterMethodName = "set" + titled;

    // Get the setter method.
    MethodInfo methodInfo = getMethod (setterMethodName, searchBaseClasses);
    return methodInfo;
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
