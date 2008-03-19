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

import com.volantis.mcs.model.descriptor.BeanDescriptorBuilder;
import com.volantis.mcs.model.descriptor.PropertyAccessor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link com.volantis.mcs.model.descriptor.BeanDescriptorBuilder}.
 */
public class BeanDescriptorBuilderImpl
        implements BeanDescriptorBuilder {

    private final BeanClassDescriptorImpl descriptor;

    private final List propertyDescriptors;

    private final Map propertyId2Descriptor;

    private final Set propertyNames;

    public BeanDescriptorBuilderImpl(BeanClassDescriptorImpl descriptor) {
        this.descriptor = descriptor;
        this.propertyDescriptors = new ArrayList();
        this.propertyId2Descriptor = new HashMap();
        propertyNames = new HashSet();
    }

    public void addPropertyDescriptor(
            PropertyIdentifier identifier, TypeDescriptor descriptor,
            PropertyAccessor accessor,
            boolean required) {

        if (identifier == null) {
            throw new IllegalArgumentException("identifier cannot be null");
        }
        if (descriptor == null) {
            throw new IllegalArgumentException("descriptor cannot be null");
        }
        if (accessor == null) {
            throw new IllegalArgumentException("accessor cannot be null");
        }

        PropertyDescriptorImpl propertyDescriptor = new PropertyDescriptorImpl();
        propertyDescriptor.setPropertyType(descriptor);
        propertyDescriptor.setPropertyAccessor(accessor);
        propertyDescriptor.setRequired(required);
        propertyDescriptor.setIdentifier(identifier);

        addDescriptor(propertyDescriptor);
    }

    private PropertyIdentifier addDescriptor(PropertyDescriptor descriptor) {
        PropertyIdentifier identifier = descriptor.getIdentifier();
        PropertyDescriptor old = (PropertyDescriptor)
                propertyId2Descriptor.get(identifier);
        if (old != null) {
            throw new IllegalStateException(
                    "Duplicate property " + identifier);
        }

        // Make sure that the names do not clash either as the names will be
        // used within a path.
        String name = identifier.getName();
        if (propertyNames.contains(name)) {
            throw new IllegalStateException("Duplicate property name " + name);
        }
        propertyNames.add(name);

        propertyDescriptors.add(descriptor);

        return identifier;
    }

    public void complete() {
        descriptor.complete(propertyDescriptors);
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
