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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.parser.Scope;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class parses the mariner schema and generates code for PAPI attributes
 * some PAPI elements, and eventually will generate the tags as well.
 */
public class RuntimeParseMarinerSchema
        extends ParseMarinerSchema {

    /**
     * Main entry point to this application.  The command line arguments are
     * important in their ordering.  These are:
     * <table border="1">
     *
     * <tr>
     * <th>Order</th>
     * <th>File</th>
     * <th>Use/Meaning</th>
     * </tr>
     *
     * <tr>
     * <td>First</td>
     * <td>Schema file</td>
     * <td>This has (or should have been) already generated from the
     * architecture files.  This is then used as the basis of any theme based
     * generation.  Generally this is known as
     * <strong>marlin-cdm-internal.xsd</strong>.  If this is a resource it must be
     * specified as a full path including the full package names, e.g.: <br />
     * <code>full/package/name/resource.file</code> or <br />
     * <code>/full/package/name/resource.file</code>.
     * </td>
     * </tr>
     *
     * <tr>
     * <td>Second</td>
     * <td>Output directory</td>
     * <td>This is where all the generated source files (based on the
     * schema and property definitions) should be placed.
     * </td>
     * </tr>
     *
     * </table>
     *
     * The first of these parameters is looked for as a file, and
     * then if not found, as a resource.  If the entity is not found as a
     * file or as a resource then the program will terminate and report an
     * error.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        try {
            // Validate the number of parameters.  Any more than are required
            // are just silently ignored.
            if (args.length < 2) {
                throw new IllegalArgumentException("A schema file and an output " +
                        "directory must be specified (in that order) for " +
                        "this program to be able to run successfully.");
            }

            String schemaName = args[0];

            // Locate the files - except obviously the output directory
            InputStream schemaStream;
            File schema = new File(schemaName);

            if (schema.exists() && schema.canRead()) {
                schemaStream = new FileInputStream(schema);
            } else {
                // It doesn't exist so try and find it as a resource
                schemaStream = ParseMarinerSchema.class.getResourceAsStream(
                        (schemaName.startsWith("/") ? "" : "/") + schemaName);
            }

            // Now validate that there are streams to use, regardless of where
            // they came from
            if (schemaStream == null) {
                // Oops it's all gone wrong!  Schema cannot be found
                throw new IllegalArgumentException("The schema file specified cannot be found as " +
                        "either a file or a resource");
            }

            System.out.println("Schema - " + schemaName);
            System.out.println("Generated Directory - " + args[1]);
            System.out.println();

            RuntimeParseMarinerSchema instance = new RuntimeParseMarinerSchema();
            // Everything should be OK here, so proceed with the parsing
            instance.parse(schemaStream, new File(args[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initialise() throws IOException {
    }

    /**
     * Do any ElementInfo specific processing.
     *
     * @param info The <code>ElementInfo</code> to process.
     */
    protected void processElement(ElementInfo info)
            throws IOException {

        System.out.println("Processing element definition '"
                + info.getName() + "'");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String name = info.getName();

        if (info.getGenerateAPIAttributes()) {
            generatePAPIAttributesClass(attributesStructureInfo,
                    info.getNaturalName(), info.getName(),
                    info.getAPIAttributesClass());
        }

        if (info.getGenerateAPIElement()) {
            //System.out.println (attributesStructureInfo);

            if (info.getBaseAPIElementClass() != null) {
                generatePAPIImplAliasElement(info);
            } else if (attributesStructureInfo.instanceOf("BlockAttributes")) {
                generatePAPIImplBlockElement(info);
            } else if (attributesStructureInfo.instanceOf("AttrsAttributes")) {
                generatePAPIImplAttrsElement(info);
            } else {
                System.out.println(attributesStructureInfo);
            }
        }

        if ((info.getGenerateAPIElement() || info.getAPIElementExists())) {
            // Always generate a Facade
            generatePAPIFacadeElement(info);
            generatePAPIImplementationDataEntry(info);
            if (info.getGenerateMarlinElementHandler()) {
                generateMarlinElementHandler(info);
            }
        }

    }

    public void schemaPreamble() {
        generatePAPIImplementationDataHeader();
    }

    public void schemaPostamble() {
        generatePAPIImplementationDataFooter();
    }

    /**
     * Do any AttributeGroupInfo specific processing.
     *
     * @param info The <code>AttributeGroupInfo</code> to process.
     */
    protected void processAttributeGroup(AttributeGroupInfo info)
            throws IOException {

        System.out.println("Processing attribute group definition '"
                + info.getName() + "'");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String groupName = info.getName();

        // Get the name of the attributes class which should be generated for
        // this attribute group, if none is specified then don't create one.
        String apiAttributesClass = attributesStructureInfo.getAPIAttributesClass();
        String apiAttributesInterface = attributesStructureInfo.getAPIAttributesInterface();
        if (apiAttributesClass != null) {
            generatePAPIAttributesClass(attributesStructureInfo,
                    attributesStructureInfo.getNaturalName(), null,
                    apiAttributesClass);
        } else if (apiAttributesInterface != null) {
            generatePAPIAttributesInterface(attributesStructureInfo,
                    attributesStructureInfo.getNaturalName(),
                    apiAttributesInterface);
        } else {
            System.out.println("  Ignoring attribute group "
                    + groupName);
            //System.out.println ("  " + attributesStructureInfo);
            return;
        }
    }

    protected void generateMarlinElementHandlerFactory(Scope scope) {

        System.out.println("  Generating MarlinElementHandlerFactory");

        String packageName = marlinSAXPackage;

        String qualifiedClassName
                = packageName + ".MarlinElementHandlerFactory";

        PrintStream out = openFileForClass(qualifiedClassName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + packageName + ";");

        // Write the imports.
        SortedSet imports = new TreeSet();
        //imports.add ("org.apache.log4j.Category");
        imports.add("com.volantis.synergetics.log.LogDispatcher");
        imports.add("com.volantis.mcs.localization.LocalizationFactory");
        imports.add("java.util.Map");
        imports.add("java.util.HashMap");

        GenerateUtilities.writeImports(out, imports, packageName);

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "", new String[]{
                    "The MarlinElementHandlerFactory class.",
                    "<p>",
                    "This class is responsible for returning the appropriate"
                + " MarlinElementHandler for a given local name"
                });

        // Write the class header.
        out.println("public class MarlinElementHandlerFactory {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Write the logger creation statement.
        GenerateUtilities.writeLogger(out, packageName,
                "MarlinElementHandlerFactory");

        // Write the declaration of the papi attributes.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "The map from element names to MarlinElementHandler.");

        out.println("  private static Map handlers;");

        /*
        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
          (out, "  ", "Create a new <code>MarlinElementHandler</code>.");

        out.println ("  public MarlinElementHandler () {");
        */

        out.println();
        out.println("  static {");
        out.println("    handlers = new HashMap();");
        out.println();

        // label the outer for loop
        ELEMENTHANDLER_GEN:
        for (int idx = 0; idx < marlinElementList.size(); idx += 1) {
            String name = (String) marlinElementList.get(idx);

            for (int i = 0;
                 i < excludeMarlinElementHandlerFactory.length; i++) {
                if (name.equals(excludeMarlinElementHandlerFactory[i])) {
                    // Skip this element by continuing with the next iteration of the
                    // outer for loop.
                    continue ELEMENTHANDLER_GEN;
                }
            }

            try {
                ElementInfo info = (ElementInfo) scope.getElementDefinition(
                        name);
                String className = info.getMarlinElementClass();
                className = info.getPrefix() + "ElementHandler";
                out.println("    handlers.put (\"" + name + "\", new "
                        + className + " ());");
            } catch (Exception e) {
                System.out.println("  Ignoring element " + name);
            }
        }
        out.println("  }");

        // Write out the accessor method.
        GenerateUtilities.openJavaDocComment(out, "  ");
        GenerateUtilities.addJavaDocComment
                (out, "  ",
                        "Get a MarlinElementHandler for the named element.");
        GenerateUtilities.addJavaDocComment
                (out, "  ", "@param name The name of the element.");
        GenerateUtilities.addJavaDocComment
                (out, "  ", "@return The MarlinElementHandler");
        GenerateUtilities.closeJavaDocComment(out, "  ");

        out.println("  public static MarlinElementHandler"
                + " getMarlinElementHandler (String name) {");
        out.println();
        out.println("    return (MarlinElementHandler) handlers.get (name);");
        out.println("  }");

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 07-Feb-05	6833/1	ianw	VBM:2005020205 IBM fixes interim checkin

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 ===========================================================================
*/
