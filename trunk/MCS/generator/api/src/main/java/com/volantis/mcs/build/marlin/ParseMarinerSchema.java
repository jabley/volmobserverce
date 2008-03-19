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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/ParseMarinerSchema.java,v 1.30 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Nov-01    Paul            VBM:2001111402 - Created.
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes.
 * 29-Nov-01    Paul            VBM:2001112805 - Added support for generating
 *                              jsp tags.
 * 30-Nov-01    Paul            VBM:2001112909 - Added support for field and
 *                              select field groups, made all generated non
 *                              abstract classes final and merged the
 *                              attributes from attribute groups together
 *                              rather than simply added them on.
 * 07-Dec-01    Paul            VBM:2001120704 - Renamed classes for "em" from
 *                              Emphasize... to Emphasis..., also deprecated
 *                              the pane attribute for all those elements which
 *                              are inline but were mistakenly made block.
 * 19-Dec-01    Paul            VBM:2001120506 - Use new schema parser, along
 *                              with information which is in the schema itself
 *                              to generate the code. This allowed a lot of
 *                              code to be removed.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - The directory for generating
 *                              the code is passed in as an argument.
 * 10-Jan-02    Adrian          VBM:2001122803 - Dynamic Visual Elements moved
 *                              to ExistingElements array
 * 14-Jan-02    Paul            VBM:2002011414 - Moved some helper code from
 *                              ParseMarinerSchema into GenerateUtilities.
 * 17-Jan-02	Steve           VBM:2002011102 - Added dividehint into list of
 *                              existing elements so it doesnt get generated.
 * 18-Jan-02    Adrian          VBM:2001121003 - Audio and Chart Elements moved
 *                              to ExistingElements array.
 * 23-Jan-02    Adrian          VBM:2002012202 - check for region tag and mixed
 *                              types when generating tld and tag files.  if
 *                              region then body is always set to JSP.  All
 *                              others, then if mixed is true the body set to
 *                              JSP.
 * 23-Jan-02    Paul            VBM:2002012202 - Removed MarinerFull and
 *                              differentiated between the jsp name and the
 *                              papi name of an attribute.
 * 25-Jan-02    Paul            VBM:2002012202 - Removed hack which made
 *                              xfaction's name attribute optional.
 * 25-Jan-02    Paul            VBM:2002012503 - Added code to resolved
 *                              attributes which are mariner expressions.
 * 31-Jan-02    Paul            VBM:2001122105 - Handle empty elements better
 *                              and set tag names on protocol attributes
 *                              classes where needed.
 * 19-Feb-02    Paul            VBM:2001100102 - Added support for tag extra
 *                              info classes, specifically for canvas.
 * 25-Feb-02    Paul            VBM:2002022503 - Commented out debugging
 *                              line.
 * 28-Feb-02    Paul            VBM:2002022804 - Removed workaround of
 *                              inconsistency on the name of the protocol class
 *                              for "ul" as it is no longer needed. Also made
 *                              the generated Jsp tags implement
 *                              getContentWriter and getDirectWriter instead
 *                              of writeContent and writeDirect.
 * 28-Feb-02    Mat             VBM:2002021203 - Added SSIInclude and SSIConfig
 *                              to existing elements.
 * 12-Mar-02    Steve           VBM:2002022203 Generate maml elements for all
 *                              elements encountered and generate the maml
 *                              element factory to build these elements at
 *                              run-time.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form and
 *                              onPaneActive related code.
 * 18-Mar-02    Ian             VBM:2002031203 - Change all calls to
 *                              writeLogger to pass package name, affected
 *                              methods are generateJspTag, generateMAMLElement
 *                              and writeMAMLFactory.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 19-Mar-02    Steve           VBM:2002021105 - Added formfragment, fragment
 *                              and layout elements to the existing tag list
 *                              as these do not need to be generated.
 *                              The PAPI attributes, MAML tag, JSP tags are
 *                              all generated for these elements.
 * 22-Feb-02    Paul            VBM:2002021802 - Fixed some minor problems with
 *                              ordering of calls to super.elementReset.
 * 22-Mar-02    Steve           VBM:2002031801  Do not generate code for the
 *                              substituteformat element.
 * 02-Apr-02    Mat             VBM:2002022009 - Changed PAPI references to API
 *                              so that other generators (eg IMDAPI) can use
 *                              the same names.
 * 03-Apr-02    Adrian          VBM:2002040201 - wrap logger.debug statements
 *                              in if (logger.isDebugEnabled()) condition to
 *                              prevent debug logging in production build.
 * 03-Apr-02    Paul            VBM:2002040205 - Generated dummy tag classes
 *                              and a dummy tag library.
 * 03-Apr-02    Adrian          VBM:2002040201 - backed out of change above as
 *                              the logger.debug statement wrapped did not
 *                              create extra objects so is no different to
 *                              calling logger.isDebugEnabled().
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * 20-May-02    Paul            VBM:2002032501 - Added array of extra info
 *                              class names indexed by jsp version and used
 *                              them instead of building the class name for
 *                              each version.
 * 23-May-02    Paul            VBM:2002042202 - Handle the anchor focus
 *                              events properly.
 * 14-Jun-02    Paul            VBM:2002053105 - Added wsdirectives to the
 *                              list of elements which are hand written.
 *  1-Jul-02    Steve           VBM:2002062401 - Added support for xfcontent
 *                              element.
 * 31-Jul-02    Paul            VBM:2002073008 - Added support for package
 *                              element.
 * 01-08-02     Sumit           VBM:2002073109 - Removed xfoptgroup from auto
 *                              generation
 * 06-Aug-02    Paul            VBM:2002073008 - Removed support for package
 *                              element.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added timer element
 * 18-Sep-02    Adrian          VBM:2002091001 - Updated method
 *                              generatePAPIImplAttributesClass to add validation
 *                              to styleClass setter methods.
 * 01-Oct-02    Allan           VBM:2002093002 - Added message to arrays
 *                              existingElements and ignoredElements so as
 *                              not to use it in the build for the moment.
 * 14-Oct-02    Mat             VBM:2002090207 - Amended generateMAMLElement()
 *                              to map attributes that have a mapRuleType set
 *                              in the xsd.
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser..
 * 22-Nov-02    Paul            VBM:2002112214 - Removed old maml code
 *                              generating methods and replaced them with new
 *                              MarlinElementHandler generating methods.
 * 22-Nov-02    Geoff           VBM:2002111504 - Add the getElementName method
 *                              to generated attributes, cleaned up imports.
 * 28-Nov-02    Mat             VBM:2002112212 - Added audio to existingElements
 * 11-Feb-03    Ian             VBM:2003020607 - Ported changes from Metis
 *                              for set/getJspProcessingType.
 * 25-Feb-03    Byron           VBM:2003022105 - Modified ignoredElements[] to
 *                              contain nativemarkup item (to temporarily
 *                              prevent compile-time errors)
 * 07-Mar-03    Byron           VBM:2003030527 - Modified processSchemaObject
 *                              method.
 * 21-Mar-03    Byron           VBM:2003031907 - Process dissecting pane element
 * 27-Mar-03    Steve           VBM:2003031907 - Removed commented out code
 * 28-Mar-03    Sumit           VBM:2003032714 - Element for menuitemgroup
 *                              should not be generated
 * 01-Apr-03    Phil W-S        VBM:2002111502 - Add in handling of the new
 *                              phone number element. This is a hand-written
 *                              element so must be included in the
 *                              existingElements array. Also, the
 *                              AbstractAnchorElement is now hand-written as
 *                              the generated code does not handle the new
 *                              inheritance tree.
 * 16-Apr-03    Geoff           VBM:2003041603 - Modified generation of
 *                              protocol method calls to handle
 *                              ProtocolException: modified
 *                              generatePAPIImplBlockElement and
 *                              generatePAPIImplAttrsElement and added
 *                              generateProtocolCallBlock.
 * 17-Apr-03    Chris W         VBM:2003041708 - Added span, template & include
 *                              to ignoredElements in initialiseExtraInfo.
 *                              ignoredElements are now removed from the list
 *                              of elements to be processed.
 * 17-Apr-03    Allan           VBM:2003041506 - Add span to the array of
 *                              existingElements so it is not auto-generated in
 *                              initialiseExtraInfo(). Remove span from
 *                              ignored list.
 * 22-Apr-03    Geoff           VBM:2003041603 - Improve commenting.
 * 23-Apr-03    Steve           VBM:2003041606 - Check complex type when
 *                              generating PAPI elements as the isMixed()
 *                              method needs to return true if the element
 *                              has mixed content. Removed the mixed type
 *                              processing from dummy JSP tag and MAML element
 *                              generation as it was never used.
 * 24-Apr-03    Chris W         VBM:2003030404 - Modified ignoredElements[] &
 *                              existingElements so maml element is generated
 *                              for nativemarkup but not papi. Modified
 *                              initialiseExtraInfo() to stop it generating
 *                              jsp tag stuff for nativemarkup element.
 *                              generateMarlinElementHandlerFactory does not
 *                              handle nativemarkup elements either.
 * 19-May-03    Chris W         VBM:2003051902 - generated jsp tags return
 *                              Writer for getContentWriter(), getDirectWriter()
 *                              writeHasMixedContentMethod writes a package
 *                              protected method instead of public.
 * 21-May-03    Chris W         VBM:2003040403 - generateJspTag() outputs jsp
 *                              tag classes in a way that supports jsp
 *                              character references.
 * 20-May-03    Byron           VBM:2003051903 - Added td to the exclude list
 *                              so that TableDataCellElement is not
 *                              auto-generated. Cleaned imports and some unused
 *                              variables.
 * 22-May-03    Byron           VBM:2003051903 - Fixed comments.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.JavaInfo;
