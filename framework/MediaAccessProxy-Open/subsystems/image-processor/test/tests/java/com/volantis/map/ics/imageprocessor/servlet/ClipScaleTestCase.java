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
import com.volantis.map.ics.imageprocessor.ImageProcessorException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.imageio.ImageIO;

import org.apache.log4j.Category;

/**
 * class that performs tests based on resizing by clipping and scaling
 */
public class ClipScaleTestCase extends TestCaseAbstract {

    final static int LENA_SIZE = 512;

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
    private RenderedOp doResize(String rule, int width, String protectedArea)
        throws Throwable, IOException, MalformedURLException {
        InputStream stream = TestUtilities.
            doImageTranscodeTest(expectations,
                                 rule, "lena.tiff",
                                 protectedArea != null
                                 ? new String[][]{
                                     {"v.width", String.valueOf(width)},
                                     {"v.p", protectedArea}}
                                 : new String[][]{
                                     {"v.width", String.valueOf(width)}},
                                 true);

        RenderedOp image = JAI.create("ImageRead", ImageIO.createImageInputStream(stream));
        return image;
    }

    private void checkCase(int width, String protectedArea, int expWidth,
                           int expHeight)
        throws Throwable, IOException, MalformedURLException {
        RenderedOp image = null;
        image = doResize(OutputImageRules.COLOURJPEG24, width, protectedArea);
        assertEquals("Image is not true colour", true,
                     TestUtilities.isTrueColour(image));
        assertEquals(image.getWidth(), expWidth);
        assertEquals(image.getHeight(), expHeight);
    }

    /**
     * Test if no protected area is defined Should work as before with scaling.
     * More thorough tesing is done by the ScaleTestCase.
     *
     * @fixme this test does not verify what it's supposed to
     */

    public void testNoProtectedArea()
        throws Throwable, IOException, MalformedURLException {
        checkCase(LENA_SIZE + 122, "0", LENA_SIZE + 122, LENA_SIZE + 122);
        checkCase(LENA_SIZE, "0", LENA_SIZE, LENA_SIZE);
        checkCase(1, "0", 1, 1);
    }

    /**
     * Test if protected area is  empty defined Should work as before with
     * scaling. More thorough tesing is done by the ScaleTestCase.
     */

    public void testEmptyProtectedArea()
        throws Throwable, IOException, MalformedURLException {
        checkCase(LENA_SIZE + 122, "0", LENA_SIZE + 122, LENA_SIZE + 122);
        checkCase(LENA_SIZE, "0", LENA_SIZE, LENA_SIZE);
        checkCase(1, "0", 1, 1);
    }

    /**
     * Test if protected area is  "0"
     * @fixme this test does not verify what it's supposed to
     */

    public void testLeftSizeArea0()
        throws Throwable, IOException, MalformedURLException {
        // bigger image
        checkCase(LENA_SIZE + 122, "0", LENA_SIZE + 122, LENA_SIZE + 122);
        checkCase(LENA_SIZE, "0", LENA_SIZE, LENA_SIZE);
        checkCase(1, "0", 1, 1);
    }

    /**
     * Test if protected area is  "left only non zero"e.
     */

    public void testLeftClippable()
        throws Throwable, IOException, MalformedURLException {
        // bigger image
        final String vp = "12";
        // should be scaled up
        checkCase(LENA_SIZE + 122, vp, LENA_SIZE + 122, LENA_SIZE + 122);
        checkCase(LENA_SIZE, vp, LENA_SIZE, LENA_SIZE);
        
        // should be clipped only
        checkCase(LENA_SIZE - 5, vp, LENA_SIZE - 5, LENA_SIZE);
        checkCase(LENA_SIZE - 12, vp, LENA_SIZE - 12, LENA_SIZE);
        
        // should be scaled down 
        checkCase(250, vp, 250, LENA_SIZE / 2);
        checkCase(1, vp, 1, 1);
    }

    /**
     * Test if protected area is rigth clippable
     */

