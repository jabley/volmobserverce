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

package com.volantis.mcs.interaction.impl;

import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InsertedIntoListEvent;
import com.volantis.mcs.interaction.event.RemovedFromListEvent;
import com.volantis.mcs.interaction.impl.operation.InsertProxyItemsOperation;
import com.volantis.mcs.interaction.impl.operation.RemoveProxyItemsOperation;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.ClassDescriptor;
import com.volantis.mcs.model.descriptor.ListAccessor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.ModifiableClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.path.IndexedStep;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.path.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * An interaction item that represents a field in a model object that references
 * a list.
 */
public class ListProxyImpl
        extends AbstractParentProxy
        implements ListProxy {

    private final ListClassDescriptor descriptor;
    private final ListAccessor accessor;
    private List proxyItems;
    private final ClassDescriptor itemClassDescriptor;

    public ListProxyImpl(
            InteractionModel interactionModel,
            ListClassDescriptor descriptor) {
        super(interactionModel);

        this.descriptor = descriptor;
        proxyItems = new ArrayList();
        accessor = descriptor.getListAccessor();
        itemClassDescriptor = descriptor.getItemClassDescriptor();
    }

    public int size() {
        return proxyItems.size();
    }

    public int getItemProxyIndex(Proxy proxy) {
        ProxyListItem item = findItem(proxy);
        return item.getProxyIndex();
    }

    public Proxy getItemProxy(int index) {

        ProxyListItem item = (ProxyListItem) proxyItems.get(index);
        Proxy proxy = item.getProxy();

        return proxy;
    }

    public Proxy addItemProxy() {

        InternalProxy itemProxy = createItemProxy();
        insertItems(proxyItems.size(), Collections.singletonList(itemProxy));

        return itemProxy;
    }

    public void removeItemProxy(Proxy itemProxy) {
        ProxyListItem item = findItem(itemProxy);

        int proxyIndex = item.getProxyIndex();
        removeItems(proxyIndex, proxyIndex);
    }

    public Operation prepareCreateAndAddProxyItemOperation() {

        Proxy itemProxy = createItemProxy();

        return prepareAddProxyItemOperation(itemProxy);
    }

    public Operation prepareAddProxyItemOperation(Proxy itemProxy) {

        List proxyItems = Collections.singletonList(itemProxy);
        int index = this.proxyItems.size();

        Operation operation = new InsertProxyItemsOperation(this, index,
                proxyItems);

        return operation;
    }

    public Operation prepareAddModelItemOperation(Object modelItem) {
        Proxy itemProxy = createItemProxy();
        itemProxy.setModelObject(modelItem);

        return prepareAddProxyItemOperation(itemProxy);
    }

    public Operation prepareRemoveProxyItemOperation(Proxy itemProxy) {

        int firstItemIndex = getItemProxyIndex(itemProxy);
        int lastItemIndex = firstItemIndex;

        return new RemoveProxyItemsOperation(this, firstItemIndex,
                lastItemIndex);
    }

    private InternalProxy createItemProxy() {
        return (InternalProxy) interactionModel
                .createProxyForType(itemClassDescriptor);
    }

    public ListClassDescriptor getListClassDescriptor() {
        return descriptor;
    }

    public Object getEmbeddedModelObject(Proxy proxy, boolean required) {
        ProxyListItem item = findItem(proxy);

        Object modelObject = parent.getEmbeddedModelObject(this, required);

        int modelIndex = item.getModelIndex();
        if (required && modelIndex == -1 &&
                itemClassDescriptor instanceof ModifiableClassDescriptor) {
            Object itemModelObject = ((ModifiableClassDescriptor) itemClassDescriptor).createModelObject();
            modelIndex = insertModelObject(item, modelObject, itemModelObject);
        }

        if (modelIndex == -1) {
            return null;
        } else {
            return accessor.get(modelObject, modelIndex);
        }
    }

    public Object setEmbeddedModelObject(
            Proxy proxy, Object newEmbeddedModelObject) {
        assertWritable();

        ProxyListItem item = findItem(proxy);
//        int index = findIndex(proxy);

        // Get the model object.
        Object modelObject = getModelObject(true);

        Object oldEmbeddedModelObject;
        int modelIndex = item.getModelIndex();
        if (modelIndex == -1) {
            insertModelObject(item, modelObject, newEmbeddedModelObject);

            oldEmbeddedModelObject = null;
        } else {

            oldEmbeddedModelObject = accessor.get(modelObject, modelIndex);
            accessor.set(modelObject, modelIndex, newEmbeddedModelObject);
        }

        return oldEmbeddedModelObject;
    }

    private int insertModelObject(
            ProxyListItem item, Object modelObject,
            Object newEmbeddedModelObject) {

        int modelIndex;
        // Count the number of proxy items that have real model objects
        // and come before the current proxy item.
        modelIndex = 0;
        int proxyIndex = item.getProxyIndex();
        for (int i = 0; i < proxyItems.size(); i++) {
            ProxyListItem listItem = (ProxyListItem) proxyItems.get(i);

            // If the item has a model object associated with it then update
            // the index, otherwise leave them alone.
            if (listItem.getModelIndex() != -1) {
                if (i < proxyIndex) {
                    modelIndex += 1;
                } else {
                    listItem.setModelIndex(listItem.getModelIndex() + 1);
                }
            }
        }
        item.setModelIndex(modelIndex);

        accessor.insert(modelObject, modelIndex, newEmbeddedModelObject);

        return modelIndex;
    }

    private ProxyListItem findItem(Proxy proxy) {
        for (int i = 0; i < proxyItems.size(); i++) {
            ProxyListItem item = (ProxyListItem) proxyItems.get(i);
            if (item.getProxy() == proxy) {
//                // Make sure that the item index is up to date.
//                item.setProxyIndex(i);
                return item;
            }
        }

        throw new IllegalArgumentException("Unknown proxy " + proxy);
    }

    protected Object createModelObject() {
        return descriptor.createModelObject();
    }

    protected Object copyModelObjectImpl() {

        // Create a copy of the model list.
        Object copy = descriptor.createModelObject();

        // Iterate over the existing model list making a copy of each
        // model object in the underlying list and adding it to the new list.
        for (int i = 0; i < proxyItems.size(); i += 1) {
            ProxyListItem item = (ProxyListItem) proxyItems.get(i);
            int modelIndex = item.getModelIndex();
            if (modelIndex != -1) {
                Proxy proxy = item.getProxy();
                Object itemModelObject = proxy.copyModelObject();
                accessor.insert(copy, modelIndex, itemModelObject);
            }
        }

        // Return the new list.
        return copy;
    }

    // Javadoc inherited
    protected void updateModelObject(Object modelObject,
                                     boolean force,
                                     boolean originator) {
        assertWritable();

        int size = accessor.size(modelObject);
        proxyItems = new ArrayList();
        for (int i = 0; i < size; i += 1) {
            InternalProxy proxy = createItemProxy();
            ProxyListItem item = new ProxyListItem(proxy);
            item.setModelIndex(i);
            item.setProxyIndex(i);
            proxyItems.add(item);
            proxy.attach(this);

            Object itemModelObject = accessor.get(modelObject, i);
            proxy.setModelObject(itemModelObject, force, false);
        }
    }

    public Proxy traverse(Step step, boolean enclosing, List proxies) {
        if (step instanceof IndexedStep) {
            IndexedStep indexedStep = (IndexedStep) step;
            int index = indexedStep.getIndex();
            if (index >= 0 && index < proxyItems.size()) {
                return getItemProxy(index);
            }
        }

        return null;
    }

    public TypeDescriptor getTypeDescriptor() {
        return descriptor;
    }

    public void insertItems(int index, List proxies) {
        assertWritable();

        // Add all the proxies from the supplied list into the list of
        // items, making sure to wrap them in a ProxyListItem wrapper.
        for (int i = 0; i < proxies.size(); i++) {
            InternalProxy proxy = (InternalProxy) proxies.get(i);
            ProxyListItem item = new ProxyListItem(proxy);
            this.proxyItems.add(index + i, item);
            proxy.attach(this);
        }

        // Now update the list of items to make sure that the indeces for the
        // item within the proxy and the model match up. Also, if any of the
        // proxies have model objects that are not in the underlying list then
        // add them into it.
        final Object modelList = getModelObject(true);
        int modelIndex = 0;
        for (int proxyIndex = 0;
             proxyIndex < this.proxyItems.size(); proxyIndex++) {
            ProxyListItem item = (ProxyListItem) this.proxyItems.get(
                    proxyIndex);
            item.setProxyIndex(proxyIndex);
            final InternalProxy proxy = item.getProxy();
            Object modelObject = proxy.getModelObject();
            if (item.getModelIndex() == -1) {
                // There is no entry in the model list for the current item.
                if (modelObject != null) {
                    // The item proxy has a model object so insert it into the
                    // correct place and update the item.
                    accessor.insert(modelList, modelIndex, modelObject);
                    item.setModelIndex(modelIndex);
                    modelIndex += 1;
                }
            } else {
                // The item has an entry in the model list so make sure that it
                // is up to date.
                item.setModelIndex(modelIndex);
                modelIndex += 1;
            }
        }

        // Remember that the proxy has been modified.
        modifiedProxy();

        int firstItemIndex = index;
        int lastItemIndex = index + proxies.size() - 1;
        InsertedIntoListEvent event = new InsertedIntoListEvent(this,
                firstItemIndex, lastItemIndex, true);
        fireEvent(event);
    }

    public List removeItems(int firstItemIndex, int lastItemIndex) {
        assertWritable();

        Object modelList = getModelObject();

        // Iterate over the items updating the indeces but not actually
        // removing anything.
        int modelIndex = 0;
        int oldProxyIndex = 0;
        int newProxyIndex = 0;
        List removedProxies = new ArrayList();
        for (Iterator i = proxyItems.iterator(); i.hasNext(); oldProxyIndex++) {
            ProxyListItem item = (ProxyListItem) i.next();
            if (oldProxyIndex < firstItemIndex ||
                    oldProxyIndex > lastItemIndex) {
                // Outside the range to be removed.

                // If the item has a model object then update its index as it
                // might have changed.
                if (item.getModelIndex() != -1) {
                    item.setModelIndex(modelIndex);
                    modelIndex += 1;
                }

                // Update the proxy index.
                item.setProxyIndex(newProxyIndex);
                newProxyIndex += 1;

            } else {
                // Inside the range to be removed.

                // Remove the proxy item from the list, detach it from its
                // parent and add the proxy to the list of removed proxies.
                i.remove();
                final InternalProxy proxy = item.getProxy();
                removedProxies.add(proxy);
                proxy.detach();

                // If the item has a model object then remove it from the
                // underlying model.
                if (item.getModelIndex() != -1) {
                    accessor.remove(modelList, modelIndex);
                }
            }
        }

        // Remember that the proxy has been modified.
        modifiedProxy();

        RemovedFromListEvent event = new RemovedFromListEvent(this,
                firstItemIndex, lastItemIndex, true);
        fireEvent(event);

        return removedProxies;
    }

    public void accept(ProxyVisitor visitor) {
        visitor.visit(this);
    }

    protected void addStepToPath(PathBuilder builder, Proxy childProxy) {
        int index = getItemProxyIndex(childProxy);
        builder.addIndexedStep(index);
    }

    private static class ProxyListItem {

        private final InternalProxy proxy;

        private int proxyIndex;

        private int modelIndex;

        public ProxyListItem(InternalProxy proxy) {
            this.proxy = proxy;
            this.proxyIndex = -1;
            this.modelIndex = -1;
        }

        public InternalProxy getProxy() {
            return proxy;
        }

        public void setProxyIndex(int proxyIndex) {
            this.proxyIndex = proxyIndex;
        }

        public int getModelIndex() {
            return modelIndex;
        }

        public int getProxyIndex() {
            return proxyIndex;
        }

        public void setModelIndex(int modelIndex) {
            this.modelIndex = modelIndex;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/11	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/5	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 26-Oct-05	9961/8	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/6	pduffin	VBM:2005101811 Added basic list operations

 25-Oct-05	9961/4	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
