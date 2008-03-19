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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/KeywordMapperFactoryGenerator.java,v 1.2 2002/06/29 01:04:51 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-02    Doug            VBM:2002040803 - Created to support the 
 *                              genertaion of CSS KeywordMapperFactory classes
 * 28-Jun-02    Paul            VBM:2002051302 - Generate the css specific
 *                              mappers and related classes into a css.mappers
 *                              package instead of a themes.mappers package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Class to generate CSS KeywordMapperFactory classes
 */
public class KeywordMapperFactoryGenerator 
  extends AbstractThemeClassGenerator {


    /**
   * Creates a new EnumerationGenerator instance.
   * @param generatedDir the directory were generated classes will be located.
   * @param packageName the genertated classes package name.
   */
  public KeywordMapperFactoryGenerator(File generatedDir, String packageName) {
    super(generatedDir, packageName);
  }

  /**
   * Return the name of the interface that Keyword Mapper factories implement
   * @return the name
   */
  public static String getInterfaceName() {
    return "KeywordMapperFactory";
  }

    /**
     * Return the name of the stub factory that Keyword Mapper factories
     * implement
     * @return the name
     */
    public static String getStubClassName() {
      return "KeywordMapperFactoryStub";
    }

  /**
   * Return the name of the CSS Keyword mapper factory
   * @return the name
   */
  public static String getCSSClassName() {
    return "CSSKeywordMapperFactory";
  }

  /**
   * Generate the interface that KeywordMapper factories must implement
   * @param mappers A Map of EnumerationInfo objects
   * @param packageName the package name
   */
  public void generateFactoryInterface(Map mappers, String packageName) {

    this.packageName = packageName;
	
    String interfaceName = getInterfaceName();

    String qualifiedName
      = packageName + "." + interfaceName;

    // Open a file.
    out = openFileForClass (qualifiedName);

    // Write the header.
    GenerateUtilities.writeHeader(out, this.getClass().getName());

    // Write the package declaration.
    out.println();
    out.println("package " + packageName + ";");


    // Write the class header.
    out.println();
    out.print("public interface " + interfaceName + " {");
        
    out.println();
    String name = null;
    for(Iterator i =mappers.values().iterator(); i.hasNext();) {
      
      EnumerationInfo ei = (EnumerationInfo)i.next();
      name = ei.getName();

      GenerateUtilities.writeJavaDocComment(out, "  ", new String[] {
       "Get the KeywordMapper for the " + name + " property",
	"@return a KeywordMapper"});
      out.println("  public KeywordMapper get" + 
		 GenerateUtilities.getTitledString(name) + 
		  "KeywordMapper();");
    }

    out.println("}");
  }

    /**
     * Generate the interface that KeywordMapper factories must implement
     * @param mappers A Map of EnumerationInfo objects
     * @param packageName the package name
     */
    public void generateFactoryStub(Map mappers, String packageName) {

        this.packageName = packageName;

        String stubClassName = getStubClassName();
        String interfaceName = getInterfaceName();

        String qualifiedName
            = packageName + "." + stubClassName;

        // Open a file.
        out = openFileForClass (qualifiedName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package declaration.
        out.println();
        out.println("package " + packageName + ";");

        // write out the imports
        SortedSet imports = new TreeSet ();
        imports.add("com.volantis.mcs.themes.mappers.KeywordMapper");
        imports.add("com.volantis.mcs.themes.mappers.KeywordMapperFactory");
        GenerateUtilities.writeImports (out, imports);

        // Write the class header.
        out.println();
        out.println("public class " + stubClassName);
        out.println("  implements " + interfaceName + " {");

        out.println();
        String name = null;
        for(Iterator i = mappers.values().iterator(); i.hasNext();) {

            EnumerationInfo ei = (EnumerationInfo)i.next();
            name = ei.getName();

            GenerateUtilities.writeJavaDocComment(out, "  ", new String[] {
                "Default implementation that simply returns null.",
                "@return null"});
            out.println("    public KeywordMapper get" +
                        GenerateUtilities.getTitledString(name) +
                        "KeywordMapper() {");
            out.println("        return null;");
            out.println("    }");
        }

        out.println("}");
    }

  /**
   * Generate the CSS KeywordMapper factory
   * @param mappers A Map of EnumerationInfo objects
   * @param packageName the package name
   */
  public void generateCSSFactoryClass(Map mappers, String packageName) {
    
    this.packageName = packageName;

    String className = getCSSClassName();

    String interfaceName = getInterfaceName();

    String qualifiedName
      = packageName + "." + className;

    // Open a file.
    out = openFileForClass(qualifiedName);

    // Write the header.
    GenerateUtilities.writeHeader(out, this.getClass().getName());

    // Write the package declaration.
    out.println();
    out.println("package " + packageName + ";");

    // write out the imports
    SortedSet imports = new TreeSet ();
    imports.add("com.volantis.mcs.themes.mappers.KeywordMapper");
    imports.add("com.volantis.mcs.themes.mappers." + interfaceName);
    GenerateUtilities.writeImports (out, imports);
    

    // Write the class header.
    out.println();
    out.println("public class " + className);
    out.println("  implements " + interfaceName + " {");

    // write out the singleton variable    
    out.println();
    out.println("  private static " +  interfaceName + " singleton;");
    out.println();
    out.println("  static {");
    out.println("    // Always initialise to prevent the synchronization");
    out.println("    // problem that could occur through lazy initialisation");
    out.println("    singleton = new " + className + "();");
    out.println("  }");

    // write out the constructor    
    out.println();
    out.println("  protected " + className  + "() {");
    out.println("  }");


    // write out the getSingleton method
    out.println();
    out.println("  public static " + interfaceName + " getSingleton() {");
    out.println("    return singleton;");
    out.println("  }");

    for(Iterator i =mappers.values().iterator(); i.hasNext();) {
      EnumerationInfo ei = (EnumerationInfo)i.next();
      writeGetMethod(ei);
    }

    out.println("}");
  }

  /**
   * Helper method that writes out the factory methods.
   * @param enumInfo Describes a KeywordMapper
   * to determine if a keyword should be written out.
   */
  public void writeGetMethod(EnumerationInfo enumInfo) {
    String name = enumInfo.getName();
    for(Iterator i = enumInfo.elementIterator(); i.hasNext();) {
      out.println();
      out.println("  // javadoc inherited");
      out.println("  public KeywordMapper get" +
              GenerateUtilities.getTitledString(name)
              + "KeywordMapper() {");
      out.println("    return " +
              EnumerationGenerator.getKeywordMapperName(enumInfo) +
              ".getSingleton();");
      out.println("  }");
      return;
    }
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 ===========================================================================
*/
