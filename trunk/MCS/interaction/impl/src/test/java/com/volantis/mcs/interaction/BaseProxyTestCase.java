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
import com.volantis.mcs.interaction.impl.BaseProxyImpl;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.interaction.impl.InternalProxyMock;
import com.volantis.mcs.model.descriptor.BaseClassDescriptorMock;

public class BaseProxyTestCase
        extends ProxyTestAbstract {

    private static final Object OLD_MODEL_OBJECT = new Object();
    private static final Object NEW_MODEL_OBJECT = new Object();

    private BaseClassDescriptorMock baseClassDescriptorMock;

    protected void setUp() throws Exception {
        super.setUp();

        baseClassDescriptorMock =
                new BaseClassDescriptorMock("baseClassDescriptorMock",
                        expectations);

        baseClassDescriptorMock.expects.getTypeClass().returns(Object.class)
                .any();
    }

    protected InternalProxy createProxy() {
        return new BaseProxyImpl(proxyModelDescriptorMock,
                baseClassDescriptorMock);
    }

    public void testSetEmbedded() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InteractionEventListenerMock listenerMock =
                new InteractionEventListenerMock("listenerMock", expectations);

        final InternalProxyMock childProxyMock =
                new InternalProxyMock("childProxyMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        proxyModelDescriptorMock.expects
                .createProxyForModelObject(NEW_MODEL_OBJECT)
                .returns(childProxyMock);

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        final BaseProxyImpl proxy = (BaseProxyImpl) createProxy(
                parentProxyMock);

        proxy.addListener(listenerMock, false);

        // =====================================================================
        //   Set Test Object Specific Expectations
        // =====================================================================

        final ProxyModelChangedEvent event = new ProxyModelChangedEvent(proxy,
                null, NEW_MODEL_OBJECT, true);
        listenerMock.expects.proxyModelChanged(event);
        parentProxyMock.expects.fireEvent(event);

        parentProxyMock.expects
                .setEmbeddedModelObject(proxy, NEW_MODEL_OBJECT)
                .returns(null);

        parentProxyMock.expects.isReadOnly().returns(false);
        childProxyMock.expects.attach(proxy);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        checkModificationCountIncremented(proxy, new Modifier() {
            public void modify() {
                Object oldModelObject = proxy.setModelObject(NEW_MODEL_OBJECT);
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

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 ===========================================================================
*/
