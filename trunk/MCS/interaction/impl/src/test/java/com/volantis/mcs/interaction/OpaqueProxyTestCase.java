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

package com.volantis.mcs.interaction;

import com.volantis.mcs.interaction.event.InteractionEventListenerMock;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.interaction.impl.OpaqueProxyImpl;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptorMock;

public class OpaqueProxyTestCase
        extends ProxyTestAbstract {

    private static final String OLD_MODEL_OBJECT = "OLD";
    private static final String NEW_MODEL_OBJECT = "NEW";

    private OpaqueClassDescriptorMock opaqueClassDescriptorMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        opaqueClassDescriptorMock = new OpaqueClassDescriptorMock(
                "opaqueClassDescriptorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        opaqueClassDescriptorMock.expects.getTypeClass().returns(String.class).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

    }

    protected InternalProxy createProxy() {
        return new OpaqueProxyImpl(proxyModelDescriptorMock,
                opaqueClassDescriptorMock);
    }

    /**
     * Test that preparing the set model object method updates the object
     * embedded in the parent's model object, updates the modification
     * count and generates the appropriate events.
     */
    public void testSetModelObject() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InteractionEventListenerMock listenerMock =
                new InteractionEventListenerMock("listenerMock", expectations);

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        final OpaqueProxyImpl simpleProxy = (OpaqueProxyImpl)
                createProxy(parentProxyMock);

        simpleProxy.addListener(listenerMock, false);

        // =====================================================================
        //   Set Test Object Specific Expectations
        // =====================================================================

        final ProxyModelChangedEvent event = new ProxyModelChangedEvent(
                simpleProxy, null, NEW_MODEL_OBJECT, true);
        listenerMock.expects.proxyModelChanged(event);
        parentProxyMock.expects.fireEvent(event);

        parentProxyMock.expects.isReadOnly().returns(false);

        parentProxyMock.expects
                .setEmbeddedModelObject(simpleProxy, NEW_MODEL_OBJECT)
                .returns(null);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        checkModificationCountIncremented(
                simpleProxy, new Modifier() {
                    public void modify() {
                        Object oldModelObject = simpleProxy.setModelObject(
                                NEW_MODEL_OBJECT);
                        assertEquals("Old model object", null, oldModelObject);
                    }
                });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/6	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
