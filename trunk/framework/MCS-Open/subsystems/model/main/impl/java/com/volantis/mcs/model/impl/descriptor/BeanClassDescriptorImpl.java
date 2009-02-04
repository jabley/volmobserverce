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

package com.volantis.mcs.model.impl.descriptor;

import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.ModelObjectFactory;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;
import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link com.volantis.mcs.model.descriptor.BeanClassDescriptor}.
 */
public class BeanClassDescriptorImpl
        extends AbstractClassDescriptor
        implements BeanClassDescriptor {

    private final ModelObjectFactory modelObjectFactory;

    private List propertyList;

    private Map propertyId2Descriptor;

    private Map propertyName2Id;

    private boolean complete;

    public BeanClassDescriptorImpl(
            Class beanClass, ModelObjectFactory modelObjectFactory) {
        super(beanClass);
        this.modelObjectFactory = modelObjectFactory;
    }

    // Javadoc inherited.
    public List getPropertyDescriptors() {
        return propertyList;
    }

    // Javadoc inherited.
    public PropertyDescriptor getPropertyDescriptor(
            PropertyIdentifier property) {
        return (PropertyDescriptor) propertyId2Descriptor.get(property);
    }

    public Object createModelObject() {
        return modelObjectFactory.createObject();
    }

    public boolean isComplete() {
        return complete;
    }

    public void accept(TypeDescriptorVisitor visitor) {
        visitor.visit(this);
    }

    public void setPropertyDescriptors(List propertyDescriptors) {
        this.propertyList = propertyDescriptors;
    }

    public void complete(List propertyDescriptors) {
        this.propertyList = propertyDescriptors;
        this.propertyId2Descriptor = new HashMap();
        this.propertyName2Id = new HashMap();
        for (int i = 0; i < propertyList.size(); i++) {
            PropertyDescriptor descriptor = (PropertyDescriptor)
                    propertyList.get(i);
            PropertyIdentifier identifier = descriptor.getIdentifier();
            propertyId2Descriptor.put(identifier, descriptor);
            propertyName2Id.put(identifier.getName(), identifier);

        }
        this.complete = true;
    }

    public PropertyIdentifier getPropertyIdentifier(String name) {
        return (PropertyIdentifier) propertyName2Id.get(name);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/9	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/5	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
