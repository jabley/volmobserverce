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
 * $Header: /src/mps/testsuite/unit/com/volantis/mps/message/ProtocolIndependentMessageTestCase.java,v 1.1 2002/11/22 13:18:38 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Steve           VBM:2002091806 - test case for 
 *                              ProtocolIndependentMessage
 * ----------------------------------------------------------------------------
 */


package com.volantis.mps.message;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import junit.framework.*;

public class ProtocolIndependentMessageTestCase extends TestCase
{
    
    public ProtocolIndependentMessageTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite()
    {
        TestSuite suite = new TestSuite(ProtocolIndependentMessageTestCase.class);
        
        return suite;
    }
    
    /** Test of getMessage method, of class com.volantis.mps.message.ProtocolIndependentMessage. */
    public void testGetMessage()
    {
        System.out.println("testGetMessage");

        ProtocolIndependentMessage msg = createMessage();
        assertEquals( "Hello mum", msg.getMessage() );
    }

    /**
     * A utility method to return a basic initialised instance of a message for
     * use in the various tests.
     *
     * @return A newly created message.
     */
    private ProtocolIndependentMessage createMessage() {
        Map assetMap = new HashMap();
        assetMap.put("Asset1", new MessageAsset("fred"));

        ProtocolIndependentMessage msg =
                new ProtocolIndependentMessage("Hello mum",
                                               "text/plain",
                                               assetMap);
        return msg;
    }

    private ProtocolIndependentMessage createMessage(String encoding) {
        Map assetMap = new HashMap();
        assetMap.put("Asset1", new MessageAsset("fred"));

        ProtocolIndependentMessage msg =
                new ProtocolIndependentMessage("Hello mum",
                                               "text/plain",
                                               assetMap,
                                               encoding);
        return msg;
    }

    /** Test of getMimeType method, of class com.volantis.mps.message.ProtocolIndependentMessage. */
    public void testGetMimeType()
    {
        System.out.println("testGetMimeType");

        ProtocolIndependentMessage msg = createMessage();
        assertEquals( "text/plain", msg.getMimeType() );
    }
    
    /** Test of getAssetMap method, of class com.volantis.mps.message.ProtocolIndependentMessage. */
    public void testGetAssetMap()
    {
        System.out.println("testGetAssetMap");

        Map assetMap = new HashMap();
        assetMap.put( "Asset1", new MessageAsset( "fred" ) );
        assetMap.put( "Asset2", new MessageAsset( "wilma" ) );
        assetMap.put( "Asset3", new MessageAsset( "bambam" ) );
        
        ProtocolIndependentMessage msg = new ProtocolIndependentMessage(
            "Hello mum", "text/plain", assetMap );
        
        Map assets = msg.getAssetMap();
        assertNotNull( assets );
        
        MessageAsset asset = (MessageAsset)assets.get( "Asset1" );
        assertNotNull( asset );
        assertEquals( "fred", asset.getText() );
        
        asset = (MessageAsset)assets.get( "Asset2" );
        assertNotNull( asset );
        assertEquals( "wilma", asset.getText() );

        asset = (MessageAsset)assets.get( "Asset3" );
        assertNotNull( asset );
        assertEquals( "bambam", asset.getText() );

        asset = (MessageAsset)assets.get( "Asset4" );
        assertNull( asset );
    }
    
    /** Test of getBaseURL method, of class com.volantis.mps.message.ProtocolIndependentMessage. */
    public void testGetBaseURL()
    {
        System.out.println("testGetBaseURL");

        ProtocolIndependentMessage msg = createMessage();

        try {
            msg.setBaseURL( new URL( "http://localhost:8080/volantis/test.jsp" ));
        }
        catch( MalformedURLException mue ) {
            fail( "Invalid URL." );
        }
        
        URL url = msg.getBaseURL();
        assertNotNull( url );
        assertEquals( "http", url.getProtocol() );
        assertEquals( "localhost", url.getHost() );
        assertEquals( 8080, url.getPort() );
        assertEquals( "/volantis/test.jsp", url.getFile() );
    }

    /**
     * This tests the {set|get}MaxMMSize methods.
     */
    public void testGetMaxMMSize() throws Exception {
        ProtocolIndependentMessage msg = createMessage();
        Integer maxSize = new Integer(10);
        msg.setMaxMMSize(maxSize);
        Integer result = msg.getMaxMMSize();
        assertNotNull("Maximum message size should exist", result);
        assertSame("Maximum sizes should be the same", maxSize, result);
    }

    /**
     * This tests the {set|get}MaxFileSize methods.
     */
    public void testGetMaxFileSize() throws Exception {
        ProtocolIndependentMessage msg = createMessage();
        Integer maxSize = new Integer(10);
        msg.setMaxFileSize(maxSize);
        Integer result = msg.getMaxFileSize();
        assertNotNull("Maximum file size should exist", result);
        assertSame("Maximum sizes should be the same", maxSize, result);

    }

    /**
     * Tests the {set|get}CharacterEncoding methods
     */
    public void testGetCharacterEncoding() throws Exception {
        ProtocolIndependentMessage encodedMsg = createMessage("Big5");
        ProtocolIndependentMessage nonEncodedMsg = createMessage(null);
        ProtocolIndependentMessage origMsg = createMessage();

        assertNotNull("Encoding should be specified",
                                encodedMsg.getCharacterEncoding());
        assertNull("Encoding should be null (not specified)",
                                nonEncodedMsg.getCharacterEncoding());
        assertNull("Encoding should be null (not specified)",
                                origMsg.getCharacterEncoding());
        assertEquals("Encoding should be Big5",
                        encodedMsg.getCharacterEncoding(),
                        "Big5");
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

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 14-Jul-04	133/1	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 ===========================================================================
*/
