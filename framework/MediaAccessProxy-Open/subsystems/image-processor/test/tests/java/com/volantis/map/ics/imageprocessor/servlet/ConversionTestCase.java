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
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.imageio.ImageIO;

import org.apache.log4j.Category;

/**
 * This class contains tests that are based around converting an image from one
 * format to another.
 */
public class ConversionTestCase extends TestCaseAbstract {

    private static final String SVG_IMAGE = "asf-logo.svg";

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
     * Load an image given a rule and test the resulting image type
     *
     * @param rule     the transcode rule to use
     * @param image    the name of the image to load
     * @param expected the string representation of the image type expected
     */
    private void doConversion(String rule, String image, String expected)
        throws Throwable, IOException, MalformedURLException {

        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations, rule, image,
                                 null, true);
        RenderedOp img = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        if (expected.equals("tc")) {
            assertEquals("Image is not truecolour", true,
                         TestUtilities.isTrueColour(img));
        } else if (expected.equals("mono")) {
            assertEquals("Image is not Monochrome", true,
                         TestUtilities.isMonochrome(img));
        } else if (expected.equals("grey")) {
            assertEquals("Image is not greyscale", true,
                         TestUtilities.isGreyscale(img));
        } else if (expected.equals("grey4")) {
            assertEquals("Image is not greyscale4", true,
                         TestUtilities.isGreyscale4(img));
        } else if (expected.equals("grey2")) {
            assertEquals("Image is not greyscale2", true,
                         TestUtilities.isGreyscale2(img));
        } else {
            assertEquals("Image is not indexed", true,
                         TestUtilities.isIndexed(img));
        }
    }

    /**
     * This test is necessary to ensure that a valid JPEG image is created
     * because of the JAI bug 4908419.
     */
    public void testTransparentGIFtoJPEG() throws Throwable {
        // This in itself will not cause the failure if the transparency is
        // left in.  But the image stream returned will be corrupted and the
        // asserts will fail.
        doConversion(OutputImageRules.COLOURJPEG24, "trans-test.gif", "tc");
    }


    /**
     * Convert true colour image to every other type
     */
    public void testConvertTCtoTC()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.COLOURBMP24, "lena.tiff", "tc");
        doConversion(OutputImageRules.COLOURJPEG24, "lena.tiff", "tc");
        doConversion(OutputImageRules.COLOURPNG24, "lena.tiff", "tc");
        doConversion(OutputImageRules.COLOURTIFF24, "lena.tiff", "tc");
    }

    public void testConvertTCtoIndexed()
        throws Throwable, IOException, MalformedURLException {
        doConversion(OutputImageRules.COLOURPNG8, "lena.tiff", "indexed");
        doConversion(OutputImageRules.COLOURGIF8, "lena.tiff", "indexed");
    }

    public void testConvertTCtoGrey8()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GREYSCALEJPEG8, "lena.tiff", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF8, "lena.tiff", "grey");
    }

    public void testConvertTCtoGrey4()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG4, "lena.tiff", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF4, "lena.tiff", "grey4");
    }

    public void testConvertTCtoGrey2()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG2, "lena.tiff", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF2, "lena.tiff", "grey2");
    }

    public void testConvertTCtoMonochrome()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG1, "lena.tiff", "mono");
        doConversion(OutputImageRules.GREYSCALEGIF1, "lena.tiff", "mono");
    }


    /**
     * Convert greyscale image to every other type
     */
    public void testConvertGreyToTC()
        throws Throwable, IOException, MalformedURLException {

       // doConversion(OutputImageRules.COLOURBMP24, "greymarbles.tiff", "tc");
        doConversion(OutputImageRules.COLOURJPEG24, "greymarbles.tiff", "tc");
        doConversion(OutputImageRules.COLOURPNG24, "greymarbles.tiff", "tc");
        doConversion(OutputImageRules.COLOURTIFF24, "greymarbles.tiff", "tc");
    }

    public void testConvertGreyToIndexed()
        throws Throwable, IOException, MalformedURLException {
        doConversion(OutputImageRules.COLOURPNG8, "greymarbles.tiff", "grey");
        doConversion(OutputImageRules.COLOURGIF8, "greymarbles.tiff", "grey");
    }

    public void testConvertGreyToGrey8()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG8, "greymarbles.tiff", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF8, "greymarbles.tiff", "grey");
    }

    public void testConvertGreyToGrey4()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG4, "greymarbles.tiff", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF4, "greymarbles.tiff", "grey4");
    }

    public void testConvertGreyToGrey2()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG2, "greymarbles.tiff", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF2, "greymarbles.tiff", "grey2");
    }


    public void testConvertGreyToMonochrome()
        throws Throwable, IOException, MalformedURLException {

        doConversion(OutputImageRules.GRAYSCALEPNG1, "greymarbles.tiff", "mono");
        doConversion(OutputImageRules.GREYSCALEGIF1, "greymarbles.tiff", "mono");
    }

    /**
     * Convert indexed image to every other type
     */
    public void testConvertIndexedToTC()
        throws Throwable {

        //doConversion(OutputImageRules.COLOURBMP24, "circusshow4256.bmp", "tc"); //JAI fails during outputting into BMP
        doConversion(OutputImageRules.COLOURJPEG24, "circusshow4256.bmp", "tc");
        doConversion(OutputImageRules.COLOURPNG24, "circusshow4256.bmp", "tc");
        doConversion(OutputImageRules.COLOURTIFF24, "circusshow4256.bmp", "tc");
    }

    public void testConvertIndexedToIndexed()
        throws Throwable {

        doConversion(OutputImageRules.COLOURPNG8, "circusshow4256.bmp", "indexed");
        doConversion(OutputImageRules.COLOURGIF8, "circusshow4256.bmp", "indexed");
    }

    public void testConvertIndexedToGrey()
        throws Throwable {

        doConversion(OutputImageRules.GRAYSCALEPNG8, "circusshow4256.bmp", "grey");
        doConversion(OutputImageRules.GREYSCALEGIF8, "circusshow4256.bmp", "grey");
    }

    public void testConvertIndexedToMonochrome()
        throws Throwable {

        doConversion(OutputImageRules.GRAYSCALEPNG1, "circusshow4256.bmp", "mono");
        doConversion(OutputImageRules.GREYSCALEGIF1, "circusshow4256.bmp", "mono");
    }

    /**
     * Convert SVG image to every other available type
     */

    public void testConvertSVGToColourBMP()
        throws Throwable {

//        doConversion(OutputImageRules.COLOURBMP24, SVG_IMAGE, "tc");
    }

    public void testConvertSVGToColourJPEG()
        throws Throwable {

        doConversion(OutputImageRules.COLOURJPEG24, SVG_IMAGE, "tc");
    }

    public void testConvertSVGToColourTIFF()
        throws Throwable {

        doConversion(OutputImageRules.COLOURTIFF24, SVG_IMAGE, "tc");
    }

    public void testConvertSVGToJPEGGreyscale() throws Throwable {
        doConversion(OutputImageRules.GREYSCALEJPEG8, SVG_IMAGE, "grey");
    }

    public void testConvertSVGToGifIndexed()
        throws Throwable {

        doConversion(OutputImageRules.COLOURGIF8, SVG_IMAGE, "indexed");
    }

    public void testConvertSVGToGifGreyscale()
        throws Throwable {

        doConversion(OutputImageRules.GREYSCALEGIF8, SVG_IMAGE, "grey");
    }

    public void testConvertSVGToGifGreyscale4()
        throws Throwable {

        doConversion(OutputImageRules.GREYSCALEGIF2, SVG_IMAGE, "grey2");
    }

    public void testConvertSVGToGifGreyscale16()
        throws Throwable {

        doConversion(OutputImageRules.GREYSCALEGIF4, SVG_IMAGE, "grey4");
    }


    public void testConvertSVGToGIFMonochrome()
        throws Throwable {

        doConversion(OutputImageRules.GREYSCALEGIF1, SVG_IMAGE, "mono");
    }

    public void testConvertSVGToPNGGreyscale()
        throws Throwable {

        doConversion(OutputImageRules.GRAYSCALEPNG8, SVG_IMAGE, "grey");
    }

    public void testConvertSVGToPNGGreyscale4()
        throws Throwable {

        doConversion(OutputImageRules.GRAYSCALEPNG2, SVG_IMAGE, "grey");

    }

    public void testConvertSVGToPNGGreyscale16()
        throws Throwable {

        doConversion(OutputImageRules.GRAYSCALEPNG4, SVG_IMAGE, "grey");
    }

    public void testConvertSVGToPNGMonochrome() throws Throwable {

        doConversion(OutputImageRules.GRAYSCALEPNG1, SVG_IMAGE, "mono");
    }

    public void testConvertSVGToPNGIndexed()
        throws Throwable {

        doConversion(OutputImageRules.COLOURPNG8, SVG_IMAGE, "indexed");
    }

    public void testConvertSVGToPNGColour()
        throws Throwable {

        doConversion(OutputImageRules.COLOURPNG24, SVG_IMAGE, "tc");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Apr-05	410/3	matthew	VBM:2004111103 refactor Greyscale*Converter

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/4	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 19-Jan-05	270/1	matthew	VBM:2004121009 Allow correct handling of gif images with transparency

 ===========================================================================
*/
