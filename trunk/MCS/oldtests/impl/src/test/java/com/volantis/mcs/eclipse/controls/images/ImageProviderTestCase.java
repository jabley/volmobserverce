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
package com.volantis.mcs.eclipse.controls.images;

import com.volantis.testtools.io.ResourceUtilities;
import junit.framework.TestCase;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class tests ImageProvider
 */
public class ImageProviderTestCase extends TestCase {

    private static final String WBMP_IMAGE = "com/volantis/mcs/eclipse/controls/images/volantis1.wbmp";
    private static final String JPG_IMAGE = "com/volantis/mcs/eclipse/controls/images/jug.jpg";
    private static final String GIF_IMAGE = "com/volantis/mcs/eclipse/controls/images/splash.gif";
    private static final String PNG_IMAGE = "com/volantis/mcs/eclipse/controls/images/duck-fill.png";
    private static final String BMP_IMAGE = "com/volantis/mcs/eclipse/controls/images/sourceforge.bmp";
    private static final String TIFFGREYSCALE_IMAGE = "com/volantis/mcs/eclipse/controls/images/greymarbles.tiff";
    private static final String TIFFTRUE_IMAGE = "com/volantis/mcs/eclipse/controls/images/lena.tiff";
    private static final String TIFF256_IMAGE = "com/volantis/mcs/eclipse/controls/images/baboon.tiff";
    private static final String TIFFMONO_IMAGE = "com/volantis/mcs/eclipse/controls/images/fax1.tiff";

    public ImageProviderTestCase(String name) {
        super(name);
    }

    /**
     * This tests the WBMPImageProvider.provideImage(File) method
     * @throws Exception
     */
    public void testProvideWBMPImage() throws Exception {
        File file = ResourceUtilities.getResourceAsFile(WBMP_IMAGE);
        ImageData imageData = null;
        try {
            imageData = new WBMPImageProvider().provideImage(file);
        } catch (IOException ioe) {
            imageData = null;
        } catch (IllegalArgumentException iaee) {
            imageData = null;
        }
        assertNotNull(imageData);
    }

    /**
     * This tests the TIFFImageProvider.provideImage(File) method for
     * greyscale, truecolour, 256-colour and mono TIFF images.
     * @throws Exception
     */
    public void testProvideTIFFImage() throws Exception {
        String[] tiffImages = new String[]{TIFFGREYSCALE_IMAGE, TIFFTRUE_IMAGE, TIFF256_IMAGE, TIFFMONO_IMAGE};
        for (int i = 0; i < tiffImages.length; i++) {
            File file = ResourceUtilities.getResourceAsFile(tiffImages[i]);
            ImageData imageData = null;
            try {
                imageData = new TIFFImageProvider().provideImage(file);
            } catch (IOException ioe) {
                imageData = null;
            } catch (IllegalArgumentException iae) {
                imageData = null;
            }
            assertNotNull(imageData);
        }
    }

    /**
     * This tests the various ImageProvider's provideImage(File) method for unsupported images
     * @throws Exception
     */
    public void testIllegalImages() throws Exception {
        boolean pass = false;
        File file = null;
        file = ResourceUtilities.getResourceAsFile(TIFF256_IMAGE);
        try {
            new WBMPImageProvider().provideImage(file);
        } catch (IOException e) {
            pass = true;
        }
        assertEquals("WBMPImageProvider should have thrown an IllegalArgumentException for " + file.getName(), pass, true);
        pass = false;
        file = ResourceUtilities.getResourceAsFile(BMP_IMAGE);
        try {
            new TIFFImageProvider().provideImage(file);
        } catch (IllegalArgumentException iae) {
            pass = true;
        }
        assertEquals("TiffImageProvider should have thrown an IllegalArgumentException for " + file.getName(), pass, true);
        pass = false;
        file = new File("/blah/blah/blah.blah");
        try {
            new TIFFImageProvider().provideImage(file);
        } catch (FileNotFoundException fnfe) {
            pass = true;
        }
        assertEquals("TiffImageProvider should have thrown an FileNotFoundException for " + file.getName(), pass, true);
        pass = false;
        file = new File("/blah1/blah/blah.blah");
        try {
            new WBMPImageProvider().provideImage(file);
        } catch (FileNotFoundException fnfe) {
            pass = true;
        }
        assertEquals("WBMPImageProvider should have thrown an FileNotFoundException for " + file.getName(), pass, true);
        pass = false;
        file = new File("/blah2/blah/blah.blah");
        try {
            new DefaultImageProvider().provideImage(file);
        } catch (FileNotFoundException fnfe) {
            pass = true;
        }
        assertEquals("DefaultImageProvider should have thrown an SWTException for " + file.getName(), pass, true);
    }

    /**
     * This tests the DefaultImageProvider.provideImage(File) method for JPG images
     * @throws Exception
     */
    public void testProvideJPGImage() throws Exception {
        File file = ResourceUtilities.getResourceAsFile(JPG_IMAGE);
        ImageData imageData = null;
        try {
            imageData = new DefaultImageProvider().provideImage(file);
        } catch (IOException e) {
            imageData = null;
        } catch (SWTException e) {
            imageData = null;
        }
        assertNotNull(imageData);
    }

    /**
     * This tests the DefaultImageProvider.provideImage(File) method for GIF images
     * @throws Exception
     */
    public void testProvideGIFImage() throws Exception {
        File file = ResourceUtilities.getResourceAsFile(GIF_IMAGE);
        ImageData imageData = null;
        try {
            imageData = new DefaultImageProvider().provideImage(file);
        } catch (IOException e) {
            imageData = null;
        } catch (SWTException e) {
            imageData = null;
        }
        assertNotNull(imageData);
    }

    /**
     * This tests the DefaultImageProvider.provideImage(File) method for PNG images
     * @throws Exception
     */
    public void testProvidePNGImage() throws Exception {
        File file = ResourceUtilities.getResourceAsFile(PNG_IMAGE);
        ImageData imageData = null;
        try {
            imageData = new DefaultImageProvider().provideImage(file);
        } catch (IOException e) {
            imageData = null;
        } catch (SWTException e) {
            imageData = null;
        }
        assertNotNull(imageData);
    }

    /**
     * This tests the DefaultImageProvider.provideImage(File) method for BMP images
     * @throws Exception
     */
    public void testProvideBMPImage() throws Exception {
        File file = ResourceUtilities.getResourceAsFile(BMP_IMAGE);
        ImageData imageData = null;
        try {
            imageData = new DefaultImageProvider().provideImage(file);
        } catch (IOException e) {
            imageData = null;
        } catch (SWTException e) {
            imageData = null;
        }
        assertNotNull(imageData);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 07-Nov-03	1795/8	pcameron	VBM:2003102804 Corrected package of WBMPImage

 07-Nov-03	1795/6	pcameron	VBM:2003102804 Changed the ImageProvider exception processing

 06-Nov-03	1795/1	pcameron	VBM:2003102804 Added ImageProvider infrastructure and implementation

 ===========================================================================
*/
