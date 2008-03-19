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

package com.volantis.mps.servlet;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mps.message.store.MessageStoreConfig;
import com.volantis.mps.message.store.MessageStoreException;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.testtools.stubs.HttpServletRequestStub;
import com.volantis.testtools.stubs.HttpServletResponseStub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.SAXException;

/**
 * This provides a unit test of some of the functionality of the
 * <code>MessageStoreServlet</code> class.  The functioning of this code as
 * a servlet is tested in the integration test
 * {@link MessageStoreServletTestCase} (hence the <strong>Unit</strong> in
 * this class name.
 */
public class MessageStoreServletUnitTestCase extends TestCaseAbstract {

    /**
     * The default number of entries to generate for test data.
     */
    protected static final int NUM_ENTRIES = 100;


    /**
     * Initialise a new instance of this test case.
     */
    public MessageStoreServletUnitTestCase() {
    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of the test case.
     */
    public MessageStoreServletUnitTestCase(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        // The MESSAGE_STORE_DIR is deleted at each point it is used so that
        // there is no dependency between tests by the same files possibly
        // existing.  However to ensure it is really deleted the same delete
        // call is made here.
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        super.tearDown();
    }

    /**
     * This tests the generation of IDs.  Ideally this would be some
     * mathematical analysis of the algorithm but given the time constraints
     * (and lack of formal reasoning theory!) this ensures uniqueness for
     * a small sample by using a Set which does not allow duplicate values.
     */
    public void testGenerateID() throws Exception {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setIdSize(12);

        Set ids = new HashSet();
        boolean success = false;

        // Generate a range of values and ensure uniqueness with Set
        for (int i = 0; i < NUM_ENTRIES; i++) {
            String id = servlet.generateID();
            success = ids.add(id);
            assertTrue("IDs should be unique (id generation " + i + ")",
                       success);
        }
    }

    /**
     * Test the validation of an ID when it exists on disk and in memory
     * and also when it does not exist.
     */
    public void testCheckValidID() throws Exception {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setIdSize(12);
        servlet.globalConfig.setLocation(
                MessageStoreTestHelper.MESSAGE_STORE_LOCATION);
        servlet.messageStoreIDs = new HashMap();

        String testID = "1234567890ab";
        assertTrue("ID should not be in use", servlet.checkValidID(testID));

        // Add to filesystem to cause an invalid response
        MessageStoreTestHelper.createDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);
        File file = servlet.createEntryFile(testID);
        file.createNewFile();
        file.deleteOnExit();
        assertFalse("ID should be in use (1)", servlet.checkValidID(testID));

        // Check added to message store
        assertNotNull("Should have been added to the in-memory message store",
                      servlet.messageStoreIDs.get(testID));

        // Clean message store
        servlet.messageStoreIDs = new HashMap();

        // Add to message store to cause an invalid response
        servlet.messageStoreIDs.put(testID, servlet.generateTimeStamp(null));
        assertFalse("ID should be in use (2)", servlet.checkValidID(testID));

        // Tidy up filesystem
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);
    }

    /**
     * Test the generation of a valid ID which is a combination of the
     * generation of an ID and checking it is valid.
     */
    public void testGenerateValidID() throws Exception {
        // To exhaust all combinations would be hard (!) so this tests that
        // the method returns valid IDs in normal use!

        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setLocation(MessageStoreTestHelper.TMPDIR);
        servlet.globalConfig.setIdSize(12);
        servlet.messageStoreIDs = new HashMap();

        Set ids = new HashSet();
        boolean success = false;

        // Generate a range of values and ensure uniqueness with Set and
        // that each is valid (the method call does that) and there is no
        // exhaustion of IDs.
        for (int i = 0; i < NUM_ENTRIES; i++) {
            String id = servlet.generateValidID();
            success = ids.add(id);
            assertTrue("IDs should be unique (id generation " + i + ")",
                       success);
        }

    }

    /**
     * Create a file reference to the file provided and ensure it matches that
     * expected.
     */
    public void testCreateEntryFile() {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setLocation(MessageStoreTestHelper.TMPDIR);

        String id = "abcdef123456";
        String expected = MessageStoreTestHelper.TMPDIR +
                System.getProperty("file.separator") +
                id +
                MessageStoreServlet.STORED_XML_EXTENSION;
        File file = servlet.createEntryFile(id);
        assertNotNull("File should have been created", file);
        assertEquals("Absolute path should match expected",
                     expected,
                     file.getAbsolutePath());
    }

    /**
     * Generate timestamps and ensure they are as expected within a certain
     * amount of millisecond tolerance.
     */
    public void testGenerateTimeStamp() throws Exception {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setLocation(MessageStoreTestHelper.TMPDIR);

        String id = "abcdef123456";

        // Test with null file
        Date timestamp = servlet.generateTimeStamp(null);
        assertNotNull("Date should exist", timestamp);

        // Test with a temporary file with expected time/date
        File testFile =
                File.createTempFile(id,
                                    MessageStoreServlet.STORED_XML_EXTENSION);
        // Should be done by the #createTempFile call but just to be safe...
        testFile.deleteOnExit();

        // Create the expected timestamp and set the file modified time to
        // be the same
        long time = System.currentTimeMillis() / 2;
        testFile.setLastModified(time);
        Date expected = new Date(time);

        // Retrieve the timestamp
        timestamp = servlet.generateTimeStamp(testFile);

        // This compares the long times less than a given boundary as comparing
        // the two dates can differ by a few milliseconds each time.
        assertTrue("Timestamp should match that expected",
                   (expected.getTime() - timestamp.getTime()) < 1000);

    }

    /**
     * Test the initialization of the message store from some fake entries.
     */
    public void testInitializeMessageStore() throws Exception {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setLocation(
                MessageStoreTestHelper.MESSAGE_STORE_LOCATION);

        // Create a test message store directory
        MessageStoreTestHelper.createDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        // Check the message store exists to prevent erroneous test results
        // just because this step of setup failed
        boolean createdOK = MessageStoreTestHelper.MESSAGE_STORE_DIR.exists();
        assertTrue("Temp message store should have been created", createdOK);

        File creation = null;
        boolean created = false;

        // Create some sample files
        for (int i = 0; i < NUM_ENTRIES; i++) {
            creation = servlet.createEntryFile(Integer.toString(i));
            created = creation.createNewFile();
            assertTrue("Test file " + i + " should have been created", created);
        }

        servlet.initializeMessageStore();

        assertNotNull("In-memory message store should exist",
                      servlet.messageStoreIDs);
        assertEquals("Should be " + NUM_ENTRIES + " + entries",
                     NUM_ENTRIES,
                     servlet.messageStoreIDs.keySet().size());

        // Tidy up any entry files to prevent dependency on each test.
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);
    }

    /**
     * This tests the construction of the configuration object for MSS from
     * the xml config file.
     */
    public void testReadConfiguration() throws Exception {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Test data
        final String tempPath = "/tmp/some/path";
        String validContents =
                "<?xml version='1.0' encoding='UTF-8'?>" +
                "<messageStoreServer\n" +
                "    xmlns=\"http://www.volantis.com/xmlns/mss/config\"\n" +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "    xsi:schemaLocation=\"http://www.volantis.com/xmlns/mss/config http://www.volantis.com/schema/config/v1.0/mss-config.xsd\"\n" +
                ">\n" +
                "\n" +
                "    <environment log4jConfigurationFile=\"" + tempPath + "\"/>\n" +
                "\n" +
                "    <message-store\n" +
                "        location=\"" + tempPath + "\"\n" +
                "        timeout=\"unlimited\"\n" +
                "        id-size=\"12\"\n" +
                "        validate=\"false\"/>\n" +
                "\n" +
                "</messageStoreServer>\n";

        // Test a non-existant file
        try {
            servlet.readConfiguration("nofile");
            fail("Should have caused an exception");
        } catch (ServletException se) {
            // Test succeeded!
        }

        // Create a valid test file
        File validConfig = new File(MessageStoreTestHelper.TMPDIR,
                                    "test-mms-valid-config.xml");
        validConfig.createNewFile();
        validConfig.deleteOnExit();
        FileWriter writer = new FileWriter(validConfig);
        writer.write(validContents);
        writer.flush();
        writer.close();

        // Obtain the config object from the config file
        MessageStoreConfig created = servlet.readConfiguration(
                validConfig.getAbsolutePath());

        // Test success...
        assertEquals("Location should be as expected",
                     tempPath,
                     created.getLocation());
        assertEquals("Timeout should be unlimited",
                     true,
                     created.isUnlimitedTimeout());
        assertEquals("ID sizes should be equal", 12, created.getIdSize());
        assertEquals("Validate should be false", false, created.isValidate());

    }

    /**
     * This tests the daemon task that is created to clean the message store.
     * <p>
     * Whilst this would be initialised and potentially run in a servlet
     * integration test this unit test allows for specific files/timestamps
     * to be constructed and to speed up the cleaning time (significantly).
     */
    public void testDaemonTidyTask() throws Exception {
        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet();

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setLocation(
                MessageStoreTestHelper.MESSAGE_STORE_LOCATION);
        servlet.globalConfig.setIdSize(12);
        servlet.globalConfig.setTimeout(300);
        servlet.messageStoreIDs = new HashMap();

        // Create some test data with 50% of items that need cleaning
        MessageStoreTestHelper.createDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        for (int i = 0; i < NUM_ENTRIES; i++) {
            String id = servlet.generateValidID();
            File testFile = servlet.createEntryFile(id);
            testFile.createNewFile();
            testFile.deleteOnExit();
            Date timestamp = servlet.generateTimeStamp(testFile);
            if (i % 2 == 0) {
                // For every other entry adjust the timestamp to be old
                long adjustedTime = timestamp.getTime() / 2;
                timestamp = new Date(adjustedTime);
                testFile.setLastModified(adjustedTime);
            }
            servlet.messageStoreIDs.put(id, timestamp);
        }

        assertEquals("Should be " + NUM_ENTRIES + " in the memory cache",
                     NUM_ENTRIES,
                     servlet.messageStoreIDs.keySet().size());

        // Create the timer and task
        Timer timer = new Timer();
        TimerTask task = servlet.createCleaningDaemonTask();

        // Repeat time of 10 seconds...
        int repeat = 10000;
        // Delay of 1 second...
        int delay = 1000;

        // Schedule the timer task
        timer.schedule(task, delay, repeat);

        // Wait to ensure the cleanup can have occurred
        Thread.sleep(repeat);

        // Cancel the timer task
        task.cancel();

        // Test success - i.e. 50% of items have been removed
        int expectedEntries = NUM_ENTRIES / 2;
        assertEquals("Message store should contain " + expectedEntries,
                     expectedEntries,
                     servlet.messageStoreIDs.keySet().size());
        File[] entries = MessageStoreTestHelper.MESSAGE_STORE_DIR.listFiles();
        assertEquals("Filesystem should contain " + expectedEntries,
                     expectedEntries,
                     entries.length);

        // Tidy up
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);
    }

    /**
     * This tests the storing of some XML, and then using the id generated
     * the retrieval of that same XML.
     */
    public void testStoreAndRetrieve() throws Exception {
        final String testXML =
                "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<root>\n" +
                "    <sample-element attr=\"3\">\n" +
                "        Some text here\n" +
                "    </sample-element>\n" +
                "    <another-element>\n" +
                "        <nested-element/>\n" +
                "    </another-element>\n" +
                "    <and-yet-another/>\n" +
                "</root>";

        // Create a test instance
        MessageStoreServlet servlet = new MessageStoreServlet() {
            public String retrievedXML = null;
            // JavaDoc inherited
            protected void processXML(HttpServletRequest request,
                                      HttpServletResponse response,
                                      InputStream message)
                    throws IOException, MarinerContextException, SAXException {
                // Don't want to test the entire MCS/Pipeline stuff here (!)
                // so this just compare the retrieved XML with the initial
                // test data - they should match
                // @TODO - sort this test out
                //assertEquals("XML should match", testXML, message);
            }
            public String getXML() {
                return retrievedXML;
            }
        };

        // Configure the config object as required (init() will not have run)
        servlet.globalConfig = new MessageStoreConfig();
        servlet.globalConfig.setLocation(
                MessageStoreTestHelper.MESSAGE_STORE_LOCATION);
        servlet.globalConfig.setIdSize(12);
        servlet.messageStoreIDs = new HashMap();

        // Test an invalid store (file does not exist - delete message store)
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        try {
            servlet.store(testXML, null);
            fail("Should have caused a store exception");
        } catch (MessageStoreException mse) {
            // Test succeeded
        }

        // Ensure the store area exists
        MessageStoreTestHelper.createDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);

        // Store the XML using platform default encoding
        String id = servlet.store(testXML, null);

        // Create a request
        HttpServletRequest request = new HttpServletRequestStub();

        // Create a response
        HttpServletResponse response = new HttpServletResponseStub() {
            // JavaDoc inherited
            public void resetBuffer() {
            }
        };

        // And then retrieve it (retrieval asserts test succeeded)
        servlet.retrieve(id, request, response);

        // Tidy up
        MessageStoreTestHelper.deleteDir(
                MessageStoreTestHelper.MESSAGE_STORE_DIR);
    }

    public void testSetXMLProlog() throws Exception {
        // Create a test instance
        final MessageStoreServlet servlet = new MessageStoreServlet() {
            public String setXMLProlog(String msg, String enc) {
                return super.setXMLProlog(msg, enc);
            }
        };

        final String encoding = "UTF-8";

        final String encXML =
                "<?xml version='1.0' encoding='UTF-8' ?>\n" +
                "<root></root>";

        final String dqNoEncXML =
                "<?xml version=\"1.0\"?>\n" +
                "<root></root>";

        final String sqNoEncXML =
                "<?xml version='1.0'?>\n" +
                "<root></root>";

        final String noPrologXML =
                "<root></root>";

        // note the use of two spaces after encoding='...'
        // todo: fix space issues after writing encoding attr to prolog
        assertTrue(getProlog(
                servlet.setXMLProlog(encXML, encoding)).equals(
                        "<?xml version='1.0' encoding='UTF-8' ?>"));
        assertTrue(getProlog(
                servlet.setXMLProlog(dqNoEncXML, encoding)).equals(
                        "<?xml encoding=\"UTF-8\"  version=\"1.0\"?>"));
        assertTrue(getProlog(
                servlet.setXMLProlog(sqNoEncXML, encoding)).equals(
                        "<?xml encoding='UTF-8'  version='1.0'?>"));
        assertTrue(getProlog(
                servlet.setXMLProlog(noPrologXML, encoding)).equals(
                        "<?xml version='1.0' encoding='UTF-8' ?>"));
    }

    /**
     * Utility method to get the declaration from an XML string
     *
     * @param xml   XML with prolog
     * @return  The prolog in the XML or <code>null</code> if no prolog present
     */
    private String getProlog(String xml) {
        // remove any whitespace from front and end
        xml = xml.trim();

        if (!(xml.startsWith("<?xml"))) {
            return null;
        }

        return xml.substring(xml.indexOf("<?xml"), (xml.indexOf("?>") + 2));

    }

    // Integration test handles:
    // init, destroy, doPost, doGet

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/5	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 19-Oct-04	208/1	matthew	VBM:2004101315 Make timeout value work in seconds rather then milliseconds

 13-Aug-04	155/1	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
