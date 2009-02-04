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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/ParseThemeSchema.java,v 1.12 2003/03/10 11:51:09 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created.
 * 29-Apr-02    Doug            VBM:2002040803 - Fixed a couple of typos.
 * 29-Apr-02    Doug            VBM:2002040803 - XML Accessor genertation code
 *                              added
 * 06-May-02    Doug            VBM:2002040803 - Added the initialiseExtraInfo
 *                              mrethod.
 * 16-May-02    Paul            VBM:2002032501 - Updated as some classes have
 *                              moved.
 * 16-May-02    Doug            VBM:2002040803 - Added code to generate the
 *                              KeywordMapperFactories to the
 *                              generateKeywordMappers method.
 *                              Added javadoc throughout
 * 21-Jun-02    Adrian          VBM:2002041702 - updated initialiseExtraInfo
 *                              to add TextDecoration to the list of jdbc
 *                              accessors which should not be auto generated.
 * 28-Jun-02    Paul            VBM:2002051302 - Generate the css specific
 *                              mappers and related classes into a css.mappers
 *                              package instead of a themes.mappers package.
 *                              Also generate an explicit set of xml mappers.
 * 30-Jul-02    Ian             VBM:2002072907 - Added support for
 *                              border-spacing.
 * 28-Aug-02    Mat             VBM:2002040825 - Added TABLE_PREFIX
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser.
 * 07-Mar-03    Byron           VBM:2003030527 - Cleaned imports and unused
 *                              variables. Fixed formatting in parse method.
 *                              Added processGroup and modified
 *                              processSchemaObject methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;


import com.volantis.mcs.build.marlin.AttributeInfo;
import com.volantis.mcs.build.parser.ComplexType;
import com.volantis.mcs.build.parser.ElementGroup;
import com.volantis.mcs.build.parser.ElementGroupReference;
import com.volantis.mcs.build.parser.ElementReference;
import com.volantis.mcs.build.parser.SchemaObject;
import com.volantis.mcs.build.parser.SchemaParser;
import com.volantis.mcs.build.themes.definitions.ComputedReference;
import com.volantis.mcs.build.themes.definitions.DefinitionsFactory;
import com.volantis.mcs.build.themes.definitions.Named;
import com.volantis.mcs.build.themes.definitions.Property;
import com.volantis.mcs.build.themes.definitions.PropertyReference;
import com.volantis.mcs.build.themes.definitions.Rule;
import com.volantis.mcs.build.themes.definitions.Rules;
import com.volantis.mcs.build.themes.definitions.TypeDefinition;
import com.volantis.mcs.build.themes.definitions.types.AbstractTypeVisitor;
import com.volantis.mcs.build.themes.definitions.types.ChoiceType;
import com.volantis.mcs.build.themes.definitions.types.FractionType;
import com.volantis.mcs.build.themes.definitions.types.Keyword;
import com.volantis.mcs.build.themes.definitions.types.Keywords;
import com.volantis.mcs.build.themes.definitions.types.PairType;
import com.volantis.mcs.build.themes.definitions.types.Type;
import com.volantis.mcs.build.themes.definitions.types.TypeRef;
import com.volantis.mcs.build.themes.definitions.types.TypeVisitor;
import com.volantis.mcs.build.themes.definitions.values.AngleValue;
import com.volantis.mcs.build.themes.definitions.values.ColorValue;
import com.volantis.mcs.build.themes.definitions.values.FractionValue;
import com.volantis.mcs.build.themes.definitions.values.FrequencyValue;
import com.volantis.mcs.build.themes.definitions.values.InheritValue;
import com.volantis.mcs.build.themes.definitions.values.IntegerValue;
import com.volantis.mcs.build.themes.definitions.values.KeywordReference;
import com.volantis.mcs.build.themes.definitions.values.LengthValue;
import com.volantis.mcs.build.themes.definitions.values.ListValue;
import com.volantis.mcs.build.themes.definitions.values.PairValue;
import com.volantis.mcs.build.themes.definitions.values.PercentageValue;
import com.volantis.mcs.build.themes.definitions.values.StringValue;
import com.volantis.mcs.build.themes.definitions.values.TimeValue;
import com.volantis.mcs.build.themes.definitions.values.Value;
import com.volantis.mcs.build.themes.definitions.values.ValueContainer;
import com.volantis.mcs.build.themes.definitions.values.ValueSource;
import com.volantis.mcs.build.themes.definitions.values.impl.ComputedValueSource;
import com.volantis.mcs.build.themes.definitions.values.impl.FixedValueSource;
import com.volantis.mcs.build.themes.definitions.values.impl.ListValueContainer;
import com.volantis.mcs.build.themes.definitions.values.impl.NullValueSource;
import com.volantis.mcs.build.themes.definitions.values.impl.PairValueContainer;
import com.volantis.mcs.build.themes.definitions.values.impl.PropertyValueSource;
import com.volantis.mcs.build.themes.definitions.values.impl.SingleValueContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

/**
 * This class parses the mariner theme schema and generates various pieces of
 * code to support themes.
 */
public class ParseThemeSchema {

    /**
     * The theme package
     */
    private static final String themePackage = "com.volantis.mcs.themes";

    /**
     * The mappers package
     */
    private static final String mappersPackage =
            "com.volantis.mcs.themes.mappers";

    /**
     * The mappers package
     */
    private static final String propertiesPackage =
            "com.volantis.mcs.themes.properties";

    /**
     * The mappers package
     */
    private static final String valuesPackage =
            "com.volantis.mcs.themes.values";

    /**
     * The css version package.
     */
    private static final String cssVersionPackage =
            "com.volantis.mcs.css.version";