import com.volantis.mcs.build.parser.AttributeGroupReference;
import com.volantis.mcs.build.parser.ComplexType;
import com.volantis.mcs.build.parser.ElementGroup;
import com.volantis.mcs.build.parser.ElementGroupReference;
import com.volantis.mcs.build.parser.ElementReference;
import com.volantis.mcs.build.parser.SchemaAttribute;
import com.volantis.mcs.build.parser.SchemaObject;
import com.volantis.mcs.build.parser.SchemaParser;
import com.volantis.mcs.build.parser.Scope;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class parses the mariner schema and generates code for PAPI attributes
 * some PAPI elements, and eventually will generate the tags as well.
 */
abstract public class ParseMarinerSchema {

    /**
     * The top level directory where the generated code should be written.
     */
    private File generatedDir;

    /**
     * The context package.
     */
    private final String contextPackage = "com.volantis.mcs.context";

    /**
     * The papi package.
     */
    private final String papiPackage = "com.volantis.mcs.papi";


    /**
     * The papi impl package.
     */
    private final String papiImplPackage = "com.volantis.mcs.papi.impl";

    /**
     * The marlin package.
     */
    private final String marlinPackage = "com.volantis.mcs.marlin";

    /**
     * The marlin sax package.
     */
    protected final String marlinSAXPackage = marlinPackage + ".sax";

    /**
     * An array of generated Marlin elements for the factory
     */
    protected List marlinElementList;

    /**
     * A list of elements that the MarlinElementHandlerFactory will not deal with
     */
    protected String[] excludeMarlinElementHandlerFactory;


    private PrintStream papiImplementationDataStream;

    /**
     * Create a new <code>ParseMarinerSchema</code>.
     */
    protected ParseMarinerSchema() {
    }

