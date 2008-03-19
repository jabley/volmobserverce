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

import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.values.InitialValueSource;

import java.util.Set;

public class PropertyDetailsImpl
        implements PropertyDetails {

    /**
     * The name of the property
     */
    private final StyleProperty property;

    /**
     * The initial value for this property
     */
    private final PropertyValue initialValue;

    /**
     * Whether this property is inherited by default
     */
    private final boolean inherited;

    /**
     * The source of the initial value.
     */
    private final InitialValueSource initialValueSource;

    private final AllowableKeywords allowableKeywords;

    private final Set supportedTypes;

    private final StyleType supportedStructure;

    private InitialValueAccuracy initialValueAccuracy;

    public PropertyDetailsImpl(PropertyDetailsBuilder builder) {
        this.property = builder.getProperty();
        this.initialValue = builder.getInitialValue();
        this.inherited = builder.isInherited();
        this.initialValueSource = builder.getInitialValueSource();
        this.allowableKeywords = builder.getAllowableKeywords();
        this.supportedTypes = builder.getSupportedTypes();
        this.supportedStructure = builder.getSupportedStructure();
        this.initialValueAccuracy = builder.getInitialValueAccuracy();
    }

    public StyleProperty getProperty() {
        return property;
    }

    public PropertyValue getInitialPropertyValue() {
        return initialValue;
    }

    public StyleValue getInitialValue() {
        return initialValue == null ? null : initialValue.getValue();
    }

    public boolean isInherited() {
        return inherited;
    }

    public boolean isComputable() {
        return initialValueSource != null && initialValueSource.isComputed();
    }

    public InitialValueSource getInitialValueSource() {
        return initialValueSource;
    }

    public AllowableKeywords getAllowableKeywords() {
        return allowableKeywords;
    }

    public Set getSupportedTypes() {
        return supportedTypes;
    }

    public StyleType getSupportedStructure() {
        return supportedStructure;
    }

    public InitialValueAccuracy getInitialValueAccuracy() {
        return initialValueAccuracy;
    }
}