    /**
     * The keyword mapper factory stub package
     */
    private static final String keywordMapperFactoryStubPackage =
            "com.volantis.mcs.css.renderer";

    /**
     * The css mapppers package
     */
    private static final String cssMappersPackage =
            "com.volantis.mcs.css.mappers";

    /**
     * The XML accessor package
     */
    private static final String xmlAccessorPackage =
            "com.volantis.mcs.accessors.xml.jdom.themes";

    /**
     * A theme version selector for CSS2.
     */
    private static ThemeVersionSelector css2Selector =
            new ThemeVersionSelector() {
                public boolean supportsVersion(ThemeVersionInfo versionInfo) {
                    return versionInfo.isSupportedInCSS2();
                }
            };

    /**
     * A theme version selector for CSS1.
     */
    private static ThemeVersionSelector css1Selector =
            new ThemeVersionSelector() {
                public boolean supportsVersion(ThemeVersionInfo versionInfo) {
                    return versionInfo.isSupportedInCSS1();
                }
            };

    /**
     * A theme version selector for CSS Mobile.
     */
    private static ThemeVersionSelector cssMobileSelector =
            new ThemeVersionSelector() {
                public boolean supportsVersion(ThemeVersionInfo versionInfo) {
                    return versionInfo.isSupportedInCSSMobile();
                }
            };

    /**
     * A theme version selector for CSS WAP.
     */
    private static ThemeVersionSelector cssWapSelector =
            new ThemeVersionSelector() {
                public boolean supportsVersion(ThemeVersionInfo versionInfo) {
                    return versionInfo.isSupportedInCSSWAP();
                }
            };

    /**
     * The top level directory where the generated code should be written.
     */
    protected File generatedDir;

    /**
     * Map the stores away the different Style Properties that we generate
     */
    protected Map stylePropertyMap;

    /**
     * Map that stores away the iformation needed to generate the KeywordMappers
     */
    protected Map enumerationMap;

    /**
     * The schema parser.
     */
    protected SchemaParser parser;

    /**
     * The JDOM namespace for the themePropertyDefinitions.xml file.
     */
    protected Namespace themePropertyDefinitionNamespace = Namespace.getNamespace
            ("http://vine.uk.volantis.com/architecture/document/xmlns/ThemeInfo");

    /**
     * Stack of objects used while parsing the themePropertyDefinitions.xml
     * file.
     */
    private Stack parseStack;

    /**
     * The factory used to create objects that represent the theme definitions.
     */
    private DefinitionsFactory definitionsFactory =
            DefinitionsFactory.getDefaultInstance();

    /**
     * A map from property name to Property object.
     */
    private Map properties = new HashMap();

    /**
     * A map from type definition name to TypeDefinition object.
     */
    private Map typeDefs = new HashMap();

