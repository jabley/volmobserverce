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

package com.volantis.mcs.interaction.sample.ui;

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptor;
import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;

import javax.swing.JComponent;

public class ComponentCreator
        implements TypeDescriptorVisitor {

    private final GUI gui;
    private Proxy proxy;
    private JComponent component;

    public ComponentCreator(GUI gui) {
        this.gui = gui;
    }

    public JComponent create(Proxy proxy) {
        this.proxy = proxy;
        this.component = null;
        proxy.getTypeDescriptor().accept(this);
        return component;
    }

    public void visit(BeanClassDescriptor descriptor) {
        BeanProxy beanItemProxy = (BeanProxy) proxy;
        component = new BeanComponent(gui, beanItemProxy);
    }

    public void visit(ListClassDescriptor descriptor) {
        ListProxy listItemProxy = (ListProxy) proxy;
        component = new ListComponent(gui, listItemProxy);
    }

    public void visit(BaseClassDescriptor descriptor) {
    }

    public void visit(OpaqueClassDescriptor descriptor) {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/4	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 ===========================================================================
*/
