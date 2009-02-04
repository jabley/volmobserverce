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
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.path.Step;

import java.util.List;

public class BaseProxyImpl
        extends AbstractParentProxy
        implements BaseProxy {

    private final BaseClassDescriptor descriptor;

    private InternalProxy concreteProxy;

    public BaseProxyImpl(
            InteractionModel interactionModel,
            BaseClassDescriptor descriptor) {
        super(interactionModel);
        this.descriptor = descriptor;
    }

    public void accept(ProxyVisitor visitor) {
        visitor.visit(this);
    }

    public BaseClassDescriptor getBaseClassDescriptor() {
        return descriptor;
    }

    public Proxy getConcreteProxy() {
        return concreteProxy;
    }

    public Object getEmbeddedModelObject(Proxy proxy, boolean required) {
        return null;
    }

    public Object setEmbeddedModelObject(
            Proxy proxy, Object newEmbeddedModelObject) {
        return null;
    }

    public TypeDescriptor getTypeDescriptor() {
        return descriptor;
    }

    protected void addStepToPath(PathBuilder builder, Proxy childProxy) {
        // Nothing to add.
    }

    public Proxy traverse(Step step, boolean enclosing, List proxies) {
        if (concreteProxy != null) {
            if (proxies != null) {
                proxies.add(concreteProxy);
            }
            return concreteProxy.traverse(step, enclosing, proxies);
        }

        return null;
    }

    protected Object createModelObject() {
        throw new IllegalStateException("Cannot create base model object");
    }

    protected Object copyModelObjectImpl() {
        return concreteProxy.copyModelObject();
    }

    // Javadoc inherited
    protected void updateModelObject(Object modelObject,
                                     boolean force,
                                     boolean originator) {
        concreteProxy = (InternalProxy)
                interactionModel.createProxyForModelObject(modelObject);
        if (concreteProxy != null) {
            concreteProxy.attach(this);
        }
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

 31-Oct-05	9961/6	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