    public void testRightClippable()
        throws Throwable, IOException, MalformedURLException {
        // bigger image
        final String vp = "0,499";
        // should be scaled up
        checkCase(LENA_SIZE + 122, vp, LENA_SIZE + 122, LENA_SIZE + 122);
        checkCase(LENA_SIZE, vp, LENA_SIZE, LENA_SIZE);
        
        // should be clipped only
        checkCase(LENA_SIZE - 5, vp, LENA_SIZE - 5, LENA_SIZE);
        checkCase(LENA_SIZE - 12, vp, LENA_SIZE - 12, LENA_SIZE);
        
        // should be scaled down 
        checkCase(250, vp, 250, LENA_SIZE / 2);
        checkCase(1, vp, 1, 1);
    }

    /**
     * Test if protected area is clippable on both sides
     */

    public void testBothClippable()
        throws Throwable, IOException, MalformedURLException {
        // bigger image
        final String vp = "6,505";
        // should be scaled up
        checkCase(LENA_SIZE + 122, vp, LENA_SIZE + 122, LENA_SIZE + 122);
        checkCase(LENA_SIZE, vp, LENA_SIZE, LENA_SIZE);
        
        // should be clipped only
        checkCase(LENA_SIZE - 5, vp, LENA_SIZE - 5, LENA_SIZE);
        checkCase(LENA_SIZE - 12, vp, LENA_SIZE - 12, LENA_SIZE);
        
        // should be scaled down 
        checkCase(250, vp, 250, LENA_SIZE / 2);
        checkCase(1, vp, 1, 1);
    }

    /**
     * Test the one colum cases
     */

    public void testOneColumners()
        throws Throwable, IOException, MalformedURLException {
        checkCase(1, "0,0", 1, LENA_SIZE);
        checkCase(1, (LENA_SIZE - 1) + "," + (LENA_SIZE - 1), 1, LENA_SIZE);
        checkCase(1, (LENA_SIZE / 2) + "," + (LENA_SIZE / 2), 1, LENA_SIZE);
    }


    /**
     * Test some malformed cases
     */

    public void testMalformed()
        throws Throwable, IOException, MalformedURLException {

        try {
            checkCase(LENA_SIZE - 1, "93wkje", LENA_SIZE, LENA_SIZE);
            fail("total garbage v.p accepted");
        } catch (IllegalArgumentException ignore) {
        }

        try {
            checkCase(LENA_SIZE - 1, "93,wkje", LENA_SIZE, LENA_SIZE);
            fail("garbage v.p accepted as second param");
        } catch (IllegalArgumentException ignore) {
        }

        try {
            checkCase(LENA_SIZE - 1, "" + LENA_SIZE, LENA_SIZE, LENA_SIZE);
            fail("invalid left bound accepted in  v.p");
        } catch (ImageProcessorException ignore) {
        }

        try {
            checkCase(LENA_SIZE - 1, "-1,-1" + LENA_SIZE, LENA_SIZE,
                      LENA_SIZE);
            fail("invalid right bound accepted in  v.p");
        } catch (IllegalArgumentException ignore) {
        }

        try {
            checkCase(LENA_SIZE - 1, "10,9", LENA_SIZE, LENA_SIZE);
            fail("invalid area accepted in  v.p");
        } catch (IllegalArgumentException ignore) {
        }

        try {
            checkCase(0, "0", 0, 0);
            fail("invalid width 0 accepted");
        } catch (ImageProcessorException ignore) {
        }
    }

    /**
     * Test cases that should be fixed
     */

    public void testAutoFix()
        throws Throwable, IOException, MalformedURLException {
        checkCase(LENA_SIZE, "0,600", LENA_SIZE, LENA_SIZE);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Nov-05	554/3	pszul	VBM:2005102504 intendation corrected to 4 spaces
 
 04-Nov-05	554/1	pszul	VBM:2005102504 intelligent clipping implemented
 
 31-Oct-05	554/1	pszul	VBM:2005102504 image clipping added
 
 ===========================================================================
 */
