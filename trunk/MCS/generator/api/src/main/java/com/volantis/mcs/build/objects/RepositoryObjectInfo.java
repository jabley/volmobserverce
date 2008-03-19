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
 * $Header: /src/voyager/com/volantis/mcs/build/objects/RepositoryObjectInfo.java,v 1.7 2002/12/04 10:31:16 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Added support for determining
 *                              the dependent and guardian objects of a
 *                              repository object from the annotations.
 * 27-Feb-02    Doug            VBM:2002011405 - Modified the method
 *                              insertRevision so that the Fields are checked
 *                              to see if the revision field already exists.
 * 04-Mar-02    Adrian          VBM:2002021908 - identityField now created by
 *                              calling FieldInfo with new constructor taking
 *                              objectField as a parameter to ensure that all
 *                              of the instance variables are correctly copied.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 17-May-02    Paul            VBM:2002050101 - Used new javadoc tags and
 *                              allow annotation to add extra imports to the
 *                              accessors.
 * 28-Nov-02    Mat             VBM:2002112213 - Added 
 *                              mariner-object-base-identity-class tag so that 
 *                              the correct base identity class can be found.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.objects;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

import com.volantis.mcs.build.javadoc.ClassInfo;
import com.volantis.mcs.build.javadoc.ConstructorInfo;
import com.volantis.mcs.build.javadoc.FieldInfo;
import com.volantis.mcs.build.javadoc.MethodInfo;
import com.volantis.mcs.build.javadoc.ParameterInfo;
import com.volantis.mcs.build.GenerateUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

/**
 * This class contains information about a repository object which is used
 * when automatically generating code for it.
 */
public class RepositoryObjectInfo {

  /** 
   * The field info for the project field.
   */
  private FieldInfo projectIdentityField = null;

    private static Map instances = new HashMap ();

  public static RepositoryObjectInfo getInstance (RootDoc rootDoc,
                                                  ClassDoc classDoc) {
    String className = classDoc.qualifiedName ();
    RepositoryObjectInfo info
      = (RepositoryObjectInfo) instances.get (className);
    if (info == null) {
      info = new RepositoryObjectInfo (rootDoc, classDoc);
    }
    
    return info;
  }

  /**
   * Information about the RepositoryObject class's guardian class.
   */
  private RepositoryObjectInfo guardianInfo;

  /**
   * Information about the RepositoryObject class.
   */
  private ClassInfo objectClassInfo;

  /**
   * Information about the RepositoryObjectIdentity class which will be used
   * as the base class of the RepositoryObjectIdentity class generated for the
   * RepositoryObject.
   */
  private ClassInfo identityBaseClassInfo;

  /**
   * Information about the RepositoryObjectIdentity which is to be generated.
   */
  private ClassInfo identityClassInfo;

  /**
   * A list of all the fields which uniquely identity the object, some
   * will have been inherited from the base class.
   */
  private List allIdentityFields;

  /**
   * A list of all the fields of the object, some
   * will have been inherited from the base class.
   */
  private List allObjectFields;

  /**
   * A list of all the fields of the object, minus the identity fields. Some
   * will have been inherited from the base class.
   */
  private List extraObjectFields;

  /**
   * A list of all the fields of the identity, minus the identity fields of
   * the guardian. Some will have been inherited from the base class.
   */
  private List extraIdentityFields;

  /**
   * A list of RepositoryObjectInfo instances for the dependent classes.
   */
  private List dependentsInfo;

  /**
   * A set of names of the required fields.
   */
  private Set requiredFields;

  /**
   * A set of names of the fields where an empty string can be output if the 
   * field value is null..
   * This fields are different from required fields in that required fields 
   * that have null values are illegal.  emptyStringFields can be required
   * but can be set to "" if null.
   */
  private Set emptyStringFields;

  /**
   * A list of any extra imports needed by the accessors.
   */
  private List accessorImports;

