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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.map.ics.configuration.OutputImageRules;

import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.media.jai.Histogram;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;

import org.apache.log4j.Category;

/**
 * class that performs tests based on clipping images to various sizes
 */
public class ClipTestCase extends TestCaseAbstract {

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

    private String getProtectedArea(int width, String mode) {
        if (mode.equals("right")) {
            return "0," + (width - 1);
        } else if (mode.equals("both")) {
            return "" + ((512 - width) / 2) + "," + ((512 - width) / 2 +
                width -
                1);
        } else {
            return "" + (512 - width);
        }
    }

    /**
     * Load and scale lena.tiff. This is a known true colour image of size
     * 512x512
     */
    private RenderedOp doClip(String rule, int width, String clipMode)
        throws Throwable, IOException, MalformedURLException {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 rule, "lena.tiff",
                                 new String[][]{
                                     {"v.width", String.valueOf(width)},
                                     {"v.p",
                                      getProtectedArea(width, clipMode)}},
                                 true);

        ImageReader reader = null;
        if (rule.equals(OutputImageRules.WBMP)) {
            // wbmps do not have a magic number so ImageRead operation cannot
            // load them.
            reader = ImageIO.getImageReadersByFormatName("wbmp").next();
        }
        ParameterBlockJAI pblock = new ParameterBlockJAI("ImageRead");
        pblock.setParameter("Input", ImageIO.createImageInputStream(stream));
        pblock.setParameter("Reader", reader);
        RenderedOp image = JAI.create("ImageRead", pblock);

        int expectedWidth = width;
        int expectedHeight = (width > 512) ? width : 512; 
        assertEquals("Incorrect image width", expectedWidth, image.getWidth());
        assertEquals("Incorrect image height", expectedHeight, image.getHeight());