    /**
     * Parse the xsd file from the supplied stream and parse
     * it, generating the code.
     *
     * @param schema       the stream from which the schema should be read.
     * @param generatedDir the directory into which the generated code should
     *                     be put.
     */
    protected void parse(InputStream schema, File generatedDir) {

        this.generatedDir = generatedDir;

        // perform specific initialisation`

        try {
            initialise();

            SAXBuilder builder
                    = new SAXBuilder(
                            "com.volantis.xml.xerces.parsers.SAXParser",
                            false);
            Document document = builder.build(schema);

            SchemaParser parser = new SchemaParser(new Factory());
            parser.addProcessingInstructionTarget("deprecated",
                    new DeprecatedTarget());
            parser.addProcessingInstructionTarget("papi",
                    new PAPITarget());

            List schemaObjects = parser.parse(document);

            initialiseExtraInfo(schemaObjects, parser.getScope());
            schemaPreamble();
            for (Iterator i = schemaObjects.iterator(); i.hasNext();) {
                SchemaObject object = (SchemaObject) i.next();
                processSchemaObject(object);
            }
            schemaPostamble();
            writeTagLibraries();

            generateMarlinElementHandlerFactory(parser.getScope());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (JDOMException jdome) {
            jdome.printStackTrace();
            return;
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
     * Allow custom function pre processing of the schema
     */
    protected void schemaPreamble() {
    }

    /**
     * Allow custom function post processing of the schema
     */
    protected void schemaPostamble() {
    }

    protected abstract void initialise() throws IOException;

    /**
     * Add any extra information needed to generate the code.
     */
    private void initialiseExtraInfo(List schemaObjects, Scope scope) {

        ElementInfo elementInfo;
        String name;

        // A list all those elements which do not need to be generated because
        // they already exist.
        String[] existingElements = new String[]{
            "audio",
            "br",
            "canvas",
            "chart",
            "dissectingpane",
            "code",
            "dividehint",
            "formfragment",
            "fragment",
            "include",
            "label",
            "layout",
            "menu",
            "menuitem",
            "menuitemgroup",
            "message",
            "meta",
            "montage",
            "noscript",
            "nativemarkup",
            "otherwise",
            "pane",
            "phonenumber",
            "pre",
            "region",
            "segment",
            "select",
            "script",
            "span",
            "substituteformat",
            "td",
            "timer",
            "unit",
            "when",
            "xfaction",
            "xfboolean",
            "xfcontent",
            "xfform",
            "xfimplicit",
            "xfmuselect",
            "xfoption",
            "xfsiselect",
            "xftextinput",
            "xfupload",
            "xfoptgroup",
        };

        // A list of those elements for which we are NOT going to generate any
        // API elements or attributes
        String[] doNotGenerateAPI = {
            "usePipeline"
        };

        // A list of elements which the MarlinElementHandlerFactory will
        // not deal with. When asked to return a MarlinElementHandler for one of
        // these elements, it will return null.
        excludeMarlinElementHandlerFactory = new String[]{
            "nativemarkup",
            "include",
            "usePipeline"
        };

        // A list of elements which will not have MarlinElementHandlers
        String[] doNotGenerateMarlinElementHandler = {
            "include"
        };

        // A list of all those element which are ignored at the moment.
        String[] ignoredElements = new String[]{
            "template"
        };

        // A list of all the table cell elements.
        String[] tableCellElements = new String[]{
            "td",
            "th"
        };

        // A list of the prefixes to use for those elements whose prefix cannot
        // be generated from their natural name, or whose natural name is not set.
        String[] specialPrefixes = new String[]{
            "xfaction", "XFAction",
            "xfboolean", "XFBoolean",
            "xfcontent", "XFContent",
            "xfform", "XFForm",
            "xfimplicit", "XFImplicit",
            "xfmuselect", "XFMultipleSelect",
            "xfoptgroup", "XFOptionGroup",
            "xfoption", "XFOption",
            "xfsiselect", "XFSingleSelect",
            "xftextinput", "XFTextInput",
            "xfupload", "XFUpload",

            "usePipeline", "UsePipeline"
        };

        marlinElementList = new ArrayList();

        // Mark all those elements which do not need to be generated because the
        // element already exists.
        for (int i = 0; i < existingElements.length; i += 1) {
            name = existingElements[i];
            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setGenerateAPIElement(false);
            elementInfo.setGenerateMarlinElementHandler(true);
            elementInfo.setAPIElementExists(true);
        }

        // Mark all those element for which we are NOT going to generate a
        // marlin element handler
        for (int i = 0; i < doNotGenerateMarlinElementHandler.length; i++) {
            name = doNotGenerateMarlinElementHandler[i];
            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setGenerateMarlinElementHandler(false);
        }

        // Mark all those elements which are ignored at the moment.
        for (int i = 0; i < ignoredElements.length; i += 1) {
            name = ignoredElements[i];

            // Remove elements that should be ignored from the list of
            // elements processed.
            for (int j = 0; j < schemaObjects.size(); j++) {
                Object o = schemaObjects.get(j);
                if (o instanceof ElementInfo) {
                    ElementInfo element = (ElementInfo) o;
                    if (name.equals(element.getName())) {
                        schemaObjects.remove(j);
                    }
                }
            }

        }

        // The prefix used for some elements cannot be generated from the natural
        // name.
        for (int i = 0; i < specialPrefixes.length; i += 2) {
            name = specialPrefixes[i];
            String prefix = specialPrefixes[i + 1];

            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setPrefix(prefix);
        }

        // Some elements do not need API elements or attributes generated
        for (int i = 0; i < doNotGenerateAPI.length; i++) {
            name = doNotGenerateAPI[i];
            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setGenerateAPIAttributes(false);
            elementInfo.setGenerateAPIElement(false);
        }

        // Handle aliases.
        elementInfo = (ElementInfo) scope.getElementDefinition("a");
        elementInfo.setBaseAPIElementClass("AbstractAnchorElement");

        elementInfo = (ElementInfo) scope.getElementDefinition("va");
        elementInfo.setBaseAPIElementClass("AbstractAnchorElement");

        elementInfo = (ElementInfo) scope.getElementDefinition("img");
        elementInfo.setBaseAPIElementClass("AbstractImageElement");

        elementInfo = (ElementInfo) scope.getElementDefinition("logo");
        elementInfo.setBaseAPIElementClass("AbstractImageElement");

        // All the heading elements use the same attributes class internally.
        for (int h = 1; h <= 6; h += 1) {
            elementInfo = (ElementInfo) scope.getElementDefinition("h" + h);
            elementInfo.setProtocolAttributesClass("HeadingAttributes");

            // Make sure that the api element sets the tag name in the
            // protocol attributes.
            elementInfo.setMustSetTagName(true);
        }

        // All the table cell elements use the same attributes class internally.
        for (int i = 0; i < tableCellElements.length; i += 1) {
            name = tableCellElements[i];
            elementInfo = (ElementInfo) scope.getElementDefinition(name);
            elementInfo.setProtocolAttributesClass("TableCellAttributes");

            // Make sure that the api element sets the tag name in the
            // protocol attributes.
            elementInfo.setMustSetTagName(true);
        }
    }

    /**
     * Do the right thing depending on the type of the specified object.
     *
     * @param object The <code>SchemaObject</code> to process.
     */
    private void processSchemaObject(SchemaObject object)
            throws IOException {

        System.out.println();

        if (object instanceof AttributeGroupInfo) {
            processAttributeGroup((AttributeGroupInfo) object);
        } else if (object instanceof ElementInfo) {
            processElement((ElementInfo) object);
        } else if (object instanceof ElementGroup) {
            // do nothing
        } else if (object instanceof ElementGroupReference) {
            // do nothing
        } else if (object instanceof ElementReference) {
            // do nothing
        } else if (object instanceof ComplexType) {
            // do nothing
        } else if (object instanceof AttributeGroupReference) {
            processAttributeGroup((AttributeGroupInfo)
                    ((AttributeGroupReference) object).getDefinition());
        } else {
            throw new IllegalArgumentException("Unhandled object " + object);
        }
    }

    /**
     * Do any AttributeGroupInfo specific processing.
     *
     * @param info The <code>AttributeGroupInfo</code> to process.
     */
    abstract protected void processAttributeGroup(AttributeGroupInfo info)
            throws IOException;

    /**
     * Do any ElementInfo specific processing.
     *
     * @param info The <code>ElementInfo</code> to process.
     */
    abstract protected void processElement(ElementInfo info)
            throws IOException;

    /**
     * Open a file relative to the generated directory.
     *
     * @param qualifiedClassName The fully qualified class name.
     * @return A PrintStream.
     */
    protected PrintStream openFileForClass(String qualifiedClassName) {
        return GenerateUtilities.openFileForClass(generatedDir,
                qualifiedClassName);
    }


    /**
     * Generate a PAPI attribute class.
     *
     * @param info The <code>AttributesStructureInfo</code> which controls the
     *             contents of the generated APIAttributes class.
     */
    protected void generatePAPIAttributesClass(
            AttributesStructureInfo info,
            String naturalName, String elementName,
            String attributesClass)
            throws IOException {

        System.out.println("  Generating PAPIAttributes");

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
            baseClass = "AbstractPAPIAttributes";
        } else {
            baseClass = baseAttributesStructureInfo.getAPIAttributesClass();
        }

        String qualifiedClassName = papiPackage + "." + attributesClass;

        PrintStream out = openFileForClass(qualifiedClassName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + papiPackage + ";");

        // Write imports

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment(out, "",
                "The " + naturalName
                + " attributes.");

        // Get the collection of interfaces which this class must implement.
        Collection interfaces = info.getInterfaceNames();

        // Write the class header.
        boolean abstractClass = info.isAbstractAPIAttributesClass();
        String abstractKeyword = (abstractClass ? "abstract " : "");
        String finalKeyword = (abstractClass ? "" : "final ");

        out.println("public " + finalKeyword + abstractKeyword + "class "
                + attributesClass + " extends " + baseClass);

        if (!interfaces.isEmpty()) {
            String interfacePrefix;
            out.println();
            interfacePrefix = "  implements ";
            for (Iterator i = interfaces.iterator(); i.hasNext();) {
                String interfaceName = (String) i.next();
                out.print(interfacePrefix + interfaceName);
                interfacePrefix = ",\n             ";
            }
        }
        out.println(" {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Write out the attribute definitions.
        Collection attributes = info.getImplementedAttributes();
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

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
            }
        }

        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment(out, "  ",
                "Create a new <code>" + attributesClass + "</code>.");
        if (elementName != null) {
            out.println("  public " + attributesClass + " () {");
            out.println("    super(PAPIFactory.getDefaultInstance()." +
                    "getPAPIElementFactory(\"" +
                    elementName + "\"));");
        } else {
            out.println("  public " + attributesClass +
                    " (PAPIElementFactory papiElementFactory) {");
            out.println("    super(papiElementFactory);");
        }

        out.println("  }");

        // Write out the impl of the getElementName method.
        // This is useful for the Protocols to be able to figure out what styles
        // to apply for fallbacks.
        if (!abstractClass) {
            out.println();
            out.println("  // Inherit Javadoc.");
            out.println("  public String getElementName () {");
            out.println("    return \"" + elementName + "\";");
            out.println("  }");
        }

        // Write out the setters and the getters.
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String memberName = JavaInfo.getJavaVariable(attributeName);
            String xmlName = attributeInfo.getName();

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

            // Create attribute setter methods.
            out.println("  public void set" + titled
                    + " (String " + memberName + ") {");
            if (inherited) {
                out.println(
                        "    super.set" + titled + " (" + memberName + ");");
            } else {
                out.println("    papiAttributes.setAttributeValue(" +
                        "null, \"" + xmlName + "\", " + memberName + ");");
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
                out.println("    return papiAttributes.getAttributeValue(" +
                        "null, \"" + xmlName + "\");");
            }
            out.println("  }");
        }

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }

    /**
     * Generate a PAPI attribute interface.
     *
     * @param info The <code>AttributesStructureInfo</code> which controls the
     *             contents of the generated PAPIAttributes interface.
     */
    protected void generatePAPIAttributesInterface(
            AttributesStructureInfo info,
            String naturalName,
            String attributesInterface)
            throws IOException {

        System.out.println("  Generating interface");

        String qualifiedClassName = papiPackage + "." + attributesInterface;

        PrintStream out = openFileForClass(qualifiedClassName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + papiPackage + ";");

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "", "The " + naturalName + " attributes interface.");

        // Write the class header.
        out.println("public interface " + attributesInterface + " {");

        // Write the copyright statement.
        GenerateUtilities.writePublicCopyright(out);

        // Write out the setters and the getters.
        List attributes = info.getAttributes();
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String memberName = JavaInfo.getJavaVariable(attributeName);

            String titled = GenerateUtilities.getTitledString(attributeName);

            boolean deprecated = attributeInfo.isDeprecated();
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
                    + " (String " + memberName + ");");

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

            out.println("  public String get" + titled + " ();");
        }

        // Close the interface definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }

    /**
     * Generate a PAPI Facade element which implements PAPIElement.
     *
     * @param info The extra information which is needed to generate the
     *             element.
     */
    protected void generatePAPIFacadeElement(ElementInfo info)
            throws IOException {

        System.out.println(" Generating PAPI Facade element");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        if (!attributesStructureInfo.isAbstractAPIAttributesClass()) {
            String elementClassName = info.getAPIElementClass();

            String qualifiedClassName = papiPackage + "." + elementClassName;

            PrintStream out = openFileForClass(qualifiedClassName);

            // Write the header.
            GenerateUtilities.writeHeader(out, this.getClass().getName());

            // Write the package.
            out.println();
            out.println("package " + papiPackage + ";");

            // Write the imports.
            out.println();
            out.println("import " + contextPackage + ".MarinerRequestContext;");

            out.println("import java.io.Writer;");
            out.println();

            // Write the class comment.
            GenerateUtilities.writeJavaDocComment
                    (out, "", "The " + info.getNaturalName() + " element.\n" +
                    "@volantis-api-include-in PublicAPI\n" +
                    "@volantis-api-include-in ProfessionalServicesAPI\n" +
                    "@volantis-api-include-in InternalAPI");

            // Write the class header.
            out.println("public final class " + elementClassName);
            out.println(" extends AbstractPAPIDelegatingElement {");

            // Write the copyright statement.
            GenerateUtilities.writeCopyright(out);

            // Write the PAPI Meta implementation
            GenerateUtilities.writeJavaDocComment
                    (out, "  ",
                            "The factory that provides the implementation objects for this element.");

            out.println(
                    "  private static PAPIElementFactory  papiElementFactory = ");
            out.println("      PAPIFactory.getDefaultInstance().getPAPIElementFactory(\"" +
                    info.getName() + "\");");
            out.println();
            out.println("  public " + elementClassName + " () {");
            out.println("    super(papiElementFactory);");
            out.println("  }");

            out.println("}");

            /*  public Writer getContentWriter (MarinerRequestContext context,
                                   PAPIAttributes papiAttributes)
     throws PAPIException;*/
        }
    }

    protected void generatePAPIImplementationDataHeader() {
        System.out.println("  Generating PAPIImplementationData Header");


        String qualifiedClassName = papiImplPackage +
                ".PAPIImplementationData";

        papiImplementationDataStream = openFileForClass(qualifiedClassName);

        PrintStream out = papiImplementationDataStream;

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + papiImplPackage + ";");
        // Write the imports.
        out.println();
        out.println("import java.util.HashMap;");
        out.println("import java.util.Map;");
        out.println("import com.volantis.mcs.papi.*;");
        out.println();
        out.println("public final class PAPIImplementationData {");
        out.println();
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "The PAPIImplementationData Map.");
        out.println(
                "  public static final Map implementationData = new HashMap();");
        out.println();
        out.println("  static {");

    }

    protected void generatePAPIImplementationDataEntry(ElementInfo info) {
        System.out.println("  Generating PAPIImplementationData Entry");


        PrintStream out = papiImplementationDataStream;

        String elementClassName = info.getAPIElementClass() + "Impl";
        String attributesClassName = info.getAPIAttributesClass();

        out.println("    implementationData.put(");
        out.println("        \"" + info.getName() + "\",");
        out.println("        new PAPIImplementationInfo(");
        out.println("            \"" + info.getName() + "\",");
        out.println("           " + elementClassName + ".class,");
        out.println("           " + attributesClassName + ".class));");
    }

    protected void generatePAPIImplementationDataFooter() {
        System.out.println("  Generating PAPIImplementationData Footer");


        PrintStream out = papiImplementationDataStream;
        out.println();
        out.println("  }");
        out.println();
        out.println("  protected PAPIImplementationData () {}");
        out.println();
        out.println("  public static Map getImplentationInfoMap() {");
        out.println("    return implementationData;");
        out.println("  }");
        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();

    }

    /**
     * Generate a PAPI element which extends BlockElement.
     *
     * @param info The extra information which is needed to generate the
     *             element.
     */
    protected void generatePAPIImplBlockElement(ElementInfo info)
            throws IOException {

        System.out.println("  Generating block element");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String elementClassName = info.getAPIElementClass();
        String attributesClassName = info.getAPIAttributesClass();

        String qualifiedClassName = papiImplPackage + "." + elementClassName +
                "Impl";

        PrintStream out = openFileForClass(qualifiedClassName);

        // Get the suffix to use for protocol methods.
        String protocolMethodSuffix = info.getProtocolMethodSuffix();

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + papiImplPackage + ";");

        // Write the imports.
        out.println();
        out.println("import " + contextPackage + ".ContextInternals;");
        out.println("import " + contextPackage + ".MarinerPageContext;");
        out.println("import " + contextPackage + ".MarinerRequestContext;");
        out.println("import " + papiPackage + ".BlockAttributes;");
        out.println("import " + papiPackage + "." + attributesClassName + ";");
        out.println("import " + papiPackage + ".PAPIException;");
        out.println();
        out.println("import com.volantis.mcs.protocols.MCSAttributes;");
        out.println("import com.volantis.mcs.protocols.VolantisProtocol;");
        // These two required for generateProtocolCallBlock()
        out.println("import com.volantis.mcs.protocols.ProtocolException;");
        out.println("import com.volantis.synergetics.log.LogDispatcher;");
        out.println(
                "import com.volantis.mcs.localization.LocalizationFactory;");
        out.println("import com.volantis.synergetics.localization." +
                "ExceptionLocalizer;");

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "", "The " + info.getNaturalName() + " element.");

        // Write the class header.
        boolean abstractClass
                = attributesStructureInfo.isAbstractAPIAttributesClass();
        String abstractKeyword = (abstractClass ? "abstract " : "");
        String finalKeyword = (abstractClass ? "" : "final ");
        out.println("public " + finalKeyword + abstractKeyword + "class "
                + elementClassName + "Impl");
        out.println("  extends BlockElementImpl {");

        GenerateUtilities.writeLogger(out, papiImplPackage,
                elementClassName + "Impl");

        GenerateUtilities.writeExceptionLocalizer(out,
                papiImplPackage,
                elementClassName + "Impl");

        // Write the declaration of the protocol attributes.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "The attributes to pass to the protocol methods.");

        String protocolAttributesClassName = info.getProtocolAttributesClass();
        out.println(
                "  private " + protocolAttributesClassName + " pattributes;");

        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "Create a new <code>" + elementClassName +
                "Impl</code>.");

        out.println("  public " + elementClassName + "Impl () {");
        out.println("    pattributes = new " + protocolAttributesClassName
                + " ();");

        if (info.getMustSetTagName()) {
            out.println("    pattributes.setTagName (\"" + info.getName()
                    + "\");");
        }

        out.println("  }");

        // write out the getMCSAttributes method
        out.println();
        out.println("    // Javadoc inherited");
        out.println("    MCSAttributes getMCSAttributes() {");
        out.println("        return pattributes;");
        out.println("    }");
        out.println();

        // Write out the elementStartImpl method.
        out.println();
        out.println("  // Javadoc inherited from super class.");
        out.println("  protected"
                + " int elementStartImpl (MarinerRequestContext context,");
        out.println("                            "
                + "BlockAttributes blockAttributes)");
        out.println("    throws PAPIException {");
        out.println();
        out.println("    MarinerPageContext pageContext");
        out.println(
                "      = ContextInternals.getMarinerPageContext (context);");
        out.println();
        out.println("    " + attributesClassName + " attributes");
        out.println("      = (" + attributesClassName + ") blockAttributes;");
        out.println();
        out.println("    pattributes.setId (attributes.getId ());");
        out.println("    pattributes.setTitle (attributes.getTitle ());");

        // Initialise the event related attributes.
        out.println();
        out.println("    // Initialise the general event attributes");
        out.println("    PAPIInternals.initialiseGeneralEventAttributes ("
                + "pageContext,");
        out.println("                                                    "
                + "attributes,");
        out.println("                                                    "
                + "pattributes);");

        // Initialise the other attributes.
        Collection attributes
                = attributesStructureInfo.getImplementedAttributes();

        writeInitialiseProtocolAttributes(out, attributes,
                attributesStructureInfo);

        // Call the protocol methods.
        boolean empty = isElementEmpty(info);
        if (empty) {
            out.println();
            out.println("    return SKIP_ELEMENT_BODY;");
        } else {
            out.println();
            out.println("    VolantisProtocol protocol"
                    + " = pageContext.getProtocol ();");

            generateProtocolCallBlock(out, "writeOpen" + protocolMethodSuffix);

            out.println();
            out.println("    return PROCESS_ELEMENT_BODY;");
        }

        // Close the method.
        out.println("  }");

        // Write out the elementEndImpl method.
        out.println();
        out.println("  // Javadoc inherited from super class.");
        out.println("  protected"
                + " int elementEndImpl (MarinerRequestContext context,");
        out.println("                          "
                + "BlockAttributes blockAttributes)");
        out.println("    throws PAPIException {");
        out.println();
        out.println("    MarinerPageContext pageContext");
        out.println(
                "      = ContextInternals.getMarinerPageContext (context);");
        out.println();
        out.println("    VolantisProtocol protocol"
                + " = pageContext.getProtocol ();");
        out.println();


        if (empty) {
            generateProtocolCallBlock(out, "write" + protocolMethodSuffix);
        } else {
            generateProtocolCallBlock(out, "writeClose" + protocolMethodSuffix);
        }
        out.println();
        out.println("    return CONTINUE_PROCESSING;");
        out.println("  }");

        // Write out the elementReset method.
        out.println();
        out.println("  // Javadoc inherited from super class.");
        out.println("  public"
                + " void elementReset (MarinerRequestContext context) {");
        out.println();
        out.println("    pattributes.resetAttributes ();");

        if (info.getMustSetTagName()) {
            out.println("    pattributes.setTagName (\"" + info.getName()
                    + "\");");
        }

        out.println();
        out.println("    super.elementReset (context);");

        out.println("  }");

        writeHasMixedContentMethod(info, out);

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }

    private void generateProtocolCallBlock(
            PrintStream out,
            String methodName) {
        // @todo add ProtocolException to all Protocol methods and remove this
        // bogus hack.
        // Protocols may throw (only) ProtocolException in general, but we
        // haven't added it to all protocol methods yet, so we can't catch
        // it directly. Instead we catch Exception and use instanceof to
        // discriminate. So, we expect to get either a RuntimeException, or
        // a non-RuntimeException which must be ProtocolException. Anything
        // else is an error.
        // This is a trap when adding other exceptions to protocol methods. A
        // refactoring IDE will not report any changes necessary and then this
        // will fail at runtime. Dangerous!
        out.println(
                "    // @todo This dodgy code should be fixed. Do not copy!");
        out.println(
                "    // See ParseMarinerSchema.generateProtocolCallBlock()");
        out.println(
                "    // for an explanation of why we make the catch this way.");
        out.println("    try {");

        out.println("      protocol." + methodName + " (pattributes);");

        out.println("    } catch (Exception e) {");
        out.println("        if (e instanceof RuntimeException) {");
        out.println(
                "            // Just a normal RuntimeException, pass it up the stack.");
        out.println("            throw (RuntimeException) e;");
        out.println("        } else if (e instanceof ProtocolException) {");
        out.println(
                "            // The non-runtime exception we are expecting.");
        out.println(
                "            // Create a PAPI exception to wrap it and pass up the stack.");
        // notice that using a localised error message here assumes that the
        // generated code will be com.volantis.mcs.papi
        out.println(
                "            logger.error(\"rendering-error\", pattributes.getTagName(), e);");
        out.println(
                "            throw new PAPIException(exceptionLocalizer.format(");
        out.println("                        \"rendering-error\", ");
        out.println("                         pattributes.getTagName()),");
        out.println("                                    e);");
        out.println("        } else {");
        out.println(
                "            // Any other non-runtime exception is an error.");
        out.println("            logger.error(\"illegal-protocol\", e);");
        out.println(
                "            throw new IllegalStateException( \"Illegal Protocol Exception: \" + e.getClass().getName() + \" : \" + e.getMessage() );");
        out.println("        }");
        out.println("    }");

    }

    /**
     * Generate a PAPI element which extends AttrsElement.
     *
     * @param info The extra information which is needed to generate the
     *             element.
     */
    protected void generatePAPIImplAttrsElement(ElementInfo info)
            throws IOException {

        System.out.println("  Generating general element");

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        String elementClassName = info.getAPIElementClass();
        String attributesClassName = info.getAPIAttributesClass();

        String qualifiedClassName = papiImplPackage + "." + elementClassName +
                "Impl";

        PrintStream out = openFileForClass(qualifiedClassName);

        // Get the suffix to use for protocol methods.
        String protocolMethodSuffix = info.getProtocolMethodSuffix();

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + papiImplPackage + ";");

        // Write the imports.
        out.println();
        out.println("import " + contextPackage + ".ContextInternals;");
        //out.println ("import " + contextPackage + ".MarinerPageContext;");
        out.println("import " + contextPackage + ".MarinerRequestContext;");
        out.println("import " + papiPackage + ".PAPIAttributes;");
        out.println("import " + papiPackage + ".PAPIException;");
        out.println("import " + papiPackage + "." + attributesClassName + ";");
        out.println("import com.volantis.mcs.protocols.MCSAttributes;");
        out.println();
        out.println("import com.volantis.mcs.protocols.VolantisProtocol;");
        // These two required for generateProtocolCallBlock()
        out.println("import com.volantis.mcs.protocols.ProtocolException;");
        out.println("import com.volantis.synergetics.log.LogDispatcher;");
        out.println(
                "import com.volantis.mcs.localization.LocalizationFactory;");
        out.println("import com.volantis.synergetics.localization." +
                "ExceptionLocalizer;");

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "", "The " + info.getNaturalName() + " element.");

        // Write the class header.
        boolean abstractClass
                = attributesStructureInfo.isAbstractAPIAttributesClass();
        String abstractKeyword = (abstractClass ? "abstract " : "");
        String finalKeyword = (abstractClass ? "" : "final ");
        out.println("public " + finalKeyword + abstractKeyword + "class "
                + elementClassName + "Impl");
        out.println("  extends AttrsElementImpl {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        GenerateUtilities.writeLogger(out, papiImplPackage,
                elementClassName + "Impl");

        GenerateUtilities.writeExceptionLocalizer(out,
                papiImplPackage,
                elementClassName + "Impl");

        // Write the declaration of the protocol attributes.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "The attributes to pass to the protocol methods.");

        String protocolAttributesClassName = info.getProtocolAttributesClass();
        out.println(
                "  private " + protocolAttributesClassName + " pattributes;");

        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "Create a new <code>" + elementClassName +
                "Impl</code>.");

        out.println("  public " + elementClassName + "Impl () {");
        out.println("    pattributes = new " + protocolAttributesClassName
                + " ();");

        if (info.getMustSetTagName()) {
            out.println("    pattributes.setTagName (\"" + info.getName()
                    + "\");");
        }

        out.println("  }");

        // write out the getMCSAttributes method
        out.println();
        out.println("    // Javadoc inherited");
        out.println("    MCSAttributes getMCSAttributes() {");
        out.println("        return pattributes;");
        out.println("    }");
        out.println();

        // Initialise the other attributes.
        Collection attributes
                = attributesStructureInfo.getImplementedAttributes();
        if (attributes.size() != 0) {
            // Only write out the styleElementStart method if it would do more
            // than just delegate to the super classes styleElementStart method.
            out.println();
            out.println("  // Javadoc inherited from super class.");
            out.println(
                    "  public int styleElementStart (MarinerRequestContext context,");
            out.println(
                    "                                PAPIAttributes papiAttributes)");
            out.println("    throws PAPIException {");
            out.println();
            out.println("    " + attributesClassName + " attributes");
            out.println(
                    "      = (" + attributesClassName + ") papiAttributes;");
            out.println();

            writeInitialiseProtocolAttributes(out, attributes,
                    attributesStructureInfo);
            // Write the rest of the styleElementStart method.
            out.println();
            out.println("    return super.styleElementStart (context, (" +
                    attributesClassName + ") papiAttributes);");
            out.println("  }");
        }

        // Write out the writeOpenMarkup method.
        out.println();
        out.println("  // Javadoc inherited from super class.");
        out.println("  void writeOpenMarkup (VolantisProtocol protocol) "
                + "throws PAPIException {");

        generateProtocolCallBlock(out, "writeOpen" + protocolMethodSuffix);

        out.println("  }");


        // Write out the writeCloseMarkup method.
        out.println();
        out.println("  // Javadoc inherited from super class.");
        out.println("  void writeCloseMarkup (VolantisProtocol protocol) {");
        out.println("    protocol.writeClose" + protocolMethodSuffix
                + " (pattributes);");
        out.println("  }");

        // Write out the elementReset method.
        out.println();
        out.println("  public"
                + " void elementReset (MarinerRequestContext context) {");
        out.println();
        out.println("    pattributes.resetAttributes ();");

        if (info.getMustSetTagName()) {
            out.println("    pattributes.setTagName (\"" + info.getName()
                    + "\");");
        }

        out.println();
        out.println("    super.elementReset (context);");

        out.println("  }");

        writeHasMixedContentMethod(info, out);

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }

    /**
     * If the XSD says that this element does not have mixed
     * content, override the method from AbstractElement that says it does
     *
     * @param info element information from XSD
     * @param out  the output stream
     */
    private void writeHasMixedContentMethod(
            ElementInfo info,
            PrintStream out) {
        ComplexType complexType = info.getComplexType();
        boolean mixed = (complexType != null && complexType.isMixed());
        if (!mixed) {
            out.println();
            out.println("  // Javadoc inherited from super class.");
            out.println("  boolean hasMixedContent() {");
            out.println("    return false;");
            out.println("  }");
        }
    }

    /**
     * Generate an alias PAPI element.
     *
     * @param info The extra information which is needed to generate the
     *             element.
     */
    protected void generatePAPIImplAliasElement(ElementInfo info)
            throws IOException {

        System.out.println("  Generating alias element");

        String elementClassName = info.getAPIElementClass();
        String baseElementClassName = info.getBaseAPIElementClass();

        String qualifiedClassName = papiImplPackage + "." + elementClassName +
                "Impl";

        PrintStream out = openFileForClass(qualifiedClassName);

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + papiImplPackage + ";");

        // Write the class comment.
        GenerateUtilities.writeJavaDocComment
                (out, "", "The " + info.getNaturalName() + " element.");

        // Write the class header.
        out.println("public final class " + elementClassName + "Impl");
        out.println("  extends " + baseElementClassName + "Impl {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }

    private boolean isElementEmpty(ElementInfo info) {

        ComplexType complexType = info.getComplexType();

        // can only be non-empty if type is not mixed
        // always false if region tag
        boolean empty;
        boolean mixed;
        if (complexType == null) {
            empty = true;
            mixed = false;
        } else {
            mixed = complexType.isMixed();
            if ("region".equals(info.getName())) {
                empty = false;
            } else {
                empty = complexType.isEmpty() && !mixed;
            }
        }

        return empty;
    }


    /**
     * Write the Jsp Tag library files.
     */
    private void writeTagLibraries() {
        // Do nothing by default

    }

    /**
     * Write code which initialises the protocol attributes.
     */

    private void writeInitialiseProtocolAttributes(
            PrintStream out,
            Collection attributes,
            AttributesStructureInfo info) {

        if (attributes.size() == 0) {
            return;
        }

        // Get a new copy of the attributes.
        attributes = info.getImplementedAttributes();

        // Look to see whether we can handle any of the properties which are
        // inherited from other groups in a special way.
        boolean focusAttributes = false;
        Collection interfaceGroups = info.getInterfaceAttributeGroups();
        Collection interfaceAttributes;

        for (Iterator i = interfaceGroups.iterator(); i.hasNext();) {
            AttributeGroupInfo groupInfo = (AttributeGroupInfo) i.next();
            AttributesStructureInfo groupAttributesStructureInfo
                    = groupInfo.getAttributesStructureInfo();
            String groupName = groupInfo.getName();

            if (groupName.equals("focus")) {
                // The focus attributes can be initialised using a method in
                // PAPIInternals.
                focusAttributes = true;

                // Remove all the focus attributes.
                interfaceAttributes =
                        groupAttributesStructureInfo.getAttributes();
                attributes.removeAll(interfaceAttributes);
            } else {
                System.err.println("  WARNING: Unhandled group " + groupName);
            }
        }

        // Handle the focus attributes specially.
        if (focusAttributes) {
            out.println();
            out.println("    // Initialise the focus event attributes");
            out.println("    PAPIInternals.initialiseFocusEventAttributes ("
                    + "pageContext,");
            out.println("                                                  "
                    + "attributes,");
            out.println("                                                  "
                    + "pattributes);");
        }

        if (attributes.size() == 0) {
            return;
        }

        // Check to see what extra variables are needed when processing the
        // attributes.
        boolean needSeparator = true;
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String protocolName = attributeInfo.getProtocolName();
            if (protocolName == null) {
                // Ignore this attribute.
                continue;
            }

            String titled = GenerateUtilities.getTitledString(attributeName);
            String protocolTitled = GenerateUtilities.getTitledString(
                    protocolName);

            // NOTE: this code used to check for mariner expression support
            // passed though the attribute info from the PAPITarget/papi PI.
            // However, it was outdated by the change from mariner expressions
            // to volantis quoted/unquoted expressions in R821 and no
            // autogenerated elements used expressions other than expr= so they
            // had no need the expression type information. The code was thus
            // removed. The changes can be seen in the revision history for
            // R821 VBM:2005020707 if necessary.

            if (needSeparator) {
                out.println();
            }

            out.println("    pattributes.set" + protocolTitled
                    + " (attributes.get" + titled + " ());");
            needSeparator = false;
        }
    }

    /**
     * Generate a MarlinElementHandler.
     *
     * @param info The <code>ElementInfo</code> which controls the
     *             contents of the generated class.
     */
    protected void generateMarlinElementHandler(ElementInfo info)
            throws IOException {

        System.out.println("  Generating MarlinElementHandler");

        marlinElementList.add(info.getName());

        AttributesStructureInfo attributesStructureInfo
                = info.getAttributesStructureInfo();

        ComplexType complexType = info.getComplexType();

        // can only be non-empty if type is not mixed
        // always false if region tag
        boolean mixed = (complexType != null && complexType.isMixed());

        String className = info.getMarlinElementClass();
        className = info.getPrefix() + "ElementHandler";
        String papiElement = info.getAPIElementClass();
        String papiAttributes = info.getAPIAttributesClass();

        String packageName = marlinSAXPackage;

        String qualifiedClassName = packageName + "." + className;

        PrintStream out = openFileForClass(qualifiedClassName);

        Collection attributes = attributesStructureInfo.getAllAttributes();

        // Write the header.
        GenerateUtilities.writeHeader(out, this.getClass().getName());

        // Write the package.
        out.println();
        out.println("package " + packageName + ";");

        // Write the imports.
        SortedSet imports = new TreeSet();
        imports.add("org.xml.sax.Attributes");
        imports.add("org.xml.sax.SAXException");
        imports.add(papiImplPackage + "." + papiElement + "Impl");
        imports.add(papiPackage + "." + papiAttributes);
        imports.add(papiPackage + ".PAPIAttributes");
        imports.add(papiPackage + ".PAPIConstants");
        imports.add(papiPackage + ".PAPIElement");
        imports.add(papiPackage + ".PAPIElementFactory");
        imports.add(papiPackage + ".PAPIException");
        imports.add(papiPackage + ".PAPIFactory");
        //imports.add ("org.apache.log4j.Category");
        imports.add("com.volantis.synergetics.log.LogDispatcher");
        imports.add("com.volantis.mcs.localization.LocalizationFactory");
        if (info.getName().equals("canvas")) {
            imports.add(contextPackage + ".MarinerRequestContext");
            imports.add(contextPackage + ".MarinerContextException");
            imports.add("com.volantis.mcs.repository.RepositoryException");
            imports.add("java.io.IOException");
        }

        GenerateUtilities.writeImports(out, imports);

        // Write the class comment.
        String comment = info.getNaturalName();

        GenerateUtilities.writeJavaDocComment
                (out, "",
                        "The MarlinElementHandler for the " + comment +
                " element.");

        // Write the class header.
        out.println("public class " + className);
        out.println("  extends AbstractElementHandler {");

        // Write the copyright statement.
        GenerateUtilities.writeCopyright(out);

        // Write the logger creation statement.
        GenerateUtilities.writeLogger(out, packageName, className);
        // Write the PAPI Meta implementation
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "The factory that provides the implementation objects for this element.");

        out.println(
                "  private static PAPIElementFactory  papiElementFactory = ");
        out.println("      PAPIFactory.getDefaultInstance().getPAPIElementFactory(\"" +
                info.getName() + "\");");
        out.println();
        // Write out the constructor.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "Create a new <code>" + className + "</code>.");


        out.println("  public " + className + "() {");
        out.println("  }");

        // Write out the createElementSpecificAttributes method.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "Create a <code>" + papiAttributes + "</code>.");

        out.println("  public PAPIAttributes createPAPIAttributes"
                + " (PAPIContentHandlerContext context) {");
        out.println(
                "    return papiElementFactory.createElementSpecificAttributes();");
        out.println("  }");

        // Write out the createPAPIElement method.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "Create a <code>" + papiElement + "</code>.");

        out.println("  public PAPIElement createPAPIElement"
                + " (PAPIContentHandlerContext context) {");
        out.println("    return papiElementFactory.createPAPIElement();");
        out.println("  }");

        // Write out the initializePAPIAttributes method.
        GenerateUtilities.writeJavaDocComment
                (out, "  ",
                        "Initialize the <code>" + papiAttributes
                + "</code> from the SAX2 attributes.");

        out.println("  public void initializePAPIAttributes"
                + " (PAPIContentHandlerContext context,");
        out.println("                                      "
                + "  Attributes saxAttributes,");
        out.println("                                      "
                + "  PAPIAttributes papiAttributes)");
        out.println("    throws SAXException {");

        out.println();
        out.println("    " + papiAttributes + " attributes = ("
                + papiAttributes + ") papiAttributes;");

        // Set the attributes
        out.println();
        out.println("    String value;");
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String titled = GenerateUtilities.getTitledString(attributeName);

            String name = attributeInfo.getName();

            out.println();
            out.println("    value = saxAttributes.getValue (\"\", \"" + name +
                    "\");");

            out.println("    if (value != null) {");
            out.println("      attributes.set" + titled + " (value);");
            out.println("    }");
        }

        out.println();
        out.println("    if (logger.isDebugEnabled ()) {");
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            AttributeInfo attributeInfo = (AttributeInfo) i.next();

            String attributeName = attributeInfo.getAPIName();
            String titled = GenerateUtilities.getTitledString(attributeName);

            out.println("      logger.debug (\"set" + titled
                    + " (\" + attributes.get" + titled + " () + \")\");");
        }
        out.println("    }");
        out.println("  }");

        // If this is the canvas element then create beforePAPIElement and
        // afterPAPIElement methods.
        if (info.getName().equals("canvas")) {

            // Write out the beforePAPIElement method.
            GenerateUtilities.writeJavaDocComment
                    (out, "  ",
                            "Create a new MarinerRequestContext for nested canvasses.");

            out.println("  public void beforePAPIElement"
                    + " (PAPIContentHandlerContext context,");
            out.println("                               "
                    + "  PAPIAttributes papiAttributes)");
            out.println("    throws SAXException {");

            out.println();
            out.println("    " + papiAttributes + " attributes = ("
                    + papiAttributes + ") papiAttributes;");
            out.println();
            out.println("    String type = attributes.getType ();");
            out.println("    if (\"portlet\".equals (type)"
                    + " || \"inclusion\".equals (type)) {");
            out.println("      try {");
            out.println("        MarinerRequestContext oldContext");
            out.println("          = context.getRequestContext ();");
            out.println("        MarinerRequestContext newContext");
            out.println("          = oldContext.createNestedContext ();");
            out.println("        context.pushRequestContext (newContext);");
            out.println("      }");
            out.println("      catch (IOException e) {");
            out.println("        throw new SAXException (e);");
            out.println("      }");
            out.println("      catch (MarinerContextException e) {");
            out.println("        throw new SAXException (e);");
            out.println("      }");
            out.println("      catch (RepositoryException e) {");
            out.println("        throw new SAXException (e);");
            out.println("      }");
            out.println("    }");
            out.println("  }");

            // Write out the afterPAPIElement method.
            GenerateUtilities.writeJavaDocComment
                    (out, "  ",
                            "Release the MarinerRequestContext for nested canvasses.");

            out.println("  public void afterPAPIElement"
                    + " (PAPIContentHandlerContext context,");
            out.println("                              "
                    + "  PAPIAttributes papiAttributes)");
            out.println("    throws SAXException {");

            out.println();
            out.println("    " + papiAttributes + " attributes = ("
                    + papiAttributes + ") papiAttributes;");
            out.println();
            out.println("    String type = attributes.getType ();");
            out.println("    if (\"portlet\".equals (type)"
                    + " || \"inclusion\".equals (type)) {");
            out.println("      MarinerRequestContext oldContext");
            out.println("        = context.popRequestContext ();");
            out.println("      oldContext.release ();");
            out.println("    }");
            out.println("  }");
        }

        // Write out the hasMixedContentModel method.
        GenerateUtilities.writeJavaDocComment
                (out, "  ", "Return " + mixed + ".");

        out.println("  public boolean canContainCharacterData () {");
        out.println("    return " + mixed + ";");
        out.println("  }");

        // Close the class definition.
        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();
    }

    protected void generateMarlinElementHandlerFactory(Scope scope) {
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10777/1	pszul	VBM:2005120903 TagAttributeTranslatorFactory now allows registration of custom translators

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 24-Jun-05	8878/1	emma	VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 15-Apr-05	7712/1	matthew	VBM:2005041107 reformat this file to match that in MCS3.3

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/2	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 07-Feb-05	6833/1	ianw	VBM:2005020205 IBM fixes interim checkin

 14-Dec-04	6451/1	ianw	VBM:2004121001 Fixup build and move some relase stuff back to development

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/6	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 08-Oct-04	5740/3	matthew	VBM:2004100708 Allow code generators to accept a resource name as well as a filename

 08-Oct-04	5740/1	matthew	VBM:2004100708 allow Code generators to read from files or resources

 14-May-04	3272/6	philws	VBM:2004021117 Fix merge issues

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 13-May-04	4201/3	mat	VBM:2004050601 Made rework changes

 13-May-04	4201/1	mat	VBM:2004050601 Provide JSP migrator

 19-Feb-04	2789/6	tony	VBM:2004012601 rework changes

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 26-Oct-03	1560/5	mat	VBM:2003092604 Add JSP nativemarkup tag

 14-Oct-03	1553/3	mat	VBM:2003092604 Changed to always create the dummy tag library entry

 14-Oct-03	1553/1	mat	VBM:2003092604 Add JSP nativemarkup tag

 15-Aug-03	1111/1	chrisw	VBM:2003081306 Move fields in AbstractMarlinContentHandler to MarlinContentHandlerContext

 14-Aug-03	958/10	chrisw	VBM:2003070704 removed select, when and otherwise from ignored elements

 13-Aug-03	958/8	chrisw	VBM:2003070704 implemented expr attribute on papi elements

 12-Aug-03	1008/6	philws	VBM:2003080805 Provide implementation of the select, when and otherwise PAPI elements

 08-Aug-03	958/1	chrisw	VBM:2003070704 half way through changes to existing PAPI

 13-Aug-03	1042/3	steve	VBM:2003073103 Implement UNIT element

 07-Aug-03	992/1	geoff	VBM:2003080411 port from metis

 30-Jun-03	552/3	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 25-Jun-03	473/2	philws	VBM:2003061913 Renaming of pipeline:includeURI, renaming of vt:include to vt:usePipeline and intro of new vt:include

 20-Jun-03	419/1	adrian	VBM:2003061606 added Expression support to jsp attributes

 13-Jun-03	408/3	doug	VBM:2003053004 commit sumits changes

 06-Jun-03	208/3	byron	VBM:2003051903 Commit after conflict resolution

 ===========================================================================
*/