    /**
     * A list of any fields to remove before persisting to xml.
     */
    private Set ignoreXmlAttributeFields;

    /**
     * The name of the xml element associated with the class of repository
     * object.
     */
    private String objectElementName;

    /**
     * A list of any API inclusion and exclusion tags.
     */
    private List replicateTags;

    /**
     * If true then there are separate accessors for the different dependent
     * objects, otherwise there is a single accessor for all dependent
     * accessors.
     */
    private boolean hasSeparateAccessors;

  /**
   * Create a new <code>RepositoryObjectInfo</code>.
   * @param rootDoc The root of the JavaDoc parse.
   * @param objectClassDoc The JavaDoc object for the RepositoryObject class.
   */
  protected RepositoryObjectInfo (RootDoc rootDoc,
                                  ClassDoc objectClassDoc) {
    
    //this.rootDoc = rootDoc;

    // Add this immediately to prevent loops.
    String qualifiedClassName = objectClassDoc.qualifiedName ();
    instances.put (qualifiedClassName, this);

    objectClassInfo = new ClassInfo (objectClassDoc);

    ClassDoc identityBaseClassDoc;
    Tag [] tags = objectClassDoc.tags ("mariner-object-base-identity-class");
    // If a base identity class has been specified, try to use it
    // otherwise search for one.
    if(tags.length == 1) {
        String baseIdentityClassName = tags[0].text();
        identityBaseClassDoc = rootDoc.classNamed (baseIdentityClassName);
        if(identityBaseClassDoc == null) {
            throw new IllegalStateException("Cannot find base identity class" +
                baseIdentityClassName);
        }
    } else {
        identityBaseClassDoc = getIdentityBaseClassDoc (rootDoc,
                                                             objectClassDoc);
    }
    
    System.out.println("Using base class " + identityBaseClassDoc.qualifiedName());
    identityBaseClassInfo = new ClassInfo (identityBaseClassDoc);

    // Create a ClassInfo for the identity class.
    String identityClassName = objectClassInfo.getName () + "Identity";
    String identityClassQualifiedName 
      = objectClassInfo.getQualifiedName () + "Identity";

    identityClassInfo = new ClassInfo (objectClassInfo.getPackageInfo (),
                                       identityBaseClassInfo,
                                       identityClassName,
                                       identityClassQualifiedName);

    allIdentityFields = new ArrayList ();
    // Find out whether this object has any dependencies.
    tags = objectClassDoc.tags ("mariner-object-guardian");
    if (tags.length == 1) {
      String guardianClass = tags [0].text ();
      ClassDoc guardianClassDoc = rootDoc.classNamed (guardianClass);
      if (guardianClassDoc == null) {
        throw new IllegalStateException ("Cannot find guardian class "
                                         + guardianClass);
      }
      guardianInfo = RepositoryObjectInfo.getInstance (rootDoc,
                                                       guardianClassDoc);
    } else if (tags.length > 1) {
      throw new IllegalStateException ("Class can only have one"
                                       + " mariner-object-guardian");
    }

    // Create an array of the new fields in the class.
    tags = objectClassDoc.tags ("mariner-object-identity-field");
    int identityFieldCount = tags.length;

    for (int i = 0; i < identityFieldCount; i += 1) {

      // Get the field name.
      String fieldName = tags [i].text ();

      FieldInfo identityField;

      // If it is inherited from the base class then ignore it.
      identityField = identityBaseClassInfo.getField (fieldName, true); 
      
      if (identityField != null) {
        if (GenerateUtilities.isProjectField(identityField)) {
            projectIdentityField = identityField;
        }
        // Add it to the list of all the identity fields.
        allIdentityFields.add (identityField);
        continue;
      }

      // Get the details of the object's field.
      FieldInfo objectField = objectClassInfo.getField (fieldName, true);

      String comment = objectField.getComment ();
      comment = getConstructorParameterComment (objectClassDoc,
                                                fieldName,
                                                comment);

      // Create a field for the identity class.
      identityField = new FieldInfo (objectField, comment);

      // Add it to the class.
      identityClassInfo.addField (identityField);

      // Add it to the list of all the identity fields.
      allIdentityFields.add (identityField);

      // Get the details of the object's getter method.
      MethodInfo objectGetter = objectClassInfo.getGetterMethod (fieldName,
                                                                 true);
      MethodInfo identityGetter
        = new MethodInfo (objectGetter.getName (),
                          objectGetter.getReturnType (),
                          objectGetter.getComment ());

      // Add it to the class.
      identityClassInfo.addMethod (identityGetter);
    }

      // Create the list of parameters for the constructor.
    List constructorParameters = new ArrayList ();
    for (Iterator i = allIdentityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();

      ParameterInfo parameter = new ParameterInfo (field.getClassInfo (),
                                                   field.getName (),
                                                   field.getType ());
      constructorParameters.add (parameter);
    }
    
    ConstructorInfo constructor = new ConstructorInfo (constructorParameters);
    identityClassInfo.addConstructor (constructor);

    // Get the list of extra identity fields.
    if (guardianInfo == null) {
      extraIdentityFields = new ArrayList (allIdentityFields);
    } else {
      ClassInfo guardianIdentityClassInfo
        = guardianInfo.getIdentityClassInfo ();

      extraIdentityFields = getExtraFields (allIdentityFields,
                                            guardianIdentityClassInfo);
    }

    // Create the set of the names of fields to ignore.
    Set ignore = new HashSet ();
    ignore.add ("identity");

    tags = objectClassDoc.tags ("mariner-object-ignore-field");
    for (int i = 0; i < tags.length; i += 1) {
      //System.out.println ("Field " + tags [i].text ()
      //+ " should be ignored");
      ignore.add (tags [i].text ());
    }

    // Initialise the list of object fields.
    allObjectFields = objectClassInfo.getAllInstanceFields ();
    for (Iterator i = allObjectFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();
      if (ignore.contains (fieldName)) {
        //System.out.println ("Ignoring " + fieldName);
        i.remove ();
      }
    }

    extraObjectFields = getExtraFields (allObjectFields, identityClassInfo);

    dependentsInfo = new ArrayList ();
    tags = objectClassDoc.tags ("mariner-object-dependent");
    for (int i = 0; i < tags.length; i += 1) {
      String dependentClassName = tags [i].text ();
      ClassDoc dependentClassDoc = rootDoc.classNamed (dependentClassName);
      if (dependentClassDoc == null) {
        throw new IllegalStateException ("Cannot find class named "
                                         + dependentClassName);
      }

      RepositoryObjectInfo dependentInfo = getInstance (rootDoc,
                                                        dependentClassDoc);
      dependentsInfo.add (dependentInfo);
    }

      tags = objectClassDoc.tags("mariner-separate-accessors");
      if (tags.length == 0) {
          hasSeparateAccessors = false;
      } else if (tags.length == 1) {
          hasSeparateAccessors = true;
      } else {
          throw new IllegalStateException ("Class can only have one"
                                           + " mariner-separate-accessors");
      }

    requiredFields = new HashSet ();
    tags = objectClassDoc.tags ("mariner-object-required-field");
    for (int i = 0; i < tags.length; i += 1) {
      String fieldName = tags [i].text ();
      requiredFields.add (fieldName);
    }

    // All the identity fields are also required.
    for (Iterator i = allIdentityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName();
      
      requiredFields.add (fieldName);
    }


    // List of fields that are output as empty strings if null
    emptyStringFields = new HashSet ();
    tags = objectClassDoc.tags ("mariner-object-null-is-empty-string-field");
    for (int i = 0; i < tags.length; i += 1) {
      String fieldName = tags [i].text ();
      emptyStringFields.add (fieldName);
    }


    // Get any extra imports needed by the accessors
    tags = objectClassDoc.tags ("mariner-generate-accessor-import");
    accessorImports = new ArrayList ();
    for (int i = 0; i < tags.length; i += 1) {
      String importName = tags [i].text ();
      
      accessorImports.add (importName);
    }

      // find any xml attributes to ignore (in this class or parents)
      ignoreXmlAttributeFields = new HashSet();
      recursivelyGetTags( objectClassDoc, "mariner-ignore-xml-attribute",
                          ignoreXmlAttributeFields);

      // Get the element name for this repository object if available
      tags = objectClassDoc.tags("mariner-object-element-name");
      if(tags!=null && tags.length>0) {
          objectElementName = tags[0].text();
      }

      // Add all the tags that should be replicated from the original class
      // onto the generated class
      replicateTags = new ArrayList();

      replicateTags.addAll(Arrays.asList(objectClassDoc.
                                         tags("volantis-api-include-in")));

      replicateTags.addAll(Arrays.asList(objectClassDoc.
                                         tags("volantis-api-exclude-from")));
      replicateTags.addAll(Arrays.asList(objectClassDoc.tags("deprecated")));
  }

