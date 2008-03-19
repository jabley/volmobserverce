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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/ParseRemoteSchema.java,v 1.5 2003/03/17 11:53:54 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Oct-02    Sumit           VBM:2002100707 - Created to autogenerate SAX
 *                              handlers from the Marlin RPDM schema
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser.
 * 03-Dec-02    Steve           VBM:2002090210 - Use the CacheEntry class
 *                              document instead of CacheInfo
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 14-Mar-03    Sumit           VBM:2003031402 - Wrapped logger.debug in
 *                              if(logger.isDebugEnabled())
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.parser.AttributeGroupReference;
import com.volantis.mcs.build.parser.AttributesStructure;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class parses the mariner schema and generates code for Remote attributes
 * and elements. It currently needs all components and assets in its class path
 * as it obtains information method parameter types
 * (int, long, booleans etc) and generates relevant mapping code. It also makes
 * use of AssetUtilities classes to convert Strings in the XML to valid
 * primitive type constants. This currently works fine as the XSD attribute
 * mapping for rpdm elements have constants with the same name e.g.
 * AudioAsset.setEncoding() maps to attribute encoding of the audioComponent
 * element. In the future, this may change which means that the marlin RPDM
 * XSD will have to be changed to include parsing instructions that provide a
 * mapping from the XSD attribute mappings to the contants used by the Assets
 */
public class ParseRemoteSchema {

    // Literals
    private final static String FOLDER = "Folder";
    private final static String EOS = ";";
    private final static String PACKAGE_DELIMETER = ".";

    /**
     * The directory where the imdapi code should be added.
     */
    private File remoteDir;

    /**
     * The assets package.
     */
    private final String assetPackage = "com.volantis.mcs.assets";

    /**
     * The components package.
     */
    private final String componentPackage = "com.volantis.mcs.components";

    /**
     * The remote components package.
     */
    private final String generatedPackage = "com.volantis.mcs.xml.remote";

    /**
     * The schema parser.
     */
    private SchemaParser parser;

    /**
     * Layout components that have new names
     */
    private final Map deprecatedFormats;

    /**
     * Factory classes that require generation
     */
    private final Map factoryClasses;

    /**
     * The Mapper for elements that are not qualifiable
     */
    private final Map unqualClasses;

