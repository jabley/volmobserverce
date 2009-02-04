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

import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;

import java.util.List;

public class ProxyWalker
        implements ProxyVisitor {

    private final ProxyVisitor visitor;

    public ProxyWalker(ProxyVisitor visitor) {
        this.visitor = visitor;
    }

    public void walk(InternalProxy proxy) {
        // Visit the proxy first.
        proxy.accept(visitor);

        // Visit the children.
        proxy.accept(this);
    }

    public void visit(BeanProxy proxy) {
        BeanClassDescriptor descriptor = proxy.getBeanClassDescriptor();
        List properties = descriptor.getPropertyDescriptors();
        for (int i = 0; i < properties.size(); i++) {
            PropertyDescriptor property = (PropertyDescriptor)
                    properties.get(i);
            InternalProxy child = (InternalProxy) proxy.getPropertyProxy(
                    property.getIdentifier());
            if (child != null) {
                walk(child);
            }
        }
    }

    public void visit(ListProxy proxy) {
        int size = proxy.size();
        for (int i = 0; i < size; i += 1) {
            InternalProxy child = (InternalProxy) proxy.getItemProxy(i);
            walk(child);
        }
    }

    public void visit(OpaqueProxy proxy) {
        // Nothing to do.
    }

    public void visit(BaseProxy proxy) {
        InternalProxy child = (InternalProxy) proxy.getConcreteProxy();
        if (child != null) {
            walk(child);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