    protected void recursivelyGetTags(
            ClassDoc doc,
            String tagName,
            Set fieldHash) {

        // recurse through parent classes first
        ClassDoc baseClassDoc = doc.superclass();
        if (baseClassDoc != null) {
            recursivelyGetTags( baseClassDoc, tagName, fieldHash );
        }

        // get the documentation for this class
        Tag [] tags = doc.tags("mariner-ignore-xml-attribute");
        for (int i = 0; i < tags.length; i += 1) {
          String fieldName = tags [i].text ();
          fieldHash.add (fieldName);
        }
    }

    /**
     * Get the set of tags that should be replicated to the generated code.
     *
     * @return the list of tags that should be replicated to the generated code
     */
    public List getReplicateTags() {
        return replicateTags;
    }

  /**
   * Get the information about the guardian repository object, may be null.
   * @return The information about the guardian class.
   */
  public RepositoryObjectInfo getGuardianInfo () {
    return guardianInfo;
  }

  /**
   * Get the information about the identity class.
   * @return The information about the identity class.
   */
  public ClassInfo getIdentityClassInfo () {
    return identityClassInfo;
  }

  /**
   * Get the information about the object class.
   * @return The information about the object class.
   */
  public ClassInfo getObjectClassInfo () {
    return objectClassInfo;
  }