    /**
     * Controls whether debugging is generated when parsing theme properties.
     */
    private boolean debugParseThemePropertyDefinitions = false;

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
     * <strong>Mariner-Theme.xsd</strong>.  If this is a resource it must be
     * specified as a full path including the full package names, e.g.: <br />
     * <code>full/package/name/resource.file</code> or <br />
     * <code>/full/package/name/resource.file</code>.
     * </td>
     * </tr>
     *
     * <tr>
     * <td>Second</td>
     * <td>Property definitions file</td>
     * <td>This is the property definitions file which is used to pick up any
     * additional information that is required by the code generator
     * but which is not in the Mariner-Theme.xsd.  If this is a resource it
     * must be specified as a full path including the full package names, e.g.:
     * <br />
     * <code>full/package/name/resource.file</code> or <br />
     * <code>/full/package/name/resource.file</code>.
     * </td>
     * </tr>
     *
     * <tr>
     * <td>Third</td>
     * <td>Output directory</td>
     * <td>This is where all the generated source files (based on the
     * schema and property definitions) should be placed.
     * </td>
     * </tr>
     *
     * </table>
     *
     * These first two of these parameters are first looked for as files, and
     * then if not found, as resources.  If the files are then not found then
     * the program will terminate and report an error.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        try {
            // Validate the number of parameters.  Any more than are required
            // are just silently ignored.
            if (args.length < 3) {
                throw new IllegalArgumentException(
                        "A schema file, a definitions file, and an output " +
                        "directory must be specified (in that order) for " +
                        "this program to be able to run successfully.");
            }

            String schemaName = args[0];
            String definitionsName = args[1];
            String outputDirectory = args[2];

            // Locate the files - except obviously the output directory
            InputStream schemaStream;
            InputStream definitionsStream;
            File schema = new File(schemaName);
            File definitions = new File(definitionsName);

            if (schema.exists() && schema.canRead()) {
                schemaStream = new FileInputStream(schema);
            } else {
                // It doesn't exist so try and find it as a resource
                schemaStream = ParseThemeSchema.class.getResourceAsStream(
                        (schemaName.startsWith("/") ? "" : "/") + schemaName);
            }

            if (definitions.exists() && definitions.canRead()) {
                definitionsStream = new FileInputStream(definitions);
            } else {
                // It doesn't exist so try and find it as a resource
                definitionsStream = ParseThemeSchema.class.getResourceAsStream(
                        (definitionsName.startsWith("/") ? "" : "/") +
                        definitionsName);
            }

            // Now validate that there are streams to use, regardless of where
            // they came from
            if (schemaStream == null) {
                // Oops it's all gone wrong!  Schema cannot be found
                throw new IllegalArgumentException(
                        "The schema file '" + schemaName +
                        "' cannot be found as " +
                        "either a file or a resource");
            }

            if (definitionsStream == null) {
                // Oops it's all gone wrong!  Definitions cannot be found
                throw new IllegalArgumentException(
                        "The definitions file specified cannot be found as " +
                        "either a file or a resource");
            }

            System.out.println("Schema - " + schemaName);
            System.out.println("Definitions - " + definitionsName);
            System.out.println("Generated Directory - " + outputDirectory);
            System.out.println();

            // Everything should be OK here, so proceed with the parsing
            ParseThemeSchema instance = new ParseThemeSchema();
            instance.parse(schemaStream,
                           definitionsStream,
                           new File(outputDirectory));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Parse the xsd and definitions from the supplied streams and parse
     * them, generating the code.
     * @param themeSchema the stream from which the schema should be read.
     * @param themeDefinitions the stream from which the definitions should
     * be read.
     * @param generatedDir the directory into which the generated code should
     * be put.
     */
    public void parse(InputStream themeSchema,
                      InputStream themeDefinitions,
                      File generatedDir) {

        this.generatedDir = generatedDir;

        stylePropertyMap = new TreeMap();
        enumerationMap = new HashMap();

        try {
            SAXBuilder builder;

            // Parse the themePropertyDefinitions.xml file to pick up any
            // additional information that is required by the code generator
            // but which is not in the Mariner-Theme.xsd.
            // Eventually the parsing of this and the
            // ThemeSchemaAnnotations.xml will completely replace the parsing
            // of the Mariner-Theme.xsd.
            parseThemePropertyDefinitions(themeDefinitions);

            builder = new SAXBuilder("com.volantis.xml.xerces.parsers.SAXParser",
                                     false);

            Document document = builder.build(themeSchema);

            // instantiate the parser
            parser = new SchemaParser(new ThemeFactory());

            // add all the processing instructions
            parser.addProcessingInstructionTarget(
                    "property", new ThemeTarget());

            parser.addProcessingInstructionTarget(
                    "value_type", new ThemeValueTypeTarget());

            parser.addProcessingInstructionTarget(
                    "enumeration", new ThemeEnumerationTarget(enumerationMap));

            List schemaObjects = parser.parse(document);

            for (Iterator i = schemaObjects.iterator(); i.hasNext();) {
                SchemaObject object = (SchemaObject) i.next();
                processSchemaObject(object);
            }

            generateKeywordMappers();

            // generate the StylePropertyDetails class
            StylePropertyDetailsGenerator stylePropertyDetailsGenerator =
                    new StylePropertyDetailsGenerator(generatedDir,
                                                      themePackage,
                            enumerationMap);
            stylePropertyDetailsGenerator.generate(stylePropertyMap.values());

            // generate the DefaultCSSVersionFactory classes
            CSSVersionFactoryGenerator css2VersionFactoryGenerator =
                    new CSSVersionFactoryGenerator(generatedDir, cssVersionPackage,
                            "CSS2", propertiesPackage, true, enumerationMap,
                            css2Selector);
            css2VersionFactoryGenerator.generate(stylePropertyMap.values());

            CSSVersionFactoryGenerator css1VersionFactoryGenerator =
                    new CSSVersionFactoryGenerator(generatedDir, cssVersionPackage,
                            "CSS1", propertiesPackage, false, enumerationMap,
                            css1Selector);
            css1VersionFactoryGenerator.generate(stylePropertyMap.values());

            CSSVersionFactoryGenerator cssMobileVersionFactoryGenerator =
                    new CSSVersionFactoryGenerator(generatedDir, cssVersionPackage,
                            "CSSMobile", propertiesPackage, true, enumerationMap,
                            cssMobileSelector);
            cssMobileVersionFactoryGenerator.generate(stylePropertyMap.values());

            CSSVersionFactoryGenerator cssWapVersionFactoryGenerator =
                    new CSSVersionFactoryGenerator(generatedDir, cssVersionPackage,
                            "CSSWAP", propertiesPackage, true, enumerationMap,
                            cssWapSelector);
            cssWapVersionFactoryGenerator.generate(stylePropertyMap.values());

            // Generate the XMLPropertyConstants class
            XMLPropertyConstantsGenerator xpcg =
                    new XMLPropertyConstantsGenerator(generatedDir, xmlAccessorPackage);
            xpcg.generate(stylePropertyMap.values());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (JDOMException jdome) {
            jdome.printStackTrace();
        } finally {
            try {
                if (themeSchema != null) {
                    themeSchema.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Push an object onto the parse stack.
     * @param object The object to push.
     */
    public void pushObject(Object object) {
        parseStack.push(object);
    }

    /**
     * Pop the object off the top of the stack.
     * @return The object that was on the top of the stack.
     */
    public Object popObject() {
        return parseStack.pop();
    }

    /**
     * Search through the stack from the top to the bottom for the first object
     * that is an instance of the specific class.
     * @param clazz The class of objects to look for, typically this will be
     * an interface.
     * @return The first instance of the specified class, or null if none could
     * be found.
     */
    public Object findObject(Class clazz) {
        for (int i = parseStack.size() - 1; i >= 0; i -= 1) {
            Object object = parseStack.get(i);
            if (clazz.isInstance(object)) {
                return object;
            }
        }
        return null;
    }

    /**
     * Add the specified property to the map of properties.
     * @param property The property to add, its name must not be null.
     */
    public void addProperty(Property property) {
        String name = property.getName();
        if (name == null) {
            throw new IllegalArgumentException("Name not set on property");
        }

        properties.put(name, property);
    }

    /**
     * Add the specified type definition to the map of type definitions.
     * @param typeDef The type definition to add, its name must not be null.
     */
    public void addTypeDef(TypeDefinition typeDef) {
        String name = typeDef.getName();
        if (name == null) {
            throw new IllegalArgumentException(
                    "Name not set on type definition");
        }

        typeDefs.put(name, typeDef);
    }

    /**
     * Get the property with the specified name.
     * @param propertyName The name of thr property to find.
     * @return The property with the specified name, or null if it could not
     * be found.
     */
    public Property getProperty(String propertyName) {
        Property property = (Property) properties.get(propertyName);
        return property;
    }

    /**
     * Get the type definition with the specified name.
     * @param typeDefName The name of thr property to find.
     * @return The type definition with the specified name, or null if it
     * could not be found.
     */
    public TypeDefinition getTypeDef(String typeDefName) {
        TypeDefinition typeDef = (TypeDefinition) typeDefs.get(typeDefName);
        return typeDef;
    }

    /**
     * Store a type in the next appropriate object from the stack. This is
     * either a TypeList or a Property.
     * <p>Note that it is important that the type should be stored directly
     * in a Property rather than passing it back through a TypeList, since the
     * type may be required before the property has been fully processed.</p>
     * @param type The type to store
     */
    private void storeType(Type type) {
        boolean searching = true;
        for (int i = parseStack.size() - 1; searching && i >= 0; i -= 1) {
            Object object = parseStack.get(i);
            if (object instanceof TypeList) {
                ((TypeList) object).addType(type);
                searching = false;
            } else if (object instanceof Property) {
                ((Property) object).setType(type);
            }
        }
    }

    /**
     * Parse the architecture specified themePropertyDefinitions.xml file for
     * additional information needed by the code generator.
     * @param themePropertyDefinitions The file to parse.
     * @throws JDOMException If there was a problem building the JDOM.
     * @throws IOException If there was a problem accessing the file.
     */
    public void parseThemePropertyDefinitions(
            InputStream themePropertyDefinitions)
            throws JDOMException, IOException {

        // Initialise the parse stack.
        parseStack = new Stack();

        // Obtain the XML document
        SAXBuilder builder;
        builder = new SAXBuilder("com.volantis.xml.xerces.parsers.SAXParser",
                                 false);
        Document document = builder.build(themePropertyDefinitions);

        // Now process the document
        processThemePropertyDocument(document);
    }

    /**
     * Recurse down through the document extracting the information needed.
     * @param document The document to analyse.
     */
    private void processThemePropertyDocument(Document document) {
        System.out.println("Processing theme property document");

        Element root = document.getRootElement();
        processThemePropertyElement(root);
    }

    /**
     * Extract the required information from an element within the theme
     * definitions file.
     * @param element The element to analyse.
     */
    private void processThemePropertyElement(Element element) {
        // Ignore any elements that are not in the theme namespace.
        if (!element.getNamespace().equals(themePropertyDefinitionNamespace)) {
            return;
        }

        String name = element.getName();

        if (debugParseThemePropertyDefinitions) {
            System.out.println("Beginning '" + name + "'");
        }

        if (name.equals("property")) {

            // Create a new property.
            Property property = definitionsFactory.createProperty();
            pushObject(property);

            processThemePropertyChildren(element);

            popObject();

            // Add the property into the map for looking up later.
            addProperty(property);

        } else if (name.equals("typeDefinition")) {

            // Create a new type definition.
            TypeDefinition typeDef = definitionsFactory.createTypeDefinition();
            TypeList typeList = new TypeList();
            pushObject(typeList);
            pushObject(typeDef);

            processThemePropertyChildren(element);

            popObject();
            popObject();

            if (!typeList.getList().isEmpty()) {
                typeDef.setType((Type) typeList.getList().get(0));
            }

            // Add the type definition into the map for looking up later.
            addTypeDef(typeDef);

        } else if (name.equals("name")) {

            // Set the name of the containing named object.
            Named named = (Named) findObject(Named.class);
            named.setName(element.getText());

        } else if (name.equals("description")) {
            // Ignore description elements and their contents, they have no
            // effect on code generation.
        } else if (name.equals("initialValue")) {

            processInitialValue(element);

        } else if (name.equals("integerValue")) {
            String valStr = element.getText();
            try {
                int intVal = Integer.parseInt(valStr);
                IntegerValue value = definitionsFactory.createIntegerValue();
                value.setInteger(intVal);
                ValueContainer container =
                        (ValueContainer) findObject(ValueContainer.class);
                container.addValue(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number: '" + valStr + "'");
            }
        } else if (name.equals("lengthValue")) {
            String valStr = element.getText();
            try {
                int intVal = Integer.parseInt(valStr);
                LengthValue value = definitionsFactory.createLengthValue();
                value.setInteger(intVal);
                value.setUnits(element.getAttributeValue("units"));
                ValueContainer container =
                        (ValueContainer) findObject(ValueContainer.class);
                container.addValue(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number: '" + valStr + "'");
            }
        } else if (name.equals("angleValue")) {
            String valStr = element.getText();

            try {
                double doubleVal = Double.parseDouble(valStr);
                String units = element.getAttributeValue("units");
                ValueContainer container =
                        (ValueContainer) findObject(ValueContainer.class);
                AngleValue value = definitionsFactory.createAngleValue();
                value.setAngle(doubleVal);
                value.setUnits(units);
                container.addValue(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid angle: '" + valStr + "'");
            }
        } else if (name.equals("stringValue")) {
            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            StringValue value = definitionsFactory.createStringValue();
            value.setString(element.getText());
            container.addValue(value);
        } else if (name.equals("colorValue")) {
            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            ColorValue value = definitionsFactory.createColorValue();
            value.setColorName(element.getText());
            container.addValue(value);
        } else if (name.equals("inheritValue")) {
            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            InheritValue value = definitionsFactory.createInheritValue();
            container.addValue(value);
        } else if (name.equals("percentageValue")) {
            String valStr = element.getText();
            try {
                double percentVal = Double.parseDouble(valStr);
                PercentageValue value =
                        definitionsFactory.createPercentageValue();
                value.setPercentage(percentVal);
                ValueContainer container =
                        (ValueContainer) findObject(ValueContainer.class);
                container.addValue(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid percentage: '" + valStr + "'");
            }
        } else if (name.equals("timeValue")) {
            String valStr = element.getText();

            try {
                double doubleVal = Double.parseDouble(valStr);
                String units = element.getAttributeValue("units");
                ValueContainer container =
                        (ValueContainer) findObject(ValueContainer.class);
                TimeValue value = definitionsFactory.createTimeValue();
                value.setTime(doubleVal);
                value.setUnits(units);
                container.addValue(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid time: '" + valStr + "'");
            }
        } else if (name.equals("pairValue")) {
            PairValueContainer pair = new PairValueContainer();

            pushObject(pair);
            processThemePropertyChildren(element);
            popObject();

            PairValue value = definitionsFactory.createPairValue();
            value.setFirst(pair.getFirst());
            value.setSecond(pair.getSecond());

            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            container.addValue(value);
        } else if (name.equals("listValue")) {
            ListValueContainer pair = new ListValueContainer();

            pushObject(pair);
            processThemePropertyChildren(element);
            popObject();

            ListValue value = definitionsFactory.createListValue();
            Value next = null;
            while((next = pair.getNext())!=null){
                value.setNext(next);
            }
            
            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            container.addValue(value);
        } else if (name.equals("frequencyValue")) {
            String valStr = element.getText();

            try {
                double doubleVal = Double.parseDouble(valStr);
                String units = element.getAttributeValue("units");
                ValueContainer container =
                        (ValueContainer) findObject(ValueContainer.class);
                FrequencyValue value = definitionsFactory.createFrequencyValue();
                value.setNumber(doubleVal);
                value.setUnits(units);
                container.addValue(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid frequency: '" + valStr + "'");
            }
        } else if (name.equals("fractionValue")) {
            PairValueContainer pair = new PairValueContainer();

            pushObject(pair);
            processThemePropertyChildren(element);
            popObject();

            FractionValue value = definitionsFactory.createFractionValue();
            value.setNumerator(pair.getFirst());
            value.setDenominator(pair.getSecond());

            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            container.addValue(value);
        } else if (name.equals("keywordRef")) {

            // Resolve the keyword reference to the keyword (must come before)
            // and then create a representation.
            ValueContainer container =
                    (ValueContainer) findObject(ValueContainer.class);
            String keywordName = element.getText();

            // Get the keyword from the property's type.
            Property property = (Property) findObject(Property.class);
            Type type = property.getType();
            if (type != null) {
                KeywordSearchContainer keywordCont =
                        new KeywordSearchContainer();
                keywordCont.setKeywordName(keywordName);
                TypeVisitor keywordFinder = new AbstractTypeVisitor() {
                    public void visitKeywords(Keywords visitee, Object obj) {
                        KeywordSearchContainer ksc =
                                (KeywordSearchContainer) obj;
                        Keyword keyword =
                                visitee.getKeyword(ksc.getKeywordName());
                        if (keyword != null) {
                            ksc.setKeyword(keyword);
                        }
                    }

                    public void visitTypeRef(TypeRef visitee, Object obj) {
                        TypeDefinition td = getTypeDef(visitee.getReference());
                        if (td != null) {
                            Type t = td.getType();
                            if (t != null) {
                                t.accept(this, obj);
                            }
                        }
                    }
                };
                type.accept(keywordFinder, keywordCont);
                if (keywordCont.getKeyword() == null) {
                    System.out.println("Keyword " + keywordName
                                       + " not found in property "
                                       + property.getName());
                } else {
                    KeywordReference reference
                            = definitionsFactory.createKeywordReference();
                    reference.setKeyword(keywordCont.getKeyword());
                    container.addValue(reference);
                }
            } else {
                System.out.println(
                        "Property has no type: could not find keywords");
            }
        } else if (name.equals("themeDefinition")) {
            // Process all the children.
            processThemePropertyChildren(element);
        } else if (name.equals("choiceType")) {
            ChoiceType choiceType = definitionsFactory.createChoiceType();
            TypeList typeList = new TypeList();

            pushObject(choiceType);
            pushObject(typeList);
            processThemePropertyChildren(element);
            popObject();
            popObject();

            Iterator it = typeList.getList().iterator();
            while (it.hasNext()) {
                choiceType.addType((Type) it.next());
            }
            storeType(choiceType);
        } else if (name.equals("pairType")) {
            PairType pairType = definitionsFactory.createPairType();

            pushObject(pairType);
            processThemePropertyChildren(element);
            popObject();

            storeType(pairType);
        } else if (name.equals("fractionType")) {
            FractionType fractionType = definitionsFactory.createFractionType();

            pushObject(fractionType);
            processThemePropertyChildren(element);
            popObject();
            storeType(fractionType);
        } else if (name.equals("first")) {
            TypeList typeList = new TypeList();
            NameHolder nameHolder = new NameHolder();

            pushObject(typeList);
            pushObject(nameHolder);
            processThemePropertyChildren(element);
            popObject();
            popObject();

            if (!typeList.getList().isEmpty()) {
                PairType pair = (PairType) findObject(PairType.class);
                pair.setFirst((Type) typeList.getList().get(0));
            }
        } else if (name.equals("second")) {
            TypeList typeList = new TypeList();
            NameHolder nameHolder = new NameHolder();

            pushObject(typeList);
            pushObject(nameHolder);
            processThemePropertyChildren(element);
            popObject();
            popObject();

            if (!typeList.getList().isEmpty()) {
                PairType pair = (PairType) findObject(PairType.class);
                pair.setSecond((Type) typeList.getList().get(0));
            }
        } else if (name.equals("numerator")) {
            TypeList typeList = new TypeList();
            NameHolder nameHolder = new NameHolder();

            pushObject(typeList);
            pushObject(nameHolder);
            processThemePropertyChildren(element);
            popObject();
            popObject();

            if (!typeList.getList().isEmpty()) {
                FractionType fraction = (FractionType) findObject(FractionType.class);
                fraction.setNumerator((Type) typeList.getList().get(0));
            }
        } else if (name.equals("denominator")) {
            TypeList typeList = new TypeList();
            NameHolder nameHolder = new NameHolder();

            pushObject(typeList);
            pushObject(nameHolder);
            processThemePropertyChildren(element);
            popObject();
            popObject();

            if (!typeList.getList().isEmpty()) {
                FractionType fraction = (FractionType) findObject(FractionType.class);
                fraction.setDenominator((Type) typeList.getList().get(0));
            }
        }else if (name.equals("type")) {
            // Process the children in order to find the keyword definitions.
            processThemePropertyChildren(element);
        } else if (name.equals("typeRef")) {

            TypeRef typeRef = definitionsFactory.createTypeRef();
            typeRef.setReference(element.getText());
            Property property = (Property) findObject(Property.class);
            property.setType(typeRef);

        } else if (name.equals("keywords")) {

            Keywords keywords = definitionsFactory.createKeywords();

            pushObject(keywords);
            processThemePropertyChildren(element);
            popObject();

            storeType(keywords);

        } else if (name.equals("keyword")) {

            // Define a new keyword and add it into the containing set.
            Keyword keyword = definitionsFactory.createKeyword();

            pushObject(keyword);

            processThemePropertyChildren(element);

            popObject();

            Keywords keywords = (Keywords) findObject(Keywords.class);
            keywords.addKeyword(keyword);
        } else if (name.equals("inherited")) {
            Property prop = (Property) findObject(Property.class);
            prop.setInherited(
                    Boolean.valueOf(element.getText()).booleanValue());
            // These are named elements that we are deliberately ignoring for
            // the time being.
        } else if (name.equals("element") ||
                name.equals("media") ||
                name.equals("specifications") ||
                name.equals("integerType") ||
                name.equals("colorType") ||
                name.equals("percentageType") ||
                name.equals("lengthType") ||
                name.equals("uriType") ||
                name.equals("mcsComponentURIType") ||
                name.equals("appliesTo") ||
                name.equals("percentages") ||
                name.equals("stringType") ||
                name.equals("dependentType") ||
                name.equals("angleType") ||
                name.equals("functionType") ||
                name.equals("timeType") ||
                name.equals("numberType") ||
                name.equals("fractionType") ||
                name.equals("frequencyType") ||
                name.equals("emptyList") ||
                // From discussion with Paul Duffin, unorderedSetType is being
                // phased out, and can be safely ignored since it is only used
                // in one case, and there the initialValue falls outside the
                // unorderedSetType.
                name.equals("unorderedSetType") ||
                // Dependent values - such as color, text alignment etc. -
                // depend on some value outside of the scope of the property
                // definitions (such as the user agent or the writing
                // direction). A generic 'initial value' can not be
                // calculated for them.
                name.equals("dependentValue")) {
            // These are types which contain a single other type - lists and
            // ordered sets. Rather than creating a simple type that can contain
            // another type, they can be ignored, allowing the type tree to be
            // navigated without adding an additional (non-functional) layer.
        } else if (name.equals("listType") ||
                name.equals("orderedSetType")) {
            processThemePropertyChildren(element);
        } else if (name.equals("propertyRef")) {
            // This only works inside initialValue.
            PropertyReference reference = (PropertyReference)
                    findObject(PropertyReference.class);
            String propertyName = element.getText();
            if (reference == null) {
                System.out.println("Ignoring reference to property '" +
                                   propertyName + "'");
            } else {
                reference.setPropertyName(propertyName);
            }
        } else if (name.equals("computedRef")) {
            // This only works inside initialValue.
            ComputedReference reference = (ComputedReference)
                findObject(ComputedReference.class);
            if (reference == null) {
                System.out.println("Ignoring reference to computedRef");
            } else {

                Named propertyName = definitionsFactory.createProperty();
                pushObject(propertyName);
                Rules rules = definitionsFactory.createRules();
                pushObject(rules);
                processThemePropertyChildren(element);
                popObject();
                popObject();

                reference.setPropertyName(propertyName.getName());
                reference.setRules(rules.getRuleSet());
            }

        } else if (name.equals("rule")) {

            Rules ruleSet = (Rules) findObject(Rules.class);

            if (ruleSet == null) {
                System.out.println("Ignoring 'rule' element");
            } else {
                Rule rule = definitionsFactory.createRule();
                pushObject(rule);
                processThemePropertyChildren(element);
                popObject();

                ruleSet.addRule(rule.getFrom(), rule.getTo());
            }

        } else if (name.equals("from")) {
            Rule rule = (Rule) findObject(Rule.class);
            if (rule == null) {
                System.out.println("Ignoring 'from' element");
            } else {
                rule.setFrom(element.getText());
            }

        } else if (name.equals("to")) {
            Rule rule = (Rule) findObject(Rule.class);
            if (rule == null) {
                System.out.println("Ignoring 'to' element");
            } else {
                rule.setTo(element.getText());
            }

        } else if (name.equals("functionType")) {

        } else {
            // Unrecognised element - print the name out
            System.out.println("Unrecognised element: " + name);
        }

        if (debugParseThemePropertyDefinitions) {
            System.out.println("Ending '" + name + "'");
        }
    }

    private void processInitialValue(Element element) {

        // Create a container to hold the initial value.
        SingleValueContainer container = new SingleValueContainer();
        pushObject(container);

        // Create a container for a property reference.
        PropertyReference reference = new PropertyReference();
        pushObject(reference);

        // Create a container for a computed reference.
        ComputedReference computedRef = new ComputedReference();
        pushObject(computedRef);

        // Process the children.
        processThemePropertyChildren(element);

        popObject();
        popObject();
        popObject();

        String computedPropertyRef = computedRef.getPropertyName();
        String propertyReference = reference.getPropertyName();
        Value initialValue = container.getValue();

        if ((propertyReference != null   && initialValue != null) ||
            (propertyReference != null   && computedPropertyRef != null) ||
            (computedPropertyRef != null && initialValue != null)) {
            throw new IllegalStateException(
                    "Only a property reference, computed reference" +
                        " or an initial value are allowed, not both");
        }

        Property property = (Property) findObject(Property.class);
        ValueSource source;
        if (initialValue != null) {
            source = new FixedValueSource(container.getValue());
            property.setInitialValue(container.getValue());
        } else if (propertyReference != null) {
            source = new PropertyValueSource(propertyReference);
        } else if (computedPropertyRef != null) {
            source = new ComputedValueSource(
                computedPropertyRef, computedRef.getRules());
        } else {
            source = new NullValueSource();
        }
        property.setInitialValueSource(source);
    }

    /**
     * Recurse down through the children of the element.
     * @param element The element whose children should be examined.
     */
    private void processThemePropertyChildren(Element element) {
        List children = element.getChildren();
        for (Iterator i = children.iterator(); i.hasNext();) {
            Element child = (Element) i.next();
            processThemePropertyElement(child);
        }
    }

    /**
     * Generate the KeywordMapper & KeywordMapperFactories Classes
     */
    public void generateKeywordMappers() {

        StylePropertyInfo[] propertyInfo =
                new StylePropertyInfo[stylePropertyMap.size()];
        stylePropertyMap.values().toArray(propertyInfo);

        // Set up the keywordMapper generator
        EnumerationGenerator mapperGenerator =
                new EnumerationGenerator(generatedDir, mappersPackage);

        StyleKeywordsGenerator keywordsGenerator =
                new StyleKeywordsGenerator(generatedDir);

        keywordsGenerator.generateUberKeywords(enumerationMap, valuesPackage);

        for (Iterator i = enumerationMap.values().iterator(); i.hasNext();) {
            EnumerationInfo ei = (EnumerationInfo) i.next();

            mapperGenerator.generateKeywordMapper(ei, cssMappersPackage);

            keywordsGenerator.generateKeywordsClass(ei, propertiesPackage);
        }

        AllowableKeywordsAccessorGenerator allowableKeywordsAccessorGenerator =
                new AllowableKeywordsAccessorGenerator(
                        generatedDir, propertiesPackage);
        allowableKeywordsAccessorGenerator.generate(propertyInfo);

        // generate the KeywordMapper factories
        KeywordMapperFactoryGenerator kmfg =
                new KeywordMapperFactoryGenerator(generatedDir, mappersPackage);
        kmfg.generateFactoryInterface(enumerationMap, mappersPackage);
        kmfg.generateFactoryStub(enumerationMap,
                                 keywordMapperFactoryStubPackage);
        kmfg.generateCSSFactoryClass(enumerationMap, cssMappersPackage);

        // generate the KeywordMapperFactorAdapter class
        KeywordMapperAccessorGenerator mapperFactoryAccessorGenerator =
                new KeywordMapperAccessorGenerator(generatedDir,
                                                   mappersPackage);

        mapperFactoryAccessorGenerator.generate(propertyInfo);
    }

    /**
     * Do the right thing depending on the type of the specified object.
     * @param object The <code>SchemaObject</code> to process.
     */
    protected void processSchemaObject(SchemaObject object) {
        if (object instanceof ThemeElementInfo) {
            processElement((ThemeElementInfo) object);
        } else if (object instanceof ElementGroup) {
            processGroup((ElementGroup) object);
        } else if (object instanceof ElementReference) {
            // do nothing
        } else if (object instanceof ElementGroupReference) {
            // do nothing
        } else if (object instanceof ComplexType) {
            // do nothing
        } else if (object instanceof AttributeInfo) {
            // do nothing
        } else {
            throw new IllegalArgumentException("Unhandled object " + object);
        }
    }

    /**
     * Do any ElementInfo specific processing.
     * @param info The <code>ElementInfo</code> to process.
     */
    private void processElement(ThemeElementInfo info) {
        System.out.println("Processing element definition '" +
                           info.getName() + "'");
        if (info.isStylePropertyElement()) {
            processStylePropertyElement(info);
        }
    }

    /**
     * Do any Group specific processing.
     * @param info The <code>GroupInfo</code> to process.
     */
    private void processGroup(ElementGroup info) {
        System.out.println("Processing group definition '" +
                           info.getName() + "'");
    }

    /**
     * Process a Theme Element
     * @param info a ThemeElementInfo object
     */
    protected void processStylePropertyElement(ThemeElementInfo info) {

        System.out.println("PROCESSING STYLE PROPERTY ELEMENT ->" +
                           info.getName());

        String propertyName = info.getName();

        // Get the property, note this may not yet have been initialised.
        Property property = getProperty(propertyName);

        // generate the accessor
        StylePropertyInfo spi = new StylePropertyInfo();

        // Save this away internally so we can extract it later and get the
        // version info out.
        spi.setThemeElementInfo(info);

        spi.setPropertyName(info.getName());
        spi.setNaturalName(info.getNaturalName());
        spi.setXMLElementName(info.getName());
        spi.setEnumerationName(info.getEnumerationName());

        // Merge the information from the Property.
        spi.merge(property);

        // read the database info into the ThemeTableInfo by
        // iterating over the value types that this property supports and
        // adding the database columns that are needed for each type.
        for (Iterator i = info.valueTypeIterator(); i.hasNext();) {

            ValueTypeInfo valueType = (ValueTypeInfo) i.next();
            spi.addValueType(valueType);
        }

        // store the property away to be used by other generators
        stylePropertyMap.put(spi.getPropertyName(), spi);
    }

    /**
     * Simple container class for keywords.
     */
    private class KeywordSearchContainer {
        /**
         * The keyword held in the container.
         */
        private Keyword keyWord;

        /**
         * The name of the keyword the container is expected to hold.
         */
        private String keyWordName;

        /**
         * Set the keyword held in this container.
         * @param key The keyword held in this container
         */
        public void setKeyword(Keyword key) {
            keyWord = key;
        }

        /**
         * Get the keyword held in this container.
         * @return The keyword held in this container
         */
        public Keyword getKeyword() {
            return keyWord;
        }

        /**
         * Set the name of the keyword to be held in this container.
         * @param name The name of the keyword to be held in this container.
         */
        public void setKeywordName(String name) {
            keyWordName = name;
        }

        /**
         * Get the name of the keyword to be held in this container.
         * @return The name of the keyword to be held in this container.
         */
        public String getKeywordName() {
            return keyWordName;
        }
    }

    /**
     * A simple wrapper class for a List to store Types in.
     */
    private class TypeList {
        private List typeList = new ArrayList();

        /**
         * Adds a type to this list.
         * @param type The type to add
         */
        public void addType(Type type) {
            typeList.add(type);
        }

        /**
         * Retrieves the list of types.
         * @return A List of types that have been added.
         */
        public List getList() {
            return typeList;
        }
    }

    /**
     * Holder for a name value - used to catch names for objects that are not
     * themselves Named, but need to trap the name element (first and second,
     * for example).
     */
    private class NameHolder implements Named {
        /**
         * The name stored in this holder.
         */
        private String name;

        // Javadoc inherited
        public void setName(String name) {
            this.name = name;
        }

        // Javadoc inherited
        public String getName() {
            return name;
        }
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05  10845/1 pduffin VBM:2005121511 Moved architecture files from CVS to MCS Depot

 29-Nov-05  10347/1 pduffin VBM:2005111405 Massive changes for performance

 29-Sep-05  9654/1  pduffin VBM:2005092817 Added support for expressions and functions in styles

 16-Sep-05  9512/1  pduffin VBM:2005091408 Fixed up some issues with theme generator and added support for border-collapse and caption-side

 05-Sep-05  9407/1  pduffin VBM:2005083007 Removed old themes model

 18-Aug-05  9007/1  pduffin VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05  7997/1  pduffin VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 14-Jan-05  6607/3  ianw    VBM:2005010703 Minor fixup

 13-Jan-05  6607/1  ianw    VBM:2005010703 New DB Schema generation

 08-Dec-04  6416/3  ianw    VBM:2004120703 New Build

 08-Dec-04  6416/1  ianw    VBM:2004120703 New Build

 22-Nov-04  5733/7  geoff   VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04  5733/5  geoff   VBM:2004093001 Support OMA WCSS subset of CSS2

 26-Oct-04  5968/1  adrianj VBM:2004083105 Add inherited property to StylePropertyDetails

 21-Oct-04  5833/3  adrianj VBM:2004082605 Fix initial values for StylePropertyDetails

 08-Oct-04  5740/3  matthew VBM:2004100708 Allow code generators to accept a resource name as well as a filename

 08-Oct-04  5740/1  matthew VBM:2004100708 allow Code generators to read from files or resources

 21-Sep-04  5573/3  claire  VBM:2004092016 New Build Mechanism: Allowing schema files to be resources

 02-Sep-04  5354/1  tom VBM:2004082008 started device theme normalization

 07-Apr-04  3272/2  philws  VBM:2004021117 Fix merge conflicts

 06-Apr-04  3746/1  mat VBM:2004031908 Support short table names

 26-Mar-04  3550/4  pduffin VBM:2004032306 Removed commented out code, added some more java doc and removed additional unused class

 25-Mar-04  3550/2  pduffin VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 21-Jan-04  2592/1  doug    VBM:2003112712 Implementation of the ThemeElementRenderer and ThemeElementParser interfaces

 12-Jan-04  2360/7  andy    VBM:2003121710 supermerge

 12-Jan-04  2360/4  andy    VBM:2003121710 fixed conflict

 04-Jan-04  2360/1  andy    VBM:2003121710 added PROJECT column to all tables

 12-Jan-04  2326/5  geoff   VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors (supermerge)

 05-Jan-04  2326/2  geoff   VBM:2003121707 Import/Export: XML Accessors: Modify Themes accessors

 09-Jan-04  2467/3  doug    VBM:2003112408 Added the StyleValueType & StylePropertyDetails type-safe enums

 17-Dec-03  2242/1  andy    VBM:2003121702 vbm2003121702

 30-Sep-03  1475/1  byron   VBM:2003092606 Move contents of accessors.xml package to jdom package

 ===========================================================================
*/
