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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/message/MessageInternalsTestCase.java,v 1.1 2002/11/22 11:41:32 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Steve           VBM:2002111211 - Test case for message internals
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.message;

import java.util.Map;
import junit.framework.*;

public class MessageInternalsTestCase extends TestCase
{
    
    public MessageInternalsTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(MessageInternalsTestCase.class);
        
        return suite;
    }
    
    /** Test of getHeaders method, of class com.volantis.mps.message.MessageInternals. */
    public void testGetHeaders() throws MessageException {
        System.out.println("testGetHeaders");

        MultiChannelMessage msg = new MultiChannelMessageImpl();
        
        msg.addHeader( MultiChannelMessage.ALL, "all1", "header1" );
        msg.addHeader( MultiChannelMessage.ALL, "all2", "header2" );

        msg.addHeader( MultiChannelMessage.MHTML, "mhtml1", "header3" );
        msg.addHeader( MultiChannelMessage.MHTML, "mhtml2", "header4" );

        msg.addHeader( MultiChannelMessage.MMS, "mms1", "header5" );
        msg.addHeader( MultiChannelMessage.MMS, "mms2", "header6" );
        
        Map all = null, mhtml = null, mms = null;
        all = MessageInternals.getHeaders( msg, MultiChannelMessage.ALL );
        mhtml = 
              MessageInternals.getHeaders( msg, MultiChannelMessage.MHTML );
        mms = MessageInternals.getHeaders( msg, MultiChannelMessage.MMS );
        
        String value;
        
        value = (String)all.get( "all1" );
        assertEquals( value, "header1" );
        value = (String)all.get( "all2" );
        assertEquals( value, "header2" );
        value = (String)all.get( "mhtml1" );
        assertNull( value );
        value = (String)all.get( "mhtml2" );
        assertNull( value );
        value = (String)all.get( "mms1" );
        assertNull( value );
        value = (String)all.get( "mms2" );
        assertNull( value );

        value = (String)mhtml.get( "all1" );
        assertNull( value );
        value = (String)mhtml.get( "all2" );
        assertNull( value );
        value = (String)mhtml.get( "mhtml1" );
        assertEquals( value, "header3" );
        value = (String)mhtml.get( "mhtml2" );
        assertEquals( value, "header4" );
        value = (String)mhtml.get( "mms1" );
        assertNull( value );
        value = (String)mhtml.get( "mms2" );
        assertNull( value );
        
        value = (String)mms.get( "all1" );
        assertNull( value );
        value = (String)mms.get( "all2" );
        assertNull( value );
        value = (String)mms.get( "mhtml1" );
        assertNull( value );
        value = (String)mms.get( "mhtml2" );
        assertNull( value );
        value = (String)mms.get( "mms1" );
        assertEquals( value, "header5" );
        value = (String)mms.get( "mms2" );
        assertEquals( value, "header6" );
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
