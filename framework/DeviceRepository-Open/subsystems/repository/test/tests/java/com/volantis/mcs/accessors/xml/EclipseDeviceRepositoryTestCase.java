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
package com.volantis.mcs.accessors.xml;

import com.volantis.devrep.device.api.xml.DeviceSchemas;
import com.volantis.devrep.localization.LocalizationFactory;
import com.volantis.devrep.repository.accessors.DeletionFilter;
import com.volantis.devrep.repository.accessors.EclipseDeviceRepository;
import com.volantis.devrep.repository.accessors.MDPRArchiveAccessor;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.io.IOUtils;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.testtools.JDOMUtils;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.ResourceTemporaryFileCreator;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.xml.schema.W3CSchemata;
import junitx.util.PrivateAccessor;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.custommonkey.xmlunit.XMLUnit;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.Filter;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.xml.sax.XMLFilter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class EclipseDeviceRepositoryTestCase extends TestCaseAbstract {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(EclipseDeviceRepositoryTestCase.class);

    /**
     * Used to created JDOM nodes
     */
    private static JDOMFactory jdomFactory = new DefaultJDOMFactory();

    public static final String DEVICE_NAME_ATTRIBUTE = "name";
    
    /**
     * The standard element name filter.
     */
    private static final DeletionFilter STANDARD_ELEMENT_FILTER = new DeletionFilter(
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
     * Used to create XSL transformers.
     */
    private static final TransformerMetaFactory transformerMetaFactory =
            new TestTransformerMetaFactory();

    /**
     * Get an input stream for a heirarchy resource.
     *
     * @param resource
     * @return the heirarchy resource as an input stream.
     */
    private static InputStream getHierarchy(String resource) throws Exception {
        InputStream stream = EclipseDeviceRepositoryTestCase.
                class.getResourceAsStream(resource);
        if (stream == null) {
            throw new Exception(resource + " not found");
        }
        return stream;
    }

    /**
     * Get an input stream for the default heirachy resource.
     *
     * @return the InputStream
     * @throws Exception
     */
    private static InputStream getHierarchy() throws Exception {
        return getHierarchy("hierarchy-architecture.xml");
    }

    /**
     * Helper method to create an XMLDeviceRepositoryAccesssor that doesn't
     * read a zip file (it can read the hierarchy and setup the JDOM from
     * this input.
     * @param inputHierarchy
     * @return the newly created accessor object.
     */
    private static EclipseDeviceRepository updateHierarchy(
            InputStream inputHierarchy,
            String filename) throws Exception {
        return updateHierarchy(inputHierarchy, filename, null);
    }

    /**
     * Helper method to create an XMLDeviceRepositoryAccesssor that doesn't
     * read a zip file (it can read the hierarchy and setup the JDOM from
     * this input.
     * @param inputHierarchy
     * @return the newly created accessor object.
     */
    private static EclipseDeviceRepository updateHierarchy(
            InputStream inputHierarchy,
            String filename,
            XMLFilter filter) throws Exception {

        EclipseDeviceRepository accessor =
                new EclipseDeviceRepository(filename,
                        transformerMetaFactory, jdomFactory, true, true,
                        filter);

        Document document = accessor.createNewDocument(inputHierarchy);
        PrivateAccessor.setField(
                accessor, "xmlHierarchyDocument", document);
        return accessor;
    }

    private static EclipseDeviceRepository updateHierarchy(
            final InputStream inputHierarchy) throws Exception {

        final EclipseDeviceRepository[] accessor =
                new EclipseDeviceRepository[1];
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {

                accessor[0] = updateHierarchy(inputHierarchy,
                        temporaryFile.getPath());
            }
        });
        return accessor[0];
    }

    /**
     * Utility method that ensures we call
     * {@link EclipseDeviceRepository#writeHierarchy} and
     * {@link EclipseDeviceRepository#writeIdentifiers} on the accessor as
     * well as save
     * @param accessor The XMLDeviceRepositoryAccessor that we are using to
     * write the repository.
     * @throws Exception if there is a problem writing the repository to the
     * file system.
     */
    void writeRepository(EclipseDeviceRepository accessor)
            throws Exception {
        accessor.writeHierarchy();
        accessor.writeIdentifiers();
        accessor.writeDefinitions();
        accessor.writeProperties();
        accessor.writeTACs();
        accessor.saveRepositoryArchive();
    }

    /**
     * Set up logging
     * @throws Exception
     */
    protected void setUp() throws Exception {
        BasicConfigurator.configure();
        Category.getRoot().setLevel(Level.OFF);
        XMLUnit.setControlParser(
            "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl");
        // this next line is strictly not required - if no test parser is
        // explicitly specified then the same factory class will be used for
        // both test and control
        XMLUnit.setTestParser(
            "com.volantis.xml.xerces.jaxp.DocumentBuilderFactoryImpl");

        XMLUnit.setSAXParserFactory(
            "com.volantis.xml.xerces.jaxp.SAXParserFactoryImpl");
        XMLUnit.setTransformerFactory(
            "com.volantis.xml.xalan.processor.TransformerFactoryImpl");
    }

    /**
     * Tear down logging.
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        Category.shutdown();
    }

    /**
     * Test the reading of a repository from a zip file stored in a jar file.
     */
    public void testRepositoryRead() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File temporaryFile) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(temporaryFile.getPath(),
                                transformerMetaFactory, jdomFactory, false, true, null);

                assertEquals("Root device name should match",
                        "Master",
                        accessor.getRootDeviceName());

                assertEquals("Root device number of children should match", 6,
                        accessor.getChildDeviceNames("Master").size());
            }
        });
    }

    /**
     * Test the reading and writing of a repository from a local file.
     */
    public void testRepositoryReadAndWrite() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                writeRepository(accessor);

                // Verify that the hierarchy is what we expect.
                verifyHierarchy(accessor);
            }
        });
    }

    /**
     * Test the writing of the repository.
     */
    public void testWriting() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());
            }
        });
    }

    /**
     * Test that hierarchy xml can be read, updated, written and reread.
     */
    public void testUpdateHierarchy() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());


                Document hierarchyDoc = (Document)
                        PrivateAccessor.getField(accessor, "xmlHierarchyDocument");

                Element root = hierarchyDoc.getRootElement();
                Element master = root.getChild("device", root.getNamespace());
                Element newDevice = new Element("device", root.getNamespace());
                newDevice.setAttribute("name", "FakeDevice");
                master.addContent(newDevice);

                accessor.writeHierarchy();
                accessor.saveRepositoryArchive();

                ZipArchive archive = (ZipArchive)
                        PrivateAccessor.getField(accessor, "repositoryArchive");
                InputStream input = archive.getInputFrom("hierarchy.xml");
                if (input != null) {
                    hierarchyDoc = accessor.createNewDocument(
                            new BufferedInputStream(input));
                }

                root = hierarchyDoc.getRootElement();
                master = root.getChild("device", root.getNamespace());
                List children = master.getChildren();

                Element element = (Element) children.get(children.size() - 1);
                assertEquals("Expected new FakeDevice device.", "FakeDevice",
                        element.getAttributeValue("name"));
            }
        });
    }

    /**
     * Test that the definitions xml can be successfully written
     */
    public void testWriteDefinitions() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());

                Element root = accessor.
                        getDevicePolicyDefinitions().getRootElement();

                List children = root.getChildren();
                Element customCat = null;
                for (int i = 0; i < children.size() && customCat == null; i++) {
                    Element child = (Element) children.get(i);
                    if (child.getName().equals("category")) {
                        String nameAtt = child.getAttributeValue("name");
                        if (nameAtt != null && nameAtt.equals("custom")) {
                            customCat = child;
                        }
                    }
                }
                if (customCat == null) {
                    customCat = new Element("category", root.getNamespace());
                    customCat.setAttribute("name", "custom");
                    root.addContent(customCat);
                }

                Element policy = new Element("policy", root.getNamespace());
                policy.setAttribute("name", "wibble");
                Element type = new Element("type", root.getNamespace());
                policy.addContent(type);
                Element bool = new Element("boolean", root.getNamespace());
                type.addContent(bool);
                customCat.addContent(policy);

                accessor.writeDefinitions();
                accessor.saveRepositoryArchive();

                boolean savedNewDefinition = false;
                root = accessor.getDevicePolicyDefinitions().getRootElement();
                children = root.getChildren();
                for (int i = 0; i < children.size(); i++) {
                    Element element = (Element) children.get(i);
                    if (element.getName().equals("category")) {
                        String name = element.getAttributeValue("name");
                        if ("custom".equals(name)) {
                            List custChildren = element.getChildren();
                            for (int j = 0; j < custChildren.size(); j++) {
                                Element ele = (Element) custChildren.get(j);
                                if (ele.getName().equals("policy")) {
                                    String polName = ele.getAttributeValue("name");
                                    if ("wibble".equals(polName)) {
                                        savedNewDefinition = true;
                                    }
                                }
                            }
                        }
                    }
                }

                assertTrue("New policy definition was not saved and retrieved.",
                        savedNewDefinition);
            }
        });
    }

    /**
     * Test that the definitions xml can be successfully read in
     */
    public void testReadDefinitions() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());

                Element root = accessor.
                        getDevicePolicyDefinitions().getRootElement();

                assertNotNull("New policy definition was not saved and retrieved.",
                        root);
            }
        });
    }

    /**
     * Test that a device can be successfully updated, written to the archive
     * and the changed device correctly read back again from the repository.
     */
    public void testUpdateDevice() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());

                Map devices = new HashMap();
                Element device = accessor.retrieveDeviceElement("PC");
                Element policy = retrievePolicy(device, "rendermode");
                policy.setAttribute("value", "wibble");
                devices.put("PC", device);

                accessor.writeDeviceElements(devices);
                accessor.saveRepositoryArchive();

                // now retrieve the policy from the repository again to see if we
                // actually wrote it out.
                device = accessor.retrieveDeviceElement("PC");
                policy = retrievePolicy(device, "rendermode");
                assertEquals("Should have seen a different value for the policy.",
                        "wibble", policy.getAttributeValue("value"));
            }
        });
    }

    /**
     * Tests that the {@link EclipseDeviceRepository#writeDeviceElements}
     * handles a device that has no custom properties
     * @throws Exception if an error occurs
     */
    public void testWriteDeviceElementsNoCustomProperties() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                ZipFile beforeZip = new ZipFile(repository);
                try {
                    assertNull("standard device should not exist",
                            beforeZip.getEntry("standard/devices/NewDevice.xml"));
                    assertNull("custom device should not exist",
                            beforeZip.getEntry("custom/devices/NewDevice.xml"));
                } finally {
                    beforeZip.close();
                }

                String policies =
                        "<policy name=\"width\" value=\"999\"/>" +
                        "<policy name=\"ssl\" value=\"true\"/>";
                String newDevice = createDeviceElementString(policies);
                Map deviceMap = new HashMap();
                deviceMap.put("NewDevice", JDOMUtils.createElement(newDevice));

                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);

                // write out the fred device
                accessor.writeDeviceElements(deviceMap);
                accessor.saveRepositoryArchive();

                ZipFile afterZip = new ZipFile(repository);
                try {
                    assertNotNull("standard device should exist",
                            afterZip.getEntry("standard/devices/NewDevice.xml"));
                    assertNull("custom device should NOT exist",
                            afterZip.getEntry("custom/devices/NewDevice.xml"));

                    Element device = parseFile(afterZip,
                            "standard/devices/NewDevice.xml");

                    assertXMLEquals("device element not as expected",
                            newDevice,
                            JDOMUtils.convertToString(device));
                } finally {
                    afterZip.close();
                }
            }
        });
    }

    /**
     * Tests that the {@link EclipseDeviceRepository#writeDeviceElements}
     * handles a device that has ONLY custom properties
     * @throws Exception if an error occurs
     */
    public void testWriteDeviceElementsOnlyCustomProperties()
            throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                ZipFile beforeZip = new ZipFile(repository);
                try {
                    assertNull("standard device should not exist",
                            beforeZip.getEntry("standard/devices/NewDevice.xml"));
                    assertNull("custom device should not exist",
                            beforeZip.getEntry("custom/devices/NewDevice.xml"));
                } finally {
                    beforeZip.close();
                }

                String policies =
                        "<policy name=\"custom.width\" value=\"999\"/>" +
                        "<policy name=\"custom.ssl\" value=\"true\"/>";
                String newDevice = createDeviceElementString(policies);
                Map deviceMap = new HashMap();
                deviceMap.put("NewDevice", JDOMUtils.createElement(newDevice));

                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);

                // write out the fred device
                accessor.writeDeviceElements(deviceMap);
                accessor.saveRepositoryArchive();

                ZipFile afterZip = new ZipFile(repository);
                try {
                    assertNotNull("standard device should exist",
                            afterZip.getEntry("standard/devices/NewDevice.xml"));
                    assertNotNull("custom device should exist",
                            afterZip.getEntry("custom/devices/NewDevice.xml"));

                    Element standardDevice = parseFile(afterZip,
                            "standard/devices/NewDevice.xml");
                    Element expectedStandard =
                            JDOMUtils.createDocument(newDevice).getRootElement();
                    Namespace ns = expectedStandard.getNamespace();
                    expectedStandard.getChild("policies", ns).
                            removeChildren("policy", ns);
                    assertXMLEquals("NewDevice device element not as expected",
                            expectedStandard,
                            standardDevice);


                    Element customDevice = parseFile(afterZip,
                            "custom/devices/NewDevice.xml");

                    assertXMLEquals("NewDevice device element not as expected",
                            newDevice,
                            customDevice);
                } finally {
                    afterZip.close();
                }
            }
        });
    }

    /**
     * Tests that the {@link EclipseDeviceRepository#writeDeviceElements}
     * handles a device that has both custom and standard properties
     * @throws Exception if an error occurs
     */
    public void testWriteDeviceElementsStandardAndCustomProperties()
            throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                ZipFile beforeZip = new ZipFile(repository);
                try {
                    assertNull("standard NewDevice device should not exist",
                            beforeZip.getEntry("standard/devices/NewDevice.xml"));
                    assertNull("custom NewDevice device should not exist",
                            beforeZip.getEntry("custom/devices/NewDevice.xml"));
                } finally {
                    beforeZip.close();
                }

                String policies =
                        "<policy name=\"width\" value=\"999\"/>" +
                        "<policy name=\"custom.ssl\" value=\"true\"/>";

                Map deviceMap = new HashMap();
                String newDevice = createDeviceElementString(policies);
                deviceMap.put("NewDevice", JDOMUtils.createElement(newDevice));

                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);

                // write out the fred device
                accessor.writeDeviceElements(deviceMap);
                accessor.saveRepositoryArchive();

                ZipFile afterZip = new ZipFile(repository);
                try {
                    assertNotNull("standard NewDevice device should exist",
                            afterZip.getEntry("standard/devices/NewDevice.xml"));
                    assertNotNull("custom NewDevice device should exist",
                            afterZip.getEntry("custom/devices/NewDevice.xml"));

                    Element standardNewDevice = parseFile(
                            afterZip, "standard/devices/NewDevice.xml");

                    String standardPolices =
                            "<policy name=\"width\" value=\"999\"/>";


                    String expectedStandard = createDeviceElementString(standardPolices);

                    assertXMLEquals("standard device element not as expected",
                            expectedStandard,
                            standardNewDevice);

                    Element customNewDevice = parseFile(
                            afterZip, "custom/devices/NewDevice.xml");

                    String customPolicies =
                            "<policy name=\"custom.ssl\" value=\"true\"/>";

                    String expectedCustom = createDeviceElementString(customPolicies);

                    assertXMLEquals("standard device element not as expected",
                            expectedCustom,
                            customNewDevice);
                } finally {
                    afterZip.close();
                }
            }
        });
    }

    /**
     * Create a device element string using the policy elements provided.
     *
     * @param policies the policy elements as a string.
     * @return the device element as a string.
     */
    private String createDeviceElementString(String policies) {
        String newDevice =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<device xmlns=\"http://www.volantis.com/xmlns/device-repository/device\" " +
                "xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " +
                "xsi:schemaLocation=\"http://www.volantis.com/xmlns/device-repository/device " +
                "http://www.volantis.com/schema/device-repository/v3.0/device.xsd\">" +
                "<policies>" +
                policies +
                "</policies>" +
                "</device>";
        return newDevice;
    }

    /**
     * Test that an added custom policy is saved to the custom file
     * @throws Exception
     */
    public void testAddCustomPolicy() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());

                Element device = accessor.retrieveDeviceElement("PC");
                Element policies = device.getChild("policies", device.getNamespace());
                Element newPolicy = new Element("policy", device.getNamespace());
                newPolicy.setAttribute("name", "custom.mypolicy");
                newPolicy.setAttribute("value", "xyz");
                policies.addContent(newPolicy);

                Map devices = new HashMap();
                devices.put("PC", device);
                ArrayList list = new ArrayList(1);
                list.add(device);
                accessor.writeDeviceElements(devices);
                accessor.saveRepositoryArchive();

                ZipArchive archive = (ZipArchive)
                        PrivateAccessor.getField(accessor, "repositoryArchive");
                device = retrieveCustomDeviceElement(accessor, archive, "PC");
                Element policy = retrievePolicy(device, "custom.mypolicy");
                assertEquals("Should have seen a different value for the policy.",
                        "xyz", policy.getAttributeValue("value"));

                device = retrieveStandardDeviceElement(accessor, archive, "PC");
                policy = retrievePolicy(device, "custom.mypolicy");
                assertNull("policy should not have been found.", policy);


                // now retrieve the policy from the repository again to see if we
                // actually wrote it out.
                device = accessor.retrieveDeviceElement("PC");
                policy = retrievePolicy(device, "custom.mypolicy");
                assertEquals("Should have seen a different value for the policy.",
                        "xyz", policy.getAttributeValue("value"));
            }
        });
    }

    /**
     * Test that definitions documents are correctly merged together.
     */
    public void testMergeDefinitionDocuments() throws Throwable {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());

                Document custom = new Document();
                Element root = new Element("definitions");
                custom.setRootElement(root);

                Element typesElement = new Element("types");
                Element type = new Element("type");
                typesElement.addContent(type);
                root.addContent(typesElement);

                Document master = (Document) custom.clone();
                Element masterRoot = master.getRootElement();

                Element browserCat = new Element("category");
                browserCat.setAttribute("name", "browser");
                masterRoot.addContent(browserCat);

                Element ergoCat = new Element("category");
                ergoCat.setAttribute("name", "ergonomics");
                masterRoot.addContent(ergoCat);


                Element customCat = new Element("category");
                customCat.setAttribute("name", "custom");
                root.addContent(customCat);

                assertTrue("master definition should have 3 child elements",
                        masterRoot.getChildren().size() == 3);

                // parameter specifications
                Class[] types = {Document.class, Document.class};
                Object[] params = {master, custom};

                // add the selector
                try {
                    PrivateAccessor.invoke(accessor, "mergeDefinitionDocuments",
                            types, params);
                } catch (Throwable throwable) {
                    // better to do this than make it throw Throwable.
                    throw new Exception(throwable);
                }

                assertTrue("master definition should now have 4 child elements",
                        masterRoot.getChildren().size() == 4);

                List children = masterRoot.getChildren();
                Element child1 = (Element) children.get(0);
                assertEquals("Unexpected child element.",
                        "types", child1.getName());

                Element child2 = (Element) children.get(1);
                String name = child2.getAttributeValue("name");
                assertEquals("Unexpected child element.", "browser", name);

                Element child3 = (Element) children.get(2);
                name = child3.getAttributeValue("name");
                assertEquals("Unexpected child element.", "ergonomics", name);

                Element child4 = (Element) children.get(3);
                name = child4.getAttributeValue("name");
                assertEquals("Unexpected child element.", "custom", name);
            }
        });
    }

    /**
     * Test that device documents are correctly merged together.
     */
    public void testMergeDeviceDocuments() throws Throwable {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Should write the repository atomically (preserve original if
                // write fails).
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());

                Document doc1 = new Document();
                Element device1 = new Element("device");
                doc1.setRootElement(device1);
                Element policies1 = new Element("policies");
                device1.addContent(policies1);

                Element policy1a = new Element("policy");
                policy1a.setAttribute("name", "abc");
                policy1a.setAttribute("value", "123");

                Element policy1b = new Element("policy");
                policy1b.setAttribute("name", "def");
                policy1b.setAttribute("value", "456");

                policies1.addContent(policy1a);
                policies1.addContent(policy1b);


                Document doc2 = new Document();
                Element device2 = new Element("device");
                doc2.setRootElement(device2);
                Element policies2 = new Element("policies");
                device2.addContent(policies2);

                Element policy2a = new Element("policy");
                policy2a.setAttribute("name", "ghi");
                policy2a.setAttribute("value", "789");

                policies2.addContent(policy2a);

                assertTrue("doc1 policies element should have 2 child policies",
                        policies1.getChildren().size() == 2);

                // parameter specifications
                Class[] types = {Document.class, Document.class};
                Object[] params = {doc1, doc2};

                // add the selector
                try {
                    PrivateAccessor.invoke(accessor, "mergeDeviceDocuments", types, params);
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                assertTrue("doc1 policies element should have 3 child policies",
                        policies1.getChildren().size() == 3);

            }
        });
    }

    /**
     * Retrieve a named policy as a JDOM Element from a specified device.
     * @param deviceElement The device Element whose policy value to retrieve.
     * @param policyName The short name of the policy whose value to retrieve.
     * @return The JDOM Element representation of the named policy value for the
     * specified device or null if the policy value is not set in the
     * named device.
     * @throws IllegalStateException If the specified device was not
     * found in the repository.
     */
    protected Element retrievePolicy(Element deviceElement, String policyName)
            throws Exception {
        Element policies = deviceElement.getChild("policies",
                deviceElement.getNamespace());

        Element policy = null;
        Iterator iterator = policies.getChildren().iterator();
        while (iterator.hasNext() && policy == null) {
            Element policyElement = (Element) iterator.next();
            String policyElementName =
                    policyElement.getAttributeValue(DEVICE_NAME_ATTRIBUTE);
            if (policyElementName.equals(policyName)) {
                policy = policyElement;
            }
        }

        return policy;
    }

    /**
     * Retrieve a Device as a JDOM Element. This method will retrieve only
     * custom policies for the device.
     * @param accessor the accessor to use (to create the document)
     * @param deviceName The name of the device to retrieve.
     * @param archive The repository to retrieve from.
     * @return The Element definition of the specified device.
     * @throws RepositoryException If there was a problem retrieving the device
     */
    protected Element retrieveCustomDeviceElement(EclipseDeviceRepository
            accessor, ZipArchive archive, String deviceName)
            throws RepositoryException {
        Document customDocument = null;
        InputStream customInput = archive.getInputFrom(
                "custom/devices/" + deviceName + ".xml");
        if (customInput != null) {
            customDocument = accessor.createNewDocument(customInput);

        }
        return customDocument.getRootElement();
    }

    /**
     * Retrieve a Device as a JDOM Element. This method will retrieve only
     * standard policies for the device.
     * @param accessor the accessor to use (to create the document)
     * @param deviceName The name of the device to retrieve.
     * @param archive The repository to retrieve from.
     * @return The Element definition of the specified device.
     * @throws RepositoryException If there was a problem retrieving the device
     */
    protected Element retrieveStandardDeviceElement(EclipseDeviceRepository
            accessor, ZipArchive archive, String deviceName)
            throws RepositoryException {
        Document customDocument = null;
        InputStream customInput = archive.getInputFrom(
                "standard/devices/" + deviceName + ".xml");
        if (customInput != null) {
            customDocument = accessor.createNewDocument(customInput);

        }
        return customDocument.getRootElement();
    }

    /**
     * Test the writing of the repository.
     */
    public void testWritingReadonlyRepository() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Create a temporary repository which will be deleted later.
                EclipseDeviceRepository accessor = updateHierarchy(
                        getHierarchy(), repository.getPath());

                // Ensure the file is READ ONLY.
                repository.setReadOnly();

                // Should now create a backup file too.
                writeRepository(accessor);
                assertTrue("Repository should exist", repository.exists());

                File backupRepository = new File(repository.getPath() + ".bak");
                assertFalse("Backup repository should NOT exist",
                        backupRepository.exists());
            }
        });
    }

    /**
     * Ensures that custom properties are written out to the custom properties
     * file.
     *
     * @todo later this has been set as "noTest" as various developers have been having trouble with it along the lines of 'Custom properties file should be the default expected:<custom/policies/resources/policies.properties> but was:<null>'
     */
    public void noTestWriteCustomProperties() throws Throwable {
        // Turn on debug logging
        Category.getRoot().setLevel(Level.DEBUG);

        final String customPolicyProps =
                "custom/policies/resources/policies.properties";
        final String policyPrefix = "policy." +
                EclipseDeviceRepository.getCustomPolicyNamePrefix();
        final String customNameKey = policyPrefix + "newCustom.name";
        final String customNameValue = "newCustomPolicy";
        final String customDescKey = policyPrefix + "newCustom.description";
        final String customDescValue = "Some helpful description";

        // Create some new custom properties
        final Properties properties = new Properties();
        properties.setProperty(customNameKey, customNameValue);
        properties.setProperty(customDescKey, customDescValue);

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                MDPRArchiveAccessor archiveAccessor = new MDPRArchiveAccessor(
                        repository.getPath(), transformerMetaFactory);
                final EclipseDeviceRepository writeRepository =
                        new EclipseDeviceRepository(archiveAccessor,
                                jdomFactory,
                                false,
                                true,
                                null);

                // There should not be a pre-existing custom policy properties
                // file.
                boolean existsBefore =
                        archiveAccessor.getArchive().exists(customPolicyProps);
                assertFalse("Custom policies.properties should not exist",
                        existsBefore);

                // Set the custom properties on the accessor.
                PrivateAccessor.setField(writeRepository, "properties", properties);

                // Write out the properties, standard and custom, and save the
                // repository.
                writeRepository.writeProperties();

                if (logger.isDebugEnabled()) {
                    // Print out details about the original repository archive
                    // and copy it to /tmp/r1.zip. This was added to help debug
                    // problems with committing VBM:2004110904.
                    dumpFile(repository, new File("/tmp/r1.zip"));
                }

                writeRepository.saveRepositoryArchive();

                if (logger.isDebugEnabled()) {
                    // Print out details about the modifed repository archive
                    // and copy it to /tmp/r2.zip. This was added to help debug
                    // problems with committing VBM:2004110904.
                    dumpFile(repository, new File("/tmp/r2.zip"));
                }

                // Create a brand new accessor to read the saved modified
                // repository. This ensures starting from a "clean slate".
                final EclipseDeviceRepository readRepository =
                        new EclipseDeviceRepository(archiveAccessor,
                                jdomFactory,
                                false,
                                true,
                                null);

                // The custom policy properties file should now exist, and be the
                // "default".
                boolean existsAfter =
                        archiveAccessor.getArchive().exists(customPolicyProps);
                assertTrue("Custom policies.properties should exist",
                        existsAfter);

                // Check that the customPropertiesPath field is set correctly.
                String actualPath =
                        (String) PrivateAccessor.getField(readRepository,
                                "customPropertiesPath");
                assertEquals("Custom properties file should be the default",
                        customPolicyProps,
                        actualPath);

                final Properties propertiesRead = readRepository.getProperties();

                // Check the custom policy's name is read back
                String name = (String) propertiesRead.get(customNameKey);
                assertEquals("Custom name should be the same",
                        customNameValue, name);

                // Check the custom policy's description is read back
                String desc =
                        (String) propertiesRead.get(customDescKey);
                assertEquals("Custom description should be the same",
                        customDescValue, desc);
            }
        });
    }

    /**
     * Debugging method to print out the details for and then copy a repository
     * archive file. This enables us to later see what the file was during the
     * test.
     * <p>
     * This was added to help debug problems with committing VBM:2004110904.
     *
     * @param repository the repository archive file to operate on.
     * @param copy the file to copy the repository archive file to.
     */
    private void dumpFile(File repository, File copy) {

        try {
            if (copy.exists() && !copy.delete()) {
                throw new RuntimeException("can't delete copy: " + copy);
            }

            printFile("Real Archive File", repository);
            new ZipArchive(repository.getPath());

            IOUtils.copyFiles(repository, copy);

            printFile("Copy Archive File", copy);
            new ZipArchive(copy.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Debugging method to print out the metadata associated with a file.
     * <p>
     * This was added to help debug problems with committing VBM:2004110904.
     *
     * @param description a textual description of the file.
     * @param file the file to print the metadata for.
     */
    private void printFile(String description, File file) {
        if (logger.isDebugEnabled()) {
            logger.debug(description + ":" + file + ", " +
                    "Exists:" + file.exists() + ", " +
                    "Length:" + file.length() + ", " +
                    "Last Modified: " + new Date(file.lastModified()));
        }
    }

    /**
     * Test to see if the retrieval of a fallback for a particular device works.
     */
    public void testGetFallbackDeviceName() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);

                assertEquals("Fallback should match",
                        null,
                        accessor.getFallbackDeviceName(null));

                assertEquals("Fallback should match",
                        "Master",
                        accessor.getFallbackDeviceName("Voice"));

                assertEquals("Fallback should match",
                        null,
                        accessor.getFallbackDeviceName("xxx"));

                assertEquals("Fallback should match",
                        "WAP-Handset",
                        accessor.getFallbackDeviceName("Nokia-WAP"));
            }
        });
    }

    /**
     * Test to see if a particular device exists in the hierarchy.
     */
    public void testDeviceExistsInHierarchy() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);
                // Master should exist
                assertTrue(accessor.deviceExists("Master"));

                // xxx shoudn't exist
                assertFalse(accessor.deviceExists("xxx"));

                // null shouldn't exist
                assertFalse(accessor.deviceExists(null));
            }
        });
    }

    /**
     * Test retrieval of a device identification elements.
     */
    public void testRetrieveDeviceIdentificationElement() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);

                // Retrieve the identification using a device that has one.
                Element identification =
                        accessor.retrieveDeviceIdentificationElement("PC");

                assertEquals("The retrieved identication element is not for the" +
                        "PC device.", "PC",
                        identification.getAttributeValue("name"));
            }
        });
    }

    /**
     * Test retrieval of a device identification elements.
     */
    public void testRetrieveDeviceTACIdentificationElement() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);

                // Retrieve the identification using a device that has one.
                Element identification =
                        accessor.retrieveTACDeviceElement("Nokia-6210");

                assertEquals("The retrieved identication element is not for the" +
                        "Nokia-6210 device.", "Nokia-6210",
                        identification.getAttributeValue("name"));
            }
        });
    }

    /**
     * Test isStandardDevice().
     */
    public void testIsStandardDevice() {
        String device = "_device";
        assertFalse("Device \"" + device +
                "\" should not be a standard device",
                EclipseDeviceRepository.isStandardDevice(device));

        device = "device";
        assertTrue("Device \"" + device +
                "\" should be a standard device",
                EclipseDeviceRepository.isStandardDevice(device));
    }

    /**
     * Test what happens when you try and load a repository without any
     * device definitions.
     */
    public void testNoDevice() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                try {
                    updateHierarchy(getHierarchy("hierarchy-empty.xml"));
                    fail("empty hierarchy is not valid");
                } catch (RepositoryException e) {
                    // success
                }
            }
        });
    }

    /**
     * Test the getting of the root device name.
     */
    public void testGetRootDeviceName() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);
                assertEquals("Root device name should match",
                        "Master",
                        accessor.getRootDeviceName());

                accessor = updateHierarchy(getHierarchy("hierarchy-root.xml"));
                assertEquals("Root device name should match",
                        "root",
                        accessor.getRootDeviceName());
            }
        });
    }

    /**
     * Test the retrieval of child device names for a particular device name.
     */
    public void testGetChildDeviceNames() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true,
                                true, null);
                List children;
                children = accessor.getChildDeviceNames(null);
                assertEquals("Child device names should match",
                        null,
                        children);

                children = accessor.getChildDeviceNames("xxx");
                assertEquals("Child device names should match",
                        null,
                        children);

                verifyHierarchy(accessor);

                children = accessor.getChildDeviceNames("NoChildrenDevice");
                assertNull("No children should exist", children);
            }
        });
    }

    /**
     * Verify that the hierarchy is what we expect (based on the hierarchy
     * field in this class). This verification is quite basic (not all nodes
     * are checked.
     */
    private void verifyHierarchy(EclipseDeviceRepository accessor) {
        List children;
        children = accessor.getChildDeviceNames("Master");
        assertNotNull("Children should exist", children);
        assertEquals("Expected number of children", 6,
                children.size());
        String expectedChildren[] = {
            "PC", "Mobile", "Voice", "TV", "Kiosk",
            "Internet-Appliance"
        };
        for (int i = 0; i < expectedChildren.length; i++) {
            assertTrue("Child should be in collection",
                    children.contains(expectedChildren[i]));
        }
    }

    /**
     * Test the renaming of the device.
     *
     * <p>This test randomly fails for some reason producing the result
     * "expected <0> but was <1>". I have commented out because it is
     * bogus.</p>
      * @todo this should be re-instated
     */
    public void notestRenameDevice() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);
                try {
                    accessor.renameDevice(null, null);
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // success
                }

                try {
                    accessor.renameDevice("PC", null);
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // success
                }

                try {
                    accessor.renameDevice("xxx", "WP");
                    fail("RepositoryException expected");
                } catch (RepositoryException e) {
                    // success
                }

                accessor.renameDevice("PC", "PeeCee");
                assertFalse("PC should not be found",
                        accessor.deviceExists("PC"));
                assertTrue("PeeCee should be found",
                        accessor.deviceExists("PeeCee"));

                accessor.renameDevice("WAP-Handset", "WP");
                assertFalse("WAP-Handset should not be found",
                        accessor.deviceExists("WAP-Handset"));
                assertTrue("WP should be found",
                        accessor.deviceExists("WP"));

                // Save the repository contents.
                writeRepository(accessor);

                // Read the contents back to verify them.
                ZipFile zipFile = new ZipFile(repository);
                try {
                    // Ensure devices are renamed in the hierarchy.
                    Element element = parseFile(zipFile, "hierarchy.xml");
                    Element master = assertContainsDevice(element, "Master");
                    element = assertContainsDevice(master, "Mobile");
                    element = assertContainsDevice(element, "Handset");
                    assertContainsDevice(element, "WP");
                    assertContainsDevice(master, "PeeCee");

                    // Ensure entries are renamed in devices directories.
                    assertNull(zipFile.getEntry("standard/devices/PC.xml"));
                    assertNotNull(zipFile.getEntry("standard/devices/PeeCee.xml"));
                    assertNull(zipFile.getEntry("standard/devices/WAP-Handset.xml"));
                    assertNotNull(zipFile.getEntry("standard/devices/WP.xml"));

                    // Ensure device names in device entry files is renamed
                    element = parseFile(zipFile, "standard/devices/PeeCee.xml");
                    assertEquals("", "PeeCee", element.getAttributeValue("name"));
                    element = parseFile(zipFile, "standard/devices/WP.xml");
                    assertEquals("", "WP", element.getAttributeValue("name"));

                    // Ensure devices in identification file are renamed
                    element = parseFile(zipFile, "identification.xml");
                    assertContainsNoDevice(element, "PC");
                    assertContainsDevice(element, "PeeCee");
                    assertContainsNoDevice(element, "WAP-Handset");
                    assertContainsDevice(element, "WP");

                    // Ensure devices in tac-identification file are renamed
                    element = parseFile(zipFile, "tac-identification.xml");
                    assertContainsNoDevice(element, "PC");
                    assertContainsDevice(element, "PeeCee");
                    assertContainsNoDevice(element, "WAP-Handset");
                    assertContainsDevice(element, "WP");
                } finally {
                    zipFile.close();
                }

                // todo: add custom as well
            }
        });
    }

    private Element parseFile(ZipFile zipFile, String name) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        ZipEntry entry = zipFile.getEntry(name);
        Document doc = builder.build(zipFile.getInputStream(entry));
        Element element = doc.getRootElement();
        return element;
    }

    /**
     * Test the removal of a device.
     *
     * <p>This test randomly fails for some reason producing the result
     * "expected <1> but was <0>". I have commented out because it is
     * bogus.</p>
      * @todo this should be re-instated
     */
    public void notestRemoveDevice() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);
                try {
                    accessor.removeDevice(null);
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // expected
                }

                try {
                    accessor.removeDevice("xxx");
                    fail("Expected an RepositoryException");
                } catch (RepositoryException e) {
                    // expected
                }

                try {
                    accessor.removeDevice("PC");
                } catch (RepositoryException e) {
                    fail("Did not expect a RepositoryException");
                    e.printStackTrace();
                }

                // Save the repository contents.
                writeRepository(accessor);

                // Read the contents back to verify them.
                ZipFile zipFile = new ZipFile(repository);
                try {
                    // Ensure PC is removed from hierarchy.
                    Element element = parseFile(zipFile, "hierarchy.xml");
                    element = assertContainsDevice(element, "Master");
                    assertContainsNoDevice(element, "PC");

                    // Ensure PC entries are removed from devices directories.
                    assertNull(zipFile.getEntry("standard/devices/PC.xml"));
                    assertNull(zipFile.getEntry("custom/devices/PC.xml"));

                    // Ensure PC is removed from identification file
                    element = parseFile(zipFile, "identification.xml");
                    element = assertContainsDevice(element, "Master");
                    assertContainsNoDevice(element, "PC");

                    // Ensure PC is removed from tac-identification file
                    element = parseFile(zipFile, "tac-identification.xml");
                    element = assertContainsDevice(element, "Master");
                    assertContainsNoDevice(element, "PC");
                } finally {
                    zipFile.close();
                }
            }
        });
    }

    private void assertDeviceFileEquals(ZipFile zipFile,
                                        String deviceDirectory,
                                        String deviceName,
                                        Map customPolicyMap)
            throws Exception {
        // Ensure PC is removed from hierarchy.
        Element element = parseFile(zipFile, "hierarchy.xml");
        element = assertContainsDevice(element, "Master");
        assertContainsNoDevice(element, "PC");

        // Ensure PC entries are removed from devices directories.
        assertNull("", zipFile.getEntry("standard/devices/PC.xml"));
        assertNull("", zipFile.getEntry("custom/devices/PC.xml"));

    }

    Element assertContainsDevice(Element element, String name) {
        List elements = element.getContent(new DeviceFilter(name));
        assertEquals("", elements.size(), 1);
        element = (Element) elements.get(0);
        return element;
    }

    void assertContainsNoDevice(Element element, String name) {
        List elements = element.getContent(new DeviceFilter(name));
        assertEquals("", elements.size(), 0);
    }

    /**
     * A JDOM filter to finding device elements with the name attribute
     * specified.
     */
    class DeviceFilter implements Filter {
        String name;

        public DeviceFilter(String name) {
            this.name = name;
        }

        public boolean matches(Object o) {
            boolean success = false;
            if (o instanceof Element) {
                Element element = (Element) o;
                if (name.equals(element.getAttributeValue("name"))) {
                    success = true;
                }
            }
            return success;
        }
    }

    /**
     * Test the exists method.
     */
    public void testExists() throws Exception {
        try {
            new EclipseDeviceRepository(null,
                    transformerMetaFactory, jdomFactory, null);
            fail("Illegal Argument Exception should've been thrown");
        } catch (IllegalArgumentException e) {
            // success
        }

        try {
            new EclipseDeviceRepository(null,
                    jdomFactory);
            fail("Illegal Argument Exception should've been thrown");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the exists method.
     */
    public void testReadingEmptyRepository() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new ResourceTemporaryFileCreator(this.getClass(),
                        "repository_empty.zip"));
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                try {
                    new EclipseDeviceRepository(repository.getPath(),
                            transformerMetaFactory, jdomFactory, null);
                    fail("Expected Repository Exception (ZipExcepiton)");
                } catch (RepositoryException e) {
                    // success
                }
            }
        });
    }


    /**
     * Test the getProperties() method.
     */
    public void testGetProperties() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, false, true, null);
                // Create a list of all policy names.
                List propsList = new ArrayList();
                Properties props = accessor.getProperties();
                Enumeration propsEnum = props.propertyNames();
                while (propsEnum.hasMoreElements()) {
                    propsList.add(propsEnum.nextElement());
                }

                // Test presence of first standard property
                assertTrue(propsList.contains("policy.J2MEconf.name"));
                // Test presence of random properties
                assertTrue(propsList.contains("policy.disptech.name"));
                assertTrue(propsList.contains("policy.network.latency.name"));
                assertTrue(propsList.contains("policy.gifinpage.name"));
                assertTrue(propsList.contains("policy.mp3inpage.name"));
                assertTrue(propsList.contains("policy.UAProf.TablesCapable.name"));
                assertTrue(propsList.contains("policy.entrytype.name"));
                assertTrue(propsList.contains("policy.portability.name"));
                assertTrue(propsList.contains("policy.msvid.name"));
                assertTrue(propsList.contains("policy.dvidcamera.name"));
                assertTrue(propsList.contains("policy.wtls.name"));
                assertTrue(propsList.contains("policy.postype.name"));
                assertTrue(propsList.contains("policy.gpng2rule.name"));
                assertTrue(propsList.contains("policy.protocol.wml.emulate.smallTag.name"));

                // Test presence of last standard property
                assertTrue(propsList.contains("policy.wtlskeystrng.description"));
            }
        });
    }

    /**
     * Ensures that the correct properties file is loaded when the local
     * specifies the language, country and variant.
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWithLanguageCountryVariantSet()
            throws Throwable {
        doTestPopulateProperties(
                new Locale("en", "US", "VARIANT1_VARIANT2"),
                "standard/policies/resources/" +
                "test_en_US_VARIANT1_VARIANT2.properties");

    }

    /**
     * Ensures that the correct properties file is loaded when the local
     * provides the language, country and an unhandled variant.
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWithLanguageCountryInvalidVariantSet()
            throws Throwable {
        doTestPopulateProperties(
                new Locale("en", "US", "variant99"),
                "standard/policies/resources/test_en_US.properties");
    }

    /**
     * Ensures that the correct properties file is loaded when the local
     * specifies the language and country.
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWithLanguageCountrySet()
            throws Throwable {
        doTestPopulateProperties(
                new Locale("en", "US"),
                "standard/policies/resources/test_en_US.properties");
    }

    /**
     * Ensures that the correct properties file is loaded when the local
     * specifies the language and an unhandled country .
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWithLanguageInvalidCountryVariantSet()
            throws Throwable {
        doTestPopulateProperties(
                new Locale("en", "DE", "variant99"),
                "standard/policies/resources/test_en.properties");
    }

    /**
     * Ensures that the correct properties file is loaded when the local
     * specifies the language.
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWithLanguageSet()
            throws Throwable {
        doTestPopulateProperties(
                new Locale("en", ""),
                "standard/policies/resources/test_en.properties");
    }

    /**
     * Ensures that the correct properties file is loaded when the local
     * specifies an unhandled language.
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWithInvalidLanguageSet()
            throws Throwable {
        doTestPopulateProperties(
                new Locale("de", ""),
                "standard/policies/resources/test.properties");
    }

    /**
     * Ensures that null is returned when the properties file does not exist
     * @throws Throwable if an error occurs
     */
    public void testPopulatePropertiesWhenFileDoesNotExist()
            throws Throwable {
        assertNull("Properties file was found",
                executePopulateProperties(new Properties(),
                        "bogus/path/test"));
    }

    /**
     * Tests the {@link EclipseDeviceRepository#populateProperties} method
     * @param locale the locale to use
     * @param expectedFilename the expected properties file.
     * @throws Throwable if an error occurs
     */
    private void doTestPopulateProperties(Locale locale,
                                          String expectedFilename)
            throws Throwable {

        String prefix = "standard/policies/resources/test";
        Properties properties = new Properties();
        Locale currentLocale = Locale.getDefault();
        try {
            Locale.setDefault(locale);
            String path = executePopulateProperties(properties, prefix);
            assertEquals("Unexpected properties file loaded ",
                    expectedFilename,
                    path);
        } finally {
            Locale.setDefault(currentLocale);
        }
    }

    /**
     * Helper method that executes the
     * {@link EclipseDeviceRepository#populateProperties} method
     * @param properties the Properities that are to be populated
     * @param prefix the prefix
     * @return the file path to the properties file that was loaded
     * @throws Throwable if an error occurs
     */
    private String executePopulateProperties(final Properties properties,
                                             final String prefix)
            throws Throwable {

        final String[] result = new String[1];
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File file) throws Exception {
                MDPRArchiveAccessor archiveAccessor = new MDPRArchiveAccessor(
                        file.getPath(), transformerMetaFactory);
                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(archiveAccessor,
                                jdomFactory,
                                false,
                                true,
                                null);
                try {
                    result[0] = (String) PrivateAccessor.invoke(accessor,
                            "populateProperties",
                            new Class[]{ZipArchive.class,
                                        String.class,
                                        Properties.class},
                            new Object[]{archiveAccessor.getArchive(),
                                         prefix,
                                         properties});
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }
            }
        });
        return result[0];
    }

    /**
     * Enusre that the {@link EclipseDeviceRepository#moveDevice} method
     * throws an IllegalArgumentException when the device argument is null
     * @throws Exception if an error occurs
     */
    public void testMoveDeviceNullDeviceArg() throws Exception {
        try {
            TemporaryFileManager manager = new TemporaryFileManager(
                    new TestDeviceRepositoryCreator());
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repository) throws Exception {
                    EclipseDeviceRepository accessor = new
                            EclipseDeviceRepository(repository.getPath(),
                                    transformerMetaFactory, jdomFactory, false, true, null);

                    accessor.moveDevice(null, "parent");
                    fail("IllegalArgumentException was not thrown for null device");
                }
            });
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Enusre that the {@link EclipseDeviceRepository#moveDevice} method
     * throws an IllegalArgumentException when the parentDevice argument is null
     * @throws Exception if an error occurs
     */
    public void testMoveDeviceNullParentDeviceArg() throws Exception {
        try {
            TemporaryFileManager manager = new TemporaryFileManager(
                    new TestDeviceRepositoryCreator());
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repository) throws Exception {
                    EclipseDeviceRepository accessor = new
                            EclipseDeviceRepository(repository.getPath(),
                                    transformerMetaFactory, jdomFactory, false, true, null);

                    accessor.moveDevice("device", null);
                    fail("IllegalArgumentException was not thrown for null parent");
                }
            });
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Enusre that the {@link EclipseDeviceRepository#moveDevice} method
     * throws an RepositoryException when the device argument specifies
     * a device that does not exist
     * @throws Exception if an error occurs
     */
    public void testMoveDeviceNonExistentDeviceArg() throws Exception {
        try {
            TemporaryFileManager manager = new TemporaryFileManager(
                    new TestDeviceRepositoryCreator());
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repository) throws Exception {
                    EclipseDeviceRepository accessor = new
                            EclipseDeviceRepository(repository.getPath(),
                                    transformerMetaFactory, jdomFactory, false, true, null);

                    accessor.moveDevice("doesNotExistDevice", "parent");
                    fail("RepositoryException was not thrown for non existent device");
                }
            });
        } catch (RepositoryException e) {
            // expected
        }
    }

    /**
     * Enusre that the {@link EclipseDeviceRepository#moveDevice} method
     * throws an RepositoryException when the parent device argument
     * specifies a device that does not exist
     * @throws Exception if an error occurs
     */
    public void testMoveDeviceNonExistentParentArg() throws Exception {
        try {
            TemporaryFileManager manager = new TemporaryFileManager(
                    new TestDeviceRepositoryCreator());
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repository) throws Exception {
                    EclipseDeviceRepository accessor = new
                            EclipseDeviceRepository(repository.getPath(),
                                    transformerMetaFactory, jdomFactory, false, true, null);

                    accessor.moveDevice("Master", "doesNotExistDevice");
                    fail("RepositoryException was not thrown for non existent parent");
                }
            });
        } catch (RepositoryException e) {
            // expected
        }
    }

    /**
     * Enusre that the {@link EclipseDeviceRepository#moveDevice} method
     * throws an IllegalAddException when the parent argument is an ancestor
     * of te device argument
     * @throws Exception if an error occurs
     */
    public void testMoveDeviceToInvalidChild() throws Throwable {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor = new
                        EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, false, true, null);

                Element pc = null;
                try {
                    pc = (Element) PrivateAccessor.invoke(
                            accessor,
                            "getHierarchyDeviceElement",
                            new Class[]{String.class},
                            new String[]{"PC"});
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                assertNotNull("PC device does not exist");
                Element pcParent = pc.getParent();
                try {
                    // try to move the master so that it is a child of PC. This should
                    // throw an IllegalAddException
                    accessor.moveDevice("Master", "PC");
                    fail("RepositoryException was not thrown when moving a device " +
                            "whose parent is a child of the device being moved");
                } catch (IllegalArgumentException e) {
                    // expected
                }
                // ensure the the PC device still has the same parent
                assertSame("PC device has an unexcepted parent",
                        pcParent,
                        pc.getParent());
            }
        });
    }

    /**
     * Enusre that the {@link EclipseDeviceRepository#moveDevice} method
     * actually moves the devices when the arguments are valid
     * @throws Exception if an error occurs
     */
    public void testMoveDevice() throws Throwable {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                EclipseDeviceRepository accessor = new
                        EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, false, true, null);

                // move the PC device to TV
                Element pc = null;
                try {
                    pc = (Element) PrivateAccessor.invoke(
                            accessor,
                            "getHierarchyDeviceElement",
                            new Class[]{String.class},
                            new String[]{"PC"});
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }
                // check that the TV device is not already the parent of the PC
                assertNotEquals(pc.getParent().getAttributeValue(
                        DeviceRepositorySchemaConstants.
                        DEVICE_NAME_ATTRIBUTE),
                        "TV");
                // do the move
                accessor.moveDevice("PC", "TV");
                // check that the TV device is the new parent of the PC device
                assertEquals("Device was not moved",
                        "TV",
                        pc.getParent().getAttributeValue(
                                DeviceRepositorySchemaConstants.
                        DEVICE_NAME_ATTRIBUTE));
            }
        });

    }

    /**
     * This tests the {@link EclipseDeviceRepository#getDeviceHierarchyDocument(String, TransformerMetaFactory, JDOMFactory)}
     * static method.
     */
    public void testGetDeviceHierarchyDocument() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                // Don't need a test instance for the static test but to test that
                // the static test hasn't altered any state an instance is required
                EclipseDeviceRepository originalRepository = new
                        EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, false,
                                true, null);

                // Grab the originalDocument before anything is executed so that it can
                // be used for comparison after each static test
                Document originalDocument = (Document)
                        PrivateAccessor.getField(originalRepository,
                                "xmlHierarchyDocument");

                // Grab the document
                Document document =
                        EclipseDeviceRepository.getDeviceHierarchyDocument(
                                repository.getPath(), transformerMetaFactory,
                                jdomFactory);

                // Ensure the internal originalDocument is the same
                assertSame("Accessor should not have been modified",
                        originalDocument,
                        PrivateAccessor.getField(originalRepository,
                                "xmlHierarchyDocument"));

                // Check the document is there!
                assertNotNull("Document should exist", document);

                // Check the root node
                assertEquals("Root device name should match",
                        "hierarchy",
                        document.getRootElement().getName());

                // Grab a device child element
                List list = document.getRootElement().getChildren();
                Element element = (Element) list.get(0);
                // Now get the device categories
                list = element.getChildren();

                // And finally check the correct number of categories exists
                assertEquals("Device categories should match expected number",
                        6,
                        list.size());
            }
        });
    }

    public void testRemovalOfStandardElement() throws Exception {
        final String part1 =
                "<device xmlns=\"http://www.volantis.com/xmlns/device-repository/device\" " +
                "xmlns:xsi=\"" + W3CSchemata.XSI_NAMESPACE + "\" " +
                "xsi:schemaLocation=\"http://www.volantis.com/xmlns/device-repository/device " +
                "http://www.volantis.com/schema/device-repository/v3.0/device.xsd\">" +
                "<policies>" +
                "<device:policy xmlns:device=\"http://www.volantis.com/xmlns/device-repository/device\" name=\"protocol.content.type\" />" +
                "<device:policy xmlns:device=\"http://www.volantis.com/xmlns/device-repository/device\" name=\"adcpm32inpage\" value=\"false\"";

        final String standardElement =
                "><device:standard>" +
                "<device:policy name=\"adcpm32inpage\" value=\"false\" />" +
                "</device:standard>" +
                "</device:policy></policies></device>";


        SAXBuilder parser = new SAXBuilder();
        Document doc = parser.build(new StringReader(
                part1 + standardElement));

        XMLOutputter outputter = new XMLOutputter();
        outputter.setOmitDeclaration(true);

        parser.setXMLFilter(STANDARD_ELEMENT_FILTER);
        Document cleaned = parser.build(new StringReader(
                outputter.outputString(doc)));

        // For some reason the outputter always appends a '\r\n' to the end
        // so just add this to the expected string anyway.
        String expected = part1 + " /></policies></device>" +
                PrivateAccessor.getField(XMLOutputter.class,
                        "STANDARD_LINE_SEPARATOR");
        String result = outputter.outputString(cleaned);

        assertEquals("Result should match:'", expected, result);
    }


    /**
     * Tests that the TAC identification document read in from the
     * repository has the expected values.
     *
     * @throws Exception if an error occurs
     *
     */

    public void testGetTACIdentificationDocument() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                EclipseDeviceRepository accessor =
                        new EclipseDeviceRepository(repository.getPath(),
                                transformerMetaFactory, jdomFactory, true, true, null);
                // Grab the document
                Document document = accessor.getDeviceTACIdentificationDocument();
                assertNotNull("TAC identification document should be non-null",
                        document);
                Element rootEl = document.getRootElement();
                assertEquals("Root element must have expected number of children",
                        5, rootEl.getChildren().size());
                Element childEl = (Element) rootEl.getChildren().get(1);
                String devName = childEl.getAttributeValue
                        (DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
                assertEquals("Second child should be Nokia 6210", "Nokia-6210",
                        devName);
                assertEquals("Second child should have two children", 2,
                        childEl.getChildren().size());
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/3	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 06-Sep-05	9407/9	pduffin	VBM:2005083007 Fixed conflict

 06-Sep-05	9407/6	pduffin	VBM:2005083007 Fixed conflict

 06-Sep-05	9407/4	pduffin	VBM:2005083007 Fixed conflict

 06-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 06-Sep-05	9453/1	rgreenall	VBM:2005040504 mergevbm from 330: Allow \ character in proxy username.

 06-Sep-05	9434/1	pcameron	VBM:2005090204 Fixed cookie propagation when HTTP caching in DSB

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 01-Jul-05	8925/2	allan	VBM:2005062308 Move ArrayUtils to Synergetics

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Dec-04	6383/1	philws	VBM:2004120206 Provide base infrastructure for FormatRenderer mechanism

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	6260/1	allan	VBM:2004110907 NullPointerException and WidgetDisposed error

 17-Nov-04	6012/5	allan	VBM:2004051307 Remove standard elements in admin mode.

 01-Jun-04	4410/5	byron	VBM:2004051307 Device Editor Administrator mode proliferates standard elements

 01-Jun-04	4410/3	byron	VBM:2004051307 Device Editor Administrator mode proliferates standard elements

 18-May-04	4410/1	byron	VBM:2004051307 Device Editor Administrator mode proliferates standard elements

 11-Nov-04	6156/1	geoff	VBM:2004110904 ICS GIF support shopuld be on by default in MCS v3.2.3

 09-Nov-04	6146/2	pcameron	VBM:2004102910 Custom policy property fixes

 08-Nov-04	6106/6	pcameron	VBM:2004102910 Custom policy property fixes

 29-Sep-04	5692/3	adrianj	VBM:2004092204 Fix to test device repository

 29-Sep-04	5692/1	adrianj	VBM:2004092204 Modify XMLDeviceRepositoryAccessor to remove/rename TAC data with device

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 27-Aug-04	5315/4	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 12-Aug-04	5167/1	adrianj	VBM:2004081107 Created MDPRArchiveAccessor for reading device repository

 11-Aug-04	5126/2	adrian	VBM:2004080303 Added GUI support for Device TACs

 04-Aug-04	5065/1	adrianj	VBM:2004080214 Added foundations for device lookup by TAC in XML repository

 07-Jul-04	4822/1	claire	VBM:2004070606 Allow devices to be selected using a regular expression

 04-May-04	4113/4	doug	VBM:2004042906 Fixed migration problem with the device repository

 22-Apr-04	3878/8	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/6	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/4	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 21-Apr-04	3016/5	adrian	VBM:2004021301 Fixed merge problems with updated XMLDeviceRepositoryAccessor

 21-Apr-04	3935/4	allan	VBM:2004020906 Fix merge issues.

 20-Apr-04	3935/1	allan	VBM:2004020906 Migration, Device Browser & Import support for policy fields.

 20-Apr-04	3909/3	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 19-Apr-04	3904/1	allan	VBM:2004020903 Support localized device policy categories

 16-Apr-04	3362/4	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 14-Apr-04	3825/2	doug	VBM:2004040509 Fixed bug with loading locale specific properties files

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 01-Apr-04	3574/1	allan	VBM:2004032401 Implement merging of device hierarchies.

 25-Mar-04	3568/1	pcameron	VBM:2004032105 Added ObservableProperties and refactored XMLDeviceRepositoryAccessor and DeviceRepositoryAccessorManager to use Properties

 18-Feb-04	3060/1	philws	VBM:2004021701 Implement runtime device repository accessor

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 11-Feb-04	2862/2	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 30-Oct-03	1729/2	geoff	VBM:2003102302 Handle device repository versions

 30-Oct-03	1599/5	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor (more review rework from Allan)

 29-Oct-03	1599/3	geoff	VBM:2003101501 Support Device access in the new XMLDeviceRepositoryAccessor

 17-Oct-03	1573/1	geoff	VBM:2003101404 XMLDeviceRepositoryAccessor does not validate the xml

 14-Oct-03	1531/1	byron	VBM:2003092205 Create a tool to build the device hierarchy

 13-Oct-03	1499/5	byron	VBM:2003092204 XML accessor improvements

 13-Oct-03	1499/3	byron	VBM:2003092204 XML accessor is resolves to a filename not a url - and other minor changes

 10-Oct-03	1499/1	byron	VBM:2003092204 Create a Device Repository accessor for the device hierarchy

 ===========================================================================
*/
