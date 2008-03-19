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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;

import java.io.File;
import java.io.PrintStream;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Code generator for the KeywordMapperAccessor class.
 */
public class KeywordMapperAccessorGenerator
    extends AbstractThemeClassGenerator {

    /**
     * Name of the class that is being generated
     */
    private static String CLASS_NAME = "KeywordMapperAccessor";

    /**
     * Creates a new <code>KeywordMapperAccessorGenerator</code> instance.
     * @param generatedDir the directory were generated classes will be located.
     * @param packageName the genertated classes package name.
     */
    public KeywordMapperAccessorGenerator(File generatedDir,
                                                String packageName) {
        super(generatedDir, packageName);
    }

    /**
     * Generates the StylePropertyDetails class
     * @param propertyInfo provides details of the keyword mappers
     */
    public void generate(StylePropertyInfo[] propertyInfo) {
        System.out.println("  Generating the " + CLASS_NAME + " class");

        StringBuffer buffer = new StringBuffer();
        buffer.append(packageName)
            .append(".")
            .append(CLASS_NAME);

        String qualifiedClassName = buffer.toString();

        // open the file
        PrintStream out = openFileForClass(qualifiedClassName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package declaration.
        out.println();
        out.println("package " + packageName + ";");

        // write out the imports
        SortedSet imports = new TreeSet();
        imports.add("java.util.Map");
        imports.add("java.util.HashMap");
        imports.add("com.volantis.mcs.themes.mappers.KeywordMapper");
        imports.add("com.volantis.mcs.themes.mappers.KeywordMapperFactory");
        GenerateUtilities.writeImports(out, imports);

        // write out the class declaration
        GenerateUtilities.writeJavaDocComment(
            out,
            "",
            new String[]{
                "Class that wraps a {@link KeywordMapperFactory} so that a",
                "KeywordMapper can be retrieved for a given property name"
            });
        out.println("public class " + CLASS_NAME + " {");

        // write out the properties member
        GenerateUtilities.writeJavaDocComment(
            out,
            "    ",
            new String[]{
                "A map that will contain the property to " +
                "KeywordMapperFactory mappings",
            });

        out.println("    private Map properties;");

        // write out the constructor
        writeConstructor(out, propertyInfo);

        // write out the getKeywordMapper method
        writeGetKeywordMapperMethod(out);

        // close the class
        out.println("}");
    }

    /**
     * Generates the constructor
     * @param out the <code>PrintStream</code> to write to
     * @param propertyInfo the array of StylePropertyInfo objects
     */
    private void writeConstructor(PrintStream out,
                                  StylePropertyInfo[] propertyInfo) {
        // write out the constructor
        GenerateUtilities.writeJavaDocComment(
            out,
            "    ",
            new String[]{
                "Creates a new <code>KeywordMapperAccessorGenerator" +
                "</code> instance",
                "@param factory the <code>KeywordMapperFactory</code> that " +
                "is to be wrapped"
            });

        StringBuffer buffer = new StringBuffer();
        buffer.append("    public ")
              .append(CLASS_NAME)
              .append("(KeywordMapperFactory factory) {");

        out.println(buffer.toString());
        out.println("        // initialize the property map");
        out.println("        properties = new HashMap();");

        for (int i = 0; i < propertyInfo.length; i++) {
            String enumName = propertyInfo[i].getEnumerationName();
            if (enumName != null) {
                String property = propertyInfo[i].getPropertyName();
                out.println("        // add the " + property +
                            " property to the map");
                out.println("        properties.put(\"" + property + "\",");
                out.println("                       factory.get" +
                            GenerateUtilities.getTitledString(enumName) +
                            "KeywordMapper());");
            }
        }
        // unfortunately these are hard coded.
        out.println("        // TODO the remaing code in this constructor is " +
                    "hard coded in the");
        out.println("        // TODO  generator. It should be obtained from " +
                    "the schema");
        out.println("        // add the background-position.first property " +
                    "to the map");
        out.println("        properties.put(\"background-position.first\",");
        out.println("                       factory.getBackgroundXPosition" +
                    "KeywordMapper());");

        out.println("        // add the background-position.second property " +
                    "to the map");
        out.println("        properties.put(\"background-position.second\",");
        out.println("                       factory.getBackgroundYPosition" +
                    "KeywordMapper());");

        out.println("        // add the mcs-mmflash-scaled-align.first " +
                    "property to the map");
        out.println("        properties.put(\"mcs-mmflash-scaled-align.first\",");
        out.println("                       factory.getMarinerMMFlashXScaled" +
                    "AlignKeywordMapper());");

        out.println("        // add the mcs-mmflash-scaled-align.second " +
                    "property to the map");
        out.println("        properties.put(\"mcs-mmflash-scaled-align.second\",");
        out.println("                       factory.getMarinerMMFlashYScaled" +
                    "AlignKeywordMapper());");


        out.println("    }");
    }

    /**
     * Generates the getKeywordMapper method
     * @param out the <code>PrintStream</code> to write to
     */
    private void writeGetKeywordMapperMethod(PrintStream out) {
        // write out the getKeywordMapper method
        GenerateUtilities.writeJavaDocComment(
            out,
            "    ",
            new String[]{
                "Returns a <code>KeywordMapper<code> for the given property",
                "@param property the property",
                "@return a KeywordMapperFactory or null if the property does " +
                "not have an associated KeywordMapper"
            });
        out.println("    public KeywordMapper getKeywordMapper" +
                    "(String property) {");
        out.println("        return (KeywordMapper) properties.get(property);");
        out.println("    }");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jan-04	2524/1	mat	VBM:2004010712 Needed merge

 21-Jan-04	2592/1	doug	VBM:2003112712 Implementation of the ThemeElementRenderer and ThemeElementParser interfaces

 ===========================================================================
*/