    /**
     * Create a new <code>ParseMarinerSchema</code>.
     */
    private ParseRemoteSchema() {
        deprecatedFormats = new HashMap();
        factoryClasses = new HashMap();
        unqualClasses = new HashMap();

        deprecatedFormats.put("paneFormat", "pane");
        deprecatedFormats.put("gridFormat", "grid");
        deprecatedFormats.put("gridFormatColumns", "gridColumns");
        deprecatedFormats.put("gridFormatColumn", "gridColumn");
        deprecatedFormats.put("gridFormatRow", "gridRow");
        deprecatedFormats.put("fragmentFormat", "fragment");
        deprecatedFormats.put("formFormat", "form");
        deprecatedFormats.put("formFragmentFormat", "formFragment");
        deprecatedFormats.put("regionFormat", "region");
        deprecatedFormats.put("replicaFormat", "replica");
        deprecatedFormats.put("emptyFormat", "empty");
        deprecatedFormats.put("rowIteratorPaneFormat", "rowIteratorPane");
        deprecatedFormats.put("columnIteratorPaneFormat", "columnIteratorPane");
        deprecatedFormats.put("dissectingPaneFormat", "dissectingPane");
        deprecatedFormats.put("layoutFormat", "layout");
        deprecatedFormats.put("basicDeviceLayoutFormat", "basicDeviceLayout");

        unqualClasses.put("themes", FOLDER);
        unqualClasses.put("layoutFormats", FOLDER);
        unqualClasses.put("assetGroups", FOLDER);
        unqualClasses.put("assetGroupFolder", FOLDER);
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
     * <strong>marlin-rpdm.xsd</strong>.  If this is a resource it must be
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
                schemaStream = ParseRemoteSchema.class.getResourceAsStream(
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
            ParseRemoteSchema instance = new ParseRemoteSchema();
            instance.parse(schemaStream, new File(args[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse the xsd file from the supplied stream and parse
     * it, generating the code.
     *
     * @param schema       the stream from which the schema should be read.
     * @param generatedDir the directory into which the generated code should
     *                     be put.
     */
    private void parse(InputStream schema, File generatedDir) {


        try {
            SAXBuilder builder =
                    new SAXBuilder("com.volantis.xml.xerces.parsers.SAXParser",
                            false);

            Document document = builder.build(schema);

            parser = new SchemaParser(new Factory());

            List schemaObjects = parser.parse(document);

            remoteDir =
                    new File(generatedDir, generatedPackage.replace('.', '/'));
            remoteDir.mkdirs();

            initialiseExtraInfo(schemaObjects, parser.getScope());

            for (Iterator i = schemaObjects.iterator(); i.hasNext();) {
                SchemaObject object = (SchemaObject) i.next();
                if ((object != null) && (object instanceof ElementInfo)) {
                    createSAXHandler((ElementInfo) object);
                }
            }

            generateFactory(factoryClasses);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JDOMException jdome) {
            jdome.printStackTrace();
        } finally {
            try {
                if (schema != null) {
                    schema.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Add any extra information needed to generate the code. All classes that
     * do not need to be autogenerated need to be added here.
     */
    private void initialiseExtraInfo(List schemaObjects, Scope scope) {
        ElementInfo elementInfo = null;
        String name;

        // A list all those elements which do not need to be generated because
        // they already exist.
        String[] existingElements =
                new String[]{
                    "basicDeviceLayoutFormat",
                    "layoutFormat",
                    "emptyFormat",
                    "gridFormat"
                };

        // A list of all those element which are ignored at the moment.
        String[] ignoredElements = new String[]{};

        // A list of the prefixes to use for those elements whose prefix cannot
        // be generated from their natural name, or whose natural name is not set.
        String[] specialPrefixes = new String[]{};

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
     * This method tests if an element is a a component, asset, folder or a
     * format and generates code accordingly.
     */
    private void createSAXHandler(ElementInfo info)
            throws IOException {
        String elementName = info.getName();
        String apiClass = GenerateUtilities.getTitledString(elementName);

        // generate a factory even if we don't what it is as it
        // isn't in the ignore list
        if (info.getGenerateAPIElement() || (info.getAPIElementExists())) {
            factoryClasses.put(elementName,
                    generatedPackage + PACKAGE_DELIMETER +
                    getElementClassName(apiClass));

            // Does this have a deprecated item? If so use the same factory to generate
            // the class
            if (deprecatedFormats.get(elementName) != null) {
                factoryClasses.put(deprecatedFormats.get(elementName),
                        generatedPackage + PACKAGE_DELIMETER +
                        getElementClassName(apiClass));
            }
        }

        if (info.getGenerateAPIElement() && (!info.getAPIElementExists())) {
            PrintStream out;
            String qualName = elementName;
            if (unqualClasses.get(qualName) != null) {
                qualName = (String) unqualClasses.get(elementName);
            }
            if (qualName.endsWith("Component")) {
                out = prepare(apiClass);
                writeComponent(out, apiClass, info);
            } else if (qualName.endsWith("Asset")) {
                out = prepare(apiClass);
                writeComponentAsset(out, apiClass, info);
            } else if (qualName.endsWith("Components") ||
                    qualName.endsWith("Folder")) {
                out = prepare(apiClass);
                writeFolder(out, apiClass);
            } else if (qualName.endsWith("Format")) {
                out = prepare(apiClass);
                writeFormat(out, info);
            } else {
                System.out.println("Ignoring " + elementName);
                return;
            }
        }
    }

    /**
     * Write out component specific code
     */
    private void writeComponent(
            PrintStream out, String apiClass,
            ElementInfo info) {

        GenerateUtilities.writeHeader(out, "ParseRemoteSchema");
        writePackage(out, generatedPackage);
        writeImports(out, true, true);
        out.println("import " + componentPackage + PACKAGE_DELIMETER +
                apiClass + EOS);

        out.println("import com.volantis.mcs.runtime.RemoteProject" + EOS);
        out.println("import com.volantis.mcs.runtime.repository." +
                "FolderInterface" + EOS);
        out.println("import com.volantis.mcs.runtime.repository.remote." +
                "PolicyResponse" + EOS);
        out.println("import com.volantis.synergetics.log.LogDispatcher;");
        out.println(
                "import com.volantis.mcs.localization.LocalizationFactory;");


        writeClassDefinition(out, apiClass, false);

        writeStartElement(out);
        out.println("       String value=null;");
        out.println("       String url = context.getPrefixURL();");
        out.println("       " + apiClass + " component = new ");
        out.println("           " + apiClass +
                "(url+atts.getValue(\"name\"));");
        out.println("       component.setProject(RemoteProject.instance);");

        Collection cacheAttributes = new ArrayList();
        Collection attrs =
                getNonCacheAttributes(info.getAttributesStructureInfo(),
                        cacheAttributes);
        Iterator itr = attrs.iterator();
        while (itr.hasNext()) {
            SchemaAttribute attr = (SchemaAttribute) itr.next();
            writeComponentAttribute(out, attr, componentPackage, apiClass,
                    true);
        }

        out.println(
                "       RemoteCacheEntry entry = new RemoteCacheEntry(component);");
        if (!cacheAttributes.isEmpty()) {
            itr = cacheAttributes.iterator();
            while (itr.hasNext()) {
                SchemaAttribute attr = (SchemaAttribute) itr.next();
                writeComponentAttribute(out, attr,
                        "com.volantis.mcs.runtime.repository.remote",
                        "RemoteCacheEntry", false);
            }

            cacheAttributes.clear();
        }

        out.println("       context.push(entry);");
        out.println("   }");

        out.print("    public void endElement(String uri, String localName, ");
        out.println("String qName, SAXContext context) {");
        out.print("        RemoteCacheEntry entry = ");
        out.println("(RemoteCacheEntry)context.pop();");
        out.println("        Object parent = context.peek();");
        out.println("        if( parent instanceof FolderInterface ) {");
        out.print("            RemoteCacheFolder folder = ");
        out.println("(RemoteCacheFolder)parent;");
        out.println("            folder.addCacheEntry(entry);");
        out.println("        } else if(parent instanceof PolicyResponse) {");
        out.print("            PolicyResponse response = ");
        out.println("(PolicyResponse)parent;");
        out.println("            response.setCacheEntry(entry);");
        out.println("        } else {");
        out.print("            throw new IllegalStateException(");
        out.println("\"Cannot add element \"+qName+\" to parent \"+parent );");
        out.println("        }");
        out.println("    }");
        out.println("}");

        out.close();
    }

    /**
     * Write out asset specific code
     */
    private void writeComponentAsset(
            PrintStream out, String apiClass,
            ElementInfo info) {

        Class utility = loadUtilityForAsset(apiClass);

        GenerateUtilities.writeHeader(out, "ParseRemoteSchema");
        writePackage(out, generatedPackage);
        writeImports(out, false, true);
        out.println("import com.volantis.mcs.runtime.RemoteProject" + EOS);
        out.println("import " + assetPackage + PACKAGE_DELIMETER +
                apiClass + EOS);
        if (utility != null) {
            out.println("import " + assetPackage + PACKAGE_DELIMETER +
                    apiClass + "Utilities;");
        }

        writeClassDefinition(out, apiClass, true);

        writeStartElement(out);
        out.println(
                "       RemoteCacheEntry parent = (RemoteCacheEntry) context.peek();");
        out.println("       " + apiClass + " asset = new ");
        out.println("           " + apiClass +
                "(parent.getComponent().getName());");
        out.println("       asset.setProject(RemoteProject.instance);");
        out.println("       String value = null;");

        Collection attrs = info.getAttributesStructureInfo().getAllAttributes();
        Iterator itr = attrs.iterator();
        while (itr.hasNext()) {
            SchemaAttribute attr = (SchemaAttribute) itr.next();
            writeAssetAttribute(out, attr, apiClass, utility);
        }
        out.print("        RemoteCacheEntry component = (RemoteCacheEntry)");
        out.println("context.peek();");
        out.println("        component.addAsset(asset);");
        out.println("    }\n");
        out.println("}");

        out.close();
    }

    /**
     * Write out folder specific code
     */
    private void writeFolder(PrintStream out, String apiClass) {

        GenerateUtilities.writeHeader(out, "ParseRemoteSchema");
        writePackage(out, generatedPackage);
        writeImports(out, true, false);


        String className = getElementClassName(apiClass);
        if (!className.endsWith("FolderBuilder")) {
            out.println("import com.volantis.mcs.runtime.repository.remote." +
                    "PolicyResponse" + EOS + "\n");
        }

        out.println("\npublic class " + className +
                " extends SAXElementHandlerImpl{\n");

        out.println("\n    private static " + className + " singleton;\n");

        out.println("    static { singleton = new " + className + "(); }\n");

        out.println("   private " + className + "() {}\n");

        out.print("   public static " + className + " getSingleton(){");
        out.println(" return singleton; }\n");
        out.println("   public void startElement(String uri, String localName, " +
                "String qName, Attributes atts, SAXContext context) {");

        out.println("       String name = atts.getValue(\"name\");");
        out.println("       String url = context.getPrefixURL();");
        out.println("       if (name != null ) {");
        out.println("           url = url + name;");
        out.println("       }");
        out.println(
                "       RemoteCacheFolder folder = new RemoteCacheFolder(url);");
        out.println("       context.push(folder);");
        out.println("   }");

        if (className.endsWith("FolderBuilder")) {
            out.print("    public void endElement(String uri, String");
            out.println(" localName, String qName, SAXContext context) {");
            out.print("        RemoteCacheFolder entry = ");
            out.println("(RemoteCacheFolder)context.pop();");
            out.print("        RemoteCacheFolder folder =");
            out.println("(RemoteCacheFolder)context.peek();");
            out.println("        folder.addCacheFolder(entry);");
            out.println("    }");
        } else {
            out.print("    public void endElement(String uri, String");
            out.println(" localName, String qName, SAXContext context) {");
            out.print("        RemoteCacheFolder entry = ");
            out.println("(RemoteCacheFolder)context.pop();");
            out.print("        PolicyResponse response =");
            out.println("(PolicyResponse)context.peek();");
            out.println("        response.setFolder(entry);");
            out.println("    }");
        }


        out.println("}");
        out.close();
    }

    /**
     * Write out format specific code
     */
    private void writeFormat(PrintStream out, ElementInfo info) {

        String elementName = info.getName();
        String apiClass = GenerateUtilities.getTitledString(elementName);

        GenerateUtilities.writeHeader(out, "ParseRemoteSchema");

        writePackage(out, generatedPackage);
        writeImports(out, false, false);
        out.println("import com.volantis.mcs.accessors.LayoutBuilder;");
        out.println("import com.volantis.mcs.layouts.FormatType;");
        out.println("import com.volantis.mcs.layouts.LayoutException;");
        out.println("import com.volantis.synergetics.log.LogDispatcher;");
        out.println(
                "import com.volantis.mcs.localization.LocalizationFactory;");
        out.println(
                "import com.volantis.mcs.xml.remote.XMLRemoteSchemaTranslations;");

        writeClassDefinition(out, apiClass, true);
        GenerateUtilities.writeTranslator(out);

        writeStartElement(out);
        apiClass = getFormatName(apiClass);

        String[] words = GenerateUtilities.getWords(apiClass);
        String constant = GenerateUtilities.getConstant(words);
        out.println(
                "        LayoutBuilder builder = context.getLayoutBuilder();");
        out.println("        int count = context.incrementLayoutIndex();");
        out.println("        try {");
        out.println("            builder.pushFormat(FormatType." + constant +
                ".getTypeName(), count);");
        out.println("            for (int i=0;i<atts.getLength();i++) {");
        out.println("                String name = atts.getQName(i);");
        out.println("                String value = atts.getValue(i);");


        out.println(
                "                String mappedName = translations.getMappedName(name, localName);");
        out.println("                if (mappedName != null) {");
        out.println("                builder.setAttribute(mappedName, value);");
        out.println("                } else {");
        out.println(
                "                logger.error(\"invalid-attribute\", new Object[]{name, value});");
        out.println("                }");
        out.println("            }");
        out.println("            builder.attributesRead ();");
        out.println("         } catch(LayoutException le) {");
        out.println(
                "            logger.error(\"layout-unexpected-exception\", le);");
        out.println("         }");
        out.println("        context.pushLayoutIndex();");
        out.println("    }");
        out.println("    public void endElement(String uri, String localName, " +
                " String qName, SAXContext context) {");
        out.println(
                "        LayoutBuilder builder = context.getLayoutBuilder();");
        out.println("        try {");
        out.println("            builder.popFormat();");
        out.println("         } catch(LayoutException le) {");
        out.println(
                "            logger.error(\"layout-unexpected-exception\",le);");
        out.println("         }");
        out.println("        context.popLayoutIndex();");
        out.println("    }");
        out.println("}");
        out.close();
    }

    private PrintStream prepare(String apiClass) throws IOException {
        String fileName =
                (remoteDir.getPath() + File.separator +
                getElementClassName(apiClass) + ".java");
        PrintStream out = new PrintStream(new FileOutputStream(fileName));
        return out;
    }

    /**
     * Write out the common class header and startElement function
     */
    private void writeClassDefinition(
            PrintStream out, String apiClass,
            boolean logger) {

        String className = getElementClassName(apiClass);
        out.println("\npublic class " + className +
                " extends SAXElementHandlerImpl {\n");

        if (logger) {
            GenerateUtilities.writeLogger(out,
                    generatedPackage, className);
        }

        out.println("    private static " + className + " singleton;\n");
        out.println("    static {");
        out.println("        singleton = new " + className + "();");
        out.println("    }\n");
        out.println("    private " + className + "() {}\n");
        out.println("    public static " + className + " getSingleton(){");
        out.println("        return singleton;");
        out.println("    }\n");
    }

    private void writeStartElement(PrintStream out) {
        out.print("   public void startElement(String uri, String localName, ");
        out.println("String qName, Attributes atts, SAXContext context) {");
    }

    private String getElementClassName(String apiClass) {
        return "SAX" + apiClass + "Builder";
    }

    /**
     * This function attempts to load a utility class for an asset from the
     * class path. If the class is found then the code generated will use the
     * utility class to translate the value from the xml to the constant for the
     * asset
     */
    private Class loadUtilityForAsset(String assetClassName) {
        Class utility = null;
        try {
            utility =
                    Class.forName("com.volantis.mcs.assets." + assetClassName +
                    "Utilities");
        } catch (ClassNotFoundException nfc) {
            utility = null;
        }

        return utility;
    }

    /**
     * If the utility class is found for an asset then this method is used to
     * try and obtain a map for translation. E.g. for AudioAsset.setEncoding()
     * the xml value of "adpcm32" needs to be translated to the integer constant
     * AudioAsset.ADPCM32. This method attempts to call
     * AssetUtility.getEncodingMap() to make this translation
     */
    private boolean getMapFromUtility(
            SchemaAttribute attribute,
            Class utility) {
        try {
            Class[] parameterTypes = {};
            utility.getMethod(getGetMethod(attribute) + "Map", parameterTypes);
        } catch (NoSuchMethodException nsme) {
            return false;
        }

        return true;
    }


    /**
     * Return the name of the format given its class
     *
     * @param className the name of the class
     * @return the name of the format that the class is for
     */
    private String getFormatName(String className) {
        int index = className.lastIndexOf("Format");
        if (index > 0) {
            return className.substring(0, index);
        } else {
            return className;
        }
    }

    /**
     * This method generates the factory class that returns the correct SAX handler
     * based on element name.
     */
    private void generateFactory(Map classes) throws IOException {
        String fileName = (remoteDir.getPath() + File.separator +
                "SAXElementBuilderFactory.java");

        PrintStream out = new PrintStream(new FileOutputStream(fileName));
        GenerateUtilities.writeHeader(out, "ParseRemoteSchema");

        writePackage(out, generatedPackage);
        out.println("import com.volantis.mcs.xml.SAXElementHandler;");
        out.println("import java.util.HashMap;\n");
        out.println("import com.volantis.synergetics.log.LogDispatcher;\n");
        out.println(
                "import com.volantis.mcs.localization.LocalizationFactory;");
        out.println("public class SAXElementBuilderFactory {");
        out.println("    private static HashMap elementMap;");
        out.println("    static {");
        out.println("        elementMap = new HashMap();");
        Iterator itr = classes.keySet().iterator();
        while (itr.hasNext()) {
            String elementName = (String) itr.next();
            String classname = classes.get(elementName) + ".getSingleton()";
            out.println("        elementMap.put(\"" + elementName + "\"," +
                    classname + ");");
        }
        out.println("    }");

        out.println(
                "    public static SAXElementHandler getHandler(String elementName) {");
        out.println(
                "    return (SAXElementHandler)elementMap.get(elementName);");
        out.println("    }");

        out.println("    private SAXElementBuilderFactory() {}");
        out.println("}");
        out.close();
    }

    /**
     * This method attempts to resolve the type of parameter required for a set
     * method on an object. E.g. AudioAsset.setEncoding needs an int but the
     * xml contains a String which needs to be converted via the getMapFromUtility
     * to an Integer and then used in the setEncoding method. This method uses
     * introspection to determine what conversion is required.
     */
    private String resolveParameter(
            String packageName, String className,
            String methodName) {
        Class resolvee = null;
        try {
            resolvee =
                    Class.forName(packageName + PACKAGE_DELIMETER + className);
        } catch (ClassNotFoundException n) {
            return null;
        }

        boolean resolved = false;
        Method[] m = resolvee.getMethods();
        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().equals(methodName)) {
                resolved = true;
                return m[i].getParameterTypes()[0].toString();
            }
        }
        if (resolved == false) {
            System.out.println("No method called " + methodName + " in class "
                    + packageName +
                    PACKAGE_DELIMETER +
                    className);
        }

        return null;
    }

    /**
     * This method uses resolveParameter to generated the set method on the
     * repository object. It will add relevant parsing code based on the parameter
     * type of the set method.
     */
    private void buildNonLookupSet(
            PrintStream out, String variable,
            String method, String type) {

        out.print("        " + variable + PACKAGE_DELIMETER + method + "(");
        type = (type == null) ? "" : type;
        if (type.equals("int")) {
            out.print("Integer.parseInt(value)");
        } else if (type.equals("boolean")) {
            out.print("Boolean.valueOf(value).booleanValue()");
        } else if (type.equals("long")) {
            out.print("Long.parseLong(value)");
        } else {
            out.print("value");
        }

        out.println(");");
    }

    /**
     * This method uses resolveParameter and the getMapFrom Utility
     * to generated the set method on the repository object.
     * It will add code to perform a lookup on the map returned from
     * getMapFromUtility and then add relevant parsing code to convert the
     * response to the correct primitive type for the set method.
     */
    private void buildLookupSet(
            PrintStream out, String variable,
            String method, String titledName, String type,
            String apiClass) {

        out.println("           java.util.Map valMap = " + apiClass +
                "Utilities.get" + titledName + "Map();");
        out.println("           Object intern = valMap.get(value);");
        out.println("           if (intern==null){");
        out.println("               intern = valMap.get(value.toUpperCase());");
        out.println("           }");
        out.println("           if (intern==null){");
        out.println("               intern = valMap.get(value.toLowerCase());");
        out.println("           }");
        out.println("           if (intern!=null) {");
        out.print("               " + variable + PACKAGE_DELIMETER + method +
                "(");
        if (type.equals("int")) {
            out.println("((Integer)intern).intValue());");
        } else if (type.equals("boolean")) {
            out.println("((Boolean)intern).booleanValue());");
        } else {
            out.println("(String)intern);");
        }
        out.println("           } else {");
        out.println("               logger.error(\"cannot-map-attribute-value\", new Object[]{ \"" +
                titledName +
                "\", value} );");
        out.println("           }");
    }

    /**
     * This method iterates through the AttributesStructure and obtains all
     * attributes not associated with teh CacheAttributes xml element
     */
    private Collection getNonCacheAttributes(
            AttributesStructure info,
            Collection cacheAttributes) {
        Collection c = new ArrayList();
        c.addAll(info.getAttributes());

        Collection grps = info.getAttributeGroups();
        if (grps != null) {
            Iterator itr = grps.iterator();
            while (itr.hasNext()) {
                AttributeGroupReference grpInfo =
                        (AttributeGroupReference) itr.next();
                String name = grpInfo.getDefinition().getName();
                AttributesStructure sttr =
                        grpInfo.getDefinition().getAttributesStructure();
                if (!name.equals("CacheAttributes")) {
                    c.addAll(getNonCacheAttributes(sttr, cacheAttributes));
                } else {
                    cacheAttributes.addAll(sttr.getAttributes());
                }
            }
        }

        return c;
    }

    private void writePackage(PrintStream out, String generatedPackage) {
        out.println("package " + generatedPackage + EOS);
        out.println();
    }

    private void writeImports(PrintStream out, boolean folder, boolean entry) {

        out.println("import org.xml.sax.Attributes;");
        out.println("import com.volantis.synergetics.log.LogDispatcher;");
        out.println(
                "import com.volantis.mcs.localization.LocalizationFactory;");
        if (folder) {
            out.print("import com.volantis.mcs.runtime.repository.");
            out.println("remote.RemoteCacheFolder;");
        }
        if (entry) {
            out.print("import com.volantis.mcs.runtime.repository.");
            out.println("remote.RemoteCacheEntry;");
        }
        out.println("import com.volantis.mcs.xml.SAXElementHandlerImpl;");
        out.println("import com.volantis.mcs.xml.SAXContext;");
    }

    private void writeComponentAttribute(
            PrintStream out, SchemaAttribute attr,
            String packageName, String className,
            boolean isComponent) {

        String methodName = getSetMethod(attr);
        String param = resolveParameter(packageName, className, methodName);

        out.println(
                "       value = atts.getValue(\"" + attr.getName() + "\");");
        out.println("       if (value!=null){");
        buildNonLookupSet(out, isComponent ? "component" : "entry",
                methodName, param);
        out.println("       }");
    }

    private void writeAssetAttribute(
            PrintStream out, SchemaAttribute attr,
            String className, Class utility) {

        String methodName = getSetMethod(attr);
        String param = resolveParameter(assetPackage, className, methodName);
        out.println(
                "       value = atts.getValue(\"" + attr.getName() + "\");");
        out.println("       if (value!=null){");
        if (utility != null) {
            if (getMapFromUtility(attr, utility)) {
                buildLookupSet(out, "asset", methodName, getTitledName(attr),
                        param, className);
            } else {
                buildNonLookupSet(out, "asset", methodName, param);
            }
        } else {
            buildNonLookupSet(out, "asset", methodName, param);
        }
        out.println("       }");
    }

    private String getTitledName(SchemaAttribute attr) {
        return GenerateUtilities.getTitledString(attr.getName());
    }

    private String getSetMethod(SchemaAttribute attr) {
        return "set" + GenerateUtilities.getTitledString(attr.getName());
    }

    private String getGetMethod(SchemaAttribute attr) {
        return "get" + GenerateUtilities.getTitledString(attr.getName());
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Mar-05	7511/5	emma	VBM:2005032204 Merge from 3.3.0 - Changing how *FormatBuilder classes (generated and hand-written) translate attribute names

 29-Mar-05	7484/3	emma	VBM:2005032204 Modifications after review

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 08-Oct-04	5740/3	matthew	VBM:2004100708 Allow code generators to accept a resource name as well as a filename

 08-Oct-04	5740/1	matthew	VBM:2004100708 allow Code generators to read from files or resources

 27-Feb-04	3215/1	steve	VBM:2004021911 Patch from Proteus2 and fixes for RemoteProject

 19-Feb-04	2789/8	tony	VBM:2004012601 rework changes

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/4	tony	VBM:2004012601 update localisation services

 12-Feb-04	2551/5	steve	VBM:2003121905 Javadoced and move component cache attribute reading

 10-Feb-04	2551/3	steve	VBM:2003121905 Remote Repository Overhaul

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 ===========================================================================
*/
