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

package com.volantis.mcs.interaction.sample.descriptors;

import com.volantis.mcs.model.descriptor.DescriptorFactory;
import com.volantis.mcs.model.descriptor.BeanDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelObjectFactory;
import com.volantis.mcs.model.descriptor.AbstractPropertyAccessor;
import com.volantis.mcs.interaction.sample.model.Address;
import com.volantis.mcs.interaction.sample.model.AddressImpl;
import com.volantis.mcs.interaction.sample.model.Contacts;
import com.volantis.mcs.interaction.sample.model.Person;
import com.volantis.mcs.model.descriptor.BeanDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelObjectFactory;
import com.volantis.mcs.model.descriptor.AbstractPropertyAccessor;
import com.volantis.mcs.model.descriptor.DescriptorFactory;

import java.util.ArrayList;
import java.util.List;

public class Descriptors {

    static {
        DescriptorFactory factory =
                DescriptorFactory.getDefaultInstance();

        ModelDescriptorBuilder builder = factory.createModelDescriptorBuilder();

        // Create the bean descriptors for Contacts.
        BeanDescriptorBuilder contactsBuilder = builder.getBeanBuilder(
                Contacts.class, null);

        // Create the bean descriptors for Person.
        BeanDescriptorBuilder personBuilder = builder.getBeanBuilder(
                Person.class, null);

        // Create the class descriptor for Address.
        BeanDescriptorBuilder addressBuilder = builder.getBeanBuilder(
                Address.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return new AddressImpl();
                    }
                });

        contactsBuilder.addPropertyDescriptor(
                Contacts.CONTACTS, builder.getStandardListDescriptor(
                        ArrayList.class,
                        builder.getClassDescriptor(Person.class)),
                new AbstractPropertyAccessor() {
                    public void set(Object object, Object value) {
                        ((Contacts) object).setContacts((List) value);
                    }

                    public Object get(Object object) {
                        return ((Contacts) object).getContacts();
                    }
                }, true);

        personBuilder.addPropertyDescriptor(
                Person.FIRST_NAME,
                builder.getTypeDescriptor(String.class),
                new AbstractPropertyAccessor() {
                    public void set(Object object, Object value) {
                        ((Person) object).setFirstName((String) value);
                    }

                    public Object get(Object object) {
                        return ((Person) object).getFirstName();
                    }
                }, false);
        personBuilder.addPropertyDescriptor(
                Person.LAST_NAME,
                builder.getTypeDescriptor(String.class),
                new AbstractPropertyAccessor() {
                    public void set(Object object, Object value) {
                        ((Person) object).setLastName((String) value);
                    }

                    public Object get(Object object) {
                        return ((Person) object).getLastName();
                    }
                }, false);
        personBuilder.addPropertyDescriptor(
                Person.AGE,
                builder.getTypeDescriptor(Integer.TYPE),
                new AbstractPropertyAccessor() {
                    public void set(Object object, Object value) {
                        ((Person) object).setAge(((Integer) value).intValue());
                    }

                    public Object get(Object object) {
                        return new Integer(((Person) object).getAge());
                    }
                }, false);
        personBuilder.addPropertyDescriptor(
                Person.ADDRESS,
                builder.getTypeDescriptor(Address.class),
                new AbstractPropertyAccessor() {
                    public void set(Object object, Object value) {
                        ((Person) object).setAddress((Address) value);
                    }

                    public Object get(Object object) {
                        return ((Person) object).getAddress();
                    }
                }, false);

        addressBuilder.addPropertyDescriptor(
                Address.LINES,
                builder.getStandardListDescriptor(
                        ArrayList.class,
                        builder.getClassDescriptor(String.class)),
                new AbstractPropertyAccessor() {
                    public Object get(Object object) {
                        return ((Address) object).getLines();
                    }

                    public void set(Object object, Object value) {
                        ((Address) object).setLines((List) value);
                    }
                }, false);

        MODEL_DESCRIPTOR = builder.getModelDescriptor();
    }

    public static final ModelDescriptor MODEL_DESCRIPTOR;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
