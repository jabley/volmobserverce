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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class StyleKeywordsGenerator
        extends AbstractThemeClassGenerator {

    private static final Set IGNORE = new HashSet();

    static {
        IGNORE.add("ColorName");
        IGNORE.add("LengthUnit");
        IGNORE.add("TimeUnit");
        IGNORE.add("AngleUnit");
        IGNORE.add("FrequencyUnit");
    }

    public StyleKeywordsGenerator(File generatedDir) {
        super(generatedDir, null);
    }

    public void generateUberKeywords(Map enumerationInfo, String packageName) {

        Set keywords = new TreeSet();
        for (Iterator i = enumerationInfo.values().iterator(); i.hasNext();) {
            EnumerationInfo info = (EnumerationInfo) i.next();
            String enumerationName = info.getName();
            if (!IGNORE.contains(enumerationName)) {
                Iterator j = info.elementIterator();
                while (j.hasNext()) {
                    EnumerationInfo.Element element = (EnumerationInfo.Element)
                            j.next();
                    keywords.add(element.getLabel());
                }
            }
        }

        for (Iterator iterator = keywords.iterator(); iterator.hasNext();) {
            String keyword = (String) iterator.next();
            System.out.println("Keyword: " + keyword);
        }


        this.packageName = packageName;

        String className = "StyleKeywords";

        System.out.println("  Generating class " + className);

        String qualifiedName = packageName + "." + className;

        // Open a file.
        out = openFileForClass(qualifiedName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package declaration.
        out.println();
        out.println("package " + packageName + ";");

        // write out the imports
        SortedSet imports = new TreeSet();
        imports.add("java.util.HashMap");
        imports.add("java.util.Map");
        imports.add("com.volantis.mcs.themes.StyleKeyword");
        imports.add("com.volantis.mcs.themes.StyleValueFactory");
        GenerateUtilities.writeImports(out, imports);

        // Write the class header.
        out.println();
        out.print("public class " + className + " {");

        // write out the member variables
        out.println();

        out.println("  private static final Map keywords = new HashMap();");
        out.println("  private static final StyleValueFactory styleValueFactory =\r\n" +
            "    StyleValueFactory.getDefaultInstance();");

        out.println();
        for (Iterator i = keywords.iterator(); i.hasNext();) {
            String keyword = (String) i.next();
            String constant = getConstant(keyword);

            String css = getCSSForKeyword(keyword);

            out.println(
                    "  public static final StyleKeyword " + constant +
                    " = get(\"" + css + "\");");
        }

        out.println();
        out.println("  private static StyleKeyword get(String name) {");
        out.println("    // Keywords should not be case sensitive.");
        out.println("    name = name.toLowerCase();");
        out.println("    StyleKeyword keyword = (StyleKeyword) keywords.get(name);");
        out.println("    if (keyword == null) {");
        out.println("      keyword = styleValueFactory.getKeyword(name);");
        out.println("      keywords.put(name, keyword);");
        out.println("    }");
        out.println("    return keyword;");
        out.println("  }");

        out.println();
        out.println("  public static StyleKeyword getKeywordByName(String name) {");
        out.println("    // Keywords should not be case sensitive.");
        out.println("    name = name.toLowerCase();");
        out.println("    return (StyleKeyword) keywords.get(name);");
        out.println("  }");

        out.println("}");
    }

    public static String getCSSForKeyword(String keyword) {
        if (keyword.startsWith("weight-")) {
            keyword = keyword.substring(7);
        }
        return keyword;
    }

    /**
     * Generate a Keyword interface.
     *
     * @param enumInfo the EnumerationInfo object
     */
    public void generateKeywordsClass(
            EnumerationInfo enumInfo,
            String packageName) {

        // Don't generate ...Keywords classes for those enumerations that
        // are being ignored.
        if (IGNORE.contains(enumInfo.getName())) {
            return;
        }


        this.packageName = packageName;

        String className = getKeywordsName(enumInfo);

        System.out.println("  Generating Keywords Class " + className);

        String qualifiedName = packageName + "." + className;

        // Open a file.
        out = openFileForClass(qualifiedName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package declaration.
        out.println();
        out.println("package " + packageName + ";");

        // write out the imports
        SortedSet imports = new TreeSet();
        imports.add("com.volantis.mcs.themes.StyleKeyword");
        imports.add("com.volantis.mcs.themes.values.StyleKeywords");
        imports.add("java.util.ArrayList");
        imports.add("java.util.List");
        GenerateUtilities.writeImports(out, imports);

        // Write the class header.
        out.println();
        out.println("public class " + className + " extends AllowableKeywords {");

        // write out the member variables
        out.println();
        out.println("  private static final List keywords = new ArrayList();");

        out.println();
        for (Iterator i = enumInfo.elementIterator(); i.hasNext();) {
            EnumerationInfo.Element e = (EnumerationInfo.Element) i.next();
            String shortLabel = getConstant(e.getLabel());

            out.println(
                    "  public static final StyleKeyword " + shortLabel +
                    " = add(keywords, StyleKeywords." + shortLabel + ");");
        }

        out.println();
        out.println("  public " + className + "() {");
        out.println("    super(keywords);");
        out.println("  }");


        out.println();
        out.println("  private static final AllowableKeywords defaultInstance = new " + className + "();");
        out.println("  public static final AllowableKeywords getDefaultInstance() {");
        out.println("    return defaultInstance;");
        out.println("  }");

        out.println("}");
    }

    /**
     * Provide a name for a Keywords interface.
     *
     * @param enumInfo the EnumerationInfo object
     * @return the interface name.
     */
    public static String getKeywordsName(EnumerationInfo enumInfo) {
        String name = enumInfo.getName();

        return getKeywordsName(name);
    }

    public static String getKeywordsName(String name) {
        if (name.startsWith("Mariner")) {
            name = "MCS" + name.substring(7);
        }
        if (name.endsWith("Units")) {
            name = name.substring(0, name.length() - 5);
        }
        return name + "Keywords";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 ===========================================================================
*/
