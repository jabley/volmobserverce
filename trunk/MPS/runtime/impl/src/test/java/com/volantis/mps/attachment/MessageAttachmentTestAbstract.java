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

package com.volantis.mps.attachment;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mps.message.MessageException;

import java.io.File;

/**
 * This is the root of the MessageAttachment test hierarchy.  Tests are
 * provided at this level for functionality within MessageAttachment using
 * test instances provided by the concrete subclasses.
 */
public abstract class MessageAttachmentTestAbstract extends TestCaseAbstract {

    /**
     * Temporary filename prefix
     */
    protected final static String TMP_FILE_PREFIX = "TestFile";

    /**
     * Local file URL
     */
    protected static String LOCAL_FILE_URL;

    /**
     * A valid value that can be used for the test instances.
     */
    protected static String VALUE;

    /**
     * A valid mime type that can be used for the test instances.
     */
    protected static final String MIME = "text/plain";

    /**
     * A valid value type that can be used for the test instances.
     */
    protected static final int VALUE_TYPE = MessageAttachment.FILE;

    /**
     * Initialise a new instance of this test case.
     */
    public MessageAttachmentTestAbstract() {

    }

    /**
     * Initialise a new named instance of this test case.
     *
     * @param s The name of this test case.
     */
    public MessageAttachmentTestAbstract(String s) {
        super(s);
    }

    // JavaDoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        // create a temporary file, use the pathname and then delete
        File localFile = File.createTempFile(TMP_FILE_PREFIX, null);
        VALUE = localFile.getAbsolutePath();

        // create a local file URL using same method
        File urlFile = File.createTempFile(TMP_FILE_PREFIX, null);
        LOCAL_FILE_URL = urlFile.toURI().toURL().toString();


    }

    // JavaDoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();

        (new File(VALUE)).delete();
        (new File(LOCAL_FILE_URL)).delete();
    }

    /**
     * This tests the creation of MessageAttachment objects.
     */
    public void testMessageAttachmentCreation() throws Exception {
        // Test valid, but undefined
        MessageAttachment ma = getTestInstance();

        // Test valid, and complete
        ma = getTestInstance(VALUE, MIME, VALUE_TYPE);

        // Test invalid - value
        try {
            ma = getTestInstance(null, MIME, VALUE_TYPE);
            fail("Previous call should have caused an exception (1)");
        } catch (MessageException me) {
            // Test success
        }

        // Test invalid - mime type
        try {
            ma = getTestInstance(VALUE, null, VALUE_TYPE);
            fail("Previous call should have caused an exception (2)");
        } catch (MessageException me) {
            // Test success
        }

        // Test invalid - value type
        try {
            ma = getTestInstance(VALUE, MIME, 5);
            fail("Previous call should have caused an exception (3)");
        } catch (MessageException me) {
            // Test success
        }
    }

    /**
     * This tests the {set|get}Value methods.
     */
    public void testValues() throws Exception {
        final String anotherFile = "file:///another.test.file";

        MessageAttachment ma = getTestInstance(VALUE, MIME, VALUE_TYPE);

        // Get value and check it is as it should be
        String retrieved = ma.getValue();
        assertEquals("Values should match", VALUE, retrieved);

        // Set an invalid value
        try {
            ma.setValue(null);
            fail("Previous call should have caused an exception");
        } catch (MessageException me) {
            // Test success
        }

        // Set an invalid value - i.e. can't be read
        try {
            ma.setValue(anotherFile);
        } catch (MessageException me) {
            // test success
        }

        // Get value and check it is as it should be
        retrieved = ma.getValue();
        assertNotSame("Values should not be equal", anotherFile, retrieved);
    }

    /**
     * This tests the {set|get}MimeType methods.
     */
    public void testMimeTypes() throws Exception {
        final String anotherMime = "text/html";

        MessageAttachment ma = getTestInstance(VALUE, MIME, VALUE_TYPE);

        // Get value and check it is as it should be
        String retrieved = ma.getMimeType();
        assertEquals("Values should match", MIME, retrieved);

        // Set an invalid value
        try {
            ma.setMimeType(null);
            fail("Previous call should have caused an exception");
        } catch (MessageException me) {
            // Test success
        }

        // Set a valid value
        ma.setMimeType(anotherMime);

        // Get value and check it is as it should be
        retrieved = ma.getMimeType();
        assertEquals("Values should match", anotherMime, retrieved);
    }

    /**
     * This tests the {set|get}ValueType methods.
     */
    public void testValueTypes() throws Exception {
        final int anotherValueType = MessageAttachment.URL;

        MessageAttachment ma = getTestInstance(VALUE, MIME, VALUE_TYPE);

        // Get value and check it is as it should be
        int retrieved = ma.getValueType();
        assertEquals("Values should match", VALUE_TYPE, retrieved);

        // Set an invalid value
        try {
            ma.setValueType(5);
            fail("Previous call should have caused an exception");
        } catch (MessageException me) {
            // Test success
        }

        // Set a valid value
        ma.setValueType(anotherValueType);

        // Get value and check it is as it should be
        retrieved = ma.getValueType();
        assertEquals("Values should match", anotherValueType, retrieved);
    }

    /**
     * This tests equality of MessageAttachment instances
     */
    public void testMessageAttachmentEquality() throws Exception {

        // Test instances
        final Object emptyTestInstance = getTestInstance();
        final Object initTestInstance = getTestInstance(VALUE,
                                                        MIME,
                                                        VALUE_TYPE);

        // Check equality methods
        assertEquals("Message attachments should be equal (1)",
                     emptyTestInstance,
                     emptyTestInstance);

        assertEquals("Message attachments should be equal (2)",
                     initTestInstance,
                     initTestInstance);

        assertTrue("Message attachments should not be equal (1)",
                   !emptyTestInstance.equals(initTestInstance));

        assertTrue("Message attachments should not be equal (2)",
                   !initTestInstance.equals(emptyTestInstance));

        assertTrue("Message attachments should not be equal (2)",
                   !initTestInstance.equals(
                           getTestInstance(LOCAL_FILE_URL,
                                           "text/plain",
                                           MessageAttachment.URL)));
    }

    /**
     * Create a valid instance of a MessageAttachment for use in tests.  This
     * expects the default constructor to be used.
     *
     * @return An initialised and usable MessageAttachment instance.
     */
    public abstract MessageAttachment getTestInstance();

    /**
     * Create a valid instance of a MessageAttachment based on the parameters
     * provided for use in tests.  This expects the paramterised constructor
     * to be used.
     *
     * @param value     The URL or file that points to a resource to use
     *                  as a location.  May not be null
     * @param mimeType  The mimetype of the resource.  May not be null
     * @param valueType Must be either MessageAttachment.VALUE or
     *                  MessageAttachment.URL depending on resource type
     *
     * @return An initialised and usable MessageAttachment instance.
     */
    public abstract MessageAttachment getTestInstance(String value,
                                                      String mimeType,
                                                      int valueType)
            throws MessageException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-May-05	693/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 05-May-05	671/7	amoore	VBM:2005050315 Updated Unit tests to use local URL rather than remote

 05-May-05	671/5	amoore	VBM:2005050315 Updated attachment file check logic and maintained coding standards

 05-May-05	671/3	amoore	VBM:2005050315 Removed javax.swing.* imports

 05-May-05	671/1	amoore	VBM:2005050315 Added file check for message attachments to ensure they are valid

 24-Sep-04	142/3	claire	VBM:2004070806 Correct invalid attachment type on PublicAPI DeviceMessageAttachment

 ===========================================================================
*/
