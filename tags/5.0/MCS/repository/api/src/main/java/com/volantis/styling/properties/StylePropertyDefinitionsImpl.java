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

package com.volantis.styling.properties;

import com.volantis.shared.iteration.IterationAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StylePropertyDefinitionsImpl
        implements StylePropertyDefinitions {

    /**
     * An array of properties indexed by property index.
     */
    private final StyleProperty[] indexedProperties;

    /**
     * A map of properties keyed by property name.
     */
    private final Map namedProperties;

    /**
     * The number of style properties.
     */
    private final int count;
    private final PropertyDetailsSet standardDetailsSet;

    public StylePropertyDefinitionsImpl(List indexedProperties) {

        count = indexedProperties.size();
        this.indexedProperties = new StyleProperty[count];
        indexedProperties.toArray(this.indexedProperties);

        List detailsList = new ArrayList();
        namedProperties = new HashMap();
        for (int i = 0; i < indexedProperties.size(); i++) {
            final StyleProperty property = (StyleProperty)
                    indexedProperties.get(i);
            PropertyDetails details = property.getStandardDetails();
            detailsList.add(details);

            namedProperties.put(property.getName(), property);
        }

        standardDetailsSet = new PropertyDetailsSetImpl(detailsList);
    }

    // Javadoc inherited.
    public int count() {
        return count;
    }

    // Javadoc inherited.
    public StyleProperty getStyleProperty(int index) {
        return indexedProperties[index];
    }

    // Javadoc inherited.
    public StyleProperty getStyleProperty(String name) {
        return (StyleProperty) namedProperties.get(name);
    }

    public PropertyDetailsSet getStandardDetailsSet() {
        return standardDetailsSet;
    }

    // Javadoc inherited.
    public IterationAction iterateStyleProperties(StylePropertyIteratee iteratee) {
        IterationAction action = IterationAction.CONTINUE;
        for (int i = 0; action == IterationAction.CONTINUE && i < count; i++) {
            StyleProperty property = this.indexedProperties[i];
            action = iteratee.next(property);
        }
        return action;
    }

    public Iterator stylePropertyIterator() {
        return new Iterator() {

            private int index = 0;

            public boolean hasNext() {
                return index < indexedProperties.length;
            }

            public Object next() {
                StyleProperty property = indexedProperties[index];
                index += 1;
                return property;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
