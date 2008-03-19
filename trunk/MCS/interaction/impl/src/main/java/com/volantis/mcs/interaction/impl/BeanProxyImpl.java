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

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.BeanPropertyChangedEvent;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.ModifiableClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyAccessor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An interaction item that represents a model object as a set of fields.
 */
public class BeanProxyImpl
        extends AbstractParentProxy
        implements BeanProxy {

    private final BeanClassDescriptor beanClassDescriptor;

    private Map property2Proxies;

    public BeanProxyImpl(
            InteractionModel interactionModel,
            BeanClassDescriptor descriptor) {
        super(interactionModel);
        this.beanClassDescriptor = descriptor;
        property2Proxies = new HashMap();
    }

    public Proxy getPropertyProxy(PropertyIdentifier property) {
        PropertyDescriptor descriptor = getRequiredDescriptor(property);
        Proxy proxy = (Proxy) property2Proxies.get(descriptor);
        if (proxy == null) {
            proxy = createPropertyProxy(descriptor);
            property2Proxies.put(descriptor, proxy);
            ((InternalProxy) proxy).attach(this);
        }

        return proxy;
    }

    private Proxy createPropertyProxy(PropertyDescriptor descriptor) {
        TypeDescriptor propertyType = descriptor.getPropertyType();
        Proxy proxy = interactionModel.createProxyForType(propertyType);

        return proxy;
    }

    public Object setPropertyProxy(
            PropertyIdentifier property, Proxy newProxy) {
        PropertyDescriptor descriptor = getRequiredDescriptor(property);
        InternalProxy oldProxy = (InternalProxy) property2Proxies.get(
                descriptor);

        // Do nothing if the proxies are the same.
        if (oldProxy != newProxy) {

            // Detach the old proxy.
            if (oldProxy != null) {
                oldProxy.detach();
            }

            // Attach the new proxy.
            ((InternalProxy) newProxy).attach(this);

            property2Proxies.put(descriptor, newProxy);

            fireEvent(new BeanPropertyChangedEvent(this, property, oldProxy,
                    newProxy, true));
        }

        return oldProxy;
    }

    public PropertyIdentifier getPropertyForProxy(Proxy proxy) {
        return findPropertyDescriptor(proxy).getIdentifier();
    }

    public BeanClassDescriptor getBeanClassDescriptor() {
        return beanClassDescriptor;
    }

    private PropertyDescriptor getRequiredDescriptor(
            PropertyIdentifier property) {
        PropertyDescriptor descriptor =
                beanClassDescriptor.getPropertyDescriptor(property);
        if (descriptor == null) {
            throw new IllegalArgumentException("Unknown property " + property);
        }
        return descriptor;
    }

    protected Object createModelObject() {
        return beanClassDescriptor.createModelObject();
    }

    public Object getEmbeddedModelObject(Proxy proxy, boolean required) {

        // Get the property's model object.
        PropertyDescriptor descriptor = findPropertyDescriptor(proxy);
        Object propertyModelObject = getEmbeddedModelObject(descriptor,
                required);

        if (required && propertyModelObject == null) {
            // The model object for the property does not exist and the caller
            // requires it because it needs to modify it. Therefore, a new
            // model object needs to be created and associated with this
            // property.
            //
            // As the caller needs to modify the object it cannot be either
            // a primitive, unchangeable or abstract type.
            ModifiableClassDescriptor modifiable = (ModifiableClassDescriptor)
                    descriptor.getPropertyType();
            propertyModelObject = modifiable.createModelObject();
            setEmbeddedModelObject(proxy, propertyModelObject);
        }

        return propertyModelObject;
    }

    private Object getEmbeddedModelObject(
            PropertyDescriptor descriptor, boolean create) {
        PropertyAccessor accessor = descriptor.getPropertyAccessor();
        if (accessor == null) {
            throw new IllegalStateException("Cannot access property directly");
        }

        Object modelObject = getModelObject(create);
        Object propertyModelObject;
        if (modelObject != null) {
            propertyModelObject = accessor.get(modelObject);
        } else {
            propertyModelObject = null;
        }

        return propertyModelObject;
    }

    public Object setEmbeddedModelObject(
            Proxy proxy, Object newEmbeddedModelObject) {

        PropertyDescriptor descriptor = findPropertyDescriptor(proxy);
        PropertyAccessor accessor = descriptor.getPropertyAccessor();
        if (accessor == null) {
            throw new IllegalStateException("Cannot access property directly");
        }

        Object modelObject = getModelObject(true);
        Object oldEmbeddedObject = accessor.get(modelObject);

        // Only do something if the old value and the new value are different.
        if (!equals(oldEmbeddedObject, newEmbeddedModelObject)) {
            accessor.set(modelObject, newEmbeddedModelObject);
        }

        return oldEmbeddedObject;
    }

    private PropertyDescriptor findPropertyDescriptor(Proxy proxy) {
        for (Iterator i = property2Proxies.entrySet().iterator();
             i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            if (entry.getValue() == proxy) {
                return (PropertyDescriptor) entry.getKey();
            }
        }

        throw new IllegalArgumentException("Unknown proxy " + proxy);
    }

    protected Object copyModelObjectImpl() {

        // Create a new model object.
        Object copy = createModelObject();

        // Populate the model object with copies of the model objects held
        // by each of the proxies.
        List list = beanClassDescriptor.getPropertyDescriptors();
        for (int i = 0; i < list.size(); i++) {
            PropertyDescriptor descriptor = (PropertyDescriptor) list.get(i);
            PropertyIdentifier identifier = descriptor.getIdentifier();
            PropertyAccessor accessor = descriptor.getPropertyAccessor();

            Proxy childProxy = getPropertyProxy(identifier);
            Object childObject = childProxy.copyModelObject();
            accessor.set(copy, childObject);
        }

        return copy;
    }

    // Javadoc inherited
    protected void updateModelObject(Object modelObject,
                                     boolean force,
                                     boolean originator) {
        List list = beanClassDescriptor.getPropertyDescriptors();
        for (int i = 0; i < list.size(); i++) {
            PropertyDescriptor descriptor = (PropertyDescriptor) list.get(i);
            PropertyIdentifier identifier = descriptor.getIdentifier();
            PropertyAccessor accessor = descriptor.getPropertyAccessor();

            Object propertyModelObject = accessor.get(modelObject);
            if (propertyModelObject != null) {
                InternalProxy propertyProxy =
                        (InternalProxy) getPropertyProxy(identifier);
                propertyProxy.setModelObject(propertyModelObject, force, false);
            }
        }
    }

    public Proxy traverse(Step step, boolean enclosing, List proxies) {
        if (step instanceof PropertyStep) {
            PropertyStep propertyStep = (PropertyStep) step;
            String property = propertyStep.getProperty();
            PropertyIdentifier identifier =
                    beanClassDescriptor.getPropertyIdentifier(property);
            if (identifier != null) {
                return getPropertyProxy(identifier);
            }
        }

        return null;
    }

    public TypeDescriptor getTypeDescriptor() {
        return beanClassDescriptor;
    }

    public void accept(ProxyVisitor visitor) {
        visitor.visit(this);
    }

    protected void addStepToPath(PathBuilder builder, Proxy childProxy) {
        PropertyIdentifier property = getPropertyForProxy(childProxy);
        builder.addPropertyStep(property);
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

 26-Oct-05	9961/8	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/4	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