        return image;
    }

    /**
     * Splits the tests image into two parts using the image clipping. Checks
     * if the sum of the histograms of the parts equals to the histogram of the
     * original image.
     *
     * @param splitOffset the image will be split (vertically) at this point
     * @throws Exception
     */
    private void checkBisectClip(int splitOffset) throws Throwable {
        String params[][] = {
            new String[]{"left", "right", "left"},
            new String[]{"512", Integer.toString(splitOffset),
                         Integer.toString(512 - splitOffset)}
        };
        Histogram histogram[] = new Histogram[3];
        for (int i = 0; i < 3; i++) {
            InputStream stream = TestUtilities.
                doImageTranscodeTest(expectations,
                                     OutputImageRules.GRAYSCALEPNG8, "lena.tiff",
                                     new String[][]{
                                         {"v.width", params[1][i]},
                                         {"v.p", getProtectedArea(
                                             Integer.parseInt(params[1][i]),
                                             params[0][i])}},
                                     true);
            RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
            // Create the parameter block.
            PlanarImage dst = null;
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(image);               // Specify the source image
            pb.add(null);                      // No ROI
            pb.add(1);                         // Sampling
            pb.add(1);                         // periods
            // Perform the histogram operation.
            dst = JAI.create("histogram", pb, null);
            
            // Retrieve the histogram data.
            Histogram hist = (Histogram) dst.getProperty("histogram");
            histogram[i] = hist;
        }
        for (int i = 0; i < 256; i++) {
            assertEquals(histogram[0].getBinSize(0, i),
                         histogram[1].getBinSize(0, i) +
                         histogram[2].getBinSize(0, i));
        }
    }

    /**
     * Splits the tests image into three parts using the image clipping. Checks
     * if the sum of the histograms of the parts equals to the histogram of the
     * original image.
     *
     * @param splitOffset the image will be split (vertically) at this point
     * @throws Exception
     */
    private void checkTrisectClip(int splitOffset) throws Throwable {
        String params[][] = {
            new String[]{"left", "right", "left", "both"},
            new String[]{"512", Integer.toString((512 - splitOffset) / 2),
                         Integer.toString(
                             512 - splitOffset - (512 - splitOffset) / 2),
                         Integer.toString(splitOffset)}
        };
        Histogram histogram[] = new Histogram[4];
        for (int i = 0; i < 4; i++) {
            InputStream stream = TestUtilities.
                doImageTranscodeTest(expectations,
                                     OutputImageRules.GRAYSCALEPNG8, "lena.tiff",
                                     new String[][]{
                                         {"v.width", params[1][i]},
                                         {"v.p", getProtectedArea(
                                             Integer.parseInt(params[1][i]),
                                             params[0][i])}},
                                     true);
            RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
            // Create the parameter block.
            PlanarImage dst = null;
            ParameterBlock pb = new ParameterBlock();
            pb.addSource(image);               // Specify the source image
            pb.add(null);                      // No ROI
            pb.add(1);                         // Sampling
            pb.add(1);                         // periods
            // Perform the histogram operation.
            dst = JAI.create("histogram", pb, null);
            
            // Retrieve the histogram data.
            Histogram hist = (Histogram) dst.getProperty("histogram");
            histogram[i] = hist;
        }
        for (int i = 0; i < 256; i++) {
            assertEquals(histogram[0].getBinSize(0, i),
                         histogram[1].getBinSize(0, i) +
                         histogram[2].getBinSize(0, i) +
                         histogram[3].getBinSize(0, i));
        }
    }

    /**
     * Test clipping
     */

    public void testClipLeftRight() throws Throwable {
        // check boundaries
        checkBisectClip(1);
        checkBisectClip(511);
        // and a value in between
        checkBisectClip(200);
    }


    public void testClipBoth() throws Throwable {
        // try an odd and even value
        checkTrisectClip(1);
        checkTrisectClip(2);
    }


    /**
     * Clip image to a smaller size
     */

    public void testClipSmallerColorBMPTrueColor()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURBMP24, 120, "left");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testClipSmallerColorJPEGTrueColor()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURJPEG24, 120, "left");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testClipSmallerColorTIFFTrueColor()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURTIFF24, 120, "left");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testClipSmallerColorPNGIndexed()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURPNG8, 120, "left");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testClipSmallerColorGIFIndexed()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURGIF8, 120, "left");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testClipSmallerGreyPNG8Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG8, 120, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipSmallerGreyGIF8Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF8, 120, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipSmallerGreyJPEG8Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEJPEG8, 120, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipSmallerGreyPNG4Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG4, 120, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipSmallerGreyGIF4Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF4, 120, "left");
        assertEquals("Image is not greyscale4", true,
                     TestUtilities.isGreyscale4(image));
    }

    public void testClipSmallerGreyPNG2Bit()
        throws Throwable, IOException, MalformedURLException {
        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG2, 120, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipSmallerMonochromePNG()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG1, 120, "left");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    public void testClipSmallerMonochromeGIF()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF1, 120, "left");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));

    }

    public void testClipSmallerMonochromeWBMP()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.WBMP, 120, "left");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    /**
     * Clip image to a larger size
     */

    public void testClipLargerDisabledColorBMPTrueColor()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURBMP24, 700, "left");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testClipLargerDisabledColorJPEGTrueColor()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURJPEG24, 700, "left");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testClipLargerDisabledColorTIFFTrueColor()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURTIFF24, 700, "left");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testClipLargerDisabledColorPNGIndexed()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURPNG8, 700, "left");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testClipLargerDisabledColorGIFIndexed()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.COLOURGIF8, 700, "left");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testClipLargerDisabledGreyPNG8bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG8, 700, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipLargerDisabledGreyGIF8Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF8, 700, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipLargerDisabledGreyJPEG8Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEJPEG8, 700, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipLargerDisabledGreyPNG4Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG4, 700, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipLargerDisabledGreyGIF4Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF4, 700, "left");
        assertEquals("Image is not greyscale4", true,
                     TestUtilities.isGreyscale4(image));
    }

    public void testClipLargerDisabledGreyPNG2Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG2, 700, "left");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testClipLargerDisabledGreyGIF2Bit()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF2, 700, "left");
        assertEquals("Image is not greyscale2", true,
                     TestUtilities.isGreyscale2(image));
    }

    public void testClipLargerDisabledMonochromePNG()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GRAYSCALEPNG1, 700, "left");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    public void testClipLargerDisabledMonochromeGIF()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.GREYSCALEGIF1, 700, "left");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));

    }

    public void testClipLargerDisabledMonochromeWBMP()
        throws Throwable, IOException, MalformedURLException {

        RenderedOp image = doClip(OutputImageRules.WBMP, 700, "left");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/5	pszul	VBM:2005102504 intendation corrected to 4 spaces
 
 04-Nov-05	554/3	pszul	VBM:2005102504 intelligent clipping implemented
 
 31-Oct-05	554/1	pszul	VBM:2005102504 image clipping added
 
 ===========================================================================
 */
