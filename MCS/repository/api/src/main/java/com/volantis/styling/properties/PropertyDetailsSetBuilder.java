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
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.styling.values.PropertyInitialValue;
import com.volantis.styling.values.FixedInitialValue;
import com.volantis.styling.values.InitialValueSource;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class PropertyDetailsSetBuilder {

    private final List builders;
    private final Map property2Builder;

    public PropertyDetailsSetBuilder() {
        builders = new ArrayList();
        property2Builder = new HashMap();
    }

    public void addBuilder(PropertyDetailsBuilder builder) {
        builders.add(builder);
        property2Builder.put(builder.getProperty(), builder);
    }

    public PropertyDetailsSet getDetailsSet() {

        List detailsList = new ArrayList();
        Map detailsMap = new HashMap();
        for (int i = 0; i < builders.size(); i++) {
            PropertyDetailsBuilder builder = (PropertyDetailsBuilder)
                    builders.get(i);

            resolveDetails(builder,  detailsList, detailsMap);
        }

        return new PropertyDetailsSetImpl(detailsList);
    }

    private void resolveDetails(
            PropertyDetailsBuilder builder, List detailsList, Map detailsMap) {

        StyleProperty property = builder.getProperty();
        PropertyDetails details = (PropertyDetails) detailsMap.get(property);
        if (details != null) {
            // The details has already been built so there is nothing to do.
            return;
        }


        // Make sure that any properties upon which this is dependent have
        // been resolved before this one.
        InitialValueSource source = builder.getInitialValueSource();
        if (source instanceof PropertyInitialValue) {
            PropertyInitialValue propertySource = (PropertyInitialValue) source;
            StyleProperty sourceProperty = propertySource.getProperty();

            PropertyDetailsBuilder sourceBuilder = (PropertyDetailsBuilder)
                    property2Builder.get(sourceProperty);
            if (sourceBuilder == null) {
                throw new IllegalStateException(
                        "Property '" + sourceProperty + "' could not be found");
            }

            // Resolve the dependent details first to ensure that they appear
            // before this one.
            resolveDetails(sourceBuilder, detailsList, detailsMap);
        }

        // Create the details.
        details = builder.getPropertyDetails();

        // Store the details away.
        detailsList.add(details);
        detailsMap.put(property, details);
    }
}
