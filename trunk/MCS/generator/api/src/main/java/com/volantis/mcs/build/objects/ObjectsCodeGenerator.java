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
 * $Header: /src/voyager/com/volantis/mcs/build/objects/ObjectsCodeGenerator.java,v 1.22 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Moved the code for generating
 *                              JDBC accessors out and added support for
 *                              generating XML accessors.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Apr-02    Mat             VBM:2002040814 - Added support for the new
 *                              RemoteAccessorGenerator()
 * 03-May-02    Paul            VBM:2002050201 - Added filters to remove
 *                              expected warnings from output.
 * 08-May-02    Paul            VBM:2002050305 - Generate an identity factory
 *                              class for all objects which are identified by
 *                              name only.
 * 17-May-02    Paul            VBM:2002050101 - Process those files which
 *                              contain mariner-object-identity-field tags.
 * 17-May-02    Allan           VBM:2002030615 - Added generator methods for
 *                              RepositoryObjectIdentityVisitor and
 *                              RepositoryObjectIdentityVisitorAdapter. Also
 *                              added a visit() method to generated identity
 *                              classes in generateIdentityClass(). Method
 *                              processRootDoc() modified to call new generator
 *                              methods.
 * 06-Jun-02	Jwk             VBM:2002060303 - Create a new Internal version
 *                              of RepositoryObjectIdentityVisitor to allow the
 *                              visitor method references to be hidden from the
 *                              public API version.
 * 12-Jun-02    Allan           VBM:2002030615 - Made the visit methods in
 *                              RepositoryObjectIdentityVisitor and friends
 *                              return an Object.
 * 14-Jun-02    Mat             VBM:2002052001 - Changed IMDAccessorGenerator
 *                              to PageAccessorGenerator
 * 19-Jul-02    Allan           VBM:2002071901 - Modified generateIdentityClass
 *                              to include RepositoryObjectDeviceIdentity
 *                              references where appropriate.
 * 09-Sep-02    Mat             VBM:2002040825 - Added
 *                              sharedAccessorCodeGenerator to generate the
 *                              accessors for the shared repository.
 * 03-Dec-02    Steve           VBM:2002090210 - Use the CacheEntry class
 *                              document instead of CacheInfo
 * 12-Mar-02    Steve           VBM:2003022403 - Added API doclet tags to
 *                              generated Identity classes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.objects;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.javadoc.ClassInfo;
import com.volantis.mcs.build.javadoc.ConstructorInfo;
import com.volantis.mcs.build.javadoc.FieldInfo;
import com.volantis.mcs.build.javadoc.MethodInfo;
import com.volantis.mcs.build.javadoc.PackageInfo;
import com.volantis.mcs.build.javadoc.ParameterInfo;

import java.io.File;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class provides common functionality to automatically generate identity
 * objects for the classes which have been processed.
 */
