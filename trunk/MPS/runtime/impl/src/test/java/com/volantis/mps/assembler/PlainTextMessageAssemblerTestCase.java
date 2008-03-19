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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/assembler/PlainTextMessageAssemblerTestCase.java,v 1.3 2003/01/09 15:34:12 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Steve           Test case for plain text message assembly.
 * 29-Nov-02    Sumit           VBM:2002112602 - assembleMessage in
 *                              testAssembleMessage has a new signature
 * 04-Dec-02    Ian             VBM:2002103006 - Recheckin under correct VBM.
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.assembler;

import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.ProtocolIndependentMessage;
import junit.framework.*;

public class PlainTextMessageAssemblerTestCase extends TestCase
{
    
    public PlainTextMessageAssemblerTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(PlainTextMessageAssemblerTestCase.class);
        
        return suite;
    }
    
    /** Test of assembleMessage method, of class com.volantis.mps.assembler.PlainTextMessageAssembler. */
    public void testAssembleMessage() throws MessageException {
        System.out.println("testAssembleMessage");
        
        ProtocolIndependentMessage msg = new ProtocolIndependentMessage(
                                    "I am a message", "text/plain", null );
        PlainTextMessageAssembler assembler = new PlainTextMessageAssembler();
        String txt = (String)assembler.assembleMessage( msg, null );
        assertEquals( "I am a message", txt );
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

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
