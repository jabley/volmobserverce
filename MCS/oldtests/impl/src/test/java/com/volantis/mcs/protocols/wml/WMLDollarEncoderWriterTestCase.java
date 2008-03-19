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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/Attic/WMLDollarEncoderWriterTestCase.java,v 1.1.2.1 2003/04/16 15:41:28 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Apr-03    steve           VBM:2003041501   Created.
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.protocols.wml;


import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * WMLDollarEncoderWriterTestCase
 * 
 * @author steve
 *
 */
public class WMLDollarEncoderWriterTestCase extends TestCase {

    /**
     * Constructor for WMLOutputterTestCase.
     * @param arg0
     */
    public WMLDollarEncoderWriterTestCase(String arg0) {
        super(arg0);
    }

    public void testSimpleVariable() throws IOException {
        
        String wml = "<postfield name='drink' value='$drink'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_NOBRACKETS + "drink" + WMLVariable.WMLV_NOBRACKETS + 
             "'/>";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testBracketVariable() throws IOException {
        String wml = "<postfield name='drink' value='$(drink)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_BRACKETED + 
             "'/>";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
        
    public void testEscapeVariable1() throws IOException {
        String wml = "<postfield name='drink' value='$(drink:e)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_ESCAPE + 
             "'/>";

        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }

    public void testEscapeVariable2() throws IOException {
        String wml = "<postfield name='drink' value='$(drink:escape)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_ESCAPE + 
             "'/>";

        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testUnescapeVariable1() throws IOException {
        String wml = "<postfield name='drink' value='$(drink:u)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_UNESC + 
             "'/>";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testUnescapeVariable2() throws IOException {
        String wml = "<postfield name='drink' value='$(drink:unesc)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_UNESC + 
             "'/>";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testNoescapeVariable1() throws IOException {
        String wml = "<postfield name='drink' value='$(drink:n)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_NOESC + 
             "'/>";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }

    public void testNoescapeVariable2() throws IOException {
        String wml = "<postfield name='drink' value='$(drink:noesc)'/>";
        String expected = "<postfield name='drink' value='" + 
             WMLVariable.WMLV_BRACKETED + "drink" + WMLVariable.WMLV_NOESC + 
             "'/>";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testMixture() throws IOException {
        String wml = "<p>Welcome, $(user)! $greeting_message</p><p>The price is $$17.00</p>";        
        String expected = "<p>Welcome, " +
                        WMLVariable.WMLV_BRACKETED + "user" + WMLVariable.WMLV_BRACKETED +
                        "! " +
                        WMLVariable.WMLV_NOBRACKETS + "greeting_message" + WMLVariable.WMLV_NOBRACKETS +
                        "</p><p>The price is " + '$' + "17.00</p>";        
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testNoBracketTerm() throws IOException {
        String wml = "Welcome to $user's grotto. A typical typo $fred)";
        String expected = "Welcome to " +
            WMLVariable.WMLV_NOBRACKETS + "user" + WMLVariable.WMLV_NOBRACKETS +
            "'s grotto. A typical typo " +
            WMLVariable.WMLV_NOBRACKETS + "fred" + WMLVariable.WMLV_NOBRACKETS +
            ")" ;
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testIllegalVariableCharacter() {
        String wml = "This is illegal $(fred')";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        try {
            encoder.write( wml );
            fail( "IOException expected.");
        }
        catch( IOException ioe ) {
            // Expected
        }
    }

    public void testIllegalMissingBracket() {
        String wml = "This has a missing bracket $(fred carry on.";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        try {
            encoder.write( wml );
            fail( "IOException expected.");
        }
        catch( IOException ioe ) {
            // Expected
        }
    }

    public void testIllegalEscapeName() {
        String wml = "Illegal escape character $(fred:wibble)";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        try {
            encoder.write( wml );
            fail( "IOException expected.");
        }
        catch( IOException ioe ) {
            // Expected
        }
    }

    public void testIllegalEscapeOneChar() {
        String wml = "Illegal escape character $(fred:q)";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        try {
            encoder.write( wml );
            fail( "IOException expected.");
        }
        catch( IOException ioe ) {
            // Expected
        }
    }

    public void testEscapeNoBrackets() throws IOException {
        String wml = "Welcome $user:escape, that will be $$0.80";
        String expected = "Welcome " +
            WMLVariable.WMLV_NOBRACKETS + "user" + WMLVariable.WMLV_NOBRACKETS +
            ":escape, that will be " + '$' + "0.80";
             
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        assertEquals( expected, actual.toString() );
    }
    
    public void testFlushNoBrackets() throws IOException {
        String wml = "This is a finished variable $abcd";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        encoder.flush();
    }

    public void testFlushMidVariable() {
        String wml = "This is an unfinished variable $(fred";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        try {
            encoder.write( wml );
            encoder.flush();
            fail( "IOException expected.");
        }
        catch( IOException ioe ) {
            // Expected
        }        
    }

    public void testFlushMidEscape() {
        String wml = "This is an unfinished variable $(fred:esc";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        try {
            encoder.write( wml );
            encoder.flush();
            fail( "IOException expected.");
        }
        catch( IOException ioe ) {
            // Expected
        }        
    }

    public void testFlushBrackets() throws IOException {
        String wml = "This is a finished variable $(abcd)";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        encoder.flush();
    }

    public void testFlushEscaped() throws IOException {
        String wml = "This is a finished variable $(abcd:noesc)";
        StringWriter actual = new StringWriter();
        WMLDollarEncoderWriter encoder = new WMLDollarEncoderWriter( actual );
        encoder.write( wml );
        encoder.flush();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3386/8	steve	VBM:2004030901 Supermerged and merged back with Proteus

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 23-Jan-04	2736/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
