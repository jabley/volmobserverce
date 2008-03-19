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

package com.volantis.mcs.interaction.sample;

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListenerMock;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.interaction.sample.descriptors.Descriptors;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.Person;

import java.util.Arrays;
import java.util.List;

public class InteractionLayerTestCase
        extends FlintstoneTestAbstract {

    public void testCreateLayerFromModel() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InteractionEventListenerMock deepListenerMock =
                new InteractionEventListenerMock(
                        "deepListenerMock", expectations);

        final InteractionEventListenerMock shallowListenerMock =
                new InteractionEventListenerMock(
                        "shallowListenerMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        Address address = createFlintStoneAddress();
        List lines = address.getLines();

        Person person = createFredFlintstone(address);

        BeanProxy rootProxy = (BeanProxy) createProxy(person);
        rootProxy.addListener(shallowListenerMock, false);
        rootProxy.addListener(deepListenerMock, true);
        Object value;

        // Check that the first name proxy works.
        OpaqueProxy firstNameProxy = (OpaqueProxy) rootProxy.getPropertyProxy(
                Person.FIRST_NAME);
        value = firstNameProxy.getModelObject();
        assertEquals("first name", "Fred", value);

        // Check that the last name proxy works.
        OpaqueProxy lastNameProxy = (OpaqueProxy) rootProxy.getPropertyProxy(
                Person.LAST_NAME);
        value = lastNameProxy.getModelObject();
        assertEquals("last name", "Flintstone", value);

        // Check that the age proxy works.
        OpaqueProxy ageProxy = (OpaqueProxy) rootProxy.getPropertyProxy(
                Person.AGE);
        value = ageProxy.getModelObject();
        assertEquals("age", new Integer(10040), value);

        // Check that the address proxy works.
        BeanProxy addressProxy = (BeanProxy) rootProxy.getPropertyProxy(
                Person.ADDRESS);
        ListProxy linesProxy = (ListProxy) addressProxy.getPropertyProxy(
                Address.LINES);
        int size = linesProxy.size();
        OpaqueProxy lineProxy;
        for (int i = 0; i < size; i += 1) {
            lineProxy = (OpaqueProxy) linesProxy.getItemProxy(i);
            String expectedLine = (String) lines.get(i);
            String actualLine = (String) lineProxy.getModelObject();
            assertEquals("Line " + i, expectedLine, actualLine);
        }

        ProxyModelChangedEvent event = new ProxyModelChangedEvent(
                firstNameProxy, "Fred", "Wilma", true);
        deepListenerMock.expects.proxyModelChanged(event);

        value = firstNameProxy.setModelObject("Wilma");
        assertEquals("old value", "Fred", value);

        // Test that paths work properly.
        checkPath(
                rootProxy, rootProxy, "", Arrays.asList(
                        new Object[]{rootProxy}));
        checkPath(
                rootProxy, linesProxy, "/address/lines",
                Arrays.asList(
                        new Object[]{
                            rootProxy,
                            addressProxy,
                            linesProxy
                        }));

        lineProxy = (OpaqueProxy) linesProxy.getItemProxy(2);
        checkPath(
                rootProxy, lineProxy, "/address/lines/2",
                Arrays.asList(
                        new Object[]{
                            rootProxy,
                            addressProxy,
                            linesProxy,
                            lineProxy
                        }));
    }

    private void checkPath(
            Proxy root, Proxy proxy,
            final String expectedPathAsString,
            List expectedProxies) {

        Path path;
        String pathAsString;
        path = proxy.getPathFromRoot();
        pathAsString = path.getAsString();
        assertEquals(expectedPathAsString, pathAsString);
        assertSame("Proxies match", proxy, root.getProxy(path));

        List actualProxies = root.getProxies(path);
        assertEquals("Proxy list", expectedProxies, actualProxies);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/2	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/8	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/6	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/4	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
