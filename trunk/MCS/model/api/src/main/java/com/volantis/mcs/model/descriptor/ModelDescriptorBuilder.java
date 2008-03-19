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

package com.volantis.mcs.model.descriptor;



/**
 */
public interface ModelDescriptorBuilder {

    ModelDescriptor getModelDescriptor();

    BeanDescriptorBuilder getBeanBuilder(
            Class clazz, ModelObjectFactory modelObjectFactory);

    TypeDescriptor getTypeDescriptor(Class type);

    ClassDescriptor getClassDescriptor(Class type);

    ListClassDescriptor getStandardListDescriptor(
            Class listClass, ClassDescriptor itemClass);

    /**
     * Add a base class descriptor for the specified class.
     *
     * <p>A base class is essentially an abstract class, the reason it is not
     * called an abstract class is to avoid confusion with a major usage of the
     * "Abstract" which is used for partial implementations of interfaces.</p>
     *
     * @param baseClass The base class.
     * @return A base class descriptor.
     */
    BaseClassDescriptor addBaseClassDescriptor(Class baseClass);

    /**
     * Add a class descriptor for the opaque class.
     *
     * @param opaqueClass The opaque class.
     * @return An opaque class descriptor.
     */
    OpaqueClassDescriptor addOpaqueClassDescriptor(Class opaqueClass);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/8	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
