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
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;

class InteractionModelImpl
        implements TypeDescriptorVisitor, InteractionModel {

    private final ModelDescriptor modelDescriptor;

    private Proxy proxy;

    public InteractionModelImpl(ModelDescriptor modelDescriptor) {
        this.modelDescriptor = modelDescriptor;
    }

    public ModelDescriptor getModelDescriptor() {
        return modelDescriptor;
    }

    public synchronized Proxy createProxyForModelObject(Object modelObject) {
        if (modelObject == null) {
            return null;
        }

        Class modelClass = modelObject.getClass();
        TypeDescriptor descriptor =
                modelDescriptor.getTypeDescriptorStrict(modelClass);
        Proxy proxy = createProxyForType(descriptor);
        proxy.setModelObject(modelObject);
        return proxy;
    }

    public synchronized Proxy createProxyForType(TypeDescriptor descriptor) {
        this.proxy = null;
        descriptor.accept(this);
        return this.proxy;
    }

    public TypeDescriptor getTypeDescriptorStrict(Class modelClass) {
        return modelDescriptor.getTypeDescriptorStrict(modelClass);
    }

    public void visit(BeanClassDescriptor descriptor) {
        proxy = new BeanProxyImpl(this, descriptor);
    }

    public void visit(ListClassDescriptor descriptor) {
        proxy = new ListProxyImpl(this, descriptor);
    }

    public void visit(BaseClassDescriptor descriptor) {
        proxy = new BaseProxyImpl(this, descriptor);
    }

    public void visit(OpaqueClassDescriptor descriptor) {
        proxy = new OpaqueProxyImpl(this, descriptor);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 ===========================================================================
*/
