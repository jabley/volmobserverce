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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes.definitions.types.impl;

import com.volantis.mcs.build.themes.definitions.types.ChoiceType;
import com.volantis.mcs.build.themes.definitions.types.Type;
import com.volantis.mcs.build.themes.definitions.types.TypeVisitor;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a choice type, consisting of one or more possible
 * choices for a type.
 */
public class ChoiceTypeImpl implements ChoiceType {
    /**
     * A List of possible types for this choice.
     */
    private List types = new ArrayList();

    // Javadoc inherited
    public void addType(Type type) {
        types.add(type);
    }

    // Javadoc inherited
    public Iterator getTypeIterator() {
        return types.iterator();
    }

    // Javadoc inherited
    public void accept(TypeVisitor visitor, Object obj) {
        visitor.visitChoiceType(this, obj);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/1	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 ===========================================================================
*/
