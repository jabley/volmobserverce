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

import com.volantis.mcs.model.descriptor.OpaqueClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;
import com.volantis.mcs.model.descriptor.BuiltinClasses;

public class OpaqueClassDescriptorImpl
        extends AbstractTypeDescriptor
        implements OpaqueClassDescriptor {

    /**
     * Indicates that the underlying object is immutable.
     *
     * <p>This applies to all primitive and autobox classes.</p>
     */
    private final boolean immutable;

    public OpaqueClassDescriptorImpl(Class type) {
        super(type);

        immutable = BuiltinClasses.isPrimitiveOrAutoBoxingClass(type);
    }

    // Javadoc inherited.
    public boolean isImmutable() {
        return immutable;
    }

    public void accept(TypeDescriptorVisitor visitor) {
        visitor.visit(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
