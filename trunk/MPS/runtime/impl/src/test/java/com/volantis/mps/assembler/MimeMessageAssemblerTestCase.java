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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/assembler/MimeMessageAssemblerTestCase.java,v 1.4 2003/01/17 15:20:54 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Dec-02    Byron           VBM:2002103009 - Created
 * 09-Dec-02    Sumit           VBM:2002110104 - Added testAssembleMessage that
 *                              tests assembly of a message consisting of
 *                              attachments of type text/plain.
 * 11-Dec-02    Ian             VBM:2002092305 - Added test for normal assets
 *                              remove MyMessageAssembler as it is not needed.
 * 17-Jan-03    Ian             VBM:2003010708 - Changed attachment disposition
 *                              to inline.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;

import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.message.MessageAsset;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.ProtocolIndependentMessage;
import com.volantis.testtools.server.HTTPServer;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.OutputStream;
import java.io.IOException;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.BodyPart;
import javax.mail.MessagingException;

import junit.framework.AssertionFailedError;

/**
 * This class does provides unit test cases for testing the
 * <code>MimeMessageAssembler</code> class
 */
public class MimeMessageAssemblerTestCase extends TestCaseAbstract {

    /**
     * Initialise a new named instance of this test case.
     *
     * @param testName The name of the test case
     */
    public MimeMessageAssemblerTestCase(String testName) {
        super(testName);
    }

    /**
     * Test the assembly of messages
     */
    public void xtestAssembleMessage() throws Exception {

        HTTPServer assetServer = new HTTPServer(9999, 3);
        assetServer.start();

        MimeMessageAssembler mimeMessageAssembler = new MimeMessageAssembler() {
            protected void checkMessageConstraints(ArrayList imageAssets,
                                                   Integer maxMessageSize,
                                                   Integer maxImageSize,
                                                   int actualMessageSize)
                    throws MessageException {
                // Do nothing - assume the constraints are never exceeded
                // This is intentional as this test is not for constraints
                // or asset size or ...
            }
        };

        HashMap assetMap = new HashMap();
        assetMap.put("mimeref", new MessageAsset("Test Message"));

        MessageAsset asset1 = new MessageAsset(MessageAsset.ON_SERVER,
                "http://127.0.0.1:9999/test.gif");
        MessageAsset asset2 = new MessageAsset(MessageAsset.ON_DEVICE,
                "http://127.0.0.1:9999/test.gif");
        assetMap.put("asset1", asset1);
        assetMap.put("asset2", asset2);

        ProtocolIndependentMessage mesg = new ProtocolIndependentMessage(
                "Hello", "text/plain", assetMap);

        MimeMultipart mimeMultipart = (MimeMultipart)
                mimeMessageAssembler.assembleMessage(mesg,
                                                     new MessageAttachments());

        // This test fails intermittently. It seems to be a flushing /
        // synchronisation issue with JavaMail. If it persists with the current
        // version and, then it's advisable to comment out the test until
        // JavaMail is fixed.
        assertEquals("Number of parts should match",
                3, mimeMultipart.getCount());

        // These tests rely on the ordering of values which differs between
        // JDK 1.3 and 1.4 WRT to the attachments of assets.  It seems to be
        // safe to assume that the main content is first...

        // Testing the Hello from mesg
        MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(0);
        assertTrue("Message content is different. Was expecting " +
                   "\"Test Message\", got \"" + part.getContent().toString(),
                   "Hello".equals(part.getContent().toString()));

        // Now test for the expected assets - order should not be important
        checkAsset((MimeBodyPart) mimeMultipart.getBodyPart(1));
        checkAsset((MimeBodyPart) mimeMultipart.getBodyPart(2));
    }

    /**
     * A utility method that tests the validity of the given
     * <code>MimeBodyPart</code> as either a text asset or an inline asset.
     * </p>
     * <p>
     * Other tests can be added.  Just be sure to add them here to avoid order
     * dependecies of the {@link MimeMultipart#getBodyPart} method.  The order
     * items are returned differs in JDK 1.3 and 1.4; probably due to the
     * implementation of the collections used.
     * </p>
     *
     * @param part The mime body part to test.
     *
     * @throws Exception If there is a failure in the tests, or a problem
     *                   processing the body part during the tests.
     */
    private void checkAsset(MimeBodyPart part) throws Exception {
        if (!isTextAsset(part) && !isInlineAsset(part)) {
            fail("Attachment failed for part " + part.getContent().toString());
        }
    }

