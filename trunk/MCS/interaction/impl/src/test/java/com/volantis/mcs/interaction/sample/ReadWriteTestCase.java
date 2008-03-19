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
package com.volantis.mcs.interaction.sample;

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.ReadWriteState;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.interaction.sample.model.Contacts;
import com.volantis.mcs.interaction.sample.model.Person;
import com.volantis.mcs.interaction.sample.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the read-write state functionality.
 */
public class ReadWriteTestCase extends FlintstoneTestAbstract {
    private ListProxy contactsProxy;
    private BeanProxy wilmaProxy;
    private BeanProxy fredProxy;

    /**
     * Set up a simple Flintstones model using the interaction layer.
     *
     * @throws Exception if an error occurs
     */
    protected void setUp() throws Exception {
        super.setUp();
        Contacts contacts = new Contacts();
        BeanProxy contactsBeanProxy = (BeanProxy) createProxy(contacts);
        contactsProxy = (ListProxy) contactsBeanProxy.getPropertyProxy(Contacts.CONTACTS);

        Person wilma = createWilmaFlintstone(createFlintStoneAddress());
        wilmaProxy = (BeanProxy) createProxy(wilma);
        contactsProxy.prepareAddProxyItemOperation(wilmaProxy).execute();

        Person fred = createFredFlintstone(createFlintStoneAddress());
        fredProxy = (BeanProxy) createProxy(fred);
        contactsProxy.prepareAddProxyItemOperation(fredProxy).execute();
    }

    /**
     * Tests that the default states for proxies are set correctly.
     */
    public void testDefaultStates() {
        assertEquals("Default state should be inherit",
                ReadWriteState.INHERIT, contactsProxy.getReadWriteState());
        assertFalse("Default inherited value should be read/write",
                contactsProxy.isReadOnly());
    }

    /**
     * Tests that a variety of modification types fail when the proxy is
     * read-only.
     */
    public void testOperationsFailWhenReadOnly() {
        // List proxy operations
        contactsProxy.setReadWriteState(ReadWriteState.READ_ONLY);
        assertOperationFails("Add model item should fail when list is RO",
                contactsProxy.prepareAddModelItemOperation(new Person()));
        assertOperationFails("Add proxy item should fail when list is RO",
                contactsProxy.prepareAddProxyItemOperation(
                        createProxy(new Person())));
        assertOperationFails("Create/add proxy item should fail when list is RO",
                contactsProxy.prepareCreateAndAddProxyItemOperation());
        assertOperationFails("Remove proxy item should fail when list is RO",
                contactsProxy.prepareRemoveProxyItemOperation(wilmaProxy));
        assertOperationFails("Set model should fail when list is RO",
                contactsProxy.prepareSetModelObjectOperation(new ArrayList()));

        // Bean proxy operations
        wilmaProxy.setReadWriteState(ReadWriteState.READ_ONLY);
        assertOperationFails("Set model should fail when bean is RO",
                wilmaProxy.prepareSetModelObjectOperation(new Person()));

        // Opaque proxy operations
        Proxy opaqueProxy = wilmaProxy.getPropertyProxy(Person.FIRST_NAME);
        opaqueProxy.setReadWriteState(ReadWriteState.READ_ONLY);
        assertOperationFails("Set model should fail when property is RO",
                opaqueProxy.prepareSetModelObjectOperation("Pebbles"));
    }

    /**
     * Tests that read/write states are inherited correctly.
     */
    public void testReadWriteInheritance() {
        contactsProxy.setReadWriteState(ReadWriteState.READ_ONLY);
        assertTrue("Child should inherit read-only state from parent",
                wilmaProxy.isReadOnly());

        contactsProxy.setReadWriteState(ReadWriteState.READ_WRITE);
        assertFalse("Child should inherit read-write state from parent",
                wilmaProxy.isReadOnly());
    }

    /**
     * Tests that events are propagated when the state changes.
     */
    public void testReadOnlyEvents() {
        wilmaProxy.setReadWriteState(ReadWriteState.READ_ONLY);
        BeanProxy wilmaAddress =
                (BeanProxy) wilmaProxy.getPropertyProxy(Person.ADDRESS);
        // Explicitly set the value for first name, so that it should not
        // propagate the event.
        wilmaProxy.getPropertyProxy(Person.FIRST_NAME).
                setReadWriteState(ReadWriteState.READ_WRITE);

        // Build up a list of sources that we expect to change
        final List expectedSources = new ArrayList();
        expectedSources.add(wilmaProxy);
        expectedSources.add(wilmaProxy.getPropertyProxy(Person.AGE));
        expectedSources.add(wilmaProxy.getPropertyProxy(Person.LAST_NAME));
        expectedSources.add(wilmaAddress);
        ListProxy lines =
                (ListProxy) wilmaAddress.getPropertyProxy(Address.LINES);
        expectedSources.add(lines);
        for (int i = 0; i < lines.size(); i++) {
            expectedSources.add(lines.getItemProxy(i));
        }

        // Set up a listener that removes sources from the expected list as
        // events arrive.
        wilmaProxy.addListener(new InteractionEventListenerAdapter() {
            public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                assertFalse("New read-only value should be false",
                        event.isReadOnly());
                assertTrue("Source of state change should be in expected list",
                        expectedSources.contains(event.getProxy()));
                expectedSources.remove(event.getProxy());
            }
        }, true);

        // Change the read write state, triggering events
        wilmaProxy.setReadWriteState(ReadWriteState.READ_WRITE);

        // Check that all expected sources fired events
        assertTrue("All expected sources should have fired events",
                expectedSources.isEmpty());
    }

    /**
     * Assert that an operation will fail with an illegal state exception.
     *
     * @param message The message to display if the assertion is incorrect
     * @param operation The operation to execute
     */
    private void assertOperationFails(String message, Operation operation) {
        try {
            operation.execute();
            fail(message);
        } catch (IllegalStateException ise) {
            // This is the expected exception - ignore it silently
        }
    }
}
