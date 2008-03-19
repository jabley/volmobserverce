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
 * $Header: /src/voyager/com/volantis/mcs/build/objects/AccessorCodeGenerator.java,v 1.10 2002/09/10 09:37:16 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Feb-02    Paul            VBM:2001122103 - Created to be used to contain
 *                              code common to classes which generate
 *                              accessors.
 * 08-Mar-02    Paul            VBM:2002030804 - Improved formatting of
 *                              copy method.
 * 18-Mar-02    Ian             VBM:2002031203 - Change call to writeLogger in 
 *                              generateAccessor to pass package name. 
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 08-May-02    Paul            VBM:2002050305 - Use the constructor which
 *                              takes an identity when copying an object.
 * 17-May-02    Paul            VBM:2002050101 - Added extra imports.
 * 29-May-02    Paul            VBM:2002050301 - Added writeGetParentIdentity
 *                              method.
 * 11-Jul-02    Mat             VBM:2002040825 - Made a writeLogger method 
                                instead of just calling the GenerateUtilities 
 *                              version to allow subclasses to override it.
 *                              Also added removeChildrenImpl methods.
 * 01-Aug-02    Phil W-S        VBM:2002080113 - Renamed any retrieveObjects,
 *                              retrieveObjectsImpl, renameObjects,
 *                              renameObjectsImpl and removeObjects methods,
 *                              replacing "Objects" with "Children".
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.objects;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.javadoc.ClassInfo;
import com.volantis.mcs.build.javadoc.ConstructorInfo;
import com.volantis.mcs.build.javadoc.FieldInfo;
import com.volantis.mcs.build.javadoc.MethodInfo;
import com.volantis.mcs.build.javadoc.ParameterInfo;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class is the base class for those classes which generate repository
 * accessors.
 */
public abstract class AccessorCodeGenerator {

  /**
   * The base directory.
   */
  protected File baseDir;

  /**
   * The directory into which the generated code will be written.
   */
  protected File generatedDir;

  /**
   * The information associated with the object.
   */
  protected RepositoryObjectInfo info;

  /**
   * The information associated with the object class.
   */
  protected ClassInfo objectClassInfo;

  /**
   * The name of object class.
   */
  protected String objectClassName;

  /**
   * A list of all the fields associated with the object class.
   */
  protected List allObjectFields;

  /**
   * A list of all the fields of the object, minus the identity fields. Some
   * will have been inherited from the base class.
   */
  protected List extraObjectFields;

  /**
   * The information associated with the object's identity class.
   */
  protected ClassInfo identityClassInfo;

  /**
   * The name of the identity class.
   */
  protected String identityClassName;

  /**
   * A list of all the fields which uniquely identity the object, some
   * will have been inherited from the base class.
   */
  protected List allIdentityFields;

  /**
   * A list of all the fields of the identity, minus the identity fields of
   * the guardian. Some will have been inherited from the base class.
   */
  protected List extraIdentityFields;

  /**
   * The information associated with the object's guardian object.
   */
  protected RepositoryObjectInfo guardianInfo;

  /**
   * The information associated with the object's guardian object class.
   */
  protected ClassInfo guardianObjectClassInfo;

  /**
   * The name of the object's guardian object class.
   */
  protected String guardianObjectClassName;

  /**
   * The information associated with the object's guardian object's identity
   * class.
   */
  protected ClassInfo guardianIdentityClassInfo;

  /**
   * The name of the object's guardian object's identity
   * class.
   */
  protected String guardianIdentityClassName;

  /**
   * A list of all the fields which uniquely identity the object's
   * guardian object.
   */
  protected List allGuardianIdentityFields;

  /**
   * The PrintStream to write to.
   */
  protected PrintStream out;

  /**
   * The name of the accessor's package.
   */
  protected String accessorPackage;

  /**
   * The name of the accessor.
   */
  protected String accessorName;

  /**
   * Create a new <code>AccessorCodeGenerator</code>.
   * @param generatedDir The directory into which the code should be generated.
   */
  public AccessorCodeGenerator (File generatedDir) {
    this.generatedDir = generatedDir;
  }