  /**
   * Get a list of all the fields which uniquely identify the object.
   * @return A list of all the fields which uniquely identify the object.
   */
  public List getAllIdentityFields () {
    return allIdentityFields;
  }

  /**
   * Get a list of all the fields of the identity minus any identity fields
   * from the guardian identity if any.
   * @return A list of all the fields of the identity.
   */
  public List getExtraIdentityFields () {
    return extraIdentityFields;
  }

  /**
   * Get a list of all the fields of the object.
   * @return A list of all the fields of the object.
   */
  public List getAllObjectFields () {
    return allObjectFields;
  }

  /**
   * Get a list of all the fields of the object minus any identity fields.
   * @return A list of all the fields of the object.
   */
  public List getExtraObjectFields () {
    return extraObjectFields;
  }

  public static List getExtraFields (List fields, ClassInfo classInfo) {

    if (classInfo == null) {
      return fields;
    }

    List extraFields = new ArrayList ();
    for (Iterator i = fields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      if (classInfo.getField (field.getName (), true) == null) {
        extraFields.add (field);
      }
    }

    return extraFields;
  }

  /**
   * Get a list of the RepositoryObjectInfo instance for the classes which are
   * dependent on the object represented by this object.
   * @return A list of the RepositoryObjectInfo instances for the dependent
   * classes.
   */
  public List getDependentsInfo () {
    return dependentsInfo;
  }

    public boolean hasSeparateAccessors() {
        return hasSeparateAccessors;
    }

