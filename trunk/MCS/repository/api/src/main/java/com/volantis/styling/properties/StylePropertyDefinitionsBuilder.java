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

import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.values.FixedInitialValue;
import com.volantis.styling.values.PropertyInitialValue;
import com.volantis.styling.values.ComputedInitialValue;
import com.volantis.styling.values.InitialValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Builds a {@link StylePropertyDefinitions} object.
 */
public class StylePropertyDefinitionsBuilder {


    /**
     * A list of {@link StylePropertyBuilder}s, in the order they were added.
     */
    private List propertyBuilderList;

    /**
     * A map of builders keyed by property name.
     */
    private Map name2PropertyBuilder;

    /**
     * Initialise.
     */
    public StylePropertyDefinitionsBuilder() {
        propertyBuilderList = new ArrayList();
        name2PropertyBuilder = new HashMap();
    }

    /**
     * Add the builder to the set of builders.
     *
     * <p>The property's index will be set to its position within the set of
     * properties.</p>
     *
     * @param builder The builder to add.
     */
    public void addBuilder(StylePropertyBuilder builder) {
        propertyBuilderList.add(builder);
        name2PropertyBuilder.put(builder.getName(), builder);
    }

    /**
     * Return the newly constructed {@link StylePropertyDefinitions} object.
     *
     * @return The newly constructed {@link StylePropertyDefinitions} object.
     */
    public StylePropertyDefinitions getDefinitions() {

        List propertyList = new ArrayList();
        Map propertyMap = new HashMap();

        // Iterate over the builders resolving them to properties.
        for (Iterator i = propertyBuilderList.iterator(); i.hasNext();) {
            StylePropertyBuilder builder = (StylePropertyBuilder) i.next();
            resolveProperty(builder, propertyList, propertyMap);
        }

        return new StylePropertyDefinitionsImpl(propertyList);
    }

    /**
     * Resolve the specified builder to a property.
     *
     * <p>The main purpose of this method is to reorder the properties so that
     * properties always come before any properties that depend on them. i.e.
     * property <code>color</code> is moved before
     * <code>border-top-color</code> as the latter's initial value is the
     * same as the former's computed value.</p>
     *
     * @param builder The builder to resolve to a property.
     * @param propertyList The list of previously resolved properties.
     * @param propertyMap A map of previously resolved properties keyed by name.
     *
     * @return The resolved property.
     */
    private StyleProperty resolveProperty(
            StylePropertyBuilder builder, List propertyList, Map propertyMap) {

        String name = builder.getName();
        StylePropertyImpl property = (StylePropertyImpl) propertyMap.get(name);
        if (property != null) {
            return property;
        }

        // Make sure that any properties upon which this is dependent have
        // been resolved before this one.
        Map rules = builder.getInitialValueRules();
        String propertyReference = builder.getPropertyInitialValue();
        StyleValue fixedValue = builder.getFixedInitialValue();

        if (propertyReference != null) {
            StylePropertyBuilder initialValueBuilder
                    = (StylePropertyBuilder) name2PropertyBuilder.get(propertyReference);
            if (initialValueBuilder == null) {
                throw new IllegalStateException(
                        "Property '" + propertyReference +
                        "' could not be found");
            }

            StyleProperty initialValueProperty = resolveProperty(
                    initialValueBuilder, propertyList, propertyMap);

            InitialValueSource initialValueSource;

            // Do we have any translation rules or do we just copy the property
            // value
            if (rules != null) {
                initialValueSource =
                    new ComputedInitialValue(initialValueProperty, rules);
            } else {
                initialValueSource =
                    new PropertyInitialValue(initialValueProperty);
            }

            builder.setInitialValueSource(initialValueSource);

        } else if (fixedValue != null) {
            // Set the fixed initial value in the builder so that it will be
            // added to the property. In future, this will not be necessary as
            // nothing should be trying to get the fixed initial value from
            // the property.
            builder.setInitialValueSource(new FixedInitialValue(fixedValue));
        }

        // Initialise the index.
        int index = propertyList.size();

        // Create the property.
        PropertyDetailsBuilder detailsBuilder = builder.getDetailsBuilder();
        property = new StylePropertyImpl(name, index, detailsBuilder);

        // Store the property away.
        propertyList.add(property);
        propertyMap.put(name, property);

        return property;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
