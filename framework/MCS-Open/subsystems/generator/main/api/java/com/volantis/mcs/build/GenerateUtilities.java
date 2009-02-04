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
 * $Header: /src/voyager/com/volantis/mcs/build/GenerateUtilities.java,v 1.9 2002/11/23 01:04:28 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Moved some helper code from
 *                              ParseMarinerSchema into GenerateUtilities.
 * 31-Jan-02    Paul            VBM:2001122105 - Added writeImports method.
 * 06-Feb-02    Paul            VBM:2001122103 - Added some more helper
 *                              methods.
 * 28-Feb-02    Paul            VBM:2002022804 - Added javax.servlet to the
 *                              list of prefixes for importing.
 * 18-Mar-02    Ian             VBM:2002031203 - Changed writeLogger to take the
 *                              package name as a parameter and pass a string
 *                              representing FQN of the class to Category.
 * 27-Apr-02    Doug            VBM:2002040803 - Modified getWords to not
 *                              split concurrent uppercase letters into
 *                              seperate words.
 * 02-May-02    Doug            VBM:2002040803 - Modified getWords to handle
 *                              numeric characters.
 * 03-May-02    Paul            VBM:2002050201 - Added writeImports which only
 *                              writes out the imports which are actually
 *                              needed.
 * 22-Nov-02    Paul            VBM:2002112214 - Improved formatting of the
 *                              logger statement for long fully qualified class
 *                              names.
 * 03-Jun-02    Allan           VBM:2003060301 - com.volantis.synergetics
 *                              added to import prefixes.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build;

import com.sun.javadoc.Tag;
import com.volantis.mcs.build.javadoc.FieldInfo;
import com.volantis.mcs.build.javadoc.ParameterInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * This class contains code which is helpful when generating code.
 */
public class GenerateUtilities {

  /**
   * The string which is used to open a plain Java comment.
   */
  public static final String COMMENT_OPEN = "/" + "*";

  /**
   * The string which is used to close a plain Java comment.
   */
  public static final String COMMENT_CLOSE = " " + "*" + "/";

  /**
   * The string which is used to open a JavaDoc comment.
   */
  public static final String JAVADOC_OPEN = "/" + "*" + "*";

  /**
   * The string which is used to close a JavaDoc comment.
   */
  public static final String JAVADOC_CLOSE = " " + "*" + "/";

  /**
   * The format of the date to add to the headers.
   */
  private static DateFormat dateFormat = new SimpleDateFormat ();

  /**
   * This is a list of the prefixes of packages which is used to group the
   * imports into blocks when writing them.
   */
  private static SortedSet importPrefixes;

  /**
   * This is a list of the prefixes of packages which is used to group the
   * imports into blocks when writing them.
   */
  static {
    importPrefixes = new TreeSet ();
    importPrefixes.add ("com.volantis.mcs.");
      importPrefixes.add ("com.volantis.styling.");
    importPrefixes.add ("com.volantis.synergetics.");
    importPrefixes.add ("java.");
    importPrefixes.add ("javax.servlet");
    importPrefixes.add ("org.apache.");
    importPrefixes.add ("org.");
  }

  /**
   * Write the header.
   * @param out The <code>PrintStream</code> to which it should be written.
   * @param generatorName The name of the class which is generating the header
   */
  public static void writeHeader (PrintStream out, String generatorName) {
    Date date = new Date ();

    out.println (COMMENT_OPEN + " -----------------------------------------"
                 + "-----------------------------------");
    out.println (" * This code was automatically generated by " +
                generatorName + "\n * on "
                 + dateFormat.format (date));
    out.println (" *");
    out.println (" * YOU MUST NOT MODIFY THIS FILE");
    out.println (" * -----------------------------------------"
                 + "-----------------------------------");
    out.println (COMMENT_CLOSE);
  }

  /**
   * Write the footer.
   * @param out The <code>PrintStream</code> to which it should be written.
   */
  public static void writeFooter (PrintStream out) {
  }

