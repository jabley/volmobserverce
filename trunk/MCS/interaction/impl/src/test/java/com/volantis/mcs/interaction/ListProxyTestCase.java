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

import com.volantis.mcs.interaction.event.InsertedIntoListEvent;
import com.volantis.mcs.interaction.event.RemovedFromListEvent;
import com.volantis.mcs.interaction.impl.InternalProxy;
import com.volantis.mcs.interaction.impl.InternalProxyMock;
import com.volantis.mcs.interaction.impl.ListProxyImpl;
import com.volantis.mcs.interaction.impl.AbstractProxy;
import com.volantis.mcs.interaction.sample.descriptors.Descriptors;
import com.volantis.mcs.interaction.sample.model.Contacts;
import com.volantis.mcs.interaction.sample.model.Person;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.AddressImpl;
import com.volantis.mcs.model.descriptor.ClassDescriptorMock;
import com.volantis.mcs.model.descriptor.ListAccessorMock;
import com.volantis.mcs.model.descriptor.ListClassDescriptorMock;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;

import java.util.List;
import java.util.ArrayList;

public class ListProxyTestCase
        extends ProxyTestAbstract {

    private static final Object MODEL_LIST = new Object();

    private ListClassDescriptorMock listClassDescriptorMock;
    private ListAccessorMock listAccessorMock;
    private ClassDescriptorMock itemClassDescriptorMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        listClassDescriptorMock = new ListClassDescriptorMock(
                "listClassDescriptorMock", expectations);

        listAccessorMock = new ListAccessorMock(
                "listAccessorMock", expectations);

        itemClassDescriptorMock = new ClassDescriptorMock(
                "itemClassDescriptorMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        listClassDescriptorMock.expects.getListAccessor()
                .returns(listAccessorMock)
                .any();

        listClassDescriptorMock.expects.getItemClassDescriptor()
                .returns(itemClassDescriptorMock)
                .any();
    }

    protected InternalProxy createProxy() {
        return new ListProxyImpl(proxyModelDescriptorMock,
                listClassDescriptorMock);
    }

    /**
     * Test that adding a proxy item causes a new proxy item to be created and
     * events to be fired.
     */
    public void testAdd() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InternalProxyMock itemProxyMock =
                new InternalProxyMock("itemProxyMock", expectations);

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        final ListProxyImpl listProxy = (ListProxyImpl) createProxy(parentProxyMock);
        listProxy.addListener(listenerMock, false);

        // =====================================================================
        //   Set Test Object Specific Expectations
        // =====================================================================

        addExpectationsForAddItemProxy(listProxy, itemProxyMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        checkModificationCountIncremented(
                listProxy, new Modifier() {
                    public void modify() {
                        Proxy itemProxy = listProxy.addItemProxy();
                        assertEquals("Item Proxy", itemProxyMock, itemProxy);
                    }
                });
    }

    /**
     * Add expectations for when the addItemProxy method is called.
     *
     * @param listProxy     The list proxy.
     * @param itemProxyMock The item proxy mock.
     */
    private void addExpectationsForAddItemProxy(
            ListProxy listProxy, final InternalProxyMock itemProxyMock) {

        // Adding an item proxy should cause a new proxy appropriate to the
        // item class to be created.
        proxyModelDescriptorMock.expects
                .createProxyForType(itemClassDescriptorMock)
                .returns(itemProxyMock);

        // The item proxy may be asked if it has a model object associated
        // with it.
        itemProxyMock.expects.getModelObject().returns(null).any();

        // Should cause an event to be sent to listeners on the object.
        InsertedIntoListEvent event = new InsertedIntoListEvent(
                listProxy, 0, 0, true);
        listenerMock.expects.addedToList(event);

        // Should also cause an event to be sent to the parent proxy.
        parentProxyMock.expects.fireEvent(event);

        parentProxyMock.expects.isReadOnly().returns(false);

        // Parent should supply the underlying model object.
        parentProxyMock.expects.getEmbeddedModelObject(listProxy, true)
                .returns(MODEL_LIST).any();

        // The item proxy should be attached to the parent.
        itemProxyMock.expects.attach(listProxy);
    }

    /**
     * Test that adding and then removing a proxy item works correctly.
     */
    public void testAddThenRemove() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InternalProxyMock itemProxyMock =
                new InternalProxyMock("itemProxyMock", expectations);

        // =====================================================================
        //   Create Test Objects
        // =====================================================================

        final ListProxyImpl listProxy = (ListProxyImpl) createProxy(parentProxyMock);
        listProxy.addListener(listenerMock, false);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        addExpectationsForAddItemProxy(listProxy, itemProxyMock);

        parentProxyMock.expects.isReadOnly().returns(false);

        // The item should be detached from the list.
        itemProxyMock.expects.detach();

        // Should cause an event to be sent to listeners on the object.
        RemovedFromListEvent event =
                new RemovedFromListEvent(listProxy, 0, 0, true);
        listenerMock.expects.removedFromList(event);

        // Should also cause an event to be sent to the parent proxy.
        parentProxyMock.expects.fireEvent(event);

        // Parent should supply the underlying model object.
        parentProxyMock.expects.getEmbeddedModelObject(listProxy, false)
                .returns(MODEL_LIST).any();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        final Proxy itemProxy = listProxy.addItemProxy();
        assertEquals("Item Proxy", itemProxyMock, itemProxy);

        checkModificationCountIncremented(
                listProxy, new Modifier() {
                    public void modify() {
                        listProxy.removeItemProxy(itemProxy);
                    }
                });
    }

    /**
     * Test that adding a couple of proxies with no model objects and then
     * getting a model object for one does not update the other.
     */
    public void testGettingModelObjectForProxyDoesNotAffectOthers() {

        ModelDescriptor modelDescriptor = Descriptors.MODEL_DESCRIPTOR;
        BeanClassDescriptor contactsDescriptor = (BeanClassDescriptor)
                modelDescriptor.getTypeDescriptorStrict(Contacts.class);

        InteractionFactory factory = InteractionFactory.getDefaultInstance();
        InteractionModel model = factory.createInteractionModel(modelDescriptor);

        // Contacts
        //   CONTACTS - list of Person
        //       ADDRESS - Address
        //

        // The proxy for the Contacts class.
        BeanProxy contactsProxy = (BeanProxy)
                model.createProxyForType(contactsDescriptor);

        // The proxy for the list of Person objects in the Contacts class.
        ListProxyImpl listProxy = (ListProxyImpl)
                contactsProxy.getPropertyProxy(Contacts.CONTACTS);

        // The proxies for two instances of Person objects in the list.
        BeanProxy person1Proxy = (BeanProxy) listProxy.addItemProxy();
        BeanProxy person2Proxy = (BeanProxy) listProxy.addItemProxy();

        // The proxy for the address of the first Person object.
        BeanProxy address1Proxy = (BeanProxy) person1Proxy.getPropertyProxy(
                Person.ADDRESS);

        // Set the proxy, this should cause the model objects for all the
        // parent proxies to be created but it should not affect the model
        // object associated with the person2Proxy.
        Address address1 = new AddressImpl();
        address1Proxy.setModelObject(address1);

        // The proxy for the address of the second Person object.
        BeanProxy address2Proxy = (BeanProxy) person2Proxy.getPropertyProxy(
                Person.ADDRESS);

        Address address2 = (Address) address2Proxy.getModelObject();
        assertNull("Getting a required embedded object in list proxy has " +
                "cause other proxies to get the same object.", address2);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/8	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
