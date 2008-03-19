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
 * $Header: /src/voyager/com/volantis/mcs/build/objects/JDBCAccessorCodeGenerator.java,v 1.19 2003/03/17 11:53:54 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Feb-02    Paul            VBM:2001122103 - Moved code from
 *                              ObjectsCodeGenerator which generated a JDBC
 *                              accessor to here.
 * 04-Mar-02    Adrian          VBM:2002021908 - Readded code to generate
 *                              code to insert the revision value into the
 *                              repository.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - updated method
 *                              writeRetrieveMultipleBody to handle creation
 *                              of RepositoryIdentity where no values are used
 *                              from the SQL ResultSet.  No longer outputs a
 *                              try... catch (SQLException... is this case.
 *                              Also added support for correctly outputing int
 *                              values for internal boolean values in method
 *                              getJDBCQuoteValue
 * 03-Apr-02    Adrian          VBM:2002040201 - wrap "logger.debug" statements
 *                              in "if (logger.isDebugEnabled())" condition to
 *                              prevent debug logging in production build.
 * 03-Apr-02    Adrian          VBM:2002040201 - backed out of some of above
 *                              changes where logger.debug statements did not
 *                              create extra objects so were no different to
 *                              calling logger.isDebugEnabled().
 * 08-May-02    Paul            VBM:2002050305 - Use the constructor which
 *                              takes an identity when retrieving an object.
 * 27-May-02    Paul            VBM:2002050301 - Fixed slight confusion in the
 *                              names of some of the variables generated and
 *                              also fixed the message in the exception thrown
 *                              from within the lock and unlock methods.
 * 10-Jun-02    Allan           VBM:2002060302 - writeUpdateObjectImpl()
 *                              modified to only write anything if there are
 *                              non-identity fields in the object.
 * 12-Jun-02    Paul            VBM:2002060701 - Added hack to cope with the
 *                              slight differences between layouts and other
 *                              objects.
 * 17-Jun-02    Allan           VBM:2002061701 - writeRenameObjectImplBody()
 *                              modified to use the identity fields as the
 *                              key for the object to rename.
 * 01-Aug-02    Phil W-S        VBM:2002080113 - Renamed any retrieveObjects,
 *                              retrieveObjectsImpl, renameObjects,
 *                              renameObjectsImpl and removeObjects methods,
 *                              replacing "Objects" with "Children".
 * 06-Sep-02    Adrian          VBM:2002082903 - update method
 *                              writeLockUnlockObjectBody to write code for all
 *                              generated accessors. Previously those accessors
 *                              with guardianInfo (ie AssetAccessors) did not
 *                              implement locking.
 * 09-Sep-02    Mat             VBM:2002040825 - Added support for prefixes.
 *                              The table names no longer have a fixed prefix
 *                              The prefix is given on the contructor.  It
 *                              will default to VM
 * 23-Nov-02    Allan           VBM:2002112301 - Removed hacks for
 *                              LayoutAccessor. Optimized imports.
 * 14-Mar-03    Sumit           VBM:2003031402 - Wrap logger.debug statements
 *                              in if(logger.isDebugEnabled()) blocks
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 * This class automatically generates JDBC accessors for the classes
 * which have been processed.
 */
public class JDBCAccessorCodeGenerator
  extends AccessorCodeGenerator {

  /**
   * Create a new <code>JDBCAccessorCodeGenerator</code>.
   * @param generatedDir The directory into which the code should be generated.
   */
  public JDBCAccessorCodeGenerator (File generatedDir) {
    super (generatedDir);
  }

  // Javadoc inherited from super class.
  protected void generateAccessor () {
    super.generateAccessor ();
  }

  // Javadoc inherited from super class.
  protected String getAccessorPackage () {
    return "com.volantis.mcs.accessors.jdbc";
  }

  // Javadoc inherited from super class.
  protected String getAccessorName (RepositoryObjectInfo info) {
    ClassInfo objectClassInfo = info.getObjectClassInfo ();
    return "JDBC" + objectClassInfo.getName () + "Accessor";
  }

  // Javadoc inherited from super class.
  protected void addImports (SortedSet imports) {
    imports.add("com.volantis.mcs.objects.Project");

    imports.add ("com.volantis.mcs.repository.jdbc.JDBCRepository");
    imports.add ("com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection");
    imports.add ("com.volantis.mcs.repository.jdbc.JDBCRepositoryException");
    imports.add ("com.volantis.mcs.repository.jdbc.JDBCPolicySource");

    imports.add ("com.volantis.mcs.repository.InternalProject");

    imports.add ("java.sql.Connection");
    imports.add ("java.sql.ResultSet");
    imports.add ("java.sql.SQLException");
    imports.add ("java.sql.Statement");
  }

  // Javadoc inherited from super class.
  protected void writeClassHeader () {

    // Write the class comment.
    String objectClassName = objectClassInfo.getName ();
    GenerateUtilities.writeJavaDocComment
      (out, "", "Provides access to " + objectClassName + " objects stored in"
       + " a JDBC based repository.");

    // Write the class declaration.
    out.println ("public class " + accessorName);
    out.println ("  extends AbstractJDBCAccessor {");
  }

  // Javadoc inherited from super class.
  protected void writeDeclareStaticFields () {
    super.writeDeclareStaticFields ();

    String tableName = objectClassName.toUpperCase ();
    GenerateUtilities.writeJavaDocComment
      (out, "  ", "The name of the table in the jdbc repository");
    out.println ("  private  String tableName;");
  }

   /**
     * Write the constructor for the class.
     */
    protected void writeConstructor() {

        String tableName = objectClassName.toUpperCase();
        // Hack to force all the NullDevice*Asset classes to share the same
        // table. Yuck.
        if (tableName.startsWith("NULLDEVICE") && tableName.endsWith("ASSET")) {
            tableName = "NULLDEVICEASSET";
        }
        GenerateUtilities.writeJavaDocComment(out, "  ",
                                              "Create a new <code>" +
                                              accessorName + "</code>.");

        if (guardianInfo == null) {
            out.println("  public " + accessorName + " (JDBCRepository repository) {");
            out.println(
                    "    // Use the default prefix for this type of accessor");
            out.println("    this(repository, \"VM\");");
            out.println("  }");
            GenerateUtilities.writeJavaDocComment(out, "  ",
                                                  "Create a new <code>" +
                                                  accessorName + "</code>.");
            out.println("  public " + accessorName + " (JDBCRepository repository, String prefix) {");
            out.println("    tableName = repository.getTableName(prefix + \"" + tableName + "\");");
        } else {

            String guardianAccessorName = getAccessorName(guardianInfo);
            out.println("  public");
            out.println(
                    "    " + accessorName + " (" + guardianAccessorName +
                    " parentAccessor, JDBCRepository repository) {");
            out.println("    this(parentAccessor, repository, \"VM\");");
            out.println("  }");
            out.println("  public");
            out.println(
                    "    " + accessorName + " (" + guardianAccessorName +
                    " parentAccessor, JDBCRepository repository, String prefix) {");
            out.println("    this.parentAccessor = parentAccessor;");
            out.println("    tableName = repository.getTableName(prefix + \"" + tableName + "\");");
        }

        writeInitialiseInstanceFields();
        out.println("  }");
    }

    /**
     * Write the instance fields.
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
                       + " (this, repository, prefix));");
        }
      }
    }

  // Javadoc inherited from super class.
  protected void writeAddObjectImplBody () {

    // Write the code which casts the RepositoryObject to the correct type.
    out.println ();
    out.println ("    " + objectClassName + " o = (" + objectClassName
                 + ") object;");

    // Write the code which gets the SQL Connection from the
    // RepositoryConnection.
    writeGetSQLConnection ();

    writeGetJDBCConnection();
    writeGetProjectName(true);

    // write the code which obtains the resolved field names
    if (info.insertRevision()) {
      writeGetResolvedFieldName("REVISION");
    }
    for (Iterator i = allObjectFields.iterator(); i.hasNext();) {
      FieldInfo field = (FieldInfo) i.next();
      String fieldName = field.getName();
      writeGetResolvedFieldName(fieldName);
    }

    // Write the code which creates the SQL statement.
    out.println ();
    out.println ("    String sql = \"insert into \" + tableName");
    StringBuffer buffer = new StringBuffer ();
    String separator = "";
    String concatOperator = " + ";
    int remaining = 60;

    // it is safe to always add the comma here since the table is guaranteed to have other columns
    //buffer.append( "PROJECT, " );

    // If we need to add revision to the list of columns to insert.
     if (info.insertRevision ()) {
       buffer.append (concatOperator);
       buffer.append ("revisionField");
       separator = " + \" , \" ";
     }

    for (Iterator i = allObjectFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName =  field.getName ();

      buffer.append (separator);

      remaining -= separator.length () + fieldName.length () + concatOperator.length();
      if (remaining < 0) {
        buffer.append ("\n     ");
        remaining = 60;
      }
      buffer.append (concatOperator);
      buffer.append (fieldName.toLowerCase() + "Field");
      separator = " + \" , \" ";
    }
    out.println ("      + \" (\" "  + buffer + " + \") \"");

    out.println ("      + \"values (\"");

    // it is safe to always add the comma here since the table is guaranteed to have other columns
    //out.println ("      + JDBCAccessorHelper.quoteValue ( projectName ) + \" , \"" );

    // If we need to add revision to the list of values to insert.
    if (info.insertRevision ()) {
      out.println ("      + JDBCAccessorHelper.UNKNOWN_REVISION + \" , \"");
    }

    for (Iterator i = allObjectFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      if (GenerateUtilities.isProjectField(field)) {
          out.print ("      + JDBCAccessorHelper.quoteValue ( projectName )" );
      } else {
          MethodInfo getter =
            objectClassInfo.getGetterMethod (field.getName (), true);
          String value = "o." + getter.getName () + " ()";
          out.print ("      + " + getJDBCQuoteValue (field, value));
      }
      if (i.hasNext ()) {
        out.println (" + \" , \"");
      } else {
        out.println ();
      }
    }
    out.println ("      + \")\";");

    // Declare the Statement.
    out.println ();
    out.println ("    Statement stmt = null;");

    // Open the try block.
    out.println ("    try {");

    // Create the statement.
    out.println ("      stmt = sqlConnection.createStatement ();");

    // Execute the statement.
    writeExecuteUpdateStatement ();

    // Close the try block.
    out.println ("    }");

    // Wrap SQL Exceptions in RepositoryExceptions.
    writeCatchSQLException ("    ");

    // Finally close the statement if necessary.
    writeFinallyCloseStatement ();
  }

  // Javadoc inherited from super class.
  protected void writeRemoveObjectImplBody () {

    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    out.println ();
    out.println ("    " + identityClassName + " id = (" + identityClassName
                 + ") identity;");

    // Write the code which gets the SQL Connection from the
    // RepositoryConnection.
    writeGetSQLConnection ();

    writeGetJDBCConnection();

    // write the code which resolves the field names
    for (Iterator i = allIdentityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      if (!GenerateUtilities.isProjectField(field)) {
        String fieldName = field.getName ();
        writeGetResolvedFieldName(fieldName);
      }
    }

    out.println ();
    writeGetProjectName(false);

    // Write the code which creates the SQL statement.
    out.println ();
    out.println ("    String sql = \"delete from \" + tableName");
    out.println ("      + \" where PROJECT = \" + "
            + "JDBCAccessorHelper.quoteValue( projectName ) + \" and \"" );
    for (Iterator i = allIdentityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      if (!GenerateUtilities.isProjectField(field)) {
        String fieldName = field.getName ();
        MethodInfo getter = identityClassInfo.getGetterMethod (fieldName,
                                                               true);
        out.println ("      + " + fieldName.toLowerCase () +"Field" + " + \" = \"");
        String value = "id." + getter.getName () + " ()";
        out.print ("      + " + getJDBCQuoteValue (field, value));
        if (i.hasNext ()) {
            out.println ();
            out.println ("      + \" and \"");
        } else {
            out.println (";");
        }
      }
    }

    // Declare the Statement.
    out.println ();
    out.println ("    Statement stmt = null;");

    // Open the try block.
    out.println ("    try {");

    // Create the statement.
    out.println ("      stmt = sqlConnection.createStatement ();");

    // Execute the statement.
    writeExecuteUpdateStatement ();

    // Close the try block.
    out.println ("    }");

    // Wrap SQL Exceptions in RepositoryExceptions.
    writeCatchSQLException ("    ");

    // Finally close the statement if necessary.
    writeFinallyCloseStatement ();
  }

  // Javadoc inherited from super class.
  protected void writeRetrieveObjectImplBody () {

    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    out.println ();
    out.println ("    " + identityClassName + " id = (" + identityClassName
                 + ") identity;");

    // Write the code which gets the SQL Connection from the
    // RepositoryConnection.
    writeGetSQLConnection ();

    writeGetJDBCConnection();

    writeGetProjectName(false);

    // Write the code which creates the SQL statement.
    writeSelectString (extraObjectFields, allIdentityFields,
                       identityClassInfo, "id");

    // Declare the Statement.
    out.println ();
    out.println ("    Statement stmt = null;");

    // Open the try block.
    out.println ("    try {");

    // Create the statement.
    out.println ("      stmt = sqlConnection.createStatement ();");

    // Execute the statement.
    out.println ();
    out.println ("      if(logger.isDebugEnabled()) {");
    out.println ("          logger.debug (sql);");
    out.println ("      }");
    out.println ("      ResultSet rs = stmt.executeQuery (sql);");
    out.println ("      if (!rs.next ()) {");
    out.println ("        return null;");
    out.println ("      }");

    // Create the object.
    out.println ();
    out.println ("      " + objectClassName + " object = new "
                 + objectClassName + " (id);");

    // Initialise the object from the result set.
    int index = 1;
    for (Iterator i = extraObjectFields.iterator (); i.hasNext ();
         index += 1) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();
      MethodInfo setter = objectClassInfo.getSetterMethod (fieldName, true);

      out.println ("      object." + setter.getName () + " ("
                   + getResultSetValue (field, "rs", index) + ");");
    }

    // Make sure that the result set does not contain any more.
    out.println ();
    out.println ("      if (rs.next ()) {");
    out.println ("        throw new RepositoryException" +
                 "(exceptionLocalizer.format(\"repository-contains-multiple-instances\",");
    out.println ("                                     " +
                 "                           \"" + objectClassName + "\"));");
    out.println ("      }");

    // Return the object.
    out.println ();
    out.println ("      return object;");

    // Close the try block.
    out.println ("    }");

    // Wrap SQL Exceptions in RepositoryExceptions.
    writeCatchSQLException ("    ");

    // Finally close the statement if necessary.
    writeFinallyCloseStatement ();
  }

  // Javadoc inherited from super class.
  protected void writeRenameObjectImplBody () {

    // Write the code which casts the RepositoryObjectIdentity to the correct
    // type.
    out.println ();
    out.println ("    " + identityClassName + " id = (" + identityClassName
                 + ") identity;");

    // Write the code which gets the SQL Connection from the
    // RepositoryConnection.
    writeGetSQLConnection ();

    writeGetJDBCConnection();
    writeGetProjectName(false);

    // get the resolved field name for the project field
    writeGetResolvedFieldName("project");

    // get the resolved field names for the remaining fields
    for (Iterator i = allIdentityFields.iterator(); i.hasNext();) {
      FieldInfo field = (FieldInfo) i.next();
      if (!GenerateUtilities.isProjectField(field)) {
        String fieldName = field.getName();
        writeGetResolvedFieldName(fieldName);
      }
    }



    // Write the code which creates the SQL statement.
    out.println ();
    out.println ("    String sql = \"update \" + tableName");
    out.println ("      + \" set \"");
    out.println ("      + nameField + \" = \"");
    out.println ("      + JDBCAccessorHelper.quoteValue (newName)");
    out.println ("      + \" where \" + projectField + \" = \" "
            + "+ JDBCAccessorHelper.quoteValue( projectName ) + \" and \"" );

    for (Iterator i = allIdentityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      if (!GenerateUtilities.isProjectField(field)) {
        String fieldName = field.getName ();
        MethodInfo getter = identityClassInfo.getGetterMethod (fieldName,
                                                               true);

        out.println ("      + " + fieldName.toLowerCase()+"Field" + " + \" =\"");
        String value = "id." + getter.getName () + " ()";
        out.print ("      + " + getJDBCQuoteValue (field, value));
        if (i.hasNext ()) {
            out.println ();
            out.println ("      + \" and\"");
        } else {
            out.println (";");
        }
      }
    }

    // Declare the Statement.
    out.println ();
    out.println ("    Statement stmt = null;");

    // Open the try block.
    out.println ("    try {");

    // Create the statement.
    out.println ("      stmt = sqlConnection.createStatement ();");

    // Execute the statement.
    writeExecuteUpdateStatement ();

    // Close the try block.
    out.println ("    }");

    // Wrap SQL Exceptions in RepositoryExceptions.
    writeCatchSQLException ("    ");

    // Finally close the statement if necessary.
    writeFinallyCloseStatement ();
  }

  // Javadoc inherited from super class.
  protected void writeUpdateObjectImplBody () {

    // If the object does not have any other fields apart from
    // those which form the identity then this method can do
    // nothing.
    if (extraObjectFields.size () == 0) {
      return;
    }

    // Write the code which casts the RepositoryObject to the correct
    // type.
    out.println ();
    out.println ("    " + objectClassName + " o = (" + objectClassName
                 + ") object;");
    out.println ("    " + identityClassName + " id = (" + identityClassName
                 + ") o.getIdentity ();");

    // Write the code which gets the SQL Connection from the
    // RepositoryConnection.
    writeGetSQLConnection ();
    writeGetJDBCConnection();
    writeGetProjectName(true);

    ArrayList resolvedFields = new ArrayList(); // stores fields which have been resolved

    // write the code which gets the resolved field names for the update string
    for (Iterator i = extraObjectFields.iterator(); i.hasNext();) {
      FieldInfo field = (FieldInfo) i.next();
      String fieldName = field.getName();
      writeGetResolvedFieldName(fieldName);
      resolvedFields.add(field);
    }

    // write the code which gets the resolved field names for the query string
    writeGetResolvedFieldNamesForQueryString(allIdentityFields, resolvedFields);

    // Write the code which creates the SQL statement.
    out.println ();
    out.println ("    String sql = \"update \" + tableName");
    out.println ("      + \" set \"");

    for (Iterator i = extraObjectFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();

      MethodInfo getter = objectClassInfo.getGetterMethod (fieldName, true);
      String value = "o." + getter.getName () + " ()";

      out.println ("      + " + fieldName.toLowerCase()+"Field" + " + \" = \"");
      out.print ("      + " + getJDBCQuoteValue (field, value));
      if (i.hasNext ()) {
        out.println (" + \" , \"");
      } else {
        out.println ();
      }
    }

    writeQueryString (allIdentityFields, identityClassInfo, "id");

    // Declare the Statement.
    out.println ();
    out.println ("    Statement stmt = null;");

    // Open the try block.
    out.println ("    try {");

    // Create the statement.
    out.println ("      stmt = sqlConnection.createStatement ();");

    // Execute the statement.
    writeExecuteUpdateStatement ();

    // Close the try block.
    out.println ("    }");

    // Wrap SQL Exceptions in RepositoryExceptions.
    writeCatchSQLException ("    ");

    // Finally close the statement if necessary.
    writeFinallyCloseStatement ();
  }

  /**
   * Write out the code for retrieving multiple values, either through an
   * enumeration, or a collection.
   * @param enumerate If true then the code needs to return an enumeration,
   * else it returns a Collection.
   * @param identities If true then the objects contained in the enumeration
   * or collection should be identities, otherwise they should be objects.
   */
  protected void writeRetrieveMultipleBody (boolean enumerate,
                                            boolean identities) {

    List selectFields = null;
    List queryFields = null;
    ClassInfo queryClassInfo = null;

    out.println ();
    if (guardianInfo == null) {
        out.println ("    // Parent must be null.");
        out.println ("    if (parent != null) {");
        out.println ("      throw new IllegalArgumentException ("
                     + "\"Parent should be null but is '\"");
        out.println ("                                          "
                     + "+ parent + \"'\");");
        out.println ("    }");

        // The generated method has to select all the objects for a project
        // so the select statement only uses the project field as
        // a query field.
        if (identities) {
          selectFields = allIdentityFields;
        } else {
          selectFields = allObjectFields;
        }

        // Always use the project in the query.
        queryFields = new ArrayList();
        queryFields.add(info.getProjectIdentityField());
        queryClassInfo = info.getIdentityClassInfo();

    } else {

      ClassInfo guardianIdentityClassInfo
        = guardianInfo.getIdentityClassInfo ();
      String guardianIdentityClassName = guardianIdentityClassInfo.getName ();
      List allGuardianIdentityFields = guardianInfo.getAllIdentityFields ();

      // Write the code which casts the RepositoryObjectIdentity to the correct
      // type.
      out.println ("    final " + guardianIdentityClassName + " parentId");
      out.println ("      = (" + guardianIdentityClassName + ") parent;");

      // The generated method has to select only those objects which belong
      // to the specified parent and so the query consists of checking the
      // guardian's fields and the selection is all of those fields of the
      // statement does not have a query and must select the columns associated
      // with all of the object's identity fields.
      if (identities) {
        selectFields = info.getExtraIdentityFields ();
      } else {
        selectFields
          = RepositoryObjectInfo.getExtraFields (allObjectFields,
                                                 guardianIdentityClassInfo);
      }

      queryFields = allGuardianIdentityFields;
      queryClassInfo = guardianIdentityClassInfo;

    }

    ClassInfo classInfo;
    String className;
    String instanceName;
    if (identities) {
      classInfo = identityClassInfo;
      instanceName = "i";
    } else {
      classInfo = objectClassInfo;
      instanceName = "o";
    }
    className = classInfo.getName ();

    // Write the code which gets the SQL Connection from the
    // RepositoryConnection.
    writeGetSQLConnection ();

    writeGetJDBCConnection();
    if(guardianInfo == null) {
      writeGetProjectAndName(null);
    } else {
      writeGetProjectAndName("parentId");
    }

    // Write the code which creates the SQL statement.
    writeSelectString (selectFields, queryFields, queryClassInfo,
                       "parentId");

    if(guardianInfo == null) {
        // If guardianInfo is null, the queryfield will contain the project.
        // We don't want it in the query field for the createObject...
        // methods below, so remove it here.
        queryFields = Collections.EMPTY_LIST;
        queryClassInfo = null;
    }
    // Declare the Statement.
    out.println ();
    out.println ("    Statement stmt = null;");

    // Open the try block.
    out.println ("    try {");

    // Create the statement.
    out.println ("      stmt = sqlConnection.createStatement ();");

    // Execute the statement.
    out.println ();
    out.println ("      if(logger.isDebugEnabled()) {");
    out.println ("          logger.debug (sql);");
    out.println ("      }");
    out.println ("      ResultSet rs = stmt.executeQuery (sql);");

    if (enumerate) {
      // Create a RepositoryEnumeration over the ResultSet.
      out.println ();
      out.println ("      RepositoryEnumeration enumeration"
                   + " = new SQLResultSetEnumeration (rs) {");
      out.println ("          protected Object getData ()");
      out.println ("            throws RepositoryException {");
      out.println ();


      if (identities) {
        if (!selectFields.isEmpty()) {
            out.println ("            try {");
        }

        // Create the object.
        writeCreateObjectFromObjectAndResultSet ("              ",
                                                 queryFields,
                                                 selectFields,
                                                 classInfo,
                                                 instanceName,
                                                 queryClassInfo,
                                                 "parentId",
                                                 "this.rs");

	    // Return the object.
	    out.println ("              return " + instanceName + ";");

	    if (!selectFields.isEmpty()) {
	        // Close the try block.
	        out.println ("            }");

	        // Wrap SQL Exceptions in RepositoryExceptions.
	        writeCatchSQLException ("            ");
	    }
    } else {
	    out.println ("            try {");
        // Create the object.
        out.println ("              " + className + " "
                     + instanceName + " = new " + className + " ();");

        // Initialise the object with the fields from the identity.
        writeInitialiseObjectFromObject ("              ",
                                         queryFields,
                                         classInfo, instanceName,
                                         queryClassInfo, "parentId");

        // Initialise the object from the result set.
        writeInitialiseObjectFromResultSet ("              ",
                                            selectFields,
                                            classInfo, instanceName,
                                            "this.rs");
	    // Return the object.
	    out.println ("              return " + instanceName + ";");

	    // Close the try block.
	    out.println ("            }");

	    // Wrap SQL Exceptions in RepositoryExceptions.
	    writeCatchSQLException ("            ");

    }

      // Close the method.
      out.println ("          }");

      // Close the RepositoryEnumeration.
      out.println ("        };");

      out.println ();

      GenerateUtilities.formatParagraph
        (out, "      // ",
         "The Statement is now owned by the enumeration and it is responsible"
         + " for closing it so prevent the finally clause from closing it.");
      out.println ("      stmt = null;");

      // Return the enumeration.
      out.println ();
      out.println ("      return enumeration;");

    } else {
      // Create and initialise the collection.
      out.println ("      List collection = new ArrayList ();");
      out.println ("      while (rs.next ()) {");

      if (identities) {
        // Create the object.
        writeCreateObjectFromObjectAndResultSet ("        ",
                                                 queryFields,
                                                 selectFields,
                                                 classInfo,
                                                 instanceName,
                                                 queryClassInfo,
                                                 "parentId",
                                                 "rs");
      } else {
        out.println ("        " + className + " "
                     + instanceName + " = new " + className + " ();");

        // Initialise the object with the fields from the identity.
        writeInitialiseObjectFromObject ("        ",
                                         queryFields,
                                         objectClassInfo, instanceName,
                                         queryClassInfo, "parentId");

        // Initialise the object from the result set.
        writeInitialiseObjectFromResultSet ("        ",
                                            selectFields,
                                            objectClassInfo, instanceName,
                                            "rs");
      }

      // Add the new instance to the collection.
      out.println ();
      out.println ("        collection.add (" + instanceName + ");");

      // Close the loop.
      out.println ("      }");

      // Return the collection.
      out.println ();
      out.println ("      return collection;");
    }

    // Close the try block.
    out.println ("    }");

    // Wrap SQL Exceptions in RepositoryExceptions.
    writeCatchSQLException ("    ");

    // Finally close the statement if necessary.
    writeFinallyCloseStatement ();
  }

    /**
     * Write code to get a project and project name.
     */
    private void writeGetProjectAndName(String id) {
        out.println("    final InternalProject project = ");
        if (id != null) {
            out.println("        jdbcConnection.getProject(" + id + ");");
        } else {
            out.println("        jdbcConnection.getLocalRepository()." +
                    "getDefaultProject();");
        }
        out.println ("    JDBCPolicySource policySource = " +
                "(JDBCPolicySource) project.getPolicySource();");
        out.println ("    String projectName = " +
                "policySource.getName();");
    }

    /**
     * Write out the code for removing all objects , either through an
     * enumeration, or a collection.
     *
     */
    protected void writeRemoveChildrenImplBody() {

        List selectFields;
        List queryFields;
        ClassInfo queryClassInfo;
        out.println();

        if (guardianInfo == null) {

            return;
        }

        ClassInfo guardianIdentityClassInfo = guardianInfo.getIdentityClassInfo();
        String guardianIdentityClassName = guardianIdentityClassInfo.getName();
        List allGuardianIdentityFields = guardianInfo.getAllIdentityFields();

        // Write the code which casts the RepositoryObjectIdentity to the correct
        // type.
        out.println("    final " + guardianIdentityClassName + " parentId");
        out.println("      = (" + guardianIdentityClassName + ") parent;");

        // The generated method has to select only those objects which belong
        // to the specified parent and so the query consists of checking the
        // guardian's fields and the selection is all of those fields of the
        // statement does not have a query and must select the columns associated
        // with all of the object's identity fields.
        selectFields = info.getExtraIdentityFields();
        queryFields = allGuardianIdentityFields;
        queryClassInfo = guardianIdentityClassInfo;

        ClassInfo classInfo;
        String className;
        String instanceName;
        classInfo = identityClassInfo;
        instanceName = "parentId";
        className = classInfo.getName();

        writeGetJDBCConnection();
        out.println ("    String projectName = jdbcConnection.getProjectName(parentId);");

        // Write the code which gets the SQL Connection from the
        // RepositoryConnection.
        writeGetSQLConnection();

        // get the resolved field names for the query string
        writeGetResolvedFieldNamesForQueryString(queryFields, null);

        out.println();
        out.println("    String sql = \"delete from \" + tableName");
        writeQueryString(queryFields, queryClassInfo, instanceName);
        out.println();

        // Declare the Statement.
        out.println();
        out.println("    Statement stmt = null;");

        // Open the try block.
        out.println("    try {");

        // Create the statement.
        out.println("      stmt = sqlConnection.createStatement ();");

        // Execute the statement.
        out.println();
        out.println("      if(logger.isDebugEnabled()) {");
        out.println("          logger.debug (sql);");
        out.println("      }");
        out.println("      int rows = stmt.executeUpdate(sql);");
        out.println("      if (logger.isDebugEnabled ()) {");
        out.println("        logger.debug (rows + \" rows processed\");");
        out.println("      }");

        // Close the try block.
        out.println("    }");

        // Wrap SQL Exceptions in RepositoryExceptions.
        writeCatchSQLException("    ");

        // Finally close the statement if necessary.
        writeFinallyCloseStatement();
    }

  // Javadoc inherited from super class.
  protected void writeEnumerateObjectIdentitiesBody () {
    writeRetrieveMultipleBody (true, true);
  }

  // Javadoc inherited from super class.
  protected void writeRetrieveObjectIdentitiesBody () {
    writeRetrieveMultipleBody (false, true);
  }

  // Javadoc inherited from super class.
  protected void writeRetrieveChildrenImplBody () {
    writeRetrieveMultipleBody (false, false);
  }

  // Javadoc inherited from super class.
  protected void writeRenameChildrenImplBody () {

    if (guardianInfo == null) {
      // The object does not have a guardian so throw an unsupported operation
      // exception.
      out.println ();
      out.println ("    throw new UnsupportedOperationException");
      out.println ("      (\"Renaming multiple components not supported\"");
      out.println ("       + \" on " + objectClassName + "s\");");
    } else {

      guardianIdentityClassInfo = guardianInfo.getIdentityClassInfo ();
      guardianIdentityClassName = guardianIdentityClassInfo.getName ();
      allGuardianIdentityFields = guardianInfo.getAllIdentityFields ();

      // Write the code which casts the RepositoryObjectIdentity to the correct
      // type.
      out.println ();
      out.println ("    " + guardianIdentityClassName + " srcParentId = ("
                   + guardianIdentityClassName + ") srcParent;");
      out.println ("    " + guardianIdentityClassName + " dstParentId = ("
                   + guardianIdentityClassName + ") dstParent;");

      // Write the code which gets the SQL Connection from the
      // RepositoryConnection.
      writeGetSQLConnection ();
      writeGetJDBCConnection();

      // get the resolved field names
      writeGetResolvedFieldNamesForQueryString(allGuardianIdentityFields, null);

      out.println("    String srcProjectName = " +
              "jdbcConnection.getProjectName(srcParentId);");
      out.println("    String dstProjectName = " +
              "jdbcConnection.getProjectName(dstParentId);");
      // Write the code which creates the SQL statement.
      out.println ();
      out.println ("    String sql = \"update \" + tableName");

      writeIdentityFieldsString(allGuardianIdentityFields,
              guardianIdentityClassInfo, "dstParentId", "set",
              "dstProjectName");
      writeIdentityFieldsString(allGuardianIdentityFields,
              guardianIdentityClassInfo, "srcParentId", "where",
              "srcProjectName");

      out.println (";");

      // Declare the Statement.
      out.println ();
      out.println ("    Statement stmt = null;");

      // Open the try block.
      out.println ("    try {");

      // Create the statement.
      out.println ("      stmt = sqlConnection.createStatement ();");

      // Execute the statement.
      writeExecuteUpdateStatement ();

      // Close the try block.
      out.println ("    }");

      // Wrap SQL Exceptions in RepositoryExceptions.
      writeCatchSQLException ("    ");

      // Finally close the statement if necessary.
      writeFinallyCloseStatement ();
    }
  }

  // Javadoc inherited from super class.
  protected void writeLockObjectBody () {
    writeUnlockLockObjectBody (true);
  }

  // Javadoc inherited from super class.
  protected void writeUnlockObjectBody () {
    writeUnlockLockObjectBody (false);
  }

  /**
   * Write the body of the lock and unlock methods.
   * @param lock If true then the generated code should lock, otherwise it
   * should unlock.
   */
  private void writeUnlockLockObjectBody (boolean lock) {

    String prefix = (lock ? "lock" : "unlock");

    // Write the code which gets the JDBCRepositoryConnection from the
    // RepositoryConnection.
    writeGetJDBCConnection ();

    out.println ();
    out.println ("    String className = \"" + objectClassName + "\";");
    out.println ("    String objectName = identity.getName ();");
    if (guardianInfo == null) {
      out.println ("    String uniqueKey = null;");
    } else {
      out.println ("    " + objectClassName + "Identity id = ("
                   + objectClassName + "Identity) identity;");
      out.println ("    StringBuffer fields = new StringBuffer();");

      for (Iterator i = allIdentityFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        String fieldName = field.getName ();
        // name is already used in locking
        if ("name".equals(fieldName)) {
          continue;
        }
        MethodInfo getter =
            identityClassInfo.getGetterMethod (fieldName, true);
        if (i.hasNext()) {
          out.println("    fields.append(id." + getter.getName () + " ())"
                      + ".append('^');");
        } else {
          out.println("    fields.append(id." + getter.getName () + " ());");
        }

      }
      out.println ("    String uniqueKey = fields.toString();");
    }

    out.println ();
    out.println ("    jdbcConnection." + prefix + " (className, objectName,"
                 + " uniqueKey);");
  }

  // Javadoc inherited from super class.
  protected void writeClassFooter () {
    // Close the class.
    out.println ("}");
  }

  /**
   * Get a block of code which quotes the given value if needed.
   * @param field The field for which the value is being quoted.
   * @param value The value to quote.
   * @return Code which quotes the value if needed.
   */
  private String getJDBCQuoteValue (FieldInfo field,
                                    String value) {
    String fieldName = field.getName ();
    String type = field.getTypeName ();

    if ("int".equals (type)) {
      return value;
    } else if ("boolean".equals (type)) {
      return "(" + value + " ? \"1\" : \"0\")";
    } else if ("String".equals (type)) {
      return "JDBCAccessorHelper.quoteValue (" + value + ")";
    } else if ("Project".equals(type)) {
      return "JDBCAccessorHelper.quoteValue(projectName)";
    } else {
      throw new IllegalArgumentException ("Don't know how to quote value "
                                          + value + " of type " + type);
    }
  }

    /**
     * Return the code which gets the correct type of value out of the result
     * set.
     *
     * @param field The field whose value is being retrieved.
     * @param resultSetName The name of the result set variable.
     * @param index The index of the value within the result set.
     * @return Code which retrieves the correct type of value out of the result
     *         set.
     */
    private String getResultSetValue(FieldInfo field,
            String resultSetName,
            int index) {

        String result;
        String type = field.getTypeName();

        if ("int".equals(type)) {
            result = resultSetName + ".getInt (" + index + ")";
        } else if ("boolean".equals(type)) {
            result = resultSetName + ".getInt (" + index + ") != 0";
        } else if ("String".equals(type)) {
            result = resultSetName + ".getString (" + index + ")";
            // If the field is required, then we need to try and transform any
            // null values back into empty strings. This is somewhat dodgy but
            // seemingly required because of the changes made for Sybase support
            // which seem to translate empty strings into nulls on write.
            boolean required = info.isEmptyStringField(field.getName());
            if (required) {
                result = "JDBCAccessorHelper.unquoteMandatory(" + result + ")";
            }
        } else if ("Project".equals(type)) {
            result = resultSetName + ".getProject()";
        } else {
            throw new IllegalArgumentException("Don't know how to get index " +
                    index + " of type " + type + " from result set " +
                    resultSetName);
        }

        return result;
    }

  /**
   * Write code which gets the SQL Connection from the RepositoryConnection.
   */
  private void writeGetSQLConnection () {
    out.println ();
    GenerateUtilities.formatParagraph
      (out, "    // ",
       "Cast the repository connection to a JDBCRepositoryConnection in"
       + " order to get the java.sql.Connection out.");
    out.println ("    Connection sqlConnection = ((JDBCRepositoryConnection)");
    out.println ("                               "
                 + " connection).getConnection ();");
  }

  /**
   * Write code which gets the JDBCRepositoryConnection from the
   * RepositoryConnection.
   */
  private void writeGetJDBCConnection () {
    out.println ();
    GenerateUtilities.formatParagraph
      (out, "    // ",
       "Cast the repository connection to a JDBCRepositoryConnection.");
    out.println ("    JDBCRepositoryConnection jdbcConnection");
    out.println ("      = (JDBCRepositoryConnection) connection;");
  }

    /**
     * Write code which gets the project name from an object or identity using
     * a JDBCRepositoryConnection.
     *
     * @param fromObject true if the source code has an object available, false
     *      if it has an identity available.
     */
    private void writeGetProjectName(boolean fromObject) {
        out.println ();
        if (fromObject) {
            out.println("    RepositoryObjectIdentity identity = " +
                    "object.getIdentity();");
        }
        out.println ("    String projectName = " +
                "jdbcConnection.getProjectName(identity);");
    }

  /**
   * Write code which executes an update statement.
   */
  private void writeExecuteUpdateStatement () {
    out.println ();
    out.println ("      if(logger.isDebugEnabled()) {");
    out.println ("          logger.debug (sql);");
    out.println ("      }");
    out.println ("      int rows = stmt.executeUpdate (sql);");
    out.println ("      if (logger.isDebugEnabled ()) {");
    out.println ("        logger.debug (rows + \" rows processed\");");
    out.println ("      }");
  }

  /**
   * Write code which catches, logs and rethrows an SQLException.
   */
  private void writeCatchSQLException (String indent) {
    out.println (indent + "catch (SQLException e) {");
    out.println (indent + "  logger.error (\"sql-exception\", e);");
    out.println (indent + "  throw new JDBCRepositoryException (e);");
    out.println (indent + "}");
  }

  /**
   * Write code which makes sure that the SQL Statement is always closed.
   */
  private void writeFinallyCloseStatement () {
    out.println ("    finally {");
    out.println ("      try {");
    out.println ("        if (stmt != null) {");
    out.println ("          stmt.close ();");
    out.println ("        }");
    out.println ("      }");
    out.println ("      catch (SQLException e) {");
    out.println ("        logger.error (\"sql-exception\", e);");
    out.println ("        throw new JDBCRepositoryException (e);");
    out.println ("      }");
    out.println ("    }");
  }

  /**
   * Write code which creates a select string.
   * Since this code will resolve short field names, it requires a jdbcConnection
   * to be present in any methods which contain code generated by this method
   * @param selectFields The fields to select.
   * @param queryFields The fields to use as the query, may be empty.
   * @param queryClassInfo The information about the class from which the
   * query values are to be obtained.
   * @param queryObjectName The name of the object of the above type from
   * which the query values are to be obtained.
   */
  private void writeSelectString (List selectFields,
                                  List queryFields,
                                  ClassInfo queryClassInfo,
                                  String queryObjectName) {

    ArrayList resolvedFields = new ArrayList();
    out.println ();

    // write code which will resolve the field names
    out.println ("    // get resolved field names for select string ");
    if (selectFields.size () == 0) {
      if (queryFields.size () == 0) {
        throw new IllegalArgumentException ("Not sure what to do");
      } else {
        FieldInfo field = (FieldInfo) queryFields.get (0);
        writeGetResolvedFieldName(field.getName ());
        resolvedFields.add(field);
      }
    } else {
      for (Iterator i = selectFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        writeGetResolvedFieldName(field.getName ());
        resolvedFields.add(field);
      }
    }

    // write code which will resolve the field names for the query
    writeGetResolvedFieldNamesForQueryString(queryFields, resolvedFields);

    // write the query
    out.println ("    String sql = \"select \"");

    int remaining = 60;

    if (selectFields.size() == 0) {
      if (queryFields.size() == 0) {
        throw new IllegalArgumentException("Not sure what to do");
      } else {
        FieldInfo field = (FieldInfo) queryFields.get(0);
        out.println("      + " + field.getName().toLowerCase() + "Field");
      }
    } else {
      StringBuffer buffer = new StringBuffer();
      String separator = "";
      String concactonator = " + ";
      for (Iterator i = selectFields.iterator(); i.hasNext();) {
        FieldInfo field = (FieldInfo) i.next();
        remaining -= separator.length() + field.getName().length() + concactonator.length();
        if (remaining < 0) {
          buffer.append("\n     ");
          remaining = 60;
        }
        buffer.append(concactonator);
        buffer.append(field.getName().toLowerCase() + "Field");
        if(i.hasNext()) {
          separator = "+  \" , \" ";
        }
        else {
          separator = "";
        }
        buffer.append(separator);
      }
      out.println("      " + buffer);
    }
    out.print("      + \" from \" + tableName");

    writeQueryString(queryFields, queryClassInfo, queryObjectName);
  }



  /**
   * Write code which creates a query string
   * Needs to be used in conjunction with writeGetResolvedFieldNamesForQueryString
   * @param queryFields The fields to use as the query, may be empty.
   * @param queryClassInfo The information about the class from which the
   * query values are to be obtained.
   * @param queryObjectName The name of the object of the above type from
   * which the query values are to be obtained.
   */
  private void writeQueryString (List queryFields,
                                 ClassInfo queryClassInfo,
                                 String queryObjectName) {
    if (queryFields.size () == 0) {
      out.println (";");
    } else {
      out.println ();
      out.println ("      + \" where \"");
      for (Iterator i = queryFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        String fieldName = field.getName ();
        MethodInfo getter = queryClassInfo.getGetterMethod (fieldName,
                                                            true);

        out.println ("      + " + fieldName.toLowerCase()+"Field" + " + \" = \"");
        String value = queryObjectName + "." + getter.getName () + " ()";
        out.print ("      + " + getJDBCQuoteValue (field, value));
        if (i.hasNext ()) {
          out.println ();
          out.println ("      + \" and \"");
        } else {
          out.println (";");
        }
      }
    }
  }

    /**
     * Write code which creates a string based on the given fields and
     * SQL operation.  It then uses the specified packageName in situations
     * where the defaults are not appropriate..
     * @param queryFields The fields to use as the query, may be empty.
     * @param queryClassInfo The information about the class from which the
     * query values are to be obtained.
     * @param queryObjectName The name of the object of the above type from
     * which the query values are to be obtained.
     * @param sqlOperation A string representation of the SQL keyword to
     * use in the string.
     * @param packageName The package name to use when building the string.
     */
    private void writeIdentityFieldsString (List queryFields,
                                            ClassInfo queryClassInfo,
                                            String queryObjectName,
                                            String sqlOperation,
                                            String packageName) {
      if (queryFields.size () == 0) {
        out.println (";");
      } else {
        out.println ();
        out.println ("      + \" " + sqlOperation + "\"");
        String value;
        for (Iterator i = queryFields.iterator (); i.hasNext ();) {
          FieldInfo field = (FieldInfo) i.next ();
          String fieldName = field.getName ();
          MethodInfo getter = queryClassInfo.getGetterMethod(fieldName,
                                                             true);

          out.println("      +  " + fieldName.toLowerCase()+"Field" + " + \" = \" ");
          if (!GenerateUtilities.isProjectField(field)) {
              value = queryObjectName + "." + getter.getName () + " ()";
              out.print("      + " + getJDBCQuoteValue (field, value));
          } else {
              out.print("      + JDBCAccessorHelper.quoteValue(" + packageName + ")");
          }
          if (i.hasNext ()) {
            out.println ();
            if("set".equals(sqlOperation)) {
                out.println ("      + \" ,\"");
            } else {
                out.println ("      + \" and\"");
            }

          }
        }
      }
    }


  /**
   * Write code which initialises an object from a result set.
   * @param indent The indent to use when outputting the code.
   * @param classInfo The class information for the object being
   * initialised.
   * @param fields The fields which should be retrieved from the result set.
   * @param objectName The name of the object being initialised.
   * @param resultSetName The name of the result set variable.
   */
  private void writeInitialiseObjectFromResultSet (String indent,
                                                   List fields,
                                                   ClassInfo classInfo,
                                                   String objectName,
                                                   String resultSetName) {

    if (fields == null) {
      return;
    }

    // Initialise the object from the result set.
    int index = 1;
    for (Iterator i = fields.iterator (); i.hasNext (); index += 1) {
      FieldInfo field = (FieldInfo) i.next ();
        if (!GenerateUtilities.isProjectField(field)) {
            String fieldName = field.getName ();
            MethodInfo setter = classInfo.getSetterMethod (fieldName, true);

            out.println (indent + objectName + "." + setter.getName () + " ("
                   + getResultSetValue (field, resultSetName, index) + ");");
        }
    }
  }

  /**
   * Write code which initialises an object from an object and a result set.
   * @param indent The indent to use when outputting the code.
   * @param queryFields The fields to retrieve from the source object
   * @param selectFields The fields to retrieve from the result set.
   * @param dstClassInfo The class information for the object being
   * initialised.
   * @param dstObjectName The name of the object being initialised.
   * @param srcClassInfo The class information for the object from which the
   * other object is being initialised.
   * @param srcObjectName The name of the object from which the other object
   * is being initialised.
   * @param resultSetName The name of the result set variable.
   */
  private
    void writeCreateObjectFromObjectAndResultSet (String indent,
                                                  List queryFields,
                                                  List selectFields,
                                                  ClassInfo dstClassInfo,
                                                  String dstObjectName,
                                                  ClassInfo srcClassInfo,
                                                  String srcObjectName,
                                                  String resultSetName) {

    String dstClassName = dstClassInfo.getName ();
    out.println (indent + dstClassName + " " + dstObjectName);
    String line = (indent + "  = new " + dstClassName + " (");
    out.print (line);

    indent = GenerateUtilities.getIndent (line.length ());

    List constructors = dstClassInfo.getConstructors ();
    if (constructors.size () != 1) {
      throw new IllegalStateException ("Must be one constructor");
    }

    ConstructorInfo constructorInfo = (ConstructorInfo) constructors.get (0);
    List parameters = constructorInfo.getParameters ();

      String params = "";
      for (Iterator i = parameters.iterator(); i.hasNext(); /**/) {
          params += ((ParameterInfo)i.next()).getName();
          params += " ";
      }
      String qs = "";
      for (Iterator i = queryFields.iterator(); i.hasNext(); /**/) {
          qs += ((FieldInfo)i.next()).getName();
          qs += " ";
      }
      String ss = "";
      for (Iterator i = selectFields.iterator(); i.hasNext(); /**/) {
          ss += ((FieldInfo)i.next()).getName();
          ss += " ";
      }

    // If the number of parameters is not the same as the total number of
    // fields then error.
    if (parameters.size() != queryFields.size() + selectFields.size()) {
      throw new IllegalStateException ("Incorrect number of  parameters (" +
              parameters.size() + ") in constructor for " + dstClassName + ". "
              + "Unable to initialise object. queryFields=" +
              queryFields.size() + ", selectFields=" + selectFields.size() +
              ".  parameters=" + params + "queries=" + qs + "selects=" + ss);
    }

    Iterator p = parameters.iterator ();
    Iterator qf = queryFields.iterator ();
    Iterator sf = selectFields.iterator ();

    int index = 1;
    String separator = null;
    for (; p.hasNext ();) {
      ParameterInfo parameter = (ParameterInfo) p.next ();
      String parameterName = parameter.getName ();
      if (separator == null) {
        separator = indent;
      } else {
        out.print (separator);
      }

      FieldInfo field;
      String fieldName;

      if (srcClassInfo == null
          || srcClassInfo.getField (parameterName, true) == null) {
        // The parameter is not from the source object so it must be part of
        // the result set.
        field = (FieldInfo) sf.next ();
        fieldName = field.getName ();
        if (GenerateUtilities.isProjectField(field)) {
            out.print("project");
        } else {
            out.print (getResultSetValue (field, resultSetName, index));
        }
        index += 1;
      } else {
        // The parameter is from the source object.
        field = (FieldInfo) qf.next ();
        fieldName = field.getName ();
        if (GenerateUtilities.isProjectField(field)) {
            out.print("project");
        } else {
            MethodInfo getter = srcClassInfo.getGetterMethod (fieldName, true);
            out.print (srcObjectName + "." + getter.getName () + " ()");
        }
      }
      if (!fieldName.equals (parameterName)) {
          throw new IllegalStateException ("Parameter " + parameterName
           + " not the same as " + fieldName);
      }
      if (p.hasNext ()) {
        out.println (" , ");
      }
    }

    out.println (");");
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10449/1	geoff	VBM:2005110803 MCS35: Export issue with textAsset contradicts import & GUI and throws exception

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 28-Apr-05	7908/3	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 28-Apr-05	7908/1	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 23-Feb-05	7101/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	7091/1	geoff	VBM:2005020703 Sybase integration

 23-Feb-05	6905/1	allan	VBM:2005020703 Added support for Sybase

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

 30-Sep-04	4511/11	tom	VBM:2004052005 Added short column support for new table columns and cache accessors

 04-Jun-04	4511/7	tom	VBM:2004052005 added support for short column names

 05-Aug-04	5081/13	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (dont fight the force)

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 17-May-04	3649/1	mat	VBM:2004031910 Add short tablename support

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 19-Feb-04	2789/1	tony	VBM:2004012601 rework changes

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 05-Feb-04	2878/1	claire	VBM:2004020514 Refactor code generation for Projects

 05-Feb-04	2851/1	mat	VBM:2004020404 Amended code generators to fix the import/export

 03-Feb-04	2767/4	claire	VBM:2004012701 Adding project handling code

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 30-Jan-04	2807/2	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 04-Jan-04	2360/1	andy	VBM:2003121710 added PROJECT column to all tables

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
