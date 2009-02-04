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

package com.volantis.mcs.context;

import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.TestURLRewriter;
import com.volantis.mcs.protocols.VolantisProtocolMock;
import com.volantis.mcs.runtime.InternalRequestHeaders;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.VolantisMock;
import com.volantis.mcs.runtime.policies.PolicyActivatorMock;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactoryMock;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.StylingFactoryMock;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Integration test case for {@link MarinerPageContext}.
 */
public class MarinerPageContextTestCase  extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /**
     * Interaction testing of initialisePage() method
     * 
     * In particular the test verifies that:<ul>
     * <li>initialisePage() method succeedes
     * <li>asset resolver is initialized before protocol initialization (VBM 2006032022)
     * <li>asset resolver is initialized before protocol initialization (VBM 2006021602)
     * </ul>   
     */
    public void testInitialisePage() throws Exception {

        // Prepare mocks and stubs

        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(
            new DefaultDevice(null, null, null));
        final VolantisProtocolMock protocolMock =
                new VolantisProtocolMock("protocolMock", expectations, null);
        final StylingFactoryMock stylingFactoryMock = 
                new StylingFactoryMock("stylingFactoryMock", expectations);
        final MarinerRequestContextMock requestContextMock =
                new MarinerRequestContextMock("requestContext", expectations);
        final EnvironmentContextMock envContextMock =
                new EnvironmentContextMock("envContext", expectations);        
        final VolantisMock volantisMock = 
                new VolantisMock("volantisMock", expectations);
        final MarinerSessionContextMock sessionContextMock = 
                new MarinerSessionContextMock("sessionContextMock", expectations);
        final DefaultApplicationContext appContext =
                new DefaultApplicationContext(requestContextMock);
        appContext.setProtocol(protocolMock);
        
        // Create the context to be tested
        
        final MarinerPageContext context = new MarinerPageContext(stylingFactoryMock);

        // Set expectations for interaction testing of the context
        
        requestContextMock.expects.getEnvironmentContext().returns(envContextMock).any();
        requestContextMock.expects.getApplicationContext().returns(appContext);

        envContextMock.expects.getSessionContext().returns(sessionContextMock);
        envContextMock.expects.getExpressionContext().returns(null);
        envContextMock.expects.getSessionDevice().returns(device).any();

        final RuntimeProjectMock runtimeProjectMock =
                new RuntimeProjectMock("runtimeProjectMock", expectations);

        volantisMock.expects.getPageGenerationCache().returns(null);
        volantisMock.expects.getPolicyFetcher().returns(null);
        volantisMock.expects.getVariantSelectionPolicy().returns(null);
        volantisMock.expects.getURLRewriter().returns(new TestURLRewriter());
        volantisMock.expects.getDefaultProject().returns(runtimeProjectMock);

        final PolicyReferenceFactoryMock referenceFactoryMock =
                new PolicyReferenceFactoryMock("referenceFactoryMock",
                        expectations);

        volantisMock.expects.getPolicyReferenceFactory()
                .returns(referenceFactoryMock).any();

        volantisMock.expects.getPageURLRewriter().returns(null).any();

        volantisMock.expects.isIMDRepositoryEnabled().returns(true).any();

        final PolicyActivatorMock policyActivatorMock =
                new PolicyActivatorMock("policyActivatorMock", expectations);
        volantisMock.expects.getPolicyActivator()
                .returns(policyActivatorMock).any();

        sessionContextMock.expects.getDevice().returns(device);
        
        protocolMock.expects.setMarinerPageContext(context);            
        protocolMock.expects.initialise()
            .does(new MethodAction() {
                public Object perform(MethodActionEvent event) throws Throwable {
                    
                    // Make sure asset resolver has been initialized (VBM 2006032022)
                    assertNotNull(context.getAssetResolver());
                    // Make sure policy ref resolver has been initialized (VBM 2006021602)
                    assertNotNull(context.getPolicyReferenceResolver());
                    
                    return null;
                }                
            });
        
        // Call the method to be tested        
        context.initialisePage(
            volantisMock,
            requestContextMock,
            new MarinerRequestContextMock("enclosingRequestContext", expectations),
            new MarinerURL(),
            new InternalRequestHeaders());    

        // To make sure that page initialization succeeded, 
        // we call it again and expect IllegalStateException
        try {
            context.initialisePage(null, null, null, null, null);
            // If we reach this point, it means that previous initialization failed 
            fail("Page initialization failed");
        }
        catch (IllegalStateException expected) {
            // That's the expected behavior
        }
        catch (Exception unexpected) {
            fail("Page initialization failed.");
        }
    }
}
