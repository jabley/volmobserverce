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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.marlin.sax;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class tests NamespaceSwitchContentHandler
 */
public class NamespaceSwitchContentHandlerMapTestCase extends TestCaseAbstract {
    
    /**
     * Test the method getContentHandlerFactory.
     */ 
    public void testGetContentHandlerFactory() throws Exception {

        NamespaceSwitchContentHandlerMap map =
                NamespaceSwitchContentHandlerMap.getInstance();

        // Create an instance of a handler factory for use in testing
        AbstractContentHandlerFactory handlerFactory = 
                new IAPIContentHandlerFactory();
        // and add it to the map
        map.addContentHandler("http://www.volantis.com/ournamespace",
                handlerFactory);

        assertSame("Unexpected ContentHandler returned.",
                map.getContentHandlerFactory(
                        "http://www.volantis.com/ournamespace"),
                handlerFactory);

        assertNull("Expected null to be returned",
                map.getContentHandlerFactory("doesnotexist"));

        assertNull("Expected null to be returned",
                map.getContentHandlerFactory(null));
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-May-04	4513/1	adrian	VBM:2004052007 Updated NamespaceSwitchContentHandler to use a HashMap instead of HashTable

 ===========================================================================
*/
