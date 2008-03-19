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
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Map;

/**
 * Code generator for various
 * {@link com.volantis.mcs.css.version.DefaultCSSVersionFactory} classes.
 * <p>
 * There will be one factory class generated for each CSSVersion that we
 * support.
 */
public class CSSVersionFactoryGenerator
    extends AbstractThemeClassGenerator {

    private String versionName;

    private String className;

    private String propertiesPackageName;

    private boolean supportsInherit;

    private Map enumerations;

    private ThemeVersionSelector versionSelector;

    /**
     * Creates a new <code>StylePropertyDetailsGenerator</code> instance.
     * @param generatedDir the directory were generated classes will be located.
     * @param packageName the genertated classes package name.
     */
    public CSSVersionFactoryGenerator(File generatedDir, String packageName,
            String versionName, String propertiesPackageName,
            boolean supportsInherit, Map enumerations,
            ThemeVersionSelector versionSelector) {
        super(generatedDir, packageName);

        this.versionName = versionName;
        this.className = this.versionName + "VersionFactory";
        this.propertiesPackageName = propertiesPackageName;
        this.supportsInherit = supportsInherit;
        this.enumerations = enumerations;
        this.versionSelector = versionSelector;
    }

    /**
     * Generates the StylePropertyDetails class
     * @param stylePropertyInfos a Collection of stylePropertyInfo objects
     */
    public void generate(Collection stylePropertyInfos) {
        System.out.println("  Generating the " + className + " class");

        String qualifiedClassName = packageName + "." + className;

        // open the file
        PrintStream out = openFileForClass(qualifiedClassName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package declaration.
        out.println();
        out.println("package " + packageName + ";");

        // write out the imports
        SortedSet imports = new TreeSet();
        imports.add("com.volantis.mcs.themes.StyleKeyword");
        imports.add("com.volantis.mcs.themes.StylePropertyDetails");
        imports.add("com.volantis.mcs.themes.StyleValueType");
        imports.add("com.volantis.mcs.themes.StyleSyntaxes");
        imports.add(propertiesPackageName + ".*");

        GenerateUtilities.writeImports(out, imports);

        // write out the class declaration
        GenerateUtilities.writeJavaDocComment(out, "", new String[]{
                "A CSSVersion factory for " + versionName + ".", });
        out.println("public class " + className + " " +
                        "implements DefaultCSSVersionFactory {");

        // write out the properties member
        GenerateUtilities.writeJavaDocComment(out, "    ", new String[]{
                "The css version to be constructed."});
        out.println("    private DefaultCSSVersion version;");

        // open the constructor
        GenerateUtilities.writeJavaDocComment(out, "    ", new String[]{
                "Initialise."});
        out.println("    public " + className + "() {");

        // start the version object
        out.println("        DefaultCSSVersion version = new DefaultCSSVersion(\"" +
                versionName + "\");");
        out.println();

        // write out each StylePropertyDetails instance that constitutes the
        // type safe enumeration
        StylePropertyInfo propertyInfo;
        for (Iterator i = stylePropertyInfos.iterator(); i.hasNext();) {
            propertyInfo = (StylePropertyInfo) i.next();
            writeOutCSSVersionProperty(out, propertyInfo);
            out.println();
        }

        // Add in the x-css.syntax type objects; there is currently only one.
        // It might be worth generating this from data if many more are added.
        GenerateUtilities.writeJavaDocComment(out, "        ",
                "Add the list of supported syntaxes for css values.");
        out.println("        version.addSyntax(StyleSyntaxes.COLOR_TRIPLETS);");
        out.println();

        // end the version object
        out.println("        this.version = version;");

        // close the constructor.
        out.println("    }");

        // write out the classes getName() method
        writeOutCreateCSSVersionMethod(out);

        // close the class declaration
        out.println("}");
    }

    /**
     * Generates a createCSSVersion() method
     * @param out the <code>PrintStream</code> to write to
     */
    private void writeOutCreateCSSVersionMethod(PrintStream out) {
        // write out the createCSSVersion() method
        out.println();
        out.println("    // Javadoc inherited");
        out.println("    public final DefaultCSSVersion createCSSVersion() {");
        out.println("        return version;");
        out.println("    }");
    }

    /**
     * Writes out an public static StylePropertyDetails instance for each
     * property that a rule supports
     * @param out the <code>PrintStream</code> to write to
     * @param info the <code>StylePropertyInfo</code> that encapsulates the
     * style property that we are writing out a StylePropertyDetails instance
     * for.
     */
    private void writeOutCSSVersionProperty(PrintStream out,
            StylePropertyInfo info) {

        String propertyName = info.getPropertyName();
        String detailsInstanceName = getStylePropertyInstanceName(propertyName);
        String variableName = getStylePropertyVariableName(propertyName);

        if (!versionSelector.supportsVersion(
                info.getThemeElementInfo())) {
            out.println("        // " + propertyName + " property " +
                    "(not supported)");
            return;
        }

        out.println("        // " + propertyName + " property");
        out.println("        final DefaultCSSProperty " + variableName +
                " =");
        out.println("                version.addProperty(StylePropertyDetails." +
                detailsInstanceName + ");");
        out.println("        " + variableName +
                ".addValueTypes(new StyleValueType[] {");
        if (supportsInherit) {
            out.println("            StyleValueType.get(\"inherit\"),");
        }
        ValueTypeInfo valueTypeInfo;
        String valueType;
        for (Iterator i = info.valueTypeIterator(); i.hasNext();) {
            valueTypeInfo = (ValueTypeInfo) i.next();
            valueType = valueTypeInfo.getName();

            out.print("            ");
            if (versionSelector.supportsVersion(valueTypeInfo)) {
                out.println("StyleValueType.get(\"" + valueType + "\"),");
            } else {
                out.println("// " + valueType + " " +
                        "(not supported)");
            }
        }
        out.println("        });");

        if (info.getEnumerationName() != null) {
            out.println("        " + variableName + ".addKeywords(new StyleKeyword[] {");
            EnumerationInfo enumInfo = (EnumerationInfo)
                    enumerations.get(info.getEnumerationName());
            final String interfaceName =
                    StyleKeywordsGenerator.getKeywordsName(enumInfo);
            for (Iterator i = enumInfo.elementIterator(); i.hasNext();) {
                EnumerationInfo.Element enumInfoElement =
                        (EnumerationInfo.Element) i.next();
                out.print("            ");
                if (versionSelector.supportsVersion(enumInfoElement)) {
                    String constantName = getConstant(enumInfoElement.getLabel());
                    out.println(interfaceName + "." + constantName + ",");
                } else {
                    out.println("// " + enumInfoElement.getLabel() + " " +
                            "(not supported)");
                }

            }
            out.println("        });");
        }
    }

    /**
     * Calculates the name of the StylePropertyDetails instance for a
     * given property. For example the StylePropertyDetails instance for the
     * property "background-color" will be "BACKGROUND_COLOR"
     * @param propertyName the name of the property
     * @return the name of the StylePropertyDetails instance for the
     * given property.
     */
    private static String getStylePropertyInstanceName(String propertyName) {
        String[] tokens = getPropertyWords(propertyName);
        return GenerateUtilities.getConstant(tokens);
    }

    /**
     * Calculates the name of the Java variable for a given property. For
     * example the Java variable for the property "background-color" will be
     * "backgroundColor"
     * @param propertyName the name of the property
     * @return the name of the StylePropertyDetails instance for the
     * given property.
     */
    private static String getStylePropertyVariableName(String propertyName) {
        String[] tokens = getPropertyWords(propertyName);
        return getVariable(tokens) + "Property";
    }

    private static String[] getPropertyWords(String propertyName) {
        StringTokenizer tokenizer = new StringTokenizer(propertyName, "-'");
        int tokenCount = tokenizer.countTokens();
        String[] tokens = new String[tokenCount];
        int count = 0;
        while (tokenizer.hasMoreTokens()) {
            tokens[count++] = tokenizer.nextToken();
        }
        return tokens;
    }

    private static String getVariable(String[] tokens) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < tokens.length; i += 1) {
            String token = tokens[i];
            if (i != 0) {
                token = GenerateUtilities.getTitledString(token);
            }
            buffer.append(token);
        }
        return buffer.toString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/7	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

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
