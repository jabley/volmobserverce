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

import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;

public class ModelDescriptorImpl
        implements ModelDescriptor {

    private final MostSpecificClassMap map;

    public ModelDescriptorImpl(MostSpecificClassMap map) {
        this.map = map;
    }

    public TypeDescriptor getTypeDescriptorStrict(Class modelClass) {
        TypeDescriptor descriptor = (TypeDescriptor) map.get(modelClass);
        if (descriptor == null) {
            throw new IllegalArgumentException("Unknown model " + modelClass);
        }

        return descriptor;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Fixed issue with mapping classes to type descriptors

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