    /**
     * Write the copyright statement.
     * @param out The <code>PrintStream</code> to which it should be written.
     * @deprecated Don't write copyrights anymore.
     */
    public static void writeCopyright(PrintStream out) {
    }

  /**
   * Write the logger creation statement.
   * @param out The <code>PrintStream</code> to which it should be written.
   */
  public static void writeLogger (PrintStream out,
                                  String packageName,
                                  String className) {

    String name = packageName + "." + className;

    out.println ();
    out.println ("  " + JAVADOC_OPEN);
    out.println ("   * Used for logging.");
    out.println ("  " + JAVADOC_CLOSE);
    out.println ("  private static final LogDispatcher logger"
                   + " = LocalizationFactory.createLogger(");
    out.println ("       " + className + ".class);");

  }

    /**
     * Write the message localizer declaration statement. The
     * {@link com.volantis.synergetics.localization.MessageLocalizer} and
     * {@link com.volantis.mcs.localization.LocalizationFactory} classes
     * are assumed to have been imported into the code being generated.
     *
     * @param out the stream to write to
     * @param packageName the package for the generated class
     * @param className the name of the generated class
     */
    public static void writeMessageLocalizer(PrintStream out,
                                             String packageName,
                                             String className) {
        out.println();
        out.println("  " + JAVADOC_OPEN);
        out.println("   * Used to obtain localized messages.");
        out.println("  " + JAVADOC_CLOSE);
        out.println("  private static final MessageLocalizer messageLocalizer"
                    + " = LocalizationFactory.createMessageLocalizer(");
        out.println("       " + className + ".class);");
    }

    /**
     * Write the translator creation statement.
     *
     * @param out The <code>PrintStream</code> to which it should be written.
     */
    public static void writeTranslator(PrintStream out) {

        out.println();
        out.println("  " + JAVADOC_OPEN);
        out.println("    * The translator to use to convert from marlin-rpdm\n" +
                    "    * names to the internally used name.");
        out.println("  " + JAVADOC_CLOSE);
        out.println("    private static XMLRemoteSchemaTranslations translations" +
                " =\n    new XMLRemoteSchemaTranslations();");

    }

    /**
     * Write the exception localizer declaration statement. The {@link
     * com.volantis.synergetics.localization.ExceptionLocalizer} and
     * {@link com.volantis.mcs.localization.LocalizationFactory} classes are
     * assumed to have been imported into the code being generated.
     *
     * @param out         the stream to write to
     * @param packageName the package for the generated class
     * @param className   the name of the generated class
     */
  public static void writeExceptionLocalizer(PrintStream out,
                                             String packageName,
                                             String className) {
    out.println ();
    out.println ("  " + JAVADOC_OPEN);
    out.println ("   * Used to retrieve localized exception messages.");
    out.println ("  " + JAVADOC_CLOSE);
    out.println ("  private static final ExceptionLocalizer exceptionLocalizer"
                   + " = LocalizationFactory.createExceptionLocalizer(");
    out.println ("       " + className + ".class);");

  }

    /**
     * Write a public copyright statement.
     * @param out The <code>PrintStream</code> to which it should be written.
     * @deprecated Don't write copyrights anymore.
     */
    public static void writePublicCopyright(PrintStream out) {
    }

  /**
   * Write the paragraph out to the output stream making sure that it
   * is less than 80 characters wide.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of every line.
   * @param paragraph The paragraph to write.
   */
  public static void formatParagraph (PrintStream out,
                                      String indent,
                                      String paragraph) {

    int lineLength = 79 - indent.length ();

    paragraph = paragraph.trim ();
    do {
      String line;
      if (paragraph.length () >= lineLength) {
        int index = paragraph.lastIndexOf (' ', lineLength);
        if (index == -1) {
          index = lineLength - 1;
          line = paragraph.substring (0, index) + "-";
          paragraph = paragraph.substring (index);
        } else {
          line = paragraph.substring (0, index).trim ();
          paragraph = paragraph.substring (index + 1).trim ();
        }
      } else {
        line = paragraph;
        paragraph = null;
      }

      out.print (indent);
      out.println (line);

    } while (paragraph != null);
  }