  /**
   * Return true if the field is to be changed to an empty string if its 
   * value is null.
   * @param fieldName The name of the field.
   * @return True if the field can be the empty string and false otherwise.
   */
  public boolean isEmptyStringField (String fieldName) {
    return emptyStringFields.contains (fieldName);
  }

    /**
     * Return true if the field is to be cremoved from the xml document.
     * @param fieldName The name of the field.
     * @return True if the field should be removed.
     */
    public boolean isIgnoreXmlAttribute (String fieldName) {
      return ignoreXmlAttributeFields.contains (fieldName);
    }

  /**
   * Return true if the field is required and false otherwise.
   * @param fieldName The name of the field.
   * @return True if the field is required and false otherwise.
   */
  public boolean isFieldRequired (String fieldName) {
    return requiredFields.contains (fieldName);
  }

  /**
   * Find the base class to use for the identity. This involves searching
   * through the base classes of the object until we find one which has an
   * identity class.
   */
  private ClassDoc getIdentityBaseClassDoc (RootDoc rootDoc,
                                            ClassDoc objectClassDoc) {
    
    ClassDoc objectBaseClassDoc = objectClassDoc.superclass ();
    while (objectBaseClassDoc != null) {

      String qualifiedObjectBaseClassName
        = objectBaseClassDoc.qualifiedName ();
      String qualifiedIdentityBaseClassName
        = qualifiedObjectBaseClassName + "Identity";

      //System.out.println ("Trying " + qualifiedIdentityBaseClassName);
    
      ClassDoc identityBaseClassDoc
        = rootDoc.classNamed (qualifiedIdentityBaseClassName);
      
      if (identityBaseClassDoc != null) {
        return identityBaseClassDoc;
      }

      objectBaseClassDoc = objectBaseClassDoc.superclass ();
    }
    
    throw new IllegalStateException ("Could not find base identity class for "
                                     + objectClassDoc.qualifiedName ());
  }

  /**
   * Get the comment associated with the constructor parameter.
   * @return The comment associated with the constructor parameter, or the
   * default value if none could be found.
   */
  public String getConstructorParameterComment (ClassDoc classDoc,
                                                String parameterName,
                                                String defaultComment) {

    ConstructorDoc [] constructorDocs = classDoc.constructors ();

    for (int i = 0; i < constructorDocs.length; i += 1) {

      ConstructorDoc constructorDoc = constructorDocs [i];
      ParamTag [] parameterTags = constructorDoc.paramTags ();

      // Check to see whether the current constructor has javadoc for the
      // parameter which matches this field.
      for (int t = 0; t < parameterTags.length; t += 1) {
        ParamTag tag = parameterTags [t];
        String name = tag.parameterName ();
        if (parameterName.equals (name)) {
          return tag.parameterComment ();
        }
      }
    }

    if (defaultComment == null) {
      return ("The value of the " + parameterName + " property of the "
              + classDoc.name ());
    }
    
    return defaultComment;
  }

  public List getAccessorImports () {
    return accessorImports;
  }

  public boolean insertRevision () {
    return (objectClassInfo.getField("revision", true)==null);
  }

    /**
     * @return The object xml element name.
     */
    public String getObjectElementName() {
        return objectElementName;
    }
    /**
     * Get the project identity field.
     * 
     * @return The project identity field.
     */
    public FieldInfo getProjectIdentityField() {
        return projectIdentityField;
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

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 18-Apr-05	7715/1	philws	VBM:2005040402 Port Public API generation changes from 3.3

 15-Apr-05	7676/1	philws	VBM:2005040402 Public API corrections and IBM Public API documentation subset generation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Feb-04	2878/1	claire	VBM:2004020514 Refactor code generation for Projects

 05-Feb-04	2851/1	mat	VBM:2004020404 Amended code generators to fix the import/export

 12-Jan-04	2532/3	andy	VBM:2004010903 removed redundant commented out code

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
