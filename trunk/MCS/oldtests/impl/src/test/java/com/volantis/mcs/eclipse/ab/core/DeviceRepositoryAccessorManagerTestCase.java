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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.core;

import com.volantis.devrep.repository.accessors.EclipseDeviceRepository;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ResolvedDevicePolicy;
import com.volantis.mcs.eclipse.core.DeviceHeaderPattern;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.ArrayObject;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import junitx.util.PrivateAccessor;
import org.apache.regexp.RE;
import org.jdom.Element;
import org.jdom.filter.Filter;
import org.jdom.input.JDOMFactory;
import org.jdom.input.DefaultJDOMFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Testcase for DeviceRepositoryAccessorManager.
 *
 * NOTE: Instances of a DeviceRepositoryAccessorManager are re-created in
 * each test method because some tests will cause modification to the
 * content of the repository (though not the repository file) and having
 * a single DeviceRepositoryAccessorManager that is re-used is liable to
 * break some tests if the repository content changes.
 */
public class DeviceRepositoryAccessorManagerTestCase extends TestCaseAbstract {
    /**
     * Test the retrieveRootDeviceName method.
     */
    public void testRetrieveRootDeviceName() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String rootDeviceName = manager.retrieveRootDeviceName();
                assertEquals("The root device name is not Master.", "Master",
                        rootDeviceName);
            }
        });
    }

    /**
     * Test the retrieveRootDeviceName method.
     */
    public void testRemoveDevice() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {
                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);
                final String deviceName = "MyTestDevice";

                assertFalse("Test device already exists!",
                            manager.deviceExists(deviceName));

                manager.createDevice("Master", deviceName);

                assertNotNull("Device identification element is null!",
                              manager.retrieveDeviceIdentification(deviceName));

                manager.removeDevice(deviceName);

                try {
                    manager.retrieveDeviceIdentification(deviceName);

                    fail("Expected an illegal argument exception when " +
                         "looking for ID element for non-existent device");
                } catch (IllegalArgumentException e) {
                    // Expected condition
                }
            }
        });
    }

    /**
     * Test that deviceExists() works for both existing and non-existing
     * devices.
     */
    public void testDeviceExists() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                assertTrue("Expected the \"PC\" device to exist.",
                        manager.deviceExists("PC"));
                assertFalse("Expected the \"FantasicAmazingDevice\" device to not exist.",
                        manager.deviceExists("FantasticAmazingDevice"));
            }
        });
    }

    /**
     * Test that getDeviceRepositoryName() provide the right name.
     */
    public void testGetDeviceRepositoryName() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                assertEquals("Unexpected device repository name.",
                        repository.getPath(), manager.getDeviceRepositoryName());
            }
        });
    }

    /**
     * Test that the
     * {@link DeviceRepositoryAccessorManager#getLocalizedPolicyName} throws
     * an IllegalArgumentException when the policyName is null
     */
    public void testGetLocalizedPolicyNameNullPolicy() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                try {
                    manager.getLocalizedPolicyName(null);
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        });
    }

    /**
     * Test that the
     * {@link DeviceRepositoryAccessorManager#getLocalizedPolicyName} throws
     * an IllegalArgumentException when the policyName is an empty string
     */
    public void testGetLocalizedPolicyNameEmptyPolicy() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                try {
                    manager.getLocalizedPolicyName("");
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        });
    }

    /**
     * Test that getLocalizedPolicyName retrieves a localized policy name.
     */
    public void testGetLocalizedPolicyName() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String localized = manager.getLocalizedPolicyName("beep");
                assertEquals("Unexpected localized policy name.", "Beep",
                        localized);
            }
        });
    }

    /**
     * Test that the
     * {@link DeviceRepositoryAccessorManager#setLocalizedPolicyName} throws
     * an IllegalArgumentException when the policyName is null
     */
    public void testSetLocalizedPolicyNameNullPolicy() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                try {
                    manager.setLocalizedPolicyName(null, "localName");
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        });
    }

    /**
     * Test that the
     * {@link DeviceRepositoryAccessorManager#setLocalizedPolicyName} throws
     * an IllegalArgumentException when the policyName is an empty string
     */
    public void testSetLocalizedPolicyNameEmptyPolicy() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                try {
                    manager.setLocalizedPolicyName("", "localName");
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        });
    }

    /**
     * Test that the
     * {@link DeviceRepositoryAccessorManager#setLocalizedPolicyName} throws
     * an IllegalArgumentException when the localizedName is null
     */
    public void testSetLocalizedPolicyNameNullLocalName() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                try {
                    manager.setLocalizedPolicyName("beep", null);
                    fail("Expected an IllegalArgumentException");
                } catch (IllegalArgumentException e) {
                    // expected
                }
            }
        });
    }

    /**
     * Test that getLocalizedPolicyName retrieves a localized policy name.
     */
    public void testSetLocalizedPolicyName() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                // unfortunately relies on getLocalizedPolicyName
                assertEquals("Unexpected localized policy name.",
                             "Beep",
                             manager.getLocalizedPolicyName("beep"));
                manager.setLocalizedPolicyName("beep", "Loud Beep");
                assertEquals("new name was not set",
                             "Loud Beep",
                             manager.getLocalizedPolicyName("beep"));
            }
        });
    }

    /**
     * Test that getLocalizedPolicyName retrieves a localized policy name.
     */
    public void testGetLocalizedCategory() throws Exception {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String localized = manager.getLocalizedPolicyCategory("system");
                assertEquals("Unexpected localized category name.", "System",
                        localized);
            }
        });
    }

    /**
     * Test that getPolicyDescription retrieves a policy description.
     */
    public void testGetPolicyDescription() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String description = manager.getPolicyDescription("beep");

                String expected = "Indicates whether the device supports basic sound" +
                        " output that can be used for feedback, such as a 'beep'";

                assertEquals("Unexpected policy description.", expected, description);
            }
        });
    }

    /**
     * Test that getPolicyCategory retrieves a correct category.
     */
    public void testGetPolicyCategory() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String category = manager.getPolicyCategory("bmpinpage");

                assertEquals("Unexpected policy category.", "image", category);
            }
        });
    }

    /**
     * Test the resolvePolicy() method.
     */
    public void testResolvePolicy() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);


                // Beep for PC should be resolved from Master and should be false.
                ResolvedDevicePolicy policy = manager.resolvePolicy("PC", "beep");
                String policyValue = policy.policy.getAttributeValue("value");
                assertEquals("Expected PC beep policy to be false.",
                        "false", policyValue);
                assertEquals("Expected origin of PC beep policy to be Master.",
                        "Master", policy.deviceName);

                // Bookmarks for PC should be resolved from PC and should be true
                policy = manager.resolvePolicy("PC", "bookmarks");
                policyValue = policy.policy.getAttributeValue("value");
                assertEquals("Expected PC bookmarks policy to be true.",
                        "true", policyValue);
                assertEquals("Expected origin of PC bookmarks policy to be PC.",
                        "PC", policy.deviceName);
            }
        });
    }

    /**
     * Test the resolvePolicy() method when an inherit element is present.
     */
    public void testResolvePolicyWithInherit() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);


                // fullpixelsx for PC should be resolved from Master as there is an
                // inherit element and so the value should be the inherited -1 rather than
                // the 800 defined as the value in PC.
                ResolvedDevicePolicy policy = manager.resolvePolicy("PC", "dvidcamera");
                String policyValue = policy.policy.getAttributeValue("value");
                assertEquals("Expected PC dvidcamera policy to be false.",
                        "false", policyValue);
                assertEquals("Expected origin of PC fullpixelsx policy to be Master.",
                        "Master", policy.deviceName);
            }
        });
    }

    /**
     * Test the retrieveDeviceElement() method.
     */
    public void testRetrieveDeviceElement() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                Element element = manager.retrieveDeviceElement("Voice");
                assertEquals("Unexpected name for the Voice device.",
                        "Voice", element.getAttributeValue("name"));
            }
        });

    }

    /**
     * Test the retrieveDeviceIdentification() method.
     */
    public void testRetrieveDeviceIdentification() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                Element identification = manager.retrieveDeviceIdentification("Master");
                assertNotNull("Expected a null identification for device \"Master\"",
                        identification);
                identification = manager.retrieveDeviceIdentification("Pogo");
                assertEquals("Unexpected a device identification device.",
                        "Pogo", identification.getAttributeValue("name"));
            }
        });
    }

    /**
     * Test the retrieveTACDeviceElement() method.
     */
    public void testRetrieveTACDeviceElement() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                Element tac = manager.retrieveDeviceTACElement("Master");
                assertNotNull("Expected a TAC Element for device \"Master\"", tac);

                tac = manager.retrieveDeviceTACElement("Nokia-6210");
                List numbers = tac.getContent(new NumberFilter());
                assertEquals("Expected two TAC values", 2, numbers.size());
                String value = ((Element)numbers.get(0)).getText();
                assertEquals("Unexpected TAC value", "350612", value);
                value = ((Element)numbers.get(1)).getText();
                assertEquals("Unexpected TAC value", "35061220", value);
            }
        });
    }

    /**
     * A JDOM filter to finding device elements with the name attribute
     * specified.
     */
    class NumberFilter implements Filter {
        public boolean matches(Object o) {
            boolean success = false;
            if (o instanceof Element) {
                Element element = (Element) o;
                if (DeviceRepositorySchemaConstants.
                        NUMBER_ELEMENT_NAME.equals(element.getName())) {
                    success = true;
                }
            }
            return success;
        }
    }

    /**
     * Tests the {@link DeviceRepositoryAccessorManager#getDeviceTACs}
     * method
     * @throws Exception if an error occurs
     */
    public void testGetDeviceTACPatterns() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String[] patterns = manager.getDeviceTACs("Nokia-6210");
                assertEquals("Expected 2 TAC patterns ", 2, patterns.length);
                assertEquals("Unexpected first pattern", "350612", patterns[0]);
                assertEquals("Unexpected second pattern", "35061220", patterns[1]);
            }
        });
    }

    /**
     * Tests the {@link DeviceRepositoryAccessorManager#setDeviceTACs}
     * method
     * @throws Exception if an error occurs
     */
    public void testSetDeviceTACPatterns() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String[] patterns = new String[] {"123456", "12345678", "87654321"};

                manager.setDeviceTACs("Nokia-6210", patterns);

                String[] updated = manager.getDeviceTACs("Nokia-6210");
                assertEquals("Expected 3 user agent patterns ", 3, updated.length);
                assertEquals("Unexpected first pattern", "123456", updated[0]);
                assertEquals("Unexpected second pattern", "12345678", updated[1]);
                assertEquals("Unexpected third pattern", "87654321", updated[2]);
            }
        });
    }

    /**
     * Test the policyNamesIterator() method.
     */
    public void testPolicyNamesIterator() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);


                // Create a list of all policy names.
                List policyNamesList = new ArrayList();
                Iterator it = manager.policyNamesIterator();
                while (it.hasNext()) {
                    policyNamesList.add(it.next());
                }

                // Test presence of first policy name
                assertTrue(policyNamesList.contains("J2MEconf"));

                // Test presence of random policies from different categories
                assertTrue(policyNamesList.contains("disptech"));
                assertTrue(policyNamesList.contains("network.latency"));
                assertTrue(policyNamesList.contains("gifinpage"));
                assertTrue(policyNamesList.contains("mp3inpage"));
                assertTrue(policyNamesList.contains("UAProf.TablesCapable"));
                assertTrue(policyNamesList.contains("entrytype"));
                assertTrue(policyNamesList.contains("portability"));
                assertTrue(policyNamesList.contains("msvid"));
                assertTrue(policyNamesList.contains("dvidcamera"));
                assertTrue(policyNamesList.contains("wtls"));
                assertTrue(policyNamesList.contains("postype"));
                assertTrue(policyNamesList.contains("gpng2rule"));
                assertTrue(policyNamesList.contains("protocol.wml.emulate.smallTag"));

                // Test presence of last policy name
                assertTrue(policyNamesList.contains("smsprotocol"));
            }
        });
    }

    /**
     * Test the categoryPolicyNamesIterator() method.
     */
    public void testCategoryPolicyNamesIterator() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);


                // Create a list of all policy names.
                List policyNamesList = new ArrayList();
                Iterator it = manager.categoryPolicyNamesIterator("image");
                while (it.hasNext()) {
                    policyNamesList.add(it.next());
                }

                // Test presence of all image category policies.
                assertTrue(policyNamesList.contains("UAProf.ImageCapable"));
                assertTrue(policyNamesList.contains("bmpinpage"));
                assertTrue(policyNamesList.contains("gifinpage"));
                assertTrue(policyNamesList.contains("jpeginpage"));
                assertTrue(policyNamesList.contains("mpegiframe"));
                assertTrue(policyNamesList.contains("pjpeginpage"));
                assertTrue(policyNamesList.contains("pnginpage"));
                assertTrue(policyNamesList.contains("preferredimagetype"));
                assertTrue(policyNamesList.contains("tiffinpage"));
                assertTrue(policyNamesList.contains("wbmpinpage"));
                assertTrue(policyNamesList.contains("videotexinpage"));

                assertEquals("List size should match", 11, policyNamesList.size());
            }
        });
    }

    /**
     * Tests the {@link DeviceRepositoryAccessorManager#getUserAgentPatterns}
     * method
     * @throws Exception if an error occurs
     */
    public void testGetUserAgentPatterns() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String[] patterns = manager.getUserAgentPatterns("PC");
                assertEquals("Expected 5 user agent patterns ", 5, patterns.length);
                assertEquals("Unexpected first pattern", "Mozilla/4.*", patterns[0]);
                assertEquals("Unexpected second pattern", "Mozilla/1.*", patterns[1]);
                assertEquals("Unexpected third pattern", "Mozilla/3.*", patterns[2]);
                assertEquals("Unexpected fourth pattern", "Mozilla/5.*", patterns[3]);
                assertEquals("Unexpected fifth pattern", "Mozilla/2.*", patterns[4]);
            }
        });
    }

    /**
     * Tests the {@link DeviceRepositoryAccessorManager#setUserAgentPatterns}
     * method
     * @throws Exception if an error occurs
     */
    public void testSetUserAgentPatterns() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                String[] patterns = new String[] {"fred", "jane"};

                manager.setUserAgentPatterns("PC", patterns);

                String[] updated = manager.getUserAgentPatterns("PC");
                assertEquals("Expected 2 user agent patterns ", 2, updated.length);
                assertEquals("Unexpected first pattern", "fred", updated[0]);
                assertEquals("Unexpected second pattern", "jane", updated[1]);
            }
        });
    }

    /**
     * Test to ensure that StandardElements are handled properly. (ie that
     * they are always the last element in of a parent node after the
     * setUserAgentPatterns method has been called).
     *
     * @throws Exception
     */
    public void testSetUserAgentPatternsWithStandardElement() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                JDOMFactory jdomFactory = new ODOMFactory();
                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(), jdomFactory, false);

                String sName = DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME;

                ODOMElement e = (ODOMElement) manager.retrieveDeviceIdentification("PC");
                //create a standard element and add it to the device identification
                ODOMElement standard = (ODOMElement) jdomFactory.element(sName,
                        e.getNamespace());
                e.addContent(standard);

                List children = e.getChildren();
                Object[] childArray = children.toArray();
                // make sure the standard element is that last element
                assertEquals("The standard element is the last child element",
                        sName, ((ODOMElement) childArray[childArray.length - 1]).
                        getName());

                //set the patterns
                String[] patterns = new String[]{"fred", "jane"};
                manager.setUserAgentPatterns("PC", patterns);

                // get the children of the element and start the tests
                children = e.getChildren();
                childArray = children.toArray();

                int count = 0;
                Iterator childIt = children.iterator();
                while (childIt.hasNext()) {
                    ODOMElement ode = (ODOMElement) childIt.next();
                    if (ode.getName().equals(sName)) {
                        count++;
                    }
                }

                // ensure that the context is correct for the test to proceed.
                //(that there is actually one, and only one, standard element
                assertEquals("Only one standard element exists", 1, count);

                // make sure the standard element is that last element
                assertEquals("The standard element is still the last element after "+
                        "setUserAgentPatterns has been called",
                        sName, ((ODOMElement) childArray[childArray.length - 1]).
                        getName());
            }
        });
    }

    /**
     * Tests the {@link DeviceRepositoryAccessorManager#getHeaderPatterns}
     * method
     * @throws Exception if an error occurs
     */
    public void testGetHeaderPatterns() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                DeviceHeaderPattern[] patterns = manager.getHeaderPatterns(
                            "Samsung-SPH-N400");
                assertEquals("Should only be 1 header pattern", 1, patterns.length);

                DeviceHeaderPattern dhp = patterns[0];
                assertEquals("header name not as expected",
                             "Profile",
                             dhp.getName());
                assertNull("header baseDevice not as expected", dhp.getBaseDevice());
                assertEquals("header regular expression not as expected",
                             "http://device\\.sprintpcs\\.com/Samsung/SPH-N400/.*",
                             dhp.getRegularExpression());
            }
        });
    }

    /**
     * Tests the {@link DeviceRepositoryAccessorManager#setHeaderPatterns}
     * method
     * @throws Exception if an error occurs
     */
    public void testSetHeaderPatterns() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                DeviceHeaderPattern[] patterns
                            = new DeviceHeaderPattern[] {
                                new DeviceHeaderPattern("name1", "re1", "baseDevice1"),
                                new DeviceHeaderPattern("name2", "re2", "baseDevice2")
                            };

                manager.setHeaderPatterns("PC", patterns);

                DeviceHeaderPattern[] updated = manager.getHeaderPatterns("PC");
                assertEquals("Expected 2 header patterns ", 2, updated.length);
                assertEquals("unexpected first header name",
                             "name1",
                             updated[0].getName());
                assertEquals("unexpected first baseDevice",
                             "baseDevice1",
                             updated[0].getBaseDevice());
                assertEquals("unexpected first regular expression",
                             "re1",
                             updated[0].getRegularExpression());
                assertEquals("unexpected second header name",
                             "name2",
                             updated[1].getName());
                assertEquals("unexpected second baseDevice",
                             "baseDevice2",
                             updated[1].getBaseDevice());
                assertEquals("unexpected second regular expression",
                             "re2",
                             updated[1].getRegularExpression());


                patterns = new DeviceHeaderPattern[] {
                                new DeviceHeaderPattern("name1", "re1", "")
                            };

                manager.setHeaderPatterns("PC", patterns);
                updated = manager.getHeaderPatterns("PC");
                assertEquals("unexpected first baseDevice",
                             null,
                             updated[0].getBaseDevice());

            }
        });
    }

    /**
     * Test to ensure that StandardElements are handled properly. (ie that
     * they are always the last element in of a parent node after calling
     * setHeaderPatterns)
     *
     * @throws Exception
     */
    public void testSetHeaderPatternsWithStandardElement() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                JDOMFactory jdomFactory = new ODOMFactory();
                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(), jdomFactory, false);

                String sName = DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME;

                ODOMElement e = (ODOMElement) manager.retrieveDeviceIdentification("PC");
                // create a standard element
                ODOMElement standard = (ODOMElement) jdomFactory.element(sName,
                        e.getNamespace());
                //add to the element
                e.addContent(standard);

                List children = e.getChildren();
                Object[] childArray = children.toArray();
                assertEquals("The standard element is the last child element",
                        sName, ((ODOMElement) childArray[childArray.length - 1]).
                        getName());

                // set the Header Patterns
                DeviceHeaderPattern[] patterns
                        = new DeviceHeaderPattern[]{
                            new DeviceHeaderPattern("name1", "re1", "baseDevice1"),
                            new DeviceHeaderPattern("name2", "re2", "baseDevice2")
                        };
                manager.setHeaderPatterns("PC", patterns);

                // get the children of the element (it should be modified by here)
                children = e.getChildren();
                childArray = children.toArray();

                int count = 0;
                Iterator childIt = children.iterator();
                while (childIt.hasNext()) {
                    ODOMElement ode = (ODOMElement) childIt.next();
                    if (ode.getName().equals(sName)) {
                        count++;
                    }
                }
                // ensure that the context is correct for the test to proceed
                assertEquals("Only one standard element exists", 1, count);

                assertEquals("The standard element is still the last child element "+
                        "after setHeaderPatterns has been called",
                        sName, ((ODOMElement) childArray[childArray.length - 1]).
                        getName());
            }
        });
    }

    /**
     * This tests the {@link DeviceRepositoryAccessorManager#selectHierarchyDevices(RE)}
     * non-static method.
     */
    public void testSelectHierarchyDevices() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new ODOMFactory(), false);

                // Test with two different regular expressions
                checkSelection(manager.selectHierarchyDevices(getTestRE(false)), false);
                checkSelection(manager.selectHierarchyDevices(getTestRE(true)), true);
            }
        });
    }

    /**
     * This tests the {@link DeviceRepositoryAccessorManager#selectIdentityDevices(RE)}
     * non-static method.
     */
    public void testSelectIdentificationDevices() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new ODOMFactory(), false);

                RE re = new RE("Moz*");
                String selection [] = manager.selectIdentityDevices(re);

                assertNotNull(selection);
                assertTrue(selection.length>0);
                // Ensure PC is in the list.
                boolean foundPC = false;
                for(int i=0; i<selection.length && !foundPC; i++) {
                    foundPC = selection[i].equals("PC");
                }

                assertTrue("Expected to find PC in the selection.", foundPC);
            }
        });
    }

    /**
     * This tests the {@link DeviceRepositoryAccessorManager#selectHierarchyDevices(String, com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory , JDOMFactory, RE)}
     * static method.
     */
    public void testStaticSeletecDevices() throws Exception {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                JDOMFactory jdomFactory = new ODOMFactory();
                TransformerMetaFactory transformerMetaFactory =
                        new TestTransformerMetaFactory();

                // Don't need a test instance for the static test but to test that
                // the static test hasn't altered any state an instance is required
                DeviceRepositoryAccessorManager manager =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(), transformerMetaFactory,
                                jdomFactory, false);

                // Grab the accessor before anything is executed so that it can be
                // used for comparison after each static test
                EclipseDeviceRepository accessor = (EclipseDeviceRepository)
                        PrivateAccessor.getField(manager, "accessor");

                // Test the method
                checkSelection(
                        DeviceRepositoryAccessorManager.selectHierarchyDevices(
                                repository.getPath(), transformerMetaFactory,
                                jdomFactory, getTestRE(false)), false);
                // Ensure the internal accessor is the same
                assertSame("Accessor should not have been modified",
                           accessor,
                           PrivateAccessor.getField(manager, "accessor"));

                // Test the method
                checkSelection(
                        DeviceRepositoryAccessorManager.selectHierarchyDevices(
                                repository.getPath(), transformerMetaFactory,
                                jdomFactory, getTestRE(true)), true);
                // Ensure the internal accessor is the same
                assertSame("Accessor should not have been modified",
                           accessor,
                           PrivateAccessor.getField(manager, "accessor"));
            }
        });
    }

    /**
     * A utility method that provides a known regular expression for testing
     * purposes.  This method can be used in conjunction with
     * {@link #checkSelection} where the <code>moreSpecific</code> parameter
     * has a similar meaning.
     *
     * @param moreSpecific True if the regular expression should be more
     *                     specific in what it matches (and thus match fewer
     *                     items).  False otherwise.
     * @return             A newly initialised regular expression.
     */
    private RE getTestRE(boolean moreSpecific) throws Exception {
        RE expression = null;
        if (moreSpecific) {
            expression = new RE("^[Pp][Cc].*Mozilla$");
        } else {
            expression = new RE("^[Pp][Cc]");
        }
        return expression;
    }

    /**
     * A utility method that checks a String array against known expected
     * values.  This method can be used in conjunction with {@link #getTestRE}
     * where the <code>moreSpecific</code> parameter has a similar meaning.
     *
     * @param actual       The array to check against the expected values
     * @param moreSpecific True if the array should be checked against more
     *                     specific expected values, false otherwise.
     */
    private void checkSelection(String[] actual,
                                boolean moreSpecific) throws Exception {
        String[] specificExpected = new String[] {
            "PC-Unix-Mozilla",
            "PC-Win32-Mozilla"
        };

        String[] expected = new String[] {
            "PC",
//            "PC-MacOS",
            "PC-UNIX",
            "PC-Win32",
//            "PC-MacOS-IE",
//            "PC-MacOS-Netscape",
//            "PC-MacOS-Omniweb",
//            "PC-MacOS-Safari",
//            "PC-UNIX-Galeon",
            "PC-UNIX-Konqueror",
//            "PC-UNIX-Lynx",
//            "PC-UNIX-Nautilus",
//            "PC-UNIX-Netscape",
            "PC-UNIX-Opera",
            "PC-Unix-Mozilla",
            "PC-Win32-Mozilla",
            "PC-Win32-Netscape",
//            "PC-Win32-Opera",
//            "PC-Win32-Other",
            "PC-Win32-IE",
//            "PC-MacOS-IE5",
//            "PC-MacOS-Netscape4",
//            "PC-UNIX-Galeon-1.3",
            "PC-UNIX-Konqueror-2",
            "PC-UNIX-Konqueror-3",
//            "PC-UNIX-Netscape4",
//            "PC-UNIX-Netscape6",
//            "PC-UNIX-Netscape7",
//            "PC-UNIX-Opera4",
//            "PC-UNIX-Opera5",
//            "PC-UNIX-Opera6",
            "PC-UNIX-Opera7",
//            "PC-Win32-Netscape3",
//            "PC-Win32-Netscape4",
//            "PC-Win32-Netscape6",
            "PC-Win32-Netscape7",
//            "PC-Win32-Opera3",
//            "PC-Win32-Opera4",
//            "PC-Win32-Opera5",
//            "PC-Win32-Opera6",
//            "PC-Win32-Opera7",
//            "PC-Win32-Oligo",
//            "PC-Win32-IE4",
//            "PC-Win32-IE5",
//            "PC-Win32-IE6",
//            "PC-Win32-IE5.5"
        };

        if (moreSpecific) {
            assertTrue("Actual " + new ArrayObject(actual) + " and specific " +
                    "expected " + new ArrayObject(specificExpected) + "arrays " +
                    "should match",
                       Arrays.equals(actual, specificExpected));
        } else {
            assertTrue("Actual " + new ArrayObject(actual) + " and expected " +
                    new ArrayObject(expected) + "arrays should match",
                       Arrays.equals(actual, expected));
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 17-Jan-05	6697/1	philws	VBM:2005011401 Fix NPE when re-creating just deleted device

 11-Jan-05	6646/1	allan	VBM:2005010403 Use URLs for asset paths and don't write empty baseDevice attrs

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 11-Oct-04	5557/5	allan	VBM:2004070608 Search unit tests

 08-Oct-04	5557/3	allan	VBM:2004070608 Unit tests and rework issues

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 27-Aug-04	5315/3	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 11-Aug-04	5126/4	adrian	VBM:2004080303 Added GUI support for Device TACs

 11-Aug-04	5126/2	adrian	VBM:2004080303 Added GUI support for Device TACs

 06-Aug-04	5088/1	byron	VBM:2004080301 Public API for device lookup: getDeviceNameByUAProfURL JDBC&XML

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 07-Jul-04	4822/1	claire	VBM:2004070606 Allow devices to be selected using a regular expression

 03-Jun-04	4532/1	byron	VBM:2004052104 ASCII-Art Image Asset Encoding

 12-May-04	4309/3	matthew	VBM:2004051112 DeviceRepositoyAccessorManager.setUserAgentPatterns and .setHeaderPatterns modified to keep standard elements as the last children of the element

 12-May-04	4309/1	matthew	VBM:2004051112 DeviceRepositoyAccessorManager.setUserAgentPatterns and .setHeaderPatterns modified to keep standard elements as the last children of the element

 11-May-04	4161/2	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 21-Apr-04	3909/7	pcameron	VBM:2004031004 Refactored the iterators

 20-Apr-04	3909/5	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 19-Apr-04	3904/1	allan	VBM:2004020903 Support localized device policy categories

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 07-Apr-04	3774/3	pcameron	VBM:2004040705 Some tweaks

 07-Apr-04	3774/1	pcameron	VBM:2004040705 DeviceRepositoryAccessorManager#resolvePolicy(String, String) uses the <inherit /> tag if present

 23-Mar-04	3546/1	pcameron	VBM:2004031102 Added getPolicyNames to DeviceRepositoryAccessorManager

 18-Feb-04	3060/1	philws	VBM:2004021701 Implement runtime device repository accessor

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