  /**
   * Write the text out to the output stream making sure that it
   * is less than 80 characters wide.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of every line.
   * @param text Some text to write out. Paragraphs within the text are
   * separated by new line characters.
   */
  public static void formatText (PrintStream out,
                                 String indent,
                                 String text) {

    StringTokenizer tokenizer = new StringTokenizer (text, "\n");
    while (tokenizer.hasMoreTokens ()) {
      formatParagraph (out, indent, tokenizer.nextToken ());
    }
  }

  /**
   * Write the sequence which opens a JavaDoc comment.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of the line.
   */
  public static void openJavaDocComment (PrintStream out,
                                         String indent) {
    out.println ();
    out.println (indent + JAVADOC_OPEN);
  }

  /**
   * Write the sequence which closes a JavaDoc comment.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of the line.
   */
  public static void closeJavaDocComment (PrintStream out,
                                          String indent) {
    out.println (indent + JAVADOC_CLOSE);
  }

  /**
   * Write part of a JavaDoc comment.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of the line.
   * @param paragraph The body of the comment.
   */
  public static void addJavaDocComment (PrintStream out,
                                        String indent,
                                        String paragraph) {
    formatText (out, indent + " * ", paragraph);
  }

  /**
   * Write a JavaDoc comment.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of the line.
   * @param lines An array of lines which are treated as paragraphs.
   */
  public static void writeJavaDocComment (PrintStream out,
                                          String indent,
                                          String [] lines) {

    openJavaDocComment (out, indent);
    for (int i = 0; i < lines.length; i += 1) {
      addJavaDocComment (out, indent, lines [i]);
    }
    closeJavaDocComment (out, indent);
  }

  /**
   * Write a JavaDoc comment.
   * @param out The PrintStream to write the paragraph to.
   * @param indent The indent which appears at the beginning of the line.
   * @param paragraph The paragraph to write.
   */
  public static void writeJavaDocComment (PrintStream out,
                                          String indent,
                                          String paragraph) {

    openJavaDocComment (out, indent);
    addJavaDocComment (out, indent, paragraph);
    closeJavaDocComment (out, indent);
  }

  /**
   * A fixed string which is used to quickly get an indent of a particular
   * size.
   */
  private static String indentString = ("                    "
                                        + "                    "
                                        + "                    "
                                        + "                    ");

  /**
   * Get a string of the specified number of spaces.
   * @param length The number of spaces in the returned string.
   * @return A string of spaces of the specified length.
   */
  public static String getIndent (int length) {
    return indentString.substring (0, length);
  }

  /**
   * Return a titled string.
   * @param string The string to convert.
   * @return The titled string which is the specified string with the first
   * character is upper case.
   */
  public static String getTitledString (String string) {
    char c = string.charAt (0);
    return Character.toUpperCase (c) + string.substring (1);
  }

  /**
   * Write the set of imports out to the specified stream.
   * @param out The PrintStream to write to.
   * @param imports The set of imports to write.
   */
  public static void writeImports (PrintStream out,
                                   SortedSet imports) {
    writeImports (out, imports, null);
  }

