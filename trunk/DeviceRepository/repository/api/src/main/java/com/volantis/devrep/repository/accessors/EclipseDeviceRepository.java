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

package com.volantis.devrep.repository.accessors;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.volantis.mcs.accessors.xml.ZipArchive;
import com.volantis.devrep.repository.api.accessors.xml.EclipseEntityResolver;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.io.IOUtils;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.xml.schema.W3CSchemata;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.devrep.device.api.xml.DeviceSchemas;
import com.volantis.devrep.repository.api.accessors.xml.DeviceRepositoryConstants;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;



import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.XMLReader;
import org.xml.sax.XMLFilter;

/**
 * This xml accessor has the ability to read the contents of the xml repository
 * (in this case, a zip file) and permit user actions on the contents of the
 * repository such as renaming, deleting and adding devices in the device
 * hierarchy.
 *
 * <p>
 * If any modifications are made then the {@link #saveRepositoryArchive} must
 * be called in order to save the modifications (changes are then immediately
 * updated in the repository).
 * <p>
 *
 * Note that this accessor permits the reading of repositories identified only
 * by a filename.
 */
public class EclipseDeviceRepository {

    /**
     * Volantis copyright mark.
     * */
    private static String mark
            = "(c) Volantis Systems Ltd 2004. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(EclipseDeviceRepository.class);

    /**
     * Used to obtain localized messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(
                    EclipseDeviceRepository.class);

    /**
     * xsi constant definiton
     */
    private static final String XSI = "xsi";

    /**
     * Schema location definition.
     */
    private static final String SCHEMA_LOCATION = "schemaLocation";


    /**
     * The standard element name filter.
     */
    public static final DeletionFilter STANDARD_ELEMENT_FILTER = new DeletionFilter(
            new DeletionFilter.NodeIdentifier [] {
                new DeletionFilter.
            NodeIdentifier(DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                    DeviceSchemas.DEVICE_CURRENT.getNamespaceURL()),
                new DeletionFilter.
            NodeIdentifier(DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                    DeviceSchemas.IDENTIFICATION_CURRENT.getNamespaceURL()),
                new DeletionFilter.
            NodeIdentifier(DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                    DeviceSchemas.TAC_IDENTIFICATION_CURRENT.getNamespaceURL())
            });

    /**
     * Permit validation to be on or off during parsing of the xml files
     * contained in the repository.
     */
    private boolean validation;

    /**
     * Ignore whitespace whilst parsing the xml input within the repository.
     */
    private boolean ignoreWhitespace;

    /**
     * The archive which stores the device repository.
     */
    private ZipArchive repositoryArchive;

    /**
     * Store the definitions.xml JDOM document.
     */
    private Document xmlDefinitionsDocument;

    /**
     * Store the hierarchy.xml JDOM document.
     * */
    private Document xmlHierarchyDocument = null;

    /**
     * Store the identification.xml JDOM document.
     */
    private Document xmlIdentificationDocument = null;

    /**
     * Store the tac-identification.xml JDOM document.
     */
    private Document xmlTACIdentificationDocument = null;

    /**
     * The Properties object which contains both standard and custom properties.
     */
    private Properties properties;

    /**
     * The full pathname within the device repository of the standard
     * properties file.
     */
    private String standardPropertiesPath;

    /**
     * The full pathname within the device repository of the custom
     * properties file.
     */
    private String customPropertiesPath;

    /**
     * The version of the device repository being accessed.
     */
    private String version;

    /**
     * The revision of the device repository being accessed.
     */
    private String revision;

    /**
     * Used when creating the documents that this accessor manages
     */
    private JDOMFactory factory;

    /**
     * The xml filter for this accessor.
     */
    private XMLFilter xmlFilter;

    /**
     * Create the XMLDeviceRepositoryAccessor instance with the specified
     * filename.
     * @param repositoryFilename the file name for the device repository.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automatic  s.
     * @param jdomFactory A {@link JDOMFactory} instance that will be used to
     * create the Documents that this accessor manages.
     * @throws IllegalArgumentException if the repositoryFilename or factory
     * arguments are null.
     */
    public EclipseDeviceRepository(String repositoryFilename,
                                       TransformerMetaFactory transformerMetaFactory,
                                       JDOMFactory jdomFactory,
                                       XMLFilter xmlFilter)
            throws RepositoryException {

        this(repositoryFilename, transformerMetaFactory, jdomFactory, true,
                true, xmlFilter);
    }

    /**
     * Create the XMLDeviceRepositoryAccessor instance with the specified
     * archive accessor.
     *
     * @param mdprAccessor the MDPRDeviceAccessor used to read the repository
     *                     file.
     * @param factory A {@link JDOMFactory} instance that will be used to
     * create the Documents that this accessor manages.
     * @throws IllegalArgumentException if the mdprAcessor or factory
     * arguments are null.
     */
    public EclipseDeviceRepository(MDPRArchiveAccessor mdprAccessor,
                                       JDOMFactory factory)
            throws RepositoryException {
        this(mdprAccessor, factory, true, true, null);
    }

    /**
     * Create the XMLDeviceRepositoryAccessor instance with the specified file
     * name and parsing criteria.
     *
     * @param repositoryFilename the file name for the device repository.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automatic upgrades.
     * @param jdomFactory A {@link JDOMFactory} instance that will be used to
     * create the Documents that this accessor manages.
     * @param validation         true if validation of the contained xml files
     *                           is required, false otherwise.
     * @param ignoreWhitespace   ignore whitespace when parsing the xml file.
     * @throws IllegalArgumentException if the repositoryFilename or factory
     * arguments are null.
     */
    public EclipseDeviceRepository(String repositoryFilename,
                                       TransformerMetaFactory transformerMetaFactory,
                                       JDOMFactory jdomFactory, boolean validation,
                                       boolean ignoreWhitespace,
                                       XMLFilter xmlFilter)
        throws RepositoryException {

        this(new MDPRArchiveAccessor(repositoryFilename,
                transformerMetaFactory),
                jdomFactory, validation, ignoreWhitespace, xmlFilter);
    }

