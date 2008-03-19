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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.unit.wml;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertiesRendererMock;
import com.volantis.mcs.protocols.unit.DOMProtocolUnitTestAbstract;
import com.volantis.mcs.protocols.wml.WMLRootConfigurationMock;

/**
 * Base class of all WML protocol unit tests.
 */
public abstract class WMLRootUnitTestAbstract
        extends DOMProtocolUnitTestAbstract {

    private WMLRootConfigurationMock wmlRootConfigurationMock;
    private StyleEmulationPropertiesRendererMock
            styleEmulationPropertiesRendererMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        styleEmulationPropertiesRendererMock
                = new StyleEmulationPropertiesRendererMock(
                "styleEmulationPropertiesRendererMock", expectations);
    }

    // Javadoc inherited.
    protected void addCommonProtocolInitialiseExpectations() {
        super.addCommonProtocolInitialiseExpectations();

        marinerPageContextMock.expects.getDevice()
                .returns(deviceMock).any();

        marinerPageContextMock.expects.getRequestContext()
                .returns(marinerRequestContextMock)
                .any();

        wmlRootConfigurationMock.expects
                .getStyleEmulationPropertyRendererSelector()
                .returns(styleEmulationPropertiesRendererMock)
                .any();
    }

    // Javadoc inherited.
    protected void addDefaultProtocolInitialiseExpectations() {
        super.addDefaultProtocolInitialiseExpectations();

        marinerPageContextMock.expects
                .getDevicePolicyValue("maxwmldeck")
                .returns(null);

        deviceMock.expects.getStyleSheetVersion()
                .returns(DevicePolicyConstants.CSS_WAP)
                .any();

        marinerPageContextMock.expects
                .getBooleanDevicePolicyValue("protocol.wml.emulate.horizontal")
                .returns(false)
                .any();

        marinerRequestContextMock.expects
                .getDevicePolicyValue("wmlcsupport")
                .returns("false")
                .any();
    }

    /*public void testDUMMY() {

    }*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 03-Oct-05	9522/3	ibush	VBM:2005091502 no_save on images

 14-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 24-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