  /**
   * Write the set of imports out to the specified stream.
   * @param out The PrintStream to write to.
   * @param imports The set of imports to write.
   */
  public static void writeImports (PrintStream out,
                                   SortedSet imports,
                                   String currentPackageName) {

    String last = null;
    if (imports.size () != 0) {

      // Get the first prefix to use.
      Iterator p = importPrefixes.iterator ();
      String prefix = null;

      String previousPackageName = null;
      String previousPrefix = null;
      for (Iterator i = imports.iterator (); i.hasNext ();) {
        String qualifiedClass = (String) i.next ();

        // Check to see whether the class is in the current package or not.
        // If it is then don't generate an import for it.
        if (currentPackageName != null) {
          int length = currentPackageName.length ();
          if (qualifiedClass.length () > length
              && qualifiedClass.charAt (length) == '.'
              && qualifiedClass.startsWith (currentPackageName)
              && qualifiedClass.indexOf ('.', length + 1) == -1) {
            continue;
          }
        }

        // Get the prefix which matches the current class, as the classes
        // are ordered using the same comparator as the prefixes we can
        // step through them side by side rather than check every prefix
        // every time around the loop.
        while ((prefix == null || !qualifiedClass.startsWith (prefix))) {
          if (p.hasNext ()) {
            prefix = (String) p.next ();
          } else {
            throw new IllegalArgumentException ("Class " + qualifiedClass
                                                + " does not match any known"
                                                + " package prefix");
          }
        }

        // Flag which controls whether we have to seperate this from the
        // previous import.
        boolean separate = false;

        // If the prefixes have changed then we need to output a blank line,
        // else we need to compare the first package in the class path after
        // removing the prefix.
        if (!prefix.equals (previousPrefix)) {
          separate = true;
          previousPrefix = prefix;
        }

        // Get the path relative to the prefix.
        String relativeClass = qualifiedClass.substring (prefix.length ());

        // The first package name in the relative class name is used to
        // determine whether a blank line is output before this import.
        int index = relativeClass.indexOf ('.');
        String packageName;
        if (index == -1) {
          packageName = "";
        } else {
          packageName = relativeClass.substring (0, index);
        }

        if (!packageName.equals (previousPackageName)) {
          separate = true;
          previousPackageName = packageName;
        }

        if (separate) {
          out.println ();
        }
        out.println ("import " + qualifiedClass + ";");
      }
    }
  }

  /**
   * Get the file for the fully qualified class name.
   * @param rootDir The root directory for the file.
   * @param qualifiedClassName The fully qualified class name.
   * @return A file object.
   */
  public static
    File getFileFromQualifiedClassName (File rootDir,
                                        String qualifiedClassName) {

    String fileName = qualifiedClassName;
    fileName = fileName.replace ('.', File.separatorChar);
    fileName = (rootDir + File.separator + fileName + ".java");

    return new File (fileName);
  }

  /**
   * Open a file for the fully qualified class name relative to the root
   * directory.
   * @param rootDir The root directory for the file.
   * @param qualifiedClassName The fully qualified class name.
   * @return A PrintStream.
   */
  public static PrintStream openFileForClass (File rootDir,
                                              String qualifiedClassName) {

    System.err.println ("Generating " + qualifiedClassName);

    File file = getFileFromQualifiedClassName (rootDir, qualifiedClassName);
    PrintStream out;

    // Make sure that the directory is there.
    File dir = file.getParentFile ();
    dir.mkdirs ();

    try {
      out = new PrintStream (new FileOutputStream (file));
    }
    catch (IOException ioe) {
      throw new IllegalStateException (ioe.getLocalizedMessage ());
    }

    return out;
  }

  /**
   * Parse a string which has titled words and return an array of the
   * lower case words.
   * @param name The string to parse.
   * @return The array of individual words. e.g. FredBloggs would be returned
   * as an array containing the two strings "fred" and "bloggs".
   */
  public static String [] getWords (String name) {

    name = getTitledString(name);

    CharacterIterator iterator = new StringCharacterIterator (name);

    // Count how many words there are in the string.
    int count = 0;
    char lastChar = 'a';
    for (char c = iterator.first (); c != CharacterIterator.DONE;
         c = iterator.next ()) {

      if ((Character.isUpperCase (c) || Character.isDigit(c))
	   && !(Character.isUpperCase(lastChar) ||
		Character.isDigit(lastChar))) {
        count += 1;
      }
      lastChar = c;
    }
      if (count == 0) {
          count = 1;
      }

    String [] words = new String [count];
    count = 0;
    int begin = 0;
    int end;
    lastChar = 'a';
    for (char c = iterator.first (); c != CharacterIterator.DONE;
         c = iterator.next ()) {


      if ((Character.isUpperCase (c) || Character.isDigit(c))
	   && !(Character.isUpperCase(lastChar) ||
		Character.isDigit(lastChar))) {

        end = iterator.getIndex ();
        if (end != 0) {
          words [count] = name.substring (begin, end).toLowerCase ();
          //System.out.println ("Word " + count + " is " + words [count]);
          begin = end;
          count += 1;
        }
      }
      lastChar = c;
    }
    end = iterator.getIndex ();
    words [count] = name.substring (begin, end).toLowerCase ();
    //System.out.println ("Word " + count + " is " + words [count]);

    return words;
  }