  /**
   * This writes a single line of getResolvedFieldName
   * @param fieldName A String representing the field name which we want the resolved
   * version of
   *
   * String exampleField = resolveField(jdbcConnection, "EXAMPLE");
   *
   */
  protected void writeGetResolvedFieldName(String fieldName) {
    out.println("    " + "String " + fieldName.toLowerCase() + "Field"
        + " = resolveFieldName(jdbcConnection, \"" + fieldName.toUpperCase() + "\");");
  }

  /**
   * Write code which creates variables which resolve short field names used by
   * the writeQueryString method
   * @param queryFields The fields to use as the query, may be empty.
   */
  protected void writeGetResolvedFieldNamesForQueryString(List queryFields, ArrayList resolvedFields) {
    if (queryFields.size() > 0) {
      out.println();
      boolean resolvedFieldAdded = false; // used for adding a comment line if necessary
      for (Iterator i = queryFields.iterator(); i.hasNext();) {
        // get the name of the new field
        FieldInfo newField = (FieldInfo) i.next();
        String fieldName = newField.getName();
        // find out it if it has already been resolved
        boolean alreadyExists = false;
        if (resolvedFields != null) {
          for (int index = 0; index < resolvedFields.size(); index++) {
            FieldInfo existingField = (FieldInfo) resolvedFields.get(index);
            if (existingField.getName().equalsIgnoreCase(fieldName)) {
              alreadyExists = true;
              break;
            }
          }
        }
        // if not then resolve it
        if (alreadyExists == false) {
          // add a comment if this is the fist field added
          if (resolvedFieldAdded == false) {
            out.println("    // get resolved field names for query string ");
          }
          writeGetResolvedFieldName(fieldName);
          resolvedFieldAdded = true;
        }
      }
    }
  }

  /**
   * Open a file relative to the generated directory.
   * @param qualifiedClassName The fully qualified class name.
   * @return A PrintStream.
   */
  protected PrintStream openFileForClass (String qualifiedClassName) {
    return GenerateUtilities.openFileForClass (generatedDir,
                                               qualifiedClassName);
  }

  /**
   * Generate an accessor for the specified repository object.
   * @param info The information about the repository object.
   */
  public void generate (RepositoryObjectInfo info) {

    this.info = info;

    objectClassInfo = info.getObjectClassInfo ();
    objectClassName = objectClassInfo.getName ();
    allObjectFields = info.getAllObjectFields ();
    extraObjectFields = info.getExtraObjectFields ();

    identityClassInfo = info.getIdentityClassInfo ();
    identityClassName = identityClassInfo.getName ();
    allIdentityFields = info.getAllIdentityFields ();
    extraIdentityFields = info.getExtraIdentityFields ();

    guardianInfo = info.getGuardianInfo ();

    if (guardianInfo == null) {
      guardianObjectClassInfo = null;
      guardianObjectClassName = null;
      guardianIdentityClassInfo = null;
      guardianIdentityClassName = null;
      allGuardianIdentityFields = null;
    } else {
      guardianObjectClassInfo = guardianInfo.getObjectClassInfo ();
      guardianObjectClassName = guardianObjectClassInfo.getName ();
      guardianIdentityClassInfo = guardianInfo.getIdentityClassInfo ();
      guardianIdentityClassName = guardianIdentityClassInfo.getName ();
      allGuardianIdentityFields = guardianInfo.getAllIdentityFields ();
    }

    generateAccessor ();
  }