    /**
     * Test whether the body part provided is a text asset of a given specific
     * structure and content.
     *
     * @param part The mime body part to test.
     *
     * @return True if the assertions succeed for the given body part, false
     *         otherwise.
     *
     * @throws Exception If there is a problem testing the body part.
     */
    private boolean isTextAsset(MimeBodyPart part) throws Exception {
        boolean isText = true;
        try {
            // Testing Text Message from the assets
            assertTrue("First attachment is of wrong content type. " +
                       "Was expecting text/plain got " + part.getContentType(),
                       "text/plain".equals(part.getContentType().toString()));
            assertTrue("First attachment text is screwy. " +
                       "Was expecting \"Test Message\" got " + part.getContent(),
                       "Test Message".equals(part.getContent().toString()));
        } catch (AssertionFailedError e) {
            isText = false;
        }
        return isText;
    }

    /**
     * Test whether the body part provided is an inline asset of a given
     * specific structure and content.
     *
     * @param part The mime body part to test.
     *
     * @return True if the assertions succeed for the given body part, false
     *         otherwise.
     *
     * @throws Exception If there is a problem testing the body part.
     */
    private boolean isInlineAsset(MimeBodyPart part) throws Exception {
        boolean isAsset = true;
        try {
            // Testing the ON_SERVER image asset
            if (!part.getDisposition().equals("inline")) {
                fail("Asset has wrong disposition got " + part.getDisposition());
            }
            if (!part.getFileName().equals("asset1")) {
                fail("Asset filename not appended to asset");
            }
        } catch (AssertionFailedError e) {
            isAsset = false;
        }
        return isAsset;
    }

    /**
     * This tests the determination of an asset's size.
     */
    public void testDetermineAssetSize() throws Exception {
        // Some test data
        final String data = "Some test bytes";
        final int size = data.getBytes().length;

        // Create a test instance
        MimeMessageAssembler mimeMessageAssembler = new MimeMessageAssembler();

        // Test a successul calculation with this asset
        BodyPart asset = new MimeBodyPart() {
            // JavaDoc inherited
            public void writeTo(OutputStream outputStream)
                    throws IOException, MessagingException {
                // Have specific test behaviour here to avoid the need to
                // create a fully fledged body part which is not the point of
                // the test here!
                outputStream.write(data.getBytes());
            }
        };

        int actualSize = mimeMessageAssembler.determineAssetSize(asset);
        assertEquals("The sizes should match", size, actualSize);

        // Now test a failure due to not having anything in the asset
        try {
            asset = new MimeBodyPart();
            actualSize = mimeMessageAssembler.determineAssetSize(asset);
            fail("Previous call should have caused an exception (1)");
        } catch (MessageException me) {
            // commented out since the new logging doesn't quite work in
            // test cases at the moment. When fixed this can be uncommented.
//            // This should cause an IO exception which triggers this message
//            // exception
//            assertEquals("The exception message should be the same (1)",
//                         "Could not determine asset size. IO error.",
//                         me.getMessage());
        }

        // And finally a failure due to a messaging exception
        try {
            asset = new MimeBodyPart() {
                // JavaDoc inherited
                public void writeTo(OutputStream outputStream)
                        throws IOException, MessagingException {
                    throw new MessagingException("Intentionally fail here");
                }
            };
            actualSize = mimeMessageAssembler.determineAssetSize(asset);
            fail("Previous call should have caused an exception (2)");
        } catch (MessageException me) {
            // commented out since the new logging doesn't quite work in
            // test cases at the moment. When fixed this can be uncommented.
//            // This should cause an IO exception which triggers this message
//            // exception
//            assertEquals("The exception message should be the same (2)",
//                         "Could not determine asset size. " +
//                         "Error whilst writing message.",
//                         me.getMessage());
        }
    }

    /**
     * This tests the validation of the message constraints.
     */
    public void testCheckMessageConstraints() throws Exception {
        // Create a test instance
        MimeMessageAssembler mimeMessageAssembler = new MimeMessageAssembler();

        // And some test data
        ArrayList assets = new ArrayList();
        Integer maxMessageSize = new Integer(10);
        Integer maxImageSize = new Integer(3);
        int actualSize = 5;

        // Test the constraint checking
        mimeMessageAssembler.checkMessageConstraints(assets,
                                                     maxMessageSize,
                                                     maxImageSize,
                                                     actualSize);
        actualSize = 15;
        try {
            mimeMessageAssembler.checkMessageConstraints(assets,
                                                         maxMessageSize,
                                                         maxImageSize,
                                                         actualSize);
            fail("The message size should have been exceeded");
        } catch (MessageException me) {
            // Success as this exception was expected
        }
    }

    /**
     * This tests correct behaviour of the getFileName(...) method
     */

