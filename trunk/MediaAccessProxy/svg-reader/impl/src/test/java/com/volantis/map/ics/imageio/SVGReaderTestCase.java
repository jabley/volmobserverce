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
package com.volantis.map.ics.imageio;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;


public class SVGReaderTestCase extends TestCase {
    private static final int IMAGE_WIDTH = 169;

    private static final int IMAGE_HEIGHT = 51;

    public void testSVGReaderPlugInAvailable() {

        String[] availableFormats = ImageIO.getReaderFormatNames();
        String svgFormat = "svg";
        List supportedFormats = Arrays.asList(availableFormats);

        String message = "SVG file format not supported. SVG Reader plugin " +
            "cannot be found.";
        assertTrue(message, supportedFormats.contains(svgFormat));
    }

    public void testReadSVGFile() throws IOException {
        InputStream is =
            SVGReaderTestCase.class.
            getResourceAsStream("images/asf-logo.svg");

        BufferedImage image = ImageIO.read(is);

        assertEquals(IMAGE_WIDTH, image.getWidth());
        assertEquals(IMAGE_HEIGHT, image.getHeight());
    }

    public static Test suite() {
        return new TestSuite(SVGReaderTestCase.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Feb-05	311/1	rgreenall	VBM:2005012701 Resolved conflicts

 ===========================================================================
*/
