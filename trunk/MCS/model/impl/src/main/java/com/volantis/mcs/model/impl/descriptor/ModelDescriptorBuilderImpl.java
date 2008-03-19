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

import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.BeanDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ClassDescriptor;
import com.volantis.mcs.model.descriptor.ListAccessor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelObjectFactory;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ModelDescriptorBuilderImpl
        implements ModelDescriptorBuilder {

    private static final Map BUILT_IN_TYPE_DESCRIPTORS;

    static {
        Map map = new HashMap();

        TypeDescriptor descriptor;
        Class[] primitive = new Class[]{
            Boolean.TYPE,
            Character.TYPE,
            Double.TYPE,
            Float.TYPE,
            Integer.TYPE,
            Long.TYPE,
            Short.TYPE,
        };

        for (int i = 0; i < primitive.length; i++) {
            Class primitiveClass = primitive[i];
            descriptor = new OpaqueClassDescriptorImpl(primitiveClass);
            map.put(primitiveClass, descriptor);
        }

        Class[] opaque = new Class[]{
            Boolean.class,
            Character.class,
            Double.class,
            Float.class,
            Integer.class,
            Long.class,
            Short.class,
            String.class
        };

        for (int i = 0; i < opaque.length; i++) {
            Class opaqueClass = opaque[i];
            descriptor = new OpaqueClassDescriptorImpl(opaqueClass);
            map.put(opaqueClass, descriptor);
        }

        // Commented out as the resulting class does not work.
        // map.put(Object.class, new BaseClassDescriptorImpl(Object.class));

        BUILT_IN_TYPE_DESCRIPTORS = map;
    }

    private static final ListAccessor STANDARD_LIST_ACCESSOR = new ListAccessor() {
        public int size(Object modelList) {
            List list = (List) modelList;
            return list.size();
        }

        public void insert(Object modelList, int position, Object value) {
            List list = (List) modelList;
            list.add(position, value);
        }

        public Object remove(Object modelList, int position) {
            List list = (List) modelList;
            return list.remove(position);
        }

        public Object get(Object modelList, int index) {
            List list = (List) modelList;
            return list.get(index);
        }

        public void set(Object modelList, int index, Object value) {
            List list = (List) modelList;
            list.set(index, value);
        }
    };

    private final Map class2Descriptor;
    private final List beanBuilders;

    private static final Comparator COMPARATOR = new Comparator() {
        public int compare(Object o1, Object o2) {

            TypeDescriptor t1 = (TypeDescriptor) o1;
            TypeDescriptor t2 = (TypeDescriptor) o2;
            Class c1 = t1.getTypeClass();
            Class c2 = t2.getTypeClass();

            // The most specific should come first and the least specific
            // last, i.e. Object.class should be the last one.
            if (c1.isAssignableFrom(c2)) {
                return +1;
            } else if (c2.isAssignableFrom(c1)) {
                return -1;
            } else {
                return -1;
            }
        }
    };

    public ModelDescriptorBuilderImpl() {
        class2Descriptor = new HashMap();
        class2Descriptor.putAll(BUILT_IN_TYPE_DESCRIPTORS);

        beanBuilders = new ArrayList();
    }

    public ModelDescriptor getModelDescriptor() {

        // Iterate over all the builders making sure that they have built the
        // class descriptors.
        for (Iterator i = beanBuilders.iterator(); i.hasNext();) {
            BeanDescriptorBuilderImpl builder = (BeanDescriptorBuilderImpl) i.next();
            builder.complete();
        }

        // Make sure that every class descriptor was created correctly.
//        Set orderedClassDescriptors = new TreeSet(COMPARATOR);
        MostSpecificClassMap map = new MostSpecificClassMap();
        for (Iterator i = class2Descriptor.values().iterator(); i.hasNext();) {
            TypeDescriptor descriptor = (TypeDescriptor) i.next();
            if (descriptor instanceof BeanClassDescriptorImpl) {
                BeanClassDescriptorImpl bean = (BeanClassDescriptorImpl) descriptor;
                if (!bean.isComplete()) {
                    throw new IllegalStateException(
                            "Descriptor for class " + descriptor.getTypeClass()
                                                      .getName() + " has not been built");
                }
            }

            map.put(descriptor.getTypeClass(), descriptor);
//            // Add them to the list in order so that each class comes after
//            // all classes that are derived from it. e.g. Object will come
//            // last.
//            orderedClassDescriptors.add(descriptor);
        }

        return new ModelDescriptorImpl(map);
    }

    public TypeDescriptor getTypeDescriptor(Class type) {
        TypeDescriptor descriptor = (TypeDescriptor)
                class2Descriptor.get(type);
        if (descriptor == null) {
            throw new IllegalStateException("No type defined for " + type);
        }

        return descriptor;
    }

    public ClassDescriptor getClassDescriptor(Class type) {
        return (ClassDescriptor) getTypeDescriptor(type);
    }

    public ListClassDescriptor getStandardListDescriptor(
            Class listClass, ClassDescriptor itemClassDescriptor) {

        if (!List.class.isAssignableFrom(listClass)) {
            throw new IllegalArgumentException(listClass + " is not a list");
        }

        return new ListClassDescriptorImpl(
                List.class, itemClassDescriptor, STANDARD_LIST_ACCESSOR);
    }

    public BaseClassDescriptor addBaseClassDescriptor(Class baseClass) {
        BaseClassDescriptorImpl descriptor = new BaseClassDescriptorImpl(
                baseClass);
        addDescriptor(descriptor);
        return descriptor;
    }

    public OpaqueClassDescriptor addOpaqueClassDescriptor(Class opaqueClass) {
        OpaqueClassDescriptorImpl descriptor = new OpaqueClassDescriptorImpl(
                opaqueClass);
        addDescriptor(descriptor);
        return descriptor;
    }

    public BeanDescriptorBuilder getBeanBuilder(
            Class beanClass, ModelObjectFactory modelObjectFactory) {

        if (modelObjectFactory == null) {
            modelObjectFactory = new ReflectiveModelObjectFactory(beanClass);
        }

        BeanClassDescriptorImpl descriptor = new BeanClassDescriptorImpl(
                beanClass, modelObjectFactory);
        addDescriptor(descriptor);

        BeanDescriptorBuilder builder = new BeanDescriptorBuilderImpl(
                descriptor);
        beanBuilders.add(builder);

        return builder;
    }

    private void addDescriptor(TypeDescriptor descriptor) {
        Class typeClass = descriptor.getTypeClass();
        TypeDescriptor existing = (TypeDescriptor)
                class2Descriptor.get(typeClass);
        if (existing != null) {
            throw new IllegalStateException(
                    "Type already defined for " + existing);
        }
        class2Descriptor.put(typeClass, descriptor);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Fixed issue with mapping classes to type descriptors

 31-Oct-05	9961/10	pduffin	VBM:2005101811 Committing restructuring

 28-Oct-05	9886/3	adrianj	VBM:2005101811 New theme GUI

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
