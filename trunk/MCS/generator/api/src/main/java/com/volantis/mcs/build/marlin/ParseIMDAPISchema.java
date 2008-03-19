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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/ParseIMDAPISchema.java,v 1.6 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Mar-02    Mat             VBM:2002022009 - Created.
 * 03-Apr-02    Mat             VBM:2002022009 - Changed the asset and
 *                              component element generators to use the setter
 *                              methods in the asset/component instead of
 *                              creating a new attributes class.  Also added
 *                              new processing instruction of IMDAPITypeTarget.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * 20-May-02    Paul            VBM:2002032501 - Stopped using
 *                              setHasJspExtraInfo as it has been removed and
 *                              it is not needed in this class anyway.
 * 14-Jun-02    Mat             VBM:2002052001 - Changed for the new IMDAPI
 *                              layout.
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser.
 * 22-Nov-02    Paul            VBM:2002112214 - Removed unused references to
 *                              maml specific properties.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags to
 *                              generated classes
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.JavaInfo;
import com.volantis.mcs.build.parser.SchemaAttribute;
import com.volantis.mcs.build.parser.SchemaObject;
import com.volantis.mcs.build.parser.SchemaParser;
import com.volantis.mcs.build.parser.Scope;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class parses the mariner schema and generates code for IMDAPI attributes
 * and elements
 */
public class ParseIMDAPISchema {

    /**
     * The directory where the imdapi code should be added.
     */
    private File imdapiDir;

    /**
     * The context package.
     */
    private final String contextPackage = "com.volantis.mcs.context";

    /**
     * The imdapi package.
     */
    private final String imdapiPackage = "com.volantis.mcs.imdapi";

    /**
     * The assets package.
     */
    private final String assetPackage = "com.volantis.mcs.assets";

    /**
     * The components package.
     */
    private final String componentPackage = "com.volantis.mcs.components";

    /**
     * The package for the component containers.
     */
    private final String containerPackage = "com.volantis.mcs.accessors.xml.jibx";

    /**
     * The schema parser.
     */
    private SchemaParser parser;

    /**
     * Javadoc comment for the elementStart method implemented by all concrete
     * implementations of AbstractIMDAPIElement.
     */
    private final static String ELEMENT_START_JAVADOC_COMMENT =
            "Called while processing this IMDAPI markup element, this " +
            "creates an asset initialised with the supplied attributes and " +
            "stores it in the latest canvas associated with the current " +
            "request." + "\n\n" +
            "@param mrc   the current request context.\n" +
            "@param attrs attributes of the asset to be stored.\n";

    /**
     * Javdoc for the elementEnd method implemented by all concrete
     * implementations of bstractIMDAPIElement.
     */
    private final static String ELEMENT_END_JAVADOC_COMMENT =
            "Called when this IMDAPI element's processing is complete.";


    private final static Map componentClassNameToPolicyType;