  /**
   * Return a string consisting of the words in the array separated by a
   * single white space.
   * @param words An array of words.
   * @return A space separated string containing the words.
   */
  public static String getNaturalName (String [] words) {
    StringBuffer buffer = new StringBuffer ();
    for (int i = 0; i < words.length; i += 1) {
      if (i != 0) {
        buffer.append (" ");
      }
      buffer.append (words [i]);
    }
    return buffer.toString ();
  }

  /**
   * Return an upper case string consisting of the words in the array
   * separated by a single white space.
   * @param words An array of words.
   * @return A space separated string containing the words.
   */
  public static String getConstant (String [] words) {
    StringBuffer buffer = new StringBuffer ();
    for (int i = 0; i < words.length; i += 1) {
      if (i != 0) {
        buffer.append ("_");
      }
      buffer.append (words [i].toUpperCase ());
    }
    return buffer.toString ();
  }

    /**
     * Checks whether the given field is of type Project.  This is necessary
     * in some code generation as Projects are not handled in the same way
     * as other identity fields.
     * <p>
     * @see com.volantis.mcs.objects.Project
     * </p>
     * @param field The field to test the type of.
     * @return True if field is an instance of type Project, false otherwise.
     */
    public static boolean isProjectField(FieldInfo field) {
        String type = field.getTypeName();
        if (type != null) {
            return type.equals("Project");
        } else {
            return false;
        }
    }

    /**
     * Checks whether the given parameter is of type Project.  This is necessary
     * in some code generation as Projects are not handled in the same way
     * as other identity fields.
     * <p>
     * @see com.volantis.mcs.objects.Project
     * </p>
     * @param parameter The parameter to test the type of.
     * @return True if parameter is an instance of type Project, false otherwise.
     */
    public static boolean isProjectField(ParameterInfo parameter) {
        String type = parameter.getTypeName();
        if (type != null) {
            return type.equals("Project");
        } else {
            return false;
        }
    }

    /**
     * Returns a string composed of the specified list of tags, each separated
     * by a carriage return. The string does not have a trailing carriage
     * return.
     *
     * @param tags the set of tags that should be output in a string
     * @return the string of tags, each separated by a carriage return
     */
    public static String getTagsString(List tags) {
        StringBuffer buffer = new StringBuffer();
        Iterator i;
        Tag tag;

        for (i = tags.iterator();
             i.hasNext();
             ) {
            tag = (Tag) i.next();

            buffer.append(tag.name()).append(' ').append(tag.text());

            if (i.hasNext()) {
                buffer.append('\n');
            }
        }

        return buffer.toString();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 22-Apr-05	7715/4	philws	VBM:2005040402 Merge conflict fixes

 18-Apr-05	7715/1	philws	VBM:2005040402 Port Public API generation changes from 3.3

 15-Apr-05	7676/1	philws	VBM:2005040402 Public API corrections and IBM Public API documentation subset generation

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 30-Mar-05	7511/4	emma	VBM:2005032204 Merge from 3.3.0 - Changing how *FormatBuilder classes (generated and hand-written) translate attribute names

 17-Jan-05	6670/1	adrianj	VBM:2005010506 Implementation of resource asset continued

 29-Mar-05	7484/1	emma	VBM:2005032204 Changing how *FormatBuilder classes (generated and hand-written) translate attribute names

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 18-Feb-04	2789/4	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Feb-04	2878/1	claire	VBM:2004020514 Refactor code generation for Projects

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/3	mat	VBM:2004010712 Amended javadoc

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 ===========================================================================
*/
