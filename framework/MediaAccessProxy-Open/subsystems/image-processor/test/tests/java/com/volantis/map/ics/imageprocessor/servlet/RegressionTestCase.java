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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.servlet;

import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.ics.imageprocessor.writer.impl.ImageWriterFactoryImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.log4j.Category;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import java.io.InputStream;
import java.util.Iterator;

/**
 * A class that contains tests that do not fit well into the other test cases.
 * Usually these test ICS when performing a number of operations such as image
 * transcoding and scaling. It is a convienient place to add new tests to allow
 * the reproduction of issues.
 */
public class RegressionTestCase extends TestCaseAbstract {

    protected void tearDown() throws Exception {
        // Required to prevent each message being logged once for each test
        // that has run in the past - this generates a huge log output and
        // seems to crash JUnit reporting with an OutOfMemoryError.
        // Maybe we should put this in the servlet destroy() but the version of
        // Servlet Unit we are using has no way to shut down servlets - later
        // versions do appear to have this.
        Category.shutdown();
        super.tearDown();
    }

    //////////////////////////////////////////////////////////////////////
    // explcit tests that show known problems in VTS
    /////////////////////////////////////////////////////////////////////

    /**
     * VMS-734 This test reproduces the issues raised.
     *
     * Load a 1-color gif and scale it. This caused an
     * exception to be thrown.
     *
     * @throws Exception
     */
    public void testOneColorGif() throws Exception {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 OutputImageRules.COLOURGIF8, "one-color.gif",
                                 new String[][]{{"v.width", "22"}},
                                 true);
        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        assertEquals("width should be 22", 22, image.getWidth());
    }

    /**
     * vbm.2004121009 This test reproduces the issues raised.
     *
     * Load a colour gif with a transparency and scale it. This caused an
     * exception to be thrown.
     *
     * @throws Exception
     */
    public void testColourGifWithTransparency() throws Throwable {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 OutputImageRules.COLOURGIF8, "transparency.gif",
                                 new String[][]{{"v.width", "22"}},
                                 true);
        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        assertEquals("width should be 22", 22, image.getWidth());
    }

    /**
     * vbm.2004121009 This test reproduces the issues raised.
     *
     * Load a grey scale gif image with a transparency and scale it. This
     * caused an exception to be thrown.
     *
     * @throws Exception
     */
    public void testGreyGifWithTransparency() throws Throwable {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 OutputImageRules.COLOURGIF8, "Big_counter.gif",
                                 new String[][]{{"v.width", "22"}},
                                 true);
        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        assertEquals("width should be 22", 22, image.getWidth());
    }

    /**
     * Load a colour gif image with a transparency and scale it save as jpeg 24
     * bit. This caused an error but no exception.
     *
     * @throws Exception
     */
    public void testGifWithTransparencyOutputJpeg24() throws Throwable {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 OutputImageRules.COLOURJPEG24, "transparency.gif",
                                 new String[][]{{"v.width", "22"}},
                                 true);
        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        assertEquals("image should be 22 wide", 22, image.getWidth());
    }

    /**
     * Try converting a input gif with 16 colours to all known output formats
     * while scaling the image. This is generalised version of the tests
     * above.
     *
     * This test does not produce particularly readable output but does ensure
     * that all such encodings work. To find out which test failed search for
     * "Processing rule" in the TEST output.xml file. find the last such entry
     * and that is probably the culprit.
     *
     * @throws Exception
     */
    public void testInputGif16ColourOutputAll() throws Throwable {
        Iterator outputIterator = ImageWriterFactoryImpl.RULES.keySet()
            .iterator();
        while (outputIterator.hasNext()) {
            String outputRule = (String) outputIterator.next();
            System.out.println(
                "16 color Gif input Processing rule: " + outputRule);
            doTestInputImage(outputRule, "launch_aod.gif");
            System.out.println("Finished processing rule: " + outputRule);
        }
    }


    /**
     * Try converting a input gif with transparency to all known output formats
     * while scaling the image. This is generalised version of the tests
     * above.
     *
     * This test does not produce particularly readable output but does ensure
     * that all such encodings work. To find out which test failed search for
     * "Processing rule" in the TEST output.xml file. find the last such entry
     * and that is probably the culprit.
     *
     * @throws Exception
     */
    public void testInputGifWithTransparencyOutputAll() throws Throwable {
        Iterator outputIterator = ImageWriterFactoryImpl.RULES.keySet()
            .iterator();
        while (outputIterator.hasNext()) {
            String outputRule = (String) outputIterator.next();
            System.out.println(
                "Transparent Gif input Processing rule: " + outputRule);
            doTestInputImage(outputRule, "transparency.gif");
            System.out.println("Finished processing rule: " + outputRule);
        }
    }

    /**
     * Try converting an input PNG with translucent transparency to all known
     * output formats while scaling the image. This is generalised version of
     * the tests above.
     *
     * This test does not produce particularly readable output but does ensure
     * that all such encodings work. To find out which test failed search for
     * "Processing rule" in the TEST output.xml file. find the last such entry
     * and that is probably the culprit.
     *
     * @throws Exception
     */
    public void testInputPngWithTranslucentTransparencyOutputAll()
        throws Throwable {
        Iterator outputIterator = ImageWriterFactoryImpl.RULES.keySet()
            .iterator();
        while (outputIterator.hasNext()) {
            String outputRule = (String) outputIterator.next();
            System.out.println("Processing rule: " + outputRule);
            doTestInputImage(outputRule,
                             "transparency24bitIndexed.png");
            System.out.println("Finished processing rule: " + outputRule);
        }
    }

    /**
     * Try converting an input PNG with bitmask transparency to all known
     * output formats while scaling the image. This is generalised version of
     * the tests above.
     *
     * This test does not produce particularly readable output but does ensure
     * that all such encodings work. To find out which test failed search for
     * "Processing rule" in the TEST output.xml file. find the last such entry
     * and that is probably the culprit.
     *
     * @throws Exception
     */
    public void testInputPngWithBitmaskTransparencyOutputAll()
        throws Throwable {
        Iterator outputIterator = ImageWriterFactoryImpl.RULES.keySet()
            .iterator();
        while (outputIterator.hasNext()) {
            String outputRule = (String) outputIterator.next();
            System.out.println("Processing rule: " + outputRule);
            doTestInputImage(outputRule,
                             "bitMaskTransparency24bitIndexed.png");
            System.out.println("Finished processing rule: " + outputRule);
        }
    }

    public void testTranscodingAnSVGFile() throws Throwable {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 OutputImageRules.COLOURJPEG24, "asf-logo.svg",
                                 null,
                                 true);
        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        image.getRendering();
    }

    /**
     * Convert gif to 24 bit bmp 2007102509
     * @throws Throwable
     */
    public void testTranscodingGIFToBMP() throws Throwable {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 OutputImageRules.COLOURBMP24, "tcm.gif",
                                 null,
                                 true);
        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        image.getRendering();
    }

    /**
     * Utility method used to transcode and scale an image. The output image
     * format is specified by the supplied rule.
     *
     * @throws Exception
     */
    private void doTestInputImage(String rule, String file)
        throws Throwable {
        RenderedOp image = TestUtilities.transcodeToImage(
                expectations,
                rule, file,
                new String[][]{{"v.width", "22"}},
                true);

        assertEquals(22, image.getWidth());
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Apr-05	388/6	pcameron	VBM:2005030810 Fixed cg8 rule when used with transparent PNGs

 24-Mar-05	386/3	pcameron	VBM:2005030810 Fixed cg8 rule when used with transparent PNGs

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 28-Jan-05	300/1	matthew	VBM:2005012705 Yet another image that causes failures

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/4	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 21-Jan-05	270/3	matthew	VBM:2004121009 allow transcodng of indexed images that have tansparency information (gif/png)

 ===========================================================================
*/
