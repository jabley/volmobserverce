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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/attachment/MessageAttachmentsTestCase.java,v 1.3 2003/03/17 12:30:22 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Dec-02    Sumit           VBM:2002112602 - Created to test message 
 *                              attachments
 * 25-Feb-03    Mat             VBM:2003022002 - Changed to use the 
 *                              BeanInitialiser to start and configure the bean
 * 17-Mar-03    Geoff           VBM:2003031403 - Removed unused/dead imports.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.attachment;

import java.util.Iterator;
import java.io.File;

import com.volantis.mps.message.MessageException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.repository.RepositoryException;


/**
 * This tests the <code>MessageAttachments<code> implementation.
 */
public class MessageAttachmentsTestCase extends TestCaseAbstract {

    /**
     * Temporary filename prefix
     */
    protected final static String TMP_FILE_PREFIX = "TestFile";

    /**
     * Array of real files to be created in java.io.tmpdir for use
     * in testing
     */
    protected File[] testFiles;

    /**
     * Initialize a new named instance of this test case.
     *
     * @param testName The name of the test.
     */
    public MessageAttachmentsTestCase(String testName) {
        super(testName);
    }

    private MessageAttachments attachments;
    private DeviceMessageAttachment one;
    private DeviceMessageAttachment two;
    private DeviceMessageAttachment three;

    // JavaDoc inherited
    public void setUp() throws Exception {
        super.setUp();

        testFiles = new File[3];

        for (int i = 0; i < testFiles.length; i++) {
            testFiles[i] = File.createTempFile(TMP_FILE_PREFIX, null);
        }

        // create attachments
        createAttachments();

    }

    // Create the test data required
    protected void createAttachments() throws Exception {
        one = new DeviceMessageAttachment(testFiles[0].getAbsolutePath(),
                                          "text/plain",
                                          MessageAttachment.FILE,
                                          "one",
                                          "one");
        // device two is a child of one so both attachments must apper when we
        // ask for device two                                        
        two = new DeviceMessageAttachment(testFiles[1].getAbsolutePath(),
                                          "text/plain",
                                          MessageAttachment.FILE,
                                          "two",
                                          "two");
        three = new DeviceMessageAttachment(testFiles[2].getAbsolutePath(),
                                            "text/html",
                                            MessageAttachment.FILE,
                                            "three",
                                            "two");
        attachments = createTestMessageAttachments();
        attachments.addAttachment(one);
        attachments.addAttachment(two);
        attachments.addAttachment(three);
    }

    // JavaDoc inherited
    public void tearDown() throws Exception {
        super.tearDown();
        attachments = null;

        // remove temporary files
        for (int i = 0; i < testFiles.length; i++) {
            testFiles[i].delete();
        }
    }

    /**
     * Test the retrieval of attachments for a specific device.
     */
    public void testGetAttachmentsForDevice() throws Exception {
        MessageAttachments sameDevice =
                attachments.getAttachmentsForDevice("two");
        Iterator itr = sameDevice.iterator();
        if (itr.hasNext()) {
            DeviceMessageAttachment attach =
                    (DeviceMessageAttachment) itr.next();
            assertEquals("First attachment for device two is not " +
                         "the expected object. ",
                         one,
                         attach);
        } else {
            fail("First attachment for device two is null");
        }
        if (itr.hasNext()) {
            DeviceMessageAttachment attach =
                    (DeviceMessageAttachment) itr.next();
            assertEquals("Second attachment for device two is not " +
                         "the expected object. ",
                         two,
                         attach);
        } else {
            fail("Second attachment for device two is null");
        }
    }

    /**
     * Test the retrieval of attachments for a specific channel.
     */
    public void testGetAttachmentsForChannel() throws Exception {
        MessageAttachments sameChannel =
                attachments.getAttachmentsForChannel("two");
        Iterator itr = sameChannel.iterator();
        assertTrue("First attachment for channel two is not " +
                   "the expected object",
                   two == itr.next());
        assertTrue("Second attachment for channel two is not " +
                   "the expected object",
                   three == itr.next());
    }

    /**
     * Create a customized version of {@link MessageAttachments} for the
     * purposes of this testing.  The reason this is done is that so these
     * simple test cases do not require the entire Volantis bean to be
     * created and initialized and a fake repository created...
     *
     * @return An initialized and testable version of {@link MessageAttachments}
     *         which can be used for testing without the need to rely on MCS
     */
    protected MessageAttachments createTestMessageAttachments() {
        return new MessageAttachments() {
            // JavaDoc inherited
            protected boolean checkDeviceAncestry(DeviceMessageAttachment dma,
                                                  String deviceName)
                    throws RepositoryException, MessageException {
                // This can return true for these tests because the dma
                // device will always be an ancestor of the device specified
                return true;
            }
        };
    }
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

 24-Sep-04	140/3	claire	VBM:2004070704 Fixing testcases and replacing some hypersonic usage with stubs

 22-Sep-04	140/1	claire	VBM:2004070704 Tidying up Transforce use in the MimeMessageAssembler and fixing testcases

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 24-Oct-03	45/5	mat	VBM:2003101502 Remove unnecessary try/catch block

 24-Oct-03	45/3	mat	VBM:2003101502 Rework tests to use AppManager properly

 23-Oct-03	45/1	mat	VBM:2003101502 Rework tests to use AppManager and generally tidy them up

 ===========================================================================
*/
