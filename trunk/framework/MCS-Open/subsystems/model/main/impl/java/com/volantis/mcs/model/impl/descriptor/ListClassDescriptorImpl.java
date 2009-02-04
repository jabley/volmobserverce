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

import com.volantis.mcs.model.descriptor.ClassDescriptor;
import com.volantis.mcs.model.descriptor.ListAccessor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;

import java.util.ArrayList;

public class ListClassDescriptorImpl
        extends AbstractClassDescriptor
        implements ListClassDescriptor {

    private final ClassDescriptor itemTypeDescriptor;
    private final ListAccessor listAccessor;

    public ListClassDescriptorImpl(
            Class type, ClassDescriptor itemTypeDescriptor,
            ListAccessor listAccessor) {
        super(type);

        this.itemTypeDescriptor = itemTypeDescriptor;
        this.listAccessor = listAccessor;
    }

    public ClassDescriptor getItemClassDescriptor() {
        return itemTypeDescriptor;
    }

    public ListAccessor getListAccessor() {
        return listAccessor;
    }

    public void accept(TypeDescriptorVisitor visitor) {
        visitor.visit(this);
    }

    public Object createModelObject() {
//        if (itemTypeDescriptor instanceof ModifiableClassDescriptor) {
//            ModifiableClassDescriptor descriptor = (ModifiableClassDescriptor)
//                    itemTypeDescriptor;
//            return descriptor.createModelObject();
//        }
//
//        throw new IllegalStateException("Cannot create model object");
        return new ArrayList();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