    public void testGetFileName() throws Exception {
        // create a test instance
        MMAGetFileName mma = new MMAGetFileName();

        // test windoze separator conversion
        String result = mma.getFileName("file.txt", "text/plain");
        assertEquals("File name should be file.txt", "file.txt", result);

        // test no extension
        result = mma.getFileName("file", "text/plain");
        assertEquals("Filename should be file.plain", "file.plain", result);

        // test path + qm
	    result = mma.getFileName("file:///c:\\path\\file.txt?someval=url", "text/plain");
	    assertEquals("Filename should be file.txt", "file.txt", result);

        // test no file path + conversion
        result = mma.getFileName("file:///c:\\path\\nofile\\", null);
        assertEquals("Should return path: file:///c:/path/nofile/",
                     "file:///c:/path/nofile/", result);

        // test url
        result = mma.getFileName("http://www.path-to-file.co.uk/file.txt", "text/plain");
        assertEquals("Should return path: file.txt",
                     "file.txt", result);

        // test extensions from table
        result = mma.getFileName("file.bmp", "image/bmp");
        assertEquals("return should be: file.bmp", "file.bmp", result);

        result = mma.getFileName("file.gif", "image/gif");
        assertEquals("return should be: file.gif", "file.gif", result);

        result = mma.getFileName("file.jpg", "image/jpeg");
        assertEquals("return should be: file.jpg", "file.jpg", result);

        result = mma.getFileName("file.png", "image/png");
        assertEquals("return should be: file.png", "file.png", result);

        result = mma.getFileName("file.tiff", "image/tiff");
        assertEquals("return should be: file.tiff", "file.tiff", result);

        result = mma.getFileName("file.pjpeg", "image/pjpeg");
        assertEquals("return should be: file.pjpeg", "file.pjpeg", result);

        result = mma.getFileName("file.wbmp", "image/vnd.wap.wbmp");
        assertEquals("return should be: file.wbmp", "file.wbmp", result);

        result = mma.getFileName("file.gsm", "audio/gsm");
        assertEquals("return should be: file.gsm", "file.gsm", result);

        result = mma.getFileName("file.midi", "audio/midi" );
        assertEquals("return should be: file.midi", "file.midi", result);

        result = mma.getFileName("file.mp3", "audio/mpeg3");
        assertEquals("return should be: file.mp3", "file.mp3", result);

        result = mma.getFileName("file.wma", "audio/x-ms-wma");
        assertEquals("return should be: file.wma", "file.wma", result);

        result = mma.getFileName("file.mid", "audio/sp-midi");
        assertEquals("return should be: file.mid", "file.mid", result);

        result = mma.getFileName("file.wav", "audio/wav");
        assertEquals("return should be: file.wav", "file.wav", result);

        result = mma.getFileName("file.mg", "application/vnd.nokia.ringing-tone");
        assertEquals("return should be: file.mg", "file.mg", result);

        result = mma.getFileName("file.mmf", "application/vnd.smaf");
        assertEquals("return should be: file.mmf", "file.mmf", result);

        result = mma.getFileName("file.rmf", "audio/mmf");
        assertEquals("return should be: file.rmf", "file.rmf", result);

        result = mma.getFileName("file.au", "audio/basic");
        assertEquals("return should be: file.au", "file.au", result);

        result = mma.getFileName("file.imy", "audio/imelody" );
        assertEquals("return should be: file.imy", "file.imy", result);

        result = mma.getFileName("file.amr", "audio/amr");
        assertEquals("return should be: file.amr", "file.amr", result);

        result = mma.getFileName("file.mpg", "video/mpeg");
        assertEquals("return should be: file.mpg", "file.mpg", result);

        result = mma.getFileName("file.wm", "video/x-ms-wm");
        assertEquals("return should be: file.wm", "file.wm", result);

        result = mma.getFileName("file.wmv", "video/x-ms-wmv");
        assertEquals("return should be: file.wmv", "file.wmv", result);

        result = mma.getFileName("file.swf", "application/x-shockwave-flash");
        assertEquals("return should be: file.swf", "file.swf", result);

        result = mma.getFileName("file.qt", "video/quicktime");
        assertEquals("return should be: file.qt", "file.qt", result);

        result = mma.getFileName("file.rv", "video/vnd.rn-realvideo");
        assertEquals("return should be: file.rv", "file.rv", result);

        result = mma.getFileName("file.rm", "application/vnd.rn-realmedia");
        assertEquals("return should be: file.rm", "file.rm", result);

        result = mma.getFileName("file.ram", "application/vnd.rn-realaudio");
        assertEquals("return should be: file.ram", "file.ram", result);

        result = mma.getFileName("file.avi", "video/avi");
        assertEquals("return should be: file.avi", "file.avi", result);


        // test none mapped extensions
        result = mma.getFileName("file.end", "image/bmp");
        assertEquals("return should be: file.bmp", "file.bmp", result);

        result = mma.getFileName("file.end", "image/gif");
        assertEquals("return should be: file.gif", "file.gif", result);

        result = mma.getFileName("file.end", "image/jpeg");
        assertEquals("return should be: file.jpg", "file.jpg", result);

        result = mma.getFileName("file.end", "image/png");
        assertEquals("return should be: file.png", "file.png", result);

        result = mma.getFileName("file.end", "image/tiff");
        assertEquals("return should be: file.tiff", "file.tiff", result);

        result = mma.getFileName("file.end", "image/pjpeg");
        assertEquals("return should be: file.pjpeg", "file.pjpeg", result);

        result = mma.getFileName("file.end", "image/vnd.wap.wbmp");
        assertEquals("return should be: file.wbmp", "file.wbmp", result);

        result = mma.getFileName("file.end", "audio/gsm");
        assertEquals("return should be: file.gsm", "file.gsm", result);

        result = mma.getFileName("file.end", "audio/midi" );
        assertEquals("return should be: file.midi", "file.midi", result);

        result = mma.getFileName("file.end", "audio/mpeg3");
        assertEquals("return should be: file.mp3", "file.mp3", result);

        result = mma.getFileName("file.end", "audio/x-ms-wma");
        assertEquals("return should be: file.wma", "file.wma", result);

        result = mma.getFileName("file.end", "audio/sp-midi");
        assertEquals("return should be: file.mid", "file.mid", result);

        result = mma.getFileName("file.end", "audio/wav");
        assertEquals("return should be: file.wav", "file.wav", result);

        result = mma.getFileName("file.end", "application/vnd.nokia.ringing-tone");
        assertEquals("return should be: file.mg", "file.mg", result);

        result = mma.getFileName("file.end", "application/vnd.smaf");
        assertEquals("return should be: file.mmf", "file.mmf", result);

        result = mma.getFileName("file.end", "audio/mmf");
        assertEquals("return should be: file.rmf", "file.rmf", result);

        result = mma.getFileName("file.end", "audio/basic");
        assertEquals("return should be: file.au", "file.au", result);

        result = mma.getFileName("file.end", "audio/imelody" );
        assertEquals("return should be: file.imy", "file.imy", result);

        result = mma.getFileName("file.end", "audio/amr");
        assertEquals("return should be: file.amr", "file.amr", result);

        result = mma.getFileName("file.end", "video/mpeg");
        assertEquals("return should be: file.mpg", "file.mpg", result);

        result = mma.getFileName("file.end", "video/x-ms-wm");
        assertEquals("return should be: file.wm", "file.wm", result);

        result = mma.getFileName("file.end", "video/x-ms-wmv");
        assertEquals("return should be: file.wmv", "file.wmv", result);

        result = mma.getFileName("file.end", "application/x-shockwave-flash");
        assertEquals("return should be: file.swf", "file.swf", result);

        result = mma.getFileName("file.end", "video/quicktime");
        assertEquals("return should be: file.qt", "file.qt", result);

        result = mma.getFileName("file.end", "video/vnd.rn-realvideo");
        assertEquals("return should be: file.rv", "file.rv", result);

        result = mma.getFileName("file.end", "application/vnd.rn-realmedia");
        assertEquals("return should be: file.rm", "file.rm", result);

        result = mma.getFileName("file.end", "application/vnd.rn-realaudio");
        assertEquals("return should be: file.ram", "file.ram", result);

        result = mma.getFileName("file.end", "video/avi");
        assertEquals("return should be: file.avi", "file.avi", result);


        // check non-mapped mime types are not changed
        result = mma.getFileName("file.txt", "text/plain");
        assertEquals("return should be: file.txt", "file.txt", result);

        result = mma.getFileName("file.uext", "text/plain");
        assertEquals("return should be: file.uext", "file.uext", result);

        result = mma.getFileName("file.cns", "audio/vnd.cns.inf1");
        assertEquals("return should be: file.cns", "file.cns", result);
    }


    /**
     * Derived class for testing getFileName method of MimeMessageAssembler
     */
    private class MMAGetFileName extends MimeMessageAssembler {

        public String getFileName(String pathName, String mimeType ) {
            return super.getFileName(pathName, mimeType);
        }

    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Apr-05	538/3	amoore	VBM:2005041310 Removed excess mappings and changed mapping to mime > ext

 14-Apr-05	538/1	amoore	VBM:2005041310 Updated getFileName(...) to not mangle certain extensions

 29-Nov-04	243/3	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 25-Oct-04	220/1	claire	VBM:2004102507 Ensuring MPS builds against MCS after Pipeline move

 30-Sep-04	172/1	claire	VBM:2004071903 Mime types not being set for non-text assets on the server

 22-Sep-04	140/1	claire	VBM:2004070704 Tidying up Transforce use in the MimeMessageAssembler and fixing testcases

 14-Jul-04	133/1	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 08-Jul-04	127/4	claire	VBM:2004070702 Update layout names to include extensions

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
