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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/assembler/ContentUtilitiesTestCase.java,v 1.2 2002/12/09 18:22:55 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Nov-02    Steve           VBM:2002103008 - Test for content guessing
 * 09-Dec-02    Chris W         VBM:2002120913 - Added test for application/smil
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import junit.framework.*;

public class ContentUtilitiesTestCase extends TestCase
{
    
    public ContentUtilitiesTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(ContentUtilitiesTestCase.class);
        
        return suite;
    }
    
    /** Test that an HTML file is correctly identified */
    public void testGuessHTML() throws IOException {
        System.out.println("testGuessHTML");
        
            byte[] content = loadResource( "html" );
            assertEquals( ContentUtilities.CU_TEXT_HTML, 
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a WML file is correctly identified */
    public void testGuessWML() throws IOException {
        System.out.println("testGuessWML");

            byte[] content = loadResource( "wml" );
            assertEquals( ContentUtilities.CU_TEXT_WML, 
                          ContentUtilities.guessContentType( content ) );
    }
    
    /** Test that a plain text file is correctly identified */
    public void testGuessText() throws IOException {
        System.out.println("testGuessText");

            byte[] content = loadResource( "txt" );
            assertEquals( ContentUtilities.CU_TEXT_PLAIN, 
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a GIF Image is correctly identified */
    public void testGuessGIF() throws IOException {
        System.out.println("testGuessGIF");

            byte[] content = loadResource( "gif" );
            assertEquals( ContentUtilities.CU_IMAGE_GIF,
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a GIF Image is correctly identified */
    public void testGuessJPEG() throws IOException {
        System.out.println("testGuessJPEG");

            byte[] content = loadResource( "jpg" );
            assertEquals( ContentUtilities.CU_IMAGE_JPEG,
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a PNG Image is correctly identified */
    public void testGuessPNG() throws IOException {
        System.out.println("testGuessPNG");
        
            byte[] content = loadResource( "png" );
            assertEquals( ContentUtilities.CU_IMAGE_PNG,
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a TIFF Image is correctly identified */
    public void testGuessTIFF() throws IOException {
        System.out.println("testGuessTIFF");

            byte[] content = loadResource( "tif" );
            assertEquals( ContentUtilities.CU_IMAGE_TIFF,
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a WBMP Image is correctly identified */
    public void testGuessWBMP() throws IOException {
        System.out.println("testGuessWBMP");

            byte[] content = loadResource( "wbmp" );
            assertEquals( ContentUtilities.CU_IMAGE_WBMP,
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test that a WBMP Image is correctly identified */
    public void testGuessSMIL() throws IOException {
        System.out.println("testGuessSMIL");

            byte[] content = loadResource( "smil" );
            assertEquals( ContentUtilities.CU_APPLICATION_SMIL,
                          ContentUtilities.guessContentType( content ) );
    }

    /** Test the static method to convert Strings from one encoding to another **/
    public void testConvertEncoding() throws Exception {
        // test message with chinese characters
        final String msg = "<?xml version='1.0'?>" +
                           "<root>\u00e5\u0090\u0083\u00e7\u009a\u0084\u00e4" +
                           "\u017e\u0080\u00e9\u0081\u0093\u00e5\u0090\u008d" +
                           "\u00e8\u008f\u009c</root>";

        // test for NullPointerException
        try {
            ContentUtilities.convertEncoding(msg, null, "UTF-8");
            fail("Should fail with a null current encoding");
        } catch(NullPointerException e) {
            // this is expected
        }

        try {
            ContentUtilities.convertEncoding(null, "UTF-8", "UTF-8");
            fail("Should fail with a null content string encoding");
        } catch(NullPointerException e) {
            // this is expected
        }

        try {
            ContentUtilities.convertEncoding(null, null, "UTF-8");
            fail("Should fail with a null current encoding and content string");
        } catch(NullPointerException e) {
            // this is expected
        }

        try {
            ContentUtilities.convertEncoding(msg, "UTF-8", null);

        } catch(NullPointerException e) {
            // this is not expected
            fail("Should not fail with a null target encoding");
        }

        // test conversion
        byte[] utf8Byte = msg.getBytes("UTF-8");

        String utf8Msg = new String(utf8Byte, "UTF-8");
        String asciiMsg = new String(utf8Byte, "US-ASCII");
        String defMsg = new String(utf8Byte);

        // test converting from same encoding to same encoding
        String result = ContentUtilities.convertEncoding(utf8Msg, "UTF-8", "UTF-8");
        assertEquals("Strngs should match", result, utf8Msg);

        // test convert from utf-8 to us-ascii
        result = ContentUtilities.convertEncoding(utf8Msg, "UTF-8", "US-ASCII");
        assertEquals("Strings should match", asciiMsg, result);

        // test default encoding
        result = ContentUtilities.convertEncoding(utf8Msg, "UTF-8", null);
        assertEquals("Strings should match", defMsg, result);


    }

    /** Load a resource into a byte array */
    private byte[] loadResource( String name ) throws IOException
    {
        ClassLoader loader = this.getClass().getClassLoader();
        String resourceName = this.getClass().getName().replace( '.', '/' );
        resourceName = resourceName + "." + name;
                
        InputStream in = loader.getResourceAsStream( resourceName );
        if( in == null ) {
            throw new IOException( "Cannot find resource " + resourceName );
        }
        byte[] buffer = new byte[ in.available() ];
        in.read( buffer );
        return buffer;       
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
