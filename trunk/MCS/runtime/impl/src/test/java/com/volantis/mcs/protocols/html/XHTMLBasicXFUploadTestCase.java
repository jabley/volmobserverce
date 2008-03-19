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

package com.volantis.mcs.protocols.html;

import java.util.Collections;
import java.util.HashMap;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.integration.PageURLRewriterMock;
import com.volantis.mcs.integration.TestURLRewriter;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.XFUploadTestCaseAbstract;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.StylesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;

/**
 * Tests XFUpload element against XHTMLprotocol
 * 
 * The expected results are:
 * <ul>
 * <li>The markup produced by the element be input of "file" type
 * <li>The form markup should have 'enctype' attribute 
 *     set to "multipart/form-data"
 * </ul>  
 */
public class XHTMLBasicXFUploadTestCase extends XFUploadTestCaseAbstract {
    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create all the necessary mocks 
        super.setUp();        

        // Add XHTML-specific mocks and expectations
        MutablePropertyValuesMock stylePropertyValuesMock 
            = new  MutablePropertyValuesMock("stylePVMock", expectations);    
        stylePropertyValuesMock
            .expects.stylePropertyIterator()
            .returns(Collections.EMPTY_LIST.iterator());
    
        StylesMock stylesMock = new StylesMock("stylesMock", expectations);        
        stylesMock
            .expects.getPropertyValues()
            .returns(stylePropertyValuesMock);
        
        formAttrs.setStyles(stylesMock);
    
        
        TestURLRewriter urlRewriter = new TestURLRewriter();      
        marinerPageContextMock
            .expects.getSessionURLRewriter()
            .returns(urlRewriter);
        
        
        PageURLRewriterMock pageURLRewriter = new PageURLRewriterMock(
                "pageURLRewriterMock", expectations);    
        pageURLRewriter
            .fuzzy.rewriteURL(
                    requestContextMock, 
                    mockFactory.expectsAny(), mockFactory.expectsAny())
            .returns(new MarinerURL("/"));        
        volantisMock
            .expects.getLayoutURLRewriter()
            .returns(pageURLRewriter);
    }

    /**
     * Test the produced markup
     */
    public void testForm() throws Exception {                                

        // Run the actual test        
        doTestForm();

        // Check the results        
        assertEquals("<input name=\"fileToUpload\" type=\"file\"/>",
                domToString(uploadDOM));        
        assertTrue("Enclosing form should have enctype attribute set ", 
                domToString(formDOM).indexOf("enctype=\"multipart/form-data\"") > 0);
    }
    
        
    /**
     * Create XHTMLBasic protocol to test against.
     */    
    protected DOMProtocol createProtocol() {
        ProtocolBuilder builder = new ProtocolBuilder();

        final DefaultDevice defaultDevice =
            new DefaultDevice("device", new HashMap(), null);
        defaultDevice.setPolicyValue(
            DevicePolicyConstants.SUPPORTS_JAVASCRIPT, "true");
    
        InternalDevice device =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);
        DOMProtocol protocol = (DOMProtocol)builder.build(
            new ProtocolRegistry.XHTMLBasicFactory(), device);

        return protocol;
    }
}