  /**
   * Generate an accessor for the current repository object.
   */
  protected void generateAccessor () {

    accessorPackage = getAccessorPackage ();
    accessorName = getAccessorName (info);

    String accessorQualifiedName
      = accessorPackage + "." + accessorName;

    // Open a file.
    out = openFileForClass (accessorQualifiedName);

    // Write the header.
    GenerateUtilities.writeHeader (out, this.getClass().getName());

    // Write the package declaration.
    out.println ();
    out.println ("package " + accessorPackage + ";");

    // Write the imports.
    SortedSet imports = new TreeSet ();
    imports.add ("com.volantis.mcs.accessors.AbstractCachingAccessor");

    imports.add ("com.volantis.mcs.objects.RepositoryObject");
    imports.add ("com.volantis.mcs.objects.RepositoryObjectIdentity");

    imports.add ("com.volantis.mcs.repository.RepositoryConnection");
    imports.add ("com.volantis.mcs.repository.RepositoryEnumeration");
    imports.add ("com.volantis.mcs.repository.RepositoryException");

    imports.add ("java.util.ArrayList");
    imports.add ("java.util.Collection");
    imports.add ("java.util.Collections");
    imports.add ("java.util.List");

    imports.add("com.volantis.synergetics.localization.ExceptionLocalizer");
    imports.add("com.volantis.synergetics.log.LogDispatcher");
    imports.add("com.volantis.mcs.localization.LocalizationFactory");

    imports.add (objectClassInfo.getQualifiedName ());
    imports.add (identityClassInfo.getQualifiedName ());

    RepositoryObjectInfo guardianInfo = info.getGuardianInfo ();
    if (guardianInfo != null) {
      imports.add (guardianInfo.getIdentityClassInfo ().getQualifiedName ());
    }

    // Add any extra imports needed by accessors.
    imports.addAll (info.getAccessorImports ());

    addImports (imports);

    GenerateUtilities.writeImports (out, imports);

    writeClassHeader ();

    // Create the log4j object.
    writeLogger (out, accessorPackage, accessorName);

      // Create the exception localizer
      GenerateUtilities.writeExceptionLocalizer(out,
                                                accessorPackage,
                                                accessorName);

    // Declare static variables.
    writeDeclareStaticFields ();

    // Initialise static variables.
    out.println ();
    out.println ("  static {");

    writeInitialiseStaticFields ();

    out.println ("  }");

    writeDeclareInstanceFields ();

    writeConstructor ();

    writeGetSupportedClassMethod ();
    writeGetDependentAccessorsMethod ();

    writeUtilityMethods ();

    writeClassBody ();

    writeClassFooter ();

    // Close the output stream.
    out.close ();

    out = null;
  }

    /**
     * Subclasses override this to return the name of the accessor package.
     * @return The name of the accessor package.
     */
    protected abstract String getAccessorPackage ();

  /**
   * Subclasses override this to return the name of the accessor class for
   * the specified repository object within the accessor package.
   * @param info The repository object whose accessor is required.
   * @return The name of the accessor class.
   */
  protected abstract String getAccessorName (RepositoryObjectInfo info);

  /**
   * Subclasses override this to add extra imports.
   */
  protected void addImports (SortedSet imports) {
  }

  /**
   * Subclasses override this to write out the class header.
   */
  protected void writeClassHeader () {
  }

  /**
   * Subclasses override this to write out declarations for static fields.
   */
  protected void writeDeclareStaticFields () {
  }

  /**
   * Subclasses override this to write out the code which initialises static
   * fields.
   */
  protected void writeInitialiseStaticFields () {
  }

  /**
   * Subclasses override this to write out declarations for instance fields.
   */
  protected void writeDeclareInstanceFields () {
    out.println ();
    out.println ("  private Collection dependentAccessors;");

    if (guardianInfo != null) {
      out.println ("  private " + getAccessorName (guardianInfo)
                   + " parentAccessor;");
    }
  }

  /**
   * Subclasses override this to write out the constructor.
   */
  protected void writeConstructor () {

    GenerateUtilities.writeJavaDocComment 
      (out, "  ", "Create a new <code>" + accessorName + "</code>.");

    if (guardianInfo == null) {
      out.println ("  public " + accessorName + " () {");
    } else {
      String guardianAccessorName = getAccessorName (guardianInfo);

      out.println ("  public");
      out.println ("    " + accessorName
                   + " (" + guardianAccessorName + " parentAccessor) {");
      out.println ("    this.parentAccessor = parentAccessor;");
    }

    writeInitialiseInstanceFields ();

    out.println ("  }");

  }

  /**
   * Subclasses override this to write out the code which initialises the
   * instance fields.
   */
  protected void writeInitialiseInstanceFields () {

    List dependents = info.getDependentsInfo ();

    out.println ();
    if (!info.hasSeparateAccessors() || dependents.size () == 0) {
      out.println ("    dependentAccessors = Collections.EMPTY_LIST;");
    } else {
      out.println ("    dependentAccessors = new ArrayList ();");
      for (Iterator i = dependents.iterator (); i.hasNext ();) {
        RepositoryObjectInfo dependentInfo = (RepositoryObjectInfo) i.next ();
        String accessorName = getAccessorName (dependentInfo);
        out.println ("    dependentAccessors.add (new " + accessorName
                     + " (this));");
      }
    }
  }

