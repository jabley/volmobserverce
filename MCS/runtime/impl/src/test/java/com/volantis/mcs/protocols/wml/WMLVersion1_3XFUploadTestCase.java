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

package com.volantis.mcs.protocols.wml;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.XFUploadTestCaseAbstract;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;

import java.util.HashMap;

/**
 * Tests XFUpload element against WML version 1.3 protocol
 * 
 * The expected results are:
 * <ul>
 * <li>The markup produced by the element should be empty
 * <li>The form markup should not have 'enctype' attribute
 * </ul>  
 */
public class WMLVersion1_3XFUploadTestCase extends XFUploadTestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    // Javadoc inherited
    protected void setUp() throws Exception {
        
        // Let the base class create all the necessary mocks 
        super.setUp();        

        // Add WML-specific expectations
        formInstance
            .expects.getPreambleBuffer(true)
            .returns(formDOM);
        
        formInstance
            .expects.getPostambleBuffer(true)
            .returns(formDOM);
    }

    /**
     * Test the produced markup
     */
    public void testForm() throws Exception {                                

        // Run the actual test        
        doTestForm();
        
        // Check the results        
        assertEquals("", domToString(uploadDOM));        
        assertTrue("Enctype attribue should not be set", 
                domToString(formDOM).indexOf("enctype") == -1);
    }
    
        
    /**
     * Create WMLVersion1_3 protocol to test against.
     */    
    protected DOMProtocol createProtocol() {

        ProtocolBuilder builder = new ProtocolBuilder();

        final DefaultDevice defaultDevice =
            new DefaultDevice("Test Device", new HashMap(), null);
        defaultDevice.setPolicyValue(DevicePolicyConstants.WML_IMAGE_NOSAVE,
                DevicePolicyConstants.WML_IMAGE_NOSAVE__ALT_NO_SAVE);

        InternalDevice device =
            INTERNAL_DEVICE_FACTORY.createInternalDevice(defaultDevice);
        DOMProtocol protocol = (DOMProtocol)builder.build(
            new ProtocolRegistry.WMLVersion1_3Factory(), device);

        return protocol;
    }
}