public abstract class ObjectsCodeGenerator
  extends Doclet {

  /**
   * The base directory.
   */
  protected static File baseDir;

  /**
   * The directory into which the generated code will be written.
   */
  protected static File generatedDir;

  private static PrintStream oldErr;

  private static PrintStream oldOut;

  // Intercept System.out and System.err
  static {
    oldErr = System.err;
    oldOut = System.out;

    System.setErr (new FilterErrPrintStream (oldErr));
    System.setOut (new FilterOutPrintStream (oldOut));
  }

  // Javadoc inherited from super class.
  public static int optionLength (String option) {
    if ("-basedir".equals (option)) {
      return 2;
    } else if ("-generateddir".equals (option)) {
      return 2;
    }

    return 0;
  }

  // Javadoc inherited from super class.
  public static boolean validOptions (String [] [] options,
                                      DocErrorReporter reporter) {

    for (int i = 0; i < options.length; i += 1) {
      String [] values = options [i];
      String option = values [0];
      if ("-basedir".equals (option)) {
        baseDir = new File (values [1]);
      } else if ("-generateddir".equals (option)) {
        generatedDir = new File (values [1]);
      }
    }

    return true;
  }

  /**
   * The JavaDoc object which represents the result of the javadoc parsing.
   */
  protected RootDoc rootDoc;

    /**
   * Process the root document.
   * @return True if successful, false otherwise.
   */
  protected boolean processRootDoc () {

    ClassDoc [] classes = rootDoc.classes ();

    for (int i = 0; i < classes.length; i += 1) {
      ClassDoc objectClassDoc = classes [i];

      processClassDoc (objectClassDoc);
    }

    return true;
  }

  /**
   * Process the class information.
   * @param objectClassDoc The JavaDoc object which represents a class which
   * was parsed.
   */
 abstract protected void processClassDoc(ClassDoc objectClassDoc);  

  private PrintStream openFileForClass (String qualifiedClassName) {
    return GenerateUtilities.openFileForClass (generatedDir,
                                               qualifiedClassName);
  }

  /**
   * Generate the Identity class for the specified RepositoryObject.
   * @param info The representation of a RepositoryObject class.
   */
  protected void generateIdentityClass (RepositoryObjectInfo info) {

    ClassInfo objectClassInfo = info.getObjectClassInfo ();
    ClassInfo identityClassInfo = info.getIdentityClassInfo ();

      // Open a file.
      PackageInfo identityPackageInfo = identityClassInfo.getPackageInfo();
      String currentPackageName = identityPackageInfo.getName();

      String identityClassName = identityClassInfo.getName();
      if (identityClassName.endsWith("ImplIdentity")) {
          int end = identityClassName.length() - "ImplIdentity".length();
          identityClassName =
                  identityClassName.substring(0, end) + "Identity";
      }
      String qualifiedClassName = currentPackageName + "." + identityClassName;
      PrintStream out = openFileForClass(qualifiedClassName);

    // Write the header.
    GenerateUtilities.writeHeader (out, this.getClass().getName());

    // Write the package statement. The identity class is added to the same
    // package as the class it identifies.
    out.println ();
    out.println ("package " + currentPackageName + ";");

    // Write the imports.
    SortedSet imports = new TreeSet ();
    ClassInfo identityBaseClassInfo = identityClassInfo.getBaseClassInfo ();
    imports.add (identityBaseClassInfo.getQualifiedName ());

    // Find out if this identity includes a device name and if so add
    // an import for RepositoryObjectDeviceIdentity.
    List identityFields = identityClassInfo.getFields ();

    Iterator fields = identityFields.iterator();
    String anotherFieldName = "";
    while(fields.hasNext() && !anotherFieldName.equals("deviceName")) {
        anotherFieldName = ((FieldInfo) fields.next()).getName();
    }

    if(anotherFieldName.equals("deviceName")) {
      imports.add("com.volantis.mcs.objects.RepositoryObjectDeviceIdentity");
    }
      
    if (currentPackageName.indexOf( "com.volantis.mcs.project" ) == -1 ) {
        imports.add("com.volantis.mcs.project.Project");
    }

    GenerateUtilities.writeImports (out, imports, currentPackageName);

    String comment;
    String separator = null;

    // Write the class comment.
    String objectClassName = objectClassInfo.getName ();
    comment = "Encapsulates those properties of a "
      + objectClassName + " which uniquely identify it.\n";

      // @todo should this condition still apply?
    if (currentPackageName.indexOf("com.volantis.mcs.objects") == -1) {
        comment += getReplicateTagsText(info);

    }

    GenerateUtilities.writeJavaDocComment (out, "", comment);

    // Write the class header.
    out.println ("public class " + identityClassName);
    out.print ("  extends " + identityBaseClassInfo.getName () );

    // If this identity includes a deviceName the add
    // "implements RepositoryObjectDeviceIdentity to the class declaration
    if(anotherFieldName.equals("deviceName")) {
      out.print("\n  implements RepositoryObjectDeviceIdentity");
    }
    out.println(" {");

    // Write the copyright statement.
    GenerateUtilities.writeCopyright (out);

    // Write the fields.
    for (Iterator i = identityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();
      String fieldName = field.getName ();

      GenerateUtilities.writeJavaDocComment (out, "  ", field.getComment ());
      out.println ("  private " + field.getTypeName ()
                   + " " + fieldName + ";");
    }

    // Write the initialisation of the base class.
    List baseConstructors = identityBaseClassInfo.getConstructors();
    System.err.println("base constructors = " + baseConstructors);

    // Based on two constructors in the absolute base class of all assets
    if (baseConstructors.size () != 2) {
      throw new IllegalStateException ("Identity base class has wrong number"
                                       + " of constructors\n" +
              "identityBaseClassInfo = " + identityBaseClassInfo.getName());
    }

    // Iterate over all constructors and replicate them in this class
    for (Iterator i = baseConstructors.iterator(); i.hasNext(); /* No ++ */) {

        ConstructorInfo baseConstructor = (ConstructorInfo)i.next();

        // Write the constructor comment.
        GenerateUtilities.openJavaDocComment(out, "  ");
        GenerateUtilities.addJavaDocComment(out, "  ", "Create a new <code>"
                + identityClassName + "</code>.");
        List constructorParameters = baseConstructor.getParameters();
        for (Iterator innerI = constructorParameters.iterator();
                                            innerI.hasNext(); /* No ++ */) {
            ParameterInfo param = (ParameterInfo)innerI.next();
            String paramName = param.getName();
            comment = "@param " + paramName + " " +
                    identityBaseClassInfo.getField(paramName, true).getComment();

            GenerateUtilities.addJavaDocComment(out, "  ", comment);
        }
        GenerateUtilities.closeJavaDocComment(out, "  ");

        // Write the constructor method declaration.
        String startLine = "  public " + identityClassName + " (";
        out.print(startLine);
        separator = null;
        for (Iterator innerI = constructorParameters.iterator();
                                            innerI.hasNext(); /* No ++ */) {
            ParameterInfo param = (ParameterInfo)innerI.next();

            // Output a parameter declaration.
            if (separator == null) {
                separator = ",\n" +
                        GenerateUtilities.getIndent(startLine.length());
            } else {
                out.print(separator);
            }
            out.print (param.getTypeName() + " " + param.getName());
        }
        if (identityFields.size () != 0) {
            for (Iterator fieldsIterator = identityFields.iterator();
                 fieldsIterator.hasNext(); /**/) {
                FieldInfo field = (FieldInfo)fieldsIterator.next();

                if (separator == null){
                    separator = ",\n" +
                             GenerateUtilities.getIndent(startLine.length());
                } else {
                    out.print(separator);
                }

                out.print (field.getTypeName() + " " + field.getName());
            }
        }
        out.println(") {");



        List parameters = baseConstructor.getParameters();
        ParameterInfo firstParam = (ParameterInfo) parameters.get(0);
        
        // If the Project is the first parameter, call the superclass
        // constructor.  If it isn't, call the superclass constructor with 
        // null for the project.
        if (!GenerateUtilities.isProjectField(firstParam)) {
            out.print("    super(null, ");
        } else {
            // Write the call to the superclass
             out.print("    super(");
        }
        
        separator = "";
        for (Iterator innerI = parameters.iterator();
                                            innerI.hasNext(); /* No ++ */) {
            ParameterInfo parameter = (ParameterInfo)innerI.next();

            
            out.print(separator);
            out.print(parameter.getName());
            separator = ", ";
        }

        out.println (");");

        // Write the initialisation of the fields.
        if (identityFields.size () != 0) {
            out.println ();
            for (Iterator innerI = identityFields.iterator ();
                 innerI.hasNext ();) {
                FieldInfo field = (FieldInfo) innerI.next ();
                String fieldName = field.getName ();

                /* Uncomment this to validate arguments.
                if (typeName.equals ("String")) {
                    out.println ("    if (" + fieldName + " == null) {");
                    out.println ("      throw new IllegalArgumentException (\""
                       + fieldName + " is null\");");
                    out.println ("    }");
                }
                */
                out.println ("    this." + fieldName + " = " + fieldName + ";");
            }
        }

        out.println ("  }");
    }

    // Write out the getter methods if any.
    for (Iterator i = identityFields.iterator (); i.hasNext ();) {
      FieldInfo field = (FieldInfo) i.next ();

      String fieldName = field.getName ();

      MethodInfo method = objectClassInfo.getGetterMethod (fieldName, true);

      // Make sure that the method has no extra parameters.
      List parameters = method.getParameters ();
      if (parameters.size () != 0) {
        throw new IllegalStateException ("Getter method has wrong number"
                                         + " of parameters");
      }

      // Write the getter method comment.
      GenerateUtilities.writeJavaDocComment (out, "  ", method.getComment ());

      // Write the getter method declaration.
      out.println ("  public " + method.getReturnTypeName () + " "
                   + method.getName () + " () {");

      // Write the method body.
      out.println ("    return " + fieldName + ";");

      // Write the method close.
      out.println ("  }");
    }

    // Write the getObjectClass method.
    out.println ();
    out.println ("  // Javadoc inherited from super class.");
    out.println ("  public Class getObjectClass () {");
    out.println ("    return " + objectClassName + ".class;");
    out.println ("  }");

    if (identityFields.size () != 0) {

      // Write the equals method.
      out.println ();
      out.println ("  // Javadoc inherited from super class.");
      out.println ("  public boolean equals (Object object) {");
      out.println ();
      GenerateUtilities.formatParagraph (out, "    // ",
                                         "Call the super class to check"
                                         + " whether this object and the other"
                                         + " object are of the same type and"
                                         + " have the same name.");
      out.println ("    if (!super.equals (object)) {");
      out.println ("      return false;");
      out.println ("    }");
      out.println ();
      out.println ("    " + identityClassName
                   + " identity = (" + identityClassName + ") object;");
      out.print ("    return ");
      separator = "";
      for (Iterator i = identityFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        String fieldName = field.getName ();
        String typeName = field.getTypeName ();

        out.print (separator);
        if (typeName.equals ("String")) {
          out.print ("equals (" + fieldName + ", identity." + fieldName + ")");
        } else {
          out.print (fieldName + " == identity." + fieldName);
        }
        separator = "      && ";

        if (i.hasNext ()) {
          out.println ();
        } else {
          out.println (";");
        }
      }
      out.println ("  }");

      // Write a hashCode method.
      out.println ();
      out.println ("  // Javadoc inherited from super class.");
      out.println ("  public int hashCode () {");
      out.println ("    return super.hashCode ()");
      for (Iterator i = identityFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        String fieldName = field.getName ();
        String typeName = field.getTypeName ();

        out.print ("      + ");
        if (typeName.equals ("boolean")) {
          out.println ("(" + fieldName + " ? 1 : 0)");
        } else if (typeName.equals ("int")) {
          out.println (fieldName);
        } else {
          out.println ("hashCode (" + fieldName + ")");
        }
        if (i.hasNext ()) {
          out.println ();
        } else {
          out.println (";");
        }
      }
      out.println ("  }");

      // Write the compareTo method.
      out.println ();
      out.println ("  // Javadoc inherited from super class.");
      out.println ("  public int compareTo (Object object) {");
      out.println ();
      GenerateUtilities.formatParagraph (out, "    // ",
                                         "Call the super class to check"
                                         + " whether this object and the other"
                                         + " object are of the same type and"
                                         + " have the same name.");
      out.println ("    int result;");
      out.println ("    if ((result = super.compareTo (object)) != 0) {");
      out.println ("      return result;");
      out.println ("    }");
      out.println ();
      out.println ("    " + identityClassName
                   + " identity = (" + identityClassName + ") object;");

      for (Iterator i = identityFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        String fieldName = field.getName ();

        out.println ();
        out.println ("    // Compare the " + fieldName);
        out.println ("    if ((result = compare (" + fieldName
                     + ", identity." + fieldName + ")) != 0) {");
        out.println ("      return result;");
        out.println ("    }");


      }
      out.println ();
      out.println ("    // The identities are equal.");
      out.println ("    return 0;");
      out.println ("  }");

      // Write a paramString method.
      out.println ();
      out.println ("  // Javadoc inherited from super class.");
      out.println ("  protected String paramString () {");
      out.println ("    return super.paramString ()");
      for (Iterator i = identityFields.iterator (); i.hasNext ();) {
        FieldInfo field = (FieldInfo) i.next ();
        String fieldName = field.getName ();

        out.print ("      + \", " + fieldName + "=\" + " + fieldName);
        if (i.hasNext ()) {
          out.println ();
        } else {
          out.println (";");
        }
      }
      out.println ("  }");
    }

    out.println ("}");

    // Close the output stream.
    out.close ();
  }

    /**
     * Returns a complete text string containing all of the replicate tags
     * separated by carriage returns.
     *
     * @param info from which the replicate tags are to be obtained
     * @return a string of the replicate tags separated by carriage returns
     */
    private String getReplicateTagsText(RepositoryObjectInfo info) {
        return GenerateUtilities.getTagsString(info.getReplicateTags());
    }

    private static final String WARNING
    = "javadoc: warning - ";
  private static final int WARNING_LENGTH
    = WARNING.length ();
  private static final String CANNOT_FIND_CLASS
    = "Cannot find class ";
  private static final int CANNOT_FIND_CLASS_LENGTH
    = CANNOT_FIND_CLASS.length ();

  /**
   * The number of warnings which we thing there were after filtering
   * out the ones that we expect.
   */
  private static int warnings;

  /**
   * Filter the error stream.
   */
  private static class FilterErrPrintStream
    extends PrintStream {

    public FilterErrPrintStream (PrintStream stream) {
      super (stream);
    }

    public void println (String x) {
      if (x.startsWith (WARNING)) {
        String w = x.substring (WARNING_LENGTH);
        if (w.startsWith (CANNOT_FIND_CLASS)) {
          String c = w.substring (CANNOT_FIND_CLASS_LENGTH);

          // Ignore cannot find class errors relating to Identity classes
          // as they are generated and StyleProperties as that is also
          // generated.
          if (c.endsWith ("Identity")
              || c.equals ("com.volantis.mcs.themes.StyleProperties")) {
            return;
          }
        }

        warnings += 1;
      }

      super.println (x);
    }
  }

  /**
   * Filter the output stream.
   */
  private static class FilterOutPrintStream
    extends PrintStream {

    public FilterOutPrintStream (PrintStream stream) {
      super (stream);
    }

    public void println (String x) {
      if (x.endsWith (" warnings")) {
        // Correct the number of warnings logged.
        if (warnings != 0) {
          super.println (warnings + " warnings");
        }
        return;
      }

      super.println (x);
    }
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Apr-05	7715/1	philws	VBM:2005040402 Port Public API generation changes from 3.3

 15-Apr-05	7676/1	philws	VBM:2005040402 Public API corrections and IBM Public API documentation subset generation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Nov-04	5882/3	ianw	VBM:2004102008 Rework to make ObjectsCodeGenerator abstract

 05-Aug-04	5081/1	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (make it simpler)

 11-Jun-04	4678/1	geoff	VBM:2004061001 old gui cleanup: remove folder support code

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 05-Feb-04	2878/1	claire	VBM:2004020514 Refactor code generation for Projects

 03-Feb-04	2767/7	claire	VBM:2004012701 Adding project handling code

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 30-Jan-04	2767/5	claire	VBM:2004012701 Add project

 30-Jan-04	2767/3	claire	VBM:2004012701 Add project

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 03-Nov-03	1698/8	pcameron	VBM:2003102411 Removed ignoreField variables and re-instated the IllegalStateExceptions

 03-Nov-03	1698/6	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 02-Sep-03	1295/1	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
