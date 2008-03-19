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
import com.volantis.mcs.themes.ThemeStructureDefinitionFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.mcs.themes.types.StyleType;
import com.volantis.styling.values.FixedInitialValue;
import com.volantis.styling.values.InitialValueSource;

import java.util.Map;
import java.util.Set;

/**
 * A {@link StyleProperty}.
 */
public class PropertyDetailsBuilder {

    private static final Map THEME_STRUCTURE_DEFINITION_MAP =
        ThemeStructureDefinitionFactory.getDefaultInstance().
            getThemeStructureDefinitionMap();

    /**
     * The name of the property
     */
    private StyleProperty property;

    /**
     * Whether this property is inherited by default
     */
    private boolean inherited;

    /**
     * The source of the initial value.
     */
    private InitialValueSource initialValueSource;

    private AllowableKeywords allowableKeywords;

    private Set supportedTypes;

    private StyleProperty propertyInitialValue;
    private StyleValue fixedInitialValue;
    private InitialValueAccuracy initialValueAccuracy;
    private PropertyValue initialValue;

    /**
     * Initialise.
     *
     * <p>This constructor is used when building the standard details as part
     * of building the property enumeration.</p>
     */
    public PropertyDetailsBuilder() {
    }

    /**
     * Initialise.
     *
     * <p>Unless overidden explicitly the resulting details will have exactly
     * the same characteristics as the standard details accessed from the
     * property.</p>
     *
     * @param property The property on which this is based.
     */
    public PropertyDetailsBuilder(StyleProperty property) {
        this.property = property;

        PropertyDetails standardDetails = property.getStandardDetails();
        inherited = standardDetails.isInherited();
        initialValueSource = standardDetails.getInitialValueSource();
        allowableKeywords = standardDetails.getAllowableKeywords();
        supportedTypes = standardDetails.getSupportedTypes();
    }

    public void setProperty(StyleProperty property) {
        this.property = property;
    }

    public StyleProperty getProperty() {
        return property;
    }

    // Javadoc inherited.
    public final PropertyValue getInitialValue() {
        if (initialValue == null) {
            if (initialValueSource instanceof FixedInitialValue) {
                FixedInitialValue fixed = (FixedInitialValue) initialValueSource;
                initialValue =
                    ThemeFactory.getDefaultInstance().createPropertyValue(
                        property, fixed.getValue());
            } else {
                initialValue = null;
            }
        }

        return initialValue;
    }

    // Javadoc inherited.
    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    // Javadoc inherited.
    public final boolean isInherited() {
        return inherited;
    }

    // Javadoc inherited.
    public void setSupportedTypes(Set types) {
        this.supportedTypes = types;
    }

    // Javadoc inherited.
    public final Set getSupportedTypes() {
        return supportedTypes;
    }

    public void setInitialValueSource(InitialValueSource source) {
        this.initialValueSource = source;

        if (initialValueSource == null) {
            initialValueAccuracy = InitialValueAccuracy.UNKNOWN;
        } else {
            initialValueAccuracy = InitialValueAccuracy.KNOWN;
        }
    }

    public InitialValueSource getInitialValueSource() {
        return initialValueSource;
    }

    public StyleType getSupportedStructure() {
        return (StyleType) THEME_STRUCTURE_DEFINITION_MAP.get(property.getName());
    }

    public void setAllowableKeywords(AllowableKeywords allowableKeywords) {
        this.allowableKeywords = allowableKeywords;
    }

    public AllowableKeywords getAllowableKeywords() {
        return allowableKeywords;
    }

    public PropertyDetails getPropertyDetails() {
        return new PropertyDetailsImpl(this);
    }

    public StyleProperty getPropertyInitialValue() {
        return propertyInitialValue;
    }

    public void setPropertyInitialValue(StyleProperty propertyInitialValue) {
        this.propertyInitialValue = propertyInitialValue;
    }

    public StyleValue getFixedInitialValue() {
        return fixedInitialValue;
    }

    public void setFixedInitialValue(StyleValue fixedInitialValue) {
        this.fixedInitialValue = fixedInitialValue;
    }

    public InitialValueAccuracy getInitialValueAccuracy() {
        return initialValueAccuracy;
    }

    public void setInitialValueAccuracy(
            InitialValueAccuracy initialValueAccuracy) {
        this.initialValueAccuracy = initialValueAccuracy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 27-Oct-05	9965/1	ianw	VBM:2005101811 interim commit

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
