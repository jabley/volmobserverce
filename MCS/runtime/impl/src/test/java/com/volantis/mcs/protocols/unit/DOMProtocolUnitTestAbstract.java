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

package com.volantis.mcs.protocols.unit;

import com.volantis.mcs.protocols.DOMProtocol;

/**
 * Base class for all DOMProtocol derived classes unit tests.
 */
public abstract class DOMProtocolUnitTestAbstract
        extends ProtocolUnitTestAbstract {

    /**
     * Type safe representation of the protocol initialised by the super
     * class.
     */
    protected DOMProtocol domProtocol;

    /**
     * Override to initialise the DOMProtocol field.
     */
    protected void initialiseProtocolWithNoDefaults() {
        super.initialiseProtocolWithNoDefaults();

        domProtocol = (DOMProtocol) protocol;
    }

    // Javadoc inherited.
    protected void addCommonProtocolInitialiseExpectations() {
        super.addCommonProtocolInitialiseExpectations();

        marinerPageContextMock.expects
                .getVolantisBean()
                .returns(volantisMock)
                .any();

        volantisMock.expects.getProtocolsConfiguration()
                .returns(protocolsConfigurationMock)
                .any();

        protocolConfigurationMock.expects.getValidationHelper()
                .returns(validationHelperMock);
    }

    // Javadoc inherited.
    protected void addDefaultProtocolInitialiseExpectations() {
        super.addDefaultProtocolInitialiseExpectations();

        marinerPageContextMock.expects
                .getBooleanDevicePolicyValue("stylesheets")
                .returns(true).any();
    }

    public void testDUMMY() {
        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 24-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
