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
package com.volantis.mcs.eclipse.ab.editors.devices.odom;

import com.volantis.mcs.eclipse.common.odom.ODOMElementTestCase;
import junitx.util.PrivateAccessor;

import java.util.List;

/**
 * Test case for DeviceODOMElement
 */
public class DeviceODOMElementTestCase extends ODOMElementTestCase {
    /**
     * Test that submitRestorableName creates a StandardElementHandler
     * and that it does this only if the name has not already been submitted.
     */
    public void testSubmitRestorableName() throws Exception {
        DeviceODOMElementFactory factory = new DeviceODOMElementFactory();
        DeviceODOMElement policies =
                (DeviceODOMElement) factory.element("policies",
                        com.volantis.mcs.eclipse.common.odom.MCSNamespace.DEVICE);
        policies.submitRestorableName("pixelsX");

        List restorables = (List)
                    PrivateAccessor.getField(policies,
                            "standardElementHandlerList");

        assertEquals("Expected the number of restorables to be 1.",
                1, restorables.size());

        policies.submitRestorableName("pixelsX");
        assertEquals("Expected the number of restorables to still be 1.",
                1, restorables.size());

        // Ensure that a StandardElementHandler has been created and attaches
        // itself to the pixelsX element if it is created.
        DeviceODOMElement policy = (DeviceODOMElement) factory.element("policy",
                com.volantis.mcs.eclipse.common.odom.MCSNamespace.DEVICE);
        policy.setAttribute("name", "pixelsX");
        policies.addContent(policy);

        StandardElementHandler seh = (StandardElementHandler)
                PrivateAccessor.getField(policy,
                "standardElementHandler");

        assertNotNull("There should be a StandardElementHandler on policy.",
                seh);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-04	4394/3	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 ===========================================================================
*/
