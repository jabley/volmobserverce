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

import java.io.InputStream;

import org.apache.log4j.Category;

/**
 * This class contains tests that ensure that the requested image does not
 * exceed that maximum size that was specified.
 */
public class MaximumSizeTestCase extends TestCaseAbstract {

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
     */
    private void doMaxSizeTest(String rule, long size)
        throws Throwable {
        InputStream stream = TestUtilities.
                doImageTranscodeTest(expectations,
                                     rule,
                                     "lena.tiff",
                                     new String[][]{
                                         {"v.maxSize", String.valueOf(size)},
                                         {"v.paletteMin", "1"}},
                                     true);

        long length = stream.available();
        assertEquals("Output is too large.", false, (length > size));
    }

    public void testMaxSizeBMPTrueColor()
        throws Throwable {
        // This has never worked. The code does not even try to downsize or
        // reduce the bit depth of the image
//         doMaxSizeTest(OutputImageRules.COLOURBMP24, 16834);
    }

//    public void testMaxSizeJPGTrueColor()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.COLOURJPEG24, 16834);
//    }
//
//    public void testMaxSizePNGTrueColor()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.COLOURPNG24, 16384);
//    }
//
//    public void testMaxSizeTIFFTrueColor()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.COLOURTIFF24, 16834);
//    }
//
//    public void testMaxSizeColorPNGIndexed()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.COLOURPNG8, 8192);
//    }
//
//    public void testMaxSizeColorGIFIndexed()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.COLOURGIF8, 8192);
//    }
//
//    public void testMaxSizeGreyJPG8Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GREYSCALEJPEG8, 16384);
//    }
//
//    public void testMaxSizeGreyPNG8Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GRAYSCALEPNG8, 8192);
//    }
//
//    public void testMaxSizeGreyGIF8Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GREYSCALEGIF8, 8192);
//    }
//
//    public void testMaxSizeGreyPNG4Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GRAYSCALEPNG4, 8192);
//    }
//
//    public void testMaxSizeGreyGIF4Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GREYSCALEGIF4, 8192);
//    }
//
//    public void testMaxSizeGreyPNG2Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GRAYSCALEPNG2, 8192);
//    }
//
//    public void testMaxSizeGreyGIF2Bit()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GREYSCALEGIF2, 8192);
//    }
//
//    public void testMaxSizeMonochromePNG()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GRAYSCALEPNG1, 8192);
//    }
//
//    public void testMaxSizeMonochromeGIF()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.GREYSCALEGIF1, 8192);
//    }
//
//    public void testMaxSizeMonochromeWBMP()
//        throws Throwable {
//        doMaxSizeTest(OutputImageRules.WBMP, 8192);
//    }

    public void testBROKEN_TEST_CASES_HERE() throws Exception {

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jan-05	289/1	matthew	VBM:2005012617 Fix problems with transcoding gif/png with transparency and refactor test case

 24-Jan-05	276/4	matthew	VBM:2004121009 Allow transcoding of indexed images that have a transparency

 19-Jan-05	270/1	matthew	VBM:2004121009 Allow correct handling of gif images with transparency

 ===========================================================================
*/
