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

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.dom.DOMFactoryMock;
import com.volantis.mcs.dom.ElementMock;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.protocols.ValidationHelperMock;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.mcs.runtime.configuration.ProtocolsConfigurationMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.ExpectationBuilder;

/**
 * Base class for protocol unit tests.
 */
public abstract class ProtocolUnitTestAbstract
        extends TestCaseAbstract {

    protected ExpectationBuilder expectations;
    protected VolantisProtocol protocol;
    protected MarinerPageContextMock marinerPageContextMock;
    protected ProtocolSupportFactoryMock protocolSupportFactoryMock;
    protected VolantisMock volantisMock;
    protected ProtocolsConfigurationMock protocolsConfigurationMock;
    protected ProtocolConfigurationMock protocolConfigurationMock;
    protected ElementMock elementMock;
    protected InternalDeviceMock deviceMock;
    protected ValidationHelperMock validationHelperMock;
    protected DOMFactoryMock domFactoryMock;
    protected MarinerRequestContextMock marinerRequestContextMock;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        expectations = mockFactory.createUnorderedBuilder();

        marinerPageContextMock = new MarinerPageContextMock(
                "marinerPageContextMock", expectations);

        protocolSupportFactoryMock = new ProtocolSupportFactoryMock(
                "protocolSupportFactoryMock", expectations);

        volantisMock = new VolantisMock("volantisMock", expectations);

        domFactoryMock = new DOMFactoryMock("domFactoryMock", expectations);

        protocolsConfigurationMock = new ProtocolsConfigurationMock(
                "protocolsConfigurationMock", expectations);

        protocolConfigurationMock = new ProtocolConfigurationMock(
                "protocolConfigurationMock", expectations);

        elementMock = new ElementMock("element", expectations);

        deviceMock = new InternalDeviceMock("device", expectations);

        validationHelperMock = new ValidationHelperMock("validationHelper",
                expectations);

        marinerRequestContextMock = new MarinerRequestContextMock(
                "requestContext", expectations);
    }

    /**
     * Initialise the protocol using default expectations.
     */
    protected void initialiseProtocolWithDefaults() {

        addDefaultProtocolInitialiseExpectations();

        initialiseProtocolWithNoDefaults();
    }

    /**
     * Initialise the protocol without any expectations.
     * <p/>
     * <p>Before calling this method it is necessary to set up expectations
     * for when the protocol is initialised.</p>
     */
    protected void initialiseProtocolWithNoDefaults() {
        addCommonProtocolInitialiseExpectations();

        protocol = createProtocol(
                protocolSupportFactoryMock,
                protocolConfigurationMock);
        protocol.setMarinerPageContext(marinerPageContextMock);
        protocol.initialise();
    }

    /**
     * Add default expectations that are needed when initialising the protocol.
     * <p/>
     * <p>Derived classes should override and extend this method.</p>
     */
    protected void addDefaultProtocolInitialiseExpectations() {

        marinerPageContextMock.expects
                .getDevicePolicyValue("css.multiclass.support")
                .returns("true")
                .any();

        marinerPageContextMock.expects
                .getBooleanDevicePolicyValue("dial.link.supported")
                .returns(true)
                .any();

        marinerPageContextMock.expects
                .getDevicePolicyValue("dial.link.info.type")
                .returns(null)
                .any();

        marinerPageContextMock.expects
                .getDevicePolicyValue("protocol.stylesheet.location.device")
                .returns(null)
                .any();

        marinerPageContextMock.expects
                .getDevicePolicyValue("protocol.stylesheet.location.layout")
                .returns(null)
                .any();

        marinerPageContextMock.expects
                .getDevicePolicyValue("protocol.stylesheet.location.theme")
                .returns(null)
                .any();

        marinerPageContextMock.expects
                .getDevicePolicyValue("protocol.supports.accesskey")
                .returns(null)
                .any();

        marinerPageContextMock.expects
                .getDevicePolicyValue("nested.tables.support")
                .returns("default")
                .any();
    }

    /**
     * Add the expectations for the protocol initialise method.
     */
    protected void addCommonProtocolInitialiseExpectations() {

        elementMock.expects.setName(null);

        domFactoryMock.expects.createElement().returns(elementMock);

        protocolSupportFactoryMock.expects.getDOMFactory()
                .returns(domFactoryMock)
                .any();
    }

    /**
     * Create a DOMProtocol to test.
     *
     * @return A DOMProtocol to test.
     */
    protected abstract VolantisProtocol createProtocol(
            ProtocolSupportFactory protocolSupportFactory,
            ProtocolConfiguration protocolConfiguration);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 12-Jul-05	8990/3	pcameron	VBM:2005052606 Allow devices to override protocol optimisation of tables

 24-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