    /**
     * Create the XMLDeviceRepositoryAccessor instance with the specified
     * archive accessor and parsing criteria.
     *
     * @param mdprAccessor the MDPRDeviceAccessor used to read the repository
     *                     file.
     * @param factory A {@link JDOMFactory} instance that will be used to
     * create the Documents that this accessor manages.
     * @param validation         true if validation of the contained xml files
     *                           is required, false otherwise.
     * @param ignoreWhitespace   ignore whitespace when parsing the xml file.
     * @param xmlFilter the XMLFilter used when writing out the repository
     * @throws IllegalArgumentException if the mdprAccessor or factory
     * arguments are null.
     */
    public EclipseDeviceRepository(MDPRArchiveAccessor mdprAccessor,
                                       JDOMFactory factory,
                                       boolean validation,
                                       boolean ignoreWhitespace,
                                       XMLFilter xmlFilter)
            throws RepositoryException {
        if (mdprAccessor == null) {
            throw new IllegalArgumentException(
                    "Archive accessor cannot be null.");
        }

        if (factory == null) {
            throw new IllegalArgumentException("factory cannot be null.");
        }

        repositoryArchive = mdprAccessor.getArchive();

        version = retrieveVersion(repositoryArchive);

        this.factory = factory;
        this.validation = validation;
        this.ignoreWhitespace = ignoreWhitespace;
        this.xmlFilter = xmlFilter;

        // Read in a JDOM document for the standard policy definitions from
        // the archive.
        InputStream input = repositoryArchive.
                getInputFrom(DeviceRepositoryConstants.STANDARD_DEFINITIONS_XML);
        if (input != null) {
            xmlDefinitionsDocument = createNewDocument(
                    new BufferedInputStream(input));

            // Check for a custom definitions xml file and merge it
            // with the standard definitions if it exists and is valid.
            input = repositoryArchive.getInputFrom(DeviceRepositoryConstants.CUSTOM_DEFINITIONS_XML);
            if (input != null) {
                Document customDefinitions =
                        createNewDocument(new BufferedInputStream(input));
                mergeDefinitionDocuments(xmlDefinitionsDocument, customDefinitions);
            }
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            DeviceRepositoryConstants.DEFINITIONS_XML));
        }

        // Read in a JDOM document for the hierarchy from the archive.
        input = repositoryArchive.getInputFrom(DeviceRepositoryConstants.HIERARCHY_XML);
        if (input != null) {
            xmlHierarchyDocument = createNewDocument(
                    new BufferedInputStream(input));
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            DeviceRepositoryConstants.HIERARCHY_XML));
        }

        // Read in a JDOM document for device identification from the archive.
        input = repositoryArchive.getInputFrom(DeviceRepositoryConstants.IDENTIFICATION_XML);
        if (input != null) {
            xmlIdentificationDocument = createNewDocument(
                    new BufferedInputStream(input));
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            DeviceRepositoryConstants.IDENTIFICATION_XML));
        }

        // Read in a JDOM document for device TAC identification from the
        // archive.
        input = repositoryArchive.getInputFrom(DeviceRepositoryConstants.TAC_IDENTIFICATION_XML);
        if (input != null) {
            xmlTACIdentificationDocument = createNewDocument(
                    new BufferedInputStream(input));
        } else {
            // No TAC file was found - this is not an error, as TAC data is
            // not available in all repositories. Ignore this and leave the
            // document null.
        }

        // Read in a JDOM document for repository from the archive.
        revision = retrieveRevision(repositoryArchive, factory);

        try {
            properties = createMergedProperties(repositoryArchive);
        } catch (IOException ioe) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            "policies.properties"),
                    ioe);
        }
    }

    /**
     * Retrieve the version of a device repository from the ZipArchive
     * representation of that repository.
     * @param repositoryArchive the repository
     * @param factory the JDOMFactory for creating JDOM objects
     * @return the revision of the provided repository
     * @throws RepositoryException
     */
    private static String retrieveRevision(ZipArchive repositoryArchive,
                                           JDOMFactory factory)
            throws RepositoryException {
        // Read in a JDOM document for repository from the archive.
        InputStream input = repositoryArchive.getInputFrom(DeviceRepositoryConstants.REPOSITORY_XML);
        return retrieveRevision(input, factory);
    }

    /**
     * Retrieve the repository revision from an InputStream.
     * @param is an InputStream attached to the repository revision file.
     * @param factory the JDOMFactory for creating JDOM objects
     * @return the version as read from the is stream
     * @throws RepositoryException if there was a problem read the version
     */
    private static String retrieveRevision(InputStream is,
                                           JDOMFactory factory)
            throws RepositoryException {
        String revision = null;
        if (is != null) {
            Document repositoryDocument = createNewDocument(
                    new BufferedInputStream(is), factory, true, true);
            revision = repositoryDocument.getRootElement().
                    getAttributeValue(DeviceRepositorySchemaConstants.
                    REVISION_ATTRIBUTE_NAME);
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            DeviceRepositoryConstants.REPOSITORY_XML));
        }

        return revision;
    }

    /**
     * Retrieve the version of a device repository from the ZipArchive
     * representation of that repository.
     * @param repositoryArchive the repository
     * @return the version of the repository
     * @throws RepositoryException if there is a problem accessing the version
     * information in the repository.
     */
    private static String retrieveVersion(ZipArchive repositoryArchive)
            throws RepositoryException {
        InputStream is = null;
        String version = null;
        try {
            // Check that the version.txt file is present and correct.
            is = repositoryArchive.getInputFrom(DeviceRepositoryConstants.VERSION_FILENAME);
            if (is == null) {
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "device-repository-file-missing",
                                DeviceRepositoryConstants.VERSION_FILENAME));
            }
            version = retrieveVersion(is);
            return version;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("unexpected-ioexception", e);
                    if (version != null) {
                        // A null version would indicate that another exception
                        // was thrown so only throw on a close() exception if this
                        // has not happened to avoid hiding the real problem.
                        throw new RepositoryException(
                                exceptionLocalizer.format(
                                        "unexpected-ioexception"),
                                e);
                    }
                }
            }
        }
    }

    /**
     * Retrieve the version from an InputStream.
     * @param is an InputStream attached to the repository version file.
     * @return the version as read from the input stream
     * @throws RepositoryException if there was a problem read the version
     */
    private static String retrieveVersion(InputStream is)
            throws RepositoryException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            IOUtils.copy(is, buffer);
        } catch (IOException e) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            DeviceRepositoryConstants.VERSION_FILENAME),
                    e);
        }
        return buffer.toString().trim();
    }

    /**
     * Set the name of the device repository being accessed by this accessor -
     * i.e. rename the file.
     * @param filename the absolute path of the filename for the repository.
     * @throws IllegalArgumentException if the provided filename would
     * produce an unwriteable repository.
     */
    public void setRepositoryName(String filename) {
        repositoryArchive.setArchiveFilename(filename);
    }

    /**
     * Provide access to the device hierarchy document.
     * @return The JDOM Document that represents device hierearchy.
     */
    public Document getDeviceHierarchyDocument() {
        return xmlHierarchyDocument;
    }

    /**
     * Provide access to the device identification document.
     * @return The JDOM Document for the device identification markup.
     */
    public Document getDeviceIdentificationDocument() {
        return xmlIdentificationDocument;
    }

    /**
     * Provide access to the device TAC identification document.
     * @return The JDOM Document for the device TAC identification markup, or
     *         null if no TAC identification document exists.
     */
    public Document getDeviceTACIdentificationDocument() {
        return xmlTACIdentificationDocument;
    }

    /**
     * Provide access to the device policy definitions document.
     * @return The JDOM Document that represents device policy definitions.
     */
    public Document getDevicePolicyDefinitions() {
        return xmlDefinitionsDocument;
    }

    /**
     * Write a file to a directory in a zip archive.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param archive the archive to write to.
     * @param directory the directory in the archive to write to.
     * @param filename the base name of the file.
     * @param document the JDOM document containing file content.
     * @throws RepositoryException
     */
    private void writeFile(ZipArchive archive, String directory,
                           String filename, Document document)
            throws RepositoryException {

        String path = getXMLFilePath(directory, filename);
        OutputStream output = archive.getOutputTo(path);
        try {
            writeDocument(document, output);
        } catch (IOException e) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-update-failure",
                            path),
                    e);
        }
    }

    /**
     * Moves the specified device so that it has a new parent in the device
     * hierarchy. Note the new parent should not be a child of the device
     * that is being moved.
     * @param device the name of the device to move. Cannot be null.
     * @param newParentDevice the name of the device that is to be the new
     * parent for the device to be moved
     * @throws RepositoryException if the device or newParentDevice does not
     * exist
     * @throws IllegalArgumentException if the device or newParentDevice
     * arguments are null.
     * @throws org.jdom.IllegalAddException if the new parent device is a
     * descendent of the device being moved.
     */
    public void moveDevice(String device, String newParentDevice)
            throws RepositoryException {
        // ensure the arguments are not null
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }
        if (newParentDevice == null) {
            throw new IllegalArgumentException(
                    "newParentDevice cannot be null");
        }
        // check that both devices actually exist
        Element deviceElement = getHierarchyDeviceElement(device);
        if (deviceElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-definition-missing",
                            device));
        }
        Element parentElement = getHierarchyDeviceElement(newParentDevice);
        if (parentElement == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-definition-missing",
                            newParentDevice));
        }

        Element oldParent = deviceElement.getParent();
        try {
            // in order to perform a move we want to detatch the device that is
            // to be moved from the it's parent and add it as a child to the
            // new parent device
            deviceElement.detach();
            // not this will throw an IllegalAddException if the parentElement is a
            // descendent of the deviceElement
            parentElement.addContent(deviceElement);
        } finally {
            if (deviceElement.getParent() == null) {
                // the move failed re attach the old parent
                oldParent.addContent(deviceElement);
            }
        }
    }

    /**
     * Take a directory and filename and return a full filepath to an xml
     * file, such as path/to/my/device-file.xml
     * <p> If the directory path is not suffixed by a path separator then one
     * is added.
     * <p> If the file name is not suffixed by a ".xml" extension then it is
     * appended.
     * @param directory The directory file path
     * @param filename The file name
     * @return A full file path to an xml file.
     */
    private String getXMLFilePath(String directory, String filename) {
        StringBuffer result = new StringBuffer(directory);
        if (!(directory.endsWith("/") || directory.endsWith("\\"))) {
            result.append(File.separator);
        }
        result.append(filename);

        if (!(filename.endsWith(DeviceRepositoryConstants.XML_SUFFIX))) {
            result.append(DeviceRepositoryConstants.XML_SUFFIX);
        }
        return result.toString();
    }

    /**
     * Get the version of the device repository associated with this
     * accessor.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Get the revision of the device repository associated with this
     * accessor.
     */
    public String getRevision() {
        return revision;
    }

    /**
     * Retrieve the identification XML for a device in the form of a JDOM
     * Element.
     * @param deviceName The name of the device whose identification XML to
     * retrieve.
     * @return The Element representing the device identification for the
     * specified device or null if there was no device element
     */
    public Element retrieveDeviceIdentificationElement(String deviceName) {
        return retrieveDeviceElementFromDocument(
                deviceName, xmlIdentificationDocument);
    }

    /**
     * Retrieve the TAC identification XML for a device in the form of a JDOM
     * Element.
     * @param deviceName The name of the device whose identification XML to
     * retrieve.
     * @return The Element representing the device TAC identification for the
     * specified device or null if there was no device element
     */
    public Element retrieveTACDeviceElement(String deviceName) {
        return retrieveDeviceElementFromDocument(
                deviceName, xmlTACIdentificationDocument);
    }

    /**
     * Retrieve the Device Element XML for a device in the form of a JDOM
     * Element from the specified Document.
     * <p> Note that this method requires that the "device" elements are
     * children of the root element and that the device name is represented as
     * by an attribute on element.  The attribute name is defined as
     * {@link XMLAccessorConstants#DEVICE_NAME_ATTRIBUTE}
     *
     * @param deviceName The name of the device whose identification XML to
     * retrieve.
     * @param document The document from which to retrieve the Device element.
     * @return The Element representing the device xml for the specified device
     * or null if there was no device element
     */
    private Element retrieveDeviceElementFromDocument(String deviceName,
                                                      Document document) {
        if (deviceName == null) {
            throw new IllegalArgumentException("deviceName cannot be null");
        }
        Element idElement = null;
        if (document != null) {
            List children = document.getRootElement().getChildren();

            // Using an Iterator because JDOM suggests this is faster than
            // children.size().
            Iterator iterator = children.iterator();
            while (iterator.hasNext() && idElement == null) {
                Element child = (Element) iterator.next();
                String childDeviceName =
                        child.getAttributeValue("name");
                if (childDeviceName.equals(deviceName)) {
                    idElement = child;
                }
            }
        }
        return idElement;
    }


    /**
     * Get the prefix for customer created device names.
     * @return the prefix for a device name that designates that device as
     * a customer created device.
     */
    public static String getCustomDeviceNamePrefix() {
        return DeviceRepositoryConstants.CUSTOM_DEVICE_NAME_PREFIX;
    }

    /**
     * Get the prefix for customer created device names.
     * @return the prefix for a device name that designates that device as
     * a customer created device.
     */
    public static String getCustomPolicyNamePrefix() {
        return DeviceRepositoryConstants.CUSTOM_POLICY_NAME_PREFIX;
    }

    /**
     * Determine if a device is a standard device (i.e. defined by Volantis)
     * or not.
     * @param deviceName the name of the device to test
     * @return true if the named device is a standard device; otherwise false
     * (note that a return value of false does not indicate that the device
     * exists).
     */
    public static boolean isStandardDevice(String deviceName) {
        return !deviceName.startsWith(getCustomDeviceNamePrefix());
    }

    /**
     * Retrieve a Device as a JDOM Element. This method will merge custom
     * attributes with standard attributes in the returned Element.
     * @param deviceName The name of the device to retrieve.
     * @return The Element definition of the specified device.
     * @throws RepositoryException If the specified device could not be found
     * or if there was a problem accessing the repository.
     */
    public Element retrieveDeviceElement(String deviceName)
            throws RepositoryException {
        Document deviceDocument = null;
        Element hierarchyDeviceElement = getHierarchyDeviceElement(deviceName);
        if (hierarchyDeviceElement != null) {

            // Read the standard device policies.
            String src = getXMLFilePath(DeviceRepositoryConstants.STANDARD_DEVICE_DIRECTORY, deviceName);
            InputStream standardInput = repositoryArchive.getInputFrom(src);

            if (standardInput != null) {
                deviceDocument = createNewDocument(standardInput);

                // Read any custom device policies.
                src = getXMLFilePath(DeviceRepositoryConstants.CUSTOM_DEVICE_DIRECTORY, deviceName);
                InputStream customInput = repositoryArchive.getInputFrom(src);
                if (customInput != null) {
                    Document customDocument = createNewDocument(customInput);

                    // We need to merge the standard and custom documents
                    // such that all the children of one of the root elements
                    // are added as children to the root of the other. In
                    // this case we will add the children of the custom root
                    // element to the children of the standard root element.
                    mergeDeviceDocuments(deviceDocument, customDocument);
                }

            } else {
                // No standard device. This is bad since this is mandatory.
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "device-definition-missing",
                                deviceName));
            }

            // The device name is required as an attribute on the device
            // root element. The XSD currently specifies the device name
            // as an optional attribute. However, in some circumstances
            // the GUI needs to know what device the device document refers
            // to and sometimes it is not possible to know this name
            // using any available contextual information. So, if there
            // is not already a device name on the device root element then
            // we add this here.
            Element device = deviceDocument.getRootElement();
            String deviceNameAttr =
                    device.getAttributeValue(DeviceRepositorySchemaConstants.
                    DEVICE_NAME_ATTRIBUTE);
            if (deviceNameAttr == null || deviceNameAttr.length() == 0) {
                device.setAttribute(DeviceRepositorySchemaConstants.
                        DEVICE_NAME_ATTRIBUTE,
                        deviceName);
            }
        }

        return deviceDocument.getRootElement();
    }

    /**
     * Merge the standard and custom documents for the definitions repository
     * files.
     *
     * The standard definitions document and the custom definitions document
     * both share the "types" element.  That is to say the information is
     * repeated in both documents.  As such we ignore any "types" elements
     * when merging the custom document contents into the standard document.
     *
     * @param standard The document containing the standard policy definitions
     * @param custom The document containing the custom policy definitions
     */
    private void mergeDefinitionDocuments(Document standard, Document custom) {
        Element sDefs = standard.getRootElement();
        Element cDefs = custom.getRootElement();

        List cChildren = cDefs.getChildren();
        while (cChildren.size() > 0) {
            Element child = (Element) cChildren.get(0);
            child.detach();
            if (!(DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_TYPES_ELEMENT_NAME.
                    equals(child.getName()))) {
                sDefs.addContent(child);
            }
        }
    }

    /**
     * Merge the standard and custom documents for a Device repository file.
     * The policy elements are removed from the custom document and added to
     * the standard.
     * @param standard The document containing the standard device policies
     * @param custom The document containing the custom device policies
     */
    private void mergeDeviceDocuments(Document standard, Document custom) {
        Element sDevice = standard.getRootElement();
        Element sPolicies =
                sDevice.getChild(DeviceRepositorySchemaConstants.
                POLICIES_ELEMENT_NAME, sDevice.getNamespace());

        Element cDevice = custom.getRootElement();
        Element cPolicies =
                cDevice.getChild(DeviceRepositorySchemaConstants.
                POLICIES_ELEMENT_NAME, cDevice.getNamespace());

        List cChildren = cPolicies.getChildren();
        while (cChildren.size() > 0) {
            Element cPolicy = (Element) cChildren.get(0);
            cPolicy.detach();
            sPolicies.addContent(cPolicy);
        }
    }


    /**
     * Remove the device from the device hierarchy. If the device to be removed
     * cannot be found, throw and exception. If the removal was successful then
     * return the removed jdom element.
     * <p>
     * This method will recurse through the children of the device removing
     * each from the hierarchy from the leaf nodes upwards.  The device
     * files, hierarchy references and identifiers will all be removed.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param deviceName the device name to remove from the hierarchy.
     * @throws RepositoryException if the element to be removed could not be
     *                             found.
     */
    public void removeDevice(String deviceName)
            throws RepositoryException {
        if (deviceName == null) {
            throw new IllegalArgumentException("deviceName cannot be null");
        }
        Element element = getHierarchyDeviceElement(deviceName);
        if (element != null) {
            removeDevice(element);
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "cannot-delete-device",
                            deviceName));
        }
    }

    /**
     * Remove the device Element from the device hierarchy.
     * <p>
     * This method will recurse through the children of the device removing
     * each from the hierarchy from the leaf nodes upwards.  The device
     * files, hierarchy references and identifiers will all be removed.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param element the device Element to remove from the hierarchy.
     * @throws RepositoryException if the element to be removed could not be
     *                             found.
     */
    private void removeDevice(Element element) throws RepositoryException {
        List children = new ArrayList(element.getChildren());
        for (int i = 0; i < children.size(); i++) {
            Element child = (Element) children.get(i);
            removeDevice(child);
        }

        String deviceName = element.getAttributeValue(
                DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);

        // copy the repository archive in case anything goes wrong.
        ZipArchive archive = new ZipArchive(repositoryArchive);

        removeFile(archive, DeviceRepositoryConstants.STANDARD_DEVICE_DIRECTORY, true, deviceName);
        removeFile(archive, DeviceRepositoryConstants.CUSTOM_DEVICE_DIRECTORY, false, deviceName);
        element.detach();

        removeDeviceIdentifiers(deviceName);
        removeDeviceTACs(deviceName);

        repositoryArchive = archive;
    }

    /**
     * Remove the device element in the identifiers document that contains the
     * device indentifiers for the named device.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param deviceName The name of the device to remove from the identifiers
     */
    private void removeDeviceIdentifiers(String deviceName) {
        Element identifiers = retrieveDeviceIdentificationElement(deviceName);
        if (identifiers != null) {
            identifiers.detach();
        }
    }

    /**
     * Remove the device element in the tac-identification document that
     * contains the TAC information for the named device.
     *
     * <p>Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.</p>
     *
     * @param deviceName The name of the device to remove from the TAC
     *                   identification
     */
    private void removeDeviceTACs(String deviceName) {
        Element tacs = retrieveTACDeviceElement(deviceName);
        if (tacs != null) {
            tacs.detach();
        }
    }

    /**
     * Rename the device element in the identifiers document that contains the
     * device indentifiers for the named device.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param oldName The current name of the device.
     * @param newName The new name of the device.
     */
    private void renameDeviceIdentifiers(String oldName, String newName) {
        Element identifiers = retrieveDeviceIdentificationElement(oldName);
        if (identifiers != null) {
            identifiers.setAttribute(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE,
                    newName);
        }
    }

    /**
     * Rename the device element in the tac-identification document that
     * contains the TAC information for the named device.
     *
     * <p>Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.</p>
     *
     * @param oldName The current name of the device.
     * @param newName The new name of the device.
     */
    private void renameDeviceTACs(String oldName, String newName) {
        Element tacs = retrieveTACDeviceElement(oldName);
        if (tacs != null) {
            tacs.setAttribute(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE,
                    newName);
        }
    }

    /**
     * Remove a file from a directory in a zip archive.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * <p>
     * This method does not guarantee the integrity of the archive.  Clients
     * are responsible for recovering errors should they arise during execution
     * of this method.
     *
     * @param archive the archive to remove the file from.
     * @param directory the directory in the archive to remove from.
     * @param mandatory if the file must exist.
     * @param filename the base name of the file.
     * @throws RepositoryException
     */
    private void removeFile(ZipArchive archive, String directory,
                            boolean mandatory, String filename)
            throws RepositoryException {

        String path = getXMLFilePath(directory, filename);
        boolean deleted = archive.delete(path);
        if (mandatory && !deleted) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "cannot-delete-file",
                            path));
        }
    }

    /**
     * Add a new device file to the device repository archive
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param deviceName The name of the device to add.
     */
    public void addDeviceElement(String deviceName)
            throws RepositoryException {
        if (deviceName == null || deviceName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "deviceName cannot be null or empty");

        }
        Map map = new HashMap();
        // add an empty device document to the devices map
        map.put(deviceName, createEmptyDeviceDocument().getRootElement());
        writeDeviceElements(map);
    }

    /**
     * Adds a device element to the identifiers document for the named
     * device. Note - no user agent or header patterns will be added to the
     * created device element.
     * @param deviceName The name of the device that requires an entry in
     * the identification document
     * @throws RepositoryException if the device already has identifiers
     * defined in the device repository.
     */
    public void addIdentifiersDeviceElement(String deviceName)
            throws RepositoryException {
        addDeviceElementToDocumentRoot(deviceName, xmlIdentificationDocument);
    }

    /**
     * Adds a device element to the TAC document for the named device. Note -
     * no user agent or header patterns will be added to the created device
     * element.
     * @param deviceName The name of the device that requires an entry in
     * the TAC document
     * @throws RepositoryException if the device already has TACs defined in
     * the device repository.
     */
    public void addTACDeviceElement(String deviceName)
            throws RepositoryException {
        addDeviceElementToDocumentRoot(deviceName, xmlTACIdentificationDocument);
    }

    /**
     * Adds a device element to the specified document for the named
     * device.
     * @param deviceName The name of the device that requires an entry in
     * the  document
     * @throws RepositoryException if the device already has an element defined
     * in the document.
     */
    private void addDeviceElementToDocumentRoot(String deviceName,
                                                Document document)
            throws RepositoryException {
        if (deviceName == null || deviceName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "deviceName cannot be null or empty");

        }
        Element existing =
                retrieveDeviceElementFromDocument(deviceName, document);
        if (existing != null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-already-exists",
                            deviceName));
        }

        Element root = document.getRootElement();
        root.addContent(createDeviceElement(deviceName, root.getNamespace()));
    }

    /**
     * Rename the device to the new name. This will only succeed if the device
     * name exists and the new device name to use does not already exist in the
     * hierarchy.
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param deviceName    the device name to be renamed.
     * @param newDeviceName the new device name.
     * @throws IllegalArgumentException if deviceName or newDeviceName is null.
     */
    public void renameDevice(String deviceName, String newDeviceName)
            throws RepositoryException {

        if (deviceName == null) {
            throw new IllegalArgumentException(
                    "Cannot rename a null device name.");
        }
        if (newDeviceName == null) {
            throw new IllegalArgumentException(
                    "Cannot rename a device name to null name.");
        }
        if (deviceExists(newDeviceName)) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-already-exists-cannot-rename",
                            deviceName));
        }
        Element element = getHierarchyDeviceElement(deviceName);
        if (element == null) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-not-found-cannot-rename",
                            deviceName));
        }

        // copy the archive in case anything goes wrong
        ZipArchive archive = new ZipArchive(repositoryArchive);

        Element parent = element.getParent();

        // Detach the element to remove it from any event-driven view.
        element.detach();

        try {
            // Rename the standard file.
            renameDeviceFile(archive,
                    DeviceRepositoryConstants.STANDARD_DEVICE_DIRECTORY, true,
                    deviceName, newDeviceName);
            renameDeviceFile(archive,
                    DeviceRepositoryConstants.CUSTOM_DEVICE_DIRECTORY, false,
                    deviceName, newDeviceName);

            renameDeviceIdentifiers(deviceName, newDeviceName);
            renameDeviceTACs(deviceName, newDeviceName);

            element.setAttribute(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE,
                    newDeviceName);

            repositoryArchive = archive;
        } finally {
            // Re-attach the element (either successfully renamed or as the
            // original value if something went horribly wrong...
            parent.addContent(element);
        }
    }

    /**
     * Rename a device file in a directory in a zip archive.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * <p>
     * This method does not guarantee the integrity of the archive.  Clients
     * are responsible for recovering errors should they arise during execution
     * of this method.
     *
     * @param archive the archive file to rename in.
     * @param deviceDirectory the directory in the archive to rename in.
     * @param mandatory if the device file must exist.
     * @param deviceName the base name of the device file.
     * @param newDeviceName the base name of the new device file.
     * @throws RepositoryException If the specified device could not be found
     * or if there was a problem renaming the device.
     */
    private void renameDeviceFile(ZipArchive archive, String deviceDirectory,
                                  boolean mandatory, String deviceName,
                                  String newDeviceName)
            throws RepositoryException {

        String devicePath = getXMLFilePath(deviceDirectory, deviceName);
        if (mandatory || archive.exists(devicePath)) {
            String newDevicePath =
                    getXMLFilePath(deviceDirectory, newDeviceName);
            if (!archive.rename(devicePath, newDevicePath)) {
                // Should only happen if the device files are out of date
                // wrt the hierarchy, which should not be often.
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "device-rename-failed",
                                new Object[]{devicePath,
                                             newDevicePath}));
            }
            // Then update it's contents to change the name internally.
            Document deviceDoc = createNewDocument(
                    archive.getInputFrom(newDevicePath));
            Element deviceElement = deviceDoc.getRootElement();
            deviceElement.setAttribute(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE,
                    newDeviceName);
            try {
                writeDocument(deviceDoc, archive.getOutputTo(newDevicePath));
            } catch (IOException e) {
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "device-update-failure",
                                newDevicePath),
                        e);
            }
        }
    }

    //
    // Hierarchy methods.
    // These operate only on the hierarchy.
    //

    /**
     * Get the child device names for the specified device name.
     *
     * @param deviceName the device name to get the children device name(s).
     * @return the collection of child device names or null if no child names
     *         could be found.
     */
    public List getChildDeviceNames(String deviceName) {

        List list = null;
        Element element = getHierarchyDeviceElement(deviceName);
        if (element != null) {
            List children = element.getChildren();
            if (children != null && children.size() > 0) {
                list = new ArrayList();
                for (int i = 0; i < children.size(); i++) {
                    Element child = (Element) children.get(i);
                    if (DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME.
                            equals(child.getName())) {
                        list.add(child.getAttributeValue(
                                DeviceRepositorySchemaConstants.
                                DEVICE_NAME_ATTRIBUTE));
                    }
                }
            }
        }
        return list;
    }

    /**
     * Return the root device's name.
     *
     * @return the root device's name, e.g. 'Master'.
     */
    public String getRootDeviceName()
            throws RepositoryException {

        String result = null;
        if (xmlHierarchyDocument != null) {
            // Should always be the 'hierarchy' element.
            Element root = xmlHierarchyDocument.getRootElement();
            List list = root.getChildren();
            if (list != null) {
                if (list.size() == 1) {
                    result = ((Element) list.get(0)).getAttributeValue(
                            DeviceRepositorySchemaConstants.
                            DEVICE_NAME_ATTRIBUTE);
                } else if (list.size() > 1) {
                    throw new RepositoryException(
                            exceptionLocalizer.format(
                                    "device-hierarchy-too-many-roots",
                                    list));
                }
            }
        }
        return result;
    }

    /**
     * Return the fallback device name for the specified device. If none is
     * found return null.
     *
     * @param deviceName
     * @return the fallback device name for the specified device. If none is
     *         found return null.
     */
    public String getFallbackDeviceName(String deviceName) {

        String result = null;
        Element element = getHierarchyDeviceElement(deviceName);
        if ((element != null) && !element.isRootElement()) {
            result = element.getParent().getAttributeValue(
                    DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
        }
        return result;
    }

    /**
     * Return true if the device name exists in the repository, false
     * otherwise.
     *
     * @param deviceName the device name to use for the search.
     * @return true if the device name exists in the repository, false
     *         otherwise.
     */
    public boolean deviceExists(String deviceName) {

        return getHierarchyDeviceElement(deviceName) != null;
    }

    //
    // Methods for serialising.
    //

    /**
     * Save the current device repository archive to the file system.
     * @throws RepositoryException if there was a problem writing the device
     * repository to the file system.
     */
    public void saveRepositoryArchive() throws RepositoryException {
        try {
            repositoryArchive.save();
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Write the device identifiers to the device repository archive.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @throws RepositoryException if there was a problem writing the device
     * repository to the file system.
     */
    public void writeIdentifiers() throws RepositoryException {
        try {
            // If we have an identification document, then output it.
            if (xmlIdentificationDocument != null) {
                // copy the archive in case anything goes wrong
                ZipArchive archive = new ZipArchive(repositoryArchive);

                OutputStream out = archive.getOutputTo(DeviceRepositoryConstants.IDENTIFICATION_XML);
                writeDocument(xmlIdentificationDocument, out);

                repositoryArchive = archive;
            }
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Write the device TAC identifiers to the device repository archive.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @throws RepositoryException if there was a problem writing the device
     * repository to the file system.
     */
    public void writeTACs() throws RepositoryException {
        try {
            // If we have an identification document, then output it.
            if (xmlTACIdentificationDocument != null) {
                // copy the archive in case anything goes wrong
                ZipArchive archive = new ZipArchive(repositoryArchive);

                OutputStream out = archive.getOutputTo(DeviceRepositoryConstants.TAC_IDENTIFICATION_XML);
                writeDocument(xmlTACIdentificationDocument, out);

                repositoryArchive = archive;
            }
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Write the device hierarchy to the device repository archive.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @throws RepositoryException if there was a problem writing to the device
     * repository.
     */
    public void writeHierarchy() throws RepositoryException {
        try {
            // If we have a hierarchy document, then output it.
            if (xmlHierarchyDocument != null) {
                // copy the archive in case anything goes wrong
                ZipArchive archive = new ZipArchive(repositoryArchive);

                OutputStream out = archive.getOutputTo(DeviceRepositoryConstants.HIERARCHY_XML);
                writeDocument(xmlHierarchyDocument, out);

                repositoryArchive = archive;
            } else {
                // Repository is invalid without a hierarchy.
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "device-repository-invalid",
                                DeviceRepositoryConstants.HIERARCHY_XML));
            }
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Write the policy definitions to the device repository archive.  The
     * definitions must be split out into standard and custom files.
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @throws RepositoryException if there was a problem writing the
     * definitions file.
     */
    public void writeDefinitions() throws RepositoryException {

        Document standardDefDoc = createDocument(
                DeviceRepositorySchemaConstants.DEFINITIONS_ELEMENT_NAME,
                DeviceSchemas.POLICY_DEFINITIONS_CURRENT.getNamespaceURL(),
                DeviceSchemas.POLICY_DEFINITIONS_CURRENT.getLocationURL());
        Element standardDefRoot = standardDefDoc.getRootElement();
        Document customDefDoc = createDocument(
                DeviceRepositorySchemaConstants.DEFINITIONS_ELEMENT_NAME,
                DeviceSchemas.POLICY_DEFINITIONS_CURRENT.getNamespaceURL(),
                DeviceSchemas.POLICY_DEFINITIONS_CURRENT.getLocationURL());
        Element customDefRoot = customDefDoc.getRootElement();

        boolean hasCustomDefs = false;

        Element defRoot = xmlDefinitionsDocument.getRootElement();
        List children = defRoot.getChildren();
        Element element = null;
        Element clone = null;
        for (int i = 0; i < children.size(); i++) {
            element = (Element) children.get(i);

            if (DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME.
                    equals(element.getName())) {
                clone = (Element) element.clone();
                String catName = clone.getAttributeValue(
                        DeviceRepositorySchemaConstants.CATEGORY_NAME_ATTRIBUTE);
                if (catName != null && catName.equals(DeviceRepositorySchemaConstants.CUSTOM_CATEGORY_NAME)) {
                    customDefRoot.addContent(clone);
                    hasCustomDefs = true;
                } else {
                    standardDefRoot.addContent(clone);
                }
            } else if (DeviceRepositorySchemaConstants.
                    POLICY_DEFINITION_TYPES_ELEMENT_NAME.
                    equals(element.getName())) {
                clone = (Element) element.clone();
                standardDefRoot.addContent(clone);
                clone = (Element) element.clone();
                customDefRoot.addContent(clone);
            }
        }

        // copy the archive in case anything goes wrong
        ZipArchive archive = new ZipArchive(repositoryArchive);

        removeFile(archive, DeviceRepositoryConstants.CUSTOM_DEFINITIONS_DIRECTORY, false,
                DeviceRepositorySchemaConstants.DEFINITIONS_DOCUMENT_NAME);
        if (hasCustomDefs) {
            writeFile(archive, DeviceRepositoryConstants.CUSTOM_DEFINITIONS_DIRECTORY,
                    DeviceRepositorySchemaConstants.DEFINITIONS_DOCUMENT_NAME,
                    customDefDoc);
        }
        removeFile(archive, DeviceRepositoryConstants.STANDARD_DEFINITIONS_DIRECTORY, false,
                DeviceRepositorySchemaConstants.DEFINITIONS_DOCUMENT_NAME);
        writeFile(archive, DeviceRepositoryConstants.STANDARD_DEFINITIONS_DIRECTORY,
                DeviceRepositorySchemaConstants.DEFINITIONS_DOCUMENT_NAME,
                standardDefDoc);

        repositoryArchive = archive;
    }

    /**
     * Write the Collection of device Elements to the device repository archive
     *
     * <p>
     * Note that, as per the class javadoc, changes made via this method are
     * only saved to disk when {@link #saveRepositoryArchive} is called.
     *
     * @param devices the Collection of device Elements to write to the device
     * repository archive
     * @throws RepositoryException if there was a problem writing the devices
     * to the repository.
     */
    public void writeDeviceElements(Map devices)
            throws RepositoryException {

        for (Iterator i = devices.keySet().iterator(); i.hasNext();) {
            String deviceName = (String) i.next();

            Element deviceElement = (Element) devices.get(deviceName);

            if (deviceElement != null) {
                // the device element consists of the devices standard and
                // custome properties merged together. When writing out
                // a device element we need to seperate out the standard and
                // custom properties so that we can write to both the
                // custom and standard files.

                // Create the JDOM Document for the standard device file.
                // a device will always have a standard file, however the
                // custom file will only exist if the device has custom
                // properties
                Document standardDeviceDocument = createEmptyDeviceDocument();
                Document customDeviceDocument = null;

                // get the policies element that will contain the merged
                // policies
                Element policiesElement = deviceElement.getChild(
                        DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                        deviceElement.getNamespace());

                if (policiesElement != null) {
                    for (Iterator j = policiesElement.getChildren().iterator();
                         j.hasNext();) {
                        Element policyElement = (Element) j.next();
                        if (policyElement != null) {
                            Element clone = (Element) policyElement.clone();
                            String name = policyElement.getAttributeValue(
                                    DeviceRepositorySchemaConstants.
                                    POLICY_NAME_ATTRIBUTE);
                            Document target = standardDeviceDocument;
                            if (name != null &&
                                    name.startsWith(DeviceRepositoryConstants.CUSTOM_POLICY_NAME_PREFIX)) {
                                // we have a custom property
                                if (customDeviceDocument == null) {
                                    // lazily create the document for
                                    // the custom device file
                                    customDeviceDocument =
                                            createEmptyDeviceDocument();
                                }
                                // set the target to be the custom document
                                target = customDeviceDocument;
                            }
                            // add the cloned policy to the target document.
                            target.getRootElement().getChild(
                                    DeviceRepositorySchemaConstants.
                                    POLICIES_ELEMENT_NAME,
                                    deviceElement.getNamespace()).addContent(clone);
                        }
                    }
                }

                // copy the archive in case anything goes wrong
                ZipArchive archive = new ZipArchive(repositoryArchive);
                // remove the custom file
                removeFile(archive, DeviceRepositoryConstants.CUSTOM_DEVICE_DIRECTORY,
                        false, deviceName);
                if (customDeviceDocument != null) {
                    writeFile(archive, DeviceRepositoryConstants.CUSTOM_DEVICE_DIRECTORY,
                            deviceName, customDeviceDocument);
                }
                removeFile(archive, DeviceRepositoryConstants.STANDARD_DEVICE_DIRECTORY,
                        false, deviceName);
                writeFile(archive, DeviceRepositoryConstants.STANDARD_DEVICE_DIRECTORY,
                        deviceName, standardDeviceDocument);
                repositoryArchive = archive;
            }
        }
    }

    /**
     * creates an empty device document that does not contain any policies.
     * It does however contain a policies element.
     * @return A <code>Document</code> for a device
     */
    private Document createEmptyDeviceDocument() {
        Document document = createDocument(
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME,
                DeviceSchemas.DEVICE_CURRENT.getNamespaceURL(),
                DeviceSchemas.DEVICE_CURRENT.getLocationURL());
        Element device = document.getRootElement();
        // add a policies element to this
        device.addContent(factory.element(
                DeviceRepositorySchemaConstants.POLICIES_ELEMENT_NAME,
                device.getNamespace()));
        return document;
    }

    /**
     * Writes out all standard properties, and all custom properties (if any)
     * as separate files within the device repository.
     * @throws RepositoryException if the properties could not be written
     */
    public void writeProperties() throws RepositoryException {
        OutputStream standardPropsOut = null;
        OutputStream customPropsOut = null;
        try {
            if (properties != null) {
                Properties customProps = new Properties();
                Properties standardProps = new Properties();

                Enumeration allPropertyNames = properties.propertyNames();

                while (allPropertyNames.hasMoreElements()) {
                    String name = (String) allPropertyNames.nextElement();
                    String value = properties.getProperty(name);
                    if (name.startsWith(DeviceRepositoryConstants.CUSTOM_POLICY_RESOURCE_PREFIX)) {
                        customProps.setProperty(name, value);
                    } else {
                        standardProps.setProperty(name, value);
                    }
                }

                // copy the archive in case anything goes wrong
                ZipArchive archive = new ZipArchive(repositoryArchive);

                // The standard properties path is guaranteed to be non-null if
                // this code is being executed. If the path is null, an
                // IOException is thrown by {@link createMergedProperties}
                // (which is called by the constructor).
                standardPropsOut = archive.getOutputTo(standardPropertiesPath);
                try {
                    standardProps.store(standardPropsOut, null);
                } catch (IOException e) {
                    throw new RepositoryException(e);
                }

                if (customProps.size() > 0) {
                    // The properties path will already be set if there were
                    // any custom properties in the repository. Otherwise,
                    // these are the first custom properties to be defined and
                    // written, so set the custom path to a suitable default.
                    if (customPropertiesPath == null) {
                        customPropertiesPath =
                                DeviceRepositoryConstants.CUSTOM_POLICIES_PROPERTIES_PREFIX +
                                DeviceRepositoryConstants.POLICIES_PROPERTIES_SUFFIX;
                    }
                    customPropsOut = archive.getOutputTo(customPropertiesPath);
                    try {
                        customProps.store(customPropsOut, null);
                    } catch (IOException e) {
                        throw new RepositoryException(e);
                    }
                }

                repositoryArchive = archive;
            }
        } finally {
            if (standardPropsOut != null) {
                try {
                    standardPropsOut.close();
                } catch (IOException e) {
                    throw new RepositoryException(e);
                }
            }
            if (customPropsOut != null) {
                try {
                    customPropsOut.close();
                } catch (IOException e) {
                    throw new RepositoryException(e);
                }
            }
        }
    }

    /**
     * Get a device element from the device hierarchy document.
     *
     * @param deviceName the device name to get
     * @return the named device element if found, or null if not found.
     */
    public Element getHierarchyDeviceElement(String deviceName) {

        return getDeviceElement(xmlHierarchyDocument.getRootElement(),
                deviceName);
    }

    /**
     * Add the deviceElement as a child element of the fallback element.
     *
     * @param deviceName the name of the device that is to be added
     * @param fallback      The name of the fallback device.
     * @throws RepositoryException If the device element already exists, or is
     *                             invalid, or if the fallback is invalid (e.g.
     *                             does not exist).
     * @throws IllegalArgumentException if deviceElement is null.
     */
    public void addHierarchyDeviceElement(String deviceName, String fallback)
            throws RepositoryException {

        if (deviceName == null || deviceName.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "deviceName cannot be null or empty.");
        }
        // We do not permit more than one device to have the same name...
        if (deviceExists(deviceName)) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-already-exists",
                            deviceName));
        }
        // Verify that we have a valid fallback. If the fallback is null
        // assume that we are adding the device to the root.
        Element parent = null;
        if (fallback == null) {
            parent = xmlHierarchyDocument.getRootElement();

            // Cannot add more that one root device name.
            List children = parent.getChildren();
            if ((children != null) && (children.size() > 0)) {
                throw new RepositoryException(
                        exceptionLocalizer.format(
                                "device-hierarchy-only-one-root"));
            }
        } else {
            parent = getHierarchyDeviceElement(fallback);
        }
        if (parent != null) {
            // create the device element and add it to it's parent
            parent.addContent(createDeviceElement(
                    deviceName,
                    xmlHierarchyDocument.getRootElement().getNamespace()));
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-fallback-invalid",
                            new Object[]{fallback,
                                         deviceName}));
        }
    }

    /**
     * Creates a device element for the given device name and namespace
     * @param name the name of the device
     * @param namespace the namespace to use when creating the element
     * @return an element instance
     */
    private Element createDeviceElement(String name, Namespace namespace) {
        Element device = factory.element(
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME,
                namespace);
        device.setAttribute(factory.attribute(
                DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE,
                name));
        return device;
    }

    /**
     * Recursively get the element with the name specified by the deviceName
     * parameter. If no name matches, return null.
     *
     * @param parent     the parent element used in the recursive search.
     * @param deviceName the device name to search for.
     * @return the Element if a match occurs, null otherwise.
     * @throws IllegalArgumentException if parent is null.
     */
    private Element getDeviceElement(Element parent, String deviceName) {

        Element result = null;
        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null.");

        }
        if (deviceName != null) {
            List namedChildren = parent.getChildren();

            if (namedChildren != null) {
                // Look in my children for a match.
                for (int i = 0; (result == null) &&
                        (i < namedChildren.size()); i++) {
                    Element child = (Element) namedChildren.get(i);
                    String attributeValue = child.getAttributeValue(
                            DeviceRepositorySchemaConstants.
                            DEVICE_NAME_ATTRIBUTE);
                    String name = child.getName();
                    if (DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME.
                            equals(name) &&
                            deviceName.equals(attributeValue)) {
                        result = child;
                    }
                }
                // Look in each of my children's children...
                for (int i = 0; (result == null) &&
                        (i < namedChildren.size()); i++) {
                    result = getDeviceElement((Element)
                            namedChildren.get(i), deviceName);
                }
            }
        }
        return result;
    }

    //
    // Helper methods for dealing with XML.
    // These should probably be refactored out into a separate object for
    // dealing with XML files as noted in the to do above.
    //

    /**
     * Create an empty JDOM document with the appropriate XMLSchema attributes
     * set up.
     *
     * @param rootName the name of the root element.
     * @param namespaceURI the default namespace for the root element.
     * @param schemaURI the external URI for the schema file associated
     * with the document
     * @return the created document.
     */
    private Document createDocument(String rootName,
                                    String namespaceURI,
                                    String schemaURI) {

        // Create the root element with the appropriate default namespace.
        Element root = factory.element(rootName, namespaceURI);

        // Add the namespace declararion for XMLSchema to root element.
        Namespace xsi = Namespace.getNamespace(XSI, W3CSchemata.XSI_NAMESPACE);
        root.addNamespaceDeclaration(xsi);

        StringBuffer buffer = new StringBuffer(namespaceURI.length() +
                schemaURI.length() + 1);
        buffer.append(namespaceURI)
                .append(' ')
                .append(schemaURI);

        // Add the location for the XSD to the root element.
        root.setAttribute(SCHEMA_LOCATION, buffer.toString(), xsi);

        // Return the document created from this root element.
        return factory.document(root);

    }

    /**
     * Create a JDOM Document from an input stream.
     *
     * @param inputStream the input stream to use for building the
     * document
     * @return the created Document
     * @throws RepositoryException if the a document could not be built from
     *                             the input stream.
     */
    public Document createNewDocument(InputStream inputStream)
            throws RepositoryException {
        return createNewDocument(inputStream, factory, validation,
                ignoreWhitespace);
    }

    /**
     * Create a JDOM Document from an input stream.
     * @param inputStream the input stream to use for building the
     * document
     * @param factory the JDOMFactory used to create the Document and its
     * Elements.
     * @param validation indicate if the document to be created should be
     * parsed against its associated schema
     * @param ignoreWhitespace indicate is whitespace in the inputstream should
     * be ignored for the Document
     * @return the Document
     * @throws RepositoryException
     */
    private static Document createNewDocument(InputStream inputStream,
                                              JDOMFactory factory,
                                              boolean validation,
                                              boolean ignoreWhitespace)
            throws RepositoryException {

        Document result = null;

        // Create a SAX Builder than uses Xerces as the parser. Note that this
        // doesn't utilize the SAX driver parameter that can be passed to the
        // JDOM SAX Builder since this causes class loader issues under JRE 1.4
        // and Eclipse.
        //
        // @todo later use a common specialization of the SAXBuilder
        SAXBuilder builder = new SAXBuilder() {
            protected XMLReader createParser() throws JDOMException {
                // Explicitly construct a Volantisized Xerces parser to
                // avoid any JRE 1.4 class loader issues
                XMLReader parser =
                        new com.volantis.xml.xerces.parsers.SAXParser();
                // This relies on use of the Volantisized JDOM to access
                // this method
                setFeaturesAndProperties(parser, true);

                return parser;
            }
        };

        // Turn on namespace support.
        builder.setFeature("http://xml.org/sax/features/namespaces", true);
        // ensure the builder creates the correct types of nodes
        builder.setFactory(factory);
        // by using a DefaultNamespaceAdapterFilter we ensure that any DEFAULT
        // namespace declarations are replaced with a bound declaration (using
        // a "device" prefixe). This is needed to as XPath does not work with
        // default namespaces.
        builder.setXMLFilter(new DefaultNamespaceAdapterFilter(
                DeviceRepositoryConstants.DEFAULT_NAMESPACE_PREFIX));

        if (validation) {
            // Enable schema validation.

            // Tell JDOM to use validation.
            builder.setValidation(true);

            // Set validation features as per the architecture B.3.2.
            builder.setFeature("http://apache.org/xml/features/validation/" +
                    "schema-full-checking", true);
            builder.setFeature("http://xml.org/sax/features/validation", true);
            builder.setFeature("http://apache.org/xml/features/validation/" +
                    "schema", true);

            builder.setIgnoringElementContentWhitespace(ignoreWhitespace);

            // Configure an entity resolver to find the schemas locally.
            JarFileEntityResolver resolver = new EclipseEntityResolver();
            resolver.addSystemIdMapping(DeviceSchemas.DEVICE_CURRENT);
            resolver.addSystemIdMapping(DeviceSchemas.CORE_CURRENT);
            resolver.addSystemIdMapping(DeviceSchemas.HEIRARCHY_CURRENT);
            resolver.addSystemIdMapping(DeviceSchemas.IDENTIFICATION_CURRENT);
            resolver.addSystemIdMapping(DeviceSchemas.TAC_IDENTIFICATION_CURRENT);
            resolver.addSystemIdMapping(DeviceSchemas.POLICY_DEFINITIONS_CURRENT);
            resolver.addSystemIdMapping(DeviceSchemas.REPOSITORY_CURRENT);

            builder.setEntityResolver(resolver);
        } else {
            // No validation requested.
            builder.setValidation(false);
        }

        try {
            result = builder.build(inputStream);
        } catch (JDOMException e) {
            throw new RepositoryException(e);
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    /**
     * Write a JDOM document to an output stream.
     *
     * @param document
     * @param out
     * @throws IOException
     */
    private void writeDocument(Document document, OutputStream out)
            throws IOException {

        // Transfer bytes from the jdom document to the ZIP file
        XMLOutputter outputter = new XMLOutputter();
        Document currentDocument = document;
        if (xmlFilter != null) {
            SAXBuilder parser = new SAXBuilder();
            parser.setXMLFilter(xmlFilter);
            try {
                currentDocument = parser.build(new StringReader(
                        outputter.outputString(document)));
            } catch (JDOMException e) {
                e.printStackTrace();
                throw new UndeclaredThrowableException(e,
                        "Could not build the a filtered document.");
            }
        }
        // Transfer bytes from the jdom document to the ZIP file
        outputter.setIndent("    ");
        outputter.setTextTrim(true);
        outputter.setNewlines(true);
        outputter.output(currentDocument, out);
        out.flush();
        out.close();
    }

    //
    // Debugging methods.
    //

    /**
     * Utility method to output the contents of this hierarchy to the console.
     */
    private void dumpHierarchy(StringBuffer buffer) {

        buffer.append("\nXML Device Repository Hierarchy\n");
        buffer.append("-------------------------------\n");
        String rootDeviceName = null;
        try {
            rootDeviceName = getRootDeviceName();
            dumpHierarchy(buffer, rootDeviceName, 0);
        } catch (RepositoryException e) {
            e.printStackTrace();
            if (logger.isDebugEnabled()) {
                logger.debug("Could not obtain root device name.", e);
            }
        }
    }

    /**
     * Recursively output the contents of this hierarchy to a buffer.
     */
    private void dumpHierarchy(StringBuffer buffer, String deviceName,
                               int level) {

        for (int i = 0; i < level; i++) {
            buffer.append("    ");
        }
        buffer.append(deviceName).append("\n");

        List list = getChildDeviceNames(deviceName);
        ++level;
        if (list != null) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                dumpHierarchy(buffer, s, level);
            }
        }
    }

    /**
     * Return a string representation of this object.
     *
     * @return a string representation of this object.
     */
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        dumpHierarchy(buffer);
        return buffer.toString();
    }

    /**
     * Get the version of a given device repository. This method is made
     * available so that version information can be accessed without the
     * need to create a new XMLDeviceRepositoryAccessor - which reads in
     * the entire repository.
     * @param repositoryFile the full path name of the device repository
     * file whose version to query.
     * @return the version of the given repository.
     * @throws RepositoryException If there is a problem accessing the version
     * information.
     */
    public static String getRepositoryVersion(File repositoryFile)
            throws RepositoryException {
        return queryRepository(repositoryFile, DeviceRepositoryConstants.VERSION_FILENAME,
                new DeviceRepositoryQuery() {
                    public String doQuery(InputStream zipEntryStream)
                            throws RepositoryException {
                        return retrieveVersion(zipEntryStream);
                    }
                });
    }

    /**
     * Get the revision of a given device repository. This method is made
     * available so that version information can be accessed without the
     * need to create a new XMLDeviceRepositoryAccessor - which reads in
     * the entire repository.
     * @param repositoryFile the full path name of the device repository
     * file whose version to query.
     * @return the revision of the given repository.
     * @throws RepositoryException If there is a problem accessing the version
     * information.
     */
    public static String getRepositoryRevision(File repositoryFile)
            throws RepositoryException {
        return queryRepository(repositoryFile, DeviceRepositoryConstants.REPOSITORY_XML,
                new DeviceRepositoryQuery() {
                    public String doQuery(InputStream zipEntryStream)
                            throws RepositoryException {
                        return retrieveRevision(zipEntryStream, new DefaultJDOMFactory());
                    }
                });
    }

    /**
     * Generic method for querying a ZipEntry in the device repository and
     * returning the result as a String. This method uses a
     * DeviceRepositoryQuery as the executee for query (i.e. a command pattern).
     * @param repositoryFile the repository file
     * @param zipEntryName the name of the ZipEntry in the repository to query
     * @param query the DeviceRepositoryQuery
     * @return the result of the query
     * @throws RepositoryException if there is a problem running the query
     */
    private static String queryRepository(File repositoryFile,
                                          String zipEntryName,
                                          DeviceRepositoryQuery query)
            throws RepositoryException {
        if (!repositoryFile.canRead()) {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "file-cannot-be-read",
                            repositoryFile));
        }

        InputStream is = null;
        String queryResult = null;
        try {
            ZipFile repositoryZip =
                    new ZipFile(repositoryFile, ZipFile.OPEN_READ);
            ZipEntry versionEntry = repositoryZip.getEntry(zipEntryName);
            is = repositoryZip.getInputStream(versionEntry);
            queryResult = query.doQuery(is);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "unexpected-ioexception"),
                    e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("unexpected-ioexception", e);
                    if (queryResult != null) {
                        // A null queryResult would indicate that another exception
                        // was thrown so only throw on a close() exception if this
                        // has not happened to avoid hiding the real problem.
                        throw new RepositoryException(
                                exceptionLocalizer.format(
                                        "unexpected-ioexception"),
                                e);
                    }
                }
            }
        }

        return queryResult;
    }

    /**
     * Get the standard policy resource prefix.
     * @return the resource prefix
     */
    public static String getStandardPolicyResourcePrefix() {
        return DeviceRepositoryConstants.STANDARD_POLICY_RESOURCE_PREFIX;
    }

    /**
     * Interface for use in a command pattern for querying individual
     * ZipEntries within a repository.
     */
    private interface DeviceRepositoryQuery {
        /**
         * Query a zip entry within a device repository.
         * @param zipEntryStream the InputStream associated with the ZipEntry
         * to be queried.
         * @return the result of the query.
         * @throws RepositoryException if there is a problem with the query.
         */
        public String doQuery(InputStream zipEntryStream)
                throws RepositoryException;
    }

    /**
     * Returns the pathname within the device repository of the properties file
     * with the given prefix, and populates the given properties object with
     * the properties found in this file.
     *
     * If properties for the current locale cannot be found then the default
     * non-localized properties file (i.e. English language) will be used. In
     * this latter case some debug will be logged.
     *
     * The rules for finding the locale specific properties file are to look
     * from most specific to most general.
     *
     * @param deviceRepository the device repository as a ZipArchive
     * @param prefix the prefix of the properties file of interest
     * @param properties the Properties object to populate
     * @return the path of the properties file within the device repository
     * @throws IOException if there is a problem reading the device repository
     */
    private String populateProperties(ZipArchive deviceRepository,
                                      String prefix,
                                      Properties properties) throws IOException {

        // get hold of the default locale so that we can try to locate the
        // appropriate properties file.
        Locale locale = Locale.getDefault();
        // the language will be either a 2 character identifier or an empty
        // string
        String language = locale.getLanguage();
        // the country will be either a 2 character identifier or an empty
        // string
        String country = locale.getCountry();
        // the country will be an empty string or some other identifier
        String variant = locale.getVariant();

        String propertiesFile = null;
        // see if we have a properites file with the following suffix
        // _LANGUAGE_COUNTRY_VARIANT.properties
        if (language.length() != 0 &&
                country.length() != 0 &&
                variant.length() != 0) {
            String checkPath =
                    getPropertiesPath(prefix, language, country, variant);
            if (deviceRepository.exists(checkPath)) {
                propertiesFile = checkPath;
            }
        }
        // If we haven't found the properties file then we try the less
        // specific suffix _LANGUAGE_COUNTRY.properites
        if (propertiesFile == null &&
                language.length() != 0 &&
                country.length() != 0) {
            String checkPath =
                    getPropertiesPath(prefix, language, country, null);
            if (deviceRepository.exists(checkPath)) {
                propertiesFile = checkPath;
            }
        }
        // If we haven't found the properties file then we try the less
        // specific suffix _LANGUAGE.properites
        if (propertiesFile == null && language.length() != 0) {
            String checkPath = getPropertiesPath(prefix, language, null, null);
            if (deviceRepository.exists(checkPath)) {
                propertiesFile = checkPath;
            }
        }
        // If we haven't found the properties file then we try the
        // .properties
        if (propertiesFile == null) {
            String checkPath = getPropertiesPath(prefix, null, null, null);
            if (deviceRepository.exists(checkPath)) {
                propertiesFile = checkPath;
            }
        }
        if (propertiesFile != null) {
            // if we found a properties file the load its contents into the
            // properties instance
            properties.load(deviceRepository.getInputFrom(propertiesFile));
        } else if (logger.isDebugEnabled()) {
            logger.debug("Could not locate the properties file for the prefix "
                    + prefix);
        }
        return propertiesFile;
    }

    /**
     * Gets the default name for the properties file using the given prefix and
     * the default locale.
     * @param prefix the file's prefix
     * @param language the locale's language can be null
     * @param country the locale's country can be null
     * @param variant the locale's variant can be null
     * @return the default pathname
     */
    private String getPropertiesPath(String prefix,
                                     String language,
                                     String country,
                                     String variant) {
        StringBuffer buffer = new StringBuffer(prefix);
        if (language != null && language.length() != 0) {
            buffer.append('_').append(language);
            if (country != null && country.length() != 0) {
                buffer.append('_').append(country);
                if (variant != null && variant.length() != 0) {
                    buffer.append('_').append(variant);
                }
            }
        }
        buffer.append(DeviceRepositoryConstants.POLICIES_PROPERTIES_SUFFIX);
        return buffer.toString();
    }

    /**
     * Creates a Properties object containing all standard and optional custom
     * properties.
     * @param deviceRepository the name of the device repository from which
     *                         to retrieve the properties
     * @return the Properties object
     * @throws IOException if the standard property file cannot be
     *                     accessed.
     */
    private Properties createMergedProperties(ZipArchive deviceRepository)
            throws IOException {
        Properties allProps = new Properties();
        Properties customProps = new Properties();

        standardPropertiesPath = populateProperties(deviceRepository,
                DeviceRepositoryConstants.STANDARD_POLICIES_PROPERTIES_PREFIX, allProps);

        // There must be standard properties.
        if (standardPropertiesPath == null) {
            throw new IOException("Error reading standard properties file.");
        }

        customPropertiesPath = populateProperties(deviceRepository,
                DeviceRepositoryConstants.CUSTOM_POLICIES_PROPERTIES_PREFIX, customProps);

        // Custom properties are optional.
        if (customPropertiesPath != null) {
            Enumeration customEnum = customProps.propertyNames();

            while (customEnum.hasMoreElements()) {
                String propertyName = (String) customEnum.nextElement();
                String value = customProps.getProperty(propertyName);
                allProps.setProperty(propertyName, value);
            }
        }

        return allProps;
    }

    /**
     * Gets all standard and custom properties.
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Provide access to the device hierarchy document from the specified
     * device repository using XML document factory provided.
     * <p>
     * This method is static so that it can be called independently of any
     * context within this class and it does not rely on it at all.  It
     * obtains a device hierarchy document from the repository named.
     *
     * @param deviceRepository The repository from which the device hierarchy
     *                         document should extracted.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automatic upgrades.
     * @param jdomFactory          A means of creating XML documents.
     *
     * @return The JDOM Document that represents the device hierarchy
     */
    public static Document
            getDeviceHierarchyDocument(String deviceRepository,
                                       TransformerMetaFactory transformerMetaFactory,
                                       JDOMFactory jdomFactory)
            throws RepositoryException {

        return getDocument(deviceRepository, transformerMetaFactory,
                jdomFactory, DeviceRepositoryConstants.HIERARCHY_XML);
    }

    /**
     * Provide access to the device identification document from the specified
     * device repository using XML document factory provided.
     * <p>
     * This method is static so that it can be called independently of any
     * context within this class and it does not rely on it at all.  It
     * obtains a device hierarchy document from the repository named.
     *
     * @param deviceRepository The repository from which the device
     *                         identification document should extracted.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automatic upgrades.
     * @param jdomFactory          A means of creating XML documents.
     *
     * @return The JDOM Document that represents the device identification
     */
    public static Document
            getDeviceIdentificationDocument(String deviceRepository,
                                            TransformerMetaFactory transformerMetaFactory,
                                            JDOMFactory jdomFactory)
            throws RepositoryException {

        return getDocument(deviceRepository, transformerMetaFactory,
                jdomFactory, DeviceRepositoryConstants.IDENTIFICATION_XML);
    }

    /**
     * Provide access to the a repository document from the specified
     * device repository using XML document factory provided.
     * <p>
     * This method is static so that it can be called independently of any
     * context within this class and it does not rely on it at all.  It
     * obtains a device hierarchy document from the repository named.
     *
     * @param deviceRepository The repository from which the
     *                         document should extracted.
     * @param transformerMetaFactory the meta factory for creating XSL
     *      transforms, used to do automatic upgrades.
     * @param jdomFactory          A means of creating XML documents.
     * @param documentFile the name of the file containing the docuemnt
     * to get
     * @return The JDOM Document that represents the device identification
     */
    private static Document getDocument(String deviceRepository,
                                        TransformerMetaFactory transformerMetaFactory,
                                        JDOMFactory jdomFactory, String documentFile)
            throws RepositoryException {

        Document document = null;

        // Access the appropriate device repository file and obtain a
        // reference to the hierarchy file
        MDPRArchiveAccessor accessor = new MDPRArchiveAccessor(
                deviceRepository, transformerMetaFactory);
        InputStream input = accessor.getArchiveEntryInputStream(documentFile);

        if (input != null) {
            // Read the document file and create the document using the
            // xml document factory provided.
            document = createNewDocument(new BufferedInputStream(input),
                    jdomFactory, false, false);
        } else {
            throw new RepositoryException(
                    exceptionLocalizer.format(
                            "device-repository-file-missing",
                            documentFile));
        }

        return document;
    }

    /**
     * Retrieves the version of the device repository file that this accessor
     * is capable of working with.
     *
     * @return The required version of the device repository
     */
    protected static String getRequiredMDPRVersion() {
        return DeviceRepositoryConstants.VERSION;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 13-Nov-05	9896/3	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 28-Apr-05	7834/1	matthew	VBM:2005041518 Add support for policy and category definition events in the device repository merge

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 17-Nov-04	6012/5	allan	VBM:2004051307 Remove standard elements in admin mode.

 09-Nov-04	6146/1	pcameron	VBM:2004102910 Custom policy property fixes

 09-Nov-04	6106/17	pcameron	VBM:2004102910 Custom policy property fixes

 08-Nov-04	6106/12	pcameron	VBM:2004102910 Custom policy property fixes

 16-Nov-04	4394/12	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/10	allan	VBM:2004051018 Undo/Redo in device editor.

 09-Nov-04	6146/1	pcameron	VBM:2004102910 Custom policy property fixes

 09-Nov-04	6106/17	pcameron	VBM:2004102910 Custom policy property fixes

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 08-Nov-04	6106/12	pcameron	VBM:2004102910 Custom policy property fixes

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 29-Sep-04	5692/1	adrianj	VBM:2004092204 Modify XMLDeviceRepositoryAccessor to remove/rename TAC data with device

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 01-Sep-04	5363/1	allan	VBM:2004081705 Fix initial selection in DeviceSelector - port from 3.2.2

 01-Sep-04	5351/1	allan	VBM:2004081705 Fix initial selection in DeviceSelector

 25-Aug-04	5298/1	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 12-Aug-04	5167/4	adrianj	VBM:2004081107 Created MDPRArchiveAccessor for reading device repository

 11-Aug-04	5126/3	adrian	VBM:2004080303 Added GUI support for Device TACs

 10-Aug-04	5147/1	adrianj	VBM:2004080318 Added support for TAC values to importer

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 07-Jul-04	4822/1	claire	VBM:2004070606 Allow devices to be selected using a regular expression

 17-May-04	4442/6	pcameron	VBM:2004042608 Usages of constants from DeviceRepositorySchemaConstants in XMLDeviceRepositoryAccessor

 17-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue

 13-May-04	4351/1	allan	VBM:2004051011 Fix NullPointerException in StandardElementHandler

 10-May-04	4239/1	allan	VBM:2004042207 SaveAs on DeviceEditor.

 10-May-04	4068/5	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 04-May-04	4007/4	doug	VBM:2004032304 Added a PrimaryPatterns form section

 04-May-04	4113/7	doug	VBM:2004042906 Fixed migration problem with the device repository

 04-May-04	4113/5	doug	VBM:2004042906 Fixed migration problem with the device repository

 04-May-04	4113/3	doug	VBM:2004042906 Fixed migration problem with the device repository

 29-Apr-04	4072/3	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 29-Apr-04	4072/1	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 26-Apr-04	4037/3	doug	VBM:2004042301 Provided mechanism for obtaining an EntityResolver that resolves all MCS repository schemas

 22-Apr-04	3878/13	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/9	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3975/3	allan	VBM:2004042005 Fix multi-value policy migration and related issues.

 21-Apr-04	3016/6	adrian	VBM:2004021301 Fixed merge problems with updated XMLDeviceRepositoryAccessor

 21-Apr-04	3016/4	adrian	VBM:2004021301 Fixed merge problems with updated XMLDeviceRepositoryAccessor

 20-Apr-04	3935/1	allan	VBM:2004020906 Migration, Device Browser & Import support for policy fields.

 16-Apr-04	3740/5	allan	VBM:2004040508 Rework issues.

 16-Apr-04	3740/3	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 14-Apr-04	3825/3	doug	VBM:2004040509 Fixed bug with loading locale specific properties files

 08-Apr-04	3806/2	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 02-Apr-04	3659/4	allan	VBM:2004033002 Update client/server enhancements.

 01-Apr-04	3574/6	allan	VBM:2004032401 Implement merging of device hierarchies.

 30-Mar-04	3574/4	allan	VBM:2004032401 Rework issues.

 26-Mar-04	3568/7	pcameron	VBM:2004032105 Added test case for ObservableProperties PropertyChangeListeners

 26-Mar-04	3568/5	pcameron	VBM:2004032105 Added ObservableProperties and refactored XMLDeviceRepositoryAccessor and DeviceRepositoryAccessorManager to use Properties

 25-Feb-04	3136/1	philws	VBM:2004021908 Remove accessor manager singletons and make MCSDeviceRepositoryProvider and its test case use the runtime device accessor correctly

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/5	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 16-Feb-04	3023/4	philws	VBM:2004010901 Fix JDK 1.4/Eclipse XML API issue with JDOM SAXBuilder and the Volantisized Xerces parser

 11-Feb-04	2862/8	allan	VBM:2004020411 Rework issues.

 11-Feb-04	2862/6	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 05-Dec-03	2075/3	mat	VBM:2003120106 Correct javadoc and tidy imports

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 07-Nov-03	1716/4	geoff	VBM:2003102915 Enhance DeviceRepositoryBuilder to use new Device methods on new accessor. (fix null value handling)

 31-Oct-03	1729/5	geoff	VBM:2003102302 Handle device repository versions (supermerge)

 30-Oct-03	1729/2	geoff	VBM:2003102302 Handle device repository versions

 30-Oct-03	1716/1	geoff	VBM:2003102915 Enhance DeviceRepositoryBuilder to use new Device methods on new accessor.

 30-Oct-03	1716/1	geoff	VBM:2003102915 Enhance DeviceRepositoryBuilder to use new Device methods on new accessor.

 30-Oct-03	1599/9	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor (more review rework from Allan)

 29-Oct-03	1599/7	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor (rework issues from Allan)

 29-Oct-03	1599/4	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 17-Oct-03	1573/1	geoff	VBM:2003101404 XMLDeviceRepositoryAccessor does not validate the xml

 14-Oct-03	1531/1	byron	VBM:2003092205 Create a tool to build the device hierarchy

 13-Oct-03	1499/11	byron	VBM:2003092204 XML accessor improvements

 13-Oct-03	1499/9	byron	VBM:2003092204 XML accessor is resolves to a filename not a url - and other minor changes

 13-Oct-03	1499/7	byron	VBM:2003092204 XML accessor is resolves to a filename not a url - and other minor changes

 13-Oct-03	1499/5	byron	VBM:2003092204 XML accessor is resolves to a filename not a url - and other minor changes

 10-Oct-03	1499/3	byron	VBM:2003092204 Create a Device Repository accessor for the device hierarchy

 ===========================================================================
*/
