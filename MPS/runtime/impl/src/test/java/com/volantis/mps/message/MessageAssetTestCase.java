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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/message/MessageAssetTestCase.java,v 1.1 2002/11/22 12:40:11 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Steve           VBM:2002102403 - Unit test for MessageAsset
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.message;

import junit.framework.*;

public class MessageAssetTestCase extends TestCase
{
    
    public MessageAssetTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(MessageAssetTestCase.class);
        
        return suite;
    }
    
    /** Test of getLocationType method, of class com.volantis.mps.message.MessageAsset. */
    public void testGetLocationType()
    {
        System.out.println("testGetLocationType");
        
        MessageAsset asset = new MessageAsset( MessageAsset.ON_DEVICE, 
                                    "http://localhost:8080/mps/messages" );
        assertEquals( MessageAsset.ON_DEVICE, asset.getLocationType() );
        
        asset = new MessageAsset( MessageAsset.ON_SERVER, 
                                    "http://localhost:8080/mps/messages" );
        assertEquals( MessageAsset.ON_SERVER, asset.getLocationType() );
        
        asset = new MessageAsset( "hello" );
        assertEquals( MessageAsset.ON_DEVICE, asset.getLocationType() );
    }
    
    /** Test of getUrl method, of class com.volantis.mps.message.MessageAsset. */
    public void testGetUrl()
    {
        System.out.println("testGetUrl");

        MessageAsset asset = new MessageAsset( MessageAsset.ON_DEVICE, 
                                    "http://localhost:8080/mps/messages" );
        assertEquals( "http://localhost:8080/mps/messages", asset.getUrl() );

        asset = new MessageAsset( MessageAsset.ON_SERVER, 
                                    "http://localhost:8080/mps/messages" );
        assertEquals( "http://localhost:8080/mps/messages", asset.getUrl() );

        asset = new MessageAsset( "hello" );
        assertNull( asset.getUrl() );
    }
    
    /** Test of getText method, of class com.volantis.mps.message.MessageAsset. */
    public void testGetText()
    {
        System.out.println("testGetText");
        
        MessageAsset asset = new MessageAsset( MessageAsset.ON_DEVICE, 
                                    "http://localhost:8080/mps/messages" );
        assertNull( asset.getText() );

        asset = new MessageAsset( MessageAsset.ON_SERVER, 
                                    "http://localhost:8080/mps/messages" );
        assertNull( asset.getText() );

        asset = new MessageAsset( "hello" );
        assertEquals( "hello", asset.getText() );
    }
    
    /** Test of isTextMessage method, of class com.volantis.mps.message.MessageAsset. */
    public void testIsTextMessage()
    {
        System.out.println("testIsTextMessage");
        
        MessageAsset asset = new MessageAsset( MessageAsset.ON_DEVICE, 
                                    "http://localhost:8080/mps/messages" );
        assertEquals( false, asset.isTextMessage() );

        asset = new MessageAsset( MessageAsset.ON_SERVER, 
                                    "http://localhost:8080/mps/messages" );
        assertEquals( false, asset.isTextMessage() );

        asset = new MessageAsset( "hello" );
        assertEquals( true, asset.isTextMessage() );
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
}
