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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/CharsetEncodingWriterTestCase.java,v 1.1.2.1 2003/04/28 08:50:14 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Apr-03    Mat             VBM:2003033108 - Created to test the 
 *                              CharsetEncodingWriter
 * 02-May-03    Mat             VBM:2003042912 - Add ';' to the end of the 
 *                              encoded numbers.
 * 12-May-03    Mat             VBM:2003033108 - Changed to use the new 
 *                              CharsetEncodingWriter
 * 23-May-03    Mat             VBM:2003042907 - Added MIBEnum to Encoding
 *                              constructors.
 * ----------------------------------------------------------------------------
 */           

package com.volantis.charset;

import com.volantis.charset.BitSetEncoding;
import com.volantis.charset.NoEncoding;

import com.volantis.charset.CharsetEncodingWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import junit.framework.*;


/**
 * A test class for the CharsetEncodingWriter
 *
 * @author mat                       
 */                                
public class CharsetEncodingWriterTestCase extends TestCase {
    
    /** Test case for the CharsetEncodingWriter
    * @param testName The test name
     */
    public CharsetEncodingWriterTestCase(String testName) {
        super(testName);
    }        
    
    /**
     * Set up the bean and an empty ServletContext.
     */
    public void setUp() {
    }

    /**
     * Destroy the objects created by the previous set up.
     */
    public void tearDown() {
    }
    
    /**
    * Test the CharsetEncodingWriter with a BitSetEncoding.
    * This will test the writer using the ISO-8859-1 character set, which 
    * can represent all characters from 0-255.
    * We will test character 43775 and check that it gets encoded.
    * We will also test character 65 ('A') and check that it doesn't get 
    * encoded.
    * @throws Exception A problem 
    */
    public void testWriteBitSetEncoding() throws Exception {
        Writer writer = new StringWriter();
        Encoding encoding = new BitSetEncoding("iso-8859-1", 4);
        CharsetEncodingWriter csew = 
            new CharsetEncodingWriter(writer, encoding);
        String testString = "\uaaff";
        csew.write(testString);
        String result = writer.toString();
        assertEquals("Character not encoded", "&#43775;", result);
        // Now a character than can be represented - 'A'
        testString = "\u0041";
        writer = new StringWriter();
        csew = new CharsetEncodingWriter(writer, encoding);
        csew.write(testString);
        result = writer.toString();
        assertEquals("Character erroneously encoded", "A", result);
    }
    
    /** Test the CharsetEncodingWriter with a NoEncoding.
    * This will test the writer using the UTF-8 character set, which can
    * represent all characters.
    * We will test with character 43775, which shouldn't get encoded.
    * @throws Exception A problem
    */
    public void testWriteNoEncoding() throws Exception {
        Writer writer = new StringWriter();
        Encoding encoding = new NoEncoding("utf8", 106);
        CharsetEncodingWriter csew = new CharsetEncodingWriter(writer, encoding);
        // Test char 43775 - should not be encoded.
        String testString = "\uaaff";
        csew.write(testString);
        String result = writer.toString();
        assertEquals("Character not encoded", "\uaaff", result);
    }

    /**
    * Test the CharsetEncodingWriter with a BitSetEncoding.
    * This will test the writer using the GB18030 character set (which
    * supersedes EUC_CN (GB2312)), which can represent all characters from
    *  0-128 and supports multibyte characters (>128) which represent chinese
    * symbols.
    */
    public void testWriteChinese() throws Exception {
        Writer writer = new StringWriter();
        Encoding encoding = new BitSetEncoding("GB18030", 2025);
        CharsetEncodingWriter csew = 
            new CharsetEncodingWriter(writer, encoding);

        // All of these are valid chinese characters
        char[] chars = new char[] {
            'a','c','c','d','e','f','g','h','i','j','k',
            'A','B','C','D','E','F','G','H','I','J','K',
            '0','1','2','3','4','5','6','7','8','9',
            0xd6b2, 0xbfc3, 0xd6ea, 0xcda9, 0xe9ab,
            0xbcd2, 0xabc7, 0xbec0, 0xd6b7, 0xa9d0};
        csew.write(chars);
        String result = writer.toString();
        char[] out = result.toCharArray();
        assertEquals( chars.length, out.length );
        for( int i = 0; i < chars.length; i++ ){
            if( chars[i] != out[i] ) {
                fail( "Invalid character " + out[i] + " at index " + i );
            }
        }

        // These are not valid chinese characters
        char[] invalidChars = new char[] {
            195,'a', 185,'z', 210,'c', 188,'d', 208,'y', 208,'q'
        };
        writer = new StringWriter();
        csew = new CharsetEncodingWriter(writer, encoding);
        csew.write(invalidChars);
        result = writer.toString();
        assertEquals("Output not as expected.",
            "&#195;a&#185;z&#210;c&#188;d&#208;y&#208;q", result );

        // Finally test some specific characters
        CharacterRepresentable rep;
        rep = encoding.checkCharacter(32654);
        assertEquals( "Character should be representable",
                      true, rep.isRepresentable());

        rep = encoding.checkCharacter(22269);
        assertEquals( "Character should be representable",
                      true, rep.isRepresentable());

        rep = encoding.checkCharacter(19968);
        assertEquals( "Character should be representable",
                      true, rep.isRepresentable());

        rep = encoding.checkCharacter(214);
        assertEquals( "Character should not be representable",
                      true, rep.notRepresentable());

        rep = encoding.checkCharacter(188);
        assertEquals( "Character should not be representable",
                      true, rep.notRepresentable());
    }

     public void testWriteCharArrayWithOffset() throws Exception {
        Writer writer = new StringWriter();
        Encoding encoding = new BitSetEncoding("iso-8859-1", 4);
        CharsetEncodingWriter csew =
            new CharsetEncodingWriter(writer, encoding);
        char[] testArr = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        String expected = "de";
        csew.write(testArr, 3, 2);
        String result = writer.toString();
        assertEquals("Characters incorrectly written", expected, result);
     }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Jan-04	2653/5	steve	VBM:2004011304 Remove visibility of constants

 19-Jan-04	2653/1	steve	VBM:2004011304 Merge from proteus

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 16-Jan-04	2576/1	steve	VBM:2004011304 Support multibyte character sets

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
