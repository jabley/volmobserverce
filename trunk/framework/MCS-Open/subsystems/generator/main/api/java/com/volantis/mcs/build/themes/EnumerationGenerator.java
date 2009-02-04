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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/EnumerationGenerator.java,v 1.5 2002/05/21 13:06:12 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created to support the
 *                              genertaion of CSS KeywordMapper classes
 * 29-Apr-02    Doug            VBM:2002040803 - Fixed bug in the generation
 *                              of the Mapper constructors
 * 29-Apr-02    Doug            VBM:2002040803 - Fixed bug in the generated
 *                              mapper getSingleton method.
 * 16-May-02    Doug            VBM:2002040803 - Moved the inner interface
 *                              EnumerationElementSelector into its own
 *                              file.
 * 21-May-02    Adrian          VBM:2002021111 - Added kludge to method
 *                              generateKeywordMapper to remove "weight-"
 *                              prefix from keywords in FontWeightKeywordMapper
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class to generate CSS KeywordMapper and KeywordEnumeration classes
 */
public class EnumerationGenerator extends AbstractThemeClassGenerator {

  /**
   * Creates a new EnumerationGenerator instance.
   * @param generatedDir the directory were generated classes will be located.
   * @param packageName the genertated classes package name.
   */
  public EnumerationGenerator(File generatedDir, String packageName) {
    super(generatedDir, packageName);
  }

    /**
   * Provide a name for a Keyword Mapper  class
   * @param enumInfo the EnumerationInfo object
   * @return the class name.
   */
  public static String getKeywordMapperName(EnumerationInfo enumInfo) {
    StringBuffer sb = new StringBuffer();
      sb.append(enumInfo.getName())
        .append("KeywordMapper");
    return sb.toString();
  }

  /**
   * Generate a KeywordMapper class
   * @param enumInfo the EnumerationInfo object
   */
  public void
    generateKeywordMapper(EnumerationInfo enumInfo, String packageName) {

    this.packageName = packageName;

    String keywordsName = StyleKeywordsGenerator.getKeywordsName(enumInfo);
    String mapperName = getKeywordMapperName(enumInfo);
//    String constPrefix = getConstant(enumInfo);

    System.out.println ("  Generating Keyword Mapper " + mapperName);

    String qualifiedName
      = packageName + "." + mapperName;

    // Open a file.
    out = openFileForClass (qualifiedName);

    // Write the header.
    GenerateUtilities.writeHeader(out, this.getClass().getName());

    // Write the package declaration.
    out.println();
    out.println("package " + packageName + ";");

    // write out the imports
    SortedSet imports = new TreeSet ();
    imports.add ("com.volantis.mcs.themes.properties." +  keywordsName);
      imports.add ("com.volantis.mcs.themes.mappers.KeywordMapper");
    imports.add ("com.volantis.mcs.themes.mappers.StandardKeywordMapper");
    GenerateUtilities.writeImports (out, imports);

    out.println();
    String[] javadoc =
      {"Map a keyword value from its internal representation its",
       "canonical external representation."};

    // write out the class javadoc
    GenerateUtilities.writeJavaDocComment(out, "", javadoc);

    // Write the class header.
    out.println("public final class " + mapperName);
    out.println("  extends StandardKeywordMapper {");

    // write out the singleton initialisation
    out.println();
    out.println("  private static KeywordMapper singleton;");
    out.println("  static {");
    out.println("    // Always initialise to prevent the synchronization");
    out.println("    // problem that could occur through lazy initialisation");
    out.println("    singleton = new " + mapperName + "();");
    out.println("  }");


    // write out the singleton method
    out.println();
    out.println("  public static KeywordMapper getSingleton() {");
    out.println("    return singleton;");
    out.println("  }");


    // write out the constructor
    out.println();
    out.println("  protected " + mapperName + "() {");
      out.println("    super(" + keywordsName + ".getDefaultInstance());");
//    // add the mappings
//    for(Iterator i = enumInfo.elementIterator(); i.hasNext();) {
//      EnumerationInfo.Element e = (EnumerationInfo.Element)i.next();
//      String label = e.getLabel();
//
//      String constant = getConstant(label);
//
//        label = StyleKeywordsGenerator.getCSSForKeyword(label);
//      out.println("    addMapping(" + keywordsName + "." + constant +
//                  ", \"" + label + "\");");
//    }
    out.println("  }");
    out.println("}");
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

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 ===========================================================================
*/
