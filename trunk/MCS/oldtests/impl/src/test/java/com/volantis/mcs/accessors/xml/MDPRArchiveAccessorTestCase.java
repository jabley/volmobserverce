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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors.xml;

import com.volantis.devrep.repository.accessors.MDPRArchiveAccessor;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.ResourceTemporaryFileCreator;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Test case for the MDPRArchiveAccessor.
 */
public class MDPRArchiveAccessorTestCase extends TestCaseAbstract {

    /**
     * Used to create XSL transformers.
     */
    private static final TransformerMetaFactory transformerMetaFactory =
            new TestTransformerMetaFactory();

    /**
     * Creator class for the upgradeable device repository file which is
     * stored in the jar.
     */
    private static final ResourceTemporaryFileCreator
            upgradeableRepositoryCreator = new ResourceTemporaryFileCreator(
                    MDPRArchiveAccessorTestCase.class,
                    "repository_upgradeable.zip");

    /**
     * Test that when an accessor is created for a file in need of an
     * update, that update occurs.
     *
     * @throws Exception if an error occurs
     */
    public void testUpdateOccurs() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                upgradeableRepositoryCreator);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                String filename = repository.getPath();
                MDPRArchiveAccessor accessor = new MDPRArchiveAccessor(filename,
                        transformerMetaFactory);
                assertTrue(accessor.isCompatible());
                assertTrue(accessor.willBeModifiedOnLoad());
                ZipArchive oa = new ZipArchive(filename);
                assertFalse(
                        "tac-identification.xml should not exist in unmodified file.",
                        oa.exists("tac-identification-xml"));
                ZipArchive za = accessor.getArchive();
                assertTrue("tac-identification.xml should exist in modified file.",
                        za.exists("tac-identification.xml"));

                // Check that the new tac-identification file contains the expected
                // data...
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                za.getInputFrom("tac-identification.xml")));
                String content = "";
                String line = in.readLine().trim();
                while (line != null) {
                    content += line + "\n";
                    line = in.readLine();
                }

                Document tacIdent = null;
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
                builder.setValidation(false);
                tacIdent = builder.build(za.getInputFrom("tac-identification.xml"));

                Element tacIdentRoot = tacIdent.getRootElement();
                assertEquals("Root element should be tacIdentification",
                        "tacIdentification", tacIdentRoot.getName());

                List devices = tacIdentRoot.getChildren("device",
                        tacIdentRoot.getNamespace());

                String[] deviceNames = {
                    "Master",
                    "Mobile",
                    "Handset",
                    "WAP-Handset",
                    "SonyEricsson-WAP",
                    "SonyEricsson-T62",
                    "SonyEricsson-Z200"
                };
                assertEquals("Wrong number of devices in TAC identification file",
                        devices.size(), deviceNames.length);
                for (int i = 0; i < deviceNames.length; i++) {
                    assertTrue("TAC identification should include '" + deviceNames[i] +
                            "' device", containsDevice(devices, deviceNames[i]));
                }
            }
        });
    }

    /**
     * Checks whether a list contains a device element for a particular device
     *
     * @param deviceElements A list of device elements
     * @param devName The device name we're looking for
     * @return True if the list contains the device
     */
    private boolean containsDevice(List deviceElements, String devName) {
        boolean contains = false;
        Iterator it = deviceElements.iterator();
        while (it.hasNext() && !contains) {
            Element device = (Element) it.next();
            if (devName.equals(device.getAttributeValue("name"))) {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Test that getArchiveEntryInputStream works for files that are
     * changed, unchanged, and don't exist.
     *
     * @throws Exception if an error occurs
     */
    public void testGetArchiveEntryInputStream() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                upgradeableRepositoryCreator);
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                String filename = repository.getPath();
                MDPRArchiveAccessor accessor = new MDPRArchiveAccessor(filename,
                        transformerMetaFactory);

                // Check that non-existent file returns null.
                InputStream is = accessor.getArchiveEntryInputStream("monkey.xml");
                assertNull("Non-existent file should return null", is);

                // Check that a previously non-existent file returns a stream if it's
                // created by the upgrade
                is = accessor.getArchiveEntryInputStream("tac-identification.xml");
                assertNotNull(is);

                // Check that an unmodified file returns a stream
                is = accessor.getArchiveEntryInputStream("version.txt");
                assertNotNull(is);
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 12-Aug-04	5167/2	adrianj	VBM:2004081107 Created MDPRArchiveAccessor for reading device repository

 ===========================================================================
*/
