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
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.BuiltinClasses;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.path.Step;

import java.util.List;

/**
 * An interaction item that represents a field in a model object that contains
 * a simple value, i.e. String, int etc.
 */
public class OpaqueProxyImpl
        extends AbstractProxy
        implements OpaqueProxy {

    private final OpaqueClassDescriptor descriptor;
    private final Class typeClass;

    public OpaqueProxyImpl(
            InteractionModel interactionModel,
            OpaqueClassDescriptor descriptor) {
        super(interactionModel);
        this.descriptor = descriptor;
        typeClass = descriptor.getTypeClass();
    }

    protected boolean isCompatible(Object object, Class typeClass) {
        if (typeClass.isPrimitive()) {
            typeClass = BuiltinClasses.primitive2AutoBoxType(typeClass);
        }

        return typeClass.isInstance(object);
    }

    protected void ensureModelObjectCompatability(Object newModelObject) {
        if (newModelObject == null) {
            // Null is compatible with everything.
        } else if (newModelObject instanceof String && typeClass != String.class) {
            // Assume that it can be converted.
        } else if (!isCompatible(newModelObject, typeClass)) {
            throw new IllegalArgumentException("Incompatible model object " +
                    newModelObject +
                    " not instance of " +
                    typeClass);
        }
    }

    public Proxy traverse(Step step, boolean enclosing, List proxies) {
        if (enclosing) {
            if (proxies != null) {
                proxies.add(this);
            }

            return this;
        }

        return null;
    }

    protected Object createModelObject() {
        throw new IllegalStateException("Cannot create opaque model object");
    }

    protected Object copyModelObjectImpl() {
        if (descriptor.isImmutable()) {
            // The underlying model object can be shared as it is immutable.
            return getModelObject();
        } else {
            throw new IllegalStateException("Cannot copy mutable opaque model objects that do not" +
                    " implement Copyable");
        }
    }

    // Javadoc inherited
    public Object setModelObject(Object newModelObject,
                                 boolean force,
                                 boolean originator) {
        if (newModelObject instanceof String && typeClass != String.class) {
            String string = ((String) newModelObject).trim();
            if (typeClass == Boolean.class || typeClass == Boolean.TYPE) {
                newModelObject = Boolean.valueOf(string);
            } else if (typeClass == Integer.class || typeClass == Integer.TYPE) {
                newModelObject = Integer.valueOf(string);
            } else {
                throw new IllegalArgumentException("No conversion from " +
                        newModelObject +
                        " to " +
                        typeClass);
            }
        }
        return super.setModelObject(newModelObject, force, originator);
    }

    // Javadoc inherited
    protected void updateModelObject(Object modelObject,
                                     boolean force,
                                     boolean originator) {
    }

    public String getModelObjectAsString() {
        Object modelObject = getModelObject();
        if (modelObject == null) {
            modelObject = "";
        }
        return modelObject.toString();
    }

    public void accept(ProxyVisitor visitor) {
        visitor.visit(this);
    }

    public TypeDescriptor getTypeDescriptor() {
        return descriptor;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Dec-05	10512/1	pduffin	Quick commit for GUI fixes

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 09-Nov-05	10199/1	pduffin	VBM:2005110413 Committing moving of paths from interaction to model subsystem.

 31-Oct-05	9961/11	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/6	pduffin	VBM:2005101811 Added path support

 25-Oct-05	9961/4	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