  /**
   * Subclasses override this to write out any utility methods needed by the
   * accessor.
   */
  protected void writeUtilityMethods () {
  }

  /**
   * Write out the getSupportedClass method.
   */
  private void writeGetSupportedClassMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public Class getSupportedClass () {");
    out.println ("    return " + objectClassName + ".class;");
    out.println ("  }");
  }

  /**
   * Write out the getDependentAccessors method.
   */
  private void writeGetDependentAccessorsMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public Collection getDependentAccessors  () {");
    out.println ("    return dependentAccessors;");
    out.println ("  }");
  }

  /**
   * Write out the body of the class.
   */
  protected void writeClassBody () {
    writeAddObjectImplMethod ();
    writeUpdateObjectImplMethod ();
    writeRemoveObjectImplMethod ();
    writeRetrieveObjectImplMethod ();
    writeRenameObjectImplMethod ();
    writeCopyObjectMethod ();
    writeEnumerateObjectIdentitiesMethod ();
    writeRetrieveObjectIdentitiesMethod ();
    writeRemoveChildrenImplMethod();
    writeRetrieveChildrenImplMethod ();
    writeRenameChildrenImplMethod ();
    writeLockObjectMethod ();
    writeUnlockObjectMethod ();
  }

  /**
   * Write out the addObjectImpl method.
   */
  protected void writeAddObjectImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected void addObjectImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                "
                 + "RepositoryObject object)");
    out.println ("    throws RepositoryException {");

    // Write the code which casts the RepositoryObject to the correct type.
    //out.println ();
    //out.println ("    " + objectClassName + " o = (" + objectClassName
    //+ ") object;");

    writeAddObjectImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the addObjectImpl method.
   */
  protected void writeAddObjectImplBody () {
  }

  /**
   * Write out the updateObjectImpl method.
   */
  protected void writeUpdateObjectImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected void updateObjectImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                   "
                 + "RepositoryObject object)");
    out.println ("    throws RepositoryException {");

    // Write the code which casts the RepositoryObject to the correct type.
    //out.println ();
    //out.println ("    " + objectClassName + " o = (" + objectClassName
    //+ ") object;");

    writeUpdateObjectImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the updateObjectImpl method.
   */
  protected void writeUpdateObjectImplBody () {
  }

  /**
   * Write out the removeObjectImpl method.
   */
  protected void writeRemoveObjectImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected void removeObjectImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                   "
                 + "RepositoryObjectIdentity identity)");
    out.println ("    throws RepositoryException {");

    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    //out.println ();
    //out.println ("    " + identityClassName + " id = (" + identityClassName
    //+ ") identity;");

    writeRemoveObjectImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the removeObjectImpl method.
   */
  protected void writeRemoveObjectImplBody () {
  }
  
  /**
   * Write out the removeChildrenImpl method.
   */
  protected void writeRemoveChildrenImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected void removeChildrenImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                   "
                 + "RepositoryObjectIdentity parent)");
    out.println ("    throws RepositoryException {");

    writeRemoveChildrenImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the removeObjectImpl method.
   */
  protected void writeRemoveChildrenImplBody () {
  }

  /**
   * Write out the retrieveObjectImpl method.
   */
  protected void writeRetrieveObjectImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected");
    out.println ("    RepositoryObject retrieveObjectImpl "
                 + "(RepositoryConnection connection,");
    out.println ("                                        "
                 + " RepositoryObjectIdentity identity)");
    out.println ("    throws RepositoryException {");

    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    //out.println ();
    //out.println ("    " + identityClassName + " id = (" + identityClassName
    //+ ") identity;");

    writeRetrieveObjectImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the retrieveObjectImpl method.
   */
  protected void writeRetrieveObjectImplBody () {
  }

  /**
   * Write out the renameObjectImpl method.
   */
  protected void writeRenameObjectImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected void renameObjectImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                   "
                 + "RepositoryObjectIdentity identity,");
    out.println ("                                   "
                 + "String newName)");
    out.println ("    throws RepositoryException {");

    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    //out.println ();
    //out.println ("    " + identityClassName + " id = (" + identityClassName
    //+ ") identity;");

    writeRenameObjectImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the renameObjectImpl method.
   */
  protected void writeRenameObjectImplBody () {
  }

  /**
   * Write out the copyObject method.
   */
  protected void writeCopyObjectMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public void copyObject ("
                 + "RepositoryConnection connection,");
    out.println ("                          "
                 + "RepositoryObjectIdentity srcIdentity,");
    out.println ("                          "
                 + "RepositoryObjectIdentity dstIdentity)");
    out.println ("    throws RepositoryException {");

    writeCopyObjectBody ();
    
    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the copyObject method.
   */
  protected void writeCopyObjectBody () {
    // Retrieve the object.
    String indent = GenerateUtilities.getIndent (objectClassName.length ());
    out.println ();
    out.println ("    " + objectClassName + " original");
    out.println ("      = (" + objectClassName + ") retrieveObject ("
                 + "connection,");
    out.println ("         " + indent          + "                  "
                 + "srcIdentity);");
    
    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    out.println ();
    out.println ("    " + identityClassName + " dstId");
    out.println ("      = (" + identityClassName + ") dstIdentity;");

    // Create a copy of the object from the identity.
    out.println ();
    out.println ("    " + objectClassName + " copy");
    out.println ("      = new " + objectClassName + " (dstId);");

    // Initialise the non identity fields.
    out.println ();
    for (Iterator i = extraObjectFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();
      MethodInfo setter = objectClassInfo.getSetterMethod (fieldName, true);
      MethodInfo getter = objectClassInfo.getGetterMethod (fieldName, true);

      out.println ("    copy." + setter.getName () + " ("
                   + "original." + getter.getName () + " ());");
    }

    // Add the copy.
    out.println ();
    out.println ("    addObject (connection, copy);");
  }

  /**
   * Write out the enumerateObjectIdentities method.
   */
  protected void writeEnumerateObjectIdentitiesMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public RepositoryEnumeration enumerateObjectIdentities ("
                 + "RepositoryConnection connection,");
    out.println ("                                                          "
                 + "RepositoryObjectIdentity parent)");
    out.println ("    throws RepositoryException {");

    writeEnumerateObjectIdentitiesBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the enumerateObjectIdentities method.
   */
  protected void writeEnumerateObjectIdentitiesBody () {
  }

  /**
   * Write out the retrieveObjectIdentities method.
   */
  protected void writeRetrieveObjectIdentitiesMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public Collection retrieveObjectIdentities ("
                 + "RepositoryConnection connection,");
    out.println ("                                              "
                 + "RepositoryObjectIdentity parent)");
    out.println ("    throws RepositoryException {");

    writeRetrieveObjectIdentitiesBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the retrieveObjectIdentities method.
   */
  protected void writeRetrieveObjectIdentitiesBody () {
  }

  /**
   * Write out the retrieveChildrenImpl method.
   */
  protected void writeRetrieveChildrenImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public Collection retrieveChildrenImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                          "
                 + "RepositoryObjectIdentity parent)");
    out.println ("    throws RepositoryException {");

    writeRetrieveChildrenImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the retrieveChildrenImpl method.
   */
  protected void writeRetrieveChildrenImplBody () {
  }

  /**
   * Write out the renameChildrenImpl method.
   */
  protected void writeRenameChildrenImplMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  protected void renameChildrenImpl ("
                 + "RepositoryConnection connection,");
    out.println ("                                     "
                 + "RepositoryObjectIdentity srcParent,");
    out.println ("                                     "
                 + "RepositoryObjectIdentity dstParent)");
    out.println ("    throws RepositoryException {");

    writeRenameChildrenImplBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the renameChildrenImpl method.
   */
  protected void writeRenameChildrenImplBody () {
  }

  /**
   * Write out the lockObject method.
   */
  protected void writeLockObjectMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public void lockObject ("
                 + "RepositoryConnection connection,");
    out.println ("                          "
                 + "RepositoryObjectIdentity identity)");
    out.println ("    throws RepositoryException {");

    writeLockObjectBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the lockObject method.
   */
  protected void writeLockObjectBody () {
  }

  /**
   * Write out the unlockObject method.
   */
  protected void writeUnlockObjectMethod () {

    // Write the method declaration.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public void unlockObject ("
                 + "RepositoryConnection connection,");
    out.println ("                            "
                 + "RepositoryObjectIdentity identity)");
    out.println ("    throws RepositoryException {");

    writeUnlockObjectBody ();

    // Close the method.
    out.println ("  }");
  }

  /**
   * Write out the body of the unlockObject method.
   */
  protected void writeUnlockObjectBody () {
  }

  /**
   * Subclasses override this to write out the class footer.
   */
  protected void writeClassFooter () {
  }

  /**
   * Helper method to write out code which initialises an object from another
   * object.
   * @param indent The indent to use when outputting the code.
   * @param fields The fields which need to be initialised.
   * @param dstClassInfo The class information for the object being
   * initialised.
   * @param dstObjectName The name of the object being initialised.
   * @param srcClassInfo The class information for the object from which the
   * other object is being initialised.
   * @param srcObjectName The name of the object from which the other object
   * is being initialised.
   */
  protected void writeInitialiseObjectFromObject (String indent,
                                                  List fields,
                                                  ClassInfo dstClassInfo,
                                                  String dstObjectName,
                                                  ClassInfo srcClassInfo,
                                                  String srcObjectName) {

    if (fields == null || fields.size () == 0) {
      return;
    }

    // Initialise the object with the fields from the identity.
    for (Iterator i = fields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      if (!GenerateUtilities.isProjectField(field)) {
          String fieldName = field.getName ();
          MethodInfo setter = dstClassInfo.getSetterMethod (fieldName, true);
          MethodInfo getter = srcClassInfo.getGetterMethod (fieldName, true);

          //System.out.println ("Field name " + fieldName
          //+ " setter " + setter
          //+ " getter " + getter);

          out.println (indent + dstObjectName + "." + setter.getName () + " ("
                       + srcObjectName + "." + getter.getName () + " ());");
      } else {
          // Write the line that creates the project.
          out.println (indent + dstObjectName + ".setProject(" + srcObjectName +
          ".getProject());");
      }
    }
  }

  /**
   * Helper method to write out code which creates a new object with a
   * default constructor.
   * @param indent The indent to use when outputting the code.
   * @param classInfo The class information for the object being created.
   * @param objectName The name of the object being created.
   */
  protected void writeCreateNewObject (String indent,
                                       ClassInfo classInfo,
                                       String objectName) {
    
    String className = classInfo.getName ();

    String separator;
    if (className.length () + objectName.length () > 32) {
      separator = "\n" + indent + "  ";
    } else {
      separator = " ";
    }

    out.println ();
    out.println (indent + className + " " + objectName + separator
                 + "= new " + className + " ();");
  }

  /**
   * Helper method to initialise an object from a set of values stored in
   * some variables.
   * @param indent The indent to use when outputting the code.
   * @param fields The fields which need setting, the field name is the name
   * of the variable.
   * @param classInfo The class information for the object being created.
   * @param objectName The name of the object being created.
   */
  protected void writeInitialiseObjectFromValues (String indent,
                                                  List fields,
                                                  ClassInfo classInfo,
                                                  String objectName) {

    if (fields == null || fields.size () == 0) {
      return;
    }

    // Initialise the object with the fields from the identity.
    for (Iterator i = fields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();
      MethodInfo setter = classInfo.getSetterMethod (fieldName, true);

      //System.out.println ("Field name " + fieldName
      //+ " setter " + setter);

      out.println (indent + objectName + "." + setter.getName ()
                   + " (" + fieldName + ");");
    }
  }

  /**
   * Helper method to create an object from a set of values stored in
   * some variables.
   * @param indent The indent to use when outputting the code.
   * @param fields The fields which need setting, the field name is the name
   * of the variable.
   * @param classInfo The class information for the object being created.
   * @param objectName The name of the object being created.
   */
  protected void writeCreateObjectFromValues (String indent,
                                              List fields,
                                              ClassInfo classInfo,
                                              String objectName) {

    String className = classInfo.getName ();

    out.println (indent + className + " " + objectName);
    writeCreateObjectFromValues (indent + "  = ", fields, classInfo);
  }

  /**
   * Helper method to create an object from a set of values stored in
   * some variables, the generated code does not contain a variable declaration
   * for the object being created.
   * @param indent The indent to use when outputting the code.
   * @param fields The fields which need setting, the field name is the name
   * of the variable.
   * @param classInfo The class information for the object being created.
   */
  protected void writeCreateObjectFromValues (String indent,
                                              List fields,
                                              ClassInfo classInfo) {

    List constructors = classInfo.getConstructors ();
    if (constructors.size () != 1) {
      throw new IllegalStateException ("Must be one constructor");
    }

    ConstructorInfo constructorInfo = (ConstructorInfo) constructors.get (0);
    List parameters = constructorInfo.getParameters ();

    // If the number of parameters is not the same as the total number of
    // fields then error.
    if (parameters.size () != fields.size ()) {
      throw new IllegalStateException ("Not enough parameters in constructor"
                                       + " to initialise object");
    }

    String className = classInfo.getName ();
    String line = (indent + "new " + className + " (");
    out.print (line);

    indent = GenerateUtilities.getIndent (line.length ());

    Iterator p = parameters.iterator ();
    Iterator f = fields.iterator ();
    String separator = null;
    for (; p.hasNext ();) {
      ParameterInfo parameter = (ParameterInfo) p.next ();
      String parameterName = parameter.getName ();

      if (separator == null) {
        separator = indent;
      } else {
        out.print (separator);
      }

      FieldInfo field = (FieldInfo) f.next ();
      String fieldName = field.getName ();

      if (!fieldName.equals (parameterName)) {
        throw new IllegalStateException ("Parameter " + parameterName
                                         + " not the same as " + fieldName);
      }

      MethodInfo getter = classInfo.getGetterMethod (fieldName, true);
      out.print (fieldName);

      if (p.hasNext ()) {
        out.println (" , ");
      }
    }

    out.println (");");
  }

  /**
   * Helper method to initialise some variables from the fields of an object.
   * @param indent The indent to use when outputting the code.
   * @param fields The fields which need setting, the field name is the name
   * of the variable.
   * @param classInfo The class information for the object being created.
   * @param instanceName The name of the object from which the values are being
   * obtained.
   */
  protected void writeGetValuesFromObject (String indent,
                                           List fields,
                                           ClassInfo classInfo,
                                           String instanceName) {

    for (Iterator i = fields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();

      String separator;
      if (fieldName.length () > 12) {
        separator = "\n" + indent + "  ";
      } else {
        separator = " ";
      }

      MethodInfo getter = classInfo.getGetterMethod (fieldName, true);

      String typeName = field.getTypeName ();
      out.println (indent + typeName + " " + fieldName
                   + separator + "= " + instanceName
                   + "." + getter.getName () + " ();");
    }
  }

  protected void writeGetParentIdentity (String indent,
                                         String instanceName,
                                         String identityName) {

    out.println ();
    out.println (indent + "RepositoryObjectIdentity " + instanceName);
    out.println (indent + "  = new " + guardianIdentityClassInfo.getName ()
                 + " (" + identityName + ".getProject ()" + ", "
                        + identityName + ".getName ()" + ");");
  }
  
  /** 
   * Write out the logger statement.
   */
  protected void writeLogger(PrintStream out,
                                  String packageName,
                                  String className) {
  
      GenerateUtilities.writeLogger(out, packageName, className);
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

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 23-Dec-04	6518/3	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 23-Dec-04	6518/1	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 15-Oct-04	5794/1	geoff	VBM:2004100801 MCS Import slow

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 30-Sep-04	4511/3	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 05-Aug-04	5081/4	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (import/export text)

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 05-Feb-04	2878/1	claire	VBM:2004020514 Refactor code generation for Projects

 03-Feb-04	2767/5	claire	VBM:2004012701 Adding project handling code

 30-Jan-04	2767/3	claire	VBM:2004012701 Add project

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