    static {
        Map map = new HashMap();
        map.put("AudioComponent", "PolicyType.AUDIO");
        map.put("ButtonImageComponent", "PolicyType.BUTTON_IMAGE");
        map.put("DynamicVisualComponent", "PolicyType.VIDEO");
        map.put("ChartComponent", "PolicyType.CHART");
        map.put("ImageComponent", "PolicyType.IMAGE");
        map.put("LinkComponent", "PolicyType.LINK");
        map.put("ResourceComponent", "PolicyType.RESOURCE");
        map.put("RolloverImageComponent", "PolicyType.ROLLOVER_IMAGE");
        map.put("ScriptComponent", "PolicyType.SCRIPT");
        map.put("TextComponent", "PolicyType.TEXT");
        componentClassNameToPolicyType = map;
    }

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
     * <strong>Mariner-imdapi.xsd</strong>.  If this is a resource it must be
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
                schemaStream = ParseIMDAPISchema.class.getResourceAsStream(
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

            // Everything should be OK here, so proceed with the parsing
            ParseIMDAPISchema instance = new ParseIMDAPISchema();
            instance.parse(schemaStream, new File(args[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new <code>ParseMarinerSchema</code>.
     */
    public ParseIMDAPISchema() {
    }

    /**
     * Parse the Mariner-Full.xsd file from the supplied stream and parse
     * it, generating the code.
     *
     * @param marinerSchema the stream from which the schema should be read.
     * @param generatedDir  the directory into which the generated code should
     *                      be put.
     */
    private void parse(InputStream marinerSchema, File generatedDir) {

        try {
            SAXBuilder builder
                    = new SAXBuilder(
                            "com.volantis.xml.xerces.parsers.SAXParser",
                            false);

            Document document = builder.build(marinerSchema);

            parser = new SchemaParser(new Factory());
            parser.addProcessingInstructionTarget("imdapi",
                    new IMDAPITarget());
            parser.addProcessingInstructionTarget("imdapiType",
                    new IMDAPITypeTarget());

            List schemaObjects = parser.parse(document);

            imdapiDir = new File(generatedDir, "com/volantis/mcs/imdapi");
            imdapiDir.mkdirs();

            initialiseExtraInfo(schemaObjects, parser.getScope());

            for (Iterator i = schemaObjects.iterator(); i.hasNext();) {
                SchemaObject object = (SchemaObject) i.next();
                processSchemaObject(object);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (JDOMException jdome) {
            jdome.printStackTrace();
            return;
        } finally {
            try {
                if (marinerSchema != null) {
                    marinerSchema.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    /**
     * Add any extra information needed to generate the code.
     */
    private void initialiseExtraInfo(List schemaObjects, Scope scope) {

        ElementInfo elementInfo = null;
        String name;

        // A list all those elements which do not need to be generated because
        // they already exist.
        String[] existingElements = new String[]{
        };

        // A list of all those element which are ignored at the moment.
        String[] ignoredElements = new String[]{
            "assetgroup"
        };

        // A list of the prefixes to use for those elements whose prefix cannot
        // be generated from their natural name, or whose natural name is not set.
        String[] specialPrefixes = new String[]{
        };

        // Mark all those elements which do not need to be generated because the
        // element already exists.
        for (int i = 0; i < existingElements.length; i += 1) {
            name = existingElements[i];
            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setGenerateAPIElement(false);
            elementInfo.setAPIElementExists(true);
        }

        // Mark all those elements which are ignored at the moment.
        for (int i = 0; i < ignoredElements.length; i += 1) {
            name = ignoredElements[i];
            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setGenerateAPIAttributes(false);
            elementInfo.setGenerateAPIElement(false);
        }

        // The prefix used for some elements cannot be generated from the natural
        // name.
        for (int i = 0; i < specialPrefixes.length; i += 2) {
            name = specialPrefixes[i];
            String prefix = specialPrefixes[i + 1];

            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setPrefix(prefix);
        }


        schemaObjects.add(elementInfo);


    }

    /**
     * Do the right thing depending on the type of the specified object.
     *
     * @param object The <code>SchemaObject</code> to process.
     */
    private void processSchemaObject(SchemaObject object)
            throws IOException {

        if (object == null) {
            return;
        }

        System.out.println();

        if (object instanceof AttributeGroupInfo) {
            processAttributeGroup((AttributeGroupInfo) object);
        } else if (object instanceof ElementInfo) {
            processElement((ElementInfo) object);
        } else {
            throw new IllegalArgumentException("Unhandled object " + object);
        }

    }

    /**
     * Do any AttributeGroupInfo specific processing.
     *
     * @param info The <code>AttributeGroupInfo</code> to process.
     */
    private void processAttributeGroup(AttributeGroupInfo info)
            throws IOException {

        System.out.println("Processing attribute group definition '"
                + info.getName() + "'");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String groupName = info.getName();

        // Get the name of the attributes class which should be generated for
        // this attribute group, if none is specified then don't create one.
        String apiAttributesClass = attributesStructureInfo.getAPIAttributesClass();
        if (apiAttributesClass != null) {
            generateIMDAPIAttributesClass(attributesStructureInfo,
                    attributesStructureInfo.getNaturalName(),
                    apiAttributesClass);
        } else {
            System.out.println("  Ignoring attribute group "
                    + groupName);
            //System.out.println ("  " + attributesStructureInfo);
            return;
        }
    }

    /**
     * Do any ElementInfo specific processing.
     *
     * @param info The <code>ElementInfo</code> to process.
     */
    private void processElement(ElementInfo info)
            throws IOException {

        String name = info.getName();

        System.out.println("Processing element definition '"
                + name + "'");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        if (info.getGenerateAPIAttributes()) {
            generateIMDAPIAttributesClass(attributesStructureInfo,
                    info.getNaturalName(), info.getAPIAttributesClass());
        }

        if (info.getGenerateAPIElement()) {
            if (attributesStructureInfo.instanceOf("AbstractAssetAttributes")) {
                generateIMDAPIAssetElement(info);
            } else if (attributesStructureInfo.instanceOf(
                    "AbstractComponentAttributes")) {
                generateIMDAPIComponentElement(info);
            } else if ("metadata".equals(name)) {
                // do nothing
            } else {
                System.out.println(attributesStructureInfo);
                throw new IllegalStateException("Don't know what type of element to"
                        + " generate for " + name);
            }
        }
    }

    /**
     * Generate a IMDAPI attribute class.
     *
     * @param info The <code>AttributesStructureInfo</code> which controls the
     *             contents of the generated IMDAPIAttributes class.
     */
    private void generateIMDAPIAttributesClass(
            AttributesStructureInfo info, String naturalName,
            String attributesClass)
            throws IOException {

        System.out.println("  Generating IMDAPIAttributes");

        if (attributesClass == null) {
            throw new IllegalArgumentException("Class name not specified");
        }

        // Find the base attribute group of these attributes.
        AttributeGroupInfo baseInfo = info.findBaseAttributeGroup();
        AttributesStructureInfo baseAttributesStructureInfo = null;
        if (baseInfo != null) {
            baseAttributesStructureInfo =
                    baseInfo.getAttributesStructureInfo();
        }

        // Get the base class to use for the attributes.
        String baseClass;
        if (baseAttributesStructureInfo == null) {
            baseClass = null;
        } else {
            baseClass = baseAttributesStructureInfo.getAPIAttributesClass();
        }

        String fileName = (imdapiDir + File.separator + attributesClass +
                ".java");

        PrintStream out = new PrintStream(new FileOutputStream(fileName));

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + imdapiPackage + ";");

        // Write the class comment.
        // @todo later the include in status should be replicated from the original definition
        GenerateUtilities.writeJavaDocComment(out, "",
                "The " + naturalName + " attributes.\n" +
                "@volantis-api-include-in PublicAPI\n" +
                "@volantis-api-include-in ProfessionalServicesAPI\n" +
                "@volantis-api-include-in InternalAPI\n");


        // Get the collection of interfaces which this class must implement.
        Collection interfaces = info.getInterfaceNames();

        // Write the class header.
        boolean abstractClass = info.isAbstractAPIAttributesClass();
        String abstractKeyword = (abstractClass ? "abstract " : "");
        String finalKeyword = (abstractClass ? "" : "final ");

        out.println("public " + finalKeyword + abstractKeyword + "class "
                + attributesClass);
        if (baseClass != null) {
            out.print("  extends " + baseClass);
        }

        if (!interfaces.isEmpty()) {
            out.println();
            String interfacePrefix = "  implements ";
            for (Iterator i = interfaces.iterator(); i.hasNext();) {
                String interfaceName = (String) i.next();
                out.print(interfacePrefix + interfaceName);
                interfacePrefix = ",\n             ";
            }
        }
        out.println(" {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Sort a copy of the attributes list.
        //attributes = sort (attributes);

        // Write out the attribute definitions.
        List extraAttributes = new ArrayList();
        Collection attributes = info.getImplementedAttributes();
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            //Element child = (Element) i.next ();
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String memberName = JavaInfo.getJavaVariable(attributeName);

            // Check to see whether this attribute is inherited from a base class
            // and if it is then ignore it.
            boolean inherited = false;
            if (baseAttributesStructureInfo != null) {
                Collection baseAttributes
                        = baseAttributesStructureInfo.getImplementedAttributes();
                for (Iterator b = baseAttributes.iterator(); b.hasNext();) {
                    SchemaAttribute baseAttribute = (SchemaAttribute) b.next();
                    if (baseAttribute.getName().equals(attributeInfo.getName())) {
                        inherited = true;
                        break;
                    }
                }

                // Set the inherited flag in the attributes.
                attributeInfo.setInherited(inherited);
            }

            // If this is inherited then ignore it.
            if (inherited) {
                System.out.println("  Attribute " + attributeInfo.getName()
                        + " is inherited");
                continue;
            }

            extraAttributes.add(attributeInfo);

            GenerateUtilities.writeJavaDocComment
                    (out, "  ", "The " + attributeName + " attribute of the "
                    + naturalName + " element");

            out.println("  private String " + memberName + ";");
        }

        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "Create a new <code>" + attributesClass + "</code>.");

        out.println("  public " + attributesClass + " () {");

        if (extraAttributes.size() != 0) {
            // Only call reinitialise if this class has its own attributes.
            out.println("    // This constructor delegates all its work to the"
                    + " reinitialise method,");
            out.println("    // no extra initialisation should be added here,"
                    + " instead it should be");
            out.println("    // added to the reinitialise method.");
            out.println("    reinitialise ();");
        }

        out.println("  }");

        // Only write out reset and reinitialise if this class has its own
        // attributes.
        if (extraAttributes.size() != 0) {
            GenerateUtilities.writeJavaDocComment
                    (out, "  ", "Resets the internal state so it is equivalent (not"
                    + " necessarily identical) to a new instance.");

            out.println("  public void reset () {");
            if (baseClass != null) {
                out.println("    super.reset ();");
            }
            out.println();
            out.println("    // Call this after calling super.reset to"
                    + " allow reinitialise to");
            out.println("    // override any inherited attributes.");
            out.println("    reinitialise ();");
            out.println("  }");

            // Write out reinitialise.
            GenerateUtilities.writeJavaDocComment
                    (out, "  ",
                            "Reinitialise all the data members."
                    +
                    " This is called from the constructor and also from reset.");

            out.println("  private void reinitialise () {");

            // Write out the code to reset the attributes.
            for (Iterator i = extraAttributes.iterator(); i.hasNext();) {
                AttributeInfo attributeInfo = (AttributeInfo) i.next();

                String attributeName = attributeInfo.getAPIName();
                String memberName = JavaInfo.getJavaVariable(attributeName);

                out.println("    " + memberName + " = null;");
            }

            out.println("  }");
        }

        // Write out the setters and the getters.
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String memberName = JavaInfo.getJavaVariable(attributeName);

            String titled = GenerateUtilities.getTitledString(attributeName);

            boolean inherited = attributeInfo.isInherited();
            boolean deprecated = attributeInfo.isDeprecated();

            // If this field has been inherited but has not been deprecated then
            // don't do anything.
            if (inherited && !deprecated) {
                continue;
            }

            String deprecatedMessage
                    = "This attribute will be removed in the near future";

            GenerateUtilities.openJavaDocComment(out, "  ");
            GenerateUtilities.addJavaDocComment
                    (out, "  ",
                            "Set the value of the " + attributeName +
                    " attribute.");
            GenerateUtilities.addJavaDocComment
                    (out, "  ", "@param " + attributeName +
                    " The new value of the "
                    + attributeName + " attribute.");

            // If this attribute has been deprecated then generate a @deprecated
            // message.
            if (deprecated) {
                GenerateUtilities.addJavaDocComment
                        (out, "  ", "@deprecated " + deprecatedMessage);
            }

            GenerateUtilities.closeJavaDocComment(out, "  ");

            out.println("  public void set" + titled
                    + " (String " + memberName + ") {");
            if (inherited) {
                out.println(
                        "    super.set" + titled + " (" + memberName + ");");
            } else {
                out.println(
                        "    this." + memberName + " = " + memberName + ";");
            }
            out.println("  }");

            GenerateUtilities.openJavaDocComment(out, "  ");
            GenerateUtilities.addJavaDocComment
                    (out, "  ",
                            "Get the value of the " + attributeName +
                    " attribute.");
            GenerateUtilities.addJavaDocComment
                    (out, "  ", "@return The value of the " + attributeName
                    + " attribute.");

            // If this attribute has been deprecated then generate a @deprecated
            // message.
            if (deprecated) {
                GenerateUtilities.addJavaDocComment
                        (out, "  ", "@deprecated " + deprecatedMessage);
            }

            GenerateUtilities.closeJavaDocComment(out, "  ");

            out.println("  public String get" + titled + " () {");
            if (inherited) {
                out.println("    return super.get" + titled + " ();");
            } else {
                out.println("    return " + memberName + ";");
            }
            out.println("  }");
        }

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();

        // Mark the file as read only.
        //file.setReadOnly ();
    }


    /**
     * Generate a IMDAPI asset element.
     *
     * @param info The extra information which is needed to generate the
     *             element.
     */
    private void generateIMDAPIAssetElement(ElementInfo info)
            throws IOException {

        System.out.println("  Generating IMDAPI asset element");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String elementClassName = info.getAPIElementClass();
        String attributesClassName = info.getAPIAttributesClass();

        String fileName = (imdapiDir.getPath() + File.separator
                + elementClassName + ".java");

        String componentClassBase =
                attributesStructureInfo.getApiComponentClassBase();

        PrintStream out = new PrintStream(new FileOutputStream(fileName));

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + imdapiPackage + ";");

        // Write the imports.
        out.println();
        out.println("import " + contextPackage + ".ContextInternals;");
        out.println("import " + contextPackage + ".MarinerPageContext;");
        out.println("import " + contextPackage + ".MarinerRequestContext;");
        out.println("import " + assetPackage + "." +
                attributesStructureInfo.getApiAssetClassName() +
                ";");
        out.println("import " + imdapiPackage + "." + componentClassBase +
                "Attributes;");
        out.println();
        out.println("import " + containerPackage + ".ComponentContainer;");
        out.println("import com.volantis.mcs.repository.RepositoryException;");

        // @todo later the include in status should be replicated from the original definition
        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "",
                        "The " + info.getNaturalName() + " element.\n" +
                "@volantis-api-include-in PublicAPI\n" +
                "@volantis-api-include-in ProfessionalServicesAPI\n" +
                "@volantis-api-include-in InternalAPI\n");

        // Write the class header.
        boolean abstractClass
                = attributesStructureInfo.isAbstractAPIAttributesClass();
        String abstractKeyword = (abstractClass ? "abstract " : "");
        String finalKeyword = (abstractClass ? "" : "final ");
        out.println("public " + finalKeyword + abstractKeyword + "class "
                + elementClassName + "\n\textends AbstractIMDAPIElement {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Write the declaration of the protocol attributes.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "The attributes to pass to the protocol methods.");


        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "Create a new <code>" + elementClassName + "</code>.");

        out.println("  public " + elementClassName + " () {");
        out.println("  }");



        // Write out the elementStartImpl method.
        out.println();
        GenerateUtilities.writeJavaDocComment(out, "  ",
                ELEMENT_START_JAVADOC_COMMENT);
        out.println("  public"
                + " void elementStart(MarinerRequestContext mrc,");
        out.println("                                  "
                + attributesClassName + " attrs)");
        out.println("    throws RepositoryException {");
        out.println();
        out.println("    MarinerPageContext pageContext");
        out.println("      = ContextInternals.getMarinerPageContext (mrc);");
        out.println();
        out.println("    " + componentClassBase + "Element component = " +
                "(" + componentClassBase + "Element)");
        out.println("           pageContext.getIMDAPIElement();");
        out.println();
        out.println("    if(component == null) {");
        out.println("        throw new IllegalStateException(\"Cannot get " +
                componentClassBase + "Element from the stack\");");
        out.println("    }");
        out.println();
        out.println("    " + componentClassBase + "Attributes " +
                "componentAttributes = component.getAttributes();");
        out.println("    pageContext.pushIMDAPIElement(this);");
        out.println();
        out.println("    " + attributesStructureInfo.getApiAssetClassName() +
                " asset = new " +
                attributesStructureInfo.getApiAssetClassName() +
                "();");
        // Initialise the other attributes.
        Collection attributes
                = attributesStructureInfo.getAllAttributes();


        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();
            String titledString =
                    GenerateUtilities.getTitledString(
                            attributeInfo.getAPIName());
            if ("string".equalsIgnoreCase(attributeInfo.getAttributeType())) {
                out.println("    asset.set" + titledString + "(attrs.get" +
                        titledString + "());");
            } else if ("integer".equalsIgnoreCase(
                    attributeInfo.getAttributeType())) {
                out.println("    asset.set" + titledString +
                        "(Integer.parseInt(attrs.get" +
                        titledString + "()));");
            } else {
                throw new IllegalStateException("Attribute " +
                        attributeInfo.getName()
                        + " has an illegal type of " +
                        attributeInfo.getAttributeType());
            }
        }

        out.println("    asset.setName(componentAttributes.getName());");
        out.println("    asset.setProject(mrc.getCurrentProject());");

        out.println();

        out.println("    ComponentContainer container = " +
                "component.getContainer();");
        out.println("    container.addAsset(asset);");
        // Close the method.
        out.println("  }");

        // Write out the elementEnd method.
        out.println();
        GenerateUtilities.writeJavaDocComment(out, "  ",
                ELEMENT_END_JAVADOC_COMMENT);
        out.println("  public"
                + " void elementEnd(MarinerRequestContext mrc,");
        out.println("                                  "
                + attributesClassName + " attrs) {");
        out.println();
        out.println("    MarinerPageContext pageContext");
        out.println("      = ContextInternals.getMarinerPageContext (mrc);");
        out.println();
        out.println("    // Reset the attributes.");
        out.println("    attrs.reset();");
        out.println("    pageContext.popIMDAPIElement();");
        out.println();

        out.println("  }");

        // Close the class definition.
        out.println("}");

        // Close the output stream.
        out.close();

        // Mark the file as read only.
        //file.setReadOnly ();
    }


    /**
     * Generate a IMDAPI component element.
     *
     * @param info The extra information which is needed to generate the
     *             element.
     */
    private void generateIMDAPIComponentElement(ElementInfo info)
            throws IOException {

        System.out.println("  Generating IMDAPI component element");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String elementClassName = info.getAPIElementClass();
        String attributesClassName = info.getAPIAttributesClass();

        String fileName = (imdapiDir.getPath() + File.separator
                + elementClassName + ".java");

        String componentClassBase =
                attributesStructureInfo.getApiComponentClassBase();
//        String containerClassName = componentClassBase + "Container";
        String policyType = (String)
                componentClassNameToPolicyType.get(componentClassBase);
        if (policyType == null) {
            throw new IllegalArgumentException(
                    "Unknown component " + componentClassBase);
        }

        PrintStream out = new PrintStream(new FileOutputStream(fileName));

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + imdapiPackage + ";");

        // Write the imports.
        out.println();
        out.println("import " + contextPackage + ".ContextInternals;");
        out.println("import " + contextPackage + ".MarinerPageContext;");
        out.println("import " + contextPackage + ".MarinerRequestContext;");
        out.println(
                "import " + componentPackage + "." + componentClassBase + ";");
        out.println("import " + containerPackage + ".ComponentContainer;");
        out.println("import com.volantis.mcs.policies.PolicyType;");
        out.println();
        out.println("import com.volantis.mcs.repository.RepositoryException;");

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "", "The " + info.getNaturalName() + " element.");

        // Write the class header.
        boolean abstractClass
                = attributesStructureInfo.isAbstractAPIAttributesClass();
        String abstractKeyword = (abstractClass ? "abstract " : "");
        String finalKeyword = (abstractClass ? "" : "final ");
        out.println("public " + finalKeyword + abstractKeyword + "class "
                + elementClassName + "\n\textends AbstractIMDAPIElement {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Write the declaration of the protocol attributes.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "The attributes to pass to the protocol methods.");

        out.println("    private " + attributesClassName +
                " componentAttrs = null;");
        out.println();

        out.println("    private ComponentContainer container;");

        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "Create a new <code>" + elementClassName + "</code>.");


        out.println("    public " + elementClassName + " () {");
        out.println("    }");

        //Write out the getAttributes method
        GenerateUtilities.writeJavaDocComment(out, "  ",
                "Return the attributes for this component");
        out.println("    public " + attributesClassName + " getAttributes() {");
        out.println("        return componentAttrs;");
        out.println("    }");

        //Write out the getContainer method
        GenerateUtilities.writeJavaDocComment(out, "  ",
                "Return the container");
        out.println("    public ComponentContainer getContainer() {");
        out.println("        return container;");
        out.println("    }");

        // Write out the elementStartImpl method.
        out.println();
        out.println("    // Javadoc inherited from super class.");
        out.println("    public"
                + " void elementStart(MarinerRequestContext mrc,");
        out.println("                                  "
                + attributesClassName + " attrs) {");
        out.println();
        out.println("    componentAttrs = attrs;");
        out.println("    MarinerPageContext pageContext");
        out.println("      = ContextInternals.getMarinerPageContext (mrc);");
        out.println();
        out.println("    pageContext.pushIMDAPIElement(this);");
        out.println();
        out.println("    " + componentClassBase + " component = new " +
                componentClassBase + "();");
        out.println("    container = new ComponentContainer(component, "
                + policyType + ");");

        // Initialise the other attributes.
        Collection attributes;
        attributes = attributesStructureInfo.getAllAttributes();


        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String titledString = GenerateUtilities.getTitledString(
                    attributeInfo.getAPIName());
            if ("string".equalsIgnoreCase(attributeInfo.getAttributeType())) {
                out.println("    component.set" + titledString + "(attrs.get" +
                        titledString + "());");
            } else if ("integer".equalsIgnoreCase(
                    attributeInfo.getAttributeType())) {
                out.println("    component.set" + titledString +
                        "(Integer.parseInt(attrs.get" +
                        titledString + "()));");
            } else {
                throw new IllegalStateException("Attribute " +
                        attributeInfo.getName()
                        + " has an illegal type of " +
                        attributeInfo.getAttributeType());
            }
        }

        // Close the method.
        out.println("  }");

        // Write out the elementEnd method.
        out.println();
        out.println("  // Javadoc inherited from super class.");
        out.println("  public"
                + " void elementEnd(MarinerRequestContext mrc,");
        out.println("                                  "
                + attributesClassName + " attrs)");
        out.println("    throws RepositoryException {");
        out.println();
        out.println("    addPolicy(mrc, container);");

        out.println("  }");

        // Close the class definition.
        out.println("}");

        // Close the output stream.
        out.close();

        // Mark the file as read only.
        //file.setReadOnly ();
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 20-May-05	8410/1	rgreenall	VBM:2005051615 Added Javadoc

 20-May-05	8326/3	rgreenall	VBM:2005051615 Improved Javadoc descriptions.

 20-May-05	8326/1	rgreenall	VBM:2005051615 Added Javadoc.

 05-May-05	8046/1	ianw	VBM:2005042713 Make javadoc work properly with packages

 05-May-05	7934/1	ianw	VBM:2005042713 Make javadoc work properly with packages

 18-Apr-05	7715/1	philws	VBM:2005040402 Port Public API generation changes from 3.3

 15-Apr-05	7676/1	philws	VBM:2005040402 Public API corrections and IBM Public API documentation subset generation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 08-Oct-04	5740/5	matthew	VBM:2004100708 Allow code generators to accept a resource name as well as a filename

 08-Oct-04	5740/3	matthew	VBM:2004100708 Allow code generators to accept a resource name as well as a filename

 08-Oct-04	5740/1	matthew	VBM:2004100708 allow Code generators to read from files or resources

 17-May-04	3012/1	mat	VBM:2004011912 Add projects to IMD repository

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 ===========================================================================
*/
