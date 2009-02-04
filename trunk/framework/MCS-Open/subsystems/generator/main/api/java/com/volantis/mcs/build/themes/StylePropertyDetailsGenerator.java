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
import com.volantis.mcs.build.themes.definitions.values.ValueSource;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map;

/**
 * Code generator for the
 * {@link com.volantis.mcs.themes.StylePropertyDetails} class.
 */
public class StylePropertyDetailsGenerator
        extends AbstractThemeClassGenerator {

    /**
     * Name of the class that is being generated
     */
    private static String CLASS_NAME = "StylePropertyDetails";

    /**
     * Creates a new <code>StylePropertyDetailsGenerator</code> instance.
     * @param generatedDir the directory were generated classes will be located.
     * @param packageName the genertated classes package name.
     * @param enumerationMap
     */
    public StylePropertyDetailsGenerator(
            File generatedDir,
            String packageName,
            Map enumerationMap) {
        super(generatedDir, packageName);

    }

    /**
     * Generates the StylePropertyDetails class
     * @param stylePropertyInfos a Collection of stylePropertyInfo objects
     */
    public void generate(Collection stylePropertyInfos) {
        System.out.println("  Generating the StylePropertyDetails class");

        StringBuffer buffer = new StringBuffer();
        buffer.append(packageName)
                .append(".")
                .append("StylePropertyDetails");

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
        imports.add("com.volantis.styling.properties.StyleProperty");
        imports.add("com.volantis.styling.properties.StylePropertyBuilder");
        imports.add("com.volantis.styling.properties.StylePropertyDefinitions");
        imports.add("com.volantis.styling.properties.StylePropertyDefinitionsBuilder");
        imports.add("com.volantis.mcs.themes.values.FrequencyUnit");
        imports.add("com.volantis.mcs.themes.values.LengthUnit");
        imports.add("com.volantis.mcs.themes.values.TimeUnit");
        imports.add("com.volantis.mcs.themes.values.AngleUnit");
        imports.add("com.volantis.mcs.themes.values.StyleKeywords");
        imports.add("com.volantis.mcs.themes.values.StyleColorNames");
        imports.add("com.volantis.mcs.themes.properties.*");
        imports.add("java.util.HashMap");

        GenerateUtilities.writeImports(out, imports);

        // write out the class declaration
        GenerateUtilities.writeJavaDocComment(
                out,
                "",
                new String[]{
                    "Type safe enumeration that provides access to the types of",
                    "<code>StyleValue</code> that each style property allows",
                });
        out.println("public class " + CLASS_NAME + " {");

        // write out the MAX constant
        GenerateUtilities.writeJavaDocComment(
                out,
                "    ",
                new String[]{
                    "Get the number of style properties " +
                    "that are currently supported."
                });
        out.println("    static int getPropertyCount() {");
        out.print("        return ");
        out.print(stylePropertyInfos.size());
        out.println(";");
        out.println("    }");

        // write out the StylePropertyDefinitions instance.
        GenerateUtilities.writeJavaDocComment(
                out,
                "    ",
                new String[]{
                    "The container for the property definitions."
                });
        out.println("    private static final StylePropertyDefinitions definitions;");

        StylePropertyInfo propertyInfo;

        // Populate the definitions.
        out.println("    static {");
        out.println("        StyleValueFactory styleValueFactory = StyleValueFactory.getDefaultInstance();");
        out.println("        StylePropertyDefinitionsBuilder definitionsBuilder" +
                  " = new StylePropertyDefinitionsBuilder();");
        out.println("        StylePropertyBuilder builder;");
        out.println("        StyleValueType[] types;");
        out.println("        HashMap rules;");
        for (Iterator i = stylePropertyInfos.iterator(); i.hasNext();) {
            propertyInfo = (StylePropertyInfo) i.next();
            writeOutStylePropertyInstances(out, propertyInfo);
        }
        out.println();
        out.println("        // Get the definitions from the builder.");
        out.println("        definitions = definitionsBuilder.getDefinitions();");
        out.println("    }");

        // write out each StyleProperty instance that constitutes the
        // type safe enumeration
        for (Iterator i = stylePropertyInfos.iterator(); i.hasNext();) {
            propertyInfo = (StylePropertyInfo) i.next();
            writeOutStylePropertyConstantDeclaration(out, propertyInfo);
        }

        writeOutGetDefinitionsMethod(out);

        // write out the getStyleProperty method
        writeOutGetNamedStylePropertyMethod(out);
        writeOutGetIndexedStylePropertyMethod(out);

        // close the class declaration
        out.println("}");
    }

    private void writeOutGetDefinitionsMethod(PrintStream out) {
        GenerateUtilities.writeJavaDocComment(
                out, "    ",
                new String[]{
                    "Get the container of StyleProperty definitions",
                    "@return The container of StyleProperty definitions"
                });
        out.println("    public static StylePropertyDefinitions " +
                    "getDefinitions() {");
        out.println("        return definitions;");
        out.println("    }");
    }

    /**
     * Generates a getStyleProperty(String property) method
     * @param out the <code>PrintStream</code> to write to
     */
    private void writeOutGetNamedStylePropertyMethod(PrintStream out) {
        // write out the getStyleProperty() method
        GenerateUtilities.writeJavaDocComment(
                out,
                "    ",
                new String[]{
                    "Returns the StyleProperty for the named property",
                    "@param name the name of the property",
                    "@return the StyleProperty for the named property or",
                    "null if the property does not exist",
                    "@deprecated Use {@link #getDefinitions} instead."
                });
        out.println("    public static StyleProperty " +
                    "getStyleProperty(String name) {");
        out.println("        return definitions.getStyleProperty(name);");
        out.println("    }");
    }

    /**
     * Generates a getStyleProperty(int index) method
     * @param out the <code>PrintStream</code> to write to
     */
    private void writeOutGetIndexedStylePropertyMethod(PrintStream out) {
        // write out the getStyleProperty() method
        GenerateUtilities.writeJavaDocComment(
                out,
                "    ",
                new String[]{
                    "Returns the StyleProperty for the indexed property",
                    "@param index the index of the property",
                    "@return the StyleProperty for the indexed property",
                    "@throws IllegalArgumentException if the index is out of range.",
                    "@deprecated Use {@link #getDefinitions} instead."
                });
        out.println("    public static StyleProperty " +
                    "getStyleProperty(int index) {");
        out.println("        return definitions.getStyleProperty(index);");
        out.println("    }");
    }

    /**
     * Writes out an public static StyleProperty instance for each
     * property that a rule supports
     * @param out the <code>PrintStream</code> to write to
     * @param info the <code>StylePropertyInfo</code> that encapsulates the
     * style property that we are writing out a StyleProperty instance
     * for.
     */
    private void writeOutStylePropertyConstantDeclaration(
            PrintStream out,
            StylePropertyInfo info) {

        String propertyName = info.getPropertyName();
        String detailsInstanceName = getStylePropertyInstanceName(propertyName);

        GenerateUtilities.writeJavaDocComment(
                out,
                "    ",
                "StyleProperty instance for '" + propertyName + "'");
        out.println("    public static final StyleProperty " +
                    detailsInstanceName +
                    " = definitions.getStyleProperty(\"" + propertyName +
                    "\");");
    }

    /**
     * Writes out an public static StyleProperty instance for each
     * property that a rule supports
     * @param out the <code>PrintStream</code> to write to
     * @param info the <code>StylePropertyInfo</code> that encapsulates the
     * style property that we are writing out a StyleProperty instance
     * for.
     */
    private void writeOutStylePropertyInstances(
            PrintStream out,
            StylePropertyInfo info) {

        String propertyName = info.getPropertyName();

        out.println();
        out.println("        // Create StyleProperty for '" +
                    propertyName + "'");
        out.println("        types = new StyleValueType[] {");
        out.println("            StyleValueType.get(\"inherit\"),");
        ValueTypeInfo valueTypeInfo;
        String valueType;
        for (Iterator i = info.valueTypeIterator(); i.hasNext();) {
            valueTypeInfo = (ValueTypeInfo) i.next();
            valueType = valueTypeInfo.getName();
            out.print("            StyleValueType.get(\"" + valueType + "\")");
            if (i.hasNext()) {
                out.println(',');
            } else {
                out.println();
            }
        }
        out.println("        };");

        out.println("        builder = new StylePropertyBuilder();");
        out.println("        builder.setName(\"" + propertyName + "\");");
        out.println("        builder.setTypes(types);");
        ValueSource initialValueSource = info.getInitialValueSource();
        initialValueSource.writeSetter("        ", out, "builder");

        String enumerationName = info.getEnumerationName();
        if (enumerationName != null) {
            out.println("        builder.setAllowableKeywords(new " +
                    StyleKeywordsGenerator.getKeywordsName(enumerationName) +
                    "());");
        }

        final boolean inherited = info.isInherited();
        out.println("        builder.setInherited(" + inherited + ");");
        out.println("        definitionsBuilder.addBuilder(builder);");
    }

    /**
     * Calculates the name of the StyleProperty instance for a
     * given property. For example the StyleProperty instance for the
     * property "background-color" will be "BACKGROUND_COLOR"
     * @param propertyName the name of the property
     * @return the name of the StyleProperty instance for the
     * given property.
     */
    static String getStylePropertyInstanceName(String propertyName) {
        StringTokenizer tokenizer = new StringTokenizer(propertyName, "-'");
        int tokenCount = tokenizer.countTokens();
        String[] tokens = new String[tokenCount];
        int count = 0;
        while (tokenizer.hasMoreTokens()) {
            tokens[count++] = tokenizer.nextToken();
        }
        return GenerateUtilities.getConstant(tokens);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/3	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 26-Oct-04	5968/1	adrianj	VBM:2004083105 Add inherited property to StylePropertyDetails

 25-Mar-04	3550/2	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 28-Jan-04	2524/4	mat	VBM:2004010712 Needed merge

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 23-Jan-04	2682/1	doug	VBM:2003112506 Added StylePropertiesSection to eclipse gui

 21-Jan-04	2659/2	allan	VBM:2003112801 RuleSection basics (read only)

 21-Jan-04	2592/1	doug	VBM:2003112712 Implementation of the ThemeElementRenderer and ThemeElementParser interfaces

 09-Jan-04	2467/5	doug	VBM:2003112408 Added the StyleValueType & StylePropertyDetails type-safe enums

 09-Jan-04	2467/3	doug	VBM:2003112408 Added the StyleValueType & StylePropertyDetails type-safe enums

 09-Jan-04	2467/1	doug	VBM:2003112408 Added the StyleValueType & StylePropertyDetails type-safe enums

 ===========================================================================
*/
