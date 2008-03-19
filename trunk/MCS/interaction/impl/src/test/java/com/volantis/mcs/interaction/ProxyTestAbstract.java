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
import com.volantis.mcs.interaction.impl.InternalParentProxyMock;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public abstract class ProxyTestAbstract
        extends TestCaseAbstract {

    protected InteractionModelMock proxyModelDescriptorMock;
    protected InternalParentProxyMock parentProxyMock;
    protected InteractionEventListenerMock listenerMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        proxyModelDescriptorMock = new InteractionModelMock(
                "proxyModelDescriptorMock", expectations);

        parentProxyMock = new InternalParentProxyMock(
                "parentProxyMock", expectations);

        listenerMock = new InteractionEventListenerMock(
                "listenerMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================
    }

    /**
     * Test that detaching a proxy from its parent preserves its model object.
     */
    public void testModelObjectPreservedAfterDetach() {


        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        InternalProxy proxy = createProxy(parentProxyMock);

        Object modelObject = new Object();

        // =====================================================================
        //   Set Test Object Specific Expectations
        // =====================================================================

        parentProxyMock.expects.getEmbeddedModelObject(proxy, false)
                .returns(modelObject);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Get the model object before removing.
        Object itemModelObjectBefore = proxy.getModelObject();

        proxy.detach();

        // Get the model object after removing.
        Object itemModelObjectAfter = proxy.getModelObject();

        assertEquals(
                "Model object changed by removal", itemModelObjectBefore,
                itemModelObjectAfter);
    }

    protected InternalProxy createProxy(
            InternalParentProxyMock parentProxyMock) {
        InternalProxy proxy = createProxy();
        proxy.attach(parentProxyMock);
        return proxy;
    }

    protected abstract InternalProxy createProxy();

    protected void checkModificationCountIncremented(
            InternalProxy proxy, Modifier modifier) {
        int modificationCountBefore = proxy.getModificationCount();

        modifier.modify();

        int modificationCountAfter = proxy.getModificationCount();

        assertTrue(
                "Modification count should be incremented, before " +
                modificationCountBefore + ", after " + modificationCountAfter,
                modificationCountAfter > modificationCountBefore);
    }

    protected static interface Modifier {
        public void modify();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
