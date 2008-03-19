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

import com.volantis.mcs.themes.DensePropertyValueArray;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.values.InitialValueFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * The implementation of {@link PropertyDetailsSet}.
 */
public class PropertyDetailsSetImpl
        implements PropertyDetailsSet {

    /**
     * The list of details for each property.
     *
     * <p>The details are in order such that the details for a property whose
     * initial value is dependent on another property comes after the details
     * for the other property. e.g. details for border-top-color come after
     * the details for color.</p>
     */
    private final List detailsList;

    /**
     * Map from {@link StyleProperty} to {@link PropertyDetails}.
     */
    private final Map property2Details;

    /**
     * The root properties.
     */
    private final MutableStyleProperties rootProperties;

    /**
     * The set of supported properties.
     */
    private final ImmutableStylePropertySet supportedProperties;

    /**
     * Initialise.
     *
     * @param detailsList See {@link #detailsList}.
     */
    public PropertyDetailsSetImpl(List detailsList) {
        this.detailsList = detailsList;
        rootProperties = ThemeFactory.getDefaultInstance().
            createMutableStyleProperties(new DensePropertyValueArray());
        property2Details = new HashMap();
        InitialValueFinder finder = new InitialValueFinder();
        MutableStylePropertySet supportedProperties =
                new MutableStylePropertySetImpl();
        for (int i = 0; i < detailsList.size(); i++) {
            PropertyDetails details = (PropertyDetails) detailsList.get(i);
            final StyleProperty property = details.getProperty();

            property2Details.put(property, details);

            StyleValue initial = finder.getInitialValue(rootProperties,
                    details);
            rootProperties.setStyleValue(property, initial);

            supportedProperties.add(property);
        }

        this.supportedProperties =
                supportedProperties.createImmutableStylePropertySet();
    }

    // Javadoc inherited.
    public PropertyDetails getPropertyDetails(StyleProperty property) {
        return (PropertyDetails) property2Details.get(property);
    }

    // Javadoc inherited.
    public IterationAction iterateStyleProperties(
            StylePropertyIteratee iteratee) {

        IterationAction action = IterationAction.CONTINUE;
        for (int i = 0;
             action == IterationAction.CONTINUE && i < detailsList.size();
             i++) {
            PropertyDetails details = (PropertyDetails) detailsList.get(i);
            action = iteratee.next(details.getProperty());
        }
        return action;
    }

    // Javadoc inherited.
    public Iterator stylePropertyIterator() {
        return new Iterator() {

            private int index = 0;

            public boolean hasNext() {
                return index < detailsList.size();
            }

            public Object next() {
                PropertyDetails details = (PropertyDetails) detailsList.get(index);
                StyleProperty property = details.getProperty();
                index += 1;
                return property;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    // Javadoc inherited.
    public StyleValues getRootStyleValues() {
        return rootProperties;
    }

    // Javadoc inherited.
    public ImmutableStylePropertySet getSupportedProperties() {
        return supportedProperties;
    }

    // Javadoc inherited.
    public StyleProperty[] getOrderedPropertyArray(
            StylePropertySet properties) {

        List supportedProperties = new ArrayList();
        // Iterate in reverse order.
        for (ListIterator i = detailsList.listIterator(detailsList.size());
             i.hasPrevious();) {

            PropertyDetails details = (PropertyDetails) i.previous();
            StyleProperty property = details.getProperty();
            if (properties.contains(property)) {
                supportedProperties.add(property);
            }
        }

        StyleProperty[] array = new StyleProperty[supportedProperties.size()];
        supportedProperties.toArray(array);

        return array;
    }
}
