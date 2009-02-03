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
import com.sun.media.jai.operator.ImageReadDescriptor;

import java.io.InputStream;
import java.util.Collection;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.log4j.Category;

/**
 * class that performs tests based on scaling images to various sizes
 */
public class ScaleTestCase extends TestCaseAbstract {


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

    /**
     * Load and scale lena.tiff. This is a known true colour image of size
     * 512x512
     */
    private RenderedOp doScale(String rule, int width, String scaleLarger)
        throws Throwable {
        InputStream stream =  TestUtilities.
                doImageTranscodeTest(expectations,
                                     rule, "lena.tiff",
                                     new String[][]{
                                         {"v.width", String.valueOf(width)},
                                         {"v.scaleLarger", scaleLarger}},
                                     true);


        ImageReader reader = null;
        if (rule.equals(OutputImageRules.WBMP)) {
            // wbmps do not have a magic number so ImageRead operation cannot
            // load them.
            reader = ImageIO.getImageReadersByFormatName("wbmp").next();
        }
        Collection collection = ImageReadDescriptor.createCollection(
            ImageIO.createImageInputStream(stream),
            null, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null,null,null, reader, null);
        RenderedOp image = (RenderedOp) collection.iterator().next();

        int expectedWidth = width;
        if (scaleLarger.equals("false") && (expectedWidth > 512)) {
            expectedWidth = 512;
        }
        assertEquals("Incorrect image width", expectedWidth, image.getWidth());
        assertEquals("Incorrect image height", expectedWidth,
                     image.getHeight());

        return image;
    }

    /**
     * Scale image to a smaller size
     */

    public void testScaleSmallerColorBMPTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURBMP24, 120, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleSmallerColorJPEGTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURJPEG24, 120, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleSmallerColorPNGTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURPNG24, 120, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleSmallerColorTIFFTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURTIFF24, 120, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleSmallerColorPNGIndexed()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURPNG8, 120, "false");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testScaleSmallerColorGIFIndexed()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURGIF8, 120, "false");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testScaleSmallerGreyPNG8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG8, 120, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleSmallerGreyGIF8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF8, 120, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleSmallerGreyJPEG8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEJPEG8, 120, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleSmallerGreyPNG4Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG4, 120, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleSmallerGreyGIF4Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF4, 120, "false");
        assertEquals("Image is not greyscale4", true,
                     TestUtilities.isGreyscale4(image));
    }

    public void testScaleSmallerGreyPNG2Bit()
        throws Throwable {
        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG2, 120, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleSmallerMonochromePNG()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG1, 120, "false");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    public void testScaleSmallerMonochromeGIF()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF1, 120, "false");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));

    }

    public void testScaleSmallerMonochromeWBMP()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.WBMP, 120, "false");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    /**
     * Scale image to a larger size
     */

    public void testScaleLargerDisabledColorBMPTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURBMP24, 700, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerDisabledColorJPEGTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURJPEG24, 700, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerDisabledColorPNGTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURPNG24, 700, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerDisabledColorTIFFTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURTIFF24, 700, "false");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerDisabledColorPNGIndexed()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURPNG8, 700, "false");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testScaleLargerDisabledColorGIFIndexed()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURGIF8, 700, "false");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testScaleLargerDisabledGreyPNG8bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG8, 700, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerDisabledGreyGIF8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF8, 700, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerDisabledGreyJPEG8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEJPEG8, 700, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerDisabledGreyPNG4Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG4, 700, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerDisabledGreyGIF4Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF4, 700, "false");
        assertEquals("Image is not greyscale4", true,
                     TestUtilities.isGreyscale4(image));
    }

    public void testScaleLargerDisabledGreyPNG2Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG2, 700, "false");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerDisabledGreyGIF2Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF2, 700, "false");
        assertEquals("Image is not greyscale2", true,
                     TestUtilities.isGreyscale2(image));
    }

    public void testScaleLargerDisabledMonochromePNG()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG1, 700, "false");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    public void testScaleLargerDisabledMonochromeGIF()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF1, 700, "false");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));

    }

    public void testScaleLargerDisabledMonochromeWBMP()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.WBMP, 700, "false");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    /**
     * Scale image to a larger size when disabled
     */

    public void testScaleLargerColorBMPTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURBMP24, 700, "true");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerColorJPEGTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURJPEG24, 700, "true");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerColorPNGTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURPNG24, 700, "true");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerColorTIFFTrueColor()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURTIFF24, 700, "true");
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
    }

    public void testScaleLargerColorPNGIndexed()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURPNG8, 700, "true");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testScaleLargerColorGIFIndexed()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.COLOURGIF8, 700, "true");
        assertEquals("Image is not indexed", true,
                     TestUtilities.isIndexed(image));
    }

    public void testScaleLargerGreyPNG8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG8, 700, "true");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerGreyGIF8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF8, 700, "true");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerGreyJPEG8Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEJPEG8, 700, "true");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerGreyPNG4Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG4, 700, "true");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerGreyGIF4Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF4, 700, "true");
        assertEquals("Image is not greyscale4", true,
                     TestUtilities.isGreyscale4(image));
    }

    public void testScaleLargerGreyPNG2Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG2, 700, "true");
        assertEquals("Image is not greyscale", true,
                     TestUtilities.isGreyscale(image));
    }

    public void testScaleLargerGreyGIF2Bit()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF2, 700, "true");
        assertEquals("Image is not greyscale2", true,
                     TestUtilities.isGreyscale2(image));
    }

    public void testScaleLargerMonochromePNG()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GRAYSCALEPNG1, 700, "true");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    public void atestScaleLargerMonochromeGIF()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.GREYSCALEGIF1, 700, "true");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }

    public void testScaleLargerMonochromeWBMP()
        throws Throwable {

        RenderedOp image = doScale(OutputImageRules.WBMP, 700, "true");
        assertEquals("Image is not monochrome", true,
                     TestUtilities.isMonochrome(image));
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Apr-05	410/3	matthew	VBM:2004111103 refactor Greyscale*Converter

 07-Apr-05	410/1	matthew	VBM:2004111103 make gp1 produce gp2 images to work around a bug in JAI

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/4	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 19-Jan-05	270/1	matthew	VBM:2004121009 Allow correct handling of gif images with transparency

 ===========================================================================
*/
