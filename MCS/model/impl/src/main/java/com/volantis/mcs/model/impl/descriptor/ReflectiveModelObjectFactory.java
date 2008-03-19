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

import com.volantis.mcs.model.descriptor.ModelObjectFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectiveModelObjectFactory
        implements ModelObjectFactory {

    private final Constructor constructor;

    public ReflectiveModelObjectFactory(Class modelObjectClass) {
        try {
            constructor = modelObjectClass.getConstructor(null);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "No default constructor found for " + modelObjectClass);
        }
    }

    public Object createObject() {
        try {
            return constructor.newInstance(null);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
