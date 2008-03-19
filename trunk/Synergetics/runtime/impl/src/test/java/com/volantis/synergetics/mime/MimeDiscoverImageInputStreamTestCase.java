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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.mime;

import junit.framework.TestCase;

import java.io.InputStream;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.ImageIO;

/**
 * This test case shows that the MimeDiscoverer works with ImageInputStreams.
 * Note that ImageInputStreams do NOT have to contain images. They are useful
 * memory or disc buffered streams (but not InputStreams)
 */
public class MimeDiscoverImageInputStreamTestCase extends TestCase {

    private MimeDiscoverer mimeDiscoverer;

    public void testTARGZ() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.tar.gz"));
        assertEquals("application/octet-stream", mimeType);
    }

    public void testAU() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.au"));
        assertEquals("audio/basic", mimeType);
    }

    public void testLIB() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.lib"));
        assertEquals("application/x-sharedlib", mimeType);
    }

    public void testAVI() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.avi"));
        assertEquals("video/x-msvideo", mimeType);
    }

    public void testWave() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.wav"));
        assertEquals("audio/x-wav", mimeType);
    }

    public void testZOO() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.zoo"));
        assertEquals("application/x-zoo", mimeType);
    }

    public void testARC() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.arc"));
        assertEquals("application/x-arc", mimeType);
    }

    public void testTIFF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test_nocompress.tif"));
        assertEquals("image/tiff", mimeType);
    }

    public void testGIF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.gif"));
        assertEquals("image/gif", mimeType);
    }

    public void testDVI() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.dvi"));
        assertEquals("application/x-dvi", mimeType);
    }

    public void testEXE() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.exe"));
        assertEquals("application/x-dosexec", mimeType);
    }

    public void testAI() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.ai"));
        assertEquals("application/postscript", mimeType);
    }

    public void testCUR() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.cur"));
        assertEquals("application/x-123", mimeType);
    }

    public void testMDE() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.mde"));
        assertEquals("application/msaccess", mimeType);
    }

    public void testMDB() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.mdb"));
        assertEquals("application/msaccess", mimeType);
    }

    public void testSWF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.swf"));
        assertEquals("application/x-shockwave-flash", mimeType);
    }

    public void testRTF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.rtf"));
        assertEquals("text/rtf", mimeType);
    }

    public void testExcel2K() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test_excel_2000.xls"));
        assertEquals("application/msword", mimeType);
    }

    public void testPDF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.pdf"));
        assertEquals("application/pdf", mimeType);
    }

    public void testEMF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.emf"));
        assertEquals("video/unknown", mimeType);
    }

    public void testFLI() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.fli"));
        assertEquals("application/octet-stream", mimeType);
    }

    public void testPNM() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.pnm"));
        assertEquals("image/x-portable-pixmap", mimeType);
    }

    public void testPPM() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.ppm"));
        assertEquals("image/x-portable-pixmap", mimeType);
    }

    public void testDTD() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.dtd"));
        assertEquals("text/plain", mimeType);
    }

    public void testICO() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.ico"));
        assertEquals("application/octet-stream", mimeType);
    }

    public void testPPS() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.pps"));
        assertEquals("application/msword", mimeType);
    }

    public void testPL() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.pl"));
        assertEquals("application/x-perl", mimeType);
    }

    public void testPNG() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.png"));
        assertEquals("image/png", mimeType);
    }

    public void testJPEG() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.jpg"));
        assertEquals("image/jpeg", mimeType);
    }

    public void testTGA() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.tga"));
        assertEquals("application/octet-stream", mimeType);
    }

    public void testPPT() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.ppt"));
        assertEquals("application/msword", mimeType);
    }

    public void testXPM() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.xpm"));
        assertEquals("image/x-xpm", mimeType);
    }

    public void testPSD() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.psd"));
        assertEquals("image/x-photoshop", mimeType);
    }

    public void testText() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.txt"));
        assertEquals("text/plain", mimeType);
    }

    public void testMP3() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(getResourceAsStream(
            "test.mp3"));
        assertEquals("audio/mpeg", mimeType);
    }

    public void testBMP() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.bmp"));
        assertEquals("image/x-ms-bmp", mimeType);
    }

    public void testTAR() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.tar"));
        assertEquals("application/x-tar", mimeType);
    }

    public void testOgg() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.ogg"));
        assertEquals("application/ogg", mimeType);
    }

    public void testEPS() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.eps"));
        assertEquals("application/postscript", mimeType);
    }

    public void testXML() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.xml"));
        assertEquals("text/xml", mimeType);
    }

    public void testWord2K() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test_word_2000.doc"));
        assertEquals("application/msword", mimeType);
    }

    public void testWord95() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test_word_6.0_95.doc"));
        assertEquals("application/msword", mimeType);
    }

    public void testPostscript() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.ps"));
        assertEquals("application/postscript", mimeType);
    }

    public void testHTML() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.html"));
        assertEquals("text/html", mimeType);
    }

    public void testSH() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.sh"));
        assertEquals("application/x-shellscript", mimeType);
    }

    public void testMMF() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.mmf"));
        assertEquals("application/vnd.smaf", mimeType);
    }

    public void testQCELP() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.qcp"));
        assertEquals("audio/qcelp", mimeType);
    }

    public void testAMR() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.amr"));
        assertEquals("audio/amr", mimeType);
    }

    public void testAMRWB() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.awb"));
        assertEquals("audio/amr-wb", mimeType);
    }

    public void testAAC() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.m4a"));
        assertEquals("audio/aac", mimeType);
    }

    public void testMLD() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.mld"));
        assertEquals("audio/imelody", mimeType);
    }

    public void testPMD() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.pmd"));
        assertEquals("application/x-pmd", mimeType);
    }

    public void testWMA() throws Exception {
        String mimeType = mimeDiscoverer.discoverMimeType(
            getResourceAsStream("test.wma"));
        assertEquals("audio/x-ms-wma", mimeType);
    }

    /**
     * Helper method to load the test resources
     *
     * @param name the name of the resource in the package-relative samples
     *             package
     * @return a non-null InputStream.
     */
    private ImageInputStream getResourceAsStream(String name)
        throws IOException {
        ImageInputStream result = ImageIO.createImageInputStream(
            MimeDiscoverImageInputStreamTestCase.class.getResourceAsStream(
            "samples/" + name));
        assertNotNull("unable to load resource: " + name, result);
        return result;
    }

    protected void setUp() throws Exception {
        mimeDiscoverer = new DefaultMimeDiscoverer();
    }

}
